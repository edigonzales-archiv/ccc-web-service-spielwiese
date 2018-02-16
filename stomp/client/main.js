var Stomp = require('@stomp/stompjs');

var url = "ws://localhost:8080/ws";
var client = Stomp.client(url);

/*
client.connect( "", "", function(frame) {

    // once connected...
    console.log("fubar");
})
*/

var connect_callback = function(frame) {
    // called back after the client is connected and authenticated to the STOMP server
    console.log("connected");
};

var error_callback = function(frame) {
    // called back after the client is connected and authenticated to the STOMP server
    console.log("error. not connected");
};

client.connect("", "", connect_callback, error_callback);





console.log("Hello, World!");
