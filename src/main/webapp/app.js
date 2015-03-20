$(function () {
    ws = new WebSocket('ws://localhost:8080/ws');
    ws.onmessage = function (msg) {
        //var data = JSON.parse(msg.data);
        render(msg);
    }

    console.log("ready!");
});

var render = function(data) {
    console.log(data);
}

