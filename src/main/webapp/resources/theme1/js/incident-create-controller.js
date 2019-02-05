
chrisApp.controller('incidentCreateController',  ['$rootScope', '$scope', '$filter','siteService','authService',
                                        'siteCreationService','companyService','userService','districtService',
                                        'areaService','clusterService','countryService','assetService',
                                        'ticketCategoryService','ticketService','serviceProviderService','statusService',
                              function  ($rootScope, $scope , $filter,siteService, authService,
                                        siteCreationService,companyService,userService,districtService,
                                        areaService,clusterService,countryService,assetService,
                                        ticketCategoryService,ticketService,serviceProviderService,statusService) {
		
		
		$scope.ticket={
			 selected:{},
			 list:[]
		}
		
		$scope.assetTypechecked = 'undefined';
		$scope.linkedTicketDetails=[];
		$scope.selectedLinkedTicketDetails=[];
		$scope.escalationLevelDetails=[];
		$scope.ticketHistoryDetail={};
		$scope.ticketData={};
		$scope.selectedSite={};
		$scope.selectedAsset={};
		$scope.selectedCategory={};
		$scope.selectedTicketStatus={};
		$scope.selectedCloseCode={};
		$scope.sessionUser={};
		$scope.slaPercent = '60';
		$scope.ticketComments=[];
		$scope.files = [];
		$scope.incidentImageList=[];
		$scope.incidentImages=[];
		$scope.totalIncidentImageSize = 0;
		$scope.statusList=[];
		$scope.status={
				selected:{},
				 list:[]	
		}
		$scope.accessSite={
				 selected:{},
				 list:[]
		 }
		$scope.selectedAsset={
				 selected:{},
				 list:[]
		 }
		$scope.selectedCategory={
				 selected:{},
				 list:[]
		 }
		$scope.selectedTicketStatus={
				 selected:{},
				 list:[]
		 }
		//Added by Supravat for Financials Requirement.
		$scope.financialCostDetails = [];
		//Ended by Supravat.
		//Added by Supravat for Related Ticket Requirement
		$scope.relatedTicketData = [];
		//Ended by Supravat.
		$scope.assignedPMStatusIDs=[];
		 $scope.assetCategory={
				 selected:{},
				 equipmentList:[],
				 serviceList:[]
				 
		 }
		 $scope.assetLocation={
				 selected:{},
				 list:[]
		 }
		 $scope.serviceProvider={
				 selected:{},
				 list:[]
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
		
		$scope.selectedEscalation={};
		
		$scope.categoryList=[];
		$scope.assetList=[];
		
		$scope.siteAssignedUserList=[];
		$scope.siteUnAssignedUserList=[];
		
		
		var viewMode = null
		angular.element(document).ready(function(){
			//console.log("loaded");
			$scope.initalizeCloseDiv();	
			viewMode = $('#mode').val();
			$scope.getLoggedInUserAccess();	
			$scope.selectedTicket={};
			$scope.assetType = null;
			if(viewMode.toUpperCase() == 'EDIT'){
				$scope.getIncidentSelected();
				$scope.refreshing=false;
			}
			$scope.originateFromPage = "Incident";
			$.jStorage.set('originateFrom', $scope.originateFromPage);
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
		
		 $scope.getImageFile=function(assetId, e){
				var ext = $('#inputImgfilepath').val().split(".").pop().toLowerCase();
				$scope.fileSize1 = null;
				$scope.fileExtension1 = ext;
				if ($.inArray(ext, [ "jpg", "jpeg", "JPEG",	"JPG", "png", "PNG" ]) == -1) {
					$('#equipmentModalMessageDiv').show();
					$('#equipmentModalMessageDiv').alert();
					$scope.equipmentModalErrorMessage = "";
					 $('#fileerrorasset').text("Supported file types to upload are jpg and png");
					$scope.isfileselected = false;
					$('#inputImgfilepath').val('');
					$('#inputImgfilepath').val(null);
					return false;
				} else if (e.target.files != undefined) {
					$('#equipmentModalMessageDiv').hide();
					$scope.isfileselected = true;
					file = $('#inputImgfilepath').prop('files');
					$scope.fileSize1 = file[0].size / 1024;
					if($scope.fileSize1 > 100){
			        	 $scope.equipmentModalErrorMessage="";
			        	 $('#fileerrorasset').text("File size exceeds 100KB");
				    	 $('#equipmentModalMessageDiv').show();
						
			         }else{
						var reader = new FileReader();
						reader.onload = $scope.onImageFileUploadReader;
						reader.readAsDataURL(file[0]);
			         }
				}else{
					 $('#fileerrorasset').text("Invalid file format");
			    	 $('#equipmentModalMessageDiv').show();
			    	 $('#inputImgfilepath').val('');
			    	 $('#inputImgfilepath').val(null);
				}
			 }
			 
			 $scope.getDocumentFile=function(serviceId, e, errorDiv, msgDiv){
				 var documentId = serviceId.id
				 var ext = $('#' + documentId).val().split(".")	.pop().toLowerCase();
				$scope.fileSize2 = null;
				$scope.fileExtension2 = ext;
				if ($.inArray(ext, [ "PDF", "pdf"]) == -1) {
					$('#'+msgDiv).text("Supported file types to upload is pdf");
					$('#'+errorDiv).show();
					$('#'+errorDiv).alert();
					$scope.serviceModalErrorMessage = "";
					$scope.isfileselected = false;
					$('#' + documentId).val('');
					$('#' + documentId).val(null);
					return false;
				} else if (e.target.files != undefined) {
					$('#'+errorDiv).hide();
					$scope.isfileselected = true;
					file = $('#' + documentId).prop('files');
					$scope.fileSize2 = file[0].size / 1024;
					if($scope.fileSize2 > 100){
			        	 $scope.serviceModalErrorMessage="";
			        	 $('#'+msgDiv).text("File size exceeds 100KB");
				    	 $('#'+errorDiv).show();
						
			         }else{
						var reader = new FileReader();
						reader.onload = $scope.onFileDocUploadReader;
						reader.readAsDataURL(file[0]);
			         }
				}else{
					 $('#'+msgDiv).text("Invalid file format");
					 $('#'+errorDiv).show();
			    	 $('#' + documentId).val('');
			    	 $('#' + documentId).val(null);
				}
			 }
			 
			 $scope.onImageFileUploadReader=function(e){
					var fileUrl = e.target.result;
					$scope.assetImageFile=e.target.result;
				}
			 
			 $scope.onFileDocUploadReader=function(e){
					var fileUrl = e.target.result;
					$scope.assetDocFile=e.target.result;
				}
		
		$scope.getIncidentSelected=function(){
			$scope.suggestedTickets=[];
			ticketService.getSelectedTicketFromSession()
			.then(function(data){
				//console.log("Ticket DetailsXXXX")
				//console.log(data)
				if(data.statusCode == 200){
					$scope.ticketData=angular.copy(data.object);
					$scope.suggestedTickets = angular.copy(data.object2);
					//$scope.ticketData.createdBy=$scope.sessionUser.username;
					$scope.getAttachments($scope.ticketData.ticketId);
					if(viewMode.toUpperCase()=="EDIT"){
						$scope.optionVal="ZERO";
	    				$scope.getLinkedTicketDetails($scope.ticketData.ticketId, $scope.ticketData.ticketAssignedType);
    				}
					$scope.setSLAWidth($scope.ticketData);
					$scope.getUserRoleStatusMap();
					$scope.getTicketCategory();
					$scope.getEscalationLevel();
					$scope.changeStatusDescription($scope.ticketData.statusDescription);
					//$('#statusDesc').text("Description: "+ $scope.ticketData.statusDescription);
					//Added By Supravat for Financials Requirements.
										
					$scope.getFinancialCosts();
					//Ended By Supravat.
					$scope.getTicketHistory();
					if($scope.ticketData.statusId == 15){
						$scope.getCloseCode();
						$('#closeNote').prop("disabled", true);
						$.each($scope.closeCodeList,function(key,val){
							if(val.code == $scope.ticketData.closeCode){
								$scope.ticketData.codeClosed=val.code;								
								$('#closeCode').prop("disabled", true);
								return false;
							}
						});
						
						$('#closeCode').prop("disabled",true);
					}
					/*if(data.object.ticketComments.length>0){
						 $scope.ticketData.comments = data.object.ticketComments;
						 $scope.ticketComments=[];
						 $.each(data.object.ticketComments,function(key,val){
							  $scope.ticketComments.push(val);
						 })
						 
					}*/
					
					if($scope.ticketData.attachments.length>0){
						$scope.ticketData.files=[];
						$.each($scope.ticketData.attachments,function(key,val){
							var  fileInfo={
									fileId:val.attachmentId,
									fileName: val.attachmentPath.substring(val.attachmentPath.lastIndexOf("/")+1),
									createdOn: val.createdDate,
									filePath: val.attachmentPath
							}
							$scope.ticketData.files.push(fileInfo);
						});
					}
					$scope.getStatus();
				}
				
			},function(data){
				console.log(data)
			});
		}
		
		 $scope.getTicketComments=function(){
			 var ticketId = $scope.ticketData.ticketId;
				ticketService.listComment(ticketId)
				.then(function(data){
					if(data.statusCode==200){
						 $scope.ticketData.comments = data.object;
						 $scope.ticketComments=[];
						 $.each(data.object,function(key,val){
							  $scope.ticketComments.push(val);
						 })
					}
				},function(data){
					console.log(data);
				});
		 }
		 
		
		$scope.setSLAWidth=function(ticketData){
            if(ticketData.slaPercent > 100){
                $scope.ticketData.width = 100;                
            }
            else{
                $scope.ticketData.width = ticketData.slaPercent;
            }                
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
		    
		 $scope.getUserRoleStatusMap=function(){
			 userService.getUserRoleStatusMap()
			 .then(function(data){
				 //console.log(data);
				 $scope.assignedPMStatusIDs=[];
				 if(data.statusCode==200){
					 if(data.object.length>0){
						 $.each(data.object,function(key,val){
							 $scope.assignedPMStatusIDs.push(val);
						 });
					 }
				 }
			 },function(data){
				console.log(data) 
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
    				$scope.retrieveAssetCategories();
    			    if(viewMode=="NEW"){
    			    	$scope.getStatus();
    			    }
    				$scope.equipmentData ={};
    				$scope.equipmentData.company=$scope.sessionUser.company;
    				//$scope.getServiceProviders($scope.equipmentData.company);
    				$scope.getAssetLocations();
    				$scope.getAllSites();
    				//$scope.getUserSiteAccess();
    				//$scope.getTicketCategory();
    				//$scope.getTicketPriority();
    			//	$scope.getAsset();
    			//	$scope.getCloseCode();
    				//$scope.setTicketPriorityAndSLA();
    				
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
		$scope.getAllSites=function(){
			$('#loadingDiv').show();
			siteService.retrieveAllSites()
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
    					var selectedSiteId = $('#siteSelectedId').val(); // From Asset Screen if Asset is selected
    					//var siteSelected = $.jStorage.get('selectedSite');	//  After asset is created and returned back to incident
    					
    					if(selectedSiteId==""){
    					  $.jStorage.set('selectedAsset',null);
    					  $.jStorage.set('selectedSite', null);	
    					}else{
    					//selectedSiteId = siteSelected.siteId;
    					var fromAssetSelected = $.jStorage.get('selectedAsset');
    					if(fromAssetSelected!=null ){
		    				$scope.assetType = $.jStorage.get('selectedAsset').assetType=="E"?"EQUIPMENT":"SERVICE";
	    					if(selectedSiteId!=null){
								 $("#siteSelect option").each(function() {
									if ($(this).val() == selectedSiteId) {
										$(this).attr('selected', 'selected');
										 $scope.selectedSiteObject={
												 siteId:parseInt(selectedSiteId),
												 siteName:$("#siteSelect option:selected").text(),
										 }
										$scope.getAsset($scope.selectedSiteObject);
										//$scope.serviceRepairType.selected.subCategoryId1=$scope.selectedAsset.subCategoryId1;
										// $scope.getServiceSubRepairType($scope.serviceRepairType.selected);
										return false;
									}
							 	});
	    					}
					      }
    					}
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
		
		$scope.linkedTicket={
				'ticketNumber':''
		};
		$scope.ticketComment={
				'comment':''
		};
		
		
		$scope.getEscalationLevel=function(){
			if(viewMode.toUpperCase()=='EDIT'){
				$scope.escalationLevelDetails=[];
					$.each($scope.ticketData.escalationLevelList,function(key,val){
						var escLevelData={
								escId:val.escId,
								spId:val.serviceProviderId,
								escLevelId:val.levelId,
								escLevelDesc:val.escalationLevel,
								escTo:val.escalationPerson,
								escEmail:val.escalationEmail,
								ticketNumber:$scope.ticketData.ticketNumber,
								ticketId:$scope.ticketData.ticketId,
								escStatus:val.status,
						};
						$scope.escalationLevelDetails.push(escLevelData);
					});
					initializeEscalateTicket();
				}
		}
		
		//Added By Supravat for Financials Requirements.
		$scope.getFinancialCosts=function(){
			$scope.isCostSaveButton= true;
			$scope.isCostDeleteButton= true;
			$scope.costNewRowCount= 0;
			
			$scope.financialCostDetails=[];
				$.each($scope.ticketData.financialList,function(key,val){						
					var fincancialCostData={
							ticketID:val.ticketId,
							costId:val.id,
							costName:val.costName,
							cost:val.cost,
							chargeBack:val.chargeBack,
							billable:val.billable,
							isDeleteCheck:false,
							isEdited:false,
					};
					$scope.financialCostDetails.push(fincancialCostData);
				});
		}
		//Ended By Supravat.
		
		$scope.addNewTicket=function(){
			//console.log("open popup");
			//console.log($scope.sessionUser.loggedInUserMail)
			$('#createTicketModal').modal('show');
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
		
		$scope.initalizeCloseDiv=function(){
			$('#ticketCloseDiv').hide();
			$("#closeCodeSelect").attr('required', false);
			$("#raisedOn").attr('required', false);
			$("#closeNote").attr('required', false);
		}
		
		$scope.getStatus=function(){
			if(viewMode.toUpperCase()=='NEW'){
				statusService.retrieveAllStatus("NA")
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
				statusService.retrieveAllStatus("NA")
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
    				//ticketStatusChange('statusSelect');
    				//Commented above line by Supravat to stop call on page load.
                },function(data){
                	//console.log(data);
                });
			}
		}
		
		$scope.getCloseCode=function(){
			$('#ticketCloseDiv').show();
			$("#closeCodeSelect").attr('required', true);
			$("#raisedOn").attr('required', true);
			$("#closeNote").attr('required', true);			
			$scope.closeCodeList =[{
				'id':1,
				'code':'Yes - Root cause fixed'
			},
			{
				'id':2,
				'code':'No - But workaround provided'
			}];
			
			$("#closeCodeSelect").empty();
			
			var options = $("#closeCodeSelect");
			options.append($("<option />").val("").text("Select option"));
			$.each($scope.closeCodeList,function() {
				options.append($("<option />").val(	this.id).text(	this.code));
			});
			$('#closedBy').val($scope.sessionUser.email);
			$scope.ticketData.closedBy = $scope.sessionUser.email;
		}
		
		
		$scope.getUserSiteAccess=function(){
			 $('#loadingDiv').show();
			 userService.getUserSiteAccess()
				.then(function(data) {
	    			//console.log(data);
	    			if(data.statusCode == 200){
	    				if(data.object.length>0){
	    					$scope.accessSite.list=[];
	    					$("#siteSelect").empty();
	    					
		    				$.each(data.object,function(key,val){
		    					var accessedSite={
			    						accessId:val.accessId,
			    						site:val.site,
			    						siteId:val.site.siteId,
			    						siteName:val.site.siteName
			    				}
		    					$scope.accessSite.list.push(accessedSite);
		    					
		    				});
		    				
		    				var options = $("#siteSelect");
	    					options.append($("<option />").val("").text("Select Site"));
	    					$.each($scope.accessSite.list,function() {
								options.append($("<option />").val(	this.siteId).text(this.siteName));
							});
	    					
	    					$('#loadingDiv').hide();
	    				}
	    				
	    			}
	            },
	            function(data) {
	                console.log('Unable to get access list')
	            });
		 }
		
		
		$scope.getAsset=function(selectedSite){
			if(selectedSite.siteId==""){
				$scope.assetType=null;
				selectedSite=null;
			}else{
			 $.jStorage.set('selectedSite',selectedSite);
			 $scope.accessSite.selected.siteId = parseInt(selectedSite.siteId);
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
		}
		
		 $scope.populateAssetType=function(type){
			 
			 var selectedSite = $('#siteSelect').val();
			 if(selectedSite == ""){
				 $.jStorage.set('selectedAsset', null);
				 
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
					
					if(equipmentList.length==0){
						$('#messageWindow').show();
						$('#errorMessageDiv').show();
						$('#errorMessageDiv').alert();
						$scope.errorMessage="No assets available for Equipments for "+ $('#siteSelect option:selected').text();
					}else{
						$('#messageWindow').hide();
					}
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
					
					if(serviceList.length==0){
						$('#messageWindow').show();
						$('#errorMessageDiv').show();
						$('#errorMessageDiv').alert();
						$scope.errorMessage="No assets available for Service for "+ $('#siteSelect option:selected').text();
					}else{
						$('#messageWindow').hide();
					}
						$("#assetSelect").empty();
						 var options = $("#assetSelect");
			    			options.append($("<option />").val("").text(
			    			"Select asset"));
			    			$.each(serviceList,function() {
			    					options.append($("<option />").val(	this.assetId).text(	this.assetName));
			    			});
					
		    			
			      }
			 
					if($scope.assetList.length>0){
						var selectedAsset = $.jStorage.get('selectedAsset');
						if(selectedAsset!=null){
						 $("#assetSelect option").each(function() {
								if ($(this).val() == selectedAsset.assetId) {
									$(this).attr('selected', 'selected');
									 $scope.selectedAssetObject={
											 assetId:selectedAsset.assetId,
											 assetName:$("#assetSelect option:selected").text(),
											 assetSP:selectedAsset.spHelpDeskEmail
									 }
									getSelectedAsset("ASSETSELECT");
									 
									return false;
								}
						 	});
						}
						
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
			     
	     //----------------------- View ticket ------------------------------------
	     
		 $scope.rowHighilited=function(row)
		    {
		      $scope.selectedRow = row;    
		    }
		 
		 $scope.getTicketDetails=function(ticket){
			$scope.selectedTicket={};
			ticketService.retrieveTicketDetails(ticket)
			.then(function(data){
				console.log(data);
				if(data.statusCode==200){
					$scope.selectedTicket = data.object
					 $scope.previewSelectedIncidentInfo($scope.selectedTicket);
				}
			},function(data){
				console.log(data);
			});
		}

		 
		 $scope.setTicketServiceProvider=function(asset){
			 $scope.ticketData.sp=asset.serviceProviderId;
			 $scope.ticketData.spType=asset.spType;
		 }
		 
		 $scope.setTicketPriorityAndSLA=function(ticketCategory){
			 //console.log($scope.ticketData);
			 var spId = $scope.ticketData.sp;
			 var spType=$scope.ticketData.spType;
			 if(viewMode.toUpperCase()=='EDIT'){
				 spId=parseInt($scope.ticketData.assignedTo);
				 spType=$scope.ticketData.ticketAssignedType;
		      }
			 if(spId==undefined){
				 //alert("No Site Selected");
				 return false;
			 }else{
				 $('#loadingDiv').show();
			 ticketService.getTicketPriorityAndSLA(spId,ticketCategory.categoryId,spType)
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
		 $scope.setTicketraisedOnDate=function(){
			 
			 $scope.CurrentDate = new Date();
			 $scope.CurrentDate = $filter('date')(new Date(), 'dd-MM-yyyy');
			 //$scope.ticketData.raisedOn = $scope.CurrentDate;
		 }
		
		 
		 $scope.saveTicket=function(){				 	          
			 console.log("save ticket called");
			 
			 $scope.ticketData.siteId=$scope.accessSite.selected.siteId;
			 $scope.ticketData.siteName=$scope.accessSite.selected.siteName;
			 
			 $scope.ticketData.assetId=$scope.assetList.selected.assetId;
			 $scope.ticketData.assetName=$scope.assetList.selected.assetName;
			 $scope.ticketData.assetCategoryId=$scope.assetList.selected.assetCategoryId;
			 $scope.ticketData.asstSpType = $scope.assetList.selected.spType;
			 $scope.ticketData.assignedTo = $scope.assetList.selected.assignedTo;
			 $scope.ticketData.categoryName=$scope.categoryList.selected.categoryName;
			 $scope.ticketData.subCategoryId1=$scope.assetList.selected.subCategoryId1;
			 $scope.ticketData.subCategoryId2 = $scope.subRepairType.selected.subCategoryId2;
			
			 $scope.ticketData.assetCategoryName=$scope.assetList.selected.assetCategoryName;
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
			$scope.persistTicket($scope.ticketData, "NEW");
		 }
		 
		 $scope.updateTicket=function(){
			 $scope.ticketData.statusId=parseInt($('#statusSelect').val());
			 $scope.ticketData.status=$('#statusSelect option:selected').text();
			 if(parseInt($("#closeCodeSelect").val())!=0){
				 $scope.ticketData.closeCode =  parseInt($("#closeCodeSelect").val());
			 }
			 //console.log($scope.ticketData);
			 $scope.ticketData.sla = $('#sla').val();
			 $scope.ticketData.mode="UPDATE";
			 $scope.persistTicket($scope.ticketData, "UPDATE");
			//Added by Supravat for allowing SLA Due Date to update on incident update screen.
			 //To Refresh SLA Percentage
			// window.location.href=hostLocation+"/incident/details/update";
			 //Ended By Supravat.
			 
		 }
		 $scope.persistTicket=function(ticketData, type){
			// $('#loadingDiv').show();
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
					  $.jStorage.set('selectedAsset',null);
					  $.jStorage.set('selectedSite', null);	
					 	$scope.successMessage = data.message;
					 	 $scope.getSuccessMessage($scope.successMessage);
	    				 $scope.incidentImageList=[];
    					 $scope.incidentImages=[];
    					 $('#totalIncidentSize').text("0KB");
	    				if(type.toUpperCase()=='NEW'){
	    					window.location.href=hostLocation+"/incident/details";
	    				}
	    				else if(type.toUpperCase()=='UPDATE'){
	    					//if($scope.ticketData.statusId==7){
	    						window.location.href=hostLocation+"/incident/details/update";
	    					//}
	    				}else{
	    					if(type.toUpperCase()=='UPLOAD'){
	    						$scope.successMessage = "Files uploaded successfully";
	    						 $scope.getSuccessMessage($scope.successMessage);
	    	    				
	    	    				$scope.getAttachments(ticketData.ticketId)
	    	    				/*if(data.object.attachments.length>0){
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
	    	    				}*/
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

		 $scope.closeMessageWindow=function(){
			 $('#messageWindow').hide();
			 $('#modalMessageDiv').hide();
			 $('#serviceModalMessageDiv').hide();
			 $('#equipmentModalMessageDiv').hide();
			 $('#incidentImageModalMessageDiv').hide();
			 
		 }
	$scope.getTicketComment=function(){
		$scope.ticketComments;
	}
	
	$scope.addNewComment = function(){
		//console.log("comment added");
		$scope.CurrentDate = new Date();
		$scope.CurrentDate = $filter('date')(new Date(), 'dd-MM-yyyy');
		if($scope.ticketComment.comment != ""){
			/*$scope.ticketComments.push({ 
				  ticket_id:$scope.ticketData.ticketId,
				  ticketNumber : $scope.ticketData.ticketNumber,
				  createdBy:$scope.sessionUser.email,
				  createdDate:$scope.CurrentDate,
				  Comment:$scope.ticketComment.comment
			 });*/
			var ticketComment={
					  ticketId:$scope.ticketData.ticketId,
					  ticketNumber : $scope.ticketData.ticketNumber,
					  comment:$scope.ticketComment.comment
			}
			 //console.log(ticketComment);
			
			 ticketService.saveComment(ticketComment)
			 .then(function(data){
				 //console.log(data);
				 if(data.statusCode == 200){
					 $("#ticketMessage").val("");
					 $scope.getTicketComments();
				 }
			 },function(data){
				 //console.log(data);
			 });
		}
		 
	};
	$scope.selectLinkTicket=function(optionSelected){
		if(optionSelected=="ZERO"){
			$scope.optionVal=optionSelected;
		}
		else if(optionSelected=="ONE"){
			$scope.optionVal=optionSelected;
		}
	}
	$scope.LinkNewTicket = function(spType, spTicket){
		  var ticketLinked=null; 
		  if(spTicket.ticketNumber != ""){
		  var isDuplicateTicket=false;
			if(spType=="EXT"){
				if($scope.linkedTicket.ticketNumber != "" ||  $scope.linkedTicket.ticketNumber != null){	
					var linkedTicket={
							parentTicketId:$scope.ticketData.ticketId,
							parentTicketNo:$scope.ticketData.ticketNumber,
							linkedTicketNo:$scope.linkedTicket.ticketNumber,
							spId:$scope.ticketData.assignedTo,
							spType:spType
					}
					ticketLinked = linkedTicket;
				}
				$.each( $scope.ticketData.linkedTickets,function(key,val){
					if($scope.linkedTicket.ticketNumber == val.spLinkedTicket){
						isDuplicateTicket =true;
						return false;
					}
				});
				
			}
			else if(spType=="RSP"){
				$scope.linkedTicket.ticketNumber = spTicket.ticketNumber;
				var linkedTicket={
						parentTicketId:$scope.ticketData.ticketId,
						parentTicketNo:$scope.ticketData.ticketNumber,
						linkedTicketNo:$scope.linkedTicket.ticketNumber,
						spId:$scope.ticketData.assignedTo,
						spType:spType
				}
				ticketLinked = linkedTicket;
				$.each($scope.linkedRspTickets,function(key,val){
					if($scope.linkedTicket.ticketNumber == val.spLinkedTicket){
						isDuplicateTicket =true;
						
						return false;
					}
				});
			}
			if(!isDuplicateTicket){
				if(ticketLinked.linkedTicketNo == null){
					/*$('#messageWindow').show();
					$('#errorMessageDiv').show();
					$('#errorMessageDiv').alert();*/
					$scope.errorMessage="Please enter the link ticket number";
					 $scope.getErrorMessage($scope.errorMessage);
				}else{
					ticketService.linkTicket(ticketLinked)
					.then(function(data){
						//console.log(data);
						if(data.statusCode == 200){
						 $("#linkedTicket").val("");
						 $scope.linkedTicket.ticketNumber = null;
						 $scope.getSuccessMessage("Ticket linked successfully");
						 $scope.getLinkedTicketDetails(ticketLinked.parentTicketId, ticketLinked.spType);
						}
					},function(data){
						//console.log(data);
					});
				}
			}else{
					$scope.linkedTicket.ticketNumber = null;
					$("#linkedTicket").val("");
				/*	$('#messageWindow').show();
					$('#errorMessageDiv').show();
					$('#errorMessageDiv').alert();*/
					$scope.errorMessage="Ticket number is already linked.";
					 $scope.getErrorMessage($scope.errorMessage);
			}
		  }else{
				/*$('#messageWindow').show();
				$('#errorMessageDiv').show();
				$('#errorMessageDiv').alert();*/
				$scope.errorMessage="Please enter the link ticket number";
				 $scope.getErrorMessage($scope.errorMessage);
		  }
		}
	

	
	$scope.getLinkedTicketDetails=function(linkedTicket, spType){
		ticketService.getLinkedTickets(linkedTicket)
		.then(function(data){
			//console.log(data);
			if(data.statusCode == 200){
			 if(spType=="EXT"){	
				 $("#linkedTicket").val("");
				 if(data.object.linkedTickets.length>0){
					 $scope.ticketData.linkedTickets = data.object.linkedTickets;
				 }else{
					 $scope.ticketData.linkedTickets=[];
				 }
			 }
			 if(spType=="RSP"){
				 if(data.object.linkedTickets.length>0){
					 $scope.linkedRspTickets = data.object.linkedTickets;
				 }else  {
					 $scope.linkedRspTickets=[];
				 }
			 }
			}
		},function(data){
			//console.log(data);
		});
	}
	

	
	$scope.getSelectedEscalation=function(selectedEscalation,id){
		if($("#chkEscalation"+id).prop("checked")){
			$scope.selectedEscalation = angular.copy(selectedEscalation)
			$scope.selectedEscalation.ticketData=$scope.ticketData;
		}
		else{
			$scope.selectedEscalation ={};
		}
		
		//console.log($scope.selectedEscalation);
	}
	
	$scope.escalateTicketConfirmation=function(){
		//console.log($scope.selectedEscalation);
		if($.isEmptyObject($scope.selectedEscalation)){
		/*	$('#messageWindow').show();
			$('#errorMessageDiv').show();
			$('#errorMessageDiv').alert();*/
			$scope.errorMessage="No Escalation level selected."
				 $scope.getErrorMessage($scope.errorMessage);
		}
		else if($scope.selectedEscalation != "undefined"){
			//console.log($scope.escalationLevelDetails);
			$('#confirmEscalate').modal('show');
		}
	}
	
	$scope.closeLinkedTicketConfirmation=function(){
		//console.log($scope.selectedEscalation);
		if($scope.selectedLinkedTicketDetails.length ==0 ){
			/*$('#messageWindow').show();
			$('#errorMessageDiv').show();
			$('#errorMessageDiv').alert();*/
			$scope.errorMessage="At a time select 1 opened linked ticket to close the status."
				 $scope.getErrorMessage($scope.errorMessage);
				
		}
		else if($scope.selectedLinkedTicketDetails.length > 0){
			//console.log($scope.escalationLevelDetails);
			$('#confirmClose').modal('show');
		}
	}
	
	$scope.unlinkTicketConfirmation=function(index, linkedTicket){
		$('#confirmUnlink').modal('show');
		$scope.unlinkTicketIndex = index;
		$scope.unlinkTktObject = angular.copy(linkedTicket);
		
	}
	
	$scope.escalateTicket=function(){
		//console.log($scope.selectedEscalation);
		if($.isEmptyObject($scope.selectedEscalation)){
			/*$('#messageWindow').show();
			$('#errorMessageDiv').show();
			$('#errorMessageDiv').alert();*/
			$scope.errorMessage="No Escalation level selected."
				 $scope.getErrorMessage($scope.errorMessage);
		}
		else if($scope.selectedEscalation != "undefined"){
			//console.log($scope.escalationLevelDetails);			
			//call API to save in DB
			//call API to save in DB
			ticketService.escalateTicket($scope.selectedEscalation)
			.then(function(data){
				//console.log(data);
				if(data.statusCode ==200){
					$scope.selectedEscalation.escStatus = data.object.escalationStatus;
					angular.forEach($scope.escalationLevelDetails, function(escalation){
						if(escalation.escId == data.object.escId){
							escalation.escStatus = $scope.selectedEscalation.escStatus;
							$('#confirmEscalate').modal('hide');
							return false;
						}
						
					});
					 $scope.getSuccessMessage("Ticket escalated successfully");
				}
				initializeEscalateTicket();
				$scope.getLinkedTicketDetails($scope.ticketData.ticketId, $scope.ticketData.ticketAssignedType);
				$scope.selectedEscalation ={};
			},function(data){
				//console.log(data);
			});
		}
		//console.log("initialized");
		//$scope.initializeEscalateTicket();
		
	}
	
	$scope.getTicketHistory=function(){
		var ticketId =  $scope.ticketData.ticketNumber;
		ticketService.getTicketHistory(ticketId)
		.then(function(data){
			//console.log(data);
			if(data.statusCode == 200){
				var ticketHistory={};
				ticketHistory.ticketId=$scope.ticketData.ticketNumber;
				ticketHistory.ticketStartDate=$scope.ticketData.raisedOn;
				ticketHistory.ticketCloseDate=$scope.ticketData.serviceRestorationTime;
				if(data.object.length>0){
					var	history=[];
					$.each(data.object,function(key,val){
						var ticketHistory={
								name:val.who,
								date:val.timeStamp,
								description:val.message	
						};
						history.push(ticketHistory)
					});
					ticketHistory.history=history;
					
				}else{
					
				}
				$scope.ticketHistoryDetail=angular.copy(ticketHistory);
				
				
			}
		},function(data){
			//console.log(data)
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
	    	// $('#messageWindow').show();
	    	 $scope.incidentImageModalErrorMessage="";
	    	 $('#incidentImageModalMessageDiv').show();
        	/* $('#errorMessageDiv').show();
        	 $('#errorMessageDiv').alert();*/
        	 $('#fileerrorincident').text('Supported file types to upload are jpg,  png and pdf');
        	 $scope.getErrorMessage("Supported file types to upload are jpg,  png and pdf");
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
	 				/*	 $('#messageWindow').show();
	 					 $('#errorMessageDiv').show();
	 		        	 $('#errorMessageDiv').alert();*/
	 					$('#fileerrorincident').text("File size exceeds 1 MB");
	 					 $scope.getErrorMessage("File size exceeds 1 MB");
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
    
    $scope.removeImage=function(indexPos){
    	 var totalSize=0;
		  $.each($scope.incidentImages,function(key,val){
			  totalSize =  totalSize + parseInt(val.fileSize);
		  });
		  
		  $.each($scope.incidentImageList,function(key,val){
			  if(val.id==indexPos){
				  $scope.incidentImageList.splice(indexPos,1);	
				  return false;
			  }
		  });
		  $.each($scope.incidentImages,function(key,val){
			 if(val.imgPos==indexPos) {
				 $scope.incidentImages.splice(indexPos,1);		
				 totalSize = totalSize - val.fileSize ;
				// $('#totalIncidentSize').text(totalIncidentImageSize+" KB");
				 $('#totalSize').text("Files ( "+$scope.incidentImages.length +" ) Total Size : "+ totalSize +" KB");
				 return false;
			 }
		  });
			if(totalSize > 1024){
					$('#incidentImageModalMessageDiv').show();
					 $('#fileerrorincident').text("File size exceeds 1 MB");
					 $scope.getErrorMessage("File size exceeds 1 MB");
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
    
    
    						//Added by Supravat for Related Ticket Requirements						    
    
						    $scope.getRelatedTicketDetails = function(ticketFor){
						    	
						    	var relatedTktInputData = null;
						    	var relTicketData = null;
						    	relatedTktInputData = {
						    			ticketId:$scope.ticketData.ticketId,
						    			siteId:$scope.ticketData.siteId
						    	};						    	
						    	
								ticketService.getRelatedTicketData(relatedTktInputData,ticketFor)
								 .then(function(relatedTktData){
									 $scope.relatedTicketData = [];
									 
									 if(relatedTktData.statusCode==200){
										 
										 $.each(relatedTktData.object,function(key,val){
												relTicketData={
														ticketId:val.ticketId,
														ticketNumber:val.ticketNumber,
														title:val.ticketTitle,
														asset:val.assetName,
														statusId:val.statusId,
														status:val.status,
												};
												$scope.relatedTicketData.push(relTicketData);
											});
										 
									 } else {
										 $scope.errorMessage = relatedTktData.message;
										 $scope.getErrorMessage($scope.errorMessage);
									 }
									
								 },function(relatedTktData){
								 });
								
							}
						    $scope.previewSelectedIncidentInfo=function(ticket){
								 $('#previewIncidentModal').modal('show');
							 }
						  /*  $scope.setTicketinSession=function(ticket){
								 ticketService.setIncidentSelected(ticket)
									.then(function(data){
										if(data.statusCode==200){
											$scope.sessionTicket = data.object;
											//window.location.href=hostLocation+"/incident/details/view";
											 $scope.previewSelectedIncidentInfo($scope.sessionTicket);
										}
									},function(data){
										$('#loadingDiv').hide();
									});
							 }*/
    						//Ended By Supravat
    
    						//Added By Supravat for Financials Requirements.
								$scope.enabledEdit = [];
								$scope.editCostDetails = function(index){
									$scope.financialCostDetails[index].isEdited=true;
									$scope.enabledEdit[index] = true;
									$scope.isCostSaveButton= false;
								}
								
								$scope.saveCostDetails = function(financialsForm){
									$('#loadingDiv').show();
									ticketService.saveFinalcialsCostItems($scope.financialCostDetails,"")
									 .then(function(data){
										 if(data.statusCode==200){
											 $scope.getIncidentSelected();
											 $('#loadingDiv').hide();
											 $scope.successMessage = data.message;
											 $scope.enabledEdit = [];
											 $scope.getSuccessMessage($scope.successMessage);
										 } else {
											 $('#loadingDiv').hide();
											 $scope.errorMessage = data.message;
											 $scope.getErrorMessage($scope.errorMessage);
										 }
										
									 },function(data){
										 //console.log(data);
										 $('#loadingDiv').hide();
									 });
									 $('#loadingDiv').hide();
									
								}
								
								$scope.deleteCostChange=function() {
									$('#messageWindow').hide();
									$('#successMessageDiv').hide();
									$('#errorMessageDiv').hide();
									var isChecked = 0;
									angular.forEach($scope.financialCostDetails,function(value,index){
										if(value.isDeleteCheck == true) {
											isChecked++;
										}
									})
									if(isChecked>0) {
										if($scope.costNewRowCount>0) {
											$scope.isCostDeleteButton= true;
										} else {
											$scope.isCostDeleteButton= false;
										}
									} else {
										$scope.isCostDeleteButton= true;
									}
								}
								
								$scope.deleteCostFinancialItems=function(){
									$('#confirmCostDelete').appendTo("body").modal('show');
								}
								
								
								$scope.confirmCostDelete=function(){
									$('#loadingDiv').show();
									ticketService.deleteFinalcialsCostItems($scope.financialCostDetails,"")
									 .then(function(data){
										 if(data.statusCode==200){
											 $scope.getIncidentSelected();
											 
											 $scope.isCostSaveButton= true;
											 $scope.isCostDeleteButton= true;
											 
											 $('#loadingDiv').hide();
											 $('#confirmCostDelete').appendTo("body").modal('hide');				 
											 $scope.successMessage = data.message;
											 
											 $scope.getSuccessMessage($scope.successMessage);
										 } else {
											 $('#loadingDiv').hide();
											
											 $scope.errorMessage = data.message;
												$scope.getErrorMessage($scope.errorMessage);
										 }
										
									 },function(data){
										 //console.log(data);
										 $('#loadingDiv').hide();
									 });
									 $('#loadingDiv').hide();
									 
								 }
								 
								 
								$scope.addNewRow = function() {
									$('#messageWindow').hide();
									$('#successMessageDiv').hide();
									$('#errorMessageDiv').hide();
									$scope.isCostSaveButton= false;
									$scope.costNewRowCount++;
									$scope.financialCostDetails.push({
										"costId":"",
										"ticketID": $scope.ticketData.ticketId,
										"costId" : "",
										"costName" : "",
										"cost" : 0.00,
										"chargeBack" : "",
										"billable" : ""
									})
								}

								$scope.removeRow = function(index) {
									//console.log("beofre",$scope.costNewRowCount);
									$scope.costNewRowCount--;
									$scope.financialCostDetails.splice(index, 1);
									//console.log("After",$scope.costNewRowCount);
									if($scope.costNewRowCount>0) {
										$scope.isCostSaveButton= false;
									} else {
										$scope.isCostSaveButton= true;
										var isChecked = 0;
										angular.forEach($scope.financialCostDetails,function(value,index){
											if(value.isDeleteCheck == true) {
												isChecked++;
											}
										})
										if(isChecked>0) {
											$scope.isCostDeleteButton= false;
										} else {
											$scope.isCostDeleteButton= true;
										}
									}
								}	
								//Ended By Supravat.					 
    
    // NOW UPLOAD THE FILES.
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
    
    /*// UPDATE PROGRESS BAR.
    function updateProgress(e) {
        if (e.lengthComputable) {
            document.getElementById('pro').setAttribute('value', e.loaded);
            document.getElementById('pro').setAttribute('max', e.total);
        }
    }
    
 // CONFIRMATION.
    function transferComplete(e) {
        alert("Files uploaded successfully.");
    }*/
    
	$scope.openAssetModal=function(){
		 var selectedSite = $('#siteSelect').val();
		 if(selectedSite == ""){
			 $('#messageWindow').show();
				$('#successMessageDiv').hide();
				$('#errorMessageDiv').show();
				$('#errorMessageDiv').alert();
				$scope.errorMessage="No site selected";
			 
		 }else{
			if($scope.assetTypechecked == 'E'){
				$('#equipmentModalMessageDiv').hide();
				$scope.addEquipmentModal();
			}
			else if($scope.assetTypechecked == 'S'){
				$('#serviceModalMessageDiv').hide();
				$scope.addServiceModal();
			}else{
				$('#messageWindow').show();
				$('#successMessageDiv').hide();
				$('#errorMessageDiv').show();
				$('#errorMessageDiv').alert();
				$scope.errorMessage="Please select an option Equipment / Service to add asset";
			}
		 }
	}
	
	$scope.addEquipmentModal=function(){
		//var scope = angular.element("#incidentWindow").scope();
		 //console.log("shibasish");
		 $scope.equipmentData={};
		 $scope.equipmentData.sites=[];
		 $('#resetAssetForm').click();
		 $("#categorySelect option").each(function() {
				if ($(this).val() == "") {
					$(this).attr('selected', 'selected');
					 $scope.assetCategory.selected=null;
					return false;
				}
		 	});
		
		$("#locationSelect option").each(function() {
			if ($(this).val() == "") {
				$(this).attr('selected', 'selected');
				 $scope.assetLocation.selected=null;
				return false;
			}
		});
		
		$("#spSelect option").each(function() {
			if ($(this).val() == "") {
				$(this).attr('selected', 'selected');
				 $scope.serviceProvider.selected=null;
				return false;
			}
		});
		 //$scope.accessSite.selected={};
		/* $("#siteSelect option").each(function(){
		 		if($(this).val() == ""){
		 			$(this).attr('selected', 'selected');
		 			 $scope.accessSite.selected=null;
					return false;
		 		}
		 	});*/
		 	$("#drpIsAsset option").each(function(){
			if($(this).val() == ""){
				$(this).attr('selected', 'selected');
				$scope.equipmentData.isAssetElectrical=null;
				return false;
			}
		});
		
		$("#drpIsPowersensor option").each(function(){
			if($(this).val() == ""){
				$(this).attr('selected', 'selected');
				$scope.equipmentData.isPWSensorAttached=null;
				return false;
			}
		});
		 $('#equipmentModal').modal('show');
		 $('#assetModalLabel').text("Add new Asset for "+ $scope.accessSite.selected.siteName);
	}

	$scope.addServiceModal=function(){
		 $scope.serviceData={};
		 $scope.serviceData.sites=[];
		 $('#resetServiceAssetForm').click();
			$("#categorySelect option").each(function() {
				if ($(this).val() == "") {
					$(this).attr('selected', 'selected');
					 $scope.assetCategory.selected=null;
					return false;
				}
		 	});
	 	
	 	$("#locationSelect option").each(function() {
			if ($(this).val() == "") {
				$(this).attr('selected', 'selected');
				 $scope.assetLocation.selected=null;
				return false;
			}
		});
	 	
	 	$("#spSelect option").each(function() {
			if ($(this).val() == "") {
				$(this).attr('selected', 'selected');
				 $scope.serviceProvider.selected=null;
				return false;
			}
		});
	 	
	 	
		 $('#serviceModal').modal('show');
		 $('#assetServiceModalLabel').text("Add new Asset for "+ $scope.accessSite.selected.siteName);
	 }
	
	 $scope.getServiceProviders=function(customer){
		 $('#loadingDiv').show();
			serviceProviderService.getServiceProviderByCustomer(customer)
			.then(function(data) {
    			//console.log(data);
    			$scope.serviceProvider.list=[];
    			$("#spSelect").empty();
    			$("#serviceSPSelect").empty();
    			if(data.statusCode == 200){
    				$.each(data.object,function(key,val){
    					$scope.serviceProvider.list.push(val);
    				});
    			}
    			var options = $("#spSelect");
				options.append($("<option />").val("").text(
				"Select Service Provider"));
				$.each($scope.serviceProvider.list,function() {
					options.append($("<option />").val(	this.serviceProviderId).text(this.name));
				});
				
				var options = $("#serviceSPSelect");
				options.append($("<option />").val("").text("Select Service Provider"));
				$.each($scope.serviceProvider.list,function() {
					options.append($("<option />").val(	this.serviceProviderId).text(this.name));
				});
				 $('#loadingDiv').hide();
            },
            function(data) {
                console.log('Unable to get  Service Provider list')
                $('#loadingDiv').hide();
            });
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
		
	 $scope.IsValidDate = function (decommissionDate, commissionDate) {				 
			
		 var isValid = false;
		 //console.log(decommissionDate);
		 if(decommissionDate == null || decommissionDate == "" || typeof(decommissionDate) === "undefined"){
			 isValid = true;					 
		 }
		 else{					 
			 var isSame;
			 var part1 = decommissionDate.split('-');
			 var part2 = commissionDate.split('-');				 
			 decommissionDate =  new Date(part1[1]+'-'+part1[0]+'-'+part1[2]);
			 commissionDate =  new Date(part2[1]+'-'+ part2[0]+'-'+ part2[2]);				 
	         isValid = moment(decommissionDate).isAfter(commissionDate);
	          if (!isValid){
	        	  isValid = moment(decommissionDate).isSame(commissionDate);
	          }	
		 }		         	          
          
          return isValid;
      }
	
	
	 
	 $scope.saveAssetEquipment=function(){				 	          
		 $scope.equipmentData.sites.push($scope.accessSite.selected.siteId);
         if($scope.IsValidDate($scope.equipmentData.deCommissionedDate,$scope.equipmentData.commisionedDate)){
       	  $('#equipmentModalMessageDiv').hide();
       	  $scope.equipmentData.categoryId=$scope.assetCategory.selected.assetCategoryId;
				 $scope.equipmentData.locationId=$scope.assetLocation.selected.locationId;
				 $scope.equipmentData.siteId=$scope.accessSite.selected.siteId;
				 $scope.equipmentData.subCategoryId1 = $scope.repairType.selected.subCategoryId1;
				 $scope.equipmentData.subCategoryId2 = $scope.subRepairType.selected.subCategoryId2;
				 if($scope.serviceProvider.selected!=null){
					 $scope.equipmentData.serviceProviderId=$scope.serviceProvider.selected.serviceProviderId;
				 }
				 //console.log($scope.equipmentData);
				// $scope.equipmentData.sites.push($scope.selectedSite.siteId);
				 
				 var assetImage={
							fileName:$scope.equipmentData.assetName,
							base64ImageString:$scope.assetImageFile,
							fileExtension: $scope.fileExtension1,
							size:$scope.fileSize1 || 0
						}
				 $scope.equipmentData.assetImage=assetImage;
				 var assetDoc={
							fileName:$scope.equipmentData.assetName,
							base64ImageString:$scope.assetDocFile,
							fileExtension: $scope.fileExtension2,
							size:$scope.fileSize2 || 0
							
						}
				 $scope.equipmentData.assetDoc = assetDoc;
				 if( $scope.equipmentData.assetImage.size > 100){
					 $scope.equipmentModalErrorMessage="File size exceeds Max limit (100KB).";
	            	 $('#equipmentModalMessageDiv').show();
	            	 $('#equipmentModalMessageDiv').alert();
	                  $scope.isfileselected=false;
	                  return false;
	             }
				 if( $scope.equipmentData.assetDoc.size > 100){
					 $scope.equipmentModalErrorMessage="File size exceeds Max limit (100KB).";
	            	 $('#equipmentModalMessageDiv').show();
	            	 $('#equipmentModalMessageDiv').alert();
	                  $scope.isfileselected=false;
	                  return false;
	             }else{
	            	 $scope.equipmentModalErrorMessage="";
	            	 $('#equipmentModalMessageDiv').hide();
	            	 $scope.saveAssetInfo($scope.equipmentData);
	             }
				
         }
         else{
       	  $scope.modalErrorMessage = "Decommission date should be after Commission date";
               $('#equipmentModalMessageDiv').show();
               $('#equipmentModalMessageDiv').alert();							
         }		          
		 
	 }
	 
	 $scope.saveAssetService =function(){
		 $scope.serviceData.sites.push($scope.accessSite.selected.siteId);
		 if($scope.IsValidDate($scope.serviceData.deCommissionedDate,$scope.serviceData.commisionedDate)){
        	  $('#serviceModalMessageDiv').hide();
        	  $scope.serviceData.categoryId=$scope.assetCategory.selected.assetCategoryId;
				 $scope.serviceData.locationId=$scope.assetLocation.selected.locationId;
				 $scope.serviceData.subCategoryId1 = $scope.serviceRepairType.selected.subCategoryId1;
				 $scope.serviceData.subCategoryId2 = $scope.serviceSubRepairType.selected.subCategoryId2;
				 $scope.serviceData.siteId=$scope.accessSite.selected.siteId;
				 if($scope.serviceProvider.selected!=null){
				   $scope.serviceData.serviceProviderId=$scope.serviceProvider.selected.serviceProviderId;
				 }
		//		 $scope.serviceData.sites.push($scope.selectedSite.siteId);
				 
				 var serviceDoc={
							fileName:$scope.serviceData.assetName,
							base64ImageString:$scope.assetDocFile,
							fileExtension: $scope.fileExtension2,
							size:$scope.fileSize2 || 0
						}
				 $scope.serviceData.assetDoc = serviceDoc;
				 
				 if($scope.serviceData.assetDoc.size > 100){
	            	 $scope.serviceModalErrorMessage="File size exceeds Max limit (100KB).";
	            	 $('#serviceModalMessageDiv').show();
	            	 $('#serviceModalMessageDiv').alert();
	                  $scope.isfileselected=false;
	                  return false;
	             }else{
	            	 $scope.serviceModalErrorMessage="";
	            	 $('#serviceModalMessageDiv').hide();
	            	 $scope.saveAssetInfo($scope.serviceData);
	             }
				
          }
          else{
        	  	$scope.serviceModalErrorMessage = "Decommission date should be after Commission date";
                $('#serviceModalMessageDiv').show();
				$('#serviceModalMessageDiv').alert();							
          }		     
		
	 }
	 $scope.saveAssetInfo=function(assetData){
		 $('#loadingDiv').show();
		 assetService.saveAssetObject(assetData)
		 .then(function(data) {
    			//console.log(data);
    			if(data.statusCode == 200){
    				$scope.successMessage = data.message;
    				$('#messageWindow').show();
    				$('#successMessageDiv').show();
    				$('#successMessageDiv').alert();
    				$('#loadingDiv').hide();
    				if(assetData.assetType == 'E'){
    					$('#assetModalCloseBtn').click();
    				}
    				else if(assetData.assetType == 'S'){
    					$('#serviceModalCloseBtn').click();
    				}
    				//$scope.getAllAsset();
    				$('#loadingDiv').hide();
    				$scope.getAsset($scope.accessSite.selected);
    			}
            },
            function(data) {
            	 console.log('Error while saving asset data')
	              //  $('#equipmentModalMessageDiv').show();
 				//$('#equipmentModalMessageDiv').alert();
 				if(assetData.assetType == 'E'){
 					  $scope.equipmentModalErrorMessage = data.message;
 					$('#equipmentModalMessageDiv').show();
	    				$('#equipmentModalMessageDiv').alert();
 				}
 				else if(assetData.assetType == 'S'){
 					$scope.serviceModalErrorMessage = data.message;
 					$('#serviceModalMessageDiv').show();
	    				$('#serviceModalMessageDiv').alert();
 				}
 				$('#loadingDiv').hide();
            });
	 }
	 
	 $scope.getSelectedLinkedTicket=function(selectedLinkedTicket){
			
		 //console.log($scope.ticketData.linkedTickets);
		 //console.log(selectedLinkedTicket);
		 var selectedLinkedTicketCount = 0;
			$scope.selectedLinkedTicket = angular.copy(selectedLinkedTicket);
				if($scope.selectedLinkedTicketDetails != "undefined"){
					selectedLinkedTicketCount = $scope.ticketData.linkedTickets.length;
				}
			//$scope.selectedLinkedTicketDetails.push($scope.selectedLinkedTicket);
			//console.log($scope.selectedLinkedTicketDetails);
			
			if($scope.selectedLinkedTicket != "undefined"){
				//$scope.selectedEscalation.escStatus = 'escalated';
				if(selectedLinkedTicketCount == 0){
					$scope.selectedLinkedTicketDetails.push($scope.selectedLinkedTicket);
				}
				else if(selectedLinkedTicketCount >0){
					for(key in selectedLinkedTicket){
						if(key=="selected" && selectedLinkedTicket.selected==true){
							if(selectedLinkedTicket.closedFlag=="CLOSED"){
								$('#closedBtn').prop("disabled",true);
							return false;
							}else{
							$scope.selectedLinkedTicketDetails.push($scope.selectedLinkedTicket);
							$('#closedBtn').prop("disabled",false);
							return false;
							}
						}
						
						if(key=="selected" && selectedLinkedTicket.selected==false){
							$scope.selectedLinkedTicketDetails.pop($scope.selectedLinkedTicket);
						}
					}
					
				}
				
				//console.log($scope.selectedLinkedTicketDetails);
				
			}
			
		}

	 $scope.closeLinkedTicket=function(){
		 //console.log($scope.selectedLinkedTicketDetails.length);
		 if($scope.selectedLinkedTicketDetails.length==1){
			 var linkedTicket = {};
			 linkedTicket.detail= $scope.selectedLinkedTicketDetails[0];
			 linkedTicket.status="CLOSED";
			 ticketService.changeLinkedTicketStatus(linkedTicket)
			 .then(function(data){
				 if(data.statusCode==200){
					 if(data.object.linkedTickets>0){
						 $scope.ticketData.linkedTickets = data.object.linkedTickets
					 }
					 $scope.getLinkedTicketDetails($scope.ticketData.ticketId, linkedTicket.spType);
					 $scope.selectedLinkedTicketDetails = [];
					 $('#confirmClose').modal('hide');
				 }
			 },function(data){
				 //console.log(data);
			 });
		 }else if($scope.selectedLinkedTicketDetails.length ==0 ){
			 //alert("At a time single ticket status can be closed")
			 /*	$('#messageWindow').show();
				$('#errorMessageDiv').show();
				$('#errorMessageDiv').alert();*/
				$scope.errorMessage="At a time select 1 opened linked ticket to close the status."
					$scope.getErrorMessage($scope.errorMessage);
		 }
	 }
	 
	 $scope.unlinkTicket=function(){
		 var linkedTicket = angular.copy($scope.unlinkTktObject);
		 ticketService.deleteLinkedTicket(linkedTicket)
		 .then(function(data){
			 //console.log(data);
			 if(data.statusCode == 200){
				 $scope.ticketData.linkedTickets =  data.object;
				 for(key in linkedTicket){
						if(key=="selected" && $scope.selectedLinkedTicket.selected==false){
							$scope.selectedLinkedTicketDetails.pop($scope.selectedLinkedTicket);
						}
					}
				 $scope.getLinkedTicketDetails($scope.ticketData.ticketId, linkedTicket.spType);
				 $('#confirmUnlink').modal('hide');
			 }
		 },function(data){
			 console.log(data);
		 });
	 }
	 
	 $scope.linkRspTicket=function(selectedRspTicket){
		 
	 }
	 
	 $scope.openChatBox=function(){
			$('#chatWindow').fadeIn();
			$scope.chatBoxView="ON";
			var ticketId = $scope.ticketData.ticketId;
			$scope.getTicketComments();
	}
	$scope.closeWindow=function(){
		$('#chatWindow').fadeOut();
	}

	$scope.deleteFile=function(feature, file){
		//console.log($scope.ticketData.files);
		 $('#loadingDiv').show();
		var incidentAttached=[];
		incidentAttached.push(file.fileId);
		var fileStr = incidentAttached.join(",");
		ticketService.deleteFileAttached(feature, fileStr)
		.then(function(data){
			//console.log(data);
			if(data.statusCode==200){
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
					$('#loadingDiv').hide();
				}
				 $('#loadingDiv').hide();
			}
		},function(data){
			//console.log(data);
			$('#loadingDiv').hide();
		});
	}
	
	$scope.changeStatusDescription=function(description){
		$scope.ticketData.statusDescription=description;
		$('#statusDesc').text("Description: "+ description);
	}
	
	$scope.getAttachments=function(ticketId){
		ticketService.getTicketAttachment($scope.ticketData.ticketId)
		.then(function(data){
			if(data.statusCode==200){
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
			}
		},function(data){
			console.log(data);
		})
		
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
			 
}]);


function getSelectedSite(dropDownId){
	$("#subrepairtypeSelect").empty();
	$('#category').empty();
	$('#componentType').empty();
	var scope = angular.element("#incidentCreateWindow").scope();
	if(dropDownId.toUpperCase() == "SITESELECT"){
		 var site={
				 siteId:parseInt($("#siteSelect").val()),
		 		 siteName:$("#siteSelect option:selected").text()
		 }
		 scope.accessSite.selected =site;
		 scope.getAsset(site);		
	 }
}

function getSelectedAsset(dropDownId){
	var scope = angular.element("#incidentCreateWindow").scope();
	if(dropDownId.toUpperCase() == "ASSETSELECT"){
		 var asset={
				 assetId:parseInt($("#assetSelect").val()),
		 		 assetName:$("#assetSelect option:selected").text()
		 }
		 scope.assetList.selected = asset;
		 $.each(scope.assetList,function(key,val){
			if(val.assetId == asset.assetId){
				console.log(val);
				$('#assignedTo').val(val.serviceProviderName);
				$('#category').val(val.assetCategoryName);
				$('#componentType').val(val.assetSubcategory1);
				var category={
						assetCategoryId:val.categoryId,
						assetCategoryName:val.assetCategoryName
				}
				scope.assetList.selected.assetCategoryName=category.assetCategoryName;
				scope.assetList.selected.assignedSp=val.serviceProviderName;
				scope.assetList.selected.assignedTo=val.serviceProviderId;
				scope.assetList.selected.spHelpDeskEmail=val.spHelpDeskEmail;
				//scope.selectedAssetObject.assetSP=val.spHealDeskEmail;
				scope.assetList.selected.assetSubcategory1=val.assetSubcategory1;
				scope.assetList.selected.assetCategoryId=val.categoryId;
				scope.assetList.selected.spType=val.spType;
				if(val.assetType=='E'){
					scope.getRepairType(category);
					if(val.subCategoryId1!=null){
						scope.assetList.selected.subCategoryId1=val.subCategoryId1
						scope.getSubComponentType(val.subCategoryId1);
					}else{
						scope.subRepairType.list=[];
		    			$("#subcomponentTypeSelect").empty();
					}
				}else if(val.assetType=='S'){
					scope.getServiceRepairType(category);
					if(val.subCategoryId1!=null){
						scope.assetList.selected.subCategoryId1=val.subCategoryId1
						scope.getServiceSubRepairType(val);
					}else{
						scope.subRepairType.list=[];
		    			$("#subcomponentTypeSelect").empty();
					}
				}
				scope.setTicketServiceProvider(val);
				return false;
			} 
		 });
		 
		 
		 scope.getTicketCategory();
	 }
}

function getSelectedCategory(dropDownId){
	var scope = angular.element("#incidentCreateWindow").scope();
	if(dropDownId.toUpperCase() == "TICKETCATEGORYSELECT"){
		 var category={
				 categoryId:parseInt($("#ticketCategorySelect").val()),
		 		 categoryName:$("#ticketCategorySelect option:selected").text()
		 }
		 scope.categoryList.selected =category;
		 //scope.getTicketPriority();
		 scope.setTicketPriorityAndSLA(scope.categoryList.selected);
	 }
}

function getSelectedPriority(dropDownId){
	var scope = angular.element("#incidentWindow").scope();
	if(dropDownId.toUpperCase() == "PRIORITYSELECT"){
		 var priority={
				 categoryId:parseInt($("#prioritySelect").val()),
		 		 categoryName:$("#prioritySelect option:selected").text()
		 }
		 scope.priorityList.selected =priority;
	 }
}

////Added by Supravat for Default selected Status Validation
////Role Status Mapping Validation.
//function checkRoleStatusMapping(){
//	var scope = angular.element("#incidentCreateWindow").scope();
//	var statusSelectedIndex = document.getElementById("statusSelect").selectedIndex;
//	if (scope.assignedPMStatusIDs.indexOf(statusSelectedIndex) === -1) {
//		return true;
//	} else {
//		//var valueId = document.getElementById("statusSelect").selectedIndex;
//	    var valueDescription = document.getElementById("statusSelect").options;	    
//		$("#btnSavePrimary").prop("disabled", true);	
//		$('#StatusRoleValidation').modal('show');		
//		document.getElementById('StatusRoleValidationMSG').innerHTML = "You are not authorized to select the status to " + valueDescription[statusSelectedIndex].text;
//		return false;
//	}
//}
//End

function ticketStatusChange(dropDownId){
	var scope = angular.element("#incidentCreateWindow").scope();
	var valueId = parseInt($("#"+dropDownId).val());
	//scope.selectedSite = valueId;
	//Added by Supravat for Status Validation
	//Role Status Mapping Validation.
	var statusSelectedIndex = document.getElementById("statusSelect").selectedIndex;
	var valueDescription = document.getElementById("statusSelect").options;
	var defaultSelectedStatusID = scope.ticketData.statusId;
	var isBackStatusToIssueRaised = false;
	if(scope.ticketData.statusId != 1) {
		if(valueId===1) {
			isBackStatusToIssueRaised = true;
		}
	}
	if(isBackStatusToIssueRaised === true) {
		
		$("#btnSavePrimary").prop("disabled", true);
		$('#StatusRoleValidation').modal('show');
		
		document.getElementById('StatusRoleValidationMSG').innerHTML = "You can not change the status back to Issue Raised.";
		document.getElementById("statusSelect").selectedIndex = scope.ticketData.statusId;
		
		return false;
	}
	
	if (scope.assignedPMStatusIDs.indexOf(valueId) === -1) {
		$("#btnSavePrimary").prop("disabled", true);
		$('#StatusRoleValidation').modal('show');
		
		document.getElementById('StatusRoleValidationMSG').innerHTML = "You are not authorized to change the status to " + valueDescription[statusSelectedIndex].text;
		document.getElementById("statusSelect").selectedIndex = scope.ticketData.statusId;
			
		return false;
	} else {
		$("#btnSavePrimary").prop("disabled", false);
		if(valueId == 15){
			scope.getCloseCode();
			scope.ticketData.statusId = valueId;
			
		}
		else{
			$('#ticketCloseDiv').hide();
			$("#closeCodeSelect").attr('required', false);
			$("#raisedOn").attr('required', false);
			$("#closeNote").attr('required', false);
			$.each(scope.ticketData.statusInfoList,function(key,val){
				if(val.statusId == valueId){
					scope.changeStatusDescription(val.description);
					return false;
				}
			});
		}
		
		if(dropDownId.toUpperCase() == "STATUSSELECT"){
			 var ticketStatus={
					 statusId:parseInt($("#statusSelect").val()),
			 		 statusName:$("#statusSelect option:selected").text()
			 }
			 scope.statusList.selected =ticketStatus;
		 }
	}
	//Ended by Supravat for Status Validation
	//console.log(selectedStatusId);
}

function validateDropdownValues(dropDownId, assetType){
	var scope = angular.element("#incidentCreateWindow").scope();
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

function initializeEscalateTicket(){
	var scope = angular.element("#incidentCreateWindow").scope();
	var escalated=false;		
	var escalationLevelCount = scope.escalationLevelDetails.length;
	if(escalationLevelCount > 0){			
		
		for(var i = 0; i <= escalationLevelCount-1; i++){				
			//if(scope.escalationLevelDetails[i].escStatus!=null){
			if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i].escStatus.toUpperCase() == 'ESCALATED'){					
				$("#chkEscalation"+i).prop("disabled", true);	
				$("#chkEscalation"+(i+1)).prop("disabled", false);
				escalated=true;					
				$("#chkEscalation"+i).prop("checked", false);					
			}
			else if(escalated){					
				if((i) < escalationLevelCount-1){
					$("#chkEscalation"+(i+1)).prop("disabled", true);
				}					
				if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i].escStatus.toUpperCase() == 'ESCALATED'){
				if((i) == escalationLevelCount-1){
					$("#chkEscalation"+(i+1)).prop("disabled", false);						
				}					
				}
				else if(i == escalationLevelCount-1){
					if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i-1].escStatus.toUpperCase() != 'ESCALATED'){
						$("#chkEscalation"+i).prop("disabled", true);
					}
					if(scope.escalationLevelDetails[i].escStatus!=null && $scope.escalationLevelDetails[i-1].escStatus.toUpperCase() == 'ESCALATED'){
						$("#chkEscalation"+i).prop("disabled", false);
					}
				}					
				else if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i].escStatus.toUpperCase() != 'ESCALATED'){
					if(scope.escalationLevelDetails[i].escStatus!=null && $scope.escalationLevelDetails[i-1].escStatus.toUpperCase() == 'ESCALATED'){							
						$("#chkEscalation"+i).prop("disabled", false);
					}
				}
				else if(scope.escalationLevelDetails[i].escStatus!=null && scope.escalationLevelDetails[i].escStatus.toUpperCase() != 'ESCALATED'){
					if((i) == escalationLevelCount-1){
						$("#chkEscalation"+i).prop("disabled", false);							
					}						
				}					
			}
			//}
			}
		// enable only 1st level
		
		if(!escalated){
			$("#chkEscalation0").prop("disabled",false);
			for(var i = 1; i <= escalationLevelCount-1; i++){
				$("#chkEscalation"+i).prop("disabled", true);
			}
		}
		
	}
};

function getTicketHistory(){
	var scope = angular.element("#incidentCreateWindow").scope();
	scope.getTicketHistory();
}

//Added by Supravat for Related Ticket Requirement
function getRelatedTicketDetails(ticketFor){
	var scope = angular.element("#incidentCreateWindow").scope();
	scope.getRelatedTicketDetails(ticketFor);
}
//Ended by Supravat.


	
