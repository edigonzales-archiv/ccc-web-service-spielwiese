var Stomp = require('@stomp/stompjs');

var url = "ws://localhost:8080/ws";
var client = Stomp.client(url);

client.connect( "", "", function(frame) {

    // once connected...
    console.log("fubar");
})

console.log("Hello, World!");
