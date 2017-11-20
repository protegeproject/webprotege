<%@ page import="edu.stanford.bmir.protege.web.server.access.AccessManager" %>
<%@ page import="edu.stanford.bmir.protege.web.server.access.ApplicationResource" %>
<%@ page import="edu.stanford.bmir.protege.web.server.access.Subject" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.ClientObjectWriter" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.UserInSessionEncoder" %>
<%@ page import="edu.stanford.bmir.protege.web.server.inject.ApplicationComponent" %>
<%@ page import="edu.stanford.bmir.protege.web.server.session.WebProtegeSession" %>
<%@ page import="edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl" %>
<%@ page import="edu.stanford.bmir.protege.web.server.user.UserDetailsManager" %>
<%@ page import="edu.stanford.bmir.protege.web.shared.access.ActionId" %>
<%@ page import="edu.stanford.bmir.protege.web.shared.app.UserInSession" %>
<%@ page import="edu.stanford.bmir.protege.web.shared.user.UserDetails" %>
<%@ page import="edu.stanford.bmir.protege.web.shared.user.UserId" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.Optional" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="edu.stanford.bmir.protege.web.server.filemanager.FileContents" %>
<%@ page import="edu.stanford.bmir.protege.web.server.filemanager.StyleCustomizationFileManager" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<!DOCTYPE html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>


    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Language" content="<%= request.getLocale() %>">

    <meta name="gwt:property" content="locale=<%= request.getLocale() %>">

    <title><%writeApplicationName(out);%></title>

    <link rel="shortcut icon" type="image/x-icon" href="favicon.ico" />

    <link rel="stylesheet" href="js/ext/resources/css/ext-all.css" type="text/css">


    <script type="text/javascript" language="javascript" src="js/ext/adapter/ext/ext-base.js"></script>

    <script type="text/javascript" language="javascript" src="js/ext/ext-all.js"></script>



    <link rel="stylesheet" href="css/WebProtege.css" type="text/css">

    <style>
        <%
            injectStyleCustomization(out);
        %>
    </style>

    <script>
        <%
            writeUserInSession(session, out);
        %>
    </script>


    <script type="text/javascript" language="javascript" src="webprotege/webprotege.nocache.js"></script>

    ${application.analytics}
</head>

<body>
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
<iframe src="" id="__download" style="width:0;height:0;border:0"></iframe>
</body>
</html>

<%!
    private final StyleCustomizationFileManager styleCustomizationFileManager = new StyleCustomizationFileManager();

    private ApplicationComponent getWebProtegeComponent() {
        ServletContext context = getServletConfig().getServletContext();
        return (ApplicationComponent) context.getAttribute(ApplicationComponent.class.getName());
    }

    private void writeApplicationName(JspWriter out) throws IOException {
        ApplicationComponent component = getWebProtegeComponent();
        out.print(component.getApplicationNameProvider().get());
    }

    private AccessManager getAccessManager() {
        ApplicationComponent component = getWebProtegeComponent();
        return component.getAccessManager();
    }

    private void injectStyleCustomization(JspWriter writer) throws IOException {
        String styleCustomization = getStyleCustomization();
        if(!styleCustomization.isEmpty()) {
            writer.println();
            writer.println("/* Custom styles that override the default WebProtege styles */");
            writer.println();
            writer.println(styleCustomization);
            writer.println();
            writer.println("/* End of custom styles */");
            writer.println();
        }
    }

    private String getStyleCustomization() {
        return styleCustomizationFileManager.getStyleCustomization();
    }

    private void writeUserInSession(HttpSession session, JspWriter out) {
        WebProtegeSession webProtegeSession = new WebProtegeSessionImpl(session);
        UserId userId = webProtegeSession.getUserInSession();
        final UserInSession userInSession;
        final UserDetails userDetails;
        if(userId.isGuest()) {
            userDetails = UserDetails.getGuestUserDetails();
        }
        else {
            UserDetailsManager userDetailsManager = getWebProtegeComponent().getUserDetailsManager();
            Optional<String> email = userDetailsManager.getEmail(userId);
            if(email.isPresent()) {
                userDetails = UserDetails.getUserDetails(userId, userId.getUserName(), Optional.of(email.get()));
            }
            else {
                userDetails = UserDetails.getUserDetails(userId, userId.getUserName(), Optional.<String>empty());
            }
        }
        Set<ActionId> allowedApplicationActions = new HashSet<ActionId>(getAccessManager().getActionClosure(Subject.forUser(userId), ApplicationResource.get()));
        userInSession = new UserInSession(userDetails, allowedApplicationActions);
        ClientObjectWriter.get("userInSession", new UserInSessionEncoder())
                .writeVariableDeclaration(userInSession, out);
    }
%>