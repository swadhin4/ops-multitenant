<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>


<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/ionicons.min.css"></c:url>' />
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/bootstrap-toggle.min.css"></c:url>' />


<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/jquery.dataTables.min.css"></c:url>' />

<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/responsive.bootstrap.min.css"></c:url>' />

<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/optionbtn.css"></c:url>' />
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/jquery.dataTables.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/dataTables.bootstrap.min.js "></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/dataTables.responsive.min.js"></c:url>'></script>
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/responsive.bootstrap.min.js"></c:url>'></script>


<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/incident-modal.css"></c:url>' />
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/angucomplete-alt.css"></c:url>'>
<link rel="stylesheet"
	href='<c:url value="/resources/theme1/css/select2.min.css"></c:url>' />
<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/select2.full.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/ext-site-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/site-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/asset-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/service-provider-service.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>

<script type="text/javascript"
	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>

<style>
.main-box.no-header {
	padding-top: 20px;
}

.table tbody tr.currentSelected {
	background-color: rgba(60, 141, 188, 0.58) !important;
}

/* .table th {
	background: none repeat scroll 0 0 #0077BF !important;
} */
.currentSelected {
	background: rgba(60, 141, 188, 0.58);
	color: #fff;
}

.currentSelected a {
	color: #fff
}

#errorMessageDiv, #successMessageDiv, #infoMessageDiv {
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

#modalMessageDiv {
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

.messageClose {
	background-color: #000;
	padding: 8px 8px 10px;
	position: relative;
	left: 8px;
}

.reqDiv.required .control-label:after {
	content: "*";
	color: red;
}

.popover {
	max-width: 500px;
}

.round.hollow a {
	border: 2px solid green;
	border-radius: 15px;
	color: green;
	font-size: 12px;
	padding: 2px 2px;
	text-decoration: none;
	font-family: 'Open Sans', sans-serif;
}

.middle {
	margin-top: 17px;
}

.info-box {
	background: #deefe5
}

.info-box-number {
	font-size: 15px;
}

.wordspace {
	display: block;
	word-wrap: break-word;
	white-space: normal"
}

.nav>li>a.ctab {
    position: relative;
    display: block;
    padding: 8px 120px;
}

