<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>


<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/font-awesome-4.7.0/css/font-awesome.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/ripples.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/main.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/responsive.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/animate.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/style.css"></c:url>' />
<jsp:useBean id="random1" class="java.util.UUID" scope="application" />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery-2.1.4.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/tether.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/ripples.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/material.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/wow.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.mmenu.min.all.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/count-to.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.inview.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/classie.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.nav.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/smooth-on-scroll.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/smooth-scroll.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/main.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/mdb.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular-route.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jstorage.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/app-main.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/register-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
</head>
<body>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
 	<div class="content-wrap">	
 	 <header class="hero-area" id="home">
 		<div class="container" style="margin-top: -80px;">
          <div class="col-md-12">
            <div class="navbar navbar-inverse sticky-navigation navbar-fixed-top" role="navigation" data-spy="affix" data-offset-top="200">
              <div class="container">
                <div class="navbar-header">
                 <a class="logo-left " href="#/">
                  <img src="${contextPath}/resources/img/logo.png" style="width: 9%;">
                  <img src="${contextPath}/resources/img/sigma.png" style="width: 9%;">
                  </a>
                </div>
              </div>
            </div>
            </div>
            </div> 
           
            <div class="contents text-left">
             <div class="container" ng-controller="registerController" style="margin-top: 80px;">
				 <div class="col-lg-6 well">
				  <span style="color:green" ng-if="responseMessage.color=='green'"><h5>{{responseMessage.info}}</h5></span>
				   <span style="color:green" ng-if="responseMessage.color=='red'"><h5>{{responseMessage.info}}</h5></span>
				 <h1>New User Registration</h1>
					<div class="row">
				<form name="registerForm" ng-submit="saveCustomer(registerForm)" novalidate>
					<div class="col-sm-12">
						<div class="row">
							<div class="col-sm-12 form-group">
								<label><i class="fa fa-envelope" aria-hidden="true"></i> Email</label>
								<span ng-show="registerForm.email.$invalid && !registerForm.email.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter a valid email id
					</span>
								<input type="email" ng-model="customer.email" name="email" placeholder="Enter email here.." class="form-control" required>
							</div>
						</div>					
						<div class="form-group">
							<label><i class="fa fa-file-text-o" aria-hidden="true"></i> Message</label>
							<span ng-show="registerForm.mailcontent.$invalid && !registerForm.mailcontent.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter your message
					</span>
							<textarea placeholder="Enter your message here.." rows="3" 
							ng-model="customer.message" name="mailcontent" class="form-control" required></textarea>
						</div>	
					<button type="submit" class="btn btn-lg btn-info" ng-disabled="registerForm.$invalid">Submit</button>	
					</div>
				</form> 
				</div>
		 </div>
 </div>
 </div>
 </header>
 </div>



</body>
<html>
