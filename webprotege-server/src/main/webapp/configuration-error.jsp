<%@ page import="edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
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
            if(exception instanceof WebProtegeConfigurationException) {
                final String message = exception.getMessage();
                out.println(message.replace("\n", "<br>"));
            }
            else {
                StringWriter sw = new StringWriter();
                sw.append("<pre>");
                exception.printStackTrace(new PrintWriter(sw));
                sw.append("</pre>");
                out.println(sw.toString().replace("\n", "<br>"));
            }

        %>
    </div>
</body>
</html>