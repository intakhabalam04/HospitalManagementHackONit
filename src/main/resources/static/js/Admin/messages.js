document.addEventListener("DOMContentLoaded", function() {
    const messageList = document.getElementById("messageList");
    const sendMessageForm = document.getElementById("sendMessageForm");

    sendMessageForm.addEventListener("submit", function(event) {
        event.preventDefault();

        const recipient = document.getElementById("recipient").value;
        const message = document.getElementById("message").value;

        // Send message to the server
        fetch('/patient/message', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ recipient: recipient, message: message }),
        })
            .then(response => response.json())
            .then(data => {
                console.log("Server response:", data);
                // Assuming the server responds with data or a success message
                // Add the message to the message list on successful submission
                const messageItem = document.createElement("div");
                messageItem.classList.add("message-item");
                messageItem.innerHTML = `<strong>${recipient}:</strong> ${message}`;
                messageList.appendChild(messageItem);
                sendMessageForm.reset();
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    });
});

function backButton() {
    window.history.back();
}
