<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Appointment Confirmation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 90%;
            max-width: 600px;
            margin: 20px auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border: 1px solid #ddd; /* Added border */
            border-radius: 5px; /* Rounded corners */
        }

        h2 {
            color: #007bff;
            border-bottom: 2px solid #007bff; /* Add bottom border */
            padding-bottom: 10px; /* Add padding */
        }

        p {
            line-height: 1.6;
            margin: 10px 0; /* Add margin */
        }

        .footer {
            margin-top: 20px;
            font-size: 12px;
            color: #888;
            border-top: 1px solid #ddd; /* Add top border */
            padding-top: 10px; /* Add padding */
            text-align: center; /* Center align text */
        }

        a {
            color: #007bff; /* Link color */
            text-decoration: none; /* Remove underline */
        }

        a:hover {
            text-decoration: underline; /* Underline on hover */
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
    <p>Team ${websiteName}</p>
    <div class="footer">
        <p>If you have any questions, feel free to <a href=${email}>contact us</a>.</p>
        <p>&copy; ${.now?string("yyyy")} ${websiteName}. All rights reserved.</p>
    </div>
</div>
</body>
</html>
