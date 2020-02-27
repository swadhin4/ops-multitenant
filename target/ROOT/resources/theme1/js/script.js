var app=angular.module('single-page-app',['ngRoute']);

app.config(function($routeProvider){

      $routeProvider
          .when('/',{
                templateUrl: 'template/home.html'
          })
          .when('/register',{
                templateUrl: 'template/register.html'
          })
          .when('/about',{
                templateUrl: 'template/about.html'
          })
          .when('/login',{
              templateUrl: 'template/login.html'
          })
	      .when('/managesite',{
	          templateUrl: 'template/manage-site.html'
	      });



});

	app.controller('userController',function($scope){
		
	});

	app.controller('homeController',function($scope){
	
	
	});
	
	app.controller('registerController',function($scope){
		$scope.user={};
		$scope.saveCustomer=function(registerForm){
			console.log($scope.user);
		}
		
	});
	
	app.controller('loginController',function($scope){
		
		
	});
