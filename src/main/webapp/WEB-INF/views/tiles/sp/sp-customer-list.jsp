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
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/sp-customer-controller.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/services.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>


</head>

<div class="content-wrapper">
	<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
	<div ng-controller="spCustomerController" id="incidentWindow">
		<div style="display:none" id="loadingDiv">
			<div class="loader">Loading...</div>
			
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
					</div>
				</div>
			</section>
			
			<section class="content">
				<div class="row">
					<div class="col-md-12">
						
						<div class="box">
						
							<div class="box-header with-border">
								<h3 class="box-title">Customer Details</h3>								
							</div>

							<div class="box-body" style="height:70%" >
								<div class="row">
									<div class="col-md-12">
										<div style="overflow-x: hidden;overflow-y:auto; height: 110%">
											<div class="table-responsive"> 
												<table id="customerDetails" class="table table-bordered table-striped" cellspacing="0">
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
											<a  class="btn btn-danger pull-left">Total Customers :  <span class="badge">{{ticket.list.length}}</span></a>
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
</div>