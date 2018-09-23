

chrisApp.controller('siteController',  ['$rootScope', '$scope', '$filter','siteService','authService',
                                        'siteCreationService','companyService','userService','districtService',
                                        'areaService','clusterService','countryService','assetService','serviceProviderService',
                              function  ($rootScope, $scope , $filter,siteService, authService,
                                        siteCreationService,companyService,userService,districtService,
                                        areaService,clusterService,countryService,assetService,serviceProviderService) {
		
		$scope.siteData={};
		$scope.sessionUser={};
		$scope.selectedArea={};
		$scope.area={
				selected:{},
				list:[]
		}
		$scope.brand={
				selected:{},
				list:[]
		}
		$scope.brand.list=[
		         {'brandId':'1','brandName':'BP'},
		         {'brandId':'2','brandName':'Esso'},
		         {'brandId':'3','brandName':'Shell'}
			];
		
		
		
		$scope.district={
				selected:{},
				list:[]
		}
		$scope.cluster={
				selected:{},
				list:[]
		}
		$scope.salesTimes={
			selected:{},
			days:null,
			fromlist:[],
			toList:[]
		}
		$scope.deliveryTimes={
				selected:{},
				days:null,
				fromlist:[],
				toList:[]
		}
		
		$scope.accessSite={
				 selected:{},
				 list:[]
		 }
		
		$scope.serviceProvider={
				 selected:{},
				 list:[]
		 }
		$scope.assetCategory={
				 selected:{},
				 equipmentList:[],
				 serviceList:[]
				 
		 }
		 $scope.assetLocation={
				 selected:{},
				 list:[]
		 }
		
		$scope.repairType={
				 selected:{},
				 list:[]
		 }
		 $scope.subRepairType={
				 selected:{},
				 list:[]					 
				 
		 }
		 $scope.serviceRepairType={
				 selected:{},
				 list:[]
		 }
		 $scope.serviceSubRepairType={
				 selected:{},
				 list:[]					 
				 
		 }
		
		$scope.siteAssignedUserList=[];
		$scope.siteUnAssignedUserList=[];
		$scope.fileExtension="";
		$scope.licenseAttachments=[];
		$scope.licenseDetails = [];
		$rootScope.sitePage={};
		               	      
		angular.element(document).ready(function(){
			$scope.selectedSite={};
			$scope.selectedSite.fileInput=null;
			var appVersion =  $.jStorage.get("selectedLanguage");
			$rootScope.language = $.jStorage.get("lang");
			$scope.originateFromPage = "Site";
			 $.jStorage.set('originateFrom', $scope.originateFromPage);
			
			$scope.enableDisableIsAssetPowersensor();
			$scope.getLoggedInUserAccess();
			 $(".licensedate").datepicker({
				 format:'dd-mm-yyyy',
			 });
			 
			 var time=0.00;
				var daysList = ['Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'];
				var dayLength=7;
				for(i=0;i<24;i++){
					time=time+1;
					
					$scope.salesTimes.fromlist.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					$scope.salesTimes.toList.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					
					$scope.deliveryTimes.fromlist.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					$scope.deliveryTimes.toList.push({
						opId:null,
						days:daysList[7-dayLength],
						val:time+":00",
						text:time+":00"
					});
					dayLength=dayLength-1;
				}
				$scope.getOperationDetails();
			
				
				 $('#siteInputFile').change( function(e) {
		             var ext=$('input#siteInputFile').val().split(".").pop().toLowerCase();
		             var fileSize = Math.round((this.files[0].size / 1024)) ;
		             $scope.siteFileSize=null;
		             $scope.fileExtension = ext;
		             if($.inArray(ext, ["jpg","jpeg","JPEG", "JPG","PDF","pdf","png","PNG"]) == -1) {
		            	 $scope.modalErrorMessage="";
				    	 $('#fileerror').text("Supported File types are jpg, png and pdf");
				    	 $('#modalSiteMessageDiv').show();
				          $scope.isfileselected=false;
				          $('#siteInputFile').val('');
				          $('#siteInputFile').val(null);
				          e.target.files=undefined;
		                 return false;
		             }
		             else {
		            	 if (e.target.files != undefined) {
		                  	 $('#modalSiteMessageDiv').hide();
			                 $scope.isfileselected=true;
			                 file = $('#siteInputFile').prop('files');
			                 $scope.siteFileSize = file[0].size / 1024;
						         if($scope.siteFileSize > 100){
						        	 $scope.modalErrorMessage="";
						        	 $('#fileerror').text("File size exceeds 100KB");
							    	 $('#modalSiteMessageDiv').show();
							          e.target.files=undefined;
									
						         }else{
				                  	 var reader = new FileReader();
				                 	 reader.onload = $scope.onSiteFileUploadReader;
				                 	 reader.readAsDataURL(file[0]);
						         }
			              }
		             }
		             
				 });
				 
				 
				 
				
		});
	
		$scope.onSiteFileUploadReader=function(e){
			var fileUrl = e.target.result;
			//console.log(fileUrl);
			$scope.uploadedFile=e.target.result;
		}
		
		 $scope.onImageFileUploadReader=function(e){
				var fileUrl = e.target.result;
				$scope.assetImageFile=e.target.result;
			}
		 
		 
		 $scope.onFileDocUploadReader=function(e){
				var fileUrl = e.target.result;
				$scope.assetDocFile=e.target.result;
			}
		
		 
		 $scope.getImageFile=function(assetId, e){
				var ext = $('#inputImgfilepath').val().split(".").pop().toLowerCase();
				$scope.fileSize1 = null;
				$scope.fileExtension1 = ext;
				if ($.inArray(ext, [ "jpg", "jpeg", "JPEG",	"JPG", "png", "PNG" ]) == -1) {
					$('#equipmentModalMessageDiv').show();
					$('#equipmentModalMessageDiv').alert();
					$scope.equipmentModalErrorMessage = "";
					 $('#fileerrorasset').text("Supported File types are jpg and png");
					$scope.isfileselected = false;
					$('#inputImgfilepath').val('');
					$('#inputImgfilepath').val(null);
					return false;
				} else if (e.target.files != undefined) {
					$('#equipmentModalMessageDiv').hide();
					$scope.isfileselected = true;
					file = $('#inputImgfilepath').prop('files');
					$scope.fileSize1 = file[0].size / 1024;
					if($scope.fileSize1 > 100){
			        	 $scope.equipmentModalErrorMessage="";
			        	 $('#fileerrorasset').text("File size exceeds 100KB");
				    	 $('#equipmentModalMessageDiv').show();
						
			         }else{
						var reader = new FileReader();
						reader.onload = $scope.onImageFileUploadReader;
						reader.readAsDataURL(file[0]);
			         }
				}else{
					 $('#fileerrorasset').text("Invalid file format");
			    	 $('#equipmentModalMessageDiv').show();
			    		$('#inputImgfilepath').val('');
			    	 $('#inputImgfilepath').val(null);
				}
			 }
			 
		 $scope.getDocumentFile=function(serviceId, e, errorDiv, msgDiv){
			 var documentId = serviceId.id
			 var ext = $('#' + documentId).val().split(".")	.pop().toLowerCase();
			$scope.fileSize2 = null;
			$scope.fileExtension2 = ext;
			if ($.inArray(ext, [ "PDF", "pdf" ]) == -1) {
				$('#'+msgDiv).text("Supported file types to upload is pdf");
				$('#'+errorDiv).show();
				$('#'+errorDiv).alert();
				$scope.serviceModalErrorMessage = "";
				$scope.isfileselected = false;
				$('#' + documentId).val('');
				$('#' + documentId).val(null);
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
		
		$scope.getIndexedName=function(val, e, licenseSelected){
			//console.log(val.id);
			var licenseId=val.id;
			 $scope.indexPos=licenseId.charAt(licenseId.length-1);
			$scope.licenseId=licenseId;
			//console.log($('#'+licenseSelected+$scope.indexPos).val());
			if($('#'+licenseSelected+$scope.indexPos).val()==""){
				$scope.licenseInfo=null;
			}else{
				$scope.licenseInfo=parseInt($('#'+licenseSelected+$scope.indexPos).val());
			}
			 var ext=$("input#"+licenseId).val().split(".").pop().toLowerCase();
			 //console.log($('#'+licenseId).val());
		     $scope.fileExtension = ext;
		     if($.inArray(ext, ["PDF","pdf"]) == -1) {
		    	 $scope.modalErrorMessage="Supported file types to upload is pdf";
		    	 $('#fileerror').text("Supported File type is pdf");
		    	 $('#modalSiteMessageDiv').show();
		    	 $('#'+licenseId).val('');
		    	 $('#'+licenseId).val(null);
		    	 e.target.files=undefined;
		          $scope.isfileselected=false;
		     }else if (e.target.files != undefined) {
		         $scope.isfileselected=true;
		         file = $('#'+licenseId).prop('files');
		         var fileSize= file[0].size / 1024;
		         if(fileSize > 100){
		        	 $scope.modalErrorMessage="";
		        	 $('#fileerror').text("File size exceeds 100KB");
			    	 $('#modalSiteMessageDiv').show();
			    	 e.target.files=undefined;
			    	 $('#'+licenseId).val('');
			    	 $('#'+licenseId).val(null);
		         }else{
			      	 var reader = new FileReader();
			     	 reader.onload = $scope.onLicenseFileUploadReader;
			     	 reader.readAsDataURL(file[0]);
		         }
		       }else{
					 $('#fileerror').text("Invalid file format");
			    	 $('#modalSiteMessageDiv').show();
			    	 $('#'+licenseId).val('');
			    	 $('#'+licenseId).val(null);
				}
			
		}
		
		
		$scope.onLicenseFileUploadReader=function(e){
			var fileUrl = e.target.result;
	    	 var fileSize = Math.round((e.total / 1024)) ;
			 var licenseImage={
						licenseId:$scope.licenseInfo,
						fileName:$('#licenseDetail'+$scope.indexPos).val(),
						base64ImageString:e.target.result,
						fileExtension: $scope.fileExtension,
						imgPos:$scope.indexPos,
						fileSize : fileSize
					}
			if($scope.licenseAttachments.length==0){
				 $scope.licenseAttachments.push(licenseImage);
			}else{
				 $.each($scope.licenseAttachments,function(key,val){
					 if($scope.indexPos == val.imgPos){
						 $scope.licenseAttachments.splice($scope.indexPos,1)
		 					return false;
		 				}
				 });
				 $scope.licenseAttachments.push(licenseImage);
			}
			 //console.log($scope.licenseAttachments);
		}
		
		$scope.enableDisableIsAssetPowersensor=function(){
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
		}
		
		/* $scope.addEquipment=function(){
			 $scope.equipmentData={};
			 $scope.equipmentData.sites=[];
			 $('#resetAssetForm').click();
			 $("#categorySelect option").each(function() {
					if ($(this).val() == "") {
						$(this).attr('selected', 'selected');
						 $scope.assetCategory.selected=null;
						return false;
					}
			 	});
		 	
		 	$("#locationSelect option").each(function() {
				if ($(this).val() == "") {
					$(this).attr('selected', 'selected');
					 $scope.assetLocation.selected=null;
					return false;
				}
			});
		 	
		 	$("#spSelect option").each(function() {
				if ($(this).val() == "") {
					$(this).attr('selected', 'selected');
					 $scope.serviceProvider.selected=null;
					return false;
				}
			});
			 $scope.accessSite.selected={};
			 $("#siteSelect option").each(function(){
			 		if($(this).val() == ""){
			 			$(this).attr('selected', 'selected');
			 			 $scope.accessSite.selected=null;
						return false;
			 		}
			 	});
		 	$("#siteSelect option").each(function(){
		 		if($(this).val() == $scope.accessSite.siteId){
		 			$(this).attr('selected', 'selected');
		 			$scope.accessSite.selected.siteId = $scope.accessSite.siteId;
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
		 	
		 	$scope.getServiceProviders($scope.sessionUser.company);
			$scope.retrieveAssetCategories();
			$scope.getAssetLocations();
		 	$scope.populateSelectedSite();
		 	
			 $('#equipmentModal').modal('show');
			 $('#assetModalLabel').text("Add new Asset");
		 }*/
		 
		/* $scope.addService=function(){
			 $scope.serviceData={};
			 $scope.serviceData.sites=[];
			 $('#resetServiceAssetForm').click();
				$("#categorySelect option").each(function() {
					if ($(this).val() == "") {
						$(this).attr('selected', 'selected');
						 $scope.assetCategory.selected=null;
						return false;
					}
			 	});
		 	
		 	$("#locationSelect option").each(function() {
				if ($(this).val() == "") {
					$(this).attr('selected', 'selected');
					 $scope.assetLocation.selected=null;
					return false;
				}
			});
		 	
		 	$("#spSelect option").each(function() {
				if ($(this).val() == "") {
					$(this).attr('selected', 'selected');
					 $scope.serviceProvider.selected=null;
					return false;
				}
			});
		 	
		
		 	
		 	$("#siteSelect option").each(function(){
		 		if($(this).val() == ""){
		 			$(this).attr('selected', 'selected');
		 			 $scope.accessSite.selected=null;
					return false;
		 		}
		 	});
		 	
		 	$scope.getServiceProviders($scope.sessionUser.company);
			$scope.retrieveAssetCategories();
			$scope.getAssetLocations();
		 	$scope.populateSelectedSite();
		 	
			 $('#serviceModal').modal('show');
			 $('#assetServiceModalLabel').text("Add new Asset");
		 }*/
		
		 $scope.retrieveAssetCategories=function(){
			 $('#loadingDiv').show();
			 assetService.retrieveAssetCategories()
			 .then(function(data) {
	    			//console.log(data);
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
	    			//console.log(data)
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
	    					
	    					/*if($scope.selectedAsset.subCategoryId1!=null){
								 $("#repairtypeSelect option").each(function() {
									if ($(this).val() == $scope.selectedAsset.subCategoryId1) {
										$(this).attr('selected', 'selected');
										$scope.repairType.selected.subCategoryId1=$scope.selectedAsset.subCategoryId1;
										 $scope.getSubRepairType($scope.repairType.selected);
										return false;
									}
							 	});
							 }*/
	    				}
	    				
	    				 $('#loadingDiv').hide();
	            },
	            function(data) {
	                //console.log(data)
	                $("#subrepairtypeSelect").empty();
	                $('#loadingDiv').hide();
	            });
			}
		 
			$scope.getServiceRepairType=function(category){
				 $('#loadingDiv').show();
				 $("#servicerepairtypeSelect").empty();
				 $("#servicesubrepairtypeSelect").empty();
				 assetService.retrieveRepairTypes(category.assetCategoryId)
		    		.then(function(data) {
		    			//console.log(data)
		    				$scope.repairType.list=[];
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
		    					
		    					/*if($scope.selectedAsset.subCategoryId1!=null){
									 $("#servicerepairtypeSelect option").each(function() {
										if ($(this).val() == $scope.selectedAsset.subCategoryId1) {
											$(this).attr('selected', 'selected');
											$scope.serviceRepairType.selected.subCategoryId1=$scope.selectedAsset.subCategoryId1;
											 $scope.getServiceSubRepairType($scope.serviceRepairType.selected);
											return false;
										}
								 	});
								 }*/
		    				}
		    			
		    			 $('#loadingDiv').hide();
		            },
		            function(data) {
		                //console.log(data)
		                $("#servicesubrepairtypeSelect").empty();
		                $('#loadingDiv').hide();
		            });
				}
		 
			$scope.getSubRepairType=function(repairtype){
				 $('#loadingDiv').show();
				 $("#subrepairtypeSelect").empty();
				 assetService.retrieveSubRepairTypes(repairtype.subCategoryId1)
		    		.then(function(data) {
		    			//console.log(data)
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
		    					
		    					/*if($scope.selectedAsset.subCategoryId2!=null){
									 $("#subrepairtypeSelect option").each(function() {
										if ($(this).val() == $scope.selectedAsset.subCategoryId2) {
											$(this).attr('selected', 'selected');
											$scope.subRepairType.selected.subCategoryId2=$scope.selectedAsset.subCategoryId2;
											return false;
										}
								 	});
								 }*/
		    					
		    				}
		    				
		    				 $('#loadingDiv').hide();
							 
		            },
		            function(data) {
		               // console.log(data);
		                $('#loadingDiv').hide();
		            });
				}
		 
			$scope.getServiceSubRepairType=function(servicerepairtype){
				 $('#loadingDiv').show();
				 $("#servicesubrepairtypeSelect").empty();
				 
				 assetService.retrieveSubRepairTypes(servicerepairtype.subCategoryId1)
		    		.then(function(data) {
		    			//console.log(data)
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
		    					
		    					/*if($scope.selectedAsset.subCategoryId2!=null){
									 $("#servicesubrepairtypeSelect option").each(function() {
										if ($(this).val() == $scope.selectedAsset.subCategoryId2) {
											$(this).attr('selected', 'selected');
											$scope.serviceSubRepairType.selected.subCategoryId2=$scope.selectedAsset.subCategoryId2;
											
											return false;
										}
								 	});
								 }*/
		    					
		    				}
		    			    $('#loadingDiv').hide();
		            },
		            function(data) {
		                //console.log(data)
		                $('#loadingDiv').hide();
		            });
				}
		 
		 $scope.getAssetLocations=function(){
			 $('#loadingDiv').show();
			 assetService.getAssetLocations()
			 .then(function(data) {
	    			//console.log(data);
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
	    			//console.log($scope.assetLocation.list);
	    			 $('#loadingDiv').hide();
	            },
	            function(data) {
	                console.log('Asset Locations retrieval failed.')
	            });
			 
		 }
		 
		 $scope.getServiceProviders=function(customer){
			 $('#loadingDiv').show();
				serviceProviderService.getServiceProviderByCustomer(customer)
				.then(function(data) {
	    			//console.log(data);
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
					 $('#loadingDiv').hide();
	            },
	            function(data) {
	                console.log('Unable to get  Service Provider list')
	                $('#loadingDiv').hide();
	            });
			}
		 
		 $scope.populateSelectedSite=function(){
			 $('#loadingDiv').show();			
	    					$scope.accessSite.list=[];
	    					$("#siteSelect").empty();
	    					$("#serviceSiteSelect").empty();
		    				//$.each(data.object,function(key,val){
		    					var accessedSite={
			    						accessId:$scope.selectedSite.accessId,
			    						site:$scope.selectedSite,
			    						siteId:$scope.selectedSite.siteId,
			    						siteName:$scope.selectedSite.siteName
			    				}
		    					$scope.accessSite.list.push(accessedSite);		    					
		    					 $('#loadingDiv').hide();		    				
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
		 
		 $scope.saveAssetEquipment=function(){	
			 $scope.equipmentData.sites=[];
			 $scope.equipmentData.sites.push($scope.selectedSite.siteId);
	          if($scope.IsValidDate($scope.equipmentData.deCommissionedDate,$scope.equipmentData.commisionedDate)){
	        	  $('#equipmentModalMessageDiv').hide();
	        	  $scope.equipmentData.categoryId=$scope.assetCategory.selected.assetCategoryId;	        	  
					 $scope.equipmentData.locationId=$scope.assetLocation.selected.locationId;
					 $scope.equipmentData.subCategoryId1 = $scope.repairType.selected.subCategoryId1;
					 $scope.equipmentData.subCategoryId2 = $scope.subRepairType.selected.subCategoryId2;
					 $scope.equipmentData.siteId=$scope.selectedSite.siteId;
					 if($scope.serviceProvider.selected!=null){
						 $scope.equipmentData.serviceProviderId=$scope.serviceProvider.selected.serviceProviderId;
					 }
					// console.log($scope.equipmentData);
					// $scope.equipmentData.sites.push($scope.selectedSite.siteId);
					 
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
						 $scope.equipmentModalErrorMessage="File size exceeds Max limit (100KB).";
		            	 $('#equipmentModalMessageDiv').show();
		            	 $('#equipmentModalMessageDiv').alert();
		                  $scope.isfileselected=false;
		                  return false;
		             }
					 if( $scope.equipmentData.assetDoc.size > 100){
						 $scope.equipmentModalErrorMessage="File size exceeds Max limit (100KB).";
		            	 $('#equipmentModalMessageDiv').show();
		            	 $('#equipmentModalMessageDiv').alert();
		                  $scope.isfileselected=false;
		                  return false;
		             }else{
		            	 $scope.equipmentModalErrorMessage="";
		            	 $('#equipmentModalMessageDiv').hide();
		            	 $scope.saveAssetInfo($scope.equipmentData);
		             }
   				
	          }
	          else{
	        	  $scope.equipmentModalErrorMessage = "Decommission date should be after Commission date";
	                $('#equipmentModalMessageDiv').show();
	                $('#equipmentModalMessageDiv').alert();							
	          }		          
			 
		 }
		 
		 $scope.saveAssetService =function(){
			 $scope.serviceData.sites=[];
			 $scope.serviceData.sites.push($scope.selectedSite.siteId);
			 if($scope.IsValidDate($scope.serviceData.deCommissionedDate,$scope.serviceData.commisionedDate)){
	        	  $('#serviceModalMessageDiv').hide();
	        	  $scope.serviceData.categoryId=$scope.assetCategory.selected.assetCategoryId;
	        	  
					 $scope.serviceData.locationId=$scope.assetLocation.selected.locationId;
					 $scope.serviceData.subCategoryId1 = $scope.serviceRepairType.selected.subCategoryId1;
					 $scope.serviceData.subCategoryId2 = $scope.serviceSubRepairType.selected.subCategoryId2;
					 
					 $scope.serviceData.siteId=$scope.selectedSite.siteId;
					 if($scope.serviceProvider.selected!=null){
					   $scope.serviceData.serviceProviderId=$scope.serviceProvider.selected.serviceProviderId;
					 }
			//		 $scope.serviceData.sites.push($scope.selectedSite.siteId);
					 
					 var serviceDoc={
								fileName:$scope.serviceData.assetName,
								base64ImageString:$scope.assetDocFile,
								fileExtension: $scope.fileExtension2,
								size:$scope.fileSize2 || 0
							}
					 $scope.serviceData.assetDoc = serviceDoc;
					 
					 if($scope.serviceData.assetDoc.size > 100){
		            	 $scope.serviceModalErrorMessage="File size exceeds Max limit (100KB).";
		            	 $('#serviceModalMessageDiv').show();
		            	 $('#serviceModalMessageDiv').alert();
		                  $scope.isfileselected=false;
		                  return false;
		             }else{
		            	 $scope.serviceModalErrorMessage="";
		            	 $('#serviceModalMessageDiv').hide();
		            	 $scope.saveAssetInfo($scope.serviceData);
		             }
    				
	          }
	          else{
	        	  	$scope.serviceModalErrorMessage = "Decommission date should be after Commission date";
	                $('#serviceModalMessageDiv').show();
    				$('#serviceModalMessageDiv').alert();							
	          }		     
			
		 }
		 
		 $scope.saveAssetInfo=function(assetData){
			 $('#loadingDiv').show();
			 assetService.saveAssetObject(assetData)
			 .then(function(data) {
	    			//console.log(data);
	    			if(data.statusCode == 200){
	    				$scope.successMessage = data.message;
	    				$('#messageWindow').show();
	    				$('#successMessageDiv').show();
	    				$('#successMessageDiv').alert();
	    				$('#infoMessageDiv').hide();
	    				if(assetData.assetType == 'E'){
	    					$('#assetModalCloseBtn').click();
	    				}
	    				else if(assetData.assetType == 'S'){
	    					$('#serviceModalCloseBtn').click();
	    				}
	    				//$scope.getAllAsset();
	    				$('#loadingDiv').hide();
	    			}
	            },
	            function(data) {
	                console.log('Error while saving asset data')
	                $('#modalMessageDiv').show();
    				$('#modalMessageDiv').alert();
    				if(assetData.assetType == 'E'){
    					$scope.equipmentModalErrorMessage = data.message;
    					$('#equipmentModalMessageDiv').show();
	    				$('#equipmentModalMessageDiv').alert();
    				}
    				else if(assetData.assetType == 'S'){
    					$scope.serviceModalErrorMessage = data.message;
    					$('#serviceModalMessageDiv').show();
	    				$('#serviceModalMessageDiv').alert();
    				}
    				$('#loadingDiv').hide();
	            });
		 }
		 
		 $scope.viewAssetForSelectedSite=function(){
			 if($scope.selectedSite!=null){
					window.location.href=hostLocation+"/asset/info/"+$scope.selectedSite.siteId;
			 }
				else{
					$('#messageWindow').show();
					$('#infoMessageDiv').show();
					$('#infoMessageDiv').alert();
					$scope.InfoMessage="Please select a site to view.";
				}
			}
		 
		 $scope.getLoggedInUserAccess =function(){
				
			 authService.loggedinUserAccess()
	    		.then(function(data) {
	    			console.log(data)
	    			if(data.statusCode == 200){
	    				$scope.sessionUser=data;
	    				$scope.getLoggedInUser($scope.sessionUser);
	    				
	    			}
	            },
	            function(data) {
	                console.log('Unauthorized Access.')
	            }); 
				
		    }
		    
		$scope.getAllSites=function(){
			$scope.findAllSites();
		}
		
		
		
		$scope.getLoggedInUser=function(loginUser){
			userService.getLoggedInUser(loginUser)
    		.then(function(data) {
    			//console.log(data)
    			if(data.statusCode == 200){
    				$scope.siteData ={};
    				$scope.sessionUser=angular.copy(data.object);
    				$scope.siteData.company=$scope.sessionUser.company;
    				$scope.getAllSites();
    				/*$scope.getServiceProviders($scope.sessionUser.company);
    				$scope.retrieveAssetCategories();
    				$scope.getAssetLocations();*/
    				//$scope.populateSelectedSite();
   				 	$('.dpedit').editableSelect();
    			}
            },
            function(data) {
                console.log('No User available')
            });
		}
		$scope.retrieveAllCountries=function(loginUser){
			countryService.retrieveAllCountries(loginUser)
    		.then(function(data) {
    			//console.log(data)
    			if(data.length>0){
    				$.each(data,function(key,val){
    					if(val.countryId == val.countryId){
    						$scope.siteData.country=val;
    						/*countryService.getCountryImage($scope.siteData.country.countryName)
    						.then(function(data){
    							console.log(data);
    							$('#countryImg').attr("src", data[0].flag);
    						},function(data){
    							console.log(data);
    						});*/
    						
    						return false;
    					}
    				})
    			}
    		},
    		 function(data) {
                //console.log(data);
            });
			
			
		}
		
		$scope.getBrand=function(){
			
    				$("#brandSelect").empty();
    				if($scope.brand.list.length>0){
    					
    					var options = $("#brandSelect");
    					options.append($("<option />").val("").text("Select Brand"));
    					$.each($scope.brand.list,function() {
    						options.append($("<option />").val(	this.brandId).text(this.brandName));
    					});
    					
    					if($scope.operation == 'EDIT'){
    						$scope.selectedSite.brand={
    								brandId:$scope.selectedSite.brandId,
    								brandName:$scope.selectedSite.brandName
    						}
    						$scope.selectedSite.brand.brandId = $scope.selectedSite.brandId;
	    					if($scope.selectedSite.brandId!=null){
	    					 $("#brandSelect option").each(function() {
	    							if ($(this).val() == $scope.selectedSite.brand.brandId) {
	    								$(this).attr('selected', 'selected');	    								
	    							return false;
	    							}
	    					 	});
	    					}else{
	    						
	    					}
    					}
    				}
		}
		$scope.getDistrictByCountry=function(loginUser){
			districtService.retrieveDistrictByCountry(loginUser)
    		.then(function(data) {
    			//console.log(data)
    			if(data.statusCode == 200){
    				$scope.district.list=[];
    				$("#districtSelect").empty();
    				if(data.object.length>0){
    					$.each(data.object,function(key,val){
    						var district={
    								districtId:val.districtId,
    								//districtCode:val.districtCode,
    								districtName:val.districtName
    						}
    						$scope.district.list.push(district);
    					});
    					var options = $("#districtSelect");
    					options.append($("<option />").val("").text("Select District"));
    					$.each($scope.district.list,function() {
    						options.append($("<option />").val(	this.districtId).text(this.districtName));
    					});
    					if($scope.operation == 'EDIT'){
	    					if($scope.selectedSite!=null){
	    					 $("#districtSelect option").each(function() {
	    							if ($(this).val() == $scope.selectedSite.district.districtId) {
	    								$(this).attr('selected', 'selected');
	    								$scope.getArea($scope.selectedSite.district);
	    							return false;
	    							}
	    					 	});
	    					}
    					}
    					$('#createSiteModal').modal('show');
    				}
    			}
            },
            function(data) {
                //console.log(data)
            });
		}
		
		$scope.getArea=function(district){
			areaService.retrieveAllAreas(district)
    		.then(function(data) {
    			//console.log(data)
    				$scope.area.list=[];
    			$("#areaSelect").empty();
    				if(data.object.length>0){
    					$.each(data.object,function(key,val){
    						var area={
    								areaId:val.areaId,
    								areaName:val.areaName,
    						}
    						$scope.area.list.push(area);
    					});
    					var options = $("#areaSelect");
    					options.append($("<option />").val("").text("Select Area"));
    					$.each($scope.area.list,function() {
    						options.append($("<option />").val(	this.areaId).text(this.areaName));
    					});
    					if($scope.operation == 'EDIT'){
	    					if($scope.selectedSite!=null){
	       					 $("#areaSelect option").each(function() {
	       							if ($(this).val() == $scope.selectedSite.area.areaId) {
	       								$(this).attr('selected', 'selected');
	       								$scope.getCluster($scope.selectedSite.district.districtId, $scope.selectedSite.area.areaId);
	       							return false;
	       							}
	       					 	});
	       					}
    					}
    				}
            },
            function(data) {
                //console.log(data)
            });
		}

		$scope.getCluster=function(distId, areaId){
			clusterService.retrieveAllClusters(distId,areaId)
    		.then(function(data) {
    			//console.log(data)
    				$scope.cluster.list=[];
    				$("#clusterSelect").empty();
    				if(data.object.length>0){
    					$.each(data.object,function(key,val){
    						var cluster={
    								clusterID:val.clusterID,
    								//regionId:val.regionId,
    								//districtId:val.districtId,
    								//countryId:val.countryId,
    								//areaId:val.area,
    								clusterName:val.clusterName,
    								//clusterDesc:val.clusterDesc
    						}
    						$scope.cluster.list.push(cluster);
    					});
    						
						var options = $("#clusterSelect");
    					options.append($("<option />").val("").text("Select Cluster"));
    					$.each($scope.cluster.list,function() {
    						options.append($("<option />").val(	this.clusterID).text(this.clusterName));
    					});
    					
    					if($scope.operation == 'EDIT'){
	    					if($scope.selectedSite!=null){
	          					 $("#clusterSelect option").each(function() {
	          							if ($(this).val() == $scope.selectedSite.cluster.clusterID) {
	          								$(this).attr('selected', 'selected');
	          							return false;
	          							}
	          					 	});
	          				}
    					}
    					
    				}
            },
            function(data) {
                //console.log(data)
            });
		}
		
		
		$scope.getAllCompanies=function(){
		}
		
		$scope.findAllSites=function(){
			$('#loadingDiv').show();
			siteService.retrieveAllSites()
			.then(function(data) {
    			//console.log(data)
    				$scope.siteList=[];
    				if(data.length>0){
    					$.each(data,function(key,val){
    						$scope.siteList.push(val);
    					});
    					$scope.getSiteDetails($scope.siteList[0]);
    					$('#loadingDiv').hide();
    				}else{
    					console.log("No sites assigned to the user.")
    					$scope.InfoMessage="No sites assigned to the user."
    						$('#messageWindow').show();
    	    				$('#infoMessageDiv').show();
    	    				$('#loadingDiv').hide();
    				}
            },
            function(data) {
                //console.log(data)
                	$scope.InfoMessage="No sites assigned to the user."
					$('#messageWindow').show();
    				$('#infoMessageDiv').show();
    				$('#infoMessageDiv').alert();
    				$('#loadingDiv').hide();
            });
			
			// $scope.getDistrictByCountry($scope.sessionUser);
			 //$scope.getArea();
			// $scope.getCluster();
		}
		
		//License related logic
		
	              			 
		$scope.addNewLicense = function(licenseDetails){
			 $scope.licenseDetails.push({ 
			  "licenseId":null,	 
			  'licenseName': "", 
			  'validfrom': "" ,
			  'validto': "",
			  'attachment':null
			 });
			 $scope.LD = {};
			 
			 $(".licensedate").datepicker({
				 format:'dd-mm-yyyy',
			 },"destroy");
			//console.log($scope.licenseDetails) 
		};
			 
		$scope.removeLicense = function(){
			  var newDataList=[];
			  $scope.selectedAll = false;
			  angular.forEach($scope.licenseDetails, function(selected){
			      if(!selected.selected){
			         newDataList.push(selected);
			       }
			    }); 
			   $scope.licenseDetails = newDataList;
		};
		
		
		   
    $scope.checkAllLicense = function () {
    	if (!$scope.selectedAll) {
		   $scope.selectedAll = true;
		  } else {
		    $scope.selectedAll = false;
		     }
		   angular.forEach($scope.licenseDetails, function(licenseDetail) {
			   licenseDetail.selected = $scope.selectedAll;
		    });
		 };


		//Sales Operation and delivery operation details//
		              			 
	$scope.operatingTimes = [	
	         { id: 0,name: '00:00'},                
  			 { id: 1,name: '1:00'},
             {id: 2, name: '2:00'},
             {id: 3, name: '3:00' },
             {id: 4, name: '4:00' },
             {id: 5, name: '5:00' },
             {id: 6, name: '6:00' },
             {id: 7, name: '7:00' },
             {id: 8, name: '8:00' },
             {id: 9, name: '9:00' },
             {id: 10,name: '10:00' },
             {id: 11,name: '11:00' },
             {id: 12,name: '12:00' },
             {id: 13, name: '13:00'},
             {id: 14, name: '14:00'},
             {id: 15, name: '15:00'},
             {id: 16, name: '16:00'},
             {id: 17, name: '17:00'},
             {id: 18, name: '18:00'},
             {id: 19, name: '19:00'},
             {id: 20, name: '20:00'},
             {id: 21, name: '21:00'},
             {id: 22, name: '22:00'},
             {id: 23, name: '23:00'},
             {id: 24, name: '23:59'},
		   ];
		              
		              
		   $scope.getOperationDetails=function(){
				$scope.salesoperationDetails = {
						selected:{},
						finalSelected:[],
						list:[
				        {
				        	 "opId":null,
				            'days':'Monday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Tuesday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Wednesday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Thursday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Friday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Saturday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Sunday',
				            'from':'',
				            'to':''
				        }
				       ]
					};
				
				
				//Delivery Operation details
				 $scope.deliveryoperationDetails = {
						 selected:{},
							finalSelected:[],
						 list:[
				        {
				        	 "opId":null,
				            'days':'Monday',
				            'from':'',
				            'to':''
				            
				        },
				        {
				        	 "opId":null,
				            'days':'Tuesday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Wednesday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Thursday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Friday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Saturday',
				            'from':'',
				            'to':''
				        },
				        {
				        	 "opId":null,
				            'days':'Sunday',
				            'from':'',
				            'to':''
				        }]
					}
				 
				
		   }           			 

		              			
	   $scope.salesOperations=[];
    	$scope.deliveryOperations=[];          
 		$scope.setStartTime=function(value, targetId, from ){
 			if(value.from==null){
 				$("#"+targetId+""+from).attr("required",true);
 				$("#"+targetId+""+from).attr("style","background-color:#5ccaae;color:#fff")
 			}else{
 				var selectedStartTime = value.from;	
	 			$("#"+targetId+""+from).val(selectedStartTime.name)
	 			$("#"+targetId+""+from).attr("style","background-color:#5ccaae;color:#fff");
 			}
 		}
 		
 		$scope.setEndTime=function(value, targetId, to){
 			if(value.to==null){
 				$("#"+targetId+""+to).attr("required",true);
 				$("#"+targetId+""+to).attr("style","background-color:#5ccaae;color:#fff")
 			}else{
	 			var selectedEndTime = value.to;	
	 			$("#"+targetId+""+to).val(selectedEndTime.name);
	 			$("#"+targetId+""+to).attr("style","background-color:#5ccaae;color:#fff")
 			}
 		}
 
 		
 		
		$scope.changeOperationTime = function(operatingTime) {
	        $scope.myTime = operatingTime;    
	    };
		              			 
		             //Submeter details
		$scope.submeterDetails = [{
	        'subMeterNumber': '',
	        'subMeterUser':''
	        
	    }];
		
		$scope.defaultSalesOps = angular.copy($scope.salesoperationDetails);
 		$scope.defaultDeliveryOps = angular.copy($scope.deliveryoperationDetails);
 		$scope.defaultsSubmeterDetails = angular.copy($scope.submeterDetails);
 		
		              			 
		$scope.addNewSubmeter = function(submeterDetails){
			 $scope.submeterDetails.push({ 
			  'subMeterNumber': '', 
			 'subMeterUser': ''				 
			 });
			 $scope.SD = {};
		 };
		              				 
		 $scope.removeSubmeter = function(){
			  var newDataList=[];
			  $scope.selectedAll = false;
			  angular.forEach($scope.submeterDetails, function(selected){
			      if(!selected.selected){
			         newDataList.push(selected);
			       }
			    }); 
			        $scope.submeterDetails = newDataList;
		  };
		              			   
  	    $scope.checkAllSubmeter = function () {
  	    	if (!$scope.selectedAll) {
  			   $scope.selectedAll = true;
  			  } else {
  			    $scope.selectedAll = false;
  			     }
  			   angular.forEach($scope.submeterDetails, function(submeterDetail) {
  				   submeterDetail.selected = $scope.selectedAll;
  			    });
  	    };
  	    
  	    
  	    
  	  $scope.addNewSite=function(){
  		  	$scope.onedit=false;
  		  	$scope.operation ="ADD";
  		  	$scope.siteData={};
  		  	$('#siteModalLabel').text("Create New Site");
  		  	//console.log($scope.salesoperationDetails);
			$scope.getAllCompanies()
			//$scope.retrieveAllCountries($scope.sessionUser);
			$scope.getDistrictByCountry($scope.sessionUser);
			$scope.getBrand();
		/*	$('#clusterSelect').empty();
			$('#areaSelect').empty();
			$scope.area.list=[];
			$scope.cluster.list=[];
			$scope.licenseDetails=[];
			$scope.getOperationDetails();
			$scope.submeterDetails = [];*/
			
		}
  	    
  	   
		 
  	    $scope.getSelectedDistrict=function(district){
	    	$scope.siteData.district = district;
	    }
  	    
  	    $scope.getSelectedArea=function(area){
  	    	$scope.siteData.area = area;
  	    }
  	    
  	    $scope.getSelectedCluster=function(cluster){
	    	$scope.siteData.cluster = cluster;
	    }
  	    
  	  $scope.IsValidDate = function (toDate, fromDate) {				 
			
			 var isValid = false;
			 //console.log(toDate);
			 if(toDate == null || toDate == "" || typeof(toDate) === "undefined"){
				 isValid = true;					 
			 }
			 else{					 
				 var isSame;
				 var part1 = toDate.split('-');
				 var part2 = fromDate.split('-');				 
				 toDate =  new Date(part1[1]+'-'+part1[0]+'-'+part1[2]);
				 fromDate =  new Date(part2[1]+'-'+ part2[0]+'-'+ part2[2]);				 
		         isValid = moment(toDate).isAfter(fromDate);
		          if (!isValid){
		        	  isValid = moment(toDate).isSame(fromDate);
		          }	
			 }		         	          
	          //console.log(isValid);
	          return isValid;
	      }

  	$scope.resetSiteDetailsTab=function(){
  		$scope.siteData.siteName = "";
  		$scope.siteData.owner = "";
  		$scope.siteData.district=null;
  		$scope.siteData.area=null;
  		
  		$scope.siteData.cluster=null;
  		$("#districtSelect")[0].selectedIndex = "";
  		$("#areaSelect")[0].selectedIndex = ""; 
  		$("#clusterSelect")[0].selectedIndex = ""; 
  		$scope.siteData.electricityId = "";
  		$scope.siteData.siteNumber1 = "";
  		$scope.siteData.siteNumber2 = "";
  		$('#siteInputFile').val('');
  		$scope.uploadedFile=null;
  		$scope.fileExtension=null;
  		$scope.siteData.salesAreaSize = "";
  		$scope.siteData.brandId = "";
  		
  	  }  
  	  
  	 $scope.resetSiteContactTab=function(){
   		$scope.siteData.zipCode = "";
   		$scope.siteData.siteAddress1 = "";
   		$scope.siteData.siteAddress2 = "";
   		$scope.siteData.siteAddress3 = "";
   		$scope.siteData.siteAddress4 = "";
   		$scope.siteData.primaryContact = "";
   		$scope.siteData.secondaryContact = "";
   		$scope.siteData.longitude = "";
   		$scope.siteData.latitude = "";   		
   		$scope.siteData.email = "";
   		$scope.siteData.contactName = "";   		
   		
   	  }
  	 
  	$scope.resetLicenseTab=function(){
  		angular.forEach($scope.licenseDetails, function(selected){
		    selected.licenseName = "";
		    selected.validfrom = "";
		    selected.validto = "";
		    selected.licenseName = "";
  			/*if(!selected.selected){
		         newDataList.push(selected);
		       }*/
		    });  
  		$.each($scope.licenseAttachments,function(key,val){
  			 $('#licenseInputFile'+val.imgPos).val('');
  			 $('#licenseInputFile'+val.imgPos).val(null);
	    	 
				val.licenseId=parseInt(val.imgPos);
				val.fileName=null,
				val.base64ImageString=null,
				val.fileExtension= null,
				val.imgPos=null,
				val.fileSize = null
		
  		});
  		
  		$scope.licenseAttachments=[];
   		
   	  }
  	
  	$scope.resetSiteSubmeterTab=function(){
  		angular.forEach($scope.submeterDetails, function(selected){
		    selected.subMeterNumber = "";
		    selected.subMeterUser = "";
		    selected.selected = 0;
		    
  			/*if(!selected.selected){
		         newDataList.push(selected);
		       }*/
		    });  		
   		
   	  }
  	
  	$scope.resetSiteOperationTab=function(){
  		
  		  		
  		/*for (var salesOps in $scope.salesoperationDetails.list) {
  			salesOps.from="";
  			salesOps.to=""; 
  		}*/
  		/*$scope.salesOperations=[];
    	$scope.deliveryOperations=[];*/
  		$.each($scope.salesoperationDetails.list,function(key,val){
  			//console.log(key + ": " + value.from + ": " + value.age);
  			$('#salesDayFrom'+key).val("00:00");
  			$('#salesDayTo'+key).val("00:00");
    		//val.selected[0].selectedIndex=0;
    		
    	});
  		
  		$.each($scope.deliveryoperationDetails.list,function(key,val){
  			//console.log(key + ": " + value.from + ": " + value.age);
  			$('#deliveryDayFrom'+key).val("00:00");
  			$('#deliveryDayTo'+key).val("00:00");
    		//val.selected[0].selectedIndex=0;
    		
    	});
   		
   	  }
  	    
	    $scope.saveSiteForm=function(formObj){
	    	//console.log($scope.siteData);
	    	$scope.salesOperations=[];
	    	$scope.deliveryOperations=[];
	    	
	    	var isValidLicense = true;	
	    	var keepGoing = true;
	    	var count = 0;	    		    	
	    	
	    	angular.forEach($scope.licenseDetails, function(licenseDetail){
	    		if (keepGoing){
	    			
	    			/*console.log(licenseDetail.validfrom);
		    		console.log(licenseDetail.validto);*/
		    		if($scope.IsValidDate(licenseDetail.validto,licenseDetail.validfrom)){
		    			//isValidLicense = true;
		    			$("#datepicker"+count+"from").css('border-color', 'lightgrey');
		    			$("#datepicker"+count+"to").css('border-color', 'lightgrey');		    			
		    		}
		    		else{
		    			isValidLicense = false;
		    			//keepGoing = false;
		    			$("#datepicker"+count+"from").css('border-color', 'red');
		    			$("#datepicker"+count+"to").css('border-color', 'red');	
		    					    			
		    		}
		    		count = count +1;
	    		}	
			    });

	    	
	    	var startSalesTime="00:00";
			var endSalesTime="00:00";
			var startDelTime="00:00";
			var endDelTime="00:00";
	    	$.each($scope.salesoperationDetails.list,function(key,val){
	    		if($('#salesDayFrom'+key).val()!=""){
	    			startSalesTime=$('#salesDayFrom'+key).val();
	    		}
	    		else{
	    			startSalesTime="00:00";
    			}
	    		if($('#salesDayTo'+key).val()!=""){
	    			endSalesTime = $('#salesDayTo'+key).val();
	    		}else{
	    			endSalesTime="00:00";
    			}
    			
		    		var salesOpsTimings={
		    				opId:val.opId,
		    				days:val.days,
		    				from:startSalesTime,
		    				to:endSalesTime
		    		}
		    		$scope.salesOperations.push(salesOpsTimings);
	    	});
	    		$.each($scope.deliveryoperationDetails.list,function(key,val){
	    			if($('#deliveryDayFrom'+key).val()!=""){
	    				startDelTime = $('#deliveryDayFrom'+key).val();
	    			}else{
	    				startDelTime="00:00";
	    			}
	    			if($('#deliveryDayTo'+key).val()!=""){
	    				endDelTime = $('#deliveryDayTo'+key).val();
	    			}else{
	    				endDelTime="00:00";
	    				
	    			}	
		    		var deliveryOpsTimings={
		    				opId:val.opId,
		    				days:val.days,
		    				from:startDelTime,
		    				to:endDelTime
		    		}
		    		$scope.deliveryOperations.push(deliveryOpsTimings);
	    	});

	    	$scope.siteData.operator = $scope.sessionUser.company;
	    	$scope.siteData.companyId=$scope.sessionUser.companyId;
	    	var finalSiteObj = {
	    			siteData: $scope.siteData,
	    			siteLicense:$scope.licenseDetails,
	    			licenseAttachments:$scope.licenseAttachments,
	    			siteOperation:$scope.salesOperations,
	    			siteDelivery:$scope.deliveryOperations,
	    			siteSubmeter:$scope.submeterDetails,
	    			createdBy:$scope.sessionUser.loggedInUserMail
	    	
	    	};
	    	//console.log(finalSiteObj);
	    	var isOperationDetails = false;
	    	if(finalSiteObj.siteOperation.length==7){
		    	$.each(finalSiteObj.siteOperation,function(key,val){
		    		if(val.from == "" && val.to==""){
		    				$('#modalMessageDiv').show();
		    				$('#messageWindow').hide();
		    				$('#successMessageDiv').hide();
		    				$('#errorMessageDiv').show();
		    				$('#errorMessageDiv').alert();
		    				$scope.modalErrorMessage = "Please enter sales operation timing for" + val.days;
		    				isOperationDetails=false;
		    			return false;
		    		}else{
		    			isOperationDetails=true;
		    		}
		    	})
	    	}
	    
	    	 if(finalSiteObj.siteDelivery.length==7){
		    	$.each(finalSiteObj.siteDelivery,function(key,val){
		    		if(val.from == "" && val.to==""){
		    			$('#modalSiteMessageDiv').show();
	    				$('#messageWindow').hide();
	    				$('#successMessageDiv').hide();
	    				$('#errorMessageDiv').show();
	    				$('#errorMessageDiv').alert();
	    				$scope.modalErrorMessage = "Please enter delivery operation timing for"+ val.days;
	    				isOperationDetails=false;
		    			return false;
		    		}else{
		    			isOperationDetails=true;
		    		}
		    	})
	    	}
	    	/* if(isOperationDetails==true){
	    		$scope.createSiteVOObject(finalSiteObj);
	    		 console.log(finalSiteObj);
	    		$scope.modalErrorMessage ="";
	    	 }else{
	    		 $('#modalMessageDiv').show();
 				$('#messageWindow').hide();
 				$('#successMessageDiv').hide();
 				$('#errorMessageDiv').show();
 				$('#errorMessageDiv').alert();
 				$scope.modalErrorMessage = "Please enter all the required fields for operation timings";
	    	 }*/
	    	 
	    	 if(isOperationDetails==true && isValidLicense == true){
		    		$scope.createSiteVOObject(finalSiteObj);
		    		 //console.log(finalSiteObj);
		    		$scope.modalErrorMessage ="";
		    	 }else if(isValidLicense == false) {
		    		 $('#modalSiteMessageDiv').show();
	 				$('#messageWindow').hide();
	 				$('#successMessageDiv').hide();
	 				$('#errorMessageDiv').show();
	 				$('#errorMessageDiv').alert();
	 				$scope.modalErrorMessage = "ValidTo must be greater than ValidFrom in LicenseDetail Tab.Please enter correct date";
		    	 }
		    	 else{
		    		 $('#modalSiteMessageDiv').show();
		 				$('#messageWindow').hide();
		 				$('#successMessageDiv').hide();
		 				$('#errorMessageDiv').show();
		 				$('#errorMessageDiv').alert();
		 				$scope.modalErrorMessage = "Please enter all the required fields for operation timings";
		    	 }
	    	
	    	
     };
	     $scope.createSiteVOObject=function(finalSiteObj){
	    	 $('#loadingDiv').show();
	    	 if($scope.siteData.brandId!=null){
	    	finalSiteObj.siteData.brandId=$scope.siteData.brandId;
	    	finalSiteObj.siteData.brandName=$scope.siteData.brandName;
	    	 }else{
	    		 finalSiteObj.siteData.brandId = null;
	    		 finalSiteObj.siteData.brandName = null;
	    	 }
	    	 finalSiteObj.uploadedFile = $scope.uploadedFile;
	    	 finalSiteObj.extension=$scope.fileExtension;
	    	 var validateSiteAndLicense=true;
	    	 if($scope.siteFileSize!=null && $scope.siteFileSize > 100 ){
	    		$('#modalSiteMessageDiv').show();
 				$('#messageWindow').hide();
 				$('#successMessageDiv').hide();
 				$('#errorMessageDiv').show();
 				$('#errorMessageDiv').alert();
 				$scope.modalErrorMessage = "File size exceeds Max 100KB";
 				$('#loadingDiv').hide();
 				validateSiteAndLicense=false;
 				
	    	 }
	    	if(finalSiteObj.licenseAttachments.length > 0){
	    		 $.each(finalSiteObj.licenseAttachments,function(key,val){
	    			 if(val.fileExtension=="gif"){
	    				 $scope.modalErrorMessage = "Supported File types are jpg, png and pdf";
	    				 $('#fileerror').text("Supported File types are jpg, png and pdf");
	    		 			$('#modalSiteMessageDiv').show();
	    		 			validateSiteAndLicense=false;
	    		 			$('#loadingDiv').hide();
	    		 			return false;
	    			 }
	    		 });
		    }else if($scope.fileExtension=="gif"){
		    	 $scope.modalErrorMessage = "Supported File types are jpg, png and pdf";
				 $('#fileerror').text("Supported File types are jpg, png and pdf");
		 			$('#modalSiteMessageDiv').show();
		 			$('#loadingDiv').hide();
		 			validateSiteAndLicense=false;
		    }
	    	 
	    	if(validateSiteAndLicense){
	    		
	    		var finalObject = siteCreationService.createSiteObject(finalSiteObj)
		    	//console.log(finalObject);
		    	siteCreationService.saveSiteObject(finalObject)
	    		.then(function(data) {
	    			//console.log(data)
	    			if(data.statusCode == 200){
	    				$('#messageWindow').show();
	    				$('#successMessageDiv').show();
	    				$('#successMessageDiv').alert();
	    				$('#errorMessageDiv').hide();
	    				$('#siteModalCloseBtn').click();
	    				$('#infoMessageDiv').hide();
	    				$scope.successMessage = data.message;
	    				 $('#loadingDiv').hide();
	    				$scope.getAllSites();
	    				
	    			}else{
	    				 $('#modalSiteMessageDiv').show();
	    				$('#messageWindow').hide();
	    				$('#successMessageDiv').hide();
	    				$('#errorMessageDiv').show();
	    				$('#errorMessageDiv').alert();
	    				$scope.modalErrorMessage = data.message;
	    				$('#loadingDiv').hide();
	    			}
	            },
	            function(data) {
	                //console.log(data)
	                $('#messageWindow').hide();
	                $('#modalSiteMessageDiv').show();
	                $('#successMessageDiv').hide();
	                $('#errorMessageDiv').show();
					$('#errorMessageDiv').alert();
					$scope.modalErrorMessage = data.message;
					$('#loadingDiv').hide();
	            });
	    	}else{
	    		$('#modalSiteMessageDiv').show();
 				$('#messageWindow').hide();
 				$('#successMessageDiv').hide();
 				$('#errorMessageDiv').show();
 				$('#errorMessageDiv').alert();
 				$scope.modalErrorMessage = "Please verify site and license attachments.";
 				$('#loadingDiv').hide();
	    	}
	    	
	     }
	     $scope.closeMessageWindow=function(){
				$('#messageWindow').hide();
				$('#successMessageDiv').hide();
				$('#errorMessageDiv').hide();
				$('#modalSiteMessageDiv').hide();
				$('#modalMessageDiv').hide();
				$('#serviceModalMessageDiv').hide();
				$('#equipmentModalMessageDiv').hide();
				$scope.modalErrorMessage="";
				$scope.successMessage="";
			}
	     //----------------------- View Site ------------------------------------
	     
	     
		 console.log("allsite retrived");
		
		 
		 $scope.getSiteDetails=function(site){
			//console.log(site);
				$scope.getSelectedSiteData(site.siteId);
				//$scope.getSelectedSiteAttachments(site.siteId);
			}
		 
		 $scope.getSelectedSiteData=function(siteId){
			 siteService.retrieveSiteDetails(siteId)
	    		.then(function(data) {
	    			console.log(data)
	    			var site=angular.copy(data.object);
					$scope.selectedSite=angular.copy(site);
					$scope.selectedSite.siteName = site.siteName;
					$scope.selectedSite.siteNumber1 = site.siteNumber1;
					$scope.selectedSite.siteNumber2 = site.siteNumber2;
					$scope.selectedSite.salesAreaSize = site.salesAreaSize;
					$scope.selectedSite.siteAddress = site.fullAddress;
					
					$scope.selectedSite.siteAddress1 = site.siteAddress1;
					$scope.selectedSite.siteAddress2 = site.siteAddress2;
					$scope.selectedSite.siteAddress3 = site.siteAddress3;
					$scope.selectedSite.siteAddress4 = site.siteAddress4;
					
					$scope.selectedSite.district = site.district;
					$scope.selectedSite.area=site.area;
					$scope.selectedSite.cluster=site.cluster;
					
					$scope.district.selected=$scope.selectedSite.district;
					$scope.area.selected = $scope.selectedSite.area;
					$scope.cluster.selected = $scope.selectedSite.cluster;
					 
					$scope.selectedSite.retailerName=$scope.selectedSite.owner;
					$scope.selectedSite.primaryContact=site.primaryContact;
					$scope.selectedSite.secondaryContact=site.secondaryContact;
					
					$scope.selectedSite.LicenseDetail = site.siteLicense;
					$scope.selectedSite.SalesOperation = site.siteOperation;
					$scope.selectedSite.DeliveryOperation = site.siteDelivery;
					$scope.selectedSite.submeterDetails = site.siteSubmeter;
					 
					$scope.siteLicense = $scope.selectedSite.LicenseDetail;
					$scope.siteSalesOperation = $scope.selectedSite.SalesOperation;
					$scope.siteDeliveryOperation = $scope.selectedSite.DeliveryOperation;
					
					$scope.siteSubmeterDetails = $scope.selectedSite.submeterDetails;
					$scope.siteData.siteId = $scope.selectedSite.siteId;
	    			//$scope.siteData = angular.copy( $scope.selectedSite);
					
					$.jStorage.set('selectedSite', $scope.selectedSite);
					//console.log($scope.selectedSite.siteAttachments);
	    		},function(data){
	    			console.log(data);
	    			
	    		})
	    		
		 }
		 
		 $scope.getSelectedSiteAttachments=function(keyName){ 
			 siteService.siteFileDownload(keyName)
	 		.then(function(data) {
	 			//console.log(data)
	 			if(data.statusCode==200){
	 				$scope.selectedSite.imageClicked=data.object;
	 			}
	 		},function(data){
	 			//console.log(data);
	 		});
		 }
		 
		 $scope.viewImage=function(){
			 if($scope.selectedSite.fileInput!=null){
			  $('#imageviewer').html("<img src='"+$('#siteImg').val()+"' style='width:50%'></img>");
			 }
		 }
		 
		 
		 $scope.updateSiteModal = function(selectedSite) {
			 //console.log(selectedSite);
			 $scope.operation ="EDIT";
			 $scope.siteData = angular.copy($scope.selectedSite);
			 
			    $scope.siteData.siteAddress1 = selectedSite.siteAddress1;
				$scope.siteData.siteAddress2 = selectedSite.siteAddress2;
				$scope.siteData.siteAddress3 = selectedSite.siteAddress3;
				$scope.siteData.siteAddress4 = selectedSite.siteAddress4;
			 $('#siteModalLabel').text("Update site");
			 $scope.retrieveAllCountries($scope.sessionUser);
			 $scope.onedit=false;
			// $scope.siteData = angular.copy(selectedSite);
			 //console.log($scope.siteData);
			 $scope.getDistrictByCountry($scope.sessionUser);
			 $scope.getBrand();
			// $scope.getArea($scope.siteData.district)
			// $scope.getCluster($scope.siteData.district.districtId,$scope.siteData.area.areaId);
			 $scope.salesoperationDetails.list = [] ;
			 $scope.deliveryoperationDetails.list = [];
			 $.each($scope.siteData.SalesOperation,function(key,val){
				 var salesTimingObject={
						 opId:val.opId,
						 days:val.days,
						 from:val.from,
						 to:val.to,
						 selected:{
							 from:{
								 name:val.from
							 },
							 to:{
								 name:val.to
							 }
						 }
				 }
				 //console.log(salesTimingObject.selected.from.name);
				 $scope.salesoperationDetails.list.push(salesTimingObject);
			 });
			 
			 $.each($scope.siteData.DeliveryOperation,function(key,val){
				 var deliveryTimingObject={
						 opId:val.opId,
						 days:val.days,
						 from:val.from,
						 to:val.to,
						 selected:{
							 from:{
								 name:val.from
							 },
							 to:{
								 name:val.to
							 }
						 }
							
				 }
				 
				 $scope.deliveryoperationDetails.list.push(deliveryTimingObject);
			 });
			 
			 
			// console.log($scope.selectedSite, $scope.salesoperationDetails,  $scope.deliveryoperationDetails)
			 
		/*	
			 $("#districtSelect option").each(function() {
					if ($(this).val() == $scope.selectedSite.district.districtId) {
						$(this).attr('selected', 'selected');
						$scope.district.selected.districtId = $scope.selectedSite.district.districtId;
						$scope.getArea($scope.district.selected);
					return false;
					}
				});
			 $("#areaSelect option").each(function() {
					if ($(this).val() == $scope.selectedSite.area.areaId) {
						$(this).attr('selected', 'selected');
						$scope.area.selected.areaId = $scope.selectedSite.area.areaId;
						$scope.getCluster($scope.district.selected.districtId, $scope.selectedSite.area.areaId);
					return false;
					}
				});
			 
			 $("#clusterSelect option").each(function() {
					if ($(this).val() == $scope.selectedSite.cluster.clusterID) {
						$(this).attr('selected', 'selected');
						$scope.cluster.selected.clusterID = $scope.selectedSite.cluster.clusterID;
					return false;
					}
				});*/
			 
			 $scope.licenseDetails = [] ;
			 $scope.licenseDetails =  selectedSite.LicenseDetail;
			 $scope.submeterDetails = [];
			 $scope.submeterDetails = selectedSite.submeterDetails;
			// $scope.showUpdateModal();
			 $('#createSiteModal').modal('show');
			}
		 
		 $scope.viewTabSelected=function(selectedSite, tabSelected){
			 $scope.updateSiteModal(selectedSite);
			 $('#'+tabSelected).click();
			 
		 }
		 
		 $scope.openCreateEquipment = function(selectedSite) {
			 
			 //console.log("Equipment modal");
			 $scope.siteData=[];
			 $scope.siteData = angular.copy(selectedSite);
			 //console.log($scope.siteData);
			 
			  }


$scope.saveAssetEquipmentForm=function(){
 //console.log("save equipment form");

	 	
	var finalAssetEquipmentObj = {
			AssetEquipment: $scope.equipmentData,
			siteData:$scope.siteData 			
	
	};
	//console.log(finalAssetEquipmentObj);
	
};

	$scope.manageUserAccess=function(selectedSite){
		$scope.manageUserAccessForSite(selectedSite);
	}
	
	$scope.manageUserAccessForSite=function(selectedSite){
		$('#assignUserModal').modal('show');
		userService.getUsersBySiteAccess(selectedSite.siteId)
		.then(function(data) {
			//console.log(data)
			if(data.statusCode == 200){
				$scope.siteAssignedUserList=[];
				$scope.siteUnAssignedUserList=[];
				
				if(data.object.length>0){
					$.each(data.object,function(key,val){
						if(val.assignedUser!=null && val.accessId!=null){
							var assignedUserSiteData={
									accessId:val.accessId,
									user:val.assignedUser,
									company:val.assignedUser.company,
									site:val.site,
									firstName:val.assignedUser.firstName,
									lastName:val.assignedUser.lastName,
									email:val.assignedUser.email
							}
							$scope.siteAssignedUserList.push(assignedUserSiteData);
						}
							
						if(val.unAssignedUser!=null && val.accessId==null){
							if(val.unAssignedUser.userRoles.length>0){
								var unAssginedUsers ={
									user:val.unAssignedUser,
									firstName:val.unAssignedUser.firstName,
									lastName:val.unAssignedUser.lastName,
									email:val.unAssignedUser.email,
									role:val.unAssignedUser.userRoles[0].role
								}
							}
							$scope.siteUnAssignedUserList.push(unAssginedUsers);
						}
						
					});
				}
			}
				
	    },
	    function(data) {
	        //console.log(data)
	    });
	}
	$scope.searchCriteria={};
	$scope.setText=function(searchType){
		$('#searchSelection').html(searchType+ ' <span class="fa fa-caret-down"></span>');
		$scope.searchCriteria.filter=searchType;
	}
	
	$scope.assignThisUserTo=function(selectedUser,selectedSite){
		//console.log(selectedUser,selectedSite);
		$scope.assignUserToSite(selectedUser, selectedSite);
	}

	$scope.assignUserToSite=function(selectedUser,selectedSite){
		siteService.assignSiteAccess(selectedUser.user.userId,selectedSite.siteId)
		.then(function(data) {
			//console.log(data)
			if(data.statusCode == 200){
				$scope.manageUserAccessForSite($scope.selectedSite);
				//Send notification after User is assigned - Start
				var fullName = selectedUser.user.firstName +" "+ selectedUser.user.lastName;
				var siteName = selectedSite.siteName;
				sendNotification(fullName +" is assigned to Site "+ siteName);
				
				//Send notification after User is assigned - End
			}
		},function(data) {
	        //console.log(data)
	    });
	}
	
	
	$scope.revokeThisUserAccess=function(selectedUser,selectedSite){
		//console.log(selectedUser,selectedSite);
		$scope.revokeUserAccessFromSite(selectedUser);
	}

	$scope.revokeUserAccessFromSite=function(selectedUser){
		siteService.removeSiteAccess(selectedUser.accessId)
		.then(function(data) {
			//console.log(data)
			if(data.statusCode == 200){
				$scope.manageUserAccessForSite($scope.selectedSite);
				//Send notification after User is assigned - Start
				var fullName = selectedUser.user.firstName +" "+ selectedUser.user.lastName;
				var siteName = $scope.selectedSite.siteName;
				sendNotification(fullName +" is revoked from  Site "+ siteName);
				
				//Send notification after User is assigned - End
			}
		},function(data) {
	        //console.log(data)
	    });
	}
	
$scope.disableRemoveButton = function(licenseId){
		
		//console.log(licenseId);
		  $scope.selectedAll = false;
		  var keepGoing = true;
		  angular.forEach($scope.licenseDetails, function(selected){
			  //console.log(selected);
			  if (keepGoing){
		      if(selected.selected){
		    	  
		    	  if(licenseId != "" && typeof(licenseId) === "number" && typeof(selected.licenseId) === "number"){
		    		  //console.log("shibu11");
		    		  //console.log(selected.licenseId);
		    		  $("#btnRemove").prop("disabled", true);
		    		  keepGoing = false;
		    	  }
		    	  else if(!(typeof(licenseId) === "number") && typeof(selected.licenseId) === "number"){
		    		  
		    		  $("#btnRemove").prop("disabled", true);
		    		  keepGoing = false;
		    	  }
		    	  else{
		    		  
		    		  $("#btnRemove").prop("disabled", false);
		    	  }			         
		       }
		      else if(!selected.selected){
		    	  if(!typeof(licenseId) === "number"){
		    		  
		    		  $("#btnRemove").prop("disabled", false);
		    	  }
		    	  else if(licenseId != ""){
		    		  $("#btnRemove").prop("disabled", false);  
		    	  }			    	  
		      }
			  }
		      
		    });
		  
	};
	$scope.deleteFile=function(feature, file){
		//console.log( $scope.selectedSite);
		 $('#loadingDiv').show();
		 var fileStr ="";
		 var selectedId = null; 
		 if($scope.selectedSite.fileInput!=null){
			 fileStr = $scope.selectedSite.fileInput;
			 selectedId =$scope.selectedSite.siteId;
		 }
		 if(file.licenseId!=null){
			 fileStr = file.attachment;
			 selectedId = file.licenseId;
			var licenseIds=[];
			licenseIds.push(file.licenseId);
			var selectedId = licenseIds.join(",");
		 }
		 
		if(fileStr!=""){ 
			siteService.deleteFileAttached(feature, selectedId)
			.then(function(data){
				//console.log(data);
				if(data.statusCode==200){
					$scope.findAllSites();
				}
				$('#loadingDiv').hide();
			},function(data){
				//console.log(data);
				$('#loadingDiv').hide();
			});
		}
	}
	
	
$scope.onlyNumbers = /^\d+$/;
$scope.filterValue = function($event){
    if(isNaN(String.fromCharCode($event.keyCode))){
        $event.preventDefault();
    }
};

$scope.validateSalesTime=function(val,$event){
	
	var arrayLength = $scope.operatingTimes.length;
	
	var charCode;
    if (event.keyCode > 0) {
        charCode = event.which || event.keyCode;
    }
    else if (typeof (event.charCode) != "undefined") {
        charCode = event.which || event.keyCode;
    }
    if (charCode == 58){
    	$scope.operatingTimes.push({
    		id:arrayLength+1,
    		name:val.salesoperationdetail.selected.from.name    		
    	})
    	$scope.operatingTimes.push({
    		id:arrayLength+1,
    		name:val.salesoperationdetail.selected.to.name    		
    	})
        return true
    }    	
    if (charCode > 31 && (charCode < 48 || charCode > 57)){
    	$event.preventDefault();    	
    	return false;
    }
    
    $scope.operatingTimes.push({
		id:arrayLength+1,
		name:val.salesoperationdetail.selected.from.name		
	})
	$scope.operatingTimes.push({
    		id:arrayLength+1,
    		name:val.salesoperationdetail.selected.to.name    		
    	})
    return true;
}

$scope.validateDeliveryTime=function(val,$event){
	
	var arrayLength = $scope.operatingTimes.length;
	
	var charCode;
    if (event.keyCode > 0) {
        charCode = event.which || event.keyCode;
    }
    else if (typeof (event.charCode) != "undefined") {
        charCode = event.which || event.keyCode;
    }
    if (charCode == 58){
    	$scope.operatingTimes.push({
    		id:arrayLength+1,
    		name:val.deliveryoperationdetail.selected.from.name    		
    	})
    	$scope.operatingTimes.push({
    		id:arrayLength+1,
    		name:val.deliveryoperationdetail.selected.to.name    		
    	})
        return true
    }    	
    if (charCode > 31 && (charCode < 48 || charCode > 57)){
    	$event.preventDefault();    	
    	return false;
    }
    
    $scope.operatingTimes.push({
		id:arrayLength+1,
		name:val.deliveryoperationdetail.selected.from.name		
	})
	$scope.operatingTimes.push({
    		id:arrayLength+1,
    		name:val.deliveryoperationdetail.selected.to.name    		
    	})
    return true;
}
			 
}]);



