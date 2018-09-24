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

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/incident-create-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
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
   
/*    $(document).on('click', '.btn-add', function(e)
		    {
		        e.preventDefault();

		        var controlForm = $('.controls:first'),
		            currentEntry = $(this).parents('.entry:first'),
		            newEntry = $(currentEntry.clone()).appendTo(controlForm);

		        newEntry.find('input').val('');
		        newEntry.find('input#incidentFile').Id('2');
		        controlForm.find('.entry:not(:last) .btn-add')
		            .removeClass('btn-add').addClass('btn-remove')
		            .removeClass('btn-success').addClass('btn-danger')
		            .html('<span class="glyphicon glyphicon-minus"></span>');
		    }).on('click', '.btn-remove', function(e)
		    {
		      $(this).parents('.entry:first').remove();
		      angular.element(this).scope().getFileDetails(this); 
				e.preventDefault();
				return false;
			});
   
   $(document).on('click', '.btn-close', function(e)
		    {
	   			
		        e.preventDefault();

		        var controlForm = $('.controls:first'),
		            currentEntry = $(this).parents('.entry:first'),
		            newEntry = $(currentEntry.clone()).appendTo(controlForm);

		        newEntry.find('input').val('');
		        
		    })
      
  }) */
  
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
			<div class="box">
			 <form name="createticketform" ng-submit="saveTicket()" >
		 			<div class="box-header with-border">
		 			<div class="col-md-12">
							<h3 class="box-title">Create New Incident</h3>
							<div class="box-tools pull-right" style="margin-top: 0px;">
								
							</div>
						</div>
						</div>
					<div class="box-body" style="height:72%" >
					<input type="hidden" id="mode" value="${mode}">
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
								  onchange="getSelectedSite('siteSelect')" required>
									
								 </select>
								 <input type="hidden" ng-model="accessSite.selected">
				                </div>
				                </div>
							<div class="row">							
							 <div class="col-md-6">							  	
							 <label class="control-label" for="ticketDescription">Ticket Description</label>
								  <textarea class="form-control" style="width: 100%;
   							 height: 158px;" rows="3" placeholder="Enter ticket description" name="title" 
   				 			ng-model="ticketData.description"></textarea>	
							 </div>
							    <div class="col-xs-6 reqDiv required">
				                <label class="control-label">Asset&nbsp;&nbsp;</label>
				              <!--   <input name="asset_type" id="equipment" value="equipment" type="radio" ng-click="equipmentChecked()" />Equipment&nbsp;&nbsp;&nbsp;
				                <input name="asset_type" id="service" value="service" type="radio" ng-click="serviceChecked()" />Service&nbsp;&nbsp;&nbsp;&nbsp; -->
				                 <input name="asset_type" id="equipment" value="equipment" ng-model="assetType" type="radio" ng-change="populateAssetType(assetType)" />Equipment&nbsp;&nbsp;&nbsp;
				                <input name="asset_type" id="service" value="service" ng-model="assetType" type="radio" ng-change="populateAssetType(assetType)" />Service
				               <a href data-toggle="modal" class="pull-right" ng-click="openAssetModal()" title="Please click to add asset if not available in list">
				               <i class="fa fa-info-circle" aria-hidden="true"></i> <b>Add Asset</b></a>
				                 <select name="assetSelect" id="assetSelect" class="form-control" onchange="getSelectedAsset('assetSelect')"
								   required></select>
								   	<input type="hidden" ng-model="selectedAsset.selected">
								   	
			          <div class="box box-widget widget-user-2" ng-if="assetType!=null && assetList.selected.assetName!=null">
			            <div class="box-footer no-padding">
			              <ul class="nav nav-stacked">
			                <li><a href="#"><b>Category</b> <span class=" pull-right badge bg-blue">{{assetList.selected.assetCategoryName}}</span></a></li>
			                <li><a href="#"><b>Component Type</b> <span class=" pull-right  badge bg-aqua">{{assetList.selected.assetSubcategory1}}</span></a></li>
			                <li><a href="#"><b>Assigned SP <span class="badge">(E)</span></b><span class=" pull-right  badge bg-green">{{assetList.selected.assignedSp}}</span></a></li>
			              </ul>
			            </div>
			          </div>
								   	
				                </div>
							</div>
							
							<!-- 	<div class="row">
							   <div class="col-xs-6 reqDiv required">
				                <label class="control-label">Site</label>				                 
				                  <select name="siteSelect" id="siteSelect" class="form-control" 
								  onchange="getSelectedSite('siteSelect')" required>
									
								 </select>
								 <input type="hidden" ng-model="accessSite.selected">
				                </div>
				                <div class="col-xs-6 reqDiv required">
				                <label class="control-label">Asset&nbsp;&nbsp;</label>
				                <input name="asset_type" id="equipment" value="equipment" type="radio" ng-click="equipmentChecked()" />Equipment&nbsp;&nbsp;&nbsp;
				                <input name="asset_type" id="service" value="service" type="radio" ng-click="serviceChecked()" />Service&nbsp;&nbsp;&nbsp;&nbsp;
				                 <input name="asset_type" id="equipment" value="equipment" ng-model="assetType" type="radio" ng-change="populateAssetType(assetType)" />Equipment&nbsp;&nbsp;&nbsp;
				                <input name="asset_type" id="service" value="service" ng-model="assetType" type="radio" ng-change="populateAssetType(assetType)" />Service
				               <a href data-toggle="modal" class="pull-right" ng-click="openAssetModal()" title="Please click to add asset if not available in list">
				               <i class="fa fa-info-circle" aria-hidden="true"></i> <b>Add Asset</b></a>
				                 <select name="assetSelect" id="assetSelect" class="form-control" onchange="getSelectedAsset('assetSelect')"
								   required></select>
								   	<input type="hidden" ng-model="selectedAsset.selected">
				                </div>
								</div> -->
								<div class="row">
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
									<div class="col-xs-6 reqDiv required">
										   <label class="control-label">Ticket Category</label>
				                  <select name="ticketCategorySelect" id="ticketCategorySelect" class="form-control" 
								onchange="getSelectedCategory('ticketCategorySelect')" required>
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
								</div>
								<div class="row">
									 <div class="col-xs-6 reqDiv required" id="statusComponent">
					                <label class="control-label" >Status</label>
					                <input type="hidden" ng-model="ticketData.status" class="form-control" required>
					                  <select name="statusSelect" id="statusSelect" class="form-control" required
									onchange="ticketStatusChange('statusSelect')">
								</select>
								<input type="hidden" ng-model="selectedTicketStatus.selected">
					                </div>
					                <div class="col-xs-6">
					               <label class="control-label" > Please click <a href data-toggle="modal" ng-click="openFileAttachModal()"><b>Here</b></a> to attach File.</label>
					              
					              <a href class="btn btn-warning">Files attached <span class="badge">{{incidentImages.length}}</span> 
					              <span class="badge" id="totalIncidentSize"></span></a>
					              
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
								
						<!-- 		<div class="row">
								<div class="col-xs-3 required">
											<label class="control-label">Category</label> 
											<input type="text" class="form-control"  id="category"
								                  name="category" ng-model="ticketData.assetCategoryName"  required readonly>	
											<input type="hidden" ng-model="ticketData.assetCategoryId" >
								</div>
							<div class="col-xs-4 required">
										<label class="control-label">Component Type</label> 
										<input type="text" class="form-control"  id="componentType"
							                  name="componentType" ng-model="ticketData.assetSubcategory1"  required readonly>
							                  	
										<input type="hidden" ng-model="ticketData.subCategoryId1" >
								</div>
						<div class="col-xs-5 required">
								<label class="control-label">SubComponent Type</label> 
								<select name="subcomponentTypeSelect" id="subcomponentTypeSelect" class="form-control" required tabindex="5"
								onchange="validateDropdownValues('subcomponentTypeSelect','E')">
									
								</select>
								<input type="hidden" ng-model="subcomponentTypeSelect.selected" >
						</div>
							</div>
								
								<div class="row">
								<div class="col-xs-4 reqDiv required">
					               <label class="control-label">Assigned To</label>				                 				                  
				                  <input type="text" class="form-control"  id="assignedTo"
					                  name="assignedTo" ng-model="ticketData.assignedTo"  required readonly>								
					                </div>
								<div class="col-xs-4 reqDiv required">
				                <label class="control-label">Ticket Category</label>
				                 				                  
				                  <select name="ticketCategorySelect" id="ticketCategorySelect" class="form-control" 
								onchange="getSelectedCategory('ticketCategorySelect')" required>
									
								</select>
								<input type="hidden" ng-model="selectedCategory.selected" >
	               			   </div>
	               			 <div class="col-xs-4 reqDiv required">
					                <label class="control-label" >Priority</label>
					                  <input type="text"  class="form-control"  
					                  name="priority" ng-model="ticketData.priorityDescription" disabled="disabled"  required >
					                </div>
					                
					                
								 
								<div class="col-xs-4 reqDiv required">
					               <label class="control-label">Raised By</label>
					               <input type="text"  class="form-control"  
					                  name="raisedBy" ng-model="ticketData.raisedBy"  required readonly>				                  
								
					                </div>
									 
					                
								</div>
								<div class="row">
								 
					             <div class="col-xs-4 reqDiv required">
					                <label class="control-label">SLA Duration (Days/Hours)</label>
					                  <input type="text" class="form-control" ng-model="ticketData.slaTime" disabled="disabled"
					                    required>
					               </div>
					               
					               <div class="col-xs-4 reqDiv required">
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
	               			 <div class="col-xs-4 reqDiv required">
					                <label class="control-label" >Status</label>
					                <input type="text" ng-model="ticketData.status" class="form-control" required>
					                  <select name="statusSelect" id="statusSelect" class="form-control" required
								onchange="ticketStatusChange('statusSelect')">
									
								</select>
								<input type="hidden" ng-model="selectedTicketStatus.selected">
					                </div>
					                
					                  
								<div class="col-xs-4 reqDiv required">
					                <label class="control-label">Raised On</label>
					                  <input type="text" class="form-control" 
					                   ng-model="ticketData.raisedOn" disabled="disabled" required >
					                </div>
									
					                
								</div> -->								
						
						
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
			
		
		
		<div class="modal fade" id="equipmentModal" data-keyboard="false" data-backdrop="static">
		<div class="modal-dialog" style="width: 80%;">
			<div class="modal-content">
			 <form name="createassetform" ng-submit="saveAssetEquipment()" >
				<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title"><span id="assetModalLabel">Add new Asset</span>   |  <a class="btn btn-info">Asset Type 
			<span class="badge">Equipment</span></a></h4>
			<div class="alert alert-danger " id="equipmentModalMessageDiv"
				style="display: none;  height: 34px;white-space: nowrap;">
				<strong>Error! </strong> {{equipmentModalErrorMessage}} <span id="fileerrorasset"></span>
				<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
			</div>

		</div>


			 <div class="modal-body" style="background-color: #eee">
			 	<div class="row">
			 	<div class="col-md-12">
			 	<div class="box">
					<div class="box-body">
					 
					<div class="row">
						<div class="col-xs-3 required">
						<input
								name="modalInput" type="hidden" class="form-control"
								 name="sitename" ng-model="equipmentData.assetId">
							<label class="control-label">Name</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="50" name="sitename" ng-model="equipmentData.assetName"
								placeholder="Enter equipment name" required tabindex="1">
						</div>
						<div class="col-xs-3 required">
							<label class="control-label">Asset Code</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="20" name="assetcode" ng-model="equipmentData.assetCode"
								placeholder="Enter asset code" required tabindex="2">
						</div>
						<div class="col-xs-3">
							<label for="exampleInputEmail1">Model</label> <input
								name="modalInput" type="text" class="form-control" tabindex="3"
								maxlength="20" name="model" ng-model="equipmentData.modelNumber"
								placeholder="Enter model number">
						</div>
						
					</div>
					
					<div class="row">
					<div class="col-xs-3 required">
								<label class="control-label">Category</label> 
								<select name="categorySelect" id="categorySelect" class="form-control" required tabindex="3"
								onchange="validateDropdownValues('categorySelect','E')">
									
								</select>
								<input type="hidden" ng-model="assetCategory.selected" >
					</div>
					<div class="col-xs-4 required">
								<label class="control-label">Component Type
								<a href="#"	class="pull-right" data-toggle="popover" data-trigger="hover" data-html="true"
									data-popover-content="#subcomponentpop"> <span class="fa fa-info-circle" aria-hidden="true"></span></a></label> 
								<select name="repairtypeSelect" id="repairtypeSelect" class="form-control" required tabindex="4"
								onchange="validateDropdownValues('repairtypeSelect','E')">
									
								</select>
								<input type="hidden" ng-model="repairType.selected" >
						</div>
						<!-- <div class="col-xs-5 required">
								<label class="control-label">SubComponent Type</label> 
								<select name="subrepairtypeSelect" id="subrepairtypeSelect" class="form-control" required tabindex="5"
								onchange="validateDropdownValues('subrepairtypeSelect','E')">
									
								</select>
								<input type="hidden" ng-model="subrepairType.selected" >
						</div> -->
					</div>

					<div class="row">
						<div class="col-xs-3">
							<label for="exampleInputEmail1">Content</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="50" name="content" ng-model="equipmentData.content"
								placeholder="Enter content">
						</div>
						<div class="col-xs-3 required">
								<label class="control-label">Location</label> 
								<select name="locationSelect" id="locationSelect" class="form-control" required 
								onchange="validateDropdownValues('locationSelect','E')">
									
								</select>
								<input type="hidden" ng-model="assetLocation.selected" required>
						</div>
						<div class="col-xs-3">
							<label for="exampleInputEmail1">Picture (Max Size 100KB)</label> <input
								type="file" class="form-control" id="inputImgfilepath" 
								name="inputImgfilepath" ng-model="equipmentData.imagePath"  accept="image/*"
								onchange="angular.element(this).scope().getImageFile(this, event )">
						</div>
						<div class="col-xs-3" >
							<label for="exampleInputEmail1">Additional
								document (Max Size 100KB)</label> <input type="file" class="form-control"
								id="inputDocfilepath" name="inputDocfilepath" ng-model="equipmentData.documentPath" 
								accept=".doc, .docx,.pdf" onchange="angular.element(this).scope().getDocumentFile(this, event , 'equipmentModalMessageDiv', 'fileerrorasset')">
						</div>

					</div>

					<div class="row">
						<div class="col-xs-3">
							<label class="control-label">Service Provider</label> <!-- <select
							ng-options="val as val.name for val in serviceProvider.list"
								class="form-control" ng-model="serviceProvider.selected" required>
							</select> -->
							<select	name="spSelect" id="spSelect"	class="form-control" 
							onchange="validateDropdownValues('spSelect','E')">
							</select> 
							<input type="hidden" ng-model="serviceProvider.selected">
						</div>
						<div class="col-xs-3 required">
							<label class="control-label">Date of Asset
								commission</label>
							<div class="input-group date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right dt1"
									id="commission" ng-model="equipmentData.commisionedDate" required>
							</div>
						</div>
						<div class="col-xs-3 ">
							<label class="control-label">Date of Asset
								decommission</label>
							<div class="input-group date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right dt1"
									id="decommission" ng-model="equipmentData.deCommissionedDate" >
							</div>
						</div>
						

					</div>
					<div class="row">
						<div class="col-xs-3 required">
							<label class="control-label">Is Asset
								Electrical</label> <select id="drpIsAsset"  required
								class="form-control" >
								<option value="">Select Asset Electrical</option>
								<option value="YES">YES</option>
								<option value="NO">NO</option>
							</select>
						</div>
						<div class="col-xs-3">
							<label for="exampleInputEmail1">Is a power sensor
								attached</label> <select id="drpIsPowersensor"
								class="form-control" >
								<option value="">Select Sensor Attached</option>
								<option value="YES">YES</option>
								<option value="NO">NO</option>
							</select>
						</div>

						<div class="col-xs-3">
							<label for="exampleInputEmail1">Sensor Number</label> <input
								type="text" maxlength="20" id="txtSensorNumber"
								class="form-control" placeholder="Enter sensor Number"
								name="comment" ng-model="equipmentData.pwSensorNumber">
						</div>

					</div>
					<div class="row">
					<div class="col-xs-9">
							<!-- <label for="exampleInputEmail1">Comments</label> <input
								type="text" maxlength="50" class="form-control"
								placeholder="Enter comment" name="comment" ng-model="equipmentData.assetDescription"> -->
								
								<label for="exampleInputEmail1">Comments</label> <!-- <input
								type="text" maxlength="500" class="form-control"
								placeholder="Enter comment" name="comment" ng-model="equipmentData.assetDescription"> -->
								<textarea class="form-control" maxlength="1000" style="width: 100%;
   				 height: 70px;" rows="3" placeholder="Enter comment" name="comment" ng-model="equipmentData.assetDescription"></textarea>
						</div>
						</div>
						
						
					
					</div>
						</div>
						</div>
						</div>
					</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default pull-left"	id="assetModalCloseBtn" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-success" >Save changes</button>
					<button type="reset" id="resetAssetForm" class="btn btn-success">RESET</button>
			</div>
									</form>
									
									
								</div>
							</div>

						</div>
	
	
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
		<div class="modal fade" id="serviceModal" data-keyboard="false" data-backdrop="static">
		<div class="modal-dialog" style="width: 80%;">
			<div class="modal-content">
			 <form name="createServiceAssetform" ng-submit="saveAssetService()" >
				<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title"><span id="assetServiceModalLabel">Add new Asset</span>   |  <a class="btn btn-info">Asset or Service type 
			<span class="badge">Service</span></a></h4>
			<div class="alert alert-danger alert-dismissable" id="serviceModalMessageDiv"
				style="display: none;  height: 34px;white-space: nowrap;">
				<strong>Error! </strong> {{serviceModalErrorMessage}} <span id="fileerrorservice"></span> 
				<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
			</div>

		</div>


			 <div class="modal-body" style="background-color: #eee">
			 	<div class="row">
			 	<div class="col-md-12">
			 	<div class="box">
					<div class="box-body">
					 
					<div class="row">
						<div class="col-xs-4 required">
						<input
								name="modalInput" type="hidden" class="form-control"
								 name="serviceName" ng-model="serviceData.assetId">
							<label class="control-label">Name</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="50" name="serviceName" ng-model="serviceData.assetName"
								placeholder="Enter service name" required>
						</div>
						<div class="col-xs-4 required">
							<label class="control-label">Service Code</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="20" name="serviceCode" ng-model="serviceData.assetCode"
								placeholder="Enter service code" required>
						</div>
						
						<div class="col-xs-4 required">
							<div class="form-group">
								<label class="control-label">Category</label> <!-- <select 
								ng-options="val as val.assetCategoryName for val in assetCategory.list"
									class="form-control" ng-model="assetCategory.selected" required>
								</select> -->
								<select name="serviceCategorySelect" id="serviceCategorySelect" class="form-control" required
								onchange="validateDropdownValues('serviceCategorySelect','S')">
									
								</select>
								<input type="hidden" ng-model="assetCategory.selected">
							</div>
						</div>
					</div>
					
					<div class="row">
					
					<div class="col-xs-4 required">
								<label class="control-label">Component Type</label> 
								<select name="servicerepairtypeSelect" id="servicerepairtypeSelect" class="form-control" required tabindex="4"
								onchange="validateDropdownValues('servicerepairtypeSelect','S')">
									
								</select>
								<input type="hidden" ng-model="repairType.selected" >
						</div>
						<!-- <div class="col-xs-5 required">
								<label class="control-label">SubComponent Type</label> 
								<select name="servicesubrepairtypeSelect" id="servicesubrepairtypeSelect" class="form-control" required tabindex="5"
								onchange="validateDropdownValues('servicesubrepairtypeSelect','S')">
									
								</select>
								<input type="hidden" ng-model="subrepairType.selected" >
						</div> -->
					</div>

					<div class="row">
						
						<div class="col-xs-4  required">
								<label class="control-label">Location</label> <!-- <select
								ng-options="val as val.locationName for val in assetLocation.list"
									class="form-control" ng-model=" assetLocation.selected" required>
									<option></option>
								</select> -->
								<select name="serviceLocationSelect" id="serviceLocationSelect" class="form-control" required 
								onchange="validateDropdownValues('serviceLocationSelect','S')">
									
								</select>
								<input type="hidden" ng-model="assetLocation.selected">
						</div>
						
						<div class="col-xs-4">
							<label for="exampleInputEmail1">Additional
								document (Max Size 100KB)</label> <input type="file" class="form-control" 
								id="inputServiceDocfilepath" accept=".doc, .docx,.pdf"
								name="inputServiceDocfilepath" ng-model="serviceData.documentPath" 
								onchange="angular.element(this).scope().getDocumentFile(this, event, 'serviceModalMessageDiv','fileerrorservice' )">
						</div>
						
						<div class="col-xs-4">
							<label class="control-label">Service Provider</label> <!-- <select
							ng-options="val as val.name for val in serviceProvider.list"
								class="form-control" ng-model="serviceProvider.selected" required>
							</select> -->
							<select	name="serviceSPSelect" id="serviceSPSelect"	class="form-control" 
							onchange="validateDropdownValues('serviceSPSelect','S')">
							</select> 
							<input type="hidden" ng-model="serviceProvider.selected">
						</div>
						

					</div>

					<div class="row">
						
						<div class="col-xs-4 required">
							<label class="control-label">Service contract start date</label>
							<div class="input-group date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right dt1"
									id="commissionDate" ng-model="serviceData.commisionedDate" required>
							</div>
						</div>
						<div class="col-xs-4 ">
							<label class="control-label">Service contract end date</label>
							<div class="input-group date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right dt1"
									id="decommissionDate" ng-model="serviceData.deCommissionedDate" >
							</div>
						</div>
						

					</div>
					<div class="row">
						
					<div class="col-xs-8">
					
							<label for="exampleInputEmail1">Comments</label> <!-- <input
								type="text" maxlength="500" class="form-control"
								placeholder="Enter comment" name="comment" ng-model="equipmentData.assetDescription"> -->
								<textarea class="form-control" maxlength="1000" style="width: 100%;
   				 height: 70px;" rows="3" placeholder="Enter comment" name="comment" ng-model="serviceData.assetDescription"></textarea>
							
							
						</div>
						
						<!-- <div class="col-xs-4 required">
							<label class="control-label">Site</label> <select
							   ng-options="val as val.siteName for val in accessSite.list"
								class="form-control" ng-model="accessSite.selected" required>
							</select>
							
							<select class="form-control" id="serviceSiteSelect" name="serviceSiteSelect" required
							onchange="validateDropdownValues('serviceSiteSelect','S')">
							</select>
							<input type="hidden" ng-model="accessSite.selected">
						</div> -->

					</div>
					</div>
						</div>
						</div>
						</div>
					</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default pull-left"	id="serviceModalCloseBtn" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-success" >SAVE CHANGES</button>
					<button type="reset" id="resetServiceAssetForm" class="btn btn-success">RESET</button>
			</div>
									</form>
									
									
								</div>
							</div>

						</div>
						
<div class="modal" id="fileAttachModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="false">
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

