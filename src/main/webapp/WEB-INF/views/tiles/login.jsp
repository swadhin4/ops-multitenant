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

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/font-awesome-4.7.0/css/font-awesome.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/ripples.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/main.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/responsive.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/animate.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/style.css"></c:url>' />



<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery-2.1.4.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular.min.js"></c:url>'></script>
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
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery.nav.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/main.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/mdb.min.js"></c:url>'></script>
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
</style>
<script type="text/javascript">

/* $(document).ready(function(){
	  $("a[data-toggle='tab']").click(function(e){
	    $(".tab-content").css("border-color",$(this).css('backgroundColor'));
	  });
	}); */

</script>
</head>
<body>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<nav class="navbar navbar-default navbar-fixed-top">
      
       <div class="row" >
       <div class="container">
        <div class="navbar-header" >
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" 
          data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div id="navbar" class="navbar-collapse collapse" >
         <!-- <div class="row"> -->
         <div class="col-md-6" >
          	<a class="logo-left pull-left" href="#/">
                  <img src="${contextPath}/resources/img/logo.png" style="width: 11%;">
                  <img src="${contextPath}/resources/img/sigma.png" style="width: 20%;">
                  </a>
               </div>
          
          <!-- </div> -->
        </div><!--/.nav-collapse -->
        </div>
      </div>
    </nav>

<div id="page-content-wrapper" style="padding-top: 80px;height:100%">
	<div class="page-content" style="background-color: #f4f5f7;">
		<div class="container-fluid" ng-controller="loginController">
	<div class="row">	
			<div class="col-md-3">
			 <h3></h3>
			</div>
			
			<div class="col-md-4 col-sm-offset-1" style="margin-top: 21px;">
				<div class="alert alert-success alert-dismissable" id="profile-success-alert"
						style="display: none;  height: 34px;white-space: nowrap;">
						<strong>Success! </strong> {{successMessage}}
						<a href ng-click="closeMessageWindow()">	<span class="messageClose">X</span></a>
					</div>
				<div class="alert alert-danger alert-dismissable" id="profile-error-alert"
						style="display: none;  height: 34px;white-space: nowrap;">
						<strong>Error! </strong> {{successMessage}}
						<a href ng-click="closeMessageWindow()">	<span class="messageClose">X</span></a>
					</div>	
			</div>
			</div>
    <section class="content">
      <div class="row">  
      <div class="col-md-3 col-sm-3" style="height:87%">

          <!-- Profile Image -->
          <div class="box box-primary">
            <div class="box-body box-profile">
                        

              
            </div>
            
          </div>
         
        </div>      
        <!-- /.col -->
        <div class="col-md-5 col-sm-6" style="background-color:white;height:350px;"><br>
          <div class="nav-tabs-custom">
            <ul class="nav nav-tabs">
              <li class="active"><a href="#registereduser" data-toggle="tab">Registered User LogIn</a></li>              
              <li><a href="#externaluser" data-toggle="tab">External Service Provider LogIn</a></li>
            </ul>
            <div class="tab-content" >
              <div class="active tab-pane" style="background-color:lightblue;border-color:green;" id="registereduser">
                <form name="loginForm" class="form-horizontal"  method="post" action="${contextPath}/j_spring_security_check">
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
						</div>	
						<div class="col-sm-12 form-group">
							<label><i class="fa fa-unlock-alt" aria-hidden="true"></i> User Type</label>
								<span ng-show="loginForm.j_password.$invalid && !loginForm.j_password.$pristine"	
								class="errorMsg pull-right"> <i
						class="fa fa-exclamation-triangle" aria-hidden="true"></i>
						Select User Type
						</span>
							<select type="select" name="usertype" id="usertype" class="form-control">
								<option value="0" selected>Select User type</option>
								<option value="1">User</option>
								<option value="2">Service Provider</option>
							</select>
						</div>
						
					<button type="submit" class="btn btn-lg btn-info" ng-disabled="loginForm.$invalid">Sign in</button>	&nbsp;&nbsp;&nbsp;
					<label><a href="${contextPath}/forgot/password/page">Forgot Password ?</a></label>
					</div>
					</div>
				</form> 

                
              </div>
              
              <div class="tab-pane" id="externaluser">
                 <form name="extloginForm" class="form-horizontal"  method="post" action="${contextPath}/j_spring_security_check">
					<div class="col-sm-12">
						<div class="row">
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
								<input type="hidden" name="usertype" value="extsp" class="form-control">
						</div>	
						</div>
					<button type="submit" class="btn btn-lg btn-info" ng-disabled="extloginForm.$invalid">Sign in</button>	
					</div>
				</form> 
              </div>
              <!-- /.tab-pane -->
            </div>
            <!-- /.tab-content -->
          </div>
          <!-- /.nav-tabs-custom -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->

    </section>  
    	   
   	 	
				
		</div>
	</div>
	<div class="row ">
       <div class="container">
        <div class="navbar-footer">          
        </div>
        <div id="navbar" class="navbar-collapse collapse pull-left">
         <!-- <div class="row"> -->
         <div class="col-md-6">
          	
               </div>
          
          <!-- </div> -->
        </div><!--/.nav-collapse -->
        </div>
      </div>
</div>
</body>
<html>
