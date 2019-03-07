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


<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/ionicons.min.css"></c:url>' />
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />


<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/jquery.dataTables.min.css"></c:url>' />

<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/responsive.bootstrap.min.css"></c:url>' />

<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/optionbtn.css"></c:url>' />
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/jquery.dataTables.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/dataTables.bootstrap.min.js "></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/dataTables.responsive.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/responsive.bootstrap.min.js"></c:url>'></script>


<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/incident-modal.css"></c:url>' />
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/sp-ext-customer-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-provider-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>

<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>

<style>
.main-box.no-header {
	padding-top: 20px;
}

.table tbody tr.currentSelected {
	background-color: rgba(60, 141, 188, 0.58) !important;
}

/* .table th {
	background: none repeat scroll 0 0 #0077BF !important;
} */
.currentSelected {
	background: rgba(60, 141, 188, 0.58);
	color: #fff;
}

.currentSelected a {
	color: #fff
}

#errorMessageDiv, #successMessageDiv, #infoMessageDiv {
	top: 0%;
	left: 50%;
	/*  width: 45em; */
	height: 3em;
	margin-top: 4em;
	margin-left: -4em;
	border: 1px solid #ccc;
	background-color: #fff;
	position: fixed;
}

#modalMessageDiv {
	top: -7%;
	left: 47%;
	/* width: 45em; */
	height: 3em;
	margin-top: 4em;
	margin-left: -15em;
	border: 1px solid #ccc;
	background-color: #fff;
	position: fixed;
}

.messageClose {
	background-color: #000;
	padding: 8px 8px 10px;
	position: relative;
	left: 8px;
}

.reqDiv.required .control-label:after {
	content: "*";
	color: red;
}

.popover {
	max-width: 500px;
}

.round.hollow a {
	border: 2px solid green;
	border-radius: 15px;
	color: green;
	font-size: 12px;
	padding: 2px 2px;
	text-decoration: none;
	font-family: 'Open Sans', sans-serif;
}

.middle {
	margin-top: 17px;
}

.info-box {
	background: #deefe5
}

.info-box-number {
	font-size: 15px;
}

.wordspace {
	display: block;
	word-wrap: break-word;
	white-space: normal"
}
</style>
</head>

