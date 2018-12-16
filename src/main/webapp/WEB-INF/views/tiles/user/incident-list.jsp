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



<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/incident-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/asset-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>




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
#errorMessageDiv, #successMessageDiv{
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

 #infoMessageDiv{
    top: 0%;
    left: 50%;
   /*  width: 45em; */
    height: 3em;
    margin-top: 4em;
    margin-left: -12em;
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
		<div ng-controller="incidentController" id="incidentWindow">
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
								<h3 class="box-title">List of Tickets</h3>
								<!-- 	<select name="spCustomerListSelect" id="spCustomerListSelect"
										class="form-control"
										onchange="angular.element(this).scope().getCustomerIncident(this, event, 'spCustomerListSelect')"
										required>
									</select> -->
								<div class="box-tools pull-right">
								<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER', 'ROLE_SITE_STAFF')">
									<a 	class="btn btn-success dropdown-toggle pull-right"
										style="margin-right: 5px;" data-toggle="dropdown"><span class="fa fa-hand-o-down"></span>
										Action<span class="caret"></span>
									</a>
									<ul class="dropdown-menu" role="menu">
										<li><a href="${contextPath}/incident/create/page"   >
										<i class="fa fa-plus" arial-hidden="true"></i> Create Ticket</a></li>
										<li><a href ng-click="viewUpdatePage()"  id="updateTicket"><span class="fa fa-edit"></span> Update Ticket </a></li>	
										<li><a href ng-click="viewSelectedTicket()"  id="closedTicket"><span class="fa fa-eye"></span> View Ticket </a></li>
									</ul>
								</sec:authorize>
										
									
								</div>
							</div>
							<div class="box-body" style="height:70%" >
							
							 <div class="row">
							 <div class="col-md-12">
								<div style="overflow-x: hidden;overflow-y:auto; height: 110%">
								<div class="table-responsive"> 
										<table id="ticketList" class="table table-bordered table-striped" cellspacing="0">
										</table>
									</div>	
									</div>
							</div>
					
							</div>
								
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
											<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
								<div class="btn-group pull-right" ng-if="ticket.list.length> 0">
								<!-- <button popover-template="'popover'" popover-placement="bottom" popover-trigger="mouseenter" type="button"
           								 class="btn btn-default">Comment</button> -->
								
								<!-- <a href class="btn btn-success" style="margin-right: 5px;" popover-template="'popover'"
									 data-placement="bottom" popover-trigger="mouseenter"  >Comment <span class="fa fa-comment"></span></a> -->
																	
							<!-- 	<a href ng-click="viewUpdatePage()" class="btn btn-success pull-right" id="updateTicket"><span class="fa fa-edit"></span> Update Ticket </a>	
								<a href ng-click="viewSelectedTicket()" class="btn btn-success pull-right" id="closedTicket"><span class="fa fa-edit"></span> View Ticket </a>	
								 -->
								</div>
								</sec:authorize>
										</div>
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
				</div>
				</section>
		</div>
	</div>

