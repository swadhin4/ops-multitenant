chrisApp.controller('reportController', 
		 ['$rootScope', '$scope', '$filter','authService','userService','reportService',
		  function  ($rootScope, $scope , $filter,authService,userService,reportService) {
			 
			 $scope.totalIncidentReport=0;
			 $scope.reportList = [];
			 angular.element(document).ready(function () {
				 $scope.getLoggedInUserAccess();
				
			 });
			 
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
			 	$scope.getLoggedInUser=function(loginUser){
					userService.getLoggedInUser(loginUser)
		    		.then(function(data) {
		    			console.log(data)
		    			if(data.statusCode == 200){
		    				$scope.sessionUser=angular.copy(data.object);
		    				$scope.getIncidentReports(loginUser);
		    			}
		            },
		            function(data) {
		                console.log('No User available')
		            });
				}
			 	
			 	$scope.getIncidentReports=function(loginUser){
					reportService.getIncidentReports(loginUser)
		    		.then(function(data) {
		    			console.log(data)
		    			if(data.statusCode == 200){
		    				 $scope.totalIncidentReport=data.object.length;
		    				 $scope.reportList= data.object;
		    				 if(data.object.length>0){
		    					 populateDataTable1($scope.reportList, 'ticketList');
		    				 }
		    				
		    				$('#ticketList_filter').addClass("pull-right");
		    			}
		            },
		            function(data) {
		                console.log('No Reports available')
		            });
				}
			 	
			 	$scope.exportReportToCSV=function(){
			 					 		
			 		$scope.JSONToCSVConvertor($scope.reportList,$scope.sessionUser.firstName + " "+ $scope.sessionUser.lastName, true)
			 	}
			 	
			 	$scope.JSONToCSVConvertor=function(JSONData, ReportTitle, ShowLabel) {
				    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
			 		for(var i = 0; i < JSONData.length; i++) {
			 		    delete JSONData[i]['id'];
						delete JSONData[i]['spId'];
			 		    delete JSONData[i]['ticketId'];
			 		}
			 		
			 		for(var i = 0; i<JSONData.length;i++) {
			 		    var a = JSONData[i];
			 		    for (var key in a) {
			 		        var temp; 
			 		        if (a.hasOwnProperty(key)) {
			 		          temp = a[key];
			 		          delete a[key];
			 		          
			 		          a[key.charAt(0).toUpperCase() + key.substring(1)] = temp;
			 		        }
			 		    }
			 		   JSONData[i] = a;
			 		}
				    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
				    
				    var CSV = '';    
				    //Set Report title in first row or line
				    
				    CSV += ReportTitle + '\r\n\n';

				    //This condition will generate the Label/Header
				    if (ShowLabel) {
				        var row = "";
				        
				        //This loop will extract the label from 1st index of on array
				        for (var index in arrData[0]) {
				            
				            //Now convert each value to string and comma-seprated
				            row += index + ',';
				        }

				        row = row.slice(0, -1);
				        
				        //append Label row with line break
				        CSV += row + '\r\n';
				    }
				    
				    //1st loop is to extract each row
				    for (var i = 0; i < arrData.length; i++) {
				        var row = "";
				        
				        //2nd loop will extract each column and convert it in string comma-seprated
				        for (var index in arrData[i]) {
				            row += '"' + arrData[i][index] + '",';
				        }

				        row.slice(0, row.length - 1);
				        
				        //add a line break after each row
				        CSV += row + '\r\n';
				    }

				    if (CSV == '') {        
				        alert("Invalid data");
				        return;
				    }   
				    
				    //Generate a file name
				    var fileName = "";
				    //this will remove the blank-spaces from the title and replace it with an underscore
				    fileName += ReportTitle.replace(/ /g,"_");   
				    
				    //Initialize file format you want csv or xls
				    var uri = 'data:text/csv;charset=utf-8,' + escape(CSV);
				    
				    // Now the little tricky part.
				    // you can use either>> window.open(uri);
				    // but this will not work in some browsers
				    // or you will not get the correct file extension    
				    
				    //this trick will generate a temp <a /> tag
				    var link = document.createElement("a");    
				    link.href = uri;
				    
				    //set the visibility hidden so it will not effect on your web-layout
				    link.style = "visibility:hidden";
				    link.download = fileName + "_Incidents.csv";
				    
				    //this part will append the anchor tag and remove it after automatic click
				    document.body.appendChild(link);
				    link.click();
				    document.body.removeChild(link);
				}
			 	
			 	
			 
}]);