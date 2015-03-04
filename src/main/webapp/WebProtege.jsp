<%@ page import="edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.WebProtegeProperties" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.ClientObjectWriter" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.ClientApplicationPropertiesEncoder" %>
<%@ page import="edu.stanford.bmir.protege.web.shared.user.UserId" %>
<%@ page import="edu.stanford.bmir.protege.web.server.app.UserInSessionEncoder" %>
<%@ page import="edu.stanford.bmir.protege.web.shared.user.UserDetails" %>
<%@ page import="edu.stanford.bmir.protege.web.shared.permissions.GroupId" %>
<%@ page import="edu.stanford.smi.protege.server.metaproject.MetaProject" %>
<%@ page import="edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager" %>
<%@ page import="edu.stanford.smi.protege.server.metaproject.User" %>
<%@ page import="edu.stanford.smi.protege.server.metaproject.Group" %>
<%@ page import="com.google.common.base.Optional" %>
<%@ page import="edu.stanford.bmir.protege.web.shared.app.UserInSession" %>
<%@ page import="com.google.common.collect.ImmutableList" %>
<%@ page import="edu.stanford.bmir.protege.web.server.session.WebProtegeSession" %>
<%@ page import="edu.stanford.bmir.protege.web.server.session.WebProtegeSessionImpl" %>
<%@ page import="edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector" %>
<%@ page import="java.io.IOException" %>
<!DOCTYPE html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>


    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Language" content="en-us">

    <title><%writeApplicationName(out);%></title>

    <link rel="stylesheet" href="js/ext/resources/css/ext-all.css" type="text/css">

    <link rel="stylesheet" href="css/WebProtege.css" type="text/css">


    <script>
        <%
            writeClientApplicationProperties(out);
        %>
    </script>

    <script>
        <%
            writeUserInSession(session, out);
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

<%!
    private void writeApplicationName(JspWriter out) throws IOException {
        WebProtegeProperties webProtegeProperties = WebProtegeInjector.get().getInstance(WebProtegeProperties.class);
        out.print(webProtegeProperties.getApplicationName());
    }

    private void writeClientApplicationProperties(JspWriter out) throws IOException {
        WebProtegeProperties webProtegeProperties = WebProtegeInjector.get().getInstance(WebProtegeProperties.class);
        ClientApplicationProperties props = webProtegeProperties.getClientApplicationProperties();
        ClientObjectWriter.get(
                "clientApplicationProperties",
                new ClientApplicationPropertiesEncoder()).writeVariableDeclaration(props, out);
    }

    private void writeUserInSession(HttpSession session, JspWriter out) {
        WebProtegeSession webProtegeSession = new WebProtegeSessionImpl(session);
        UserId userId = webProtegeSession.getUserInSession();
        final UserInSession userInSession;
        final ImmutableList.Builder<GroupId> builder = ImmutableList.builder();
        if(userId.isGuest()) {
            userInSession = new UserInSession(
                UserDetails.getGuestUserDetails(),
                ImmutableList.<GroupId>of()
            );
        }
        else {
            final MetaProject metaProject = MetaProjectManager.getManager().getMetaProject();
            User user = metaProject.getUser(userId.getUserName());
            for(Group group : user.getGroups()) {
                builder.add(GroupId.get(group.getName()));
            }
            userInSession = new UserInSession(
                UserDetails.getUserDetails(userId, userId.getUserName(), Optional.fromNullable(user.getEmail())),
                builder.build()
            );
        }
        ClientObjectWriter.get("userInSession", new UserInSessionEncoder())
                .writeVariableDeclaration(userInSession, out);
    }

%>