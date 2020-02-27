<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>

<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-controller.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-service.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js"></c:url>'></script>
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
.main-box {
    background: #FFFFFF;
    -webkit-box-shadow: 1px 1px 2px 0 #CCCCCC;
    -moz-box-shadow: 1px 1px 2px 0 #CCCCCC;
    -o-box-shadow: 1px 1px 2px 0 #CCCCCC;
    -ms-box-shadow: 1px 1px 2px 0 #CCCCCC;
    box-shadow: 1px 1px 2px 0 #CCCCCC;
    margin-bottom: 16px;
    -webikt-border-radius: 3px;
    -moz-border-radius: 3px;
    border-radius: 3px;
}
.table a.table-link.danger {
    color: #e74c3c;
}
.label {
    border-radius: 3px;
    font-size: 0.875em;
    font-weight: 600;
}
.user-list tbody td .user-subhead {
    font-size: 0.875em;
    font-style: italic;
}
.user-list tbody td .user-link {
    display: block;
    font-size: 1.25em;
    padding-top: 3px;
    margin-left: 60px;
}
a {
    color: #3498db;
    outline: none!important;
}
.user-list tbody td>img {
    position: relative;
    max-width: 50px;
    float: left;
    margin-right: 15px;
}

.table thead tr th {
    text-transform: uppercase;
    font-size: 0.875em;
}
.table thead tr th {
    border-bottom: 2px solid #e7ebee;
}
.table tbody tr td:first-child {
    font-size: 1.125em;
    font-weight: 300;
}
.table tbody tr td {
    font-size: 0.875em;
    vertical-align: middle;
    border-top: 1px solid #e7ebee;
    padding: 12px 8px;
}
#errorMessageDiv, #successMessageDiv{
    top: 0%;
    left: 50%;
   /*  width: 45em; */
    height: 3em;
    margin-top: 4em;
    margin-left: -15em;
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
    margin-left: -12em;
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
  /*  #floating-panel {
        position: absolute;
        top: 10px;
        left: 25%;
        z-index: 5;
        background-color: #fff;
        padding: 5px;
        border: 1px solid #999;
        text-align: center;
        font-family: 'Roboto','sans-serif';
        line-height: 30px;
        padding-left: 10px;
      } */
  
</style>
<script>
$(function() {
	 $('.toggle-on').removeAttr('style');
	 $('.toggle-off').removeAttr('style');
	 
	  $('.dt1').datepicker({
		     format:'dd/mm/yyyy',
		    });
		    
		    $('.drpedit').editableSelect();
		   
   $('body').on('focus',".dt1", function(){
       $(this).datepicker();
   });
		   
		   /* function setDateControl()
		   {
		    alert("shibu");
		    $('.datepicker1from').datepicker({
		     
		     format:'dd/mm/yyyy',
		    });
		   } */
		   
		   $("input").keypress(function (event) {
		       var className = $(this).attr("class");
		       if(className = "dt1")
		        $('.dt1').datepicker({
		         format:'dd/mm/yyyy',
		        }); 
		   });
  })
  
  
  
