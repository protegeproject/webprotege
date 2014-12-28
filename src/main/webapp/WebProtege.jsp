<%@ page import="edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.WebProtegeProperties" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.ClientObjectWriter" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.ClientApplicationPropertiesEncoder" %>
<!DOCTYPE html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>


    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Language" content="en-us">

    <!-- The title is copied in by maven -->
    <title>${application.name}</title>

    <link rel="stylesheet" href="js/ext/resources/css/ext-all.css" type="text/css">

    <link rel="stylesheet" href="css/WebProtege.css" type="text/css">


    <script>
        <%
            ClientApplicationProperties props = WebProtegeProperties.get().getClientApplicationProperties();
            ClientObjectWriter<ClientApplicationProperties> writer =
                    new ClientObjectWriter<ClientApplicationProperties>("clientApplicationProperties", new ClientApplicationPropertiesEncoder());
            writer.writeVariableDeclaration(props, out);
        %>
    </script>

    <script type="text/javascript" language="javascript" src="webprotege/webprotege.nocache.js"></script>

    <script type="text/javascript" language="javascript" src="js/ext/adapter/ext/ext-base.js"></script>

    <script type="text/javascript" language="javascript" src="js/ext/ext-all.js"></script>



    ${application.analytics}
</head>

<body>
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
<iframe src="" id="__download" style="width:0;height:0;border:0"></iframe>
</body>
</html>

