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
	 $scope.errMsg="";
	 $scope.qtyMsg="";
	$scope.productVendor={
			selectedProduct:'',
			selectedVendor:'',
			selectedWarehouse:''	
	};
	
	$scope.hardwareUnits=[
		      {	unit:'KG',value: "Kilogram"},
		      { unit:"PCS",value:"Pieces"},
		      { unit:"LTR",value:"Litre"},
		      { unit:"PKT", value:"Packect"}
	      ]
	
	$scope.orderItem={};
	
	$scope.allItems = [];
	$scope.hardwareproducts=[];
	angular.element(document).ready(function () {
		$scope.getAllProducts();
		$scope.getAllAgentTypes();
		$scope.order={};
		$scope.hardwareProduct={};
		$scope.emptyHardware={};
		//$scope.getAllHardwares();	
		$scope.selectedHardwareUnit;
		//$scope.getAllGrades();
		
		$scope.populateAllItems();	
		
		 $('#purchaseOrderInvoice').change( function(e) {
             var ext=$('input#purchaseOrderInvoice').val().split(".").pop().toLowerCase();
           // alert($.inArray(ext, ["csv","json"]));
             if($.inArray(ext, ["pdf","jpg","jpeg","JPEG", "JPG","png","PNG","PDF"]) == -1) {
                return false;
                }
             else if (e.target.files != undefined) {
                $scope.isfileselected=true;
                file = $('#purchaseOrderInvoice').prop('files');
                if (file.length > 0) {
                    var fileToLoad = file[0];
                    var fileReader = new FileReader();
                    fileReader.onload = function(fileLoadedEvent) {
                      var srcData = fileLoadedEvent.target.result; // <--- data: base64
                      console.log(srcData);
                      $scope.imageData = srcData;
                      $scope.fileType=ext;
                      /*var newImage = document.createElement('img');
                      newImage.src = srcData;
                      console.log(srcData);
                      $("#imgTest").html(newImage.outerHTML);
                      $("#imgTest img").attr("style","width:100%")*/
                    }
                    fileReader.readAsDataURL(fileToLoad);
                  }
                
              }
               
         });
    });
	

	
	
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
	
	

	
   
    $scope.gradeItems=[];
	
	$scope.selectedItem={};
	$scope.populateAllItems=function(){
		$scope.getAllGrades();
		$scope.getAllHardwares();
	}
	
	$scope.getAllGrades=function(){
		$http.get(webContextPath+"/grades/list").success(
		function(data) {
			console.log(data);
			if(data.statusCode==200){
				 $scope.gradeItems=[];
				$.each(data.object,function(key,val){
					 var orderItem={
							  gradeId:val.gradeId,
							  item:val,
							  description:val.description,
							  category:'VENDOR',
							  availableStock:val.totalAvailableStock,
							  vendorId:val.vendorId,
							  productId:val.productId
							  
					  }
					$scope.allItems.push(orderItem);
				});
			}else{
				
			}
		});
	}
	
	$scope.getAllHardwares=function(){
		$http.get(webContextPath+"/hardware/list").success(
		function(data) {
			console.log(data);
			if(data.statusCode==200){
				$scope.hardwareproducts=[];
				$.each(data.object,function(key,val){
					$scope.hardwareproducts.push(val);
				});
				
				$.each($scope.hardwareproducts,function(key,val){
					  var orderItem={
							  hardwareStockId:val.hardwareStockId,
							  item:val,
							  description:val.description,
							  category:'HARDWARE',
							  availableStock:val.totalUnits
							  
					  }
					$scope.allItems.push(orderItem);
				});
			}else{
				
			}
		});
	}
	
	$scope.purchaseItem={};
	$scope.vendorPurchaseItemList =[];
	$scope.hardwarePurchaseItemList = [];
	$scope.focusIn=function(){
		
	}
	
	$scope.focusOut-function(selectedItem){
		console.log(selectedItem);
	}
	
	$scope.getVendorInfo=function(selectedVendor){
		$scope.purchaseItem.selectedVendor = selectedVendor;
	}
	
	$scope.selectedItem=function(selected){
		 if (selected) {
		       console.log(selected)
		       $scope.selectedItem.originalObject=selected.originalObject
		       $scope.purchaseItem.selectedItem = selected.originalObject;
		      } else {
		        console.log('cleared');
		      }
	}
	
	$scope.addPurchaseItems = function(purchaseOrderForm){
			console.log(purchaseOrderForm);
			var inValidParams=[];
			angular.forEach(purchaseOrderForm, function(value, key) {
				  if(key[0] == '$') return;
				  	console.log(key, value.$valid)
			  		if(value.$valid==false){
			  			value.$invalid=true;
			  			value.$pristine=false;
			  			var validPurchaseObject={
			  					field:key,
			  					val:value.$modelValue||""
			  			}
			  			inValidParams.push(validPurchaseObject);
			  		}
				  	return false;
				});
		 
			console.log(inValidParams);
			if(inValidParams.length==0){
				var validPurchaseObject=angular.copy($scope.purchaseItem);
				console.log(validPurchaseObject);
				if(validPurchaseObject.selectedItem !=undefined){
					if(validPurchaseObject.selectedItem.category.toUpperCase() == 'HARDWARE'){
						if($scope.hardwarePurchaseItemList.length == 0){
							var hardwareObject = {
									hardwareStockId:validPurchaseObject.selectedItem.hardwareStockId,
									description:validPurchaseObject.selectedItem.description,
									category:'HARDWARE',
									quantity:0,
									totalUnits:0,
									unitPrice:0,
									totalPrice:0
							}
							$scope.hardwarePurchaseItemList.push(hardwareObject);
						}else{
							var isHardwareObjectExists=false;
							$.each($scope.hardwarePurchaseItemList,function(key,val){
								if(val.hardwareStockId == validPurchaseObject.selectedItem.hardwareStockId){
									isHardwareObjectExists =true;
									return false;
								}
							});
							if(!isHardwareObjectExists){
								var hardwareObject = {
										hardwareStockId:validPurchaseObject.selectedItem.hardwareStockId,
										description:validPurchaseObject.selectedItem.description,
										category:'HARDWARE',
										quantity:0,
										unitCostPrice:0,
										totalCostPrice:0
								}
								$scope.hardwarePurchaseItemList.push(hardwareObject);
							}
						}
					}
					
					if(validPurchaseObject.selectedItem.category.toUpperCase() == 'VENDOR'){
						if($scope.vendorPurchaseItemList.length == 0){
							var vendorPurchaseObject = {
									gradeId:validPurchaseObject.selectedItem.gradeId,
									description:validPurchaseObject.selectedItem.description,
									vendorId:validPurchaseObject.selectedItem.vendorId,
									productId:validPurchaseObject.selectedItem.productId,
									length:0,
									width:0,
									quantity:0,
									totalSQMtr:0,
									unitCostPrice:0,
									totalCostPrice:0,
									category:'VENDOR',
									
							}
							$scope.vendorPurchaseItemList.push(vendorPurchaseObject);
						}else{
							var isVendorObjectExists =false;
							$.each($scope.vendorPurchaseItemList,function(key,val){
								if(val.gradeId == validPurchaseObject.selectedItem.gradeId){
									isVendorObjectExists = true;
									return false;
								}
							});
							
							if(!isVendorObjectExists){
								var vendorPurchaseObject = {
										gradeId:validPurchaseObject.selectedItem.gradeId,
										description:validPurchaseObject.selectedItem.description,
										length:0,
										width:0,
										quantity:0,
										totalSQMtr:0,
										unitCostPrice:0,
										totalCostPrice:0,
										category:'VENDOR'
								}
								$scope.vendorPurchaseItemList.push(vendorPurchaseObject);
							}
						}
					}
				}
				
				$('#ex7_value').val('');
				
			}
	}
	
	$scope.previewPurchaseOrderDetails=function(){
		
		var hardwareInvalid= false;
		var hardwareItemName="";
		$scope.hardwareItemId=null;
		$scope.vendorItemId=null;
		var vendorInvalid=false;
		var vendorProductName="";
		$scope.hardwareOrderError="";
		$scope.vendorOrderError="";
		$scope.purchaseOrderError="";
		
		$scope.totalVendorQuantityItems=0;
		$scope.totalHardwareQtyItems=0;
		$scope.totalQuantitySQMtr=0;
		$scope.totalPurchaseOrderAmount=0;
		
		if($scope.hardwarePurchaseItemList.length > 0){
			$.each($scope.hardwarePurchaseItemList,function(key,val){
				if(val.quantity == 0 && val.unitCostPrice == 0 && val.totalCostPrice == 0){
					hardwareInvalid = true;
					hardwareItemName = val.description;
					$scope.hardwareItemId = val.hardwareStockId;
					return false;
				}
				var calculatedtotalPrice = parseFloat(val.quantity) *  parseFloat(val.unitCostPrice);
				if(calculatedtotalPrice != parseFloat(val.totalCostPrice)){
					hardwareInvalid = true;
					hardwareItemName = val.description;
					$scope.hardwareItemId = val.hardwareStockId;
					return false;
				}
				else{
					$scope.totalPurchaseOrderAmount=$scope.totalPurchaseOrderAmount+calculatedtotalPrice;
				}
			});
			
			if(hardwareInvalid){
				hardwareInvalid = true;
				$scope.hardwareOrderError = "500" ;
			}else{
				hardwareInvalid = false;
			}
		}
		
		if($scope.vendorPurchaseItemList.length > 0){
			 var total
			$.each($scope.vendorPurchaseItemList,function(key,val){
				if(val.quantity == 0 && val.unitCostPrice == 0 && val.totalSQMtr == 0 && val.totalCostPrice == 0){
					vendorInvalid = true;
					vendorProductName = val.description;
					$scope.vendorItemId = val.gradeId;
					return false;
				}
				
				var calculatedtotalPrice = parseFloat(val.totalSQMtr) *  parseFloat(val.unitCostPrice);
				if(calculatedtotalPrice != parseFloat(val.totalCostPrice)){
					vendorInvalid = true;
					vendorProductName = val.description;
					$scope.vendorItemId = val.gradeId;
					return false;
				}else{
					$scope.totalPurchaseOrderAmount=$scope.totalPurchaseOrderAmount+calculatedtotalPrice;
				}
			});
			
			if(vendorInvalid){
				vendorInvalid = true;
				$scope.vendorOrderError = "500";
			}else{
				vendorInvalid = false;
			}
		}
		
		if(!hardwareInvalid && !vendorInvalid){
			$('#purchase-order-error-alert').hide();
			var orderDate=$('#orderdatepicker').val();
			var invoiceDate=$("#invoicedatepicker").val();
			
			var vendorPurchaseItems = 0;
			var hardwarePurchaseitems = 0;
			var totalVendorSQMtr = 0 ;
			var purhcaseVendorTotalPrice = 0;
			var hardwareTotalPrice = 0;
			$.each($scope.vendorPurchaseItemList,function(key,val){
				vendorPurchaseItems = vendorPurchaseItems + parseFloat(val.quantity);
				totalVendorSQMtr = totalVendorSQMtr  + parseFloat(val.totalSQMtr);
			});
			$scope.totalVendorQuantityItems = vendorPurchaseItems;
			$scope.totalQuantitySQMtr = totalVendorSQMtr;
			
			$.each($scope.hardwarePurchaseItemList,function(key,val){
				hardwarePurchaseitems =  hardwarePurchaseitems  + parseFloat(val.quantity);
			});

			$scope.totalHardwareQtyItems =  hardwarePurchaseitems;
			
			var purchaseOrderDetails={
					purchaseOrderNumber:$scope.purchaseItem.orderNumber,
					purchaseInvoiceNumber:$scope.purchaseItem.invoiceNumber,
					orderDate:orderDate,
					invoiceDate:invoiceDate,
					vendorItems:$scope.vendorPurchaseItemList,
					totalQuantitySQMtr:$scope.totalQuantitySQMtr,
					category1:'VENDOR',
					category2:'HARWARE',
					hardwareItems:$scope.hardwarePurchaseItemList,
					vendorId:$scope.purchaseItem.selectedVendor.vendorId,
					totalVendorItems:$scope.totalVendorQuantityItems,
					totalHardwarItems:$scope.totalHardwareQtyItems,
					purchaseOrderAmount:$scope.totalPurchaseOrderAmount,
					purchaseOrderImage:$scope.imageData,
					imageType:$scope.fileType
			}
			
			$scope.verfiedPurchaseDetails = angular.copy(purchaseOrderDetails);
		console.log(purchaseOrderDetails);
			/*$http.post(webContextPath+"/purchase/order/save", purchaseOrderDetails)
			.success(function(data) {
				console.log(data);
			});*/
		$('#previewbtn').click();
		}else{
			if($scope.vendorOrderError == "500" ){
				$('#purchase-order-error-alert').alert();
				$('#purchase-order-error-alert').show();
				 $("#purchase-order-error-alert").fadeIn(2000, 500);
				$scope.purchaseOrderError="Please enter valid values for Unit Cost Price, Total SQMTR, Quantity and Total price for the items.";
			}
			if($scope.hardwareOrderError == "500"){
				$('#purchase-order-error-alert').alert();
				$('#purchase-order-error-alert').show();
				 $("#purchase-order-error-alert").fadeIn(2000, 500);
				$scope.purchaseOrderError="Please enter valid values for Unit Cost Price, Quantity and Total price for the items.";
			}
		}
	}
	
	$scope.savePurchaseOrderDetails=function(){
		console.log($scope.verfiedPurchaseDetails);
			$http.post(webContextPath+"/purchase/order/save", $scope.verfiedPurchaseDetails)
			.success(function(data) {
				console.log(data);
				if(data.statusCode == 200){
					$('#success-alertp').alert();
					$('#success-alertp').show();
					$("#success-alertp").fadeIn(2000, 500);
					$scope.successMessage = data.message;
					$('#danger-alertp').hide();
				}else{
					$('#success-alertp').hide();
					$('#danger-alertp').alert();
					$('#danger-alertp').show();
					$("#danger-alertp").fadeIn(2000, 500);
					$scope.errorMessage = data.message;
				}
			});
	}
	
	$scope.getPurchaseOrderDetails=function(selectedVendor){
		$scope.productype = "vendortype";
		$scope.vendorPurchaseOrdes = [];
		if(selectedVendor !=null){
			$http.get(webContextPath+"/purchase/order/details/"+selectedVendor.vendorId)
			.success(function(data) {
				console.log(data);
				if(data.statusCode == 200){
					$.each(data.object,function(key,val){
						var vendorPurchaseObject = {
								purchaseId:val.purchaseOrderId,
								orderNumber:val.purchaseOrderNumber,
								invoiceNumber:val.purchaseInvoiceNumber,
								orderAmount:val.purchaseOrderAmount,
								stockEntryStatus:val.isStockEntry
						}
						$scope.vendorPurchaseOrdes.push(vendorPurchaseObject);
					});
				}else{
					$scope.errorMessage = data.message;
					 $('#danger-stock-alert').show();
					 $('#danger-stock-alert').alert();
		             $("#danger--stock-alert").fadeIn(2000, 500);
		             $('#success-stock-alert').hide();
				}
			});
		}
	}
	
	$scope.showPurchasedItems = function(selectedOrder){
		$scope.selectedPurchaseOrder = selectedOrder;
		$scope.purchasedOrderItemList = [];
		$http.get(webContextPath+"/purchase/order/items/"+selectedOrder.purchaseId)
		.success(function(data) {
			console.log(data);
			if(data.statusCode == 200){
				$.each(data.object,function(key,val){
					var purchasedProductInfo={
							purchaseOrderItemId:val.purchaseOrderItem,
							description:val.description,
							gradeId:val.gradeId,
							arrivedQuantity:val.quantity,
							unitCostPrice:parseFloat(val.unitCostPrice),
							totalSQMtr:parseFloat(val.totalSQMtr),
							totalCostPrice:parseFloat(val.totalCostPrice)
					}
					$scope.purchasedOrderItemList.push(purchasedProductInfo);
				});
				$('#purchaseOrderModal').modal('show');
			}else if(data.statusCode == 204){
				
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
				 $scope.addOrderItem($scope.selectedOrderHardware,'add','NONVENDORCATEGORY')
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
		$('#item-alert').hide();
		$('#successmodal-alert').hide();
		$('#dangermodal-alert').hide();
		$('#success-stock-alert').hide();
		$('#danger-stock-alert').hide()
		$scope.operation='ADD';
		$scope.gradeDetail={};
		$('#purchase-order-error-alert').hide();
	 };
	
	
	$scope.copyAddress=function(){
		//if($('#copyaddress').is(':checked')){
	    	$scope.order.addressVO={
	    		addressLine1:$scope.order.customerVO.addressLine1,
	    		addressLine2:$scope.order.customerVO.addressLine2,
	    		mobile:$scope.order.customerVO.mobile,
	    		email:$scope.order.customerVO.email,
	    		state:$scope.order.customerVO.state,
	    		city:$scope.order.customerVO.city
	    	};
	   // }
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
				agentId:null,
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
				$scope.purchaseOrderVendors = [];
				$.each(data.object, function(key, val) {
					console.log(val);
					$scope.vendors.push(val);
					$scope.purchaseOrderVendors.push(val);
				});
			}
			//$scope.getSelectedVendor($scope.vendors[0]);
		});
	}
	
	$scope.gradeList=function(){
		$http.get(webContextPath+"/grades/list").success(
		function(data) {
			console.log(data);
			if(data.statusCode==200){
				 $scope.allgrades=[];
				 $scope.productselected=false;
				 $scope.vendorselected=false;
				 
				$.each(data.object,function(key,val){
					 var grade={
							  gradeId:val.gradeId,
							  description:val.description,
							  length:val.length,
							  width:val.width,
							  vat:val.vat,
							  price:val.price,
							  category:'VENDOR',
							  availableStock:val.totalAvailableStock
							  
					  }
					$scope.allgrades.push(grade);
				});
			}else{
				
			}
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
				$.each(data,function(key,val){
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
						 gradeId:val.gradeId,
						 length:val.length,
						 width:val.width,
						 sizeUnit:val.sizeUnit,
						 price:val.price,  
				         vat:val.vat,
				         quantity:0,
				         description:val.description
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
	
	
	
	$scope.gradeStockAvailable=0;
	$scope.selectedGrade={};
	$scope.warehouseGradeQty=[];
	$scope.isresult=false;
	$scope.isgradeSelected=false;
	$scope.gradeWarehouses=[];
/*	$scope.getAvailableStock=function(selectedGrade, from){
		$scope.selectedGrade={};
		$scope.isgradeSelected=true;
		$scope.warehouseGradeQty=[];
		if(selectedGrade == undefined){
			$scope.errMsg="Item not available";
		}
		else if(selectedGrade!=null && $scope.isgradeSelected){
			
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
						 if($scope.selectedGrade.totalStockAvailable>=100){
							 $scope.selectedGrade.warehouseBreakups=$scope.warehouseGradeQty;
							 $scope.addOrderItem(selectedGrade,'add','VENDORCATEGORY')
						 }else{
							 console.log("check item added")
						 }
						 
					 	$scope.selectedGrade.productInfo=$scope.selectedProduct;
						$scope.selectedGrade.vendorInfo=$scope.selectedVendor;
						
						console.log($scope.warehouseGradeQty);
				}
				
				//$scope.gradeStockAvailable=data;
			  });
			}else{
				console.log("Grade Detail Not selected");
				
			}
				
		}*/
			
	$scope.itemStocklevel=false;
	$scope.checkStockForSelectedItem=function(selectedItem, from){
		 console.log(selectedItem);
		 
		 $scope.warehouseGradeQty=[];
		 if(from.toUpperCase() == 'STOCK_ENTRY'){
			 $scope.isgradeSelected=true;
			 var selectedVendorItem = $scope.getSelectedItemWarehouseDetails(selectedItem);
			 console.log($scope.warehouseGradeQty)
		 }
		 else if(from.toUpperCase() == 'ORDER_ENTRY'){
				 if(selectedItem.category == undefined){
					 
				 }
		 		 else if(selectedItem.category.toUpperCase() == 'VENDOR'){
		 			var selectedVendorItem = $scope.getSelectedItemWarehouseDetails(selectedItem.item);
		 			 if(selectedVendorItem.totalAvailableStock>=100){
		 				 $scope.itemStocklevel=false;
						 $scope.selectedGrade.warehouseBreakups=$scope.warehouseGradeQty;
						 $scope.addOrderItem($scope.selectedGrade,'add','VENDORCATEGORY')
						
					 }else{
						 $scope.itemStocklevel=true;
					 }
				 	$scope.selectedGrade.productInfo=$scope.selectedProduct;
					$scope.selectedGrade.vendorInfo=$scope.selectedVendor;
					console.log($scope.warehouseGradeQty);
		 			 
				 }else if(selectedItem.category.toUpperCase() == 'HARDWARE'){
					 $scope.getHardwareAvailableStock(selectedItem.item);
				 }
		 }
	}
	
	$scope.purchasedProductSelected={};
	$scope.getSelectedItemWarehouseDetails=function(selectedItem){
		$scope.selectedGrade.description=selectedItem.description;
		$scope.selectedGrade.unitPrice=selectedItem.price;
		$scope.selectedGrade=selectedItem;
		console.log($scope.selectedGrade);
		$http.get(webContextPath+'/stock/available/'+selectedItem.gradeId)
		.success(function(data){
			console.log(data);
			if(data.statusCode==200){
				$scope.isresult=true;
				var totalStock=0;
				 $.each(data.object,function(key,val){
					 var orderQty=0;
						var gradeStockQty={
								stockId:val.stockId,
								warehouseId:parseInt(val.warehouseId),
								warehouseName:val.warehouseName,
								quantity:val.quantity,
								capacity:val.capacity,
								availableSpace:val.availableSpace,
								totalGradeStock:val.totalGradeStock,
								newQuantity:0,
						        qty:orderQty,
						        stockLevel:100,
						        valid:$scope.isValidEntry(orderQty,val.quantity,val),
						        sellingPrice:val.gradesVO.price
						}
						totalStock=totalStock+gradeStockQty.quantity;
						 $scope.selectedGrade.totalStockAvailable=totalStock;
						$scope.warehouseGradeQty.push(gradeStockQty);
						if(selectedItem.gradeId == val.gradesVO.gradeId){
							$scope.purchasedProductSelected.sellingPrice = val.gradesVO.price;
						}
				 });
			}
       });
		return $scope.selectedGrade;
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
		var isValid=$scope.validateTotalGrade(gradeSelected, $scope.purchasedProductSelected.sellingPrice);
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
						purchaseStockItemId:$scope.selectedGrade.purchaseOrderItemId,
						sellingPrice:$scope.purchasedProductSelected.sellingPrice
				};
				
				console.log("gradeWarehouseData : " + JSON.stringify(gradeWarehouseData));
				
				$http.post(webContextPath+'/stock/entry', JSON.stringify(gradeWarehouseData))
				.success(function(data){
						console.log(data);
						if(data.statusCode==200){
							$scope.successMessage=data.message;
							 $('#successmodal-alert').show();
							 $('#successmodal-alert').alert();
				             $("#successmodal-alert").fadeIn(2000, 500);
				             $('#dangermodal-alert').hide();
				           //  $scope.checkStockForSelectedItem(gradeSelected,'STOCK_ENTRY');
				            // $scope.getWarehouseStockList();
				             $scope.showPurchasedItems($scope.selectedPurchaseOrder);
				             
				            /* if($scope.purchasedOrderItemList.size == 1){
				            	 	$scope.updatePurchaseOrderStatus($scope.selectedPurchaseOrder);
				             }else{
				            	 $scope.showPurchasedItems($scope.selectedPurchaseOrder);
				             }*/
						} else if(data.statusCode==206){
				            	  $scope.successMessage=data.message;
				            	  $('#success-stock-alert').show();
								  $('#success-stock-alert').alert();
						          $("#success-stock-alert").fadeIn(2000, 500);
						          $('#closeupdateorderbtn').click();
			                }else{
								$scope.errorMessage=data.message; 
								 $('#dangermodal-alert').show();
								 $('#dangermodal-alert').alert();
					             $("#dangermodal-alert").fadeIn(2000, 500);
					             $('#successmodal-alert').hide();
						}
				});
		}else{
			//$scope.errorMessage = "Sum of total quantity of warehouses for \""+ gradeSelected.description  +"\" must be equal to "+ gradeSelected.arrivedQuantity +"."
			 $('#dangermodal-alert').show();
			 $('#dangermodal-alert').alert();
             $("#dangermodal-alert").fadeIn(2000, 500);
             $('#successmodal-alert').hide();
		}
	}
	
	/*$scope.updatePurchaseOrderStatus=function(selectedOrder){
		$http.post(webContextPath+'/purchase/order/statusupdate', selectedOrder)
		.success(function(data){
				console.log(data);
				if(data.statusCode==200){
				
				}
		});
	}*/
	
	$scope.isValidQuantity=false;
	$scope.totalQuantity;
	$scope.validateTotalGrade=function(selectedGrade, purchasedProduct){
		var isValid=true;
		$scope.totalQuantity=0;
		 $.each($scope.warehouseGradeQty,function(key,val){
			 if(isNaN(val.newQuantity)){
				 isValid=false;
				 $scope.errorMessage="Please enter a valid quantity for warehouse : "+ val.warehouseName ; 
				 return false;
			 }else{
				 $scope.totalQuantity=$scope.totalQuantity+parseInt(val.newQuantity);
			 }
		 });
		 if($scope.totalQuantity == selectedGrade.arrivedQuantity){
			 isValid = true;
		 }
		 else{
			 isValid=false;
			 $scope.errorMessage = "Sum of total quantity of warehouses for \""+ selectedGrade.description  +"\" must be equal to "+ selectedGrade.arrivedQuantity +"."
		 }
		 
		 if($scope.purchasedProductSelected.sellingPrice == 0){
			 isValid=false;
			 $scope.errorMessage = "Please enter the selling price for \""+ selectedGrade.description+"\"."
		 }
		 
		 
		/* if(selectedGrade.newQuantity > $scope.selectedGrade.totalQuantityArrived){
			isValid=false;
			 $scope.errorMessage="Stock quantity should not be greater than quantity arrived"; 
		}else{
			
			$.each($scope.warehouseGradeQty,function(key,val){
				$scope.totalQuantity=$scope.totalQuantity+parseInt(val.newQuantity);
			 });
			if($scope.totalQuantity != $scope.selectedGrade.totalQuantityArrived){
				isValid=false;
				$scope.errorMessage="Stock quantity should not be greater than quantity arrived"; 
			}
		}*/
		 return isValid;
	}
	
	$scope.warehouseGradeStockList =[];
	$scope.getWarehouseStockList=function(){
	 $http.get(webContextPath+'/stock/list')
		.success(function(data){
			console.log(data);
			$scope.warehouseGradeStockList = []
			if(data.statusCode == 200){
				$.each(data.object,function(key,val){
					
				});
			}
		});
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
		$scope.isItemAdded="false";
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
						 $scope.isItemAdded="false";
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
						 $scope.isItemAdded="false";
					}
				});
			}
			
			
			
			if(!itemExists){
				var totalSizeSqmtr=0.0;
				var orderItem={};
				totalSizeSqmtr=orderProduct.length * orderProduct.width;
				/*if(orderProduct.sizeUnit=="MTR"){
					totalSizeSqmtr=orderProduct.length * orderProduct.width;
				}else if(orderProduct.sizeUnit=="FT"){
					
				}*/
				 orderItem = {
							gradeId : orderProduct.gradeId,
							quantity : 0,
							unitPrice : orderProduct.price,
							unitSQMTRPerPiece:orderProduct.unitSQMTRPerPiece,
							description:orderProduct.description,
							vat:orderProduct.vat,
							length:orderProduct.length,
							width:orderProduct.width,
							totalSqmtr:totalSizeSqmtr.toFixed(2),
						   // gradeStockInfo: selectedGrade.warehouseBreakups,
							totalPrice: $scope.getItemTotalPrice(orderProduct, mode),
							productInfo:$scope.productVendor.selectedProduct,
							vendorInfo:$scope.productVendor.selectedVendor
						};
				console.log(orderItem);
				if(mode.toUpperCase()=='ADD'){
					orderItem.isNew=true;
					orderItem.gradeStockInfo=$scope.selectedGrade.warehouseBreakups;
					$scope.orderItems.push(orderItem);
					$scope.isItemAdded="true";
				}else if(mode.toUpperCase()=='EDIT'){
					orderItem.isNew=false;
					orderItem.warehouseBreakups=$scope.selectedGrade.warehouseBreakups;
					$scope.clonedOrder.itemList.push(orderItem);
					$scope.isItemAdded="true";
				}
				  
			}	
			console.log($scope.orderItems);
		   }else if(category.toUpperCase()=='NONVENDORCATEGORY'){
			   if(mode.toUpperCase()=='ADD'){
					$.each($scope.hardwareItems,function(key,val){
						if(orderProduct.hardwareStockId==val.hardwareStockId){
							itemExists=true;
							$scope.duplicateitem=true;
							$('#item-alert').alert();
							$('#item-alert').show();
							return false;
						}else{
							itemExists=false;
							$scope.duplicateitem=false;
							$('#item-alert').hide();
						}
					});
					if(!itemExists){
						var totalSizeSqmtr=0.0;
						var hardwareVO={};
						hardwareVO = angular.copy(orderProduct);
						hardwareVO.quantity=0;
						hardwareVO.totalPrice=0.0;
						$scope.hardwareItems.push(hardwareVO);
						$scope.isItemAdded="true";
						console.log($scope.hardwareItems);  
					}
				}else if(mode.toUpperCase()=='EDIT'){
					$.each($scope.clonedOrder.hardwareVOList,function(key,val){
						if(orderProduct.hardwareStockId==val.hardwareStockId){
							itemExists=true;
							$scope.duplicateitem=true;
							$('#item-alert').alert();
							$('#item-alert').show();
							return false;
						}else{
							itemExists=false;
							$scope.duplicateitem=false;
							$('#item-alert').hide();
						}
					});
					
					if(!itemExists){
						var totalSizeSqmtr=0.0;
						var hardwareVO={};
						hardwareVO = angular.copy(orderProduct);
						hardwareVO.quantity=0;
						hardwareVO.totalPrice=0.0;
						$scope.clonedOrder.hardwareVOList.push(hardwareVO);
						$scope.isItemAdded="true";
						console.log($scope.clonedOrder.hardwareVOList);  
					}
				}
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
					  var orderItemTotalPrice=val.quantity * val.unitSQMTRPerPiece * val.unitPrice;
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
			    	  var orderItemTotalPrice=val.quantity * val.unitSQMTRPerPiece * val.unitPrice;
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
	   
	   	 $scope.gradeWarehouses=[];
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
		 };
	 
		
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
	 $scope.searchedCustomer ={};
	 $scope.deliveryAddress =[];
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
					$scope.order.addressVO={};
					 $scope.deliveryAddress =[];
					if(data.object==null){
						$scope.errorView=true;
						$scope.errorMsg="Customer does not exists";
					 }else{
						 $scope.order.customerVO=data.object;
						 $scope.errorView=false;
						 $scope.errorMsg=""; 
						$.each(data.object.deliveryAddressVO,function(key,val){
							console.log(key);
							if(key == 0){
								$scope.deliveryAddress.push({
									  isSelected:true,
									  value:val,
									  customerMobile:parseInt($scope.order.customerVO.mobile),
									  deliveryMobile:val.mobile
								});
								$scope.order.addressVO.addressId=angular.copy(val.addressId);
								$scope.order.addressVO.addressLine1=angular.copy(val.addressLine1);
								$scope.order.addressVO.addressLine2=angular.copy(val.addressLine2);
								$scope.order.addressVO.mobile=parseInt($scope.order.customerVO.mobile);
								
							}else{
								$scope.deliveryAddress.push({
									  isSelected:false,
									  value:val,
									  customerMobile:parseInt($scope.order.customerVO.mobile),
									  deliveryMobile:val.mobile
								});
								$scope.order.addressVO.addressId=angular.copy(val.addressId);
								$scope.order.addressVO.addressLine1=angular.copy(val.addressLine1);
								$scope.order.addressVO.addressLine2=angular.copy(val.addressLine2);
								$scope.order.addressVO.mobile=parseInt($scope.order.customerVO.mobile);
							}
						});
						
						$scope.searchedCustomer={
							customer:$scope.order.customerVO,
							isNew:false,
							deliveryAddress:$scope.deliveryAddress
						};
						
						
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
					  if(data.object==null){
						$scope.errorView=true;
						$scope.errorMsg="Customer does not exists";
					 }else{
						 $scope.order.customerVO=data.object;
						 $scope.errorView=false;
						$scope.errorMsg=""; 
					 }
				}
			});
		}
	}
	
	$scope.selectDeliveryAddress=function(selectedValue, address, orderForm){
		 if(address == undefined){
				$('#newAddressForm1').show();
				$('#newAddressForm2').show();
				$scope.order.addressVO={};
				orderForm.deliveryAddressLine1.$invalid = false;
				orderForm.deliveryAddressLine1.$pristine = true;
				orderForm.deliveryAddressMobile.$invalid = false;
				orderForm.deliveryAddressMobile.$pristine = true;
		}else{
		 if(address.value.addressId!=null){
				console.log(orderForm.deliveryAddressLine1.$invalid)
				orderForm.deliveryAddressLine1.$invalid = true;
				orderForm.deliveryAddressLine1.$pristine = false;
				orderForm.deliveryAddressMobile.$invalid = true;
				orderForm.deliveryAddressMobile.$pristine = false;
				
				$scope.order.addressVO=angular.copy(address.value)
				if(address.mobile==undefined || address.mobile == null ){
				$scope.order.addressVO.mobile=address.customerMobile;
				}else{
					$scope.order.addressVO.mobile =address.customerMobile;
				}
				console.log($scope.order.addressVO);
				$('#newAddressForm1').hide();
				$('#newAddressForm2').hide();
			}
		}
		
		/*if(address.isSelected == false){
			$('#newAddressForm1').show();
			$('#newAddressForm2').show();
			$scope.order.addressVO={};
			orderForm.deliveryAddressLine1.$invalid = false;
			orderForm.deliveryAddressLine1.$pristine = true;
			orderForm.deliveryAddressMobile.$invalid = false;
			orderForm.deliveryAddressMobile.$pristine = true;
		}else if(address.isSelected == true){
			console.log(orderForm.deliveryAddressLine1.$invalid)
			orderForm.deliveryAddressLine1.$invalid = true;
			orderForm.deliveryAddressLine1.$pristine = false;
			orderForm.deliveryAddressMobile.$invalid = true;
			orderForm.deliveryAddressMobile.$pristine = false;
			
			$scope.order.addressVO=angular.copy(address)
			if(address.mobile==null){
			$scope.order.addressVO.mobile=address.customerMobile;
			}else{
				$scope.order.addressVO.mobile =address.customerMobile;
			}
			console.log($scope.order.addressVO);
			$('#newAddressForm1').hide();
			$('#newAddressForm2').hide();
		}*/
		
	}
	
	$scope.onlyNumbers = /^\d+$/;
	$scope.decimalNumbers = /^[0-9]+([,.][0-9]+)?$/g;
	
	$scope.filterValue = function($event){
        if(isNaN(String.fromCharCode($event.keyCode))){
            $event.preventDefault();
        }
	};
	   
	$scope.verifiedOrder={};
	$scope.verifyOrder=function(orderForm){
		//$('#invoiceModal').modal('show');
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
						itemId:null,
						gradeId:val.gradeId,
						description:val.description,
					    quantity:val.quantity,
					    unitPrice:val.unitPrice,
					   // sizeUnit:val.sizeUnit,
					    length:val.length,
					    width:val.width,
					    vat:val.vat,
					    perPieceUnitSQMTR:val.unitSQMTRPerPiece,
					    totalSqmtr:parseFloat(val.quantity) * val.unitSQMTRPerPiece,
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
						itemId:null,
						hardwareStockId:val.hardwareStockId,
						description:val.description,
					    quantity:val.quantity,
					    unitPrice:val.unitPrice,
					    totalUnits:val.totalUnits,
					    vat:val.vat,
					    totalPrice:parseFloat(val.totalPrice),
					});
					 
				});
			}
			
			if($scope.totalOrderAmount>0){
				var orderDetail={
						agentId:parseInt($scope.referredAgents.selected),
						customerVO:$scope.order.customerVO,
						addressVO:$scope.order.addressVO,
						itemList:orderList,
						hardwareVOList:hardwareOrderList,
						orderAmount:parseFloat($scope.totalOrderAmount)
						
				}
				
				console.log(orderDetail.itemList);
				console.log(orderDetail.hardwareVOList);
				  var validationStatus = $scope.validateStockBreakUp(orderDetail);
				 if(validationStatus.status==200){	
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
								message:validationStatus.message
						}
				 }	
				//$scope.createOrder($scope.verifiedOrder.value);
				
			}else{
				$scope.verifiedOrder={
						status:404,
						message:"Please add quantity for the item to create order."
				}
			}
		}else{
			$scope.verifiedOrder={
					status:500
			}
		}
   }
	
	$scope.validateStockBreakUp=function(orderDetail){
		var checkOrderQuantityEquality=[];
		var validationStatus={
				status:200,
				message:''
		}
		var quantityErrMsgInfo="";
		if(orderDetail.itemList.length>0){
			$.each(orderDetail.itemList,function(key,val){
				if(val.isValidTotal==false){
					checkOrderQuantityEquality.push(val.description);
					quantityErrMsgInfo="Sum of total quantity of warehouses for \""+ val.description  +"\" must be equal ";
					console.log("Total Sum of quantity of warehouses for "+ val.description  +" must be equal to "+val.quantity );
					validationStatus={
							status:412,
							message:quantityErrMsgInfo
					    }
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
					validationStatus={
							status:412,
							message:quantityErrMsgInfo
					    }
					return false;
				}
			})
			
	   }
	   
	   console.log(validationStatus)
	   return validationStatus;
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
	
	$scope.billType="CASH";
	$scope.setBillType=function(billType){
		$scope.billType=billType;
	}
	$scope.getPaymentTypes=function(){
		$scope.orderPayment={};
		$scope.paymentTypes =  [ "CASH", "CHEQUE", "DEMAND DRAFT", "CARD"];
	}
	
	$scope.getSelectedPayment=function(selectedPaymentType){
		console.log($scope.orderPayment)
		if($scope.orderPayment.paymentType!=null){
			if($scope.orderPayment.paymentType.toUpperCase()=='CASH'){
				$scope.selectedType=1;
				$scope.orderPayment.chequeDraftDate="";
				$scope.orderPayment.chequeDraftNo="";
				$scope.orderPayment.bankName="";
				$scope.orderPayment.branchName="";
			}
			else if($scope.orderPayment.paymentType.toUpperCase()=='CHEQUE'){
				$scope.selectedType=2;
				$scope.typeLabel="Cheque";
				$scope.orderPayment.receipt="";
				
			}else if($scope.orderPayment.paymentType.toUpperCase()=='DEMAND DRAFT'){
				$scope.selectedType=3;
				$scope.typeLabel="Demand Draft";
				$scope.orderPayment.receipt="";
				
			}else if($scope.orderPayment.paymentType.toUpperCase()=='CARD'){
				$scope.selectedType=4;
				$scope.orderPayment.chequeDraftDate="";
				$scope.orderPayment.chequeDraftNo="";
				$scope.orderPayment.bankName="";
				$scope.orderPayment.branchName="";
				$scope.orderPayment.receipt="";
				
			}else{
				
			}

		}else{
			
		}
	}
	
	
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
	
	
  	$scope.cancelOrder=function(){
		$http.get(webContextPath+"/order/cancel/"+$scope.finalOrder.number)
		.success(function(data){
			console.log(data);
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
	
	$scope.confirmedOrder ={};
	$scope.generateInvoice=function(orderNumber){
		console.log(orderNumber);
		$scope.confirmedOrder ={};
		$http.get(webContextPath+"/order/search/"+orderNumber)
		.success(function(data){
			var totalItems = 0;
			console.log(data);
			if(data.statusCode==200){
				$scope.confirmedOrder=angular.copy(data.object);
				totalItems=totalItems+$scope.confirmedOrder.itemList.length+$scope.confirmedOrder.hardwareVOList.length;
				$('#invoiceModal').modal('show');
			}else{
				
			}
		});
	}
	$scope.saveCashPayment=function(payment,confirmedOrder){
		console.log(payment);
		var invoiceVO = {};
		if(payment.paymentType.toUpperCase() == "CASH"){
			invoiceVO.invoiceType="CASH";
			invoiceVO.paymentType=payment.paymentType;
			invoiceVO.orderNumber=confirmedOrder.orderNumber;
			invoiceVO.orderVO=confirmedOrder;
		}else if(payment.paymentType.toUpperCase() != "CASH"){
			invoiceVO.invoiceType="ECASH";
			invoiceVO.paymentType=payment.paymentType;
			invoiceVO.chequeDraftNo = payment.chequeDraftNo;
			invoiceVO.chequeDraftDate = payment.chequeDraftDate;
			invoiceVO.bankName = payment.bankName;
			invoiceVO.branchName = payment.branchName;
			invoiceVO.orderNumber=confirmedOrder.orderNumber;
			invoiceVO.orderVO=confirmedOrder;
		}
		$http.post(webContextPath+"/invoice/save", invoiceVO)
		.success(function(data){
			if(data.statusCode == 200){
				$('#invoicePrintId').click();
			}
		});
	}
	$scope.saveCreditPayment=function(payment, orderNumber){
		var invoiceVO = {};
		invoiceVO.invoiceType="CREDIT";
		if(payment.creditType.toUpperCase() == 'PARTIAL'){
			invoiceVO.creditType="PARTIAL";
			invoiceVO.paymentType=payment.paymentType;
			invoiceVO.chequeDraftNo = payment.chequeDraftNo;
			invoiceVO.chequeDraftDate = payment.chequeDraftDate;
			invoiceVO.bankName = payment.bankName;
			invoiceVO.branchName = payment.branchName;
			invoiceVO.orderNumber=confirmedOrder.orderNumber;
			invoiceVO.orderVO=confirmedOrder;
		}else  if(payment.creditType.toUpperCase() == 'FULL'){
			invoiceVO.creditType="PARTIAL";
			invoiceVO.orderNumber=confirmedOrder.orderNumber;
			invoiceVO.orderVO=confirmedOrder;
		}
		
		$http.post(webContextPath+"/invoice/save", invoiceVO)
		.success(function(data){
			$('#invoiceconfirm').html(data);
		});
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
		$scope.verifiedEditOrder={};
		if(editedOrder.itemList.length>0){
			$.each(editedOrder.itemList,function(key,val){
				isValidQty=$scope.validateStockEditOrderQty(val,val.warehouseBreakups);
				 console.log("validated : " +isValidQty);
				 if(!isValidQty){
					 $scope.verifiedEditOrder.status=500;
					 $scope.verifiedEditOrder.index=index;
					 return false;
				 }
			});
		}
		
		if(editedOrder.hardwareVOList.length>0){
			$.each(editedOrder.hardwareVOList,function(key,val){
				console.log(val);
				isValidQty=true;
			});
		}
		
		if(isValidQty){
			$scope.verifiedEditOrder.status=200;
			$('#closeorderbtn').click();
			$('#editedPreForm').modal('show');
		}else{
			
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



