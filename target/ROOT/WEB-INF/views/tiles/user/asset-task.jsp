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
	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />


<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/select2.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/select2.full.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/moment.min.js"></c:url>'></script>


<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.min.js"></c:url>'></script>

<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>



<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/asset-task-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/service-provider-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/asset-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>


<style>
.main-box.no-header {
	padding-top: 20px;
}

.currentSelected {
	background: rgba(60, 141, 188, 0.58);
	color: #fff;
}

.currentSelected a {
	color: #fff
}

.col-xs-3 label {
	font-weight: bold;
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

#modalMessageDiv, #serviceModalMessageDiv, #equipmentModalMessageDiv {
	top: -7%;
	left: 60%;
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

.col-xs-3.required .control-label:after {
	content: "*";
	color: red;
}

.col-xs-4.required .control-label:after {
	content: "*";
	color: red;
}

.col-xs-5.required .control-label:after {
	content: "*";
	color: red;
}

.col-xs-6.required .control-label:after {
	content: "*";
	color: red;
}
</style>

<script>
	$(document).ready(function() {

		$('input').attr('autocomplete', 'off');

		$('.toggle-on').removeAttr('style');
		$('.toggle-off').removeAttr('style');

		$(".dt1").datepicker({
			format : "dd-mm-yyyy",
			autoclose: true
		})

		/* $('siteSelect').multiselect();
		 $('serviceSiteSelect').multiselect();  */

	})
</script>
</head>
<div class="content-wrapper">
	<div ng-controller="assetTaskController" id="assetTaskWindow">
		<div style="display: none" id="loadingDiv">
			<div class="loader">Loading...</div>
		</div>
		<section class="content" style="min-height: 35px; display: none"
			id="messageWindow">
			<div class="row">
				<div class="col-md-12">
					<div class="alert alert-success alert-dismissable"
						id="successMessageDiv"
						style="display: none; height: 34px; white-space: nowrap;">
						<strong>Success! </strong> {{successMessage}}
					</div>
					<div class="alert alert-danger alert-dismissable"
						id="errorMessageDiv"
						style="display: none; height: 34px; white-space: nowrap;">
						<strong>Error! </strong> {{errorMessage}} <a href><span
							class="messageClose" ng-click="closeMessageWindow()">X</span></a>
					</div>
				</div>
			</div>
		</section>

		
		<section class="content">
			<div class="box">
				<form name="createUpdatetaskform" ng-submit="saveAssetTaskService()">
						<div class="box-body" style="background-color: #eee">
							<div class="row" style="margin-right: -5px; margin-left: -5px;">
								<div class="box">
									<div class="box-body">
										<h3 class="box-title">
											<a href="${contextPath}/asset/details"> <span
												title="Asset or Service"><i class="fa fa-th-list"
													aria-hidden="true"></i></span></a> Asset Name: {{taskHeaderName}}
										</h3>
										<input type="hidden" id="mode" value="${taskOperation}">
										<input type="hidden" class="form-control" id="siteId"
											value="${siteId}">
										<div class="row">
											<div class="col-xs-6 required">
												<label class="control-label">Task Name</label> <input
													name="modalInput" type="text" class="form-control"
													maxlength="50" name="taskName"
													style="width: 100%; height: 44px;"
													ng-model="serviceData.taskName"
													placeholder="Enter Task Name" required>
											</div>
											<div class="col-xs-6">
												<div class="row">
											<div class="col-xs-6 required">
												<div class="form-group">
													<label class="control-label">Planned Start Date</label>

													<div class="input-group date">
														<div class="input-group-addon">
															<i class="fa fa-calendar"></i>
														</div>
														<input type="text" class="form-control pull-right dt1"
															placeholder="Planned Start Date" id="planStartDate"
															ng-model="serviceData.planStartDate"
															style="width: 100%; height: 44px;" required>

													</div>


												</div>
											</div>
											<div class="col-xs-6 required">

												<label class="control-label">Planned Completion Date</label>
												<div class="input-group date">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input type="text" class="form-control pull-right dt1"
														placeholder="Planned Completion Date" id="planComplDate"
														ng-model="serviceData.planComplDate"
														style="width: 100%; height: 44px;" required>

												</div>
											</div>
										</div>
											</div>

										</div>
										<br>

										<div class="row">
										<div class="col-xs-6">
												<label class="control-label">Task Description</label>


												<textarea class="form-control"
													style="width: 100%; height: 84px;" rows="3"
													placeholder="Enter Task Description" name="taskDescription"
													ng-model="serviceData.taskDescription"></textarea>
											</div>
											<div class="col-xs-6">
											<div class="row">
											<div class="col-xs-6  required">
												<label class="control-label">Assigned To</label> <input
													type="text" class="form-control"
													style="width: 100%; height: 44px;" name="assignTo"
													ng-model="serviceData.assignTo"
													placeholder="Enter Assigned To" required>
											</div>
											<div class="col-xs-6 required">
												<label class="control-label">Status</label> <select
													ng-hide="taskOperation =='CreateTask'" name="status"
													id="status" style="width: 100%; height: 44px;"
													class="form-control" ng-dropdown required>
													<option value="New" selected="selected">New</option>
												</select> <select ng-hide="taskOperation =='UpdateTask'"
													name="status" id="status" class="form-control" ng-dropdown
													required">
													<option value="">Select Status</option>
													<option value="INPROGRESS">In Progress</option>
													<option value="CLOSED">Closed</option>
													<option value="REJECTED">Rejected</option>
												</select>


											</div>


										</div>
											</div>
											<!-- <div class="col-xs-6 required">
												<div class="form-group">
													<label class="control-label">Planned Start Date</label>

													<div class="input-group date">
														<div class="input-group-addon">
															<i class="fa fa-calendar"></i>
														</div>
														<input type="text" class="form-control pull-right dt1"
															placeholder="Planned Start Date" id="planStartDate"
															ng-model="serviceData.planStartDate"
															style="width: 100%; height: 44px;" required>

													</div>


												</div>
											</div>
											<div class="col-xs-6 required">

												<label class="control-label">Planned Completion Date</label>
												<div class="input-group date">
													<div class="input-group-addon">
														<i class="fa fa-calendar"></i>
													</div>
													<input type="text" class="form-control pull-right dt1"
														placeholder="Planned Completion Date" id="planComplDate"
														ng-model="serviceData.planComplDate"
														style="width: 100%; height: 44px;" required>

												</div>
											</div> -->
										</div>
										<br>

										<!-- <div class="row">
											<div class="col-xs-6  required">
												<label class="control-label">Assigned To</label> <input
													type="text" class="form-control"
													style="width: 100%; height: 44px;" name="assignTo"
													ng-model="serviceData.assignTo"
													placeholder="Enter Assigned To" required>
											</div>
											<div class="col-xs-6 required">
												<label class="control-label">Status</label> <select
													ng-hide="taskOperation !='CreateTask'" name="status"
													id="status" style="width: 100%; height: 44px;"
													class="form-control" ng-dropdown required
													ng-model="serviceData.status">
													<option value="New">New</option>
												</select> <select ng-hide="taskOperation !='UpdateTask'"
													name="status" id="status" class="form-control" ng-dropdown
													required ng-model="serviceData.status">
													<option value="In Progress">In Progress</option>
													<option value="Closed">Closed</option>
													<option value="Rejected">Rejected</option>
												</select>


											</div>


										</div> -->
										<div class="row"
											ng-hide="serviceData.status =='New' || serviceData.status =='In Progress'">
											<div class="col-xs-6 ">
												<label class="control-label">Resolution Comment</label>

												<textarea class="form-control"
													style="width: 100%; height: 84px;" rows="3"
													placeholder="Enter Resolution Comment"
													name="resolutionComment"
													ng-model="serviceData.resolutionComment"></textarea>

											</div>
										</div>

										<div class="row pull-right"
											style="margin-top: 15px; margin-right: 1px;">

											<button type="submit" class="btn btn-success">Save
												changes</button>
											<button type="reset" id="resetServiceAssetForm"
												class="btn btn-success">RESET</button>
										</div>
									</div>
								</div>
							</div>
						</div>
				</form>
			</div>
		</section>
	</div>
</div>