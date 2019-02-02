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
							$scope.status={
									selected:{},
									 list:[]	
							}
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
							  //Added by Supravat for Incident Tasks
							 $scope.incidentTask={};
							
							 $scope.ticketType = {};
							 //End
							$scope.suggestedTickets = [];
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
							var viewMode = null;
							angular.element(document).ready(function(){
								console.log("loaded 11111111");
								//Added to show task tab or npt based on ticketType -- RSP -- CUSTOMER
								$scope.ticketType = {};
								$scope.ticketType = $.jStorage.get('ticketType');
								console.log("ticketType----->",$scope.ticketType);
								//End
								$scope.initalizeCloseDiv();	
								viewMode = $('#mode').val();
								$scope.getLoggedInUserAccess();	
								$scope.selectedTicket={};
								
								if(viewMode.toUpperCase() == 'EDIT'){
									$scope.getIncidentSelected();
									$scope.refreshing=false;
									$scope.optionVal="ZERO";
								}
								$scope.incidentImageList=[];
								$scope.incidentImages=[];
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
							
							//Added by Supravat for Incident Tasks
							$scope.rowTaskHighilited=function(row)
							 {
								 $scope.selectedTaskRow = row;    
							 }
							
							$scope.getIncidentTasks=function(){
								 $.jStorage.set('incidentTask',null);
								 var ticketNumber =  $scope.ticketData.ticketNumber;
								 console.log("Inside getIncidentTask ticketNumber--->",ticketNumber);
								 $scope.incidentTask.list = [];
								 console.log("$scope.incidentTask List--->",$scope.incidentTask.list);
								 $.jStorage.set('incidentTask', $scope.incidentTask);
							      
							  ticketService.getIncidentTaskDetails($scope.ticketData.ticketId)//Need to pass incident Ticket ID
								 .then(function(data){
									 console.log(data);
									if(data.statusCode == 200){
										 if(data.object.length>0){
											 $.each(data.object,function(key,val){
												 var taskObject={
														 taskId:val.taskId,
														 taskName:val.taskName,
														 taskNumber:val.taskNumber,
														 ticketNumber:$scope.ticketData.ticketNumber,
														 ticketId:$scope.ticketData.ticketId,
														 taskDesc:val.taskDesc,
														 planStartDate:val.planStartDate,
														 planEndDate:val.planEndDate,
														 taskAssignedTo:val.taskAssignedTo,
														 resComments:val.resComments,
														 taskStatus:val.taskStatus,
														 createdBy:val.createdBy
												 }
												 $scope.incidentTask.list.push(taskObject);
											 });
										 }
									} 
								 },function(data){
									 console.log(data);
								 });
							
							
							}
							$scope.openIncidentTask=function(isCreateUpdate,selectedTask){
								 if(isCreateUpdate == 'C'){
									 $scope.taskOperation ="CreateTask";
									 $('#taskModal').modal('show');
									 $scope.incidentTask.taskId=null;
									 $scope.incidentTask.taskName=null;
									 $scope.incidentTask.taskNumber=null;
									 $scope.incidentTask.taskDesc=null;
									 $scope.incidentTask.planStartDate=null;
									 $scope.incidentTask.planEndDate=null;
									 $scope.incidentTask.taskAssignedTo=null;
									 $scope.incidentTask.resComments=null;
									 $scope.incidentTask.ticketId=$scope.ticketData.ticketId;
									 $scope.incidentTask.taskStatus="NEW";
								 }
								 if(isCreateUpdate == 'U'){
									 $scope.taskOperation ="UpdateTask";
									 $scope.incidentTask.taskId=selectedTask.taskId;
									 $scope.incidentTask.taskNumber=selectedTask.taskNumber;
									 $scope.incidentTask.taskName=selectedTask.taskName;
									 $scope.incidentTask.taskDesc=selectedTask.taskDesc;
									 $scope.incidentTask.planStartDate=selectedTask.planStartDate;
									 $scope.incidentTask.planEndDate=selectedTask.planEndDate;
									 $scope.incidentTask.taskAssignedTo=selectedTask.taskAssignedTo;
									 $scope.incidentTask.resComments=selectedTask.resComments;
									 $scope.incidentTask.taskStatus=selectedTask.taskStatus;
									 $scope.incidentTask.ticketId=$scope.ticketData.ticketId;
									 $scope.incidentTask.taskSelectedStatus=selectedTask.taskStatus;
									 $('#taskModal').modal('show');
								 }
							}
							
							
							
							$scope.saveIncidentTask =function(){
								
								
								 if($scope.incidentTask.taskId==null){
									 $scope.incidentTask.taskStatus=$('#taskStatus').val();
								 }
								 else{
									 $scope.incidentTask.taskStatus=$('#taskStatusUpdate').val();
								 }
								 console.log("Save Data",$scope.incidentTask);
								 var planEndDate = $scope.incidentTask.planEndDate;
								 var planStartDate = $scope.incidentTask.planStartDate;
								 if($scope.IsValidTaskDate(planEndDate,planStartDate)){
									 
									 if($scope.IsEmail($scope.incidentTask.taskAssignedTo)){
										 	$('#messageWindow').hide();
						 					$('#errorMessageDiv').hide();
						 					$scope.saveIncidentTaskInfo($scope.incidentTask);
						 					//window.location.href=hostLocation+"/asset/details";
									 } else {
										 $scope.errorMessage = "Assigned To is not valid. Enter a valid Email ID.";
										  $scope.getErrorMessage( $scope.errorMessage)
									 }
									 
									 
						          }
						          else{	        	  
						        	  
						        	  $scope.errorMessage = "Planned Completion Date should be greater Planned Start Date";
						        	  $scope.getErrorMessage( $scope.errorMessage)
						          }		     
								
							 }
							 
							 $scope.IsEmail = function (assignedTo) {	
								 var isValid = false;
								 var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
							        if(!regex.test(assignedTo)) {
							        	isValid = false;
							        }else{
							        	isValid = true;
							        }
							        return isValid;
							 }
							 
							 $scope.IsValidTaskDate = function (planComplDate, planStartDate) {				 
									
								 var isValid = false;
								
								 if(planComplDate == null || planComplDate == "" || typeof(planComplDate) === "undefined"){
									 isValid = true;					 
								 }
								 else{					 
									 var isSame;
									 var part1 = planComplDate.split('-');
									 var part2 = planStartDate.split('-');				 
									 planComplDate =  new Date(part1[1]+'-'+part1[0]+'-'+part1[2]);
									 planStartDate =  new Date(part2[1]+'-'+ part2[0]+'-'+ part2[2]);				 
							         isValid = moment(planComplDate).isAfter(planStartDate);
							          if (!isValid){
							        	  isValid = moment(planComplDate).isSame(planStartDate);
							          }	
								 }		         	          
						          
						          return isValid;
						      }
							 
							 $scope.saveIncidentTaskInfo=function(incidentTaskData){
								 
								 console.log("Inside saveIncidentTaskInfo------->",incidentTaskData);
								 
								// $('#loadingDiv').show();
								 ticketService.saveIncidentTask(incidentTaskData)
								 .then(function(data) {
						    			
						    			if(data.statusCode == 200){
						    				$scope.successMessage = data.message;
						    				 $scope.getSuccessMessage($scope.successMessage);
						    				 getIncidentTasks();
						    				//window.location.href=hostLocation+"/asset/details";
						    				$('#taskModal').modal('hide');
						    			//	$('#loadingDiv').hide();
						    			}
						            },
						            function(data) {
						            	 console.log('Error while saving asset Task Data')	
						            	 $scope.getErrorMessage(data)
						            	// $('#loadingDiv').hide();
						            });
							 }
							
							
							
							
							
							//End
							
							$scope.getIncidentSelected=function(){
								$('#loadingDiv1').show();
								ticketService.getSelectedTicketFromSession()
								.then(function(data){
									//console.log("Ticket DetailsXXXX")
									console.log(data)
									if(data.statusCode == 200){
										$scope.ticketData=angular.copy(data.object);
										//$scope.ticketData.createdBy=$scope.sessionUser.username;
										$scope.getAttachments($scope.ticketData.ticketId);
										if(viewMode.toUpperCase()=="EDIT"){
						    				$scope.getLinkedTicketDetails($scope.ticketData.ticketId);
					    				}
										var rspMappedCustomer = $.jStorage.get("selectedRSPCustomer");
										$scope.ticketData.mappedCustomer = rspMappedCustomer.custName;
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
										if(data.object.ticketComments.length>0){
											 $scope.ticketData.comments = data.object.ticketComments;
											 $scope.ticketComments=[];
											 $.each(data.object.ticketComments,function(key,val){
												  $scope.ticketComments.push(val);
											 })
											 
										}
										
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
										$('#loadingDiv1').hide();
									}
									
								},function(data){
									console.log(data)
									$('#loadingDiv1').hide();
								});
							}
							
							$scope.selectLinkTicket=function(optionSelected){
								if(optionSelected=="ZERO"){
									$scope.optionVal=optionSelected;
									$scope.suggestedTickets = [];
								}
								else if(optionSelected=="ONE"){
									$scope.optionVal=optionSelected;
									$scope.pullRSPSuggestedTickets();
								}
							}
							$scope.pullRSPSuggestedTickets=function(){
								var selectedTicket = $scope.ticketData;
								ticketService.pullRSPSuggestedTickets()
								.then(function(data){
									console.log(data)
									$scope.suggestedTickets = [];
									if(data.statusCode == 200){
										if(data.object.length>0){
											$.each(data.object,function(key,val){
												
												$scope.suggestedTickets.push(val);
											});
											
										}
										if(data.object2.length>0){
											$.each(data.object2,function(key,val){
												$scope.suggestedTickets.push(val);
											});
										}
									}
								
								},function(data){
									console.log(data)
								});
							}
							$scope.displayTickets=function(option){
								$('#filterText').text(option);
								$.each($scope.suggestedTickets,function(key,val){
									if(val.ticketNumber.startsWith(option)){
										$scope.searchText=option;
										return false;
									}
								});
							}
							$scope.LinkNewTicket = function(spType, spTicket){
								  var ticketLinked=null; 
								  $scope.linkedTicket={};
								  if(spTicket.ticketNumber != ""){
								  var isDuplicateTicket=false;
										$scope.linkedTicket.ticketNumber = spTicket.ticketNumber;
										var linkedTicket={
												parentTicketId:$scope.ticketData.ticketId,
												parentTicketNumber:$scope.ticketData.ticketNumber,
												linkedTicketId:spTicket.ticketId,
												linkedTicketNumber:spTicket.ticketNumber,
												linkedTicketType:spType
										}
										ticketLinked = linkedTicket;
										$.each($scope.ticketData.linkedTickets,function(key,val){
											if($scope.linkedTicket.ticketNumber == val.spLinkedTicket){
												isDuplicateTicket =true;
												return false;
											}
										});
										console.log(ticketLinked);
									if(!isDuplicateTicket){
											ticketService.saveRSPLinkedTicket(ticketLinked,"EDIT")
											.then(function(data){
												if(data.statusCode == 200){
												 $scope.getSuccessMessage(data.message);
												 $scope.getLinkedTicketDetails(ticketLinked.parentTicketId, ticketLinked.linkedTicketType);
												}
											},function(data){
												console.log(data);
											});
									}else{
											$scope.errorMessage="Ticket number is already linked.";
											 $scope.getErrorMessage($scope.errorMessage);
									}
								  }
								}
							

							
							$scope.getLinkedTicketDetails=function(linkedTicket, type){
								ticketService.getRSPLinkedTickets(linkedTicket)
								.then(function(data){
									//console.log(data);
									if(data.statusCode == 200){
										 if(data.object.length>0){
											 $scope.ticketData.linkedTickets = data.object;
										 }else  {
											 $scope.ticketData.linkedTickets=[];
										 }
									}
								},function(data){
									//console.log(data);
								});
							}
							
							
							$scope.unlinkTicketConfirmation=function(index, linkedTicket){
								$('#confirmUnlink').modal('show');
								$scope.unlinkTicketIndex = index;
								$scope.unlinkTktObject = angular.copy(linkedTicket);
								
							}
							 $scope.unlinkTicket=function(){
								 var linkedTicket = angular.copy($scope.unlinkTktObject);
								 ticketService.deleteLinkedTicket(linkedTicket)
								 .then(function(data){
									 //console.log(data);
									 if(data.statusCode == 200){
										 $scope.getSuccessMessage(data.message);
										 $('#unlinkNoBtn').click();
										 $scope.getLinkedTicketDetails($scope.ticketData.ticketId, linkedTicket.spType);
									 }
								 },function(data){
									 console.log(data);
									 $scope.errorMessage="Unable to to unlink the ticket ";
									 $scope.getErrorMessage($scope.errorMessage);
								 });
							 }
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
							$scope.setSLAWidth=function(ticketData){
					            if(ticketData.slaPercent > 100){
					                $scope.ticketData.width = 100;                
					            }
					            else{
					                $scope.ticketData.width = ticketData.slaPercent;
					            }                
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
							
							 $scope.openChatBox=function(){
									$('#chatWindow').fadeIn();
									$scope.chatBoxView="ON";
							}
							$scope.closeWindow=function(){
								$('#chatWindow').fadeOut();
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
									 $scope.assetList.selected = "";
									 //$scope.repairType.list=[];
						    		 //$("#repairtypeSelect").empty();
						    		 $scope.subRepairType.list=[];
						    		 $("#subcomponentTypeSelect").empty();
									 console.log("EQUIPMENT------>",$scope.assetList.selected);
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
									 $scope.assetList.selected = "";
									 //$scope.repairType.list=[];
						    		 //$("#repairtypeSelect").empty();
						    		 $scope.subRepairType.list=[];
						    		 $("#subcomponentTypeSelect").empty();
									 console.log("SERVICE------>",$scope.assetList.selected);
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
													$scope.assetList.selected.subCategoryId1=val.subCategoryId1
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
										 else if(assetType == 'S'){
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
							$scope.changeStatusDescription=function(description){
								$scope.ticketData.statusDescription=description;
								$('#statusDesc').text("Description: "+ description);
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
										 $scope.ticketData.mode="IMAGEUPLOAD";
											 var rspMappedCustomer = $.jStorage.get("selectedRSPCustomer");
											$scope.ticketData.rspCustMappedCompanyCode=rspMappedCustomer.custCode;
											$scope.ticketData.rspCustMappedCompanyName=rspMappedCustomer.custName;
											$scope.ticketData.rspCustMappedCompanyId=rspMappedCustomer.custId;
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
								  $.each($scope.incidentImageList,function(key,val){
									  if(val.id==indexPos){
										  $scope.incidentImageList.splice(indexPos,1);	
										  return false;
									  }
								  });
								//  $scope.incidentImageList.splice(indexPos,1);
								  $.each($scope.incidentImages,function(key,val){
									 if(val.imgPos==indexPos) {
										 $scope.incidentImages.splice(indexPos,1);		
										 totalSize = totalSize - val.fileSize ;
										// $('#totalIncidentSize').text(totalIncidentImageSize+" KB");
										 $('#totalSize').text("Files ( "+$scope.incidentImages.length +" ) Total Size : "+ totalSize +" KB");
										// $scope.incidentImageList.splice(indexPos,1);
										 return false;
									 }
								  });
								if(totalSize > 1024){
									$('#incidentImageModalMessageDiv').show();
				/*					 $('#messageWindow').show();
									 $('#errorMessageDiv').show();
						        	 $('#errorMessageDiv').alert();
				*/					 $('#fileerrorincident').text("File size exceeds 1 MB");
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
								var ticketAssignedType=$.jStorage.get('ticketType');
								$scope.ticketData.ticketAssignedType = ticketAssignedType;
								var rspMappedCustomer = $.jStorage.get("selectedRSPCustomer");
								$scope.ticketData.rspCustMappedCompanyCode=rspMappedCustomer.custCode;
								$scope.ticketData.rspCustMappedCompanyName=rspMappedCustomer.custName;
								$scope.ticketData.rspCustMappedCompanyId=rspMappedCustomer.custId;
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
								 var rspMappedCustomer = $.jStorage.get("selectedRSPCustomer");
									$scope.ticketData.rspCustMappedCompanyCode=rspMappedCustomer.custCode;
									$scope.ticketData.rspCustMappedCompanyName=rspMappedCustomer.custName;
									$scope.ticketData.rspCustMappedCompanyId=rspMappedCustomer.custId;
								 $scope.persistTicket($scope.ticketData, "UPDATE");
								 
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
						    						window.location.href=hostLocation+"/serviceprovider/rsp/incident/update";
						    					//}
						    				}else{
						    					if(type.toUpperCase()=='UPLOAD'){
						    						$scope.successMessage = "Files uploaded successfully";
						    						 $scope.getSuccessMessage($scope.successMessage);
						    	    				$scope.getAttachments(ticketData.ticketId)
						    	    			/*	if(data.object.attachments.length>0){
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

						} ]);

function initializeEscalateTicket(){
	var scope = angular.element("#spIncidentCreateWindow").scope();
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