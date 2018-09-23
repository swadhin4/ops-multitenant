angular.module('demo', []).directive('positive', [function() {
    return {
        require: 'ngModel',
        link: function(scope, elem, attrs, ctrl) {
            if (!ctrl) return;
            ctrl.$validators.positive = function(value) {
                return value && value >= 0;
            };
        }
    };
}]);

productApp.controller('gradeController', ['$rootScope','$scope','$http','$filter', function($rootScope,$scope,$http,$filter) {
	$scope.productselected=false;
	$scope.vendorselected=false;
	$scope.warehouseselected=false;
	$scope.productype="";
	$scope.gradeDetail={};
	$scope.productVendor={
			selectedProduct:'',
			selectedVendor:'',
			selectedWarehouse:''	
	};
	
	$scope.hardwareUnits=[
		      {	unit:'KG',value: "Kilogram"},
		      { unit:"PCS",value:"Pieces"},
		      { unit:"LTR",value:"Litre"},
		      {unit:"PKT", value:"Packect"}
	      ]
	
	$scope.orderItem={};
	
	$scope.getSelectedProduct=function(selectedProduct){
		$scope.orderItem.selectedProduct=selectedProduct;
		if(selectedProduct==null){
			
		}
		else if(selectedProduct.productName.toUpperCase()=='HARDWARE'){
			$scope.productype=selectedProduct.productName;
			console.log($scope.productype)
		}else{
			$scope.productype=selectedProduct.productName;
			console.log($scope.productype)
			$scope.productVendor.selectedProduct=selectedProduct;
			$scope.productselected=true;
			//$scope.viewGrades();
			$scope.getVendorList();
		}
	};
	
	
	$scope.getSelectedVendor=function(selectedVendor){
		$scope.productVendor.selectedVendor=selectedVendor;
		$scope.vendorselected=true;
		$scope.orderItem.selectedVendor=selectedVendor;
		$scope.viewGrades();
	};
	
	$scope.getSelectedWarehouse=function(selectedWarehouse){
		console.log(selectedWarehouse);
		$scope.productVendor.selectedWarehouse=selectedWarehouse;
		$scope.warehouseselected=true;
	}

	angular.element(document).ready(function () {
		$scope.getAllProducts();
		$scope.getAllAgentTypes();
		$scope.order={};
		$scope.hardwareProduct={};
		$scope.emptyHardware={};
		$scope.hardwareproducts=[];
		$scope.getAllHardwares();	
		$scope.selectedHardwareUnit;
    });
	
	$scope.getAllHardwares=function(){
		$http.get(webContextPath+"/hardware/list").success(
		function(data) {
			console.log(data);
			if(data.statusCode==200){
				$scope.hardwareproducts=[];
				$.each(data.object,function(key,val){
					$scope.hardwareproducts.push(val);
				});
			}else{
				
			}
		});
	}
	
    $scope.allItems = [];
	$scope.getAllGrades=function(){
		$http.get(webContextPath+"/grades/list").success(
		function(data) {
			console.log(data);
			if(data.statusCode==200){
				$scope.allItems=[];
				$.each(data.object,function(key,val){
					$scope.allItems.push(val);
				});
			}else{
				
			}
		});
	}
	
	
	$scope.operation='ADD';
	$scope.addHardwareProduct=function(hardwareForm){
	/*	$scope.hardwareproducts.push(hardware);
		$scope.hardwareProduct={};
		$scope.hardwarelist=angular.copy($scope.hardwareproducts);*/
		$scope.saveHardware(hardwareForm);
	}
	
	$scope.setSelectedUnit=function(unit){
		console.log(unit);
		$scope.selectedHardwareUnit=unit;
	}
	$scope.saveHardware=function(aForm){
		console.log(aForm);
		 var inValidParams=[];
			angular.forEach(aForm, function(value, key) {
				  if(key[0] == '$') return;
				  	console.log(key, value.$valid)
			  		if(value.$valid==false){
			  			value.$invalid=true;
			  			value.$pristine=false;
			  			var validHardwareObject={
			  					field:key,
			  					val:value.$modelValue||""
			  			}
			  			inValidParams.push(validHardwareObject);
			  		}
				  	return false;
				});
		 
			console.log(inValidParams);
			if(inValidParams.length==0){
				 var hardwareData={};
				 if($scope.operation.toUpperCase()=='ADD'){
					 hardwareData=angular.copy($scope.hardwareProduct);
				 }else if($scope.operation.toUpperCase()=='EDIT'){
					 hardwareData=angular.copy($scope.hardwareProduct);
				 }
				 console.log($scope.selectedHardwareUnit);
				 hardwareData.unitType=$scope.selectedHardwareUnit.value;
				console.log("Hardware Data : " +JSON.stringify(hardwareData));
				$http.post(webContextPath+"/hardware/save", hardwareData).success(
					function(data) {
						if(data.statusCode==200){
							 if($scope.operation.toUpperCase()=='ADD'){
								 $scope.successMessage="Hardware details saved successfully."
								 $('#success-alert').show();
								 $('#success-alert').alert();
					             $("#success-alert").fadeIn(2000, 500);
					             $('#danger-alert').hide();
					          $scope.getAllHardwares();
							 }else if($scope.operation.toUpperCase()=='EDIT'){
								 $scope.successMessage="Hardware details updated successfully."
									 $('#success-alert').show();
								 $('#success-alert').alert();
					             $("#success-alert").fadeIn(2000, 500);
					             $('#danger-alert').hide();
					            $scope.getAllHardwares();
							 }
						}else{
							 $scope.errorMessage=data.message;
							 if($scope.operation.toUpperCase()=='ADD'){
								 $('#danger-alert').show();
								 $('#danger-alert').alert();
					             $("#danger-alert").fadeIn(2000, 500);
					             $('#success-alert').hide();
							 }else if($scope.operation.toUpperCase()=='EDIT'){
								 $('#danger-alert').show();
								 $('#danger-alert').alert();
					             $("#danger-alert").fadeIn(2000, 500);
					             $('#success-alert').hide();
							 }
						}
					});
			  }
	}
	
	$scope.editSelectedHardware=function(selectedHardware){
		console.log(selectedHardware);
		$scope.hardwareProduct=angular.copy(selectedHardware);
		$.each($scope.hardwareUnits,function(key,val){
			if(val.value.toUpperCase() == selectedHardware.unitType.toUpperCase() ){
				$scope.selectedHardwareUnit=val;
				return false;
			}
		});
		$scope.operation='EDIT';
		console.log($scope.selectedHardwareUnit);
		
	}
	$scope.selectedOrderHardware=null;
	$scope.getHardwareAvailableStock=function(selectedHardware){
		
		$.each($scope.hardwareproducts,function(key,val){
			if(val.hardwareStockId == selectedHardware.hardwareStockId){
				$scope.selectedOrderHardware=val;
				return false;
			}
		});
		console.log($scope.selectedOrderHardware);
	}
	
	$scope.reset=function(){
		$scope.hardwareProduct=angular.copy($scope.emptyHardware);
		$scope.selectedHardwareUnit={};
		$('#success-alert').hide();
		$('#danger-alert').hide();
		$scope.operation='ADD';
		$scope.gradeDetail={};
	 };
	
	
	$scope.copyAddress=function(){
		if($('#copyaddress').is(':checked')){
	    	$scope.order.addressVO={
	    		addressLine1:$scope.order.customerVO.addressLine1,
	    		addressLine2:$scope.order.customerVO.addressLine2,
	    		mobile:$scope.order.customerVO.mobile,
	    		email:$scope.order.customerVO.email,
	    		state:$scope.order.customerVO.state,
	    		city:$scope.order.customerVO.city
	    	};
	    }
		console.log($scope.order.customerVO);
		console.log($scope.order.addressVO);
	};
	
	
	$scope.referredAgents={
			 selected:'',
			 values:[]
	 };
	$scope.referredAgents.selected = "NO AGENT";
	$scope.getAllAgentTypes=function(){
		var agentObject={
				agentId:0,
				name:"NO AGENT"
		}
	  $http.get(webContextPath+'/agent/list')
		.success(function(data) {
			console.log(data);
			if(data.object.length>0){
				$scope.referredAgents.values=[];
				$.each(data.object,function(k,val){
					$scope.referredAgents.values.push(val);
				});
				$scope.referredAgents.values.push(agentObject);
			}else{
				
			}
		});
	}
	
	$scope.getAllProducts=function(){
		$http.get(webContextPath+'/product/list')
		.success(function(data) {
			console.log(data);
			if(data.statusCode==200){
				$scope.products=[];
				$.each(data.object, function(key, val) {
					$scope.products.push(val);
				});
				// $scope.productselected=true;
				 //$scope.getVendorList();
			}
		 });
	}
	
	$scope.getVendorList=function(){
		$http.get(webContextPath+'/vendor/list')
		.success(function(data) {
			console.log(data);
			if(data.statusCode==200){
				$scope.vendors=[];
				$.each(data.object, function(key, val) {
					console.log(val);
					$scope.vendors.push(val);
				});
			}
			//$scope.getSelectedVendor($scope.vendors[0]);
		});
	}
	
	
	
	  $scope.newgrades=[];
	  $scope.thicknessUnits = [
                 {value: "MM", text: 'MM'},
                 {value: "CM", text: 'CM'},
                 {value: "INCH", text: 'INCH'},
	  ]; 
	  
	  $scope.sizeUnits = [
                   {value: "FT", text: 'Ft'},
                   {value: "MTR", text: 'Meter'}
	     ]; 
	 $scope.isProductVendorSelected=false;
		$scope.vendorGrades=[];
		$scope.viewGrades=function(){
			console.log($scope.productselected,$scope.vendorselected);
			$('#gradesuccess-alert').hide();
	   		$('#gradedanger-alert').hide();
			if($scope.productselected && $scope.vendorselected){
				$scope.isProductVendorSelected=true;
				var vendorId=$scope.productVendor.selectedVendor.vendorId;
				var productId=$scope.productVendor.selectedProduct.productId;
			$http.get(webContextPath+'/vendor/product/grades/'+vendorId+'/'+productId)
			.success(function(data) {
				console.log(data);
				$scope.newgrades=[];
				$scope.vendorGrades=[];
				$.each(data,function(i,val){
					console.log(val);
				var grade={
						     gradeId: val[0],		
							 gradeName: val[1],
							 thickness: val[2],
							 thicknessUnit:val[3],
							 length:val[4],
							 width:val[5],
							 sizeUnit:val[6],
							 price:val[7],  
							 vat:val[8],
							 description:val[13]
				  }
				
				var vendorGrade={
						 gradeId:val[0],
						 gradeName: val[1],
						 thickness: val[2],
						 thicknessUnit:val[3],
						 length:val[4],
						 width:val[5],
						 sizeUnit:val[6],
						 price:val[7],  
				         gradeDetails:val[1]+" - "+val[2]+val[3]+" [ " +val[4]+val[6] + " x "+val[5]+val[6]+" ]",
				         vat:val[8],
				         quantity:0,
				         description:val[13]
				}
					$scope.newgrades.push(grade);
					$scope.vendorGrades.push(vendorGrade);
					$scope.vendorGradeStock=[];
				})
			});
			}else{
				$scope.isProductVendorSelected=false;
			}
		}
		
		$scope.getTemplate = function (grade) {
			console.log(grade)
			if(grade.gradeId==null){
				return 'add';
			}
			else if (grade.gradeId === $scope.gradeDetail.gradeId) {
	        	return 'edit';
			}
	        else {
	        	return 'display';
	        }
	    };
	    
	    $scope.editGrade = function (selectedGrade) {
	    	$scope.gradeDetail = angular.copy(selectedGrade);
	    };

		
		$scope.states=[];
		$http.get(webContextPath+'/resources/json/state-cities.json').
		success(function(data) {
	    	$scope.states = data;
	    	console.log($scope.states);
		});
		
		 $scope.cities=[];
			$scope.getCities = function(fromSelect, object) {
				if(object=='customer'){
					$scope.order.customerVO.state=fromSelect.name;
				}else if(object=='delivery'){
					$scope.order.addressVO.state=fromSelect.name;
				}
				$scope.cities=fromSelect.cities;
				console.log($scope.cities);
			};
			
			$scope.getCityName=function(citySelect,obj){
				if(obj=='customer'){
					$scope.order.customerVO.city=citySelect.name;
				}else if(obj=='delivery'){
					$scope.order.addressVO.city=citySelect.name;
				}
			}
		
		
	$scope.getSelectedGradeData=function(selectedGradeDetail){
		console.log(selectedGradeDetail.gradeId);
		console.log(selectedGradeDetail.gradeDetails);
	};
	
	$scope.filterValue = function($event){
        if(isNaN(String.fromCharCode($event.keyCode))){
            $event.preventDefault();
        }
	};
	
	$scope.gradeStockAvailable=0;
	$scope.selectedGrade={};
	$scope.warehouseGradeQty=[];
	$scope.isresult=false;
	$scope.isgradeSelected=false;
	
	$scope.getAvailableStock=function(selectedGrade, from){
		$scope.selectedGrade={};
		console.log(selectedGrade);
		$scope.isgradeSelected=true;
		$scope.warehouseGradeQty=[];
		if(selectedGrade!=null && $scope.isgradeSelected){
			
			//$scope.selectedGrade.totalQuantityArrived=0;
			$scope.selectedGrade.description=selectedGrade.description;
			$scope.selectedGrade.unitPrice=selectedGrade.price;
			$scope.selectedGrade=selectedGrade;
			$http.get(webContextPath+'/stock/available/'+selectedGrade.gradeId)
			.success(function(data){
				console.log(data);
				if(data.statusCode==200){
					$scope.isresult=true;
					var totalStock=0;
					 $.each(data.object,function(key,val){
							var gradeStockQty={
									stockId:val.stockId,
									warehouseId:parseInt(val.warehouseId),
									warehouseName:val.warehouseName,
									quantity:val.quantity,
									capacity:val.capacity,
									availableSpace:val.availableSpace,
									totalGradeStock:val.totalGradeStock,
									newQuantity:'',
							        qty:0,
							        stockLevel:100
							}
							totalStock=totalStock+gradeStockQty.quantity;
							$scope.selectedGrade.totalStockAvailable=totalStock;
							if(from.toUpperCase()=='ORDER_ENTRY'){
								if(gradeStockQty.quantity==0){
									console.log("No Stock available in warehouse \""+ gradeStockQty.warehouseName+"\"");
								}else{
									$scope.warehouseGradeQty.push(gradeStockQty);
								}
							}
							else{
								$scope.warehouseGradeQty.push(gradeStockQty);
							}
					 });
							
						$scope.selectedGrade.productInfo=$scope.selectedProduct;
						$scope.selectedGrade.vendorInfo=$scope.selectedVendor;
						$scope.selectedGrade.warehouseBreakups=$scope.warehouseGradeQty;
						console.log($scope.warehouseGradeQty);
				}
				
				//$scope.gradeStockAvailable=data;
			  });
			}else{
				console.log("Grade Detail Not selected");
			}
				
		}
			
	
/*	$scope.getGradeStockDetails=function(selectedGrade){
		console.log(selectedGrade);
		$scope.isgradeSelected=true;
		$scope.warehouseGradeQty=[];
		if($scope.isgradeSelected){
			$scope.selectedGrade.totalQuantityArrived=0;
			$scope.selectedGrade.detail=selectedGrade.gradeDetails;
			$scope.selectedGrade.unitPrice=selectedGrade.price;
			$scope.selectedGrade.vat=selectedGrade.vat;
			$http.get(webContextPath+'/stock/available/'+selectedGrade.gradeId)
			.success(function(data){
				console.log(data);
				if(data.statusCode==200){
					$scope.isresult=true;
						for(key in data.object){
							var gradeStockQty={
									stockId:data.object[key].stockId,
									warehouseid:data.object[key].warehouseId,
									warehouseName:data.object[key].warehouseName,
									quantity:data.object[key].quantity,
									capacity:data.object[key].capacity,
									availableSpace:data.object[key].availableSpace,
									totalGradeStock:data.object[key].totalGradeStock,
									newQuantity:'',
									stockLevel:100
							}
							$scope.selectedGrade.totalStockAvailable=gradeStockQty.totalGradeStock;
							$scope.warehouseGradeQty.push(gradeStockQty);
						}
				 	console.log($scope.warehouseGradeQty);
				}
				
				//$scope.gradeStockAvailable=data;
			});
				
		}
			
	}*/
	
	$scope.saveGradeQuantity=function(warehouses,gradeSelected){
		var wareHouseEntries=[];
		var gradeWarehouseData={};
		var isValid=$scope.validateTotalGrade(gradeSelected);
		if(isValid){
			$.each(warehouses,function(key,val){
				console.log(val.warehouseName+":"+ val.newQuantity);
					var quanityData={
						 stockId:val.stockId,
						 warehouseid:val.warehouseId,
					     quantity:val.newQuantity
					}
					wareHouseEntries.push(quanityData);
			});
				gradeWarehouseData={
						gradeId:$scope.selectedGrade.gradeId,
						totalQuantity:gradeSelected.totalQuantityArrived,
						warehouseBreakup:wareHouseEntries,
				};
				
				console.log("gradeWarehouseData : " + JSON.stringify(gradeWarehouseData));
				$http.post(webContextPath+'/stock/entry', JSON.stringify(gradeWarehouseData))
				.success(function(data){
						console.log(data);
						if(data.statusCode==200){
							$scope.successMessage=data.message;
							 $('#success-alert').show();
							 $('#success-alert').alert();
				             $("#success-alert").fadeIn(2000, 500);
				             $('#danger-alert').hide();
				             $scope.getAvailableStock(gradeSelected,'STOCK_ENTRY');
						}else{
							$scope.errorMessage=data.message; 
							$('#danger-alert').show();
							 $('#danger-alert').alert();
				             $("#danger-alert").fadeIn(2000, 500);
				             $('#success-alert').hide();
						}
				});
		}else{
			 $('#danger-alert').show();
			 $('#danger-alert').alert();
             $("#danger-alert").fadeIn(2000, 500);
             $('#success-alert').hide();
		}
	}
	
	$scope.isValidQuantity=false;
	$scope.totalQuantity;
	$scope.validateTotalGrade=function(selectedGrade){
		var isValid=true;
		
		 if(selectedGrade.newQuantity > $scope.selectedGrade.totalQuantityArrived){
			isValid=false;
			 $scope.errorMessage="Stock quantity should not be greater than quantity arrived"; 
		}else{
			$scope.totalQuantity=0;
			$.each($scope.warehouseGradeQty,function(key,val){
				$scope.totalQuantity=$scope.totalQuantity+parseInt(val.newQuantity);
			 });
			if($scope.totalQuantity != $scope.selectedGrade.totalQuantityArrived){
				isValid=false;
				$scope.errorMessage="Stock quantity should not be greater than quantity arrived"; 
			}
		}
		 return isValid;
	}
	
	$scope.addGrade = function() {
		var selProduct=$scope.productVendor.selectedProduct.productId;
		var selVendor=$scope.productVendor.selectedVendor.vendorId;
		  var grade={
		     gradeId:null,
		     description:'',
			 length:'',
			 width:'',
			 vat:'',
			 price:''
		  }
		$scope.newgrades.push(grade);
		/*$http.get(webContextPath+'/vendor/validateproduct/'+selVendor+'/'+selProduct)
		.success(
				function(data,status) {
					console.log(status)
					if(data.statusCode==200){
						$scope.isproductVendor=true;
						$scope.gradesinserted = {
								 gradeId: null,		
								 gradeName: '',
								 thickness:'',
								 thicknessUnit:'',
								 length:'',
								 width:'',
								 sizeUnit:'',
								 price:'',
								 isNew: true,
								 vat:'',
								 description:''	 
							    };
							 $scope.newgrades.push($scope.gradesinserted);
						
					}else if(data.statusCode==500){
						 $('#danger-alert-grade').alert();
			             $("#danger-alert-grade").fadeTo(4000, 500);
					}
				});*/
   };
   
   		$scope.orderItems=[];
   		$scope.hardwareItems=[];
		$scope.grandTotal=0;
		$scope.duplicateitem=false;
		$scope.clonedOrder={};
	   $scope.addOrderItem=function(orderProduct,mode, category){
		   console.log(orderProduct, category);
		   if(category.toUpperCase()=='VENDORCATEGORY'){
			var itemExists=false;
			if(mode.toUpperCase()=='ADD'){
				$.each($scope.orderItems,function(key,val){
					if(orderProduct.gradeId==val.gradeId){
						itemExists=true;
						$scope.duplicateitem=true;
						return false;
					}else{
						itemExists=false;
						$scope.duplicateitem=false;
					}
				});
			}else if(mode.toUpperCase()=='EDIT'){
				//$scope.orderItems=$scope.clonedOrder.itemList;
				$.each($scope.clonedOrder.itemList,function(key,val){
					if(orderProduct.gradeId==val.gradeId){
						itemExists=true;
						$scope.duplicateitem=true;
						return false;
					}else{
						itemExists=false;
						$scope.duplicateitem=false;
					}
				});
			}
			
			
			
			if(!itemExists){
				var totalSizeSqmtr=0.0;
				var orderItem={};
				if(orderProduct.sizeUnit=="MTR"){
					totalSizeSqmtr=orderProduct.length * orderProduct.width;
				}else if(orderProduct.sizeUnit=="FT"){
					
				}
				 orderItem = {
							gradeId : orderProduct.gradeId,
						//	gradeDetail:selectedGrade.productInfo.productName +"-"+ selectedGrade.vendorInfo.name + "-"+selectedGrade.gradeDetails,
							quantity : 0,
							unitPrice : orderProduct.price,
							vat:orderProduct.vat,
							length:orderProduct.length,
							width:orderProduct.width,
							sizeUnit:orderProduct.sizeUnit,
							totalSqmtr:totalSizeSqmtr.toFixed(2),
						//	isNew : true,
						   // gradeStockInfo: selectedGrade.warehouseBreakups,
							totalPrice: $scope.getItemTotalPrice(orderProduct, mode),
							productInfo:$scope.productVendor.selectedProduct,
							vendorInfo:$scope.productVendor.selectedVendor
						};
				console.log(orderItem);
				if(mode.toUpperCase()=='ADD'){
					orderItem.gradeDetail=$scope.productVendor.selectedProduct.productName +"-"+ $scope.productVendor.selectedVendor.name + "-"+orderProduct.gradeDetails,
					orderItem.isNew=true;
					orderItem.gradeStockInfo=$scope.selectedGrade.warehouseBreakups;
					$scope.orderItems.push(orderItem);
				}else if(mode.toUpperCase()=='EDIT'){
					orderItem.isNew=false;
					orderItem.warehouseBreakups=$scope.selectedGrade.warehouseBreakups;
					orderItem.gradeDetail=$scope.productVendor.selectedProduct.productName +"-"+ $scope.productVendor.selectedVendor.name + "-"+orderProduct.gradeDetails,
					$scope.clonedOrder.itemList.push(orderItem);
				}
				  
			}	
			console.log($scope.orderItems);
		   }else if(category.toUpperCase()=='NONVENDORCATEGORY'){
			  
			   if(mode.toUpperCase()=='ADD'){
					$.each($scope.hardwareItems,function(key,val){
						if(orderProduct.hardwareStockId==val.hardwareStockId){
							itemExists=true;
							$scope.duplicateitem=true;
							return false;
						}else{
							itemExists=false;
							$scope.duplicateitem=false;
						}
					});
					if(!itemExists){
						var totalSizeSqmtr=0.0;
						var hardwareVO={};
						hardwareVO = angular.copy(orderProduct);
						hardwareVO.quantity=0;
						hardwareVO.totalPrice=0.0;
						$scope.hardwareItems.push(hardwareVO);
						console.log($scope.hardwareItems);  
					}
				}else if(mode.toUpperCase()=='EDIT'){
					$.each($scope.clonedOrder.hardwareVOList,function(key,val){
						if(orderProduct.hardwareStockId==val.hardwareStockId){
							itemExists=true;
							$scope.duplicateitem=true;
							return false;
						}else{
							itemExists=false;
							$scope.duplicateitem=false;
						}
					});
					
					if(!itemExists){
						var totalSizeSqmtr=0.0;
						var hardwareVO={};
						hardwareVO = angular.copy(orderProduct);
						hardwareVO.quantity=0;
						hardwareVO.totalPrice=0.0;
						$scope.clonedOrder.hardwareVOList.push(hardwareVO);
						console.log($scope.clonedOrder.hardwareVOList);  
					}
				}
		   }
	   };
	   
	   $scope.filterValue = function($event){
	        if(isNaN(String.fromCharCode($event.keyCode))){
	            $event.preventDefault();
	        }
	};
	   
	   $scope.orderTotalPrice=0.0;
	   
	   $scope.orderAddType='ADD';
	   $scope.orderEditType='EDIT';
	   
	   $scope.getItemTotalPrice=function(item,from){
		   var totalPrice=0.0;		  
		  if(from.toUpperCase()=='ADD'){
			  $.each($scope.orderItems,function(key,val){
				  if(val.gradeId==item.gradeId){
					  var orderItemTotalPrice=val.quantity * val.totalSqmtr * val.unitPrice;
					  totalPrice=orderItemTotalPrice.toFixed(2);
					  val.totalPrice=totalPrice;
					  $scope.getGrandTotal('ADD');
					  return false;
				  }
			  });
		  }else if(from.toUpperCase()=='EDIT'){
			  $.each($scope.clonedOrder.itemList,function(key,val){
				  if(val.gradeId==item.gradeId){
					  var orderItemTotalPrice=val.quantity * val.totalSqmtr * val.unitPrice;
					  totalPrice=orderItemTotalPrice.toFixed(2);
					  val.totalPrice=totalPrice;
					  $scope.getGrandTotal('EDIT');
					  return false;
				  }
			  });
			 
		  }
		   return totalPrice;
	   };
	   
	   $scope.getHardwareTotalPrice=function(item,from){
		   var totalPrice=0.0;
		   if(from.toUpperCase()=='ADD'){
				  $.each($scope.hardwareItems,function(key,val){
					  if(val.hardwareStockId==item.hardwareStockId){
						  var hardwareItemTotalPrice=parseInt(val.quantity) *  val.unitPrice;
						  totalPrice=hardwareItemTotalPrice.toFixed(2);
						  val.totalPrice=totalPrice;
						  $scope.getHardwareGrandTotal('ADD');
						  return false;
					  }
					  
				  });
			  }else if(from.toUpperCase()=='EDIT'){
				  $.each($scope.clonedOrder.hardwareVOList,function(key,val){
					  if(val.hardwareStockId==item.hardwareStockId){
						  var hardwareItemTotalPrice=parseInt(val.quantity) *  val.unitPrice;
						  totalPrice=hardwareItemTotalPrice.toFixed(2);
						  val.totalPrice=totalPrice;
						  $scope.getHardwareGrandTotal('EDIT');
						  return false;
					  }
				  });
				 
			  }
			   return totalPrice;
	   }
	 
	  
	   $scope.removeOrderItem=function(selectedGrade){
		  console.log($scope.orderItems);
		  $.each($scope.orderItems,function(key,val){
			  if(val.gradeId==selectedGrade.gradeId){
				  var index = $scope.orderItems.indexOf(selectedGrade);
			  	  $scope.orderItems.splice(index, 1);
			  	  return false;
		  		}  
		     });
	   }
	   
	   $scope.removeOrderHardwareItem=function(selectedHardware){
			  console.log($scope.hardwareItems);
			  $.each($scope.hardwareItems,function(key,val){
				  if(val.hardwareStockId==selectedHardware.hardwareStockId){
					  var index = $scope.hardwareItems.indexOf(selectedHardware);
				  	  $scope.hardwareItems.splice(index, 1);
				  	  return false;
			  		}  
			     });
		   }
	   
	   $scope.getGrandTotal = function(from){
		    var grandTotal = 0.0;
		    var totalPrice=0.0;
		    var itemList=[];
		    if(from=='ADD'){
		    	itemList=$scope.orderItems;
		    	 $.each($scope.orderItems,function(key,val){
			    	  var orderItemTotalPrice=val.quantity * val.totalSqmtr * val.unitPrice;
					  totalPrice=orderItemTotalPrice.toFixed(2);
					  grandTotal=parseFloat(grandTotal)+parseFloat(totalPrice);
				  });
		    	 
		    	 $scope.getTotalOrderAmount();
		    }
			  
		    else if(from=='EDIT'){
		    	itemList=$scope.clonedOrder.itemList;
		    	 $.each(itemList,function(key,val){
			    	  var orderItemTotalPrice=val.quantity * val.totalSqmtr * val.unitPrice;
					  totalPrice=orderItemTotalPrice.toFixed(2);
					  grandTotal=parseFloat(grandTotal)+parseFloat(totalPrice);
				  });
			    $scope.clonedOrder.orderAmount=grandTotal;
		    }
		   
		    $scope.grandTotal=parseFloat(grandTotal);
		  return $scope.grandTotal;
		}
	   
	   $scope.hardwareGrandTotal=0.0;
	   $scope.getHardwareGrandTotal = function(from){
		    var grandTotal = 0.0;
		    var totalPrice=0.0;
		    var itemList=[];
		    if(from.toUpperCase()=='ADD'){
		    	itemList=$scope.hardwareItems;
		    	 $.each($scope.hardwareItems,function(key,val){
			    	  var hardwareItemTotalPrice=parseInt(val.quantity) * val.unitPrice;
					  totalPrice=hardwareItemTotalPrice.toFixed(2);
					  grandTotal=parseFloat(grandTotal)+parseFloat(totalPrice);
				  });
		    	 $scope.getTotalOrderAmount();
		    }
			  
		    else if(from.toUpperCase()=='EDIT'){
		    	 $.each($scope.clonedOrder.hardwareVOList,function(key,val){
			    	  var hardwareItemTotalPrice=parseInt(val.quantity) * val.unitPrice;
					  totalPrice=hardwareItemTotalPrice.toFixed(2);
					  grandTotal=parseFloat(grandTotal)+parseFloat(totalPrice);
				  });
		    	 $scope.getTotalOrderAmount();
		    }
		   
		    $scope.hardwareGrandTotal=parseFloat(grandTotal).toFixed(2);
		    return $scope.hardwareGrandTotal;
		}
	   
	   
	   $scope.totalOrderAmount=0.0;
	   $scope.getTotalOrderAmount=function(){
		   var totalOrderAmt=0.0;
		   $scope.totalOrderAmount=parseFloat($scope.hardwareGrandTotal)+parseFloat($scope.grandTotal);
		   totalOrderAmt= $scope.totalOrderAmount;
		   return totalOrderAmt;
	   }
	   
	   	/* $scope.gradeWarehouses=[];
		 $scope.getWarehouseGrades=function(selectedGrade,operation){
			 var warehouseGrades=[];
			 
			  $http.get(webContextPath+'/stock/available/'+selectedGrade.gradeId)
				.success(function(data){
					console.log(data);
				if(operation=='ADD'){	
					if(data.statusCode==200){
						 $scope.gradeWarehouses=[];
						
							$.each(data.object,function(key,val){
								var orderQty=0;
								var gradeStockQty={
										warehouseid:val.warehouseId,
										warehouseName:val.warehouseName,
										quantity:val.quantity,
										qty:orderQty,
										capacity:val.capacity,
										availableSpace:val.availableSpace,
										totalGradeStock:val.totalGradeStock,
										valid:$scope.isValidEntry(orderQty,val.quantity,val),
										
								};
								warehouseGrades.push(gradeStockQty);
								$scope.gradeWarehouses.push(gradeStockQty);
							});
							
						 }
				}
				 else if(operation=='EDIT'){
					 $scope.gradeWarehouses=[];
					 $.each(data.object,function(key,val){
								 $.each(selectedGrade.warehouseBreakups,function(k,v){
									 console.log(v);
									 if(val.warehouseId==v.warehouseid){
										 var gradeStockQty={
													warehouseid:val.warehouseId,
													warehouseName:val.warehouseName,
													quantity:val.quantity,
													qty:v.qty,
													capacity:val.capacity,
													availableSpace:val.availableSpace,
													totalGradeStock:val.totalGradeStock
													//valid:$scope.isValidEntry(orderQty,val.quantity,val),
													
											};
										 	warehouseGrades.push(gradeStockQty);
											$scope.gradeWarehouses.push(gradeStockQty);	 
										 return false;
									  } 									 
									 
								 });
								 
								
							console.log(warehouseGrades);
						});
					 
				 }
				});
	
			  
			  return warehouseGrades;
		 };*/
	 
		 $scope.errMsg="";
		 $scope.qtyMsg="";
		 $scope.gradeTotalQty=[];
		 $scope.isValidEntry=function(entry,max,warehouse){
			 $scope.errMsg="";
			 $scope.qtyMsg="";
			 if(entry>=0 && entry<=max){
				 return true;
			 }else{
				 $scope.errMsg="Please enter a valid quantity  and must be less than available "+ warehouse.warehouseName
				 return false;
			 }
			 
		 }
	   
	  // $scope.isgradeqtyequal=false;
	   
	   $scope.validateStockOrderQty=function(gradeItemInfo,warehouseItem){
		    var totalQuantity=parseInt(gradeItemInfo.quantity);
		  	var gradeTotalQty=0;
		  	var isgradeqtyequal=false;
		  	$scope.warehousebreakups=[];
		  	if(warehouseItem.length>0){
			  	$.each(warehouseItem,function(key,val){
			  		console.log("Available Quantity at  " + val.warehouseName + ": "+ val.quantity);
			  		console.log("Ordered Quantity : " + val.qty);
			  		if(val.qty==0){
			  			return false;
			  		}
			  		else if(parseInt(val.qty)>0 && parseInt(val.qty)<val.quantity){
			  			$scope.warehousebreakups.push(val);
			  		} 
			  	});
			  	console.log($scope.warehousebreakups);	
			  	if($scope.warehousebreakups.length>0){
				 	$.each($scope.warehousebreakups,function(key,val){
				  		gradeTotalQty=gradeTotalQty+parseInt(val.qty);
				  	});
		 		if(totalQuantity==gradeTotalQty){
				   console.log("ok");
				  isgradeqtyequal=true;
		  		}else{
		  			isgradeqtyequal=false;
		  			//$scope.qtyMsg="Grade :"+ gradeItemInfo.value.gradeDetail  +"has ordered quantity : "+ totalQuantity + " and  Total quantity from warehouses:" + gradeTotalQty;
		  		}
			  }
		  	}
		 return isgradeqtyequal;
	   }
	   
	 $scope.errorMsg=""
	 $scope.errorView=false;	 
	$scope.searchCustomer=function(searchValue, type){
		var mobileSearch="";
		var emailSearch="";
		if(type.toUpperCase()=='MOBILE'){
			console.log(searchValue);
			mobileSearch=$.trim(searchValue);
			$http.get(webContextPath+"/customer/search/"+mobileSearch+"/"+type)
			.success(function(data){
				console.log(data);
				if(data.statusCode==200){
					$scope.order.customerVO=data.object;
					if(data.object==null){
						$scope.errorView=true;
						$scope.errorMsg="Customer does not exists";
					 }else{
						 $scope.errorView=false;
							$scope.errorMsg=""; 
					 }
				}
			});
		}else if(type.toUpperCase()=='EMAIL'){
			console.log(searchValue);
			emailSearch=$.trim(searchValue);
			$http.get(webContextPath+"/customer/search/"+emailSearch+"/"+type)
			.success(function(data){
				console.log(data);
				if(data.statusCode==200){
					$scope.order.customerVO=data.object;
					  if(data.object==null){
						$scope.errorView=true;
						$scope.errorMsg="Customer does not exists";
					 }else{
						 $scope.errorView=false;
							$scope.errorMsg=""; 
					 }
				}
			});
		}
	}
	
	$scope.onlyNumbers = /^\d+$/;
	   
	$scope.verifiedOrder={};
	$scope.verifyOrder=function(orderForm){
		console.log(orderForm);
		var inValidParams=[];
		angular.forEach(orderForm, function(value, key) {
			  if(key[0] == '$') return;
			  	console.log(key, value.$valid)
		  		if(value.$valid==false){
		  			value.$invalid=true;
		  			value.$pristine=false;
		  			var validOrderObject={
		  					field:key,
		  					val:value.$modelValue||""
		  			}
		  			inValidParams.push(validOrderObject);
		  		}
			  	return false;
			});
		console.log(inValidParams);
		if(inValidParams.length==0){
			console.log($scope.order);
			console.log($scope.orderItems);
			var orderList=[];
			var hardwareOrderList=[];
			if($scope.orderItems.length>0){
				$.each($scope.orderItems,function(key,val){
					orderList.push({
						itemId:"",
						gradeId:val.gradeId,
						gradeDetail:val.gradeDetail,
					    quantity:val.quantity,
					    unitPrice:val.unitPrice,
					    sizeUnit:val.sizeUnit,
					    length:val.length,
					    width:val.width,
					    vat:val.vat,
					    totalSqmtr:val.totalSqmtr,
					    totalPrice:parseFloat(val.totalPrice),
					    warehouseBreakups:val.gradeStockInfo,
					    isValidTotal:$scope.validateStockOrderQty(val,val.gradeStockInfo),
					    productInfo:val.productInfo,
						vendorInfo:val.vendorInfo
					});
					 
				}); 
			}
			if($scope.hardwareItems.length>0){
				$.each($scope.hardwareItems,function(key,val){
					hardwareOrderList.push({
						itemId:"",
						hardwareStockId:val.hardwareStockId,
						description:val.description,
					    quantity:val.quantity,
					    unitPrice:val.unitPrice,
					    vat:val.vat,
					    totalPrice:parseFloat(val.totalPrice),
					});
					 
				});
			}
			
			if($scope.totalOrderAmount>0){
				var orderDetail={
						//agentVO:$scope.referredAgents.selected,
						customerVO:$scope.order.customerVO,
						addressVO:$scope.order.addressVO,
						itemList:orderList,
						hardwareVOList:hardwareOrderList,
						orderAmount:parseFloat($scope.totalOrderAmount)
						
				}
				
				console.log(orderDetail.itemList);
				console.log(orderDetail.hardwareVOList);
				var checkOrderQuantityEquality=[];
				var quantityErrMsgInfo="";
				if(orderDetail.itemList.length>0){
					$.each(orderDetail.itemList,function(key,val){
						if(val.isValidTotal==false){
							checkOrderQuantityEquality.push(val.gradeDetail);
							quantityErrMsgInfo="Sum of total quantity of warehouses for "+ val.gradeDetail  +" must be equal ";
							console.log("Total Sum of quantity of warehouses for "+ val.gradeDetail  +" must be equal to "+val.quantity );
							return false;
						}
					})
				}
				
			   if(orderDetail.hardwareVOList.length>0){
				   $.each(orderDetail.hardwareVOList,function(key,val){
						if(val.quantity==0){
							checkOrderQuantityEquality.push(val.description);
							quantityErrMsgInfo="Please enter the quantity for hardware item \""+ val.description  +"\"";
							console.log("Please enter the quantity for hardware item \""+ val.description  +"\"");
							return false;
						}
					})
			   }
				  
				  
				 if(checkOrderQuantityEquality.length==0){	
						$scope.verifiedOrder={
								status:200,
								value:orderDetail
						}
					 console.log("Item added for Order validated successfully" );
						//$('#verifyorderbtn').click();
						 console.log(JSON.stringify($scope.verifiedOrder) );
						$http.post(webContextPath+"/order/verify", JSON.stringify($scope.verifiedOrder.value))
						.success(function(data){
							console.log(data);
							if(data.statusCode==200){
								$('#verifyorderbtn').click();
							}else if(data.statusCode==500){
								
							}
						});
				 }else{
					 $scope.verifiedOrder={
								status:412,
								message:quantityErrMsgInfo
						}
				 }	
				//$scope.createOrder($scope.verifiedOrder.value);
				
			}else{
				$scope.verifiedOrder={
						status:404
				}
			}
		}else{
			$scope.verifiedOrder={
					status:500
			}
		}
   }
	
	$scope.finalOrder={};
	
	
	$scope.orderNumber=null;	
	
	$scope.createOrder=function(){
		if($scope.verifiedOrder.status==200){
			$http.post(webContextPath+"/order/confirm", JSON.stringify($scope.verifiedOrder.value))
			.success(function(data){
				$scope.orderNumber=null;
				console.log(data);
				if(data.statusCode==200){
					$scope.orderNumber=data.object.orderNumber;
				}else if(data.statusCode==500){
					
				}
			});
		}else{
			
		}
	};	
	
	
	$scope.editedOrder={};
	
	$scope.searchOrder=function(){
		console.log($scope.finalOrder);
	
		//var orderData=OrderSearchService.searchOrder($scope.finalOrder.number)
		$scope.totalOrderItems=0;
		$http.get(webContextPath+"/order/search/"+$scope.finalOrder.number)
		.success(function(data){
			console.log(data);
			$scope.selectedProduct={};
			$scope.selectedVendor={};
			if(data.statusCode==200){
				$scope.editedOrder=data.object;
				$scope.editedOrder.orderAmount=data.object.orderAmount;
				$scope.clonedOrder=angular.copy($scope.editedOrder);
				$scope.totalOrderItems=$scope.totalOrderItems+$scope.clonedOrder.itemList.length+$scope.clonedOrder.hardwareVOList.length;
			}else{
				
			}
		/*	var orders=[];
			if(data.statusCode==200){
				var customerDetails=data.object.customerVO.firstName +" "+data.object.customerVO.lastName
				var orderData={
						orderNumber:data.object.orderNumber,
						customer:customerDetails,
						details:data.object.itemList,
						hardwareVOList:data.object.hardwareVOList,
						amount:parseFloat(data.object.orderAmount),
						status:data.object.status
				}
				orders.push(orderData);
				//$scope.loadOrderDataTable(orders);
			}else {
			}*/
		});
  };	
	
	
  $scope.loadOrderDataTable=function(orderData){
		 $('#ordersInfoTable').DataTable({
		       "aaData": orderData,
		        "aoColumns": [
		            { "mData": "orderNumber" },
		            { "mData": "customer" },
		            { "mData": "amount" },
		            { "mData": "status" }
		          ]
	    });
	 }
		
	$scope.editOrder=function(){
		//$scope.order=$scope.editedOrder;
		$scope.selectedProduct=$scope.editedOrder.productVO;
		$scope.selectedVendor=$scope.editedOrder.vendorVO;
		$scope.orderItems=[];
	    $.each($scope.editedOrder.itemList,function(key,val){
	    	console.log(val);
	    	/*var orderGradeDetails={
	    			gradeId : val.gradeId,
					gradeDetail : val.productVO.productName +"-"+ val.vendorVO.name + "-"+val.gradeDetail,
					quantity : val.quantity,
					unitPrice : val.unitPrice,
					length:val.length,
					width:val.width,
					sizeUnit:val.sizeUnit,
					totalSqmtr:val.totalSqmtr,
					totalPrice:parseFloat(val.totalPrice),
					warehouseBreakups:val.warehouseBreakups,
	    	}*/
	    	
	    	
	    	var editedOrderItem = {
					gradeId : val.gradeId,
					gradeDetail : val.gradeDetail,
					quantity : val.quantity,
					unitPrice : val.unitPrice,
					length:val.length,
					width:val.width,
					sizeUnit:val.sizeUnit,
					totalSqmtr:totalSizeSqmtr,
					vat:val.vat,
					totalPrice:parseFloat(val.orderAmount),
					isExisiting : true,
					gradeStockInfo: val.warehouseBreakups,
					isValidTotal:$scope.validateStockEditOrderQty(val)
				};

		console.log(editedOrderItem);
		$scope.editedOrderItems.push(editedOrderItem);
	    	//$scope.editOrderItem(orderGradeDetails);
	    });
		$('#editorderpage').click();
	}
	
	$scope.getEditedQuanitybreakups=function(warehouses){
		var quantitybreakUps=[];
		$.each(warehouses,function(key,val){
			quantitybreakUps.push({
				warehouseid:val.warehouseid,
				qty:val.quantity,
			});
		});
		return quantitybreakUps;
	};
	
/*	  $scope.editOrderItem=function(selectedGrade){
		   console.log(selectedGrade);
			var itemExists=false;
			
			$.each($scope.editedOrderItems,function(key,val){
				if(selectedGrade.gradeId==val.gradeId){
					itemExists=true;
					$scope.duplicateitem=true;
					return false;
				}else{
					itemExists=false;
					$scope.duplicateitem=false;
				}
			});
			
			if(!itemExists){
				var totalSizeSqmtr=0.0;
				var orderItem={};
				if(selectedGrade.sizeUnit=="MTR"){
					totalSizeSqmtr=selectedGrade.length * selectedGrade.width;
				}
				 orderItem = {
							gradeId : selectedGrade.gradeId,
							gradeDetail : selectedGrade.gradeDetail,
							quantity : selectedGrade.quantity,
							unitPrice : selectedGrade.unitPrice,
							length:selectedGrade.length,
							width:selectedGrade.width,
							sizeUnit:selectedGrade.sizeUnit,
							totalSqmtr:totalSizeSqmtr,
							totalPrice:parseFloat(selectedGrade.totalPrice),
							isExisiting : true,
							gradeStockInfo: selectedGrade.warehouseBreakups,
							isValidTotal:$scope.validateStockEditOrderQty(selectedGrade)
						};

				console.log(orderItem);
				$scope.editedOrderItems.push(orderItem);
			}	
			console.log($scope.editedOrderItems);
	   };*/
	
	$scope.verifyEditedOrder=function(editedOrder,index){
		console.log(editedOrder);
		var validateItems=[];
		var isValidQty=false;
		$scope.verifiedEditOrder={}
		$.each(editedOrder.itemList,function(key,val){
			isValidQty=$scope.validateStockEditOrderQty(val,val.warehouseBreakups);
			 console.log("validated : " +isValidQty);
			 if(!isValidQty){
				 $scope.verifiedEditOrder.status=500;
				 $scope.verifiedEditOrder.index=index;
				 return false;
			 }
		});
		if(isValidQty){
			$scope.verifiedEditOrder.status=200;
			$('#closeorderbtn').click();
			$('#editedPreForm').modal('show');
		}
	}   
	
	$scope.updateOrder=function(clonedOrder){
		if($scope.verifiedEditOrder.status==200){
			$http.post(webContextPath+"/order/update", JSON.stringify(clonedOrder))
			.success(function(data){
				console.log(data);
				if(data.statusCode==200){
					$scope.verifiedEditOrder.isUpdated='YES';
					$scope.verifiedEditOrder.orderNumber=clonedOrder.orderNumber;
				}else{
					$scope.verifiedEditOrder.isUpdated='NO';
				}
			});
		}else{
			
		}
	};	
	
	
	$scope.deleteEditedItem=function(editedOrder,item){
		 var index = $scope.clonedOrder.itemList.indexOf(item);
		 console.log(index);
		  $scope.clonedOrder.itemList.splice(index, 1);   
		  if($scope.clonedOrder.itemList.length==0){
			  
		  }
	}
	   
	$scope.validateStockEditOrderQty=function(orderedGrade,warehouseList){
		var totalQuantity=orderedGrade.quantity;
		var gradeTotalQty=0;
		var isgradeqtyequal=false;	
		 if(warehouseList.length>0){
			 	$.each(warehouseList,function(key,val){
			  		gradeTotalQty=gradeTotalQty+parseInt(val.qty);
			  	});
		 		if(totalQuantity==gradeTotalQty){
				   console.log("ok");
				  isgradeqtyequal=true;
		  		}else{
		  		 isgradeqtyequal=false;
		  		}
		  }
	 return isgradeqtyequal;
	}
	   

	$scope.getQuanitybreakups=function(warehouseInfo){
		var quantitybreakUps=[];
		//$scope.validateStockOrderQty(gardeItemInfo,warehouseInfo);
		$.each(warehouseInfo,function(key,val){
			quantitybreakUps.push({
				warehouseId:val.warehouseid,
				qty:val.qty,
			});
		});
		return quantitybreakUps;
	}
   
   $scope.selectedVendor="";
   $scope.updateGrade=function(vendor){
		$scope.selectedVendor=vendor;
	}
    $scope.gradeVos;
    $scope.successMessage="";
    $scope.statusCode=0;
   	  $scope.saveGrades = function(data, id) {
   		  console.log("Saving Data : "+ JSON.stringify(data));
   		  
   	 	  var isValid=true;
	   	  for(key in data){
	   		  if(data[key] == ""){
	   			$scope.statusMessage="Grade details should not be empty.    ";
	   			$scope.statusCode=500;
	   			$('#gradedanger-alert').show();
	   			$('#gradesuccess-alert').hide();
	   			isValid=false;
	   			$scope.statusMessage= key +" should not be empty.    ";
	   			return false;
	   		  }
	   	  }
	   	  if(isValid){
	   		$scope.gradeVos={
	   			 vendorVO :$scope.productVendor.selectedVendor,
	   			 productVO:$scope.productVendor.selectedProduct,
	   			 gradeId:id,
	   			 description:data.description,
	   			 length:data.length,
	   			 width:data.width,
	   			 vat:data.vat,
	   			 price:data.price,
	   		  };
	   	  console.log($scope.gradeVos);
	  
	   	
		  $http.post(webContextPath+'/grades/vendor/save', $scope.gradeVos)
		   .success(function(data,status){
			   console.log(data);
			   console.log(status);
			   if(data.statusCode==200){
				   $scope.statusCode=data.statusCode;
				   $scope.statusMessage=data.message;
	        		   $('#gradedanger-alert').hide();
			   			$('#gradedanger-alert').alert();
			   			$('#gradesuccess-alert').show();
	        	    $scope.reset();
			   }else{
				   $scope.statusCode=data.statusCode;
				   $scope.statusMessage=data.message;
				   $('#gradedanger-alert').show();
		   			$('#gradedanger-alert').alert();
		   			$('#gradesuccess-alert').hide();
			   }
			  // $scope.showVendorDetail($scope.vendor);
		   });
	   	  }
	  };
	  
	  	  $scope.removeGrade = function(index, gradeId) {
	  		  
	  		  if(gradeId==null){
	  			$scope.newgrades.splice(index, 1);
	  		  }
	  		  else{
		  		 $http.get(webContextPath+'/grades/delete/'+gradeId)
		  		 .success(function(data){
		  			 if(data.statusCode==200){
		  				$scope.statusCode=data.statusCode;
		  				$scope.newgrades.splice(index, 1);
		  				$scope.statusMessage=data.message;
		  				 $('#successp-alert').alert();
		         		 $("#successp-alert").fadeTo(2000, 500).slideUp(500, function(){
		         		}); 
		  			 }else{
		  				 $scope.statusCode=data.statusCode;
		  				 $scope.statusMessage=data.message;
		  			 }
		  			 
		  		 });
	  		  }
	  			 
		  };
		  
		  $scope.vendorGradeStock=[];
		  
		  $scope.addGradeStock=function(gradeQuantity){
			  console.log($scope.selectedGrade, gradeQuantity);
			     var gradeStock={
			    		 gradeId:$scope.selectedGrade.value.gradeId,
			    		 quantity:gradeQuantity,
			    		 gradeDetail:$scope.selectedGrade.value.gradeDetails
			     } 
			     $scope.createGradeStockList(gradeStock);
		  }
		  
		  $scope.createGradeStockList=function(gradeData){
			  console.log(gradeData);
			  $.ajax({
				   	url:webContextPath+'/stock/grade/quantity/add',
				   	data:JSON.stringify(gradeData),
				   	type:'post',
				   	dataType:"json",
				   	headers: { 
				        'Accept': 'application/json',
				        'Content-Type': 'application/json' 
				    },
				   	success:function(data){
				   		console.log(data);
				   	}
			   });
		  }
		  
		  
		  
		  $scope.saveStockDetail=function(){
			  $scope.vendorGradeStock=[];
			  $.each($scope.vendorGradeStock, function(key,val){
				  var stockData={
						   gradeId:val.gradeId,
						   quantity:val.quantity
				   };
				  $scope.vendorGradeStock.push(stockData);
			  });
			  
			   
			   $.ajax({
				   	url:webContextPath+'/stock/grade/update',
				   	data:JSON.stringify($scope.vendorGradeStock) ,
				   	type:'post',
				   	dataType:"json",
				   	headers: { 
				        'Accept': 'application/json',
				        'Content-Type': 'application/json' 
				    },
				   	success:function(data){
				   		console.log(data);
				   	}
			   });
		  }
		  
	  
		  
}]);

