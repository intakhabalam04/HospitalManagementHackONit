document.addEventListener("DOMContentLoaded", function () {
    // Fetch the data (replace with your actual data fetching code)
    fetch('/doctor/doctors-appointments')
        .then(response => response.json())
        .then(appointments => {
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            const pastAppointments = appointments.filter(appointment => {
                const appointmentDate = new Date(appointment.appointmentDate);
                appointmentDate.setHours(0, 0, 0, 0);
                return appointmentDate < today;
            });

            const todayAppointments = appointments.filter(appointment => {
                const appointmentDate = new Date(appointment.appointmentDate);
                appointmentDate.setHours(0, 0, 0, 0);
                return appointmentDate.getTime() === today.getTime();
            });

            const futureAppointments = appointments.filter(appointment => {
                const appointmentDate = new Date(appointment.appointmentDate);
                appointmentDate.setHours(0, 0, 0, 0);
                return appointmentDate > today;
            });


            const tables = {
                'past-appointments-table': pastAppointments,
                'today-appointments-table': todayAppointments,
                'future-appointments-table': futureAppointments
            };

            Object.keys(tables).forEach(tableId => {
                const table = document.querySelector(`#${tableId}`);
                const appointments = tables[tableId];

                appointments.forEach(appointment => {
                    const tr = document.createElement('tr');

                    // Create a table data cell for each required property
                    const properties = ['patientName', 'appointmentDate', 'symptoms', 'appointmentTime'];
                    properties.forEach(property => {
                        const td = document.createElement('td');
                        td.textContent = appointment[property];
                        tr.appendChild(td);
                    });

                    const reportCell = document.createElement('td');
                    console.log(appointment.prescriptionGiven)
                    try {
                        console.log("2")
                        const link = document.createElement('a');
                        link.href = '#'
                        link.textContent = 'View Reports';
                        link.addEventListener('click', function (event) {
                            event.preventDefault();
                            fetch(`/doctor/${appointment.id}/prescription`)
                                .then(response => response.blob())
                                .then(blob => {
                                    const url = URL.createObjectURL(blob);
                                    const a = document.createElement('a');
                                    a.href = url;
                                    a.download = `prescription-${appointment.id}.pdf`;
                                    document.body.appendChild(a);
                                    a.click();
                                    document.body.removeChild(a);
                                    URL.revokeObjectURL(url);
                                })
                                .catch(error => {
                                    console.error('Error fetching prescription:', error);
                                });
                        });
                        reportCell.appendChild(link);

                    } catch (e) {

                        reportCell.textContent = 'Not Available';
                    }

                    tr.appendChild(reportCell);


                    table.appendChild(tr);
                });
            });
        })
});