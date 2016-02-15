<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type ="text/javascript">
function registerSSE(){
	var source = new EventSource('http://localhost:8080/GPMS/SendResponse');
	source.addEventListener('server-time', function(e) {
		document.getElementById('ticker').innerHTML=e.data;}, false);
}</script>
</head>
<body onload="registerSSE()">
<div id="ticker">[TIME]</div>

</body>
</html>