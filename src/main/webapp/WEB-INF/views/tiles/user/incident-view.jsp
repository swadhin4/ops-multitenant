<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>



<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />

<link rel="stylesheet" media="screen" href='<c:url value="/resources/theme1/css/bootstrap-datetimepicker-standalone.css"></c:url>' />
<link rel="stylesheet" media="screen" href='<c:url value="/resources/theme1/css/bootstrap-datetimepicker.css"></c:url>' />
<link rel="stylesheet" media="screen" href='<c:url value="/resources/css/bootstrap-datetimepicker.min.css"></c:url>' />

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.min.js"></c:url>'></script>

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/incident-create-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/asset-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-provider-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>




<style>
	.main-box.no-header {
    padding-top: 20px;
}

.table tbody tr.currentSelected {
background-color: rgba(60, 141, 188, 0.58) !important;
}
.currentSelected{
	background:rgba(60, 141, 188, 0.58);
    color:#fff;
}
.currentSelected a{
	color:#fff
}
#errorMessageDiv, #successMessageDiv, #infoMessageDiv{
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
 #modalMessageDiv{
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
.popover {max-width:500px;}



</style>

<script>

$(function() {
	
      
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
	<div  ng-controller="incidentCreateController" id="incidentCreateWindow">
	<div style="display:none" id="loadingDiv"><div class="loader">Loading...</div></div>
	<section class="content" style="min-height:35px;display:none" id="messageWindow">
				<div class="row">
					<div class="col-md-12">
						<div class="alert alert-success alert-dismissable" id="successMessageDiv"
							style="display: none;  height: 34px;white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Success! </strong> {{successMessage}} 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
						<div class="alert alert-danger alert-dismissable"
							id="errorMessageDiv"
							style="display: none; height: 34px; white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Error! </strong> {{errorMessage}} 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
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
		<div class="row">
			<div class="box" >
				<div class="box-header with-border">
					<h3 class="box-title"><a href="${contextPath}/incident/details/" title="View All Incidents"><i class="fa fa-th-list" aria-hidden="true"></i></a> Incident - {{ticketData.ticketNumber}}</h3>
					<div class="box-tools pull-right" style="margin-top: 0px;">
						<input type="hidden" id="mode" value="${mode}">
					<a  href ng-click="openChatBox();" >
			        	<img src="${contextPath}/resources/img/chatbot.png" style="width: 12%;" 
			        	class="pull-right" data-toggle="tooltip" data-original-title="Incident work notes">
        			</a>
					</div>
				</div>
				<div class="box-body">
				<div class="row">
				<div class="col-md-12">
				<div class="nav-tabs-custom">
		          <!--   <ul class="nav nav-tabs">
		              <li class="active"><a href="#primaryinfo" data-toggle="tab">Primary Details</a></li>              
		              <li><a href="#escalate" data-toggle="tab" onclick="initializeEscalateTicket()">Escalation</a></li>
		              <li><a href="#linkedticket" data-toggle="tab" >Linked Tickets</a></li>              
		              <li><a href="#tickethistory" data-toggle="tab" onclick="getTicketHistory()">Ticket History</a></li>
		            </ul> -->
	             <div class="tab-content" style="background-color: #eee;">
	              <div class="active tab-pane" id="primaryinfo">
			        <div class="row">
		        	 <div class="col-md-8 col-sm-12">
	                  	<div class="box">
				        <div class="box-body">
				        <img class="img-circle img-bordered-sm" src="${contextPath}/resources/img/incident.png" alt="user image" style="    width: 6%;">  
				        <a  class="link-black text-sm"><i class="glyphicon glyphicon-time"></i> <b>{{ticketData.ticketTitle}}</b></a>
				        <div style="pull-right">
				        	<i class="fa fa-share margin-r-5"></i> <b>Status : </b>{{ticketData.status}}
				         	<a class="pull-right btn-box-tool">Priority: {{ticketData.priorityDescription}} </a>
				         </div>
				       
				        </div>
				        
				      </div>
	                  </div>
	                  <div class="col-md-4 col-sm-12">
	                  <div class="box">
	                   <div class="box-body">
			         SLA {{ticketData.slaPercent}} %
					 <dl class="dl-horizontal">
			             <div class="progress">
			                 <div class="progress-bar" 
			                 ng-class="{'progress-bar-danger': ticketData.slaPercent >=100, 'progress-bar-warning': ticketData.slaPercent>75 && ticketData.slaPercent<100, 'progress-bar-info': ticketData.slaPercent>0 && ticketData.slaPercent<75}" 
			                 role="progressbar" ng-style="{width: ticketData.width+'%'}"></div>
			             </div>                                           
			    	 </dl>
			    	 </div>
			    	 </div>
	                  </div>
	                  </div>
	                  <div class="row">
                    	<div class="col-md-12 col-sm-12">
                  		<div class="box">
				        <div class="box-body">
				         <a  class="link-black text-sm"><i class="glyphicon glyphicon-time"></i> <b>Ticket Description</b></a>
				        </div>
				        <div class="box-footer">
				          {{ticketData.description}}
				        </div>
				      </div>
				      </div>
				      </div>
	                  <div class="row">
                    	<div class="col-md-4 col-sm-4">
                  		<div class="box">
				        <div class="box-body">
				         <a  class="link-black text-sm"><i class="glyphicon glyphicon-time"></i> <b>Created Date</b></a>
				        </div>
				        <!-- /.box-body -->
				        <div class="box-footer">
				          {{ticketData.createdOn}}
				        </div>
				      </div>
				      </div>
				      <div class="col-md-4 col-sm-4">
                  		<div class="box">
				        <div class="box-body">
				         <a  class="link-black text-sm"><i class="glyphicon glyphicon-time"></i> <b>SLA Due Date</b></a>
				        </div>
				        <!-- /.box-body -->
				        <div class="box-footer">
				         {{ ticketData.sla}}
				        </div>
				      </div>
				      </div>
				      	<div class="col-md-4 col-sm-4">
                  		<div class="box">
				        <div class="box-body">
				         <a href="#" class="link-black text-sm"><i class="glyphicon glyphicon-time"></i> <b>Issue Start Time </b></a>
				        </div>
				        <!-- /.box-body -->
				        <div class="box-footer">
			{{ticketData.ticketStartTime}}
				        </div>
				      </div>
				      </div>
					  </div>
					  <div class="row">
                    	<div class="col-md-4 col-sm-4">
                  		<div class="box">
				        <div class="box-body">
				         <a href="#" class="link-black text-sm"><i class="fa fa-sitemap"></i> <b>Site Name </b></a>
				        </div>
				        <!-- /.box-body -->
				        <div class="box-footer">
			{{ticketData.siteName}}
				        </div>
				      </div>
				      </div>
				      <div class="col-md-4 col-sm-4">
                  		<div class="box">
				        <div class="box-body">
				        <a href="#" class="link-black text-sm"><i class="fa fa-sitemap"></i> <b>Asset Name </b></a>
				        </div>
				        <!-- /.box-body -->
				        <div class="box-footer">
				         {{ ticketData.assetName}}
				        </div>
				      </div>
				      </div>
				      <div class="col-md-4 col-sm-4">
                  		<div class="box">
				        <div class="box-body">
				         <a   class="link-black text-sm"><i class="glyphicon glyphicon-user"></i> <b>Assigned to Service Provider</b></a>
				        </div>
				        <!-- /.box-body -->
				        <div class="box-footer">
				         {{ticketData.assignedSP}}
				        </div>
				      </div>
				      </div>
					  </div>
					  <div class="row">
                    	<div class="col-md-4 col-sm-4">
                  		<div class="box">
				        <div class="box-body">
				        <a   class="link-black text-sm"><i class="glyphicon glyphicon-user"></i> <b>Raised By </b></a>
				        </div>
				        <!-- /.box-body -->
				        <div class="box-footer">
			{{ticketData.raisedBy}}
				        </div>
				      </div>
				      </div>
				      <div class="col-md-4 col-sm-4">
                  		<div class="box">
				        <div class="box-body">
				        <a  class="link-black text-sm"><i class="glyphicon glyphicon-time"></i> <b>Service Restoration Date </b></a>
				        </div>
				        <!-- /.box-body -->
				        <div class="box-footer">
						{{ticketData.serviceRestorationTime}}
				        </div>
				      </div>
				      </div>
				      <div class="col-md-4 col-sm-4">
                  		<div class="box">
				        <div class="box-body">
				        <a  class="link-black text-sm"><i class="glyphicon glyphicon-user"></i> <b>Has issue been fixed permanently? </b></a>
				        </div>
				        <div class="box-footer">
						{{ ticketData.codeClosed}}
				        </div>
				      </div>
				      </div>
				      
					  </div>
					   <div class="row">
                    	<div class="col-md-12 col-sm-12">
                  		<div class="box">
				        <div class="box-body">
				        <a   class="link-black text-sm"><i class="glyphicon glyphicon-user"></i> <b>Resolution Notes </b></a>
				        
				         <ul class="list-inline">
	                    <li class="pull-right">
	                      <a href  ng-click="openChatBox();" class="link-black text-sm"><i class="fa fa-comments-o margin-r-5"></i> Comments
	                        ({{ticketComments.length}})</a></li>
	                  </ul>
				        </div>
				        <!-- /.box-body -->
				        <div class="box-footer">
						{{ticketData.closeNote}}
				        </div>
				      </div>
				      </div>
				      </div>
	
			        <div class="row">
			     	 <div class="col-md-6" >
			    	 <div class="tab-pane table-responsive" id="linkedticket">
						<div class="box box-success">
							<div class="box-header with-border">
								<h3 class="box-title">Linked ticket details</h3>
							</div>
							
							<div class="box-body" >
								<table id="example2"
									class="table table-hover table-condensed">
									<thead>
										<tr ng-if="ticketData.linkedTickets.length > 0">
											<th><b>Linked Ticket Number</b></th>
											<th><b>Status</b></th>
										</tr>
									</thead>
									<tbody ng-if="ticketData.linkedTickets.length > 0">
										<tr ng-repeat="linkedTkt in ticketData.linkedTickets" >
											<td>
												<!-- <span class="label">{{linkedTicket.linkedTicketNo}}</span>     -->
												<label class="control-label">{{linkedTkt.spLinkedTicket}}</label>
												<!-- <input type="text" class="form-control" ng-model="linkedTicket.linkedTicketNo" disabled="disabled"> -->
											</td>

											<td style="vertical-align:middle"><span
												ng-class="{{linkedTkt.closedFlag == 'CLOSED'}} ? 'label label-danger' : 'label label-success'">{{linkedTkt.closedFlag}}</span>
											</td>

										</tr>
									</tbody>
									<tbody ng-if="ticketData.linkedTickets.length == 0">
										<tr >
											<td>
												There are no linked tickets available.
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							
								
							<!-- /.box-body -->
						</div>
						</div>
						</div>
						<div class="col-md-6" >
						<div class="box box-success">
							<div class="box-header with-border">
								<h3 class="box-title">Financial Details</h3>
							</div>
							
							<div class="box-body" >
			    	 <div class="tab-pane" id="escalate">
						<table id="example2"
							class="table table-hover table-condensed">
							<thead>
								<tr>
									<th><b>Cost Name</b></th>
									<th><b>Cost (&euro;)</b></th>
									<th><b>Charge Back</b></th>
									<th><b>Billable</b></th>

								</tr>
							</thead>
							<tbody ng-if="financialCostDetails.length > 0">
								<tr ng-repeat="financialRow in financialCostDetails">
									<td>{{financialRow.costName}}</td>
									<td>{{financialRow.cost}}</td>
									<td>{{financialRow.chargeBack == 1 ? "Yes" : "No"}}</td>
									<td>{{financialRow.billable == 1 ? "Yes" : "No"}}</td>

								</tr>

							</tbody>
							<tbody ng-if="financialCostDetails.length == 0">
								<tr >
									<td>
										There are no financial cost available.
									</td>
								</tr>
							</tbody>
						</table>
						</div>
			    	 </div>
			    	 </div>
			    	 </div>
					</div>
			    	 
			    	 <!-- Escalation -->
			    	 
			    	 <div class="row" id="escalationDetails">
						<div class="col-md-6" >
						<div class="box box-success">
							<div class="box-header with-border">
								<h3 class="box-title">Escalation Details</h3>
							</div>
							
							<div class="box-body" >
			    		 <div class="tab-pane" id="escalate">
						<table id="example2"
							class="table table-hover table-condensed">
							<thead>
								<tr>
									<th><b>Escalation Level</b></th>
									<th><b>Name</b></th>
									<th><b>Email</b></th>
									<th><b>Status</b></th>

								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="escalation in escalationLevelDetails">
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
								</tr>

							</tbody>
						</table>
						</div>
			    	 </div>
			    	 </div>
			    	 </div>
					<div class="col-md-6" >
						<div class="box box-success">
							<div class="box-header with-border">
								<h3 class="box-title">Attachment Details</h3>
							</div>
							
							<div class="box-body" >
								<table id="example2"
									class="table table-hover table-condensed">
									<thead>
										<tr ng-if="ticketData.files.length > 0">
											<th><b>FileName</b></th>
											<th><b>Created On</b></th>
											<th><b>Download</b></th>
											
										</tr>
									</thead>
								 <tbody ng-repeat="file in ticketData.files">
					<tr>
						<td>{{file.fileName}}</td>
						<td>{{file.createdOn}}</td>
						<td><a href="${contextPath}/selected/file/download?keyname={{file.filePath}}" download>
						<i class="fa fa-cloud-download fa-2x" aria-hidden="true"></i></a></td>
						<td>  </td>
					</tr>
				</tbody>
									<tbody ng-if="ticketData.files.length == 0">
										<tr >
											<td>
												There are no attachment available.
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							
								
							<!-- /.box-body -->
						</div>
					</div>
					</div>
			    	 
			    	 
			    	 
			    	 <!-- Ticket History -->
			    	 
			    	 
	              	
	        		<div class="tab-pane table-responsive" id="tickethistory">
					<div class="form-group" >
						<div class="box box-success">
							<div class="box-header with-border">
								<h3 class="box-title">Ticket History</h3>
							</div>
							
							<div class="box-body" >
					
					
					
					
						<!-- <div class="tab-pane table-responsive" id="tickethistory">
							<div class="box">
								<div class="box-header with-border">
									<h3 class="box-title">Ticket History</h3>
								</div>
								<div class="box-body"> -->

									<div class="table-responsive">
										<div class="row">
											<div class="col-md-12">
												<!-- The time line -->
										<ul class="timeline timeline-inverse">
											<!-- timeline time label -->
											<li class="time-label" ng-if="ticketHistoryDetail.history.length > 0">
											<span class="bg-red" ng-show="ticketHistoryDetail.ticketCloseDate != null ">
													{{ticketHistoryDetail.ticketCloseDate}} </span>
													 <span class="bg-aqua" ng-show="ticketHistoryDetail.ticketCloseDate == null ">
                   									 {{CurrentDate}}
                  								</span>
													</li>
											<li class="time-label" ng-if="ticketHistoryDetail.history.length == 0"><span class="bg-aqua">
													Ticket is not yet updated since the creation date. </span></li>		
											<li	ng-repeat="tktHistory in ticketHistoryDetail.history">
												<i class="fa fa-user bg-aqua"></i>

												<div class="timeline-item">
													<span class="time"><i class="fa fa-clock-o"></i>
														{{tktHistory.date}}</span>

													<h3 class="timeline-header">
														{{tktHistory.description}} by {{tktHistory.name}}
													</h3>

												</div>
											</li>

											<li class="time-label"><span class="bg-green">
													Created On : {{ticketData.raisedOn}} </span></li>

											<li><i class="fa fa-clock-o bg-gray"></i></li>
										</ul>
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
				
					 <div class="col-md-6" id="chatWindow" style="display:none">
          <div class="box" >
          	<div style="width:30%;position:fixed;bottom:13px;right:0px;margin:0;background-color:#dbdcdc">
            <div class="box-header with-border">
              <h3 class="box-title" style="color:#000">Work Notes</h3>
             	 <div class="box-tools pull-right">
                    <a href="javascript:void(0);"  class="badge bg-yellow" data-toggle="tooltip" ng-click="closeWindow();">
                   		 <i class="fa fa-minus" aria-hidden="true"></i>
	                </a>
                </div>
            </div>
            <div class="box-body">
              <div class="row">
	   			 <div class="col-md-12">			 
                  <div class="direct-chat-messages" style="height: 430px;" id="messageWindow">
                  <div ng-repeat="ticket in ticketComments" id="messagebox">
                    <div class="direct-chat-msg"  >
                      <div class="direct-chat-info clearfix">
                        <span class="direct-chat-name pull-left" style="color:#000">{{ticket.createdBy}}</span>
                        <span class="direct-chat-timestamp pull-right" style="color:#000">{{ticket.createdDate}}</span>
                      </div>
                      <img class="direct-chat-img" src="${contextPath}/resources/img/swadhin.jpg" alt="message user image">
                     	 
                      <div class="direct-chat-text" id="audioMessage" style="background-color: #4CAF50;
    					color: #fff;">
                       {{ticket.comment}}
                      </div>
                      
                    </div>

					</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="box-footer">
              <div class="row">
                <div class="col-sm-12">
                      <input type="text" name="ticketMessage" id="ticketMessage" ng-model="ticketComment.comment" style="height:50px"
                      placeholder="Type Message ..." class="form-control" ng-enter="addNewComment()">
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
