

chrisApp.factory('serviceProviderService',  ['$http', '$q',function ($http, $q) {
 		var ServiceProviderService = {
 			createServiceProvider:createServiceProvider,
 			saveServiceProvider:saveServiceProvider,
 			serviceProviderFinalObject:{},
 			getAllServiceProviders:getAllServiceProviders,
 			getServiceProviderByCustomer:getServiceProviderByCustomer,
 			validateSPLogin:validateSPLogin,
 			getLoggedInSPUser:getLoggedInSPUser,
 			getSelectedServiceProvider:getSelectedServiceProvider,
 			resetServiceProviderPassword:resetServiceProviderPassword,
 			getSPAgents:getSPAgents,
 			grantOrRevokeAcess:grantOrRevokeAcess,
 			saveRSPExternalCustomer:saveRSPExternalCustomer,
 			retrieveExternalCustomerRSP:retrieveExternalCustomerRSP
        };
 		return ServiceProviderService;
 		
 		function retrieveExternalCustomerRSP(){
			  var def = $q.defer();
		        $http.get(hostLocation+"/serviceprovidercompany/ext/customer/list")
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
 	    function saveRSPExternalCustomer(extCustomer) {
 	        var def = $q.defer();
 	        $http.post(hostLocation+"/serviceprovidercompany/save/ext/customer",extCustomer)
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
 	    
 		
 		function grantOrRevokeAcess(spId, accessValue){
			  var def = $q.defer();
		        $http.get(hostLocation+"/serviceprovider/rsp/access/customer/"+spId+"/"+accessValue)
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
 		
 		function getSPAgents(customerCode){
 			  var def = $q.defer();
 		        $http.get(hostLocation+"/serviceprovider/rsp/active/user/list/"+customerCode)
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
 		
 		function resetServiceProviderPassword(serviceProvider){
		     var def = $q.defer();
	        $http.get(hostLocation+"/serviceprovider/resetpassword/"+serviceProvider.serviceProviderId)
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
 		
 		function getSelectedServiceProvider(spId, spAccess){
		     var def = $q.defer();
	        $http.get(hostLocation+"/serviceprovider/info/"+spId+"/"+spAccess)
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
 	    function getAllServiceProviders(spType) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/serviceprovider/list/"+spType)
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
 			findRSPRegions:findRSPRegions,
 			getCountryByRegion:getCountryByRegion
        };
 		return RegionService;
 	// implementation
 	    function findAllRegions() {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/serviceprovider/regions")
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
 	    
 	   function findRSPRegions() {
	        var def = $q.defer();
	        $http.get(hostLocation+"/serviceprovidercompany/regions")
	            .success(function(data) {
	                def.resolve(data);
	            })
	            .error(function(data) {
	                def.reject(data);
	            });
	        return def.promise;
	    }
 	   
 	  function getCountryByRegion(region) {
	        var def = $q.defer();
	        $http.get(hostLocation+"/serviceprovidercompany/country/region/"+region.regionId)
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
