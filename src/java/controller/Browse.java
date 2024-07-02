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
        List<Genre> genreList = GenreProvider.getGenreList();
        List<String> genreNames = GenreProvider.getGenreList().stream()
                .map(Genre::getGenre)
                .map(genre -> "'" + genre + "'")
                .collect(Collectors.toList());

        url = switch (action) {
            case "home" -> {
                listBookDefault(request, response, 8);
                request.setAttribute("book_genre_list", genreList);
                yield "views/HomePage.jsp";
            }
            case "productList" -> {
                listBookWithFilter(request, response, 6);
                request.setAttribute("book_genre_list", genreList);
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

            case "test" ->
                Test(request, response);

            default ->
                throw new AssertionError();
        }
    }

    private void listBookDefault(HttpServletRequest request, HttpServletResponse response, int size) throws ServletException, IOException {
        int page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

        List<Book> bookList = bd.getBookList();
        int totalItems = bookList.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
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

    private void listBookWithFilter(HttpServletRequest request, HttpServletResponse response, int size) throws ServletException, IOException {
        int page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
        String ge = request.getParameter("genre") == null ? "all" : request.getParameter("genre");
        HttpSession session = request.getSession();
        clearMessages();
        List<String> genreList;
        if (session.getAttribute("genreListSession") == null) {
            genreList = new ArrayList<>();
            session.setAttribute("genreListSession", genreList);
        } else {
            genreList = (List<String>) session.getAttribute("genreListSession");
            if (genreList.size() >= 4) {
                warningMessages.add("A maximum of 4 genres can be selected.");
            } else {
                if (!ge.equals("all") && !genreList.contains(ge)) {
                    genreList.add(ge);
                }

            }
            session.setAttribute("genreListSession", genreList);
        }

        List<Book> bookList;
        if (genreList.isEmpty()) {
            bookList = bd.getBookList();
        } else {
            bookList = bd.getBookListWithGenreFilter(genreList);
        }
        
        int totalItems = bookList.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<Book> paginatedBookList = bookList.subList(fromIndex, toIndex);
        addMessages(request);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("bookList", paginatedBookList);
    }

    private void Test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        clearMessages();
        successMessages.add("Loggin success.");
        request.getRequestDispatcher("/views/HomePage.jsp").forward(request, response);
    }
}
