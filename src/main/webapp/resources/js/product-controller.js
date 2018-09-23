productApp.controller('productController', ['$rootScope','$scope','$http', function($rootScope,$scope,$http) {
	$rootScope.products=[];
	$rootScope.product;
	$scope.show=true;
	$scope.productselected=false;
	$scope.productPrevSelected=null;
	$scope.operation='ADD';
	 angular.element(document).ready(function () {
		 $rootScope.getAllProducts();
	 });
	
	$rootScope.getAllProducts=function(){
		$http.get(webContextPath+'/product/list')
		.success(function(data) {
			console.log(data);
			$rootScope.products=[];
			if(data.statusCode==200){
				$.each(data.object, function(key, val) {
					$rootScope.products.push(val);
				});
				console.log($rootScope.products)
				//$rootScope.productDetail=$rootScope.products[0];
				$rootScope.productselected=true;
			}
			
		 });
	}
	
	$scope.reset=function(){
		$scope.operation='ADD';
		$scope.productDetail={};
		$rootScope.getAllProducts();
		$('#successp-alert').hide();
		$('#dangerp-alert').hide();
	}
	
	$scope.showProductDetail=function(product){
		$scope.productselected=true;
		$scope.productDetail=product;
		$scope.operation='EDIT';
		$('#successp-alert').hide();
		$('#dangerp-alert').hide();
		
	};
	
	
	$scope.confirmDeleteProduct=function(productSelected){
		$scope.productDetail=productSelected;
		$('#confirmModal').modal('show');
		$('#successp-alert').hide();
		$('#dangerp-alert').hide();
	}
	
	$scope.deleteProduct=function(product){
		var product=$scope.productDetail;
		console.log(product);
		$http.get(webContextPath+"/product/delete/"+product.productId)
		.success(function(data){
			if(data.statusCode==200){
				$scope.message="Product \""+product.productName+"\" deleted successfully";
				$('#successp-alert').alert();
				$('#successp-alert').show();
				$('#dangerp-alert').hide();
         		$("#successp-alert").fadeTo(2000, 500);
        		$rootScope.getAllProducts();
        		$('#confirmModalNo').click();
			}else{
				$scope.message=data.message;
				$('#successp-alert').hide();
				$('#dangerp-alert').alert();
				$('#dangerp-alert').show();
	     		$("#dangerp-alert").fadeTo(2000, 500);
	    		$rootScope.getAllProducts();
			}
		});
	};
	
	$scope.saveProduct = function(productForm) {
		console.log($scope.productDetail);
		$scope.message="";
		var productDetail=angular.copy($scope.productDetail);
		
		if($.trim(productDetail.productName)==""){
			$scope.message="Product name should not be empty.";
			$('#dangerp-alert').alert();
     		$("#dangerp-alert").fadeTo(2000, 500);
		}else{
			var url="";
			if($scope.operation.toUpperCase()=='ADD'){
				url=webContextPath+"/product/save";
			}else if($scope.operation.toUpperCase()=='EDIT'){
				url=webContextPath+"/product/update";
			}
			$http.post(url,productDetail)
			.success(function(data){
				console.log(data);
				if(data.statusCode==200){
					$scope.message=data.message;
					$('#successp-alert').alert();
					$('#successp-alert').show();
					$('#dangerp-alert').hide();
		     		$("#successp-alert").fadeTo(2000, 500);
		            $rootScope.getAllProducts();
				}else{
					$scope.message=data.message;
					$('#dangerp-alert').alert();
					$('#successp-alert').hide();
					$('#dangerp-alert').show();
	         		$("#dangerp-alert").fadeTo(2000, 500);
	         		$rootScope.getAllProducts();
				}
				 	
			  });
		}
	};
		
}]);

