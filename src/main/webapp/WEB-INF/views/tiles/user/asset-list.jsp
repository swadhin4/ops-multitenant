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
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/moment.min.js"></c:url>'></script>


<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/asset-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
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
     
	$('siteSelect').multiselect();
	 $('serviceSiteSelect').multiselect(); 	 
	
  })
  
</script>
</head>
<div class="content-wrapper">
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
		<div ng-controller="assetController" id="assetWindow">
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
				<div class="col-md-6">
						<div class="box" >
							<div class="box-header with-border">
								<h3 class="box-title">List of Assets and Services</h3>
								<div class="box-tools pull-right" style="margin-top: 0px;">
									<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER','ROLE_SITE_STAFF')">
										<a href class="btn btn-success dropdown-toggle pull-right" data-toggle="dropdown"><span class="fa fa-plus"></span>
										Add Asset or Service <span class="caret"></span>
									  </a>
								  	<ul class="dropdown-menu" role="menu">
										<!-- <li><a href data-toggle="modal" ng-click="addEquipment()">Add an Equipment</a></li> -->
										<li><a href  ng-click="openAssetPage('E')" ><span class="fa fa-plus"></span>Add an Equipment</a></li>
										<li><a href ng-click="openAssetPage('S')"><span class="fa fa-plus"></span>Add	a Service</a></li>
									</ul>
									</sec:authorize>	
									
								</div>
							</div>
							<div class="box-body" style="overflow-y:auto;overflow-x:hidden;height:58%"  >
									<div class="row">
	 								<div class="col-md-12">
										<input type="text" class="form-control"	placeholder="Search Asset" ng-model="searchAsset">
										<input type="hidden" class="form-control" id="siteId" value="${siteId}">
									 </div>
									 </div>
									 <div class="row">
									 <div class="col-md-12">
									 <div class="row">
									 <div class="col-md-12">
										<div class="table-responsive">
												<table id="example1"
													class="table table-bordered">
													<thead>
														<tr>
															<th>Name</th>
															<th>Category</th>
															<th>Site</th>
														</tr>
													</thead>
													<tbody>
														<tr ng-repeat="asset in asset.list | filter: searchAsset | orderBy :'assetName'"
															ng-class="{currentSelected:$index == selectedRow}" 
															ng-click="getAssetDetails(asset);rowHighilited($index)" >
															<td ng-if="asset.spType=='EXT'"><span class="badge" style="background-color:red">E</span>
																	{{asset.assetName}}
															</td><td ng-if="asset.spType=='RSP'"><span class="badge" style="background-color:green">R</span>
																	{{asset.assetName}}
															</td>
															<td>
																{{asset.assetCategoryName}}</td>
															<td>
																{{asset.siteName}}</td>
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
										<a  class="btn btn-danger">Total Assets :  <span class="badge">{{asset.list.length}}</span></a>
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
									<div class="box-tools pull-right">
									<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER','ROLE_SITE_STAFF')">
									<div class="btn-group pull-right">
											<a href ng-click="openAssetPage()" ng-if="asset.list.length>0"
										class="btn btn-success"	style="margin-right: 5px;" data-toggle="modal"><span class="fa fa-edit"></span>
										Edit 
									</a>
									</div>
									</sec:authorize>
									</div>
								</div>
							<div class="box-body" style="overflow-y:auto;overflow-x:hidden;height:58%">
								<div class="row">
								<div class="col-md-12">
									 <div class="table-responsive">
										<table class="table no-margin" ng-if="selectedAsset.assetType == 'E' ">
											<!-- <thead> -->
											<tr><td style="width:50%">Name</td><td align="right">{{selectedAsset.assetName}}</td>
											</tr>
											<tr><td style="width:50%">
											<span >Asset Code</span>
											</td>
											<td align="right">{{selectedAsset.assetCode}}</td>
											</tr>
											<!-- </thead> -->
											<tbody>
											<tr><td>Model</td><td align="right">{{selectedAsset.modelNumber}}</td></tr>
											<tr><td>Category</td><td align="right">{{selectedAsset.assetCategoryName}}</td></tr>
											<tr><td>Component Type</td><td align="right">{{selectedAsset.assetSubcategory1}}</td></tr>
											<!-- <tr><td>SubComponent Type</td><td align="right">{{selectedAsset.assetSubcategory2}}</td></tr> -->
											<tr><td>Service Provider</td><td align="right">
											<span class="badge" style="background-color:red" ng-if="selectedAsset.spType=='EXT'">E</span> 
											<span class="badge" style="background-color:green" ng-if="selectedAsset.spType=='RSP'">R</span> 
											{{selectedAsset.serviceProviderName}}</td>
											</tr>
											<tr><td>Location</td><td align="right">{{selectedAsset.locationName}}</td></tr>
											<tr><td>Date of Commission</td><td align="right">{{selectedAsset.commisionedDate}}</td></tr>
											<tr><td>Date of DeCommission</td><td align="right">{{selectedAsset.deCommissionedDate}}</td></tr>
											<tr><td>Site</td><td align="right">{{selectedAsset.siteName}}</td></tr>
											<tr><td>Content</td><td align="right">{{selectedAsset.content}}</td></tr>
											
											<tr><td>Is Asset Electrical</td>
											<td align="right"><span ng-if="selectedAsset.isAssetElectrical=='YES'" style="color:#16db16;">
											<i class="fa fa-check-circle-o fa-2x" aria-hidden="true"></i></span>
											<span ng-if="selectedAsset.isAssetElectrical=='NO' || selectedAsset.isAssetElectrical == NULL " style="color:red">
											<i class="fa fa-times-circle-o fa-2x" aria-hidden="true"></i></span>
											</td></tr>
											
											<tr><td>Is Power Sensor Attached</td><td align="right">
											<span ng-if="selectedAsset.isPWSensorAttached=='YES'" style="color:#16db16;">
											<i class="fa fa-check-circle-o fa-2x" aria-hidden="true"></i></span>
											<span ng-if="selectedAsset.isPWSensorAttached=='NO' ||  selectedAsset.isPWSensorAttached == NULL" style="color:red">
											<i class="fa fa-times-circle-o fa-2x" aria-hidden="true"></i></span>
											</td></tr>
											
											<tr>
											<td>Sensor Number</td><td align="right">
												{{selectedAsset.pwSensorNumber}}
											</td></tr>
											<tr>
											<td>Attached Picture</td>
											<td align="right" ng-if="selectedAsset.imagePath!=null">
												
												<a href="${contextPath}/selected/file/download?keyname={{selectedAsset.imagePath}}" download>
												<i class="fa fa-picture-o fa-2x" aria-hidden="true"></i></a>
												<a href ng-click="deleteFile('ASSET', selectedAsset,'IMG')" data-toggle="tooltip" data-original-title="Delete this file">
												<i class="fa fa-remove fa-2x" aria-hidden="true"></i></a>
											</td></tr>
											<tr>
											<td>Attached Additional Document</td>
											<td align="right" ng-if="selectedAsset.documentPath!=null">
												<a href="${contextPath}/selected/file/download?keyname={{selectedAsset.documentPath}}" download><i class="fa fa-file-text fa-2x" aria-hidden="true"></i></a>
												<a href ng-click="deleteFile('ASSET', selectedAsset,'DOC')" data-toggle="tooltip" data-original-title="Delete this file">
												<i class="fa fa-remove fa-2x" aria-hidden="true"></i></a>
											</td></tr>
											</tbody>
										</table>
										
										<table class="table no-margin" ng-if="selectedAsset.assetType == 'S' ">
											<thead>
											<tr><td style="width:40%">Name</td><td align="right">{{selectedAsset.assetName}}</td>
											</tr>
											<tr><td style="width:40%">Service Code</td><td align="right">{{selectedAsset.assetCode}}</td>
											</tr>
											</thead>
											<tbody>
											<tr><td>Category</td><td align="right">{{selectedAsset.assetCategoryName}}</td></tr>
											<tr><td>Component Type</td><td align="right">{{selectedAsset.assetSubcategory1}}</td></tr>
											<!-- <tr><td>SubComponent Type</td><td align="right">{{selectedAsset.assetSubcategory2}}</td></tr> -->
											<tr><td>Location</td><td align="right">{{selectedAsset.locationName}}</td></tr>
											<tr><td>Service Provider</td><td align="right"> 
											<span class="badge" style="background-color:red" ng-if="selectedAsset.spType=='EXT'">E</span> 
											<span class="badge" style="background-color:green" ng-if="selectedAsset.spType=='RSP'">R</span> 
											{{selectedAsset.serviceProviderName}}</td>
											</tr>
											<tr><td>Service contract start date</td><td align="right">{{selectedAsset.commisionedDate}}</td></tr>
											<tr><td>Service contract end date</td><td align="right">{{selectedAsset.deCommissionedDate}}</td></tr>
											<tr><td>Site</td><td align="right">{{selectedAsset.siteName}}</td></tr>
											<tr>
											<td>Attached Additional Document</td>
											<td align="right" ng-if="selectedAsset.documentPath!=null">
												<a href="${contextPath}/selected/file/download?keyname={{selectedAsset.documentPath}}" download>
												<i class="fa fa-file-text fa-2x" aria-hidden="true"></i></a>
												<a href ng-click="deleteFile('ASSET', selectedAsset,'DOC')" data-toggle="tooltip" data-original-title="Delete this file">
												<i class="fa fa-remove fa-2x" aria-hidden="true"></i></a>
											</td></tr>
											</tbody>
										</table>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
									<div class="box-header with-border">
										<h3 class="box-title" style="margin-left:-12px">Description</h3>
									</div>
									</div>
									<div style="margin-left:10px">
									<div class="col-md-12">
										{{selectedAsset.assetDescription}}
									</div>	
									</div>
								</div>
							</div>
           
								
							
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
											<a href class="btn btn-success" ng-if="selectedAsset.assetType == 'S' ">Asset type <span class="badge">SERVICE</span></a>
											<a href class="btn btn-success" ng-if="selectedAsset.assetType == 'E' ">Asset type <span class="badge">EQUIPMENT</span></a>
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									
									<!-- /.col -->
									<div class="col-sm-4 col-xs-6">
										<div class="description-block">
										
														<span class="pull-right" >
														
									Mark for deletion <input id="toggledelete" type="checkbox" class="toggleYesNo form-control" 
										data-width="70" 
										data-toggle="toggle" data-size="small"
										data-off="Active" data-on="Delete" data-onstyle="danger" data-offstyle="success"
										onchange="angular.element(this).scope().isDelete(this, event )"> </span>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
	 <div class="modal fade" style="position: absolute" id="confirmDelete" data-dismiss="modal" data-keyboard="false" data-backdrop="static" data-toggle="modal" role="dialog" aria-labelledby="confirmDeleteLabel" aria-hidden="true"> 
  <div class="modal-dialog"> 
    <div class="modal-content"> 
      <div class="modal-header"> 
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>  
        <h4 class="modal-title">Delete Asset / Service</h4> 
      </div> 
      <div class="modal-body"> 
        <p>Are you sure you want to delete the asset ?</p>  
      </div> 
    <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" ng-click="resetDelete()">No</button>  
        <button type="button" class="btn btn-danger" id="confirm" ng-click="confirmDelete()">Yes</button>
      </div>  
    </div>  
  </div>  
