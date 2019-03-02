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

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/optionbtn.css"></c:url>' />
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/jquery.dataTables.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/dataTables.bootstrap.min.js "></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/dataTables.responsive.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/responsive.bootstrap.min.js"></c:url>'></script>


<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/incident-modal.css"></c:url>' />
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/sp-customer-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-provider-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>

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
.middle{
margin-top: 17px;
}
.info-box{
background:#deefe5
}
.info-box-number {
    font-size: 15px;
}
.wordspace{
 display: block;
    word-wrap:break-word;
    white-space: normal"
}

</style>
</head>

<div class="content-wrapper">
	<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
	<div ng-controller="spCustomerController" id="incidentWindow">
		<div style="display: none" id="loadingDiv">
			<div class="loader">Loading...</div>
		</div>

		<section class="content" style="min-height: 35px; display: none"
			id="messageWindow">
		</section>

		<section class="content">
			<div class="row">
				<div class="col-md-12">
					<div class="box" style="height: 90%">
						<div class="box-header with-border">
								<div class="row">
								<div class="col-md-3">
										<select name="spCustomerListSelect" id="spCustomerListSelect" 
											ng-model="isCustomerSelected.spCustomerListSelect" ng-dropdown required
											class="form-control" onchange="angular.element(this).scope().getSelectionOption(this, event, 'spCustomerListSelect')"
											required>
										</select>
								</div>
								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio4" ng-model="rspPageViewFor" value="INCIDENTS" 
											ng-change="displayCustomerView(rspPageViewFor)"/> <label
												for="radio4"   >
												Incidents 
												</label> 
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio5" ng-model="rspPageViewFor" value="SITES" 
											ng-change="displayCustomerView(rspPageViewFor)"/> <label
												for="radio5" >Sites</label>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio6" ng-model="rspPageViewFor" value="ASSETS" 
											ng-change="displayCustomerView(rspPageViewFor)"/> <label
												for="radio6" >Assets</label>
										</div>
									</div>
								</div>
									</div>

			<!-- 				<div class="row" style="margin-top: 10px">
								<div class="col-md-4">
									<select name="spCustomerListSelect" id="spCustomerListSelect"
										class="form-control" style=" height:90px;"
										onchange="angular.element(this).scope().getCustomerIncident(this, event, 'spCustomerListSelect')"
										required>
									</select>

								</div>
								<div class="col-md-2 text-right" style="margin-top: 8px;">
									<label>Country Name:</label>

								</div>





								<div class="col-md-4">

									<div class="info-box info-box-sm dropdown">
									  <div class="info-box-icon bg-aqua">
									    <i class="fa fa-briefcase"></i>
									  </div>
									
									  <div class="info-box-content">
									    <span class="info-box-text">Country: </span>
									    <span class="info-box-number" id="countryName"></span>
									  </div>
									
									  <div class="info-box-dropdown">
									    Right side button for dropdown
									  </div>
									</div>

								</div>
							</div>
 -->
						</div>
					  <div class="row dropdown" >
					   <div class="col-xs-12">
					    <div class="box">
				<div class="box-header">
				<div class="row" ng-show="isCustomerSelected.spCustomerListSelect.length > 0 && spCustomerIncidentList.list.length>0 ">
				<div class="col-md-3">
					<div class="funkyradio">
						<div class="funkyradio-primary">
							<input type="radio" name="radio" id="radio3"
								ng-model="ticketCreatedOrAssigned" value="CUSTOMER"
								ng-change="checkTicketsAssignedOrCreated(ticketCreatedOrAssigned)" />
							<label for="radio3"> Tickets by Customer
							</label>
						</div>
					</div>
				</div>
				<div class="col-md-3">
					<div class="funkyradio">
						<div class="funkyradio-primary">
							<input type="radio" name="radio" id="radio2"
								ng-model="ticketCreatedOrAssigned" value="RSP"
								ng-change="checkTicketsAssignedOrCreated(ticketCreatedOrAssigned)" />
							<label for="radio2">Tickets by Company</label>
						</div>
					</div>
				</div>

				<div class="box-tools dropdown pull-right">
					<div class="input-group input-group-sm" style="width: 450px;">
						<input type="text" name="table_search"
							class="form-control pull-right" ng-model="ticketsearch"
							placeholder="Search by incident number, asset name, site name, service provider, status">
	
						<div class="input-group-btn">
							<button type="submit" class="btn btn-default">
								<i class="fa fa-search"></i>
							</button>
						</div>
					</div>
						<a class="btn btn-success dropdown-toggle pull-right"
						style="margin-right: 5px;" data-toggle="dropdown"><span
						class="fa fa-hand-o-down"></span> Action<span
						class="caret"></span> </a>
					<ul class="dropdown-menu" role="menu">
						<li><a
							href="${webContextPath}/serviceprovider/rsp/incident/create">
								<i class="fa fa-plus" arial-hidden="true"></i> Create
								Ticket
						</a></li>
						<li><a href ng-click="viewRSPUpdatePage()"
							id="updateTicket"><span class="fa fa-edit"></span>
								Update Ticket </a></li>
					</ul>
					<%-- <a href="${webContextPath}/serviceprovider/rsp/incident/create" class="btn btn-primary pull-right " >Create Ticket </a></li> --%>
				  </div>
				  <div class="col-md-5">
					<h3 class="box-title">List of Tickets created by {{ticketCreatedOrAssigned}}</h3>
					</div>
					
				</div>
			</div>
			<div class="box-body table-responsive no-padding" >
			<table class="table table-hover"
				ng-show="isCustomerSelected.spCustomerListSelect.length > 0 && spCustomerIncidentList.list.length>0">
				<tbody style="font-size: .9em">
					<tr">
						<th style="width: 12%">Ticket Number</th>
						<th style="width: 10%">Site Name</th>
						<th style="width: 10%">Asset Name</th>
						<th style="width: 20%">Title</th>
						<th>Created On</th>
						<th>Sla Due Date</th>
						<th>Service Provider</th>
						<th tyle="width:10%">Status</th>

					</tr>
					<tr
						ng-repeat="val in spCustomerIncidentList.list | filter: ticketsearch"	ng-class="{currentSelected:$index == selectedRow}"
						ng-click="setTicketinSession(val);rowHighilited($index)">
						<th class="todo-list">{{val.ticketNumber}} <a href
							ng-click="previewSelectedIncidentInfo(val)"
							data-toggle="modal"><i class="fa fa-eye pull-right"></i></a>
						</th>
						<td>{{val.siteName}}</td>
						<td>{{val.assetName}}</td>
						<td>{{val.ticketTitle}}</td>
						<td>{{val.raisedOn}}</td>
						<td>{{val.sla}}</td>
						<td>{{val.assignedSP}}</td>
						<td><span class="label label-success"
							ng-if="val.statusId == 1">{{val.status}}</span> <span
							class="label label-warning" ng-if="val.statusId == 2">{{val.status}}</span>
							<span class="label label-info" ng-if="val.statusId == 3">{{val.status}}</span>
							<span class="label label-default"
							ng-if="val.statusId == 4">{{val.status}}</span> <span
							class="label label-danger" ng-if="val.statusId == 5">{{val.status}}</span>
							<span class="label label-danger" ng-if="val.statusId == 6">{{val.status}}</span>
							<span class="label label-danger" ng-if="val.statusId > 6">{{val.status}}</span>

						</td>

					</tr>
				</tbody>
			</table>
			<table class="table table-hover"
				ng-show="siteList.length>0 ">
				<tbody style="font-size: .9em">
					<tr">
						<th style="width: 30%">Site Name</th>
						<th style="width: 20%">Owner</th>
						<th style="width: 10%">Brand Name</th>
						<th style="width: 20%">Contact Name</th>
						<th style="width: 30%">Contact Number</th>
						<th style="width: 10%">Email</th>

					</tr>
					<tr ng-repeat="val in siteList | filter: ticketsearch"
						ng-class="{currentSelected:$index == selectedRow}"
						ng-click="rowHighilited($index)">
						<th class="todo-list">{{val.siteName}} <a href
							ng-click="previewSelectedSiteInfo(val)"
							data-toggle="modal"> <i class="fa fa-eye pull-right"></i></a>
						</th>
						<td>{{val.siteOwner}}</td>
						<td>{{val.brandName}}</td>
						<td>{{val.contactName}}</td>
						<td>{{val.primaryContact}}</td>
						<td>{{val.email}}</td>
					</tr>
				</tbody>
			</table>
				<table class="table table-hover"
				ng-show="assetList.length>0 ">
				<tbody style="font-size: .9em">
					<tr">
						<th style="width: 30%">Asset Name</th>
						<th style="width: 10%">Location Name</th>
						<th style="width: 20%">Category</th>
						<th style="width: 20%">Site Name</th>
						<th style="width: 30%">Date Commissioned</th>

					</tr>
					<tr ng-repeat="val in assetList | filter: ticketsearch"
						ng-class="{currentSelected:$index == selectedRow}"
						ng-click="rowHighilited($index)">
						<th class="todo-list">{{val.assetName}} <a href
							ng-click="previewSelectedAssetInfo(val)"
							data-toggle="modal"> <i class="fa fa-eye pull-right"></i></a>
						</th>
						<td>{{val.locationName}}</td>
						<td>{{val.assetCategoryName}}</td>
						<td>{{val.siteName}}</td>
						<td>{{val.commisionedDate}}</td>
					</tr>
				</tbody>
				</table>
			  </div>
			</div>
			</div>
			</div>
				</div>

			</div>
		
		</div>


			<div class="modal right fade" id="previewIncidentModal" tabindex="-1"
				role="dialog" aria-labelledby="previewIncidentModalLabel2">
				<div class="modal-dialog" role="document">
					<div class="modal-content">

						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="myModalLabel2">{{selectedTicket.ticketNumber}}
								- Overview</h4>
							<span class="badge"
								style="background-color: #cfe4e2; color: #000"><i
								class="fa fa-flag" aria-hidden="true" style="color: red;"></i>
								{{selectedTicket.status}}</span>
						</div>
						<div class="modal-body">
							<div style="display: none" id="loadingDivRsp">
								<div class="loader">Loading...</div>
							</div>
							<div class="box box-solid">
								<div class="box-header with-border">
									<i class="fa fa-text-width"></i>
									<h3 class="box-title">{{selectedTicket.ticketTitle}}</h3>
								</div>
								<!-- /.box-header -->
								<div class="box-body">
									<dl>
										<dt>Description</dt>
										<dd>{{selectedTicket.description}}</dd>
									</dl>
								</div>
							</div>
							<div class="box box-widget widget-user"
								ng-if="selectedTicket.ticketNumber != null">
								<!-- Add the bg color to the header using any of the bg-* classes -->
								<div class="widget-user-header"
									style="background-color: #00a65a; color: #fff">
									<h3 class="widget-user-username">Site :
										{{selectedTicket.siteName}}</h3>
									<h5 class="widget-user-desc">Owner :
										{{selectedTicket.siteOwner}}</h5>
								</div>
								<div class="box-footer">
									<div class="row">
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Contact Name</h5>
												<span class="description-text">{{selectedTicket.assignedSP}}</span>
											</div>
										</div>
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Site Number</h5>
												<span class="description-text">{{selectedTicket.siteContact}}</span>
											</div>
										</div>
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Email</h5>
												<span class="description-text">{{selectedTicket.email}}</span>
											</div>
											<!-- /.description-block -->
										</div>
									</div>
									<!-- /.row -->
								</div>
							</div>
							<div class="box box-widget widget-user"
								ng-if="selectedTicket.ticketNumber != null">
								<!-- Add the bg color to the header using any of the bg-* classes -->
								<div class="widget-user-header"
									style="background-color: #00a65a; color: #fff">
									<h3 class="widget-user-username">Asset :
										{{selectedTicket.assetName}}</h3>
									<h5 class="widget-user-desc">Code :
										{{selectedTicket.assetCode}}</h5>
								</div>
								<div class="box-footer">
									<div class="row">
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Service Provider</h5>
												<span class="description-text">{{selectedTicket.assignedSP}}</span>
											</div>
											<!-- /.description-block -->
										</div>
										<!-- /.col -->
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Commissioned On</h5>
												<span class="description-text">{{selectedTicket.assetCommissionedDate}}</span>
											</div>
											<!-- /.description-block -->
										</div>
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Sub Category</h5>
												<span class="description-text">{{selectedTicket.assetSubCategory1}}</span>
											</div>
											<!-- /.description-block -->
										</div>
										<!-- /.col -->
										<!-- /.col -->
									</div>
									<!-- /.row -->
								</div>
							</div>
							<div class="info-box bg-yellow">
								<span class="info-box-icon"><i
									class="ion ion-ios-pricetag-outline" style="margin-top: 26px;"></i></span>

								<div class="info-box-content">
									<span class="info-box-text">SLA ( % )</span> <span
										class="info-box-number">{{selectedTicket.slaPercent}}</span>
									<div class="progress">
										<div class="progress-bar"
											ng-class="{'progress-bar-danger': selectedTicket.slaPercent >=100, 'progress-bar-warning': selectedTicket.slaPercent>75 && selectedTicket.slaPercent<100, 'progress-bar-info': selectedTicket.slaPercent>0 && selectedTicket.slaPercent<75}"
											role="progressbar"
											ng-style="{width: sessionTicket.width+'%'}">
											<span class="progress-text">SLA(%)
												{{selectedTicket.slaPercent}} </span>
										</div>
										<span class="progress-description"> 50% Increase in 30
											Days </span>

									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
				<div class="modal right fade" id="previewSiteModal" tabindex="-1"
				role="dialog" aria-labelledby="previewSiteModal">
				<div class="modal-dialog" role="document">
					<div class="modal-content">

						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="myModalLabel2">{{selectedSite.siteName}}
								- Overview</h4>
							<span class="badge"
								style="background-color: #cfe4e2; color: #000"><i
								class="fa fa-flag" aria-hidden="true" style="color: red;"></i>
								{{selectedSite.status}}</span>
						</div>
						<div class="modal-body">
							<div style="display: none" id="loadingDivRsp">
								<div class="loader">Loading...</div>
							</div>
									<div class="box box-widget widget-user"
								ng-if="selectedSite.siteId != null">
								<!-- Add the bg color to the header using any of the bg-* classes -->
								<div class="widget-user-header"
									style="background-color: #00a65a; color: #fff">
									<h3 class="widget-user-username">Address 
									<h5 class="widget-user-desc">
										{{selectedSite.siteAddress1}} {{selectedSite.siteAddress2}} {{selectedSite.siteAddress3}} {{selectedSite.siteAddress4}}</h5>
								</div>
								<div class="box-footer">
									<div class="row">
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Contact Name</h5>
												<span class="description-text">{{selectedSite.contactName}}</span>
											</div>
										</div>
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Contact Number</h5>
												<span class="description-text">{{selectedSite.primaryContact}}</span>
											</div>
										</div>
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Email</h5>
												<span class="description-text">{{selectedSite.email}}</span>
											</div>
											<!-- /.description-block -->
										</div>
									</div>
									<!-- /.row -->
								</div>
							</div>
							<div class="box box-solid">
								<div class="box-header with-border">
									<h3 class="box-title">License Details</h3>
								</div>
								<!-- /.box-header -->
								<div class="box-body">
									<div class="table-responsive">
							<table class="table no-margin">
							<tbody>
									<tr>
										<th>Name</th>
										<th>Valid From</th>
										<th>Valid To</th>
										<th>Attachment</th>
									</tr>
									<tr
										ng-repeat="license in selectedSite.LicenseDetail">
										<td><a href>{{license.licenseName}}</a></td>
										<td>{{license.validfrom}}</td>
										<td>{{license.validto}}</td>
										<td><a
											href="${contextPath}/selected/file/download?keyname={{license.attachment}}"
											download ng-if="license.attachment!=null"> <i
												class="fa fa-cloud-download fa-2x"
												aria-hidden="true"></i></a> <a href
											ng-click="deleteFile('LICENSE', license)"
											data-toggle="tooltip"
											data-original-title="Delete this file"
											ng-if="license.attachment!=null"> <i
												class="fa fa-remove fa-2x" aria-hidden="true"></i></a>
										</td>

									</tr>

								</tbody>
							</table>
						</div>
								</div>
							</div>

							<div class="box box-solid">
								<div class="box-header with-border">
									<h3 class="box-title">Operation Timings</h3>
								</div>
								<div class="box-body table-responsive no-padding">
								 <div class="col-md-6">
									 <table class="table no-margin">
										<tbody>
											<tr>
												<th>Weekdays</th>
												<th>Sales</th>
											</tr>
											<tr ng-repeat="timing in selectedSite.SalesOperation">
												<td>{{timing.days}}</td>
												<td ng-if="timing.from == '00:00' && timing.to == '00:00'">
													<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"
															ng-click="updateSiteModal(selectedSite)"> <span
															class="label label-warning">{{timing.from}} -
																{{timing.to}}</span>
														</a>
													</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"> <span
															class="label label-warning">{{timing.from}} -
																{{timing.to}}</span></a>
													</sec:authorize>
												</td>
												<td ng-if="timing.from == '00:00' && timing.to != '00:00'">
													<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"
															ng-click="updateSiteModal(selectedSite)"> <span
															class="label label-danger"> <i
																class="fa fa-exclamation-triangle" aria-hidden="true"></i>
																{{timing.from}} - {{timing.to}}
														</span>
														</a>
													</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_SITE_STAFF')">
														<a href data-toggle="modal"> <span
															class="label label-warning">{{timing.from}} -
																{{timing.to}} </span></a>
													</sec:authorize>

												</td>
												<td ng-if="timing.from != '00:00' && timing.to == '00:00'">
													<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"
															ng-click="updateSiteModal(selectedSite)"> <span
															class="label label-danger"> <i
																class="fa fa-exclamation-triangle" aria-hidden="true"></i>{{timing.from}}
																- {{timing.to}}
														</span>
														</a>
													</sec:authorize> <sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"> <span
															class="label label-warning">{{timing.from}} -
																{{timing.to}} </span></a>
													</sec:authorize>
												</td>

												<td ng-if="timing.from != '00:00' && timing.to != '00:00'">
													<span class="label label-success">{{timing.from}} -
														{{timing.to}} </span>


												</td>
											</tr>
										</tbody>
									</table>
									</div>
									<div class="col-md-6">
									<table class="table no-margin">
										<tbody>
											<tr>
												<th>Weekdays</th>

												<th>Delivery</th>

											</tr>
											<tr
												ng-repeat="timing in selectedSite.DeliveryOperation">
												<td>{{timing.days}}</td>
												<td
													ng-if="timing.from == '00:00' && timing.to == '00:00'">
													<sec:authorize
														access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"
															ng-click="updateSiteModal(selectedSite)">
															<span class="label label-warning">{{timing.from}}
																- {{timing.to}}</span>
														</a>
													</sec:authorize> <sec:authorize
														access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"> <span
															class="label label-warning">{{timing.from}}
																- {{timing.to}}</span></a>
													</sec:authorize>
												</td>
												<td
													ng-if="timing.from == '00:00' && timing.to != '00:00'">
													<sec:authorize
														access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"
															ng-click="updateSiteModal(selectedSite)">
															<span class="label label-danger"> <i
																class="fa fa-exclamation-triangle"
																aria-hidden="true"></i> {{timing.from}} -
																{{timing.to}}
														</span>
														</a>
													</sec:authorize> <sec:authorize
														access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"> <span
															class="label label-warning">{{timing.from}}
																- {{timing.to}} </span></a>
													</sec:authorize>

												</td>
												<td
													ng-if="timing.from != '00:00' && timing.to == '00:00'">
													<sec:authorize
														access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"
															ng-click="updateSiteModal(selectedSite)">
															<span class="label label-danger"> <i
																class="fa fa-exclamation-triangle"
																aria-hidden="true"></i>{{timing.from}} -
																{{timing.to}}
														</span>
														</a>
													</sec:authorize> <sec:authorize
														access="hasAnyRole('ROLE_SP_AGENT')">
														<a href data-toggle="modal"> <span
															class="label label-warning">{{timing.from}}
																- {{timing.to}} </span></a>
													</sec:authorize>
												</td>

												<td
													ng-if="timing.from != '00:00' && timing.to != '00:00'">
													<span class="label label-success">{{timing.from}}
														- {{timing.to}} </span>


												</td>

											</tr>

										</tbody>
									</table>
									</div>
								</div>
							</div>

							<div class="box box-solid">
								<div class="box-header with-border">
									<h3 class="box-title">Submeter Details</h3>
								</div>
								<div class="box-body table-responsive no-padding">
							<table class="table no-margin">
								<tbody
									<tr>
										<th>Submeter Number/Electricity Id (MPAN)</th>
										<th>User</th>

									</tr>
									<tr ng-repeat="submeter in selectedSite.submeterDetails">
										<td>{{submeter.subMeterNumber}}</td>
										<td>{{submeter.subMeterUser}}</td>
									</tr>
								</tbody>
							</table>
							</div>
							</div>

					
							
						</div>
					</div>
				</div>
			</div>
			
					<div class="modal right fade" id="previewAssetModal" tabindex="-1"
				role="dialog" aria-labelledby="previewAssetModal">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<h4 class="modal-title" id="myModalLabel3">{{selectedAsset.assetName}}
								- Overview</h4>
							<span class="badge"
								style="background-color: #cfe4e2; color: #000"><i
								class="fa fa-flag" aria-hidden="true" style="color: red;"></i>
								Code : {{selectedAsset.assetCode}} |  Model Number :  {{selectedAsset.modelNumber}} | Asset Type : {{selectedAsset.assetType}}</span>
						</div>
						<div class="modal-body">
							<div style="display: none" id="loadingDivRsp">
								<div class="loader">Loading...</div>
							</div>
							<div class="box box-solid">
								<div class="box-header with-border">
									<h3 class="box-title">Description</h3>
								</div>
								<div class="box-body">
									<dl>
										<dd>{{selectedAsset.assetDescription}}</dd>
									</dl>
								</div>
							</div>
							<div class="box box-solid">
								<div class="box-header with-border">
									<h3 class="box-title">Content</h3>
								</div>
								<div class="box-body">
									<dl>
										<dd>{{selectedAsset.content}}</dd>
									</dl>
								</div>
							</div>
							<div class="box box-solid">
								<div class="box-header with-border">
									<i class="fa fa-text-width"></i>
									<h3 class="box-title">Subcategory : {{selectedAsset.assetSubcategory1}}</h3>
								</div>
								<div class="box-body">
									<div class="col-sm-4 border-right">
										<div class="description-block">
											<h5 class="description-header">Asset Electrical</h5>
											<span class="description-text">{{selectedAsset.isAssetElectrical}}</span>
										</div>
									</div>
									<div class="col-sm-4 border-right">
										<div class="description-block">
											<h5 class="description-header">PW Sensor Attached</h5>
											<span class="description-text">{{selectedAsset.isPWSensorAttached}}</span>
										</div>
									</div>
									<div class="col-sm-4 border-right">
										<div class="description-block">
											<h5 class="description-header">PW Sensor Number</h5>
											<span class="description-text">{{selectedAsset.pwSensorNumber}}</span>
										</div>
									</div>
								</div>
							</div>
								<div class="box box-widget widget-user"
								ng-if="selectedAsset.assetId != null">
								<!-- Add the bg color to the header using any of the bg-* classes -->
								<div class="widget-user-header"
									style="background-color: #00a65a; color: #fff">
									<h3 class="widget-user-username">Site Details 
									<h5 class="widget-user-desc">
										{{selectedAsset.siteName}}</h5>
								</div>
								<div class="box-footer">
									<div class="row">
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Site Owner</h5>
												<span class="description-text">{{selectedAsset.siteOwner}}</span>
											</div>
										</div>
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Commissioned Date</h5>
												<span class="description-text">{{selectedAsset.commisionedDate}}</span>
											</div>
										</div>
										<div class="col-sm-4 border-right">
											<div class="description-block">
												<h5 class="description-header">Decomissioned Date</h5>
												<span class="description-text">{{selectedAsset.deCommissionedDate}}</span>
											</div>
										</div>
									</div>
								</div>
							</div>
							</div>
						</div>
					</div>
				</div>
		</section>
		

	</div>
</div>
</html>