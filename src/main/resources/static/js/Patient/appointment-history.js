document.addEventListener("DOMContentLoaded", function() {

    fetch('/patient/all-appointments')
        .then(response => response.json())
        .then(appointments => {
            const appointmentList = document.getElementById('appointmentList');

            appointments.forEach(appointment => {
                const row = appointmentList.insertRow();
                row.insertCell().textContent = appointment.patientName;
                row.insertCell().textContent = appointment.doctorName;
                row.insertCell().textContent = appointment.consultationFee;
                row.insertCell().textContent = appointment.appointmentDate;
                row.insertCell().textContent = appointment.appointmentTime;
            });
        });
});
