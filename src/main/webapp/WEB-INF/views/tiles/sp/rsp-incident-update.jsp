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

<link rel="stylesheet" media="screen"
	href='<c:url value="/resources/theme1/css/bootstrap-datetimepicker-standalone.css"></c:url>' />
<link rel="stylesheet" media="screen"
	href='<c:url value="/resources/theme1/css/bootstrap-datetimepicker.css"></c:url>' />
<link rel="stylesheet" media="screen"
	href='<c:url value="/resources/css/bootstrap-datetimepicker.min.css"></c:url>' />
<link href='<c:url value="/resources/theme1/font-awesome-4.7.0/css/font-awesome.min.css"></c:url>'  rel="stylesheet">
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/incident-modal.css"></c:url>' />

<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.min.js"></c:url>'></script>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/optionbtn.css"></c:url>' />
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
 <link rel="stylesheet" href='<c:url value="/resources/theme1/css/baguetteBox.min.css"></c:url>' />
  <link rel="stylesheet" href='<c:url value="/resources/theme1/css/thumbnail-gallery.css"></c:url>' />  
 <script type="text/javascript" src='<c:url value="/resources/theme1/js/baguetteBox.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/sp-incident-create-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/site-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/asset-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/service-provider-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>



<style>
.main-box.no-header {
	padding-top: 20px;
}

