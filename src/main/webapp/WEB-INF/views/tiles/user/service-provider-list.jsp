<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap-toggle.min.js"></c:url>'></script>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-provider-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-provider-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
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

#errorMessageDiv, #successMessageDiv, #infoMessageDiv{
    top: 0%;
    left: 58%;
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

.col-sm-3.required .control-label:after {
  content:"*";
  color:red;
}

.col-sm-6.required .control-label:after {
  content:"*";
  color:red;
}
.reqDiv.required .control-label:after {
  content:"*";
  color:red;
}

  
</style>
<script>
$(function() {
	 $('.toggle-on').removeAttr('style');
	 $('.toggle-off').removeAttr('style');
	 
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
		<div  ng-controller="serviceProviderController" id="serviceProviderWindow">
		<div style="display:none" id="loadingDiv"><div class="loader">Loading...</div></div>
			<section class="content" style="min-height:17px;display:none" id="messageWindow">
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
							<strong>Error! </strong> {{errorMessage}} 
						</div> -->
					</div>
				</div>
			</section>	
			<section class="content">
				<div class="row">
				<div class="col-md-6">
				
						<div class="box" >
							<div class="box-header with-border">
								<h3 class="box-title">List of Service Provider</h3>
								<div class="box-tools pull-right">
									<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
											<a href  class="btn btn-success" ng-click="addNewServiceProvider()" >
											<i class="fa fa-plus" arial-hidden="true"></i> Add Service Provider</a>
									</sec:authorize>	
									<%-- <button type="button" class="btn btn-box-tool" >
										<a href class="btn btn-info"><i class="fa fa-download" arial-hidden="true"></i> Download </a>
									</button> --%>
								</div>
							</div>
							<div class="box-body" style="height:70%" >
								<div class="row">
	 								<div class="col-md-12">
										<input type="text" class="form-control"		placeholder="Search Service Provider" ng-model="searchSp">
									 </div>
										<div class="col-md-12">
											<div class="row">
									 <div class="col-md-12">
									 <div style="overflow: auto; height: 350px">
									   <ul class="products-list product-list-in-box"  style="overflow:auto;height:340px">
							                <li class="item" ng-repeat="val in allServiceProviders | filter:searchSp | orderBy: 'sp.name'" >
							                  <div class="product-img" style="margin-top: -12px;">
							                    <img src="${contextPath}/resources/theme1/img/sp-icon.jpg" alt="Product Image">
							                        
							                  </div>
							                  <div class="product-info">
							                    <a href ng-click="getSelectedServiceProvider(val)" class="product-title">{{val.sp.name}}
							                      </a>
							                    	<span class="product-description">
							                          {{val.sp.email}}
							                        </span>
							                  </div>
							                   <i class="fa fa-check-circle-o fa-2x pull-right" style="    margin-top: -39px;" aria-hidden="true" ng-if="val.sp.serviceProviderId == selectedServiceProvider.serviceProviderId"></i>
							                </li>
							             
							              </ul>
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
										<a  class="btn btn-danger">Total Service Provider :  <span class="badge">{{allServiceProviders.length}}</span></a>
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
								<h3 class="box-title">Service Provider Detail</h3>
								<div class="box-tools pull-right">
								<%-- 	<button type="button" class="btn btn-box-tool"
										data-widget="collapse">
										<i class="fa fa-minus"></i>
									</button> --%>
									<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
									<div class="btn-group">
											<div class="btn-group pull-right">
											<a href ng-click="editServiceProvider()" ng-if="allServiceProviders.length>0"
										class="btn btn-success"	style="margin-right: 5px;" data-toggle="modal"><span class="fa fa-edit">
										Edit </span>
									</a>
									</div>
									</div>
									</sec:authorize>
									<%-- <button type="button" class="btn btn-box-tool"
										data-widget="remove">
										<i class="fa fa-times"></i>
									</button> --%>
								</div>
							</div>
							<div class="box-body" style="overflow-y:auto;overflow-x:hidden;height:72%">
								<div class="row">
									
									 <div class="col-md-12">
											 <div class="table-responsive" >
										<table class="table no-margin">
											<!-- <thead>	 -->										
											<tr><td style="width:40%"><b>Company Name</b></td><td style="text-align: right">{{selectedServiceProvider.name}}</td>
											</tr>
											<tr><td style="width:40%"><b>Company Code</b></td><td style="text-align: right">{{selectedServiceProvider.code}}</td>
											</tr>
											<tr><td style="width:40%"><b>Company Email</b></td><td style="text-align: right">{{selectedServiceProvider.email}}</td>
											</tr>
											<tr><td style="width:40%"><b>Help Desk Email</b></td><td style="text-align: right">{{selectedServiceProvider.helpDeskEmail}}</td>
											</tr>
											<tr><td style="width:40%"><b>Help Desk number</b></td><td style="text-align: right">{{selectedServiceProvider.helpDeskNumber}}</td>
											</tr>
											<!-- </thead> -->
											<tbody>											
											<tr><td><b>Country</b></td><td style="text-align: right">{{selectedServiceProvider.country.countryName}}</td></tr>
											<tr><td><b>Additional Details</b></td><td style="text-align: right">{{selectedServiceProvider.additionalDetails}}</td></tr>											
											</tbody>
										</table>
										</div>
										</div>
								</div>
								<div class="row">
									   <div class="col-md-12">
									   <div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">SLA Details</h3>
								</div>
						   <div class="table-responsive">
                <table class="table no-margin">
                  <thead>
                  <tr>
                    <th>Priority</th>
                    <th style="text-align: right">Duration</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr ng-repeat="val in selectedServiceProvider.slaListVOList">
                    <td><a href>{{val.ticketPriority.description}}</a></td>
                    <td style="text-align: right">{{val.duration}} <span ng-if="val.unit=='days'">Calendar</span> {{val.unit}}</td>
                  </tr>

                  </tbody>
                </table>
              </div>
			  </div>
			  </div>
			  </div>
			  <div class="row">
									   <div class="col-md-12">
									   <div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Further SLA details and comments</h3>
								</div>
						 <div class="table-responsive">
                <table class="table no-margin">
                  <thead>
                  <tr>
                    
                  </tr>
                  </thead>
                  <tbody>
                  <tr >                    
                    <td>{{selectedServiceProvider.slaDescription}}</td>
                  </tr>

                  </tbody>
                </table>
              </div>
			  </div>
								</div>
								</div>
              
              				<div class="row">
									   <div class="col-md-12">
									   <div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Escalation Details</h3>
								</div>
						 <div class="table-responsive">
                <table class="table no-margin">
                  <thead>
                  <tr>
                    <th>Person</th>
                    <th style="text-align: right">Email</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr ng-repeat="val in selectedServiceProvider.escalationLevelList">
                    <td><a href>{{val.escalationPerson}}</a></td>
                    <td style="text-align: right">{{val.escalationEmail}}</td>
                  </tr>

                  </tbody>
                </table>
              </div>
			  </div>
								</div>
								</div>					
					
								</div>
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-6">
										<div class="description-block border-right">
											<a  class="btn btn-danger pull-left">SLA Priorities <span class="badge">{{selectedServiceProvider.slaListVOList.length}}</span></a>
										</div>
									</div>
									
									<div class="col-sm-6">
										<div class="description-block border-right">
											<a  class="btn btn-danger pull-right">Escalation Levels <span class="badge">{{selectedServiceProvider.escalationLevelList.length}}</span></a>
										</div>
									</div>
									<!-- <div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
											
										</div>
									</div>
									<div class="col-sm-3 col-xs-6">
										<div class="description-block">
											
										</div>
									</div> -->
								</div>
							</div>
						</div>
						
						
										</div>
								
								
							
							
							</div>
						
						
						
								<div class="modal fade" id="createServiceProviderModal" data-keyboard="false" data-backdrop="static" >
	<div class="modal-dialog" style="width:82%;">
      <div class="modal-content">
       <form  name="createServiceProviderform" ng-submit="saveServiceProviderForm(createServiceProviderform)">
        <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<h4 class="modal-title" id="serviceModaltitle">Create New Service Provider</h4>
			<div class="alert alert-danger alert-dismissable" id="modalMessageDiv"
				style="display: none;  height: 34px;white-space: nowrap;">
				<strong>Error! </strong> {{modalErrorMessage}} 
				<a href><span class="messageClose" ng-click="closeMessageWindow()">X</span></a>
			</div>
		</div>
                            
            <div class="modal-body" style="background-color: #eee">
            
            <div class="nav-tabs-custom">
            <ul class="nav nav-tabs" style="background-color: rgba(60, 141, 188, 0.34);">
              <li class="active"><a href="#spDetailsTab" data-toggle="tab" aria-expanded="true">Service Provider Details</a></li>
              <li class=""><a href="#slaDetailsTab" data-toggle="tab" aria-expanded="false">Service Level Agreement Details</a></li>
              <li class=""><a href="#escalationTab" data-toggle="tab" aria-expanded="false">Escalation Level Details</a></li>
              <li class="pull-right"><a href="#" class="text-muted"><i class="fa fa-gear"></i></a></li>
            </ul>
            <div class="tab-content">
              <div class="tab-pane active" id="spDetailsTab">
				<div class="row">
					<div class="box-body">
					<div class="col-md-6 col-sm-6 required">
						<div class="form-group">
							<label for="company_name">Company Name <span class="control-label"></span></label> 
							<input  name="company_name" ng-model="serviceProvider.name" placeholder="Company Name" 
							class="form-control"  type="text" required maxlength="50">
						</div>
					</div>
                	<div class="col-md-3 col-sm-3">
						<div class="form-group">
						 <label for="company_code">Company Code</label>
						 <input  name="company_code"  placeholder="Company Code" 
						 ng-model="serviceProvider.code" class="form-control"  
						 type="text" maxlength="50">
					   </div>
					</div>
					<div class="col-md-3 col-sm-3 required">
						<div class="form-group">
						 <label for="helpdeskemail">Help Desk Email <span class="control-label"></span></label>
						 <input  name="helpdeskemail"  placeholder="Help desk email" 
						 ng-model="serviceProvider.helpDeskEmail" class="form-control"  
						 type="email" required>
					   </div>
					</div>
					
					<div class="col-md-3 col-sm-3 required">
					<div class="form-group">
						<label for="company_email">Company Email <span class="control-label"></span></label>
						<input name="company_email" placeholder="Company email address" class="form-control"  maxlength="50"
						type="email" ng-model="serviceProvider.email" required>
					</div>
					</div>
					<div class="col-md-3 col-sm-3">
					<div class="form-group">
						<label for="company_help_number">Help Desk Number </label>
						<input name="help_number" placeholder="Company help desk number" class="form-control"  maxlength="11"
						type="text" ng-model="serviceProvider.helpDeskNumber" ng-keypress="filterValue($event)" ng-pattern="onlyNumbers">
					</div>
					</div>
					
					<div class="col-md-3 col-sm-3 required">
					<div class="form-group">
						<label>Region <span class="control-label"></span></label>
						<%-- <select name="region" class="form-control selectpicker" required
						ng-model="serviceProvider.region" ng-change="getRegionCountry(serviceProvider.region)"
						ng-options="val as val.regionName for val in regionList">
							  <option value="">Select Region</option>
						</select> --%>
						<select name="regionSelect" class="form-control selectpicker" required
						id="regionSelect" onchange="validateDropdownValues('regionSelect')">
						</select>
						<input type="hidden" ng-model="serviceProvider.region">
					</div>
					</div>
					
					
					<div class="col-md-3 col-sm-3  required">
					<div class="form-group">
						<label> Country <span class="control-label"></span></label>
					<%-- 	<select name="Country" class="form-control selectpicker" required
						ng-model="serviceProvider.country"
						ng-options="val as val.countryName for val in countryList">
							 <option value="">Select Country</option>
						</select> --%>
						
						<select name="countrySelect" class="form-control selectpicker" required
						id="countrySelect" onchange="validateDropdownValues('countrySelect')">
						</select>
						<input type="hidden" ng-model="serviceProvider.country">
					</div>
					</div>
					<div class="col-md-12 col-12-4">
					<div class="form-group">
						<label for ="description"> Additional Details</label>
						<textarea class="form-control" id="description" name="Details" placeholder="Additional Details"
						ng-model="serviceProvider.additionalDetails"></textarea>
					</div>
					</div>
					</div>
					</div>
              </div>
              <div class="tab-pane" id="slaDetailsTab">
			  <div class="row">
					<div class="box-body">
               	<div class="col-md-8">
					<div class="form-group">
						<table class="table table-responsive table-sm"  >
							<thead>
							  <tr>
								<th class="col-md-6">Priority</th>
								<th class="col-md-2">Duration</th>
								<th class="col-md-4">Unit</th>
								
							  </tr>
							</thead>
							<tbody>
							  <tr ng-repeat="val in priorities">
								<td class="reqDiv required">
								{{$index + 1}}-{{val.priority}}
								[{{val.description}}]<span class="control-label"></span>
								</td>
								<td>
								<input name="slaId" placeholder="" class="form-control" type="hidden" 
								id="slaId{{$index}}"  ng-model="val.slaId">
								<input name="Duration" placeholder="" class="form-control" type="text" 
								ng-keypress="filterValue($event)" maxlength="3" ng-pattern="onlyNumbers"
								id="duration{{$index}}"  required
								ng-model="val.duration"></td>
								<td>
									<select name="Unit" class="form-control selectpicker" ng-model="val.unit" required>
									 <option value="">Select Unit</option>
									  <option value="hours">Hours</option>
									  <option value="days">Calendar Days</option>
									</select>
								</td>
								
							  </tr>
							</tbody>
						</table>
					</div>
					<div class="form-group">
						<label for ="description">Further SLA details and comments</label>
						<textarea class="form-control" id="slaDescription" name="slaDescription" placeholder="SLA Description"
						ng-model="serviceProvider.slaDescription"></textarea>
					</div>
					</div>
					
				<!-- <div class="col-md-6">
				
					<div class="form-group">
						<table class="table table-responsive table-sm"  border="0">
							<thead>
							  <tr>
								<th class="col-md-1">Esc Level</th>
								<th class="col-md-2">Contact</th>
								<th class="col-md-3">Email</th>
							  </tr>
							</thead>
							<tbody>
							  <tr ng-repeat="val in escalationLevels">
								<td class="reqDiv required">Level {{$index + 1}}<span ng-if="$index==0" class="control-label"></span>
								<input type="hidden" ng-model="val.escId" name="escId">
								</td>
								<td><input name="escalationContact" placeholder="" id="escalationContact{{$index}}" 
								class="form-control" type="text" ng-model="val.contact" maxlength="50" ng-required='$index==0'></td>
								
								<td><input name="escalationEmail" placeholder="" id="escalationEmail{{$index}}"  
								class="form-control" type="email" ng-model="val.email" maxlength="50" ng-required='$index==0' ></td>
							  </tr>
							  
							</tbody>
						</table>
					</div>
					</div> -->
              </div>
             
            </div>
          </div>
         		
         		<div class="tab-pane" id="escalationTab">
			  <div class="row">
					<div class="box-body">               						
				<div class="col-md-8">
				
					<div class="form-group">
						<table class="table table-responsive table-sm"  border="0">
							<thead>
							  <tr>
								<th class="col-md-1">Esc Level</th>
								<th class="col-md-2">Contact Name</th>
								<th class="col-md-3">Email</th>
							  </tr>
							</thead>
							<tbody>
							  <tr ng-repeat="val in escalationLevels">
								<td class="reqDiv required">Level {{$index + 1}}<span ng-if="$index==0" class="control-label"></span>
								<input type="hidden" ng-model="val.escId" name="escId">
								</td>
								<td><input name="escalationContact" placeholder="" id="escalationContact{{$index}}" 
								class="form-control" type="text" ng-model="val.contact" maxlength="50" ng-required='$index==0'></td>
								
								<td><input name="escalationEmail" placeholder="" id="escalationEmail{{$index}}"  
								class="form-control" type="email" ng-model="val.email" maxlength="50" ng-required='$index==0' ></td>
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
				<button type="button" class="btn btn-default pull-left"	id="spModalCloseBtn" data-dismiss="modal">CLOSE</button>
				<button type="submit" class="btn btn-success" onclick="validate_tab(createServiceProviderform)" >SAVE CHANGES</button>
				<button type="reset" id="resetAddUserForm" class="btn btn-success">RESET</button>
			</div>
				</form>
			</div>
			</div>
						
				</div>
				</section>
				
		</div>
	</div>
