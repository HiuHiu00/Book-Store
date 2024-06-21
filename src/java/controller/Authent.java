package controller;

import EmailSender.EmailSender;
import dao.CustomerDAO;
import EmailSender.CountdownInfo;
import entity.Account;
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

    //
    CustomerDAO cd = new CustomerDAO();
    EmailSender es = new EmailSender();
    // Lists to store different types of messages
    List<String> successMessages = new ArrayList<>();
    List<String> infoMessages = new ArrayList<>();
    List<String> warningMessages = new ArrayList<>();
    List<String> errorMessages = new ArrayList<>();

    private static final long serialVersionUID = 1L;
    private Map<String, CountdownInfo> emailToCountdownMap = new HashMap<>();

    /**
     * Method description: Clears all message lists.
     */
    private void clearMessages() {
        successMessages.clear();
        infoMessages.clear();
        warningMessages.clear();
        errorMessages.clear();
    }

    /**
     * Method description: Adds message lists as attributes to the request.
     *
     * @param request - The HttpServletRequest.
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
     * @param request - The HttpServletRequest.
     * @param response - The HttpServletResponse.
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
            default ->
                "index.html";
        };
        request.getRequestDispatcher(url).forward(request, response);
    }

    /**
     * Handles the HTTP <code>Post</code> method.
     *
     * @param request - The HttpServletRequest.
     * @param response - The HttpServletResponse.
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
            default ->
                throw new AssertionError();
        }
    }

    /**
     * Method description: Handles the <code>login</code> process.
     *
     * @param request - The HttpServletRequest.
     * @param response - The HttpServletResponse.
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
                successMessages.add("Correct email and password.");
                addMessages(request);

                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("email", email);

            errorMessages.add("Your email is incorrect or not register yet!");
            addMessages(request);

            request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
        }
    }

    /**
     * Method description: Handles the <code>registration</code> process.
     *
     * @param request - The HttpServletRequest.
     * @param response - The HttpServletResponse.
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
     * Method description:
     *
     * @param request - The HttpServletRequest.
     * @param response - The HttpServletResponse.
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
                String otp = cd.generateRandomOTP();
                
                cd.addOTPForAccountByEmail(otp, email);
                ServletContext context = getServletContext();
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(() -> {
                    es.sendMsgEmail(context, email, "Your New OTP Code", "ocType", otp);
                }, 1, TimeUnit.SECONDS);

                int countdownDuration = 30;
                countdownInfo = new CountdownInfo(countdownDuration, System.currentTimeMillis());
                emailToCountdownMap.put(email, countdownInfo);
                successMessages.add("Your OTP Code was sent successfully to email: " + email);
                request.setAttribute("countdownDuration", countdownDuration);
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
     * Method description:
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void getNewPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

//        ServletContext context = getServletContext();
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.schedule(() -> {
//            es.sendMsgEmail(context, email, "Your New Password", "pType");
//        }, 1, TimeUnit.SECONDS);
        clearMessages();
        successMessages.add("Your password was sent successfully to email: " + email);
        addMessages(request);
        request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
    }
}
