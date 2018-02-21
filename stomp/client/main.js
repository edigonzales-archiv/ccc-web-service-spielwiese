var Stomp = require('@stomp/stompjs');

var url = "ws://localhost:8080/ws";
var stompClient = Stomp.client(url);

function connect() {

    var headers = {
        login: 'mylogin',
        passcode: 'mypasscode',
        // additional header
        'client-id': 'my-client-id'
    };  
    stompClient.connect("stefan", "Ziegler", onConnected, onError);
}

function onConnected() {
    console.log("connected");

    stompClient.subscribe('/user/queue/reply', onMessageReceived);
    console.log("subscribed")


    var chatMessage = {
        sender: "stefan",
        content: "Ich bin die Nachricht.",
        type: 'CHAT'
    };
    stompClient.send("/app/fubar", {token: '2b4ae06e-14ce-11e8-b642-0ed5f89f718b'}, JSON.stringify(chatMessage));
    console.log("message gesendet")
}

function onError(error) {
    console.log("error: ");
    console.log(error);
}

function onMessageReceived(payload) {
    console.log("message received");
    console.log(payload);
}


connect();


console.log("Hello, World!");
