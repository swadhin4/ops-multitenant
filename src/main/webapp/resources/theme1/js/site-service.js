

chrisApp.factory('siteCreationService',  ['$http', '$q',function ($http, $q) {
 		var SiteCreationService = {
 			siteFinalObject:{},
 			createSiteObject:createSiteObject,
 			saveSiteObject:saveSiteObject
 			
        };
 		return SiteCreationService;
		
 		function createSiteObject(finalObject){
 				var districtId=null;
 				var areaId=null;
 				var clusterId=null;
 			   if(finalObject.siteData.district!=undefined){
 				  districtId=finalObject.siteData.district.districtId
 			   }
 			   if(finalObject.siteData.area!=undefined){
 				  areaId=finalObject.siteData.area.areaId
 			   }
 			   if(finalObject.siteData.cluster!=undefined){
 				  clusterId=finalObject.siteData.cluster.clusterID
 			   }
 				siteObject={
 					siteId:finalObject.siteData.siteId || null,
 					siteName:finalObject.siteData.siteName || null,
 					owner:finalObject.siteData.owner || null,
 					companyId:finalObject.siteData.companId || null,
 					electricityId:finalObject.siteData.electricityId || null,
 					//country:finalObject.siteData.country || null,
 					//operator : finalObject.siteData.operator || null,
 					districtId:districtId,
 					areaId:areaId,
 					clusterId:clusterId,
 					siteNumber1:parseInt(finalObject.siteData.siteNumber1) || null,
 					siteNumber2:parseInt(finalObject.siteData.siteNumber2) || null, 
 					fileInput:finalObject.uploadedFile || null,
 					fileExtension:finalObject.extension || null,
 					salesAreaSize:finalObject.siteData.salesAreaSize || null,
 					
 				//-------------- X -----------------------//
 					
 					contactName:finalObject.siteData.contactName || null,
 					email:finalObject.siteData.email || null,
 					longitude:finalObject.siteData.longitude || null,
 					latitude:finalObject.siteData.latitude || null,
 					primaryContact:finalObject.siteData.primaryContact || null,
 					secondaryContact:finalObject.siteData.secondaryContact || null,
 					siteAddress1:finalObject.siteData.siteAddress1 || null,
 					siteAddress2:finalObject.siteData.siteAddress2 || null,
 					siteAddress3:finalObject.siteData.siteAddress3 || null,
 					siteAddress4:finalObject.siteData.siteAddress4 || null,
 					zipCode:finalObject.siteData.zipCode || null,
 					
 				//-------------- X -----------------------//	
 					
 					siteLicense:finalObject.siteLicense || null,
 					licenseAttachments:finalObject.licenseAttachments,
 					siteOperation:finalObject.siteOperation || null,
 					siteDelivery:finalObject.siteDelivery || null,
 					siteSubmeter:finalObject.siteSubmeter || null,
 				//-----------X---------------------//
 					brandId:finalObject.siteData.brandId || null,
 					brandName:finalObject.siteData.brandName
 					
 					
 				}
 				SiteCreationService.siteFinalObject=angular.copy(siteObject);
 				return SiteCreationService.siteFinalObject;
 		}
 		
 	// implementation
 	    function saveSiteObject(siteObject) {
 	        var def = $q.defer();
 	        $http.post(hostLocation+"/site/create",siteObject)
 	            .success(function(data) {
 	            	console.log(data)
 	                def.resolve(data);
 	            })
 	            .error(function(data) {
 	            	console.log(data)
 	                def.reject(data);
 	            });
 	        return def.promise;
 	    }
		
}]);

