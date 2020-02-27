<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta charset="utf-8">
<meta name="description" content="">
<meta name="viewport" content="width=device-width, height=device-height">

<spring:url value="/resources/css/bootstrap.min.css"
	var="resourceBootstrapCSSUrl" />
<spring:url value="/resources/css/font-awesome.min.css"
	var="resourceFontAwesomeCSSUrl" />
<spring:url value="/resources/css/AdminLTE.min.css"
	var="resourceAdminLTECSSUrl" />
<spring:url value="/resources/css/jquery-jvectormap-1.2.2.css"
	var="resourceJVectorCSSUrl" />
<spring:url value="/resources/css/ionicons.min.css"
	var="resourceIconeCSSUrl" />
<spring:url value="/resources/css/style.css" var="resourceStyleCSSUrl" />
<spring:url value="/resources/css/_all-skins.min.css"
	var="resourceStyleSkinCSSUrl" />

<spring:url
	value="/resources/css/font-awesome-4.3.0/css/font-awesome.min.css" 	var="resourceFontAwesomeCSSURL" />
<link rel="stylesheet" href="${resourceFontAwesomeCSSURL}">

<link rel="stylesheet" href="${resourceBootstrapCSSUrl}">
<link rel="stylesheet" href="${resourceStyleCSSUrl}">
<!-- Font-Awesome -->
<link rel="stylesheet" href="${resourceFontAwesomeCSSUrl}">
<!-- Ionicons -->
<link href="${resourceIconeCSSUrl}" rel="stylesheet" type="text/css" />
<!-- jvectormap -->
<link href="${resourceJVectorCSSUrl}" rel="stylesheet" type="text/css" />
<!-- Theme style -->
<link href="${resourceAdminLTECSSUrl}" rel="stylesheet" type="text/css" />
<link href="${resourceStyleSkinCSSUrl}" rel="stylesheet" type="text/css" />




<spring:url value="/resources/js/jQuery-2.1.4.min.js" 	var="resourceJqueryUrl" />
<spring:url value="/resources/js/bootstrap.min.js" 	var="resourceBootstrapUrl" />
<spring:url value="/resources/js/fastclick.min.js" 	var="resourceFastClickUrl" />
<spring:url value="/resources/js/app.min.js" var="resourceAppminUrl" />
<spring:url value="/resources/js/jquery.sparkline.min.js" 	var="resourceSparklineUrl" />
<spring:url value="/resources/js/jquery-jvectormap-1.2.2.min.js" 	var="resourceJvectorUrl" />
<spring:url value="/resources/js/jquery-jvectormap-world-mill-en.js" 	var="resourceJVectorJSUrl" />
<spring:url value="/resources/js/jquery.slimscroll.min.js" 	var="resourceSlimScrollUrl" />
<spring:url value="/resources/js/jquery.dataTables.min.js" 	var="resourceDatatableUrl" />
<spring:url value="/resources/js/dataTables.bootstrap.min.js" 	var="resourceDatatableBootStrapUrl" />

<spring:url value="/resources/css/jquery.toastmessage.css" var="resourceJqToastMessageCSSUrl"/>
<link href="${resourceJqToastMessageCSSUrl}" rel="stylesheet">

<link rel="stylesheet"	href='<c:url value="/resources/css/xeditable.css"></c:url>'>

<script src="${resourceJqueryUrl}" type="text/javascript"></script>
<script src="${resourceBootstrapUrl}" type="text/javascript"></script>
<script src="${resourceFastClickUrl}" type="text/javascript"></script>
<script src="${resourceAppminUrl}" type="text/javascript"></script>
<script src="${resourceSparklineUrl}" type="text/javascript"></script>
<script src="${resourceJvectorUrl}" type="text/javascript"></script>
<script src="${resourceJVectorJSUrl}" type="text/javascript"></script>
<script src="${resourceSlimScrollUrl}" type="text/javascript"></script>

<script src="${resourceDatatableUrl}" type="text/javascript"></script>
<script src="${resourceDatatableBootStrapUrl}" type="text/javascript"></script>

<spring:url value="/resources/js/jquery.toastmessage.js" var="resourceJqToastMessageJSUrl"/>
<script type="text/javascript" src="${resourceJqToastMessageJSUrl}"></script>

