function sendMessage() {
    var userInput = document.getElementById('user-input').value;
    if (userInput.trim() !== '') {
        document.getElementById('user-input').value = '';
        document.getElementById('im').style.display='none'
        appendMessage('You: ' + userInput, 'user-message');
        showTypingIndicator();

        setTimeout(function () {
            fetch('/patient/chatbot', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    message: userInput
                })
            })
                .then(response => response.json())
                .then(data => {
                    hideTypingIndicator();
                    appendMessage('Bot: ' + data.response, 'chatbot-message');

                    // Focus back on the input box after receiving the response
                    document.getElementById('user-input').focus();
                })
                .catch(error => console.error('Error:', error));

            document.getElementById('user-input').value = '';
        }, 1000);
    }
}

document.getElementById('user-input').addEventListener('keydown', function (event) {
    if (event.keyCode === 13) { // Enter key code is 13
        event.preventDefault(); // Prevent default Enter key behavior (e.g., line break in textarea)
        sendMessage(); // Call sendMessage function when Enter key is pressed
    }
});



function appendMessage(message, className) {
    var chatMessages = document.getElementById('chat-messages');
    var messageDiv = document.createElement('div');
    messageDiv.innerHTML = message;
    messageDiv.classList.add(className);
    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function showTypingIndicator() {
    appendMessage('Bot is typing...', 'typing-indicator');
}

function hideTypingIndicator() {
    var typingIndicator = document.querySelector('.typing-indicator');
    if (typingIndicator) {
        typingIndicator.remove();
    }
}

function backButton() {
    window.history.back();
}