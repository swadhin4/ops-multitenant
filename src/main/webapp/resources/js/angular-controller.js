productApp.controller('productController',  function($rootScope,$scope,$http,SharedService) {
	$scope.productvendors=[];
	$scope.products=[];
	$scope.product;
	$scope.show=true;
	$scope.productselected=false;
	$scope.productPrevSelected=null;
	$http.get('list')
	.success(function(data) {
		$.each(data.productVOList, function(key, val) {
			$scope.products.push(val);
		});
		 $scope.productDetail=$scope.products[0];
		 console.log("Default Selecteion:" + $scope.productDetail );
		 $scope.productselected=true;
		
	});
	$scope.highlightListProduct = function(divid) {
		var id=divid.$index;
		if ($scope.productPrevSelected != null) {
			$('#' + $scope.productPrevSelected+" span a").removeClass('activeBlue');
		}
		$scope.productPrevSelected = 'pdiv'+ id;
		$('#pdiv'+ id + ' span a').addClass('activeBlue');//#a9d0f5
		
	};
	
	$scope.showProductDetail=function(product){
		$scope.productselected=true;
		$scope.vendorselected=false;
		$scope.productDetail=product;
		$('#Save').text("Update");
		$http.get(webContextPath+"/vendors/"+product.productId)
		.success(function(data){
		});
		
	};
	$scope.addProduct=function(){
		$scope.productDetail={};
		 $(document.getElementById("productDetail.productName")).focus();
		$('#Save').text("Save");
		
	};
		$scope.saveProduct = function(product) {
			if($scope.productDetail.productId==null){
				var newProduct={
						"productName":$scope.productDetail.productName,
						"productCode":$scope.productDetail.productCode
				};
			$http.post("saveproductnew",newProduct)
			.success(function(data){
				if(data.statusCode=="0"){
						$('#successp-alert').alert();
	             		$("#successp-alert").fadeTo(2000, 500).slideUp(500, function(){
	             		}); 
					$http.get('list')
					.success(function(data) {
						$scope.products=[];
						$.each(data.productVOList, function(key, val) {
							$scope.products.push(val);
						});
					});
				}else if(data.statusCode=="1"){
					$('#dangerp-alert').alert();
             		$("#dangerp-alert").fadeTo(2000, 500).slideUp(500, function(){
             		}); 
				}
				 	
			  });
			}else{
				var editableProduct={
						"productId":$scope.productDetail.productId,
						"productName":$scope.productDetail.productName,
						"productCode":$scope.productDetail.productCode
				};
				$http.post("updateproduct",editableProduct)
				.success(function(data){
					if(data.statusCode=="0"){
						$('#successp-alert').alert();
	             		$("#successp-alert").fadeTo(2000, 500).slideUp(500, function(){
	             		}); 
					$http.get('list')
					.success(function(data) {
						$scope.products=[];
						$.each(data.productVOList, function(key, val) {
							$scope.products.push(val);
						});
					});
				}else if(data.statusCode=="1"){
					$('#dangerp-alert').alert();
             		$("#dangerp-alert").fadeTo(2000, 500).slideUp(500, function(){
             		}); 
				}
				});
			}
			
		};
		
	
		
});

