function sendMessage() {
    var userInput = document.getElementById('user-input').value;
    if (userInput.trim() !== '') {
        appendMessage('You: ' + userInput, 'user-message'); // Display user message
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
                    .then(response => response.json())
                    .then(data => {
                        hideTypingIndicator();
                        appendMessage('Bot: ' + data.response, 'chatbot-message'); // Display chatbot response
                    })
                    .catch(error => console.error('Error:', error))
            }, 1000);

            document.getElementById('user-input').value = '';
        });

    }

    function appendMessage(message, className) {
        var chatMessages = document.getElementById('chat-messages');
        var messageDiv = document.createElement('div');
        messageDiv.textContent = message;
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
}
