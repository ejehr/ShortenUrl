<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>shorten Url</title>
<script>

</script>
</head>
<body>
	<form id = "f1" name = "f1" action="/ShortUrl" method="post">
		<div>
			<span>실제 URL : 
				<input type="text" id="O_URL" name ="O_URL" style="width:400px;" value=" ${O_URL}"/>
				<input type="button" value="변환" onclick="submit();"/> 
			</span>
		</div>
		<div>
			<span>변경 URL : 
				<input type="text" id="S_URL" name ="S_URL" style="width:400px;" value=" ${S_URL}" readonly/>
			</span>
		</div>
	</form>
</body>
</html>