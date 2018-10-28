<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html ng-app="chrisApp">
<head>
<title>OPS365 Application</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='0'>
<meta http-equiv='pragma' content='no-cache'>

<link rel="icon" type="image/png" sizes="16x16" href='<c:url value="/resources/img/favicon/favicon-16x16.png"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/bootstrap.min.css"></c:url>' /> 
<link rel="stylesheet" 	href='<c:url value="/resources/css/ionicons/css/ionicons.min.css"></c:url>'/>

<%-- <link rel="stylesheet"	href='<c:url value="/resources/css/jquery-jvectormap-1.2.2.css"></c:url>' /> --%>

<link rel="stylesheet"	href='<c:url value="/resources/theme1/font-awesome-4.7.0/css/font-awesome.min.css"></c:url>' />
<%-- <link rel="stylesheet"	href='<c:url value="/resources/theme1/css/ripples.min.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/main.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/responsive.css"></c:url>' /> --%>
<link rel="stylesheet"	href='<c:url value="/resources/theme1/css/animate.min.css"></c:url>' />

<link rel="stylesheet" href='<c:url value="/resources/theme1/css/jquery-editable-select.min.css"></c:url>' />
<link rel="stylesheet" href='<c:url value="/resources/theme1/css/datepicker3.css"></c:url>' />
<%-- <link rel="stylesheet" href='<c:url value="/resources/css/bootstrap-datetimepicker.min.css"></c:url>' /> --%>


<link rel="stylesheet"	href='<c:url value="/resources/dist/css/pms-admintLTE.css"></c:url>' />
<link rel="stylesheet" href='<c:url value="/resources/theme1/css/style4.css"></c:url>' />
<%-- <link rel="stylesheet"	href='<c:url value="/resources/theme1/css/style.css"></c:url>' />
<link rel="stylesheet"	href='<c:url value="/resources/dist/css/skins/skin-red.min.css"></c:url>' /> --%>
<link rel="stylesheet" href='<c:url value="/resources/theme1/css/bootstrap-multiselect.css"></c:url>' />

 <script type="text/javascript" src='<c:url value="/resources/theme1/chart/jsapi.js"></c:url>'></script>
 <!-- <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script> -->

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jquery-2.1.4.min.js"></c:url>'></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script> -->
<script type="text/javascript"  src='<c:url value="/resources/theme1/js/bootstrap-datepicker.js"></c:url>'></script>

<script type="text/javascript"  src='<c:url value="/resources/theme1/js/jquery-editable-select.js"></c:url>'></script>

<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/tether.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/bootstrap.min.js"></c:url>'></script>
<script type="text/javascript" 	src='https://adminlte.io/themes/AdminLTE/bower_components/fastclick/lib/fastclick.js'></script>
<script type="text/javascript"  src='<c:url value="/resources/theme1/js/bootstrap-multiselect.js"></c:url>'></script>

<script type="text/javascript" 	src='<c:url value="/resources/dist/js/adminlte.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/dist/js/app.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/angular.min.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/jstorage.js"></c:url>'></script>
<script type="text/javascript" 	src='<c:url value="/resources/theme1/js/app-main.js"></c:url>'></script>
<script type="text/javascript" src="<c:url value="/resources/theme1/js/language-controller.js"></c:url>"></script>
<script type="text/javascript" src="<c:url value="/resources/theme1/js/language-service.js"></c:url>"></script>
<script type="text/javascript" src="<c:url value="/resources/theme1/js/moment.min.js"></c:url>"></script>

<script type="text/javascript" src='<c:url value="/resources/js/sockjs.min.js"></c:url>'></script>
 <script type="text/javascript" src='<c:url value="/resources/js/stomp.min.js"></c:url>'></script>
 <script type="text/javascript" src="<c:url value="/resources/theme1/pms/websocket.js"></c:url>"></script>
 



<style>
	thead {
		background-color: #bfbfbf;
		color: White;
	    height: 20px;
	}
	 .alert-success {
	
   			display:table;
    		background-color: rgb(122, 221, 0) !important;
    		border-color:#fff !important
    		
    }
    .alert-danger {
      	display:table;
   		 background-color: rgba(237, 73, 10, 0.92) !important;
   		 border-color:#fff !important
    		
    }
   
    .alert{
        width: 100%;
    	margin-top: -33px;
        padding: 7px;
            margin-bottom: 0px;
            border: 0px solid transparent !important;
    }
    .alert .close{
        color: #000 !important;
   		 opacity: 2.2 !important;
    }
   .alert-dismissable .close {
    position: relative;
    top: -2px;
    color: inherit;
      margin-right: 19px;
} 

