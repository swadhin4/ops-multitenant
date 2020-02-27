function ToasterUtils() {
	/**
	 * 0-success
	 * 1-info
	 * 2-warning
	 * 3-error
	 */
	var type = [ "success", "info", "warning", "error" ];
	/**
	 * 0-toast-top-right
	 * 1-toast-bottom-right
	 * 2-toast-bottom-left
	 * 3-toast-top-left
	 * 4-toast-top-full-width
	 * 5-toast-bottom-full-width
	 * 6-toast-top-center
	 * 7-toast-bottom-center
	 */
	var position = [ "toast-top-right", "toast-bottom-right",
			"toast-bottom-left", "toast-top-left", "toast-top-full-width",
			"toast-bottom-full-width", "toast-top-center",
			"toast-bottom-center" ];
	toastr.options = {
			  "closeButton": true,
			  "debug": false,
			  "newestOnTop": true,
			  "progressBar": false,
			  "positionClass": "toast-top-full-width",
			  "preventDuplicates": false,
			  "onclick": null,
			  "showDuration": "3000",
			  "hideDuration": "1000",
			  "timeOut": "5000",
			  "extendedTimeOut": "1000",
			  "showEasing": "swing",
			  "hideEasing": "linear",
			  "showMethod": "fadeIn",
			  "hideMethod": "fadeOut"
	};
	
	return {
	   	showSuccess : function(msg,title){
	   		toastr.options.positionClass = position[4];
	   		toastr[type[0]](msg, title);
	   	},
	   	showError : function(msg,title){
	   		toastr.options.positionClass = position[4];
	   		toastr[type[3]](msg, title);
	   	},
	   	showInfo : function(msg,title){
	   		toastr.options.positionClass = position[4];
	   		toastr[type[1]](msg, title);
	   	},
	   	showWarning : function(msg,title){
	   		toastr.options.positionClass = position[4];
	   		toastr[type[2]](msg, title);
	   	},
	   	show : function(toasterType,toasterPosition,msg,title){
	   		toastr.options.positionClass = position[toasterPosition];
	   		toastr[type[toasterType]](msg, title);
	   	}
	};
}