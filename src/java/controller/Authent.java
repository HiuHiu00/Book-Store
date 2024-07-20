package controller;

import EmailSender.EmailSender;
import dao.CustomerDAO;
import EmailSender.CountdownInfo;
import dao.AdminDAO;
import dao.BrowseDAO;
import dao.CartAndOrderDAO;
import entity.Account;
import entity.Book;
import entity.Cart;
import entity.Genre;
import entity.GenreProvider;
import jakarta.servlet.ServletContext;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebServlet(name = "Authent", urlPatterns = {"/authent"})
public class Authent extends HttpServlet {

    // Instances for various operations
    CustomerDAO cd = new CustomerDAO();
    AdminDAO ad = new AdminDAO();
    EmailSender es = new EmailSender();
    BrowseDAO bd = new BrowseDAO();
    CartAndOrderDAO caod = new CartAndOrderDAO();
    // Lists to store different types of messages
    List<String> successMessages = new ArrayList<>();
    List<String> infoMessages = new ArrayList<>();
    List<String> warningMessages = new ArrayList<>();
    List<String> errorMessages = new ArrayList<>();
    // A constant for serialization purposes
    private static final long serialVersionUID = 1L;
    // A map to store countdown information associated with email addresses
    private Map<String, CountdownInfo> emailToCountdownMap = new HashMap<>();

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
        String action = request.getParameter("action") == null ? "login" : request.getParameter("action");
        url = switch (action) {
            case "login" ->
                "views/Login.jsp";
            case "register" ->
                "views/Register.jsp";
            case "forgotPassword" ->
                "views/ForgotPassword.jsp";
            case "adminLogin" ->
                "views/AdminLogin.jsp";
            case "logout" -> {
                logout(request, response);
                yield "views/Login.jsp";
            }
            default ->
                "index.html";
        };
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Handles the HTTP <code>Post</code> method.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "login" : request.getParameter("action");
        switch (action) {
            case "login" ->
                login(request, response);
            case "register" ->
                register(request, response);
            case "getNewPassword" ->
                getNewPassword(request, response);
            case "getOtpCode" ->
                sendOtp(request, response);
            case "adminLogin" ->
                adminLogin(request, response);
            default ->
                throw new AssertionError();
        }
    }

    /**
     * Handles the <code>login</code> process.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("pass");

        boolean emailExists = cd.checkAccountExistsByEmail(email);
        String passwordCheck = cd.getPasswordByEmail(email);

        clearMessages();

        if (emailExists) {
            if (password == null ? passwordCheck != null : !password.equals(passwordCheck)) {
                request.setAttribute("email", email);

                errorMessages.add("Your email or password are not correct!");
                addMessages(request);

                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            } else {
                HttpSession session = request.getSession();
                Account me = cd.getPublicAccountInfobyEmail(email);
                session.setAttribute("accountLoggedIn", me);
                session.setAttribute("isLoggedIn", true);
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

                listBookDefault(request, 8);
                List<Genre> genreList = GenreProvider.getGenreList();
                request.setAttribute("book_genre_list", genreList);

                successMessages.add("Loggin success.");
                addMessages(request);
                request.getRequestDispatcher("/views/HomePage.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("email", email);

            errorMessages.add("Your email is incorrect or not register yet!");
            addMessages(request);

            request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
        }

    }

    /**
     * Handles the <code>login</code> process.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void adminLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        String rKey = request.getParameter("rkey");

        boolean emailExists = ad.checkAccountExistsByEmail(email);
        String passwordCheck = ad.getPasswordByEmail(email);
        String rKeyCheck = ad.getRoleKeyByEmail(email);
        int accountRole = ad.getAccountRoleIDByEmail(email);
        clearMessages();

        if (emailExists && accountRole > 0 && accountRole != 1) {
            if (password == null ? passwordCheck != null : !password.equals(passwordCheck)) {
                request.setAttribute("email", email);

                errorMessages.add("Your email or password are not correct!");
                addMessages(request);

                request.getRequestDispatcher("/views/AdminLogin.jsp").forward(request, response);
            } else {
                if (rKey == null ? rKeyCheck != null : !rKey.equals(rKeyCheck)) {
                    request.setAttribute("email", email);
                    request.setAttribute("password", password);

                    errorMessages.add("Incorrect Login Key for admin/staff");
                    addMessages(request);

                    request.getRequestDispatcher("/views/AdminLogin.jsp").forward(request, response);
                } else {
                    if (accountRole == 2) {
                        successMessages.add("Loggin as Admin success.");
                    }
                    if (accountRole == 3) {
                        successMessages.add("Loggin as Staff success.");
                    }
                    addMessages(request);

                    request.getRequestDispatcher("/views/AdminLogin.jsp").forward(request, response);
                }
            }
        } else {
            request.setAttribute("email", email);

            errorMessages.add("Your email is incorrect or not have permission to login as Admin/Staff!");
            addMessages(request);

            request.getRequestDispatcher("/views/AdminLogin.jsp").forward(request, response);
        }

    }

    /**
     * Handles the <code>registration</code> process.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("passw");
        String cpassword = request.getParameter("confirmPass");

        clearMessages();

        boolean emailExists = cd.checkAccountExistsByEmail(email);

        if (emailExists) {
            request.setAttribute("email", email);
            request.setAttribute("password", password);

            clearMessages();
            errorMessages.add("Your email already registered!");
            addMessages(request);

            request.getRequestDispatcher("/views/Register.jsp").forward(request, response);
        } else {
            if (password.equals(cpassword)) {
                cd.addAccount(email, password);

                successMessages.add("Registered successfully!");
                addMessages(request);

                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            } else {
                request.setAttribute("email", email);
                request.setAttribute("passw", password);

                errorMessages.add("Confirm password and password not matched!");
                addMessages(request);

                request.getRequestDispatcher("/views/Register.jsp").forward(request, response);
            }
        }
    }

    /**
     * Handles the <code>logout</code> process.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("IsLogged");
            session.removeAttribute("accountLoggedIn");
            session.invalidate();
        }
        clearMessages();
        successMessages.add("Logout successfully!");
        addMessages(request);
    }

    /**
     * Sends an OTP to the user's email for password recovery.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void sendOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        boolean emailExists = cd.checkAccountExistsByEmail(email);

        clearMessages();

        if (emailExists) {
            CountdownInfo countdownInfo = emailToCountdownMap.get(email);
            if (countdownInfo != null && countdownInfo.isValid()) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = (currentTime - countdownInfo.getStartTime()) / 1000;
                int remainingTime = countdownInfo.getDuration() - (int) elapsedTime;

                if (remainingTime <= 0) {
                    remainingTime = 0;
                }

                warningMessages.add("You must wait for the previous otp code is turn to end before you can do it again");

                request.setAttribute("countdownDuration", remainingTime);
            } else {
                String otp = cd.generateRandomOTP(6);
                cd.addOTPForAccountByEmail(otp, email);

                ServletContext context = getServletContext();
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(() -> {
                    es.sendMsgEmail(context, email, "Your New OTP Code", "ocType", otp);
                }, 1, TimeUnit.SECONDS);

                int countdownDuration = 30;
                countdownInfo = new CountdownInfo(countdownDuration, System.currentTimeMillis());
                emailToCountdownMap.put(email, countdownInfo);

                request.setAttribute("countdownDuration", countdownDuration);

                successMessages.add("Your OTP Code was sent successfully to email: " + email);
            }

            request.setAttribute("email", email);

            addMessages(request);

            request.getRequestDispatcher("/views/ForgotPassword.jsp").forward(request, response);
        } else {
            request.setAttribute("email", email);

            errorMessages.add("This email does not registered yet or is incorrect!");
            addMessages(request);

            request.getRequestDispatcher("/views/ForgotPassword.jsp").forward(request, response);
        }
    }

    /**
     * Handles the process of setting a new password after OTP verification.
     *
     * @param request The HttpServletRequest.
     * @param response The HttpServletResponse.
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void getNewPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("hiddenEmail");
        String otp = request.getParameter("otpCode");

        boolean emailExists = cd.checkAccountExistsByEmail(email);

        clearMessages();

        if (emailExists) {
            String otpMatch = cd.getVerifyCodeByEmail(email);

            if (otp.equals(otpMatch) && otpMatch != null) {
                String newPassword = cd.generateRandomPassword(8);
                cd.addNewPasswordForAccountByEmail(newPassword, email);

                ServletContext context = getServletContext();
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(() -> {
                    es.sendMsgEmail(context, email, "Your New Password", "npType", newPassword);
                }, 1, TimeUnit.SECONDS);

                successMessages.add("Your password was sent successfully to email: " + email + otp);
                addMessages(request);

                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            } else {
                CountdownInfo countdownInfo = emailToCountdownMap.get(email);
                long currentTime = System.currentTimeMillis();
                long elapsedTime = (currentTime - countdownInfo.getStartTime()) / 1000;
                int remainingTime = countdownInfo.getDuration() - (int) elapsedTime;
                if (remainingTime <= 0) {
                    remainingTime = 0;
                }

                request.setAttribute("countdownDuration", remainingTime);

                errorMessages.add("Incorrect code not matching, please check it again!");
                addMessages(request);

                request.getRequestDispatcher("/views/ForgotPassword.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("email", email);

            errorMessages.add("This email does not registered yet or is incorrect!");
            addMessages(request);

            request.getRequestDispatcher("/views/ForgotPassword.jsp").forward(request, response);
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

}