function validateDropdownValues(dropDownId){
	var scope = angular.element("#siteWindow").scope();
	 var valueId = parseInt($("#"+dropDownId).val());
	 if(valueId == ""){
	 
	 }else{
		 if(dropDownId.toUpperCase() == "DISTRICTSELECT"){
			 var district={
					 districtId:parseInt($("#districtSelect").val()),
			 		 districtName:$("#districtSelect option:selected").text()
			 }
			scope.district.selected=district;
			 scope.getArea(district);
			 scope.siteData.district = district;
		 }
		 if(dropDownId.toUpperCase() == "BRANDSELECT"){
			 var brand={
					 brandId:parseInt($("#brandSelect").val()),
			 		 brandName:$("#brandSelect option:selected").text()
			 }
			scope.brand.selected=brand;			 
			 scope.siteData.brandId = brand.brandId;
			 scope.siteData.brandName = brand.brandName
			 
		 }
		 
		 if(dropDownId.toUpperCase() == "AREASELECT"){
			 if(scope.siteData.district!=null){
				 var area={
						 areaId:parseInt($("#areaSelect").val()),
				 		 areaName:$("#areaSelect option:selected").text()
				 }
				scope.area.selected=area;
				scope.siteData.area = area;
				scope.getCluster(scope.district.selected.districtId,area.areaId )
			 }
				
		 }
		 
		 if(dropDownId.toUpperCase() == "CLUSTERSELECT"){
			 if(scope.siteData.district!=null && scope.siteData.area!=null ){
				 var cluster={
						 clusterID:parseInt($("#clusterSelect").val()),
				 		 clusterName:$("#clusterSelect option:selected").text()
				 }
					 scope.cluster.selected=cluster;
					 scope.siteData.cluster = cluster;
			 }else{
				 
			 }
			
		 }
	 }
}

