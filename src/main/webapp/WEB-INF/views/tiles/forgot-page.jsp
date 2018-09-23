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
<%-- <link rel="stylesheet"	href='<c:url value="/resources/theme1/css/main.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/responsive.css"></c:url>' /> --%>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/animate.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/dist/css/AdminLTE.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/style.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/dist/css/skins/skin-black-light.min.css"></c:url>' />

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
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/main.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/mdb.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular-route.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jstorage.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/app-main.js"></c:url>'></script>
<%-- <script type="text/javascript" 	src='<c:url value="/resources/theme1/js/controller.js"></c:url>'></script> --%>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/register-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/password-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<style>
	@media (min-width: 1200px){

.container {
       width: 85%;
}
}

#imageDiv { 
 background: url('/resources/img/mockup.jpg') no-repeat center center fixed; 
 -webkit-background-size: cover;
 -moz-background-size: cover;
 -o-background-size: cover;
 background-size: cover;
}

.panel-default {
 opacity: 0.9;
 margin-top:30px;
}
.form-group.last {
 margin-bottom:0px;
}
.errorMsg{
color:red;
}
</style>
<script>
var webContextPath="${pageContext.request.contextPath}";
var logging = false;
$(function(){
	$('#tog').click(function(){
		
		toggleLogs();
	});
	
	$('#stopLog').click(function(){
		logging=false;
		toggleLogs();
	});
	
	$('#refreshLog').click(function(){
		logging=true;
		refreshLog();
	});
});

   function refreshLog() {
    if (logging) {
    	//$('#logModal').modal('show');
     $.get(webContextPath+'/server/app/log', function(data) {
     	 $('#log').html(data);
     });
    }
   
   }

   function toggleLogs() {
    if (logging) {
     logging = false;
     $("#tog").val("Start");
    } else {
     logging = true;
     $("#tog").val("Stop");
     refreshLog();
    }
   }
 	
