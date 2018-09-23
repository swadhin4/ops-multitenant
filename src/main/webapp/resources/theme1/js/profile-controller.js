

chrisApp.controller('profileController',  ['$rootScope', '$scope', '$filter', '$location','userService','authService',
                                        'companyService','registrationService','roleService','siteService',
                                        function ($rootScope, $scope , $filter,$location,userService,authService,
                                        companyService,registrationService,roleService,siteService) {
	$scope.selectedRow =0;
	$scope.user ={};
	$scope.appUsers=[];	
	$scope.sessionUser={};
	$scope.loggedInUserDetail = [];
	$scope.selectedUser ={};
	
	$scope.onlyNumbers = /^\d+$/;
	$scope.filterValue = function($event){
	    if(isNaN(String.fromCharCode($event.keyCode))){
	        $event.preventDefault();
	    }
	};
	
	$scope.passwordDetail = {};
		angular.element(document).ready(function(){		
			 
			 $scope.findLoggedInUserAccess();		
			 
		});
		
		
		
		
		
				
		//---------------------X----------------------
				    
	    $scope.findLoggedInUserAccess=function(){
	    	$scope.getLoggedInUserAccess();
	    };	    
	   
	    $scope.getUserDetail=function(user){
	    	//console.log(user)
	    	$scope.selectedUser = angular.copy(user);
	    };
		    
   //--------- Services Call from User Controller -------------------	
		    
		    		    
		    $scope.getLoggedInUserAccess =function(){		    	
			   
				authService.loggedinUserAccess()
	    		.then(function(data) {
	    			console.log("loggedin user");	    			
	    			$scope.loggedInUserEmail = data.loggedInUserMail;
	    			    			
	    			if(data.statusCode == 200){
	    				$scope.sessionUser=data;
	    				$scope.getLoggedInUser($scope.sessionUser);
	    			}	    			
	            },
	            function(data) {
	                console.log('Unauthorized Access.')
	            }); 	
		    	
		    }
		    
		    $scope.getLoggedInUser=function(loginUser){
				userService.getLoggedInUser(loginUser)
	    		.then(function(data) {
	    			
	    			if(data.statusCode == 200){
	    				
	    				$scope.sessionUser=angular.copy(data.object);
	    				console.log('User object logged in')
	    				
	    				$scope.loggedInUserDetail = angular.copy($scope.sessionUser);
	    			}
	            },
	            function(data) {
	                console.log('No User available')
	            });
			}
		    
		    $scope.updateProfile=function($securityForm){
		    	if ($scope.profileForm.$valid) {
		    		
		    	}
		    	 $scope.updateProfileDetails($scope.loggedInUserDetail);
		    	
		    };
		    
		    $scope.updateProfileDetails=function(updatedUserDetails){
		    	userService.updateProfile(updatedUserDetails)
		    	.then(function(data){
		    		$('#profile-success-alert').show();
					$('#profile-success-alert').alert();
					$scope.successMessage = data.message;
					$('#profile-error-alert').hide();
		    	},function(data){
		    		 	$('#profile-error-alert').show();
						$('#profile-error-alert').alert();
						$('#profile-success-alert').hide();
						$scope.errorMessage = data.message;
		    	});
		    }
		    
		    $scope.updatePassword=function($securityForm){
		    	if ($scope.securityForm.$valid) {
	    	        //do stuff
		    		$scope.changePassword($scope.passwordDetail);
	    	    } 
		    };
		    
		    $scope.changePassword=function(userPasswords){
		    	userService.changePassword($scope.passwordDetail)
	    		.then(function(data) {
	    			
	    			if(data.statusCode==200){
						$('#profile-success-alert').show();
						$('#profile-success-alert').alert();
						$scope.successMessage = data.message;
						$('#profile-error-alert').hide();
						$('#passwordConfirmModal').modal('show');
					}else{
						$('#profile-error-alert').show();
						$('#profile-error-alert').alert();
						$('#profile-success-alert').hide();
						$scope.errorMessage = data.message;
					}
	            },
	            function(data) {
	                console.log('No User available')
	                $('#profile-error-alert').show();
					$('#profile-error-alert').alert();
					$('#profile-success-alert').hide();
					$scope.errorMessage = data.message;
	            });
		    }
		    
		    $scope.closeMessageWindow=function(){
		    	$('#profile-success-alert').hide();
		    	$('#profile-error-alert').hide();
				$scope.successMessage="";
				$scope.errorMessage="";
			}
		    
		    
}]);

chrisApp.directive('validPasswordC', function () {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            
              ctrl.$setValidity('noMatch', true);

                attrs.$observe('validPasswordC', function (newVal) {
                    if (newVal === 'true') {
                        ctrl.$setValidity('noMatch', true);
                    } else {
                        ctrl.$setValidity('noMatch', false);
                    }
                });
        }
    }
});
