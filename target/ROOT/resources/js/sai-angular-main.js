var webContextPath="${pageContext.request.contextPath}";
var productApp=angular.module("productApp", ["ngRoute","xeditable","angucomplete-alt"]);

productApp.factory("OrderSearchService", ["$http",
      function ($http) {
		return {
	        searchOrder: function (orderNumber) {
	        	 var url=webContextPath+"/order/search/"+orderNumber
	        	 var orderData = $http.get(url);
	        	 var responseObj = this;
	        	orderData.then(function(response) {
	        		angular.extend(responseObj,response.data.object)  
	            });
	        	return responseObj;
	        }
     }
  }]);


productApp.directive('validNumber', function() {
    return {
      require: '?ngModel',
      link: function(scope, element, attrs, ngModelCtrl) {
        if(!ngModelCtrl) {
          return; 
        }

        ngModelCtrl.$parsers.push(function(val) {
          if (angular.isUndefined(val)) {
              var val = '';
          }
          
          var clean = val.replace(/[^-0-9\.]/g, '');
          var negativeCheck = clean.split('-');
			var decimalCheck = clean.split('.');
          if(!angular.isUndefined(negativeCheck[1])) {
              negativeCheck[1] = negativeCheck[1].slice(0, negativeCheck[1].length);
              clean =negativeCheck[0] + '-' + negativeCheck[1];
              if(negativeCheck[0].length > 0) {
              	clean =negativeCheck[0];
              }
              
          }
            
          if(!angular.isUndefined(decimalCheck[1])) {
              decimalCheck[1] = decimalCheck[1].slice(0,2);
              clean =decimalCheck[0] + '.' + decimalCheck[1];
          }

          if (val !== clean) {
            ngModelCtrl.$setViewValue(clean);
            ngModelCtrl.$render();
          }
          return clean;
        });

        element.bind('keypress', function(event) {
          if(event.keyCode === 32) {
            event.preventDefault();
          }
        });
      }
    };
  });