.table tbody tr.currentSelected {
	background-color: rgba(60, 141, 188, 0.58) !important;
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

<script>
	$(function() {

		$('input').attr('autocomplete', 'off');
		
		$('#datetimepicker1').datetimepicker({
			format : 'DD-MM-YYYY HH:MM A',
		});
	
		//Added by Supravat for allowing SLA Due Date to update on incident update screen.
		var dateObject = new Date();
       	var todayDate = new Date(dateObject.getFullYear(), dateObject.getMonth(), dateObject.getDate());
		//Ended by Supravat.
		$('#datetimepicker2').datetimepicker({
			format : 'DD-MM-YYYY HH:mm',
			minDate: todayDate//Added by Supravat for allowing SLA Due Date to update on incident update screen.
		});
		$('#datetimepicker3').datetimepicker({
			format : 'DD-MM-YYYY HH:MM A',
		});
		$('input').attr('autocomplete', 'off');
		 $('.toggle-on').removeAttr('style');
		 $('.toggle-off').removeAttr('style');
		 $(".dt1").datepicker({
	         format:"dd-mm-yyyy",
	         autoclose: true
	     })
		/* $('#ticketList').DataTable({
			"processing": true
			
		}); */

		/* $('.toggle-on').removeAttr('style');
		$('.toggle-off').removeAttr('style');
 */
		$('.select2').select2();

		var isIE = window.ActiveXObject || "ActiveXObject" in window;
		if (isIE) {
			$('.modal').removeClass('fade');
		}

		$("[data-toggle=popover]").popover({
			container : 'body',
			html : true,
			content : function() {
				var content = $(this).attr("data-popover-content");
				//alert($(content).children(".popover-body").html());
				return $(content).children(".popover-body").html();
			}
		});

	})

	function validate_tab(thisform) {

		$('.tab-pane input, .tab-pane select, .tab-pane textarea').on(
				'invalid', function() {

					// Find the tab-pane that this element is inside, and get the id
					var $closest = $(this).closest('.tab-pane');
					var id = $closest.attr('id');
					//alert(id);
					// Find the link that corresponds to the pane and have it show
					$('.nav a[href="#' + id + '"]').tab('show');
				});
	}
</script>
</head>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<div class="content-wrapper">
	<div ng-controller="spIncidentCreateController" id="spIncidentCreateWindow">
		<section class="content" style="min-height: 35px; display: none"
			id="messageWindow">
			<div class="row">
				<div class="col-md-12">
					<!-- <div class="alert alert-success alert-dismissable"
						id="successMessageDiv"
						style="display: none; height: 34px; white-space: nowrap;">
						<button type="button" class="close" >x</button>
						<strong>Success! </strong> {{successMessage}} <a href><span
							class="messageClose" ng-click="closeMessageWindow()">X</span></a>
					</div>
					<div class="alert alert-danger alert-dismissable"
						id="errorMessageDiv"
						style="display: none; height: 34px; white-space: nowrap;">
						<button type="button" class="close" >x</button>
						<strong>Error! </strong> {{errorMessage}} <span
							id="fileerrorincident"></span> <a href><span
							class="messageClose" ng-click="closeMessageWindow()">X</span></a>
					</div> -->
					<!-- <div class="alert alert-danger alert-dismissable" id="errorMessageDiv"
							style="display: none;  height: 34px;white-space: nowrap;">
							<button type="button" class="close">x</button>
							<strong>Error! </strong> {{errorMessage}} <span class="messageClose" ng-click="closeMessageWindow()">X</span>
						</div> -->
				</div>
			</div>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-md-12">
					<div class="box">
						<div class="box-header with-border">
							<h3 class="box-title">
								<a href="${contextPath}/serviceprovidercompany/customers"
									title="View All Incidents"> <i class="fa fa-th-list"
									aria-hidden="true"></i></a> Update Incident -
								{{ticketData.ticketNumber}}
							</h3>
							<div class="box-tools pull-right" style="margin-top: 0px;">
								<input type="hidden" id="mode" value="${mode}">
								<div class-"row">
								<div class="col-md-3">
						 <span class="badge">{{ticketData.mappedCustomer}}</span>
       					 </div>
					<%-- 	<div class="col-md-3">
						<a  href="${contextPath}/serviceprovider/rsp/incident/update" data-toggle="tooltip" data-original-title="Refresh"> 
						 <span  style="font-size:12px;" >
						 <i class="fa fa-refresh fa-2x" style="margin-right: 17px;" aria-hidden="true"></i>
       					 </span>
       					 </a>
       					 </div>
						<div class="col-md-6 round hollow text-center">
						<a href ng-click="openChatBox();" id="addClass" class="pull-right" ><span class="glyphicon glyphicon-comment"></span>&nbsp;Add Worknote </a>
       				 	</div> --%>
       				 		<div class="col-md-3">
						<a  href="${contextPath}/serviceprovider/rsp/incident/update" data-toggle="tooltip" data-original-title="Refresh"> 
						 <span  style="font-size:12px; margin-right:40px" >
						 <i class="fa fa-refresh fa-2x" aria-hidden="true"></i>
       					 </span>
       					 </a>
       					 </div>
						<div class="col-md-6">
						<a href ng-click="openChatBox();" id="addClass" class="pull-right" >
						<i class="fa fa-comments fa-2x" aria-hidden="true"></i></a>
							</div>
							</div>
						</div>
						<div class="box-body" style="height:450px">
							<div class="row">
								<div class="col-md-12">
									<div class="nav-tabs-custom">
										<ul class="nav nav-tabs">
											<li class="active"><a href="#primaryinfo"
												data-toggle="tab">Primary Details</a></li>
											<li><a href="#linkedticket" data-toggle="tab">Linked Tickets</a>
										<span class="label label-warning" style="position: relative;
        top: -33px; left: 211px">{{ticketData.linkedTickets.length || 0}}</span></a>			
													</li>
											 <li><a href="#escalate" data-toggle="tab"
												onclick="initializeEscalateTicket()">Escalation</a></li> 
											<!--  Added by Supravat -->
											<li><a href="#financials" data-toggle="tab">Financials
											<span class="label label-warning" style="position: relative;
    top: -8px;left: 0px;">{{ticketData.financialList.length || 0}}</span></a>
												
											</li>
											<!--  Ended by Supravat -->	
											<li><a href="#attachments" onclick="angular.element(this).scope().getAttachments()" data-toggle="tab">Attachments
													<span class="label label-warning" style="position: relative;
    top: -8px;">{{ticketData.files.length || 0}}</span>
											</a></li>
											<li><a href="#tickethistory" data-toggle="tab"
												onclick="getTicketHistory()">Ticket History</a></li>
											<!--  Added by Supravat for adding Related Tickets Tab-->
											<li><a href="#relatedTickets" ng-click="getRelatedTicketDetails(ticketData.ticketAssignedType)" data-toggle="tab">Related Tickets</a>
											<span class="label label-warning" style="position: relative;
    top: -33px;left: 110px;">{{relatedTicketData.length || 0}}</span>
											</li>
											<!--  Ended by Supravat -->
											
											<!-- Added by Supravat for adding Incident Task -->
											
											<li ng-show="ticketType=='RSP'"><a href="#incidentTask" ng-click="getIncidentTasks()" data-toggle="tab">Tasks</a>
											<span class="label label-warning" style="position: relative;
    top: -33px;left: 57px;">{{incidentTask.list.length || 0}}</span>
											</li>
											
											<!-- End -->
											
											
										</ul>
										<div class="tab-content">
											<div class="active tab-pane" id="primaryinfo">
											<div style="display: none" id="loadingDiv1">
			<div class="loader">Loading...</div>
		</div>
												<div class="row">
												<div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-aqua"><i class="fa fa-user middle" ></i></span>

            <div class="info-box-content">
              <span class="info-box-text">Raised By</span>
              <span class="info-box-number">{{ticketData.raisedBy}}</span>
               <a href="#" class="pull-right" style="margin-top: -43px;"
				data-toggle="popover" data-trigger="hover"
				data-html="true" data-popover-content="#raisedbypop"
				data-placement="left"> <span
				class="fa fa-info-circle fa-2x" aria-hidden="true"></span></a>
            </div>
          </div>
        </div>
        										<div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-red"><i class="fa fa-building middle"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">Assigned To</span>
              <span class="info-box-number">{{ticketData.assignedSP}}</span>
            </div>
          </div>
        </div>
        
        										<div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-green"><i class="fa fa-calendar middle"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">Raised On</span>
              <span class="info-box-number">{{ticketData.createdOn}}</span>
            </div>
          </div>
        </div>
        <div class="col-md-3 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-yellow"><i class="fa fa-cubes middle"></i></span>
            <div class="info-box-content">
              <span class="info-box-text">Asset</span>
              <span class="info-box-number">{{ticketData.assetName}}</span>
              <a href="#" class="pull-right" style="margin-top: -43px;"
				data-toggle="popover" data-trigger="hover"
				data-html="true" data-popover-content="#assetpop"
				data-placement="left"> <span
				class="fa fa-info-circle fa-2x" aria-hidden="true"></span></a>
            </div>
          </div>
        </div>
        </div>
        <div class="row">
        <form name="updateticketform" ng-submit="updateTicket()">
													<!-- <div class="col-lg-3 col-xs-6">
														<div class="small-box bg-yellow">
															<div class="inner">
																<p>
																	Raised BY <a href="#" class="pull-right"
																		data-toggle="popover" data-trigger="hover"
																		data-html="true" data-popover-content="#raisedbypop">
																		<span class="fa fa-info-circle fa-2x"
																		aria-hidden="true"></span>
																	</a>
																</p>
															</div>
															<a href="#" class="small-box-footer">
																{{ticketData.createdBy}} </a>
														</div>
													</div> -->
													<!-- <div class="col-lg-3 col-xs-6">
														<div class="small-box bg-yellow">
															<div class="inner">
																<p>Assigned To</p>
															</div>
															<a href="#" class="small-box-footer">
																{{ticketData.assignedSP}} </a>
														</div>
													</div> -->
												<!-- 	<div class="col-lg-3 col-xs-6">
														<div class="small-box bg-yellow">
															<div class="inner">
																<p>Raised On</p>
															</div>
															<a href="#" class="small-box-footer">
																{{ticketData.raisedOn}} </a>
														</div>
													</div>
													<div class="col-lg-3 col-xs-6">
														<div class="small-box bg-yellow">
															<div class="inner">
																<p>
																	Asset <a href="#" class="pull-right"
																		data-toggle="popover" data-trigger="hover"
																		data-html="true" data-popover-content="#assetpop"
																		data-placement="left"> <span
																		class="fa fa-info-circle fa-2x" aria-hidden="true"></span></a>
																</p>
															</div>
															<a href="#" class="small-box-footer">
																{{ticketData.assetName}} </a>
														</div>
													</div> -->
												
												<%-- <div class="row">
													<div class="col-md-12">
														SLA {{ticketData.slaPercent}} %
														<dl class="dl-horizontal">
															<div class="progress">
																<div class="progress-bar"
																	ng-class="{'progress-bar-danger': ticketData.slaPercent >=100, 'progress-bar-warning': ticketData.slaPercent>75 && ticketData.slaPercent<100, 'progress-bar-info': ticketData.slaPercent>0 && ticketData.slaPercent<75}"
																	role="progressbar"
																	ng-style="{width: ticketData.width+'%'}"></div> --%>

																  <%-- <div class="progress-bar" ng-class="{'progress-bar-danger': {{ticketData.slaPercent}}>=100}" role="progressbar" ng-style="{width:(ticketData.width+'%')}" ></div>
																 <div class="progress-bar progress-bar-warning" ng-if="ticketData.slaPercent > 75 && ticketData.slaPercent < 100 " ></div> 
			                      <div class="progress-bar progress-bar-info" ng-if="ticketData.slaPercent > 0 && ticketData.slaPercent <=75 " ></div> 
			                 

																 <div class="progress-bar progress-bar-danger active" role="progressbar" ng-if="ticketData.slaPercent >=100"
  								aria-valuemin="0" aria-valuemax="100" style="width:100%">
   								  SLA {{ticketData.slaPercent}} %
  								</div>
  								
  								<div class="progress-bar progress-bar-warning active" role="progressbar" ng-if="ticketData.slaPercent > 75 && ticketData.slaPercent < 100"
  								aria-valuemin="0" aria-valuemax="100" style="width: {{ticketData.slaPercent}} %">
   								  SLA {{ticketData.slaPercent}} %
  								</div>

																<div class="progress-bar progress-bar-danger active" 
  								role="progressbar"
  								aria-valuemin="0" aria-valuemax="100" style="width:30 %">
   								  SLA {{ticketData.slaPercent}} %
  								</div> --%>

													<!-- 		</div>
														</dl>
													</div>
												</div> -->
													<div class="col-md-8">
															<div class="row">
																<div class="col-md-12 reqDiv required">
																	<label class="control-label" for="title">Ticket
																		Title</label> <input type="text" class="form-control"
																		placeholder="Enter ticket title" name="title"
																		maxlength="50" ng-model="ticketData.ticketTitle"
																		required>

																</div>
																<div class="col-md-12">
																	<label class="control-label">Ticket
																		Description</label>
																	<textarea class="form-control"
																		style="width: 100%; height: 84px;" rows="3"
																		placeholder="Enter ticket description" name="title"
																		ng-model="ticketData.description"></textarea>
																</div>
																								
																								
													<div class="col-xs-12">
											          <div class="info-box">
											            <span class="info-box-icon bg-red">
											            <i class="fa fa-sitemap middle"></i></span>
											            <div class="info-box-content">
											              <span class="info-box-text">Site</span>
											              <span class="info-box-number wordspace">{{ticketData.siteName}}<br>
											              Address :  {{ticketData.siteAddress}} <br> Contact : {{ticketData.siteContact}}
											              </span>
											            </div>
											          </div>
       												 </div>
       												 </div>
       												 <div class="row" id="ticketCloseDiv">
     												 		<div class="col-xs-6 reqDiv required">
     												 		 <div class="row">
															<label class="control-label">Restoration confirmed by </label> 
															<input type="text" class="form-control"
																name="closedBy" id="closedBy" val="{{ticketData.closedBy}}"
																ng-model="ticketData.closedBy" readonly="readonly" autofocus >
															</div>
															 <div class="row">
															<!-- <div class="col-xs-4 reqDiv required"> -->
															<label class="control-label">Has issue been
																fixed permanently ?</label> <select name="closeCodeSelect"
																id="closeCodeSelect" class="form-control">
															</select>
															</div>
															<!-- <input type="text" id="closeCode" ng-model="ticketData.codeClosed" class="form-control">
															<input type="hidden" ng-model="selectedCloseCode.selected"> -->
														<!-- 	</div>	 -->
														</div>
														
														
														<div class="col-md-6 reqDiv required">

															<label class="control-label">Restoration notes</label>
															<textarea class="form-control"
																style="width: 100%; height: 92px;" rows="3"
																placeholder="Enter ticket close note" name="closeNote"
																id="closeNote" ng-model="ticketData.closeNote"></textarea>
														</div>
       												<!--  <div class="col-xs-6">
											          <div class="info-box">
											            <span class="info-box-icon bg-red">
											            <i class="fa fa-sitemap middle"></i></span>
											            <div class="info-box-content" >
											              <span class="info-box-text">Address</span>
											              <span class="info-box-number wordspace" >{{ticketData.siteAddress}}</span>
											            </div>
											          </div>
       												 </div> -->
																	</div>
																</div>

														<!-- 	<div class="row">
																<div class="col-xs-4 reqDiv required">
																	<label class="control-label">Site <a href="#"
																		class="pull-right" data-toggle="popover"
																		data-trigger="hover" data-html="true"
																		data-popover-content="#sitepop"> <span
																			class="fa fa-info-circle" aria-hidden="true"></span></a></label>

																	<input type="text" ng-model="ticketData.siteName"
																		class="form-control" required readonly>
																
									
																	<input type="hidden" ng-model="selectedSite.selected">
																</div> -->

															<!-- 	<div class="col-xs-4 reqDiv required">
																	<label class="control-label">Ticket Category</label> <select
																		name="ticketCategorySelect" id="ticketCategorySelect"
																		class="form-control"
																		onchange="getSelectedCategory('ticketCategorySelect')"
																		required>

																	</select> <input type="hidden"
																		ng-model="selectedCategory.selected.categoryName">
																</div> -->
																<!--  <div class="col-xs-4 reqDiv required">
				                <label class="control-label">Asset&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				                <input name="asset_type" id="equipment" value="equipment" type="radio" />Equipment&nbsp;&nbsp;&nbsp;
				                <input name="asset_type" id="service" value="service" type="radio" />Service
				                </label>				                
				                  <select name="assetSelect" id="assetSelect" class="form-control" 
								   required>
									
								</select>
								<input type="hidden" ng-model="selectedAsset.selected">
				                  
				                </div> -->

															<!-- </div> -->


														<!-- 	<div class="row">
																<div class="col-xs-4 reqDiv required">
																	<label class="control-label">Issue Start Time</label>
																	<div class="form-group">
																		<input type='text' class="form-control"
																			ng-model="ticketData.ticketStartTime"
																			disabled="disabled" />
																	</div>

																</div>

															</div> -->

													<%-- 		<div class="row" id="ticketCloseDiv">
															<div class="col-xs-4 reqDiv required">
																	<label class="control-label">Restoration confirmed by </label> 
																	<input type="text" class="form-control"
																		name="closedBy" id="closedBy" val="{{ticketData.closedBy}}"
																		ng-model="ticketData.closedBy" readonly="readonly" autofocus >
																</div>
																<div class="col-xs-4 reqDiv required">
																	<label class="control-label">Has issue been
																		fixed permanently ?</label> <select name="closeCodeSelect"
																		id="closeCodeSelect" class="form-control">
																	</select>
																	<input type="text" id="closeCode" ng-model="ticketData.codeClosed" class="form-control">
																	<input type="hidden"
																		ng-model="selectedCloseCode.selected">
																</div>
																
																<div class="col-md-4 reqDiv required">

																	<label class="control-label">Restoration notes</label>
																	<textarea class="form-control"
																		style="width: 100%; height: 70px;" rows="3"
																		placeholder="Enter ticket close note" name="closeNote"
																		id="closeNote" ng-model="ticketData.closeNote"></textarea>
																</div>

															</div>
 --%>

														<!-- 	<div class="row">

																<div class="col-md-12" style="margin-top:50px;">
																	<div class="pull-right">
																		<button type="submit" class="btn btn-success">Save
																			changes</button>
																		<button type="reset" id="reseCreateTicketForm" class="btn btn-success ">RESET</button>
																	</div>
																</div>
															</div> -->
														
													


															<!-- 	<div class="col-xs-4 reqDiv required">
																	<label class="control-label">Priority</label> <select
																		name="prioritySelect" id="prioritySelect"
																		class="form-control" required disabled="disabled"
																		onchange="getSelectedPriority('prioritySelect')">

																	</select>
																	<input type="text"  class="form-control"  
					                  name="priority" ng-model="ticketData.priority"  required >
																</div> -->
																<div class="col-md-4">
																<div class="row">
																<div class="col-md-12  reqDiv required">
																	<label class="control-label">SLA</label>
																		<div class='input-group date' id='datetimepicker2'>
																		<!-- Remove disabled properties from below input text by Supravat for allowing SLA Due Date to update on incident update screen.-->
																			</span><input type='text' class="form-control" autocomplete="off"
																				ng-model="ticketData.sla" id="sla"/> 
																				<span
																				class="input-group-addon"> <span
																				class="glyphicon glyphicon-calendar"></span>
																		</div>
														<dl class="dl-horizontal">
															<div class="progress">
																<div class="progress-bar"
																	ng-class="{'progress-bar-danger': ticketData.slaPercent >=100, 'progress-bar-warning': ticketData.slaPercent>75 && ticketData.slaPercent<100, 'progress-bar-info': ticketData.slaPercent>0 && ticketData.slaPercent<75}"
																	role="progressbar"
																	ng-style="{width: ticketData.width+'%'}">
																	 <span class="progress-text">SLA(%) {{ticketData.slaPercent}} </span>
																	</div>
																	</div>
																	
																	</dl>
					<%-- 						<div class="progress-group">
                    <span class="progress-text">SLA {{ticketData.slaPercent}} %</span>
                    <span class="progress-number"><b>160</b>/200</span>

                    <div class="progress-bar">
                      <div ng-class="{'progress-bar-danger': ticketData.slaPercent >=100, 'progress-bar-warning': ticketData.slaPercent>75 && ticketData.slaPercent<100, 'progress-bar-info': ticketData.slaPercent>0 && ticketData.slaPercent<75}" ng-style="{width: ticketData.width+'%'}"></div>
                    </div>
                  </div> --%>
                  </div>
                  </div>
														<div class="row" >
														<div class="col-md-12  reqDiv required">
														 	 <label class="control-label">Issue Start Time</label> <span class="badge pull-right">{{ticketData.ticketStartTime}}</span><br>
															<label class="control-label">Ticket Category</label> <select
																name="ticketCategorySelect" id="ticketCategorySelect"
																class="form-control"
																onchange="getSelectedCategory('ticketCategorySelect')"
																required>
															</select> <input type="hidden"
																ng-model="selectedCategory.selected.categoryName">
																	<div class="box box-widget widget-user-2" ng-if="ticketData.priorityId!=null">
				            <div class="box-footer no-padding">
				              <ul class="nav nav-stacked">
				                <li><a href="#"><b>Priority</b> <span class=" pull-right badge bg-blue">{{ticketData.priorityDescription}}</span></a></li>
				              </ul>
				            </div>
				          </div>
				         								 <label class="control-label">Status <a
																		href="#" class="pull-right" data-toggle="popover"
																		data-trigger="hover" data-html="true"
																		data-popover-content="#statuspop"
																		data-placement="left"> <span
																			class="fa fa-info-circle" aria-hidden="true"></span></a></label>

																	<select name="statusSelect" id="statusSelect"
																		class="form-control" required
																		onchange="angular.element(this).scope().ticketStatusChange('statusSelect')">

																	</select> <input type="hidden"
																		ng-model="selectedTicketStatus.selected">
				          
																</div>
															</div>
															<div class="row">
															<div class="col-md-12" style="margin-top:50px;">
																	<div class="pull-right">
																		<button type="submit" class="btn btn-success">Save
																			changes</button>
																		<!-- <button type="reset" id="reseCreateTicketForm" class="btn btn-success ">RESET</button> -->
																	</div>
																</div>
															</div>
															</div>	
															
															</form>
											</div>
											</div>
											<div class="tab-pane" id="escalate">
											<div style="display: none" id="loadingDiv2">
			<div class="loader">Loading...</div>
		</div>
											<div class="row">
												<div class="col-md-12">
												<div class="box box-success">
												<div class="box-header with-border">
													<h3 class="box-title">Escalation levels</h3>
												</div>	
												<div class="box-body">	
													<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12" ng-repeat="escalation in escalationLevelDetails">
											          <div class="info-box">
											            <span class="info-box-icon white">
											            <i class="fa fa-user middle" ></i><br>
											            </span>
											            <div class="info-box-content">
											              <span class="info-box-text">{{escalation.escTo}}</span>
											              <span class="info-box-number ng-binding">{{escalation.escEmail}}</span>
											              <span class="badge"  ng-if="escalation.escStatus!='Escalated'" ng-if="ticketData.ticketAssignedType=='RSP'">
											               <input
															type="checkbox" id="chkEscalation{{$index}}"
															ng-change="getSelectedEscalation(escalation,$index)"
															ng-model="escalation.selected" /> Check to escalate</span>
															 <a style="color:red" ng-if="escalation.escStatus=='Escalated'">
																<span class="fa fa-info-circle " aria-hidden="true">{{escalation.escStatus}} to Level {{escalation.escLevelDesc}}</span>
															</a>
											            </div>
											          </div>
											        </div>
											        </div>
									       		 </div>
									       		 <div class="box-footer">
									       		 	<div class="row">
									       		 		<div class="col-md-12">
									       		 		<button class="btn btn-xs btn-danger pull-right"
																	type="button" data-toggle="modal"
																	ng-click="escalateTicketConfirmation()"
																	data-title="Escalate Ticket"
																	data-message="Are you sure you want to escalate this ticket ?">Escalate</button>
									       		 		</div>
									       		 	</div>
									       		 </div>
									        </div>
									        </div>
									        </div>
										<!-- 		<table id="example2"
													class="table table-hover table-condensed">
													<thead>
														<tr>
															<th></th>
															<th><b>Escalation Level</b></th>
															<th><b>Name</b></th>
															<th><b>Email</b></th>
															<th><b>Status</b></th>

														</tr>
													</thead>
													<tbody>
														<tr ng-repeat="escalation in escalationLevelDetails">
															<td><label class="control-label"></label> <input
																type="checkbox" id="chkEscalation{{$index}}"
																ng-change="getSelectedEscalation(escalation,$index)"
																ng-model="escalation.selected" /></td>
															<td><input type="hidden" class="form-control"
																ng-model="escalation.escId"> <label
																class="control-label">{{escalation.escLevelDesc}}</label>
															</td>
															<td><label class="control-label">{{escalation.escTo}}</label>

															</td>

															<td><label class="control-label">{{escalation.escEmail}}</label>

															</td>

															<td><span class="label label-danger">{{escalation.escStatus}}</span>
															</td>

														</tr>
														<tr>
															<td></td>
															<td></td>
															<td></td>
															<td></td>
															<td>
																<button class="btn btn-xs btn-danger pull-right"
																	type="button" data-toggle="modal"
																	ng-click="escalateTicketConfirmation()"
																	data-title="Escalate Ticket"
																	data-message="Are you sure you want to escalate this ticket ?">Escalate</button>
															</td>
														</tr>

													</tbody>
												</table> -->
											</div> 
											<div class="tab-pane" id="linkedticket">
											<div style="display: none" id="loadingDiv3">
			<div class="loader">Loading...</div>
		</div>
													<div class="row" ng-if="ticketData.ticketAssignedType=='RSP'">
														<div class="col-md-3">
														<div class="funkyradio">
															<div class="funkyradio-primary">
																<input type="radio" name="radio" id="radio1" ng-model="optionVal" value="ZERO" 
																ng-change="selectLinkTicket(optionVal)"/> <label
																	for="radio1"   >
																	Link new ticket
																	</label>
															</div>
														</div>
													</div>
													<div class="col-md-3">
														<div class="funkyradio">
															<div class="funkyradio-primary">
																<input type="radio" name="radio" id="radio2" ng-model="optionVal" value="ONE" 
																ng-change="selectLinkTicket(optionVal)"/> <label
																	for="radio2" >Link ticket from suggestion list</label>
															</div>
														</div>
													</div>
													</div>
												<div class="row" ng-if="ticketData.ticketAssignedType=='RSP'">
														<div class="col-md-6" ng-if="optionVal=='ZERO'">
														<div class="box box-success">
												<div class="box-header with-border">
													<h3 class="box-title">Enter Link Ticket Number</h3>
												</div>
												<div class="box-body table-responsive no-padding">
													<table class="table table-hover">
														<tbody style="font-size: .9em">
																<tr>
																<th><input type="text" class="form-control" id="linkedTicket"
														   placeholder="Add a linked ticket to associate customer ticket {{ticketData.ticketNumber}}"
															maxlength="20" ng-model="linkedTicket.ticketNumber"></th>
																<th>
																	<a href ng-click="LinkNewTicket('RSP', linkedTicket)">
																<span class="badge bg-green" style="    font-size: 1.4em;" >
																<i class="fa fa-link"></i></span></a>
																</th>
																</tr>
																</tbody>
															</table>
															</div>	
												</div>
														</div>
												<div class="col-md-6" ng-if="optionVal=='ONE'">
												<div class="box box-success">
												<div class="box-header with-border">
													<h3 class="box-title">Suggested Tickets</h3>
												</div>
												<div class="box-body table-responsive no-padding" style="overflow:auto; height:300px">
													<div class="col-md-12">
													<table class="table table-hover">
														<tbody style="font-size: .9em">
															<tr>
																<th style="width: 22%">Ticket Number</th>
																<th style="width: 40%">Title </th>
																<th style="width:30%">Asset</th>
																<th style="width:10%">Action</th>
															</tr>
															<tr ng-repeat="ticket in suggestedTickets | filter : searchText" ng-if="suggestedTickets.length>0">
																<th style="width: 12%">{{ticket.ticketNumber}}</th>
																<td style="width: 40%">{{ticket.ticketTitle}}</td>
																<td style="width:10%">{{ticket.assetName}}</td>
																<td> 
																<a href ng-click="LinkNewTicket(ticket.ticketAssignedType, ticket)">
																<span class="badge bg-green" style="    font-size: 1.4em;" >
																<i class="fa fa-link"></i></span></a></td>
															</tr>
															<tr  ng-if="suggestedTickets==null">
																<th colspan="4" style="text-align: center">No ticket available</th>
															</tr>
														</tbody>
													</table>
												</div>	
												</div>
												<div class="box-footer">
												<div class="col-md-12">
													<a href class="btn btn-danger">Total linked tickets : <span class="badge">{{suggestedTickets.length}}</span></a>
												</div>
												</div> 
												</div>	
												</div>
													<div class="col-md-6">
												<div class="box box-success">
												<div class="box-header with-border">
													<h3 class="box-title">Linked Tickets</h3>
												</div>
												<div class="box-body table-responsive no-padding">
													<table class="table table-hover">
														<tbody style="font-size: .9em">
															<tr ng-repeat="ticket in ticketData.linkedTickets">
																<th style="width: 12%">{{ticket.spLinkedTicket}}</th>
																<td style="vertical-align:middle;width: 60%;">
																		<span class=" pull-right label label-success">{{ticket.linkedTicketStatus}}</span>
																		</td>
																<td><a href ng-click="unlinkTicketConfirmation($index,ticket)"><span class="badge bg-red pull-right" style="    font-size: 1.4em;" >
																<i class="fa fa-unlink"></i></span></a></th>
															</tr>
															<tr  ng-if="ticketData.linkedTickets.length==0 || ticketData.linkedTickets==null ">
																<th colspan="4" style="text-align: center">No ticket linked</th>
															</tr>
														</tbody>
													</table>
												</div>
												</div>	
												</div>
												</div>
											</div> 
												<div class="tab-pane" id="attachments" style="height:65%">
											<div style="display: none" id="loadingDiv5">
			<div class="loader">Loading...</div>
		</div>
													<div class="row" >
														<div class="col-md-6">
														  <div class="box">
											           		 <div class="box-header">
											             		 <h3 class="box-title">Attachment List</h3>
															</div>
														 <div class="box-body table-responsive no-padding" style="overflow-x:hidden">
			<div class="row gallery-container">
    
    <div class="tz-gallery">

        <div class="row">
            <div class="col-sm-6 col-md-4" ng-repeat="file in ticketData.files">
                <div class="thumbnail" ng-if="file.filePath!=null">
                    <a class="lightbox" href="${contextPath}/selected/file/download?keyname={{file.filePath}}" style="width:100%" target="_blank">
                        <img src="${contextPath}/selected/file/download?keyname={{file.filePath}}" alt="Park" style="width:100%">
                    </a>
                    <div class="caption">
                        <h3>{{file.createdOn}}</h3>
                       <a
						href="${contextPath}/selected/file/download?keyname={{file.filePath}}"
						download data-toggle="tooltip"
						data-original-title="Download this file"> <i
							class="fa fa-cloud-download" style="font-size:1.5em"
							aria-hidden="true"></i></a> <a href
						ng-click="deleteFile('INCIDENT', file)"
						data-toggle="tooltip"
						data-original-title="Delete this file"> <i style="font-size:1.5em;color:red"
							class="fa fa-trash" aria-hidden="true"></i></a>
                    </div>
                </div>
            </div>
        </div>

    </div>

</div>
						</div>
						</div>
					</div>
					<div class="col-md-6">
					  <div class="box">
		           		 <div class="box-header">
		             		 <h3 class="box-title">Add file to upload. image/*,.pdf only.</h3>
		             		 <a href ng-click="addNewImage()"><span class="badge pull-right"><i style="font-size:1.5em;color:#fff"
											class="fa fa-plus-circle"  aria-hidden="true"></i> Add File</span></a>
						</div>
						 <div class="box-body table-responsive no-padding">
								<form role="form">
									<div class="controls">
		             					 <table class="table table-hover">
											<tbody>
												<tr
													ng-repeat="incidentImage in incidentImageList">
													<td>{{incidentImage.imageId}}</td>	
													<td><input type="file"
														id="incidentImage{{$index}}"
														class="form-control"
														name="incidentImage[{{$index}}]"
														accept="image/*,.doc, .docx,.pdf"
														onchange="angular.element(this).scope().getIndexedName(this, event)"
														style="width: 80%"></td>
													<td><span id="imgsize{{$index}}"
														class="badge"></span></td>
													<td><a class="btn btn-danger" href
														ng-click="removeImage($index)"> <i
															class="fa fa-trash-o" aria-hidden="true"
															style="font-size: 1.4em;"></i>
													</a></td>
												</tr>
											</tbody>
										</table>
									   </div>
									   <span class="badge" id="totalSize"></span>
										<button type="button" class="btn btn-success pull-right" ng-if="incidentImageList.length>0"
										ng-click="uploadFiles('EDIT')" value="Upload"
										id="btnUpload">Upload</button>
								</form>
								</div>
								</div>
							</div>
				</div>		
												</div>
									<%-- 		<div class="tab-pane" id="attachments">
											<div style="display: none" id="loadingDiv4">
			<div class="loader">Loading...</div>
		</div>
												<div class="box-body">
													<div class="row">
														<div class="nav-tabs-custom">
															<ul class="nav nav-tabs"
																style="background-color: rgba(60, 141, 188, 0.34);">

																<li class="active"><a href="#attachmentViewTab"
																	data-toggle="tab" aria-expanded="true"
																	id="siteViewLink"><b>View Attachment</b></a></li>
																<li><a href="#addAttachmentTab" data-toggle="tab"
																	aria-expanded="true" id="siteContactLink"><b>Add
																			New Files</b></a></li>

															</ul>
															<div class="tab-content">
																<div class="tab-pane active" id="attachmentViewTab">
																	<table class="table no-margin">
																		<thead>
																			<tr>
																				<th>File Name</th>
																				<th>Created On</th>
																				<th>Action</th>
																			</tr>
																		</thead>
																		<tbody ng-repeat="file in ticketData.files">
																			<tr>
																				<td>{{file.fileName}}</td>
																				<td>{{file.createdOn}}</td>
																				<td><a
																					href="${contextPath}/selected/file/download?keyname={{file.filePath}}"
																					download data-toggle="tooltip"
																					data-original-title="Download this file"> <i
																						class="fa fa-cloud-download fa-2x"
																						aria-hidden="true"></i></a> <a href
																					ng-click="deleteFile('INCIDENT', file)"
																					data-toggle="tooltip"
																					data-original-title="Delete this file"> <i
																						class="fa fa-remove fa-2x" aria-hidden="true"></i></a>
																				</td>
																			</tr>
																		</tbody>
																	</table>



																</div>

																<div class="tab-pane" id="addAttachmentTab">
																	<p>Please choose a file to upload. image/*,.pdf
																		only.</p>
																	<form role="form">
																		<div class="controls">
																			<div class="entry input-group col-xs-8">
																				<input type="button" class="btn btn-success addnew"
																					style="margin-right: 5px;" onclick=""
																					ng-click="addNewImage()" value="Add New">
																				<div style="overflow-y: auto; height: 250px">
																					<table id="example2"
																						class="table  table-hover table-condensed">
																						<tbody>
																							<tr
																								ng-repeat="incidentImage in incidentImageList">
																								<td><input type="file"
																									id="incidentImage{{$index}}"
																									class="form-control"
																									name="incidentImage[{{$index}}]"
																									accept="image/*,.doc, .docx,.pdf"
																									onchange="angular.element(this).scope().getIndexedName(this, event)"
																									style="width: 80%"></td>
																								<td><span id="imgsize{{$index}}"
																									class="badge"></span></td>
																								<td><a class="btn btn-danger" href
																									ng-click="removeImage($index)"> <i
																										class="fa fa-trash-o" aria-hidden="true"
																										style="font-size: 1.4em;"></i>
																								</a></td>
																							</tr>
																						</tbody>
																					</table>
																				</div>
																				<span class="badge" id="totalSize"></span>
																			</div>


																		</div>
																		</br>
																		<button type="button" class="btn btn-success"
																			ng-click="uploadFiles('EDIT')" value="Upload"
																			id="btnUpload">Upload</button>
																	</form>
																</div>

															</div>
														</div>
													</div>
												</div>
											</div> --%>

											<div class="tab-pane" id="tickethistory">
												<div class="box">
													<div class="box-header with-border">
														<h3 class="box-title">Ticket History</h3>
													</div>
													<div class="box-body">

														<!-- <div class="table-responsive"> -->
															<div class="row">
																	<!-- The time line -->
																	<ul class="timeline">
																		<!-- timeline time label -->
																		<li class="time-label"
																			ng-if="ticketHistoryDetail.history.length > 0">
																			<span class="bg-red"
																			ng-show="ticketHistoryDetail.ticketCloseDate != null ">
																				{{ticketHistoryDetail.ticketCloseDate}} </span> <span
																			class="bg-aqua"
																			ng-show="ticketHistoryDetail.ticketCloseDate == null ">
																				{{CurrentDate}} </span>
																		</li>
																		<li class="time-label"
																			ng-if="ticketHistoryDetail.history.length == 0"><span
																			class="bg-aqua"> Ticket is not yet updated
																				since the creation date. </span></li>
																		<li
																			ng-repeat="tktHistory in ticketHistoryDetail.history">
																			<i class="fa fa-user bg-aqua"></i>

																			<div class="timeline-item">
																				<span class="time"><i class="fa fa-clock-o"></i>
																					{{tktHistory.date}}</span>

																				<h3 class="timeline-header">
																					{{tktHistory.description}} by {{tktHistory.name}}</h3>

																			</div>
																		</li>

																		<li class="time-label"><span class="bg-green">
																				Created On : {{ticketData.createdOn}} </span></li>

																		<li><i class="fa fa-clock-o bg-gray"></i></li>
																	</ul>
																</div>
														<!-- </div> -->
													</div>

												</div>
											</div>


												<!-- Added By Supravat for Financials Requirements. -->
											<div class="tab-pane" id="financials">
<div style="display: none" id="loadingDiv5">
			<div class="loader">Loading...</div>
		</div>
												<div class="box box-primary">
													<div class="box-header with-border">
														<h3 class="box-title">Cost Details</h3>
														<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
														<p class="text-info">Note: Cost Name, Cost, Charge Back and Billable are required cost item(s).</p>
														</sec:authorize>
													</div>
													<div class="box-body no-padding ">
														<form name="financialsForm" ng-submit="saveCostDetails()">
															<div class="row">
																<div class="col-md-12">
																	<div class="form-group" style="margin-bottom:50px;">
																		<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
																		<button type="button" style="float: right; margin-right: 5px;"
																		class="btn btn-success bg-red" ng-click="deleteCostFinancialItems()" ng-disabled="isCostDeleteButton">Delete
																		Cost Items</button>
																		
																			<input type="button" value="Add Cost Item"
																				class="btn btn-success addnew pull-right"
																				style="margin-right: 5px;" ng-click="addNewRow()" />
																		</sec:authorize>
																	</div>

																	<table id="financialsTable"
																		class="table table-striped">
																		<tbody>
																			<tr>
																				<th style="width: 40%;"><b>Cost Name</b></th>
																				<th style="width: 30%;"><b>Cost (&euro;)</b></th>
																				<th style="width: 12%;"><b>Charge Back</b></th>
																				<th style="width: 12%;"><b>Billable</b></th>
																				<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
																				<th style="width: 6%;" colspan="2"></th>
																				</sec:authorize>
																			</tr>


																			<tr ng-repeat="row in financialCostDetails">
																				<td><input type="hidden" class="form-control"
																					ng-model="row.ticketID" id="financialTicketId{{$index}}">
																					<input type="hidden" class="form-control"
																					ng-model="row.costId" id="financialBoxId{{$index}}"> <input
																					type="text" class="form-control"
																					id="financialCostName{{$index}}"
																					ng-disabled="!enabledEdit[{{$index}}] && row.costId.length != 0"
																					ng-model="row.costName" autocomplete="off" maxlength="50"
																					required /></td>
																				<td><input type="text" class="form-control"
																					id="financialCost{{$index}}"
																					ng-disabled="!enabledEdit[{{$index}}] && row.costId.length != 0"
																					ng-model="row.cost" valid-cost required autocomplete="off" /></td>
																				<td><select name="ChargeBack"
																					id="financialChargeBack{{$index}}"
																					ng-disabled="!enabledEdit[{{$index}}] && row.costId.length != 0"
																					class="form-control selectpicker" ng-model="row.chargeBack"
																					required>
																						<option value="">Select</option>
																						<option value="1">Yes</option>
																						<option value="2">No</option>
																				</select></td>
																				<td><select name="Billable"
																					ng-disabled="!enabledEdit[{{$index}}] && row.costId.length != 0"
																					id="financialBillable{{$index}}"
																					class="form-control selectpicker" ng-model="row.billable"
																					required>
																						<option value="">Select</option>
																						<option value="1">Yes</option>
																						<option value="2">No</option>
																				</select></td>
																				<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
																				<td ng-show="row.costId.length != 0" style="valign:middle;">
																					<button type="button" ng-show="row.costId.length != 0"
																								ng-click="editCostDetails($index)">
																						<span class="fa-stack" style="width: 1.9vw; height:1.85vw; vertical-align: super;">
																							<i class="fa fa-pencil-square-o" style="font-size: 27px; color: green"></i>
																						</span>
																					</button>
																				</td>
																				<td ng-show="row.costId.length != 0" style="valign:middle;">
																					<input type="checkbox" ng-show="row.costId.length != 0" ng-change="deleteCostChange()" style="margin-top: 0px;width: 33px; height: 33px;"
																								id="isDeleteCheck{{$index}}" ng-model="row.isDeleteCheck" />
																				</td>
																				<td ng-show="row.costId.length == 0" colspan="2">
																					<button ng-click="removeRow($index)"
																								ng-show="row.costId.length == 0">
																						<span class="fa-stack" style="width: 1.9vw; height:1.85vw; vertical-align: super;">
																							<i class="fa fa-remove" style="font-size: 27px; color: red"></i>
																						<span>
																					</button>
																				</td>
																				</sec:authorize>
																			</tr>

																		</tbody>
																	</table>

																</div>
															</div>
															<div class="row">

																<div class="col-md-12">
																	<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
																	<button type="submit" style="float: right; margin-right: 5px;"
																		class="btn btn-success" ng-disabled="isCostSaveButton">Save changes</button>
																	</sec:authorize>
																</div>
															</div>
														</form>
													</div>
												</div>
											</div>
										<div class="modal fade"
												id="confirmCostDelete" data-dismiss="modal" data-keyboard="false"
												data-backdrop="static" data-toggle="modal" role="dialog"
												aria-labelledby="confirmDeleteLabel" aria-hidden="true">
												<div class="modal-dialog">
													<div class="modal-content">
														<div class="modal-header">
															<h4 class="modal-title">Financials Cost Items</h4>
														</div>
														<div class="modal-body">
															<p>Are you sure you want to delete the selected cost item(s) ?</p>
														</div>
														<div class="modal-footer">
															<button type="button" class="btn btn-default" data-dismiss="modal">No</button>
															<button type="button" class="btn btn-danger" id="confirm"
																ng-click="confirmCostDelete()">Yes</button>
														</div>
													</div>
												</div>
											</div>

											
											
											
											
											<!-- Added By Supravat for Related Tickets Requirements. -->
											<div class="tab-pane" id="relatedTickets">
																		          <div class="box box-primary">
            <div class="box-header">
              <h3 class="box-title">Related Tickets</h3>

              <div class="box-tools">
                <div class="input-group input-group-sm" style="width: 450px;">
                  <input type="text" name="table_search" class="form-control pull-right" ng-model="ticketsearch" 
                  placeholder="Search by incident number, asset name, site name, service provider, status">

                  <div class="input-group-btn">
                    <button type="submit" class="btn btn-default"><i class="fa fa-search"></i></button>
                  </div>
                </div>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body table-responsive no-padding">
              <table class="table table-striped">
              <tbody style="font-size: .9em"><tr ">
					<th style="width:12%">Ticket Number</th>
					<th style="width:20%">Title</th>
					<th>Asset</th>
					<th tyle="width:10%">Status</th>
					
					</tr>
                <tr ng-repeat="val in relatedTicketData | filter: ticketsearch" ng-class="{currentSelected:$index == selectedRow}" 
               	 ng-click="rowHighilited($index)">
                	<th class="todo-list">
                  {{val.ticketNumber}}
                  <a href="#" ng-click="getTicketDetails(val);" data-toggle="modal"  ><i class="fa fa-eye pull-right"></i></a>
                  </th>
					<td>{{val.title}}</td>
					<td>{{val.asset}}</td>
					<td><span class="label label-success" ng-if="val.statusId == 1">{{val.status}}</span>
					<span class="label label-warning" ng-if="val.statusId == 2">{{val.status}}</span>
					<span class="label label-info" ng-if="val.statusId == 3">{{val.status}}</span>
					<span class="label label-default" ng-if="val.statusId == 4">{{val.status}}</span>
					<span class="label label-danger" ng-if="val.statusId == 5">{{val.status}}</span>
					<span class="label label-danger" ng-if="val.statusId == 6">{{val.status}}</span>
					<span class="label label-danger" ng-if="val.statusId > 6">{{val.status}}</span>
					
					</td>
					
                </tr>
              </tbody></table>
            </div>
          </div>
											</div>

											<!-- Supravat End -->
											
											<!-- Added By Supravat for Incident Task. -->
											<div class="tab-pane" id="incidentTask">
												<div class="box">
													<div class="box-header with-border">
														<h3 class="box-title">Task List</h3>
														<div class="box-tools pull-right">
														<%-- <sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER','ROLE_SITE_STAFF')"> --%>
														<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
														<div class="btn-group pull-right">
															<button type="button"
															class="btn btn-success dropdown-toggle pull-right"
															style="margin-right: 5px;" data-toggle="dropdown"><span class="fa fa-gear"></span>
															Action <span class="caret"></span>
															</button>
															
															<ul class="dropdown-menu" role="menu">
															<li> <a href ng-click="openIncidentTask('C',incidentTasks)">  <span class="fa fa-tasks" aria-hidden="true"></span>Create Task</a></li>
														</ul>
														</div>
														</sec:authorize>
														<%-- </sec:authorize> --%>
														</div>
													</div>
													<div class="box-body table-responsive no-padding">
																	<table id="incidentTaskDetails"
																		class="table table-hover ">
																		<tbody>
																		<tr>
																			<th style="width:10%;"><b>Task ID</b></th>
																			<th style="width:30%;"><b>Title</b></th>
																			<th style="width:20%;"><b>Status</b></th>
																			<th style="width:30%;"><b>Assigned To</b></th>
																			<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
																			<th style="width:10%;"><b>Action</b></th>
																			</sec:authorize>
																		</tr>
																	

																			<tr
													ng-repeat="inctask in incidentTask.list"
													ng-class="{currentSelected:$index == selectedTaskRow}"
													ng-click="rowTaskHighilited($index)">
													<td>{{inctask.taskNumber}}</td>
													<td>{{inctask.taskName}}</td>
													<td>{{inctask.taskStatus}}</td>
													<td>{{inctask.taskAssignedTo}}</td>
													<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
													<td align="center"><a href ng-click="openIncidentTask('U',inctask)"> <i
															class="fa fa-edit" aria-hidden="true"></i></a></td>
															</sec:authorize>
												</tr>

																		</tbody>
																	</table>

													</div>
												</div>
												
												
			<div class="modal right fade" id="taskModal" tabindex="-1" role="dialog" aria-labelledby="taskModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel2" ng-if="incidentTask.taskId==null">Create Task</h4>
					<h4 class="modal-title" id="myModalLabel3" ng-if="incidentTask.taskId!=null">Update Task</h4>
				</div>
				<div class="modal-body">
				       <div class="box box-solid">
            <div class="box-header with-border">
              <h3 class="box-title">Incident : {{ticketData.ticketNumber}}</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
            <form name="createUpdatetaskform" ng-submit="saveIncidentTask()">
              <dl>
                <dt>Task Name <i style="color:red">*</i> </dt>
                <dd>
					 <input
						name="taskname" type="text" class="form-control"
						maxlength="50" name="taskName" 
						ng-disabled="incidentTask.taskSelectedStatus =='CLOSED' || incidentTask.taskSelectedStatus =='REJECTED'"
						ng-model="incidentTask.taskName"
						placeholder="Enter Task Name" required>
				</dd>
              </dl>
                <dl>
                <dt>Task Description</dt>
                <dd>
					<textarea class="form-control"
					 rows="3"
					 ng-disabled="incidentTasks.taskSelectedStatus =='CLOSED' || incidentTask.taskSelectedStatus =='REJECTED'"
					placeholder="Enter Task Description" name="taskDescription"
					ng-model="incidentTask.taskDesc"></textarea>
				</dd>
                </dl>
                 <dl>
                 <div class="row">
                 <div class="col-xs-6">
                <dt>Planned Start Date <i style="color:red">*</i></dt>
                <dd>
				<div class="input-group date">
				<div class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</div>
				<input type="text" class="form-control pull-right dt1"
					placeholder="Planned Start Date" id="planStartDate"
					ng-disabled="incidentTask.taskSelectedStatus =='CLOSED' || incidentTask.taskSelectedStatus =='REJECTED'"
					ng-model="incidentTask.planStartDate" required>

			     </div>
			     </div>
			       <div class="col-xs-6">
                <dt>Planned Completion Date <i style="color:red">*</i></dt>
                <dd>
			     <div class="input-group date">
					<div class="input-group-addon">
						<i class="fa fa-calendar"></i>
					</div>
					<input type="text" class="form-control pull-right dt1"
						placeholder="Completion Date" id="planComplDate"
						ng-disabled="incidentTask.taskSelectedStatus =='CLOSED' || incidentTask.taskSelectedStatus =='REJECTED'"
						ng-model="incidentTask.planEndDate" required>

						
				</div>
				</dd>
				</div>
				</div>
                </dl>
                 <dl>
                <dt>Assigned To <i style="color:red">*</i></dt>
                <dd>
					<input
					type="text" class="form-control" name="assignTo"
					ng-model="incidentTask.taskAssignedTo"
					ng-disabled="incidentTask.taskSelectedStatus =='CLOSED' || incidentTask.taskSelectedStatus =='REJECTED'"
					placeholder="Enter Assigned To" required>
				</dd>
                </dl>
                 <label class="control-label">Status <i style="color:red">*</i></label>
                <div >
					<select ng-if="taskOperation =='CreateTask'"
					 name="taskStatus" ng-model="incidentTask.taskStatus"
					id="taskStatus" class="form-control" required>
					<option value="NEW" selected="selected">New</option>
					</select>
					<select  ng-if="taskOperation =='UpdateTask'" 
						name="taskStatusUpdate" id="taskStatusUpdate" class="form-control" 
						ng-disabled="incidentTask.taskSelectedStatus =='CLOSED' || incidentTask.taskSelectedStatus =='REJECTED'"
						required ng-model="incidentTask.taskStatus">
						<option value="NEW" ng-hide="incidentTask.taskSelectedStatus =='INPROGRESS'">New</option>
						<option value="INPROGRESS">In Progress</option>
						<option value="CLOSED">Closed</option>
						<option value="REJECTED">Rejected</option>
					</select>
				
                </div>
                <div ng-hide="incidentTask.taskStatus =='NEW' || incidentTask.taskStatus =='INPROGRESS'">
					<label class="control-label">Resolution Comment</label>
					<textarea class="form-control" rows="3"
						placeholder="Enter Resolution Comment"
						ng-disabled="incidentTask.taskSelectedStatus =='CLOSED' || incidentTask.taskSelectedStatus =='REJECTED'"
						name="resolutionComment"
						ng-model="incidentTask.resComments"></textarea>
				
                </div>
                 <div ng-if="incidentTask.taskId==null" style="margin-top: 15px; margin-right: 1px;"
                 ng-hide="incidentTask.taskSelectedStatus =='CLOSED' || incidentTask.taskSelectedStatus =='REJECTED'">
                 <button type="submit" class="btn btn-success">Save	changes</button>
					<button type="reset" id="resetServiceAssetForm"
						class="btn btn-success">RESET</button>
                 </div>
                 <div ng-if="incidentTask.taskId!=null" style="margin-top: 15px; margin-right: 1px;"
                 ng-hide="incidentTask.taskSelectedStatus =='CLOSED' || incidentTask.taskSelectedStatus =='REJECTED'">
                 <button type="submit" class="btn btn-success">Update changes</button>
                 </div>
                </form>
            </div>
          </div>
								
          </div>
				</div>
				</div>
		</div>
												
												
											</div>

											<!-- Supravat End -->


										</div>
									</div>
								</div>
								<div class="col-md-6" id="chatWindow" style="display: none">
									<div class="box">
										<div
											style="z-index:9;width: 30%; position: fixed; bottom: 0px; right: 0px; margin: 0; background-color: #dbdcdc">
											<div class="box-header with-border">
												<h3 class="box-title" style="color: #000">Work Notes</h3>
												<div class="box-tools pull-right">
													<a href="javascript:void(0);" class="badge bg-yellow"
														data-toggle="tooltip" ng-click="closeWindow();"> <i
														class="fa fa-minus" aria-hidden="true"></i>
													</a>
												</div>
											</div>
											<div class="box-body">
												<div class="row">
													<div class="col-md-12">
														<div class="direct-chat-messages" style="height: 430px;"
															id="messageWindow">
															<div ng-repeat="ticket in ticketComments" id="messagebox">
																<div class="direct-chat-msg">
																	<div class="direct-chat-info clearfix">
																		<span class="direct-chat-name pull-left"
																			style="color: #000">{{ticket.createdBy}}</span> <span
																			class="direct-chat-timestamp pull-right"
																			style="color: #000">{{ticket.createdDate}}</span>
																	</div>
																	<img class="direct-chat-img"
																		src="${contextPath}/resources/img/swadhin.jpg"
																		alt="message user image">

																	<div class="direct-chat-text" id="audioMessage"
																		style="background-color: #4CAF50; color: #fff;">
																		{{ticket.comment}}</div>

																</div>

															</div>
														</div>
													</div>
												</div>
											</div>
											<div class="box-footer">
												<div class="row">
													<div class="col-sm-12">
														<input type="text" name="ticketMessage" id="ticketMessage"
															ng-model="ticketComment.comment" style="height: 50px;  border-radius: 20px;"
															placeholder="Type Message ..." class="form-control"
															ng-enter="addNewComment()">
													</div>
												</div>
											</div>
										</div>
									</div>

								</div>
							</div>

							<div id="raisedbypop" class="hidden" style="width: 30px">
								<!-- <div class="popover-heading">This is the heading for #2</div> -->

								<div class="popover-body">
									<a class="pull-left btn-box-tool">Name:
										{{ticketData.assignedSP}} </a></br> <a class="pull-left btn-box-tool">Phone
										Number: {{ticketData.raisedUser}} </a>
								</div>
							</div>

							<div id="assetpop" class="hidden" style="width: 30px">
								<!-- <div class="popover-heading">This is the heading for #2</div> -->

								<div class="popover-body">
									<a class="pull-left">Asset Code: {{ticketData.assetCode}} </a></br>
									<a class="pull-left">Model: {{ticketData.assetModel}} </a></br>
									<a class="pull-left">Category: {{ticketData.assetCategoryName}} </a></br>
									<a class="pull-left">Component Type: {{ticketData.assetSubCategory1}} </a></br>
									<a class="pull-left">Subcomponent Type: {{ticketData.assetSubCategory2}} </a>
								</div>
							</div>
							<div id="sitepop" class="hidden" style="width: 30px">
								<div class="popover-body">
									<a class="pull-left ">Contact Number:
										{{ticketData.siteContact}} </a></br> <a class="pull-left ">Site
										Number1: {{ticketData.siteNumber1}} </a></br> <a class="pull-left ">Site
										Number2: {{ticketData.siteNumber2}} </a></br> <a class="pull-left ">Address:
										{{ticketData.siteAddress}} </a>
								</div>
							</div>

							<div id="statuspop" class="hidden" style="width: 30px">

								<div class="popover-body">
									<a class="pull-left btn-box-tool" id="statusDesc"></a>
								</div>
							</div>

							<!-- Added by Supravat for Status validation (Role Status Mapping Validation). -->
							<div class="modal fade" id="StatusRoleValidation" role="dialog"
								aria-labelledby="confirmDeleteLabel" aria-hidden="true"
								data-keyboard="false" data-backdrop="static">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header label-warning">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">&times;</button>
											<h4 class="modal-title">Status warning</h4>
										</div>
										<div class="modal-body">
											<p id="StatusRoleValidationMSG"></p>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default"
												data-dismiss="modal">OK</button>
										</div>
									</div>
								</div>
							</div>
							<!-- Ended by Supravat for Status validation -->


							<div class="modal fade" id="confirmEscalate" role="dialog"
								aria-labelledby="confirmDeleteLabel" aria-hidden="true"
								data-keyboard="false" data-backdrop="static">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">&times;</button>
											<h4 class="modal-title">Escalate ticket</h4>
										</div>
										<div class="modal-body">
											<p>Are you sure you want to escalate this ticket ?</p>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default"
												data-dismiss="modal">No</button>
											<button type="button" class="btn btn-danger" id="confirm"
												ng-click="escalateTicket()">Yes</button>
										</div>
									</div>
								</div>
							</div> 
							<div class="modal fade" id="confirmUnlink" role="dialog"
								aria-labelledby="confirmDeleteLabel" aria-hidden="true"
								data-keyboard="false" data-backdrop="static">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">&times;</button>
											<h4 class="modal-title">Unlink ticket</h4>
										</div>
										<div class="modal-body">
											<p>Are you sure, you want to unlink this ticket ?</p>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default" id="unlinkNoBtn"
												data-dismiss="modal">No</button>
											<button type="button" class="btn btn-danger" id="confirm"
												ng-click="unlinkTicket()">Yes</button>
										</div>
									</div>
								</div>
							</div>
									<div class="modal right fade" id="previewIncidentModal" tabindex="-1" role="dialog" aria-labelledby="previewIncidentModalLabel2">
		<div class="modal-dialog" role="document">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel2">{{selectedTicket.ticketNumber}} - Overview</h4>
					<span class="badge" style="background-color: #cfe4e2;color:#000"><i class="fa fa-flag" aria-hidden="true" style="color:red;"></i> {{selectedTicket.status}}</span>
				</div>
				<div class="modal-body">
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
								<div class="box box-widget widget-user" ng-if="selectedTicket.ticketNumber != null">
            <!-- Add the bg color to the header using any of the bg-* classes -->
            <div class="widget-user-header" style="background-color: #00a65a;color:#fff">
              <h3 class="widget-user-username">Site : {{selectedTicket.siteName}}</h3>
              <h5 class="widget-user-desc">Owner : {{selectedTicket.siteOwner}}</h5>
            </div>
           <!--  <div class="widget-user-image">
              <img class="img-circle" src="../dist/img/user1-128x128.jpg" alt="User Avatar">
            </div> -->
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
          						<div class="box box-widget widget-user" ng-if="selectedTicket.ticketNumber != null">
            <!-- Add the bg color to the header using any of the bg-* classes -->
            <div class="widget-user-header" style="background-color: #00a65a;color:#fff">
              <h3 class="widget-user-username">Asset : {{selectedTicket.assetName}}</h3>
              <h5 class="widget-user-desc">Code : {{selectedTicket.assetCode}}</h5>
            </div>
           <!--  <div class="widget-user-image">
              <img class="img-circle" src="../dist/img/user1-128x128.jpg" alt="User Avatar">
            </div> -->
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
            <span class="info-box-icon"><i class="ion ion-ios-pricetag-outline" style="margin-top: 26px;"></i></span>

            <div class="info-box-content">
              <span class="info-box-text">SLA ( % )</span>
              <span class="info-box-number">{{selectedTicket.slaPercent}}</span>
              <div class="progress">
               <div class="progress-bar"
				ng-class="{'progress-bar-danger': selectedTicket.slaPercent >=100, 'progress-bar-warning': selectedTicket.slaPercent>75 && selectedTicket.slaPercent<100, 'progress-bar-info': selectedTicket.slaPercent>0 && selectedTicket.slaPercent<75}"
				role="progressbar"
				ng-style="{width: sessionTicket.width+'%'}">
				 <span class="progress-text">SLA(%) {{selectedTicket.slaPercent}} </span>
				</div>
              <span class="progress-description">
                    50% Increase in 30 Days
                  </span>
                  
            </div>
            <!-- /.info-box-content -->
          </div>
				</div>
				</div>
			</div><!-- modal-content -->
		</div><!-- modal-dialog -->
	</div><!-- modal -->
							<!-- <div class="modal fade" id="confirmClose" role="dialog"
								aria-labelledby="confirmDeleteLabel" aria-hidden="true"
								data-keyboard="false" data-backdrop="static">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal"
												aria-hidden="true">&times;</button>
											<h4 class="modal-title">Close Linked ticket</h4>
										</div>
										<div class="modal-body">
											<p>Are you sure you want to close the linked ticket ?</p>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-default"
												data-dismiss="modal">No</button>
											<button type="button" class="btn btn-danger" id="confirm"
												ng-click="closeLinkedTicket()">Yes</button>
										</div>
									</div>
								</div>
							</div> -->

							<%-- <div class="modal" id="fileAttachModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false" data-keyboard="false" data-backdrop="static">
      <div class="modal-dialog" style="width: 60%;">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button>
          <h4 class="modal-title" id="upload-avatar-title">View / Upload files</h4>
          <div class="alert alert-danger alert-dismissable" id="incidentImageModalMessageDiv"
				style="display: none;  height: 34px;white-space: nowrap;">
				<strong>Error! </strong> {{incidentImageModalErrorMessage}} 
				<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
			</div>
        </div>
        <div class="modal-body">
        <div class="nav-tabs-custom">
        	<ul class="nav nav-tabs" style="background-color: rgba(60, 141, 188, 0.34);">
            
							<li class="active">
				       		 <a  href="#attachmentViewTab" data-toggle="tab" aria-expanded="true" id="siteViewLink"><b>View Attachment</b></a>
							</li>
							<li><a href="#addAttachmentTab" data-toggle="tab" aria-expanded="true" id="siteContactLink"><b>Add New Files</b></a>
							</li>
							
            </ul>
        	<div class="tab-content">
                <div class="tab-pane active" id="attachmentViewTab">
                	<div class="box-body">
              		<div class="table-responsive">
                <table class="table no-margin">
                  <thead>
                  <tr>
                    <th>File Name</th>
                    <th>Created On</th>
                    <th>Action</th>
                  </tr>
                  </thead>
                 <tbody ng-repeat="file in ticketData.files">
					<tr>
						<td>{{file.fileName}}</td>
						<td>{{file.createdOn}}</td>
						<td><a href="${contextPath}/selected/file/download?keyname={{file.filePath}}" download data-toggle="tooltip" data-original-title="Download this file">
							<i class="fa fa-cloud-download fa-2x" aria-hidden="true"></i></a>
							<a href ng-click="deleteFile('INCIDENT', file)" data-toggle="tooltip" data-original-title="Delete this file">
							<i class="fa fa-remove fa-2x" aria-hidden="true"></i></a>
						</td>
					</tr>
				</tbody>
                </table>
              </div>
              
               </br>
            <button type="button" class="btn btn-default" id="btnUploadCancel" data-dismiss="modal">Close</button>
              
            </div>
                
                </div>
                
                <div class="tab-pane" id="addAttachmentTab">
                	<p>Please choose a file to upload. image/*,.doc, .docx,.pdf only.</p>
          <form role="form">
          <div class="controls">                       
              <div class="entry input-group col-xs-12">
                <input type="button" class="btn btn-success addnew" style="margin-right: 5px;" onclick="" ng-click="addNewImage()" value="Add New">
                <div  style="overflow-y:auto;height:250px">
                <table id="example2" class="table  table-hover table-condensed">
                 <tbody>
                <tr ng-repeat="incidentImage in incidentImageList">
                  <td>
                  <input type="file" id="incidentImage{{$index}}" class="form-control" 
                  name="incidentImage[{{$index}}]" accept="image/*,.doc, .docx,.pdf" 
                  onchange="angular.element(this).scope().getIndexedName(this, event)" style="width:80%">
                   <a class="btn btn-danger" href  ng-click="removeImage($index)" >
                  <i class="fa fa-trash-o" aria-hidden="true" style="font-size: 1.4em;"></i>
                  </a>
                  </td>
                </tr>
                <tr>
                	<td colspan="2">Total Size : {{totalIncidentImageSize}} KB</td>
                </tr>
                </tbody>
                </table>
                </div>
              </div>
              
           
          </div>            
            </br>
            <button type="button" class="btn btn-default" id="btnUploadCancel" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-success" ng-click="uploadFiles()" value="Upload">Upload</button>
          </form>
                </div>
                
            </div>
        </div>
          
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
<!-- </div> -->

</div> --%>
						</div>
					</div>
				</div>
		</section>


	</div>
</div>