chrisApp
		.controller(
				'spCustomerController',
				[
						'$rootScope',
						'$scope',
						'$filter',
						'$location',
						'userService',
						'companyService',
						'registrationService',
						function($rootScope, $scope, $filter, $location,
								userService, authService, companyService,
								registrationService, roleService, siteService) {

							$scope.spCustomerList = {
								selected : {},
								list : []
							}

							$scope.spCustomerIncidentList = {
								selected : {},
								list : []
							}

							angular
									.element(document)
									.ready(
											function() {

												$('#loadingDiv').show();

												userService
														.getSPCustomerList()
														.then(
																function(data) {
																	console
																			.log(data);
																	debugger
																	if (data.statusCode == 200) {

																		$scope.spCustomerList.list = [];
																		$(
																				"#spCustomerListSelect")
																				.empty();

																		debugger
																		if (data.object.length > 0) {
																			$
																					.each(
																							data.object,
																							function(
																									key,
																									val) {
																								var custList = {
																									custId : val.customerId,
																									custCode : val.customerCode,
																									custName : val.customerName,
																									custDBName : val.custDBName,
																									countryName : val.countryName
																								}
																								$scope.spCustomerList.list
																										.push(custList);
																							});
																			// console.log(spCustomerList);
																			var options = $("#spCustomerListSelect");
																			console
																					.log(options);
																			options
																					.append($(
																							"<option />")
																							.val(
																									"")
																							.text(
																									"Select Customer"));
																			$
																					.each(
																							$scope.spCustomerList.list,
																							function() {
																								options
																										.append($(
																												"<option />")
																												.val(
																														this.custCode)
																												.text(
																														this.custName));
																							});
																			$(
																					'#loadingDiv')
																					.hide();
																		}

																		/*
																		 * var
																		 * table =
																		 * $('#customerDetails').DataTable( {
																		 * responsive:
																		 * true } );
																		 * 
																		 * new
																		 * $.fn.dataTable.FixedHeader(
																		 * table );
																		 */
																	}

																},
																function(data) {
																	console
																			.log(data);
																});

												/*
												 * userService.getSPCustomerList().then(
												 * function(data) {
												 * console.log(data); debugger
												 * if (data.statusCode == 200) {
												 * $scope.spCustomerList.list =
												 * []; debugger if
												 * (data.object.length > 0) {
												 * $.each(data.object,
												 * function(key, val) {
												 * $scope.spCustomerList.list
												 * .push(val); }); } debugger
												 * console.log("spCustomerList",
												 * $scope.spCustomerList.list);
												 * $('#loadingDiv').hide();
												 * debugger
												 * populateSPCustomerDataTable(
												 * $scope.spCustomerList.list,
												 * 'customerDetails');
												 * 
												 * var table =
												 * $('#customerDetails').DataTable( {
												 * responsive: true } );
												 * 
												 * new
												 * $.fn.dataTable.FixedHeader(
												 * table ); }
												 *  }, function(data) {
												 * console.log(data); });
												 */
											});

							$scope.getCustomerIncident = function(cCode, e,
									spCustomerListSelect) {
								// this array will store selected customer list
								// details.
								$scope.selectedCustList = [];

								for (var i = 0; i < $scope.spCustomerList.list.length; i++) {
									if ($scope.spCustomerList.list[i].custCode === cCode.value) {
										$scope.selectedCustList
												.push($scope.spCustomerList.list[i]);
									}
								}

								console.log($scope.selectedCustList);

								// countryName
								if (cCode.value.length > 0) {
									$("#countryName")
											.val(
													$scope.selectedCustList[0].countryName);

									userService
											.getSPCustomerTicketList(
													$scope.selectedCustList[0].custCode,
													$scope.selectedCustList[0].custDBName)
											.then(
													function(data) {
														console
																.log(
																		"getSPCustomerTicketList----->",
																		data)
														if (data.statusCode == 200) {

															$scope.spCustomerIncidentList.list = [];
															debugger
															if (data.object.length > 0) {
																$
																		.each(
																				data.object,
																				function(
																						key,
																						val) {
																					$scope.spCustomerIncidentList.list
																							.push(val);
																				});
															}

															populateSPIncidentDataTable(
																	$scope.spCustomerIncidentList.list,
																	'incidentDetails');

															var table = $(
																	'#incidentDetails')
																	.DataTable(
																			{
																				responsive : true
																			});

															new $.fn.dataTable.FixedHeader(
																	table);

															$('#loadingDiv')
																	.hide();
														}
													},
													function(data) {
														console
																.log('Unable to change the status of the user')

													});

								} else {
									$("#countryName").val("");
								}

								/*$.each($scope.spCustomerList, function(key,val) {
									debugger
									if(val.custCode == cCode.value) {
										$scope.spCustomerList.selected = val;
										//console.log(spCustomerList);
										return false;
									}
								});*/

							}

						} ]);
function populateSPIncidentDataTable(data, tableDivName) {
	debugger
	console.log("populateSPIncidentDataTable", data);
	$('#' + tableDivName).dataTable({
		"aaData" : data,
		"bAutoWidth" : false,
		"order" : [ [ 0, "desc" ] ],
		"aoColumns" : [ {
			"sTitle" : "Ticket Number",
			"mData" : "ticketNumber"
		}, {
			"sTitle" : "Site Name",
			"mData" : "siteName"
		}, {
			"sTitle" : "Title",
			"mData" : "ticketTitle"
		}, {
			"sTitle" : "Created On",
			"mData" : "raisedOn"
		}, {
			"sTitle" : "Sla Due Date",
			"mData" : "sla"
		}, {
			"sTitle" : "Service Provider",
			"mData" : "assignedSP"
		}, {
			"sTitle" : "Status",
			"mData" : "status"
		} ]
	});
}