<script type="text/javascript" src='<c:url value="/resources/js/angular.min.js"></c:url>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/angular-route.min.js"></c:url>'></script>
<script src='<c:url value="/resources/js/xeditable.min.js"></c:url>'></script>


<script type="text/javascript">
	webContextPath = "${pageContext.request.contextPath}";
	$(document).ready(function() {

		$('#managementdiv').addClass('test');
		$('#plydiv').removeClass('test');
		$('#veneerdiv').removeClass('test');
		$('#hardwarediv').removeClass('test');

		$('#management').click(function() {
			$('#managementdiv').addClass('test');
			$('#plydiv').removeClass('test');
			$('#veneerdiv').removeClass('test');
			$('#hardwarediv').removeClass('test');
		});

		$('#plydiv').click(function() {
			$('#plydiv').addClass('test');
			$('#veneerdiv').removeClass('test');
			$('#hardwarediv').removeClass('test');
			$('#managementdiv').removeClass('test');

		});

		$('#veneer').click(function() {
			$('#veneerdiv').addClass('test');
			$('#plydiv').removeClass('test');
			$('#hardwarediv').removeClass('test');
			$('#managementdiv').removeClass('test');
		});

		$('#hardware').click(function() {
			$('#veneerdiv').removeClass('test');
			$('#plydiv').removeClass('test');
			$('#hardwarediv').addClass('test');
			$('#managementdiv').removeClass('test');
		});

		$('#addvendor').click(function() {
			$('#addvendor').attr('data-toggle', 'modal');
			$('#addvendor').attr('data-target', '.addmodal');
			$('#vendorModalLabel').html('Add New Vendor');

			$.ajax({
				url : webContextPath + "/vendor/add",
				type : 'get',
				sucess : function(data) {
					$('#vendorbody').html(data);
				},
				failure : function(data, textStatus) {
					console.log("error");
				}
			})
		});

		$('#editvendor').click(function() {
			$('#editvendor').attr('data-toggle', 'modal');
			$('#editvendor').attr('data-target', '.addmodal');
			$('#vendorModalLabel').html('Edit Vendor');
		})

	});
	
	function showToastSuccessMessage(message){
		console.log('...show message...');
		 $().toastmessage('showToast', {
	         text     : message,
	         sticky   : false,
	         position : 'middle-center',
	         type     : 'success',
	         closeText: '',
	         close    : function () {
	             console.log("toast is closed ...");
	         }
	     });
	}

	function showToastErrorMessage(message){
		 $().toastmessage('showToast', {
	         text     : message,
	         sticky   : false,
	         position : 'middle-center',
	         type     : 'error',
	         closeText: '',
	         close    : function () {
	             console.log("toast is closed ...");
	         }
	     });
	}

</script>

<style>
thead {
		background-color: #bfbfbf;
		color: White;
	    height: 20px;
	}
a div.dashboardtab {
	position: relative;
	text-decoration: none;
	display: block;
	text-align: center;
	-webkit-transition: all .1s ease;
	-moz-transition: all .1s ease;
	-ms-transition: all .1s ease;
	-o-transition: all .1s ease;
	transition: all .1s ease;
}

.test {
	background-color: #E6E906;
	position: relative;
	color: #2E2E2E;
	font-size: large;
}

