productApp.controller('employeeController', ['$rootScope','$scope','$http','$filter','$q', function($rootScope,$scope,$http,$filter,$q) {
	 $rootScope.appEmployees=[];
	 $scope.employee={};
	 $scope.emptyEmployee={};
	 var table = null;
	 $scope.availableRoles = [];
	 angular.element(document).ready(function () {
		 $rootScope.getEmployeeList();
		 
		 $scope.identityTypes=['Voter ID','Pan Card','Aadhaar Card','Passport','Driving Licence']
		 
		 $('#example1 tbody').on( 'click', 'tr', function () {
		     
		    } );
      });
	 
	 $scope.selectedRow =0;
	 $scope.rowHighilited=function(row)
	    {
	      $scope.selectedRow = row;    
	    }
	
	 $scope.addEmployee=function(){
		$scope.employeeForm.$setPristine();
		 $scope.employee={};
		 $('#employeeModalWindow').modal('show');
	 }
	 $scope.successMessage="";
	 $scope.errorMessage="";
	 $scope.processStart=false;
	 $scope.saveEmployee=function(employeeForm, type){
		 $scope.processStart=true;
		 console.log(employeeForm);
		 var inValidParams=[];
			angular.forEach(employeeForm, function(value, key) {
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
			 var employeeData={};
			 if(type.toUpperCase()=='ADD'){
				 employeeData=angular.copy($scope.employee);
			 }else if(type.toUpperCase()=='EDIT'){
				 employeeData=angular.copy($scope.editedEmployee);
			 }
			 $http.post(webContextPath+'/employee/save', employeeData)
				.success(function(data) {
					console.log(data);
					if(type.toUpperCase()=='ADD'){
						if(data.statusCode==200){
							 $scope.successMessage="Employee data saved successfully."
							 $('#success-alert').show();
							 $('#success-alert').alert();
				             $("#success-alert").fadeIn(2000, 500);
				             $rootScope.getEmployeeList();
				             employeeForm.$setPristine(true);
				             $scope.processStart=false;
				             $('#danger-alert').hide();
						}else {
							$scope.errorMessage=data.message;
							$('#success-alert').hide();
							 $('#danger-alert').alert();
							 $('#danger-alert').show();
				             $("#danger-alert").fadeIn(2000, 500);
				             
						}
					}else if(type.toUpperCase()=='EDIT'){
						if(data.statusCode==200){
							 $scope.successMessage="Employee data updated successfully."
							  $('#successedit-alert').show();
							 $('#successedit-alert').alert();
				             $("#successedit-alert").fadeIn(2000, 500);
				             $rootScope.getEmployeeList();
				             $scope.processStart=false;
				             $('#dangeredit-alert').hide();
						}else {
							 $('#successedit-alert').hide();
							$scope.errorMessage=data.message;
							$('#dangeredit-alert').show();
							$('#dangeredit-alert').alert();
				            $("#dangeredit-alert").fadeIn(2000, 500);
				             
						}
					}
			});
		 }	 
	 }
	 $scope.reset=function(){
		 $scope.employee=angular.copy($scope.emptyEmployee);
		 $('#successedit-alert').hide();
			$('#dangeredit-alert').hide();
			$('#success-alert').hide();
			$('#success-alert').hide();
	 };
	 
	 $rootScope.getEmployeeList=function(){
			$http.get(webContextPath+'/employee/list')
			.success(function(data) {
				console.log(data);
				if(data.statusCode==200){
					$rootScope.appUsers=[];
					var userData=data.object;
					$.each(userData, function(key, val) {
						$rootScope.appUsers.push(val);
					});
					$scope.getEmployeeDetail($rootScope.appUsers[0]);
				}else{
					
				}
			});
	 }
	 
	 $scope.getEmployeeDetail=function(selectedEmployee){
		 console.log(selectedEmployee);
		 $scope.selectedEmployee=angular.copy(selectedEmployee);
			var roles=[];
			$scope.employeeRole;
			 if($scope.selectedEmployee.userVO!=null){
				 var roleNames=[];
				 var role;
				 for (key in $scope.selectedEmployee.userVO.roles){
					 	 roleName=$scope.selectedEmployee.userVO.roles[key];
						 roleNames.push(roleName);
						 role={
							  roleId:key,
							  roleName:roleName,
							  description:roleNames.join(", ")
						  }
					  
				 }
				 roles.push(role);
				 $scope.employeeRole=roles;
			 }
		 
	 }
	 $scope.editSelectedEmployee=function(editSelectedEmployee){
		 $scope.editedEmployee=angular.copy($scope.selectedEmployee);
		 $('#employeeEditModalWindow').modal('show');
	 }
	 
	 $scope.deleteEmployeeModal=function(){
		 $('#confirmModal').modal('show');
	 }
	 $scope.deleteEmployee=function(selectedEmployee){
		 $http.get(webContextPath+'/employee/delete/'+$scope.selectedEmployee.employeeId)
			.success(function(data) {
				console.log(data);
				if(data.statusCode==200){
					 	 $scope.successMessage="Employee deleted successfully."
						 $('#success-alert').alert();
			             $("#success-alert").fadeIn(2000, 500);
			             $rootScope.getEmployeeList();
			             $('#confirmModalNo').modal('hide');
				}else{
					
				}
			});
	 }
	 	
	 $scope.createUser=function(selectedEmployee){
		console.log(selectedEmployee)
		 $http.get(webContextPath+'/user/roles')
		 .success(function(data){
			 $scope.availableRoles=[];
			 if(data.statusCode==200){
				 $.each(data.object,function(key,val){
					 $scope.availableRoles.push(val);
				 });
				 $scope.roleSelected=[];
				 $('#createUserDiv').modal('show');
				 $scope.employeeSelected=selectedEmployee;
				 $scope.employeeName=selectedEmployee.firstName + " " +selectedEmployee.lastName
				 console.log($scope.availableRoles)
			 }
			});
	 };
	 
	
	 
	 $scope.statusMessage="";
	 $scope.stateChanged = function (isChecked, role) {
		if(isChecked.value.toUpperCase()=='TRUE'){
			console.log(role.roleName);
			$scope.roleSelected.push(role);
			$scope.statusMessage="";
		}else if(isChecked.value.toUpperCase()=='FALSE'){
			var index = $scope.roleSelected.indexOf(role);
			$scope.roleSelected.splice(index, 1);
		}
		
		console.log($scope.roleSelected);
	 }
	 
	 $scope.assignRole=function(){
		 $scope.statusMessage="";
		 var roleDetails=[];
		 $.each($scope.roleSelected,function(key,val){
			 roleDetails.push(val.roleId);
		 });
		 var userdata={
				 firstName:$scope.employeeSelected.firstName,
				 lastName:$scope.employeeSelected.lastName,
				 roleIds:roleDetails
		 }
		 if(roleDetails.length==0){
			 $scope.statusMessage="Please select the roles for the user."
		 }else{
		 $http.post(webContextPath+'/user/create/'+$scope.employeeSelected.employeeId, JSON.stringify(userdata))
		 .success(function(data){
			 console.log(data);
			 if(data.statusCode==200){
				 $scope.statusMessage="";
				 $rootScope.getEmployeeList();
				 $('#roleDivCloseBtn').click();
			 }else{
				 $scope.statusMessage="Error in creating user."
			 }
			});
		 }
	 }
	 
	 
	 
	 
	 function refreshEmployeeDataTable(jsonData) {
			$('#example1').dataTable().fnDestroy();
			//$scope.loadEmployeeDataTable(jsonData);
		}
	 /*$scope.loadEmployeeDataTable=function(userData){
		 table=$('#example1').DataTable({
		       "aaData": userData,
		        "aoColumns": [
		            { "mData": "firstName" },
		            { "mData": "lastName" },
		            { "mData": function(data){
		            	 return data.idType +"-"+data.idProof
		            } },
		            { "mData": "email" },
		            { "mData": "mobile" }
		        ]
		    });
	 }*/
		
	$scope.states=[];
	$http.get(webContextPath+'/resources/json/state-cities.json').
	success(function(data) {
    	$scope.states = data;
    	console.log($scope.states);
	});
		
	
			  
    $scope.cities=[];
	$scope.getCities = function() {
		console.log($scope.employee.addressVO.state);
		$.each($scope.states, function(i, val) {
			console.log(val);
			if (val.name == $scope.employee.addressVO.state) {
				$scope.cities = val.cities;
				return false;
			}
		});
		console.log($scope.cities);
	};
	
	$scope.onlyNumbers = /^\d+$/;
	$scope.filterValue = function($event){
        if(isNaN(String.fromCharCode($event.keyCode))){
            $event.preventDefault();
        }
	};
		
	
}]);

