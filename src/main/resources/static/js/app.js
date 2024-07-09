const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/greeting-websocket'
});

let roomNumber = 110; // 일단 예시로 방 번호 110로 설정

stompClient.onConnect = (frame) => {

    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings/' + roomNumber, (greeting) => {
        const message = JSON.parse(greeting.body);
        showGreeting(message);
    });

};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

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
    stompClient.activate();
    console.log("Activated");
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var nameValue = $("#name").text();
    var message = $("#message").val();
    var userId = $("#userId").text();

    document.getElementById('message').value = '';

    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': nameValue, 'message': message, 'roomId' : roomNumber, 'userId' : userId})
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message.username + "  " + message.content + "</td></tr>");
}


$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});