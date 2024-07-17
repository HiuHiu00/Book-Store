<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!--===============================================================================================-->	
        <link rel="icon" type="image/png" href="assets/Template1/images/icons/favicon.ico"/>
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="assets/Template1/vendor/bootstrap/css/bootstrap.min.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css"/>
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="assets/Template1/vendor/animate/animate.css">
        <!--===============================================================================================-->	
        <link rel="stylesheet" type="text/css" href="assets/Template1/vendor/css-hamburgers/hamburgers.min.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="assets/Template1/vendor/select2/select2.min.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="assets/Template1/css/util.css">
        <link rel="stylesheet" type="text/css" href="assets/Template1/css/main.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css" rel="stylesheet">


    </head>
    <body>
        <div class="limiter">
            <div class="container-login100">
                <div style="padding-top: 33px;" class="wrap-login100">
                    <div class="login100-pic js-tilt" data-tilt>
                        <img src="assets/Template1/images/img2.png" alt="IMG">
                    </div>

                    <form class="login100-form validate-form" action="authent?action=adminLogin" method="post">
                        <span style="padding-bottom: 0;" class="login100-form-title">
                            Admin/Staff Login
                        </span>
                        <div class="text-center p-t-2 p-b-50">
                            <span class="txt1">
                                Login as
                            </span>
                            <a class="txt2" href="authent?action=login">
                                Member <i class="fas fa-long-arrow-alt-right m-l-5" aria-hidden="true"></i>
                            </a>
                        </div>
                        <div class="wrap-input100 validate-input" data-validate = "Valid email is required: ex@abc.xyz">
                            <c:choose>
                                <c:when test="${not empty requestScope.email}">                    
                                    <input class="input100" type="text" name="email" placeholder="Email" value="${requestScope.email}" required>
                                </c:when>
                                <c:otherwise>
                                    <input class="input100" type="text" name="email" placeholder="Email" required>
                                </c:otherwise>
                            </c:choose>
                            <span class="focus-input100"></span>
                            <span class="symbol-input100">
                                <i class="fa fa-envelope" aria-hidden="true"></i>
                            </span>
                        </div>

                        <div class="wrap-input100 validate-input" data-validate = "Password is required">
                            <c:choose>
                                <c:when test="${not empty requestScope.password}">                    
                                    <input class="input100" type="password" name="pass" placeholder="Password" value="${requestScope.password}" required>
                                </c:when>
                                <c:otherwise>
                                    <input class="input100" type="password" name="pass" placeholder="Password" required>
                                </c:otherwise>
                            </c:choose>
                            <span class="focus-input100"></span>
                            <span class="symbol-input100">
                                <i class="fa fa-lock" aria-hidden="true"></i>
                            </span>
                        </div>

                        <div class="wrap-input100 validate-input" data-validate = "Key is required">
                            <input class="input100" type="password" name="rkey" placeholder="Admin Key" required>
                            <span class="focus-input100"></span>
                            <span class="symbol-input100">
                                <i class="fa fa-key" aria-hidden="true"></i>
                            </span>
                        </div>

                        <div class="container-login100-form-btn" id="loginBtn">

                            <button class="login100-form-btn" type="submit">
                                <span>Login</span>
                                <span><i class="fas fa-sign-in-alt" style="margin-left: 5px;"> </i></span>
                            </button>
                        </div>
                        <div class="text-center p-t-12">
                            <a class="txt2" href="browse?action=home">
                                <i class="fas fa-long-arrow-alt-left m-r-5"></i> Go to Home Page
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>

        <script>
            $(document).ready(function () {
                toastr.options.closeButton = true;
                toastr.options.progressBar = true;
            <%
                   List<String> successMessages = (List<String>) request.getAttribute("successMessages");
                   List<String> infoMessages = (List<String>) request.getAttribute("infoMessages");
                   List<String> warningMessages = (List<String>) request.getAttribute("warningMessages");
                   List<String> errorMessages = (List<String>) request.getAttribute("errorMessages");
            %>

            <% if (successMessages != null) { %>
            <% for (String message : successMessages) { %>
                toastr.success('<%= message %>', '', {timeOut: 5000});
            <% } %>
            <% } %>

            <% if (infoMessages != null) { %>
            <% for (String message : infoMessages) { %>
                toastr.info('<%= message %>', '', {timeOut: 5000});
            <% } %>
            <% } %>

            <% if (warningMessages != null) { %>
            <% for (String message : warningMessages) { %>
                toastr.warning('<%= message %>', '', {timeOut: 5000});
            <% } %>
            <% } %>

            <% if (errorMessages != null) { %>
            <% for (String message : errorMessages) { %>
                toastr.error('<%= message %>', '', {timeOut: 5000});
            <% } %>
            <% } %>
            });
        </script>
        <!--===============================================================================================-->	
        <script src="assets/Template1/vendor/jquery/jquery-3.2.1.min.js"></script>
        <!--===============================================================================================-->
        <script src="assets/Template1/vendor/bootstrap/js/popper.js"></script>
        <script src="assets/Template1/vendor/bootstrap/js/bootstrap.min.js"></script>
        <!--===============================================================================================-->
        <script src="assets/Template1/vendor/select2/select2.min.js"></script>
        <!--===============================================================================================-->
        <script src="assets/Template1/vendor/tilt/tilt.jquery.min.js"></script>
        <script>

            $('.js-tilt').tilt({
                scale: 1.1
            })
        </script>
        <!--===============================================================================================-->
        <script src="assets/Template1/js/main2.js"></script>
    </body>
</html>