</div> 
	 <%-- <div class="modal fade" id="equipmentModal" data-keyboard="false" data-backdrop="static">
		<div class="modal-dialog" style="width: 80%;">
			<div class="modal-content">
			 <form name="createassetform" ng-submit="saveAssetEquipment()" >
				<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>				
			</button>
			<h4 class="modal-title"><span id="assetModalLabel">Add new Asset</span>   |  <a class="btn btn-info">Asset or Service type 
			<span class="badge">Equipment</span> 
			</a>
			</h4>
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
								<span class="slider round"></span>
						</div>
						<div class="col-xs-3 required">
							<label class="control-label">Asset code</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="20" name="assetcode" ng-model="equipmentData.assetCode"
								placeholder="Enter Asset or Service code" required tabindex="2">
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
								<label class="control-label">Component Type</label> 
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
						<div class="col-xs-3" ng-if="equipmentData.assetId!=null">
							<label for="exampleInputEmail1">Picture (Max Size 100KB)</label> <input
								type="file" class="form-control" id="inputImgfilepath" 
								name="inputImgfilepath" ng-model="equipmentData.imagePath"  accept="image/*"
								onchange="angular.element(this).scope().getImageFile(this, event )">
						</div>
						<div class="col-xs-3" ng-if="equipmentData.assetId!=null">
							<label for="exampleInputEmail1">Additional
								document (Max Size 100KB)</label> <input type="file" class="form-control"
								id="inputDocfilepath" name="inputDocfilepath" ng-model="equipmentData.documentPath" 
								accept=".doc, .docx,.pdf" 
								onchange="angular.element(this).scope().getDocumentFile(this, event, 'equipmentModalMessageDiv','fileerrorasset' )">
						</div>

					</div>

					<div class="row">
					<div class="col-md-9 col-sm-9">
						<div class="row">
						<div class="col-xs-4">
							<label class="control-label">Service Provider</label> <!-- <select
							ng-options="val as val.name for val in serviceProvider.list"
								class="form-control" ng-model="serviceProvider.selected" required>
							</select> -->
							<select	name="spSelect" id="spSelect"	class="form-control" 
							onchange="validateDropdownValues('spSelect','E')">
							</select> 
							<input type="hidden" ng-model="serviceProvider.selected">
						</div>
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
						</div>
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
						</div>
						<div  class="col-xs-3 required">
							<div class="col-md-12">
							<label class="control-label">Site</label> <!-- <select
							   ng-options="val as val.siteName for val in accessSite.list"
								class="form-control" ng-model="accessSite.selected" required>
							</select> -->
							<label class="form-control" ng-if="operation == 'EDIT'">{{selectedAsset.siteName}}</label>
																
							<Select  ng-if="operation == 'ADD'" class="form-control" id="siteSelect" name="siteSelect" multiple="multiple" required
							onchange="validateDropdownValues('siteSelect','E')">
							</Select>
							<input type="hidden" ng-model="accessSite.selected">
							</div>
						</div>
					</div>
					<div class="row">
						 <div class="col-xs-9">
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

						</div> --%>
						
	<%-- <div class="modal fade" id="serviceModal" data-keyboard="false" data-backdrop="static">
		<div class="modal-dialog" style="width: 80%;">
			<div class="modal-content">
			 <form name="createServiceAssetform" ng-submit="saveAssetService()" >
				<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title"><span id="assetServiceModalLabel">Add new Asset</span>   |  <a class="btn btn-info">Asset or Service type 
			<span class="badge">Service</span>			
			</a>
		
			</h4>
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
							<label class="control-label">Service code</label> <input
								name="modalInput" type="text" class="form-control"
								maxlength="20" name="serviceCode" ng-model="serviceData.assetCode"
								placeholder="Enter Asset or Service code" required>
						</div>
						
						<div class="col-xs-4 required">
							<div class="form-group">
								<label class="control-label">Category</label> 
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
								<input type="hidden" ng-model="serviceRepairType.selected" >
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
						<div class="col-md-8 col-sm-9">
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
											
						
						</div>
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
						
						</div>
						
						<div  class="col-xs-4 required">
							<div class="col-md-12">
								
							<label class="control-label">Site</label> <!-- <select
							   ng-options="val as val.siteName for val in accessSite.list"
								class="form-control" ng-model="accessSite.selected" required>
							</select> -->
							<label class="form-control" ng-show="operation == 'EDIT'">{{selectedAsset.siteName}}</label>
							
							<select class="form-control" ng-show="operation == 'ADD'" id="serviceSiteSelect" name="serviceSiteSelect" multiple="multiple" required
							onchange="validateDropdownValues('serviceSiteSelect','S')">
							</select>
							<input type="hidden" ng-model="accessSite.selected">
						
					         </div>
						</div>
					</div>
					<div class="row">
						
						 <div class="col-xs-8">
							<label for="exampleInputEmail1">Comments</label> 
							<!-- <input type="text" maxlength="50" class="form-control"
								placeholder="Enter comment" name="comment" ng-model="serviceData.assetDescription"> -->
								
								<textarea class="form-control" maxlength="1000" style="width: 100%;
   				 height: 70px;" rows="3" placeholder="Enter comment" name="comment" ng-model="serviceData.assetDescription"></textarea>
						</div> 
						
						<div class="col-xs-4" ng-if="serviceData.assetId!=null">
							<label for="exampleInputEmail1">Additional
								document (Max Size 100KB)</label> <input type="file" class="form-control" 
								id="inputServiceDocfilepath" accept=".doc, .docx,.pdf"
								name="inputServiceDocfilepath" ng-model="serviceData.documentPath" 
								onchange="angular.element(this).scope().getDocumentFile(this, event, 'serviceModalMessageDiv','fileerrorservice' )">								
												
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

						</div> --%>
					
				</div>
				</section>
			</div>
		</div>
	