<div class="content-wrapper">
	<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
	<div ng-controller="spExtCustomerController" id="incidentWindow">
		<div style="display: none" id="loadingDiv">
			<div class="loader">Loading...</div>
		</div>
		<section class="content">
			<div class="row">
				<div class="col-md-12">
					<div class="box" style="height: 90%">
						<div class="box-header with-border">
							<div class="row">
								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio4"
												ng-model="pageViewFor" value="INCIDENTS"
												ng-change="displayExternalCustomerView(pageViewFor)" /> <label
												for="radio4"> Incidents </label>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio5"
												ng-model="pageViewFor" value="SITES"
												ng-change="displayExternalCustomerView(pageViewFor)" /> <label
												for="radio5">Sites</label>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio6"
												ng-model="pageViewFor" value="ASSETS"
												ng-change="displayExternalCustomerView(pageViewFor)" /> <label
												for="radio6">Assets</label>
										</div>
									</div>
								</div>


								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio7"
												ng-model="pageViewFor" value="CUSTOMERS"
												ng-change="displayExternalCustomerView(pageViewFor)" /> <label
												for="radio7">Customers</label>
										</div>
									</div>
								</div>

							</div>
						</div>
						<div class="row">
						<div class="box-tools pull-right">
						<div class="col-md-12">
						<a class="btn btn-success  pull-right" ng-click="addEditExtCustomer(val,'Add')"
							style="margin-right: 5px;"><span
							class="fa fa-hand-o-down"></span> Add Customer 
						</a>
						</div>
					   </div>
						</div>	
						<div class="row dropdown">
							<div class="col-xs-6" >
								<div class="box" ng-show="extCustList.length>0 ">
									<div class="box-header">
										<div class="row">
											<div class="col-md-12">
											<h3 class="box-title">Customer List</h3>
											</div>
										</div>
									</div>
									<div class="box-body table-responsive no-padding">
										<table class="table table-hover">
											<tbody style="font-size: .9em">
												<tr">
													<th style="width: 20%">Customer Name</th>
													<th style="width: 10%">Customer Code</th>
													<th style="width: 20%">Country</th>
													<th style="width: 20%">Primary Email</th>
													<th style="width: 20%">Primary Phone</th>
													<th style="width: 10%">Actions</th>

												</tr>
												<tr ng-repeat="val in extCustList | filter: ticketsearch"
													ng-class="{currentSelected:$index == selectedRow}"
													ng-click="rowHighilited($index); getExtCustPriorities(val);">
													<th class="todo-list">{{val.customerName}}</th>
													<td>{{val.companyCode}}</td>
													<td>{{val.countryName}}</td>
													<td>{{val.primaryContactEmail}}</td>
													<td>{{val.primaryContactNumber}}</td>
													<td><a href ng-click="addEditExtCustomer(val,'EDIT')"
														data-toggle="modal"> <i class="fa fa-edit"></i></a> <a
														href ng-click="previewSelectedExtCustomer(val)"
														data-toggle="modal"> <i class="fa fa-eye"></i></a></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="box" ng-show="selectedExtCust.selectedSlaListVOList.length>0">
								<div class="box-header">
										<div class="row">
											<div class="col-md-12">
											<h3 class="box-title">SLA List</h3>
											</div>
										</div>
									</div>
									<div class="box-body table-responsive no-padding" >
										<table class="table table-responsive table-sm">
											<tbody>
												<tr>
													<th class="col-md-7">Priority</th>
													<th class="col-md-2">Duration</th>
													<th class="col-md-3">Unit</th>

												</tr>
												<tr ng-repeat="val in selectedExtCust.selectedSlaListVOList">
													<td class="reqDiv required">{{$index +
														1}}-{{val.priority}} [{{val.description}}]<span
														class="control-label"></span>
													</td>
													<td><input name="slaId" placeholder=""
														class="form-control" type="hidden" id="slaId{{$index}}"
														ng-model="val.slaId"> 
														<input name="Duration"
														placeholder="" class="form-control" type="text"
														ng-keypress="filterValue($event)" maxlength="3"
														ng-pattern="onlyNumbers" id="duration{{$index}}"
														required ng-model="val.duration"></td>
													<td><select name="Unit"
														class="form-control selectpicker" ng-model="val.unit"
														required>
															<option value="">Select Unit</option>
															<option value="hours">Hours</option>
															<option value="days">Calendar Days</option>
													</select></td>

												</tr>
											</tbody>
										</table>
										<div class="form-group">
											<label for="description">Further SLA details and
												comments</label>
											<textarea class="form-control" id="slaDescription" 
												name="slaDescription" placeholder="SLA Description"
												ng-model="extCustServiceProvider.slaDescription"></textarea>
										</div>
								</div>
								<div class="box-footer pull-right">
									<button type="button" ng-click="updateExternalCustSLA(extCustServiceProvider)" class="btn btn-success">UPDATE SLA</button>
								</div>
							</div>
						</div>
					</div>

				</div>

			</div>
		</div>

			<div class="modal right fade" id="addEditExtCustomerModal"
				tabindex="-1" role="dialog"
				aria-labelledby="addEditExtCustomerModal">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
					<form  name="saveExtCustform" ng-submit="saveExtCustomerform(saveExtCustform)">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="myModalLabel3">External Customer</h4>
						</div>
						<div class="modal-body">
							<div style="display: none" id="loadingDivRsp">
								<div class="loader">Loading...</div>
							</div>
							<div class="box box-solid">
								<div class="box-header with-border">
									<h3 class="box-title">Customer Name</h3>
								</div>
								<div class="box-body">
									<input name="companyName" ng-model="extCustServiceProvider.customerName"
										placeholder="Company Name" class="form-control" type="text"
										required maxlength="50">
								</div>

								<div class="box-header with-border">
									<h3 class="box-title">Customer Code</h3>
								</div>
								<div class="box-body">
									<input name="companyCode" placeholder="Company Code"
										ng-model="extCustServiceProvider.companyCode" class="form-control"
										type="text" maxlength="50">
								</div>

								<div class="box-header with-border">
									<h3 class="box-title">Region</h3>
								</div>
								<div class="box-body">
									<select name="regionSelect" class="form-control selectpicker"
										required id="regionSelect"
										onchange="angular.element(this).scope().validateDropDownValues(this, event, 'regionSelect')">
									</select> <input type="hidden" ng-model="extCustServiceProvider.region">
								</div>

								<div class="box-header with-border">
									<h3 class="box-title">Country</h3>
								</div>
								<div class="box-body">
									<select name="countrySelect" class="form-control selectpicker"
										required id="countrySelect"
										onchange="angular.element(this).scope().validateDropDownValues(this, event, 'countrySelect')">
									</select> <input type="hidden" ng-model="extCustServiceProvider.country">
								</div>

								<div class="box-header with-border">
									<h3 class="box-title">Primary Contact Email</h3>
								</div>
								<div class="box-body">
									<input  name="primaryContactEmail"  placeholder="Primary Contact Email" 
									 ng-model="extCustServiceProvider.primaryContactEmail" class="form-control"  
									 type="email" required>
								</div>

								<div class="box-header with-border">
									<h3 class="box-title">Secondary Contact Email</h3>
								</div>
								<div class="box-body">
									<input  name="secondaryContactEmail"  placeholder="Secondary Contact Email" 
									 ng-model="extCustServiceProvider.secondaryContactEmail" class="form-control"  
									 type="email" >
								</div>

								<div class="box-header with-border">
									<h3 class="box-title">Primary Contact Number</h3>
								</div>
								<div class="box-body">
									<input name="primaryContactNumber" placeholder="Primary Contact Number" class="form-control"  maxlength="11" required
										type="text" ng-model="extCustServiceProvider.primaryContactNumber" ng-keypress="filterValue($event)" ng-pattern="onlyNumbers">
								</div>

								<div class="box-header with-border">
									<h3 class="box-title">Secondary Contact Number</h3>
								</div>
								<div class="box-body">
									<input name="secondaryContactNumber" placeholder="Secondary Contact Number" class="form-control"  maxlength="11"
										type="text" ng-model="extCustServiceProvider.secondaryContactNumber" ng-keypress="filterValue($event)" ng-pattern="onlyNumbers">
								</div>
								
							</div>
					   		<div class="box box-solid" >
								<div class="row">
									<div class="box-body" ng-if="priorities.length>0">
										<div class="col-md-12">
											<div class="form-group">
												<table class="table table-responsive table-sm">
													<tbody>
														<tr>
															<th class="col-md-6">Priority</th>
															<th class="col-md-2">Duration</th>
															<th class="col-md-4">Unit</th>

														</tr>
														<tr ng-repeat="val in priorities">
															<td class="reqDiv required">{{$index +
																1}}-{{val.priority}} [{{val.description}}]<span
																class="control-label"></span>
															</td>
															<td><input name="slaId" placeholder=""
																class="form-control" type="hidden" id="slaId{{$index}}"
																ng-model="val.slaId"> <input name="Duration"
																placeholder="" class="form-control" type="text"
																ng-keypress="filterValue($event)" maxlength="3"
																ng-pattern="onlyNumbers" id="duration{{$index}}"
																required ng-model="val.duration"></td>
															<td><select name="Unit"
																class="form-control selectpicker" ng-model="val.unit"
																required>
																	<option value="">Select Unit</option>
																	<option value="hours">Hours</option>
																	<option value="days">Calendar Days</option>
															</select></td>
														</tr>
													</tbody>
												</table>
											</div>
										</div>
										<div class="form-group">
									<label for="description">Further SLA details and
										comments</label>
									<textarea class="form-control" id="slaDescription1"
										name="slaDescription" placeholder="SLA Description"
										ng-model="extCustServiceProvider.slaDescription"></textarea>
								</div>
									</div>
									<div class="box-footer pull-right" style="margin-bottom:10px;">
											<button type="submit" class="btn btn-success">SAVE CHANGES</button>
											<button type="reset" id="resetAddEditExtCustform" class="btn btn-success">RESET</button>
										</div>
								</div>
							
							</div> 
								
								
						</div>					
					</form>
					</div>
				</div>
			</div>
		</section>
	</div>
</div>
</html>