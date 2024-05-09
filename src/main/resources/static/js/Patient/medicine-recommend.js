document.getElementById('btn1').addEventListener('click', function() {
    const medicineInput = document.getElementById('medicineInput').value;
    const recommendationResult = document.getElementById('recommendationResult');

    recommendationResult.style.display = 'none'

    fetch('/patient/recommend', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ medicine: medicineInput }),
    })
    .then(response => response.json())
    .then(data => {
        recommendationResult.style.display='block'
        recommendationResult.innerHTML = data.response;

        
    .catch((error) => {
        console.error('Error:', error);
    });
});