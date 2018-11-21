chrisApp.controller('spCustomerController', [
		'$rootScope',
		'$scope',
		'$filter',
		'$location',
		'userService',
		'companyService',
		'registrationService',
		function($rootScope, $scope, $filter, $location, userService,
				authService, companyService, registrationService, roleService,
				siteService) {

			$scope.spCustomerList = {
				selected : {},
				list : []
			}

			angular.element(document).ready(
					function() {
						
						
						
						
						$('#loadingDiv').show();

						userService.getSPCustomerList().then(
								function(data) {
									console.log(data);
									debugger
									if (data.statusCode == 200) {
										$scope.spCustomerList.list = [];
										debugger
										if (data.object.length > 0) {
											$.each(data.object, function(key,
													val) {
												$scope.spCustomerList.list
														.push(val);
											});
										}
										debugger
										console.log("spCustomerList",
												$scope.spCustomerList.list);
										$('#loadingDiv').hide();
										debugger
										populateSPCustomerDataTable(
												$scope.spCustomerList.list,
												'customerDetails');
										
										/*var table = $('#customerDetails').DataTable( {
									        responsive: true
									    } );
									 
									    new $.fn.dataTable.FixedHeader( table );*/
									}

								}, function(data) {
									console.log(data);
								});
					});

		} ]);
function populateSPCustomerDataTable(data, tableDivName) {
	debugger
	console.log("populateSPCustomerDataTable", data);
	$('#' + tableDivName).dataTable(
			{
				"aaData" : data,
				"bAutoWidth": false,
				"order" : [ [ 0, "desc" ] ],
				"aoColumns" : [
						{
							"sTitle" : "SP Customer Code",
							"mData" : "customerCode",
							"sClass" : "hidden"

						},
						{
							"sTitle" : "SP Customer ID",
							"mData" : "customerId",
							"sClass" : "hidden"

						},
						{
							"sTitle" : "SP Customer DB",
							"mData" : "custDBName",
							"sClass" : "hidden"

						},{
							"sTitle" : "",
							"mData" : "icon",
							"className": "text-center",
							"render" : function(data, type, row) {
								return '<a data-id="'
										+ row[0] + '"><i class="fa fa-user" style="font-size:48px;color:green"></i></a>'
							}
						},
						{
							"sTitle" : "Customer Name",
							"mData" : "customerName"
						},
						{
							"sTitle" : "Country Name",
							"mData" : "countryName"
						},
						{
							"sTitle" : "Action",
							"mData" : "status",
							"className": "text-center",
							"render" : function(data, type, row) {
								return '<a href style="color:#07ab07" title="View Incident" data-id="'
										+ row[0] + '"><i class="fa fa-eye" style="font-size:30px;color:blue"></i></a>'
							}
						} ]
			});
}