</script>
</head>
<body>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
  <nav class="navbar navbar-default navbar-fixed-top">
       <div class="row ">
       <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" 
          data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
         <div class="row">
         <div class="col-md-6">  
          	<a class="logo-left" href>
                  <img src="${contextPath}/resources/img/logo.png" style="width: 11%;">
                  <img src="${contextPath}/resources/img/sigma.png" style="width: 20%;">
                  </a>
            </div>
         	 
          <ul class="nav navbar-nav navbar-right" style="margin-top: 23px; ">
          <li ><a href  class="wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="400ms">Get Started<span class="sr-only">(current)</span></a></li>
            <li><a href  class="wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="400ms">Support</a></li>
            <li><a href="${contextPath}/login"
            class="wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="400ms">Login</a></li>
           <!--  <li><a href data-toggle="modal" data-target=".logModal" 
            class="wow fadeInUp" data-wow-duration="1000ms" data-wow-delay="400ms" id="tog" >Application Log</a></li> -->
          </ul>
          </div>
        </div><!--/.nav-collapse -->
        </div>
      </div>
    </nav>
     <div class="row" id="imageDiv">
			<div class="row">
			 <div class="panel panel-default">
			  <div class="panel-body">
			  		<div class="container text-center" style="margin-top: 14px;">
								   <div class="col-md-12" >
              <c:if test="${message ne null}">
          	 <div class="btn btn-danger"
				id="loginErrorDiv" 
				style="display: block; height: 34px; white-space: nowrap;">
				<!-- <button type="button" class="close" >x</button> -->
				<strong>Error! </strong> ${message} 
			</div>
			</c:if>
			</div>
          		</div>
          	</div>
         
          	</div>
          	</div>
          	   <div class="row">
          	    <div class="container">
          	     <div class="col-md-6">
            <div class="panel panel-default" ng-controller="passwordController">
              <form name="passwordRestForm" ng-submit="verifyCustomer(passwordRestForm)" novalidate>
                <div class="panel-heading"> <strong class="">Forgot your password? No worries, still you can create new password</strong>
                </div>
                <div class="panel-body">
	         		<!--  <div class="row">
							<div class="col-sm-12 form-group">
								<label><i class="fa fa-envelope" aria-hidden="true"></i> Username</label>
								<span ng-show="passwordRestForm.username.$invalid && !passwordRestForm.username.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter your username
					</span>
								<input type="text" ng-model="customer.username" 
								name="username" placeholder="Enter username here.." class="form-control" required>
							</div>
						</div> -->
						
						<div class="row">
							<div class="col-sm-12 form-group">
								<label><i class="fa fa-envelope" aria-hidden="true"></i> Email</label>
								<span ng-show="passwordRestForm.email.$invalid && !passwordRestForm.email.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter your email
					</span>
								<input type="email" ng-model="customer.email" 
								name="email" placeholder="Enter email here.." class="form-control" required>
							</div>
						</div>
						<div class="alert alert-success alert-dismissable"
							id="successMessageDiv" 
							style="display: none; height: 34px; white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Success! </strong> {{successMsg}} 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
						<div class="alert alert-dander alert-dismissable"
							id="infoMessageDiv" 
							style="display: none; height: 34px; white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Error! </strong> Something is wrong with the server. Email cannot be sent. 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
						</div>
				<div class="panel-footer">
					<div class="row">
					<div class="col-sm-12">
				    <input  type="submit" class="btn btn-md btn-info pull-left" ng-disabled="passwordRestForm.$invalid" value="SUBMIT"/>	
					<input type="reset" class="btn btn-md btn-default pull-left"	id="resetRegisterFormBtn" value="RESET"/>
					</div>
					</div>
			</div>
									</form>
									
                </div>
                </div>
                </div>
    </div>
      
    	  	 
    		<div class="row">
    	  	  <div class="panel panel-default">
                <div class="panel-body">
          	    <div class="col-md-4 col-sm-4" style="text-align: center;">
          	      <img src="${contextPath}/resources/img/incident.png" style="    width:30%;"><br>
          	       <h4>Simplify Incident Management Process</h4>
          	    </div>
          	     <div class="col-md-4 col-sm-4" style="text-align: center;" >
          	      <img src="${contextPath}/resources/img/site-asset.jpg" style="    width: 47%;">
          	      <br>
          	       <h4>Easily Manage your sites and assets</h4>
          	    </div>
          	    <div class="col-md-4 col-sm-4" style="text-align: center;">
          	      <img src="${contextPath}/resources/img/Performance.png" style="    width: 47%;"><br>
          	       <h4>Gain insights into Service Providers' Performance</h4>
          	    </div>
          	    </div>
          	    </div>
          	 </div>
    		</div>
    		
						 <div class="modal fade loginModal" id="loginModal">
		<div class="modal-dialog" style="width: 50%;">
			<div class="modal-content">
			<form name="loginForm"  method="post" action="${contextPath}/j_spring_security_check">
				<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">	<span aria-hidden="true">&times;</span>
			</button>
			<h1 class="modal-title">Sign in</h1>
			</div>
			 <div class="modal-body" style="background-color: #eee">
			 	<div class="row">
			 	<div class="col-md-12">
			 	<div class="box">
					<div class="box-body">
						 <div class="col-lg-12">
	         		 <div class="row">
					<div class="col-sm-12">
						<div class="row">
							<div class="col-sm-12 form-group">
								<label><i class="fa fa-envelope" aria-hidden="true"></i> Email</label>
								<span ng-show="loginForm.j_username.$invalid && !loginForm.j_username.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter a valid email id
					</span>
								<input type="email" ng-model="user.email"  name="j_username" 
								placeholder="Enter email here.." class="form-control" required>
							</div>
						<div class="col-sm-12 form-group">
							<label><i class="fa fa-unlock-alt" aria-hidden="true"></i> Password</label>
								<span ng-show="loginForm.j_password.$invalid && !loginForm.j_password.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter the password
						</span>
						<input type="password" ng-model="user.password" name="j_password" placeholder="Enter password.." class="form-control" required>
						<label><a href="${contextPath}/forget/password/page">Forgot Password</a></label>
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
				<div class="modal-footer">
				    <button type="submit" class="btn btn-lg btn-info pull-left" ng-disabled="loginForm.$invalid">Sign in</button>	
					<button type="button" class="btn btn-lg btn-default pull-left"	id="loginCloseBtn" data-dismiss="modal">Close</button>
					<!-- <button type="submit" class="btn btn-primary" ng-disabled="loginForm.$invalid">LOGIN</button>
					<button type="reset" id="resetServiceAssetForm" class="btn btn-primary">RESET</button> -->
			</div>
									</form>
									
									
								</div>
							</div>

						</div>
						
						
								 <div class="modal fade registerModal" id="registerModal">
		<div class="modal-dialog" style="width: 50%;">
			<div class="modal-content">
			<form name="registerForm" ng-submit="saveCustomer(registerForm)" novalidate>
				<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">	<span aria-hidden="true">&times;</span>
			</button>
			<h1 class="modal-title">New User Registration</h1>
			</div>
			 <div class="modal-body" style="background-color: #eee">
			 	<div class="row">
			 	<div class="col-md-12">
			 	<div class="box">
					<div class="box-body">
						 <div class="col-lg-12">
	         		 <div class="row">
					<div class="col-sm-12">
						<div class="row">
							<div class="col-sm-12 form-group">
								<label><i class="fa fa-envelope" aria-hidden="true"></i> Email</label>
								<span ng-show="registerForm.email.$invalid && !registerForm.email.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter a valid email id
					</span>
								<input type="email" ng-model="customer.email" 
								name="email" placeholder="Enter email here.." class="form-control" required>
							</div>
						<div class=" col-sm-12 form-group">
							<label><i class="fa fa-file-text-o" aria-hidden="true"></i> Message</label>
							<span ng-show="registerForm.mailcontent.$invalid && !registerForm.mailcontent.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Please enter your message
						</span>
							<textarea placeholder="Enter your message here.." rows="3" 
							ng-model="customer.message" name="mailcontent" class="form-control" required></textarea>
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
				<div class="modal-footer">
				    <button type="submit" class="btn btn-lg btn-info pull-left" ng-disabled="registerForm.$invalid">Submit</button>	
					<button type="button" class="btn btn-lg btn-default pull-left"	id="registerCloseBtn" data-dismiss="modal">Close</button>
			</div>
									</form>
									
									
								</div>
							</div>

						</div>
						
						
						     <div class="modal fade logModal" id="logModal" data-keyboard="false" data-backdrop="static">
	<div class="modal-dialog" style="width:82%;">
      <div class="modal-content">
        <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title"><span id="siteModalLabel">Application Log</span></h4>
			
		</div>
		 <div class="modal-body" style="background-color: #eee; overflow: auto;height:400px">
		 	<div id="log" style="width: 100%; padding: 0; margin: 0">
		 	
		 	</div>
		 </div>
		 <div class="modal-footer">
				 <input type="button" id="refreshLog" class="btn btn-warning" value="REFRESH">
					<input type="button" id="stopLog" class="btn btn-warning" value="STOP">
					
				</div>
		 </div>
		 </div>
		 </div>

</body>
<html>
