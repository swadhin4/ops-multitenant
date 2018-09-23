productApp.controller('agentController', ['$rootScope','$scope','$http','$filter','$q', function($rootScope,$scope,$http,$filter,$q) {
	$scope.marketingagents=[];
	$scope.agentselected=false;
	$scope.agentPrevSelected=null;
	$scope.successMessage="";
	$scope.errorMessage="";
	
	 $scope.selectedRow =0;
	 $scope.rowHighilited=function(row)
	    {
	      $scope.selectedRow = row;    
	    }
	$scope.agent={};
	$scope.emptyAgent={};
	
	
	 angular.element(document).ready(function () {
		 $scope.getAllAgents();
		 $scope.getAllAgentTypes();
		 
	 });
	 
		$scope.getAllAgents=function(){
		  $http.get(webContextPath+'/agent/list')
			.success(function(data) {
				console.log(data);
				$scope.marketingagents=[];
				if(data.statusCode==200){
					$.each(data.object, function(key, val) {
						$scope.marketingagents.push(val);
					});
					$scope.showAgentDetail($scope.marketingagents[0]);
				}
				
			});
		}
	


	
		$scope.agentTypes=[];
		$scope.getAllAgentTypes=function(){
		  $http.get(webContextPath+'/agent/types')
			.success(function(data) {
				console.log(data);
				$scope.agentTypes=data;
				
			});
		}
	
	$scope.showAgentDetail=function(agent){
		$scope.agentselected=true;
		$scope.selectedAgent = angular.copy(agent);
	};
	  
			
		$scope.newagents=[];
		$scope.addAgent = function() {
			$scope.agentForm.$setPristine();
			$scope.agent={};
			$('#agentModalWindow').modal('show');
			
			$('#success-alert').hide();
			$('#danger-alert').hide();
			$('#success-edit-alert').hide();
			$('#danger-edit-alert').hide();
			$('#delete-success-alert').hide();
     		$('#delete-error-alert').hide();
			
		}
		
		$scope.editSelectedAgent = function(selectedAgent) {
			console.log(selectedAgent);
			$scope.agentEdit=angular.copy($scope.selectedAgent);
			$('#agentModalEditWindow').modal('show');
			
			$('#success-alert').hide();
			$('#danger-alert').hide();
			$('#success-edit-alert').hide();
			$('#danger-edit-alert').hide();
			$('#delete-success-alert').hide();
     		$('#delete-error-alert').hide();
			
		}
		
		$scope.reset=function(from){
			if(from==undefined){
				$scope.agent=angular.copy($scope.emptyAgent);
				$('#success-alert').hide();
				$('#danger-alert').hide();
			}else if(from.toUpperCase()=='DELETE'){
				$('#delete-success-alert').hide();
	     		$('#delete-error-alert').hide();
			}
		 };
		$scope.saveAgent = function(aForm, type) {
			 console.log(aForm);
			 var inValidParams=[];
				angular.forEach(aForm, function(value, key) {
					  if(key[0] == '$') return;
					  	console.log(key, value.$valid)
				  		if(value.$valid==false){
				  			value.$invalid=true;
				  			value.$pristine=false;
				  			var validOrderObject={
				  					field:key,
				  					val:value.$modelValue||""
				  			}
				  			inValidParams.push(validOrderObject);
				  		}
					  	return false;
					});
			 
				console.log(inValidParams);
				if(inValidParams.length==0){
				 var agentData={};
				 if(type.toUpperCase()=='ADD'){
					 agentData=angular.copy($scope.agent);
				 }else if(type.toUpperCase()=='EDIT'){
					 agentData=angular.copy($scope.agentEdit);
				 }
				console.log("Agent Data : " +JSON.stringify(agentData));
				$http.post(webContextPath+"/agent/save", agentData).success(
					function(data) {
						if(data.statusCode==200){
							
							// $scope.addAgent();
							
							 if(type.toUpperCase()=='ADD'){
								 $scope.successMessage="Agent details saved successfully."
								 $('#success-alert').show();
								 $('#success-alert').alert();
					             $("#success-alert").fadeIn(2000, 500);
					             $('#danger-alert').hide();
					             $scope.getAllAgents();
							 }else if(type.toUpperCase()=='EDIT'){
								 $scope.successMessage="Agent details updated successfully."
								  $('#success-edit-alert').show();
								 $('#success-edit-alert').alert();
					             $("#success-edit-alert").fadeIn(2000, 500);
					             $('#danger-edit-alert').hide();
					             $scope.getAllAgents();
							 }
						}else{
							 $scope.errorMessage=data.message;
							 if(type.toUpperCase()=='ADD'){
								 $('#danger-alert').show();
								 $('#danger-alert').alert();
					             $("#danger-alert").fadeIn(2000, 500);
					             $('#success-alert').hide();
							 }else if(type.toUpperCase()=='EDIT'){
								 $('#danger-edit-alert').show();
								 $('#danger-edit-alert').alert();
					             $("#danger-edit-alert").fadeIn(2000, 500);
					             $('#success-edit-alert').hide();
							 }
							
						}
					});
			  }
		  }
	  
		  $scope.deleteAgentModal=function(){
			  $('#confirmModal').modal('show');
		  }
	  
		  $scope.deleteAgent=function(agent){
				console.log(agent);
				$http.get(webContextPath +"/agent/delete/"+agent.agentId)
				.success(function(data){
					if(data.statusCode==200){
						$scope.globalMessage="Agent \""+ agent.name +"\" deleted successfully.";
						$('#delete-success-alert').alert();
		         		$("#delete-success-alert").fadeTo(2000, 500);
		         		$('#delete-success-alert').show();
		         		$('#delete-error-alert').hide();
		         		$('#confirmModalNo').click();
		         		$scope.getAllAgents();
					}else{
						$scope.globalMessage=data.message;
						$('#delete-success-alert').hide();
						$('#delete-error-alert').show();
						$('#delete-error-alert').alert();
		         		$("#delete-error-alert").fadeTo(2000, 500);
					}
				});
			};

			

			$scope.onlyNumbers = /^\d+$/;
			$scope.filterValue = function($event){
		        if(isNaN(String.fromCharCode($event.keyCode))){
		            $event.preventDefault();
		        }
			};
}]);

