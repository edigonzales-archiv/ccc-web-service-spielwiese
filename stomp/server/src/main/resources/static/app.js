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
	var client = $("#client").val();
	//console.log(client);
	
	var sessionId = $("#sessionid").val();
	//console.log(sessionId);
	
	
	stompClient = Stomp.client('ws://localhost:8080/ccc/register'); // TODO: Avoid url, port & server context if client code is in the same server.
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        
        var headers = {'sessionId': sessionId, 'client': client};
        stompClient.subscribe('/queue/ccc_' + sessionId, function (greeting) {
            //showCccResponse(JSON.parse(greeting.body).content);
        		showCccResponse(greeting.body);
        }, headers);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendCccMessage() {
	var client = $("#client").val();
	var sessionId = $("#sessionid").val();
	
	var application = $("#application").val();
	var message = $("#message").val();
			
    var headers = {'sessionId': sessionId, 'client': client};
    stompClient.send("/app/" + application, headers, message);
}

function showCccResponse(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendCccMessage(); });
});

