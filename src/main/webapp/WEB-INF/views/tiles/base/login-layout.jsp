<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="icon" type="image/png" sizes="16x16" href='<c:url value="/resources/img/favicon/favicon-16x16.png"></c:url>' />
<title>OPS365 Application</title>
</head>

<body>
	<tiles:insertAttribute name="body" />
</body>
</html>