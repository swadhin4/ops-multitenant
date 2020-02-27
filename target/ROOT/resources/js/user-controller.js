productApp.controller('userController', ['$rootScope','$scope','$http','$filter','$q', function($rootScope,$scope,$http,$filter,$q) {
	$rootScope.appUsers=[];
	$scope.selectedUser={};
	
	 $scope.selectedRow =0;
	 $scope.rowHighilited=function(row)
	    {
	      $scope.selectedRow = row;    
	    }
	 angular.element(document).ready(function () {
		 $rootScope.getUserList();
    });
	
	 $rootScope.getUserList=function(){
			$http.get(webContextPath+'/user/list')
			.success(function(data) {
				console.log(data);
				if(data.statusCode==200){
					var userData=data.object;
					//$scope.loadDataTable(userData);
					var userInfo={};
					$rootScope.appUsers=[];
					$.each(userData, function(key, val) {
						console.log(val);
						var roles=[];
						 if(val.roles!=null){
							  var roleNames=[];
							 
							 for (key in val.roles){
								 roleName=val.roles[key];
								 var role={
									  userRoleId:key,
									  roleName:roleName,
									  description:roleNames.join(", ")
								  }
								 roles.push(role);
							 }
							 var roleAssigned=[];
							 $.each(roles,function(key,val){
								 roleAssigned.push(val.roleName)
							 });
							
							 userInfo={
										userid:val.userId,
										firstName:val.firstName,
										lastName:val.lastName,
										userName:val.userName,
										roles:roles,
										phone:val.phoneNo,
										assignedRoles:roleAssigned.join(", ")
									};
							 $rootScope.appUsers.push(userInfo);
						 }
						
					});
					/*$('#example1').dataTable({
						aaData:$rootScope.appUsers
					});*/
					//$scope.loadDataTable(userInfo);
				}else{
					
				}
			});
	 }
	 
	 $scope.roleSelected=[];
	 	$scope.assignRoles=function(selectedUser){
			console.log(selectedUser)
			
			 $http.get(webContextPath+'/user/roles')
			 .success(function(data){
				 console.log(data);
				 $scope.availableRoles=[];
				 if(data.statusCode==200){
					 var unassignedRoles=[];
					 $.each(data.object,function(key,val1){
						 unassignedRoles.push(val1);
						 $.each(selectedUser.roles, function(key,val2){
							 if(parseInt(val2.userRoleId) == val1.roleId){
									 $scope.selectedRole={
											 roleId:parseInt(val2.userRoleId),
											 value:'TRUE',
											 roleName:val1.roleName
									 }	 
									 $scope.roleSelected.push($scope.selectedRole) 
									 $scope.availableRoles.push($scope.selectedRole);
									 var index = unassignedRoles.indexOf(val1);
									 unassignedRoles.splice(index, 1);
									return false; 
							} 
						 });
					 });
					 $.each(unassignedRoles,function(key,val){
						 $scope.selectedRole={
								 roleId:parseInt(val.roleId),
								 value:'FALSE',
								 roleName:val.roleName
						 }	 
						 $scope.availableRoles.push($scope.selectedRole);
					 });
					 $('#roleUserDiv').modal('show');
					 $scope.selectedUser = selectedUser;
					 console.log($scope.availableRoles)
				 }
				});
		   
		 };
		 
		 $scope.statusMessage="";
		 $scope.stateChanged = function (isChecked, role) {
			 $scope.roleSelected=[];
			if(isChecked.value.toUpperCase()=='TRUE'){
				$scope.roleSelected.push(role);
				$scope.statusMessage="";
			}else if(isChecked.value.toUpperCase()=='FALSE'){
				var index = $scope.roleSelected.indexOf(role);
				$scope.roleSelected.splice(index, 1);
			}
			
		 }
		 
		 $scope.updateUserRole=function(selectedUser){
			 $scope.statusMessage="";
			 var roleDetails=[];
			 $.each($scope.availableRoles,function(key,val){
				 if(val.value=='TRUE'){
				 roleDetails.push(val.roleId);
				 }
			 });
			 var userdata={
					 userId:selectedUser.userid,
					 firstName:selectedUser.firstName,
					 lastName:selectedUser.lastName,
					 roleIds:roleDetails
			 }
			 console.log(userdata);
			 if(roleDetails.length==0){
				 $scope.statusMessage="Please select the roles for the user."
			 }else{
				 console.log(userdata);
				 $http.post(webContextPath+'/user/role/update', JSON.stringify(userdata))
				 .success(function(data){
					 console.log(data);
					 if(data.statusCode==200){
						 $scope.statusMessage="";
						 $rootScope.getUserList();
						 $('#roleDivCloseBtn').click();
					 }else{
						 $scope.statusMessage="Error in creating user."
					 }
					});
				 }
			 }
			
		 
		 $scope.manageUser=function(selectedUser){
			 
		 }
	
}]);

