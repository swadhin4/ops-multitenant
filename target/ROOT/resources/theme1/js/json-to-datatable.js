var oTableAssum=null;
var ok2;
function populateDataTable1(data, tableDivName){
	console.log("Swadhin");
	var finalKeys = [];
	var finalValues = [];

	var retObj = null;
	if($.isArray(data)){
		retObj=data;

	}
	else{
		for(key in data){
			if($.isArray(data[key])){
				retObj=data[key];
			}else{
			$.each(data, function(item, val) {
				if (val instanceof Object) {
					retObj = val;
				}
			  });
			}
		}
		
	}
	retObj = retObj == null ? data : retObj; 
	var objectZero = null;
	var keepGoing = true;
	if ($.isArray(retObj)) {
		$.each(retObj, function(item, val) {
			if (keepGoing) {
				if (item == 0) {
					objectZero = val;
					keepGoing = false;
				}
			}
		});
	} else if (retObj instanceof Object) {
		objectZero = retObj;
		keepGoing = false;

	}


	$.each(objectZero, function(key, value) {
		var strLabel=key.replace(/([a-z])([A-Z])/g, '$1 $2');
		if(strLabel.toUpperCase()=="TICKET START TIME"){
			finalKeys.push("ISSUE START TIME");
		}else{
			finalKeys.push(strLabel.toUpperCase());
		}
		
		finalValues.push(key);
	});
	jsonToDataTableConversion(finalKeys,finalValues,retObj,tableDivName);
}

function jsonToDataTableConversion(finalKeys, finalValues,retObj, tableDivName){
	var arrayForTable = [];
	parentJSONLinkObject=[];
	serviceLinkTitles=[];
	
	$.each(finalKeys, function(index, value) {
		var myColumns={};
		var primaryLink=0;
		var parentLinkedObjectid=-1;
		var searchable=false;
		if(index==0 || index==1){
			searchable=true;
		}
		else{
			searchable=false;
		}
		
		myColumns={
				'id'	: index,  
				'sTitle' : finalKeys[index],
				'mData' : finalValues[index],
				'sWidth' : "100%",
				//"render": function ( data, type, full, meta ) {
					//if($.isArray(data)){
						/*parentJSONLinkObject.push({
							"parentLinkedObjectid" : parentLinkedObjectid++,
							"value":data
						});
						
						primaryLinkObject=data;
						var lowerLink=finalKeys[index].toLowerCase();
						var finalLinkValue=lowerLink.charAt(0).toUpperCase() + lowerLink.slice(1);
						var properLinkString=finalLinkValue.replace(/\w\S*///g, function(txt){
							//return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
						//});
						//return '<a href="javascript:void(0);" onclick="linkObjectDetails('+parentLinkedObjectid+',\''+properLinkString+'\');" data-toggle="modal" data-target=".bs-example-modal-lg">'+properLinkString+'</a>';
						//return '<a href="javascript:void(0);" onclick="showRawDataMessageLink('+parentLinkedObjectid+',\''+properLinkString+'\');">'+properLinkString+'</a>';*/
					//}
				//	else{
					//	serviceResultJson=retObj;
					//    serviceLinkTitles.push(data);
					//	return data;
					//} 
				//},
		}
		arrayForTable.push(myColumns);
	});


	//////console.log(arrayForTable);
	displayDataTable(arrayForTable,retObj, tableDivName)
}

function displayDataTable(arrayForTable,jsonObject, tableDivName){
	var jsonObjArray = [];
	if($.isArray(jsonObject)){
		jsonObjArray = jsonObject;
	}else{
		jsonObjArray.push(jsonObject);
	}
	if(oTableAssum==null){
		oTableAssum = $('#'+tableDivName).dataTable({			
			"bInfo": true,
			"bLengthChange": true,
		    "bFilter": true,
			"bDestroy": true,  
			'aoColumns': arrayForTable,
			'aaData':jsonObject,
			'useFloater': false,
			'isResponsive': true,
			'plugins': ["C"],
			"oColVis": { //configure the column vis plugin
				"activate": "mouseover",
				"aiExclude": [ 0 ], // don't allow 2nd column to be hidden
				"colVisDropdownSelector": ".col-vis-dropdown-container" //put the column hiding control inside the element at this selector
			},
			"columnDefs": [
			               { "visible": false, "targets": [0,1,2,7], className: 'hidden' }
			           ]

		});
	}
	else{
		oTableAssum.fnClearTable();
		oTableAssum=null;
		//$('#jsonTable').empty();
		$('#'+tableDivName+' thead').html('');
		oTableAssum = $('#'+tableDivName).dataTable({			
			"bInfo": true,
			"bLengthChange": true,
		    "bFilter": true,
			"bDestroy": true,  
			'aoColumns': arrayForTable,
			'aaData':jsonObject,
			'useFloater': false,
			'isResponsive': true,
			'plugins': ["C"],
			"oColVis": { //configure the column vis plugin
				"activate": "mouseover",
				"aiExclude": [ 0 ], // don't allow 2nd column to be hidden
				"colVisDropdownSelector": ".col-vis-dropdown-container" //put the column hiding control inside the element at this selector
			},
			"columnDefs": [
			               { "visible": false, "targets": [0,1,2,7], className: 'hidden' }
			           ]
		});

		$('#'+tableDivName+'_filter').removeClass('form-control search-query');
	}
	jsonArrForExport=jsonObjArray;
}

	