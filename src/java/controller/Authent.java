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

/**
 *
 * @author FPT-LAPTOP
 */
@WebServlet(name = "Authent", urlPatterns = {"/authent"})
public class Authent extends HttpServlet {

    CustomerDAO cd = new CustomerDAO();

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
                request.setAttribute("errorMes", "Your email or password are not correct!");
                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            } else {
                request.setAttribute("successMes", "Correct email and password.");
                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("email", email);
            request.setAttribute("errorMes", "Your email is incorrect or not register yet!");
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
            request.setAttribute("errorMes", "Your email already registered!");
            request.getRequestDispatcher("/views/Register.jsp").forward(request, response);
        } else {
            if (password.equals(cpassword)) {
                cd.addAccount(email,password);
                request.setAttribute("successMes", "Registered successfully!");
                request.getRequestDispatcher("/views/Login.jsp").forward(request, response);
            } else {
                request.setAttribute("email", email);
                request.setAttribute("passw", password);
                request.setAttribute("errorMes", "Confirm password and password not matched!");
                request.getRequestDispatcher("/views/Register.jsp").forward(request, response);
            }
        }
    }
}