productApp.controller('vendorController',  function($rootScope,$scope,$http,$filter,$q) {
	
	$scope.vendorselected=false;
	$scope.vendorPrevSelected=null;
	$scope.highlightListVendor = function(divid) {
	    var id=divid.$index;
		if ($scope.vendorPrevSelected != null) {
			$('#' + $scope.vendorPrevSelected+" span a").removeClass('activeBlue');
		}
		$scope.vendorPrevSelected = 'vdiv'+ id;
		$('#vdiv'+ id + " span a").addClass('activeBlue');//#a9d0f5
		
	};
		
	$scope.vendor="";
	$scope.vendors=[];
	$scope.vendordata={};
	$http.get(webContextPath+'/vendor/list')
	.success(function(data) {
		console.log(data);
		$.each(data.vendorVOList, function(key, val) {
			console.log(val);
			$scope.productvendors.push(val);
		});
	});
	

	
	  
	$scope.states=[];
	$http.get(webContextPath+'/resources/json/state-cities.json').
	success(function(data) {
    	$scope.states = data;
 	console.log($scope.states);
	});
		
	
			  
     $scope.cities=[];
		$scope.getCities = function() {
			$scope.vendor.state=$scope.vendor.stateSelected;
			console.log($scope.vendor.stateSelected);
			$.each($scope.states, function(i, val) {
				if (val.stateid == $scope.vendor.stateSelected.stateid) {
					$scope.cities = val.cities;
					return false;
				}
			});
			console.log($scope.cities);
		};
		
		$scope.setCity=function(){
			$scope.vendor.city=$scope.citySelected; 
		}
			
			
	$scope.showVendorDetail=function(vendor){
		console.log(vendor + " - " +$scope.productDetail );	
		$scope.vendorselected=true;
		$scope.vendor = vendor;
		$('#savevendor').text("Update");
		//$scope.showStates(vendor);
		$scope.newgrades=[];
		/*$http.get('/app/vendor/grades/'+vendor.vendorId)
		.success(function(data) {
			$.each(data,function(i,val){
			var grade={
					     gradeId: val[0],		
						 gradeName: val[1],
						 thickness: val[2],
						 thicknessUnit:val[3],
						 length:val[4],
						 width:val[5],
						 sizeUnit:val[6],
						 price:val[7],  
			  }
				$scope.newgrades.push(grade);
			})
		});*/
		
		$http.get(webContextPath+'/vendor/product/grades/'+vendor.vendorId+"/"+$scope.productDetail.productId)
		.success(function(data) {
			$.each(data,function(i,val){
			var grade={
					     gradeId: val[0],		
						 gradeName: val[1],
						 thickness: val[2],
						 thicknessUnit:val[3],
						 length:val[4],
						 width:val[5],
						 sizeUnit:val[6],
						 price:val[7],  
			  }
				$scope.newgrades.push(grade);
			})
		});
	}
	
	$scope.newvendors=[];
	$scope.newthinkness=[];
	$scope.newgrades=[];
	$scope.addVendor = function() {
	    $scope.show=true;
		$scope.vendor={};
		
		$('#savevendor').text("Save");
		$(document.getElementById('vendor.name')).focus();
		$scope.cities=[];
		
	}
		
	
		/*$scope.thickness=[];*/
	$scope.isproductVendor=false;
		$scope.addGrade = function() {
			var selProduct=$scope.productDetail.productId;
			var selVendor=$scope.vendor.vendorId;
			$http.get(webContextPath+"/vendor/validateproduct/"+selVendor+"/"+selProduct).success(
					function(data) {
						if(data.statusCode=="0"){

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
									 isNew: true
									 
								    };
								 $scope.newgrades.push($scope.gradesinserted);
							
						}else if(data.statusCode=="1"){
							 $('#danger-alert-grade').alert();
				             $("#danger-alert-grade").fadeTo(4000, 500).slideUp(500, function(){
				             });  
						}
					});
			
	   };
	   
	  
	  /* $scope.addThickness = function(grade) {
			$scope.thicknessinserted = {
			 thicknessId: $scope.newthinkness.length + 1,		
			 thickness: '',
		   };
		 $scope.newthinkness.push($scope.thicknessinserted);
	   };*/
	   $scope.selectedVendor="";
	   $scope.updateGrade=function(vendor){
			$scope.selectedVendor=vendor;
		}
	   $scope.gradeVos;
	   	  $scope.saveGrades = function(data, id) {
	   		  console.log("ok:"+ JSON.stringify(data));
		   		$scope.gradeVos={
		   			 vendorVO :$scope.vendor,
		   			 productVO:$scope.productDetail,
		   			 gradeName:data.gradeName,
		   			 gradeId:id,
		   			 thickness:data.thickness,
		   			thicknessUnit:data.thicknessUnit,
		   			length:data.length,
		   			width:data.width,
		   			sizeUnit:data.sizeUnit,
		   			price:data.price
		   			
		   		  };
		   	  console.log($scope.gradeVos);
		    //$scope.user not updated yet
		    //angular.extend(data, {id: id});
		  $http.post(webContextPath+'/grades/vendor/save', $scope.gradeVos)
		   .success(function(data){
			   console.log(data);
			   $scope.showVendorDetail($scope.vendor);
		   });
		    
		  };
	   
		  $scope.filterGrade= function(grade) {
			    return grade.isDeleted !== true;
			  };

			  
		 /* $scope.removeGrade = function(id) {
			    var filtered = $filter('filter')($scope.newgrades, {gradeId: id});
			    if (filtered.length) {
			      filtered[0].isDeleted = true;
			    }
			  };*/
			  
			  $scope.removeGrade = function(index) {
				    $scope.newgrades.splice(index, 1);
				  };

		  
			  $scope.removeThickness = function(id) {
				    var filtered = $filter('filter')($scope.newthickness, {id: id});
				    if (filtered.length) {
				      filtered[0].isDeleted = true;
				    }
				  };
			  
		  $scope.saveVendor = function(data) {
			  
			  if($scope.productselected && $scope.vendorselected){
				  $scope.product=[];
				  $scope.product.push($scope.productDetail);
				     $scope.vendordata = {
						  vendorId:$scope.vendor.vendorId,
					      name: $scope.vendor.name,
					      phone:$scope.vendor.phone,
					      addressLine1:$scope.vendor.addressLine1,
					      addressLine2:$scope.vendor.addressLine2,
					      state:$scope.vendor.stateSelected.stateid,
					      city:$scope.vendor.citySelected.cityid,
					      contactPerson:$scope.vendor.contactPerson,
					      productVOList:$scope.product
					 };
			  if($scope.vendor.vendorId==null || $scope.vendor.vendorId==""){
				 	
				 	 console.log("Vendor Data : " +JSON.stringify($scope.vendordata));
			 $http.post(webContextPath+"/vendor/savevendornew", $scope.vendordata).success(
					function(data) {
						$http.get(webContextPath+'/vendor/list').success(function(data) {
							$scope.productvendors = [];
							$.each(data.vendorVOList, function(key, val) {
								$scope.productvendors.push(val);
							});
						});
					});
						 $('#success-alert').alert();
			             $("#success-alert").fadeTo(2000, 500).slideUp(500, function(){
			             }); 
			  }
			  else{
				  console.log($scope.productDetail, console.log($scope.vendor));
								  
				$http.post(webContextPath+"/vendor/updatevendor", $scope.vendordata)
					.success(function(data){
						if(data.statusCode=="0"){
							$http.get(webContextPath+'/vendor/list')
							.success(function(data) {
								alert(JSON.stringify(data));
								
								$scope.productvendors=[];
								$.each(data.vendorVOList, function(key, val) {
									$scope.productvendors.push(val);
								});
									$('#success-alert').alert();
									$("#success-alert").fadeTo(2000, 500).slideUp(500, function(){
					             }); 
								});
						}else{
								$('#danger-alert-already').alert();
					             $("#danger-alert-already").fadeTo(2000, 500).slideUp(500, function(){
					             // $("#danger-alert").alert('close');
					             }); 
							}
						});
					
				 
			  }
			    
			  }else{
				  $('#danger-alert').alert();
		             $("#danger-alert").fadeTo(2000, 500).slideUp(500, function(){
		             // $("#danger-alert").alert('close');
		             });  
			   }
			  };
	  
			
			
			$scope.showProducts=function(){
				
				if($scope.products.length) {
			        var selected = $filter('filter')($scope.products, {productId: $scope.products.productId});
			        return selected.length ? selected[0].name : 'Not set';
			      } else {
			        return $scope.products.productName;
			      }
					
			}
			$scope.productselectedvendors=[];
			$scope.getVendors=function(product){
				$http.get(webContextPath+"/vendors/"+product.productDetail.productId)
				.success(function(data){
					
					if(data.vendorVOList.length == 0){
						$scope.productselectedvendors=[];
						
					}else{
						$scope.productselectedvendors=[];
						$.each(data.vendorVOList,function(i,val){
							$scope.productselectedvendors.push(val);
						})
					}
				})
			}
			
          $scope.showVendors=function(){
				
				if($scope.productvendors.length) {
			        var selected = $filter('filter')($scope.productvendors, {vendorId: $scope.productvendors.vendorId});
			        return selected.length ? selected[0].name : 'Not set';
			      } else {
			        return $scope.productvendors.name;
			      }
					
			}
	
});

