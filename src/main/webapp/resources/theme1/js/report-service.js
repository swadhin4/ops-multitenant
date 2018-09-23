
chrisApp.factory('reportService',  ['$http', '$q',function ($http, $q) {
 		var ReportService = {
 			getIncidentReports:getIncidentReports
        };
 		return ReportService;
		
 		
 	// implementation
 	    function getIncidentReports(loginUser) {
 	        var def = $q.defer();
 	        $http.get(hostLocation+"/reports/details/"+loginUser.email)
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

