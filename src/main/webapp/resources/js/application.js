var webContextPath;
var homeDataObject;
var trueOrFalseOption = "true:true;false:false";
var jqgridUserRolesFilter;

$(document).ready(function() {

	jqgridUserRolesFilter = getRoles();
	startTime();
});

function startTime() {
	$('#dateTime').text(getDateTime());
	t = setTimeout(function() {
		startTime();
	}, 500);
}

function getDateTime() {
	var today = new Date();

	var month = today.getMonth() + 1;
	var day = today.getDate();

	var output = today.getFullYear() + '/'
			+ (('' + month).length < 2 ? '0' : '') + month + '/'
			+ (('' + day).length < 2 ? '0' : '') + day;

	var h = today.getHours();
	var m = today.getMinutes();
	var s = today.getSeconds();
	// add a zero in front of numbers<10
	m = checkTime(m);
	s = checkTime(s);
	var finalTime = output + " " + h + ":" + m + ":" + s;
	return finalTime;
}

function checkTime(i) {
	if (i < 10) {
		i = "0" + i;
	}
	return i;
}

function getRoles() {
	var rolesUrl = webContextPath + "/user/roles";
	var roleResponse = "";
	$.ajax({
		type : "get",
		url : rolesUrl,
		async : false,
		success : function(data, textStatus) {
			roleResponse = data;
		},
		error : function(textStatus, errorThrown) {
		}
	});
	console.log('role response..', roleResponse);
	return roleResponse;
}

/**
 * 
 * inEffectDuration:  600,   // in effect duration in miliseconds
	stayTime:         3000,   // time in miliseconds before the item has to disappear
	text:               '',   // content of the item
	sticky:          false,   // should the toast item sticky or not?
	type:         'notice',   // notice, warning, error, success
	position:  'top-right',   // top-left, top-center, top-right, middle-left, middle-center, middle-right
                          // Position of the toast container holding different toast.
                          // Position can be set only once at the very first call,
                          // changing the position after the first call does nothing
	closeText:         '',    // text which will be shown as close button,
                          // set to '' when you want to introduce an image via css
	close:            null    // callback function when the toastmessage is closed
 */
function showToastSuccessMessage(message){
	console.log('...show message...');
	 $().toastmessage('showToast', {
         text     : message,
         sticky   : false,
         position : 'middle-center',
         type     : 'success',
         closeText: '',
         close    : function () {
             console.log("toast is closed ...");
         }
     });
}

function showToastErrorMessage(message){
	 $().toastmessage('showToast', {
         text     : message,
         sticky   : false,
         position : 'middle-center',
         type     : 'error',
         closeText: '',
         close    : function () {
             console.log("toast is closed ...");
         }
     });
}

function jqGridDatePicker(elem) {
	  $(elem).datepicker({
	    dateFormat: "dd-mm-yy"
	  });
}