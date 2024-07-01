fetch('/doctor/prescription-needed')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        const tableRows = generateTableRows(data);

        const table = document.querySelector('table');
        table.innerHTML += tableRows;
    })
    .catch(error => {
        console.error('There has been a problem with your fetch operation:', error);
    });

// Get the modal
var modal = document.getElementById("prescriptionModal");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function () {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// Global variable to store the current appointment id
var currentAppointmentId;

// Function to generate the table rows
function generateTableRows(data) {
    let rows = '';


    data.forEach(item => {
        rows += `
            <tr>
                <td>${item.patientName}</td>
                <td>${item.age}</td>
                <td>${item.symptoms}</td>
                <td>${item.gender}</td>
                <td><button onclick="openPrescriptionModal('${item.id}')">Prescription</button></td>
            </tr>
        `;
    });

    return rows;
}

// Function to open the modal
function openPrescriptionModal(appointmentId) {
    currentAppointmentId = appointmentId;
    modal.style.display = "block";
}

document.getElementById("savePrescription").onclick = function () {
    var drugsNameText = document.getElementById("prescriptionText").value;

    fetch('/doctor/save-prescription', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: currentAppointmentId,
            drugsName: drugsNameText,
        }),
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);

            // Prompt the user for the number of days
            let daysInput = prompt("Enter the number of days for the prescription");

            // Validate the input to ensure it's a number
            let days = parseInt(daysInput);

            if (isNaN(days)) {
                alert("Invalid input. Please enter a valid number.");
                return;
            }

            // Update the appointment after the input days
            fetch('/doctor/update-appointment', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    id: currentAppointmentId,
                    days: days,
                }),
            })
                .then(response => response.json())
                .then(data => {
                    console.log('Appointment updated:', data);
                })
                .catch((error) => {
                    console.error('Error:', error);
                });

            console.log(days);
        })
        .catch((error) => {
            console.error('Error:', error);
        });

    // Close the modal
    modal.style.display = "none";
}
