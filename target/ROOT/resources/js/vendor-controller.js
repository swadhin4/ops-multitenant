productApp.controller('vendorController', ['$rootScope','$scope','$http','$filter','$q', function($rootScope,$scope,$http,$filter,$q) {
	$rootScope.productvendors=[];
	$scope.vendorselected=false;
	$scope.vendorPrevSelected=null;
	 $scope.selectedRow =0;
	 $scope.rowHighilited=function(row)
	    {
	      $scope.selectedRow = row;    
	    }
	$scope.vendor={};
	$scope.emptyVendor={};
	$rootScope.vendors=[];
	$rootScope.vendordata={};
	
	 angular.element(document).ready(function () {
		 $rootScope.getVendorList();
	 });
	
	 $rootScope.getVendorList=function(){
			$http.get(webContextPath+'/vendor/list')
			.success(function(data) {
				console.log(data);
				if(data.statusCode==200){
					$rootScope.productvendors=[]
					$.each(data.object, function(key, val) {
						$rootScope.productvendors.push(val);
					});
					$scope.showVendorDetail($rootScope.productvendors[0]);
				}
			});
	 }
		
	$scope.showVendorDetail=function(vendor){
		console.log(vendor);
		$scope.selectedVendor = angular.copy(vendor);
	};
	  

		
			
		$scope.newvendors=[];
		$scope.newthinkness=[];
		$scope.newgrades=[];
		
		$scope.addVendor = function() {
			$scope.vendor={};
			$scope.vendorForm.$setPristine();
			$('#vendorModalWindow').modal('show');
			
		}
		
		$scope.editSelectedVendor = function(selectedVendor) {
			console.log(selectedVendor);
			$scope.vendorEdit=angular.copy($scope.selectedVendor);
			$('#vendorModalEditWindow').modal('show');
			
		}
		
		$scope.reset=function(){
			$scope.vendor=angular.copy($scope.emptyVendor);
			$('#success-edit-alert').hide();
			$('#danger-edit-alert').hide();
			$('#success-alert').hide();
			$('#success-alert').hide();
		}
		
		$scope.successMessage="";
		$scope.errorMessage="";
	  $scope.saveVendor = function(aForm, type) {
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
				 var vendorData={};
				 if(type.toUpperCase()=='ADD'){
					 vendorData=angular.copy($scope.vendor);
				 }else if(type.toUpperCase()=='EDIT'){
					 vendorData=angular.copy($scope.vendorEdit);
				 }
				console.log("Vendor Data : " +JSON.stringify(vendorData));
				$http.post(webContextPath+"/vendor/save", vendorData).success(
					function(data) {
						if(data.statusCode==200){
							 if(type.toUpperCase()=='ADD'){
								 $scope.successMessage="Vendor details saved successfully."
								 $('#success-alert').show();
								 $('#success-alert').alert();
					             $("#success-alert").fadeIn(2000, 500);
					             $('#danger-alert').hide();
					             $rootScope.getVendorList();
							 }else if(type.toUpperCase()=='EDIT'){
								 $scope.successMessage="Vendor details updated successfully."
								  $('#success-edit-alert').show();
								 $('#success-edit-alert').alert();
					             $("#success-edit-alert").fadeIn(2000, 500);
					             $('#danger-edit-alert').hide();
					             $rootScope.getVendorList();
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
	  
	  $scope.deleteVendorModal=function(){
		  $('#confirmModal').modal('show');
	  }
	  
	  $scope.deleteVendor=function(vendor){
			console.log(vendor);
			$http.get(webContextPath +"/vendor/delete/"+vendor.agentId)
			.success(function(data){
				if(data.statusCode==200){
					$scope.successMessage=data.message;
					$('#success-alert').alert();
	         		$("#success-alert").fadeTo(2000, 500);
	         		$scope.getAllAgents();
				}else{
					$scope.errorMessage=data.message;
					$('#danger-alert').alert();
	         		$("#danger-alert").fadeTo(2000, 500);
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

