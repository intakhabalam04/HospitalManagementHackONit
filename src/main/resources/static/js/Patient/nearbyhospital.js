const button1 = document.getElementById('fetch-hospitals')

let apiEndpoint = 'https://api.opencagedata.com/geocode/v1/json'
let apiKey = '270c3c88dfc647109eec70b693c70cc3'

button1.addEventListener('click', () => {
    navigator.geolocation.getCurrentPosition(getLocation, failedLocation)
})

function getLocation(position) {
    getUserCurrentAddress(position.coords.latitude, position.coords.longitude)
}

function failedLocation() {
    console.log('failed')
}

const getUserCurrentAddress = async (latitude, longitude) => {

    let query = `${latitude},${longitude}`
    let apiUrl = `${apiEndpoint}?key=${apiKey}&q=${query}`

    try{
        const res = await fetch(apiUrl)
        const data = await res.json()
        const pincode = data.results[0].components.postcode
        showHospital(pincode)
    }catch(error){
        console.log(error)
    }

}

let hospitalsData = [];

window.onload = function() {
    fetch('/static/json/hositalsinindia.json')
        .then(response => response.json())
        .then(data => {
            hospitalsData = data;
        });
}

function showHospital(pincode){

    const hospital = hospitalsData.find(hospital => hospital.Pincode == pincode);
    const hospitalNameDiv = document.getElementById('hospitals-list');
    if (hospital) {
        hospitalNameDiv.textContent = `Nearest Hospital: ${hospital.Hospital}`;
    } else {
        hospitalNameDiv.textContent = 'No hospital found for this pincode';
    }
}