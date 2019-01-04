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
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />


<%-- <script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.js"></c:url>'></script> --%>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/moment.min.js"></c:url>'></script>


<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.min.js"></c:url>'></script>

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>



<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/equipment-create-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-provider-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/asset-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>


<style>
		.main-box.no-header {
    padding-top: 20px;
}


.currentSelected{
	background:rgba(60, 141, 188, 0.58);
    color:#fff;
}
.currentSelected a{
color:#fff
}
.col-xs-3 label {
    font-weight: bold;
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
 #modalMessageDiv, #serviceModalMessageDiv, #equipmentModalMessageDiv{
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

.col-xs-5.required .control-label:after {
  content:"*";
  color:red;
}

.col-xs-6.required .control-label:after {
  content:"*";
  color:red;
}

.col-xs-12.required .control-label:after {
  content:"*";
  color:red;
}
</style>

<script>

$(document).ready(function()  {
	$('input').attr('autocomplete', 'off');
	
	 $('.toggle-on').removeAttr('style');
	 $('.toggle-off').removeAttr('style');
	 
	 
	 $(".dt1").datepicker({
        format:"dd-mm-yyyy"
    })
    
	/* $('siteSelect').multiselect();
	 $('serviceSiteSelect').multiselect();  */	 
	
 })
  
</script>
</head>
<div class="content-wrapper">
		<div  ng-controller="equipmentcreateController" id="equipmentcreateWindow">
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
				<form name="createassetform" ng-submit="saveAssetEquipment()" >
				<div class="row">
			 	<div class="col-md-12">
			 	
			 		<div class="box-header with-border">
			 		<input type="hidden" id="mode" value="${mode}">
			 		<input type="hidden" class="form-control" id="siteId" value="${siteId}">
								<h3 class="box-title">{{HeaderName}}</h3>
								<!-- <span class="badge badge-success" style="background-color:green" ng-if="operation == 'EDIT' ">{{selectedAsset.siteName}}</span> -->
								<span class="badge badge-success" style="background-color:green" ng-if="operation == 'NEW' && selectedSite.siteId !=null">{{selectedSite.siteName}}</span>
								<div class="box-tools pull-right" style="margin-top: 0px;">
									
								</div>
							</div>
				
			 <div class="box-body" style="background-color: #eee">
			 	<div class="row">
			 	<div class="col-md-8">
			 	<div class="box">
					<div class="box-body">
					 
					<div class="row">
						<div class="col-xs-12 required">
						<input
								 type="hidden" class="form-control"
								 name="sitename" ng-model="equipmentData.assetId">
							<label class="control-label">Name</label> <input
								 type="text" class="form-control"
								maxlength="50" name="sitename" ng-model="equipmentData.assetName"
								placeholder="Enter equipment name" required tabindex="1">
								<span class="slider round"></span>
						</div>
						
						
					</div><br>
					
					<!-- <div class="row"> -->
					<!-- <div class="col-md-9 "> -->
						
					
					<div class="row">
					
					<div class="col-xs-4 required">
							<label class="control-label">Asset code</label> <input
								 type="text" class="form-control"
								maxlength="20" name="assetcode" ng-model="equipmentData.assetCode"
								placeholder="Enter Asset or Service code" required tabindex="2">
						</div>
						<div class="col-xs-4">
							<label for="exampleInputEmail1">Model</label> <input
								 type="text" class="form-control" tabindex="3"
								maxlength="20" name="model" ng-model="equipmentData.modelNumber"
								placeholder="Enter model number">
						</div>
						<div class="col-xs-4 required">
								<label class="control-label">Location</label> 
								<select name="locationSelect" id="locationSelect" class="form-control" required 
								onchange="validateDropdownValues('locationSelect','E')">
									
								</select>
								<input type="hidden" ng-model="assetLocation.selected" required>
						</div>
					</div>
					<br>
					<div class="row">
					
					<div class="col-xs-4 required">
								<label class="control-label">Category</label> 
								<select name="categorySelect" id="categorySelect" class="form-control" required tabindex="3"
								onchange="validateDropdownValues('categorySelect','E')">
									
								</select>
								<input type="hidden" ng-model="assetCategory.selected" >
						</div>
					<div class="col-xs-4 required">
								<label class="control-label">Component Type</label> 
								<select name="repairtypeSelect" id="repairtypeSelect" class="form-control" required tabindex="4"
								onchange="validateDropdownValues('repairtypeSelect','E')">
									
								</select>
								<input type="hidden" ng-model="repairType.selected" >
						</div>
						<div class="col-xs-4">
							  <input name="sp_type" id="rsp" value="RSP" ng-model="spType" type="radio" ng-change="populateServiceProvider(spType)" /><b>Registered SP</b>&nbsp;&nbsp;&nbsp;
				                <input name="sp_type" id="ext" value="EXT" ng-model="spType" type="radio" ng-change="populateServiceProvider(spType)" /><b>External SP</b>
							<!-- <select
							ng-options="val as val.name for val in serviceProvider.list"
								class="form-control" ng-model="serviceProvider.selected" required>
							</select> -->
							<select	name="spSelect" id="spSelect"	class="form-control" 
							onchange="validateDropdownValues('spSelect','E')">
							</select> 
							<input type="hidden" ng-model="serviceProvider.selected">
						</div>
					
					</div>
					<br>
					<div class="row">
					
						
						
						
						
						<!-- <div class="col-xs-5 required">
								<label class="control-label">SubComponent Type</label> 
								<select name="subrepairtypeSelect" id="subrepairtypeSelect" class="form-control" required tabindex="5"
								onchange="validateDropdownValues('subrepairtypeSelect','E')">
									
								</select>
								<input type="hidden" ng-model="subrepairType.selected" >
						</div> -->
					</div>
					
										
					<div class="row">
						<div class="col-xs-4" >
							<label for="exampleInputEmail1">Picture (Max Size 100KB)</label> <input
								type="file" class="form-control" id="inputImgfilepath" 
								name="inputImgfilepath" ng-model="equipmentData.imagePath"  accept="image/*"
								onchange="angular.element(this).scope().getImageFile(this, event )">
						</div>
						<div class="col-xs-8">
							<label for="exampleInputEmail1">Additional
								document (Max Size 100KB)</label> <input type="file" class="form-control"
								id="inputDocfilepath" name="inputDocfilepath" ng-model="equipmentData.documentPath" 
								accept=".doc, .docx,.pdf" 
								onchange="angular.element(this).scope().getDocumentFile(this, event, 'equipmentModalMessageDiv','fileerrorasset' )">
						</div>
						<div class="col-xs-4">
						<!-- <label class="control-label" ng-if="operation == 'EDIT' || (operation == 'NEW' && selectedSite.siteId !=null)">Site</label> -->
						<!-- <label class="form-control" ng-if="operation == 'EDIT'">{{selectedAsset.siteName}}</label>
							<label class="form-control" ng-if="operation == 'NEW' && selectedSite.siteId !=null">{{selectedSite.siteName}}</label> -->	
						</div>
					</div><br>
					
					<div class="row">
					<div class="col-md-12 ">
						<div class="row">
						
						<div class="col-xs-4 required">
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
						<div class="col-xs-4 ">
							<label class="control-label">Date of Asset decommission</label>
							<div class="input-group date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right dt1"
									id="decommission" ng-model="equipmentData.deCommissionedDate" >
							</div>
						</div>
						<div class="col-xs-4">
							<label for="exampleInputEmail1">Content</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="50" name="content" ng-model="equipmentData.content"
								placeholder="Enter content">
						</div>
						</div><br>
						<div class="row">
						<div class="col-xs-4 required">
							<label class="control-label">Is Asset
								Electrical</label> <select id="drpIsAsset"  required
								class="form-control" >
								<option value="">Select Asset Electrical</option>
								<option value="YES">YES</option>
								<option value="NO">NO</option>
							</select>
						</div>
						<div class="col-xs-4">
							<label for="exampleInputEmail1">Is a power sensor
								attached</label> <select id="drpIsPowersensor"
								class="form-control" >
								<option value="">Select Sensor Attached</option>
								<option value="YES">YES</option>
								<option value="NO">NO</option>
							</select>
						</div>
						<div class="col-xs-4">
							<label for="exampleInputEmail1">Sensor Number</label> <input
								type="text" maxlength="20" id="txtSensorNumber"
								class="form-control" placeholder="Enter sensor Number"
								name="comment" ng-model="equipmentData.pwSensorNumber">
						</div>

						
						</div>
						<div class="row">
						
						
						</div>
						</div>
					<!-- 	<div  class="col-xs-3 required">
							<div class="col-md-12">
							<label class="control-label" ng-if="operation == 'NEW' && selectedSite.siteId == null" >Site</label> 															
							<Select ng-if="operation == 'NEW' && selectedSite.siteId == null"   class="form-control select2" id="siteSelect" name="siteSelect" multiple="multiple" required
							onchange="validateDropdownValues('siteSelect','E')" style="height:100px">
							</Select>
							<input type="hidden" ng-model="accessSite.selected">
							</div>
						</div> -->
					</div>
					<br>
					<div class="row">
						 <div class="col-xs-12">
							<label for="exampleInputEmail1">Comments</label> <!-- <input
								type="text" maxlength="500" class="form-control"
								placeholder="Enter comment" name="comment" ng-model="equipmentData.assetDescription"> -->
								<textarea class="form-control" maxlength="1000" style="width: 100%;
   				 height: 70px;" rows="3" placeholder="Enter comment" name="comment" ng-model="equipmentData.assetDescription"></textarea>
						</div>
						
						<!-- <div class="col-xs-3" ng-show="operation == 'EDIT'">
						<label for="exampleInputEmail1">&nbsp;&nbsp;</label></br></br>
						<input id="toggledelete" type="checkbox" class="toggleYesNo form-control" 
										data-width="70" 
										data-toggle="toggle" data-size="small"
										data-off="Active" data-on="Delete" data-onstyle="danger" data-offstyle="primary"
										onchange="angular.element(this).scope().isDelete(this, event )">&nbsp;<b>Mark for deletion</b>
						</div> -->
					</div>
					</div>
						</div>
						</div>
						
						<div class="col-xs-4 ">
						<div class="box" ng-if="operation == 'NEW' && selectedSite.siteId == null">
						<div class="box-body">
						<div class="row">
						    <div class="col-md-12 required">
							<label class="control-label" ng-if="operation == 'NEW' && selectedSite.siteId == null" >Site</label> 															
							<Select ng-if="operation == 'NEW' && selectedSite.siteId == null"   
							class="form-control select2" id="siteSelect" name="siteSelect" multiple="multiple" required
							onchange="validateDropdownValues('siteSelect','E')" style="height:500px">
							</Select>
							
							<input type="hidden" ng-model="accessSite.selected">
							</div>
						</div>
						</div>
						</div>
						<div class="box box-widget widget-user" ng-if="equipmentData.assetId != null || selectedSite.siteId !=null">
            <!-- Add the bg color to the header using any of the bg-* classes -->
           <div class="widget-user-header" style="background-color: #00a65a;color:#fff">
              <h3 class="widget-user-username">Site : {{selectedSite.siteName}}</h3>
              <h5 class="widget-user-desc">Owner : {{selectedSite.siteOwner}}</h5>
            </div>
           <!--  <div class="widget-user-image">
              <img class="img-circle" src="../dist/img/user1-128x128.jpg" alt="User Avatar">
            </div> -->
            <div class="box-footer">
              <div class="row">
                <div class="col-sm-6 border-right">
                  <div class="description-block">
                    <h5 class="description-header">Contact Name</h5>
                    <span class="description-text">{{selectedSite.contactName}}</span>
                  </div>
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <div class="col-sm-6 border-right">
                  <div class="description-block">
                    <h5 class="description-header">Email</h5>
                    <span class="description-text">{{selectedSite.email}}</span>
                  </div>
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <!-- /.col -->
              </div>
              <!-- /.row -->
            </div>
          </div>
          <div class="box box-widget widget-user" ng-if="equipmentData.assetId != null ">
            <!-- Add the bg color to the header using any of the bg-* classes -->
          <div class="widget-user-header" style="background-color: #00a65a;color:#fff">
              <h3 class="widget-user-username">{{selectedAsset.serviceProviderName}}</h3>
              <h5 class="widget-user-desc">SP Type : <span ng-if="selectedAsset.spType=='RSP'">REGISTERED</span>
              <span ng-if="selectedAsset.spType=='EXT'">EXTERNAL</span></h5>
            </div>
           <!--  <div class="widget-user-image">
              <img class="img-circle" src="../dist/img/user1-128x128.jpg" alt="User Avatar">
            </div> -->
            <div class="box-footer">
              <div class="row">
                <div class="col-sm-6 border-right">
                  <div class="description-block">
                    <h5 class="description-header">Contact Email</h5>
                    <span class="description-text">{{selectedAsset.spHelpDeskEmail}}</span>
                  </div>
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <div class="col-sm-6 border-right">
                  <div class="description-block">
                   <!--  <h5 class="description-header">Email</h5>
                    <span class="description-text">{{selectedSite.email}}</span> -->
                  </div>
                  <!-- /.description-block -->
                </div>
                <!-- /.col -->
                <!-- /.col -->
              </div>
              <!-- /.row -->
            </div>
          </div>
          
          
						</div>
						</div>
					</div>
				<div class="box-footer pull-right">
					
					<button type="submit" class="btn btn-success" >Save changes</button>
					<button type="reset" id="resetAssetForm" class="btn btn-success">RESET</button>
			</div>
			</div>
			</div>
									</form>				

						</div>
			</section>						

						</div>
						</div>

