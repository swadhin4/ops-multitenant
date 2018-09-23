

chrisApp.controller('languageController',  ['$rootScope', '$scope', '$filter', 'translatorService',
                                        function ($rootScope, $scope , $filter,translatorService) {
	
	$rootScope.language="English";
	$rootScope.sitePage={};
	angular.element(document).ready(function(){
		//$scope.getEngTranslators();
		//$scope.getFreTranslators();
	});
	$scope.changeLanguage=function(lg1){
		var fileSelected = {};
		$rootScope.sitePage = fileSelected.sitePage;
		if(lg1=="en"){
			$rootScope.language="English";
			fileSelected = $.jStorage.get("translatedEngFile");
		}else{
			$rootScope.language="French";
			fileSelected = $.jStorage.get("translatedFreFile");
		}
		$.jStorage.set("selectedLanguage", lg1);
		$.jStorage.set("lang", $rootScope.language);
		$rootScope.sitePage = fileSelected.sitePage;
	}
	$scope.getEngTranslators=function(){
		translatorService.getEnglishLanguageTranslator()
		.then(function(data){
			console.log(data);
			$.jStorage.set("translatedEngFile", data);
			$scope.changeLanguage('en');
		},function(data){
			console.log(data);
		});
	}
	
	$scope.getFreTranslators=function(){
		translatorService.getFrenchLanguageTranslator()
		.then(function(data){
			console.log(data);
			$.jStorage.set("translatedFreFile", data);
		},function(data){
			console.log(data);
		});
	}

	console.log($rootScope.language)
}]);


