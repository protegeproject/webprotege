<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true"%>
<html>
<head>
    <title>WebProtege Configuration Error</title>
</head>
<body style="font-family: arial,sans-serif; color: #464646; font-size: 14px;">
    <h1>WebProtege Initialization Error</h1>
    <p>An error occurred during the initialization of WebProtege which prevented WebProtege from starting.
    </p>
    <h2>Error Message</h2>
    <div style="color: #af0002;">
        <%
            final String message = exception.getMessage();
            out.println(message.replace("\n", "<br>"));
        %>
    </div>
</body>
</html>