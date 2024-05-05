
document.addEventListener("DOMContentLoaded", function() {
    const appointmentsData = [
        { patientName: "John Doe", doctorName: "Dr. Smith", date: "2024-05-10", time: "09:00 AM" },
        { patientName: "Jane Smith", doctorName: "Dr. Johnson", date: "2024-05-12", time: "02:00 PM" },
    ];

    const appointmentList = document.getElementById("appointmentList");

    appointmentsData.forEach(appointment => {
        const appointmentItem = document.createElement("div");
        appointmentItem.classList.add("appointment-item");
        appointmentItem.innerHTML = `
      <h3>${appointment.patientName}</h3>
      <p><strong>Doctor:</strong> ${appointment.doctorName}</p>
      <p><strong>Date:</strong> ${appointment.date}</p>
      <p><strong>Time:</strong> ${appointment.time}</p>
    `;
        appointmentList.appendChild(appointmentItem);
    });
});