.navbar-custom-menu .nav>li>a>.label {
   position: absolute;
    top: 7px;
    right: 4px;
    text-align: center;
    font-size: 14px;
    padding: 4px 5px;
    line-height: .9;
}

.content {
    min-height: 90px;
    padding: 0px;
    margin-right: auto;
    margin-left: auto;
    padding-left: 15px;
    padding-right: 15px;
}


.loader,
        .loader:after {
            border-radius: 50%;
            width: 10em;
            height: 10em;
        }
        .loader {            
            margin: 250px auto;
            font-size: 10px;
            position: relative;
            text-indent: -9999em;
            border-top: 1.1em solid rgba(255, 255, 255, 0.2);
            border-right: 1.1em solid rgba(255, 255, 255, 0.2);
            border-bottom: 1.1em solid rgba(255, 255, 255, 0.2);
            border-left: 1.1em solid #ffffff;
            -webkit-transform: translateZ(0);
            -ms-transform: translateZ(0);
            transform: translateZ(0);
            -webkit-animation: load8 1.1s infinite linear;
            animation: load8 1.1s infinite linear;
        }
        @-webkit-keyframes load8 {
            0% {
                -webkit-transform: rotate(0deg);
                transform: rotate(0deg);
            }
            100% {
                -webkit-transform: rotate(360deg);
                transform: rotate(360deg);
            }
        }
        @keyframes load8 {
            0% {
                -webkit-transform: rotate(0deg);
                transform: rotate(0deg);
            }
            100% {
                -webkit-transform: rotate(360deg);
                transform: rotate(360deg);
            }
        }
        #loadingDiv, #loadingDiv1, #loadingDiv2 , #loadingDiv3, #loadingDiv4, #loadingDiv5{
            position:absolute;;
            top: 52px;
   			 left: 0px;
            width:100%;
            height:100%;
            background-color:#000;
              opacity: 0.7;
   z-index: 9999;
   text-align: center;
        }

</style>
<script type="text/javascript">
/* $(window).on('load', function(){
  setTimeout(removeLoader, 2000); //wait for page load PLUS two seconds.
}); */
var $ = jQuery.noConflict(); 
$(function () {
	 $('i').each(function (i, e) {
		    var label;
		    switch ($(e).attr('id')) {
		        case 'dashboard':
		            label = 'Dashboard';
		            break;
		        case 'user':
		            label = 'User';
		            break;
		        case 'site':
		            label = 'Site';
		            break;
		        case 'asset':
		            label = 'Asset or Service';
		            break;
		        case 'sp':
		            label = 'Service Provider';
		            break;
		        case 'inc':
		            label = 'Incident';
		            break;
		        case 'rep':
		            label = 'Report';
		            break;
		    }
		    $(e).tooltip({ 'trigger': 'focus', 'title': label });
		});
	 
	 $('.goog-te-combo').val('hi');
	 
	// loadEngTranslators();
	// loadFrTranslators();
	// setApplicationLanguage('en');
	})
function removeLoader(){
    $( "#loadingDiv" ).fadeOut(500, function() {
      // fadeOut complete. Remove the loading div
     // $( "#loadingDiv" ).remove(); //makes page more lightweight 
  });  
} 
 /* function loadEngTranslators(){
	 $.ajax({
		 url:hostLocation+"/resources/theme1/json/language_en.json",
		 success:function(data){
			 console.log(data);
			 $.jStorage.set("eng", data);
		 }
	 })
 }   
 function loadFrTranslators(){
	 $.ajax({
		 url:hostLocation+"/resources/theme1/json/language_fr.json",
		 success:function(data){
			 console.log(data);
			 $.jStorage.set("french", data);
		 }
     });   
}
 function setApplicationLanguage(appLanguage){
	var frVersion =  $.jStorage.get("french");
	var engVersion =  $.jStorage.get("eng");
	console.log(frVersion);
	console.log(frVersion);
	 if(appLanguage=="fr"){
		 loadEngTranslators();
	 	$.jStorage.set("selectedLanguage", frVersion);
	 }else{
		 loadEngTranslators();
		 $.jStorage.set("selectedLanguage", engVersion);
	 }
	
 } */
</script>
</head>

