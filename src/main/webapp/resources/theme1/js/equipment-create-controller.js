chrisApp.controller('equipmentcreateController', 
		 ['$rootScope', '$scope', '$filter','authService','userService',
		  'siteService','serviceProviderService','assetService',
		  function  ($rootScope, $scope , $filter,authService,userService,
				  siteService,serviceProviderService,assetService) {
			 
			 
			 
			 $scope.equipmentData ={};			 
			 $scope.selectedRow =0;
			 $scope.selectedAsset={};
			 $scope.operation = {};
						 
			 $scope.asset={
					 selected:{},
					 list:[]
			 }
			 $scope.assetCategory={
					 selected:{},
					 equipmentList:[],
					 serviceList:[]
					 
			 }
			 $scope.repairType={
					 selected:{},
					 list:[]
			 }
			
			 $scope.serviceRepairType={
					 selected:{},
					 list:[]
			 }
			 
			 $scope.assetLocation={
					 selected:{},
					 list:[]
			 }
			 $scope.serviceProvider={
					 selected:{},
					 list:[]
			 }
			 $scope.accessSite={
					 selected:{},
					 list:[]
			 }
			 
			 $scope.selectedSites = [];
			 
			 $scope.assetId = '';
			 
						 			 
			 angular.element(document).ready(function () {
				 $scope.operation = $('#mode').val();
				 $scope.originateFrom = $.jStorage.get('originateFrom');
				 $scope.getLoggedInUserAccess();
				
					$("#drpIsAsset").change(
							function() {

								var selectedText = $(this).find("option:selected")
										.text();
								var powersensorSelectedText = $('#drpIsPowersensor :selected').text();

								var selectedValue = $(this).val();
								if (selectedText == "NO") {
									$('#drpIsPowersensor').val("");
									$('#txtSensorNumber').val("");
									$("#drpIsPowersensor").prop("disabled", true);
									$("#txtSensorNumber").prop("disabled", true);
									$scope.equipmentData.pwSensorNumber=null;
								} else if (selectedText == "YES" && powersensorSelectedText == "Select Sensor Attached") {
									$("#drpIsPowersensor").prop("disabled", false);
									$("#txtSensorNumber").prop("disabled", false);
									$("#drpIsPowersensor").attr('required', true);
								} else if (selectedText == "YES"
										&& powersensorSelectedText == "YES") {
									$("#txtSensorNumber").prop("disabled", false);
									$("#txtSensorNumber").attr('required', true);
								} else if (selectedText == "YES"
										&& powersensorSelectedText == "NO") {
									$("#txtSensorNumber").prop("disabled", true);
									$('#txtSensorNumber').val("");
								}
								else if (selectedText == "YES"	) {
									$("#drpIsPowersensor").prop("disabled", false);
									$("#txtSensorNumber").prop("disabled", false);
							}
								$scope.equipmentData.isAssetElectrical=selectedValue;

							});

					$("#drpIsPowersensor").change(
							function() {

								var selectedText = $(this).find("option:selected")
										.text();
								var assetSelectedText = $('#drpIsAsset :selected')
										.text();

								var selectedValue = $(this).val();
								if (selectedText == "NO"
										&& assetSelectedText == "YES") {
									$("#txtSensorNumber").prop("disabled", true);
								} else if (selectedText == "YES"
										&& assetSelectedText == "YES") {
									$("#txtSensorNumber").prop("disabled", false);
									$("#txtSensorNumber").attr('required', true);
								}
								$scope.equipmentData.isPWSensorAttached=selectedValue;
							});
					
			
					 var commDate=null;
					 var decomDate=null;
					 
					 $("#commission").datepicker({
						 format:"dd-mm-yyyy"
					 })
					 
					  
					 
					 $("#decommission").datepicker({ todayHighlight:'TRUE',
						    autoclose: true,format:"dd-mm-yyyy"
			         });
					 
					 if ($scope.operation == 'NEW'){
						 $scope.addEquipment();
						 if($scope.originateFrom == 'Site'){
							 $scope.selectedSite = $.jStorage.get('selectedSite');
						 }
						 else{
							 $.jStorage.set('selectedSite', null);
							 $scope.selectedSite = null;
						 }
						 
						  
					 }
					 else if($scope.operation == 'EDIT'){
						 $scope.refreshing=false;
						 $scope.selectedAsset = $.jStorage.get('selectedAsset');
						 $scope.selectedSite = $.jStorage.get('selectedSite');
						 //console.log("get selected asset----");
						 //console.log($scope.selectedAsset);
						 editAsset();
					 }
					
			 });
			 
			 $scope.closeMessageWindow=function(){
				 $('#messageWindow').hide();
				 //$('#modalMessageDiv').hide();
				 $('#errorMessageDiv').hide();
 				 
			 }
			 
			 $scope.getImageFile=function(assetId, e){
				var ext = $('#inputImgfilepath').val().split(".").pop().toLowerCase();
				$scope.errorMessage = "";
				$scope.fileSize1 = null;
				$scope.fileExtension1 = ext;
				if ($.inArray(ext, [ "jpg", "jpeg", "JPEG",	"JPG", "png", "PNG" ]) == -1) {
					$scope.errorMessage = "Supported file types to upload are jpg and png";
					$('#messageWindow').show();
					$('#errorMessageDiv').show();
					$('#errorMessageDiv').alert();					
					// $('#fileerrorasset').text("Supported file types to upload are jpg and png");
					$scope.isfileselected = false;
					$('#inputImgfilepath').val(null);
					 e.target.files=undefined;
					return false;
				} else if (e.target.files != undefined) {
					$('#messageWindow').hide();
					$('#errorMessageDiv').hide();
					$scope.isfileselected = true;
					file = $('#inputImgfilepath').prop('files');
					$scope.fileSize1 = file[0].size / 1024;
					if($scope.fileSize1 > 100){
			        	 $scope.errorMessage="File size exceeds 100KB";
			        	 //$('#fileerrorasset').text("File size exceeds 100KB");
			        	 $('#messageWindow').show();
							$('#errorMessageDiv').show();
							$('#errorMessageDiv').alert();
				    	 $('#inputImgfilepath').val('');
				    	 $('#inputImgfilepath').val(null);
				    	 e.target.files=undefined;
						
			         }else{
						var reader = new FileReader();
						reader.onload = $scope.onImageFileUploadReader;
						reader.readAsDataURL(file[0]);
			         }
				}else{
					$scope.errorMessage="Invalid file format";
					 //$('#fileerrorasset').text("Invalid file format");
					 $('#messageWindow').show();
						$('#errorMessageDiv').show();
						$('#errorMessageDiv').alert();
			    	 $('#inputImgfilepath').val(null);
				}
			 }
			 
			 $scope.getDocumentFile=function(serviceId, e, errorDiv, msgDiv){
				 var documentId = serviceId.id
				 var ext = $('#' + documentId).val().split(".")	.pop().toLowerCase();
				$scope.fileSize2 = null;
				$scope.fileExtension2 = ext;
				if ($.inArray(ext, [ "PDF", "pdf"]) == -1) {
					$('#'+msgDiv).text("Supported file types to upload is pdf");
					$('#'+errorDiv).show();
					$('#'+errorDiv).alert();
					$scope.serviceModalErrorMessage = "";
					$scope.isfileselected = false;
					$('#' + documentId).val('');
					$('#' + documentId).val(null);
					 e.target.files=undefined;
					return false;
				} else if (e.target.files != undefined) {
					$('#'+errorDiv).hide();
					$scope.isfileselected = true;
					file = $('#' + documentId).prop('files');
					$scope.fileSize2 = file[0].size / 1024;
					if($scope.fileSize2 > 100){
			        	 $scope.serviceModalErrorMessage="";
			        	 $('#'+msgDiv).text("File size exceeds 100KB");
				    	 $('#'+errorDiv).show();
				    	 $('#' + documentId).val('');
				    	 $('#' + documentId).val(null);
				    	 e.target.files=undefined;
			         }else{
						var reader = new FileReader();
						reader.onload = $scope.onFileDocUploadReader;
						reader.readAsDataURL(file[0]);
			         }
				}else{
					 $('#fileerrorservice').text("Invalid file format");
					 $('#'+msgDiv).show();
			    	 $('#' + documentId).val('');
			    	 $('#' + documentId).val(null);
				}
			 }
			 
			 $scope.onImageFileUploadReader=function(e){
					var fileUrl = e.target.result;
					$scope.assetImageFile=e.target.result;
				}
			 
			 $scope.onFileDocUploadReader=function(e){
					var fileUrl = e.target.result;
					$scope.assetDocFile=e.target.result;
				}
			 
			 $scope.getLoggedInUserAccess =function(){
					authService.loggedinUserAccess()
		    		.then(function(data) {
		    			//console.log(data)
		    			if(data.statusCode == 200){
		    				$scope.sessionUser=data;
		    				$scope.getLoggedInUser($scope.sessionUser);
		    				
		    			}
		            },
		            function(data) {
		                console.log('Unauthorized Access.')
		            }); 
					
			    }
			 $scope.getLoggedInUser=function(loginUser){
					userService.getLoggedInUser(loginUser)
		    		.then(function(data) {
		    			//console.log(data)
		    			if(data.statusCode == 200){
		    				$scope.sessionUser=angular.copy(data.object);
		    				$scope.equipmentData.company=$scope.sessionUser.company;
		    				var siteId = $('#siteId').val();
		    				if(siteId==""){
		    					//$scope.getAllAsset();
		    				}else{
		    					
		    					$scope.getSelectedSiteAssets(siteId);
		    				}
		    				$scope.getModalPopUpData();
		    			}
		            },
		            function(data) {
		                console.log('No User available')
		            });
				}
			 
			 $scope.getModalPopUpData=function(){
				 var company = $scope.sessionUser.company;
				 //$scope.getServiceProviders();
 				$scope.retrieveAssetCategories();
 				$scope.getAssetLocations();
 				//$scope.testSaveAssetObject();
 				$scope.getUserSiteAccess();
			 }
			 
						 
			 $scope.getSelectedSiteAssets=function(selectedSite){
				 $('#loadingDiv').show();
				 assetService.getAssetBySite(selectedSite)
						.then(function(data) {
		    			//console.log(data);
		    				if(data.length>0){
		    				$scope.asset.list=[];
			    			$.each(data,function(key,val){
			    				$scope.asset.list.push(val);
			    				$('#messageWindow').hide();
			    				$('#infoMessageDiv').hide();
			    				$('#loadingDiv').hide();
			    			})
			    			$scope.getAssetDetails($scope.asset.list[0]);
		    			  }else{
		    				  $scope.InfoMessage="No assets available for the user"
									$('#messageWindow').show();
				    				$('#infoMessageDiv').show();
				    				$('#infoMessageDiv').alert();
				    				$('#loadingDiv').hide();
		    			  	}
		    				
			            },
			            function(data) {
			                console.log('Unable to get asset list')
			                	$scope.InfoMessage="No assets available for the user"
								$('#messageWindow').show();
			    				$('#infoMessageDiv').show();
			    				$('#infoMessageDiv').alert();
			    				$('#loadingDiv').hide();
			            });
				 }
			 
			 $scope.getAssetDetails=function(asset){
				 assetService.getAssetInfo(asset.assetId)
				 .then(function(data){
					if(data.statusCode == 200){
						$scope.selectedAsset=angular.copy(data.object);
						//console.log($scope.selectedAsset)
					} 
				 },function(data){
					 //console.log(data);
				 });
			
			
			}
			 
			 $scope.addEquipment=function(){
				 //$scope.operation = 'ADD';
				 $scope.HeaderName = "Create New Equipment";
				 $('#messageWindow').hide();
					$('#errorMessageDiv').hide();
					
				 $scope.equipmentData={};
				 $scope.equipmentData.sites=[];
				 //$scope.getModalPopUpData();
				 $('#resetAssetForm').click();
				 $("#categorySelect option").each(function() {
						if ($(this).val() == "") {
							$(this).attr('selected', 'selected');
							 $scope.assetCategory.selected.assetCategoryId=null;
							return false;
						}
				 	});
			 	
			 	$("#locationSelect option").each(function() {
					if ($(this).val() == "") {
						$(this).attr('selected', 'selected');
						 $scope.assetLocation.selected.locationId=null;
						return false;
					}
				});
			 	
			 	$("#spSelect option").each(function() {
					if ($(this).val() == "") {
						$(this).attr('selected', 'selected');
						 $scope.serviceProvider.selected.serviceProvderId=null;
						return false;
					}
				});
				 $scope.accessSite.selected={};
				 $("#siteSelect option").each(function(){
				 		if($(this).val() == ""){
				 			$(this).attr('selected', 'selected');
				 			 $scope.accessSite.selected.siteId=null;
							return false;
				 		}
				 	});
				 	$("#drpIsAsset option").each(function(){
			 		if($(this).val() == ""){
			 			$(this).attr('selected', 'selected');
			 			$scope.equipmentData.isAssetElectrical=null;
						return false;
			 		}
			 	});
			 	
			 	$("#drpIsPowersensor option").each(function(){
			 		if($(this).val() == ""){
			 			$(this).attr('selected', 'selected');
			 			$scope.equipmentData.isPWSensorAttached=null;
						return false;
			 		}
			 	});
				 $('#equipmentModal').modal('show');
				 $('#assetModalLabel').text("Add new Asset");
			 }
			  
			 
			 function editAsset(){
				 //$scope.operation ="EDIT";
				 var scope = angular.element("#equipmentcreateWindow").scope();
				 console.log('inside Edit---');
				 //console.log($scope.selectedAsset);
				 console.log($scope.selectedAsset);
				 var selectedSite = {
						 siteId:$scope.selectedAsset.siteId,
						 siteName:$scope.selectedAsset.siteName,
						 contactName:$scope.selectedAsset.contactName,
						 email:$scope.selectedAsset.email,
						 siteOwner:$scope.selectedAsset.siteOwner
				 }
				 $scope.selectedSite = selectedSite;
				 if($scope.selectedAsset.assetType == 'E'){
					
					 $('#inputImgfilepath').val('');
					 $('#inputDocfilepath').val('');
					 //$('#equipmentModal').modal('show');
					 //$('#assetModalLabel').text("Update Asset");
					 $scope.HeaderName = "Update Equipment";
						 //console.log($scope.selectedAsset);
						 $scope.equipmentData=angular.copy($scope.selectedAsset);
						 $scope.spType=$scope.selectedAsset.spType;
						//console.log($scope.selectedAsset.categoryId);
						 $scope.populateServiceProvider($scope.spType);
						 if($scope.selectedAsset.categoryId!=null){
							 $("#categorySelect").empty();							
							 
							 var options = $("#categorySelect");
		    					options.append($("<option />").val("").text(
								"Select Category"));
		    					$.each($scope.assetCategory.equipmentList,function() {
									options.append($("<option />").val(	this.assetCategoryId).text(	this.assetCategoryName));
								});
							 $("#categorySelect option").each(function() {
								 
								
								if ($(this).val() == $scope.selectedAsset.categoryId) {
									$(this).attr('selected', 'selected');
									$scope.assetCategory.selected.assetCategoryId=$scope.selectedAsset.categoryId;
									$scope.getRepairType($scope.assetCategory.selected);
									
									return false;
								}
						 	});
						 }
						 
						 
						 if($scope.selectedAsset.locationId!=null){
							 $("#locationSelect").empty();
							 var options = $("#locationSelect");
		    					options.append($("<option />").val("").text(
								"Select Location"));
		    					$.each($scope.assetLocation.list,function() {
									options.append($("<option />").val(	this.locationId).text(	this.locationName));
								});
							 $("#locationSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.locationId) {
									$(this).attr('selected', 'selected');
									$scope.assetLocation.selected.locationId= $scope.selectedAsset.locationId;
									return false;
								}
							});
						 }
					 	
						 if($scope.selectedAsset.serviceProviderId!=null){
							 $("#spSelect").empty();
							 var options = $("#spSelect");
		    					options.append($("<option />").val("").text(
								"Select Service Provider"));
		    					$.each($scope.serviceProvider.list,function() {
									options.append($("<option />").val(	this.serviceProviderId).text(this.name));
								});
						 	$("#spSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.serviceProviderId) {
									$(this).attr('selected', 'selected');
									$.each($scope.serviceProvider.list,function(key,val){
										if(val.serviceProviderId ==  $scope.selectedAsset.serviceProviderId){
											$scope.serviceProvider.selected.serviceProviderId = $scope.selectedAsset.serviceProviderId;
											return false;
										}
									});
									
									
								}
							});
						 }
					 	
						/* 	$("#drpIsAsset option").each(function(){
						 		if($(this).val() == $scope.selectedAsset.isAssetElectrical){
						 			$(this).attr('selected', 'selected');
						 			$scope.equipmentData.isAssetElectrical=$scope.selectedAsset.isAssetElectrical;
									return false;
						 		}
						 	});
					 	
						 	$("#drpIsPowersensor option").each(function(){
						 		if($(this).val() == $scope.selectedAsset.isPWSensorAttached){
						 			$(this).attr('selected', 'selected');
						 			$scope.equipmentData.isPWSensorAttached=$scope.selectedAsset.isPWSensorAttached;
									return false;
						 		}
						 	});
						 	*/
						 	
					 	if($scope.selectedAsset.siteId!=null){
					 		$("#siteSelect").empty();
					 		var options = $("#siteSelect");
	    					options.append($("<option />").val("").text("Select Site"));
	    					$.each($scope.accessSite.list,function() {
								options.append($("<option />").val(	this.siteId).text(this.siteName));
							});
						 	$("#siteSelect option").each(function(){
						 		if($(this).val() == $scope.selectedAsset.siteId){
						 			$(this).attr('selected', 'selected');
						 			$scope.accessSite.selected.siteId = $scope.selectedAsset.siteId;
									return false;
						 		}
						 	});
						 	 $('#siteSelect').removeAttr("multiple");
							 $('#siteSelect').attr("disable", true);
					 	}
					 	
					 	if($scope.selectedAsset.isAssetElectrical=="YES" && $scope.selectedAsset.isPWSensorAttached=="NO"){
					 		$('#txtSensorNumber').prop('disabled', true);
					 		$("#txtSensorNumber").attr('required', false);
					 		$("#drpIsAsset").val("YES");
					 		$("#drpIsPowersensor").val("NO");
					 		$scope.equipmentData.isAssetElectrical=$scope.selectedAsset.isAssetElectrical;
					 		$scope.equipmentData.isPWSensorAttached=$scope.selectedAsset.isPWSensorAttached;
					 		
					 	}
					 	else if($scope.selectedAsset.isAssetElectrical=="NO" && $scope.selectedAsset.isPWSensorAttached=="YES"){
					 		$('#txtSensorNumber').prop('disabled', false);
					 		$("#txtSensorNumber").attr('required', true);
					 		$("#drpIsAsset").val("NO");
					 		$("#drpIsPowersensor").val("YES");
					 		$scope.equipmentData.isAssetElectrical=$scope.selectedAsset.isAssetElectrical;
					 		$scope.equipmentData.isPWSensorAttached=$scope.selectedAsset.isPWSensorAttached;
					 		
					 	}
					 	else if($scope.selectedAsset.isAssetElectrical=="NO" && $scope.selectedAsset.isPWSensorAttached=="NO"){
					 		$('#txtSensorNumber').prop('disabled', true);
					 		$("#txtSensorNumber").attr('required', false);
					 		$("#drpIsAsset").val("NO");
					 		$("#drpIsPowersensor").val("NO");
					 		$scope.equipmentData.isAssetElectrical=$scope.selectedAsset.isAssetElectrical;
					 		$scope.equipmentData.isPWSensorAttached=$scope.selectedAsset.isPWSensorAttached;
					 		
					 	}else{
					 		$('#txtSensorNumber').prop('disabled',false);
					 		$("#txtSensorNumber").attr('required', true);
					 		$("#drpIsAsset").val($scope.selectedAsset.isAssetElectrical);
					 		$("#drpIsPowersensor").val($scope.selectedAsset.isPWSensorAttached);
					 		$scope.equipmentData.isAssetElectrical=$scope.selectedAsset.isAssetElectrical;
					 		$scope.equipmentData.isPWSensorAttached=$scope.selectedAsset.isPWSensorAttached;
					 	}
					 
					 }			 
				
			 	}
			 $scope.getUserSiteAccess=function(){
				 $('#loadingDiv').show();
				 userService.getUserSiteAccess()
					.then(function(data) {
		    			console.log(data);
		    			
		    				if(data.length>0){
		    					$scope.accessSite.list=[];
		    					$("#siteSelect").empty();
		    					$("#serviceSiteSelect").empty();
			    				$.each(data,function(key,val){
			    					var accessedSite={
				    						//accessId:val.accessId,
				    						//site:val.site,
				    						siteId:val.siteId,
				    						siteName:val.siteName
				    				}
			    					$scope.accessSite.list.push(accessedSite);
			    					 $('#loadingDiv').hide();
			    				});
			    				
			    				var options = $("#siteSelect");
		    					options.append($("<option />").val("").text("Select Site"));
		    					$.each($scope.accessSite.list,function() {
									options.append($("<option />").val(	this.siteId).text(this.siteName));
								});
		    					var options = $("#serviceSiteSelect");
		    					options.append($("<option />").val("").text("Select Site"));
		    					$.each($scope.accessSite.list,function() {
									options.append($("<option />").val(	this.siteId).text(this.siteName));
								});
		    				}
		    				
		    			
		            },
		            function(data) {
		                console.log('Unable to get access list')
		            });
			 }
			 $scope.populateServiceProvider=function(spType){
				 $scope.getServiceProviders(spType.toUpperCase());
			 }
			 
			 $scope.getServiceProviders=function(spType){
				 $('#loadingDiv').show();
					//serviceProviderService.getServiceProviderByCustomer(customer)
				 	serviceProviderService.getAllServiceProviders(spType)
					.then(function(data) {
		    			
		    			$scope.serviceProvider.list=[];
		    			$("#spSelect").empty();
		    			$("#serviceSPSelect").empty();
		    			if(data.statusCode == 200){
		    				$.each(data.object,function(key,val){
		    					$scope.serviceProvider.list.push(val);
		    				});
		    			}
		    			var options = $("#spSelect");
    					options.append($("<option />").val("").text(
						"Select Service Provider"));
    					$.each($scope.serviceProvider.list,function() {
							options.append($("<option />").val(	this.serviceProviderId).text(this.name));
						});
    					
    					 if($scope.selectedAsset.serviceProviderId!=null){
							/* $("#spSelect").empty();
							 var options = $("#spSelect");
		    					options.append($("<option />").val("").text(
								"Select Service Provider"));
		    					$.each($scope.serviceProvider.list,function() {
									options.append($("<option />").val(	this.serviceProviderId).text(this.name));
								});*/
						 	$("#spSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.serviceProviderId) {
									$(this).attr('selected', 'selected');
									$.each($scope.serviceProvider.list,function(key,val){
										if(val.serviceProviderId ==  $scope.selectedAsset.serviceProviderId){
											$scope.serviceProvider.selected.serviceProviderId = $scope.selectedAsset.serviceProviderId;
											return false;
										}
									});
									
									
								}
							});
						 }
    				/*	
    					var options = $("#serviceSPSelect");
    					options.append($("<option />").val("").text("Select Service Provider"));
    					$.each($scope.serviceProvider.list,function() {
							options.append($("<option />").val(	this.serviceProviderId).text(this.name));
						});*/
    					 $('#loadingDiv').hide();
		            },
		            function(data) {
		                console.log('Unable to get  Service Provider list')
		                $('#loadingDiv').hide();
		            });
				}
			 
			 $scope.retrieveAssetCategories=function(){
				 $('#loadingDiv').show();
				 assetService.retrieveAssetCategories()
				 .then(function(data) {
		    			
		    			$scope.assetCategory.equipmentList=[];
		    			$scope.assetCategory.serviceList = []
		    			
		    			$("#categorySelect").empty();
		    			$("#serviceCategorySelect").empty();
		    			$.each(data,function(key,val){
		    				if(val.assetType=='E'){
		    					$scope.assetCategory.equipmentList.push(val);
		    				}
		    				
		    				else if(val.assetType=='S'){
		    					$scope.assetCategory.serviceList.push(val);
		    				}
		    				
		    			});	
		    			var options = $("#categorySelect");
    					options.append($("<option />").val("").text(
						"Select Category"));
    					$.each($scope.assetCategory.equipmentList,function() {
							options.append($("<option />").val(	this.assetCategoryId).text(	this.assetCategoryName));
						});
    					
    					
    					var options = $("#serviceCategorySelect");
    					options.append($("<option />").val("").text(
						"Select Category"));
    					$.each($scope.assetCategory.serviceList,function() {
							options.append($("<option />").val(	this.assetCategoryId).text(	this.assetCategoryName));
						});
    					$("#repairtypeSelect").empty();
    					$scope.repairType.list=[];
    					
    					/*$scope.subRepairType.list=[];
		    			$("#subrepairtypeSelect").empty();*/
    					
    					$scope.serviceRepairType.list=[];
		    			$("#servicerepairtypeSelect").empty();
		    			
		    			/*$scope.serviceSubRepairType.list=[];
		    			$("#servicesubrepairtypeSelect").empty();*/
		    			
		    			if($scope.selectedAsset.categoryId!=null){
							 $("#categorySelect option").each(function() {
								 
								 
								if ($(this).val() == $scope.selectedAsset.categoryId) {
									$(this).attr('selected', 'selected');
									$scope.assetCategory.selected.assetCategoryId=$scope.selectedAsset.categoryId;
									$scope.getRepairType($scope.assetCategory.selected);
									
									return false;
								}
						 	});
						 }
		    			
    					 $('#loadingDiv').hide();
		            },
		            function(data) {
		                console.log('Asset Categories retrieval failed.')
		                $('#loadingDiv').hide();
		            });
			 }
			 
			 $scope.getRepairType=function(category){
				 $('#loadingDiv').show();
				 $("#repairtypeSelect").empty();
				 $("#subrepairtypeSelect").empty();
				 assetService.retrieveRepairTypes(category.assetCategoryId)
		    		.then(function(data) {
		    			
		    				$scope.repairType.list=[];
		    			$("#repairtypeSelect").empty();
		    				if(data.statusCode == 200){
		    					$.each(data.object,function(key,val){
		    						var repairType={
		    								subCategoryId1:val.subCategoryId1,
		    								assetSubcategory1:val.assetSubcategory1,
		    						}
		    						$scope.repairType.list.push(repairType);
		    					});
		    					var options = $("#repairtypeSelect");
		    					options.append($("<option />").val("").text("Select ComponentType"));
		    					$.each($scope.repairType.list,function() {
		    						options.append($("<option />").val(	this.subCategoryId1).text(this.assetSubcategory1));
		    					});
		    					
		    					if($scope.selectedAsset.subCategoryId1!=null &&  $scope.operation == 'EDIT'){
									 $("#repairtypeSelect option").each(function() {
										if ($(this).val() == $scope.selectedAsset.subCategoryId1) {
											$(this).attr('selected', 'selected');
											$scope.repairType.selected.subCategoryId1=$scope.selectedAsset.subCategoryId1;
											// $scope.getSubRepairType($scope.repairType.selected);
											return false;
										}
								 	});
								 }
		    				}
		    				
		    				 $('#loadingDiv').hide();
		            },
		            function(data) {
		                
		                $("#subrepairtypeSelect").empty();
		                $('#loadingDiv').hide();
		            });
				}
			
			
			 $scope.getSubRepairType=function(repairtype){
				 $('#loadingDiv').show();
				 $("#subrepairtypeSelect").empty();
				 assetService.retrieveSubRepairTypes(repairtype.subCategoryId1)
		    		.then(function(data) {
		    			
		    				$scope.subRepairType.list=[];
		    			$("#subrepairtypeSelect").empty();
		    				if(data.statusCode==200){
		    					$.each(data.object,function(key,val){
		    						var subRepairType={
		    								subCategoryId2:val.subCategoryId2,
		    								assetSubcategory2:val.assetSubcategory2,
		    						}
		    						$scope.subRepairType.list.push(subRepairType);
		    					});
		    					var options = $("#subrepairtypeSelect");
		    					options.append($("<option />").val("").text("Select SubComponentType"));
		    					$.each($scope.subRepairType.list,function() {
		    						options.append($("<option />").val(	this.subCategoryId2).text(this.assetSubcategory2));
		    					});
		    					
		    					if($scope.selectedAsset.subCategoryId2!=null && $scope.operation == 'EDIT'){
									 $("#subrepairtypeSelect option").each(function() {
										if ($(this).val() == $scope.selectedAsset.subCategoryId2) {
											$(this).attr('selected', 'selected');
											$scope.subRepairType.selected.subCategoryId2=$scope.selectedAsset.subCategoryId2;
											return false;
										}
								 	});
								 }
		    					
		    				}
		    				
		    				 $('#loadingDiv').hide();
							 
		            },
		            function(data) {
		                
		                $('#loadingDiv').hide();
		            });
				}
				
				
			 $scope.getAssetLocations=function(mode){
				 $('#loadingDiv').show();
				 assetService.getAssetLocations()
				 .then(function(data) {
		    			
		    			$scope.assetLocation.list=[];
		    			$("#locationSelect").empty();
		    			$("#serviceLocationSelect").empty();
		    			$.each(data,function(key,val){
		    				$scope.assetLocation.list.push(val);
		    				
		    			});	
		    			
		    			var options = $("#locationSelect");
    					options.append($("<option />").val("").text(
						"Select Location"));
    					$.each($scope.assetLocation.list,function() {
							options.append($("<option />").val(	this.locationId).text(	this.locationName));
						});
    					
    					 if($scope.selectedAsset.locationId!=null){
							/* $("#locationSelect").empty();
							 var options = $("#locationSelect");
		    					options.append($("<option />").val("").text(
								"Select Location"));
		    					$.each($scope.assetLocation.list,function() {
									options.append($("<option />").val(	this.locationId).text(	this.locationName));
								});*/
							 $("#locationSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.locationId) {
									$(this).attr('selected', 'selected');
									$scope.assetLocation.selected.locationId= $scope.selectedAsset.locationId;
									return false;
								}
							});
						 }
    					
    					/*var options = $("#serviceLocationSelect");
    					options.append($("<option />").val("").text(
						"Select Location"));
    					$.each($scope.assetLocation.list,function() {
							options.append($("<option />").val(	this.locationId).text(	this.locationName));
						});
		    			console.log($scope.assetLocation.list);*/
		    			 $('#loadingDiv').hide();
		            },
		            function(data) {
		                console.log('Asset Locations retrieval failed.')
		            });
				 
			 }
			 
			 $scope.IsValidDate = function (decommissionDate, commissionDate) {				 
					
				 var isValid = false;
				 
				 if(decommissionDate == null || decommissionDate == "" || typeof(decommissionDate) === "undefined"){
					 isValid = true;					 
				 }
				 else{					 
					 var isSame;
					 var part1 = decommissionDate.split('-');
					 var part2 = commissionDate.split('-');				 
					 decommissionDate =  new Date(part1[1]+'-'+part1[0]+'-'+part1[2]);
					 commissionDate =  new Date(part2[1]+'-'+ part2[0]+'-'+ part2[2]);				 
			         isValid = moment(decommissionDate).isAfter(commissionDate);
			          if (!isValid){
			        	  isValid = moment(decommissionDate).isSame(commissionDate);
			          }	
				 }		         	          
		          
		          return isValid;
		      }
			
						 
			 $scope.saveAssetEquipment=function(){				 	          
				 if($scope.operation=="NEW" && $scope.selectedSite == null){
					 $scope.equipmentData.sites = [];
					 for (var i = 0; i < $scope.accessSite.selected.length; i++) {
						 $scope.equipmentData.sites.push($scope.accessSite.selected[i].siteId)
					 }
				 }else if($scope.operation=="NEW" && $scope.selectedSite.siteId != null){
					 $scope.equipmentData.sites.push($scope.selectedSite.siteId);
				 } else if($scope.operation=="EDIT"){
					 $scope.equipmentData.sites=[];
					 $scope.equipmentData.sites.push($scope.selectedAsset.siteId);
				 }
		          if($scope.IsValidDate($scope.equipmentData.deCommissionedDate,$scope.equipmentData.commisionedDate)){
		        	  $('#messageWindow').hide();
		                $('#errorMessageDiv').hide();
		        	  //$('#equipmentModalMessageDiv').hide();
		        	  $scope.equipmentData.categoryId=$scope.assetCategory.selected.assetCategoryId;
						 $scope.equipmentData.locationId=$scope.assetLocation.selected.locationId;
						 $scope.equipmentData.subCategoryId1 = $scope.repairType.selected.subCategoryId1;
						// $scope.equipmentData.subCategoryId2 = $scope.subRepairType.selected.subCategoryId2;
						 
						 //$scope.equipmentData.siteId=$scope.accessSite.selected.siteId;
						 if($scope.serviceProvider.selected!=null){
							 $scope.equipmentData.serviceProviderId=$scope.serviceProvider.selected.serviceProviderId;
							 $scope.equipmentData.spType=$scope.spType;
						 }
						 
						 var assetImage={
									fileName:$scope.equipmentData.assetName,
									base64ImageString:$scope.assetImageFile,
									fileExtension: $scope.fileExtension1,
									size:$scope.fileSize1 || 0
								}
						 $scope.equipmentData.assetImage=assetImage;
						 var assetDoc={
									fileName:$scope.equipmentData.assetName,
									base64ImageString:$scope.assetDocFile,
									fileExtension: $scope.fileExtension2,
									size:$scope.fileSize2 || 0
									
								}
						 $scope.equipmentData.assetDoc = assetDoc;
						 if( $scope.equipmentData.assetImage.size > 100){
							 $scope.errorMessage="File size exceeds Max limit (100KB).";
							 $('#messageWindow').show();
				                $('#errorMessageDiv').show();
			    				$('#errorMessageDiv').alert();	
			                  $scope.isfileselected=false;
			                  return false;
			             }
						 if( $scope.equipmentData.assetDoc.size > 100){
							 $scope.errorMessage="File size exceeds Max limit (100KB).";
							 $('#messageWindow').show();
				                $('#errorMessageDiv').show();
			    				$('#errorMessageDiv').alert();	
			            	
			                  $scope.isfileselected=false;
			                  return false;
			             }else{
			            	 $scope.saveAssetInfo($scope.equipmentData);
			             }
	   				
	    				
		          }
		          else{
		        	  /*$scope.equipmentModalErrorMessage = "Decommission date should be after Commission date";
		                $('#equipmentModalMessageDiv').show();
	    				$('#equipmentModalMessageDiv').alert();	*/		
		        	  
		        	  $scope.errorMessage = "Decommission date should be after Commission date";
		        	  $('#messageWindow').show();
		                $('#errorMessageDiv').show();
	    				$('#errorMessageDiv').alert();	
		          }		          
				 
			 }
			 
			 
			 $scope.saveAssetInfo=function(assetData){
				 $('#loadingDiv').show();
				 assetService.saveAssetObject(assetData)
				 .then(function(data) {
		    			
		    			if(data.statusCode == 200){
		    				$scope.successMessage = data.message;
		    				$('#messageWindow').show();
		    				$('#successMessageDiv').show();
		    				$('#successMessageDiv').alert();
		    				$('#infoMessageDiv').hide();
		    				
		    				//$scope.getAllAsset();
		    				if($scope.originateFrom == "Asset" && $scope.operation=="NEW"){
		    					window.location.href=hostLocation+"/asset/details";
		    				}
		    				else if($scope.originateFrom == "Site" && $scope.operation=="NEW"){
		    					window.location.href=hostLocation+"/site/details";
		    				}
		    				
		    				$('#loadingDiv').hide();
		    			}
		            },
		            function(data) {
		            	 console.log('Error while saving asset data')
			              
			              //  $('#equipmentModalMessageDiv').show();
		 				//$('#equipmentModalMessageDiv').alert();
		 				if(assetData.assetType == 'E'){
		 					  $scope.equipmentModalErrorMessage = data.message;
		 					 $('#messageWindow').show();
		 					$('#errorMessageDiv').show();
		 					$('#errorMessageDiv').alert();
		 				}
		 				else if(assetData.assetType == 'S'){
		 					$scope.serviceModalErrorMessage = data.message;
		 					$('#serviceModalMessageDiv').show();
			    				$('#serviceModalMessageDiv').alert();
		 				}
		 				$('#loadingDiv').hide();
		            });
			 }
			
}]);
function validateDropdownValues(dropDownId, assetType){
	var scope = angular.element("#equipmentcreateWindow").scope();
	 var valueId = parseInt($("#"+dropDownId).val());
	 if(valueId == ""){
		 
	 }else{
		 if(assetType == 'E'){
				 if(dropDownId.toUpperCase() == "CATEGORYSELECT"){
					 var category={
							 assetCategoryId:parseInt($("#categorySelect").val()),
					 		 assetCategoryName:$("#categorySelect option:selected").text()
					 }
					 scope.assetCategory.selected = category;
					 scope.getRepairType(category);
					 
				 }
				 if(dropDownId.toUpperCase() == "REPAIRTYPESELECT"){
					 var repairtype={
							 subCategoryId1:parseInt($("#repairtypeSelect").val()),
							 assetSubcategory1:$("#repairtypeSelect option:selected").text()
					 }
					 scope.repairType.selected = repairtype;
					 //scope.getSubRepairType(repairtype);
				 }
				 /*if(dropDownId.toUpperCase() == "SUBREPAIRTYPESELECT"){
					 var subrepairtype={
							 subCategoryId2:parseInt($("#subrepairtypeSelect").val()),
							 assetSubcategory2:$("#subrepairtypeSelect option:selected").text()
					 }
					 scope.subRepairType.selected = subrepairtype;
					 
				 }
				 */
				 if(dropDownId.toUpperCase() == "LOCATIONSELECT"){
					 var location={
							 locationId:parseInt($("#locationSelect").val()),
					 		 locationName:$("#locationSelect option:selected").text()
					 }
					 scope.assetLocation.selected =location;
				 }
				 if(dropDownId.toUpperCase() == "SPSELECT"){
					 var serviceProvider={
							 serviceProviderId:parseInt($("#spSelect").val()),
					 		 serviceProviderName:$("#spSelect option:selected").text()
					 }
					 scope.serviceProvider.selected = serviceProvider;
				 }
				 if(dropDownId.toUpperCase() == "SITESELECT"){
					 var sites = $('#siteSelect option:selected');
					 
					 var selectedSitesId = $('#siteSelect').val();					 
					scope.selectedSites = [];
					 for (var i = 0; i < selectedSitesId.length; i++) {
						 if(selectedSitesId[i] != ""){
							 var Id = parseInt(selectedSitesId[i]);
							 var txt = $("#siteSelect option[value='"+Id+"']").text();
							 scope.selectedSites.push({
						            'siteId': Id,
						            'siteName': txt
						        });
						 }
						 
					    }
				 
					 
					 //console.log(scope.selectedSites);
					 scope.accessSite.selected =scope.selectedSites;
					 
				 }
				 scope.equipmentData.assetType=assetType;
		 }
	 }
}

