package controller;

import dao.BrowseDAO;
import dao.CartAndOrderDAO;
import entity.Account;
import entity.Author;
import entity.Book;
import entity.Cart;
import entity.Genre;
import entity.GenreProvider;
import entity.Publisher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "Browse", urlPatterns = {"/browse"})
public class Browse extends HttpServlet {

    BrowseDAO bd = new BrowseDAO();
    CartAndOrderDAO caod = new CartAndOrderDAO();
    // Lists to store different types of messages
    List<String> successMessages = new ArrayList<>();
    List<String> infoMessages = new ArrayList<>();
    List<String> warningMessages = new ArrayList<>();
    List<String> errorMessages = new ArrayList<>();

    /**
     * Clears all message lists.
     */
    private void clearMessages() {
        successMessages.clear();
        infoMessages.clear();
        warningMessages.clear();
        errorMessages.clear();
    }

    /**
     * Adds message lists as attributes to the request.
     *
     * @param request The HttpServletRequest.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void addMessages(HttpServletRequest request) throws ServletException, IOException {
        request.setAttribute("successMessages", successMessages);
        request.setAttribute("infoMessages", infoMessages);
        request.setAttribute("warningMessages", warningMessages);
        request.setAttribute("errorMessages", errorMessages);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        clearMessages();
        HttpSession session = request.getSession();
        Account me = (Account) session.getAttribute("accountLoggedIn");

        String url = "";
        String page = request.getParameter("action") == null ? "home" : request.getParameter("action");

        url = determinePageUrl(page, request, session);
        processCart(request, me);
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        clearMessages();
        HttpSession session = request.getSession();
        Account me = (Account) session.getAttribute("accountLoggedIn");

        String action = request.getParameter("action") == null ? "home" : request.getParameter("action");
        String currentPage = request.getParameter("currentPage");
        String url = "";
        switch (action) {
            case "addToCart" -> {
                Boolean isLogged = (Boolean) session.getAttribute("isLoggedIn");
                if (isLogged != null && isLogged.equals(true)) {
                    addToCart(request, me);
                    url = determinePageUrl(currentPage, request, session);
                } else {
                    warningMessages.add("You must login before you want to add to cart or buy a book!");
                    addMessages(request);
                    url = "views/Login.jsp";
                }
            }
            case "removeFromCart" -> {
                removeFromCart(request);
                url = determinePageUrl(currentPage, request, session);
            }
            default ->
                throw new AssertionError();
        }
        processCart(request, me);
        request.getRequestDispatcher(url).forward(request, response);

    }

    /**
     * Determines the URL to forward the request based on the current page. Sets
     * the necessary attributes in the request for each page type.
     *
     * @param page the current page identifier to determine the URL and set
     * relevant attributes.
     * @param request The HttpServletRequest.
     * @return the URL to forward the request to, based on the current page.
     */
    private String determinePageUrl(String page, HttpServletRequest request, HttpSession session) throws ServletException, IOException {
        if (!"bookList".equals(page)) {
            session.removeAttribute("genreSelected");
            session.removeAttribute("currentPriceRangeLevelSelected");
            session.removeAttribute("currentAuthorSelected");
            session.removeAttribute("currentPublisherSelected");
            session.removeAttribute("currentSortOptionSelected");
        }
        String url = switch (page) {
            case "home" -> {
                listBookDefault(request, 8);
                List<Genre> genreList = GenreProvider.getGenreList();
                request.setAttribute("book_genre_list", genreList);
                yield "views/HomePage.jsp";
            }
            case "bookList" -> {
                listBookWithFilter(request, 6);
                List<String> genreNames = GenreProvider.getGenreList().stream()
                        .map(Genre::getGenre)
                        .map(genre -> "'" + genre + "'")
                        .collect(Collectors.toList());
                request.setAttribute("book_genre_name_list", genreNames);
                yield "views/BookList.jsp";
            }
            case "bookDetail" -> {
                bookDetail(request, Integer.parseInt(request.getParameter("bookID")));
                yield "views/BookDetail.jsp";
            }
            default ->
                "index.html";
        };

        return url;
    }

