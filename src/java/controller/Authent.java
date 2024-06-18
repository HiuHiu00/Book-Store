/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import EmailSender.EmailSender;
import dal.CustomerDAO;
import entity.Account;
import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author FPT-LAPTOP
 */
@WebServlet(name = "Authent", urlPatterns = {"/authent"})
public class Authent extends HttpServlet {

    CustomerDAO cd = new CustomerDAO();
    EmailSender es = new EmailSender();

    List<String> successMessages = new ArrayList<>();
    List<String> infoMessages = new ArrayList<>();
    List<String> warningMessages = new ArrayList<>();
    List<String> errorMessages = new ArrayList<>();
    private static final long serialVersionUID = 1L;

    private void clearMessages() {
        successMessages.clear();
        infoMessages.clear();
        warningMessages.clear();
        errorMessages.clear();
    }

    private void addMessages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("successMessages", successMessages);
        request.setAttribute("infoMessages", infoMessages);
        request.setAttribute("warningMessages", warningMessages);
        request.setAttribute("errorMessages", errorMessages);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "";
        String action = request.getParameter("action") == null ? "login" : request.getParameter("action");
        switch (action) {
            case "login":
                url = "views/Login.jsp";
                break;
            case "register":
                url = "views/Register.jsp";
                break;
            case "forgotPassword":
                url = "views/ForgotPassword.jsp";
                break;
            default:
                url = "index.html";
                break;
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "login" : request.getParameter("action");
        switch (action) {
            case "login":
                login(request, response);
                break;
            case "register":
                register(request, response);
                break;
            case "getNewPassword":
                getNewPassword(request, response);
                break;
            case "getOtpCode":
                sendOtp(request, response);
                break;
            default:
                throw new AssertionError();
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("pass");
        boolean emailExists = cd.checkAccountExistsByEmail(email);
        String passwordCheck = cd.getPasswordByEmail(email);

        if (emailExists) {
            if (password == null ? passwordCheck != null : !password.equals(passwordCheck)) {
                request.setAttribute("email", email);

                clearMessages();
                errorMessages.add("Your email or password are not correct!");
                addMessages(request, response);

                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            } else {
                clearMessages();
                successMessages.add("Correct email and password.");
                addMessages(request, response);

                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("email", email);

            clearMessages();
            errorMessages.add("Your email is incorrect or not register yet!");
            addMessages(request, response);

            request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("passw");
        String cpassword = request.getParameter("confirmPass");

        boolean emailExists = cd.checkAccountExistsByEmail(email);
        if (emailExists) {
            request.setAttribute("email", email);
            request.setAttribute("password", password);

            clearMessages();
            errorMessages.add("Your email already registered!");
            addMessages(request, response);

            request.getRequestDispatcher("/views/Register.jsp").forward(request, response);
        } else {
            if (password.equals(cpassword)) {
                cd.addAccount(email, password);

                clearMessages();
                successMessages.add("Registered successfully!");
                addMessages(request, response);

                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            } else {
                request.setAttribute("email", email);
                request.setAttribute("passw", password);

                clearMessages();
                errorMessages.add("Confirm password and password not matched!");
                addMessages(request, response);

                request.getRequestDispatcher("/views/Register.jsp").forward(request, response);
            }
        }
    }

    private void sendOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        boolean emailExists = cd.checkAccountExistsByEmail(email);

        if (emailExists) {
            String otp = cd.generateRandomOTP();
            cd.addOTPForAccountByEmail(otp, email);

            ServletContext context = getServletContext();
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> {
                es.sendMsgEmail(context, email, "Your New OTP Code", "ocType");
            }, 1, TimeUnit.SECONDS);

            request.setAttribute("email", email);

            clearMessages();
            successMessages.add("Your OTP Code was sent successfully to email: " + email);
            addMessages(request, response);
            request.getRequestDispatcher("/views/ForgotPassword.jsp").forward(request, response);
        } else {
            request.setAttribute("email", email);

            clearMessages();
            errorMessages.add("This email does not registered yet or is incorrect!");
            addMessages(request, response);

            request.getRequestDispatcher("/views/ForgotPassword.jsp").forward(request, response);
        }
    }

    private void getNewPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        ServletContext context = getServletContext();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            es.sendMsgEmail(context, email, "Your New Password", "pType");
        }, 1, TimeUnit.SECONDS);

        clearMessages();
        successMessages.add("Your password was sent successfully to email: " + email);
        addMessages(request, response);
        request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
    }
}
