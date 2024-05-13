document.addEventListener('DOMContentLoaded', function () {
    var canvas = document.getElementById('signature');
    var signaturePad = new SignaturePad(canvas);

    var clearButton = document.getElementById('clearSignature');
    clearButton.addEventListener('click', function () {
        signaturePad.clear();
    });

    var form = document.getElementById('donationForm');
    form.addEventListener('submit', function () {
        document.getElementById('signatureData').value = signaturePad.toDataURL();
    });
});
document.addEventListener('DOMContentLoaded', function () {
    var canvas = document.getElementById('signature');
    var signaturePad = new SignaturePad(canvas);

    var clearButton = document.getElementById('clearSignature');
    clearButton.addEventListener('click', function () {
        signaturePad.clear();
    });

    var form = document.getElementById('donationForm');
    form.addEventListener('submit', function (event) {
        event.preventDefault(); // Prevent the form from submitting normally

        var signatureData = signaturePad.toDataURL();
        document.getElementById('signatureData').value = signatureData;

        // Serialize form data into a JSON object
        var formData = new FormData(form);
        var data = {};
        formData.forEach((value, key) => {
            if (value === "Yes") {
                data[key] = true;
            } else if (value === "No") {
                data[key] = false;
            } else {
                data[key] = value;
            }
        });
        console.log(data);
        // Send a POST request to the server
        fetch('/patient/donate-organs', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        }).then(json => {
            console.log(json);
            // Handle the response here
        }).catch(e => {
            console.error('An error occurred', e);
        });
    });
});