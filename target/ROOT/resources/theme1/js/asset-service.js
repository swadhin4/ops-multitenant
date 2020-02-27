
chrisApp.factory('assetCreationService',  ['$http', '$q',function ($http, $q) {
 		var AssetCreationService = {
 			assetFinalObject:{},
 			createAssetObject:createAssetObject,
 			saveAssetObject:saveAssetObject
        };
 		return AssetCreationService;
		
 		function createAssetObject(finalObject){
 				assetObject={
 					
 				}
 				AssetCreationService.assetFinalObject=angular.copy(assetObject);
 				return SiteCreationService.assetFinalObject;
 		}
 		
 	
		
}]);

chrisApp.factory('assetService',  ['$http', '$q',function ($http, $q) {
		var AssetService = {
				retrieveAssetCategories:retrieveAssetCategories,
				getAssetLocations:getAssetLocations,
				saveAssetObject:saveAssetObject,
				findAllAssets:findAllAssets,
				getAssetBySite:getAssetBySite,
				deleteFileAttached:deleteFileAttached,
				getAssetInfo:getAssetInfo,
				deleteAsset:deleteAsset,
				retrieveRepairTypes:retrieveRepairTypes,
				retrieveSubRepairTypes:retrieveSubRepairTypes,
				saveAssetTask:saveAssetTask
		};
		return AssetService;
		
		
		// implementation
 	    function saveAssetTask(assetTaskObject) {
 	        var def = $q.defer();
 	        $http.post(hostLocation+"/asset/task/save",assetTaskObject)
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
		
 	    function retrieveRepairTypes(assetCategoryId) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/asset/category/repairtype/"+assetCategoryId)
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
 	    
 	    function retrieveSubRepairTypes(repairTypeId) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/asset/category/subrepairtype/"+repairTypeId)
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
 	    function deleteAsset(asset) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/asset/delete/"+asset.assetId)
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
 	    function getAssetInfo(assetId) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/asset/info/"+assetId)
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
 	    function deleteFileAttached(feature,assetId,type) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/file/attachement/delete/"+feature+"/"+assetId+"/"+type)
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
 	    function findAllAssets(viewType) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/asset/grouplist/"+viewType)
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
 	    function getAssetBySite(siteId, custType) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/asset/list/"+siteId+"/"+custType)
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
 	    function saveAssetObject(assetObject) {
 	        var def = $q.defer();
 	        $http.post(hostLocation+"/asset/create",assetObject)
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
	    function retrieveAssetCategories() {
	        var def = $q.defer();
	        $http.get(hostLocation+"/asset/categories")
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
	    function getAssetLocations() {
	        var def = $q.defer();
	        $http.get(hostLocation+"/asset/locations")
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

