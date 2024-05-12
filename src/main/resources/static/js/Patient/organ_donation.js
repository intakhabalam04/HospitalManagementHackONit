document.addEventListener('DOMContentLoaded', function () {
    var canvas = document.getElementById('signature');
    var signaturePad = new SignaturePad(canvas);

    var clearButton = document.getElementById('clearSignature');
    clearButton.addEventListener('click', function () {
        signaturePad.clear();
    });

    var form = document.getElementById('donationForm');
    form.addEventListener('submit', function () {
        var signatureData = signaturePad.toDataURL(); // Convert signature to data URL
        console.log(signatureData)
        document.getElementById('signatureData').value = signatureData;
    });
});