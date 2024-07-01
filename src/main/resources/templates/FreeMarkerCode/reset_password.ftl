<!DOCTYPE html>
<html>
<head>
    <title>Reset Password</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
            color: #333;
        }
        .container {
            width: 90%;
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #ff5a5f;
        }
        p {
            line-height: 1.6;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            color: #fff;
            background-color: #ff5a5f;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
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
    <h2>Hi ${userName},</h2>
    <p>We’ve received a request to reset your password.</p>
    <p>If you didn’t make the request, just ignore this message. Otherwise, you can reset your password.</p>
    <a href="${resetLink}" class="button">Reset your password</a>
    <p>Thanks,</p>
    <p>Team Jansevak</p>
    <div class="footer">
        <p>If you have any questions, feel free to <a href="mailto:support@jansevak.com">contact us</a>.</p>
        <p>&copy; ${currentYear} Jansevak. All rights reserved.</p>
    </div>
</div>
</body>
</html>
