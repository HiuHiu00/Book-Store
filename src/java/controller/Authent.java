/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CustomerDAO;
import entity.Account;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FPT-LAPTOP
 */
@WebServlet(name = "Authent", urlPatterns = {"/authent"})
public class Authent extends HttpServlet {

    CustomerDAO cd = new CustomerDAO();

    List<String> successMessages = new ArrayList<>();
    List<String> infoMessages = new ArrayList<>();
    List<String> warningMessages = new ArrayList<>();
    List<String> errorMessages = new ArrayList<>();

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
            case "forgotPassword":
                forgotPassword(request, response);
                break;
            case "sendOtp":
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        boolean emailExists = cd.checkAccountExistsByEmail(email);
        if (emailExists) {
            request.setAttribute("email", email);

            clearMessages();
            successMessages.add("Email exists!");
            addMessages(request, response);
            request.getRequestDispatcher("/views/ForgotPassword.jsp").forward(request, response);
        } else {
            request.setAttribute("email", email);

            clearMessages();
            errorMessages.add("This email does not registered or is incorrect!");
            addMessages(request, response);

            request.getRequestDispatcher("/views/ForgotPassword.jsp").forward(request, response);
        }
    }

    private void forgotPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
