<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/profile-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>

<style type="text/css">

.reqDiv.required .control-label:after {
  content:"*";
  color:red;
}
.messageClose{
	background-color: #000;
    padding: 8px 8px 10px;
    position: relative;
    left: 8px;
}

</style>
</head>

<div class="content-wrapper">
		<div  ng-controller="profileController" id="profileviewWindow">
	<div class="row">
	<div class="col-md-12">
			<div class="col-md-3">
			 <h3> User Profile</h3>
			</div>
			<div class="col-md-4 col-sm-offset-1" style="margin-top: 21px;">
				<div class="alert alert-success alert-dismissable" id="profile-success-alert"
						style="display: none;  height: 34px;white-space: nowrap;">
						<strong>Success! </strong> {{successMessage}}
						<a href ng-click="closeMessageWindow()">	<span class="messageClose">X</span></a>
					</div>
				<div class="alert alert-danger alert-dismissable" id="profile-error-alert"
						style="display: none;  height: 34px;white-space: nowrap;">
						<strong>Error! </strong> {{errorMessage}}
						<a href ng-click="closeMessageWindow()">	<span class="messageClose">X</span></a>
					</div>	
			</div>
			</div>
    <section class="content">
      <div class="row">
        <div class="col-md-3 col-sm-3" style="height:100%">

          <!-- Profile Image -->
          <div class="box box-primary">
            <div class="box-body box-profile">
              <img class="profile-user-img img-responsive img-circle" src="${contextPath}/resources/img/swadhin.jpg" alt="User profile picture">

              <h3 class="profile-username text-center">{{loggedInUserDetail.firstName}} {{loggedInUserDetail.lastName}}</h3>
              
              <p class="text-muted text-center">{{loggedInUserDetail.company.companyName}}</p> 
              <p class="text-muted text-center">{{loggedInUserDetail.role.description}}</p>           

              
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->

          <!-- About Me Box -->
          
          <!-- /.box -->
        </div>
        <!-- /.col -->
        <div class="col-md-9 col-sm-9">
          <div class="nav-tabs-custom">
            <ul class="nav nav-tabs">
              <li class="active"><a href="#activity" data-toggle="tab">Update Profile</a></li>              
              <li><a href="#settings" data-toggle="tab">Security Settings</a></li>
            </ul>
            <div class="tab-content">
              <div class="active tab-pane" id="activity">
                <form class="form-horizontal" name="profileForm" ng-submit="updateProfile()">
                  <div class="form-group reqDiv required">
                    <label  class="col-sm-2 control-label">First Name</label>

                    <div class="col-sm-10">
                      <input type="text" class="form-control" id="inputName" 
                      ng-model="loggedInUserDetail.firstName" placeholder="Enter First Name" required>
                    </div>
                  </div>
                  <div class="form-group reqDiv required">
                    <label for="inputName" class="col-sm-2 control-label">Last Name</label>

                    <div class="col-sm-10">
                      <input type="text" class="form-control" id="inputName" 
                      ng-model="loggedInUserDetail.lastName" placeholder="Enter Last Name" required>
                    </div>
                  </div>
                  <div class="form-group reqDiv required">
                    <label for="inputName" class="col-sm-2 control-label">Phone / Mobile</label>

                    <div class="col-sm-10">
                      <input type="text" class="form-control" ng-pattern="onlyNumbers" ng-keypress="filterValue($event)"  maxlength="11" 
                      id="inputName" ng-model="loggedInUserDetail.phoneNo" placeholder="Enter Phone Number" required>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputEmail" class="col-sm-2 control-label">Email</label>

                    <div class="col-sm-10">
                      <input type="email" class="form-control" id="inputEmail" 
                      ng-model="loggedInUserDetail.email" disabled="disabled">
                    </div>
                  </div>
                  
                  
                  <div class="form-group">
                    <label for="inputSkills" class="col-sm-2 control-label">Role</label>

                    <div class="col-sm-10">
                      <input type="text" class="form-control" id="inputrole" 
                      ng-model="loggedInUserDetail.role.description" disabled="disabled">
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                      <button type="submit" class="btn btn-danger" ng-disabled="profileForm.$invalid">Update Profile</button>
                     
                    </div>
                  </div>
                </form>

                
              </div>
              <!-- /.tab-pane -->
              
              <!-- /.tab-pane -->

              <div class="tab-pane" id="settings">
                <form class="form-horizontal" name="securityForm" ng-submit="updatePassword($securityForm)">
                  <div class="form-group reqDiv required">
                    <label for="inputName" class="col-sm-3 control-label pull-left">Old Password</label>

                    <div class="col-sm-7">
                      <input type="password" name="Password" class="form-control" 
                      id="inputpassword" ng-model="passwordDetail.oldPassword" placeholder="Enter Old Password" required>
                      
                    </div>
                  </div>
                  <div class="form-group reqDiv required">
                    <label   class="col-sm-3 control-label pull-left">New Password</label>

                    <div class="col-sm-7">
                      <input type="password" name="newpassword" ng-minlength="6" class="form-control" 
                      id="inputnewpassword" ng-model="passwordDetail.newPassword" placeholder="Enter New Password" required>
                      <span style="color:red" ng-show="securityForm.newpassword.$error.minlength">
                      Password Should Contain atleast 6 Characters</span>
                    </div>
                  </div>
                  <div class="form-group reqDiv required">
                    <label for="inputName" class="col-sm-3 control-label pull-left">Confirm Password</label>

                    <div class="col-sm-7">
                      <input type="password" name="confirmpassword" class="form-control" id="inputconfirmpassword" ng-model="passwordDetail.confirmPassword"
                      data-valid-password-c="{{passwordDetail.newPassword === passwordDetail.confirmPassword}}" placeholder="Confirm New Password" required>
                      <span style="color:red" ng-show="passwordDetail.newPassword != passwordDetail.confirmPassword">Confirm password should be same as new password.</span>
                    </div>
                  </div> 
                  
                  <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-7">
                      <button type="submit" ng-disabled="securityForm.$invalid" class="btn btn-danger">UPDATE PASSWORD</button>
                      <input  type="reset" class="btn btn-danger" value="RESET">
                    </div>
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
    
    	   
   	 	<div id="passwordConfirmModal" class="modal fade passwordConfirmModal" data-keyboard="false" data-backdrop="static">
			<div class="modal-dialog" style="width: 30%">
				<div class="modal-content">
				<div class="box-header" style="background-color: rgba(0, 129, 239, 0.2);">
				<h3 class="box-title" id="modaltitle">Password change confirmation</h3>
					<a href="${webContextPath}/logout"  data-dismiss="modal" aria-hidden="true" class="close">X</a>
					
				</div>
				<div class="modal-body">
				<p>
					You have successfully changed your password. Please relogin using the new password.
				</p>
				</div>
				<div class="box-footer">
					<a href="${webContextPath}/logout" class="btn btn-primary">RE LOGIN</a>
				</div>
				</div>
			</div>
			
		</div>
				
		</div>
	</div>	
	</div>
