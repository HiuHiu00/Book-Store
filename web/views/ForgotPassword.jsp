<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

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

        <style>
            .toast {
                position: fixed;
                top: 20px;
                right: 20px;
                padding: 15px;
                border-radius: 5px;
                display: none;
                overflow: hidden;
            }

            .toast::after {
                content: "";
                display: block;
                height: 5px;
                background-color: rgba(255, 255, 255, 0.7);
                position: absolute;
                bottom: 0;
                left: 0;
                width: 100%;
                transform: scaleX(0);
                transform-origin: bottom left;
            }

            .toast.show {
                display: block;
                animation: fadeIn 0.5s, fadeOut 0.5s 4.5s;
            }

            .toast.show::after {
                animation: scaleX 5s linear;
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                }
                to {
                    opacity: 1;
                }
            }

            @keyframes fadeOut {
                from {
                    opacity: 1;
                }
                to {
                    opacity: 0;
                }
            }

            @keyframes scaleX {
                from {
                    transform: scaleX(0);
                }
                to {
                    transform: scaleX(1);
                }
            }

            .error {
                background-color: #ff3333;
                color: #fff;
            }

            .success {
                background-color: #33cc33;
                color: #fff;
            }

            .notification {
                background-color: #ffff66;
                color: #000;
            }

            .border-container {
                border: 2px solid transparent;
                padding: 10px;
                transition: border-color 0.3s, background-color 0.3s;
                overflow: hidden;
            }

            .highlight {
                border-color: #4CAF50; /* Green border */
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

                    <form class="login100-form validate-form" action="authent?action=register" method="post">
                        <span class="login100-form-title">
                            Get new password
                        </span>

                        <!-- Email and Get OTP Code Container -->
                        <div id="emailContainer" class="border-container visible">
                            <div class="wrap-input100 validate-input" data-validate="Valid email is required: ex@abc.xyz">
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

                            <div class="container-login100-form-btn" id="getOtpBtn">
                                <button class="login100-form-btn" type="button" onclick="getOtpCode()">
                                    <span>Get OTP Code</span>
                                    <span><i class="fa fa-key" style="margin-left: 5px;"></i></span>
                                </button>
                            </div>
                        </div>

                        <!-- OTP Code and Verify Container -->
                        <div id="otpContainer" class="border-container hidden">
                            <div class="wrap-input100 validate-input" data-validate="OTP code is required">
                                <input class="input100" type="text" name="otpCode" placeholder="Enter OTP Code" maxlength="6" onfocus="highlightContainer('otpContainer')" onblur="resetContainers()">
                                <span class="focus-input100"></span>
                                <span class="symbol-input100">
                                    <i class="fa fa-key" aria-hidden="true"></i>
                                </span>
                            </div>
                            
                            <div class="container-login100-form-btn" id="verifyBtn">
                                <button class="login100-form-btn" type="submit">
                                    <span>Verify</span>
                                    <span><i class="fa fa-envelope" style="margin-left: 5px;"></i></span>
                                </button>
                            </div>

                            <a class="back-link" onclick="toggleVisibility('otpContainer', 'emailContainer')">Back to Email</a>
                        </div>

                        <div class="text-center p-t-6">
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
        <div id="toast" class="toast"></div>

        <script>
            var errorMes = "${errorMes}".trim();
            var successMes = "${successMes}".trim();
            var notificationMes = "${notificationMes}".trim();

            if (errorMes !== "") {
                showToast(errorMes, "error");
            }
            if (successMes !== "") {
                showToast(successMes, "success");
            }
            if (notificationMes !== "") {
                showToast(notificationMes, "notification");
            }

            function showToast(message, type) {
                var toast = document.getElementById('toast');
                var icon = '';
                if (type === "error") {
                    icon = '<i class="fa fa-times-circle icon"></i>';
                } else if (type === "success") {
                    icon = '<i class="fa fa-check-circle icon"></i>';
                } else if (type === "notification") {
                    icon = '<i class="fa fa-info-circle icon"></i>';
                }
                toast.innerHTML = icon + "  " + message;
                toast.className = "toast " + type + " show";
                setTimeout(function () {
                    toast.className = "toast " + type;
                }, 5000);
            }

            function getOtpCode() {
                toggleVisibility('emailContainer', 'otpContainer');
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
