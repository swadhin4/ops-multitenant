<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html>
<script>
var webContextPath="${pageContext.request.contextPath}";
</script>
 <script type="text/javascript" src='<c:url value="/resources/theme1/chart/loader.js"></c:url>'></script>
 <script type="text/javascript" src='<c:url value="/resources/theme1/js/dashboard.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
 <style>
 
 </style>
<head>
<title>Home</title>
</head>
<div class="content-wrapper">
			<section class="content">
			<h3 >Summarized View of Site and Service Providers Incidents</h3>
			<div class="row">
			<div class="col-sm-6 col-md-6 col-lg-6">
			<div class="box" >
				<div class="box-header with-border">
						<div id="filter_site_pie_div" align="left"></div>
					</div>
				<div class="box-body">				
				<div class="row">
				<div class="col-md-12" id="dashboard_div">
					<div id="siteDiv1" style="overflow-x:hidden; overflow-y:hidden">
						<div class="row" >
							  <div class="col-md-12">
							  <div id="chart_site_pie_div"></div>
							  </div>
						</div>
					</div>
				</div>
			</div>
			</div>
			</div>
			</div>
		 
			<div class="col-sm-6 col-md-6 col-lg-6">
			<div class="box" >
				<div class="box-header with-border">
						<div id="filter_site_bar_div" align="left"></div>
					</div>
				<div class="box-body">				
				<div class="row">
				<div class="col-md-12" id="dashboard_div">
					<div id="siteDiv2" style="overflow-x:hidden; overflow-y:hidden" >
					<div class="row" >
							  <div class="col-md-12">
							  <div id="chart_site_bar_div"></div>
							  </div>
						</div>
					</div>
				</div>
			</div>
			</div>
			</div>
			</div>
		 </div>
					<div class="row">
			<div class="col-sm-6 col-md-6 col-lg-6">
			<div class="box" >
				<div class="box-header with-border">
						<div id="filter_SP_pie_div" align="left"></div>
					</div>
				<div class="box-body">				
				<div class="row">
				<div class="col-md-12" id="dashboard_SP_div">
					<div id="spDiv1" style="overflow-x:hidden; overflow-y:hidden">
						<div class="row" >
							  <div class="col-md-12">
							  <div id="chart_SP_pie_div"></div>
							  </div>
						</div>
					</div>
				</div>
			</div>
			</div>
			</div>
			</div>
		 
			<div class="col-sm-6 col-md-6 col-lg-6">
			<div class="box" >
				<div class="box-header with-border">
						<div id="filter_SP_bar_div" align="left"></div>
					</div>
				<div class="box-body">				
				<div class="row">
				<div class="col-md-12" id="dashboard_div">
					<div id="spDiv2" style="overflow-x:hidden; overflow-y:hidden">
					<div class="row" >
							  <div class="col-md-12">
							  <div id="chart_SP_bar_div"></div>
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
