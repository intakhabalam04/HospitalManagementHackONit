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
                // Check if payment status is "Pending"
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
                    // Add the link to the cell
                    cell.appendChild(link);
                    // Add a title to the link for hover text
                    link.title = "Click here for payment";
                } else {
                    // If payment status is not "Pending", just add the status text
                    row.insertCell().textContent = appointment.paymentStatus;
                }

                if (appointment.appointmentStatus.toString() === 'PENDING') {
                    // Create a new cell
                    const cell = row.insertCell();
                    // Create a new link element
                    const link = document.createElement('a');
                    // Set the href attribute to the video call URL
                    link.href = `/patient/videocall?roomID=${appointment.roomID}&appointmentid=${appointment.id}`;
                    // Set the text content to 'Video Call'
                    link.textContent = 'Meet Now';
                    link.title = 'Click here to start a video call'
                    link.target = '_blank';
                    // Add the link to the cell
                    cell.appendChild(link);
                }else{
                    row.insertCell().textContent = appointment.appointmentStatus;
                }
            });
        });
});