

chrisApp.controller('spUserController',  ['$rootScope', '$scope', '$filter', '$location','userService','authService',
                                        'companyService','registrationService','roleService','siteService',
                                        function ($rootScope, $scope , $filter,$location,userService,authService,
                                        companyService,registrationService,roleService,siteService) {
	$scope.selectedSPUserRow =0;
	$scope.user ={};
	$scope.appUsers=[];
	$scope.selectedUser ={};
	$scope.sessionUser={};
	$scope.loggedInUserEmail = "";
	$scope.SPCustomers={};
	
	$scope.onlyNumbers = /^\d+$/;
	$scope.filterValue = function($event){
	    if(isNaN(String.fromCharCode($event.keyCode))){
	        $event.preventDefault();
	    }
	};
	
		angular.element(document).ready(function(){
			 $scope.findAllSPUsers();
			 $scope.rowSPUserHighilited(0);
			 $scope.findLoggedInSPUserAccess();
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
			$scope.getSPUserAllRoles('NEW');
		});
		
		$scope.rowSPUserHighilited=function(row){
	      $scope.selectedSPUserRow = row;    
	    }
		
		$scope.closeSPUserMessageWindow=function(){
			$('#messageWindow').hide();
			$('#successMessageDiv').hide();
			$('#errorMessageDiv').hide();
		}
		
		//---------------------X----------------------
		$scope.findAllSPUsers=function(){
		  $scope.getAllSPUsers();
	    };
		    
	    $scope.findLoggedInSPUserAccess=function(){
	    	$scope.getLoggedInSPUserAccess();
	    };
	    
	    $scope.addNewSPUser=function(){
	    	$('#resetAddSPUserForm').click();
	    //	$scope.getAllROCompanies();
	    	$scope.getSPUserAllRoles('NEW');
	    	$('#createSPUserModal').modal('show');
	    };
	    
	    
	    
	    $scope.SPCustomers = [
            { CustomerCode: 1, CustomerName: "John Hammond", Location: "United States", Selected: true },
            { CustomerCode: 2, CustomerName: "Mudassar Khan", Location: "India", Selected: true },
            { CustomerCode: 3, CustomerName: "Suzanne Mathews", Location: "France", Selected: true },
            { CustomerCode: 4, CustomerName: "Robert Schidner", Location: "Russia", Selected: true }
           ];
	    
	    
	    $scope.CheckUncheckHeader = function () {
            $scope.IsAllChecked = true;
            for (var i = 0; i < $scope.SPCustomers.length; i++) {
                if (!$scope.SPCustomers[i].Selected) {
                    $scope.IsAllChecked = false;
                    break;
                }
            };
        };
        $scope.CheckUncheckHeader();

        $scope.CheckUncheckAll = function () {
            for (var i = 0; i < $scope.SPCustomers.length; i++) {
                $scope.SPCustomers[i].Selected = $scope.IsAllChecked;
            }
        };
	    
	    
	    $scope.saveNewSPUser=function(){
	    	//$scope.user.isEnabled = $('#enabledUser').val();
	    	console.log($scope.user);
	    	$scope.persistSPUser($scope.user);
	    };
	    $scope.getSPUserDetail=function(user){
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
	    	 $scope.selectedUser.companyName= $scope.selectedUser.company;
	    	 var userCompany={
	    			 company:$scope.selectedUser.company
	    	 }
	    	 $scope.selectedUser.company= userCompany;
	    	// $scope.selectedUser.enabledordisabled=enabledOrDisabled;
	    	console.log($scope.selectedUser);
	    		$scope.updateSPUserRoleInfo($scope.selectedUser);
	    	}
	    	
	    }
		    
   //--------- Services Call from User Controller -------------------	
		    
		    $scope.getAllSPUsers=function(){
		    	$('#loadingDiv').show();
		    	//var appuser=this;
			  //  appuser.userList=[];
		    	//appuser.retrieveAllUsers=function(){
	    		userService.retrieveAllSPUsers()
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$scope.appUsers=[];
	    				$.each(data.object,function(key,val){
	    					var userData={
	    							userId:val.userId,
	    							firstName:val.firstName,
	    							lastName:val.lastName,
	    							email:val.emailId,
	    							roleId:val.roleId,
	    							description:val.roleName,
	    							company:val.companyName,
	    							isEnabled:val.enabled,
	    							phone:val.phoneNo
	    					}
	    					$scope.appUsers.push(userData);
	    					$scope.getSPUserDetail($scope.appUsers[0]);
	    					$('#loadingDiv').hide();
	    				});
	    				console.log($scope.appUsers)
	    			}
	            },
	            function(data) {
	                console.log('UserList retrieval failed.')
	                $('#loadingDiv').hide();
	            }); 	
		    	//}
		    	// appuser.retrieveAllUsers();
		    	
		    }
		    
		    $scope.getLoggedInSPUserAccess =function(){
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
		    
		    $scope.getSPUserAllRoles=function(option){
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
		    
		   
		    
		    $scope.persistSPUser=function(persitedSPUser){
		    	$('#loadingDiv').show();
		    	//var registerObj=this;
		    	//registerObj.registerUser=function(){
					registrationService.registerUser(persitedSPUser)
		    		.then(function(data) {
		    			console.log(data)
		    			if(data.statusCode == 200){
		    				$('#newUserCloseBtn').click();
		    				$('#messageWindow').show();
		    				$scope.successMessage = data.message;
		    				$('#successMessageDiv').show();
		    				$('#successMessageDiv').alert();
		    				$('#errorMessageDiv').hide();
		    				$scope.findAllSPUsers();
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
		    
		    $scope.manageSPUser=function(selectedUser){
		    	console.log(selectedUser);
		    	$scope.user=selectedUser;
		    	$scope.getSPUserAllRoles('EDIT');
		    }
		    
		    $scope.updateSPAccountStatus=function(selectedUser, status){
		    	if(status == '1'){
		    		$scope.changeSPAccountStatus(selectedUser,1);
		    	}else if(status == '0'){
		    		$scope.changeSPAccountStatus(selectedUser,0);
		    	}
		    }
		    
		    $scope.changeSPAccountStatus=function(selectedUser, status){
		    	$('#loadingDiv').show();
		    	userService.enableOrDisableUser(selectedUser.userId,status)
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$('#messageWindow').show();
	    				$scope.successMessage = data.message;
	    				$('#successMessageDiv').show();
	    				$('#successMessageDiv').alert();
	    				$scope.findAllSPUsers();
	    				$('#loadingDiv').hide();
	    			}
	            },
	            function(data) {
	                console.log('Unable to change the status of the user')
	                
	            }); 	
		    }
		    
		    $scope.updateSPUserRoleInfo=function(selectedUser){
		    	$('#loadingDiv').show();
		    	roleService.updateRole(selectedUser)
		    	.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$('#messageWindow').show();
	    				$scope.successMessage = data.message;
	    				$('#successMessageDiv').show();
	    				$('#successMessageDiv').alert();
	    				$scope.findAllSPUsers();
	    				$('#loadingDiv').hide();
	    			}
	            },
	            function(data) {
	                console.log('Unable to change the role for the user')
	                $('#loadingDiv').hide();
	                
	            }); 	
		    }
}]);
function validateSPUserDropdownValues(dropDownId){
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

