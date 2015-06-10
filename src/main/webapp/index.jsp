<%@page import="com.willshex.blogwt.server.service.PersistenceService"%>
<%@page
	import="com.willshex.blogwt.server.service.permission.PermissionServiceProvider"%>
<%@page import="com.willshex.blogwt.shared.api.datatype.Permission"%>
<%@page import="com.willshex.blogwt.shared.api.datatype.Role"%>
<%@page import="com.googlecode.objectify.Key"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.willshex.blogwt.server.service.role.RoleServiceProvider"%>
<%@page import="com.willshex.blogwt.server.service.user.UserServiceProvider"%>
<%@page
	import="com.willshex.blogwt.server.service.session.SessionServiceProvider"%>
<%@page import="com.willshex.blogwt.shared.api.datatype.Session"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.willshex.blogwt.server.service.property.IPropertyService"%>
<%@page import="com.willshex.blogwt.shared.api.datatype.Property"%>
<%@page import="com.willshex.blogwt.shared.api.helper.PropertyHelper"%>
<%@page
	import="com.willshex.blogwt.server.service.property.PropertyServiceProvider"%>
<%
	IPropertyService propertyService = PropertyServiceProvider.provide();

	Property title = null, extendedTitle = null, copyrightHolder = null, copyrightUrl = null;
	List<Property> properties = null;
	title = propertyService.getNamedProperty(PropertyHelper.TITLE);
	Session userSession = null;

	if (title != null) {
		extendedTitle = propertyService.getNamedProperty(PropertyHelper.EXTENDED_TITLE);
		copyrightHolder = propertyService.getNamedProperty(PropertyHelper.COPYRIGHT_HOLDER);
		copyrightUrl = propertyService.getNamedProperty(PropertyHelper.COPYRIGHT_URL);

		properties = Arrays.asList(new Property[] { title, extendedTitle, copyrightHolder, copyrightUrl });

		Cookie[] cookies = request.getCookies();
		String sessionId = null;

		if (cookies != null) {
			for (Cookie currentCookie : cookies) {
				if ("session.id".equals(currentCookie.getName())) {
					sessionId = currentCookie.getValue();
				}
			}

			if (sessionId != null) {
				userSession = SessionServiceProvider.provide().getSession(Long.valueOf(sessionId));

				if (userSession != null) {
					userSession.user = UserServiceProvider.provide().getUser(userSession.userKey.getId());

					if (userSession.user.roleKeys != null) {
						userSession.user.roles = RoleServiceProvider.provide().getIdRolesBatch(PersistenceService.keysToIds(userSession.user.roleKeys));
					}

					if (userSession.user.permissionKeys != null) {
						userSession.user.permissions = PermissionServiceProvider.provide().getIdPermissionsBatch(
								PersistenceService.keysToIds(userSession.user.permissionKeys));
					}
				}
			}
		}
	}
%>
<!doctype html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link href='https://fonts.googleapis.com/css?family=Noto+Sans' rel='stylesheet'
	type='text/css'>
<link rel="icon" href="favicon.ico" type="image/x-icon">
<title><%=title == null ? "Blogwt" : title.value%></title>
<%
	if (properties != null) {
		out.println("<script type=\"text/javascript\" language=\"javascript\">");
		out.print("var properties='[");
		boolean first = true;
		for (Property property : properties) {
			if (first) {
				first = false;
			} else {
				out.print(",");
			}
			out.print(property.toString().replace("'", "\\'"));
		}
		out.println("]';");

		if (userSession != null) {
			out.println("var session='" + userSession.toString().replace("'", "\\'") + "';");
		}
		out.println("</script>");
	}
%>
<script type="text/javascript" <%="language=\"javascript\""%>
	src="blogwt/blogwt.nocache.js"></script>
</head>
<body>
	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>
	<iframe src="javascript:''" name="login" style="display: none"></iframe>
	<form id="login-form" style="display: none" action="" target="login">
		<input type="text" id="login-username"><input type="password"
			id="login-password">
	</form>
	<noscript>
		<div
			style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
			Your web browser must have JavaScript enabled in order for this application
			to display correctly.</div>
	</noscript>
</body>
</html>