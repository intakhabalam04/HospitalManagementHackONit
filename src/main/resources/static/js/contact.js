document.addEventListener("DOMContentLoaded", function () {
    const contactForm = document.getElementById('contactForm');

    contactForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const message = document.getElementById('message').value;

        const contactDetails = {
            name: name,
            email: email,
            message: message
        }

        fetch('/send_message', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(contactDetails)
        })
            .then(response => response.json())
            .then(data => {
                alert("Message sent successfully!")
                window.location.reload();
            })
            .catch(error => {
                console.error('Error sending message:', error);
            });
    });
});