document.addEventListener("DOMContentLoaded", function () {
    const appointmentForm = document.getElementById('appointmentForm');
    const doctorSelect = document.getElementById('doctor');
    const consultancyFeeInput = document.getElementById('consultancyFee');

    const allOption = document.createElement('option');
    allOption.value = '';
    allOption.textContent = 'ALL';
    doctorSelect.appendChild(allOption);

    consultancyFeeInput.value = 'Select Doctor';

    fetch('/patient/all')
        .then(response => response.json())
        .then(data => {
            data.forEach(doctor => {
                const option = document.createElement('option');
                option.value = doctor.id;
                option.textContent = `${doctor.name} (${doctor.specialization})`;
                doctorSelect.appendChild(option);

                option.dataset.consultancyFee = doctor.consultancyFee;
            });
        })
        .catch(error => {
            console.error('Error fetching doctors:', error);
        });

    doctorSelect.addEventListener('change', function () {
        const selectedOption = doctorSelect.options[doctorSelect.selectedIndex];
        const fee = selectedOption.dataset.consultancyFee;
        consultancyFeeInput.value = fee || 'Select Doctor';
    });

    appointmentForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const selectedDoctorId = doctorSelect.value;
        const selectedDate = document.getElementById('date').value;
        const selectedTime = document.getElementById('time').value;
        const patientName = document.getElementById('patientName').value;
        const age = document.getElementById('age').value;
        const symptoms = document.getElementById('symptoms').value;
        const gender = document.querySelector('input').value;
        const appointmentFee = consultancyFeeInput.value;

        const appointmentDetails = {
            doctorid: selectedDoctorId,
            appointmentDate: selectedDate,
            appointmentTime: selectedTime,
            patientName: patientName,
            age: age,
            symptoms: symptoms,
            gender: gender
        }


        fetch('/patient/book-appointment', {
            method: 'POST', headers: {
                'Content-Type': 'application/json'
            }, body: JSON.stringify(appointmentDetails)
        })
            .then(response => response.json())
            .then(async data => {
                if (data.success) {
                    console.log(data.appointmentId)
                    try {
                        const res = await paymentStart(appointmentFee);

                        const paymentUpdateResponse = await fetch('/patient/verify_payment', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({
                                appointmentId: data.appointmentId,
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
                } else {
                    alert('Failed to book appointment');
                }
            })
            .catch(error => {
                console.error('Error booking appointment:', error);
            });
    });
});
