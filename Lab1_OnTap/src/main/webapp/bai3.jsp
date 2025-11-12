<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!doctype html>
<html lang="vi">
<head>
<meta charset="utf-8">
<title>Đăng Ký</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        padding: 20px;
    }
    .container {
        width: 500px;
        margin: 0 auto;
        background: #fff;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 5px;
    }
    h1 {
        text-align: center;
        color: #007bff;
    }
    .form-group {
        margin-bottom: 15px;
    }
    label {
        font-weight: bold;
    }
    input[type="text"], input[type="password"], select, textarea {
        width: 100%;
        padding: 8px;
        margin-top: 5px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }
    .form-inline {
        display: flex;
        align-items: center;
        margin-top: 5px;
    }
    .form-inline input {
        margin-right: 5px;
    }
    button {
        background-color: orange;
        border: none;
        padding: 10px 20px;
        color: #fff;
        cursor: pointer;
        border-radius: 4px;
    }
    button:hover {
        background-color: darkorange;
    }
    a {
        text-decoration: none;
        color: #007bff;
    }
</style>
</head>
<body>

<jsp:include page="menu.jsp"></jsp:include>

<div class="container">
    <h1>Đăng Ký</h1>
    <form action="" method="POST">
        <div class="form-group">
            <label for="username">Tên đăng nhập:</label>
            <input id="username" type="text" name="username" />
        </div>

        <div class="form-group">
            <label for="password">Mật khẩu:</label>
            <input type="password" id="password" name="password" />
        </div>

        <div class="form-group">
            <label>Giới tính:</label>
            <div class="form-inline">
                <input type="radio" id="male" name="gioiTinh" value="Nam" />
                <label for="male">Nam</label>
            </div>
            <div class="form-inline">
                <input type="radio" id="female" name="gioiTinh" value="Nữ" />
                <label for="female">Nữ</label>
            </div>
        </div>

        <div class="form-group">
            <label for="married">Tình trạng:</label>
            <div class="form-inline">
                <input type="checkbox" id="married" name="married" value="Lập gia đình" />
                <label for="married">Đã có gia đình chưa?</label>
            </div>
        </div>

        <div class="form-group">
            <label for="country">Quốc tịch:</label>
            <select name="country" id="country">
                <option value="Việt Nam">Việt Nam</option>
                <option value="Japan">Japan</option>
                <option value="USA">USA</option>
                <option value="Korea">Korea</option>
            </select>
        </div>

        <div class="form-group">
            <label>Sở thích:</label>
            <div class="form-inline">
                <input type="checkbox" name="soThich" value="Đọc sách" />
                <label>Đọc sách</label>
            </div>
            <div class="form-inline">
                <input type="checkbox" name="soThich" value="Du lịch" />
                <label>Du lịch</label>
            </div>
            <div class="form-inline">
                <input type="checkbox" name="soThich" value="Cờ bạc" />
                <label>Cờ bạc</label>
            </div>
            <div class="form-inline">
                <input type="checkbox" name="soThich" value="Xem phim" />
                <label>Xem phim</label>
            </div>
        </div>

        <div class="form-group">
            <label for="ghiChu">Ghi chú:</label>
            <textarea name="ghiChu" id="ghiChu" rows="3"></textarea>
        </div>
        <hr>
        <button formaction="/LAB1_JAVA41_PHOI_FALL2025/bai3">Đăng ký</button>
    </form>

    <br>
    <a href="/LAB1_JAVA41_PHOI_FALL2025">Về trang chủ</a>
</div>

</body>
</html>
