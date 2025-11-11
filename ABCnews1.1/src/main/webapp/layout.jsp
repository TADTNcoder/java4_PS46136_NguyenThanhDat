<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>News Portal</title>

    <!-- ✅ Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- ✅ Custom CSS -->
    <link rel="stylesheet" href="<c:url value='/assets/css/style.css'/>" />

    <style>
        /* ==== RESET ==== */
        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
            line-height: 1.6;
        }

        /* ✅ khoảng cách tránh navbar */
        .container {
            padding-top: calc(80px + 2rem);
        }

        /* ==== NEWS CARD ==== */
        .news-card {
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            overflow: hidden;
            transition: transform 0.2s ease-in-out;
        }

        .news-card:hover {
            transform: translateY(-5px);
        }

        .news-card img {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }

        .news-card .card-body {
            padding: 1.2rem;
        }

        .news-card h5 {
            font-size: 1.2rem;
            font-weight: 600;
            margin-bottom: 0.8rem;
        }

        /* ==== FOOTER ==== */
        footer {
            background: #212529;
            color: #adb5bd;
            text-align: center;
            padding: 1rem 0;
            margin-top: 4rem;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>

    <!-- ✅ Header -->
    <jsp:include page="/views/partials/header.jsp" />

    <!-- ✅ Main -->
    <div class="container">
        <jsp:include page="${contentPage}" />
    </div>

    <!-- ✅ Footer -->
    <jsp:include page="/views/partials/footer.jsp" />

    <!-- ✅ JS đặt cuối -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="<c:url value='/assets/js/script.js'/>"></script>
</body>
</html>
