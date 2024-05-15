window.onload = function () {
    // Get the plan number from the URL
    var urlParams = new URLSearchParams(window.location.search);
    var plan = urlParams.get('plan');

    let apiEndpoint = 'https://api.opencagedata.com/geocode/v1/json'
    let apiKey = '270c3c88dfc647109eec70b693c70cc3'


    navigator.geolocation.getCurrentPosition(getLocation, failedLocation)

    // Fetch the insurance data from the server
    fetch('/patient/insurance/' + plan)
        .then(response => response.json())
        .then(data => {
            // Populate the form fields with the insurance data
            document.getElementById('name').value = data.name;
            document.getElementById('dob').value = data.dob;
            document.getElementById('email').value = data.email;
            document.getElementById('gender').value = data.gender.toLowerCase()
            document.getElementById('pincode').value = data.pincode;
            document.getElementById('premium').value = data.premium;
            document.getElementById('sumInsured').value = data.sumInsured;
            document.getElementById('insuranceName').value = data.insuranceName;
            document.getElementById('policyEndDate').value = data.policyEndDate;
        })
        .catch(error => console.error('Error:', error));

    function getLocation(position) {
        getUserCurrentAddress(position.coords.latitude, position.coords.longitude).then(r => console.log(r))
    }

    function failedLocation() {
        console.log('failed')
    }

    const getUserCurrentAddress = async (latitude, longitude) => {
        console.log(latitude)
        console.log(longitude)

        let query = `${latitude},${longitude}`
        let apiUrl = `${apiEndpoint}?key=${apiKey}&q=${query}`

        try {
            const res = await fetch(apiUrl)
            const data = await res.json()
            document.getElementById('pincode').value = data.results[0].components.postcode
        } catch (error) {
            console.log(error)
        }

    }
};

document.getElementById('insuranceForm').addEventListener('submit', function(event) {
    event.preventDefault();

    var insurance = {
        name: document.getElementById('name').value,
        dob: document.getElementById('dob').value,
        email: document.getElementById('email').value,
        gender: document.getElementById('gender').value,
        pincode: document.getElementById('pincode').value,
        premium: document.getElementById('premium').value,
        sumInsured: document.getElementById('sumInsured').value,
        insuranceName: document.getElementById('insuranceName').value,
        policyEndDate: document.getElementById('policyEndDate').value
    };

    console.log(insurance)

    fetch('/patient/insurance', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(insurance),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('Insurance saved successfully with ID: ' + data.insuranceId);
        } else {
            alert('Error: ' + data.message);
        }
    })
    .catch((error) => {
        console.error('Error:', error);
    });
});