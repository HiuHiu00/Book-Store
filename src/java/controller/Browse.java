package controller;

import dao.BrowseDAO;
import entity.Book;
import entity.Genre;
import entity.GenreProvider;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "Browse", urlPatterns = {"/browse"})
public class Browse extends HttpServlet {

    BrowseDAO bd = new BrowseDAO();
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
        String url = "";
        String action = request.getParameter("action") == null ? "home" : request.getParameter("action");

        url = switch (action) {
            case "home" -> {
                listBookDefault(request, 8);
                List<Genre> genreList = GenreProvider.getGenreList();
                request.setAttribute("book_genre_list", genreList);
                yield "views/HomePage.jsp";
            }
            case "productList" -> {
                listBookWithFilter(request, 6);
                List<String> genreNames = GenreProvider.getGenreList().stream()
                        .map(Genre::getGenre)
                        .map(genre -> "'" + genre + "'")
                        .collect(Collectors.toList());
                request.setAttribute("book_genre_name_list", genreNames);
                yield "views/ProductList.jsp";
            }
            default ->
                "index.html";
        };
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
        String action = request.getParameter("action") == null ? "home" : request.getParameter("action");
        switch (action) {
            case "filter" -> {

            }
            default ->
                throw new AssertionError();
        }
    }

    private void listBookDefault(HttpServletRequest request, int size) throws ServletException, IOException {
        int page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
        clearMessages();
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

    private void listBookWithFilter(HttpServletRequest request, int size) throws ServletException, IOException {
        clearMessages();
        HttpSession session = request.getSession();

        String genre = request.getParameter("addGenre");
        List<String> genreSelected = (List<String>) session.getAttribute("genreSelected");
        if (genreSelected == null) {
            genreSelected = new ArrayList<>();
        }
        if (genre != null) {
            if (genreSelected.contains(genre)) {
                warningMessages.add("Duplicate genre!");
            } else if (genreSelected.size() >= 4) {
                warningMessages.add("Only 4 genres can be selected");
            } else {
                genreSelected.add(genre);
            }
        }
        session.setAttribute("genreSelected", genreSelected);

        String selectedPriceValue = request.getParameter("selectedPrice");
        Double minPrice = null;
        Double maxPrice = null;

        if (selectedPriceValue == null) {
            if (session.getAttribute("currentPriceSelectedSession") != null) {
                selectedPriceValue = session.getAttribute("currentPriceSelectedSession").toString();
            } else {
                request.setAttribute("currentPriceSelected", 0);
                session.setAttribute("currentPriceSelectedSession", 0);
            }
        }

        if (selectedPriceValue != null) {
            switch (selectedPriceValue) {
                case "1" -> {
                    minPrice = 0.0;
                    maxPrice = 20.0;
                    request.setAttribute("currentPriceSelected", 1);
                    session.setAttribute("currentPriceSelectedSession", 1);
                }
                case "2" -> {
                    minPrice = 20.0;
                    maxPrice = 50.0;
                    request.setAttribute("currentPriceSelected", 2);
                    session.setAttribute("currentPriceSelectedSession", 2);
                }
                case "3" -> {
                    minPrice = 50.0;
                    maxPrice = 100.0;
                    request.setAttribute("currentPriceSelected", 3);
                    session.setAttribute("currentPriceSelectedSession", 3);
                }
                case "4" -> {
                    minPrice = 100.0;
                    maxPrice = 999.0;
                    request.setAttribute("currentPriceSelected", 4);
                    session.setAttribute("currentPriceSelectedSession", 4);
                }
                default -> {
                    minPrice = null;
                    maxPrice = null;
                    request.setAttribute("currentPriceSelected", 0);
                    session.setAttribute("currentPriceSelectedSession", 0);
                }
            }
        }
        
        int page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
        List<Book> FilterSearchBookList = bd.getBookListWithFilterSearch(page, size, genreSelected, minPrice, maxPrice, null, null);
        int totalPages = (int) Math.ceil((double) FilterSearchBookList.size() / size);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("bookList", FilterSearchBookList);
        addMessages(request);
    }

}
