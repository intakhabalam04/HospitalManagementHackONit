document.addEventListener('DOMContentLoaded', function() {
    const script1 = document.createElement('script');
    script1.src = 'https://cdn.jsdelivr.net/npm/sweetalert2@11';
    document.head.appendChild(script1);

    const script2 = document.createElement('script');
    script2.src = 'https://code.jquery.com/jquery-3.6.0.min.js';
    script2.onload = function() {
        const script3 = document.createElement('script');
        script3.src = 'https://checkout.razorpay.com/v1/checkout.js';
        document.head.appendChild(script3);
    };
    document.head.appendChild(script2);
});
const paymentStart = (amount) => {
    return new Promise((resolve, reject) => {
        // existing code...
        if(amount==='' || amount===null){
            Swal.fire({
                title: "Error!",
                text: "Please enter a valid amount.",
                icon: "error"
            })
            reject('Invalid amount');
            return;
        }else if(amount<1){
            Swal.fire({
                title: "Error!",
                text : "Please enter amount greater than equal to 1 .",
                icon: "error"
            })
            reject('Invalid amount');
            return;
        }
        createOrder(amount, resolve, reject);
    });
}

function createOrder(amount, resolve, reject){
    $.ajax({
        url: '/patient/create_order',
        data: JSON.stringify({
            amount: amount, info: 'order_request'
        }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            if (response.status === 'created') {
                initializePayment(response, resolve, reject);
            }
        }, error: function (error) {
            console.log(error);
            reject(error);
        }
    })
}

function initializePayment(response, resolve, reject) {
    const options = {
        key: 'rzp_test_awxSLkLfejkRl3',
        amount: response.amount,
        currency: 'INR',
        name: 'Appointment Fee',
        description: 'Jansevak Appointment Fee',
        // image: 'https://en.wikipedia.org/wiki/Image#/media/File:Image_created_with_a_mobile_phone.png',
        order_id: response.id,
        handler: function (response) {
            console.log(response)
            Swal.fire({
                title:'Success',
                text:'Your payment was successful',
                icon:'success'
            }).then(() => {
                resolve(response)
            })


        }, prefill: {
            name: 'Intakhab Alam',
            email: 'intakhabalam2004@gmail.com',
            contact: '6202933673'
        }, notes: {
            address: 'Kolkata'
        }, theme: {
            color: '#152'
        }
    }
    let rzp = Razorpay(options)

    rzp.on('payment.failed', function (response) {
        console.log(response.error.code)
        console.log(response.error.description)
        console.log(response.error.source)
        console.log(response.error.step)
        console.log(response.error.reason)
        console.log(response.error.metadata.order_id)
        console.log(response.error.metadata.payment_id)
        Swal.fire({
            title: response.error.reason,
            text:response.error.description,
            icon: 'error'
        })
        reject(response.error);
    })
    rzp.on('payment.success', function (response) {
        resolve(response);
    })
    rzp.open()
}
