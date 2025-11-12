<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Result form!</title>
  </head>
  <body>
    
    <div class = "border border-info p-1 mx-auto col-7">
    <h1 style="text-align: center" class="text-danger">Thông tin đăng ký</h1>
    	<ul>
    		<li><span class="d-inline-block" style="width: 150px;">Tên đăng nhập</span>:<b>${username}</b></li>
    		<li><span class="d-inline-block" style="width: 150px;"> Mật khẩu</span>:<b>${password}</b></li>
    		<li><span class="d-inline-block" style="width: 150px;">Giới tính</span>:<b>${gioiTinh}</b></li>
    		<li><span class="d-inline-block" style="width: 150px;">Tình trạng hôn nhân</span>:<b>${married}</b></li>
    		<li><span class="d-inline-block" style="width: 150px;">Quốc tịch</span>:<b>${country}</b></li>
    		<li><span class="d-inline-block" style="width: 150px;">Sở thích</span>:<b>${favourites}</b></li>
    		<li><span class="d-inline-block" style="width: 150px;">Ghi chú</span>:<b>${ghiChu}</b></li>
    	</ul>
    </div>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
  </body>
</html>