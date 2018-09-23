chrisApp.controller('registerController', 
		 ['$rootScope', '$scope', '$filter','userService',
function  ($rootScope, $scope , $filter,userService) {

		$scope.customer={};	 
		$scope.responseMessage={};
		
	    $scope.saveCustomer=function(){
    		var customer=this;
    		var newCustomer = angular.copy($scope.customer);
    		customer.registerUser=function(){
	    		userService.registerUser(newCustomer)
	    		.then(function(data) {
	    			 console.log(data);
	    			 if(data.statusCode == 200){
	    				 $scope.responseMessage={
	    						 info:data.message,
	    						 color:"green"
	    				 };
	    				 $scope.customer.setPristine();
	    				 $scope.customer={};
	    			 }else{
	    				 $scope.responseMessage={
    						 info:data.message,
    						 color:"red"
	    			 	 };
	    			 }
	            },
	            function(data) {
	            	console.log(data)
	            	 $scope.responseMessage={
						 info:data.message,
						 color:"red"
   			 	    };
	            }); 	
	    	}
    		customer.registerUser();
      };
			 
}]);

chrisApp.controller('loginController', 
		 ['$rootScope', '$scope', '$filter', '$location','userService',
	function ($rootScope, $scope , $filter,$location,userService) {
		$scope.user={};	 
		 var loggedInuser =null;
	    $scope.validateUser=function(){
	    		var appuser=this;
	    	    appuser.validatedUser={};
	    	    $scope.loader1=true;
		    	appuser.validateUser=function(){
		    		userService.validateUser( $scope.user.email, $scope.user.password)
		    		.then(function(data) {
		    			 $scope.savedUser = angular.copy(data);
		    			 $scope.savedUser.password="";
		    			 console.log(data);
		    			 $scope.user ={};
		    			 loggedInuser = $.jStorage.set('loggedInUser', $scope.savedUser);
		    			 $location.path('/userhome');
		            },
		            function(data) {
		               console.log(data)
		            }); 	
		    	}
		    appuser.validateUser();
	    };
	    
}]);

