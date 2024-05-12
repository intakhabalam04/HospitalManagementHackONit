/*
document.querySelector('.split-form').addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent the form from submitting normally

    let formData = new FormData(this);


    fetch('?q=register', { // The URL to send the request to
        method: 'POST', // The method to use (POST, GET, etc)
        body: formData // The data to send
    })
    .then(response => response.json()) // Parse the JSON response
    .then(data => {
        console.log(data); // Log the data for debugging purposes
        // Here you can handle the response (e.g., show a success message, redirect the user, etc.)
    })
    .catch(error => {
        console.error('Error:', error); // Log any errors
    });
});*/
