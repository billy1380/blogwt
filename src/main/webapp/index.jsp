<%@page import="com.google.gson.JsonArray"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page
	import="com.willshex.blogwt.server.service.property.IPropertyService"%>
<%@page import="com.willshex.blogwt.shared.api.datatype.Property"%>
<%@page import="com.willshex.blogwt.shared.api.helper.PropertyHelper"%>
<%@page
	import="com.willshex.blogwt.server.service.property.PropertyServiceProvider"%>
<%
	IPropertyService propertyService = PropertyServiceProvider
			.provide();

	Property title = null, extendedTitle = null, copyrightHolder = null, copyrightUrl = null;
	List<Property> properties = null;
	title = propertyService.getNamedProperty(PropertyHelper.TITLE);

	if (title != null) {
		extendedTitle = propertyService
				.getNamedProperty(PropertyHelper.EXTENDED_TITLE);
		copyrightHolder = propertyService
				.getNamedProperty(PropertyHelper.COPYRIGHT_HOLDER);
		copyrightUrl = propertyService
				.getNamedProperty(PropertyHelper.COPYRIGHT_URL);

		properties = Arrays.asList(new Property[] {
				title, extendedTitle, copyrightHolder, copyrightUrl });
		
		
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
<link href='https://fonts.googleapis.com/css?family=Noto+Sans'
	rel='stylesheet' type='text/css'>
<title><%=title == null ? "Blogwt" : title.value%></title>
<%

	if (properties != null) {
		out.println("<script type=\"text/javascript\" language=\"javascript\">");
		out.print("var properties='[");
		boolean first = true;
		for (Property property: properties) {
			if (first) {
				first = false;
			} else {
				out.print(",");
			}
			out.print(property.toString().replace("'", "\\'"));
		}
		out.println("]';");
		out.println("</script>");
	}

%>
<script type="text/javascript" language="javascript"
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
			Your web browser must have JavaScript enabled in order for this
			application to display correctly.</div>
	</noscript>

</body>
</html>