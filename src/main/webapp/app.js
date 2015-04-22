$(function () {
    var mapOptions = {
        center: { lat: 20, lng: 0},
        zoom: 3
    };
    var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

    var protocol = location.protocol === "https:" ? "wss:" : "ws:";

    ws = new WebSocket(protocol + "//" + location.host + "/ws");

    ws.onmessage = function (msg) {
        var data = JSON.parse(msg.data);
        render(data, map);
    }
});

var render = function(data, map) {
    console.log(data);

    var myLatlng = new google.maps.LatLng(data.geoLocation.latitude, data.geoLocation.longitude);

    // To add the marker to the map, use the 'map' property
    var marker = new google.maps.Marker({
        position: myLatlng,
        map: map,
        animation: google.maps.Animation.DROP,
        title: data.text
    });
}
