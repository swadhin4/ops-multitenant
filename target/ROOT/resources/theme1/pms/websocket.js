var stompClient = null;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
	console.log('Connected called');
	var today = moment().format("MMM DD, YYYY");
	 var notification={
				notificationDate:today,
				msgArr:[],
				msgCount:0
		 };
	 
	 $.jStorage.set('todaysNotification', notification);  
    var socket = new SockJS('/notify-socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/adminnotification', function (notificationResponse) {
        	console.log("REceived msg "+notificationResponse.body);
            displayNotifications(JSON.parse(notificationResponse.body));
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    } 
    setConnected(false);
    console.log("Disconnected");
}

/*function sendName(empName) {
    stompClient.send("/saiapp/notify", {}, JSON.stringify({'name': empName.firstName + ""+ empName.lastName}));
}*/

function sendNotification(msgObj) {
	console.log("sendNotification - "+msgObj);
    stompClient.send("/pms/notify", {}, msgObj);
}

function displayNotifications(messageObject) {
	console.log("displayNotifications - "+messageObject.content);
	var notifyObj=  $.jStorage.get('todaysNotification');  
	console.log("notifyObj "+notifyObj.msgCount);
	if(notifyObj!=null){
		 $("#msgDropdown").append('<li> <a href="#">'
          		 +'<i class="fa fa-users text-aqua"></i>'+ messageObject.content	
        		  +'</a></li>');
		notifyObj.msgArr.push(messageObject.content);
		$('#msgCountElmId2').text(notifyObj.msgArr.length);
		$('#msgCountElmId').text(notifyObj.msgArr.length);
	}
	
	
	 
}



function displayNotificationAlert(count) {
	console.log("displayNotificationAlert - "+count);
   $('#msgCountElmId').text(count);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
/*    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });*/
    connect();
});