function setTimeValues(dropDownId, option){
	var scope = angular.element("#siteWindow").scope();
	var selectedVal = dropDownId.id;
	//console.log(selectedVal)
	if(option=="from"){
		$.each(scope.salesTimes.fromlist,function(key,val){
			if(selectedVal.toUpperCase() == "SALESSELECTFROM"+key){
				//console.log("SALESSELECTFROM"+key)
				var object={
						opId:val.opId,
						days:val.days,
						val:$('#'+selectedVal).val(),
						text:$('#'+selectedVal).val()
				}
				scope.salesTimes.selected=object;
				//console.log(scope.salesTimes.selected)
				return false;
			}
		});
		$.each(scope.salesTimes.fromlist,function(key,val){
			if(selectedVal.toUpperCase() == "DELIVERYFROM"+key){
				//console.log("DELIVERYFROM"+key)
				var object={
						opId:val.opId,
						days:val.days,
						val:$('#'+selectedVal).val(),
						text:$('#'+selectedVal).val()
				}
				scope.salesTimes.selected=object;
				//console.log(scope.salesTimes.selected)
				return false;
			}
		});
	}
	if(option=="to"){
		$.each(scope.salesTimes.toList,function(key,val){
			if(selectedVal.toUpperCase() == "SALESSELECTTO"+key){
				//console.log("SALESSELECTTO"+key)
				var object={
						opId:val.opId,
						days:val.days,
						val:$('#'+selectedVal).val(),
						text:$('#'+selectedVal).val()
				}
				scope.salesTimes.selected=object;
				//console.log(scope.salesTimes.selected)
				return false;
			}
		});
		$.each(scope.salesTimes.toList,function(key,val){
			if(selectedVal.toUpperCase() == "DELIVERYTO"+key){
				//console.log("DELIVERYTO"+key)
				var object={
						opId:val.opId,
						days:val.days,
						val:$('#'+selectedVal).val(),
						text:$('#'+selectedVal).val()
				}
				scope.salesTimes.selected=object;
				//console.log(scope.salesTimes.selected)
				return false;
			}
		});
	}
		
	}
	
