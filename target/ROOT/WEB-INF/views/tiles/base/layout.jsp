<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link rel="icon" type="image/png" sizes="16x16" href='<c:url value="/resources/img/favicon/favicon-16x16.png"></c:url>' />
<body class="hold-transition skin-blue sidebar-collapse sidebar-mini">
	<%-- <div class="wrapper" style="height: 90%; background-color:transparent;
    position: relative;
    overflow-x: hidden;
    overflow-y: auto;">
			<tiles:insertAttribute name="header" />
			<tiles:insertAttribute name="body" />
			<tiles:insertAttribute name="footer" />
	</div> --%>
	
	<div class="wrapper">
			<tiles:insertAttribute name="header" />
			<tiles:insertAttribute name="body" />
			<tiles:insertAttribute name="footer" />
	</div>

