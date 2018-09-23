

chrisApp.factory('serviceProviderService',  ['$http', '$q',function ($http, $q) {
 		var ServiceProviderService = {
 			createServiceProvider:createServiceProvider,
 			saveServiceProvider:saveServiceProvider,
 			serviceProviderFinalObject:{},
 			getAllServiceProviders:getAllServiceProviders,
 			getServiceProviderByCustomer:getServiceProviderByCustomer,
 			validateSPLogin:validateSPLogin,
 			getLoggedInSPUser:getLoggedInSPUser
        };
 		return ServiceProviderService;
 		
 		function getLoggedInSPUser(){
		     var def = $q.defer();
 	        $http.get(hostLocation+"/sp/session/user")
 	            .success(function(data) {
 	            	//console.log(data)
 	                def.resolve(data);
 	            })
 	            .error(function(data) {
 	            	console.log(data)
 	                def.reject(data);
 	            });
 	        return def.promise;
		}
 		
 		function validateSPLogin(sp){
 		     var def = $q.defer();
 		     var serviceProvider={
 		    		 spId:null,
 		    		 email:sp.email,
 		    		 accessCode:sp.accesscode,
 		    		 role:null,
 		    		 spName:null,
 		    		 isValidated:false
 		     }
  	        $http.post(hostLocation+"/sp/login/validator",serviceProvider)
  	            .success(function(data) {
  	            	//console.log(data)
  	                def.resolve(data);
  	            })
  	            .error(function(data) {
  	            	console.log(data)
  	                def.reject(data);
  	            });
  	        return def.promise;
 		}
		
 		function createServiceProvider(finalObject){
 				serviceProviderObject={
 					serviceProviderId:null,
 					name:finalObject.serviceProvider.name || null,
 					code:finalObject.serviceProvider.owner || null,
 					email:finalObject.serviceProvider.email || null,
 					country:finalObject.serviceProvider.country || null,
 					region:finalObject.serviceProvider.region || null,
 					additionalDetails:finalObjectserviceProvider.additionalDetails || null,
 					slaListVOList:[],
 					escalationLevelList:[]
 					
 				}
 				ServiceProviderService.serviceProviderFinalObject=angular.copy(serviceProviderObject);
 				return ServiceProviderService.serviceProviderFinalObject;
 		}
 		
 	// implementation
 	    function saveServiceProvider(serviceProvider) {
 	        var def = $q.defer();
 	        $http.post(hostLocation+"/serviceprovider/create",serviceProvider)
 	            .success(function(data) {
 	            	//console.log(data)
 	                def.resolve(data);
 	            })
 	            .error(function(data) {
 	            	console.log(data)
 	                def.reject(data);
 	            });
 	        return def.promise;
 	    }
 	    
 	// implementation
 	    function getAllServiceProviders() {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/serviceprovider/list")
 	            .success(function(data) {
 	            	//console.log(data)
 	                def.resolve(data);
 	            })
 	            .error(function(data) {
 	            	console.log(data)
 	                def.reject(data);
 	            });
 	        return def.promise;
 	    }
 	    
 	// implementation
 	    function getServiceProviderByCustomer(customer) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/serviceprovider/list/by/"+customer.companyId)
 	            .success(function(data) {
 	            	//console.log(data)
 	                def.resolve(data);
 	            })
 	            .error(function(data) {
 	            	console.log(data)
 	                def.reject(data);
 	            });
 	        return def.promise;
 	    }
		
}]);



chrisApp.factory('regionService',  ['$http', '$q',function ($http, $q) {
 		var RegionService = {
 			findAllRegions:findAllRegions,
        };
 		return RegionService;
		
 		
 	// implementation
 	    function findAllRegions() {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/test/api/regions")
 	            .success(function(data) {
 	            	//console.log(data)
 	                def.resolve(data);
 	            })
 	            .error(function(data) {
 	            	console.log(data)
 	                def.reject(data);
 	            });
 	        return def.promise;
 	    }
		
}]);
