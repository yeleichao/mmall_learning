<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
<head>

</head>
<body>
<h2>Hello World!</h2>

sprimgmvc上传文件
<form name="form1" action="${pageContext.request.contextPath}/manager/product/upload.do" method="post" enctype="multipart/form-data">

    <input type="file" name="upload_file" />
    <input type="submit" name="上传文件" />

</form>

</body>
</html>