    /**
     * Processes the cart for the given account and sets the necessary
     * attributes in the request.
     *
     * @param request The HttpServletRequest.
     * @param me The account for which the cart is to be processed. If null, the
     * cart is considered empty.
     */
    private void processCart(HttpServletRequest request, Account me) throws ServletException, IOException {
        if (me == null) {
            request.setAttribute("cartProductCount", 0);
        } else {
            request.setAttribute("cartProductCount", caod.getProductNumbersOfCartByAccountID(me.getAccountID()));
            List<Cart> cartList = caod.getCartListByAccountID(me.getAccountID());
            request.setAttribute("cartList", cartList);
            Double totalPrice = 0.0;
            Double totalPriceAfterDiscount = 0.0;
            for (Cart cart : cartList) {
                Double discountPercent;
                if (cart.getDiscount().getDiscountPercent() == null) {
                    discountPercent = 0.0;
                } else {
                    discountPercent = cart.getDiscount().getDiscountPercent();
                }
                Double currentPrice = cart.getBook().getPrice() * (100 - discountPercent) / 100;
                totalPrice = totalPrice + cart.getBook().getPrice();
                totalPriceAfterDiscount = totalPriceAfterDiscount + currentPrice;
            }
            request.setAttribute("totalPriceAfterDiscount", totalPriceAfterDiscount);
            request.setAttribute("totalPrice", totalPrice);
        }
    }