chrisApp.controller('userController',  ['$rootScope', '$scope', '$filter', '$location','userService','authService',
                                        'companyService','registrationService','roleService','siteService',
                                        function ($rootScope, $scope , $filter,$location,userService,authService,
                                        companyService,registrationService,roleService,siteService) {
	$scope.selectedRow =0;
	$scope.user ={};
	$scope.appUsers=[];
	$scope.selectedUser ={};
		angular.element(document).ready(function(){
			 $scope.findAllUsers();
			 $scope.rowHighilited(0);
			 $scope.findLoggedInUserAccess();
			 $('#toggle-event').change(function() {
			      $('#console-event').html($(this).prop('checked'));
			      $('#enabledUser').val($(this).prop('checked'));
			      $scope.user.isEnabled=$(this).prop('checked');
			      console.log($scope.user);
			    });
			 $('.select2').select2()
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
	    	$scope.getAllRoles();
	    };
	    
	    $scope.saveNewUser=function(){
	    	//$scope.user.isEnabled = $('#enabledUser').val();
	    	console.log($scope.user);
	    	$scope.persistUser($scope.user);
	    };
	    $scope.getUserDetail=function(user){
	    	console.log(user)
	    	$scope.selectedUser = angular.copy(user);
	    };
		    
   //--------- Services Call from User Controller -------------------	
		    
		    $scope.getAllUsers=function(){
		    	
		    	//var appuser=this;
			  //  appuser.userList=[];
		    	//appuser.retrieveAllUsers=function(){
	    		userService.retrieveAllUsers()
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$scope.appUsers=[];
	    				$.each(data.object,function(key,val){
	    					var roleName ="";
	    					if(val.roleNames.length>0){
	    						var roles = val.roleNames[0].split(",");
	    						roleName=roles[1];
	    					}
	    					var userData={
	    							userId:val.userId,
	    							firstName:val.firstName,
	    							lastName:val.lastName,
	    							email:val.emailId,
	    							role:roleName,
	    							createdAt:val.createdAt,
	    							company:val.company
	    					}
	    					$scope.appUsers.push(userData);
	    					$scope.getUserDetail($scope.appUsers[0])
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
		    
		    $scope.getAllRoles=function(){
		    	//adminUser.retrieveRoles=function(){
		    	$scope.roles=[];
					roleService.retrieveRoles()
		    		.then(function(data) {
		    			console.log(data)
		    			if(data.statusCode == 200){	
		    			$.each(data.object,function(key,val){
		    				$scope.roles.push(val);
		    			});
		    			$('#createUserModal').modal('show');
		    			}
		            },
		            function(data) {
		                console.log('Unable to get role List')
		            }); 	
		    	//}
				//adminUser.retrieveRoles();
		    }
		    
		   
		    
		    $scope.persistUser=function(persitedUser){
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
}]);

chrisApp.controller('serviceProviderController',  ['$rootScope', '$scope', '$filter','authService',
function  ($rootScope, $scope , $filter, authService) {
		
		$scope.serviceProvider={};
		
		angular.element(document).ready(function(){
			$scope.getLoggedInUserAccess();
		});
		
		 $scope.getLoggedInUserAccess =function(){
				authService.loggedinUserAccess()
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				
	    			}
	            },
	            function(data) {
	                console.log('Unauthorized Access.')
	            }); 	
		    }
		 
		$scope.addNewServiceProvider=function(){
			$('#createServiceProviderModal').modal('show');
		} 
}]);    

chrisApp.controller('siteController',  ['$rootScope', '$scope', '$filter','siteService','authService',
function  ($rootScope, $scope , $filter,siteService, authService) {
		
		$scope.siteData={};
		
		angular.element(document).ready(function(){
			$scope.getLoggedInUserAccess();
		});
		
		 $scope.getLoggedInUserAccess =function(){
			   // var authUser=this;
				//$scope.userAccessDetail={};
				//authUser.featureList={};
				//authUser.authorizeUserAccess=function(){
				authService.loggedinUserAccess()
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				
	    			}
	            },
	            function(data) {
	                console.log('Unauthorized Access.')
	            }); 	
		    	//}
				//authUser.authorizeUserAccess();
		    }
		    
		
		$scope.addNewSite=function(){
			$('#createSiteModal').modal('show');
		}
		//License related logic
		$scope.licenseDetails = [
	      {
	        'licensename':'Admin',
	        'validfrom':'',
	        'validto':''
	    }];
	              			 
		$scope.addNewLicense = function(licenseDetails){
			 $scope.licenseDetails.push({ 
			  'licensename': "", 
			  'validfrom': "" ,
			  'validto': "",
			 });
			 $scope.LD = {};
		};
			 
	$scope.removeLicense = function(){
		  var newDataList=[];
		  $scope.selectedAll = false;
		  angular.forEach($scope.licenseDetails, function(selected){
		      if(!selected.selected){
		         newDataList.push(selected);
		       }
		    }); 
		        $scope.licenseDetails = newDataList;
		   };
		   
    $scope.checkAllLicense = function () {
    	if (!$scope.selectedAll) {
		   $scope.selectedAll = true;
		  } else {
		    $scope.selectedAll = false;
		     }
		   angular.forEach($scope.licenseDetails, function(licenseDetail) {
			   licenseDetail.selected = $scope.selectedAll;
		    });
		 };


		//Sales Operation and delivery operation details//
		              			 
	$scope.operatingTimes = [		                         
  			 { Id: 1,name: '1:00'},
             {Id: 2, Name: '2:00'},
             {Id: 3, Name: '3:00' },
             {Id: 4, Name: '4:00' },
             {Id: 5, Name: '5:00' },
             {Id: 6, Name: '6:00' },
             {Id: 7, Name: '7:00' },
             {Id: 8, Name: '8:00' },
             {Id: 9, Name: '9:00' },
             {Id: 10,Name: '10:00' },
             {Id: 11,Name: '11:00' },
             {Id: 12,Name: '12:00' },
             {Id: 13, Name: '13:00'},
             {Id: 14, Name: '14:00'},
             {Id: 15, Name: '15:00'},
             {Id: 16, Name: '16:00'},
             {Id: 17, Name: '17:00'},
             {Id: 18, Name: '18:00'},
             {Id: 19, Name: '19:00'},
             {Id: 20, Name: '20:00'},
             {Id: 21, Name: '21:00'},
             {Id: 22, Name: '22:00'},
             {Id: 23, Name: '23:00'},
             {Id: 24, Name: '24:00'},
		   ];
		              
		              
		              			 
	$scope.salesoperationDetails = [
        {
            'days':'Monday',
            'from':'',
            'to':''
        },
        {
            'days':'Tuesday',
            'from':'',
            'to':''
        },
        {
            'days':'Wednessday',
            'from':'',
            'to':''
        },
        {
            'days':'Thursday',
            'from':'',
            'to':''
        },
        {
            'days':'Friday',
            'from':'',
            'to':''
        },
        {
            'days':'Saturday',
            'from':'',
            'to':''
        },
        {
            'days':'Sunday',
            'from':'',
            'to':''
        }];
		              			
		             //Delivery Operation details
 $scope.deliveryoperationDetails = [
        {
            'days':'Monday',
            'from':'',
            'to':''
        },
        {
            'days':'Tuesday',
            'from':'',
            'to':''
        },
        {
            'days':'Wednessday',
            'from':'',
            'to':''
        },
        {
            'days':'Thursday',
            'from':'',
            'to':''
        },
        {
            'days':'Friday',
            'from':'',
            'to':''
        },
        {
            'days':'Saturday',
            'from':'',
            'to':''
        },
        {
            'days':'Sunday',
            'from':'',
            'to':''
        }];
		              
	$scope.changeOperationTime = function(operatingTime) {
        $scope.myTime = operatingTime;    
    };
		              			 
		             //Submeter details
	$scope.submeterDetails = [{
        'submeterno':'0001236758',
        'submeteruser':'Alex Ques'
        
    }];
		              			 
$scope.addNewSubmeter = function(submeterDetails){
		 
		 $scope.submeterDetails.push({ 
		  'submeterno': "", 
		 'submeteruser': ""				 
		 });
		 $scope.SD = {};
		 };
		              				 
	$scope.removeSubmeter = function(){
		  var newDataList=[];
		  $scope.selectedAll = false;
		  angular.forEach($scope.submeterDetails, function(selected){
		      if(!selected.selected){
		         newDataList.push(selected);
		       }
		    }); 
		        $scope.submeterDetails = newDataList;
		   };
		              			   
  	    $scope.checkAllSubmeter = function () {
  	    	if (!$scope.selectedAll) {
  			   $scope.selectedAll = true;
  			  } else {
  			    $scope.selectedAll = false;
  			     }
  			   angular.forEach($scope.submeterDetails, function(submeterDetail) {
  				   submeterDetail.selected = $scope.selectedAll;
  			    });
  			 };



		
		              			 
	    $scope.saveSiteForm=function(formObj){
	    	
	    	//alert(formObj.$error);
	    	/*alert(JSON.stringify($scope.siteData));    	
	    	
	    	alert(JSON.stringify($scope.licenseDetails));
	    	
	    	alert(JSON.stringify($scope.salesoperationDetails));*/
	    	
	    	var finalSiteObj = {
	    			siteData: $scope.siteData,
	    			siteLicense:$scope.licenseDetails,
	    			siteOperation:$scope.salesoperationDetails,
	    			siteDelivery:$scope.deliveryoperationDetails,
	    			siteSubmeter:$scope.submeterDetails
	    	
	    	};
	    	console.log(finalSiteObj);
	    	//alert($scope.siteData.licenseDetails.push(angular.copy(JSON.stringify($scope.licenseDetails))));
   		/*var customer=this;
   		var newCustomer = angular.copy($scope.customer);
   		customer.registerUser=function(){
	    		userService.registerUser(newCustomer)
	    		.then(function(data) {
	    			 console.log(data);
	            },
	            function(data) {
	            	console.log(data)
	            }); 	
	    	}
   		customer.registerUser();*/
     };
			 
}]);



