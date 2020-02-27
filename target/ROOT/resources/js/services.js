angular.module('productApp').factory("employeeService", ['$http', '$q',function ($http, $q) {
        // interface
        var EmployeeService = {
            employeeList: [],
            getAllEmployee: getAllEmployee,
           // getEmployeeByName:getEmployeeByName
        };
        
        return EmployeeService;

        // implementation
        function getAllEmployee() {
            var def = $q.defer();
            $http.get(webContextPath+'/employee/list')
                .success(function(data) {
                	console.log(data)
                	EmployeeService.employeeList = data;
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get albums");
                });
            return def.promise;
        }
}]);


angular.module('productApp').factory("agentService", ['$http', '$q',function ($http, $q) {
    // interface
    var AgentService = {
        agentList: [],
        getAllAgents: getAllAgents,
    };
    
    return AgentService;

    // implementation
    function getAllAgents() {
        var def = $q.defer();
        $http.get(webContextPath+'/agent/list')
            .success(function(data) {
            	console.log(data)
            	AgentService.agentList = data;
                def.resolve(data);
            })
            .error(function() {
                def.reject("Failed to get agents");
            });
        return def.promise;
    }
    
    
}]);



angular.module('productApp').factory("stockService", ['$http', '$q',function ($http, $q) {
    // interface
    var StockService = {
    	updateStocks:updateStocks,
    };
    
    return StockService;

    // implementation
    function updateStocks(gradeWarehouseData) {
        var def = $q.defer();
        $http.post(webContextPath+'/stock/entry', JSON.stringify(gradeWarehouseData))
            .success(function(data) {
            	console.log(data)
                def.resolve(data);
            })
            .error(function() {
                def.reject("Failed to get agents");
            });
        return def.promise;
    }
    
    
}]);



