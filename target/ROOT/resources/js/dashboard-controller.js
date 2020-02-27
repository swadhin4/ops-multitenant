productApp.controller('dashboardController', ['$rootScope','$scope','$http','employeeService','agentService', 
                                              function($rootScope,$scope,$http,employeeService,agentService) {
	
	 var table = null;
	 $scope.orderPayment={};
	 $scope.allAgents =[];
	 $scope.allEmployees =[];
	 $scope.selectedRow =0;
	 $scope.rowHighilited=function(row)
	    {
	      $scope.selectedRow = row;    
	    }
	 
	 
	$scope.getLedgerDetails=function(){
		$scope.orderInfo={};
		$scope.orderList=[];
		 $http.get(webContextPath+'/dashboard/ledgerview')
			.success(function(data) {
				var pendingOrders=[];
				console.log(data);
				pendingOrders=data.object.orderVOList;
				$.each(pendingOrders,function(key,val1){
					$.each(val1.orderPaymentDetailVOList,function(key,val2){
						if(val2.paymentAmount==0.00){
							var nextDueDate=val2.nextPmtDueDate;
							var paymentDone=val2.paymentAmount;
						}
						
					});
					 $scope.orderInfo = {
								//paymentAmount:paymentAmount,
								//dueDate:nextDate,
								customerVO:val1.customerVO,
								orderNumber:val1.orderNumber,
								collectionAgent:val1.collectionAgent,
								orderAmount:val1.orderAmount,
								orderStatus:val1.orderStatus,
								orderId:val1.orderId
						}
					 $scope.orderList.push($scope.orderInfo);
					
				});
				// $('#orderTable').dataTable();
				// $('#orderTable_filter').addClass('pull-right');
				// $('#orderTable_paginate').addClass('pull-right');
			});
	}
	
	
	$scope.getMyAssignedOrders=function(){
		$scope.orderPaymentList=[];
		 $http.get(webContextPath+'/dashboard/followup/orders')
			.success(function(data) {
				console.log(data);
				$scope.orderPaymentList=[];
				$.each(data.object,function(key,val){
					 var orderPaymentInfo = {
							 	orderId:val.orderId,
							 	orderNumber:val.orderNumber,
								customerVO:val.customerVO,
								collectionAgent:val.collectionAgent,
								orderAmount:val.orderAmount,
								orderStatus:val.orderStatus,
								paymentAmount:val.paymentAmount,
								dueAmount:val.dueAmount
								
						}

					 $scope.orderPaymentList.push(orderPaymentInfo);
					
				});
			});
	}
	
	$scope.getAllInvoices=function(){
		$scope.orderInvoiceList=[];
		 $http.get(webContextPath+'/invoice/list')
			.success(function(data) {
				console.log(data);
				$scope.orderInvoiceList=[];
				$.each(data.object,function(key,val){
					 $scope.orderInvoiceList.push(val);
				});
			});
	}
	
	$scope.billType="CASH";
	$scope.setBillType=function(billType){
		$scope.billType=billType;
	}
	$scope.orderPayment={};
	$scope.getPaymentTypes=function(){
		$scope.paymentTypes =  [ "CASH", "CHEQUE", "DEMAND DRAFT", "CARD"];
		$scope.getCreditTypes();
	}
	
	$scope.getCreditTypes=function(){
		$scope.creditTypes =  [ "PARTIAL", "FULL"];
	}
	$scope.getSelectedCredit=function(type){
		if(type.toUpperCase() == 'PARTIAL'){
			$scope.creditType = 0;
		}else{
			$scope.creditType = 1;
		}
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
	
	$scope.getBankDepositDetails = function(paymentDetail, orderInfo){
		console.log(paymentDetail);
		$scope.depositDetails={
				paymentInfo:angular.copy(paymentDetail),
				orderInfo:angular.copy(orderInfo)
		}
		$scope.bankCredit={};
		console.log($scope.depositDetails);
		
		$http.get(webContextPath+"/bank/account/numbers")
		.success(function(data){
			console.log(data);
			if(data.statusCode == 200){
				$scope.bankCredit.bankNames=[];
				var banks=[];
				for(key in data.object){
					if(key!=undefined){
						var values =  data.object[key];
						$scope.bankCredit.bankNames.push({
							bankName:key,
							values:data.object[key]
						});
					}
				}
				console.log($scope.bankCredit.bankNames);
				$('#bankCreditModal').modal('show');
			}else if (data.statusCode == 404){
				
			}else{
				
			}
		});
		
	}
	$scope.getSelectedBankAccountNumbers=function(selectedBank){
		$scope.bankCredit.accountNumbers=[];
		$.each(selectedBank.values,function(key,val){
			$scope.bankCredit.accountNumbers.push(val);
		});
		
		var paymentDate = new Date($('#paymentdate').text().replace( /(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3"));
   	    var currentDate = new Date();
   	    
	   	 var ndays;
	     var tv1 = paymentDate.valueOf();  // msec since 1970
	     var tv2 = currentDate.valueOf();
	
	     ndays = (tv2 - tv1) / 1000 / 86400;
	     ndays = Math.round(ndays - 0.5);
	     selectedDays = ndays;
	     $("#depositDate").datepicker({ todayHighlight:'TRUE',
			 	startDate: "-"+selectedDays+"d",
			    autoclose: true,format:"dd-mm-yyyy"
		 });
	   //  $('#dateDiff').val(ndays);
   	      console.log(ndays);
	}
	
	
	$scope.saveBankCredit=function(bankCreditForm, creditdata, paymentDetails ){
		 var inValidParams=[];
			angular.forEach(bankCreditForm, function(value, key) {
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
				var depositDate = $('#depositDate').val();
				
				var orderPaymentCreditInfo={
						bankName:creditdata.bank.bankName,
						accountNumber:creditdata.accountNumber,
						depositDate:depositDate,
						orderPaymentDetailVO: paymentDetails.paymentInfo
				}
				 $http.post(webContextPath+'/bank/credit/save', orderPaymentCreditInfo)
					.success(function(data) {
						console.log(data);
					});
			}
	}
	
	$scope.getAccountDetails=function(){
		$http.get(webContextPath+"/bank/list")
		.success(function(data){
			console.log(data);
			if(data.statusCode == 200){
				$scope.bankNames=[];
				$.each(data.object,function(key,val){
					$scope.bankNames.push(val);
				});
			}
			else{
				
			}
		});
	}
	
	$scope.getBankNames=function(){
		$scope.banks =  [];
		
	}
	
	$scope.selectedOrder={};
	$scope.viewOrderPaymentScreen=function(order){
		$scope.orderNumber=order.orderNumber;
		console.log(order);
		$scope.selectedOrder=angular.copy(order);
		$scope.orderPayment={};
		$('#paymentUpdateModal').modal('show');
	}
	
	$scope.isNextPaymentDueDate="false";
	$scope.updateOrderPayment=function(orderPayment){
		console.log(orderPayment);
		var isValidPayment=false;
		if($scope.selectedOrder.dueAmount==parseInt(orderPayment.paymentAmount)){
			isValidPayment=true;
		}else if($scope.selectedOrder.dueAmount >= parseInt(orderPayment.paymentAmount)){
			isValidPayment=true;
			if(orderPayment.nextPmtDueDate == undefined || orderPayment.nextPmtDueDate == ""){
				isValidPayment=false;
				$scope.isNextPaymentDueDate="true";
			}else{
				isValidPayment=true;
				$scope.isNextPaymentDueDate="true";
			}
			
		}
	
			if(isValidPayment){
				$http.post(webContextPath+"/payment/confirm/"+$scope.orderNumber,orderPayment)
				.success(function(data){
					console.log(data);
					if(data.statusCode == 200){
						$scope.getMyAssignedOrders();
						$('#paymentWindowClose').click();
					}else if (data.statusCode == 404){
						
					}else if(data.statusCode == 500){
						
					}
				});
			}else{
				
			}
	}
	
	
	
	$scope.viewPaymentBreakups=function(order){
		console.log(order);
	}
	
	$scope.reset=function(){
		$('#danger-alert').hide();
		$scope.orderPayment={};
	 };
	$scope.errorMessage="";
	$scope.getCollectionAgentDetails=function(order, operation){
		$scope.orderCollectionAgents=[];
		$scope.selectedOrder=order;
		$scope.isAgentWindowOperation = operation;
		if(operation == "ADD"){
			$('#agentWindowDTPicker').show();
		}else{
			$('#agentWindowDTPicker').hide();
		}
		 $http.get(webContextPath+'/user/agents')
			.success(function(data) {
				console.log(data);
				if(data.statusCode==200){
					$.each(data.object,function(key,val){
						for(k in val.roles){
							console.log(val.roles[k]);
							if(val.roles[k] == "COLLECTION_MANAGER"){
								
							}
						}
						var agentInfo={
								name:val.firstName + " " +val.lastName,
								id:val.userId,
								operation:operation
						}
						$scope.orderCollectionAgents.push(agentInfo);
					})
					//$scope.orderCollection.selectedOrder=order;
					
					$('#agentModal').modal('show');
					console.log($scope.orderCollectionAgents);
					$('#danger-alert').hide();
				}else{
					$scope.errorMessage=data.message;
					 $('#danger-alert').show();
					 $('#danger-alert').alert();
		             $("#danger-alert").fadeIn(2000, 500);
				}
			});
		
	}
	
	$scope.findAllCollectionAgents=function(){
   		var appuser=this;
   	    appuser.employeeList=[];
	    	appuser.getAllEmployee=function(){
	    		employeeService.getAllEmployee()
	    		.then(function(data) {
	    			appuser.employeeList = data;
	                console.log('Employee List returned to controller from service.');
	                var allEmployees=appuser.employeeList;
	                if(allEmployees.object.length>0){
	                	$.each(allEmployees.object,function(key,val){
							for(k in val.roles){
								console.log(val.roles[k]);
								if(val.roles[k] == "COLLECTION_MANAGER"){
									
								}
							}
							var agentInfo={
									name:val.firstName + " " +val.lastName,
									id:val.userId,
									employeeId:val.employeeId
							}
							$scope.allEmployees.push(agentInfo);
						});
	                }else{
	                }
	            },
	            function(data) {
	                console.log('Employeelist retrieval failed.')
	            }); 	
	    	}
	    	 appuser.getAllEmployee();
   };
   
   $scope.getAllCollectionAgents=function(){
  		var appuser=this;
  	    appuser.collectionAgents=[];
	    	appuser.getAllAgents=function(){
	    		agentService.getAllAgents()
	    		.then(function(data) {
	    			appuser.collectionAgents = data;
	                console.log('Agent List returned to controller from service.');
	                $scope.allAgents=appuser.collectionAgents;
	                if($scope.allAgents.length>0){
	                	
	                }else{
	                	
	                }
	            },
	            function(data) {
	                console.log('Agent List retrieval failed.')
	            }); 	
	    	}
	    	 appuser.getAllAgents();
  };
	
  
  $scope.getInvoiceStatus=function(order){
	  $scope.invoiceData={};
		$http.get(webContextPath+"/invoice/details/"+order.orderNumber)
		.success(function(data){
			console.log(data);
			if(data.statusCode == 200){
				 $scope.invoiceData=angular.copy(data.object)
				$('#invoicePaymentModal').modal('show');
			}
			else if(data.statusCode == 500){
				
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
  
	$scope.getSelectedAgent=function(selectedAgent){
		console.log(selectedAgent);
		$scope.selectedAgent=selectedAgent;
		
	}
	$scope.assignAgentfor=function(){
		var nextDueDate=$('#datepicker').val();
		console.log(nextDueDate);
		var selectedOrder=$scope.selectedOrder;
		console.log(selectedOrder);
		if(nextDueDate == ""){
			nextDueDate = null;
		}
		$http.get(webContextPath+'/order/update/agent/'+selectedOrder.orderId+'/'+$scope.selectedAgent.id+'/'+nextDueDate)
		.success(function(data) {
			console.log(data);
			if(data.statusCode==200){
				$scope.getLedgerDetails();
				$('#agentClose').click();
			}else{
				
			}
		});
	}
	
	$scope.getOrderDetails=function(order){
		console.log(order);
		if(order== undefined){
			
		}else{
			$scope.collectionUser=order.collectionAgent;
		}
		$http.get(webContextPath+'/order/search/'+order.orderNumber)
		.success(function(data) {
			console.log(data);
			if(data.statusCode==200){
				if(data.object.orderPaymentDetailVOList.length>0){
					$scope.pendingOrder=data.object;
					$('#pendingOrderModal').modal('show');
				}else{
					$scope.pendingOrder=data.object;
					$('#pendingOrderModal').modal('show');
					 //$scope.errorMessage="No Payments made";
					 //$('#danger-alert').show();
					// $('#danger-alert').alert();
		            // $("#danger-alert").fadeIn(2000, 500);
				}
			}else{
				
			}
		});
	}
	
	
	$scope.viewFollowUps=function(order){
		$('#followUpModal').modal("show");
		$http.get(webContextPath+'/order/search/'+order.orderNumber)
		.success(function(data) {
			console.log(data);
			if(data.statusCode==200){
				if(data.object.orderPaymentDetailVOList.length>0){
					$scope.followUpOrder=data.object;
				}else{
					$scope.followUpOrder=data.object;
				}
			}else{
				
			}
		});
		
	}
	
	$scope.followupOption="TODAY";
	$scope.selectedFollowUpDate="";
	$scope.changeFollowUpOption=function(){
		console.log($scope.followupOption)
		if($scope.followupOption.toUpperCase() == 'DATEWISE'){
			$('#datewisefollowup').show();
		}else{
			$('#datewisefollowup').hide();
		}
		
	}
	
	$scope.followUps=[];
	$scope.totalPendingFollowUpAmount=0;
	$scope.displayFollowUpsDetails=function(){
		var followUpParams ={};
		if($scope.followupOption == {}){
			
		}else{
		if($scope.followupOption.toUpperCase() == "TODAY"){
			if($scope.selectedCollectionAgent == undefined ){
				followUpParams ={
						viewType:$scope.followupOption
				}
			}else{
				followUpParams ={
						employeeId:$scope.selectedCollectionAgent.employeeId,
						viewType:$scope.followupOption
				}
			}
		}else{
			var selectedDate=$('#followupDate').val();
			
			if($scope.selectedCollectionAgent == undefined ){
				if(selectedDate == ""){
					console.log("Enter the selected Date");
				}else{
					$scope.selectedFollowUpDate = selectedDate;
					followUpParams ={
							viewType:$scope.followupOption,
							dateWise:selectedDate
					}
				}
			}else{
				if(selectedDate == ""){
					console.log("Enter the selected Date");
				}else{
					followUpParams ={
							employeeId:$scope.selectedCollectionAgent.employeeId,
							viewType:$scope.followupOption,
							dateWise:selectedDate
					}
				}
			}
			
		}
		
			console.log(followUpParams);
			$http.post(webContextPath+"/dashboard/agent/followups",followUpParams)
			.success(function(data) {
				console.log(data);
				if(data.statusCode==200){
					$scope.followUps = [];
					$scope.totalPendingFollowUpAmount = 0;
					if(data.object.length >0 ){
						$.each(data.object,function(key,val){
							$scope.followUps.push(val);
							$scope.totalPendingFollowUpAmount=$scope.totalPendingFollowUpAmount+parseInt(val.amountDue);
						});
					}else{
						
					}
				}else{
					
				}
			});
		}
		
	}
	
/*	 $scope.loadOrderDataTable=function(orderData){
		 table=$('#orderTable').DataTable({
		       "aaData": orderData,
		        "aoColumns": [
		            { "mData": "orderNumber" },
		            { "mData": function(data){
		            	return data.customerVO.firstName +" "+data.customerVO.lastName
		            }
		            },
		            { "mData": function(data){
		            	 return data.orderAmount
		            } },
		            { "mData": function(data){
		            	return data.customerVO.mobile
		            } },
		            { "mData": function(data){
		            	if(data.collectionAgent==""){
		            	//return '<a href="javascript:void(0);" onclick="assignCollectionAgent(data)"><span class="badge">Assign Collection Agent</span></a>'
		            		return '<button type="button" class="btn btn-lg btn-danger" data-toggle="popover" title="Popover title" data-content="And here is some amazing content. It is very engaging. Right?">Click to toggle popover</button>';
		            	}else{
		            		return data.collectionAgent
		            	}
		            } },
		            { "mData": function(data){
		            	return ""
		            } },
		            { "mData": function(data){
		            	return ""
		            }  },
		            { "mData": "orderStatus" },
		        ]
		    });
		 
		 $('#orderTable_filter').addClass('pull-right');
		 $('#orderTable_paginate').addClass('pull-right');
	 }
	*/
	
}]);

function assignCollectionAgent(orderData){
	
}
