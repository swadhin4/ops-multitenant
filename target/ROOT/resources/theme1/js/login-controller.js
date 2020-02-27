
chrisApp.controller('loginController', 
		 ['$rootScope', '$scope', '$filter', '$location','userService','serviceProviderService',
	function ($rootScope, $scope , $filter,$location,userService,serviceProviderService) {
		$scope.user={};	 
		$rootScope.tenants={};	 
		angular.element(document).ready(function(){
			//$scope.getTenants();
		});
		
		$scope.getTenants=function(){
			userService.getTanants()
			.then(function(data){
				console.log(data);
				$.jStorage.set('tenants', data.object);
				$rootScope.tenants = data.object;
			},function(data){
				console.log(data);
			});
		}
	/*	 var loggedInuser =null;
	    $scope.validateUser=function(){
	    		var appuser=this;
	    	    appuser.validatedUser={};
	    	    $scope.loader1=true;
		    	appuser.validateUser=function(){
		    		userService.validateUser( $scope.user.email, $scope.user.password)
		    		.then(function(data) {
		    			 $scope.savedUser = angular.copy(data);
		    			 $scope.savedUser.password="";
		    			 
		    			 $scope.user ={};
		    			 loggedInuser = $.jStorage.set('loggedInUser', $scope.savedUser);
		    			 $location.path('/userhome');
		            },
		            function(data) {
		               
		            }); 	
		    	}
		    appuser.validateUser();
	    };
	    
	    $scope.validateExternalSP=function(){
	    	
	    	$scope.validateSPLogin($scope.sp);
	    }
	    
	    $scope.validateSPLogin=function(sp){
	    	serviceProvider.validateLogin(sp)
	    	.then(function(data){
	    		
	    	},function(data){
	    		
	    	});
	    }*/
	    
}]);

