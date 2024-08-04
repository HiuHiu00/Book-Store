<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Fruitables - Vegetable Website Template</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">

        <!-- Google Web Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap" rel="stylesheet"> 

        <!-- Icon Font Stylesheet -->
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Libraries Stylesheet -->
        <link href="assets/Template2/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
        <link href="assets/Template2/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">


        <!-- Customized Bootstrap Stylesheet -->
        <link href="assets/Template2/css/bootstrap.min.css" rel="stylesheet">

        <!-- Template Stylesheet -->
        <link href="assets/Template2/css/style.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css" rel="stylesheet">
        <style>

            .no-style {
                all: unset;
                text-decoration: none;
                color: inherit;
            }
        </style>
    </head>

    <body>

        <!-- Spinner Start -->
        <div id="spinner" class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50  d-flex align-items-center justify-content-center">
            <div class="spinner-grow text-primary" role="status"></div>
        </div>
        <!-- Spinner End -->


        <!-- Navbar start -->
        <div class="container-fluid fixed-top">
            <div class="container topbar bg-primary d-none d-lg-block">
                <div class="d-flex justify-content-between">
                    <div class="top-info ps-2">
                        <small class="me-3"><i class="fas fa-map-marker-alt me-2 text-secondary"></i> <a href="#" class="text-white">Address 123, Vietnam, Hanoi.</a></small>
                        <small class="me-3"><i class="fas fa-envelope me-2 text-secondary"></i><a href="#" class="text-white">clothingshoponlineg1se1754@gmail.com</a></small>
                    </div>
                    <div class="top-link pe-2">
                        <a href="#" class="text-white"><small class="text-white mx-2">Privacy Policy</small>/</a>
                        <a href="#" class="text-white"><small class="text-white mx-2">Terms of Use</small>/</a>
                        <a href="#" class="text-white"><small class="text-white ms-2">Sales and Refunds</small></a>
                    </div>
                </div>
            </div>
            <div class="container px-0">
                <nav class="navbar navbar-light bg-white navbar-expand-xl">
                    <a href="browse?action=home" class="navbar-brand"><h1 class="text-primary display-6">My Book Store</h1></a>
                    <button class="navbar-toggler py-2 px-3" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
                        <span class="fa fa-bars text-primary"></span>
                    </button>
                    <div class="collapse navbar-collapse bg-white" id="navbarCollapse">
                        <div class="navbar-nav mx-auto">
                            <a href="browse?action=home" class="nav-item nav-link active">Home</a>
                            <a href="browse?action=bookList" class="nav-item nav-link active">Book List</a>
                            <!--                            <div class="nav-item dropdown">
                                                            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">Pages</a>
                                                            <div class="dropdown-menu m-0 bg-secondary rounded-0">
                                                                <a href="cart.html" class="dropdown-item">Cart</a>
                                                                <a href="chackout.html" class="dropdown-item">Chackout</a>
                                                            </div>
                                                        </div>-->
                        </div>
                        <div class="d-flex m-3 me-0">
                            <!--<button class="btn-search btn border border-secondary btn-md-square rounded-circle bg-white me-4" data-bs-toggle="modal" data-bs-target="#searchModal"><i class="fas fa-search text-primary"></i></button>-->
                            <a href="#" class="position-relative me-4 my-auto" data-bs-toggle="modal" data-bs-target="#cartModal">
                                <i class="fa fa-shopping-bag fa-2x"></i>
                                <span class="position-absolute bg-secondary rounded-circle d-flex align-items-center justify-content-center text-dark px-1" style="top: -5px; left: 15px; height: 20px; min-width: 20px;">${requestScope.cartProductCount}</span>
                            </a>
                            <div class="nav-item dropdown">
                                <a href="#" class="my-auto nav-link"> <i class="fas fa-user fa-2x"></i> </a>
                                <div class="dropdown-menu m-0 bg-secondary rounded-0">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.isLoggedIn}">          
                                            <a href="#" class="dropdown-item">Profile</a>
                                            <a href="authent?action=logout" class="dropdown-item">Logout</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="authent?action=login" class="dropdown-item">Login</a>
                                            <a href="authent?action=register" class="dropdown-item">Register</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </nav>
            </div>
        </div>
        <!-- Navbar End -->

        <!-- Profile Start -->
        <c:set var="profile" value="${requestScope.profile}" />
        <div class="container rounded bg-white mt-5 mb-5" style="padding-top: 100px;">
            <div class="row">
                <div class="col-md-3 border-end">
                    <div class="d-flex flex-column align-items-center text-center p-3 py-5"><img class="rounded-circle mt-5" width="150px" src="assets/Template2/${profile.accountDetail.imagePath}"><span class="font-weight-bold"></span><span class="text-black-50">${profile.email}</span><span> </span></div>
                </div>
                <div class="col-md-5 border-end">
                    <div class="p-3 py-5">
                        <form action="authent?action=updateProfile" method="post">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h4 class="text-right">Profile Information</h4>
                            </div>
                            <div class="row mt-3">
                                <div class="col-md-12"><label class="labels">Your Name</label><input type="text" name="username" class="form-control" placeholder="Your name" value="${profile.accountDetail.username}"></div>
                            </div>
                            <div class="row mt-3">
                                <div class="col-md-12"><label class="labels">Mobile Phone Number</label><input type="text" name="phoneNumber" class="form-control" maxlength="10" placeholder="Enter phone number" value="${profile.accountDetail.phoneNumber}"></div>
                            </div>
                            <div class="row mt-3">
                                <div class="col-md-12"><label class="labels">Address Line</label><input type="text" name="address" class="form-control" placeholder="Enter address line" value="${profile.accountDetail.address}"></div>   
                            </div>
                            <div class="row mt-3">
                                <div class="col-md-12"><label class="labels">Gender</label>
                                    <div class="d-flex align-items-center">
                                        <div class="form-check me-3">
                                            <input class="form-check-input" type="radio" name="gender" id="male" value="Male" ${profile.accountDetail.gender ? 'checked' : ''}>
                                            <label class="form-check-label" for="male">M/Male</label>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" name="gender" id="female" value="Female" ${profile.accountDetail.gender ? '' : 'checked'}>
                                            <label class="form-check-label" for="female">F/Female</label>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="mt-5 text-center"><button class="btn btn-primary profile-button" type="submit">Update Profile</button></div>
                        </form>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="p-3 py-5">
                        <form action="authent?action=changePassword" method="post">
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <h4 class="text-right">Change Password</h4>
                            </div>
                            <div class="row mt-3">
                                <div class="col-md-12"><label class="labels">Your password</label><input type="password" name="currentPassword" class="form-control" placeholder="Enter your password" <c:if test="${not empty requestScope.currentPassword}">value="${requestScope.currentPassword}"</c:if> required></div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-md-12"><label class="labels">New password</label><input type="password" name="newPassword" class="form-control" placeholder="Enter new password" <c:if test="${not empty requestScope.newPassword}">value="${requestScope.newPassword}"</c:if> required></div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-md-12"><label class="labels">Confirm new password</label><input type="password" name="confirmNewPassword" class="form-control" placeholder="Enter new password again" required></div>
                                </div>
                                <div class="mt-5 text-center"><button class="btn btn-primary profile-button" type="submit">Change Password</button></div>
                            </form>
                        </div>
                    </div>

                </div>
            </div>

            <!-- Profile End -->

            <!--Modal Start--> 
            <style>
                .modal-dialog-right {
                    position: absolute;
                    top: 0;
                    right: 0;
                    margin: 0;
                    height: 100%;
                }
                .modal-dialog-scrollable {
                    max-height: 100%;
                    width: 30%;
                }
                .book-image img {
                    max-width: 100%;
                    height: auto;
                }
                .book-info p {
                    margin: 0;
                }
            </style>
            <div class="modal fade" id="cartModal" tabindex="-1" aria-labelledby="cartModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-scrollable modal-dialog-right">
                    <div class="modal-content" style="height: 100%">
                        <div class="modal-header">
                            <h5 class="modal-title" id="cartModalLabel">Your Cart</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                        <c:choose>
                            <c:when test="${not empty sessionScope.isLoggedIn}">          
                                <ul class="list-group">
                                    <c:forEach  items="${cartList}" var="cl">
                                        <li class="list-group-item d-flex align-items-center">
                                            <div class="book-image" style="flex: 0 0 20%;">
                                                <img src="assets/Template2/${cl.book.cover_imagePath}" alt="Book image" class="img-fluid">
                                            </div>
                                            <div class="book-info ms-3" style="flex: 0 0 70%;">
                                                <p class="mb-1">${cl.book.title}</p>
                                                <c:choose>
                                                    <c:when test="${cl.discount.discountPercent eq 0}">
                                                        Price: ${cl.book.price}$
                                                    </c:when>
                                                    <c:otherwise>Price: 
                                                        <span style="color:gray; text-decoration: line-through;">${cl.book.price}$</span>
                                                        <span >${cl.book.price - cl.book.price*cl.discount.discountPercent/100}$</span>
                                                    </c:otherwise>
                                                </c:choose>
                                                </p>
                                                <p class="mb-1">Quantity: ${cl.cartDetail.quantity}</p>
                                            </div>
                                            <div class="action-buttons ms-auto" style="flex: 0 0 10%; display: flex; flex-direction: column; gap: 0.5rem;">
                                                <form>
                                                    <a href="browse?action=bookDetail&bookID=${cl.book.bookID}" class="btn btn-secondary btn-sm rounded-circle">
                                                        <i class="fas fa-bookmark"></i>
                                                    </a>
                                                </form>
                                                <form action="browse?action=removeFromCart" method="post" class="removeFromCartForm">
                                                    <input name="currentPage" type="text" value="home" hidden>
                                                    <input name="bookIDToRemove" type="text" value="${cl.book.bookID}" hidden>
                                                    <input name="bookTitleToRemove" type="text" value="${cl.book.title}" hidden>
                                                    <a class="btn btn-danger btn-sm rounded-circle" onclick="document.getElementById('removeFromCartForm').submit();">
                                                        <i class="fas fa-times"></i>
                                                    </a>
                                                </form>
                                                <script>
                                                    document.querySelectorAll('.removeFromCartForm').forEach(function (button) {
                                                        button.addEventListener('click', function (event) {
                                                            event.preventDefault();
                                                            button.closest('form').submit();
                                                        });
                                                    });
                                                </script>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center" style="padding: 20px; ">
                                    <h2><img style="max-width: 20%" src="assets/Template2/images/npic.jpg" alt="thumbnail_not_found">
                                        <strong>You must have an account or log in to see your own shopping cart</strong>
                                    </h2>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="modal-footer">
                        <c:if test="${not empty sessionScope.isLoggedIn}">   
                            <div>Total:&Tab;<fmt:formatNumber value="${totalPriceAfterDiscount}" type="number" minFractionDigits="2" maxFractionDigits="2"/>$</div>
                            <div style="color: #ffb524;font-size: 0.9em">(Saving:&Tab;<fmt:formatNumber value="${totalPrice-totalPriceAfterDiscount}" type="number" minFractionDigits="2" maxFractionDigits="2"/>$)</div>
                        </c:if>
                        <button onclick="window.location.href = 'browse?action=checkout'" type="button" style="width: 100%" class="btn btn-secondary" data-bs-dismiss="modal" <c:if test="${ empty sessionScope.isLoggedIn}">disabled</c:if> >Checkout</button>
                        <button onclick="window.location.href = 'browse?action=buyNow'" type="button" style="width: 100%" class="btn btn-secondary" data-bs-dismiss="modal" <c:if test="${ empty sessionScope.isLoggedIn}">disabled</c:if>>Buy Now</button>
                        </div>
                    </div>
                </div>
            </div>
            <!--Modal End--> 

            <!-- Footer Start -->
            <div class="container-fluid bg-dark text-white-50 footer pt-5 mt-5">
                <div class="container py-5">
                    <div class="pb-4 mb-4" style="border-bottom: 1px solid rgba(226, 175, 24, 0.5) ;">
                        <div class="row g-4">
                            <div class="col-lg-3">
                                <a href="#">
                                    <h1 class="text-primary mb-0">My Book Store</h1>
                                    <p class="text-secondary mb-0">Life is Short, Read Fast.</p>
                                </a>
                            </div>
                            <div class="col-lg-6">
                                <div class="position-relative mx-auto">
                                    <input class="form-control border-0 w-100 py-3 px-4 rounded-pill" type="number" placeholder="Your Email">
                                    <button type="submit" class="btn btn-primary border-0 border-secondary py-3 px-4 position-absolute rounded-pill text-white" style="top: 0; right: 0;">Subscribe Now</button>
                                </div>
                            </div>
                            <div class="col-lg-3">
                                <div class="d-flex justify-content-end pt-3">
                                    <a class="btn  btn-outline-secondary me-2 btn-md-square rounded-circle" href=""><i class="fab fa-twitter"></i></a>
                                    <a class="btn btn-outline-secondary me-2 btn-md-square rounded-circle" href=""><i class="fab fa-facebook-f"></i></a>
                                    <a class="btn btn-outline-secondary me-2 btn-md-square rounded-circle" href=""><i class="fab fa-youtube"></i></a>
                                    <a class="btn btn-outline-secondary btn-md-square rounded-circle" href=""><i class="fab fa-linkedin-in"></i></a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row g-5">
                        <div class="col-lg-3 col-md-6">
                            <div class="footer-item">
                                <h4 class="text-light mb-3">Why People Like us!</h4>
                                <p class="mb-4">typesetting, remaining essentially unchanged. It was 
                                    popularised in the 1960s with the like Aldus PageMaker including of Lorem Ipsum.</p>
                                <a href="" class="btn border-secondary py-2 px-4 rounded-pill text-primary">Read More</a>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="d-flex flex-column text-start footer-item">
                                <h4 class="text-light mb-3">Shop Info</h4>
                                <a class="btn-link" href="">About Us</a>
                                <a class="btn-link" href="">Contact Us</a>
                                <a class="btn-link" href="">Privacy Policy</a>
                                <a class="btn-link" href="">Terms & Condition</a>
                                <a class="btn-link" href="">Return Policy</a>
                                <a class="btn-link" href="">FAQs & Help</a>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="d-flex flex-column text-start footer-item">
                                <h4 class="text-light mb-3">Account</h4>
                                <a class="btn-link" href="">My Account</a>
                                <a class="btn-link" href="">Shop details</a>
                                <a class="btn-link" href="">Shopping Cart</a>
                                <a class="btn-link" href="">Wishlist</a>
                                <a class="btn-link" href="">Order History</a>
                                <a class="btn-link" href="">International Orders</a>
                            </div>
                        </div>
                        <div class="col-lg-3 col-md-6">
                            <div class="footer-item">
                                <h4 class="text-light mb-3">Contact</h4>
                                <p>Address: 123, Vietnam, Hanoi.</p>
                                <p>Email: clothingshoponlineg1se1754@gmail.com</p>
                                <p>Phone: +0123 4567 8910</p>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Footer End -->

            <!-- Copyright Start -->
            <div class="container-fluid copyright bg-dark py-4">
                <div class="container">
                    <div class="row">
                        <div class="col-md-6 text-center text-md-start mb-3 mb-md-0">
                            <span class="text-light"><a href="#"><i class="fas fa-copyright text-light me-2"></i>MyBookStore</a>, All right reserved.</span>
                        </div>
                        <div class="col-md-6 my-auto text-center text-md-end text-white">
                            <!--/*** This template is free as long as you keep the below author’s credit link/attribution link/backlink. ***/-->
                            <!--/*** If you'd like to use the template without the below author’s credit link/attribution link/backlink, ***/-->
                            <!--/*** you can purchase the Credit Removal License from "https://htmlcodex.com/credit-removal". ***/-->
                            Designed By <a class="border-bottom" href="https://htmlcodex.com">HTML Codex</a> Distributed By <a class="border-bottom" href="https://themewagon.com">ThemeWagon</a>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Copyright End -->



            <!-- Back to Top -->
            <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top"><i class="fa fa-arrow-up"></i></a>   

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
                                toastr.success('<%= message %>', 'Success', {timeOut: 5000});
            <% } %>
            <% } %>

            <% if (infoMessages != null) { %>
            <% for (String message : infoMessages) { %>
                                toastr.info('<%= message %>', 'Notification', {timeOut: 5000});
            <% } %>
            <% } %>

            <% if (warningMessages != null) { %>
            <% for (String message : warningMessages) { %>
                                toastr.warning('<%= message %>', 'Warning', {timeOut: 5000});
            <% } %>
            <% } %>

            <% if (errorMessages != null) { %>
            <% for (String message : errorMessages) { %>
                                toastr.error('<%= message %>', 'Invalid', {timeOut: 5000});
            <% } %>
            <% } %>
                            });
        </script>
        <!-- JavaScript Libraries -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="assets/Template2/lib/easing/easing.min.js"></script>
        <script src="assets/Template2/lib/waypoints/waypoints.min.js"></script>
        <script src="assets/Template2/lib/lightbox/js/lightbox.min.js"></script>
        <script src="assets/Template2/lib/owlcarousel/owl.carousel.min.js"></script>

        <!-- Template Javascript -->
        <script src="assets/Template2/js/main.js"></script>
    </body>

</html>