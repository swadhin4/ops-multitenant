<%@ page import="java.util.Date"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<title>Home</title>

<link rel="stylesheet"	href='<c:url value="/resources/bootstrap/css/bootstrap.min.css"></c:url>' />
<!-- Font Awesome -->
<link rel="stylesheet"	href='<c:url value="/resources/css/font-awesome-4.4.0/css/font-awesome.min.css"></c:url>' />
<!-- Ionicons -->
<link rel="stylesheet"	href='<c:url value="/resources/css/ionicons/css/ionicons.min.css"></c:url>' />
<!-- Theme style -->
<link rel="stylesheet"	href='<c:url value="/resources/dist/css/AdminLTE.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/dist/css/skins/skin-blue.min.css"></c:url>' />

<script	src='<c:url value="/resources/plugins/jQuery/jQuery-2.1.4.min.js"></c:url>'></script>
	
<style>
.taxclass{
font-size:12px
}
</style>
</head>
<body>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
 <section class="content"  style="padding-left:0px">     
	<div class="container">
		<div class="box">
			<div class="box-header with-border">
				<h3 class="box-title">Application APIs</h3>
				<div class="box-tools pull-right">
				</div>
			</div>
             <div class="box-body table-responsive no-padding">
              <table class="table table-hover">
               <tbody>
             	<c:forEach items="${handlerMethods}" var="entry">
             	 <tr>
             	    <td>
				      <div class="col-md-12">
				        <p>
				          <c:if test="${not empty entry.key.patternsCondition.patterns}">
				            ${entry.key.patternsCondition.patterns}
				          </c:if>
				        </p>
	      			  </div>
             	    </td>
      			 </tr> 
      		  </c:forEach>
      		  </tbody>
      		  </table>
            </div>
                            
			<div class="box-footer">
				<div class="row">
					<div class="col-sm-4 col-xs-6">
						<div class="description-block border-right">
						</div>
					</div>
					<!-- /.col -->
					<div class="col-sm-4 col-xs-6">
						<div class="description-block border-right">
						</div>
					</div>
					<!-- /.col -->
					<div class="col-sm-4 col-xs-6">
						<div class="description-block border-right">
						</div>
					</div>
					<!-- /.col -->
					<div class="col-sm-3 col-xs-6">
						<div class="description-block">
						</div>
					</div>
				</div>
			</div>
		  </div>
	  </div>
</section>
</body>
</html>