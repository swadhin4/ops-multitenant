<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/user-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<style>
	.main-box.no-header {
    padding-top: 20px;
}

.currentSelected{
	background:rgba(60, 141, 188, 0.58);
    color:#fff;
}
.currentSelected a{
	color:#fff
}
.main-box {
    background: #FFFFFF;
    -webkit-box-shadow: 1px 1px 2px 0 #CCCCCC;
    -moz-box-shadow: 1px 1px 2px 0 #CCCCCC;
    -o-box-shadow: 1px 1px 2px 0 #CCCCCC;
    -ms-box-shadow: 1px 1px 2px 0 #CCCCCC;
    box-shadow: 1px 1px 2px 0 #CCCCCC;
    margin-bottom: 16px;
    -webikt-border-radius: 3px;
    -moz-border-radius: 3px;
    border-radius: 3px;
}
.table a.table-link.danger {
    color: #e74c3c;
}
.label {
    border-radius: 3px;
    font-size: 0.875em;
    font-weight: 600;
}
.user-list tbody td .user-subhead {
    font-size: 0.875em;
    font-style: italic;
}
.user-list tbody td .user-link {
    display: block;
    font-size: 1.25em;
    padding-top: 3px;
    margin-left: 60px;
}
a {
    color: #3498db;
    outline: none!important;
}
.user-list tbody td>img {
    position: relative;
    max-width: 50px;
    float: left;
    margin-right: 15px;
}

.table thead tr th {
    text-transform: uppercase;
    font-size: 0.875em;
}
.table thead tr th {
    border-bottom: 2px solid #e7ebee;
}
.table tbody tr td:first-child {
    font-size: 1.125em;
    font-weight: 300;
}
.table tbody tr td {
    font-size: 0.875em;
    vertical-align: middle;
    border-top: 1px solid #e7ebee;
    padding: 12px 8px;
}
#errorMessageDiv, #successMessageDiv{
    top: 0%;
    /* left: 50%; */
   /*  width: 45em; */
    height: 3em;
    margin-top: 4em;
   /*  margin-left: -15em; */
    border: 1px solid #ccc;
    background-color: #fff;
    position: fixed;
}
 #modalMessageDiv{
   top: -7%;
    left: 47%;
    /* width: 45em; */
    height: 3em;
    margin-top: 4em;
    margin-left: -12em;
    border: 1px solid #ccc;
    background-color: #fff;
    position: fixed;
    }
.messageClose{
	background-color: #000;
    padding: 8px 8px 10px;
    position: relative;
    left: 8px;
}

.reqDiv.required .control-label:after {
  content:"*";
  color:red;
}
  
</style>
<script>
$(function() {
	 $('.toggle-on').removeAttr('style');
	 $('.toggle-off').removeAttr('style');
  })
