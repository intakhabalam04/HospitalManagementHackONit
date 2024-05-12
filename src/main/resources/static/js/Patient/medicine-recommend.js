window.onload = function () {
    const recommendationResult = document.getElementById('recommendationResult');
    recommendationResult.style.display = 'none';
};

document.getElementById('btn1').addEventListener('click', async function () {
    const medicineInput = document.getElementById('medicineInput').value;
    const recommendationResult = document.getElementById('recommendationResult');
    const spinner = document.getElementById('loadingSpinner');
    const reommendationButton = document.getElementById('btn1');
    reommendationButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Fetching...';
    reommendationButton.disabled = true;
    spinner.style.display = 'block';

    recommendationResult.style.display = 'none'


    await new Promise(resolve => setTimeout(resolve, 1000));


    fetch('/patient/recommend', {
        method: 'POST', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify({
            medicine: medicineInput
        }),
    })
        .then(response => response.json())
        .then(data => {
            recommendationResult.style.display = 'block'
            recommendationResult.innerHTML = data.response;

        })
        .catch((error) => {
            console.error('Error:', error);
        }).finally(() => {
        spinner.style.display = 'none';
        reommendationButton.innerHTML = 'Get Recommendation';
        reommendationButton.disabled = false;
    });
});


fetch('/static/json/drug_name.json')
    .then(response => response.json())
    .then(data => {
        let i =1;
        const dataList = document.getElementById('medicineList');
        for (let key in data) {
            let option = document.createElement('option');
            option.value = data[key];
            dataList.appendChild(option);
        }
    })
    .catch((error) => {
        console.error('Error:', error);
    });