/*


chrisApp.controller('newuserregisterController', 
		 ['$rootScope', '$scope', '$filter','userService',
function  ($rootScope, $scope , $filter,userService) {
		
			 $scope.newcustomer={};
			 $scope.sites = [
			                 {name:'Malaysite'},
			                 {name:'Shibusite'},
			                 {name:'Ranjansite'},
			                 {name:'Swadhinsite'},
			                 {name:'Chrissite'},
			                 {name:'Rajeshsite'},
			                 {name:'Rameshsite'},
			                 {name:'Shilpasite'}
			                 
			                 
			               ];
			 
			 
	    $scope.saveCustomer=function(){
	    	//alert($scope.selection);
	    	
  		
  		var newregisterCustomer = angular.copy($scope.newcustomer);
  		//alert($scope.newregisterCustomer);
  		newcustomer.registerUser=function(){
	    		userService.registerUser(newregisterCustomer)
	    		.then(function(data) {
	    			 console.log(data);
	            },
	            function(data) {
	            	console.log(data)
	            }); 	
	    	}
  		newcustomer.registerUser();
    };
			 
}]);


chrisApp.directive('multiSelect', function() {

	  function link(scope, element) {
	    var options = {
	      enableClickableOptGroups: true,
	      onChange: function() {
	        element.change();
	      }
	    };
	    element.multiselect(options);
	  }

	  return {
	    restrict: 'A',
	    link: link
	  };
	});*/

