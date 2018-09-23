<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Home</title>

<spring:url value="/resources/css/dataTables.bootstrap.css"	var="resourceDataTableCSSURL" />
<link rel="stylesheet" href="${resourceDataTableCSSURL}">

<spring:url value="/resources/css/jquery.dataTables.min.css"	var="resourceJqueryDataTableCSSURL" />
<link rel="stylesheet" href="${resourceJqueryDataTableCSSURL}">

<script src='<c:url value="/resources/js/xeditable.min.js"></c:url>'></script>

<script type="text/javascript"	src='<c:url value="/resources/plugins/datatables/jquery.dataTables.min.js"></c:url>'></script>
<script type="text/javascript"	src='<c:url value="/resources/plugins/datatables/dataTables.bootstrap.min.js"></c:url>'></script>
	
<script type="text/javascript"	src='<c:url value="/resources/js/employee-controller.js"></c:url>'></script>
<script type="text/javascript"	src='<c:url value="/resources/js/multiselect.js"></c:url>'></script>
<style>
.errorMsg{
color:red;
font-size: 12px;
}


multiselect {
    display:block;
}
multiselect .btn {
    width: 100%;
    background-color: #FFF;
}
multiselect .btn.error{
    border: 1px solid #da4f49 !important;
}
multiselect .dropdown-menu {
    max-height: 300px;
    overflow-y: auto;
}
multiselect .dropdown-menu {
    width: 100%;
    box-sizing: border-box;
    padding: 2px;
}
multiselect .dropdown-menu > li > a {
    padding: 3px 10px;
    cursor:pointer;
}
</style>
</head>
<body ng-app="productApp">
	<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content">
				<div class="row" ng-controller="employeeController">
						<!-- <div class="box">
								<div class="box-header">
									<h3 class="box-title">Employee Details</h3>
								</div>
								<div class="box-body">
									<div class="col-md-6">
										<div class="row">
											<div class="form-group">
												<input type="hidden" name="employee.employeeId"
													ng-model="employee.employeeId" id="employee.employeeId"
													class="form-control">
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.firstname.$invalid && !employeeForm.firstname.$pristine }">
													<label class="control-label">Firstname</label><span class="errorMsg">*</span> 
													 <span ng-show="employeeForm.firstname.$invalid && !employeeForm.firstname.$pristine" class="errorMsg pull-right">
													 <i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Firstname is required</span>	<input
														type="text" name="firstname" placeholder="Enter firstname"
														ng-model="employee.firstName" id="firstname"
														class="form-control" required>
														
												</div>
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.lastname.$invalid && !employeeForm.lastname.$pristine }">
													<label class="control-label">Lastname</label><span class="errorMsg">*</span>
													<span ng-show="employeeForm.lastname.$invalid && !employeeForm.lastname.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Lastname is required</span>
													 <input
														type="text" name="lastname" placeholder="Enter lastname"
														ng-model="employee.lastName" id="lastname"
														class="form-control" required>
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.email.$invalid && !employeeForm.email.$pristine }">
													<label class="control-label">Email</label>
													<span ng-show="employeeForm.email.$invalid && !employeeForm.email.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Enter a valid email</span>
													 <input
														type="email" name="email" placeholder="Enter email"
														ng-model="employee.email" id="email"
														class="form-control" >
												</div>
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.mobile.$invalid && !employeeForm.mobile.$pristine }">
													<label class="control-label">Mobile</label><span class="errorMsg">*</span>
													<span ng-show="employeeForm.mobile.$invalid && !employeeForm.mobile.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Mobile is required</span>
													<input
														type="text" name="mobile" placeholder="Enter mobile number" ng-keypress="filterValue($event)"
														ng-model="employee.mobile" id="mobile" maxlength="10" ng-pattern="onlyNumbers"
														class="form-control" required>
														
												</div>
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.idtype.$invalid && !employeeForm.idtype.$pristine }">
													<label class="control-label">Select ID Proof</label> 
													<span class="errorMsg">*</span>
													<span ng-show="employeeForm.idtype.$invalid && !employeeForm.idtype.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Select ID Proof</span>
													<select
														name="idtype" class="form-control" 
														id="idtype"  required
														ng-model="employee.idType">
														<option ng-repeat="type in identityTypes" ng-value="type">{{type}}</option>
													</select>
													
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.idProof.$invalid && !employeeForm.idProof.$pristine }">
													<label class="control-label">ID number</label>
													  <span class="errorMsg">*</span> 
													  <span ng-show="employeeForm.idProof.$invalid && !employeeForm.idProof.$pristine" class="errorMsg pull-right">
													  <i class="fa fa-exclamation-triangle" aria-hidden="true"></i>ID number is required</span>
													  <input
														type="text" name="idProof" required
														ng-model="employee.idProof" id="idProof"
														class="form-control">
												</div>

											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="row">
											<div class="form-group">
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.addressLine1.$invalid && !employeeForm.addressLine1.$pristine }">
													<label class="control-label">Address Line1</label><span class="errorMsg">*</span>
													<span ng-show="employeeForm.addressLine1.$invalid && !employeeForm.addressLine1.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Addressline1 is required</span>
													<textarea style="height: 93px"
														name="addressLine1" required
														ng-model="employee.addressVO.addressLine1"
														id="addressLine1" class="form-control"></textarea>
														
												</div>


												<div class="col-md-6">
													<label class="control-label">Address Line 2</label>
													<textarea style="height: 93px"
														name="employee.addressVO.addressLine2"
														ng-model="employee.addressVO.addressLine2"
														id="employee.addressLine2" class="form-control"
														></textarea>
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.state.$invalid && !employeeForm.state.$pristine }">
													<label class="control-label">State</label> <span class="errorMsg">*</span>
													<span ng-show="employeeForm.state.$invalid && !employeeForm.state.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>State is required</span>
													<select
														name="state" class="form-control"
														id="state" ng-change="getCities()"
														 ng-model="employee.addressVO.state">
														<option ng-repeat="state in states"
															ng-selected="employee.addressVO.state.stateid == state.stateid"
															ng-value="state.name">{{state.name}}</option>
													</select>
													
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.citySelect.$invalid && !employeeForm.citySelect.$pristine }">
													<label class="control-label">City</label>
													<span class="errorMsg">*</span> 
													<span ng-show="employeeForm.citySelect.$invalid && !employeeForm.citySelect.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>City Name is required</span>
													<select
														id="citySelect"  class="form-control" required
														name="citySelect" ng-model="employee.addressVO.city">
														<option ng-repeat="city in cities" ng-value="city.name">
															{{city.name}}</option>
													</select>
													
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.zipcode.$invalid && !employeeForm.zipcode.$pristine }">
													<label class="control-label">Pincode</label>
													<span class="errorMsg">*</span> 
													<span ng-show="employeeForm.zipcode.$invalid && !employeeForm.zipcode.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Zipcode is required</span>
													<input
														type="text" name="zipcode" required
														ng-model="employee.addressVO.zipcode"
														id="zipcode" class="form-control">
														
														
												</div>


											</div>
										</div>
									</div>


								</div>
								/.box-body
								<div class="box-footer">
									<div class="col-md-12">
										<input type="checkbox" ng-model="employee.isUser"
											name="employeeUser"> <span>Create user for	this employee</span>
									</div>
									<div class="col-md-6">
										<a href data-toggle="tootip" title="Add Employee"
											class="btn btn-primary" ng-click="addEmployee();"
											aria-expanded="true"> Add Employee </a>
										
										<button type="button" class="btn btn-primary"
											ng-click="saveEmployee(employee)" id="saveemployee">Save</button>
										
										<input type="submit" value="CREATE EMPLOYEE" ng-disabled="employeeForm.$invalid"
										class="btn btn-primary" id="create-my-acct-btn">
										<input type="button" value="RESET" ng-click="employeeForm.$setPristine()"	class="btn btn-primary" id="ResetBtn">			
									</div>
									<div class="col-md-6">
										<div class="alert alert-success" id="success-alert"
													style="display: none">
													<button type="button" class="close" data-dismiss="alert">x</button>
													<strong>Success! </strong> {{successMessage}}
												</div>
												<div class="alert alert-danger" id="danger-alert"
													style="display: none">
													<button type="button" class="close" data-dismiss="alert">x</button>
													<strong>Error! </strong> {{errorMessage}}
												</div>
										</div>		
								</div>
								
						</div>
						 -->

					<div class="modal fade bs-employee-modal" tabindex="-1"
						role="dialog" id="employeeModalWindow" data-keyboard="false"
						data-backdrop="static">
						<div class="modal-dialog" style="width: 80%">
							<div class="modal-content">
								<form id="employee-form" name="employeeForm"
									ng-submit="saveEmployee(employeeForm, 'add')" novalidate>
									<div class="box">
								<div class="box-header">
									<h3 class="box-title">New Employee Details</h3>
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span>X</span>
								</button>
								</div>
								<div class="box-body">
									<div class="col-md-6">
										<div class="row">
											<div class="form-group">
												<input type="hidden" name="employee.employeeId"
													ng-model="employee.employeeId" id="employee.employeeId"
													class="form-control">
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.firstname.$invalid && !employeeForm.firstname.$pristine }">
													<label class="control-label">Firstname</label><span class="errorMsg">*</span> 
													 <span ng-show="employeeForm.firstname.$invalid && !employeeForm.firstname.$pristine" class="errorMsg pull-right">
													 <i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Firstname is required</span>	<input
														type="text" name="firstname" placeholder="Enter firstname"
														ng-model="employee.firstName" id="firstname"
														class="form-control" required>
														
												</div>
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.lastname.$invalid && !employeeForm.lastname.$pristine }">
													<label class="control-label">Lastname</label><span class="errorMsg">*</span>
													<span ng-show="employeeForm.lastname.$invalid && !employeeForm.lastname.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Lastname is required</span>
													 <input
														type="text" name="lastname" placeholder="Enter lastname"
														ng-model="employee.lastName" id="lastname"
														class="form-control" required>
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.email.$invalid && !employeeForm.email.$pristine }">
													<label class="control-label">Email</label>
													<span ng-show="employeeForm.email.$invalid && !employeeForm.email.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Enter a valid email</span>
													 <input
														type="email" name="email" placeholder="Enter email"
														ng-model="employee.email" id="email"
														class="form-control" >
												</div>
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.mobile.$invalid && !employeeForm.mobile.$pristine }">
													<label class="control-label">Mobile</label><span class="errorMsg">*</span>
													<span ng-show="employeeForm.mobile.$invalid && !employeeForm.mobile.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Mobile is required</span>
													<input
														type="text" name="mobile" placeholder="Enter mobile number" ng-keypress="filterValue($event)"
														ng-model="employee.mobile" id="mobile" maxlength="10" ng-pattern="onlyNumbers"
														class="form-control" required>
														
												</div>
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.idtype.$invalid && !employeeForm.idtype.$pristine }">
													<label class="control-label">Select ID Proof</label> 
													<span class="errorMsg">*</span>
													<span ng-show="employeeForm.idtype.$invalid && !employeeForm.idtype.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Select ID Proof</span>
													<select
														name="idtype" class="form-control" 
														id="idtype"  required
														ng-model="employee.idType">
														<option ng-repeat="type in identityTypes" ng-value="type">{{type}}</option>
													</select>
													
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.idProof.$invalid && !employeeForm.idProof.$pristine }">
													<label class="control-label">ID number</label>
													  <span class="errorMsg">*</span> 
													  <span ng-show="employeeForm.idProof.$invalid && !employeeForm.idProof.$pristine" class="errorMsg pull-right">
													  <i class="fa fa-exclamation-triangle" aria-hidden="true"></i>ID number is required</span>
													  <input
														type="text" name="idProof" required
														ng-model="employee.idProof" id="idProof"
														class="form-control">
												</div>

											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="row">
											<div class="form-group">
												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.addressLine1.$invalid && !employeeForm.addressLine1.$pristine }">
													<label class="control-label">Address Line1</label><span class="errorMsg">*</span>
													<span ng-show="employeeForm.addressLine1.$invalid && !employeeForm.addressLine1.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Addressline1 is required</span>
													<textarea style="height: 93px"
														name="addressLine1" required
														ng-model="employee.addressVO.addressLine1"
														id="addressLine1" class="form-control"></textarea>
														
												</div>


												<div class="col-md-6">
													<label class="control-label">Address Line 2</label>
													<textarea style="height: 93px"
														name="employee.addressVO.addressLine2"
														ng-model="employee.addressVO.addressLine2"
														id="employee.addressLine2" class="form-control"
														></textarea>
												</div>

											<!-- 	<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.state.$invalid && !employeeForm.state.$pristine }">
													<label class="control-label">State</label> <span class="errorMsg">*</span>
													<span ng-show="employeeForm.state.$invalid && !employeeForm.state.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>State is required</span>
													<select
														name="state" class="form-control"
														id="state" ng-change="getCities()"
														 ng-model="employee.addressVO.state">
														<option ng-repeat="state in states"
															ng-selected="employee.addressVO.state.stateid == state.stateid"
															ng-value="state.name">{{state.name}}</option>
													</select>
													
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.citySelect.$invalid && !employeeForm.citySelect.$pristine }">
													<label class="control-label">City</label>
													<span class="errorMsg">*</span> 
													<span ng-show="employeeForm.citySelect.$invalid && !employeeForm.citySelect.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>City Name is required</span>
													<select
														id="citySelect"  class="form-control" required
														name="citySelect" ng-model="employee.addressVO.city">
														<option ng-repeat="city in cities" ng-value="city.name">
															{{city.name}}</option>
													</select>
													
												</div> -->

												<div class="col-md-6" ng-class="{ 'has-error' : employeeForm.zipcode.$invalid && !employeeForm.zipcode.$pristine }">
													<label class="control-label">Pincode</label>
													<span class="errorMsg">*</span> 
													<span ng-show="employeeForm.zipcode.$invalid && !employeeForm.zipcode.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Zipcode is required</span>
													<input
														type="text" name="zipcode" required
														ng-model="employee.addressVO.zipcode"
														id="zipcode" class="form-control">
														
														
												</div>


											</div>
										</div>
									</div>


								</div>
								<div class="box-footer">
									<!-- <div class="col-md-12">
										<input type="checkbox" ng-model="employee.isUser"
											name="employeeUser"> <span>Create user for	this employee</span>
									</div> -->
									<div class="col-md-6">
										<input type="submit" value="CREATE EMPLOYEE" ng-disabled="employeeForm.$invalid"
										class="btn btn-primary" id="create-my-acct-btn">
										<input type="button" value="RESET" ng-click="employeeForm.$setPristine()"	class="btn btn-primary" id="ResetBtn">			
									</div>
									<div class="col-md-6">
										<div class="alert alert-success" id="success-alert"
													style="display: none">
													<button type="button" class="close" data-dismiss="alert">x</button>
													<strong>Success! </strong> {{successMessage}}
												</div>
												<div class="alert alert-danger" id="danger-alert"
													style="display: none">
													<button type="button" class="close" data-dismiss="alert">x</button>
													<strong>Error! </strong> {{errorMessage}}
												</div>
										</div>		
								</div>
								
						</div>
								
							
								</form>
							</div>
						</div>
					</div>
					
						<div class="modal fade bs-employee-modal-edit" tabindex="-1"
						role="dialog" id="employeeModalWindow" data-keyboard="false"
						data-backdrop="static">
						<div class="modal-dialog" style="width: 80%">
							<div class="modal-content">
								<form id="employee-form" name="employeeEditForm"
									ng-submit="saveEmployee(employeeEditForm,'edit')" novalidate>
									<div class="box">
								<div class="box-header">
									<h3 class="box-title">Edit Employee Details</h3>
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span>X</span>
								</button>
								</div>
								<div class="box-body">
									<div class="col-md-6">
										<div class="row">
											<div class="form-group">
												<input type="hidden" name="employee.employeeId"
													ng-model="editedEmployee.employeeId" id="editedEmployee.employeeId"
													class="form-control">
												<div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.firstname.$invalid && !employeeEditForm.firstname.$pristine }">
													<label class="control-label">Firstname</label><span class="errorMsg">*</span> 
													 <span ng-show="employeeEditForm.firstname.$invalid && !employeeEditForm.firstname.$pristine" class="errorMsg pull-right">
													 <i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Firstname is required</span>	<input
														type="text" name="firstname" placeholder="Enter firstname"
														ng-model="editedEmployee.firstName" id="firstname"
														class="form-control" required>
														
												</div>
												<div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.lastname.$invalid && !employeeEditForm.lastname.$pristine }">
													<label class="control-label">Lastname</label><span class="errorMsg">*</span>
													<span ng-show="employeeEditForm.lastname.$invalid && !employeeEditForm.lastname.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Lastname is required</span>
													 <input
														type="text" name="lastname" placeholder="Enter lastname"
														ng-model="editedEmployee.lastName" id="lastname"
														class="form-control" required>
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.email.$invalid && !employeeEditForm.email.$pristine }">
													<label class="control-label">Email</label>
													<span ng-show="employeeEditForm.email.$invalid && !employeeEditForm.email.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Enter a valid email</span>
													 <input
														type="email" name="email" placeholder="Enter email"
														ng-model="editedEmployee.email" id="email"
														class="form-control" >
												</div>
												<div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.mobile.$invalid && !employeeEditForm.mobile.$pristine }">
													<label class="control-label">Mobile</label><span class="errorMsg">*</span>
													<span ng-show="employeeEditForm.mobile.$invalid && !employeeEditForm.mobile.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Mobile is required</span>
													<input
														type="text" name="mobile" placeholder="Enter mobile number" ng-keypress="filterValue($event)"
														ng-model="editedEmployee.mobile" id="mobile" maxlength="10" ng-pattern="onlyNumbers"
														class="form-control" required>
														
												</div>
												<div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.idtype.$invalid && !employeeEditForm.idtype.$pristine }">
													<label class="control-label">Select ID Proof</label> 
													<span class="errorMsg">*</span>
													<span ng-show="employeeEditForm.idtype.$invalid && !employeeEditForm.idtype.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Select ID Proof</span>
													<select
														name="idtype" class="form-control" 
														id="idtype"  required
														ng-model="editedEmployee.idType">
														<option ng-repeat="type in identityTypes" ng-value="type">{{type}}</option>
													</select>
													
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.idProof.$invalid && !employeeEditForm.idProof.$pristine }">
													<label class="control-label">ID number</label>
													  <span class="errorMsg">*</span> 
													  <span ng-show="employeeEditForm.idProof.$invalid && !employeeEditForm.idProof.$pristine" class="errorMsg pull-right">
													  <i class="fa fa-exclamation-triangle" aria-hidden="true"></i>ID number is required</span>
													  <input
														type="text" name="idProof" required
														ng-model="editedEmployee.idProof" id="idProof"
														class="form-control">
												</div>

											</div>
										</div>
									</div>
									<div class="col-md-6">
										<div class="row">
											<div class="form-group">
												<div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.addressLine1.$invalid && !employeeEditForm.addressLine1.$pristine }">
													<label class="control-label">Address Line1</label><span class="errorMsg">*</span>
													<span ng-show="employeeEditForm.addressLine1.$invalid && !employeeEditForm.addressLine1.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Addressline1 is required</span>
													<textarea style="height: 93px"
														name="addressLine1" required
														ng-model="editedEmployee.addressVO.addressLine1"
														id="addressLine1" class="form-control"></textarea>
														
												</div>


												<div class="col-md-6">
													<label class="control-label">Address Line 2</label>
													<textarea style="height: 93px"
														name="editedEmployee.addressVO.addressLine2"
														ng-model="editedEmployee.addressVO.addressLine2"
														id="editedEmployee.addressLine2" class="form-control"
														></textarea>
												</div>

												<!-- <div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.state.$invalid && !employeeEditForm.state.$pristine }">
													<label class="control-label">State</label> <span class="errorMsg">*</span>
													<span ng-show="employeeEditForm.state.$invalid && !employeeEditForm.state.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>State is required</span>
													<select
														name="state" class="form-control"
														id="state" ng-change="getCities()"
														 ng-model="editedEmployee.addressVO.state">
														<option ng-repeat="state in states"
															ng-selected="editedEmployee.addressVO.state.stateid == state.stateid"
															ng-value="state.name">{{state.name}}</option>
													</select>
													
												</div>

												<div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.citySelect.$invalid && !employeeEditForm.citySelect.$pristine }">
													<label class="control-label">City</label>
													<span class="errorMsg">*</span> 
													<span ng-show="employeeEditForm.citySelect.$invalid && !employeeEditForm.citySelect.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>City Name is required</span>
													<select
														id="citySelect"  class="form-control" required
														name="citySelect" ng-model="editedEmployee.addressVO.city">
														<option ng-repeat="city in cities" ng-value="city.name">
															{{city.name}}</option>
													</select> 
													
												</div>-->

												<div class="col-md-6" ng-class="{ 'has-error' : employeeEditForm.zipcode.$invalid && !employeeEditForm.zipcode.$pristine }">
													<label class="control-label">Pincode</label>
													<span class="errorMsg">*</span> 
													<span ng-show="employeeEditForm.zipcode.$invalid && !employeeEditForm.zipcode.$pristine" class="errorMsg pull-right">
													<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>Zipcode is required</span>
													<input
														type="text" name="zipcode" required ng-keypress="filterValue($event)"
														ng-model="editedEmployee.addressVO.zipcode" ng-pattern="onlyNumbers"
														id="zipcode" class="form-control" maxlength="6">
														
														
												</div>


											</div>
										</div>
									</div>


								</div>
								<div class="box-footer">
									<!-- <div class="col-md-12">
										<input type="checkbox" ng-model="editedEmployee.isUser"
											name="employeeUser"> <span>Create user for	this employee</span>
									</div> -->
									<div class="col-md-6">
										<input type="submit" value="UPDATE EMPLOYEE" ng-disabled="employeeEditForm.$invalid"
										class="btn btn-primary" id="create-my-acct-btn">
									</div>
									<div class="col-md-6">
										<div class="alert alert-success" id="successedit-alert"
													style="display: none">
													<button type="button" class="close" data-dismiss="alert">x</button>
													<strong>Success! </strong> {{successMessage}}
												</div>
												<div class="alert alert-danger" id="dangeredit-alert"
													style="display: none">
													
													<strong>Error! </strong> {{errorMessage}}<button type="button" class="close" data-dismiss="alert">x</button>
												</div>
										</div>		
								</div>
								
						</div>
								
							
								</form>
							</div>
						</div>
					</div>
				<div id="createUserDiv" class="modal fade createusermmodal"
					data-keyboard="false" data-backdrop="static">
					<div class="modal-dialog" style="width: 30%">
						<div class="modal-content">
							<div class="modal-header">
								<a href="#" data-dismiss="modal" aria-hidden="true"
									class="close">X</a>
								<h3>Create User</h3>
							</div>
							<div class="modal-body">
								<div class="row">
									<div class="form-group">
										<div class="col-md-6">
											<label>Select Roles</label> <span class="errorMsg">*</span>
													<span
														ng-show="roleForm.roleType.$invalid && !roleForm.roleType.$pristine "
														class="errorMsg pull-right"> <i
														class="fa fa-exclamation-triangle" aria-hidden="true"></i>Select
														a role for the employee
													</span> 
													<!-- <select name="roleType" class="form-control"
														ng-init="getAvailableRoles()"
														ng-options="val as val for val in availableRoles"
														ng-model="selectedEmployee.selectedRole"
														required>
														<option ng-selected="true" value="">Select
															Roles</option>
													</select> -->
													<multiselect class="input-xlarge" multiple="true"
													        ng-model="selectedEmployee.selectedRole"
													        options="val as val for val in availableRoles"
													        change="selected()" ></multiselect>
													        <div class="well well-small">
													            {{selectedEmployee.selectedRole}}
													        </div>
										</div>
									</div>
								</div>

							</div>
							<div class="modal-footer">
								<a href class="btn btn-primary">Create</a> <a href="#"
									data-dismiss="modal" aria-hidden="true" class="btn btn-primary">Close</a>
							</div>
						</div>
					</div>

				</div>
				<div class="col-xs-12">
						<div class="box">
							<div class="box-header">
								<h3 class="box-title">Employee List</h3><br>
								<!-- <a href class="btn btn-primary" data-target=".bs-employee-modal" data-toggle="modal">Add Employee</a> -->
							</div>
							<div class="box-body">
								
								<div class="col-md-6" style="overflow:auto;height:400px">
									<input type="text" class="form-control" placeholder="Search Employee" ng-model="searchEmployee">
									<table id="example1" class="table table-bordered table-striped" style="height:400px">
										<thead>
											<tr>
												<th>Name</th>
												<th>Mobile</th>
											</tr>
										</thead>
										<tbody>
										 <tr ng-repeat="employee in appUsers | filter: searchEmployee">
										 <td><a href ng-click="getEmployeeDetail(employee)"><i class="fa fa-user" aria-hidden="true"></i> {{employee.firstName}} {{employee.lastName}}</a></td>
										 <td><i class="fa fa-mobile" aria-hidden="true"></i> {{employee.mobile}}</td>
										 </tr>
										</tbody>
								</table>
								</div>
							<div class="col-md-6">
								<div class="panel panel-info" ng-if="selectedEmployee!=null">
									<div class="panel-heading">
										<h3 class="panel-title">{{selectedEmployee.firstName}} {{selectedEmployee.lastName}}</h3>
									</div>
									<div class="panel-body"  style="height:320px">
										<div class="row">
											<div class="col-md-3 col-lg-3 " align="center">
												<i class="fa fa-user fa-4x" aria-hidden="true"></i>
											</div>
											<div class=" col-md-9 col-lg-9">
												<table class="table table-user-information">
													<tbody>
														<tr>
															<td>Department</td>
															<td>Sales</td>
														</tr>
														<tr>
															<td>ID Proof</td>
															<td>{{selectedEmployee.idProof}}</td>
														</tr>
														
														<tr>
															<td>Address</td>
															<td>{{selectedEmployee.addressVO.addressLine1}},
																{{selectedEmployee.addressVO.addressLine2}}
															</td>
														</tr>
														
														<tr>
															<td>Username</td>
															<td><span ng-if="selectedEmployee.userVO==null">
																<a href ng-click="createUser(selectedEmployee)"><span class="badge">Create User</span></a>
															</span>
															<span ng-if="selectedEmployee.userVO!=null">
																{{selectedEmployee.userVO.userName}}
															</span>
															</td>
														</tr>
														<tr ng-if="selectedEmployee.userVO!=null">
															<td>Roles</td>
															<td>
																{{employeeRoles}}
															</td>
														</tr>

													</tbody>
												</table>
											</div>
										</div>
									</div>
									<div class="panel-footer">
										<div>
										 <a href ng-click="editSelectedEmployee(selectedEmployee)" 
										 	data-toggle="modal" data-target=".bs-employee-modal-edit"
											data-original-title="Edit this user" data-toggle="tooltip"
											type="button" class="btn btn-sm btn-warning"><i
												class="glyphicon glyphicon-edit"></i></a>
										 <a href ng-click="deleteEmployee(selectedEmployee)" 
											data-original-title="Remove this user" data-toggle="tooltip"
											type="button" class="btn btn-sm btn-danger"><i
												class="glyphicon glyphicon-remove"></i></a>
										</div>
									</div>

								</div>
							</div>
						</div>
						<!-- /.box -->
					</div>
					</div>
					
				</div>


		</section>
	</div>

</body>
</html>