</script>
</head>
<div class="content-wrapper">
		<div  ng-controller="userController" id="userWindow">
		<div style="display:none" id="loadingDiv"><div class="loader">Loading...</div></div>
			<section class="content" style="min-height:35px;display:none" id="messageWindow">
				<div class="row">
					<div class="col-md-6 col-sm-offset-4">
						<div class="alert alert-success alert-dismissable" id="successMessageDiv"
							style="display: none;  height: 34px;white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Success! </strong> {{successMessage}} <a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
						<div class="alert alert-danger alert-dismissable" id="errorMessageDiv"
							style="display: none;  height: 34px;white-space: nowrap;">
							<!-- <button type="button" class="close">x</button> -->
							<strong>Error! </strong> {{errorMessage}} <span class="messageClose" ng-click="closeMessageWindow()">X</span>
						</div>
					</div>
				</div>
			</section>	
			<section class="content">
				<div class="row">
				<div class="col-md-6">
						<div class="box" >
							<div class="box-header with-border">
								<h3 class="box-title">List of Users</h3>
								<div class="box-tools pull-right">
								 	<button type="button" class="btn btn-box-tool">
										<a href  class="btn btn-success" ng-click="addNewUser()" style="    margin-top: -8px;"><i class="fa fa-user-plus" aria-hidden="true"></i> Add User</a>
									</button>
									<!-- <button type="button" class="btn btn-box-tool" >
										<a href class="btn btn-info"><i class="fa fa-download" arial-hidden="true"></i> Download </a>
									</button> -->
									<!--<div class="btn-group">
										<button type="button" class="btn btn-box-tool dropdown-toggle"
											data-toggle="dropdown">
											<i class="fa fa-wrench"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
										</ul>
									</div>
									 -->
								</div>
							</div>
							<div class="box-body" style="height:440px">
								<div class="row">
	 								<div class="col-md-12">
										<input type="text" class="form-control"
											placeholder="Search User" ng-model="searchUser">
									 </div>
										<div class="col-md-12">
											<div style="overflow: auto; height: 383px">
											<table id="example1" 
												class="table table-bordered">
												<thead>
													<tr>
														<th width="40%">Name</th>
														<th>Role</th>
													</tr>
												</thead>
												<tbody>
													<tr ng-repeat="user in appUsers | filter: searchUser" 
													ng-click="getUserDetail(user);rowHighilited($index)"
													ng-class="{currentSelected:$index == selectedRow}">
														<td><a href >
																{{user.firstName}} {{user.lastName}}</a></td>	
														<td><i class="fa fa-mobile" aria-hidden="true"></i>
															{{user.role.description}}</td>
													</tr>
												</tbody>
											</table>
											</div>
										</div>
									</div>
								</div>
									<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
										<a  class="btn btn-danger">Total Users :  <span class="badge">{{appUsers.length}}</span></a>
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
									<!-- 	<a  class="btn btn-success">Download <i class="fa fa-download" arial-hidden="true"></i></a> -->
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
										<!-- 	<a href class="btn btn-info"><i class="fa fa-user" arial-hidden="true"></i> Add User</a> -->
										</div>
									</div>
								</div>
								<!-- /.row -->
							</div>
							</div>	
						</div>	
						
								<div class="col-md-6">
						<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">User Detail</h3>
								<div class="box-tools pull-right">
									<div class="btn-group pull-right" ng-if="appUsers.length> 0">
								<!-- <a href class="btn btn-success" style="margin-right: 5px;"
									 data-toggle="modal" ng-click="manageUser(selectedUser)">Edit User<span class="fa fa-edit"></span></a> -->
									 </div>
								</div>
							</div>
							<!-- /.box-header -->
							<div class="box-body">
							<div class="row">
									<div class="col-md-3">
										<div >
											<img src="${contextPath}/resources/img/swadhin.jpg" 
											style="width:50%;margin-left:27px;border-radius: 50%;">
										</div>
									</div>
									   <div class="col-md-4">
											 <a href="#" >
												<b>
													<i class="fa fa-user" aria-hidden="true"></i> {{selectedUser.firstName}}
													{{selectedUser.lastName}}
												</b>
												<br>
												 <i class="fa fa-envelope" aria-hidden="true"></i> {{selectedUser.email}}
												 <br>
												 <i class="fa fa-phone-square" aria-hidden="true"></i> {{selectedUser.phone}}
											</a>
										</div>
										 <div class="col-md-5">
											 <a href="#" >
												<b>
													<i class="fa fa-check-circle-o" aria-hidden="true"></i> 
													<span style="color:{{selectedUser.status}}">{{selectedUser.label}}</span>
												</b>
												<br>
												<i class="fa fa-info-circle" aria-hidden="true"></i> {{selectedUser.role.description}}
											</a>
										</div>
								</div>
								<%-- <div class="row">
								     <div class="col-md-3">
									</div>
									<div class="col-md-9">
										<i class="fa fa-user" aria-hidden="true"></i> Status: Active
									 	<i class="fa fa-envelope" aria-hidden="true"></i> Role: {{selectedUser.role}}
									</div>
								</div> --%>
								</div>
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right pull-left">
											<i class="fa fa-building" aria-hidden="true"></i> {{selectedUser.company.companyName}}
										</div>
									</div>
									<!-- <div class="col-sm-2 col-xs-6">
										<div class="description-block border-right">
											<span ng-if="selectedUser.company.companyType == 'RO'">Retailer or Owner organization</span>
										</div>
									</div> -->
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
											 
										</div>
									</div>
									<div class="col-sm-4 col-xs-6">
										<div class="description-block pull-right" style="margin-top: 0px;">
											
										</div>
									</div> 
								</div>
							</div>
						</div>
						
						<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Manage User</h3>
								<div class="box-tools pull-right">
								
								</div>
							</div>
							<div class="box-body">
								<div class="row">
								<div class="col-md-12">
								<h5 ng-if="selectedUser.isEnabled == 0">
									User Account is currently Locked. 
									<a href class="btn btn-success pull-right" style="margin-top: -10px;" 
									ng-click="updateAccountStatus(selectedUser,'1')" >Enable the account</a></h5>
								<h5 ng-if="selectedUser.isEnabled == 1">
									User Account is currently active. 
									<a href class="btn btn-danger pull-right" style="margin-top: -10px;" 
									ng-click="updateAccountStatus(selectedUser, '0')">Disable the account</a></h5>
								
								</div>
								</div>
								<div class="row">
									<div class="col-md-8">
										Select a role from the dropdown and click to change the role.
										<select name="roleSelect1" id="roleSelect1" class="form-control" 
 										 onchange="validateDropdownValues('roleSelect1')">
										 </select>
									</div>
									<div class="col-md-4">
									<a href class="btn btn-success pull-right" style="margin-top: 18px;" ng-click="updateUserRole(selectedUser)" >Change Role</a></h5>
									</div>
									<!-- <div class="col-md-4" ng-if="">
								     <input id="toggle-event1" type="checkbox" class="toggleYesNo"
										data-width="100" data-height="35" checked 
										data-toggle="toggle" 
										data-off="Disabled" data-on="Enabled">
									</div> -->
									<div class="col-md-4">
								    	
									</div>
								</div>
							</div>
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
										<!-- /.description-block -->
									</div>
								</div>
							</div>
						</div>
						
						
						</div>
						
								<div class="modal fade" id="createUserModal" data-keyboard="false" data-backdrop="static">
									<div class="modal-dialog" style="width: 60%">
										<div class="modal-content">
											<form role="form" name="userForm" ng-submit="saveNewUser()">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal"
														aria-label="Close">
														<span aria-hidden="true">&times;</span>
													</button>
													<h4 class="modal-title">Add New User</h4>
													<div class="alert alert-danger alert-dismissable" id="modalMessageDiv"
														style="display: none;  height: 34px;white-space: nowrap;">
														<strong>Error! </strong> {{modalErrorMessage}} <span class="messageClose">X</span>
													</div>
												</div>
												<div class="modal-body" style="background-color: #eee">
													<div class="row">
														<div class="col-md-12">
															<!-- general form elements -->
															<div class="box box-primary">

																<div class="box-header">
																	<h3 class="box-title">Profile Information</h3>
																</div>
																<!-- /.box-header -->
																<!-- form start -->

																<div class="box-body">
																   <div class="row">
																	<div class="col-md-6 reqDiv required">
																		<label class="control-label">Firstname</label> <input maxlength="50"
																			ng-model="user.firstName" type="text" class="form-control" id="firstName"
																			placeholder="Enter first name" required>
																	</div>
																	<div class="col-md-6 reqDiv required">
																		<label class="control-label">Lastname</label> <input maxlength="50"
																			ng-model="user.lastName"  type="text" class="form-control" id="lastName"
																			placeholder="Enter last name" required>
																	</div>
																  </div>
																   <div class="row">
																	<div class="col-md-6 reqDiv required">
																		<label class="control-label">Email</label> 
																			<input type="email" class="form-control"
																			id="email" ng-model="user.email"  maxlength="50"
																			placeholder="Email Address" required>
																	</div>
																	<div class="col-md-6 reqDiv required">
																		<label class="control-label">Phone / Mobile</label> <input
																			ng-model="user.phoneNo"  type="text" class="form-control" id="lastName"
																			placeholder="Enter Phone Number"
																			ng-pattern="onlyNumbers" ng-keypress="filterValue($event)"  maxlength="11" required>
																	</div>
																	</div>
																	<div class="row">
																	
																	<div class="col-md-6 reqDiv required">
																		<label class="control-label">Select Role</label> 
																		<!-- <select ng-model="roles.selected"
																		ng-options="val.roleId as val.description for val in roles.list"	class="form-control" required>
																			<option value="">Select Role</option>
																		</select> -->
																		 <select name="roleSelect" id="roleSelect" class="form-control" 
								 										 onchange="validateDropdownValues('roleSelect')">
																		 </select>
																		<input type="hidden" ng-model="user.role">
																		<div class="checkbox">
																		<input id="toggle-event" type="checkbox"
																			data-width="100" data-height="35" checked 
																			data-toggle="toggle" 
																			data-off="Disabled" data-on="Enabled">
																	<%-- 	<div id="console-event"></div> --%>
																		<input type="hidden" id="enabledUser" value="">
																	   </div>			
																	</div>
																	</div>
																</div>

																<div class="box-footer">
																	<!--  <button type="submit" class="btn btn-primary">Submit</button> -->
																</div>

															</div>
														</div>
												</div>
												</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default pull-left"	id="newUserCloseBtn" data-dismiss="modal">Close</button>
													<button type="submit" class="btn btn-primary">Save changes</button>
													<button type="reset" id="resetAddUserForm" class="btn btn-primary">RESET</button>
												</div>
											</form>
										
									</div>
								</div>

							</div>
						
						</div>
					
				
				</section>
				
		</div>
	</div>
