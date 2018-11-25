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
	$scope.SPUserCustomers={};
	$scope.SPUserAllCustomers={};
	
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
			$scope.rowSPUserHighilited(0);
		}
		
		//---------------------X----------------------
		$scope.findAllSPUsers=function(){
		  $scope.getAllSPUsers();
	    };
		    
	    $scope.findLoggedInSPUserAccess=function(){
	    	$scope.getLoggedInSPUserAccess();
	    };
	    
	    $scope.addNewSPUser=function(){
	    	$scope.user.userType="SP";
	    	 $scope.SPUserAllCustomers = [];
	    	userService.retrieveAllSPCustomers()
	    	.then(function(data){
	    		console.log(data);
	    		$('#resetAddSPUserForm').click();
		    	if(data.statusCode==200){
		    		if(data.object.length>0){
		    			$.each(data.object,function(key,val){
		    				 $scope.SPUserAllCustomers.push(val);
		    			});
		    		}
		    		$scope.getSPUserAllRoles('NEW');
		    		$('#createSPUserModal').modal('show');
		    	}
		    	
	    	},function(data){
	    		console.log(data);
	    	});
	    	
	    };
	    
	    $scope.CheckUncheckHeaderUser = function () {
            $scope.IsAllCheckedUser = true;
            for (var i = 0; i < $scope.SPUserCustomers.length; i++) {
                if (!$scope.SPUserCustomers[i].selected) {
                    $scope.IsAllCheckedUser = false;
                    break;
                }
            };
        };
        

        $scope.CheckUncheckAllUser = function () {
            for (var i = 0; i < $scope.SPUserCustomers.length; i++) {
                $scope.SPUserCustomers[i].selected = $scope.IsAllCheckedUser;
            }
        };
        
        
        $scope.CheckUncheckHeaderUserAll = function () {
            $scope.IsAllCheckedUserAll = true;
            for (var i = 0; i < $scope.SPUserAllCustomers.length; i++) {
                if (!$scope.SPUserAllCustomers[i].selected) {
                    $scope.IsAllCheckedUserAll = false;
                    break;
                }
            };
        };
        

        $scope.CheckUncheckAllUserAll = function () {
            for (var i = 0; i < $scope.SPUserAllCustomers.length; i++) {
                $scope.SPUserAllCustomers[i].selected = $scope.IsAllCheckedUserAll;
            }
        };
        
        $scope.updateUserMapping=function(user){
	    	console.log("updateUserMapping Map Customer Update---->",user)
	    	$scope.selectedUser = angular.copy(user);
	    	$scope.SPUserCustomersData = [];
	    	for (var i = 0; i < $scope.SPUserCustomers.length; i++) {
	    		var accessId = $scope.SPUserCustomers[i].accessId;
	    		var isSelected = $scope.SPUserCustomers[i].selected;
	    		var isDelFlagEnabled = $scope.SPUserCustomers[i].delFlagEnabled;
	    		
	    		
	    		if(isSelected) {
	    			$scope.SPUserCustomersData.push({
    					"accessId":accessId || null,
    					"customerId":$scope.SPUserCustomers[i].customerId,
                		"customerCode":$scope.SPUserCustomers[i].customerCode,
                		"customerName":$scope.SPUserCustomers[i].customerName,
            			"countryName": $scope.SPUserCustomers[i].countryName,
            			"isDelFlagEnabled": isDelFlagEnabled,
            			 operation:"UPDATE",
            			 value: (isSelected==true ?"ZERO":"ONE")
            		});
	    			
	    			} else {
	    				$scope.SPUserCustomersData.push({
	    					"accessId":accessId || null,
	    					"customerId":$scope.SPUserCustomers[i].customerId,
	                		"customerCode":$scope.SPUserCustomers[i].customerCode,
	                		"customerName":$scope.SPUserCustomers[i].customerName,
	            			"countryName": $scope.SPUserCustomers[i].countryName,
	            			"isDelFlagEnabled": isDelFlagEnabled,
	            			operation:"NEW",
	            			 value: ( isSelected==false ?"ONE":"ZERO")
	            			});
	    			} 			    		
	    	}
	    	console.log("Map Customer Update SPUserCustomersData---->",$scope.SPUserCustomersData);
	    		    	
	    	userService.updateSPUserCustomers($scope.selectedUser.userId,$scope.SPUserCustomersData)
    		.then(function(data) {
    			console.log("updateSPUserCustomers----->",data)
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
	    
	    
	    $scope.saveNewSPUser=function(){
	    	//$scope.user.isEnabled = $('#enabledUser').val();
	    	console.log("saveNewSPUser---->",$scope.user);
	    	$scope.SPUserAllCustomersData = [];
	    	for (var i = 0; i < $scope.SPUserAllCustomers.length; i++) {
	    		if($scope.SPUserAllCustomers[i].selected){                	
                	$scope.SPUserAllCustomersData.push({
                		"customerId":$scope.SPUserAllCustomers[i].customerId,
                		"customerCode":$scope.SPUserAllCustomers[i].customerCode,
                		"customerName":$scope.SPUserAllCustomers[i].customerName,
            			"countryName": $scope.SPUserAllCustomers[i].countryName
            		});            		
                }
	    	}
	    	console.log("SPUserCustomersData---->",$scope.SPUserAllCustomersData);
	    	$scope.newSPUserAction($scope.user,$scope.SPUserAllCustomersData);
	    	
	    	
	    };
	    
	    $scope.getSPUserDetail=function(user){
	    	console.log(user)
	    	 $scope.SPUserCustomers=[];
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
	    	
	    	userService.retrieveCustomersBySelectedSP($scope.selectedUser.userId)
	    	.then(function(data){
	    		console.log(data);
		    	if(data.statusCode==200){
		    		if(data.object.length>0){
		    			$.each(data.object,function(key,val){
		    				 $scope.SPUserCustomers.push(val);
		    			});
		    			
		    			//For header check true while updating Customer
		    			//Supravat
		    			var isAllSelected = 0
		    			for (var i = 0; i < $scope.SPUserCustomers.length; i++) {
		    	    		if($scope.SPUserCustomers[i].selected){
		    	    			isAllSelected++;
		    	    		}
		    			}
		    			console.log("isAllSelected---->",isAllSelected);
		    			console.log("length",$scope.SPUserCustomers.length);
		    			if(isAllSelected==$scope.SPUserCustomers.length) {
		    				$scope.IsAllCheckedUser = true;
		    			} else {
		    				$scope.IsAllCheckedUser = false;
		    			}
		    			//End
		    			
		    			
		    		}
		    	}
		    	
	    	},function(data){
	    		console.log(data);
	    	});
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
	    		userService.retrieveAllUsers()
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
	    							phone:val.phoneNo,
	    					}
	    					$scope.appUsers.push(userData);
	    					
	    				});
	    				$('#loadingDiv').hide();
	    				$scope.getSPUserDetail($scope.appUsers[0]);
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
		    
		   
		    
		    $scope.newSPUserAction=function(persitedSPUser,SPUserAllCustomersData){
		    	console.log("newSPUserAction persitedSPUser----->",persitedSPUser)
		    	console.log("newSPUserAction SPUserAllCustomersData----->",SPUserAllCustomersData)
		    	$('#loadingDiv').show();
		    	//var registerObj=this;
		    	//registerObj.registerUser=function(){
		    	persitedSPUser.customerList = SPUserAllCustomersData;
					registrationService.registerUser(persitedSPUser)
		    		.then(function(data) {
		    			console.log("newSPUserAction----->",data)
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
		    	selectedUser.status=0;
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

