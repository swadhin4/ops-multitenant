chrisApp.factory("passwordService", ['$http', '$q',function ($http, $q) {
	var PasswordService = {
		resetPassword:resetPassword
    };
 	return PasswordService;
    // implementation
    function resetPassword(email, type) {
        var def = $q.defer();
        var data={
        	email: email
        }
        $http.post(hostLocation+"/forgot/password/reset?email="+email+"&type="+type)
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