    /**
     * Lists books with default pagination and attributes.
     *
     * @param request The HttpServletRequest.
     * @param size The number of items per page.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void listBookDefault(HttpServletRequest request, int size) throws ServletException, IOException {
        int page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
        List<Book> bookList = bd.getBookList();

        int totalItems = bookList.size();
        int totalPages;

        if (totalItems != 0) {
            totalPages = (int) Math.ceil((double) totalItems / size);
        } else {
            totalPages = 1;
        }
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<Book> paginatedBookList = bookList.subList(fromIndex, toIndex);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("bookList", paginatedBookList);
    }

    /**
     * Lists books with filters applied based on user selections.
     *
     * @param request The HttpServletRequest.
     * @param size The number of items per page.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void listBookWithFilter(HttpServletRequest request, int size) throws ServletException, IOException {

        HttpSession session = request.getSession();
        //FilterSearchByGenre
        String addGenre = request.getParameter("addGenre");
        String removeGenre = request.getParameter("removeGenre");
        List<String> genreSelected = getGenresSelected(session, addGenre, removeGenre);
        //FilterSearchByPriceRange
        String selectedPriceRangeLevel = request.getParameter("selectedPriceRangeLevel");
        Map<String, Double> priceRange = getPriceRangeSelected(request, session, selectedPriceRangeLevel);
        Double minPrice = priceRange.get("minPrice");
        Double maxPrice = priceRange.get("maxPrice");
        //FilterSearchByAuthorName
        String selectedAuthorValue = request.getParameter("selectedAuthor");
        String authorName = getAuthorNameSelected(request, session, selectedAuthorValue);
        //FilterSearchByPublisherName
        String selectedPublisherValue = request.getParameter("selectedPublisher");
        String publisherName = getPublisherNameSelected(request, session, selectedPublisherValue);
        //FilterSearchBySortingOption
        String sortingOptionValue = request.getParameter("sortingOption");
        String sortingOption = getSortOption(request, session, sortingOptionValue);
        int page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

        List<Book> FilterSearchBookList = bd.getBookListWithFilterSearch(page, size, genreSelected, minPrice, maxPrice, authorName, publisherName, sortingOption);

        if (null == FilterSearchBookList || FilterSearchBookList.isEmpty()) {
            request.setAttribute("noBook", "There are no books matching your featured search!");
        }

        request.setAttribute("bookList", FilterSearchBookList);
        addMessages(request);
    }

    /**
     * Retrieves selected genres from session and updates them based on user
     * actions.
     *
     * @param session The HttpSession.
     * @param addGenre The genre to add.
     * @param removeGenre The genre to remove.
     * @return The updated list of selected genres.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private List<String> getGenresSelected(HttpSession session, String addGenre, String removeGenre) throws ServletException, IOException {
        List<String> genreSelected = (List<String>) session.getAttribute("genreSelected");
        if (genreSelected == null) {
            genreSelected = new ArrayList<>();
        }
        if (addGenre != null) {
            if (genreSelected.contains(addGenre)) {
                warningMessages.add("Duplicate genre!");
            } else if (genreSelected.size() >= 4) {
                warningMessages.add("Only 4 genres can be selected");
            } else {
                genreSelected.add(addGenre);
            }
            session.setAttribute("genreSelected", genreSelected);
        }

        if (removeGenre != null && !removeGenre.isEmpty()) {
            genreSelected.remove(removeGenre);
        }
        return genreSelected;
    }

    /**
     * Retrieves the selected price range level and its corresponding price
     * range from session or request parameters.
     *
     * @param request The HttpServletRequest.
     * @param session The HttpSession.
     * @param selectedPriceRangeLevel The selected price range level.
     * @return A map containing the minimum and maximum prices.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private Map<String, Double> getPriceRangeSelected(HttpServletRequest request, HttpSession session, String selectedPriceRangeLevel) throws ServletException, IOException {
        Double minPrice = null;
        Double maxPrice = null;

        if (selectedPriceRangeLevel == null || selectedPriceRangeLevel.isEmpty()) {
            selectedPriceRangeLevel = (String) session.getAttribute("currentPriceRangeLevelSelected");
            if (selectedPriceRangeLevel == null) {
                selectedPriceRangeLevel = "Level0";
            }
        }

        switch (selectedPriceRangeLevel) {
            case "Level1" -> {
                minPrice = 0.0;
                maxPrice = 20.0;
                session.setAttribute("currentPriceRangeLevelSelected", "Level1");
            }
            case "Level2" -> {
                minPrice = 20.0;
                maxPrice = 50.0;
                session.setAttribute("currentPriceRangeLevelSelected", "Level2");
            }
            case "Level3" -> {
                minPrice = 50.0;
                maxPrice = 100.0;
                session.setAttribute("currentPriceRangeLevelSelected", "Level3");
            }
            case "Level4" -> {
                minPrice = 100.0;
                maxPrice = 999.0;
                session.setAttribute("currentPriceRangeLevelSelected", "Level4");
            }
            default -> {
                session.setAttribute("currentPriceRangeLevelSelected", "Level0");
            }
        }

        request.setAttribute("currentPriceRangeLevelSelected", selectedPriceRangeLevel);

        List<Map<String, String>> priceRangesLevel = List.of(
                Map.of("value", "Level1", "label", "0$ - 20$"),
                Map.of("value", "Level2", "label", "20$ - 50$"),
                Map.of("value", "Level3", "label", "50$ - 100$"),
                Map.of("value", "Level4", "label", ">100$")
        );
        request.setAttribute("priceRanges", priceRangesLevel);

        Map<String, Double> priceRange = new HashMap<>();
        priceRange.put("minPrice", minPrice);
        priceRange.put("maxPrice", maxPrice);

        return priceRange;
    }

    /**
     * Retrieves the selected author's name based on user selection.
     *
     * @param request The HttpServletRequest.
     * @param session The HttpSession.
     * @param selectedAuthorValue The selected author's value.
     * @return The selected author's name.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String getAuthorNameSelected(HttpServletRequest request, HttpSession session, String selectedAuthorValue) throws ServletException, IOException {
        String authorName = null;
        List<Author> authorList = bd.getAuthorList();
        request.setAttribute("authorList", authorList);
        if (selectedAuthorValue == null || selectedAuthorValue.isEmpty()) {
            selectedAuthorValue = (String) session.getAttribute("currentAuthorSelected");
            if (selectedAuthorValue == null) {
                selectedAuthorValue = "authorAll";
            }
        }
        if ("authorAll".equals(selectedAuthorValue)) {
            session.setAttribute("currentAuthorSelected", "authorAll");
        } else {
            for (Author author : authorList) {
                if (selectedAuthorValue.equals(author.getAuthorName())) {
                    session.setAttribute("currentAuthorSelected", author.getAuthorName());
                    authorName = author.getAuthorName();
                    break;
                }
            }
        }
        return authorName;
    }

    /**
     * Retrieves the selected publisher's name based on user selection.
     *
     * @param request The HttpServletRequest.
     * @param session The HttpSession.
     * @param selectedPublisherValue The selected publisher's value.
     * @return The selected publisher's name.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String getPublisherNameSelected(HttpServletRequest request, HttpSession session, String selectedPublisherValue) throws ServletException, IOException {
        String publisherName = null;
        List<Publisher> publisherList = bd.getPublisherList();
        request.setAttribute("publisherList", publisherList);
        if (selectedPublisherValue == null || selectedPublisherValue.isEmpty()) {
            selectedPublisherValue = (String) session.getAttribute("currentPublisherSelected");
            if (selectedPublisherValue == null) {
                selectedPublisherValue = "publisherAll";
            }
        }
        if ("publisherAll".equals(selectedPublisherValue)) {
            session.setAttribute("currentPublisherSelected", "publisherAll");
        } else {
            for (Publisher publisher : publisherList) {
                if (selectedPublisherValue.equals(publisher.getPublisherName())) {
                    session.setAttribute("currentPublisherSelected", publisher.getPublisherName());
                    publisherName = publisher.getPublisherName();
                    break;
                }
            }
        }
        return publisherName;
    }

    /**
     * Retrieves the selected sorting option from session or request parameters.
     *
     * @param request The HttpServletRequest.
     * @param session The HttpSession.
     * @param sortingOptionValue The selected sorting option.
     * @return The selected sorting option.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private String getSortOption(HttpServletRequest request, HttpSession session, String sortingOptionValue) throws ServletException, IOException {
        if (sortingOptionValue == null || sortingOptionValue.isEmpty()) {
            sortingOptionValue = (String) session.getAttribute("currentSortOptionSelected");
            if (sortingOptionValue == null) {
                sortingOptionValue = "Nothing";
            }
        }

        switch (sortingOptionValue) {
            case "AtoZ", "ZtoA", "IncrementPrice", "DecrementPrice" ->
                session.setAttribute("currentSortOptionSelected", sortingOptionValue);
            default -> {
                session.setAttribute("currentSortOptionSelected", "Nothing");
                sortingOptionValue = "Nothing";
            }
        }

        request.setAttribute("currentSortOptionSelected", sortingOptionValue);

        return sortingOptionValue;
    }

    /**
     *
     * @param request
     * @param bookId
     * @throws ServletException
     * @throws IOException
     */
    private void bookDetail(HttpServletRequest request, int bookId) throws ServletException, IOException {
        Book bookDetail = bd.getBookDetailByID(bookId);
        request.setAttribute("bookDetail", bookDetail);

        List<Genre> genres = bd.getBookGenresByID(bookId);
        request.setAttribute("genres", genres);
    }

