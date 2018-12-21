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

<style>
.main-box.no-header {
	padding-top: 20px;
}

.table tbody tr.currentSelected {
	background-color: rgba(60, 141, 188, 0.58) !important;
}

.table th {
	background: none repeat scroll 0 0 #0077BF !important;
}

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
			<div class="row">
				<div class="col-md-12">
					<div class="alert alert-success alert-dismissable"
						id="successMessageDiv"
						style="display: none; height: 34px; white-space: nowrap;">
						<!-- <button type="button" class="close" >x</button> -->
						<strong>Success! </strong> {{successMessage}} <a href><span
							class="messageClose" ng-click="closeMessageWindow()">X</span></a>
					</div>
					<div class="alert alert-info alert-dismissable" id="infoMessageDiv"
						style="display: none; height: 34px; white-space: nowrap;">
						<!-- <button type="button" class="close" >x</button> -->
						<strong>Info! </strong> {{InfoMessage}} <a href><span
							class="messageClose" ng-click="closeMessageWindow()">X</span></a>
					</div>
				</div>
			</div>
		</section>

		<section class="content">
			<div class="row">
				<div class="col-md-12">
					<div class="box" style="height: 70%">
						<div class="box-header with-border">
							<h3 class="box-title">Registered Service Provider Activities</h3>

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

						<div class="box-body" style="height: 50%">
							<div class="row">
								<div class="col-md-12">
									<div class="nav-tabs-custom">
										<ul class="nav nav-tabs">
											<li><a href="#ticketsTab" onclick="getIncidents()"
												data-toggle="tab">Incidents</a> <!-- <span class="label label-warning"
												style="position: relative; top: -50px; left: 80px;">{{spCustomerList.length
													|| 0}}</span></li> -->
											<li><a href="#sitesTab" onclick="getSiteData()"
												data-toggle="tab">Sites</a> <!-- <span class="label label-warning"
												style="position: relative; top: -50px; left: 50px;">{{spCustomerList.length
													|| 0}}</span></li> -->
											<li><a href="#assetsTab" onclick="getAssetData()"
												data-toggle="tab">Assets</a> <!-- <span class="label label-warning"
												style="position: relative; top: -50px; left: 70px;">{{spCustomerList.length
													|| 0}}</span></li> -->
										</ul>


										<div class="tab-content">
											<div class="tab-pane active" id="ticketsTab">
													<div class="row">
													<div class="col-md-3">
															<select name="spCustomerListSelect" id="spCustomerListSelect"
																class="form-control" onchange="angular.element(this).scope().getCustomerIncident(this, event, 'spCustomerListSelect')"
																required>
															</select>
						
														</div>
														<div class="col-md-3">
														<div class="funkyradio">
															<div class="funkyradio-primary">
																<input type="radio" name="radio" id="radio3" ng-model="ticketCreatedOrAssigned" value="CUSTOMER" 
																ng-change="checkTicketsAssignedOrCreated(ticketCreatedOrAssigned)"/> <label
																	for="radio3"   >
																	Tickets Created by Customer
																	</label>
															</div>
														</div>
													</div>
													<div class="col-md-3">
														<div class="funkyradio">
															<div class="funkyradio-primary">
																<input type="radio" name="radio" id="radio2" ng-model="ticketCreatedOrAssigned" value="RSP" 
																ng-change="checkTicketsAssignedOrCreated(ticketCreatedOrAssigned)"/> <label
																	for="radio2" >Tickets Created by Company</label>
															</div>
														</div>
													</div>
													
													
														<a href="${webContextPath}/serviceprovider/rsp/incident/create" class="btn btn-primary pull-right " >Create Ticket </a></li>
													</div>	
														<div class="row">
															<div class="col-md-12">
																<div style="overflow-x: hidden; overflow-y: auto; height: 110%">
																	<div class="table-responsive">
																		<table id="incidentDetails"
																			class="table table-bordered table-striped"
																			cellspacing="0">
																		</table>
																	</div>
																</div>
															</div>
														</div>
												</div>

											<div class="tab-pane" id="sitesTab">
												
												<div class="row">

													<div class="col-md-6">

														<div class="box">
															<div class="box-header with-border">
																<h3 class="box-title">List of Sites</h3>
															</div>
															<div class="box-body"
																style="height: 65%; overflow-y: visible; overflow-x: hidden">
																<div class="row">
																	<div class="col-md-12">
																		<input type="text" class="form-control"
																			placeholder="Search Site" ng-model="searchSite">
																	</div>
																	<div class="col-md-12" ng-if="siteList.length> 0">
																		<ul class="products-list product-list-in-box">
																			<li class="item"
																				ng-repeat="site in siteList | filter:searchSite | orderBy:'siteName'">
																				<div class="product-img" style="margin-top: -10px;">
																					<img
																						src="${contextPath}/resources/theme1/img/site-icon.png"
																						alt="Product Image">
																				</div>
																				<div class="product-info">
																					<div class="col-md-9">
																						<a href="javascript:void(0)"
																							ng-click="getSiteDetails(site); viewImage()"
																							class="product-title">{{site.siteName}} </a> <span
																							class="product-description">
																							{{site.fullAddress}} </span>
																					</div>
																					<div class="col-md-3">
																						<span class="badge pull-right"
																							style="background-color: green; color: white">
																							{{sitePage.brand}} [{{site.brandName}}] </span>
																					</div>
																					<div class="col-sm-4">
																						<i class="fa fa-map-marker" aria-hidden="true"
																							ng-if="site.district.districtName!=null"></i>
																						{{site.district.districtName || ''}}
																					</div>
																					<div class="col-sm-4">
																						<i class="fa fa-map-marker" aria-hidden="true"
																							ng-if="site.area.areaName!=null"></i>
																						{{site.area.areaName || ''}}
																					</div>
																					<div class="col-sm-4">
																						<i class="fa fa-map-marker" aria-hidden="true"
																							ng-if="site.cluster.clusterName!=null"></i>
																						{{site.cluster.clusterName || ''}}
																					</div>
																				</div>

																			</li>

																		</ul>
																	</div>


																</div>
															</div>

															<div class="box-footer">
																<div class="row">
																	<div class="col-sm-3 col-xs-6">
																		<div class="description-block border-right">
																			<a class="btn btn-danger">Total Sites : <span
																				class="badge">{{siteList.length}}</span></a>
																		</div>
																	</div>
																	<!-- /.col -->
																	<div class="col-sm-3 col-xs-6">
																		<div class="description-block border-right"></div>
																	</div>
																	<!-- /.col -->
																	<div class="col-sm-3 col-xs-6">
																		<div class="description-block border-right"></div>
																	</div>
																	<!-- /.col -->
																	<div class="col-sm-3 col-xs-6">
																		<div class="description-block">
																			<!-- 	<a href class="btn btn-info"><i class="fa fa-user" arial-hidden="true"></i> Add User</a> -->
																		</div>
																	</div>
																</div>
															</div>
														</div>

													</div>

													<div class="col-md-6">
														<div class="box">
															<div class="box-header with-border">
																<h3 class="box-title">Site Detail</h3>

															</div>
															<div class="box-body" style="overflow-y:auto;overflow-x:hidden;height:72%">
																<div class="row">
																	<div class="col-md-12">
																		<div class="table-responsive">
																			<table class="table no-margin">
																				<tr>
																					<td><i class="fa fa-sitemap"
																						aria-hidden="true"></i> Site Name</td>
																					<td class="pull-right">{{selectedSite.siteName}}</td>
																				</tr>
																				<tbody>
																					<tr>
																						<td><i class="fa fa-building"
																							aria-hidden="true"></i> Site Operator</td>
																						<td align="right">{{selectedSite.retailerName}}</td>
																					</tr>
																					<tr>
																						<td><i class="fa fa-building"
																							aria-hidden="true"></i> Site Owner</td>
																						<td align="right">{{sessionUser.company.companyName}}</td>
																					</tr>
																					<tr>
																						<td><i class="fa fa-bolt" aria-hidden="true"></i>
																							Electricity Id (MPAN)</td>
																						<td align="right">{{selectedSite.electricityId}}</td>
																					</tr>
																					<tr>
																						<td><i class="fa fa-sitemap"
																							aria-hidden="true"></i> Site ID Number 1</td>
																						<td align="right">{{selectedSite.siteNumber1}}</td>
																					</tr>
																					<tr>
																						<td><i class="fa fa-sitemap"
																							aria-hidden="true"></i> Site ID Number 2</td>
																						<td align="right">{{selectedSite.siteNumber2}}</td>
																					</tr>
																					<tr>
																						<td><i class="fa fa-area-chart"
																							aria-hidden="true"></i> Sales Area Size (M<sup>2</sup>)</td>
																						<td align="right">{{selectedSite.salesAreaSize}}</td>
																					</tr>

																				</tbody>
																			</table>
																		</div>
																	</div>
																	<div class="col-md-12">
																		<div class="box box-danger">
																			<div class="box-header with-border">
																				<h3 class="box-title">
																					<i class="fa fa-picture-o" aria-hidden="true"></i>
																					Attachments
																				</h3>
																				<a class="users-list-name"
																					href="javascript:void(0);"></a>
																				<div class="box-tools"></div>
																			</div>
																			<!-- /.box-header -->
																			<div class="box-body no-padding">
																				<input type="hidden" id="siteImg"
																					value="${contextPath}/selected/file/download?keyname={{selectedSite.fileInput}}">
																				<div class="col-md-12">
																					<div id="noimage"
																						ng-if="selectedSite.fileInput==null">
																						<img
																							src="${contextPath}/resources/theme1/img/no-available-image.png"
																							style="width: 50%"></img>
																					</div>
																				</div>
																			</div>
																			<div class="box-footer"
																				ng-if="selectedSite.fileInput!=null">
																				<a
																					href="${contextPath}/selected/file/download?keyname={{selectedSite.fileInput}}"
																					class="uppercase" download> <i
																					class="fa fa-cloud-download fa-2x"
																					aria-hidden="true"></i></a> <a href
																					ng-click="deleteFile('SITE', selectedSite)"
																					data-toggle="tooltip"
																					data-original-title="Delete this file"> <i
																					class="fa fa-remove fa-2x" aria-hidden="true"></i></a>
																			</div>
																			<!-- /.box-footer -->
																		</div>
																	</div>
																</div>


																<div class="box">
																	<div class="box-header with-border">
																		<h3 class="box-title">
																			<i class="fa fa-bars" aria-hidden="true"></i> Contact
																			Information
																		</h3>

																	</div>
																	<div class="box-body">

																		<div class="table-responsive">
																			<table class="table no-margin">
																				<!-- <thead> -->
																				<tr>
																					<td><i class="fa fa-user" aria-hidden="true"></i>
																						Name</tdd>
																					<td align="right">{{selectedSite.contactName}}</td>
																				</tr>
																				<tr>
																					<td><i class="fa fa-envelope"
																						aria-hidden="true"></i> Email</td>
																					<td align="right">{{selectedSite.email}}</td>
																				</tr>
																				<tr>
																					<td><i class="fa fa-building-o"></i> Address</td>
																					<td align="right">{{selectedSite.siteAddress}}
																					</td>
																				</tr>
																				<tr>
																					<td><i class="fa fa-map-marker"
																						aria-hidden="true"></i> Latitude</td>
																					<td align="right">{{selectedSite.latitude}}</td>
																				</tr>
																				<td><i class="fa fa-map-marker"
																					aria-hidden="true"></i> Longitude</td>
																				<td align="right">{{selectedSite.longitude}}</td>
																				</tr>
																				<tr>
																					<td><i class="fa fa-phone-square"
																						aria-hidden="true"></i> Contact Number</td>
																					<td align="right">{{selectedSite.primaryContact}}<br>
																						{{selectedSite.secondaryContact || ''}}
																					</td>
																				</tr>
																				</tr>
																				<!--  </thead> -->

																				</tbody>
																			</table>
																		</div>
																		<!-- /.table-responsive -->
																	</div>
																</div>

																<div class="box">
																	<div class="box-header with-border">
																		<h3 class="box-title">License Information</h3>

																	</div>
																	<div class="box-body">

																		<div class="table-responsive">
																			<table class="table no-margin">
																				<thead>
																					<tr>
																						<th>Name</th>
																						<th>Valid From</th>
																						<th>Valid To</th>
																						<th>Attachment</th>
																					</tr>
																				</thead>
																				<tbody>
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
																		<!-- /.table-responsive -->
																	</div>



																	<div class="box-footer">
																		<div class="row">
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block border-right"></div>
																				<!-- /.description-block -->
																			</div>
																			<!-- /.col -->
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block border-right"></div>
																				<!-- /.description-block -->
																			</div>
																			<!-- /.col -->
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block border-right"></div>
																				<!-- /.description-block -->
																			</div>
																			<!-- /.col -->
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block"></div>
																				<!-- /.description-block -->
																			</div>
																		</div>
																	</div>
																</div>

																<div class="box">
																	<div class="box-header with-border">
																		<h3 class="box-title">Operation Timings</h3>

																	</div>
																	<div class="box-body">
																		<div class="row">
																			<div class="col-md-6">
																				<div class="table-responsive"
																					style="overflow-x: hidden">
																					<table class="table no-margin">
																						<thead>
																							<tr>
																								<th>Weekdays</th>
																								<th>Sales</th>
																							</tr>
																						</thead>
																						<tbody>

																							<tr
																								ng-repeat="timing in selectedSite.SalesOperation">
																								<td>{{timing.days}}</td>
																								<td
																									ng-if="timing.from == '00:00' && timing.to == '00:00'">
																									<sec:authorize
																										access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
																										<a href data-toggle="modal"
																											ng-click="updateSiteModal(selectedSite)">
																											<span class="label label-warning">{{timing.from}}
																												- {{timing.to}}</span>
																										</a>
																									</sec:authorize> <sec:authorize
																										access="hasAnyRole('ROLE_SITE_STAFF')">
																										<a href data-toggle="modal"> <span
																											class="label label-warning">{{timing.from}}
																												- {{timing.to}}</span></a>
																									</sec:authorize>
																								</td>
																								<td
																									ng-if="timing.from == '00:00' && timing.to != '00:00'">
																									<sec:authorize
																										access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
																										<a href data-toggle="modal"
																											ng-click="updateSiteModal(selectedSite)">
																											<span class="label label-danger"> <i
																												class="fa fa-exclamation-triangle"
																												aria-hidden="true"></i> {{timing.from}} -
																												{{timing.to}}
																										</span>
																										</a>
																									</sec:authorize> <sec:authorize
																										access="hasAnyRole('ROLE_SITE_STAFF')">
																										<a href data-toggle="modal"> <span
																											class="label label-warning">{{timing.from}}
																												- {{timing.to}} </span></a>
																									</sec:authorize>

																								</td>
																								<td
																									ng-if="timing.from != '00:00' && timing.to == '00:00'">
																									<sec:authorize
																										access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
																										<a href data-toggle="modal"
																											ng-click="updateSiteModal(selectedSite)">
																											<span class="label label-danger"> <i
																												class="fa fa-exclamation-triangle"
																												aria-hidden="true"></i>{{timing.from}} -
																												{{timing.to}}
																										</span>
																										</a>
																									</sec:authorize> <sec:authorize
																										access="hasAnyRole('ROLE_SITE_STAFF')">
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
																			<div class="col-md-6">
																				<div class="table-responsive">
																					<table class="table no-margin">
																						<thead>
																							<tr>
																								<th>Weekdays</th>

																								<th>Delivery</th>

																							</tr>
																						</thead>
																						<tbody>
																							<tr
																								ng-repeat="timing in selectedSite.DeliveryOperation">
																								<td>{{timing.days}}</td>
																								<td
																									ng-if="timing.from == '00:00' && timing.to == '00:00'">
																									<sec:authorize
																										access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
																										<a href data-toggle="modal"
																											ng-click="updateSiteModal(selectedSite)">
																											<span class="label label-warning">{{timing.from}}
																												- {{timing.to}}</span>
																										</a>
																									</sec:authorize> <sec:authorize
																										access="hasAnyRole('ROLE_SITE_STAFF')">
																										<a href data-toggle="modal"> <span
																											class="label label-warning">{{timing.from}}
																												- {{timing.to}}</span></a>
																									</sec:authorize>
																								</td>
																								<td
																									ng-if="timing.from == '00:00' && timing.to != '00:00'">
																									<sec:authorize
																										access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
																										<a href data-toggle="modal"
																											ng-click="updateSiteModal(selectedSite)">
																											<span class="label label-danger"> <i
																												class="fa fa-exclamation-triangle"
																												aria-hidden="true"></i> {{timing.from}} -
																												{{timing.to}}
																										</span>
																										</a>
																									</sec:authorize> <sec:authorize
																										access="hasAnyRole('ROLE_SITE_STAFF')">
																										<a href data-toggle="modal"> <span
																											class="label label-warning">{{timing.from}}
																												- {{timing.to}} </span></a>
																									</sec:authorize>

																								</td>
																								<td
																									ng-if="timing.from != '00:00' && timing.to == '00:00'">
																									<sec:authorize
																										access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
																										<a href data-toggle="modal"
																											ng-click="updateSiteModal(selectedSite)">
																											<span class="label label-danger"> <i
																												class="fa fa-exclamation-triangle"
																												aria-hidden="true"></i>{{timing.from}} -
																												{{timing.to}}
																										</span>
																										</a>
																									</sec:authorize> <sec:authorize
																										access="hasAnyRole('ROLE_SITE_STAFF')">
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

																	</div>



																	<div class="box-footer">
																		<div class="row">
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block border-right"></div>
																				<!-- /.description-block -->
																			</div>
																			<!-- /.col -->
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block border-right"></div>
																				<!-- /.description-block -->
																			</div>
																			<!-- /.col -->
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block border-right"></div>
																				<!-- /.description-block -->
																			</div>
																			<!-- /.col -->
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block"></div>
																				<!-- /.description-block -->
																			</div>
																		</div>
																	</div>
																</div>
																<div class="box">
																	<div class="box-header with-border">
																		<h3 class="box-title">Submeter Details</h3>
																		
																	</div>
																	<div class="box-body">

																		<div class="table-responsive">
																			<table class="table no-margin">
																				<thead>
																					<tr>
																						<th>Submeter Number/Electricity Id (MPAN)</th>
																						<th>User</th>

																					</tr>
																				</thead>
																				<tbody
																					ng-repeat="submeter in selectedSite.submeterDetails">
																					<tr>
																						<td>{{submeter.subMeterNumber}}</td>
																						<td>{{submeter.subMeterUser}}</td>
																					</tr>
																				</tbody>
																			</table>
																		</div>
																		<!-- /.table-responsive -->
																	</div>



																	<div class="box-footer">
																		<div class="row">
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block border-right"></div>
																				<!-- /.description-block -->
																			</div>
																			<!-- /.col -->
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block border-right"></div>
																				<!-- /.description-block -->
																			</div>
																			<!-- /.col -->
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block border-right"></div>
																				<!-- /.description-block -->
																			</div>
																			<!-- /.col -->
																			<div class="col-sm-3 col-xs-6">
																				<div class="description-block"></div>
																				<!-- /.description-block -->
																			</div>
																		</div>
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
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
									</div>
								</div>
							</div>
							</div>
						</div>
							
						</div>
												
												
												
												
												
												
											</div>


											<div class="tab-pane" id="assetsTab">
												<div class="row">

													<div class="col-md-6">
														<div class="box">
															<div class="box-header with-border">
																<h3 class="box-title">List of Assets and Services</h3>
																
															</div>
															<div class="box-body"
																style="overflow-y: auto; overflow-x: hidden; height: 58%">
																<div class="row">
																	<div class="col-md-12">
																		<input type="text" class="form-control"
																			placeholder="Search Asset" ng-model="searchAsset">
																		<input type="hidden" class="form-control" id="siteId"
																			value="${siteId}">
																	</div>
																</div>
																<div class="row">
																	<div class="col-md-12">
																		<div class="row">
																			<div class="col-md-12">
																				<div class="table-responsive">
																					<table id="example1" class="table table-bordered">
																						<thead>
																							<tr>
																								<th>Name</th>
																								<th>Category</th>
																								<th>Site</th>
																							</tr>
																						</thead>
																						<tbody>
																							<tr
																								ng-repeat="asset in asset.list | filter: searchAsset | orderBy :'assetName'"
																								ng-class="{currentSelected:$index == selectedRow}"
																								ng-click="getAssetDetails(asset);rowHighilited($index)">
																								<td><a href> <i class="fa fa-cubes"
																										aria-hidden="true"></i> {{asset.assetName}}
																								</a></td>
																								<td>{{asset.assetCategoryName}}</td>
																								<td>{{asset.siteName}}</td>
																							</tr>
																						</tbody>
																					</table>
																				</div>
																			</div>
																		</div>
																	</div>

																</div>
															</div>

															<div class="box-footer">
																<div class="row">
																	<div class="col-sm-3 col-xs-6">
																		<div class="description-block border-right">
																			<a class="btn btn-danger">Total Assets : <span
																				class="badge">{{asset.list.length}}</span></a>
																		</div>
																	</div>
																	<!-- /.col -->
																	<div class="col-sm-3 col-xs-6">
																		<div class="description-block border-right"></div>
																	</div>
																	<!-- /.col -->
																	<div class="col-sm-3 col-xs-6">
																		<div class="description-block border-right"></div>
																	</div>
																	<!-- /.col -->
																	<div class="col-sm-3 col-xs-6">
																		<div class="description-block">
																			<!-- 	<a href class="btn btn-info"><i class="fa fa-user" arial-hidden="true"></i> Add User</a> -->
																		</div>
																	</div>
																</div>
															</div>
														</div>

													</div>




													<div class="col-md-6">
														<div class="box">
															<div class="box-header with-border">
																<h3 class="box-title">Asset / Service Detail</h3>																
															</div>
															<div class="box-body"
																style="overflow-y: auto; overflow-x: hidden; height: 58%">
																<div class="row">
																	<div class="col-md-12">
																		<div class="table-responsive">
																			<table class="table no-margin"
																				ng-if="selectedAsset.assetType == 'E' ">
																				<!-- <thead> -->
																				<tr>
																					<td style="width: 50%">Name</td>
																					<td align="right">{{selectedAsset.assetName}}</td>
																				</tr>
																				<tr>
																					<td style="width: 50%"><span>Asset
																							Code</span></td>
																					<td align="right">{{selectedAsset.assetCode}}</td>
																				</tr>
																				<!-- </thead> -->
																				<tbody>
																					<tr>
																						<td>Model</td>
																						<td align="right">{{selectedAsset.modelNumber}}</td>
																					</tr>
																					<tr>
																						<td>Category</td>
																						<td align="right">{{selectedAsset.assetCategoryName}}</td>
																					</tr>
																					<tr>
																						<td>Component Type</td>
																						<td align="right">{{selectedAsset.assetSubcategory1}}</td>
																					</tr>
																					<!-- <tr><td>SubComponent Type</td><td align="right">{{selectedAsset.assetSubcategory2}}</td></tr> -->
																					<tr>
																						<td>Service Provider</td>
																						<td align="right">{{selectedAsset.serviceProviderName}}</td>
																					</tr>
																					<tr>
																						<td>Location</td>
																						<td align="right">{{selectedAsset.locationName}}</td>
																					</tr>
																					<tr>
																						<td>Date of Commission</td>
																						<td align="right">{{selectedAsset.commisionedDate}}</td>
																					</tr>
																					<tr>
																						<td>Date of DeCommission</td>
																						<td align="right">{{selectedAsset.deCommissionedDate}}</td>
																					</tr>
																					<tr>
																						<td>Site</td>
																						<td align="right">{{selectedAsset.siteName}}</td>
																					</tr>
																					<tr>
																						<td>Content</td>
																						<td align="right">{{selectedAsset.content}}</td>
																					</tr>

																					<tr>
																						<td>Is Asset Electrical</td>
																						<td align="right"><span
																							ng-if="selectedAsset.isAssetElectrical=='YES'"
																							style="color: #16db16;"> <i
																								class="fa fa-check-circle-o fa-2x"
																								aria-hidden="true"></i></span> <span
																							ng-if="selectedAsset.isAssetElectrical=='NO' || selectedAsset.isAssetElectrical == NULL "
																							style="color: red"> <i
																								class="fa fa-times-circle-o fa-2x"
																								aria-hidden="true"></i></span></td>
																					</tr>

																					<tr>
																						<td>Is Power Sensor Attached</td>
																						<td align="right"><span
																							ng-if="selectedAsset.isPWSensorAttached=='YES'"
																							style="color: #16db16;"> <i
																								class="fa fa-check-circle-o fa-2x"
																								aria-hidden="true"></i></span> <span
																							ng-if="selectedAsset.isPWSensorAttached=='NO' ||  selectedAsset.isPWSensorAttached == NULL"
																							style="color: red"> <i
																								class="fa fa-times-circle-o fa-2x"
																								aria-hidden="true"></i></span></td>
																					</tr>

																					<tr>
																						<td>Sensor Number</td>
																						<td align="right">
																							{{selectedAsset.pwSensorNumber}}</td>
																					</tr>
																					<tr>
																						<td>Attached Picture</td>
																						<td align="right"
																							ng-if="selectedAsset.imagePath!=null"><a
																							href="${contextPath}/selected/file/download?keyname={{selectedAsset.imagePath}}"
																							download> <i class="fa fa-picture-o fa-2x"
																								aria-hidden="true"></i></a> <a href
																							ng-click="deleteFile('ASSET', selectedAsset,'IMG')"
																							data-toggle="tooltip"
																							data-original-title="Delete this file"> <i
																								class="fa fa-remove fa-2x" aria-hidden="true"></i></a>
																						</td>
																					</tr>
																					<tr>
																						<td>Attached Additional Document</td>
																						<td align="right"
																							ng-if="selectedAsset.documentPath!=null"><a
																							href="${contextPath}/selected/file/download?keyname={{selectedAsset.documentPath}}"
																							download><i class="fa fa-file-text fa-2x"
																								aria-hidden="true"></i></a>
																						</td>
																					</tr>
																				</tbody>
																			</table>

																			<table class="table no-margin"
																				ng-if="selectedAsset.assetType == 'S' ">
																				<thead>
																					<tr>
																						<td style="width: 40%">Name</td>
																						<td align="right">{{selectedAsset.assetName}}</td>
																					</tr>
																					<tr>
																						<td style="width: 40%">Service Code</td>
																						<td align="right">{{selectedAsset.assetCode}}</td>
																					</tr>
																				</thead>
																				<tbody>
																					<tr>
																						<td>Category</td>
																						<td align="right">{{selectedAsset.assetCategoryName}}</td>
																					</tr>
																					<tr>
																						<td>Component Type</td>
																						<td align="right">{{selectedAsset.assetSubcategory1}}</td>
																					</tr>
																					<tr>
																						<td>Location</td>
																						<td align="right">{{selectedAsset.locationName}}</td>
																					</tr>
																					<tr>
																						<td>Service Provider</td>
																						<td align="right">{{selectedAsset.serviceProviderName}}</td>
																					</tr>
																					<tr>
																						<td>Service contract start date</td>
																						<td align="right">{{selectedAsset.commisionedDate}}</td>
																					</tr>
																					<tr>
																						<td>Service contract end date</td>
																						<td align="right">{{selectedAsset.deCommissionedDate}}</td>
																					</tr>
																					<tr>
																						<td>Site</td>
																						<td align="right">{{selectedAsset.siteName}}</td>
																					</tr>
																					<tr>
																						<td>Attached Additional Document</td>
																						<td align="right"
																							ng-if="selectedAsset.documentPath!=null"><a
																							href="${contextPath}/selected/file/download?keyname={{selectedAsset.documentPath}}"
																							download> <i class="fa fa-file-text fa-2x"
																								aria-hidden="true"></i></a>
																						</td>
																					</tr>
																				</tbody>
																			</table>
																		</div>
																	</div>
																</div>
																<div class="row">
																	<div class="col-md-12">
																		<div class="box-header with-border">
																			<h3 class="box-title" style="margin-left: -12px">Description</h3>
																		</div>
																	</div>
																	<div style="margin-left: 10px">
																		<div class="col-md-12">
																			{{selectedAsset.assetDescription}}</div>
																	</div>
																</div>
															</div>



															<div class="box-footer">
																<div class="row">
																	<div class="col-sm-4 col-xs-6">
																		<!-- <div class="description-block border-right">
																			<a href class="btn btn-success"
																				ng-if="selectedAsset.assetType == 'S' ">Asset
																				type <span class="badge">SERVICE</span>
																			</a> <a href class="btn btn-success"
																				ng-if="selectedAsset.assetType == 'E' ">Asset
																				type <span class="badge">EQUIPMENT</span>
																			</a>
																		</div> -->
																		<!-- /.description-block -->
																	</div>
																	<!-- /.col -->
																	<div class="col-sm-4 col-xs-6">
																		<div class="description-block border-right"></div>
																		<!-- /.description-block -->
																	</div>
																	<!-- /.col -->

																	<!-- /.col -->
																	<div class="col-sm-4 col-xs-6">
																		<div class="description-block">

																			<!-- <span class="pull-right"> Mark for deletion <input
																				id="toggledelete" type="checkbox"
																				class="toggleYesNo form-control" data-width="70"
																				data-toggle="toggle" data-size="small"
																				data-off="Active" data-on="Delete"
																				data-onstyle="danger" data-offstyle="success"
																				onchange="angular.element(this).scope().isDelete(this, event )">
																			</span> -->
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
							</div>
						</div>


						

					</div>
				</div>
			</div>
		</section>

	</div>
</div>
</html>