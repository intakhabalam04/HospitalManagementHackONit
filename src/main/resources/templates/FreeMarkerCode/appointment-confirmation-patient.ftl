<!DOCTYPE html>
<html>
<head>
    <title>Appointment Confirmation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
        }
        .container {
            width: 100%;
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #007bff;
        }
        p {
            line-height: 1.6;
        }
        .footer {
            margin-top: 20px;
            font-size: 12px;
            color: #888;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Appointment Confirmation</h2>
    <p>Dear ${patientName},</p>
    <p>Your appointment has been successfully booked. Here are the details:</p>
    <p><strong>Doctor:</strong> Dr. ${doctorName}</p>
    <p><strong>Date:</strong> ${appointmentDate}</p>
    <p><strong>Time:</strong> ${appointmentTime}</p>
    <p><strong>Consultation Fee:</strong> ${consultationFee}</p>
    <p>Please arrive 10 minutes before your scheduled time. If you need to cancel or reschedule, please contact our office as soon as possible.</p>
    <p>Thank you for choosing our services.</p>
    <p>Best Regards,</p>
    <p>Team Jansevak</p>
    <div class="footer">
        <p>If you have any questions, feel free to <a href="mailto:support@hospital.com">contact us</a>.</p>
        <p>&copy; ${currentYear} Team Jansevak. All rights reserved.</p>
    </div>
</div>
</body>
</html>
