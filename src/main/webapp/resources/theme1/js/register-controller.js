chrisApp.controller('registerController', 
		 ['$rootScope', '$scope', '$filter','userService',
function  ($rootScope, $scope , $filter,userService,translatorService) {
		$scope.pageText={};
		$scope.customer={};	 
		$scope.responseMessage={};
		angular.element(document).ready(function(){
			//translatePage('en');
		});
		
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
	    				// $scope.customer={};
	    				 $('#successMessageDiv').show();
	    				 $('#successMessageDiv').alert();
	    				 $('#infoMessageDiv').hide();
	    				 $('#resetRegisterFormBtn').click();
	    			 }else{
	    				 $scope.responseMessage={
    						 info:data.message,
    						 color:"red"
	    			 	 };
	    			 }
	            },
	            function(data) {
	            	console.log(data)
	            	 $('#successMessageDiv').hide();
	            	 $('#infoMessageDiv').alert();
	            	 $('#infoMessageDiv').show();
	            	 $scope.responseMessage={
						 info:data.message,
						 color:"red"
   			 	    };
	            }); 	
	    	}
    		customer.registerUser();
      };
      
      
      $scope.closeMessageWindow=function(){
    	  $('#successMessageDiv').hide();
    	  $('#infoMessageDiv').hide();
      }
      $scope.translatePageText=function(language){
		  translatorService.getLanguageTranslator(language)
		  .then(function(data){
			  console.log(data);
				$scope.pageText.demoRequest=data.register.demoRequest;
		  },function(data){
			  console.log(data);
		  });
	  }
			 
}]);

chrisApp.controller('passwordController', 
		 ['$rootScope', '$scope', '$filter','userService','passwordService',
function  ($rootScope, $scope , $filter,userService,passwordService) {

	  $scope.customer={};	 
   
	  $scope.verifyCustomer=function(passwordForm){
		  $scope.sendPasswordResetLink($scope.customer);
	  }
     
	  $scope.sendPasswordResetLink=function(customer){
		  passwordService.resetPassword(customer.email, customer.usertype)
		  .then(function(data){
			  console.log(data)
			  if(data.statusCode == 200){
				  $('#messageWindow').show();
				  $('#successMessageDiv').show();
				  $('#infoMessageDiv').hide();
				  $('#successMessageDiv').alert();
				  $scope.successMsg=data.message;
			  }
		  },function(data){
			  console.log(data)
			  $('#messageWindow').show();
			  $('#infoMessageDiv').show();
			  $('#successMessageDiv').hide();
			  $('#infoMessageDiv').alert();
		  });
	  }
	  
	  $scope.resetCustomerNewPassword=function(passwordForm){
   	  
	  }
     
	  $scope.closeMessageWindow=function(){
	   	  $('#successMessageDiv').hide();
	   	  $('#infoMessageDiv').hide();
	   }
	 
			 
}]);

function translatePage(language){
	var scope = angular.element("#registerWindow").scope();
	scope.translatePageText(language);
}