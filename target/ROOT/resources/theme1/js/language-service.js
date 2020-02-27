chrisApp.factory("translatorService", ['$http', '$q',function ($http, $q) {
	//34-209-65-191
	
	 	var TranslatorService = {
	 			getEnglishLanguageTranslator:getEnglishLanguageTranslator,
	 			getFrenchLanguageTranslator:getFrenchLanguageTranslator
	        };
	 	
	 	return TranslatorService;
	 	
	 	 // implementation
	    function getEnglishLanguageTranslator() {
	        var def = $q.defer();
	       var url=hostLocation+"/resources/theme1/json/language_en.json";
	        $http.get(url)
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
	    
	    
	    function getFrenchLanguageTranslator() {
	        var def = $q.defer();
	        var url=hostLocation+"/resources/theme1/json/language_fr.json";
	        $http.get(url)
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