<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
	 <header class="main-header">
	 <a href class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"> <img src="${contextPath}/resources/img/logo.png" style="width: 100%;"></span>
      <span class="logo-lg"><img src="${contextPath}/resources/img/sigma.png" style="width: 100%; margin-top: 0px;"></span>
    </a>
    <nav class="navbar navbar-static-top">
		<a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
            
      </a>
      
      <div >
        <ul class="nav navbar-nav">
        <li class="dropdown notifications-menu">
        <a><span >For support enquiries contact: info@sigmasurge.com</span></a> 
        </li>
         
        </ul>
     	
        </div>
      <div class="navbar-custom-menu" ng-controller="languageController" >
        <ul class="nav navbar-nav">
          <!-- <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            	<i class="fa fa-language fa-2x" aria-hidden="true"></i> {{ language }}
            </a>
            <ul class="dropdown-menu">
              <li> <a href="javascript:void(0);" ng-click="changeLanguage('en')">English</a></li>
              <li> <a href="javascript:void(0);" ng-click="changeLanguage('fr')">French</a> </li>
            </ul>
          </li> -->
          <li class="dropdown notifications-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-bell-o"></i>
              <span class="label label-warning" id="msgCountElmId">0</span>
            </a>
            <ul class="dropdown-menu">
              <li class="header">You have <span id="msgCountElmId2">0</span> notifications</li>
              <li>
                <!-- inner menu: contains the actual data -->
               <ul class="menu" id="msgDropdown">
                </ul>
              </li>
              <li class="footer"><a href="#">View all</a></li>
            </ul>
          </li>
          <!-- User Account: style can be found in dropdown.less -->
          <li class="dropdown user user-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <img src="${contextPath}/resources/img/swadhin.jpg" class="user-image" alt="User Image">
              <span class="hidden-xs">${user.firstName}  ${user.lastName}</span>
            </a>
            <ul class="dropdown-menu">
              <!-- User image -->
              <li class="user-header">
                <img src="${contextPath}/resources/img/swadhin.jpg" class="img-circle" alt="User Image">
                <p style="color: #000">
                  ${user.firstName}  ${user.lastName}
                  <small> ${user.company.companyName}</small>
                  <c:forEach items="${user.userRoles}" var="roles">
                  <small style="color: #000">${roles.role.roleName}</small>
                     </c:forEach>
                </p>
              </li>
              <!-- Menu Body -->
              <!-- Menu Footer-->
              <li class="user-footer">
                <div class="pull-left">
                  <a href="${contextPath}/user/profile" class="btn btn-default btn-flat">Profile</a>
                </div>
                <div class="pull-right">
                 <sec:authorize access="isAuthenticated()">
                  <a href="${contextPath}/logout" class="btn btn-default btn-flat">Sign out</a>
                  </sec:authorize>
                </div>
              </li>
            </ul>
          </li>
          <!-- Control Sidebar Toggle Button -->
         <!--  <li>
            <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
          </li> -->
        </ul>
      </div>
    </nav>
	</nav>
	</header>
    <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- Sidebar user panel -->
      <div class="user-panel">
        <div class="pull-left image">
          <img src="${contextPath}/resources/img/swadhin.jpg" class="img-circle" alt="User Image">
        </div>
        <div class="pull-left info">
          <p >${user.firstName}  ${user.lastName}</p>
         
        </div>
      </div>
     <c:if test="${user.sysPassword eq 'NO'}">
      <!-- sidebar menu: : style can be found in sidebar.less -->
      <ul class="sidebar-menu" data-widget="tree" id="tooltip-ex">
        <!-- <li class="header">MAIN NAVIGATION</li> -->
       
        <li>
          <a href="${contextPath}/appdashboard"  >
            <i class="fa fa-dashboard" data-toggle="tooltip" data-placement="right" data-container="body"  id="dashboard"></i> <span >Dashboard</span>
          </a>
        </li>
         <sec:authorize access="hasAnyRole('ROLE_SP_ADMIN')">
    	 <li>
          <a href="${contextPath}/user/details" >
            <i class="fa fa-users" aria-hidden="true" data-toggle="tooltip" data-placement="right"  id="user" ></i> <span>SP User</span>
          </a>
        </li>
        </sec:authorize>
        <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
    	 <li>
          <a href="${contextPath}/user/details" >
            <i class="fa fa-users" aria-hidden="true" data-toggle="tooltip" data-placement="right"  id="user" ></i> <span>User</span>
          </a>
        </li>
        </sec:authorize>
        <sec:authorize access="hasAnyRole('ROLE_SITE_STAFF','ROLE_MAINTENANCE_STAFF','ROLE_OPS_MANAGER')">
        <li>
          <a href="${contextPath}/site/details" ><i class="fa fa-sitemap" aria-hidden="true" data-toggle="tooltip" data-placement="right"  id="site"></i>
             <span>Site</span>
          </a>
        </li>
        
        <li>
          <a href="${contextPath}/serviceprovider/details" >
            <i class="fa fa-building" aria-hidden="true" data-toggle="tooltip" data-placement="right"  id="sp" data-container="body"></i> <span>Service Provider</span>
          </a>
        </li>
        
           <li>
          <a href="${contextPath}/asset/details" >
            <i class="fa fa-cubes" aria-hidden="true" data-toggle="tooltip" data-placement="right"  id="asset" data-container="body"></i> <span>Asset or Service</span>
          </a>
        </li>           
        
          <li>
          <a href="${contextPath}/incident/details" ><i class="fa fa-ticket" aria-hidden="true" data-toggle="tooltip" data-placement="right"  id="inc"></i> 
            <span>Incident</span>
          </a>
        </li>
        <li>
          <a href="${contextPath}/reports/view" >
            <i class="fa fa-newspaper-o" aria-hidden="true" data-toggle="tooltip" data-placement="right"  id="rep"></i> <span>Reports</span>
          </a>
        </li>
       </sec:authorize>
      </ul>
      </c:if>
    </section>
  </aside>
  <!-- 
    <aside class="control-sidebar control-sidebar-dark">
    Create the tabs
    <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
      <li><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
      <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
    </ul>
    Tab panes
    <div class="tab-content">
      Home tab content
      <div class="tab-pane  active" id="control-sidebar-home-tab">
        <h3 class="control-sidebar-heading">Recent Activity</h3>
        <ul class="control-sidebar-menu">
          <li>
            <a href="javascript:void(0)">
              <i class="menu-icon fa fa-birthday-cake bg-red"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>

                <p>Will be 23 on April 24th</p>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <i class="menu-icon fa fa-user bg-yellow"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Frodo Updated His Profile</h4>

                <p>New phone +1(800)555-1234</p>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <i class="menu-icon fa fa-envelope-o bg-light-blue"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Nora Joined Mailing List</h4>

                <p>nora@example.com</p>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <i class="menu-icon fa fa-file-code-o bg-green"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Cron Job 254 Executed</h4>

                <p>Execution time 5 seconds</p>
              </div>
            </a>
          </li>
        </ul>
        /.control-sidebar-menu

        <h3 class="control-sidebar-heading">Tasks Progress</h3>
        <ul class="control-sidebar-menu">
          <li>
            <a href="javascript:void(0)">
              <h4 class="control-sidebar-subheading">
                Custom Template Design
                <span class="label label-danger pull-right">70%</span>
              </h4>

              <div class="progress progress-xxs">
                <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <h4 class="control-sidebar-subheading">
                Update Resume
                <span class="label label-success pull-right">95%</span>
              </h4>

              <div class="progress progress-xxs">
                <div class="progress-bar progress-bar-success" style="width: 95%"></div>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <h4 class="control-sidebar-subheading">
                Laravel Integration
                <span class="label label-warning pull-right">50%</span>
              </h4>

              <div class="progress progress-xxs">
                <div class="progress-bar progress-bar-warning" style="width: 50%"></div>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <h4 class="control-sidebar-subheading">
                Back End Framework
                <span class="label label-primary pull-right">68%</span>
              </h4>

              <div class="progress progress-xxs">
                <div class="progress-bar progress-bar-primary" style="width: 68%"></div>
              </div>
            </a>
          </li>
        </ul>
        /.control-sidebar-menu

      </div>
      /.tab-pane

      Settings tab content
      <div class="tab-pane" id="control-sidebar-settings-tab">
        <form method="post">
          <h3 class="control-sidebar-heading">General Settings</h3>

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Report panel usage
              <input type="checkbox" class="pull-right" checked>
            </label>

            <p>
              Some information about this general settings option
            </p>
          </div>
          /.form-group

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Allow mail redirect
              <input type="checkbox" class="pull-right" checked>
            </label>

            <p>
              Other sets of options are available
            </p>
          </div>
          /.form-group

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Expose author name in posts
              <input type="checkbox" class="pull-right" checked>
            </label>

            <p>
              Allow the user to show his name in blog posts
            </p>
          </div>
          /.form-group

          <h3 class="control-sidebar-heading">Chat Settings</h3>

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Show me as online
              <input type="checkbox" class="pull-right" checked>
            </label>
          </div>
          /.form-group

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Turn off notifications
              <input type="checkbox" class="pull-right">
            </label>
          </div>
          /.form-group

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Delete chat history
              <a href="javascript:void(0)" class="text-red pull-right"><i class="fa fa-trash-o"></i></a>
            </label>
          </div>
          /.form-group
        </form>
      </div>
      /.tab-pane
    </div>
  </aside> -->
  <!-- /.control-sidebar -->
  <!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
  <div class="control-sidebar-bg"></div>

