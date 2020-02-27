productApp.controller('warehouseController', ['$rootScope','$scope','$http','$filter','employeeService', function($rootScope,$scope,$http,$filter,employeeService) {
	
	$scope.warehouses=[];
	$scope.warehouse={};
	$scope.emptyWarehouse={};
	$scope.allEmployees=[];
	
	  $scope.isDataAvailable1=false;
	    $scope.isDataAvailable2=false;
	
	    $scope.selectedRow =0;
		 $scope.rowHighilited=function(row)
		    {
		      $scope.selectedRow = row;    
		    }
	 angular.element(document).ready(function () {
		 $scope.getAllWarehouses();
		 $scope.getAllEmployees();
	 });
	
	 $scope.getAllWarehouses=function(){
		 $http.get(webContextPath+'/warehouse/list')
			.success(function(data) {
				console.log(data);
				if(data.statusCode==200){
					$scope.warehouses=[];
					$.each(data.object, function(key, val) {
						$scope.warehouses.push(val);
					});
					$scope.showWarehouseDetail($scope.warehouses[0]);
				}
			});
	 }
	 $scope.getAllEmployees=function(){
		 var appuser=this;
 	      appuser.employeeList=[];
	    	appuser.getAllEmployee=function(){
	    		employeeService.getAllEmployee()
	    		.then(function(data) {
	    			appuser.employeeList = data.object;
	                console.log('Employee List returned to controller from service.');
	                if(appuser.employeeList.length>0){
	                	$.each(appuser.employeeList,function(key,val){
	                		  var employee={
	                				  employeeId:val.employeeId,
	                				  firstName:val.firstName,
	                				  lastName:val.lastName,
	                				  name:val.firstName+" "+val.lastName,
	                				  mobile:val.mobile
	                		  }
	                		  $scope.warehouse.employeeVO=angular.copy(employee);
	                		  $scope.allEmployees.push($scope.warehouse.employeeVO);
	                	});
	                	$scope.isDataAvailable1=true;
	                }
	            },
	            function(data) {
	                console.log('Employeelist retrieval failed.')
	            }); 	
	    	}
	    	 appuser.getAllEmployee();
	 }
	 
	 $scope.getSelectedEmployee=function(selectedEmloyee){
		 $scope.warehouse.employeeVO=selectedEmloyee;
	 }
	
	$scope.showWarehouseDetail=function(selectedWarehouse){
		$scope.selectedWarehouse=selectedWarehouse;
		console.log($scope.selectedWarehouse)
	}
	
	$scope.addWarehouse = function() {
		$scope.warehouseForm.$setPristine();
		$scope.warehouse={};
		$('#warehouseModalWindow').modal('show');
		
	}
	
	$scope.editSelectedWarehouse = function(selectedWarehouse) {
		console.log(selectedWarehouse);
		$scope.warehouseEdit=angular.copy($scope.selectedWarehouse);
		$.each($scope.allEmployees,function(key,val){
			if(val.employeeId == $scope.selectedWarehouse.employeeVO.employeeId){
				$scope.warehouseEdit.employeeVO=val;
				return false;
			}
		});
		$('#warehouseEditModalWindow').modal('show');
		
	}
	
	$scope.reset=function(){
		$scope.warehouse=angular.copy($scope.emptyWarehouse);
		$('#success-edit-alert').hide();
		$('#danger-edit-alert').hide();
		$('#success-alert').hide();
		$('#success-alert').hide();
	 };
	$scope.saveWarehouse = function(aForm, type) {
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
			 var warehouseData={};
			 if(type.toUpperCase()=='ADD'){
				 warehouseData=angular.copy($scope.warehouse);
			 }else if(type.toUpperCase()=='EDIT'){
				 warehouseData=angular.copy($scope.warehouseEdit);
			 }
			console.log("Warehouse Data : " +JSON.stringify(warehouseData));
			$http.post(webContextPath+"/warehouse/save", warehouseData).success(
				function(data) {
					if(data.statusCode==200){
						 if(type.toUpperCase()=='ADD'){
							 $scope.successMessage="Warehouse details saved successfully."
							 $('#success-alert').show();
							 $('#success-alert').alert();
				             $("#success-alert").fadeIn(2000, 500);
				             $('#danger-alert').hide();
				             $scope.getAllWarehouses();
						 }else if(type.toUpperCase()=='EDIT'){
							 $scope.successMessage="Warehouse details updated successfully."
							  $('#success-edit-alert').show();
							 $('#success-edit-alert').alert();
				             $("#success-edit-alert").fadeIn(2000, 500);
				             $('#danger-edit-alert').hide();
				             $scope.getAllWarehouses();
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
  
	  $scope.deleteWarehouseModal=function(){
		  $('#confirmModal').modal('show');
	  }
  
	  $scope.deleteWarehouse=function(warehouse){
			console.log(warehouse);
			$http.get(webContextPath +"/warehouse/delete/"+warehouse.warehouseId)
			.success(function(data){
				if(data.statusCode==200){
					$scope.successMessage=data.message;
					$('#success-alert').alert();
	         		$("#success-alert").fadeTo(2000, 500);
	         		$scope.getAllWarehouses();
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

