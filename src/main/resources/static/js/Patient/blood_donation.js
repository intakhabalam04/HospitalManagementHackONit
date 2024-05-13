function register() {
    const name = document.getElementById("name").value;
    const gender = document.querySelector('input[name="gender"]:checked') ? document.querySelector('input[name="gender"]:checked').value === '0' ? 'Male' : 'Female' : 'Gender not selected';
    const bloodGroup = document.getElementById('bloodGroup').options[document.getElementById('bloodGroup').selectedIndex].text;
    const weight = document.getElementById("weight").value;
    const diseases = document.querySelector('input[name="diseases"]:checked').value;
    const street = document.getElementById("street").value;
    const area = document.getElementById("area").value;
    const city = document.getElementById("city").options[document.getElementById("city").selectedIndex].text;
    const pincode = document.getElementById("pincode").value;
    const mobile = document.getElementById("mobile").value;
    const dateOfBirth = new Date(document.getElementById("datepicker1").value);

    const today = new Date();
    let age = today.getFullYear() - dateOfBirth.getFullYear();
    const m = today.getMonth() - dateOfBirth.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < dateOfBirth.getDate())) {
        age--;
    }

    // Check if age is less than 18
    if (age < 18) {
        alert("Age should be greater than 18");
        return;
    }

    if(weight < 50) {
        alert("Weight should be greater than 50");
        return;
    }

    if(diseases === 'Yes') {
        alert("You are not eligible for blood donation");
    }
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