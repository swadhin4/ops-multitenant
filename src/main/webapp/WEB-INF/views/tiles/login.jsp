<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>

<html ng-app="chrisApp">
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>
<head>
<title>OPS365 Application</title>

<link rel="icon" type="image/png" sizes="16x16" href='<c:url value="/resources/img/favicon/favicon-16x16.png"></c:url>' />

<%-- 
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/font-awesome-4.7.0/css/font-awesome.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/ripples.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/main.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/responsive.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/animate.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/style.css"></c:url>' />

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/login.css"></c:url>' />



<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/tether.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/ripples.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/material.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/wow.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.mmenu.min.all.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/count-to.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.inview.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/classie.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.nav.js"></c:url>'></script> --%>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap/css/bootstrap4.1.1.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/login.css"></c:url>' />
<%-- <script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery-2.1.4.min.js"></c:url>'></script> --%>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery-3.2.1.min.js"></c:url>'></script> 
<script type="text/javascript" 	src='<c:url value="/resources/theme1/css/bootstrap/js/bootstrap4.1.1.min.js"></c:url>'></script> 
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular.min.js"></c:url>'></script>
<%-- <script type="text/javascript" 	src='<c:url value="/resources/theme1/js/main.js"></c:url>'></script> --%>
<%-- <script type="text/javascript" 	src='<c:url value="/resources/theme1/js/mdb.min.js"></c:url>'></script> --%>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular-route.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jstorage.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/app-main.js"></c:url>'></script>
<%-- <script type="text/javascript" 	src='<c:url value="/resources/theme1/js/controller.js"></c:url>'></script> --%>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/login-controller.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-provider-service.js"></c:url>'></script>

<style type="text/css">

.nav nav-tabs>li>a {
  background-color: lightblue; 
  border-color: #777777;
  color:#fff;
}

.nav .nav-tab{
  box-shadow: 0 1px 3px rgba(0,0,0,.25);
}
/* .tab-content{
  padding:50px 0px 0px 8px;  
  border: 2px solid green;
  box-shadow: 0 1px 3px rgba(0,0,0,.25);
  margin-left:1px;
} */

/* .box-shadow(@shadow: 0 1px 3px rgba(0,0,0,.25)) {
  -webkit-box-shadow: @shadow;
     -moz-box-shadow: @shadow;
          box-shadow: @shadow;
} */

a, a:hover, a:active {
  color: blue;
}

@media (min-width: 1200px){

	.container {
	       width: 100%;
	}
}


.panel-default {
 opacity: 0.9;
 margin-top:30px;
}
.form-group.last {
 margin-bottom:0px;
}
</style>
</head>
<body>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
	<div  style="background-color: #fff;width:97%">
		<div  ng-controller="loginController">
		<div class="row">
		 <div class="container register" >
                <div class="row">
                    <div class="col-md-4 register-left">
                        <%--  <img src="${contextPath}/resources/theme1/img/logo_white.png" alt=""/><br> --%>
                         <img src="${contextPath}/resources/img/logo.png" alt="" style="width: 50%;"><br>
                 		 <img src="${contextPath}/resources/img/sigma.png" alt="" style="width: 60%;">
                       
                    </div>
                    <div class="col-md-8 register-right">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs">
                            <li class="active">
                                <a  href="#home" aria-expanded="true" class ="btn btn-success" data-toggle="tab"  >Registered User</a>
                            </li>
                            <li>
                                <a  href="#profile" aria-expanded="false" class ="btn btn-danger pull-right" data-toggle="tab" >External User</a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="home" >
                                <h3 class="register-heading">Login as Registered User </h3>
                                <div class="row register-form">
                                    <div class="col-md-12">
                                       <form name="loginForm" class="form-horizontal"  method="post" action="${contextPath}/j_spring_security_check">
					<div class="col-sm-12">
							<div class="col-sm-12 form-group">
								<span ng-show="loginForm.j_username.$invalid && !loginForm.j_username.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter a valid email id
					</span>
								<input type="email" ng-model="user.email"  name="j_username" 
								placeholder="Enter email here.." class="form-control" required>
							</div>
						<div class="col-sm-12 form-group">
								<span ng-show="loginForm.j_password.$invalid && !loginForm.j_password.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter the password
						</span>
							<input type="password" ng-model="user.password" name="j_password" placeholder="Enter password.." class="form-control" required>
						</div>	
						<div class="col-sm-12 form-group">
								<span ng-show="loginForm.j_password.$invalid && !loginForm.j_password.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Select User Type
						</span>
							<select type="select" name="usertype" id="usertype" class="form-control">
								<option value="0" selected>Select login type</option>
								<option value="1">Customer</option>
								<option value="2">Service Provider</option>
								
							</select>
											
					<label><a href="${contextPath}/forgot/password/page">Forgot Password ?</a></label>
						</div>
						<div class="col-sm-12 form-group">
						 <input type="submit" class="btn btn-lg btn-info" ng-disabled="loginForm.$invalid" value="Sign In"/><br/>
						 </div>
					</div>
					</form> 
					</div>
				
                                </div>
                            </div>
                            <div class="tab-pane" id="profile"  >
                                <h3  class="register-heading">Login as External SP  </h3>
                                <div class="row register-form">
                                    <div class="col-md-12">
                                   <%--  <c:if test="${user eq 0}">${message}</c:if> --%>
                                 <form name="extloginForm" class="form-horizontal"  method="post" action="${contextPath}/j_spring_security_check">
					<div class="col-sm-12">
							<div class="col-sm-12 form-group">
								<label><i class="fa fa-envelope" aria-hidden="true"></i> Username</label>
								<span ng-show="extloginForm.spemail.$invalid && !extloginForm.spemail.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter a valid username
					</span>
								<input type="text" ng-model="sp.email"  name="j_username" 
								placeholder="Enter username here.." class="form-control" required>
							</div>
						<div class="col-sm-12 form-group">
							<label><i class="fa fa-unlock-alt" aria-hidden="true"></i> Access Key</label>
								<span ng-show="extloginForm.accesscode.$invalid && !extloginForm.accesscode.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter the access code
					</span>
								<input type="password" ng-model="sp.accesscode" name="j_password" placeholder="Enter access code.." class="form-control" required>
								<input type="hidden" name="usertype" id="usertype" value="extsp" class="form-control">
						</div>	
						<div class="col-sm-12 form-group">
						 <input type="submit" class="btn btn-lg btn-info" ng-disabled="extloginForm.$invalid" value="Sign In"/><br/>
						 </div>
					</div>
				</form> 
                                    </div>
                                </div>
                            </div>
                        </div>
                        </div>
                    </div>
                </div>

            </div>
         </div>
		</div>
	</div>
      
</body>
<html>
