<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prescription Uploaded</title>
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
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        h2 {
            color: #28a745;
            border-bottom: 2px solid #28a745;
            padding-bottom: 10px;
        }
        p {
            line-height: 1.6;
            margin: 10px 0;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            color: #fff;
            background-color: #28a745;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
        }
        .button:hover {
            background-color: #218838;
        }
        .footer {
            margin-top: 20px;
            font-size: 12px;
            color: #888;
            border-top: 1px solid #ddd;
            padding-top: 10px;
            text-align: center;
        }
        a {
            color: #28a745;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Prescription Uploaded</h2>
    <p>Dear ${patientName},</p>
    <p>Your doctor has uploaded a new prescription for your recent appointment. Please find the details below:</p>
    <p><strong>Doctor:</strong> Dr. ${doctorName}</p>
    <p><strong>Appointment Date:</strong> ${appointmentDate}</p>
    <p><strong>Prescription:</strong> See the attached PDF for your prescription details.</p>
    <p>Thank you for choosing our services. Please contact us if you have any questions or concerns regarding your prescription.</p>
    <p>Best Regards,</p>
    <p>Team ${websiteName}</p>
    <div class="footer">
        <p>If you have any questions, feel free to <a href=${email}>contact us</a>.</p>
        <p>&copy; ${.now?string("yyyy")} ${websiteName}. All rights reserved.</p>
    </div>
</div>
</body>
</html>
