<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<jsp:include page="menu.jsp"></jsp:include>

<form action="/LAB1_JAVA41_PHOI_FALL2025/tinhhieu" method="post" >
  <input name="canha" placeholder="canha?" value="${canha}" ></br>
  <input name="canhb" placeholder="canhb?" value="${canhb}"></br>
  
<button formaction="/LAB1_JAVA41_PHOI_FALL2025/chuvi">Tinh chuvi</button>
<button formaction="/LAB1_JAVA41_PHOI_FALL2025/dientich">Tinh dien tich</button>
<a href="/LAB1_JAVA4_PHOI_FALL2025">về trang chủ </a>

 <input type="submit" value="tinhhieu"/>
  <h1>chu vi là: ${cv}</h1>
    <h1>dien tich là: ${dt}</h1>
      <h1>hieu là: ${hieu}</h1>
</form>
</body>
</html>