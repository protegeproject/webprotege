<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<html>
<head>
    <title>WebProtege Configuration Error</title>
</head>
<body style="font-family: arial,sans-serif;">
    <p>A fatal error occurred during the initialization of WebProtege:
    </p>
    <div style="color: maroon;">
        <%
            out.println(exception.getMessage());
        %>
    </div>
</body>
</html>