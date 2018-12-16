chrisApp.controller('servicecreateController', 
		 ['$rootScope', '$scope', '$filter','authService','userService',
		  'siteService','serviceProviderService','assetService',
		  function  ($rootScope, $scope , $filter,authService,userService,
				  siteService,serviceProviderService,assetService) {			 
			 
			 
			 //$scope.equipmentData ={};
			 $scope.serviceData ={};
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
					 var commDate=null;
					 var decomDate=null;
					 
					 $("#commission").datepicker({
						 format:"dd-mm-yyyy"
					 })					 
					  
					 
					 $("#decommission").datepicker({ todayHighlight:'TRUE',
						    autoclose: true,format:"dd-mm-yyyy"
			         });
					 
					 if ($scope.operation == 'NEW'){
						 $scope.HeaderName = "Create service";
						 $("#serviceSiteSelect").attr('required', true);
						 $scope.addService();
						 
						 if($scope.originateFrom == 'Site'){
							 $("#serviceSiteSelect").attr('required', false);
							 $scope.selectedSite = $.jStorage.get('selectedSite');
						 }
						 if($scope.originateFrom == 'Incident'){
							 $("#serviceSiteSelect").attr('required', false);
							 $scope.selectedSite = $.jStorage.get('selectedSite');
							if($scope.selectedSites.length>0){
								$.each($scope.selectedSites,function(key,val){
									if(val.siteId != $scope.selectedSite.siteId){
										$scope.selectedSites.push($scope.selectedSite);
										return false;
									}
								});
							}
							
							 $scope.getSelectedSiteData($scope.selectedSite.siteId);
							 $("#serviceSiteSelect option").each(function(){
						 		if($(this).val() == $scope.selectedSite.siteId){
						 			$(this).attr('selected', 'selected');
						 			$scope.accessSite.selected.siteId = $scope.selectedSite.siteId;
									return false;
						 		}
							 	});
							 
							 
						 }
						 else{
							 $.jStorage.set('selectedSite', null);
							 $scope.selectedSite = null;
						 }
					 }
					 else if($scope.operation == 'EDIT'){
						 $("#serviceSiteSelect").attr('required', false);
						 $scope.HeaderName = "Update service";
						 $scope.refreshing=false;
						 $scope.selectedAsset = $.jStorage.get('selectedAsset');						 
						 editAsset();
					 }
					
			 });
			 
			 $scope.getSelectedSiteData=function(siteId){
				 siteService.retrieveSiteDetails(siteId)
		    		.then(function(data) {
		    			console.log(data)
		    			var site=angular.copy(data.object);
						$scope.selectedSite=angular.copy(site);
						$scope.selectedAsset= $scope.selectedSite;
						//console.log($scope.selectedSite.siteAttachments);
		    		},function(data){
		    			console.log(data);
		    			
		    		})
			 }
			 
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
		    			
		    			if(data.statusCode == 200){
		    				$scope.sessionUser=angular.copy(data.object);
		    				//$scope.equipmentData.company=$scope.sessionUser.company;
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
				// $scope.getServiceProviders(company);
 				$scope.retrieveAssetCategories();
 				$scope.getAssetLocations();
 				//$scope.testSaveAssetObject();
 				$scope.getUserSiteAccess();
			 }
			 
			  
			 $scope.getSelectedSiteAssets=function(selectedSite){
				 $('#loadingDiv').show();
				 assetService.getAssetBySite(selectedSite)
						.then(function(data) {
		    			
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
						
					} 
				 },function(data){
					 
				 });
			
			
			}
			 
			
			 
			 $scope.addService=function(){
				 //$scope.operation = 'ADD';
				 $('#messageWindow').hide();
					$('#errorMessageDiv').hide();
					
				 $scope.serviceData={};
				 $scope.serviceData.sites=[];
				 //$scope.getModalPopUpData();
				 $('#resetServiceAssetForm').click();
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
						 $scope.serviceProvider.selected.serviceProviderId=null;
						return false;
					}
				});
			 	
			
			 	
			 	$("#siteSelect option").each(function(){
			 		if($(this).val() == ""){
			 			$(this).attr('selected', 'selected');
			 			 $scope.accessSite.selected.siteId=null;
						return false;
			 		}
			 	});
				 $('#serviceModal').modal('show');
				 $('#assetServiceModalLabel').text("Add new Asset");
			 }
			 
			 
			 function editAsset(){
				 //$scope.operation ="EDIT";
				 var scope = angular.element("#servicecreateWindow").scope();
				 
				 				 
				 if($scope.selectedAsset.assetType == 'S'){
					 //$('#assetServiceModalLabel').text("Update Asset");
					 $('#inputServiceDocfilepath').val('');
						 //console.log($scope.selectedAsset);
						 $scope.serviceData=angular.copy($scope.selectedAsset);
						 $scope.spType=$scope.selectedAsset.spType;
						 $scope.serviceData.spType=$scope.spType;
							 $scope.populateServiceProvider($scope.spType);
						 if($scope.selectedAsset.categoryId!=null){
							 $("#serviceCategorySelect").empty();
							 var options = $("#serviceCategorySelect");
		    					options.append($("<option />").val("").text(
								"Select Category"));
		    					$.each($scope.assetCategory.serviceList,function() {
									options.append($("<option />").val(	this.assetCategoryId).text(	this.assetCategoryName));
								});
		    					//$scope.assetCategory.selected = $scope.selectedAsset.categoryId
							 	$("#serviceCategorySelect option").each(function() {
									if ($(this).val() == $scope.selectedAsset.categoryId) {
										$(this).attr('selected', 'selected');
										$scope.assetCategory.selected.assetCategoryId = $scope.selectedAsset.categoryId;
										$scope.getServiceRepairType($scope.assetCategory.selected);
									return false;
								}
						 	});
						 }
						 
						/* if($scope.selectedAsset.serviceRepairTypeId!=null){
							 $("#servicerepairtypeSelect").empty();
							 var options = $("#servicerepairtypeSelect");
		    					options.append($("<option />").val("").text(
								"Select ComponentType"));
		    					$.each($scope.serviceRepairType.list,function() {
									options.append($("<option />").val(	this.repairId).text(	this.repairName));
								});
							 $("#servicerepairtypeSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.serviceRepairTypeId) {
									$(this).attr('selected', 'selected');
									$scope.serviceRepairType.selected.subCategoryId1=$scope.selectedAsset.assetSubcategory1;
									$scope.getServiceSubRepairType($scope.serviceRepairType.selected);
									return false;
								}
						 	});
						 }
						 if($scope.selectedAsset.serviceSubRepairTypeId!=null){
							 $("#servicesubrepairtypeSelect").empty();
							 var options = $("#servicesubrepairtypeSelect");
		    					options.append($("<option />").val("").text(
								"Select SubComponentType"));
		    					$.each($scope.serviceSubRepairType.list,function() {
									options.append($("<option />").val(	this.subCategoryId2).text(	this.assetSubcategory2));
								});
							 $("#servicesubrepairtypeSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.serviceSubRepairTypeId) {
									$(this).attr('selected', 'selected');
									$scope.serviceSubRepairType.selected.subCategoryId2=$scope.selectedAsset.subCategoryId2;
									return false;
								}
						 	});
						 }*/
						 
						 if($scope.selectedAsset.locationId!=null){
							 $("#serviceLocationSelect").empty();
							 var options = $("#serviceLocationSelect");
		    					options.append($("<option />").val("").text(
								"Select Location"));
		    					$.each($scope.assetLocation.list,function() {
									options.append($("<option />").val(	this.locationId).text(	this.locationName));
								});
						 	$("#serviceLocationSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.locationId) {
									$(this).attr('selected', 'selected');
									$scope.assetLocation.selected.locationId= $scope.selectedAsset.locationId;
									return false;
								}
							});
						 }
					 	if($scope.selectedAsset.serviceProviderId !=  null){
					 		$("#serviceSPSelect").empty();
					 		var options = $("#serviceSPSelect");
	    					options.append($("<option />").val("").text("Select Service Provider"));
	    					$.each($scope.serviceProvider.list,function() {
								options.append($("<option />").val(	this.serviceProviderId).text(this.name));
							});
						 	$("#serviceSPSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.serviceProviderId) {
									$(this).attr('selected', 'selected');
									$scope.serviceProvider.selected.serviceProviderId = $scope.selectedAsset.serviceProviderId;
									return false;
								}
							});
					 	}
					 	
					 	if($scope.selectedAsset.siteId !=  null){
					 		 $("#serviceSiteSelect").empty();
					 		var options = $("#serviceSiteSelect");
	    					options.append($("<option />").val("").text("Select Site"));
	    					$.each($scope.accessSite.list,function() {
								options.append($("<option />").val(	this.siteId).text(this.siteName));
							});
						 	$("#serviceSiteSelect option").each(function(){
						 		if($(this).val() == $scope.selectedAsset.siteId){
						 			$(this).attr('selected', 'selected');
						 			$scope.accessSite.selected.siteId = $scope.selectedAsset.siteId;
									return false;
						 		}
						 	});
					 	}
					 	$('#serviceModal').modal('show');
					 }
			 	}
			 $scope.getUserSiteAccess=function(){
				 $('#loadingDiv').show();
				 userService.getUserSiteAccess()
					.then(function(data) {
		    			
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
				 $scope.serviceData.spType=spType.toUpperCase();
			 }
			 
			 $scope.getServiceProviders=function(spType){
				 $('#loadingDiv').show();
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
    					
    					var options = $("#serviceSPSelect");
    					options.append($("<option />").val("").text("Select Service Provider"));
    					$.each($scope.serviceProvider.list,function() {
							options.append($("<option />").val(	this.serviceProviderId).text(this.name));
						});
    					
					 	if($scope.selectedAsset.serviceProviderId !=  null){
					 		/*$("#serviceSPSelect").empty();
					 		var options = $("#serviceSPSelect");
	    					options.append($("<option />").val("").text("Select Service Provider"));
	    					$.each($scope.serviceProvider.list,function() {
								options.append($("<option />").val(	this.serviceProviderId).text(this.name));
							});*/
						 	$("#serviceSPSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.serviceProviderId) {
									$(this).attr('selected', 'selected');
									$scope.serviceProvider.selected.serviceProviderId = $scope.selectedAsset.serviceProviderId;
									return false;
								}
							});
					 	}
					 	
					 	if($scope.selectedAsset.siteId !=  null){
					 		 $("#serviceSiteSelect").empty();
					 		var options = $("#serviceSiteSelect");
	    					options.append($("<option />").val("").text("Select Site"));
	    					$.each($scope.accessSite.list,function() {
								options.append($("<option />").val(	this.siteId).text(this.siteName));
							});
						 	$("#serviceSiteSelect option").each(function(){
						 		if($(this).val() == $scope.selectedAsset.siteId){
						 			$(this).attr('selected', 'selected');
						 			$scope.accessSite.selected.siteId = $scope.selectedAsset.siteId;
									return false;
						 		}
						 	});
					 	}
    					
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
							 	$("#serviceCategorySelect option").each(function() {
									if ($(this).val() == $scope.selectedAsset.categoryId) {
										$(this).attr('selected', 'selected');
										$scope.assetCategory.selected.assetCategoryId = $scope.selectedAsset.categoryId;
										$scope.getServiceRepairType($scope.assetCategory.selected);
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
			 
			
			
				$scope.getServiceRepairType=function(category){
					 $('#loadingDiv').show();
					 $("#servicerepairtypeSelect").empty();
					 $("#servicesubrepairtypeSelect").empty();
					 assetService.retrieveRepairTypes(category.assetCategoryId)
			    		.then(function(data) {
			    			
			    				$scope.serviceRepairType.list=[];
			    			$("#servicerepairtypeSelect").empty();
			    			if(data.statusCode == 200){
			    					$.each(data.object,function(key,val){
			    						var repairType={
			    								subCategoryId1:val.subCategoryId1,
			    								assetSubcategory1:val.assetSubcategory1,
			    						}
			    						$scope.serviceRepairType.list.push(repairType);
			    					});
			    					var options = $("#servicerepairtypeSelect");
			    					options.append($("<option />").val("").text("Select ComponentType"));
			    					$.each($scope.serviceRepairType.list,function() {
			    						options.append($("<option />").val(	this.subCategoryId1).text(this.assetSubcategory1));
			    					});
			    					
			    					if($scope.selectedAsset.subCategoryId1!=null && $scope.operation == 'EDIT'){
										 $("#servicerepairtypeSelect option").each(function() {
											if ($(this).val() == $scope.selectedAsset.subCategoryId1) {
												$(this).attr('selected', 'selected');
												$scope.serviceRepairType.selected.subCategoryId1=$scope.selectedAsset.subCategoryId1;
												// $scope.getServiceSubRepairType($scope.serviceRepairType.selected);
												return false;
											}
									 	});
									 }
			    				}
			    			
			    			 $('#loadingDiv').hide();
			            },
			            function(data) {
			                
			                $("#servicesubrepairtypeSelect").empty();
			                $('#loadingDiv').hide();
			            });
					}
			 
				
				$scope.getServiceSubRepairType=function(servicerepairtype){
					 $('#loadingDiv').show();
					 $("#servicesubrepairtypeSelect").empty();
					 
					 assetService.retrieveSubRepairTypes(servicerepairtype.subCategoryId1)
			    		.then(function(data) {
			    			
			    				$scope.serviceSubRepairType.list=[];
			    			$("#servicesubrepairtypeSelect").empty();
			    			    if(data.statusCode==200){
			    					$.each(data.object,function(key,val){
			    						var subRepairType={
			    								subCategoryId2:val.subCategoryId2,
			    								assetSubcategory2:val.assetSubcategory2,
			    						}
			    						$scope.serviceSubRepairType.list.push(subRepairType);
			    					});
			    					var options = $("#servicesubrepairtypeSelect");
			    					options.append($("<option />").val("").text("Select SubComponentType"));
			    					$.each($scope.serviceSubRepairType.list,function() {
			    						options.append($("<option />").val(	this.subCategoryId2).text(this.assetSubcategory2));
			    					});
			    					
			    					if($scope.selectedAsset.subCategoryId2!=null && $scope.operation == 'EDIT'){
										 $("#servicesubrepairtypeSelect option").each(function() {
											if ($(this).val() == $scope.selectedAsset.subCategoryId2) {
												$(this).attr('selected', 'selected');
												$scope.serviceSubRepairType.selected.subCategoryId2=$scope.selectedAsset.subCategoryId2;
												
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
    					
    					var options = $("#serviceLocationSelect");
    					options.append($("<option />").val("").text(
						"Select Location"));
    					$.each($scope.assetLocation.list,function() {
							options.append($("<option />").val(	this.locationId).text(	this.locationName));
						});
		    			
		    			
		    			 if($scope.selectedAsset.locationId!=null){
						 	$("#serviceLocationSelect option").each(function() {
								if ($(this).val() == $scope.selectedAsset.locationId) {
									$(this).attr('selected', 'selected');
									$scope.assetLocation.selected.locationId= $scope.selectedAsset.locationId;
									return false;
								}
							});
						 }
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
			
						 
			 
			 
			 $scope.saveAssetService =function(){
				 if($scope.operation=="NEW" && $scope.selectedSite == null){
				 	$scope.serviceData.sites = [];
					 for (var i = 0; i < $scope.accessSite.selected.length; i++) {
						 $scope.serviceData.sites.push($scope.accessSite.selected[i].siteId)
					 }
					 }
				 else if($scope.operation=="NEW" && $scope.selectedSite.siteId != null){
					 $scope.serviceData.sites.push($scope.selectedSite.siteId);
				 }
				 else if($scope.operation=="EDIT"){
						 $scope.serviceData.sites.push($scope.selectedAsset.siteId);
					 }
				
				 if($scope.IsValidDate($scope.serviceData.deCommissionedDate,$scope.serviceData.commisionedDate)){
					 $('#messageWindow').hide();
	 					$('#errorMessageDiv').hide();
	 					
		        	  $scope.serviceData.categoryId=$scope.assetCategory.selected.assetCategoryId;
		        	  
		        	  	$scope.serviceData.subCategoryId1 = $scope.serviceRepairType.selected.subCategoryId1;
						 //$scope.serviceData.subCategoryId2 = $scope.serviceSubRepairType.selected.subCategoryId2;
						 $scope.serviceData.locationId=$scope.assetLocation.selected.locationId;
						 
						 //$scope.serviceData.siteId=$scope.accessSite.selected.siteId;
						 if($scope.serviceProvider.selected!=null){
						 $scope.serviceData.serviceProviderId=$scope.serviceProvider.selected.serviceProviderId;
						 }
						 //
						 
						 var serviceDoc={
									fileName:$scope.serviceData.assetName,
									base64ImageString:$scope.assetDocFile,
									fileExtension: $scope.fileExtension2,
									size:$scope.fileSize2 || 0
								}
						 $scope.serviceData.assetDoc = serviceDoc;
						 
						 if($scope.serviceData.assetDoc.size > 100){
			            	 $scope.serviceModalErrorMessage="File size exceeds Max limit (100KB).";
			            	 $('#messageWindow').show();
			 					$('#errorMessageDiv').show();
			 					$('#errorMessageDiv').alert();
			                  $scope.isfileselected=false;
			                  return false;
			             }else{
			            	 $scope.saveAssetInfo($scope.serviceData);
			             }
	    				//console.log($scope.serviceData);
		          }
		          else{
		        	  /*$scope.modalErrorMessage = "Decommission date should be after Commission date";
		                $('#serviceModalMessageDiv').show();
	    				$('#serviceModalMessageDiv').alert();*/	
		        	  
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
		    				if($scope.originateFrom == "Asset" && $scope.operation == 'NEW'){
		    					window.location.href=hostLocation+"/asset/details";
		    				}
		    				else if($scope.originateFrom == "Site" && $scope.operation == 'NEW'){
		    					window.location.href=hostLocation+"/site/details";
		    				}	
		    				
		    				else if($scope.originateFrom == "Incident" && $scope.operation == 'NEW'){
		    					window.location.href=hostLocation+"/incident/details/create";
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
		 					$('#equipmentModalMessageDiv').show();
			    				$('#equipmentModalMessageDiv').alert();
		 				}
		 				else if(assetData.assetType == 'S'){
		 					$scope.serviceModalErrorMessage = data.message;
		 					$('#messageWindow').show();
		 					$('#errorMessageDiv').show();
		 					$('#errorMessageDiv').alert();
		 				}
		 				$('#loadingDiv').hide();
		            });
			 }
			 
	
			 			 
				$scope.deleteFile=function(feature, file, type){
					
					 $('#loadingDiv').show();
					 var fileStr ="";
					 if($scope.selectedAsset.imagePath!=null){
						 fileStr = $scope.selectedAsset.imagePath;
					 }
					 if($scope.selectedAsset.documentPath!=null){
						 fileStr = $scope.selectedAsset.documentPath;
					 }
					if(fileStr!=""){ 
						assetService.deleteFileAttached(feature, $scope.selectedAsset.assetId, type)
						.then(function(data){
							
							if(data.statusCode==200){
									 //$scope.getAllAsset();
							}
							$('#loadingDiv').hide();
						},function(data){
							
							$('#loadingDiv').hide();
						});
					}
				}
			 
			
}]);
function validateDropdownValues(dropDownId, assetType){
	var scope = angular.element("#servicecreateWindow").scope();
	 var valueId = parseInt($("#"+dropDownId).val());
	 if(valueId == ""){
		 
	 }else{
		 if(assetType == 'S'){
			 if(dropDownId.toUpperCase() == "SERVICECATEGORYSELECT"){
				 var category={
						 assetCategoryId:parseInt($("#serviceCategorySelect").val()),
				 		 assetCategoryName:$("#serviceCategorySelect option:selected").text()
				 }
				 scope.assetCategory.selected = category;
				 scope.getServiceRepairType(category);
				 
			 }
			 if(dropDownId.toUpperCase() == "SERVICEREPAIRTYPESELECT"){
				 var servicerepairtype={
						 subCategoryId1:parseInt($("#servicerepairtypeSelect").val()),
						 assetSubcategory1:$("#servicerepairtypeSelect option:selected").text()
				 }
				 scope.serviceRepairType.selected = servicerepairtype;
				// scope.getServiceSubRepairType(servicerepairtype);
			 }
			/* if(dropDownId.toUpperCase() == "SERVICESUBREPAIRTYPESELECT"){
				 var servicesubrepairtype={
						 subCategoryId2:parseInt($("#servicesubrepairtypeSelect").val()),
						 assetSubcategory2:$("#servicesubrepairtypeSelect option:selected").text()
				 }
				 scope.serviceSubRepairType.selected = servicesubrepairtype;
				 
			 }*/
			 if(dropDownId.toUpperCase() == "SERVICELOCATIONSELECT"){
				 var location={
						 locationId:parseInt($("#serviceLocationSelect").val()),
				 		 locationName:$("#serviceLocationSelect option:selected").text()
				 }
				 scope.assetLocation.selected =location;
			 }
			 if(dropDownId.toUpperCase() == "SERVICESPSELECT"){
				 var serviceProvider={
						 serviceProviderId:parseInt($("#serviceSPSelect").val()),
				 		 serviceProviderName:$("#serviceSPSelect option:selected").text()
				 }
				 
				 scope.serviceProvider.selected = serviceProvider;
			 }
			 if(dropDownId.toUpperCase() == "SERVICESITESELECT"){
				/* var site={
						 siteId:parseInt($("#serviceSiteSelect").val()),
				 		 siteName:$("#siteSelect option:selected").text()
				 }
				 scope.accessSite.selected =site;*/
				 var sites = $('#serviceSiteSelect option:selected');
				 
				 var selectedSitesId = $('#serviceSiteSelect').val();					 
				  scope.selectedSites = [];
				 for (var i = 0; i < selectedSitesId.length; i++) {
					 if(selectedSitesId[i] != ""){
						 var Id = parseInt(selectedSitesId[i]);
						 var txt = $("#serviceSiteSelect option[value='"+Id+"']").text();
						 scope.selectedSites.push({
					            'siteId': Id,
					            'siteName': txt
					        });
					 }
					 
				    }					 
				 
				 //console.log(scope.selectedSites);
				 scope.accessSite.selected =scope.selectedSites;
				 
			 }
			 scope.serviceData.assetType=assetType;
		 }
	 }
}

