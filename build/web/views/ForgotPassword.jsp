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
            .border-container {
                border: 2px solid transparent;
                padding: 10px;
                transition: border-color 0.3s, background-color 0.3s;
                overflow: hidden;
            }

            .highlight {
                border-color: #4CAF50;
                border-radius: 3.4%;
                background-color: #f9f9f9;
            }

            .dim {
                opacity: 0.5;
            }

            .hidden {
                max-height: 0;
                opacity: 0;
                transition: max-height 0.5s ease-out, opacity 0.5s ease-out;
            }

            .visible {
                max-height: 500px; /* Adjust according to your content */
                opacity: 1;
                transition: max-height 0.5s ease-in, opacity 0.5s ease-in;
            }

            .back-link {
                display: block;
                margin-top: 20px;
                text-align: center;
                color: #4CAF50;
                cursor: pointer;
                text-decoration: none;
            }

            .back-link:hover {
                text-decoration: underline;
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

                    <form class="login100-form validate-form" action="authent?action=forgotPassword" method="post">
                        <span class="login100-form-title">
                            Get new password
                        </span>

                        <div id="emailContainer" class="border-container visible">
                            <div class="wrap-input100 validate-input" data-validate = "Valid email is required: ex@abc.xyz">
                                <c:choose>
                                    <c:when test="${not empty requestScope.email}">                    
                                        <input class="input100" type="text" name="email" placeholder="Email" value="${requestScope.email}" onfocus="highlightContainer('emailContainer')" onblur="resetContainers()">
                                    </c:when>
                                    <c:otherwise>
                                        <input class="input100" type="text" name="email" placeholder="Email" onfocus="highlightContainer('emailContainer')" onblur="resetContainers()">
                                    </c:otherwise>
                                </c:choose>

                                <span class="focus-input100"></span>
                                <span class="symbol-input100">
                                    <i class="fa fa-envelope" aria-hidden="true"></i>
                                </span>
                            </div>

                            <div class="container-login100-form-btn" id="otpBtn" >
                                <button class="login100-form-btn" type="button" onclick="getOtpCode()">
                                    <span>Get OTP Code</span>
                                    <span><i class="fa fa-key" style="margin-left: 5px;"> </i></span>
                                </button>
                            </div>
                        </div>

                        <div id="otpContainer" class="border-container hidden">
                            <div class="wrap-input100 validate-input" data-validate = "Valid otp code is required: 6-digits" onfocus="highlightContainer('otpContainer')" onblur="resetContainers()">
                                <input class="input100" type="text" ame="otpCode" placeholder="Enter OTP Code" maxlength="6">
                                <span class="focus-input100"></span>
                                <span class="symbol-input100">
                                    <i class="fa fa-envelope" aria-hidden="true"></i>
                                </span>
                            </div>

                            <div class="container-login100-form-btn" id="verifyBtn">

                                <button class="login100-form-btn" type="submit">
                                    <span>Verify</span>
                                    <span><i class="fa fa-envelope" style="margin-left: 5px;"> </i></span>
                                </button>
                            </div>
                        </div>
                        <a class="back-link" onclick="toggleVisibility('otpContainer', 'emailContainer')">Back to Email</a>

                        <div class=" login100-form text-center p-t-6">
                            Back to login page
                            <a class="txt2" href="authent?action=login">
                                <i class="fa fa-long-arrow-right m-l-5" aria-hidden="true"></i>
                                Back
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
        <script>

                            function getOtpCode() {
    var email = $('input[name="email"]').val();
    if (validateEmail(email)) {
        $.ajax({
            url: 'authent?action=sendOtp',
            type: 'POST',
            dataType: 'json', // Expecting JSON response
            data: { email: email },
            success: function(response) {
                if (response.status === "success") {
                    toastr.success(response.message, '', {timeOut: 5000});
                    toggleVisibility('emailContainer', 'otpContainer');
                } else if (response.status === "error") {
                    toastr.error(response.message, '', {timeOut: 5000});
                }
            },
            error: function() {
                toastr.error('An error occurred. Please try again.', '', {timeOut: 3000});
            }
        });
    } else {
        toastr.warning('Please enter a valid email address.', '', {timeOut: 3000});
    }
}

                            function validateEmail(email) {
                                var re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                                return re.test(email);
                            }


                            function toggleVisibility(hideId, showId) {
                                document.getElementById(hideId).classList.add('hidden');
                                document.getElementById(hideId).classList.remove('visible');
                                document.getElementById(showId).classList.remove('hidden');
                                document.getElementById(showId).classList.add('visible');
                            }

                            function highlightContainer(containerId) {
                                document.getElementById(containerId).classList.add('highlight');
                                var otherContainerId = containerId === 'emailContainer' ? 'otpContainer' : 'emailContainer';
                                document.getElementById(otherContainerId).classList.add('dim');
                            }

                            function resetContainers() {
                                document.getElementById('emailContainer').classList.remove('highlight');
                                document.getElementById('otpContainer').classList.remove('highlight');
                                document.getElementById('emailContainer').classList.remove('dim');
                                document.getElementById('otpContainer').classList.remove('dim');
                            }

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
