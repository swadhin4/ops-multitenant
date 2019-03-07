chrisApp.controller('spExtCustomerController',
			[
				'$rootScope',
				'$scope',
				'$filter',
				'$location',
				'userService',
				'companyService',
				'registrationService',
				'authService',
				'serviceProviderService',
				'regionService',
				'countryService',
				'siteService',
				'ticketService',
				function($rootScope, $scope, $filter, $location,
						userService,  companyService,
						registrationService,authService, serviceProviderService, regionService, countryService, siteService,ticketService) {

				$scope.spCustomerList = {
					selected : {},
					list : []
				}

				$scope.spCustomerIncidentList = {
					selected : {},
					list : []
				}
				
				$scope.salesTimes={
						fromlist:[],
						toList:[]
				}
					$scope.deliveryTimes={
						fromlist:[],
						toList:[]
				}
				//For Asset Requirement
				$scope.asset = {
					selected : {},
					list : []
				}
				
				$scope.area={
						selected:{},
						list:[]
				}
				
				$scope.district={
						selected:{},
						list:[]
				}
				$scope.cluster={
						selected:{},
						list:[]
				}
				
				$scope.started = false;

				$scope.equipmentData = {};
				$scope.serviceData = {};
				$scope.selectedRow = null;
				$scope.selectedAsset = {};
				$scope.operation = {};
				
				$scope.rowHighilited=function(row)
			    {
			      $scope.selectedRow = row;    
			    }
				
				$scope.regionList=[];
				$scope.countryList=[];
				$scope.extCustServiceProvider={};
				//

				angular.element(document).ready(function() {
					$scope.selectedSite={};
					$scope.selectedSite.fileInput=null;
					 $scope.sessionTicket=null;
					$scope.getLoggedInUserAccess();	
					$scope.ticketCreatedOrAssigned="CUSTOMER";
					$scope.pageViewFor="CUSTOMERS";
					$('#incidentDetails').on('click', 'tbody tr',function(){
						 $('#incidentDetails tbody > tr').removeClass('currentSelected');
						  $(this).addClass('currentSelected');
						  var rowIndex =  $(this).find('td:eq(1)').text();
					            var currentTicket={
						        		ticketNumber:rowIndex
						        }
					            var selectedTicket={};
					            $.each($scope.spCustomerIncidentList.list,function(key,val){
						        	if(val.ticketNumber == currentTicket.ticketNumber){						        	
						        		 $scope.ticketSelected=val;
						        		 if(val.ticketId!=null){
						        				$scope.setTicketinSession(val);						        				
						        			}else{
						        				console.log("Ticket not selected")
						        			}
						        		 return false;
						        	}
						        });
					});
					
					
				});
				
				
				 $scope.getTicketDetails=function(ticket){
					 	$('#loadingDivRsp').show();
						$scope.selectedTicket={};
						ticketService.retrieveTicketDetails(ticket)
						.then(function(data){
							console.log(data);
							if(data.statusCode==200){
								$scope.selectedTicket = data.object
								$scope.ticketData=angular.copy($scope.selectedTicket);
								//$scope.previewSelectedIncidentInfo($scope.selectedTicket);
								$('#loadingDivRsp').hide();
							}
						},function(data){
							console.log(data);
							$('#loadingDivRsp').hide();
						});
					}
					 
				
				$scope.previewSelectedIncidentInfo=function(val){
					$scope.getTicketDetails(val);
					$('#previewIncidentModal').modal('show');
					
				}
				$scope.previewSelectedSiteInfo=function(val){
					$scope.getSiteDetails(val);
					$('#previewSiteModal').modal('show');
				}
				
				$scope.previewSelectedAssetInfo=function(val){
					$scope.getAssetDetails(val);
					$('#previewAssetModal').modal('show');
				}
				//Ext Customer
				$scope.addEditExtCustomer=function(val,isAddEdit){
					//$scope.getAssetDetails(val);
					console.log("addEditExtCustomer",isAddEdit);
					if(isAddEdit == 'Add'){
						$scope.getEscalationLevel();
						$scope.extCustServiceProvider={};
						$scope.extCustServiceProvider.slaListVOList=[];
						$scope.extCustServiceProvider.escalationLevelList=[];
						$scope.getRegionList('ADD');
						$scope.getPriorities();
					} else {
						$scope.extCustServiceProvider=val;
						$scope.getRegionList('EDIT');
						$scope.priorities =[];
						var priorityList=['Critical', 'High', 'Medium', 'Low'];
						var description=['Operation is completely down','Operation is partially interrupted', 'Performance degraded','General service request'];
						$scope.getExtCustPriorities(val);
						/*
						
						$scope.escalationLevels = [];
						$.each($scope.selectedServiceProvider.escalationLevelList,function(key,val){
							var escalation={
									escId:val.escId,
									contact:val.escalationPerson,
									email:val.escalationEmail
							}
							$scope.escalationLevels.push(escalation);
						});
						
						var maxLevel = 4;
						var currentLevel =$scope.escalationLevels.length;
						var requiredCount = maxLevel - currentLevel;
						if($scope.escalationLevels.length<=4){
							for(var i=0;i<=requiredCount-1;i++){
								var escalation={
										escId:null,
										contact:null,
										email:null
								}
								$scope.escalationLevels.push(escalation);
							}
						}
						*/
					}
					$('#addEditExtCustomerModal').modal('show');
				}
				
				$scope.getExtCustPriorities=function(selectedCust){
					$scope.selectedExtCust=selectedCust;
					serviceProviderService.getExtCustSLAList(selectedCust)
					.then(function(data){
						console.log(data);
						if(data.statusCode==200){
							$scope.selectedExtCust.selectedSlaListVOList=[];
							var description=['Operation is completely down','Operation is partially interrupted', 'Performance degraded','General service request'];
							$.each(data.object,function(key,val){
								var priority={
										index:key,
										slaId:val.slaId,
										priority:priority,
										description:description[key],
										duration:val.duration,
										unit:val.unit,
										
								}
								$scope.selectedExtCust.selectedSlaListVOList.push(priority);
							})
						}
					},function(data){
						console.log(data)
						$scope.extCustServiceProvider.slaListVOList=[];
						
					});
				}
				
				$scope.onlyNumbers = /^\d+$/;
				$scope.filterValue = function($event){
			        if(isNaN(String.fromCharCode($event.keyCode))){
			            $event.preventDefault();
			        }
				};
				
				$scope.getEscalationLevel=function(){
					$scope.escalationLevels =[];
					$scope.escalationValues =[];
					var escalations=["Level 1", "Level 2", "Level 3", "Level 4"];
					for(var i=1;i<=4;i++){
						var escalation={
								escId:null,
								level:escalations[i-1],
								contact:null,
								email:null,
						}
						$scope.escalationLevels.push(escalation);
					}
				}
				
				
				
				$scope.saveExtCustomerform=function(saveExtCustform){
					console.log("saveExtCustomerform",saveExtCustform);
					$scope.extCustServiceProvider.slaListVOList=[];
					$scope.extCustServiceProvider.escalationLevelList=[];
					$.each($scope.priorities,function(key,val){
						var finalPriorityObject={
								slaId:val.slaId,
								priorityId:val.pId,
								ticketPriority:{
									priorityId:val.pId,
									priority:val.priority
								},
								priority:val.priority,
								duration:val.duration,
								unit:val.unit
						}
						$scope.extCustServiceProvider.slaListVOList.push(finalPriorityObject);
					})
					$scope.saveExtCustServiceProvider($scope.extCustServiceProvider);
				}
				
				$scope.saveExtCustServiceProvider=function(spData){
					console.log("saveExtCustCustomer",spData);
					$('#loadingDiv').show();
					serviceProviderService.saveRSPExternalCustomer(spData)
					.then(function(data) {
		    			if(data.statusCode == 200){
		    				 $scope.successMessage = data.message;
		    				 $scope.getSuccessMessage($scope.successMessage);
		    				 $scope.getExternalCustomerData();
		    				 $('#loadingDiv').hide();
		    			}else{
		    				$scope.modalErrorMessage = data.message;
		    				 $scope.getErrorMessage($scope.modalErrorMessage);
		    				$('#loadingDiv').hide();
		    			}
		            },
		            function(data) {
		                console.log('Unable to save Service Provider Ext Customer')
		                $scope.modalErrorMessage = data.message;
		                $scope.getErrorMessage($scope.modalErrorMessage);
						$('#loadingDiv').hide();
		            });
				}
				
				$scope.updateExternalCustSLA=function(selectedCustomer){
					console.log("saveExtCustCustomerSLA",$scope.selectedExtCust);
					$('#loadingDiv').show();
					$scope.selectedExtCust.slaListVOList = $scope.selectedExtCust.selectedSlaListVOList;
					serviceProviderService.updateExtCustSLA($scope.selectedExtCust)
					.then(function(data) {
		    			if(data.statusCode == 200){
		    				 $scope.successMessage = data.message;
		    				 $scope.getSuccessMessage($scope.successMessage);
		    				 $scope.getExternalCustomerData();
		    				 $('#loadingDiv').hide();
		    			}else{
		    				$scope.modalErrorMessage = data.message;
		    				 $scope.getErrorMessage($scope.modalErrorMessage);
		    				$('#loadingDiv').hide();
		    			}
		            },
		            function(data) {
		                console.log('Unable to update Ext Customer SLA')
		                $scope.modalErrorMessage = data.message;
		                $scope.getErrorMessage($scope.modalErrorMessage);
						$('#loadingDiv').hide();
		            });
				}
				
				
				$scope.previewSelectedExtCustomer=function(val){
					console.log("previewSelectedExtCustomer",val);
					$scope.extCustServiceProvider=val;
					//$('#addEditExtCustomerModal').modal('show');
				}
				
				
				
				$scope.getRegionList=function(operation){
					$('#loadingDiv').show();
					regionService.findRSPRegions()
		    		.then(function(data) {
		    			$scope.regionList=[];
		    			$("#regionSelect").empty();
		    			if(data.statusCode==200){
		    				$.each(data.object,function(key,val){
			    				var region={
			    					regionId:val.regionId,
			    					regionCode:val.regionCode,
			    					regionName:val.regionName
			    				}
			    				$scope.regionList.push(region);
			    			});
		    			}
		    			
		    			$('#loadingDiv').hide();
		    			var options = $("#regionSelect");
						options.append($("<option />").val(0).text("Select Region"));
						$.each($scope.regionList,function() {
							options.append($("<option />").val(	this.regionId).text(this.regionName));
						});
						if(operation == 'ADD'){
							$("#regionSelect option[value='0']").attr('selected','selected');
						}else{
							$("#regionSelect option[value='"+$scope.extCustServiceProvider.regionId+"']").attr('selected','selected');
							var region={
									regionId:$scope.extCustServiceProvider.regionId,
									regionName:$scope.extCustServiceProvider.regionName
							}
							$scope.getRegionCountry(region);
						}
		            },
		            function(data) {
		                console.log('Error in getting region list')
		                $('#loadingDiv').hide();
		            }); 	
				}
				
				$scope.getRegionCountry=function(region){
					$scope.getCountryListBy(region);
				}
				$scope.getCountryListBy=function(region){
					$('#loadingDiv').show();
					regionService.getCountryByRegion(region)
		    		.then(function(data) {
		    			
		    			$scope.countryList=[];
		    			$("#countrySelect").empty();
		    			if(data.statusCode==200){
			    			$.each(data.object,function(key,val){
			    				$scope.countryList.push(val);
			    			});
			    			var options = $("#countrySelect");
							options.append($("<option />").val(0).text("Select Country"));
							$.each($scope.countryList,function() {
								options.append($("<option />").val(	this.countryId).text(this.countryName));
							});
							if($scope.extCustServiceProvider!=null){
								$("#countrySelect option").each(function() {
									if ($(this).val() == $scope.extCustServiceProvider.countryId) {
										$(this).attr('selected', 'selected');
									return false;
									}
							 	});
								//$scope.extCustServiceProvider.country = $scope.extCustServiceProvider.country;
							}
		    			}
						$('#loadingDiv').hide();
						
		            },
		            function(data) {
		                console.log('Error in getting list')
		                $('#loadingDiv').hide();
		            });
				}
				$scope.setServiceProviderCountry=function(selectedCountry){
					$scope.extCustServiceProvider.country = selectedCountry;
				}
				
				
				
				$scope.getPriorities=function(){
					$scope.priorities =[];
					$scope.priorityValues =[];
					var priorityList=['Critical', 'High', 'Medium', 'Low'];
					var description=['Operation is completely down','Operation is partially interrupted', 'Performance degraded','General service request'];
					for(var i=1;i<=4;i++){
						var priority={
								slaId:null,
								pId:i,
								priority:priorityList[i-1],
								description:description[i-1],
								duration:null,
								unit:null,
								
						}
						$scope.priorities.push(priority);
					}
				}
				
				//Ext Customer
				
				$scope.setTicketinSession=function(ticket){
					var connectedDB = $scope.selectedCustList[0].custDBName;
					ticket.customerDB = connectedDB;
					if($scope.ticketCreatedOrAssigned=="CUSTOMER"){
						ticket.ticketAssignedType="CUSTOMER";
					}
					else{
						ticket.ticketAssignedType=$scope.ticketCreatedOrAssigned;
					}
					 ticketService.setIncidentSelected(ticket)
						.then(function(data){
							//console.log(data);
							if(data.statusCode==200){
								console.log("Ticket logged in session");
								$scope.sessionTicket = data.object;
							}
						},function(data){
							console.log(data);
						});
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
					$scope.viewRSPUpdatePage=function(){
						console.log($scope.sessionTicket);
						if($scope.sessionTicket!=null){
							if($scope.sessionTicket.statusId==15){
								$('#messageWindow').show();
								$('#infoMessageDiv').show();
								$('#infoMessageDiv').alert();
								$scope.InfoMessage="This ticket cannot be updated because the service is already restored."
				       		 }else{
				       			//${webContextPath}/serviceprovider/rsp/incident/create
								//Need to call rsp-incident-update.jsp
								window.location.href=hostLocation+"/serviceprovider/rsp/incident/update"
								$('#messageWindow').hide();
								$('#errorMessageDiv').hide();
				       		 }
						}else{
							$('#messageWindow').show();
							$('#infoMessageDiv').show();
							$('#infoMessageDiv').alert();
							$scope.InfoMessage="Please select a ticket to update."
						}
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
			    				
			    				$scope.displayExternalCustomerView($scope.pageViewFor);
			    			}
			            },
			            function(data) {
			                console.log('No User available')
			            });
					}
				
				$scope.checkTicketsAssignedOrCreated=function(ticketType){
					console.log(ticketType);
						$scope.siteList=[];
						$scope.assetList=[];
						$scope.ticketCreatedOrAssigned=ticketType;
						//keep this type to show task tab or not in update incident.
						$.jStorage.set('ticketType', ticketType);
						//End
						$scope.selectedRow=null;
						 $scope.sessionTicket=null;
						if($scope.ticketCreatedOrAssigned=="RSP"){
							$scope.findTicketsCreated();
						}else{
							$scope.ticketCreatedOrAssigned="CUSTOMER";
							$scope.getSPCustomerIncidents($scope.selectedCustList[0].spCode,$scope.selectedCustList[0].custDBName);
						}
					
				}
				
				$scope.displayExternalCustomerView=function(viewType){
					$scope.pageViewFor = viewType;
					console.log("displayExternalCustomerView--->",viewType);
					if($scope.pageViewFor=="INCIDENTS"){
						$scope.extCustList=[];
						$scope.extCustServiceProvider.slaListVOList=[];
						$scope.assetList=[];
						$scope.siteList=[];
						window.location.href=hostLocation+"/serviceprovidercompany/externalcustomers/incidents"
						
					}
					else if($scope.pageViewFor=="SITES"){
						$scope.spCustomerIncidentList.list=[];
						$scope.assetList=[];
						$scope.extCustList=[];
						$scope.extCustServiceProvider.slaListVOList=[];
						window.location.href=hostLocation+"/serviceprovidercompany/externalcustomers/sites"
					}
					else if($scope.pageViewFor=="ASSETS"){
						$scope.spCustomerIncidentList.list=[];
						$scope.siteList=[];
						$scope.extCustList=[];
						$scope.extCustServiceProvider.slaListVOList=[];
						window.location.href=hostLocation+"/serviceprovidercompany/externalcustomers/assets"
					}
					else if($scope.pageViewFor=="CUSTOMERS"){
						$scope.spCustomerIncidentList.list=[];
						$scope.siteList=[];
						$scope.assetList=[];
						$scope.getExternalCustomerData();
					}
				}
				$scope.getExternalCustomerData =function(){
					$('#loadingDiv').show();
					serviceProviderService.retrieveExternalCustomerRSP()
						.then(function(data) {
		    			console.log(data)
		    				$scope.extCustList=[];
		    				if(data.statusCode==200){
		    					$.each(data.object,function(key,val){
		    						$scope.extCustList.push(val);
		    					});
		    					$scope.rowHighilited(0);
		    					$scope.getExtCustPriorities($scope.extCustList[0]);
		    				}
		    				$('#loadingDiv').hide();
						},
    				 function(data) {
						console.log(data);	
	    				$('#loadingDiv').hide();
 		            });
				}
				
				
				
				
				//
				$scope.getSpCustomerList=function(){
					$('#loadingDiv').show();
					userService.getSPCustomerList().then(function(data) {
						console.log(data);
						if (data.statusCode == 200) {
							$scope.spCustomerList.list = [];
							$("#spCustomerListSelect").empty();
							if (data.object.length > 0) {
								$.each(data.object,function(key,val) {
									var custList = {
										custId : val.customerId,
										custCode : val.customerCode,
										custName : val.customerName,
										custDBName : val.custDBName,
										countryName : val.countryName,
										spCode:val.spCode
									}
									$scope.spCustomerList.list.push(custList);
								});
								// console.log(spCustomerList);
								var options = $("#spCustomerListSelect");
								console.log(options);
								options.append($("<option />").val("").text("Select Customer"));
								$.each($scope.spCustomerList.list,function() {
									options.append($("<option />").val(this.custCode).text(this.custName));
								});
							}
						}
						$('#loadingDiv').hide();
					},
					function(data) {
						console.log(data);
						$('#loadingDiv').hide();
					});

				}
				$scope.getSelectionOption = function(cCode, e,spCustomerListSelect) {
					// this array will store selected customer list
					// details.
						$scope.selectedCustList = [];
						/*if ($.fn.DataTable.isDataTable("#incidentDetails")) {
							  $('#incidentDetails').DataTable().clear().destroy();
						}*/
						for (var i = 0; i < $scope.spCustomerList.list.length; i++) {
							if ($scope.spCustomerList.list[i].custCode === cCode.value) {
								$scope.selectedCustList.push($scope.spCustomerList.list[i]);
								//$scope.spCustomerList.selected = $scope.spCustomerList.list[i];
							}
						}
		
						console.log("------------------->",$scope.selectedCustList[0]);
						
					// countryName
					console.log("cCode.value",cCode.value);
					if (cCode.value.length > 0) {
						console.log("Ifffffff111111--->",$scope.selectedCustList.length);   
						//$("#countryName").html("Country: "+$scope.selectedCustList[0].countryName);
						//console.log("countryName",$scope.selectedCustList[0].countryName);
						var encodedString = window.btoa($scope.selectedCustList[0].custDBName); 
						$scope.spCustomerList.selected = encodedString ;
						$.jStorage.set("selectedRSPCustomer", $scope.selectedCustList[0]);
						$.jStorage.set("selectedCustomer", $scope.spCustomerList.selected);
						$.jStorage.set("selectedCustomerCode", $scope.selectedCustList[0].custCode);
						$scope.getSPCustomerIncidents($scope.selectedCustList[0].spCode,$scope.selectedCustList[0].custDBName);
					} else {
						$scope.selectedCustList = [];
						console.log("ELseeeeeeeeeeeeeeee111111--->",$scope.selectedCustList.length);
						console.log("ELseeeeeeeeeeeeeeee",$scope.selectedCustList);
						$("#countryName").text(" ");
						if ($.fn.DataTable.isDataTable("#incidentDetails")) {
							  $('#incidentDetails').DataTable().clear().destroy();
						}
						
					}
				
				}
				
				
				$scope.getSPCustomerIncidents=function(spCode,dbName){
				userService.getSPCustomerTicketList(spCode,dbName)
				.then(function(data) {
					console.log("getSPCustomerTicketList----->",data)
					//keep this type to show task tab or not in update incident.
					$.jStorage.set('ticketType', 'CUSTOMER');
					//End
					if (data.statusCode == 200) {
						$scope.spCustomerIncidentList.list = [];
						if (data.object.length > 0) {
							/*if ($.fn.DataTable.isDataTable("#incidentDetails")) {
								  $('#incidentDetails').DataTable().clear().destroy();
							}*/
							$.each(data.object,function(key,val) {
								$scope.spCustomerIncidentList.list.push(val);
							});
						}
						$('#loadingDiv').hide();
						$("#countryName").text($scope.selectedCustList[0].countryName);
						//populateSPIncidentDataTable($scope.spCustomerIncidentList.list,'incidentDetails');					
					}
				},function(data) {
					console.log('Unable to change the status of the user');
				});
				}
				$scope.findTicketsCreated=function(){
					$('#loadingDiv').show();
					userService.getSPIncidentCreatedList($scope.selectedCustList[0].custDBName,$scope.ticketCreatedOrAssigned )
					.then(function(data){
						console.log(data);
						if(data.statusCode == 200){
							if ($.fn.DataTable.isDataTable("#incidentDetails")) {
								  $('#incidentDetails').DataTable().clear().destroy();
							}
							$scope.spCustomerIncidentList.list=[];
							$.each(data.object,function(key,val){
								$scope.spCustomerIncidentList.list.push(val);
				    			})
				    		$('#loadingDiv').hide();	
						populateSPIncidentDataTable($scope.spCustomerIncidentList.list,'incidentDetails');
						}
					},function(data){
						console.log(data);
						$('#messageWindow').hide();
						$('#infoMessageDiv').hide();
						$('#loadingDiv').hide();
					});
					
				}
				
				$scope.getSiteData=function(){
					var custCompCode = $scope.selectedCustList[0].custCode;
					var custDb  = 	$scope.selectedCustList[0].custDBName;
					siteService.retrieveSiteForSelectedCustomer(custDb, custCompCode)
						.then(function(data) {
			    			console.log(data)
			    				$scope.siteList=[];
			    				if(data.length>0){
			    					$.each(data,function(key,val){
			    						$scope.siteList.push(val);
			    					});
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
				//Sites Requiremnet
				
				$scope.findAllSites=function(){
					$('#loadingDiv').show();
					siteService.retrieveAllSites()
					.then(function(data) {
		    			//console.log(data)
		    				$scope.siteList=[];
		    				if(data.length>0){
		    					$.each(data,function(key,val){
		    						$scope.siteList.push(val);
		    					});
		    					$scope.getSiteDetails($scope.siteList[0]);
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
					
					// $scope.getDistrictByCountry($scope.sessionUser);
					 //$scope.getArea();
					// $scope.getCluster();
				}
				
				
				
				$scope.getSiteDetails=function(site){
					//console.log(site);
						$scope.getSelectedSiteData(site.siteId);
						//$scope.getSelectedSiteAttachments(site.siteId);
					}
				
				
				$scope.getSelectedSiteData=function(siteId){
					var custDb  = 	$scope.selectedCustList[0].custDBName;
					 siteService.retrieveCustomerSiteDetails(siteId,custDb)
			    		.then(function(data) {
			    			console.log(data)
			    			var site=angular.copy(data.object);
							$scope.selectedSite=angular.copy(site);
							$scope.selectedSite.siteName = site.siteName;
							$scope.selectedSite.siteNumber1 = site.siteNumber1;
							$scope.selectedSite.siteNumber2 = site.siteNumber2;
							$scope.selectedSite.salesAreaSize = site.salesAreaSize;
							$scope.selectedSite.siteAddress = site.fullAddress;
							
							$scope.selectedSite.siteAddress1 = site.siteAddress1;
							$scope.selectedSite.siteAddress2 = site.siteAddress2;
							$scope.selectedSite.siteAddress3 = site.siteAddress3;
							$scope.selectedSite.siteAddress4 = site.siteAddress4;
							
							$scope.selectedSite.district = site.district;
							$scope.selectedSite.area=site.area;
							$scope.selectedSite.cluster=site.cluster;
							
							$scope.district.selected=$scope.selectedSite.district;
							$scope.area.selected = $scope.selectedSite.area;
							$scope.cluster.selected = $scope.selectedSite.cluster;
							 
							$scope.selectedSite.retailerName=$scope.selectedSite.owner;
							$scope.selectedSite.primaryContact=site.primaryContact;
							$scope.selectedSite.secondaryContact=site.secondaryContact;
							
							$scope.selectedSite.LicenseDetail = site.siteLicense;
							$scope.selectedSite.SalesOperation = site.siteOperation;
							$scope.selectedSite.DeliveryOperation = site.siteDelivery;
							$scope.selectedSite.submeterDetails = site.siteSubmeter;
							 
							$scope.siteLicense = $scope.selectedSite.LicenseDetail;
							$scope.siteSalesOperation = $scope.selectedSite.SalesOperation;
							$scope.siteDeliveryOperation = $scope.selectedSite.DeliveryOperation;
							
							$scope.siteSubmeterDetails = $scope.selectedSite.submeterDetails;
							$scope.siteData.siteId = $scope.selectedSite.siteId;
			    			//$scope.siteData = angular.copy( $scope.selectedSite);
							
							$.jStorage.set('selectedSite', $scope.selectedSite);
							//console.log($scope.selectedSite.siteAttachments);
			    		},function(data){
			    			console.log(data);
			    			
			    		})
			    		
				 }
				
				$scope.viewImage=function(){
					 if($scope.selectedSite.fileInput!=null){
					  $('#imageviewer').html("<img src='"+$('#siteImg').val()+"' style='width:50%'></img>");
					 }
				 }
				
				$scope.searchCriteria={};
				$scope.setText=function(searchType){
					$('#searchSelection').html(searchType+ ' <span class="fa fa-caret-down"></span>');
					$scope.searchCriteria.filter=searchType;
				}
				
				var time=0.00;
				var daysList = ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'];
				var dayLength=7;
				for(i=0;i<24;i++){
					time=time+1;
					
					$scope.salesTimes.fromlist.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					$scope.salesTimes.toList.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					
					$scope.deliveryTimes.fromlist.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					$scope.deliveryTimes.toList.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					dayLength=dayLength-1;
				}
				//$scope.getOperationDetails();
				
				
				
				//
				
				
				
				
			//For Asset Requirement
				
				$scope.getAssetData =function(){
					var custCompCode = $scope.selectedCustList[0].custCode;
					$('#loadingDiv').show();
					siteService.retrieveAssetsForRSP(custCompCode)
						.then(function(data) {
		    			console.log(data)
		    				$scope.assetList=[];
		    				if(data.statusCode==200){
		    					$.each(data.object,function(key,val){
		    						$scope.assetList.push(val);
		    					});
		    				}
		    				$('#loadingDiv').hide();
						},
    				 function(data) {
						console.log(data);	
	    				$('#loadingDiv').hide();
 		            });
				
			    }
				
				$scope.getAssetDetails=function(asset){
					 siteService.getAssetInfo(asset.assetId)
					 .then(function(data){
						 console.log(data);
						if(data.statusCode == 200){
							$scope.selectedAsset=angular.copy(data.object);
						} 
					 },function(data){
						 console.log(data);
					 });
				}
			 
	/*			$scope.getSelectedSiteAssets=function(selectedSite){
					 $('#loadingDiv').show();
					 assetService.getAssetBySite(selectedSite)
							.then(function(data) {
			    			
			    			if(data.statusCode==200){
		    					if(data.object.length>0){
			    				$scope.asset.list=[];
				    			$.each(data.object,function(key,val){
				    				$scope.asset.list.push(val);
				    				$('#messageWindow').hide();
				    				$('#infoMessageDiv').hide();
				    				$('#loadingDiv').hide();
				    			})
				    			$scope.getAssetDetails($scope.asset.list[0]);
			    			  }
		    					else{
			    				  $scope.InfoMessage="No assets available for the user"
										$('#messageWindow').show();
					    				$('#infoMessageDiv').show();
					    				$('#infoMessageDiv').alert();
					    				$('#loadingDiv').hide();
			    			  	}
			    			}
			    				
				            },
				            function(data) {
				                console.log('Unable to get asset list')
				                	$scope.InfoMessage="No assets available for the user"
									$('#messageWindow').show();
				    				$('#infoMessageDiv').show();
				    				$('#infoMessageDiv').alert();
				    				$('#loadingDiv').hide();
				            });
					 }
				
				$scope.getAllAsset=function(){
					 $('#loadingDiv').show();
					 assetService.findAllAssets()
							.then(function(data) {
			    			console.log(data);
			    				if(data.statusCode==200){
			    					if(data.object.length>0){
					    				$scope.asset.list=[];
						    			$.each(data.object,function(key,val){
						    				$scope.asset.list.push(val);
						    				$('#messageWindow').hide();
						    				$('#infoMessageDiv').hide();
						    			});
						    				$scope.getAssetDetails($scope.asset.list[0]);
						    				 $scope.equipmentData.isDelete = 0;
											 $scope.serviceData.isDelete = 0;
											 $( '#toggledelete' ).parent().addClass('off');
						    				$('#loadingDiv').hide();
				    				
			    					}
			    				}
			    				else{
			    				  $scope.InfoMessage="No assets available for the user"
										$('#messageWindow').show();
					    				$('#infoMessageDiv').show();
					    				$('#infoMessageDiv').alert();
					    				$('#loadingDiv').hide();
			    			  	}
							
				            },
				            function(data) {
				                console.log('Unable to get asset list')
				                	$scope.InfoMessage="No assets available for the user"
									$('#messageWindow').show();
				    				$('#infoMessageDiv').show();
				    				$('#infoMessageDiv').alert();
				    				$('#loadingDiv').hide();
				            });
					 }
				
				$scope.getAssetDetails=function(asset){
					 assetService.getAssetInfo(asset.assetId)
					 .then(function(data){
						 console.log(data);
						if(data.statusCode == 200){
							$scope.selectedAsset=angular.copy(data.object);
							
							$.jStorage.set('selectedAsset', $scope.selectedAsset);
							//$rootScope.selectedAsset = $scope.selectedAsset;
							//$scope.send(asset.assetId);
						} 
					 },function(data){
						 
					 });
				
				
				}*/
				
			//End
				$scope.validateDropDownValues=function(e, i, dropDownId){
					 if(dropDownId.toUpperCase() == "REGIONSELECT"){
						 var region={
								 regionId:parseInt($("#regionSelect").val()),
								 regionName:$("#regionSelect option:selected").text()
						 }
						 $scope.extCustServiceProvider.region = region;
						 $scope.getCountryListBy(region);
					 }	 
					 else if(dropDownId.toUpperCase() == "COUNTRYSELECT"){
						 var country={
								 countryId:parseInt($("#countrySelect").val()),
								 countryName:$("#countrySelect option:selected").text()
						 }
						 $scope.extCustServiceProvider.countryId = country.countryId;
						 $scope.extCustServiceProvider.countryName = country.countryName;
					 }	 
					 
				}	
				
				 $scope.getSuccessMessage=function(msg){
					 $('#successDiv').show();
					 $('#successDiv').alert();
					 $('#successMessage').text(msg);
				}
				
				$scope.getErrorMessage=function(msg){
					 $('#errorDiv').show();
					 $('#errorDiv').alert();
					 $('#errorMessage').text(msg);
				}
		} 
	]);


function getAssetData(){
	var scope = angular.element("#spAssetData").scope();
	//scope.getAssetData();
}


function getSiteData(){
	var scope = angular.element("#spSiteData").scope();
	//scope.getSiteData();
}



	function populateSPIncidentDataTable(data, tableDivName) {
		console.log("populateSPIncidentDataTable", data);
		$('#' + tableDivName).dataTable({
			"aaData" : data,
			"bAutoWidth" : false,
			"order" : [ [ 0, "ticketNumber" ] ],
			"aoColumns" : [{
				"sTitle" : "Ticket ID",
				"mData" : "ticketId",
				"sClass": "hidden"
				
			}, {
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
		
		$('#incidentDetails_length').hide();
}