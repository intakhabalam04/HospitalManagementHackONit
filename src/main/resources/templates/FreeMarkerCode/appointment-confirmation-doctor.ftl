<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
    <p>Dear ${doctorName},</p>
    <p>You have a new appointment scheduled.</p>
    <p><strong>Patient Name:</strong> ${patientName}</p>
    <p><strong>Date:</strong> ${appointmentDate}</p>
    <p><strong>Time:</strong> ${appointmentTime}</p>
    <p><strong>Consultation Fee:</strong> ${consultationFee}</p>
    <p>Please be prepared for the appointment.</p>
    <p>You can join the video call using the following link:</p>
    <p><a href="http://localhost:8080/doctor/videocall?roomID=4569">Join Video Call</a></p>
    <p>Best Regards,</p>
    <p>Your Hospital Management Team</p>
    <div class="footer">
        <p>If you have any questions, feel free to <a href="mailto:support@hospital.com">contact us</a>.</p>
        <p>&copy; ${.now?string("yyyy")} Team Jansevak. All rights reserved.</p>
    </div>
</div>
</body>
</html>
