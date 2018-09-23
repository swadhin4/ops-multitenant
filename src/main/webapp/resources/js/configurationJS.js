function renderConfigurationGrid(){
	
	var actionUrl = webContextPath + '/configuration/grid/records';
	  var statusValues = "INITIAL:INITIAL;COMPLETE:COMPLETE;APPROVED:APPROVED;REJECTED:REJECTED;ERROR:ERROR";
	  var lastsel="";
	  $("#configurationgrid").jqGrid({
		  url: actionUrl,
          mtype: "get",
          datatype: "json",
          height: 450,
          width: 900,
          colNames: ['ID', 'Module', 'Config Key', 'Config Value',
              'Config Datatype'],
          colModel:[{
        	  name: 'id',
              index: 'id',
              hidden: true,
              width: 20,
              sorttype: "int",
              editable: false,
              editoptions: {
                readonly: true,
                size: 10
              }  
          }, {
              name: 'module',
              index: 'module',
              width: 210,
              sorttype: 'text',
              editable: false,
              search:false
            },
            {
                name: 'configKey',
                index: 'configKey',
                width: 220,
                sorttype: 'text',
                editable: false
              },
              {
                  name: 'configValue',
                  index: 'configValue',
                  width: 210,
                  sorttype: 'text',
                  search:false,
					editable: true,
					editrules : {
						required : true
					}
                },
                {
                    name: 'configDatatype',
                    index: 'configDatatype',
                    width: 210,
                    sorttype: 'text',
                    editable: false,
                    search:false
                  }
            ],
            postData : {},
            caption: "Configuration Seetings",
            pager: '#configurationPager',
            rowNum: 20,
            rownumbers: true,
            rowList: [20, 40, 60],
            sortname: 'module',
            viewrecords: true,
            emptyrecords: "Empty records",
            shrinkToFit: false,
            sortorder: "asc",
            loadonce: false,
            hidegrid: false,
            loadComplete: function() {
            },
            jsonReader: {
              root: "rows",
              page: "page",
              total: "total",
              records: "records",
              repeatitems: false,
              cell: "cell",
              id: "id"
            },
			onSelectRow: function(id){
				console.log(lastsel,'...done...',id);
					console.log('...inside...');
					$('#configurationgrid').jqGrid('restoreRow',lastsel);
					$('#configurationgrid').jqGrid('editRow',id,true);
					lastsel=id;
			},
			editurl: webContextPath + "/configuration/grid/edit.json"
	  });

$("#configurationgrid").jqGrid('navGrid', '#configurationPager', {
    edit: false,
    add: false,
    del: false,
    search: true,
    excel: false,
    searchtext: "Filter",
    searchtitle: "Filter",
    refreshtext: "Refresh",
    refreshtitle: "Refresh"
  }, {}, {}, {}, { // search
    sopt: ['cn', 'eq', 'ne', 'lt', 'gt', 'bw', 'ew'],
    closeOnEscape: true,
    multipleSearch: true,
    closeAfterSearch: false
  });

}