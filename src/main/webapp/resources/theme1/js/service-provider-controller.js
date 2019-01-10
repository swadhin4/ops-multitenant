

chrisApp.controller('serviceProviderController',  ['$rootScope', '$scope', '$filter',
                                                   'authService','serviceProviderService','regionService','countryService',
                                         function  ($rootScope, $scope , $filter, authService,
                                        		 	serviceProviderService,regionService,countryService) {
		
		$scope.serviceProvider={};
		$scope.regionList=[];
		$scope.countryList=[];
		$scope.allServiceProviders=[];
		angular.element(document).ready(function(){
			$scope.getLoggedInUserAccess();
		});
		
		 $scope.getLoggedInUserAccess =function(){
			authService.loggedinUserAccess()
    		.then(function(data) {
    			
    			if(data.statusCode == 200){
    				$scope.getServiceProviderList("ALL");
    			}
            },
            function(data) {
                console.log('Unauthorized Access.')
            }); 	
		    }
		 
		$scope.addNewServiceProvider=function(){
			$scope.getRegionList('ADD');
			$scope.getPriorities();
			$scope.getEscalationLevel();
			$scope.serviceProvider={};
			$scope.serviceProvider.slaListVOList=[];
			$scope.serviceProvider.escalationLevelList=[];
			$('#createServiceProviderModal').modal('show');
		} 
		
		
		
		$scope.getRegionList=function(operation){
			$('#loadingDiv').show();
			regionService.findAllRegions()
    		.then(function(data) {
    			
    			$scope.regionList=[];
    			$("#regionSelect").empty();
    			$.each(data,function(key,val){
    				var region={
    					regionId:val.regionId,
    					regionCode:val.regionCode,
    					regionName:val.regionName
    				}
    				$scope.regionList.push(region);
    				$('#loadingDiv').hide();
    			});
    			var options = $("#regionSelect");
				options.append($("<option />").val(0).text("Select Region"));
				$.each($scope.regionList,function() {
					options.append($("<option />").val(	this.regionId).text(this.regionName));
				});
				if(operation == 'ADD'){
					$("#regionSelect option[value='0']").attr('selected','selected');
				}else{
					$("#regionSelect option[value='"+$scope.selectedServiceProvider.region.regionId+"']").attr('selected','selected');
					var region={
							regionId:$scope.selectedServiceProvider.region.regionId
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
			countryService.getCountryByRegion(region)
    		.then(function(data) {
    			
    			$scope.countryList=[];
    			$("#countrySelect").empty();
    			if(data.length>0){
	    			$.each(data,function(key,val){
	    				$scope.countryList.push(val);
	    			});
	    			var options = $("#countrySelect");
					options.append($("<option />").val(0).text("Select Country"));
					$.each($scope.countryList,function() {
						options.append($("<option />").val(	this.countryId).text(this.countryName));
					});
					if($scope.selectedServiceProvider!=null){
						$("#countrySelect option").each(function() {
							if ($(this).val() == $scope.selectedServiceProvider.country.countryId) {
								$(this).attr('selected', 'selected');
							return false;
							}
					 	});
						$scope.serviceProvider.country = $scope.selectedServiceProvider.country;
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
			$scope.serviceProvider.country = selectedCountry;
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
		
		
		$scope.saveServiceProviderForm=function(serviceProviderForm){
			$scope.serviceProvider.slaListVOList=[];
			$scope.serviceProvider.escalationLevelList=[];
			$.each($scope.priorities,function(key,val){
				var finalPriorityObject={
						slaId:val.slaId,
						priorityId:val.pId,
						ticketPriority:{
							priorityId:val.pId,
							priority:val.priority
						},
						duration:val.duration,
						unit:val.unit
				}
				$scope.serviceProvider.slaListVOList.push(finalPriorityObject);
			})
			
			$.each($scope.escalationLevels,function(key,val){
			 if(val.contact!=null && val.email!=null){	
				var finalEscalationObject={
						escId:val.escId,
						escalationLevel:key+1,
						escalationPerson:val.contact,
						escalationEmail:val.email
				}
				$scope.serviceProvider.escalationLevelList.push(finalEscalationObject);
			 }
			});
			
			
			$scope.saveServiceProvider($scope.serviceProvider);
		}
		
		$scope.saveServiceProvider=function(spData){
			$('#loadingDiv').show();
			serviceProviderService.saveServiceProvider(spData)
			.then(function(data) {
    			
    			if(data.statusCode == 200){
    				$scope.successMessage = data.message;
    				 $scope.getSuccessMessage($scope.successMessage);
    				$('#spModalCloseBtn').click();
    				$scope.getServiceProviderList("ALL");
    				$('#infoMessageDiv').hide();
    				$('#loadingDiv').hide();
    			}else{
    				$scope.modalErrorMessage = data.message;
    				   $scope.getErrorMessage($scope.modalErrorMessage);
    				$('#loadingDiv').hide();
    			}
            },
            function(data) {
                console.log('Unable to save Service Provider')
                $scope.modalErrorMessage = data.message;
                $scope.getErrorMessage($scope.modalErrorMessage);
				$('#loadingDiv').hide();
            });
		}
		
		$scope.getServiceProviderList=function(viewType){
			$('#loadingDiv').show();
			serviceProviderService.getAllServiceProviders(viewType)
			.then(function(data) {
    			
    			if(data.statusCode == 200){
    				$scope.allServiceProviders=[];
    				if(data.object.length>0){
    				$.each(data.object,function(key,val){
    					var spData={
    							sp:val,
    							isSelected:false
    					}
    					$scope.allServiceProviders.push(spData);
    				});
    				$scope.getSelectedServiceProvider($scope.allServiceProviders[0], $scope.allServiceProviders[0].sp.accessGranted);
    					
	    				$('#messageWindow').hide();
	    				$('#infoMessageDiv').hide();
    				}else{
    					$scope.InfoMessage="Currently no service provider is available"
    					$('#messageWindow').show();
        				$('#infoMessageDiv').show();
        				$('#infoMessageDiv').alert();
    				}
    				$('#loadingDiv').hide();
    			}
            },
            function(data) {
                console.log('Unable to get  Service Provider list')
                $scope.InfoMessage="Currently no service provider is available"
					$('#messageWindow').show();
    				$('#infoMessageDiv').show();
    				$('#infoMessageDiv').alert();
    				$('#loadingDiv').hide();
            });
		}
		
		$scope.getSelectedServiceProvider=function(serviceProviderObj, spAccess){
			console.log(serviceProviderObj);
			var spTypeView=null;
			if(spAccess==null){
				spTypeView = "EXT"
			}
			else if(spAccess!=null){
				spTypeView = "RSP"
			}
			serviceProviderService.getSelectedServiceProvider(serviceProviderObj.sp.serviceProviderId, spTypeView)
			.then(function(data) {
				console.log(data)
    			if(data.statusCode == 200){
    				$scope.selectedServiceProvider = angular.copy(data.object);
    				$scope.selectedServiceProvider.isSelected=true;
    				$scope.selectedServiceProvider.spTypeView=spTypeView;
    			}
			}, function(data) {
                console.log('Unable to get  Service Provider details')
            });
		}
		
		$scope.grantOrRevokeAcess=function(){
			$('#loadingDiv').show();
			console.log($scope.selectedServiceProvider.accessGranted);
			serviceProviderService.grantOrRevokeAcess($scope.selectedServiceProvider.serviceProviderId, 
					$scope.selectedServiceProvider.accessGranted)
				.then(function(data) {
					console.log(data)
	    			if(data.statusCode == 200){
	    				$('#loadingDiv').hide();
	    				$scope.successMessage = data.message;
	    				 $scope.getSuccessMessage($scope.successMessage);
	    				$scope.getServiceProviderList("ALL");
	    			}
				}, function(data) {
	                console.log('Unable to provide access ')
	                $scope.errorMessage = data.message;
	                $scope.getErrorMessage($scope.errorMessage);
					$('#loadingDiv').hide();
	        });
		 }
		
		$scope.editServiceProvider=function(){
			if($scope.serviceProvider!=undefined){
			$('#createServiceProviderModal').modal('show');
			$('#serviceModaltitle').text("Update Service Provider");
			$scope.serviceProvider = $scope.selectedServiceProvider;
			 
			$scope.getRegionList('EDIT');
			//$scope.priorities = $scope.selectedServiceProvider.slaListVOList;
			$scope.priorities =[];
			var priorityList=['Critical', 'High', 'Medium', 'Low'];
			var description=['Operation is completely down','Operation is partially interrupted', 'Performance degraded','General service request'];
			//$scope.getPriorities();
			var i = 1;
			$.each($scope.selectedServiceProvider.slaListVOList,function(key,val){
				
				var slaPriorities={
						slaId:val.slaId,
						priority:priorityList[i-1],
						description:description[i-1],
						duration:val.duration,
						unit:val.unit
						
				}
				$scope.priorities.push(slaPriorities);
				i=i+1
			});
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
			}
		}
		
		$scope.onlyNumbers = /^\d+$/;
		$scope.filterValue = function($event){
	        if(isNaN(String.fromCharCode($event.keyCode))){
	            $event.preventDefault();
	        }
		};
		$scope.closeMessageWindow=function(){
			 $('#messageWindow').hide();
			 $('#modalMessageDiv').hide();
			 $scope.successMessage="";
			 $scope.modalErrorMessage="";
		}
		
		$scope.resetPassword=function(){
			$('#loadingDiv').show();
			serviceProviderService.resetServiceProviderPassword($scope.selectedServiceProvider)
			.then(function(data) {
				console.log(data)
    			if(data.statusCode == 200){
    				$('#loadingDiv').hide();
    				$scope.selectedServiceProvider = $scope.selectedServiceProvider;
    				$scope.successMessage = "Password reset successfully";
    				$scope.getSuccessMessage($scope.successMessage);
    				$('#resetConfirmNo').click();
    				
    			}
			}, function(data) {
                console.log('Unable to reset service provider password')
                $scope.errorMessage = "Unable to reset service provider password";
                $scope.getErrorMessage($scope.errorMessage);
				$('#loadingDiv').hide();
            });
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

function validateDropdownValues(dropDownId){
	var scope = angular.element("#serviceProviderWindow").scope();
	 var valueId = parseInt($("#"+dropDownId).val());
	 if(valueId == 0){
	 
	 }else{
		 if(dropDownId.toUpperCase() == "REGIONSELECT"){
			 var region={
					 regionId:parseInt($("#regionSelect").val()),
			 		 regionName:$("#regionSelect option:selected").text()
			 }
			scope.getRegionCountry(region);
		 }
		 
		 if(dropDownId.toUpperCase() == "COUNTRYSELECT"){
			 var country={
					 countryId:parseInt($("#countrySelect").val()),
			 		 countryName:$("#countrySelect option:selected").text()
			 }
			scope.setServiceProviderCountry(country);
		 }
	 }
}
