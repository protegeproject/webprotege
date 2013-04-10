<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<html>
<head>
    <title>WebProtege Configuration Error</title>
</head>
<body style="font-family: arial,sans-serif;">
    <h1>WebProtege Initialization Error</h1>
    <p>An error occurred during the initialization of WebProtege which prevented WebProtege from starting.
    </p>
    <h2>Error Message</h2>
    <div style="color: maroon;">
        <%
            out.println(exception.getMessage());
        %>
    </div>
</body>
</html>