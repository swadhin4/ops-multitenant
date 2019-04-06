<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ page session="false"%>
<html ng-app="chrisApp">
<head>
<title>Home</title>
</head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>
<%--  <script type="text/javascript" src='<c:url value="/resources/theme1/chart/loader.js"></c:url>'></script>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.3.0/bootbox.min.js"></script>
 
 
  --%>
<script type="text/javascript" src='<c:url value="/resources/theme1/js/dashboard.js?n=${System.currentTimeMillis()  + UUID.randomUUID().toString()}"></c:url>'></script>
<style>
.tree, .tree ul {
    margin:-9px;
    padding:0;
    list-style:none
}
.tree ul {
    margin-left:1em;
    position:relative
}
.tree ul ul {
    margin-left:.5em
}
.tree ul:before {
    content:"";
    display:block;
    width:0;
    position:absolute;
    top:0;
    bottom:0;
    left:0;
    border-left:1px solid
}
.tree li {
    margin:0;
    padding:0 1em;
    line-height:2em;
    color:#369;
    font-weight:700;
    position:relative
}
.tree ul li:before {
    content:"";
    display:block;
    width:10px;
    height:0;
    border-top:1px solid;
    margin-top:-1px;
    position:absolute;
    top:1em;
    left:0
}
.tree ul li:last-child:before {
    background:#fff;
    height:auto;
    top:1em;
    bottom:0
}
.indicator {
    margin-right:5px;
}
.tree li a {
    text-decoration: none;
    color:#369;
}
.tree li button, .tree li button:active, .tree li button:focus {
    text-decoration: none;
    color:#369;
    border:none;
    background:transparent;
    margin:0px 0px 0px 0px;
    padding:0px 0px 0px 0px;
    outline: 0;
}
 </style>

<div class="content-wrapper" >
<div  ng-controller="dashboardController">
			<section class="content">
			
	<div class="box" >
		<div class="box-header with-border">
		<h3 class="box-title">Summarized View of Site and Service Providers Incidents</h3>
		</div>
			<div class="box-body">	
			    <div class="row">
			        <!-- <div class="col-md-4">
			            <ul id="tree1" ><a href="#">
			                <li ng-repeat="site in  opsEntities">Site : {{site.siteName}}</a>
			                    <ul>
			                        <li ng-repeat="asset in site.assetList">Asset: {{asset.assetName}}
			                            <ul>
			                                <li>Incidents
			                                    <ul>
			                                        <li ng-repeat="incident in asset.incidentList">{{incident.ticketName}}</li>
			                                    </ul>
			                                </li>
			                            </ul>
			                        </li>
			                    </ul>
			                </li>
			            </ul>
			        </div> -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-aqua">
            <div class="inner">
              <h3>150</h3>

              <p>Sites</p>
            </div>
            <div class="icon">
              <i class="fa fa-sitemap" style="    margin-top: 16px;"></i>
            </div>
            <a href="#" class="small-box-footer">
              More info <i class="fa fa-arrow-circle-right"></i>
            </a>
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-green">
            <div class="inner">
              <h3>53<sup style="font-size: 20px"></sup></h3>

              <p>Assets</p>
            </div>
            <div class="icon">
              <i class="fa fa-cubes " style="    margin-top: 16px;"></i>
            </div>
            <a href="#" class="small-box-footer">
              More info <i class="fa fa-arrow-circle-right"></i>
            </a>
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-yellow">
            <div class="inner">
              <h3>44</h3>

              <p>User Registrations</p>
            </div>
            <div class="icon">
              <i class="fa fa-users " style="    margin-top: 16px;"></i>
            </div>
            <a href="#" class="small-box-footer">
              More info <i class="fa fa-arrow-circle-right"></i>
            </a>
          </div>
        </div>
        <!-- ./col -->
        <div class="col-lg-3 col-xs-6">
          <!-- small box -->
          <div class="small-box bg-red">
            <div class="inner">
              <h3>65</h3>

              <p>Service Provider</p>
            </div>
            <div class="icon">
              <i class="fa fa-building " style="    margin-top: 16px;"></i>
            </div>
            <a href="#" class="small-box-footer">
              More info <i class="fa fa-arrow-circle-right"></i>
            </a>
          </div>
        </div>
        <!-- ./col -->
      </div>
		
		  </div>
	</div>
		<div class="box" >
		<div class="box-header with-border">
		<h3 class="box-title">Application File Attachments</h3>
		</div>
		 <div class="box-body table-responsive no-padding">
		 	<div style="display:none" id="loadingDiv"><div class="loader">Loading...</div></div>
			<table class="table table-hover">
				<tbody style="font-size: .9em">
					<tr">
						<th style="width: 12%">Filename</th>
						<th style="width: 10%">Size</th>
						<th style="width: 10%">Last Modified</th>
					</tr>
					<tr ng-repeat="val in attachments">
						<td>{{val.imagePath}}</td>
						<td>{{val.imageSize}}</td>
						<td>{{val.creationDate}}</td>
					</tr>
				</tbody>
			</table>
		</div>
			</div>
		<!-- 	<div class="row">
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
		 </div> -->
		<!-- 			<div class="row">
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
		 </div> -->

			</section>
				</div>
		</div>
