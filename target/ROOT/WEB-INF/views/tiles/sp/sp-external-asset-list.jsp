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
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/optionbtn.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/incident-modal.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/moment.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/ext-asset-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
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
         format:"dd-mm-yyyy",
         autoclose: true
     })
	$('siteSelect').multiselect();
	 $('serviceSiteSelect').multiselect(); 	 
	
	 
  		
});
  
</script>
</head>
<div class="content-wrapper">
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
		<div ng-controller="assetController" id="assetWindow">
		<div style="display:none" id="loadingDiv"><div class="loader">Loading...</div></div>
			<section class="content">
				<div class="row">
				<div class="col-md-12">
					<div class="box" >
						<div class="box-header with-border">
							<div class="row">
								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio4"
												ng-model="pageViewFor" value="INCIDENTS"
												ng-change="displayExternalCustomerView(pageViewFor)" /> <label
												for="radio4"> Incidents </label>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio5"
												ng-model="pageViewFor" value="SITES"
												ng-change="displayExternalCustomerView(pageViewFor)" /> <label
												for="radio5">Sites</label>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio6"
												ng-model="pageViewFor" value="ASSETS"
												ng-change="displayExternalCustomerView(pageViewFor)" /> <label
												for="radio6">Assets</label>
										</div>
									</div>
								</div>


								<div class="col-md-3">
									<div class="funkyradio">
										<div class="funkyradio-primary">
											<input type="radio" name="radioHeader" id="radio7"
												ng-model="pageViewFor" value="CUSTOMERS"
												ng-change="displayExternalCustomerView(pageViewFor)" /> <label
												for="radio7">Customers</label>
										</div>
									</div>
								</div>

							</div>
						</div>
					<div class="row">
					<div class="col-md-6">
						<div class="box" >
							<div class="box-header with-border">
							<div class="row col-md-4">
							<select name="extCustSelect" id="extCustSelect" class="form-control  pull-left" 
							style="margin-right: 5px;" 
							ng-model="extCustomer.selected"  required
							onchange="angular.element(this).scope().getCustomerSelected(this, event, 'extCustSelect')">
							</select>
							</div>
								<!-- <h3 class="box-title">List of Assets and Services</h3> -->
								<div class="box-tools pull-right" style="margin-top: 0px;">
									<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
										<a href class="btn btn-success dropdown-toggle pull-right" data-toggle="dropdown"><span class="fa fa-plus"></span>
										Add Asset or Service <span class="caret"></span>
									  </a>
								  	<ul class="dropdown-menu" role="menu">
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
													<tbody>
														<tr>
															<th>Name</th>
															<th>Category</th>
															<th>Site</th>
														</tr>
														<tr ng-repeat="asset in asset.list | filter: searchAsset | orderBy :'assetName'"
															ng-class="{currentSelected:asset.assetId == selectedRow}" 
															ng-click="rowHighilited(asset, $index)" >
															<td >
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
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
										<a  class="btn btn-danger">Total Assets :  <span class="badge">{{asset.list.length}}</span></a>
										</div>
									</div>
									<!-- /.col -->
									<!-- /.col -->
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
									<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
									<div class="btn-group pull-right">
										<button type="button"
										class="btn btn-success dropdown-toggle pull-right"
										style="margin-right: 5px;" data-toggle="dropdown"><span class="fa fa-gear"></span>
										Action <span class="caret"></span>
										</button>
										
										<ul class="dropdown-menu" role="menu">
										<li> <a href ng-click="openAssetPage()"  ng-if="asset.list.length>0">  <span class="fa fa-edit" aria-hidden="true"></span>Edit</a></li>
										<li> <a href ng-click="isDelete(i, s)"  ng-if="asset.list.length>0">  <span class="fa fa-trash" aria-hidden="true"></span>Delete</a></li>
										<li> <a href="${contextPath}/incident/details/create"> <span  class="fa fa-plus" aria-hidden="true"></span>Create Incident</a></li>
										<li> <a href ng-click="openAssetTaskPage('C', selectedAsset)"  ng-if="asset.list.length>0">  <span class="fa fa-tasks" aria-hidden="true"></span>Create Task</a></li>
									</ul>
									</div>
									</sec:authorize>
									</div>
								</div>
							<div class="box-body" style="overflow-y:auto;overflow-x:hidden;height:59%">
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
											<tr><td style="width:40%">Name</td><td align="right">{{selectedAsset.assetName}}</td>
											</tr>
											<tr><td style="width:40%">Service Code</td><td align="right">{{selectedAsset.assetCode}}</td>
											</tr>
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
								
								<div class="row">
								<div class="col-md-12">
									<div class="box-header with-border">
										<h3 class="box-title" style="margin-left: -12px">Asset Task List</h3>
									</div>
								</div>
								<div class="col-md-12">
									<div class="table-responsive">
										<table id="taskDeatils" class="table table-bordered">
											<tbody>
												<tr>
													<th>Task ID</th>
													<th>Task Number</th>
													<th>Task Name</th>
													<th>Status</th>
													<th>Action</th>
												</tr>
												<tr
													ng-repeat="task in selectedAsset.taskList | filter: searchAssetTask | orderBy :'taskName'"
													ng-class="{currentSelected:$index == selectedTaskRow}"
													ng-click="rowTaskHighilited($index)">
													<td>{{task.taskId}}</td>
													<td>{{task.taskNumber}}</td>
													<td>{{task.taskName}}</td>
													<td>{{task.taskStatus}}</td>
													<td align="center"><a href ng-click="openAssetTaskPage('U', task)"> <i
															class="fa fa-edit" aria-hidden="true"></i></a></td>
												</tr>
											</tbody>
										</table>
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
									</div>
									<div class="col-sm-4 col-xs-6">
										<div class="description-block border-right">
											<a href class="btn btn-success" ng-if="selectedAsset.delFlag == 0 ">Asset Status<span class="badge">Active</span></a>
											<a href class="btn btn-danger" ng-if="selectedAsset.delFlag == 1 ">Asset Status <span class="badge">Deactive</span></a>
										</div>
								</div>
							</div>
						</div>
						</div>
					</div>
					
		 <div class="modal fade" id="confirmDelete" data-dismiss="modal" data-keyboard="false" data-backdrop="static" data-toggle="modal" role="dialog" aria-labelledby="confirmDeleteLabel" aria-hidden="true"> 
  <div class="modal-dialog"> 
    <div class="modal-content"> 
      <div class="modal-header"> 
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="confirmDelbtn" >&times;</button>  
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
		<div class="modal right fade" id="taskModal" tabindex="-1" role="dialog" aria-labelledby="taskModal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title" id="myModalLabel2" ng-if="selectedAsset.taskId==null">Create Task</h4>
					<h4 class="modal-title" id="myModalLabel3" ng-if="selectedAsset.taskId!=null">Update Task</h4>
				</div>
				<div class="modal-body">
				       <div class="box box-solid">
            <div class="box-header with-border">
              <h3 class="box-title">Asset : {{selectedAsset.assetName}}</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
            <form name="createUpdatetaskform" ng-submit="saveAssetTask()">
              <dl>
                <dt>Task Name <i style="color:red">*</i> </dt>
                <dd>
					 <input
						name="taskname" type="text" class="form-control"
						maxlength="50" name="taskName" 
						ng-disabled="selectedAsset.taskSelectedStatus =='Closed' || selectedAsset.taskSelectedStatus =='Rejected'"
						ng-model="selectedAsset.taskName"
						placeholder="Enter Task Name" required>
				</dd>
              </dl>
                <dl>
                <dt>Task Description</dt>
                <dd>
					<textarea class="form-control"
					 rows="3"
					ng-disabled="selectedAsset.taskSelectedStatus =='Closed' || selectedAsset.taskSelectedStatus =='Rejected'"
					placeholder="Enter Task Description" name="taskDescription"
					ng-model="selectedAsset.taskDesc"></textarea>
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
					ng-disabled="selectedAsset.taskSelectedStatus =='Closed' || selectedAsset.taskSelectedStatus =='Rejected'"
					ng-model="selectedAsset.planStartDate" required>

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
						ng-disabled="selectedAsset.taskSelectedStatus =='Closed' || selectedAsset.taskSelectedStatus =='Rejected'"
						ng-model="selectedAsset.planEndDate" required>

						
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
					ng-model="selectedAsset.taskAssignedTo"
					ng-disabled="selectedAsset.taskSelectedStatus =='Closed' || selectedAsset.taskSelectedStatus =='Rejected'"
					placeholder="Enter Assigned To" required>
				</dd>
                </dl>
                 <label class="control-label">Status <i style="color:red">*</i></label>
                <div >
					<select ng-if="taskOperation =='CreateTask'"
					 name="taskStatus" ng-model="selectedAsset.taskStatus"
					id="taskStatus" class="form-control" required>
					<option value="New" selected="selected">New</option>
					</select>
					<select  ng-if="taskOperation =='UpdateTask'" 
						name="taskStatusUpdate" id="taskStatusUpdate" class="form-control" 
						ng-disabled="selectedAsset.taskSelectedStatus =='Closed' || selectedAsset.taskSelectedStatus =='Rejected'"
						required ng-model="selectedAsset.taskStatus">
						<option value="New" ng-hide="selectedAsset.taskSelectedStatus =='In Progress'">New</option>
						<option value="In Progress">In Progress</option>
						<option value="Closed">Closed</option>
						<option value="Rejected">Rejected</option>
					</select>
				
                </div>
                <div ng-hide="selectedAsset.taskStatus =='New' || selectedAsset.taskStatus =='In Progress'">
					<label class="control-label">Resolution Comment</label>
					<textarea class="form-control" rows="3"
						placeholder="Enter Resolution Comment"
						ng-disabled="selectedAsset.taskSelectedStatus =='Closed' || selectedAsset.taskSelectedStatus =='Rejected'"
						name="resolutionComment"
						ng-model="selectedAsset.resComments"></textarea>
				
                </div>
                 <div ng-if="selectedAsset.taskId==null" style="margin-top: 15px; margin-right: 1px;"
                 ng-hide="selectedAsset.taskSelectedStatus =='Closed' || selectedAsset.taskSelectedStatus =='Rejected'">
                 <button type="submit" class="btn btn-success">Save	changes</button>
					<button type="reset" id="resetServiceAssetForm"
						class="btn btn-success">RESET</button>
                 </div>
                 <div ng-if="selectedAsset.taskId!=null" style="margin-top: 15px; margin-right: 1px;"
                 ng-hide="selectedAsset.taskSelectedStatus =='Closed' || selectedAsset.taskSelectedStatus =='Rejected'">
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
				</div>
				</div>
				</div>
				</section>
			</div>
		</div>
	