
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<aside class="main-sidebar">
	<section class="sidebar">
		<ul class="sidebar-menu">
            <li><a href="${contextPath}/appdashboard"><i class="fa fa-link"></i> <span>Home</span></a></li>
            <li class="treeview active">
              <a href="#"><i class="fa fa-link"></i> <span>Management</span> <i class="fa fa-angle-left pull-right"></i></a>
              <ul class="treeview-menu">
             		<li><a href="${contextPath}/employee/home"><i class="fa fa-user"></i> Employee</a></li>
              		<li><a href="${contextPath}/user/home"><i class="fa fa-users"></i> Users</a></li>
              		<li><a href="${contextPath}/agent/details" ><i class="fa fa-user"></i> Marketing agents</a></li>
					<li><a href="${contextPath}/product/details"><i	class="fa fa-cubes"></i> Product</a></li>
					<li><a href="${contextPath}/vendor/details" ng-click="getVendorList();"><i	class="fa fa-industry"></i> Vendor</a></li>
					<li><a href="${contextPath}/grades/details"> <i class="fa fa-sitemap"></i> Grade</a></li>
					<li><a href="${contextPath}/warehouse/details">	<i class="fa fa-database"></i> Warehouse</a></li>
					
				</ul>
            </li>
             <li class="treeview active">
              <a href="#"><i class="fa fa-link"></i> <span>Transaction</span> <i class="fa fa-angle-left pull-right"></i></a>
              <ul class="treeview-menu">
             		 <li><a href="${contextPath}/purchase/details"><i	class="fa fa-circle-o"></i>Purchase</a></li>
					<li><a href="${contextPath}/stock/details"><i	class="fa fa-circle-o"></i>Stock</a></li>
					<li><a href="${contextPath}/order/home"><i	class="fa fa-circle-o"></i>Order</a></li>
					<li><a href="${contextPath}/invoice/home"><i	class="fa fa-circle-o"></i>Invoice</a></li>
					
				</ul>
            </li>
          </ul>
        </section>
</aside>


