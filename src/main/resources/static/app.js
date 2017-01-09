var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#updates").show();
    }
    else {
        $("#updates").hide();
    }
    $("#pullUpdates").html("");
}

function connect() {
    var socket = new SockJS('/upload-status-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        setConnected(true);
        stompClient.subscribe('/user/queue/pull-status', function (statusUpdate) {
            showStatus(JSON.parse(statusUpdate.body).message);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
}

function sendName() {
    stompClient.send("/app/pull-link", {}, JSON.stringify({'link': $("#url").val()}));
}

function showStatus(message) {
    $("#pullUpdates").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});