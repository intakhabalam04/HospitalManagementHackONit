function register() {
    const name = document.getElementById("name").value;
    const gender = document.querySelector('input[name="gender"]:checked') ? document.querySelector('input[name="gender"]:checked').value === '0' ? 'Male' : 'Female' : 'Gender not selected';
    const dateOfBirth = document.getElementById("datepicker1").value;
    const bloodGroup = document.getElementById('bloodGroup').options[document.getElementById('bloodGroup').selectedIndex].text;
    const weight = document.getElementById("weight").value;
    const diseases = document.querySelector('input[name="diseases"]:checked').value;
    const street = document.getElementById("street").value;
    const area = document.getElementById("area").value;
    const city = document.getElementById("city").options[document.getElementById("city").selectedIndex].text;
    const pincode = document.getElementById("pincode").value;
    const mobile = document.getElementById("mobile").value;


    const data = {
        name: name,
        gender: gender.toUpperCase(),
        dateOfBirth: dateOfBirth,
        bloodGroup: bloodGroup,
        weight: weight,
        diseases: diseases === 'Yes',
        street: street,
        area: area,
        city: city,
        pincode: pincode,
        mobile: mobile
    }

    console.log(data)

    fetch('/patient/blood-donation', {
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

}