#time-range p {
    font-family:"Arial", sans-serif;
    font-size:14px;
    color:#333;
}
.ui-slider-horizontal {
    height: 8px;
    background: #D7D7D7;
    border: 1px solid #BABABA;
    box-shadow: 0 1px 0 #FFF, 0 1px 0 #CFCFCF inset;
    clear: both;
    margin: 8px 0;
    -webkit-border-radius: 6px;
    -moz-border-radius: 6px;
    -ms-border-radius: 6px;
    -o-border-radius: 6px;
    border-radius: 6px;
}
.ui-slider {
    position: relative;
    text-align: left;
}
.ui-slider-horizontal .ui-slider-range {
    top: -1px;
    height: 100%;
}
.ui-slider .ui-slider-range {
    position: absolute;
    z-index: 1;
    height: 8px;
    font-size: .7em;
    display: block;
    border: 1px solid #5BA8E1;
    box-shadow: 0 1px 0 #AAD6F6 inset;
    -moz-border-radius: 6px;
    -webkit-border-radius: 6px;
    -khtml-border-radius: 6px;
    border-radius: 6px;
    background: #81B8F3;
    background-image: url('data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgipZHRoPSIxMDAlIiBoZWlnaHQ9IjEwMCUiIGZpbGw9InVybCgjZ3JhZCkiIC8+PC9zdmc+IA==');
    background-size: 100%;
    background-image: -webkit-gradient(linear, 50% 0, 50% 100%, color-stop(0%, #A0D4F5), color-stop(100%, #81B8F3));
    background-image: -webkit-linear-gradient(top, #A0D4F5, #81B8F3);
    background-image: -moz-linear-gradient(top, #A0D4F5, #81B8F3);
    background-image: -o-linear-gradient(top, #A0D4F5, #81B8F3);
    background-image: linear-gradient(top, #A0D4F5, #81B8F3);
}
.ui-slider .ui-slider-handle {
    border-radius: 50%;
    background: #F9FBFA;
    background-image: url('data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgipZHRoPSIxMDAlIiBoZWlnaHQ9IjEwMCUiIGZpbGw9InVybCgjZ3JhZCkiIC8+PC9zdmc+IA==');
    background-size: 100%;
    background-image: -webkit-gradient(linear, 50% 0, 50% 100%, color-stop(0%, #C7CED6), color-stop(100%, #F9FBFA));
    background-image: -webkit-linear-gradient(top, #C7CED6, #F9FBFA);
    background-image: -moz-linear-gradient(top, #C7CED6, #F9FBFA);
    background-image: -o-linear-gradient(top, #C7CED6, #F9FBFA);
    background-image: linear-gradient(top, #C7CED6, #F9FBFA);
    width: 22px;
    height: 22px;
    -webkit-box-shadow: 0 2px 3px -1px rgba(0, 0, 0, 0.6), 0 -1px 0 1px rgba(0, 0, 0, 0.15) inset, 0 1px 0 1px rgba(255, 255, 255, 0.9) inset;
    -moz-box-shadow: 0 2px 3px -1px rgba(0, 0, 0, 0.6), 0 -1px 0 1px rgba(0, 0, 0, 0.15) inset, 0 1px 0 1px rgba(255, 255, 255, 0.9) inset;
    box-shadow: 0 2px 3px -1px rgba(0, 0, 0, 0.6), 0 -1px 0 1px rgba(0, 0, 0, 0.15) inset, 0 1px 0 1px rgba(255, 255, 255, 0.9) inset;
    -webkit-transition: box-shadow .3s;
    -moz-transition: box-shadow .3s;
    -o-transition: box-shadow .3s;
    transition: box-shadow .3s;
}
.ui-slider .ui-slider-handle {
    position: absolute;
    z-index: 2;
    width: 22px;
    height: 22px;
    cursor: default;
    border: none;
    cursor: pointer;
}
.ui-slider .ui-slider-handle:after {
    content:"";
    position: absolute;
    width: 8px;
    height: 8px;
    border-radius: 50%;
    top: 50%;
    margin-top: -4px;
    left: 50%;
    margin-left: -4px;
    background: #30A2D2;
    -webkit-box-shadow: 0 1px 1px 1px rgba(22, 73, 163, 0.7) inset, 0 1px 0 0 #FFF;
    -moz-box-shadow: 0 1px 1px 1px rgba(22, 73, 163, 0.7) inset, 0 1px 0 0 white;
    box-shadow: 0 1px 1px 1px rgba(22, 73, 163, 0.7) inset, 0 1px 0 0 #FFF;
}
.ui-slider-horizontal .ui-slider-handle {
    top: -.5em;
    margin-left: -.6em;
}
.ui-slider a:focus {
    outline:none;
}

#slider-range, #slider-rangeDelivery{
  width: 100%;
  margin: 0 auto;
}
#time-range, #time-rangeDelivery {
   width: 100%;
}

</style>
<script>
$(function(){
	$("#slider-range").slider({
	    range: true,
	    min: 0,
	    max: 1439,
	    step: 1,
	    values: [0, 1439],
	    slide: function (e, ui) {
	      //alert(ui.values[0]);  
	      var hours1 = Math.floor(ui.values[0] / 60);
	        var minutes1 = ui.values[0] - (hours1 * 60);

	        if (hours1 < 10) hours1 = "0" + hours1;
	        if (minutes1 < 10) minutes1 = "0" + minutes1;
	        if (minutes1 == 0) minutes1 = '00';
	       /* if (hours1 <= 1) {
	          hours1 = 1;
	          minutes1 = minutes1 ; 
	        } */
	        if (hours1 >= 12) {
	          hours1 = hours1+1;
	          //minutes1 = minutes1 ; 
	        } 
	        if (hours1 >= 24) {
	            return;
	          }
	        if(hours1 == 23 && minutes1 == 59){
	          hours1 = 23;
	           minutes1 = minutes1 ;
	        }


	        $('.slider-time').html(hours1 + ':' + minutes1);
	        $('.slider-startTime').val(hours1 + ':' + minutes1);
	        

	        var hours2 = Math.floor(ui.values[1] / 60);
	        var minutes2 = ui.values[1] - (hours2 * 60);

	        if (hours2 < 10) hours2 = "0" + hours2;
	        if (minutes2 < 10) minutes2 = "0" + minutes2;
	        if (minutes2 == 0) minutes2 = '00';
	        if (hours2 >= 12) {
	          hours2 = hours2 +1;
	            
	        } 
	        if (hours2 >= 24) {
	            return;
	          }
	        if(hours2 == 23 && minutes2 == 59){
	          hours2 = 23;
	           minutes2 = minutes2 ;
	        }

	        $('.slider-time2').html(hours2 + ':' + minutes2);
	        $('.slider-endTime').val(hours2 + ':' + minutes2);
	    }
	});
	
	$("#slider-rangeDelivery").slider({
	    range: true,
	    min: 0,
	    max: 1440,
	    step: 1,
	    values: [0, 1440],
	    slide: function (e, ui) {
	      //alert(ui.values[0]);  
	      var hours1 = Math.floor(ui.values[0] / 60);
	        var minutes1 = ui.values[0] - (hours1 * 60);

	        if (hours1 < 10) hours1 = "0" + hours1;
	        if (minutes1 < 10) minutes1 = "0" + minutes1;
	        if (minutes1 == 0) minutes1 = '00';
	       /* if (hours1 <= 1) {
	          hours1 = 1;
	          minutes1 = minutes1 ; 
	        } */
	        if (hours1 >= 12) {
	          hours1 = hours1+1;
	          //minutes1 = minutes1 ; 
	        } 
	        if (hours1 >= 24) {
	            return;
	          }
	        if(hours1 == 23 && minutes1 == 59){
	          hours1 = 23;
	           minutes1 = minutes1 ;
	        }


	        $('.slider-time').html(hours1 + ':' + minutes1);
	        $('.slider-delStartTime').val(hours1 + ':' + minutes1);
	        

	        var hours2 = Math.floor(ui.values[1] / 60);
	        var minutes2 = ui.values[1] - (hours2 * 60);

	        if (hours2 < 10) hours2 = "0" + hours2;
	        if (minutes2 < 10) minutes2 = "0" + minutes2;
	        if (minutes2 == 0) minutes2 = '00';
	        if (hours2 >= 12) {
	          hours2 = hours2 +1;
	            
	        } 
	        if (hours2 >= 24) {
	            return;
	          }
	        if(hours2 == 23 && minutes2 == 59){
	          hours2 = 23;
	           minutes2 = minutes2 ;
	        }

	        $('.slider-time2').html(hours2 + ':' + minutes2);
	        $('.slider-delEndTime').val(hours2 + ':' + minutes2);
	    }
	});
	
	
});
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
	<div ng-controller="extSiteController" id="siteWindow">
		<div style="display: none" id="loadingDiv">
			<div class="loader">Loading...</div>
		</div>
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
						<div class="row" id="createSiteWindow" style="display:none">
						  <form  name="createsiteform" ng-submit="saveSiteForm(createsiteform)">
						<div class="col-md-2 col-sm-6 col-xs-12">
          <div class="info-box">
            <span class="info-box-icon bg-red"><i class="fa fa-building middle" data-original-title="" title=""></i></span>

            <div class="info-box-content">
             <a href ng-click="findAllSites()"> <span class="info-box-number ng-binding">{{siteData.customer.companyName}}</span></a>
            </div>
          </div>
        </div>
								<div class="col-md-10">
					   	 	    <div class="nav-tabs-custom">
           					 <ul class="nav nav-tabs" style="background-color: rgba(60, 141, 188, 0.34);">
							<li class="active">
				       		 <a  href="#siteDetailsTab" data-toggle="tab" aria-expanded="true" id="siteViewLink" class="ctab"><b>Site Details</b></a>
							</li>
							<li><a href="#siteContactsTab" data-toggle="tab" aria-expanded="true" id="siteContactLink" class="ctab"><b>Site Contacts</b></a>
							</li>
				  			<li><a href="#operationTab" data-toggle="tab" aria-expanded="true" id="siteOperationLink" class="ctab"><b>Site Operation info</b></a>
							</li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="siteDetailsTab">
				<div class="row">
					<div class="box-body">
						 <div class="col-md-12">
							<div class="row">
							<div class="col-md-8 reqDiv required">
							  <input type="hidden"  class="form-control" placeholder="Enter Site Name" 
                 					name="siteName" ng-model="siteData.siteId" >	
							 <label class="control-label" for="siteName">Site Name</label> 
								  <input type="text" maxlength="50" class="form-control" placeholder="Enter Site Name" 
                 					name="siteName" ng-model="siteData.siteName" required >	
							 </div>
							<div class="col-md-4 reqDiv required">
							 <label class="control-label" for="owner">Site Owner</label>
								  <input type="text" maxlength="50" class="form-control" placeholder="Enter Site Owner" 
                  				name="owner" ng-model="siteData.owner" required>	
							</div>
							</div>
								<div class="row">
							 <div class="col-xs-4">
				                <label for="district">District</label>
				                  <select name="districtSelect" id="districtSelect" class="form-control" 
								  onchange="validateDropdownValues('districtSelect')">
								 </select>
								<input type="hidden" ng-model="district.selected">
				                </div>
				                <div class="col-xs-4">
				                <label for="area">Area</label>
				                  <select name="areaSelect" id="areaSelect" class="form-control" 
								  onchange="validateDropdownValues('areaSelect')">
								</select>
								<input type="hidden" ng-model="area.selected">
				                </div>
				                <div class="col-xs-4">
				                <label for="cluster">Cluster</label>
				                  <select name="clusterSelect" id="clusterSelect" class="form-control" 
								onchange="validateDropdownValues('clusterSelect')">
								</select>
								<input type="hidden" ng-model="cluster.selected">
	               			 </div>
								</div>
								
								<div class="row">
								<div class="col-xs-4">
					               <label for="electricityId">Electricity Id (MPAN)</label>
									  <input type="text" maxlength="50" class="form-control" 
									  placeholder="Enter Electricity ID (MPAN)" name="electricityId" ng-model="siteData.electricityId">
					                </div>
									<div class="col-xs-4 reqDiv required">
					                <label class="control-label" for="siteNumber1">Site ID Number 1</label>
					                  <input type="text" maxlength="11" class="form-control" placeholder="Enter Site ID Number 1" 
					                  name="sitenumber1" ng-model="siteData.siteNumber1" required ng-pattern="onlyNumbers" ng-keypress="filterValue($event)">
					                </div>
					                <div class="col-xs-4">
					                <label for="siteNumber2">Site ID Number 2</label>
					                  <input type="text" maxlength="11" class="form-control" placeholder="Enter Site ID Number 2" 
					                  name="sitenumber2" ng-model="siteData.siteNumber2" ng-pattern="onlyNumbers" ng-keypress="filterValue($event)">
					               </div>
								</div>
								<div class="row">
									   <div class="col-xs-4">
					                <label for="fileInput">File Input (Max Size 100KB)</label>
					                  <input type="file" id="siteInputFile" class="form-control" 
					                  name="inputfilepath"  accept="image/*,.doc, .docx,.pdf" >
					                </div>
					                
					                <div class="col-xs-4">
					                <label for="siteNumber2">Size of Sales Area (M<sup>2</sup>)</label>
					                  <input type="text" maxlength="8" class="form-control" placeholder="Enter Size of Sales Area" 
					                  name="salesareasize" ng-model="siteData.salesAreaSize" valid-number >
					               </div>
					               
					               <div class="col-xs-4 reqDiv required">
				                <label class="control-label" for="brand">Brand</label>				                 
				                  <select name="brandSelect" id="brandSelect" class="form-control" required
								  onchange="validateDropdownValues('brandSelect')">
									
								 </select>
								<input type="hidden" ng-model="brand.selected" required>
				                </div>
								</div><br>
								<div ng-if="siteData.siteId == NULL || siteData.siteId == '' ">
								<button type="button" id="resetSiteDetailTab" ng-click="resetSiteDetailsTab()" class="btn btn-danger pull-right">RESET</button>
								</div>
						</div>
						</div>
						</div>
						</div>
              
              <div class="tab-pane" id="siteContactsTab">
				  <div class="row">
				   <div class="col-md-6">
				   <div class="row">
				<div class="col-md-12"> 
				  <div class="panel panel-default">
     <div class="panel-body"> 
					<div class="box-body">
						 <div class="row">
                <div class="col-xs-6 reqDiv required">
                <label class="control-label" for="contactName">Contact Name</label>
                  <input type="text" maxlength="50" class="form-control" 
                  placeholder="Enter site contact name" name="contactname" ng-model="siteData.contactName" required>
                </div>
                <div class="col-xs-6">
                <div class="form-group reqDiv required"  >
                <label class="control-label" for="emailAddress">Email Address</label>
                  <input type="email" class="form-control"  maxlength="50"
                  placeholder="Enter email" name="email" ng-model="siteData.email" required>
                 </div>
                </div>
			  </div>
              
            <div class="row">
                <div class="col-xs-6">
                <label for="longitude">Longitude</label>
                  <input type="text" rel="txtTooltip" data-toggle="tooltip"  id="lng" maxlength="30" class="form-control" 
                  placeholder="Enter longitude" name="longitude" ng-model="siteData.longitude" ng-required='siteData.latitude'  >
                </div>
                <div class="col-xs-6">
                <label for="latitude">Latitude</label>
                  <input type="text" rel="txtTooltip" data-toggle="tooltip" id="lat" maxlength="30" class="form-control" 
                  placeholder="Enter latitude" name="latitude" ng-model="siteData.latitude" ng-required='siteData.longitude' >
                </div>
              
              </div>
			  
			  <div class="row">
                 <div class="col-xs-6">
                <div class="form-group reqDiv required"  >
                <label class="control-label" for="primaryContactNum">Primary Contact</label>
                  <input type="text" ng-pattern="onlyNumbers" ng-keypress="filterValue($event)" 
                   maxlength="11" class="form-control" placeholder="Enter Primary contact No" 
                  name="primaryphoneno" ng-model="siteData.primaryContact" required>
                </div>
                </div>
                <div class="col-xs-6">
                <label for="secondaryContactNum">Secondary Contact</label>
                  <input type="text" min="0" maxlength="11" class="form-control" placeholder="Enter Secondary Contact No" 
                  name="secondaryphoneno" ng-model="siteData.secondaryContact" 
                  ng-pattern="onlyNumbers" ng-keypress="filterValue($event)">
                </div>
                </div>
				   </div>
				   </div>
				   </div>
				   </div>
				   </div>
				   
				   
				   </div>
				<div class="col-md-6">
				<div class="row">
				<div class="col-md-12"> 
				  <div class="panel panel-default">
     <div class="panel-body"> 
				<div class="box-body">
				<div class="row">
				
				<div class="col-xs-6">
                <label for="Address1">Address Line1</label>
                  <input type="text" min="0" maxlength="50" class="form-control" placeholder="Enter Address Line1" 
                  name="Address1" ng-model="siteData.siteAddress1" >
                </div>
                <div class="col-xs-6">
                <label for="Address2">Address Line2</label>
                  <input type="text" min="0" maxlength="50" class="form-control" placeholder="Enter Address Line2" 
                  name="Address2" ng-model="siteData.siteAddress2" >
                </div>
				
				</div>
				<div class="row" >
				<div class="col-xs-6">
                <label for="Address3">Address Line3</label>
                  <input type="text" min="0" maxlength="50" class="form-control" placeholder="Enter Address Line3" 
                  name="Address3" ng-model="siteData.siteAddress3" >
                </div>
				<div class="col-xs-6">
                <label for="Address4">Address Line4</label>
                  <input type="text" min="0" maxlength="50" class="form-control" placeholder="Enter Address Line4" 
                  name="Address4" ng-model="siteData.siteAddress4">
                </div>
                
                
				<!-- <label for="siteAddress">Site Address</label>                  
				  <textarea class="form-control" style="width: 100%;
   				 height: 176px;" rows="3" placeholder="Enter site address" name="address" 
   				 ng-model="siteData.address"></textarea> -->
				
				</div>
				<div class="row">
				<div class="col-xs-6">
                <label for="Zip">Zipcode</label>
                  <input type="text" min="0" maxlength="15" class="form-control" placeholder="Enter Zip code" 
                  name="Zip" ng-model="siteData.zipCode">
                </div>
				</div>
			   <div ng-if="siteData.siteId == NULL || siteData.siteId == '' ">
	   		   <button type="button" id="resetSiteContactTab" ng-click="resetSiteContactTab()" class="btn btn-danger pull-right">RESET</button>
	   		   </div>
	   		   </div>
           </div>
           </div>
           </div>
			   </div>
	    	</div>
            </div>
		</div>
					
				    <div class="tab-pane " id="operationTab" >
				<div class="row">
					<div class="box-body">
					   <div class="row">
					    <div class="col-md-12">
							 <div class="col-md-6" >
           <div class="box-header pull-center" style="height:35px">
              <span class="pull-center">
              <b>Sales Operation Schedule</b>
              </span>
            </div>
            <div id="time-range" ><p><div id="slider-range"></div>  </p></div>
              <table id="example2" class="table table-bordered table-hover table-condensed">
                <tbody>
                <tr>
                
                  <th style="width:30%"><b>Operating Days</b></th>
                  <!-- <th style="width:43%"><b>Select Sales timing (24 hrs)</b></th> -->
                  <th><b>Sales Operation Timinings (24 hrs)</b></th>               
                  
                </tr>
             
          <tr  ng-repeat="salesoperationdetail in salesoperationDetails.list"> 
                               	                     
                  <td >
                  
                  <label class="control-label">{{salesoperationdetail.days}}</label>
                  
                  </td>
                  
                  <td>
                 
                                  <div class="input-group">

    <input type="text" class="form-control selector slider-startTime"  placeholder="Start" 
    id="salesDayFrom{{$index}}" ng-keypress="validateSalesTime(this,$event)" ng-model="salesoperationdetail.selected.from.name" value="{{salesoperationdetail.selected.from.name}}" maxlength="5" />
    <span class="input-group-addon">-</span>
    <input type="text" class="form-control selector slider-endTime" placeholder="End" 
    id="salesDayTo{{$index}}"  ng-keypress="validateSalesTime(this,$event)" ng-model="salesoperationdetail.selected.to.name" value="{{salesoperationdetail.selected.from.name}}" maxlength="5"/>

  <!--   <input type="text" class="form-control"  placeholder="Start" id="salesDayFrom{{$index}}" ng-model="salesoperationdetail.selected.from.name"  readonly/>
    <span class="input-group-addon">-</span>
    <input type="text" class="form-control" placeholder="End" id="salesDayTo{{$index}}" ng-model="salesoperationdetail.selected.to.name" readonly />
 -->

                  </td>                  
                </tr>
                </tbody>
              </table>
              </div>
              
              <div class="col-md-6" >
              <div class="box-header pull-center" style="height:35px">
              <span class="pull-center">
              <b>Delivery Operation Schedule</b>
              </span>
            </div>
            <div id="time-rangeDelivery" ><p><div id="slider-rangeDelivery"></div> </p></div>
            <div class="box-body no-padding">
              <table id="example2" class="table table-bordered table-hover table-condensed " >
               <tbody>
                <tr>
                
                  <th  style="width:30%"><b>Operating Days</b></th>
                  <!-- <th style="width:45%"><b>Select Delivery Timing (24 HRS)</b></th> -->
                  <th><b>Operating Timings (24 HRS)</b></th>         
                  
                </tr>
                <tr style="height:30px" ng-repeat="deliveryoperationdetail in deliveryoperationDetails.list" >                	                     
                  <td >
                  <hr style="padding:0px; margin:0px;"><label class="control-label">{{deliveryoperationdetail.days}}</label></td>
                  <td>
                 
                                  <div class="input-group">
    <input type="text" class="form-control slider-delStartTime"  placeholder="Start" id="deliveryDayFrom{{$index}}" ng-keypress="validateDeliveryTime(this,$event)" 
    value="{{deliveryoperationdetail.selected.from.name}}" ng-model="deliveryoperationdetail.selected.from.name" maxlength="5" />
    <span class="input-group-addon">-</span>
    <input type="text" class="form-control slider-delEndTime" placeholder="End" id="deliveryDayTo{{$index}}" ng-keypress="validateDeliveryTime(this,$event)" 
    value="{{deliveryoperationdetail.selected.from.name}}" ng-model="deliveryoperationdetail.selected.to.name" maxlength="5" />


                  </td>                 
                </tr>
                </tbody>
              </table>
              </div>
              </div>
						</div>
						</div>
						<div ng-if="siteData.siteId == NULL || siteData.siteId == '' ">
						  <button type="button" id="resetSiteOperationTab" ng-click="resetSiteOperationTab()" class="btn btn-danger pull-right">RESET</button>
						</div>
						</div>
						
						</div>
						</div>	
		
					</div>
					</div>
					<div class="box-footer">
					<button type="submit" onclick="validate_tab(createsiteform)" class="btn btn-success pull-right" >Save changes</button>
					</div>
				</div>
				</form>
					     </div>
							<div class="row" id="siteView">
						<div class="col-md-6">
						<div class="box">
							<div class="box-header with-border">
							<div class="row col-md-4">
							<select name="extCustSelect" id="extCustSelect" class="form-control  pull-left" 
							style="margin-right: 5px;" 
							ng-model="extCustomer.selected"  required
							onchange="angular.element(this).scope().getCustomerSelected(this, event, 'extCustSelect')">
							</select>
							</div>
								<div class="box-tools pull-right">
									<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
										<a href  data-toggle="modal" class="btn btn-success" ng-click="addNewSite()" >
										<i class="fa fa-plus" arial-hidden="true"></i> Add Site</a>
									</sec:authorize>	
									
								</div>
							</div>
							<div class="box-body" style="height:65%;overflow-y:visible;overflow-x:hidden" >
								<div class="row">
	 								<div class="col-md-12">
										<input type="text" class="form-control"	placeholder="Search Site" ng-model="searchSite">
									 </div>
										<div class="col-md-12" ng-if="siteList.length> 0">
											<ul class="products-list product-list-in-box">
												<li class="item"
													ng-repeat="site in siteList | filter:searchSite | orderBy:'siteName'">
													<div class="product-img" style="margin-top: -10px;">
														<img src="${contextPath}/resources/theme1/img/site-icon.png"
															alt="Product Image">
													</div>
													<div class="product-info">
													<div class="col-md-9">
														<a href="javascript:void(0)"
															ng-click="getSiteDetails(site); viewImage()" class="product-title">{{site.siteName}}
														</a> 
														<span class="product-description">
															{{site.fullAddress}} 
														</span>
													</div>
													<div class="col-md-3">
														<span class="badge pull-right" style="background-color:green;color:white">
															{{sitePage.brand}} [{{site.brandName}}] 
														</span>
													</div>	
													<div class="col-sm-4">
													 <i class="fa fa-map-marker" aria-hidden="true" ng-if="site.district.districtName!=null"></i> {{site.district.districtName || ''}}
													</div>
													<div class="col-sm-4">
													<i class="fa fa-map-marker" aria-hidden="true" ng-if="site.area.areaName!=null"></i> {{site.area.areaName || ''}}
													</div>
													<div class="col-sm-4">
													<i class="fa fa-map-marker" aria-hidden="true" ng-if="site.cluster.clusterName!=null"></i> {{site.cluster.clusterName || ''}}
													</div>		
													</div>
													
												</li>

											</ul>
										</div>
										
										
									</div>
              				 </div>
                            
							<div class="box-footer">
								<div class="row">
									<div class="col-sm-3 col-xs-6">
										<div class="description-block border-right">
										<a  class="btn btn-danger">Total Sites :  <span class="badge">{{siteList.length}}</span></a>
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
							<h3 class="box-title">Site Detail</h3>
								<div class="box-tools pull-right">
								<sec:authorize access="hasAnyRole('ROLE_SP_AGENT')">
								<div class="btn-group pull-right" >
									<button type="button"
										class="btn btn-success dropdown-toggle pull-right"
										style="margin-right: 5px;" data-toggle="dropdown"><span class="fa fa-gear"></span>
										Manage Site <span class="caret"></span>
									</button>

									<ul class="dropdown-menu" role="menu">
										<li> <a href="${contextPath}/asset/equipment/create">  <span class="fa fa-plus" aria-hidden="true"></span>Add Equipment</a></li>
										<li> <a href="${contextPath}/asset/service/create"> <span  class="fa fa-plus" aria-hidden="true"></span>Add Service</a></li>
										<li>  <a href data-toggle="modal" ng-click="viewAssetForSelectedSite()" ><span  class="fa fa-eye" aria-hidden="true"></span>View Asset</a></li>
										<li ng-if="siteList.length> 0"><a href  style="margin-right: 5px;" data-toggle="modal" ng-click="manageUserAccess(selectedSite)">
									 		<span class="fa fa-user"></span> Manage User Access </a></li>
									 	<li><a href  style="margin-right: 5px;" data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
									 		<span class="fa fa-edit"></span>Edit Site</a></li>
										<!-- <li><a href data-toggle="modal" >Add an Equipment</a></li>
										<li><a href data-toggle="modal">Add	a Service</a></li> -->
										
									</ul>

								</div>
								</sec:authorize>
								</div>
							</div>
							<div class="box-body" style="overflow-y:auto;overflow-x:hidden;height:72%">
								<div class="row">
									<div class="col-md-12">
									 <div class="table-responsive">
										<table class="table no-margin">
											<!-- <thead>											
											<tr><th><i class="fa fa-sitemap" aria-hidden="true"></i> Site Name</th><th class="pull-right">{{selectedSite.siteName}}</th></tr>
											</thead> -->
											<tr><td><i class="fa fa-sitemap" aria-hidden="true"></i> Site Name</td><td class="pull-right">{{selectedSite.siteName}}</td></tr>
											<tbody>
											<tr><td><i class="fa fa-building" aria-hidden="true"></i> Site Operator</td><td align="right">{{selectedSite.retailerName}}</td></tr>
											<tr><td><i class="fa fa-building" aria-hidden="true"></i> Site Owner</td><td align="right">{{sessionUser.company.companyName}}</td></tr>
											<tr><td><i class="fa fa-bolt" aria-hidden="true"></i> Electricity Id (MPAN)</td><td align="right">{{selectedSite.electricityId}}</td></tr>
											<tr><td><i class="fa fa-sitemap" aria-hidden="true"></i> Site ID Number 1</td><td align="right">{{selectedSite.siteNumber1}}</td></tr>
											<tr><td><i class="fa fa-sitemap" aria-hidden="true"></i> Site ID Number 2</td><td align="right">{{selectedSite.siteNumber2}}</td></tr>
											<tr><td><i class="fa fa-area-chart" aria-hidden="true"></i> Sales Area Size (M<sup>2</sup>)</td><td align="right">{{selectedSite.salesAreaSize}}</td></tr>
											
											</tbody>
										</table>
										</div>
									</div>
										<div class="col-md-12"> 
											<div class="box box-danger">
												<div class="box-header with-border">
													<h3 class="box-title"><i class="fa fa-picture-o" aria-hidden="true"></i> Attachments</h3>
													<a class="users-list-name"	href="javascript:void(0);"  ></a>
													<%-- <div class="box-tools"  ng-if="selectedSite.fileInput!=null">
														<a href="${contextPath}/selected/file/download?keyname={{selectedSite.fileInput}}" class="uppercase" download>
													<i class="fa fa-cloud-download fa-2x" aria-hidden="true"></i></a>
													
														<a href ng-click="deleteFile('SITE', selectedSite)" data-toggle="tooltip" data-original-title="Delete this file">
												<i class="fa fa-remove fa-2x" aria-hidden="true"></i></a>
													</div> --%>
												</div>
												<!-- /.box-header -->
												<div class="box-body no-padding">
													 <input type="hidden" id="siteImg" value="${contextPath}/selected/file/download?keyname={{selectedSite.fileInput}}">
												 <div class="col-md-12">
												 	<div id="noimage" ng-if="selectedSite.fileInput==null">
												 	 <img src="${contextPath}/resources/theme1/img/no-available-image.png" style="width:50%"></img>
												 	</div>
												 	<%-- <div id="noimage" ng-if="selectedSite.fileInput!=null">
												 	 <img src="${contextPath}/selected/file/download?keyname={{selectedSite.fileInput}}" style="width:50%"></img>
												 	</div> --%>
												 </div>
												</div>
												<div class="box-footer" ng-if="selectedSite.fileInput!=null">
													<%-- <a href="${contextPath}/selected/file/download?keyname={{selectedSite.fileInput}}" class="uppercase" download>
													<i class="fa fa-cloud-download fa-2x" aria-hidden="true"></i></a>
													
													<a href ng-click="deleteFile('SITE', selectedSite)" data-toggle="tooltip" data-original-title="Delete this file">
												<i class="fa fa-remove fa-2x" aria-hidden="true"></i></a> --%>
												</div>
												<!-- /.box-footer -->
											</div>
										</div>
								</div>
								
								
										<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title"><i class="fa fa-bars" aria-hidden="true"></i> Contact Information</h3>
							<div class="box-tools pull-right">
							<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
								<div class="btn-group pull-right">
									<a href="" class="btn btn-info" style="margin-right: 5px;" title="Edit contact information"
									data-toggle="modal" ng-click="viewTabSelected(selectedSite,'siteContactLink')">
									<span class="fa fa-edit fa-2x" style="    font-size: 1.2em;"></span></a>
								</div>
								</sec:authorize>
								</div>
							</div>
							<div class="box-body">
								
              <div class="table-responsive">
                <table class="table no-margin">
                  <!-- <thead> -->
                  <tr>
                    <td><i class="fa fa-user" aria-hidden="true"></i> Name</tdd><td align="right">{{selectedSite.contactName}}</td>
                    </tr>
                    <tr>
                    <td><i class="fa fa-envelope" aria-hidden="true"></i> Email</td><td align="right">{{selectedSite.email}}</td>
                    </tr>
                    <tr>
                    <td><i class="fa fa-building-o"></i> Address</td><td align="right">{{selectedSite.siteAddress}} </td>
                    </tr>
                    <tr>
                    <td><i class="fa fa-map-marker" aria-hidden="true"></i> Latitude</td><td align="right">{{selectedSite.latitude}} </td>
                    </tr>
                      <td><i class="fa fa-map-marker" aria-hidden="true"></i> Longitude</td><td align="right">{{selectedSite.longitude}} </td>
                    </tr>
                    <tr>
                    <td><i class="fa fa-phone-square" aria-hidden="true"></i> Contact Number</td><td align="right">{{selectedSite.primaryContact}}<br> 
                    					 {{selectedSite.secondaryContact || ''}}</td>
                    </tr>
                  </tr>
                 <!--  </thead> -->

                  </tbody>
                </table>
              </div>
              <!-- /.table-responsive -->
            </div>
						</div>
								
					
						
							<div class="box">
							<div class="box-header with-border">
								<h3 class="box-title">Operation Timings</h3>
							<div class="box-tools pull-right">
							<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
								<a href="" class="btn btn-info" style="margin-right: 5px;" title="Edit Operation timings"
									data-toggle="modal" ng-click="viewTabSelected(selectedSite,'siteOperationLink')">
									<span class="fa fa-edit fa-2x" style="font-size: 1.0em;"></span></a>
								</sec:authorize>
								</div>
							</div>
							<div class="box-body">
							<div class="row">
								<div class="col-md-6">			
						  <div class="table-responsive" style="overflow-x:hidden">
							<table class="table no-margin">
							  <tbody>
							  <tr>
								<th>Weekdays</th>
								<th>Sales</th>
							  </tr>
							  <tr ng-repeat="timing in selectedSite.SalesOperation">
								<td>{{timing.days}}</td>
								<td ng-if="timing.from == '00:00' && timing.to == '00:00'">
								<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
								<a href data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
								<span class="label label-warning">{{timing.from}} - {{timing.to}}</span></a>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('ROLE_SITE_STAFF')">
								<a href data-toggle="modal" >
								<span class="label label-warning">{{timing.from}} - {{timing.to}}</span></a>
								</sec:authorize>
								</td>
								<td ng-if="timing.from == '00:00' && timing.to != '00:00'">
								<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
								<a href data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
								<span class="label label-danger">
								<i class="fa fa-exclamation-triangle" aria-hidden="true"></i> {{timing.from}} - {{timing.to}} </span></a>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('ROLE_SITE_STAFF')">
								<a href data-toggle="modal" >
								<span class="label label-warning">{{timing.from}} - {{timing.to}} </span></a>
								</sec:authorize>
								
								</td>	
								<td ng-if="timing.from != '00:00' && timing.to == '00:00'">
								<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
								<a href data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
								<span class="label label-danger">
								<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>{{timing.from}} - {{timing.to}} </span></a>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('ROLE_SITE_STAFF')">
								<a href data-toggle="modal" >
								<span class="label label-warning">{{timing.from}} - {{timing.to}} </span></a>
								</sec:authorize>
								</td>
								
								<td ng-if="timing.from != '00:00' && timing.to != '00:00'">
								<span class="label label-success">{{timing.from}} - {{timing.to}} </span>
								
								
								 </td>													 
							  </tr>

							  </tbody>
							</table>
						  </div>
						  </div>
						  <div class="col-md-6">			
						  <div class="table-responsive">
							<table class="table no-margin">
							<tbody>
							  <tr>
								<th>Weekdays</th>
							
								<th>Delivery</th>
								
							  </tr>
							  <tr ng-repeat="timing in selectedSite.DeliveryOperation">
									<td>{{timing.days}}</td>
								<td ng-if="timing.from == '00:00' && timing.to == '00:00'">
								<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
								<a href data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
								<span class="label label-warning">{{timing.from}} - {{timing.to}}</span></a>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('ROLE_SITE_STAFF')">
								<a href data-toggle="modal" >
								<span class="label label-warning">{{timing.from}} - {{timing.to}}</span></a>
								</sec:authorize>
								</td>
								<td ng-if="timing.from == '00:00' && timing.to != '00:00'">
								<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
								<a href data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
								<span class="label label-danger">
								<i class="fa fa-exclamation-triangle" aria-hidden="true"></i> {{timing.from}} - {{timing.to}} </span></a>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('ROLE_SITE_STAFF')">
								<a href data-toggle="modal" >
								<span class="label label-warning">{{timing.from}} - {{timing.to}} </span></a>
								</sec:authorize>
								
								</td>	
								<td ng-if="timing.from != '00:00' && timing.to == '00:00'">
								<sec:authorize access="hasAnyRole('ROLE_MAINTENANCE_STAFF', 'ROLE_OPS_MANAGER')">
								<a href data-toggle="modal" ng-click="updateSiteModal(selectedSite)">
								<span class="label label-danger">
								<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>{{timing.from}} - {{timing.to}} </span></a>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('ROLE_SITE_STAFF')">
								<a href data-toggle="modal" >
								<span class="label label-warning">{{timing.from}} - {{timing.to}} </span></a>
								</sec:authorize>
								</td>
								
								<td ng-if="timing.from != '00:00' && timing.to != '00:00'">
								<span class="label label-success">{{timing.from}} - {{timing.to}} </span>
								
								
								 </td>
												 
							  </tr>

							  </tbody>
							</table>
						  </div>
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
								<div class="box-footer">
								<div class="row">
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
										<div class="description-block border-right">
											
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
							
						</div>
					

				</div>

			</div>
		</div>
		</section>
	</div>
</div>
</html>