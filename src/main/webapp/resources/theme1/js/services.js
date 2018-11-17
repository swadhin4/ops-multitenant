
chrisApp.factory("userService", ['$http', '$q',function ($http, $q) {
	//34-209-65-191
	
	 	var UserService = {
	 			user:{},
	            userList: [],
	            retrieveAllUsers:retrieveAllUsers,
	            validateUser:validateUser,
	            getLoggedInUser:getLoggedInUser,
	            registerUser:registerUser,
	            getUserSiteAccess:getUserSiteAccess,
	            getUsersBySiteAccess:getUsersBySiteAccess,
	            changePassword:changePassword,
	            enableOrDisableUser:enableOrDisableUser,
	            updateProfile:updateProfile,
	            getUserRoleStatusMap:getUserRoleStatusMap,
	            retrieveAllSPRoles:retrieveAllSPRoles,
	            retrieveAllSPCustomers:retrieveAllSPCustomers,
	            retrieveCustomersBySelectedSP:retrieveCustomersBySelectedSP,
	            updateSPUserCustomers:updateSPUserCustomers,
	            getSPCustomerList:getSPCustomerList,
	            getSPCustomerTicketList: getSPCustomerTicketList
	        };
	 	
	 	return UserService;
	 	 // implementation
	 	
	 	//
	 	function getSPCustomerList() {
            var def = $q.defer();
            $http.get(hostLocation+"/serviceprovidercompany/mappedcustomers")
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
	 	
	 	
	 	function getSPCustomerTicketList(custCode,custDBName) {
            var def = $q.defer();
            $http.get(hostLocation+"/serviceprovidercompany/customers/tickets/"+custCode+"/"+custDBName)
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
	 	
	 	
	 	//
        function updateSPUserCustomers(selectedSPUserId, customerVOList) {
            var def = $q.defer();
            $http.post(hostLocation+"/serviceprovidercompany/updatecustomers/"+selectedSPUserId, customerVOList )
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
	    function getUserRoleStatusMap() {
	        var def = $q.defer();
	        	url=hostLocation+"/role/getstatusroleids";
	        $http.get(url)
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
        function updateProfile(userDetails) {
            var def = $q.defer();
            $http.post(hostLocation+"/user/profile/update", userDetails )
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
        function registerUser(customer) {
            var def = $q.defer();
            $http.post(hostLocation+"/api/register/customer", customer )
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
        function validateUser(username,password) {
            var def = $q.defer();
            var user={
            		emailId:username,
            		password:password
            }
            $http.post(hostLocation+"/api/authenticate/user", user )
                .success(function(data) {
                	//console.log(data)
                	UserService.user = data;
                    def.resolve(data);
                })
                .error(function(data) {
                	console.log(data)
                    def.reject(data);
                });
            return def.promise;
        }
        
     // implementation
             
        function getLoggedInUser(loggedInUser) {
        	 // implementation
                var def = $q.defer();
               
                $http.get(hostLocation+"/user/logged")
                    .success(function(data) {
                    	//console.log(data)
                        def.resolve(data);
                    })
                    .error(function() {
                        def.reject("Failed to get loggedin user");
                    });
                return def.promise;
        }
        
        // implementation
        function retrieveAllUsers() {
            var def = $q.defer();
            $http.get(hostLocation+"/user/list")
                .success(function(data) {
                	//console.log(data)
                	UserService.userList = data;
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get albums");
                });
            return def.promise;
        }
        
        // implementation
        function retrieveAllSPRoles() {
            var def = $q.defer();
            $http.get(hostLocation+"/serviceprovidercompany/getusers")
                .success(function(data) {
                	//console.log(data)
                	UserService.userList = data;
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get albums");
                });
            return def.promise;
        }
     // implementation
        function retrieveAllSPCustomers() {
            var def = $q.defer();
            $http.get(hostLocation+"/serviceprovidercompany/getcustomers")
                .success(function(data) {
                	//console.log(data)
                	UserService.userList = data;
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get albums");
                });
            return def.promise;
        }
        
        
     // implementation
        function retrieveCustomersBySelectedSP(spid) {
            var def = $q.defer();
            $http.get(hostLocation+"/serviceprovidercompany/selectedcustomer/"+spid)
                .success(function(data) {
                	//console.log(data)
                	UserService.userList = data;
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get albums");
                });
            return def.promise;
        }
        
        function getUserSiteAccess(){
        	 var def = $q.defer();
        	  $http.get(hostLocation+"/site/list")
           //  $http.get(hostLocation+"/user/site/access")
                 .success(function(data) {
                 	//console.log(data)
                     def.resolve(data);
                 })
                 .error(function() {
                     def.reject("Failed to get user site access list");
                 });
             return def.promise;
        }
        
        function getUsersBySiteAccess(siteId){
        	var def = $q.defer();
             $http.get(hostLocation+"/user/site/access/list/"+siteId)
                 .success(function(data) {
                 	//console.log(data)
                     def.resolve(data);
                 })
                 .error(function() {
                     def.reject("Failed to get user site access list");
                 });
             return def.promise;
        }
        
        
   	 // implementation
        function changePassword(passwordDetails) {
            var def = $q.defer();
            $http.post(hostLocation+"/user/passwordchange", passwordDetails )
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
           function enableOrDisableUser(userId,userStatus) {
        	   var UserData={
        			   userId:userId,
        			   isEnabled:userStatus
        	   }
               var def = $q.defer();
               $http.post(hostLocation+"/user/enableordisable/", UserData)
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

chrisApp.factory("siteService", ['$http', '$q',function ($http, $q) {
		var SiteService = {
 			site:{},
            siteList: [],
            retrieveAllSites:retrieveAllSites,
           retrieveSiteDetails:retrieveSiteDetails,
           // saveSite:saveSite,
            assignSiteAccess:assignSiteAccess,
            removeSiteAccess:removeSiteAccess,
            siteFileDownload:siteFileDownload,
            deleteFileAttached:deleteFileAttached
        };
		
	 	return SiteService;
	 // implementation
 	    function deleteFileAttached(feature,siteId) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/file/attachement/delete/"+feature+"/"+siteId+"/"+null)
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
	 	
	 	function siteFileDownload(keyname) {
            var def = $q.defer();
            $http.post(hostLocation+"/site/selected/file?keyname="+keyname)
                .success(function(data) {
                     //console.log(data)
                     SiteService.site=data;
                    def.resolve(data);
                })
                .error(function(data) {
                     console.log(data)
                    def.reject(data);
                });
            return def.promise;
        }
	 	
	    function retrieveSiteDetails(siteId) {
            var def = $q.defer();
            $http.get(hostLocation+"/site/v1/selected/"+siteId)
                .success(function(data) {
                     //console.log(data)
                     SiteService.site=data;
                    def.resolve(data);
                })
                .error(function(data) {
                     console.log(data)
                    def.reject(data);
                });
            return def.promise;
        }
 
	 	
	    // implementation
        function retrieveAllSites() {
            var def = $q.defer();
            $http.get(hostLocation+"/site/list")
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
        function assignSiteAccess(userId, siteId) {
            var def = $q.defer();
            $http.get(hostLocation+"/user/assign/site/"+userId+"/"+siteId)
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
        function removeSiteAccess(accessId) {
            var def = $q.defer();
            $http.get(hostLocation+"/user/revoke/site/"+accessId)
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
        
        

	  /*  // implementation
        function saveSite(siteData) {
            var def = $q.defer();
            $http.post("http://34.209.65.191:8282/test/api/site/save",siteData)
                .success(function(data) {
                	console.log(data)
                	SiteService.site =data;
                    def.resolve(data);
                })
                .error(function(data) {
                	console.log(data)
                    def.reject(data);
                });
            return def.promise;
        }*/
 }]);

chrisApp.factory("companyService", ['$http', '$q',function ($http, $q) {
	var CompanyService = {
			company:{},
        companyList: [],
        retrieveAllROCompanies:retrieveAllROCompanies,
        retrieveAllSPCompanies:retrieveAllSPCompanies,
        
    };
	
 	return CompanyService;
 	
    // implementation
    function retrieveAllROCompanies() {
        var def = $q.defer();
        $http.get(hostLocation+"/test/api/companies")
            .success(function(data) {
            	//console.log(data)
            	$.each(data,function(key,val){
            		CompanyService.companyList.push(val);
            	});
            	
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
    // implementation
    function retrieveAllSPCompanies() {
        var def = $q.defer();
        $http.get("http://34.209.65.191:8282/test/api/asset/serviceprovider")
            .success(function(data) {
            	//console.log(data)
            	$.each(data,function(key,val){
            		CompanyService.companyList.push(val);
            	});
            	
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
}]);

/*chrisApp.factory("assetService", ['$http', '$q',function ($http, $q) {
	var AssetService = {
		asset:{},
        assetList: [],
        retrieveAllAsset:retrieveAllAsset,
        serviceProvider:{},
		getServiceProviderDetail:getServiceProviderDetail
    };
 	return AssetService;
 	
    // implementation
    function retrieveAllAsset(siteSelected) {
    	var siteId = siteSelected.siteId;
        var def = $q.defer();
        $http.get("http://34.209.65.191:8282/test/api/asset/site/"+siteId)
            .success(function(data) {
            	console.log(data)
            	$.each(data,function(key,val){
            		var asset={
            				assetId:val.assetId,
            				assetName:val.assetName
            		}
            		AssetService.assetList.push(asset);
            	});
            	
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
    function getServiceProviderDetail(companyId) {
    	var serviceProviderId = companyId;
        var def = $q.defer();
        $http.get("http://34.209.65.191:8282/test/api/ticket/assignedto/"+serviceProviderId)
            .success(function(data) {
            	console.log(data)
            	AssetService.serviceProvider=angular.copy(data);
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
}]);*/


chrisApp.factory("countryService", ['$http', '$q',function ($http, $q) {
	var CountryService = {
			country:{},
        countrylist: [],
        retrieveAllCountries:retrieveAllCountries,
        retrieveUserCountry:retrieveUserCountry,
        getCountryList:getCountryList,
        getCountryByRegion:getCountryByRegion,
        getCountryImage:getCountryImage
    };
	
 	return CountryService;
 	
    function getCountryImage(country) {
        var def = $q.defer();
        $http.get("https://restcountries.eu/rest/v2/name/"+country+"?fullText=true")
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
    function retrieveAllCountries(user) {
        var def = $q.defer();
        $http.get(hostLocation+"/test/api/countries")
            .success(function(data) {
            	//console.log(data)
            	$.each(data,function(key,val){
            		CountryService.countrylist.push(val);
            	});
            	
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
    // implementation
    function getCountryList(user) {
    	var countryList = 	[];
    	 var def = $q.defer();
    	 var userCountry = {};
    	$http.get(hostLocation+"/test/api/countries")
        .success(function(data) {
        	//console.log(data)
        	$.each(data,function(key,val){
        		countryList.push(val);
        	});
        	
        	 $.each(countryList,function(key,val){
        		 if(val.countryId == user.company.countryId){
        			 userCountry = angular.copy(val);
        			 return false;
        		 }
        	 });
        	
            def.resolve(data);
        })
        .error(function(data) {
        	console.log(data)
            def.reject(data);
        });
    	
    	
    	 return userCountry;
    }
    
 // implementation
	    function getCountryByRegion(region) {
	        var def = $q.defer();
	        $http.get(hostLocation+"/serviceprovider/country/"+region.regionId)
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
    function retrieveUserCountry() {
    	
    }
    
    
}]);

chrisApp.factory("areaService", ['$http', '$q',function ($http, $q) {
	var AreaService = {
		area:{},
        areaList: [],
        retrieveAllAreas:retrieveAllAreas,
    };
 	return AreaService;
 	
    // implementation
    function retrieveAllAreas(district) {
        var def = $q.defer();
        $http.get(hostLocation+"/site/v1/area/"+district.districtId)
            .success(function(data) {
            	//console.log(data)
            	$.each(data,function(key,val){
            		AreaService.areaList.push(val);
            	});
            	
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
}]);

chrisApp.factory("districtService", ['$http', '$q',function ($http, $q) {
	var DistrictService = {
		district:{},
        districtList: [],
        retrieveAllDistricts:retrieveAllDistricts,
        retrieveDistrictByCountry:retrieveDistrictByCountry
    };
 	return DistrictService;
 	
    // implementation
    function retrieveAllDistricts() {
        var def = $q.defer();
        $http.get(hostLocation+"/test/api/districts")
            .success(function(data) {
            	//console.log(data)
            	$.each(data,function(key,val){
            		DistrictService.districtList.push(val);
            	});
            	
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
 // implementation
    function retrieveDistrictByCountry(loginUser) {
        var def = $q.defer();
        $http.get(hostLocation+"/site/district/api/country/1")
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


chrisApp.factory("clusterService", ['$http', '$q',function ($http, $q) {
	var ClusterService = {
		cluster:{},
       clusterList: [],
        retrieveAllClusters:retrieveAllClusters,
    };
 	return ClusterService;
 	
    // implementation
    function retrieveAllClusters(districtId, areaId) {
        var def = $q.defer();
        $http.get(hostLocation+"/site/v1/cluster/"+districtId+"/"+areaId)
            .success(function(data) {
            	//console.log(data)
            	$.each(data,function(key,val){
            		ClusterService.clusterList.push(val);
            	});
            	
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
}]);

chrisApp.factory("ticketCategoryService", ['$http', '$q',function ($http, $q) {
	var TicketCategoryService = {
		category:{},
        categories: [],
        retrieveAllCategories:retrieveAllCategories
    };
	
	
 	return TicketCategoryService;
 	
    // implementation
    function retrieveAllCategories() {
        var def = $q.defer();
        $http.get(hostLocation+"/incident/ticketcategories")
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

chrisApp.factory("statusService", ['$http', '$q',function ($http, $q) {
	var StatusService = {
		status:{},
        statusList: [],
        retrieveAllStatus:retrieveAllStatus,
    };
	
	
 	return StatusService;
 	
    // implementation
    function retrieveAllStatus() {
        var def = $q.defer();
        $http.get(hostLocation+"/incident/status/CT")
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
chrisApp.factory("ticketService", ['$http', '$q',function ($http, $q) {
	var TicketService = {
		ticket:{},
        ticketList: [],
        openTicketList:[],
        approachingTicketList:[],
        priorityTicketList:[],
        escalatedTicketList:[],
        saveTicket:saveTicket,
        displayAllOpenTickets:displayAllOpenTickets,
        summaryViewOfOpenTickets:summaryViewOfOpenTickets,
        summaryViewSLATickets:summaryViewSLATickets,
        retrieveTicketDetails:retrieveTicketDetails,
        getPriorityTickets:getPriorityTickets,
        getSPTickets:getSPTickets,
        getTicketPriorityAndSLA:getTicketPriorityAndSLA,
        setIncidentSelected:setIncidentSelected,
        getSelectedTicketFromSession:getSelectedTicketFromSession,
        escalateTicket:escalateTicket,
        linkTicket:linkTicket,
        getLinkedTickets:getLinkedTickets,
        deleteLinkedTicket:deleteLinkedTicket,
        changeLinkedTicketStatus:changeLinkedTicketStatus,
        getTicketHistory:getTicketHistory,
        saveComment:saveComment,
        listComment:listComment,
        listAttachedFiles:listAttachedFiles,
        deleteFileAttached:deleteFileAttached,
        addImage:addImage,
        removeImage:removeImage,
      //Added By Supravat for Financials Requirements.
		deleteFinalcialsCostItems:deleteFinalcialsCostItems,
        saveFinalcialsCostItems:saveFinalcialsCostItems,
		//Ended By Supravat.
        //Added By Supravat for Related Tickets Requirements.
        getRelatedTicketData:getRelatedTicketData,
		//Ended By Supravat.
        getTicketAttachment:getTicketAttachment
        
        
    };
 	return TicketService;
 	
 	 // implementation
    function getTicketAttachment(ticketId) {
        var def = $q.defer();
        $http.get(hostLocation+"/incident/file/attachments/"+ticketId)
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
 	
 	//Added By Supravat for Related Tickets Requirements.
 	function getRelatedTicketData(relatedInputData, mode) {
        var def = $q.defer();
        var url=""
        	url=hostLocation+"/site/relatedtickets/"+relatedInputData.ticketId + "/"+relatedInputData.siteId;
        /*if(mode!=undefined && mode.toUpperCase()=="SP"){
        	url=hostLocation+"/sp/site/relatedtickets/"+relatedInputData.ticketId + "/"+relatedInputData.siteId;
        }else{
        	url=hostLocation+"/site/relatedtickets/"+relatedInputData.ticketId + "/"+relatedInputData.siteId;
        }*/
        
        $http.get(url)
        .success(function(data) {
            def.resolve(data);
        })
        .error(function(data) {
            def.reject(data);
        });
        return def.promise;
    }
 	//Ended By Supravat.
 	
 	//Added By Supravat for Financials Requirements.
 	function saveFinalcialsCostItems(financialCostDetails, mode) {
        var def = $q.defer();
        var url=""
        	url=hostLocation+"/incident/updateFinancial";
	       /* if(mode!=undefined && mode.toUpperCase()=="SP"){
	        	url=hostLocation+"/sp/financeupdate";
	        }else{
	        	url=hostLocation+"/incident/updateFinancial";
	        }*/
        $http.post(url,financialCostDetails)
            .success(function(data) {
                def.resolve(data);
            })
            .error(function(data) {
                def.reject(data);
            });
        return def.promise;
    }
 	
 	function deleteFinalcialsCostItems(financialCostDetails, mode) {
        var def = $q.defer();
        var url=hostLocation+"/incident/deleteFinancial";
	        /*if(mode!=undefined && mode.toUpperCase()=="SP"){
	        	url=hostLocation+"/sp/incident/deleteFinancial";
	        }else{
	        	url=hostLocation+"/incident/deleteFinancial";
	        }*/
        $http.post(url,financialCostDetails)
            .success(function(data) {
                def.resolve(data);
            })
            .error(function(data) {
                def.reject(data);
            });
        return def.promise;
    }
 	//Ended By Supravat.
 	
 	
 // implementation
    function removeImage(incidentImage, mode) {
        var def = $q.defer();
       var  url=hostLocation+"/incident/image/remove";
      /*  if(mode!=undefined && mode.toUpperCase()=="SP"){
        	url=hostLocation+"/sp/incident/image/remove";
        }else{
        	url=hostLocation+"/incident/image/remove";
        }*/
        $http.post(url, incidentImage)
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
    function addImage(incidentImage, mode) {
        var def = $q.defer();
        url=hostLocation+"/incident/image/upload";
       /* if(mode!=undefined && mode.toUpperCase()=="SP"){
        	url=hostLocation+"/sp/image/upload";
        }else{
        	url=hostLocation+"/incident/image/upload";
        }*/
        $http.post(url, incidentImage)
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
    function listAttachedFiles(attachment, mode) {
        var def = $q.defer();
        url=hostLocation+"/incident/attachment/info";
        /*if(mode!=undefined && mode.toUpperCase()=="SP"){
        	url=hostLocation+"/sp/incident/comment/save";
        }else{
        	url=hostLocation+"/incident/attachment/info";
        }*/
        $http.post(url, attachment)
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
    function deleteFileAttached(feature,fileList) {
        var def = $q.defer();
        $http.get(hostLocation+"/file/attachement/delete/"+feature+"/"+fileList+"/"+null)
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
    function listComment(ticketId) {
        var def = $q.defer();
        $http.get(hostLocation+"/incident/comment/list/"+ticketId)
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
    function saveComment(ticket, mode) {
        var def = $q.defer();
        url=hostLocation+"/incident/comment/save";
       /* if(mode!=undefined && mode.toUpperCase()=="SP"){
        	url=hostLocation+"/sp/incident/comment/save";
        }else{
        	url=hostLocation+"/incident/comment/save";
        }*/
        $http.post(url, ticket)
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
    function getTicketHistory(ticketId) {
        var def = $q.defer();
        $http.get(hostLocation+"/incident/history/"+ticketId)
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
    function changeLinkedTicketStatus(linkTicket, mode) {
        var def = $q.defer();
        url=hostLocation+"/incident/linkedticket/status/"+linkTicket.detail.id+"/"+linkTicket.status
        /*if(mode!=undefined && mode.toUpperCase()=="SP"){
        	url=hostLocation+"/sp/linkedticket/status/"+linkTicket.detail.id+"/"+linkTicket.status
        }else{
        	url=hostLocation+"/incident/linkedticket/status/"+linkTicket.detail.id+"/"+linkTicket.status
        }*/
        $http.get(url)
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
    function deleteLinkedTicket(linkTicket,mode) {
        var def = $q.defer();
        url=hostLocation+"/incident/linkedticket/delete/"+linkTicket.id
       /* if(mode!=undefined && mode.toUpperCase()=="SP"){
        	url=hostLocation+"/sp/linkedticket/delete/"+linkTicket.id
        }else{
        	url=hostLocation+"/incident/linkedticket/delete/"+linkTicket.id
        }*/
        $http.get(url)
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
    function getLinkedTickets(custTicket,mode) {
        var def = $q.defer();
        url=hostLocation+"/incident/linkedticket/list/"+custTicket
      /*  if(mode!=undefined && mode.toUpperCase()=="SP"){
        	url=hostLocation+"/sp/linkedticket/list/"+custTicket
        }else{
        	url=hostLocation+"/incident/linkedticket/list/"+custTicket
        }*/
        $http.get(url)
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
    function linkTicket(linkedTicket, mode) {
        var def = $q.defer();
        url=hostLocation+"/incident/linkedticket/"+linkedTicket.parentTicketId+"/"+linkedTicket.parentTicketNo+"/"+linkedTicket.linkedTicketNo
       /* if(mode!=undefined && mode.toUpperCase()=="SP"){
        	url=hostLocation+"/sp/linkedticket/"+linkedTicket.parentTicketId+"/"+linkedTicket.parentTicketNo+"/"+linkedTicket.linkedTicketNo
        }else{
        	url=hostLocation+"/incident/linkedticket/"+linkedTicket.parentTicketId+"/"+linkedTicket.parentTicketNo+"/"+linkedTicket.linkedTicketNo
        }*/
        $http.get(url)
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
    function escalateTicket(escalations) {
        var def = $q.defer();
        $http.post(hostLocation+"/incident/escalate/", escalations)
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
	    function getSelectedTicketFromSession(mode) {
	        var def = $q.defer();
	        var url=""
	        	url=hostLocation+"/incident/session/ticket/update";
		       /* if(mode!=undefined && mode.toUpperCase()=="SP"){
		        	url=hostLocation+"/sp/session/ticket/update";
		        }else{
		        	url=hostLocation+"/incident/session/ticket/update";
		        }*/
	        $http.get(url)
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
	    function setIncidentSelected(selectedTicket,mode) {
	        var def = $q.defer();
	        var url=""
	        	url=hostLocation+"/incident/selected/ticket";
	        /*if(mode!=undefined && mode.toUpperCase()=="SP"){
	        	url=hostLocation+"/sp/selected/ticket";
	        }else{
	        	url=hostLocation+"/incident/selected/ticket";
	        }*/
	        $http.post(url, selectedTicket)
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
 	    function getTicketPriorityAndSLA(spId, categoryId) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/incident/priority/sla/"+spId+"/"+categoryId)
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
    function saveTicket(customerTicket, mode) {
        var def = $q.defer();
        var url=hostLocation+"/incident/create";
	        /*if(mode!=undefined && mode.toUpperCase()=="SP"){
	        	url=hostLocation+"/sp/incident/update";
	        }else{
	        	url=hostLocation+"/incident/create";
	        }*/
        $http.post(url,customerTicket)
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
    function retrieveTicketDetails(ticket) {
        var def = $q.defer();
        $http.get(hostLocation+"/incident/ticket/"+ticket.id)
            .success(function(data) {
            	//console.log(data)
            	TicketService.ticket=data;
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
    // implementation
    function displayAllOpenTickets(mode) {
        var def = $q.defer();
        var url=""
	        if(mode!=undefined && mode.toUpperCase()=="SP"){
	        	url=hostLocation+"/sp/incident/list";
	        }else{
	        	url=hostLocation+"/incident/list";
	        }
        $http.get(url)
            .success(function(data) {
            	//console.log(data)
            	TicketService.ticketList=data;
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
 // implementation
    function summaryViewOfOpenTickets() {
        var def = $q.defer();
        $http.get(hostLocation+"/test/api/siteticketstatus")
            .success(function(data) {
            	//console.log(data)
            	TicketService.openTicketList=data;
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
    
 // implementation
    function summaryViewSLATickets() {
        var def = $q.defer();
        $http.get(hostLocation+"/test/api/slatickets")
            .success(function(data) {
            	//console.log(data)
            	TicketService.approachingTicketList=data;
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
    
 // implementation
    function getPriorityTickets() {
        var def = $q.defer();
       /* $http.get("http://34.209.65.191:8282/test/api/siteticketstatus")
            .success(function(data) {
            	console.log(data)
            	TicketService.priorityTicketList=data;
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });*/
        return def.promise;
    }
    
    // implementation
    function getSPTickets() {
        var def = $q.defer();
        $http.get(hostLocation+"/test/api/sptickets")
            .success(function(data) {
            	//console.log(data)
            	TicketService.escalatedTicketList=data;
                def.resolve(data);
            })
            .error(function(data) {
            	console.log(data)
                def.reject(data);
            });
        return def.promise;
    }
    
}]);

chrisApp.factory("authService", ['$http', '$q',function ($http, $q) {
	var AuthService = {
        featureAccessList:{},
        loggedinUserAccess:loggedinUserAccess,
    };
	
	
 	return AuthService;
 	
    // implementation
    function loggedinUserAccess() {
        var def = $q.defer();
        $http.get(hostLocation+"/auth/user/access")
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

chrisApp.factory("registrationService", ['$http', '$q',function ($http, $q) {
	var RegisterService = {
        featureAccessList:{},
        registerUser:registerUser,
    };
 	return RegisterService;
 	
    // implementation
    function registerUser(user) {
        var def = $q.defer();
        $http.post(hostLocation+"/user/register",user)
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

chrisApp.factory("roleService", ['$http', '$q',function ($http, $q) {
	var RoleService = {
        role:{},
        retrieveRoles:retrieveRoles,
        updateRole:updateRole
        
    };
 	return RoleService;
 	
    // implementation
    function retrieveRoles() {
        var def = $q.defer();
        $http.get(hostLocation+"/user/roles")
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
    function updateRole(userInfo) {
        var def = $q.defer();
        $http.post(hostLocation+"/user/role/update", userInfo)
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


