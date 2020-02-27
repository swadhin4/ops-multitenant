chrisApp.controller('assetTaskController', 
		 ['$rootScope', '$scope', '$filter','authService','userService',
		  'siteService','serviceProviderService','assetService',
		  function  ($rootScope, $scope , $filter,authService,userService,
				  siteService,serviceProviderService,assetService) {			 
			 
			 
			 //$scope.equipmentData ={};
			 
			 
			 $scope.taskHeaderName = '';
			 
			 
			 
			 
			 
			 //
			 
			 
			 
			 $scope.serviceData ={};
			 //$scope.selectedRow =0;
			 //$scope.selectedAsset={};
			 //$scope.operation = {};
						 
			 /*$scope.asset={
					 selected:{},
					 list:[]
			 }
			 $scope.assetCategory={
					 selected:{},
					 equipmentList:[],
					 serviceList:[]
					 
			 }
			 $scope.repairType={
					 selected:{},
					 list:[]
			 }
			
			 $scope.serviceRepairType={
					 selected:{},
					 list:[]
			 }
			
			 $scope.assetLocation={
					 selected:{},
					 list:[]
			 }
			 $scope.serviceProvider={
					 selected:{},
					 list:[]
			 }
			 $scope.accessSite={
					 selected:{},
					 list:[]
			 }*/
			 
			 //$scope.selectedSites = [];
			 
			 //$scope.assetId = '';
			 
						 
			 
			 angular.element(document).ready(function () {
				 $scope.taskOperation = $.jStorage.get('taskOperation');
				 $scope.originateFrom = $.jStorage.get('originateFrom');
				
				 $scope.getLoggedInUserAccess();
				 //$scope.serviceData.status='New';
				 
				 //taskOperation
				/*console.log("operation",$scope.operation);
				$scope.selectedSites = $.jStorage.get('selectedSites');
				$scope.taskHeaderName = $scope.selectedSites.siteName;
				console.log("taskHeaderName",$scope.taskHeaderName);*/
				 debugger;
				 $scope.selectedSite = $.jStorage.get('selectedAsset');
					
					
					console.log("SiteID",$scope.selectedSite);
					console.log("taskOperation",$scope.taskOperation);
					$scope.taskOperation = "UpdateTask";
				$scope.taskHeaderName = $scope.selectedSite.assetName;	
				if ($scope.taskOperation == "CreateTask") {
					console.log("taskHeaderName--->",$scope.taskHeaderName);
					$scope.serviceData.status='New';
				}
				
				if ($scope.taskOperation == "UpdateTask") {
					$scope.serviceData.status='In Progress';
				}
				 
				 //	  			 
					 
					
			 });
			 
			 
			 $scope.closeMessageWindow=function(){
				 $('#messageWindow').hide();
				 $('#errorMessageDiv').hide(); 				 
			 }
			 
			 
			 
			 //Save Create And Update Asset Task
			 
			 
			 
			 $scope.saveAssetTaskService =function(){
				 debugger
				 console.log("Save Date",$scope.serviceData);
				 
				 
				 
				 if($scope.IsValidDate($scope.serviceData.planComplDate,$scope.serviceData.planStartDate)){
					 
					 if($scope.IsEmail($scope.serviceData.assignTo)){
						 $('#messageWindow').hide();
		 					$('#errorMessageDiv').hide();
		 					//$scope.saveAssetTaskInfo($scope.serviceData);
		 					window.location.href=hostLocation+"/asset/details";
					 } else {
						 $scope.errorMessage = "Assigned To is not valid. Enter a valid Email ID.";
			        	  $('#messageWindow').show();
			              $('#errorMessageDiv').show();
		    			  $('#errorMessageDiv').alert();
					 }
					 
					 
		          }
		          else{	        	  
		        	  
		        	  $scope.errorMessage = "Planned Completion Date should be after Planned Start Date";
		        	  $('#messageWindow').show();
		                $('#errorMessageDiv').show();
	    				$('#errorMessageDiv').alert();	
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
			 
			 $scope.IsValidDate = function (planComplDate, planStartDate) {				 
					
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
			 
			 $scope.saveAssetTaskInfo=function(assetTaskData){
				 $('#loadingDiv').show();
				 assetService.saveAssetObject(assetData)
				 .then(function(data) {
		    			
		    			if(data.statusCode == 200){
		    				$scope.successMessage = data.message;
		    				$('#messageWindow').show();
		    				$('#successMessageDiv').show();
		    				$('#successMessageDiv').alert();
		    				$('#infoMessageDiv').hide();
		    				
		    				//$scope.getAllAsset();
		    				window.location.href=hostLocation+"/asset/details";
		    				
		    				$('#loadingDiv').hide();
		    			}
		            },
		            function(data) {
		            	 console.log('Error while saving asset Task Data')			            
		            });
			 }
			 
			 
			 
			 //End
			 
			
			 
			 $scope.getLoggedInUserAccess =function(){
					authService.loggedinUserAccess()
		    		.then(function(data) {
		    			
		    			if(data.statusCode == 200){
		    				$scope.sessionUser=data;
		    				//$scope.getLoggedInUser($scope.sessionUser);
		    				
		    			}
		            },
		            function(data) {
		                console.log('Unauthorized Access.')
		            }); 
					
			    }
			 
			
			 
			 
			 
			 
			 		 			 
				
			 
			
}]);