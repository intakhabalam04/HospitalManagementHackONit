/*
document.addEventListener("DOMContentLoaded", function () {

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

                // Payment status handling
                if (appointment.paymentStatus.toString() === 'PENDING') {
                    const cell = row.insertCell();
                    const link = document.createElement('a');
                    link.href = '#';
                    link.textContent = 'PENDING';
                    link.addEventListener('click', async function (event) {
                        event.preventDefault();
                        try {
                            const res = await paymentStart(appointment.consultationFee);
                            const paymentUpdateResponse = await fetch('/patient/verify_payment', {
                                method: 'POST', headers: {
                                    'Content-Type': 'application/json'
                                }, body: JSON.stringify({
                                    appointmentId: appointment.id,
                                    paymentId: res.razorpay_payment_id,
                                    orderId: res.razorpay_order_id,
                                    signature: res.razorpay_signature
                                })
                            });
                            if (!paymentUpdateResponse.ok) {
                                throw new Error('Failed to update payment status');
                            }
                            location.reload();
                        } catch (error) {
                            console.error('Error during payment:', error);
                        }
                    });
                    cell.appendChild(link);
                    link.title = "Click here for payment";
                } else {
                    row.insertCell().textContent = appointment.paymentStatus;
                }

                // Appointment status handling
                if (appointment.appointmentStatus.toString() === 'PENDING') {
                    const cell = row.insertCell();
                    const link = document.createElement('a');
                    link.href = `/patient/videocall?roomID=${appointment.roomID}&appointmentid=${appointment.id}`;
                    link.textContent = 'Meet Now';
                    link.title = 'Click here to start a video call'
                    link.target = '_blank';
                    cell.appendChild(link);
                } else {
                    row.insertCell().textContent = appointment.appointmentStatus;
                }

                // Prescription handling
                const prescriptionCell = row.insertCell();
                if (appointment.prescriptionGiven) {
                    const link = document.createElement('a');
                    link.href = '#';
                    link.textContent = 'Download Prescription';
                    link.addEventListener('click', function (event) {
                        event.preventDefault();
                        fetch(`/patient/${appointment.id}/prescription`)
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
                    prescriptionCell.appendChild(link);
                } else {
                    prescriptionCell.textContent = 'Not Available';
                }
            });
        });
});
*/

document.addEventListener("DOMContentLoaded", function() {
    // Fetch the data (replace with your actual data fetching code)
    fetch('/patient/all-appointments')
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
                    const properties = ['doctorName', 'consultationFee', 'appointmentDate', 'appointmentTime'];
                    properties.forEach(property => {
                        const td = document.createElement('td');
                        td.textContent = appointment[property];
                        tr.appendChild(td);
                    });

                    // Payment status handling
                    const paymentCell = document.createElement('td');
                    if (appointment.paymentStatus.toString() === 'PENDING') {
                        const link = document.createElement('a');
                        link.href = '#';
                        link.textContent = 'PENDING';
                        link.addEventListener('click', async function(event) {
                            event.preventDefault();
                            try {
                                const res = await paymentStart(appointment.consultationFee);
                                const paymentUpdateResponse = await fetch('/patient/verify_payment', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    body: JSON.stringify({
                                        appointmentId: appointment.id,
                                        paymentId: res.razorpay_payment_id,
                                        orderId: res.razorpay_order_id,
                                        signature: res.razorpay_signature
                                    })
                                });
                                if (!paymentUpdateResponse.ok) {
                                    throw new Error('Failed to update payment status');
                                }
                                location.reload();
                            } catch (error) {
                                console.error('Error during payment:', error);
                            }
                        });
                        paymentCell.appendChild(link);
                        link.title = "Click here for payment";
                    } else {
                        paymentCell.textContent = appointment.paymentStatus;
                    }
                    tr.appendChild(paymentCell);

                    // Appointment status handling
                    const statusCell = document.createElement('td');
                    if (appointment.appointmentStatus.toString() === 'PENDING') {
                        const link = document.createElement('a');
                        link.href = `/patient/videocall?roomID=${appointment.roomID}&appointmentid=${appointment.id}`;
                        link.textContent = 'Meet Now';
                        link.title = 'Click here to start a video call';
                        link.target = '_blank';
                        statusCell.appendChild(link);
                    } else {
                        statusCell.textContent = appointment.appointmentStatus;
                    }
                    tr.appendChild(statusCell);

                    // Prescription handling
                    const prescriptionCell = document.createElement('td');
                    if (appointment.prescriptionGiven) {
                        const link = document.createElement('a');
                        link.href = '#';
                        link.textContent = 'Download Prescription';
                        link.addEventListener('click', function(event) {
                            event.preventDefault();
                            fetch(`/patient/${appointment.id}/prescription`)
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
                        prescriptionCell.appendChild(link);
                    } else {
                        prescriptionCell.textContent = 'Not Available';
                    }
                    tr.appendChild(prescriptionCell);

                    table.appendChild(tr);
                });
            });
        });
});

