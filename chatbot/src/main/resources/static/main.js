'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;
var mostRecentMessage = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

const connect = async (event) => {
    username = document.querySelector('#name').value.trim();
    if(username) {
        axios.post("http://localhost:8080/chat/current", {
            username: username
        })
            .then(response => {
               // Handle response
               mostRecentMessage = response.data.message;
               usernamePage.classList.add('hidden');
               chatPage.classList.remove('hidden');
               connectingElement.classList.add('hidden');

               createMessageElement(mostRecentMessage, "LISA");
            })
    }
    event.preventDefault();
}

function createMessageElement(message, sender) {
    var messageElement = document.createElement('li');

    messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(sender[0]);

    avatarElement.style['background-color'] = getAvatarColor(sender);

    avatarElement.appendChild(avatarText);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);


    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;

}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent) {
        createMessageElement(messageContent, username);
        messageInput.value = '';

        axios.post("http://localhost:8080/chat/", {
            headers: {
                'Content-Type': 'application/json'
            },
            'username': username,
            'message': messageContent
        })
            .then(response => {
               // Handle response
               mostRecentMessage = response.data.message;
               createMessageElement(mostRecentMessage, "LISA");
            })
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    createMessageElement(message);
}

function getAvatarColor(messageSender) {
    var index = 0;
    if (messageSender == "LISA") {
        index = colors.length-1;
    } else {
        var hash = 0;
        for (var i = 0; i < messageSender.length; i++) {
            hash = 31 * hash + messageSender.charCodeAt(i);
        }
        index = Math.abs(hash % (colors.length-1));
    }
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)