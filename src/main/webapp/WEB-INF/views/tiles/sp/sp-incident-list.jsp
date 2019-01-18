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


<link rel="stylesheet"  href='<c:url value="/resources/theme1/css/ionicons.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />

						
<link rel="stylesheet" href='<c:url value="/resources/theme1/css/jquery.dataTables.min.css"></c:url>' />
						
<link rel="stylesheet" href='<c:url value="/resources/theme1/css/responsive.bootstrap.min.css"></c:url>' />


<script type="text/javascript"  src='<c:url value="/resources/theme1/js/jquery.dataTables.min.js"></c:url>'></script>
<script type="text/javascript"  src='<c:url value="/resources/theme1/js/dataTables.bootstrap.min.js "></c:url>'></script>
<script type="text/javascript"   src='<c:url value="/resources/theme1/js/dataTables.responsive.min.js"></c:url>'></script>
<script type="text/javascript"   src='<c:url value="/resources/theme1/js/responsive.bootstrap.min.js"></c:url>'></script>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/incident-modal.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/sp-incident-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/asset-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
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
	
	
	/* $('#ticketList').DataTable({
		"processing": true
		
	}); */
	
	$('.toggle-on').removeAttr('style');
	 $('.toggle-off').removeAttr('style');
	 
   $('body').on('focus',".issueStartDate", function(){
       $(this).datepicker({
    	   format:'dd-mm-yyyy',
       });
   });
   $('.select2').select2();
   
   var isIE = window.ActiveXObject || "ActiveXObject" in window;
   if (isIE) {
       $('.modal').removeClass('fade');
   } 
  
      
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
<div class="content-wrapper">
	<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
		<div  ng-controller="spIncidentController" id="spIncidentWindow">
		<div style="display:none" id="loadingDiv"><div class="loader">Loading...</div></div>
			<section class="content" style="min-height: 35px; display: none"
				id="messageWindow">
				<div class="row">
					<div class="col-md-12">
						<div class="alert alert-success alert-dismissable"
							id="successMessageDiv"
							style="display: none; height: 34px; white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Success! </strong> {{successMessage}} 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
						<div class="alert alert-info alert-dismissable"
							id="infoMessageDiv"
							style="display: none; height: 34px; white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Info! </strong> {{InfoMessage}} 
							<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
						</div>
					</div>
				</div>
			</section>	
			<section class="content">
				<div class="row">
				<div class="col-md-12">
						<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">My Tickets</h3>
								<div class="box-tools pull-right">
								<c:if test="${savedsp.spId ne null}">
									<a 	class="btn btn-success dropdown-toggle pull-right"
										style="margin-right: 5px;" data-toggle="dropdown"><span class="fa fa-hand-o-down">
										Action<span class="caret"></span>
									</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href ng-click="viewUpdatePage()"  id="updateTicket"><span class="fa fa-edit"></span> Update Ticket </a></li>	
										<li><a href ng-click="viewSelectedTicket()"  id="closedTicket"><span class="fa fa-eye"></span> View Ticket </a></li>
									</ul>
								</</c:if>
										
									
								</div>
							</div>
						<!-- 	<div class="box-body" style="height:70%;margin-top:10px;" >
							 <div class="row">
							 <div class="col-md-12">
								<div style="overflow-x: hidden;overflow-y:auto; height: 110%">
										<table id="ticketList" class="table table-bordered table-striped" cellspacing="0">
										</table>
									</div>
							</div>
					
							</div>
								
                         </div> -->
                                <div class="box-body table-responsive no-padding">
              <table class="table table-hover">
              <tbody style="font-size: .9em"><tr ">
					<th style="width:12%">Ticket Number</th>
					<th style="width:10%">Site Name</th>
					<th style="width:10%">Asset Name</th>
					<th style="width:20%">Title</th>
					<th>Created On</th>
					<th>Sla Due Date</th>
					<th>Service Provider</th>
					<th tyle="width:10%">Status</th>
					
					</tr>
                <tr ng-repeat="val in ticket.list | filter: ticketsearch" ng-class="{currentSelected:$index == selectedRow}" 
               	 ng-click="setTicketinSession(val);rowHighilited($index)">
                	<th class="todo-list">
                  {{val.ticketNumber}}
                  <a href="#" ng-click="previewSelectedIncidentInfo(val)" data-toggle="modal"  ><i class="fa fa-eye pull-right"></i></a>
                  </th>
					<td>{{val.siteName}}</td>
					<td>{{val.assetName}}</td>
					<td>{{val.ticketTitle}}</td>
					<td>{{val.raisedOn}}</td>
					<td>{{val.sla}}</td>
					<td>{{val.assignedSP}}</td>
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
                            
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
										<a  class="btn btn-danger pull-left">Total Tickets :  <span class="badge">{{ticket.list.length}}</span></a>
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
											<sec:authorize access="hasAnyRole('ROLE_SP_EXTERNAL')">
								<div class="btn-group pull-right" ng-if="ticket.list.length> 0">
								</div>
								</sec:authorize>
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
						
								<div class="modal right fade" id="previewIncidentModal" tabindex="-1" role="dialog" aria-labelledby="previewIncidentModalLabel2">
		<div class="modal-dialog" role="document">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel2">{{sessionTicket.ticketNumber}} - Overview</h4>
					<span class="badge" style="background-color: #cfe4e2;color:#000"><i class="fa fa-flag" aria-hidden="true" style="color:red;"></i> {{sessionTicket.status}}</span>
				</div>
				<div class="modal-body">
				       <div class="box box-solid">
            <div class="box-header with-border">
              <i class="fa fa-text-width"></i>
              <h3 class="box-title">{{sessionTicket.ticketTitle}}</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <dl>
                <dt>Description</dt>
                <dd>{{sessionTicket.description}}</dd>
              </dl>
            </div>
          </div>
								<div class="box box-widget widget-user" ng-if="sessionTicket.ticketNumber != null">
            <!-- Add the bg color to the header using any of the bg-* classes -->
            <div class="widget-user-header bg-aqua-active">
              <h3 class="widget-user-username">Site : {{sessionTicket.siteName}}</h3>
              <h5 class="widget-user-desc">Owner : {{sessionTicket.siteOwner}}</h5>
            </div>
           <!--  <div class="widget-user-image">
              <img class="img-circle" src="../dist/img/user1-128x128.jpg" alt="User Avatar">
            </div> -->
            <div class="box-footer">
              <div class="row">
                <div class="col-sm-4 border-right">
                  <div class="description-block">
                    <h5 class="description-header">Contact Name</h5>
                    <span class="description-text">{{sessionTicket.assignedSP}}</span>
                  </div>
                </div>
                 <div class="col-sm-4 border-right">
                  <div class="description-block">
                    <h5 class="description-header">Site Number</h5>
                    <span class="description-text">{{sessionTicket.siteContact}}</span>
                  </div>
                </div>
                <div class="col-sm-4 border-right">
                  <div class="description-block">
                    <h5 class="description-header">Email</h5>
                    <span class="description-text">{{sessionTicket.email}}</span>
                  </div>
                  <!-- /.description-block -->
                </div>
              </div>
              <!-- /.row -->
            </div>
          </div>
          						<div class="box box-widget widget-user" ng-if="sessionTicket.ticketNumber != null">
            <!-- Add the bg color to the header using any of the bg-* classes -->
            <div class="widget-user-header bg-green-active">
              <h3 class="widget-user-username">Asset : {{sessionTicket.assetName}}</h3>
              <h5 class="widget-user-desc">Code : {{sessionTicket.assetCode}}</h5>
            </div>
           <!--  <div class="widget-user-image">
              <img class="img-circle" src="../dist/img/user1-128x128.jpg" alt="User Avatar">
            </div> -->
            <div class="box-footer">
              <div class="row">
                <div class="col-sm-4 border-right">
                  <div class="description-block">
                    <h5 class="description-header">Service Provider</h5>
                    <span class="description-text">{{sessionTicket.assignedSP}}</span>
                  </div>
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <div class="col-sm-4 border-right">
                  <div class="description-block">
                    <h5 class="description-header">Commissioned On</h5>
                    <span class="description-text">{{sessionTicket.assetCommissionedDate}}</span>
                  </div>
                  <!-- /.description-block -->
                </div>
                <div class="col-sm-4 border-right">
                  <div class="description-block">
                 <h5 class="description-header">Sub Category</h5>
                    <span class="description-text">{{sessionTicket.assetSubCategory1}}</span>
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
              <span class="info-box-number">{{sessionTicket.slaPercent}}</span>
              <div class="progress">
               <div class="progress-bar"
				ng-class="{'progress-bar-danger': sessionTicket.slaPercent >=100, 'progress-bar-warning': sessionTicket.slaPercent>75 && sessionTicket.slaPercent<100, 'progress-bar-info': sessionTicket.slaPercent>0 && sessionTicket.slaPercent<75}"
				role="progressbar"
				ng-style="{width: sessionTicket.width+'%'}">
				 <span class="progress-text">SLA(%) {{sessionTicket.slaPercent}} </span>
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
				
		
				</div>
				</section>
				
		
		</div>
	</div>

