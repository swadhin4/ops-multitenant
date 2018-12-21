chrisApp
		.controller(
				'spIncidentCreateController',
				 ['$rootScope', '$scope', '$filter','siteService','authService',
                  'siteCreationService','companyService','userService','districtService',
                  'areaService','clusterService','countryService','assetService',
                  'ticketCategoryService','ticketService','serviceProviderService','statusService',
        function  ($rootScope, $scope , $filter,siteService, authService,
                  siteCreationService,companyService,userService,districtService,
                  areaService,clusterService,countryService,assetService,
                  ticketCategoryService,ticketService,serviceProviderService,statusService) {

							$scope.ticketData = {};
							
							$scope.ticket={
									 selected:{},
									 list:[]
								}
							
							
							$scope.assetTypechecked = 'undefined';
							$scope.selectedSite={};
							$scope.statusList=[];
							$scope.selectedCategory={};
							
							$scope.selectedCategory={
									 selected:{},
									 list:[]
							 }
							
							$scope.sessionUser={};
							
							$scope.assetCategory={
									equipmentList:[],
									serviceList:[]
							}
			    			
							$scope.accessSite={
									list:[],
									selected:{}
							}
							 $scope.repairType={
									 selected:{},
									 list:[]
							 }
							 $scope.subRepairType={
									 selected:{},
									 list:[]					 
									 
							 }
							 $scope.serviceRepairType={
									 selected:{},
									 list:[]
							 }
							 $scope.serviceSubRepairType={
									 selected:{},
									 list:[]					 
									 
							 }
							
							var viewMode = null;
							angular.element(document).ready(function(){
								//console.log("loaded");
								$scope.initalizeCloseDiv();	
								viewMode = $('#mode').val();
								$scope.getLoggedInUserAccess();	
								$scope.selectedTicket={};
								if(viewMode.toUpperCase() == 'EDIT'){
									$scope.getIncidentSelected();
									$scope.refreshing=false;
								}
								
								$("#drpIsAsset").change(
										function() {

											var selectedText = $(this).find("option:selected").text();
											var powersensorSelectedText = $('#drpIsPowersensor :selected').text();

											var selectedValue = $(this).val();
											if (selectedText == "NO") {
												$('#drpIsPowersensor').val("");
												$('#txtSensorNumber').val("");
												$("#drpIsPowersensor").prop("disabled", true);
												$("#txtSensorNumber").prop("disabled", true);
												$scope.equipmentData.pwSensorNumber=null;
											} else if (selectedText == "YES" && powersensorSelectedText == "Select Sensor Attached") {
												$("#drpIsPowersensor").prop("disabled", false);
												$("#txtSensorNumber").prop("disabled", false);
												$("#drpIsPowersensor").attr('required', true);
											} else if (selectedText == "YES"
													&& powersensorSelectedText == "YES") {
												$("#txtSensorNumber").prop("disabled", false);
												$("#txtSensorNumber").attr('required', true);
											} else if (selectedText == "YES"
													&& powersensorSelectedText == "NO") {
												$("#txtSensorNumber").prop("disabled", true);
												$('#txtSensorNumber').val("");
											}
											else if (selectedText == "YES"	) {
												$("#drpIsPowersensor").prop("disabled", false);
												$("#txtSensorNumber").prop("disabled", false);
										}
											$scope.equipmentData.isAssetElectrical=selectedValue;

										});
								
								$("#drpIsPowersensor").change(
										function() {

											var selectedText = $(this).find("option:selected")
													.text();
											var assetSelectedText = $('#drpIsAsset :selected')
													.text();

											var selectedValue = $(this).val();
											if (selectedText == "NO"
													&& assetSelectedText == "YES") {
												$("#txtSensorNumber").prop("disabled", true);
											} else if (selectedText == "YES"
													&& assetSelectedText == "YES") {
												$("#txtSensorNumber").prop("disabled", false);
												$("#txtSensorNumber").attr('required', true);
											}
											$scope.equipmentData.isPWSensorAttached=selectedValue;
										});
								
								
							});
							
							
							$scope.initalizeCloseDiv=function(){
								$('#ticketCloseDiv').hide();
								$("#closeCodeSelect").attr('required', false);
								$("#raisedOn").attr('required', false);
								$("#closeNote").attr('required', false);
							}
							
							
							$scope.getLoggedInUserAccess =function(){
								authService.loggedinUserAccess()
					    		.then(function(data) {
					    			//console.log(data)
					    			if(data.statusCode == 200){
					    				$scope.sessionUser=data;
					    				$scope.getLoggedInUser($scope.sessionUser);
					    			}
					            },
					            function(data) {
					                console.log('Unauthorized Access.')
					            }); 
								
						    }
							
							
							
							$scope.getLoggedInUser=function(loginUser){
								//console.log(loginUser)
								userService.getLoggedInUser(loginUser)
					    		.then(function(data) {
					    			//console.log(data)
					    			if(data.statusCode == 200){
					    				$scope.siteData ={};
					    				$scope.sessionUser=angular.copy(data.object);
					    				$scope.siteData.company=$scope.sessionUser.company;
					    				$scope.getSPAgents();
					    			    if(viewMode=="NEW"){
					    			    	$scope.getStatus();
					    			    }
					    				$scope.equipmentData ={};
					    				$scope.equipmentData.company=$scope.sessionUser.company;
					    			//	$scope.getAssetLocations();
					    				$scope.getAllSites();
					    				
					    				$scope.ticketData.raisedBy=$scope.sessionUser.email;
					    				$scope.ticketData.closedBy=$scope.sessionUser.email;
					    				$scope.setTicketraisedOnDate();
					   				 	$('.dpedit').editableSelect();
					    			}
					            },
					            function(data) {
					                console.log('No User available')
					            });
							}
							
							$scope.getSPAgents=function(){
								var customerCode = $.jStorage.get("selectedCustomerCode");
								serviceProviderService.getSPAgents(customerCode)
					    		.then(function(data) {
					    			console.log(data);
					    			if(data.statusCode==200){
					    				$("#spAgentsSelect").empty();
					    				var options = $("#spAgentsSelect");
										options.append($("<option />").val("").text(
										"Select Agent"));
										$.each(data.object,function() {
											options.append($("<option />").val(	this.userId).text(	this.emailId));
										});
					    			}
					    		},function(data){
					    			console.log(data);
					    		});
							}
							
							$scope.getSPAgentSelected=function(){
								$scope.ticketData.rspAssignedAgent = $("#spAgentsSelect option:selected").text()
							}
							$scope.retrieveAssetCategories=function(){
								 $('#loadingDiv').show();
								 assetService.retrieveAssetCategories()
								 .then(function(data) {
						    			//console.log(data);    			
						    			$scope.assetCategory.equipmentList=[];
						    			$scope.assetCategory.serviceList = []
						    			
						    			$("#categorySelect").empty();
						    			$("#serviceCategorySelect").empty();
						    			$.each(data,function(key,val){
						    				if(val.assetType=='E'){
						    					$scope.assetCategory.equipmentList.push(val);
						    				}
						    				
						    				else if(val.assetType=='S'){
						    					$scope.assetCategory.serviceList.push(val);
						    				}
						    				
						    			});	
						    			
						    			//console.log($scope.assetCategory.equipmentList);
						    			//console.log("shibu3333");
						    			var options = $("#categorySelect");
										options.append($("<option />").val("").text(
										"Select Category"));
										$.each($scope.assetCategory.equipmentList,function() {
											options.append($("<option />").val(	this.assetCategoryId).text(	this.assetCategoryName));
										});
										
										var options = $("#serviceCategorySelect");
										options.append($("<option />").val("").text(
										"Select Category"));
										$.each($scope.assetCategory.serviceList,function() {
											options.append($("<option />").val(	this.assetCategoryId).text(	this.assetCategoryName));
										});
										 $('#loadingDiv').hide();
						            },
						            function(data) {
						                console.log('Asset Categories retrieval failed.')
						                $('#loadingDiv').hide();
						            });
							 }
							
							
							$scope.populateAssetType=function(type){
								 
								 var selectedSite = $('#siteSelect').val();
								 if(selectedSite == ""){
									 //alert ("No Site Selected");
									 
								 }else{
									 if($scope.assetList==null){
										  $('#messageWindow').show();
											$('#successMessageDiv').hide();
											$('#errorMessageDiv').show();
											$('#errorMessageDiv').alert();
											$scope.errorMessage="No assets available for site "+ $('#siteSelect option:selected').text()
											$scope.ticketData.assignedTo=null;
											$scope.ticketData.slaTime=null;
											$scope.ticketData.priorityDescription=null;
						 					$("#ticketCategorySelect").empty();
						 					 $("#assetSelect").empty();
						 					if(type.toUpperCase()=='EQUIPMENT'){
						 						$scope.assetTypechecked = 'E';
						 					}else if(type.toUpperCase()=='SERVICE'){
						 						$scope.assetTypechecked = 'S';
						 					}
									 }else{	 
								 if(type.toUpperCase()=='EQUIPMENT'){
									 $scope.assetTypechecked = 'E';
										//console.log($scope.assetTypechecked);
										var equipmentList = [];
										$.each($scope.assetList,function(key,val){
											if(val.assetType == 'E' && val.siteId == $scope.accessSite.selected.siteId){
												equipmentList.push(val);
											}
										});
									 $("#assetSelect").empty();
									 var options = $("#assetSelect");
						    			options.append($("<option />").val("").text(
						    			"Select asset"));
						    			$.each(equipmentList,function() {
						    					options.append($("<option />").val(	this.assetId).text(	this.assetName));
						    			});
								 }else if(type.toUpperCase()=='SERVICE'){
									 $scope.assetTypechecked = 'S';
										//console.log($scope.assetTypechecked);
										var serviceList = [];
										$.each($scope.assetList,function(key,val){
											if(val.assetType == 'S' && val.siteId == $scope.accessSite.selected.siteId){
												serviceList.push(val);
											}
										});
										 $("#assetSelect").empty();
										 var options = $("#assetSelect");
							    			options.append($("<option />").val("").text(
							    			"Select asset"));
							    			$.each(serviceList,function() {
							    					options.append($("<option />").val(	this.assetId).text(	this.assetName));
							    			});
								   }
								  }
								 }
							 }
							
							$scope.getAssetForSelectedSite =function (t, e, dropDownId){
								if(dropDownId.toUpperCase() == "SITESELECT"){
									 var site={
											 siteId:parseInt($("#siteSelect").val()),
									 		 siteName:$("#siteSelect option:selected").text()
									 }
									 $scope.accessSite.selected =site;
									 $scope.getAsset(site);		
								 }
							}
							$scope.getAsset=function(selectedSite){
								//console.log(selectedSite);
								 $('#loadingDiv').show();
								 assetService.getAssetBySite(selectedSite.siteId)
									.then(function(data) {
										console.log(data);
					 					$scope.assetList=[];
					 				if(data.statusCode==200){
					 					if(data.object.length>0){
											$.each(data.object,function(key,val){
							    				$scope.assetList.push(val);
							    			});
											if($scope.assetType == null){
												//alert($scope.assetType);
											}else{
												//alert($scope.assetType);
												$scope.populateAssetType($scope.assetType);
											}
											//$scope.getTicketCategory();
						 				  }
					 				}
					 				$('#loadingDiv').hide();
									},  function(data) {
						                console.log('Unable to get asset list')
						                $('#loadingDiv').hide();
										$('#messageWindow').show();
										$('#successMessageDiv').hide();
										$('#errorMessageDiv').show();
										$('#errorMessageDiv').alert();
										$scope.errorMessage="No assets available for site "+ selectedSite.siteName
										$scope.ticketData.assignedTo=null;
										$scope.ticketData.slaTime=null;
										$scope.ticketData.priorityDescription=null;
					 					$("#ticketCategorySelect").empty();
					 					 $("#assetSelect").empty();
									});
								
							}
							
							$scope.getSelectedAsset= function (t, e,dropDownId){
								if(dropDownId.toUpperCase() == "ASSETSELECT"){
									 var asset={
											 assetId:parseInt($("#assetSelect").val()),
									 		 assetName:$("#assetSelect option:selected").text()
									 }
									 $scope.assetList.selected = asset;
									 $.each($scope.assetList,function(key,val){
										if(val.assetId == asset.assetId){
											//console.log(val);
											$('#assignedTo').val(val.serviceProviderName);
											$('#category').val(val.assetCategoryName);
											$('#componentType').val(val.assetSubcategory1);
											var category={
													assetCategoryId:val.categoryId,
													assetCategoryName:val.assetCategoryName
											}
											 $scope.assetList.selected.assetCategoryName=category.assetCategoryName;
											$scope.assetList.selected.assignedSp=val.serviceProviderName;
											$scope.assetList.selected.assignedTo=val.serviceProviderId;
											$scope.assetList.selected.spHelpDeskEmail=val.spHelpDeskEmail;
											$scope.assetList.selected.assetSubcategory1=val.assetSubcategory1;
											$scope.assetList.selected.assetCategoryId=val.categoryId;
											$scope.selectedAsset= $scope.assetList.selected;
											if(val.assetType=='E'){
												$scope.getRepairType(category);
												if(val.subCategoryId1!=null){
													$scope.assetList.selected.subCategoryId1=val.subCategoryId1
													$scope.getSubComponentType(val.subCategoryId1);
												}else{
													$scope.subRepairType.list=[];
									    			$("#subcomponentTypeSelect").empty();
												}
											}else if(val.assetType=='S'){
												$scope.getServiceRepairType(category);
												if(val.subCategoryId1!=null){
													$scope.getServiceSubRepairType(val);
												}else{
													$scope.subRepairType.list=[];
									    			$("#subcomponentTypeSelect").empty();
												}
											}
											$scope.setTicketServiceProvider(val);
											return false;
										} 
									 });
									 
									 
									 $scope.getTicketCategory();
								 }
							}
							
							 $scope.getRepairType=function(category){
								 $('#loadingDiv').show();
								 $("#repairtypeSelect").empty();
								 $("#subrepairtypeSelect").empty();
								 $scope.ticketData.assetCategoryId=category.assetCategoryId;
								 assetService.retrieveRepairTypes(category.assetCategoryId)
						    		.then(function(data) {
						    			//console.log(data)
						    				$scope.repairType.list=[];
						    			$("#repairtypeSelect").empty();
						    				if(data.statusCode == 200){
						    					$.each(data.object,function(key,val){
						    						var repairType={
						    								subCategoryId1:val.subCategoryId1,
						    								assetSubcategory1:val.assetSubcategory1,
						    						}
						    						$scope.repairType.list.push(repairType);
						    					});
						    					var options = $("#repairtypeSelect");
						    					options.append($("<option />").val("").text("Select ComponentType"));
						    					$.each($scope.repairType.list,function() {
						    						options.append($("<option />").val(	this.subCategoryId1).text(this.assetSubcategory1));
						    					});
						    					
						    					if($scope.selectedAsset.subCategoryId1!=null){
													 $("#repairtypeSelect option").each(function() {
														if ($(this).val() == $scope.selectedAsset.subCategoryId1) {
															$(this).attr('selected', 'selected');
															$scope.repairType.selected.subCategoryId1=$scope.selectedAsset.subCategoryId1;
															// $scope.getSubRepairType($scope.repairType.selected);
															return false;
														}
												 	});
												 }
						    				}
						    				
						    				 $('#loadingDiv').hide();
						            },
						            function(data) {
						                //console.log(data)
						                $("#subrepairtypeSelect").empty();
						                $('#loadingDiv').hide();
						            });
								}
							 
								$scope.getSubComponentType=function(subCategoryId){
									 $('#loadingDiv').show();
									 $("#subrepairtypeSelect").empty();
									 $scope.ticketData.subCategoryId1=subCategoryId;
									 assetService.retrieveSubRepairTypes(subCategoryId)
							    		.then(function(data) {
							    			//console.log(data)
							    				$scope.subRepairType.list=[];
							    			$("#subcomponentTypeSelect").empty();
							    				if(data.statusCode==200){
							    					$.each(data.object,function(key,val){
							    						var subRepairType={
							    								subCategoryId2:val.subCategoryId2,
							    								assetSubcategory2:val.assetSubcategory2,
							    						}
							    						$scope.subRepairType.list.push(subRepairType);
							    					});
							    					var options = $("#subcomponentTypeSelect");
							    					options.append($("<option />").val("").text("Select SubComponentType"));
							    					$.each($scope.subRepairType.list,function() {
							    						options.append($("<option />").val(	this.subCategoryId2).text(this.assetSubcategory2));
							    					});
							    				}
							    				
							    				 $('#loadingDiv').hide();
												 
							            },
							            function(data) {
							                //console.log(data);
							                $('#loadingDiv').hide();
							            });
									}
							 
								$scope.getSubRepairType=function(repairtype){
									 $('#loadingDiv').show();
									 $("#subrepairtypeSelect").empty();
									 assetService.retrieveSubRepairTypes(repairtype.subCategoryId1)
							    		.then(function(data) {
							    			//console.log(data)
							    				$scope.subRepairType.list=[];
							    			$("#subrepairtypeSelect").empty();
							    				if(data.statusCode==200){
							    					$.each(data.object,function(key,val){
							    						var subRepairType={
							    								subCategoryId2:val.subCategoryId2,
							    								assetSubcategory2:val.assetSubcategory2,
							    						}
							    						$scope.subRepairType.list.push(subRepairType);
							    					});
							    					var options = $("#subrepairtypeSelect");
							    					options.append($("<option />").val("").text("Select SubComponentType"));
							    					$.each($scope.subRepairType.list,function() {
							    						options.append($("<option />").val(	this.subCategoryId2).text(this.assetSubcategory2));
							    					});
							    					
							    					/*if($scope.selectedAsset.subCategoryId2!=null){
														 $("#subrepairtypeSelect option").each(function() {
															if ($(this).val() == $scope.selectedAsset.subCategoryId2) {
																$(this).attr('selected', 'selected');
																$scope.subRepairType.selected.subCategoryId2=$scope.selectedAsset.subCategoryId2;
																return false;
															}
													 	});
													 }*/
							    					
							    				}
							    				
							    				 $('#loadingDiv').hide();
												 
							            },
							            function(data) {
							                //console.log(data);
							                $('#loadingDiv').hide();
							            });
									}
							 
								$scope.getServiceRepairType=function(category){
									 $('#loadingDiv').show();
									 $("#servicerepairtypeSelect").empty();
									 $scope.ticketData.assetCategoryId = category.assetCategoryId;
									 assetService.retrieveRepairTypes(category.assetCategoryId)
							    		.then(function(data) {
							    			//console.log(data)
							    				$scope.repairType.list=[];
							    			$("#servicerepairtypeSelect").empty();
							    			if(data.statusCode == 200){
							    					$.each(data.object,function(key,val){
							    						var repairType={
							    								subCategoryId1:val.subCategoryId1,
							    								assetSubcategory1:val.assetSubcategory1,
							    						}
							    						$scope.serviceRepairType.list.push(repairType);
							    					});
							    					var options = $("#servicerepairtypeSelect");
							    					options.append($("<option />").val("").text("Select ComponentType"));
							    					$.each($scope.serviceRepairType.list,function() {
							    						options.append($("<option />").val(	this.subCategoryId1).text(this.assetSubcategory1));
							    					});
							    					
							    					if($scope.selectedAsset.subCategoryId1!=null){
														 $("#servicerepairtypeSelect option").each(function() {
															if ($(this).val() == $scope.selectedAsset.subCategoryId1) {
																$(this).attr('selected', 'selected');
																$scope.serviceRepairType.selected.subCategoryId1=$scope.selectedAsset.subCategoryId1;
																// $scope.getServiceSubRepairType($scope.serviceRepairType.selected);
																return false;
															}
													 	});
													 }
							    				}
							    			
							    			 $('#loadingDiv').hide();
							            },
							            function(data) {
							                //console.log(data)
							                $("#servicesubrepairtypeSelect").empty();
							                $('#loadingDiv').hide();
							            });
									}
								$scope.getServiceSubRepairType=function(servicerepairtype){
									 $('#loadingDiv').show();
									 $("#subcomponentTypeSelect").empty();
									 $scope.ticketData.subCategoryId1 =servicerepairtype.subCategoryId1;
									 assetService.retrieveSubRepairTypes(servicerepairtype.subCategoryId1)
							    		.then(function(data) {
							    			//console.log(data)
							    			$scope.subRepairType.list=[];
								    			$("#subcomponentTypeSelect").empty();
							    			    if(data.statusCode==200){
							    					$.each(data.object,function(key,val){
							    						var subRepairType={
							    								subCategoryId2:val.subCategoryId2,
							    								assetSubcategory2:val.assetSubcategory2,
							    						}
							    						$scope.subRepairType.list.push(subRepairType);
							    					});
							    					var options = $("#subcomponentTypeSelect");
							    					options.append($("<option />").val("").text("Select SubComponentType"));
							    					$.each($scope.subRepairType.list,function() {
							    						options.append($("<option />").val(	this.subCategoryId2).text(this.assetSubcategory2));
							    					});
							    					
							    					/*if($scope.selectedAsset.subCategoryId2!=null){
														 $("#servicesubrepairtypeSelect option").each(function() {
															if ($(this).val() == $scope.selectedAsset.subCategoryId2) {
																$(this).attr('selected', 'selected');
																$scope.serviceSubRepairType.selected.subCategoryId2=$scope.selectedAsset.subCategoryId2;
																
																return false;
															}
													 	});
													 }*/
							    					//console.log("servicesubrepairtype");
									    			//console.log($scope.subRepairType.list);
							    				}
							    			    $('#loadingDiv').hide();
							            },
							            function(data) {
							                //console.log(data)
							                $('#loadingDiv').hide();
							            });
									}
							
							
							 $scope.setTicketServiceProvider=function(asset){
								 $scope.ticketData.sp=asset.serviceProviderId;
								 $scope.ticketData.spType=asset.spType;
							 }
							 
							 $scope.validateDropdownValues=function(dropDownId, assetType){
									 var valueId = parseInt($("#"+dropDownId).val());
									 if(valueId == ""){
										 
									 }else{
										 if(assetType == 'E'){
												 if(dropDownId.toUpperCase() == "SUBCOMPONENTTYPESELECT"){
													 var subrepairtype={
															 subCategoryId2:parseInt($("#subcomponentTypeSelect").val()),
															 assetSubcategory2:$("#subcomponentTypeSelect option:selected").text()
													 }
													 $scope.subRepairType.selected = subrepairtype;
													 
												 }	 
							 
										 }
									 }
									 
							 }
							$scope.getAssetLocations=function(){
								 $('#loadingDiv').show();
								 assetService.getAssetLocations()
								 .then(function(data) {
						    			//console.log(data);
						    			$scope.assetLocation.list=[];
						    			$("#locationSelect").empty();
						    			$("#serviceLocationSelect").empty();
						    			$.each(data,function(key,val){
						    				$scope.assetLocation.list.push(val);
						    				
						    			});	
						    			
						    			var options = $("#locationSelect");
										options.append($("<option />").val("").text(
										"Select Location"));
										$.each($scope.assetLocation.list,function() {
											options.append($("<option />").val(	this.locationId).text(	this.locationName));
										});
										
										var options = $("#serviceLocationSelect");
										options.append($("<option />").val("").text(
										"Select Location"));
										$.each($scope.assetLocation.list,function() {
											options.append($("<option />").val(	this.locationId).text(	this.locationName));
										});
						    			//console.log($scope.assetLocation.list);
						    			 $('#loadingDiv').hide();
						            },
						            function(data) {
						                console.log('Asset Locations retrieval failed.')
						            });
								 
							 }
							
							

							$scope.setTicketraisedOnDate = function() {
								$scope.CurrentDate = new Date();
								$scope.CurrentDate = $filter('date')(new Date(), 'dd-MM-yyyy');
							}
							
							
							
							$scope.getAllSites=function(){
								$('#loadingDiv').show();
								var encodedString = $.jStorage.get("selectedCustomer");
								var customerLocation = window.atob(encodedString);
								siteService.retrieveSPAllSites(customerLocation)
								.then(function(data) {
					    			//console.log(data)
					    				$scope.siteList=[];
					    				$scope.accessSite.list = [];
					    				if(data.length>0){
					    					$.each(data,function(key,val){
					    						$scope.accessSite.list.push(val);
					    					});
					    				//	$scope.getSiteDetails($scope.siteList[0]);
					    					var options = $("#siteSelect");
					    					options.append($("<option />").val("").text("Select Site"));
					    					$.each($scope.accessSite.list,function() {
												options.append($("<option />").val(	this.siteId).text(this.siteName));
											});
					    					$('#loadingDiv').hide();
					    				}else{
					    					console.log("No sites assigned to the user.")
					    					$scope.InfoMessage="No sites assigned to the user."
					    						$('#messageWindow').show();
					    	    				$('#infoMessageDiv').show();
					    	    				$('#loadingDiv').hide();
					    				}
					            },
					            function(data) {
					                //console.log(data)
					                	$scope.InfoMessage="No sites assigned to the user."
										$('#messageWindow').show();
					    				$('#infoMessageDiv').show();
					    				$('#infoMessageDiv').alert();
					    				$('#loadingDiv').hide();
					            });
							}
							
							
							
							$scope.getStatus=function(){
								var encodedString = $.jStorage.get("selectedCustomer");
								var customerLocation = window.atob(encodedString);
								if(viewMode.toUpperCase()=='NEW'){
									statusService.retrieveAllStatus(customerLocation)
					                .then(function(data){
					                	console.log(data);
					                	$("#statusSelect").empty();
					    				var options = $("#statusSelect");
					    				options.append($("<option />").val("").text("Select status"));
					    				$.each(data,function(){
					    					if(this.statusId==1){
					    						options.append($("<option />").val(	this.statusId).text(this.status));
					    					}
					    				});
					                },function(data){
					                	//console.log(data);
					                });
									
								}else if(viewMode.toUpperCase()=='EDIT'){
									$scope.ticketData.statusInfoList=[];
									statusService.retrieveAllStatus()
					                .then(function(data){
					                	//console.log(data);
					                	$("#statusSelect").empty();
					    				var options = $("#statusSelect");
					    				options.append($("<option />").val("").text("Select status"));
					    				$.each(data,function(){
					    					options.append($("<option />").val(	this.statusId).text(	this.status));
					    				});
					    				$("#statusSelect option").each(function() {
											//console.log($(this).val());
											if ($(this).val() == $scope.ticketData.statusId) {
												$(this).attr('selected', 'selected');
												return false;
											}
									 	});
					    				$.each(data,function(key,val){
					    					var statusInfo={
					    							statusId:val.statusId,
					    							status:val.status,
					    							description:val.description
					    					}
					    					$scope.ticketData.statusInfoList.push(statusInfo);
					    				});
					                },function(data){
					                	//console.log(data);
					                });
								}
							}
							
							
							$scope.getTicketCategory=function(){
								$('#loadingDiv').show();
								ticketCategoryService.retrieveAllCategories()
								.then(function(data) {
					    			//console.log(data)
					    				$scope.categoryList=[];
					    				if(data.length>0){
					    					$.each(data,function(key,val){
					    						var category={
					    	            				categoryId:val.id,
					    	            				categoryName:val.ticketCategory
					    	            		}
					    						$scope.categoryList.push(category);
					    					});
					    					$('#loadingDiv').hide();
					    					$("#ticketCategorySelect").empty();
					    					var options = $("#ticketCategorySelect");
					    					options.append($("<option />").val("").text("Select category"));
					    					$.each($scope.categoryList,function() {
					    						options.append($("<option />").val(	this.categoryId).text(	this.categoryName));
					    					});
					    					if(viewMode.toUpperCase() == 'EDIT'){
					    						var slC={};
					    						$("#ticketCategorySelect option").each(function() {
					    							//console.log($(this).val());
													if ($(this).val() == $scope.ticketData.categoryId) {
														$(this).attr('selected', 'selected');
														slC.categoryId=$scope.ticketData.categoryId;
														slC.categoryName=$scope.ticketData.categoryName
														return false;
													}
											 	});
					    						$scope.selectedCategory.selected = slC;
					    						$scope.getTicketPriority($scope.selectedCategory.selected);
					    					}
					    				}else{
					    					console.log("No categories found")
					    				}
					    				$('#loadingDiv').hide();
					            },
					            function(data) {
					                //console.log(data)
					                console.log("No categories found")
									$('#loadingDiv').hide();
					            });
								
							}
							
							$scope.openFileAttachModal=function(){
								$scope.incidentImageList=[{
									id:0,
									attachment:""
								}];
								$('#incidentImage0').val('');
								$('#fileAttachModal').modal('show');
								$('#fileAttachModal').removeData();
								$scope.incidentImages=[];
								
							}
							
							
							$scope.addNewImage=function(){
								var length=$scope.incidentImageList.length;
								var imageComponent={
										id:length,
										size:0	
								}
								$scope.incidentImageList.push(imageComponent);
							};
							
							
						    // GET THE FILE INFORMATION.
						    $scope.getIndexedName = function (val, e) {
						    	//console.log(val.id);
								var incidentImageId=val.id;
								$scope.incidentImageId=incidentImageId;
								 var ext=$("input#"+incidentImageId).val().split(".").pop().toLowerCase();
								 //console.log($('#'+incidentImageId).val());
								 var fileSize = Math.round((e.target.files[0].size / 1024)) ;
								 $scope.incidentFileName = e.target.files[0].name.split('.')[0];
							     $scope.fileExtension = ext;
							     $scope.indexPos=incidentImageId.charAt(incidentImageId.length-1);
							     if($.inArray(ext, ["jpg","jpeg","JPEG", "JPG","PDF","pdf","png","PNG"]) === -1) {
							    	 $('#messageWindow').show();
							    	 $scope.incidentImageModalErrorMessage="";
							    	 $('#incidentImageModalMessageDiv').show();
						        	 $('#errorMessageDiv').show();
						        	 $('#errorMessageDiv').alert();
						        	 $('#fileerrorincident').text('Supported file types to upload are jpg,  png and pdf');
							          $scope.isfileselected=false;	 
							          $('#'+incidentImageId).val('');
							          $('#'+incidentImageId).val(null);
							     }
							 
							     else {
							    	 if (e.target.files != undefined) {
								    	 file = $('#'+incidentImageId).prop('files');
								    	 var reader = new FileReader();
								    	 reader.onload = $scope.onFileUploadReader;
								     	 reader.readAsDataURL(file[0]);
							    	 }
									     
							       }
						    };
						    var totalIncidentImageSize=0;
						    $scope.onFileUploadReader=function(e){
						    	$('#loadingDiv').show();
						    	var fileUrl = e.target.result;
						    	var fileSize = Math.round((e.total / 1024)) ;
						 		var incidentImage={
									fileName:$scope.ticketData.ticketNumber,
									file:$scope.incidentFileName,
									incidentImgId:$scope.incidentImageId,
									imgPos:$scope.indexPos,
									base64ImageString:e.target.result,
									fileExtension: $scope.fileExtension,
									fileSize : fileSize
								}
						 		$('#imgsize'+$scope.indexPos).text(fileSize+" KB");
						 		ticketService.addImage(incidentImage)
						 		.then(function(data){
						 			//console.log(data);
						 				var totalSize=0;
						 				$scope.incidentImages=[];
						 				if(!$.isEmptyObject(data.object)){
						 					for(key in data.object){
							 					totalSize =  totalSize + parseInt(data.object[key].fileSize);
							 					$scope.incidentImages.push(data.object[key]);
							 				};
							 				$('#totalSize').text("Files ( "+$scope.incidentImages.length +" ) Total Size : "+ totalSize +" KB");
							 				if(totalSize > 1024){
							 					$('#incidentImageModalMessageDiv').show();
							 					 $('#messageWindow').show();
							 					 $('#errorMessageDiv').show();
							 		        	 $('#errorMessageDiv').alert();
							 					$('#fileerrorincident').text("File size exceeds 1 MB");
							 					$('#uploadImgBtn').attr("disabled",true)
							 					$('#btnUpload').attr("disabled",true)
							 				}else{
							 					$('#incidentImageModalMessageDiv').hide();
							 					 $('#messageWindow').hide();
							 					$('#uploadImgBtn').removeAttr("disabled");
							 					$('#btnUpload').removeAttr("disabled");
							 					$('#fileerrorincident').text("");
							 				}
						 				}
						 				$('#loadingDiv').hide();
						 		},function(data){
						 			//console.log(data)
						 			$('#loadingDiv').hide();
						 		});
						 		
						 	 }
						    $scope.uploadFiles = function (mode) {
						    	//console.log($scope.incidentImages);
						    	
						    	if($scope.incidentImages.length>0){
						    		 var totalSize=0;
						   		  $.each($scope.incidentImages,function(key,val){
						   			  totalSize =  totalSize + parseInt(val.fileSize);
						   			$('#totalIncidentSize').text(totalSize+" KB");
						   		  });
						    		
						    		if(totalSize > 1024){
						    			 $('#messageWindow').show();
								    	 $('#fileerrorincident').text('File size exceeds Max limit ( 1 MB )');
							        	 $('#errorMessageDiv').show();
							        	 $('#errorMessageDiv').alert();
						    		}else{
									 $scope.ticketData.incidentImageList=$scope.incidentImages;
									 if(mode=="EDIT"){
										 $scope.ticketData.mode="IMAGEUPLOAD"
									     $scope.persistTicket( $scope.ticketData, "UPLOAD");
									 }else{
										 $('#btnUploadCancel').click();
									 }
										 
						    		}
								 }
						    }
						    
						    $scope.removeImage=function(indexPos){
						    	 var totalSize=0;
								  $.each($scope.incidentImages,function(key,val){
									  totalSize =  totalSize + parseInt(val.fileSize);
								  }); 
								  $scope.incidentImageList.splice(indexPos,1);
								  $.each($scope.incidentImages,function(key,val){
									 if(val.imgPos==indexPos) {
										 $scope.incidentImages.splice(indexPos,1);		
										 totalSize = totalSize - val.fileSize ;
										// $('#totalIncidentSize').text(totalIncidentImageSize+" KB");
										 $('#totalSize').text("Files ( "+$scope.incidentImages.length +" ) Total Size : "+ totalSize +" KB");
										 $scope.incidentImageList.splice(indexPos,1);
										 return false;
									 }
								  });
								if(totalSize > 1024){
									$('#incidentImageModalMessageDiv').show();
									 $('#messageWindow').show();
									 $('#errorMessageDiv').show();
						        	 $('#errorMessageDiv').alert();
									$('#fileerrorincident').text("File size exceeds 1 MB");
									$('#uploadImgBtn').attr("disabled",true)
									$('#btnUpload').attr("disabled",true)
								}else{
									$('#incidentImageModalMessageDiv').hide();
									 $('#messageWindow').hide();
									$('#uploadImgBtn').removeAttr("disabled");
									$('#btnUpload').removeAttr("disabled");
									$('#fileerrorincident').text("");
								}

						    }
						    
							$scope.getTicketPriority=function(){
								$scope.priorityList=[{
									'priorityId':1,
									'priorityCode':'P1',
									'priorityName':'Critical'
								},
								{
									'priorityId':2,
									'priorityCode':'P2',
									'priorityName':'High'
								},
								{
									'priorityId':3,
									'priorityCode':'P3',
									'priorityName':'Medium'
								},
								{
									'priorityId':4,
									'priorityCode':'P4',
									'priorityName':'Low'
								}];
								
								$("#prioritySelect").empty();
								
								var options = $("#prioritySelect");
								options.append($("<option />").val("").text(
								"Select Priority"));
								$.each($scope.priorityList,function() {
									options.append($("<option />").val(	this.priorityId).text(	this.priorityName));
								});
								
								var category={
										categoryId:$scope.ticketData.categoryId
								}
								$scope.setTicketPriorityAndSLA(category);
								
							}
						    
						    $scope.getSelectedCategory=function(dropDownId){
						    	if(dropDownId.toUpperCase() == "TICKETCATEGORYSELECT"){
						    		 var category={
						    				 categoryId:parseInt($("#ticketCategorySelect").val()),
						    		 		 categoryName:$("#ticketCategorySelect option:selected").text()
						    		 }
						    		 $scope.categoryList.selected =category;
						    		 //scope.getTicketPriority();
						    		 $scope.setTicketPriorityAndSLA($scope.categoryList.selected);
						    	 }
						    }
						    
						    $scope.setTicketPriorityAndSLA=function(ticketCategory){
								 //console.log($scope.ticketData);
								 var spId = $scope.ticketData.sp;
								 var spType=$scope.ticketData.spType;
								 if(viewMode.toUpperCase()=='EDIT'){
									 spId=parseInt($scope.ticketData.assignedTo);
							      }
								 if(spId==undefined){
									 //alert("No Site Selected");
									 return false;
								 }else{
									 $('#loadingDiv').show();
								 ticketService.getTicketPriorityAndSLA(spId,ticketCategory.categoryId, spType)
								 .then(function(data){
									 //console.log(data);
									 if(data.statusCode == 200){
										 $scope.ticketData.priorityId = data.object.priorityId;
										 $scope.ticketData.priorityDescription = data.object.priorityName;
										 //$scope.ticketData.sla=data.object.ticketSlaDueDate;
										 $scope.ticketData.categoryId=data.object.ticketCategoryId;
										 $scope.ticketData.unit= data.object.units;
										 $scope.ticketData.duration = data.object.duration;
										 $scope.ticketData.categoryName=ticketCategory.categoryName;
										 $scope.ticketData.slaTime =  $scope.ticketData.duration + " " +  $scope.ticketData.unit;
										 if(viewMode.toUpperCase()=='EDIT'){
											 $.each($scope.priorityList,function(key,val){
												 if(val.priorityId == $scope.ticketData.priorityId){
													 $('#prioritySelect').val(""+val.priorityId+"").prop('selected', true);
													 return false;
												 }
											 });
										 }
									 }
									  $('#statusComponent').attr("style :margin-top: -82px");
									  
									 $('#loadingDiv').hide();
								 },function(data){
									 //console.log(data);
									 $('#loadingDiv').hide();
								 });
								 }
								
							 }
							
							$scope.ticketStatusChange=function(dropDownId){
								if(dropDownId.toUpperCase() == "STATUSSELECT"){
								 var ticketStatus={
										 statusId:parseInt($("#statusSelect").val()),
								 		 statusName:$("#statusSelect option:selected").text()
								 }
								 $scope.statusList.selected =ticketStatus;
							   }
							}
							
							function validateDropdownValues(dropDownId, assetType){
								var scope = angular.element("#spIncidentCreateWindow").scope();
								 var valueId = parseInt($("#"+dropDownId).val());
								 if(valueId == ""){
									 
								 }else{
									 if(assetType == 'E'){
											 if(dropDownId.toUpperCase() == "CATEGORYSELECT"){
												 var category={
														 assetCategoryId:parseInt($("#categorySelect").val()),
												 		 assetCategoryName:$("#categorySelect option:selected").text()
												 }
												 scope.assetCategory.selected = category;
												 scope.getRepairType(category);
											 }
											 
											 if(dropDownId.toUpperCase() == "REPAIRTYPESELECT"){
												 var repairtype={
														 subCategoryId1:parseInt($("#repairtypeSelect").val()),
														 assetSubcategory1:$("#repairtypeSelect option:selected").text()
												 }
												 scope.repairType.selected = repairtype;
												// scope.getSubRepairType(repairtype);
											 }
											 if(dropDownId.toUpperCase() == "SUBCOMPONENTTYPESELECT"){
												 var subrepairtype={
														 subCategoryId2:parseInt($("#subcomponentTypeSelect").val()),
														 assetSubcategory2:$("#subcomponentTypeSelect option:selected").text()
												 }
												 scope.subRepairType.selected = subrepairtype;
												 
											 }
											 if(dropDownId.toUpperCase() == "LOCATIONSELECT"){
												 var location={
														 locationId:parseInt($("#locationSelect").val()),
												 		 locationName:$("#locationSelect option:selected").text()
												 }
												 scope.assetLocation.selected =location;
											 }
											 if(dropDownId.toUpperCase() == "SPSELECT"){
												 var serviceProvider={
														 serviceProviderId:parseInt($("#spSelect").val()),
												 		 serviceProviderName:$("#spSelect option:selected").text()
												 }
												 scope.serviceProvider.selected = serviceProvider;
											 }
											 if(dropDownId.toUpperCase() == "SITESELECT"){
												 var site={
														 siteId:parseInt($("#siteSelect").val()),
												 		 siteName:$("#siteSelect option:selected").text()
												 }
												 scope.accessSite.selected =site;
											 }
											 scope.equipmentData.assetType=assetType;
									 }else if(assetType == 'S'){
										 if(dropDownId.toUpperCase() == "SERVICECATEGORYSELECT"){
											 var category={
													 assetCategoryId:parseInt($("#serviceCategorySelect").val()),
											 		 assetCategoryName:$("#serviceCategorySelect option:selected").text()
											 }
											 scope.assetCategory.selected = category;
											 scope.getServiceRepairType(category);
										 }
										 
										 if(dropDownId.toUpperCase() == "SERVICEREPAIRTYPESELECT"){
											 var servicerepairtype={
													 subCategoryId1:parseInt($("#servicerepairtypeSelect").val()),
													 assetSubcategory1:$("#servicerepairtypeSelect option:selected").text()
											 }
											 scope.serviceRepairType.selected = servicerepairtype;
											// scope.getServiceSubRepairType(servicerepairtype);
										 }
										 if(dropDownId.toUpperCase() == "SERVICESUBREPAIRTYPESELECT"){
											 var servicesubrepairtype={
													 subCategoryId2:parseInt($("#servicesubrepairtypeSelect").val()),
													 assetSubcategory2:$("#servicesubrepairtypeSelect option:selected").text()
											 }
											 scope.serviceSubRepairType.selected = servicesubrepairtype;
											 
										 }
										 if(dropDownId.toUpperCase() == "SERVICELOCATIONSELECT"){
											 var location={
													 locationId:parseInt($("#serviceLocationSelect").val()),
											 		 locationName:$("#serviceLocationSelect option:selected").text()
											 }
											 scope.assetLocation.selected =location;
										 }
										 if(dropDownId.toUpperCase() == "SERVICESPSELECT"){
											 var serviceProvider={
													 serviceProviderId:parseInt($("#serviceSPSelect").val()),
											 		 serviceProviderName:$("#serviceSPSelect option:selected").text()
											 }
											 
											 scope.serviceProvider.selected = serviceProvider;
										 }
										 if(dropDownId.toUpperCase() == "SERVICESITESELECT"){
											 var site={
													 siteId:parseInt($("#serviceSiteSelect").val()),
											 		 siteName:$("#siteSelect option:selected").text()
											 }
											 scope.accessSite.selected =site;
										 }
										 scope.serviceData.assetType=assetType;
									 }
								 }
							}
							
							
							
							

							$scope.saveTicket = function() {
								console.log("save ticket called");

								$scope.ticketData.siteId = $scope.accessSite.selected.siteId;
								$scope.ticketData.siteName = $scope.accessSite.selected.siteName;

								$scope.ticketData.assetId = $scope.assetList.selected.assetId;
								$scope.ticketData.assetName = $scope.assetList.selected.assetName;
								$scope.ticketData.assetCategoryId = $scope.assetList.selected.assetCategoryId;
								$scope.ticketData.assignedTo = $scope.assetList.selected.assignedTo;
								$scope.ticketData.categoryName = $scope.categoryList.selected.categoryName;
								$scope.ticketData.subCategoryId1 = $scope.assetList.selected.subCategoryId1;
								$scope.ticketData.subCategoryId2 = $scope.subRepairType.selected.subCategoryId2;

								$scope.ticketData.assetCategoryName = $scope.assetList.selected.assetCategoryName;
								$scope.ticketData.assetSubCategory1 = $scope.assetList.selected.assetSubCategory1;
								$scope.ticketData.assetSubCategory2 = $('#subcomponentTypeSelect option:selected').text();
								 $scope.ticketData.statusId=$('#statusSelect').val();
								 $scope.ticketData.status=$('#statusSelect option:selected').text();
								 $scope.ticketData.ticketStartTime = $('#ticketStartTime').val();
								 //console.log(moment($scope.ticketData.ticketStartTime, 'YYYY-MM-DD h:m:s A').format('YYYY-MM-DD HH:mm:ss'));
								 //$scope.ticketData.ticketStartTime = moment($scope.ticketData.ticketStartTime, 'DD-MM-YYYY HH:MM').format('DD-MM-YYYY HH:MM');
								
								 
								 //console.log($scope.accessSite);
								 //console.log($scope.accessSite.selected);
								 
								 //console.log($scope.ticketData);
								 $scope.ticketData.mode="NEW";
								 $scope.ticketData.rspAssignedAgent = $("#spAgentsSelect option:selected").text()
								$scope.persistTicket($scope.ticketData, "NEW");
							}

							 $scope.persistTicket=function(ticketData, type){
								 if(type.toUpperCase()=='UPDATE'){
									 $('#loadingDiv1').show();
								 }
								 else if(type.toUpperCase()=='UPLOAD'){
									 $('#loadingDiv5').show();
								 }
								 ticketService.saveTicket(ticketData)
								 .then(function(data){
									 //console.log(data);
									 if(data.statusCode == 200){
										 	$scope.successMessage = data.message;
										 	$('#messageWindow').show();
						    				$('#successMessageDiv').show();
						    				$('#successMessageDiv').alert();
						    				$('#errorMessageDiv').hide();
						    				 $scope.incidentImageList=[];
					    					 $scope.incidentImages=[];
					    					 $('#totalIncidentSize').text("0KB");
						    				if(type.toUpperCase()=='NEW'){
						    					window.location.href=hostLocation+"/serviceprovidercompany/customers";
						    				}
						    				else if(type.toUpperCase()=='UPDATE'){
						    					//if($scope.ticketData.statusId==7){
						    						window.location.href=hostLocation+"/serviceprovidercompany/customers";
						    					//}
						    				}else{
						    					if(type.toUpperCase()=='UPLOAD'){
						    						$scope.successMessage = "Files uploaded successfully";
						    					 	$('#messageWindow').show();
						    	    				$('#successMessageDiv').show();
						    	    				$('#successMessageDiv').alert();
						    	    				$('#errorMessageDiv').hide();
						    	    				if(data.object.attachments.length>0){
						    	    					$scope.ticketData.files=[];
						    	    					$.each(data.object.attachments,function(key,val){
						    	    						var  fileInfo={
						    	    								fileId:val.attachmentId,
						    	    								fileName: val.attachmentPath.substring(val.attachmentPath.lastIndexOf("/")+1),
						    	    								createdOn: val.createdDate,
						    	    								filePath: val.attachmentPath
						    	    						}
						    	    						$scope.ticketData.files.push(fileInfo);
						    	    					});
						    	    				}else{
						    	    					$scope.ticketData.files=[];
						    	    				}
						    					 }
						    				}
									 }
									 $('#loadingDiv1').hide();
									 $('#loadingDiv5').hide();
								 },function(data){
									//console.log(data); 
									$('#loadingDiv').hide();
									$('#messageWindow').show();
									$('#successMessageDiv').hide();
									$('#errorMessageDiv').show();
									$('#errorMessageDiv').alert();
								 });
							 }

						} ]);