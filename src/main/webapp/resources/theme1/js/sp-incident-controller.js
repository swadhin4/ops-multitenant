
chrisApp.controller('spIncidentController',  ['$rootScope', '$scope', '$filter','siteService','serviceProviderService',
                                        'siteCreationService','companyService','userService','districtService',
                                        'areaService','clusterService','countryService','assetService',
                                        'ticketCategoryService','ticketService',
                              function  ($rootScope, $scope , $filter,siteService, serviceProviderService,
                                        siteCreationService,companyService,userService,districtService,
                                        areaService,clusterService,countryService,assetService,
                                        ticketCategoryService,ticketService) {
		
		
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
		$scope.sessionTicket ={};
		$scope.assetCategory={
				 selected:{},
				 equipmentList:[],
				 serviceList:[]
				 }
		
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
		
		$scope.selectedEscalation={};
		$scope.selectedLinkedTicket={};
		
		$scope.categoryList=[];
		
		
		$scope.siteAssignedUserList=[];
		$scope.siteUnAssignedUserList=[];
		
		$scope.selectedTicket={};
		$scope.ticketSelected;
		angular.element(document).ready(function(){
			console.log("loaded");
			$scope.initalizeCloseDiv();			
			$scope.getLoggedInUser();	
			  var selected = [];
			  $scope.sessionTicket=null;
			$('#ticketList').on('click', 'tbody tr',function(){
				 $('#ticketList tbody > tr').removeClass('currentSelected');
				  $(this).addClass('currentSelected');
				  var rowIndex =  $(this).find('td:eq(1)').text();
			            var currentTicket={
				        		ticketNumber:rowIndex
				        }
			            var selectedTicket={};
			            $.each($scope.ticket.list,function(key,val){
				        	if(val.ticketNumber == currentTicket.ticketNumber){
				        	/*	selectedTicket={
				        				ticketId:val.id, 
				        				ticketTitle:val.title, 
				        				ticketNumber:val.ticketNumber,
				        				description:val.description, 
				        				siteId:val.siteId, 
				        				siteName:val.siteName, 
				        				assetId:val.assetId, 
				        				assetName:val.assetName, 
				        				categoryId:val.categoryId, 
				        				categoryName:val.categoryName, 
				        				statusId:val.statusId, 
				        				status:val.status, 
				        				raisedOn:val.createdOn, 
				        				assignedTo:val.assignedTo, 
				        				assignedSP:val.assignedSP, 
				        				raisedBy:val.raisedBy,
				        				priorityDescription:val.priority, 
				        				sla:val.slaDueDate,
				        				ticketStartTime:val.ticketStartTime,
				        				
				        		}*/
				        		 $scope.ticketSelected=val;
				        		// getSelectedTicketInfo(val);
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
		
		 $scope.rowHighilited=function(row)
		    {
		      $scope.selectedRow = row;    
		    }
		 
		 $scope.previewSelectedIncidentInfo=function(ticket){
			 $('#previewIncidentModal').modal('show');
		 }
		
		$scope.getLoggedInUser=function(){
			
			serviceProviderService.getLoggedInSPUser()
    		.then(function(data) {
    			if(data.statusCode == 200){
    				$scope.spData ={};
    				$scope.sessionUser=angular.copy(data.object);
    				$scope.getAllTickets();
   				 	$('.dpedit').editableSelect();
    			}
            },
            function(data) {
                console.log('No User available')
            });
		}
		
		$scope.getAllTickets=function(){
			$scope.findAllTickets();
		}
		
		
		
		$scope.findAllTickets=function(){
			$('#loadingDiv').show();
			ticketService.displayAllOpenTickets("sp")
			.then(function(data){
				//console.log(data);
				if(data.statusCode == 200){
				$scope.ticket.list=[];
				$.each(data.object,function(key,val){
	    				$scope.ticket.list.push(val);
	    			})
	    		$('#loadingDiv').hide();	
				 //$('#updateTicket').hide();
				// $scope.getTicketDetails($scope.ticket.list[0]);
				populateDataTable($scope.ticket.list,'ticketList');
				}
			},function(data){
				//console.log(data);
			});
	
			
			$('#messageWindow').hide();
			$('#infoMessageDiv').hide();
			$('#loadingDiv').hide();
    			//console.log("shibu");
			 /*$scope.jsonObj = angular.toJson($scope.ticket.list);*/
			 	//console.log($scope.ticket.list);
			 	
			$('#loadingDiv').hide();
		}
		
		 $scope.setTicketinSession=function(ticket){
			 //$('#loadingDiv').show();
			 ticketService.setIncidentSelected(ticket,"sp")
				.then(function(data){
					//console.log(data);
					if(data.statusCode==200){
						console.log("Ticket logged in session");
						//console.log(data.object);
						$scope.sessionTicket = data.object;
					}
					if(ticket.statusId==6){
	        			 $('#closedTicket').show();
	        			 $('#updateTicket').show();
	        		 }else{
	        			 $('#closedTicket').show();
	        			 $('#updateTicket').show();
	        		 }
					//$('#loadingDiv').hide();
				},function(data){
					//console.log(data);
					//$('#loadingDiv').hide();
				});
		 }

		$scope.viewUpdatePage=function(){
			if($scope.sessionTicket!=null){
				if($scope.sessionTicket.statusId==15){
					$('#messageWindow').show();
					$('#infoMessageDiv').show();
					$('#infoMessageDiv').alert();
					$scope.InfoMessage="This ticket cannot be updated because its already closed."
	       		 }else{
					window.location.href=hostLocation+"/sp/incident/details/update"
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
		
		$scope.viewSelectedTicket=function(){
			if($scope.sessionTicket!=null){
				window.location.href=hostLocation+"/sp/incident/details/view";
				$('#messageWindow').hide();
				$('#errorMessageDiv').hide();
			}
			else{
				$('#messageWindow').show();
				$('#infoMessageDiv').show();
				$('#infoMessageDiv').alert();
				$scope.InfoMessage="Please select a ticket to view."
			}
		}
		

		$scope.initalizeCloseDiv=function(){
			$('#ticketCloseDiv').hide();
			$("#closeCodeSelect").attr('required', false);
			$("#raisedOn").attr('required', false);
			$("#closeNote").attr('required', false);
		}
		
		 $scope.closeMessageWindow=function(){
			 $('#messageWindow').hide();
			 
		 }
		
		
			     
	     //----------------------- View ticket ------------------------------------
	     
	     
		 //console.log("allticket retrived");
		 
		 $scope.getTicketDetails=function(ticket){
			//console.log(ticket);
			$scope.selectedTicket=angular.copy(ticket);
			$scope.ticketData=angular.copy(ticket);
			/*ticketService.retrieveTicketDetails(ticket)
			.then(function(data){
				//console.log(data);
			},function(data){
				
			});*/
		}

	
			 
}]);


function getSelectedSite(dropDownId){
	var scope = angular.element("#spIncidentWindow").scope();
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
	var scope = angular.element("#spIncidentWindow").scope();
	if(dropDownId.toUpperCase() == "ASSETSELECT"){
		 var asset={
				 assetId:parseInt($("#assetSelect").val()),
		 		 assetName:$("#assetSelect option:selected").text()
		 }
		 scope.assetList.selected = asset;
		 $.each(scope.assetList,function(key,val){
			if(val.assetId == asset.assetId){
				$('#assignedTo').val(val.serviceProviderName);
				return false;
			} 
		 });
		 
	 }
}

function getSelectedCategory(dropDownId){
	var scope = angular.element("#spIncidentWindow").scope();
	if(dropDownId.toUpperCase() == "CATEGORYSELECT"){
		 var category={
				 categoryId:parseInt($("#categorySelect").val()),
		 		 categoryName:$("#categorySelect option:selected").text()
		 }
		 scope.categoryList.selected =category;
		 scope.setTicketPriorityAndSLA(scope.categoryList.selected);
	 }
}

function getSelectedPriority(dropDownId){
	var scope = angular.element("#spIncidentWindow").scope();
	if(dropDownId.toUpperCase() == "PRIORITYSELECT"){
		 var priority={
				 categoryId:parseInt($("#prioritySelect").val()),
		 		 categoryName:$("#prioritySelect option:selected").text()
		 }
		 scope.priorityList.selected =priority;
	 }
}

function ticketStatusChange(dropDownId){
	var scope = angular.element("#spIncidentWindow").scope();
	//console.log("status change");
	var valueId = parseInt($("#"+dropDownId).val());
	//scope.selectedSite = valueId;
	if(valueId == 15){
		
		$('#ticketCloseDiv').show();
		$("#closeCodeSelect").attr('required', true);
		$("#raisedOn").attr('required', true);
		$("#closeNote").attr('required', true);
	}
	else{
		$('#ticketCloseDiv').hide();
		$("#closeCodeSelect").attr('required', false);
		$("#raisedOn").attr('required', false);
		$("#closeNote").attr('required', false);
	}
	
	if(dropDownId.toUpperCase() == "STATUSSELECT"){
		 var ticketStatus={
				 statusId:parseInt($("#statusSelect").val()),
		 		 statusName:$("#statusSelect option:selected").text()
		 }
		 scope.statusList.selected =ticketStatus;
	 }
	//console.log(valueId);
	//console.log(selectedStatusId);
}

function populateDataTable(data, tableDivName){
	
	$('#'+tableDivName).dataTable({
		"aaData" : data,
		"order": [[ 0, "desc" ]],
		"aoColumns" : [{
			"sTitle" : "Ticket ID",
			"mData" : "ticketId",
			"sClass": "hidden"
			
		}, {
			"sTitle" : "Ticket Number",
			"mData" : "ticketNumber"
		},{
			"sTitle" : "Site Name",
			"mData" : "siteName"
		},{
			"sTitle" : "Asset Name",
			"mData" : "assetName"
		},{
			"sTitle" : "Title",
			"mData" : "ticketTitle"
		},{
			"sTitle" : "Created On",
			"mData" : "raisedOn"
		},{
			"sTitle" : "Sla Due Date",
			"mData" : "sla"
		},{
			"sTitle" : "Status",
			"mData" : "status",
			"render": function ( data, type, full, meta ) {
				if(full.statusId==1){
					return '<a href style="color:#07ab07"> <i class="fa fa-arrow-circle-up" aria-hidden="true"></i> '+data+'</a>';
				}
				else if(full.statusId==2){
					return '<a href style="color:#e4bf08"> <i class="fa fa-file-archive-o" aria-hidden="true"></i> '+data+'</a>';
				}
				else if(full.statusId==3){
					return '<a href style="color:#ca9f57"> <i class="fa fa-cog fa-spin  fa-fw"></i> '+data+'</a>';
				}
				else if(full.statusId==4){
					return '<a href  style="color:#0a5061"> <i class="fa fa-retweet" aria-hidden="true"></i> '+data+'</a>';
				}
				else if(full.statusId==5){
					return '<a href style="color:#0a5061">'+data+'</a>';
				}
				else if(full.statusId==6){
					return '<a href style="color:red"> <i class="fa fa-times-circle" aria-hidden="true"></i> '+data+'</a>';
				 }
				else if(full.statusId==7){
					return '<a href style="color:#F033FF"> <i class="fa fa-check-circle-o" aria-hidden="true"></i> '+data+'</a>';
				 }
				else {
					return '<a href style="color:#F033FF"> <i class="fa fa-check-circle-o" aria-hidden="true"></i> '+data+'</a>';
				 }
			}
		}]
	});
}

function getSelectedTicketInfo(val){
	var scope = angular.element("#spIncidentWindow").scope();
	var selectedTicket=val;
	if(selectedTicket.ticketId!=null){
		scope.setTicketinSession(selectedTicket);
	}else{
		console.log("Ticket not selected")
	}
}


