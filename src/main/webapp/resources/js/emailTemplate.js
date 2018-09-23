function initemailTemplateGrid(){
  
  //var toasterUtils = ToasterUtils();
  //var editUserAction = false;
  var actionUrl = webContextPath + "/emailTemplate/records";
  return function() {
   
    $("#emailTemplateGrid").jqGrid(
        {
          url : actionUrl,
          datatype : 'json',
          mtype : 'GET',
          colNames : [ 'Id', "Name",
              'Subject', "Created At" ],
          colModel : [ {
            name : 'id',
            index : 'id',
            width : 55,
            editable : false,
            editoptions : {
              readonly : true,
              size : 10
            },
            hidden : true
          }, {
            name : 'name',
            index : 'name',
            width : 100,
            formatter:showtemplate,
            editable : true,
            editrules : {
              required : true
            },
            editoptions : {
              size : 20
            }
          }, {
            name : 'subject',
            index : 'subject',
            width : 100,
            formatter:showtemplate
          },{
            name : 'createdAt',
            index : 'createdAt',
            width : 50,
            formatter:showtemplate
          
          } ],
          postData : {},
          rowNum : 20,
          rowList : [ 20, 30, 40, 50 ],
          height : 450,
          width : 900,
          rownumbers : true,
          pager : '#emailTemplatePager',
          sortname : 'id',
          viewrecords : true,
          sortorder : "asc",
          caption : "Email Template",
          emptyrecords : "Empty records",
          loadonce : false,
          hidegrid: false, 
          loadComplete : function() {
          },
          jsonReader : {
            root : "rows",
            page : "page",
            total : "total",
            records : "records",
            repeatitems : false,
            cell : "cell",
            id : "id"
          }
        });

    $("#grid").jqGrid('navGrid', '#emailTemplatePager', {
      edit : false,
      add : false,
      del : false,
      search : true,
      excel : true,
        searchtext: "Filter",
        searchtitle: "Filter",
        refreshtext: "Refresh",
        refreshtitle: "Refresh"
    }, {}, {}, {}, { //search
      sopt : [ 'cn', 'eq', 'ne', 'lt', 'gt', 'bw', 'ew' ],
      closeOnEscape : true,
      multipleSearch : true,
      closeAfterSearch : false
    });

    $("#emailTemplateGrid").navButtonAdd('#emailTemplatePager', {
      caption : "Edit",
      buttonicon : "ui-icon-pencil",
      onClickButton : editRow,
      position : "last",
      title : "",
      cursor : "pointer"
    });

    
  };

  

  function editRow() {
    $("#emailTemplateGrid").jqGrid('setColProp', 'userName', {
      editoptions : {
        readonly : true,
        size : 10
      }
    });
    $("#emailTemplateGrid").jqGrid('setColProp', 'password', {
      hidden : false
    });
    $("#emailTemplateGrid").jqGrid('setColProp', 'password', {
      editrules : {
        required : false
      }
    });

    // Get the currently selected row
    var row = $('#grid').jqGrid('getGridParam', 'selrow');

    if (row != null) {
      //editUserAction = true;
      $('#grid').jqGrid('editGridRow', row, {
        url : webContextPath + '/user/update',
        editData : {},
        recreateForm : true,
        beforeShowForm : function(form) {
          $('#pData').hide();
          $('#nData').hide();
        },
        beforeInitData : function(form) {
        },
        closeAfterEdit : true,
        reloadAfterSubmit : true,
        afterSubmit : function(response, postdata) {

          //var result = eval('(' + response.responseText + ')');
          if (response.statusCode == 0) {
            $('#msgbox').text(response.message);
            $('#msgbox').dialog({
              title : 'Success',
              modal : true,
              buttons : {
                "Ok" : function() {
                  $(this).dialog("close");
                }
              }
            });
          } else {

          }
          // only used for adding new records
          var newId = null;

          return [ "false", "", newId ];
        }
      });
    } else {
      $('#msgbox').text('You must select a record first!');
      $('#msgbox').dialog({
        title : 'Error',
        modal : true,
        buttons : {
          "Ok" : function() {
            $(this).dialog("close");
          }
        }
      });
    }
    $("#emailTemplateGrid").jqGrid('setColProp', 'userName', {
      editoptions : {
        readonly : false,
        size : 10
      }
    });
    $("#emailTemplateGrid").jqGrid('setColProp', 'password', {
      hidden : true
    });

  }

  function deleteRow() {
    //editUserAction = false;
    // Get the currently selected row
    var row = $('#grid').jqGrid('getGridParam', 'selrow');

    // A pop-up dialog will appear to confirm the selected action
    if (row != null)
      $('#grid').jqGrid(
          'delGridRow',
          row,
          {
            url : webContextPath + '/user/delete',
            recreateForm : true,
            beforeShowForm : function(form) {
              //Change title
              $(".delmsg").replaceWith(
                  '<span style="white-space: pre;">'
                      + 'Delete selected record?'
                      + '</span>');
              //hide arrows
              $('#pData').hide();
              $('#nData').hide();
            },
            reloadAfterSubmit : true,
            closeAfterDelete : true,
            serializeDelData : function(postdata) {
              var rowdata = $('#grid').getRowData(postdata.id);
              // append postdata with any information 
              return {
                id : postdata.id,
                oper : postdata.oper,
                userName : rowdata.userName
              };
            },
            afterSubmit : function(response, postdata) {
              //var result = eval('(' + response.responseText + ')');
              if (response.statusCode == 0) {
                $('#msgbox').text(response.message);
                $('#msgbox').dialog({
                  title : 'Success',
                  modal : true,
                  buttons : {
                    "Ok" : function() {
                      $(this).dialog("close");
                    }
                  }
                });
              } else {

              }
              // only used for adding new records
              var newId = null;

              return [ "false", "", newId ];
            }
          });
    else {
      $('#msgbox').text('You must select a record first!');
      $('#msgbox').dialog({
        title : 'Error',
        modal : true,
        buttons : {
          "Ok" : function() {
            $(this).dialog("close");
          }
        }
      });
    }
  }
  function showtemplate(cellvalue, options, rowObject){
    var link = "<a href='javascript:void(0);' onclick=openTemplateModal('"+rowObject.id+"')>"+cellvalue+"</a>";
    return link;
  }
}
function openTemplateModal(templateID){
  $.ajax({
    url:webContextPath+"/emailTemplate/template/"+templateID,
    type: 'get', 
    success: function(data){
      $( "#dialog").append(data);
      $( "#dialog" ).dialog( "open" );
    },
  error: function(textStatus,errorThrown){
    alert(textStatus+""+errorThrown);
}
  });
  
  //$('#emailModal').modal('show');
}