    /**
     *
     * @param request
     * @param me
     * @throws ServletException
     * @throws IOException
     */
    private void addToCart(HttpServletRequest request, Account me) throws ServletException, IOException {
        String quantity = request.getParameter("bookQuantity") == null ? "1" : request.getParameter("bookQuantity");
        int bookID = Integer.parseInt(request.getParameter("bookID"));
        String bookName = request.getParameter("bookTitle");
        int cartId = caod.getCartIdByAccountID(me.getAccountID());

        if (caod.isBookExistedInCart(bookID, cartId)) {
            caod.increaseBookQuantityByOne(bookID, cartId,  Integer.parseInt(quantity)); 
            successMessages.add("Increment \"" + bookName + "\" quantity in your cart by "+ quantity +" succesfully.");
        } else {
            caod.addBookToCartByBookID(bookID, Integer.parseInt(quantity), cartId);
            successMessages.add("Add \"" + bookName + "\" to your cart succesfully.");
        }

        addMessages(request);
    }

    /**
     *
     * @param request
     * @throws ServletException
     * @throws IOException
     */
    private void removeFromCart(HttpServletRequest request) throws ServletException, IOException {
        int bookID = Integer.parseInt(request.getParameter("bookIDToRemove"));
        String bookName = request.getParameter("bookTitleToRemove");

        caod.removeBookFromCartByBookID(bookID);
        successMessages.add("Remove \"" + bookName + "\" from your cart succesfully. ");

        addMessages(request);
    }
}
