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

<link rel="stylesheet" media="screen" href='<c:url value="/resources/theme1/css/bootstrap-datetimepicker.css"></c:url>' />
<link rel="stylesheet" media="screen" href='<c:url value="/resources/theme1/css/bootstrap-datetimepicker-standalone.css"></c:url>' />
<link rel="stylesheet" media="screen" href='<c:url value="/resources/css/bootstrap-datetimepicker.min.css"></c:url>' />


<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.min.js"></c:url>'></script>

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/sp-incident-create-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/asset-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
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
 #modalMessageDiv,  #serviceModalMessageDiv, #equipmentModalMessageDiv{
   top: -7%;
    left: 64%;
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

.col-xs-3.required .control-label:after {
  content:"*";
  color:red;
}
.col-xs-4.required .control-label:after {
  content:"*";
  color:red;
}
.reqDiv.required .control-label:after {
  content:"*";
  color:red;
}
.popover {max-width:500px;}

.entry:not(:first-of-type)
{
    margin-top: 10px;
}

.glyphicon
{
    font-size: 12px;
}

</style>

<script>

$(function() {
	
	$('input').attr('autocomplete', 'off');
	
	 $('#datetimepicker1').datetimepicker({
		 format:"DD-MM-YYYY HH:mm"
	 }); 

	 $(".dt1").datepicker({
         format:"dd-mm-yyyy"
     })
	
	$('.toggle-on').removeAttr('style');
	 $('.toggle-off').removeAttr('style');
	 
   
   $('.select2').select2();
   
   var isIE = window.ActiveXObject || "ActiveXObject" in window;
   if (isIE) {
       $('.modal').removeClass('fade');
   } 
  
   $('#btnUploadCancel').click(function(){
		$('#upload-files').modal('toggle');
	});
});
   

  
	  function validate_tab(thisform) {          
         $('.tab-pane input, .tab-pane select, .tab-pane textarea').on('invalid', function() {
                
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
		<div  ng-controller="spIncidentCreateController" id="spIncidentCreateWindow">
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
					</div>
				</div>
			</section>
			<section class="content">
			<div class="box">
			 <form name="spcreateticketform" ng-submit="saveTicket()" >
		 			<div class="box-header with-border">
		 			<div class="col-md-12">
							<h3 class="box-title">Create New Incident</h3>
							<div class="box-tools pull-right" style="margin-top: 0px;">
								
							</div>
						</div>
						</div>
					<div class="box-body" style="height:72%" >
					<input type="hidden" id="mode" value="${mode}">
					 <div class="col-md-12">
						<div class="row" >
		                   <div class="col-md-6">
		                      <span class="badge pull-left"> <i class="fa fa-user" aria-hidden="true"></i> Raised by: {{ticketData.raisedBy}}
		                   </div>
		                   <div class="col-md-6" >
		                        <span class="badge pull-right"> <i class="fa fa-clock-o" aria-hidden="true"></i>   Raised On: {{CurrentDate}}</span>
		                  </div>
                		</div>
							<div class="row">
							<div class="col-md-6 reqDiv required">							  	
							 <label class="control-label" for="title">Ticket Title</label>
							 <input type="text" class="form-control" placeholder="Enter ticket title" name="title" maxlength="50"
					                   ng-model="ticketData.ticketTitle"  required >
							 </div>
							 
							   <div class="col-xs-6 reqDiv required">
				                <label class="control-label">Site</label>				                 
				                  <select name="siteSelect" id="siteSelect" class="form-control" 
								  onchange="angular.element(this).scope().getAssetForSelectedSite(this, event, 'siteSelect')" required>
									
								 </select>
								 <input type="hidden" ng-model="accessSite.selected">
				                </div>
				                </div>
							<div class="row">							
							 <div class="col-md-6">							  	
							 <label class="control-label" for="ticketDescription">Ticket Description</label>
								  <textarea class="form-control" style="width: 100%;
		   							 height: 106px;" rows="3" placeholder="Enter ticket description" name="title" 
		   				 			ng-model="ticketData.description"></textarea>
		   				 			<div class="row">
		   				 			 <div class="col-md-12">	
		   				 			 	 <label class="control-label">Service Provider Agents</label>				                 
				                  <select name="spAgentsSelect" id="spAgentsSelect" class="form-control" 
								  onchange="angular.element(this).scope().getSPAgentSelected(this, event, 'spAgentsSelect')" required>
								 </select> 
		   				 			</div>
		   				 			</div>
				                </div>		
							    <div class="col-xs-6 reqDiv required">
				                <label class="control-label">Asset&nbsp;&nbsp;</label>
				              <!--   <input name="asset_type" id="equipment" value="equipment" type="radio" ng-click="equipmentChecked()" />Equipment&nbsp;&nbsp;&nbsp;
				                <input name="asset_type" id="service" value="service" type="radio" ng-click="serviceChecked()" />Service&nbsp;&nbsp;&nbsp;&nbsp; -->
				                 <input name="asset_type" id="equipment" value="equipment" ng-model="assetType" type="radio" ng-change="populateAssetType(assetType)" />Equipment&nbsp;&nbsp;&nbsp;
				                <input name="asset_type" id="service" value="service" ng-model="assetType" type="radio" ng-change="populateAssetType(assetType)" />Service
				                 <select name="assetSelect" id="assetSelect" class="form-control" onchange="angular.element(this).scope().getSelectedAsset(this, event, 'assetSelect')" 
								   required></select>
								   	<input type="hidden" ng-model="selectedAsset.selected">
								   	
			          <div class="box box-widget widget-user-2" ng-if="assetType!=null && assetList.selected.assetName!=null">
			            <div class="box-footer no-padding">
			              <ul class="nav nav-stacked">
			                <li><a href="#"><b>Category</b> <span class=" pull-right badge bg-blue">{{assetList.selected.assetCategoryName}}</span></a></li>
			                <li><a href="#"><b>Component Type</b> <span class=" pull-right  badge bg-aqua">{{assetList.selected.assetSubcategory1}}</span></a></li>
			                <li><a href="#"><b>Assigned SP <span class="badge">E</span></b><span class=" pull-right  badge bg-green">
			                {{assetList.selected.assignedSp}}  - {{assetList.selected.spHelpDeskEmail}} </span></a></li>
			              </ul>
			            </div>
			          </div>
								 	
				                </div>
							</div>
							</div>
								
								<div class="col-md-8">
								<div class="row">
								<div class="col-xs-6 required">
								<label class="control-label">SubComponent Type</label> 
								<select name="subcomponentTypeSelect" id="subcomponentTypeSelect" class="form-control" required tabindex="5"
								onchange="angular.element(this).scope().validateDropdownValues('subcomponentTypeSelect','E')">
									
								</select>
								<input type="hidden" ng-model="subcomponentTypeSelect.selected" >
								</div>
								   <div class="col-xs-6 reqDiv required">
					                <label class="control-label">Issue Start Time</label>
					                <div class="form-group">
				                <div class='input-group date' id='datetimepicker1'>
				                    <input type='text' class="form-control" ng-model="ticketData.ticketStartTime" id="ticketStartTime" required/>
				                    <span class="input-group-addon">
				                        <span class="glyphicon glyphicon-calendar"></span>
				                    </span>
				                </div>
			               		 </div>
			                	</div>
			                	</div>
			                		<div class="row">
									 <div class="col-xs-6 reqDiv required" id="statusComponent">
					                <label class="control-label" >Status</label>
					                <input type="hidden" ng-model="ticketData.status" class="form-control" required>
					                  <select name="statusSelect" id="statusSelect" class="form-control" required
									onchange="angular.element(this).scope().ticketStatusChange('statusSelect')">
								</select>
								<input type="hidden" ng-model="selectedTicketStatus.selected">
					                </div>
					                <div class="col-xs-6">
					               <label class="control-label" > Please click <a href data-toggle="modal" ng-click="openFileAttachModal()"><b>Here</b></a> to attach File.</label>
					              
					              <a href class="btn btn-warning">Files attached <span class="badge">{{incidentImages.length}}</span> 
					              <span class="badge" id="totalIncidentSize"></span></a>
					              
					               </div>
	               			   </div>
			                	</div>
									<div class="col-xs-4 reqDiv required">
										   <label class="control-label">Ticket Category</label>
				                  <select name="ticketCategorySelect" id="ticketCategorySelect" class="form-control" 
								onchange="angular.element(this).scope().getSelectedCategory('ticketCategorySelect')" required>
								</select>
								<input type="hidden" ng-model="selectedCategory.selected" >
									<div class="box box-widget widget-user-2" ng-if="ticketData.priorityId!=null">
						            <div class="box-footer no-padding">
						              <ul class="nav nav-stacked">
						                <li><a href="#"><b>Priority</b> <span class=" pull-right badge bg-blue">{{ticketData.priorityDescription}}</span></a></li>
						                <li><a href="#"><b>SLA Duration (Days/Hours)</b><span class=" pull-right  badge bg-red">{{ticketData.slaTime}}</span></a></li>
						              </ul>
						            </div>
						          </div>
									</div>
									
							
	               			   	<div class="row" id="ticketCloseDiv">
							<div class="col-xs-4 reqDiv required">
					                <label class="control-label" >Close Code</label>
					                  <select name="closeCodeSelect" id="closeCodeSelect" class="form-control" required>
									
								</select>
								<input type="hidden" ng-model="selectedCloseCode.selected">
					                </div>
					        <div class="col-xs-4 reqDiv required">
					                <label class="control-label">Closed By</label>
					                  <input type="text" class="form-control" 
					                  name="raisedOn" id="raisedOn" ng-model="ticketData.closedBy"  required >
					                </div>
							<div class="col-md-4 reqDiv required">
							  	
							 <label class="control-label" >Close Note</label>
								  <textarea class="form-control" style="width: 100%;
   				 height: 70px;" rows="3" placeholder="Enter ticket close note" name="closeNote" id="closeNote" 
   				 ng-model="ticketData.closeNote"></textarea>	
							 </div>
							
							</div>
													
						
						
								</div>
						<div class="box-footer">
							<div class="col-md-12 ">
							<div class="pull-right">		
							<button type="submit" class="btn btn-success " >Save changes</button>
							<button type="reset" id="reseCreateTicketForm" class="btn btn-success">RESET</button>
							</div>
							</div>	
						</div>
				
									</form>									
								

						</div>
			</section>	
			
		
		
	
	
	
		<div id="subcomponentpop" class="hidden" style="width: 30px">
			<div class="popover-body">
			<table id="example2" class="table table-hover table-condensed">
			<thead>
				<tr>					
					<th><b>SubComponent</b></th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="subrepair in subRepairType.list">
					<td><label class="control-label">{{subrepair.assetSubcategory2}}</label></td>
				</tr>
			</tbody>
			</table>
			</div>
		</div>
		
						
<div class="modal" id="fileAttachModal" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h4 class="modal-title" id="upload-avatar-title">Upload new files</h4>
          <div class="alert alert-danger alert-dismissable" id="incidentImageModalMessageDiv"
				style="display: none;  height: 34px;white-space: nowrap;">
				<strong>Error! </strong> {{incidentImageModalErrorMessage}} <span id="fileerrorincident"></span>
				<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
			</div>
        </div>
        <div class="modal-body">
          <p>Please choose a file to upload. image/*,.pdf only.</p>
          <form role="form">
          <div class="controls">
              <div class="entry input-group col-xs-12">
                <input type="button" class="btn btn-success addnew" style="margin-right: 5px;" onclick="" ng-click="addNewImage()" value="Add New">
                <div  style="overflow-y:auto;height:250px">
                <table id="example2" class="table  table-hover table-condensed">
                 <tbody>
                   <tr ng-repeat="incidentImage in incidentImageList">
                   <td align="center">
                  <span class="badge" style="margin-top: 6px;"> {{$index + 1}} </span> 
                   </td>
                   <td>
	                 <input type="file" id="incidentImage{{$index}}" class="form-control" 
	                  name="incidentImage[{{$index}}]" accept="image/*,.pdf" 
	                  onchange="angular.element(this).scope().getIndexedName(this, event)" style="width:80%">
	                  </td>
	                  <td>
	                  <span id="imgsize{{$index}}" class="badge"></span>
                  </td>
                  <td>
                   <a class="btn btn-danger" href  ng-click="removeImage($index)" >
                  <i class="fa fa-trash-o" aria-hidden="true" style="font-size: 1.4em;"></i>
                  </a>
                  </td>
                </tr>	
                </tbody>
                </table>
                </div>
                <span class="badge" id="totalSize"></span>	
              </div>
              
           
          </div>
            </br>
            <button type="button" class="btn btn-default" id="btnUploadCancel" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-success" ng-click="uploadFiles('NEW')" value="Upload" id="uploadImgBtn">Upload</button>
          </form>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
<!-- </div> -->

</div>
						</div>
						</div>