.active1 {
	position: relative;
	background-color: #E6E6E6;
	color: #2E2E2E;
}
</style>
</head>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
        <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
            <form role="search" class="navbar-form-custom" action="search_results.html">
                <div class="form-group">
                    <input type="text" placeholder="Search for something..." class="form-control" name="top-search" id="top-search">
                </div>
            </form>
        </div>
            <ul class="nav navbar-top-links navbar-right">
                <li>
                    <span class="m-r-sm text-muted welcome-message">Welcome to INSPINIA+ Admin Theme.</span>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-envelope"></i>  <span class="label label-warning">16</span>
                    </a>
                    <ul class="dropdown-menu dropdown-messages">
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="img/a7.jpg">
                                </a>
                                <div class="media-body">
                                    <small class="pull-right">46h ago</small>
                                    <strong>Mike Loreipsum</strong> started following <strong>Monica Smith</strong>. <br>
                                    <small class="text-muted">3 days ago at 7:58 pm - 10.06.2014</small>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="img/a4.jpg">
                                </a>
                                <div class="media-body ">
                                    <small class="pull-right text-navy">5h ago</small>
                                    <strong>Chris Johnatan Overtunk</strong> started following <strong>Monica Smith</strong>. <br>
                                    <small class="text-muted">Yesterday 1:21 pm - 11.06.2014</small>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="img/profile.jpg">
                                </a>
                                <div class="media-body ">
                                    <small class="pull-right">23h ago</small>
                                    <strong>Monica Smith</strong> love <strong>Kim Smith</strong>. <br>
                                    <small class="text-muted">2 days ago at 2:30 am - 11.06.2014</small>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="mailbox.html">
                                    <i class="fa fa-envelope"></i> <strong>Read All Messages</strong>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell"></i>  <span class="label label-primary">8</span>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        <li>
                            <a href="mailbox.html">
                                <div>
                                    <i class="fa fa-envelope fa-fw"></i> You have 16 messages
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="profile.html">
                                <div>
                                    <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small">12 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="grid_options.html">
                                <div>
                                    <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="notifications.html">
                                    <strong>See All Alerts</strong>
                                    <i class="fa fa-angle-right"></i>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>


                <li>
                    <a href="login.html">
                        <i class="fa fa-sign-out"></i> Log out
                    </a>
                </li>
                <li>
                    <a class="right-sidebar-toggle">
                        <i class="fa fa-tasks"></i>
                    </a>
                </li>
            </ul>

        </nav>
        </div>
<body class="skin-blue sidebar-mini">
	<header class="main-header" style="background-color: white">
		<%--   <div class="row">
		        <div class="col-md-1">
		       		<img src="${contextPath}/resources/images/saibaba.jpg" style="width:142%" /> 
		       		
		        </div>
		 	  <div class="col-md-11">
					 <img src="${contextPath}/resources/images/plywood1.jpg" width="100%" height="122px"/> 
		   </div> --%>
		<div class="container">
			<h1>Spring BOOT APP</h1>
			<h5><%=session.getAttribute("username") %></h5>
		</div>
		
  <div class="container-fluid">
    <nav class="navbar navbar-default">
      <div class="container">
        <div class="collapse navbar-collapse" id="navbar-collapse-1">
          <ul class="nav navbar-nav navbar-left">
              <li class="active"><a href="${contextPath}/">Home</a></li>
						<li><a class="dropdown-toggle" href="#"
							data-toggle="dropdown">Administration<i class="icon-chevron-down"></i></a>
							<ul class="dropdown-menu pull-left">
								<li><a href="${contextPath}/product/details"><i	class="fa fa-circle-o"></i>Product</a></li>
								<li><a href="${contextPath}/vendor/details"><i	class="fa fa-circle-o"></i>Vendor</a></li>		
								<li><a href="${contextPath}/grades/details"><i	class="fa fa-circle-o"></i>Grade</a></li>		
								<li class="treeview"><a href="${contextPath}/stock/details">
										<i class="fa fa-circle-o"></i> <span>Stock</span> <!--  Display All Warehouse -->
								</a></li>
								<li class="treeview"><a href="${contextPath}/warehouse/details">
										<i class="fa fa-circle-o"></i> <span>Warehouse</span> <!--  Display All Warehouse -->
								</a></li>
							</ul>
				</li>
				
				<li><a  href="${contextPath}/order/home">Order Management</a>
				</li>
				
				<li><a class="dropdown-toggle" href="#"
							data-toggle="dropdown">Transaction<i class="icon-chevron-down"></i></a>
							<ul class="dropdown-menu pull-left">
								<li><a href="${contextPath}/transaction/stock"><i
										class="fa fa-circle-o"></i>Stock Management</a></li>
								<li class="treeview"><a href="${contextPath}/transaction/warehouse">
										<i class="fa fa-circle-o"></i> <span>Warehouse Movement</span> 
								</a></li>
							</ul>
				</li>
						
				
          </ul>
        </div>
      </div>
    </nav>
    </div>
	</header>
	
	
</body>
</html>