function assetValidateDropdownValues(dropDownId, assetType){
	var scope = angular.element("#siteWindow").scope();
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
					// scope.getSubRepairType(repairtype);
				 }
				 if(dropDownId.toUpperCase() == "SUBREPAIRTYPESELECT"){
					 var subrepairtype={
							 subCategoryId2:parseInt($("#subrepairtypeSelect").val()),
							 assetSubcategory2:$("#subrepairtypeSelect option:selected").text()
					 }
					 scope.subRepairType.selected = subrepairtype;
					 
				 }
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
					 var site={
							 siteId:parseInt($("#siteSelect").val()),
					 		 siteName:$("#siteSelect option:selected").text()
					 }
					 scope.accessSite.selected =site;
				 }
				 scope.equipmentData.assetType=assetType;
		 }else if(assetType == 'S'){
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
			 if(dropDownId.toUpperCase() == "SERVICESUBREPAIRTYPESELECT"){
				 var servicesubrepairtype={
						 subCategoryId2:parseInt($("#servicesubrepairtypeSelect").val()),
						 assetSubcategory2:$("#servicesubrepairtypeSelect option:selected").text()
				 }
				 scope.serviceSubRepairType.selected = servicesubrepairtype;
				 
			 }
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
				 var site={
						 siteId:parseInt($("#serviceSiteSelect").val()),
				 		 siteName:$("#siteSelect option:selected").text()
				 }
				 scope.accessSite.selected =site;
			 }
			 scope.serviceData.assetType=assetType;
		 }
	 }
}


