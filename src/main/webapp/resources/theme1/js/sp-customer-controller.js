chrisApp.controller('spCustomerController',
			[
				'$rootScope',
				'$scope',
				'$filter',
				'$location',
				'userService',
				'companyService',
				'registrationService',
				'authService',
				'siteService',
				'ticketService',
				function($rootScope, $scope, $filter, $location,
						userService,  companyService,
						registrationService, authService, siteService,ticketService) {

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
				$scope.selectedRow = 0;
				$scope.selectedAsset = {};
				$scope.operation = {};
				
				$scope.rowHighilited=function(row)
			    {
			      $scope.selectedRow = row;    
			    }
				
				
				//

				angular.element(document).ready(function() {
					$scope.selectedSite={};
					$scope.selectedSite.fileInput=null;
					 $scope.sessionTicket=null;
					$scope.getLoggedInUserAccess();	
					$scope.ticketCreatedOrAssigned="CUSTOMER";
					$scope.pageViewFor="INCIDENTS";
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
				
				$scope.getCurrentTicket=function(){
					//Row Selection
					
				}
				
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
			    				$scope.getSpCustomerList();
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
						if($scope.ticketCreatedOrAssigned=="RSP"){
							$scope.findTicketsCreated();
						}else{
							$scope.ticketCreatedOrAssigned="CUSTOMER";
							$scope.getSPCustomerIncidents($scope.selectedCustList[0].spCode,$scope.selectedCustList[0].custDBName);
						}
					
				}
				
				$scope.displayCustomerView=function(viewType){
					$scope.pageViewFor = viewType;
					if($scope.pageViewFor=="INCIDENTS"){
						$scope.checkTicketsAssignedOrCreated($scope.ticketCreatedOrAssigned);
						
					}
					else if($scope.pageViewFor=="SITES"){
						$scope.spCustomerIncidentList.list=[];
						$scope.assetList=[];
						$scope.getSiteData();
					}
					if($scope.pageViewFor=="ASSETS"){
						$scope.spCustomerIncidentList.list=[];
						$scope.siteList=[];
						$scope.getAssetData();
					}
				}
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