</script>
</head>
<div id="page-content-wrapper">
	<div class="page-content">
		<div class="container-fluid" ng-controller="siteController">
			<section class="content" style="min-height:35px;display:none" id="messageWindow">
				<div class="row">
					<div class="col-md-12">
						<div class="alert alert-success alert-dismissable" id="successMessageDiv"
							style="display: none;  height: 34px;white-space: nowrap;">
							<!-- <button type="button" class="close" >x</button> -->
							<strong>Success! </strong> {{successMessage}} <span class="messageClose" >X</span>
						</div>
						<div class="alert alert-danger alert-dismissable" id="errorMessageDiv"
							style="display: none;  height: 34px;white-space: nowrap;">
							<!-- <button type="button" class="close">x</button> -->
							<strong>Error! </strong> {{errorMessage}} <span class="messageClose">X</span>
						</div>
					</div>
				</div>
			</section	
			<section class="content">
				<div class="row">
				<div class="col-md-6">
						<div class="box" >
							<div class="box-header with-border">
								<h3 class="box-title">List of Sites</h3>
								<div class="box-tools pull-right">
									<sec:authorize access="hasAnyRole('ROLE_SALES_MANAGER', 'ROLE_OPS_MANAGER')">
									 	<button type="button" class="btn btn-box-tool"> 
											<a href  class="btn btn-info" 
											ng-click="addNewSite()" ><i class="fa fa-user" arial-hidden="true"></i> Add Site</a>
										</button>
									</sec:authorize>	
									<button type="button" class="btn btn-box-tool" >
										<a href class="btn btn-info"><i class="fa fa-download" arial-hidden="true"></i> Download </a>
									</button>
									<!--<div class="btn-group">
										<button type="button" class="btn btn-box-tool dropdown-toggle"
											data-toggle="dropdown">
											<i class="fa fa-wrench"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
										</ul>
									</div>
									 -->
								</div>
							</div>
							<div class="box-body" style="height:440px">
								<div class="row">
	 								<div class="col-md-12">
										<input type="text" class="form-control"
											placeholder="Search Site" ng-model="searchSite">
									 </div>
										<div class="col-md-12">
											<div style="overflow: auto; height: 383px">
											<table id="example1" 
												class="table table-bordered">
												<thead>
													<tr>
														<th width="40%">Name</th>
														<th>Role</th>
														<th>Created On</th>
													</tr>
												</thead>
											<%-- 	<tbody>
													<tr ng-repeat="site in siteList | filter: searchSite"
													ng-class="{currentSelected:$index == selectedRow}">
														<td>
											<a href ng-click="getUserDetail(user);rowHighilited($index)">
																{{user.firstName}} {{user.lastName}}</a></td>	
														<td><i class="fa fa-mobile" aria-hidden="true"></i>
															{{user.role}}</td>
														<td><i class="fa fa-calendar" aria-hidden="true"></i>
															{{user.createdAt}}</td>	
													</tr>
												</tbody> --%>
											</table>
											</div>
										</div>
									</div>
								
		
                            
                            </div>
                            
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
										<a  class="btn btn-warning">Total Sites :  <span class="badge">{{appUsers.length}}</span></a>
										</div>
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
										<a  class="btn btn-success">Download <i class="fa fa-download" arial-hidden="true"></i></a>
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
							
							<div class="modal fade" id="createSiteModal" >
	<div class="modal-dialog" style="width:82%;">
      <div class="modal-content">
       <form  name="createsiteform" ng-submit="saveSiteForm(createsiteform)">
        <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title">Create New Site   |  <a class="btn btn-warning">Company <span class="badge">{{sessionUser.company.companyName}}</span></a></h4>
			<div class="alert alert-danger alert-dismissable" id="modalMessageDiv"
				style="display: none;  height: 34px;white-space: nowrap;">
				<strong>Error! </strong> {{modalErrorMessage}} <span class="messageClose">X</span>
			</div>

		</div>
		 <div class="modal-body" style="background-color: #eee">
            
            <div class="nav-tabs-custom">
            <ul class="nav nav-tabs" style="background-color: rgba(60, 141, 188, 0.34);">
            
							<li class="active">
				       		 <a  href="#siteDetailsTab" data-toggle="tab" aria-expanded="true"><b>Site Details</b></a>
							</li>
							<li><a href="#siteContactsTab" data-toggle="tab" aria-expanded="true"><b>Site Contacts</b></a>
							</li>
							<li><a href="#licenseTab" data-toggle="tab" aria-expanded="true"><b>License Details</b></a>
							</li>
				  			<li><a href="#operationTab" data-toggle="tab" aria-expanded="true"><b>Site Operation info</b></a>
							</li>
							<li><a href="#submeterTab" data-toggle="tab" aria-expanded="true"><b>Submeter Details</b></a>
							</li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="siteDetailsTab">
				<div class="row">
					<div class="box-body">
							<div class="col-md-3">
								<div class="col-md-12">
								 <label for="country">Country</label>       
								<a class="btn btn-warning" style="width:100%"><span class="badge">{{siteData.country.countryName}}</span></a>
								</div>
							</div>
						
						 <div class="col-md-9">
							<div class="row">
							<div class="col-md-8">
							 <label for="district">Site Name</label>
								  <input type="text" maxlength="50" class="form-control" placeholder="Enter Site Name" 
                 					name="siteName" ng-model="siteData.name" required>	
							 </div>
							<div class="col-md-4">
							 <label for="district">Site Owner</label>
								  <input type="text" maxlength="50" class="form-control" placeholder="Enter Site Owner" 
                  				name="owner" ng-model="siteData.owner">	
							</div>
							</div>
								<div class="row">
							 <div class="col-xs-4">
				                <label for="district">District</label>
				                  <select class="form-control" ng-model="siteData.selectedDistrict"
				                  ng-options="val as val.districtName for val in siteData.districtList" required>
				                    <option value="">Select District</option>
				                  </select>
				                </div>
				                <div class="col-xs-4">
				                <label for="area">Area</label>
				                  <select class="form-control" ng-model="siteData.selectedArea"
				                     ng-options="val as val.areaName for val in siteData.areaList">
				                    <option value="">Select Area</option>
				                  </select>
				                </div>
				                <div class="col-xs-4">
				                <label for="cluster">Cluster</label>
				                  <select class="form-control" ng-model="siteData.selectedCluster"
				                     ng-options="val as val.clusterName for val in siteData.clusterList">
				                    <option value="">Select Cluser</option>
				                  </select>
	               			 </div>
								</div>
								
								<div class="row">
								<div class="col-xs-4">
					               <label for="electricityId">Electricity ID</label>
									  <input type="text" maxlength="50" class="form-control" 
									  placeholder="Enter Electricity ID Number" name="electricityId" ng-model="siteData.electricityId">
					                </div>
									<div class="col-xs-4">
					                <label for="siteNumber1">Site Number1</label>
					                  <input type="text" maxlength="50" class="form-control" placeholder="Enter Site Number1" 
					                  name="sitenumber1" ng-model="siteData.siteNumber1">
					                </div>
					                <div class="col-xs-4">
					                <label for="siteNumber2">Site Number2</label>
					                  <input type="text" maxlength="50" class="form-control" placeholder="Enter Site Number2" 
					                  name="sitenumber2" ng-model="siteData.siteNumber2">
					               </div>
								</div>
								<div class="row">
									   <div class="col-xs-6">
					                <label for="fileInput">File Input</label>
					                  <input type="file" class="form-control" name="inputfilepath" ng-model="siteData.fileObject">
					                </div>
								</div>
								
						</div>
						</div>
						</div>
						</div>
              
              <div class="tab-pane" id="siteContactsTab">
				  <div class="row">
				   <div class="col-md-6">
					<div class="box-body">
						 <div class="row">
						  <div class="col-md-12">
                <div class="col-xs-6">
                <label for="contactName">Contact Name</label>
                  <input type="text" maxlength="50" class="form-control" 
                  placeholder="Enter site contact name" name="contactname" ng-model="siteData.contactName" required>
                </div>
                <div class="col-xs-6">
                <div class="form-group"  >
                <label for="emailAddress">Email Address</label>
                  <input type="email" class="form-control" 
                  placeholder="Enter email" name="email" ng-model="siteData.email" required>
                 </div>
                </div>
              </div>
			  </div>
              
              <div class="row">
                <div class="col-md-12">
                <div class="col-xs-6">
                <label for="longitude">Longitude</label>
                  <input type="text" maxlength="50" class="form-control" 
                  placeholder="Enter longitude" name="longitude" ng-model="siteData.longitude">
                </div>
                <div class="col-xs-6">
                <label for="latitude">Latitude</label>
                  <input type="text" maxlength="50" class="form-control" 
                  placeholder="Enter latitude" name="latitude" ng-model="siteData.latitude">
                </div>
              
                </div>
              </div>
			  
			  <div class="row">
                <div class="col-md-12">
                 <div class="col-xs-6">
                <div class="form-group"  >
                <label for="primaryContactNum">Primary contact No</label>
                  <input type="number" class="form-control" placeholder="Enter Primary contact No" 
                  name="primaryphoneno" ng-model="siteData.primaryContact" required>
                </div>
                </div>
                <div class="col-xs-6">
                <label for="secondaryContactNum">Secondary Contact No</label>
                  <input type="number" class="form-control" placeholder="Enter Secondary Contact No" 
                  name="secondaryphoneno" ng-model="siteData.secondaryContact">
                </div>
                </div>
              </div>
				   </div>
				   </div>
				<div class="col-md-6">
				<div class="box-body">
				<div class="row">
				<div class="col-md-12">
				
				<label for="siteAddress">Site Address</label>                  
				  <textarea class="form-control" style="width: 547px;
    height: 176px;" rows="3" placeholder="Enter site address" name="address" ng-model="siteData.address"></textarea>
				
				</div>
			   </div>
	   		   </div>
	    	</div>
            </div>
		</div>
			
              <div class="tab-pane " id="licenseTab">
				<div class="row">
					<div class="box-body">
					   <div class="row">
					    <div class="col-md-12">
							<div class="form-group">
              <input ng-hide="!licenseDetails.length" type="button" class="btn btn-danger pull-right" style="margin-right: 5px;" ng-click="removeLicense()" value="Remove">&nbsp;&nbsp;
              <input type="button" class="btn btn-primary addnew pull-right" style="margin-right: 5px;" onclick="" ng-click="addNewLicense()" value="Add New">
           </div>
                
              <table id="example2" class="table table-bordered table-hover table-condensed">
                <thead>
                <tr>
                <th><input type="checkbox" ng-model="selectedAll" ng-click="checkAllLicense()" /></th>
                  <th><b>License Name</b></th>
                  <th><b>Valid From</b></th>
                  <th><b>Valid To</b></th>
                  <th><b>Attach File</b></th>                  
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="licenseDetail in licenseDetails">
                	<td>
                     <input type="checkbox" ng-model="licenseDetail.selected"/></td>
                  <td>
                  <input type="text" class="form-control" ng-model="licenseDetail.licenseName" required>
                   </td>
                  <td>
                  <div class="input-group date">
                  <div class="input-group-addon">
                    <i class="fa fa-calendar"></i>
                  </div>
                  <input type="text" class="form-control pull-right dt1" id="datepicker{{$index}}from" 
                  ng-model="licenseDetail.validfrom">
                </div>                  
                   
                  </td>
                  <td>
                  
                  <div class="input-group date">
                  <div class="input-group-addon">
                    <i class="fa fa-calendar"></i>
                  </div>
                  <input type="text" class="form-control pull-right dt1" id="datepicker{{$index}}to" 
                  ng-model="licenseDetail.validto">
                </div>                  
                  
                  </td>
                  <td>
                  <input type="file" id="exampleInputFile"> 
                  </td>
                  
                </tr>
                </tbody>
                </table>
						</div>
						</div>
						</div>
						</div>
						</div>
					
				    <div class="tab-pane " id="operationTab">
				<div class="row">
					<div class="box-body">
					   <div class="row">
					    <div class="col-md-12">
							 <div class="col-md-6" >
           <div class="box-header pull-center" style="background-color:lightblue;height:35px">
              <span class="pull-center">
              <b>SalesOperation Schedule</b>
              </span>
            </div>
              <table id="example2" class="table table-bordered table-hover table-condensed">
                <thead>
                <tr>
                
                  <th><b>OperatingDays</b></th>
                  <th><b>Operating From</b></th>
                  <th><b>Operating To</b></th>               
                  
                </tr>
                </thead>
                <tbody>
                <tr  ng-repeat="salesoperationdetail in salesoperationDetails.operationDays">                	                     
                  <td>
                  
                  <label>{{salesoperationdetail.days}}</label>
                  
                  </td>
                  <td>
                  <select class="form-control " ng-model="salesoperationdetail.from" 
                  ng-options="val as val.name for val in salesoperationDetails.operationTimings" required>  
                  <option value="">Select From Time</option>                             	
                  </select>
                  
                  </td>
                  <td>
                  
                  <select class="form-control drpedit" ng-model="salesoperationdetail.to" 
                  ng-options="val as val.name for val in salesoperationDetails.operationTimings" required>
                   <option value="">Select To Time</option>     
                  </select>
                  
                  
                  </td>                  
                </tr>
                </tbody>
              </table>
              </div>
              
              <div class="col-md-6" >
              <div class="box-header pull-center" style="background-color:lightblue;height:35px">
              <span class="pull-center">
              <b>DeliveryOperation Schedule</b>
              </span>
            </div>
            <div class="box-body no-padding">
              <table id="example2" class="table table-bordered table-hover table-condensed " style="height:50px;">
                <thead>
                <tr>
                
                  <th width="100px">OperatingDays</th>
                  <th>Operating From</th>
                  <th>Operating To</th>               
                  
                </tr>
                </thead>
                <tbody>
                <tr style="height:30px" ng-repeat="deliveryoperationdetail in deliveryoperationDetails">                	                     
                  <td><hr style="padding:0px; margin:0px;"><label>{{deliveryoperationdetail.days}}</label></td>
                  <td>
                  
                   <select class="form-control drpedit" ng-model="deliveryoperationdetail.from" 
                   ng-options="operatingTime as operatingTime.Name for operatingTime in operatingTimes" required>
                   <option value="">Select From Time</option>     
                  </select>
                  
                  
                  </td>
                  <td>
                   <select class="form-control drpedit" ng-model="deliveryoperationdetail.to" 
                   ng-options="operatingTime as operatingTime.Name for operatingTime in operatingTimes" required>
                   <option value="">Select From Time</option>     
                  </select>
                  
                  </td>                  
                </tr>
                </tbody>
              </table>
              </div>
              </div>
						</div>
						</div>
						</div>
						</div>
						</div>	
			
				    <div class="tab-pane " id="submeterTab">
				<div class="row">
					<div class="box-body">
					   <div class="row">
					    <div class="col-md-12">
							
           <div class="form-group">
              <input ng-hide="!submeterDetails.length" type="button" class="btn btn-danger pull-right"  style="margin-right: 5px;" ng-click="removeSubmeter()" value="Remove">
              <input type="button" class="btn btn-primary addnew pull-right" style="margin-right: 5px;" ng-click="addNewSubmeter()" value="Add New">
           </div>
              <table id="example2" class="table table-bordered table-hover table-condensed ">
                <thead>
                <tr>
                <th><input type="checkbox" ng-model="selectedAll" ng-click="checkAllSubmeter()" /></th>
                  <th>Submeter Number</th>
                  <th>User</th>                                    
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="submeterDetail in submeterDetails">
                	<td>
                        <input type="checkbox" ng-model="submeterDetail.selected"/></td>                     
                  
                  <td>                  
                  <input type="text" maxlength="50" class="form-control" ng-model="submeterDetail.subMeterNumber">
                  </td>
                  <td>                  
                  <input type="text" maxlength="50" class="form-control" ng-model="submeterDetail.subMeterUser">                
                  </td>
                                    
                </tr>
                </tbody>
                </table>
						</div>
						</div>
						</div>
						</div>
						</div>
							
		
		</div>
		</div>
		</div>
       
				<div class="modal-footer">
					<button type="button" class="btn btn-default pull-left"	id="siteModalCloseBtn" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary" ng-click="saveSiteForm(createsiteform)">Save changes</button>
					<button type="reset" id="resetAddUserForm" class="btn btn-primary">RESET</button>
				</div>
				</form>
			</div>
			</div>
           </div>
							
						</div>
						
								<div class="col-md-6">
						<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Site Detail</h3>
								<div class="box-tools pull-right">
									<button type="button" class="btn btn-box-tool"
										data-widget="collapse">
										<i class="fa fa-minus"></i>
									</button>
									<div class="btn-group">
										<button type="button" class="btn btn-box-tool dropdown-toggle"
											data-toggle="dropdown">
											<i class="fa fa-wrench"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li><a href="#">Action</a></li>
											<li><a href="#">Another action</a></li>
											<li><a href="#">Something else here</a></li>
											<li class="divider"></li>
											<li><a href="#">Separated link</a></li>
										</ul>
									</div>
									<button type="button" class="btn btn-box-tool"
										data-widget="remove">
										<i class="fa fa-times"></i>
									</button>
								</div>
							</div>
							<!-- /.box-header -->
							<div class="box-body">
							<div class="row">
									<div class="col-md-3">
										<div  style="background-color: #ccc;">
											<img src="${contextPath}/resources/img/swadhin.jpg" 
											style="width:50%;margin-left:27px;border-radius: 50%;">
										</div>
									</div>
									   <div class="col-md-9">
											 <a href="#" >
												<b>
													<i class="fa fa-user" aria-hidden="true"></i> {{site.name}}
												</b>
												<br>
												 <i class="fa fa-envelope" aria-hidden="true"></i> {{site.location}}
											</a>
										</div>
								</div>
								<div class="row">
								     <div class="col-md-3">
									</div>
									<div class="col-md-9">
										<i class="fa fa-user" aria-hidden="true"></i> Status: Active
									 	<i class="fa fa-envelope" aria-hidden="true"></i> 
									</div>
								</div>
								</div>
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
										<!-- /.description-block -->
									</div>
								</div>
							</div>
						</div>
						
						<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Features Access</h3>
								<div class="box-tools pull-right">
									<button type="button" class="btn btn-box-tool"
										data-widget="collapse">
										<i class="fa fa-minus"></i>
									</button>
									<div class="btn-group">
										<button type="button" class="btn btn-box-tool dropdown-toggle"
											data-toggle="dropdown">
											<i class="fa fa-wrench"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li><a href="#">Action</a></li>
											<li><a href="#">Another action</a></li>
											<li><a href="#">Something else here</a></li>
											<li class="divider"></li>
											<li><a href="#">Separated link</a></li>
										</ul>
									</div>
									<button type="button" class="btn btn-box-tool"
										data-widget="remove">
										<i class="fa fa-times"></i>
									</button>
								</div>
							</div>
							<!-- /.box-header -->
							<div class="box-body">
								<div class="row">
									<div class="col-md-3">
										<div  style="background-color: #ccc;">
											<img src="${contextPath}/resources/img/swadhin.jpg" 
											style="width:50%;margin-left:27px;border-radius: 50%;">
										</div>
									</div>
									   <div class="col-md-9">
											 <a href="#" >
												<b>
													<i class="fa fa-user" aria-hidden="true"></i> 
												</b>
												<br>
												 <i class="fa fa-envelope" aria-hidden="true"></i> 
											</a>
										</div>
								</div>
								<div class="row">
								     <div class="col-md-3">
									</div>
									<div class="col-md-9">
										<i class="fa fa-user" aria-hidden="true"></i> Status: Active
									 	<i class="fa fa-envelope" aria-hidden="true"></i> Role: 
									</div>
								</div>
							</div>
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
										<!-- /.description-block -->
									</div>
								</div>
							</div>
						</div>
						
							<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Sites Assigned</h3>
								<div class="box-tools pull-right">
									<button type="button" class="btn btn-box-tool"
										data-widget="collapse">
										<i class="fa fa-minus"></i>
									</button>
									<div class="btn-group">
										<button type="button" class="btn btn-box-tool dropdown-toggle"
											data-toggle="dropdown">
											<i class="fa fa-wrench"></i>
										</button>
										<ul class="dropdown-menu" role="menu">
											<li><a href="#">Action</a></li>
											<li><a href="#">Another action</a></li>
											<li><a href="#">Something else here</a></li>
											<li class="divider"></li>
											<li><a href="#">Separated link</a></li>
										</ul>
									</div>
									<button type="button" class="btn btn-box-tool"
										data-widget="remove">
										<i class="fa fa-times"></i>
									</button>
								</div>
							</div>
							<div class="box-body">
								<div class="row">
									<div class="col-md-3">
										<div  style="background-color: #ccc;">
											<img src="${contextPath}/resources/img/swadhin.jpg" 
											style="width:50%;margin-left:27px;border-radius: 50%;">
										</div>
									</div>
									   <div class="col-md-9">
											 <a href="#" >
												<b>
													<i class="fa fa-user" aria-hidden="true"></i> 
												</b>
												<br>
												 <i class="fa fa-envelope" aria-hidden="true"></i> 
											</a>
										</div>
								</div>
								<div class="row">
								     <div class="col-md-3">
									</div>
									<div class="col-md-9">
										<i class="fa fa-user" aria-hidden="true"></i> Status: Active
									 	<i class="fa fa-envelope" aria-hidden="true"></i> Role: 
									</div>
								</div>
							</div>
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
										<!-- /.description-block -->
									</div>
									<!-- /.col -->
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
										<!-- /.description-block -->
									</div>
								</div>
							</div>
						</div>
						</div>
						
				</div>
				</section>
				
		</div>
	</div>
</div>
