<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
            #suggestions {
                position: absolute;
                border: 1px solid #ddd;
                max-height: 150px;
                overflow-y: auto;
                display: none;
                background-color: white;
                z-index: 1000;
                width: 100%;
            }
            #suggestions div {
                padding: 10px;
                cursor: pointer;
            }
            #suggestions div:hover {
                background-color: #f0f0f0;
            }
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
                            <a href="#" class="position-relative me-4 my-auto">
                                <i class="fa fa-shopping-bag fa-2x"></i>
                                <span class="position-absolute bg-secondary rounded-circle d-flex align-items-center justify-content-center text-dark px-1" style="top: -5px; left: 15px; height: 20px; min-width: 20px;">0</span>
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

        <!-- Fruits Shop Start-->
        <div class="container-fluid fruite py-5">
            <div class="container py-5">
                <h1 class="mb-4">My Book Store</h1>
                <div class="row g-4">
                    <div class="col-lg-12">
                        <div class="row g-4">
                            <div class="col-xl-3">
                                <h4>Featured Genres</h4>
                                <div style="position: relative;">
                                    <input type="text" class="form-control p-3" id="autocomplete-input" placeholder="Enter book genre you want to find">
                                    <div id="suggestions"></div>
                                </div>
                            </div>
                            <div class="col-6"></div>
                            <div class="col-xl-3">
                                <div class="bg-light ps-3 py-3 rounded d-flex justify-content-between mb-4">
                                    <label for="sortingOptions">Default Sorting:</label>
                                    <select id="sortingOptions" name="sortingOptions" class="border-0 form-select-sm bg-light me-3" form="fruitform">
                                        <option value="Nothing" <c:if test='${sessionScope.currentSortOptionSelected == "Nothing"}'>selected</c:if>>Nothing</option>
                                        <option value="AtoZ" <c:if test='${sessionScope.currentSortOptionSelected == "AtoZ"}'>selected</c:if>>A-Z</option>
                                        <option value="ZtoA" <c:if test='${sessionScope.currentSortOptionSelected == "ZtoA"}'>selected</c:if>>Z-A</option>
                                        <option value="IncrementPrice" <c:if test='${sessionScope.currentSortOptionSelected == "IncrementPrice"}'>selected</c:if>>&uarr; Price</option>
                                        <option value="DecrementPrice" <c:if test='${sessionScope.currentSortOptionSelected == "DecrementPrice"}'>selected</c:if>>&darr; Price</option>
                                        </select>
                                    </div>
                                </div>

                                <script>
                                    function sendSortingOption() {
                                        var selectedSortingOption = document.getElementById('sortingOptions').value;
                                        var url = 'browse?action=bookList&sortingOption=' + encodeURIComponent(selectedSortingOption);
                                        window.location.href = url;
                                    }

                                    var sortingSelect = document.getElementById('sortingOptions');
                                    sortingSelect.addEventListener('change', sendSortingOption);
                                </script>
                            </div>
                            <div class="row g-4">
                                <div class="col-lg-3">
                                    <div class="row g-4">
                                        <div class="col-lg-12">
                                            <div class="mb-3">
                                                <div id="selected-genres" style="max-width: 100%; height: 170px; border: 2px solid #28a745; border-radius: 10px; margin-top: 5px; padding:  1% 3%;">
                                                    <style>
                                                        .genre-choose li {
                                                            transition: box-shadow 0.3s ease, background-color 0.3s ease;
                                                        }

                                                        .genre-choose li:hover {
                                                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                                                            background-color: #f0f0f0;
                                                            cursor: pointer;
                                                            border-radius: 10px;
                                                        }
                                                    </style>
                                                    <ul class="list-unstyled fruite-categorie genre-choose">
                                                    <c:forEach var="genre" items="${sessionScope.genreSelected}">
                                                        <li onclick="removeGenre('${genre}')">
                                                            <div class="d-flex justify-content-between fruite-name">
                                                                <a style="color: #81c408"><i class="fas fa-book me-2"></i>${genre}</a>
                                                                <span><i class="fas fa-times"></i></span>
                                                            </div>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <script>
                                        const genreList = ${requestScope.book_genre_name_list};
                                        const input = document.getElementById('autocomplete-input');
                                        const suggestionBox = document.getElementById('suggestions');

                                        input.addEventListener('input', function () {
                                            const query = input.value.toLowerCase();
                                            suggestionBox.innerHTML = '';
                                            if (query) {
                                                const filteredSuggestions = genreList.filter(item => item.toLowerCase().includes(query));
                                                filteredSuggestions.forEach(suggestion => {
                                                    const div = document.createElement('div');
                                                    div.textContent = suggestion;
                                                    div.addEventListener('click', function () {
                                                        input.value = suggestion;
                                                        suggestionBox.style.display = 'none';
                                                        window.location.href = 'browse?action=bookList&addGenre=' + encodeURIComponent(suggestion);
                                                    });
                                                    suggestionBox.appendChild(div);
                                                });
                                                suggestionBox.style.display = 'block';
                                            } else {
                                                suggestionBox.style.display = 'none';
                                            }
                                        });
                                        function removeGenre(genre) {
                                            window.location.href = 'browse?action=bookList&removeGenre=' + encodeURIComponent(genre);
                                        }
                                    </script>
                                    <hr style="border-top: 2px solid #28a745;">
                                    <div class="col-lg-12">
                                        <div class="mb-3">
                                            <h4>Featured Price Range</h4>
                                            <div class="mb-2">
                                                <input type="radio" class="me-2" value="Level0" id="priceRangeAll" name="priceRange" 
                                                       <c:if test='${sessionScope.currentPriceRangeLevelSelected == "Level0"}'>checked</c:if>>
                                                       <label for="priceRangeAll">All</label>
                                                </div>
                                            <c:forEach var="price" items="${priceRanges}">
                                                <div class="mb-2">
                                                    <input type="radio" class="me-2" value="${price.value}" id="priceRange${price.value}" name="priceRange" 
                                                           <c:if test='${sessionScope.currentPriceRangeLevelSelected == price.value}'>checked</c:if>>
                                                    <label for="priceRange${price.value}">${price.label}</label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <script>
                                        function sendPriceRange() {
                                            var selectedPriceRangeLevelValue = document.querySelector('input[name="priceRange"]:checked').value;
                                            var url = 'browse?action=bookList&selectedPriceRangeLevel=' + encodeURIComponent(selectedPriceRangeLevelValue);
                                            window.location.href = url;
                                        }

                                        var priceRangeLevelradioButtons = document.querySelectorAll('input[name="priceRange"]');
                                        priceRangeLevelradioButtons.forEach(function (radio) {
                                            radio.addEventListener('change', sendPriceRange);
                                        });
                                    </script>
                                    <hr style="border-top: 2px solid #28a745;">
                                    <div class="col-lg-12">
                                        <div class="mb-3">
                                            <h4>Featured Author</h4>
                                            <div class="mb-2">
                                                <input type="radio" class="me-2" value="authorAll" id="authorAll" name="authorName" 
                                                       <c:if test='${sessionScope.currentAuthorSelected == "authorAll"}'>checked</c:if>>
                                                       <label for="authorAll">All</label>
                                                </div>
                                            <c:forEach var="author" items="${authorList}">
                                                <div class="mb-2">
                                                    <input type="radio" class="me-2" value="${author.authorName}" id="author${author.authorName}" name="authorName" 
                                                           <c:if test='${sessionScope.currentAuthorSelected == author.authorName}'>checked</c:if>>
                                                    <label for="author${author.authorName}">${author.authorName}</label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <script>
                                        function sendAuthorName() {
                                            var selectedAuthor = document.querySelector('input[name="authorName"]:checked').value;
                                            var url = 'browse?action=bookList&selectedAuthor=' + encodeURIComponent(selectedAuthor);
                                            window.location.href = url;
                                        }

                                        var authorRadioButtons = document.querySelectorAll('input[name="authorName"]');
                                        authorRadioButtons.forEach(function (radio) {
                                            radio.addEventListener('change', sendAuthorName);
                                        });
                                    </script>
                                    <hr style="border-top: 2px solid #28a745;">
                                    <div class="col-lg-12">
                                        <div class="mb-3">
                                            <h4>Featured Publisher</h4>
                                            <div class="mb-2">
                                                <input type="radio" class="me-2" value="publisherAll" id="publisherAll" name="publisherName" 
                                                       <c:if test='${sessionScope.currentPublisherSelected == "publisherAll"}'>checked</c:if>>
                                                       <label for="publisherAll">All</label>
                                                </div>
                                            <c:forEach var="publisher" items="${publisherList}">
                                                <div class="mb-2">
                                                    <input type="radio" class="me-2" value="${publisher.publisherName}" id="publisher${publisher.publisherName}" name="publisherName" 
                                                           <c:if test='${sessionScope.currentPublisherSelected == publisher.publisherName}'>checked</c:if>>
                                                    <label for="publisher${publisher.publisherName}">${publisher.publisherName}</label>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <script>
                                        function sendPublisherName() {
                                            var selectedPublisher = document.querySelector('input[name="publisherName"]:checked').value;
                                            var url = 'browse?action=bookList&selectedPublisher=' + encodeURIComponent(selectedPublisher);
                                            window.location.href = url;
                                        }

                                        var publisherRadioButtons = document.querySelectorAll('input[name="publisherName"]');
                                        publisherRadioButtons.forEach(function (radio) {
                                            radio.addEventListener('change', sendPublisherName);
                                        });
                                    </script>
                                </div>
                            </div>

                            <div class="col-lg-9">
                                <div class="row g-4 justify-content-center">
                                    <c:choose>
                                        <c:when test="${not empty noBook}">
                                            <div class="text-center" style="padding: 20px; ">
                                                <h2><img style="max-width: 20%" src="assets/Template2/images/npic.jpg" alt="thumbnail_not_found"><strong>${noBook}</strong></h2>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach items="${bookList}" var="bl">
                                                <div class="col-md-6 col-lg-6 col-xl-4">
                                                    <a href="browse?action=bookDetail&bookID=${bl.bookID}" class="no-style">
                                                        <div class="rounded position-relative fruite-item">
                                                            <div class="fruite-img">
                                                                <img src="assets/Template2/${bl.cover_imagePath}" class="img-fluid w-100 rounded-top" alt="">
                                                            </div>
                                                            <div class="text-white bg-secondary px-3 py-1 rounded position-absolute" style="top: 10px; left: 10px;">${bl.ISBN13}</div>
                                                            <div class="p-4 border border-secondary border-top-0 rounded-bottom">
                                                                <h4>${bl.title}</h4>
                                                                <p><c:choose>
                                                                        <c:when test="${fn:length(bl.description) <= 250}">
                                                                            ${bl.description}
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            ${fn:substring(bl.description, 0, 250)}...
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </p>
                                                                <div class="d-flex justify-content-between flex-lg-wrap">
                                                                    <c:choose>
                                                                        <c:when test="${bl.discount.discountPercent eq 0}">
                                                                            <p class="text-dark fs-5 fw-bold mb-0">${bl.price}$</p>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span style="color:gray; font-size: 0.9rem; text-decoration: line-through;" class="fs-5 fw-bold mb-0">${bl.price}$</span>
                                                                            <span style="color: red; font-size: 0.9rem;" class="mb-0">-${Math.round(bl.discount.discountPercent)}%</span>
                                                                            <p style="font-size: 0.9rem;" class="text-dark fs-5 fw-bold mb-0">${bl.price - bl.price*bl.discount.discountPercent/100}$</p>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                            <a href="#1" class="btn border border-secondary rounded-pill px-3 text-primary" style="align-content: center;"><i class="fa fa-shopping-bag me-2 text-primary"></i> Add to cart</a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </div>
                                            </c:forEach>

                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Fruits Shop End-->


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