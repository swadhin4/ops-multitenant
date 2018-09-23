var orderApp=angular.module("orderApp", ["xeditable","checklist-model"]);


orderApp.controller('orderController',  function($rootScope,$scope,$http,$filter,$q) {
	
	$scope.orderSteps=['New Challan','Challan Status','Cancel Challan','Approved Challans','New Order','Order Status','Cancel Order','List Orders'];
	$scope.informationPanel=true;
	$scope.formPanel=false;
	
	$scope.challanproducts=[];
	$http.get(webContextPath+'/product/list')
	.success(function(data) {
		$.each(data.productVOList, function(key, val) {
			$scope.challanproducts.push(val);
		});
	});
	
	$scope.vendorProducts=[];
	$http.get(webContextPath+"/vendor/list")
	.success(function(data){
		
		if(data.vendorVOList.length == 0){
			$scope.vendorProducts=[];
			
		}else{
			$scope.vendorProducts=[];
			$.each(data.vendorVOList,function(i,val){
				$scope.vendorProducts.push(val);
			})
		}
	})
	
	  $scope.selectedProduct;
		$scope.setProduct=function(productSelected){
			  $scope.selectedProduct=productSelected;
			
		}
		$scope.selectedVendor;
		$scope.setVendor=function(vendorSelected){
			  $scope.selectedVendor=vendorSelected;
		}
	
		$scope.selectedgrades;
		$scope.totalGrades;
		$scope.productVendor;
		 
		$scope.findGrades=function(selectedVendor){
			$scope.selectedVendor=selectedVendor;
			$scope.gradeList=[];
			$http.get(webContextPath+"/grades/names/"+selectedVendor.vendorId+"/"+$scope.selectedProduct.productId)
			.success(function(data){
				if(data.length==0){
					$scope.selectedgrades.length=0;
				}else{
					$scope.totalGrades=data.length;
					console.log(data);
					$scope.selectedgrades=data;
					}
				})
		}
		
		$scope.thickness;
		$scope.gradeName;
		$scope.findThickness=function(selectedGrade){
			//alert($scope.selectedVendor.vendorId)
			$scope.gradeName=selectedGrade;
			$http.get(webContextPath+"/grades/thickness/names/"+$scope.selectedVendor.vendorId+"/"+$scope.selectedProduct.productId+"/"+selectedGrade)
			.success(function(data){
				console.log(data);
				if(data.length==0){
				}else{
					$scope.thickness=data;
					}
				})
		}
		
		$scope.selectedThickness;
		$scope.thicknessSizes;
		$scope.findThicknessSize=function(selectedThickness){
			$scope.selectedThickness=selectedThickness;
			$http.get(webContextPath+"/grades/thickness/sizes/"+$scope.selectedVendor.vendorId+"/"+$scope.selectedProduct.productId+"/"+$scope.gradeName+"/"+selectedThickness)
			.success(function(data){
				console.log(data);
				if(data.length==0){
				}else{
					$scope.thicknessSizes=data;
					}
				})
		}
			
		$scope.sizePrice;
		$scope.findPrice=function(selectedSizes){
			$http.get(webContextPath+"/grades/thickness/sizes/price/"+$scope.selectedVendor.vendorId+"/"+$scope.selectedProduct.productId+"/"+$scope.gradeName+"/"+$scope.selectedThickness+"/"+selectedSizes)
			.success(function(data){
				console.log(data);
				if(data.length==0){
				}else{
					$scope.sizePrice=data;
					}
				})
		}
		

		
});



