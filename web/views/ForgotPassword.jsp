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
        <link rel="icon" type="image/png" href="assets/images/icons/favicon.ico"/>
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="assets/vendor/bootstrap/css/bootstrap.min.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="assets/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="assets/vendor/animate/animate.css">
        <!--===============================================================================================-->    
        <link rel="stylesheet" type="text/css" href="assets/vendor/css-hamburgers/hamburgers.min.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="assets/vendor/select2/select2.min.css">
        <!--===============================================================================================-->
        <link rel="stylesheet" type="text/css" href="assets/css/util.css">
        <link rel="stylesheet" type="text/css" href="assets/css/main.css">

        <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css" rel="stylesheet">
        <style>
            .login100-form-btn:disabled {
                background: #999999;
                cursor: not-allowed;
            }
        </style>
    </head>
    <body>
        <div class="limiter">
            <div class="container-login100">
                <div style="padding-top: 33px;" class="wrap-login100">
                    <div class="login100-pic js-tilt" data-tilt>
                        <img src="assets/images/img2.png" alt="IMG">
                    </div>

                    <div class="login100-form validate-form">
                        <span class="login100-form-title">
                            Get new password
                        </span>
                        <form class="validate-form form-otp" action="authent?action=getOtpCode" method="post">
                            <div class="wrap-input100 validate-input" data-validate="Valid email is required: ex@abc.xyz">
                                <c:choose>
                                    <c:when test="${not empty requestScope.email}">
                                        <input class="input100" type="text" name="email" placeholder="Email" value="${requestScope.email}">
                                    </c:when>
                                    <c:otherwise>
                                        <input class="input100" type="text" name="email" placeholder="Email">
                                    </c:otherwise>
                                </c:choose>
                                <span class="focus-input100"></span>
                                <span class="symbol-input100">
                                    <i class="fa fa-envelope" aria-hidden="true"></i>
                                </span>
                            </div>
                            <div class="container-login100-form-btn" style="padding-top: 0; padding-bottom: 25px;" id="otpBtn">
                                <c:choose>
                                    <c:when test="${not empty requestScope.email}">
                                        <button id="otpButton" class="login100-form-btn" type="submit" disabled>
                                            <span>Resend OTP Code</span>
                                            <span><i class="fa fa-key" style="margin-left: 5px;"></i></span>
                                            <span id="countdown" style="margin-left: 10px;"></span>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="login100-form-btn" type="submit">
                                            <span>Get OTP Code</span>
                                            <span><i class="fa fa-key" style="margin-left: 5px;"></i></span>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>

                        <c:if test="${not empty requestScope.email}">
                            <form class="validate-form form-password" action="authent?action=getNewPassword" method="post">
                                <div class="wrap-input100 validate-input" data-validate="Valid otp code is required: 6-digits">
                                    <input class="input100" type="text" name="otpCode" placeholder="Enter OTP Code" maxlength="6">
                                    <span class="focus-input100"></span>
                                    <span class="symbol-input100">
                                        <i class="fa fa-envelope" aria-hidden="true"></i>
                                    </span>
                                </div>
                                <div class="container-login100-form-btn" style="padding-top: 0;" id="verifyBtn">
                                    <button class="login100-form-btn" type="submit">
                                        <span>Verify</span>
                                        <span><i class="fa fa-envelope" style="margin-left: 5px;"></i></span>
                                    </button>
                                </div>
                            </form>
                        </c:if>

                        <div class=" login100-form text-center p-t-6">
                            Back to login page
                            <a class="txt2" href="authent?action=login">
                                <i class="fa fa-long-arrow-right m-l-5" aria-hidden="true"></i>
                                Back
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
        <script>
            function startCountdown(duration, display) {
                let timer = duration, minutes, seconds;
                let otpButton = document.getElementById('otpButton');
                setInterval(function () {
                    minutes = parseInt(timer / 60, 10);
                    seconds = parseInt(timer % 60, 10);

                    minutes = minutes < 10 ? "0" + minutes : minutes;
                    seconds = seconds < 10 ? "0" + seconds : seconds;

                    display.textContent = minutes + ":" + seconds;

                    if (--timer < 0) {
                        timer = 0;
                        otpButton.disabled = false;
                        display.textContent = "";
                    }
                }, 1000);
            }

            window.onload = function () {
                let countdownDuration = 3, display = document.querySelector('#countdown');
                startCountdown(countdownDuration, display);
            };

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
        <script src="assets/vendor/jquery/jquery-3.2.1.min.js"></script>
        <!--===============================================================================================-->
        <script src="assets/vendor/bootstrap/js/popper.js"></script>
        <script src="assets/vendor/bootstrap/js/bootstrap.min.js"></script>
        <!--===============================================================================================-->
        <script src="assets/vendor/select2/select2.min.js"></script>
        <!--===============================================================================================-->
        <script src="assets/vendor/tilt/tilt.jquery.min.js"></script>
        <script>
            $('.js-tilt').tilt({
                scale: 1.1
            })
        </script>
        <!--===============================================================================================-->
        <script src="assets/js/main2.js"></script>
    </body>
</html>
