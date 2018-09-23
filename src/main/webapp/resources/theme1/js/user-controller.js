

chrisApp.controller('userController',  ['$rootScope', '$scope', '$filter', '$location','userService','authService',
                                        'companyService','registrationService','roleService','siteService',
                                        function ($rootScope, $scope , $filter,$location,userService,authService,
                                        companyService,registrationService,roleService,siteService) {
	$scope.selectedRow =0;
	$scope.user ={};
	$scope.appUsers=[];
	$scope.selectedUser ={};
	$scope.sessionUser={};
	$scope.loggedInUserEmail = "";
	
	$scope.onlyNumbers = /^\d+$/;
	$scope.filterValue = function($event){
	    if(isNaN(String.fromCharCode($event.keyCode))){
	        $event.preventDefault();
	    }
	};
	
		angular.element(document).ready(function(){
			 $scope.findAllUsers();
			 $scope.rowHighilited(0);
			 $scope.findLoggedInUserAccess();
			 $scope.user.isEnabled="true";
			 $('#toggle-event1').parent().addClass("divToggle");
			 $('#toggle-event').change(function() {
			      $('#console-event').html($(this).prop('checked'));
			      $('#enabledUser').val($(this).prop('checked'));
			      $scope.user.isEnabled=$(this).prop('checked');
			      console.log($scope.user);
			    });
			 $('#toggle-event1').change(function() {
			      $scope.selectedUser.enabledordisabled=$(this).prop('checked');
			      console.log($scope.selectedUser.enabledordisabled);
			    }); 
			 $('.select2').select2();
			 
				$scope.getAllRoles('NEW');
		});
		
		$scope.rowHighilited=function(row){
	      $scope.selectedRow = row;    
	    }
		
		$scope.closeMessageWindow=function(){
			$('#messageWindow').hide();
			$('#successMessageDiv').hide();
			$('#errorMessageDiv').hide();
		}
		
		//---------------------X----------------------
		$scope.findAllUsers=function(){
		  $scope.getAllUsers();
	    };
		    
	    $scope.findLoggedInUserAccess=function(){
	    	$scope.getLoggedInUserAccess();
	    };
	    
	    $scope.addNewUser=function(){
	    	$('#resetAddUserForm').click();
	    //	$scope.getAllROCompanies();
	    	$scope.getAllRoles('NEW');
	    	$('#createUserModal').modal('show');
	    };
	    
	    $scope.saveNewUser=function(){
	    	//$scope.user.isEnabled = $('#enabledUser').val();
	    	console.log($scope.user);
	    	$scope.persistUser($scope.user);
	    };
	    $scope.getUserDetail=function(user){
	    	console.log(user)
	    	$scope.selectedUser = angular.copy(user);
	    	if($scope.selectedUser.isEnabled == 1){
	    		$scope.selectedUser.label="Active";
	    		$scope.selectedUser.status="green";
	    		//$('.divToggle').addClass('btn btn-primary');
	    	   //$('.divToggle').removeClass('btn btn-default off');
	    	}
	    	else if($scope.selectedUser.isEnabled == 0){
	    		$scope.selectedUser.label="Not Active";
	    		$scope.selectedUser.status="red";
	    		//$('.divToggle').addClass('btn btn-default off');
	    		//$('.divToggle').removeClass('btn-primary');
	    	}
	    };
	    
	    $scope.updateUserRole=function(){
	    	var roleId = $("#roleSelect1").val();
	    	if(roleId == ""){
	    	
	    	}else{
	    	 var role={
					 roleId:parseInt(roleId),
			 		 description:$("#roleSelect1 option:selected").text()
			 }
	    	 var enabledOrDisabled= $('#toggle-event1').prop('checked');
	    	 $scope.selectedUser.roleSelected=role;
	    	// $scope.selectedUser.enabledordisabled=enabledOrDisabled;
	    	console.log($scope.selectedUser);
	    		$scope.updateRoleInfo($scope.selectedUser);
	    	}
	    	
	    }
		    
   //--------- Services Call from User Controller -------------------	
		    
		    $scope.getAllUsers=function(){
		    	$('#loadingDiv').show();
		    	//var appuser=this;
			  //  appuser.userList=[];
		    	//appuser.retrieveAllUsers=function(){
	    		userService.retrieveAllUsers()
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$scope.appUsers=[];
	    				$.each(data.object,function(key,val){
	    					var role ={};
	    					if(val.roleNames.length>0){
	    						var roles = val.roleNames[0].split(",");
	    						role.roleId=roles[0];
	    						role.description=roles[1];
	    					}
	    					var userData={
	    							userId:val.userId,
	    							firstName:val.firstName,
	    							lastName:val.lastName,
	    							email:val.emailId,
	    							role:role,
	    							createdAt:val.createdAt,
	    							company:val.company,
	    							isEnabled:val.enabled,
	    							phone:val.phoneNo
	    					}
	    					$scope.appUsers.push(userData);
	    					$scope.getUserDetail($scope.appUsers[0]);
	    					$('#loadingDiv').hide();
	    				});
	    				console.log($scope.appUsers)
	    			}
	            },
	            function(data) {
	                console.log('UserList retrieval failed.')
	            }); 	
		    	//}
		    	// appuser.retrieveAllUsers();
		    	
		    }
		    
		    $scope.getLoggedInUserAccess =function(){
			   // var authUser=this;
				//$scope.userAccessDetail={};
				//authUser.featureList={};
				//authUser.authorizeUserAccess=function(){
				authService.loggedinUserAccess()
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$scope.loggedInUserEmail = data.loggedInUserMail;
	    				$scope.sessionUser=data;
	    				
	    			}
	            },
	            function(data) {
	                console.log('Unauthorized Access.')
	            }); 	
		    	//}
				//authUser.authorizeUserAccess();
		    }
		    
		    $scope.getAllROCompanies=function(){
		    	//var adminUser=this;
				$scope.roCompanyList=[];
				
				//adminUser.retrieveAllROCompanies=function(){
					companyService.retrieveAllROCompanies()
		    		.then(function(data) {
		    			console.log(data)
		    				
			    			$.each(data,function(key,val){
			    				$scope.roCompanyList.push(val);
			    			});
		            },
		            function(data) {
		                console.log('Unable to get company List')
		            }); 	
		    	//}
				//adminUser.retrieveAllROCompanies();
		    }
		    
		    $scope.getAllRoles=function(option){
		    	//adminUser.retrieveRoles=function(){
		    	$scope.roles={
		    		selected:{},
		    		list:[]	
		    	}
					roleService.retrieveRoles()
		    		.then(function(data) {
		    			console.log(data)
		    			if(data.statusCode == 200){	
		    			$.each(data.object,function(key,val){
		    				$scope.roles.list.push(val);
		    			});
		    			$("#roleSelect").empty();
		    			var options = $("#roleSelect");
    					options.append($("<option />").val("").text("Select Role"));
    					$.each($scope.roles.list,function() {
    						options.append($("<option />").val(	this.roleId).text(this.description));
    					});
    					$("#roleSelect1").empty();
		    			var options2 = $("#roleSelect1");
    					options2.append($("<option />").val("").text("Select Role"));
    					$.each($scope.roles.list,function() {
    						options2.append($("<option />").val(this.roleId).text(this.description));
    					});
			    			if(option.toUpperCase() == 'EDIT'){
			    				 $("#roleSelect option").each(function() {
	          							if ($(this).val() == parseInt($scope.selectedUser.role.roleId)) {
	          								$(this).attr('selected', 'selected');
	          							return false;
	          							}
	          					 });
			    				 
			    				 
			    			}
		    			}
		            },
		            function(data) {
		                console.log('Unable to get role List')
		            }); 	
		    	//}
				//adminUser.retrieveRoles();
		    }
		    
		   
		    
		    $scope.persistUser=function(persitedUser){
		    	$('#loadingDiv').show();
		    	//var registerObj=this;
		    	//registerObj.registerUser=function(){
					registrationService.registerUser(persitedUser)
		    		.then(function(data) {
		    			console.log(data)
		    			if(data.statusCode == 200){
		    				$('#newUserCloseBtn').click();
		    				$('#messageWindow').show();
		    				$scope.successMessage = data.message;
		    				$('#successMessageDiv').show();
		    				$('#successMessageDiv').alert();
		    				$('#errorMessageDiv').hide();
		    				$scope.findAllUsers();
		    				$('#loadingDiv').hide();
		    			}else{
		    				$scope.modalErrorMessage = data.message;
		    				$('#modalMessageDiv').show();
		    				$('#modalMessageDiv').alert();
		    				$('#errorMessageDiv').hide();
		    			}
		            },
		            function(data) {
		                console.log('Unable to register user')
		              $('#messageWindow').show();
		                $scope.errorMessage = data.message;
		                $('#successMessageDiv').hide();
		                $('#errorMessageDiv').alert();
		                $('#errorMessageDiv').show();
	    				
		            }); 	
		    	//}
		    	//registerObj.registerUser();
		    }
		    
		    $scope.manageUser=function(selectedUser){
		    	console.log(selectedUser);
		    	$scope.user=selectedUser;
		    	$scope.getAllRoles('EDIT');
		    }
		    
		    $scope.updateAccountStatus=function(selectedUser, status){
		    	if(status == '1'){
		    		$scope.changeAccountStatus(selectedUser,1);
		    	}else if(status == '0'){
		    		$scope.changeAccountStatus(selectedUser,0);
		    	}
		    }
		    
		    $scope.changeAccountStatus=function(selectedUser, status){
		    	$('#loadingDiv').show();
		    	userService.enableOrDisableUser(selectedUser.userId,status)
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$('#messageWindow').show();
	    				$scope.successMessage = data.message;
	    				$('#successMessageDiv').show();
	    				$('#successMessageDiv').alert();
	    				$scope.findAllUsers();
	    				$('#loadingDiv').hide();
	    			}
	            },
	            function(data) {
	                console.log('Unable to change the status of the user')
	                
	            }); 	
		    }
		    
		    $scope.updateRoleInfo=function(selectedUser){
		    	$('#loadingDiv').show();
		    	roleService.updateRole(selectedUser)
		    	.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$('#messageWindow').show();
	    				$scope.successMessage = data.message;
	    				$('#successMessageDiv').show();
	    				$('#successMessageDiv').alert();
	    				$scope.findAllUsers();
	    				$('#loadingDiv').hide();
	    			}
	            },
	            function(data) {
	                console.log('Unable to change the role for the user')
	                
	            }); 	
		    }
}]);
function validateDropdownValues(dropDownId){
	var scope = angular.element("#userWindow").scope();
	 var valueId = parseInt($("#"+dropDownId).val());
	 if(valueId == ""){
	 
	 }else{
		 if(dropDownId.toUpperCase() == "ROLESELECT"){
			 var role={
					 roleId:parseInt($("#roleSelect").val()),
			 		 description:$("#roleSelect option:selected").text()
			 }
			 scope.user.role=role;
		 }
		 if(dropDownId.toUpperCase() == "ROLESELECT1"){
			 var role={
					 roleId:parseInt($("#roleSelect1").val()),
			 		 description:$("#roleSelect1 option:selected").text()
			 }
			 scope.selectedUser.roleSelected=role;
		 }
	 }
}

