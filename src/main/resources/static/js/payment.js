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
const paymentStart = () => {

    const amount = document.getElementById('payment_field').value
    if(amount==='' || amount===null){
        Swal.fire({
            title: "Error!",
            text: "Please enter a valid amount.",
            icon: "error"
        })
        return;
    }else if(amount<1){
        Swal.fire({
            title: "Error!",
            text : "Please enter amount greater than equal to 1 .",
            icon: "error"
        })
        return;
    }
    createOrder(amount)
}

function createOrder(amount){
    $.ajax({
        url: '/user/create_order',
        data: JSON.stringify({
            amount: amount, info: 'order_request'
        }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            if (response.status === 'created') {
                initializePayment( response);
            }
        }, error: function (error) {
            console.log(error)
        }
    })
}
function initializePayment(response) {
    const options = {
        key: 'rzp_test_awxSLkLfejkRl3',
        amount: response.amount,
        currency: 'INR',
        name: 'Payment tutorial',
        description: 'Tutorial',
        // image: 'https://en.wikipedia.org/wiki/Image#/media/File:Image_created_with_a_mobile_phone.png',
        order_id: response.id,
        handler: function (response) {
            console.log(response)
            Swal.fire({
                title:'Success',
                text:'Thanks for donating',
                icon:'success'
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
    })
    rzp.open()
}
