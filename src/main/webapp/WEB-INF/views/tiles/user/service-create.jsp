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


<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/moment.min.js"></c:url>'></script>


<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-datetimepicker.min.js"></c:url>'></script>

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>



<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-create-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
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
		<div  ng-controller="servicecreateController" id="servicecreateWindow">
		<div style="display:none" id="loadingDiv"><div class="loader">Loading...</div></div>
				<section class="content" style="min-height:35px;display:none" id="messageWindow">
				<div class="row">
					<div class="col-md-12">
						<div class="alert alert-success alert-dismissable" id="successMessageDiv"
							style="display: none;  height: 34px;white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Success! </strong> {{successMessage}} 
							
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
			 				
				<form name="createServiceAssetform" ng-submit="saveAssetService()" >
				
				<div class="row">
			 	<div class="col-md-12">
			 	
			 		<div class="box-header with-border">
			 		<input type="hidden" id="mode" value="${mode}">
			 		<input type="hidden" class="form-control" id="siteId" value="${siteId}">
								<h3 class="box-title">{{HeaderName}}</h3>
								<!-- <span class="badge badge-success" style="background-color:green" ng-if="operation == 'EDIT' ">{{selectedAsset.siteName}}</span>
								<span class="badge badge-success" style="background-color:green" ng-if="operation == 'NEW' && selectedSite.siteId !=null">{{selectedSite.siteName}}</span>
								<div class="box-tools pull-right" style="margin-top: 0px;"> -->
									
								</div>
							</div>
				
			 <div class="box-body" style="background-color: #eee">
			 				 	<div class="row">
			 	<div class="col-md-8">
			 	<div class="box">
					<div class="box-body">
					 
					<div class="row">
						<div class="col-xs-6 required">
						<input
								name="modalInput" type="hidden" class="form-control"
								 name="serviceName" ng-model="serviceData.assetId">
							<label class="control-label">Name</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="50" name="serviceName" ng-model="serviceData.assetName"
								placeholder="Enter service name" required>
						</div>
						<div class="col-xs-6 required">
							<label class="control-label">Service code</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="20" name="serviceCode" ng-model="serviceData.assetCode"
								placeholder="Enter Asset or Service code" required>
						</div>
						
					</div><br>
					
					<div class="row">
					<div class="col-xs-6 required">
							<div class="form-group">
								<label class="control-label">Category</label> 
								<select name="serviceCategorySelect" id="serviceCategorySelect" class="form-control" required
								onchange="validateDropdownValues('serviceCategorySelect','S')" >
									
								</select>
								<input type="hidden" ng-model="assetCategory.selected">
							</div>
						</div>
					<div class="col-xs-6 required">
					
								<label class="control-label">Component Type</label> 
								<select name="servicerepairtypeSelect" id="servicerepairtypeSelect" class="form-control" required tabindex="4"
								onchange="validateDropdownValues('servicerepairtypeSelect','S')">
									
								</select>
								<input type="hidden" ng-model="serviceRepairType.selected" >
						</div>
						<!-- <div class="col-xs-5 required">
								<label class="control-label">SubComponent Type</label> 
								<select name="servicesubrepairtypeSelect" id="servicesubrepairtypeSelect" class="form-control" required tabindex="5"
								onchange="validateDropdownValues('servicesubrepairtypeSelect','S')">
									
								</select>
								<input type="hidden" ng-model="subrepairType.selected" >
						</div> -->
					</div><br>

					<div class="row">
						<div class="col-xs-6  required">
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
						<div class="col-xs-6">
							<label class="control-label">Service Provider</label> <!-- <select
							ng-options="val as val.name for val in serviceProvider.list"
								class="form-control" ng-model="serviceProvider.selected" required>
							</select> -->
							<select	name="spSelect" id="serviceSPSelect"	class="form-control" 
							onchange="validateDropdownValues('serviceSPSelect','S')">
							</select> 
							<input type="hidden" ng-model="serviceProvider.selected">
						</div>
											
						
						</div><br>
						<div class="row">
						  <div class="col-xs-6 required">
							<label class="control-label">Service contract start date</label>
							<div class="input-group date">
								<div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								</div>
								<input type="text" class="form-control pull-right dt1"
									id="commissionDate" ng-model="serviceData.commisionedDate" required>
							</div>
						</div>
						<div class="col-xs-6 ">
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
					<div class="row"><br>
						
						 <div class="col-xs-12">
							<label for="exampleInputEmail1">Comments</label> 
							<!-- <input type="text" maxlength="50" class="form-control"
								placeholder="Enter comment" name="comment" ng-model="serviceData.assetDescription"> -->
								
								<textarea class="form-control" maxlength="1000" style="width: 100%;
   				 height: 70px;" rows="3" placeholder="Enter comment" name="comment" ng-model="serviceData.assetDescription"></textarea>
						</div> 
						
						

					</div>
					<!-- <div class="row" ng-show="operation == 'EDIT'">
					
					<div class="col-xs-4 pull-right">
						
						<input id="toggledeleteService" type="checkbox" class="toggleYesNo form-control" 
										data-width="70" 
										data-toggle="toggle" data-size="small"
										data-off="Active" data-on="Delete" data-onstyle="danger" data-offstyle="primary"
										onchange="angular.element(this).scope().isDelete(this, event )">&nbsp;<b>Mark for deletion</b>
						</div>
					</div> -->
					
						
						</div>
						</div>
						</div>
						<div class="col-md-4">
			 	<div class="box">
					<div class="box-body" style="height:71%">
					 
					<div class="row">
						<div  class="col-xs-12 required" ng-if="serviceData.assetId == null" &&>
								
							<label class="control-label" ng-if="operation == 'NEW' && selectedSite.siteId == null">Site</label> 
							<!-- <label class="form-control" ng-show="operation == 'EDIT'">{{selectedAsset.siteName}}</label>
							<label class="form-control" ng-if="operation == 'NEW' && selectedSite.siteId !=null">{{selectedSite.siteName}}</label> -->	
							
							<select class="form-control" ng-show="operation == 'NEW' && selectedSite.siteId == null"" id="serviceSiteSelect" name="serviceSiteSelect" multiple="multiple" required
							onchange="validateDropdownValues('serviceSiteSelect','S')" style="height:300px"> 
							</select>
							<input type="hidden" ng-model="accessSite.selected">
						
						</div>
						<div  class="col-xs-12">
								<div class="box box-widget widget-user" ng-if="serviceData.assetId != null || selectedSite.siteId!=null">
            <!-- Add the bg color to the header using any of the bg-* classes -->
            <div class="widget-user-header bg-aqua-active">
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
				</div>		</div>
						<div class="row">
						<div class="col-xs-12" >
							<label for="exampleInputEmail1">Additional
								document (Max Size 100KB)</label> <input type="file" class="form-control" 
								id="inputServiceDocfilepath" accept=".doc, .docx,.pdf"
								name="inputServiceDocfilepath" ng-model="serviceData.documentPath" 
								onchange="angular.element(this).scope().getDocumentFile(this, event, 'serviceModalMessageDiv','fileerrorservice' )">								
												
						</div>	
						</div>	
						</div>
						</div>
						</div>
						</div>
					</div>					</div>
				<div class="box-footer pull-right">
					
					<button type="submit" class="btn btn-success" >Save changes</button>
					<button type="reset" id="resetServiceAssetForm" class="btn btn-success">RESET</button>
			</div>
			</div>
			</div>
									</form>				

						</div>
			</section>						

						</div>
						</div>

