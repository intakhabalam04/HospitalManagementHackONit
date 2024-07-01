package com.intakhab.hospitalmanagementhackonit.ControllerImpl;

import com.intakhab.hospitalmanagementhackonit.Controller.PaymentController;
import com.intakhab.hospitalmanagementhackonit.Service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/patient/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String , Object> data){
        System.out.println(data);
        double amt = Double.parseDouble(data.get("amount").toString());
        System.out.println(amt);


        try {
            RazorpayClient client = new RazorpayClient("rzp_test_awxSLkLfejkRl3","Ko5jficcwVxKMoNPSg45cFMu");
            JSONObject object = new JSONObject();
            object.put("amount",amt*100);
            object.put("currency","INR");
            object.put("receipt","txn_123456");
            object.put("payment_capture",1);

            Order order = client.Orders.create(object);
            System.out.println(order);

            return order.toString();

        } catch (RazorpayException e) {
            e.printStackTrace();
            return "Error";
        }

    }

    @PostMapping("/patient/verify_payment")
    @ResponseBody
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String , Object> data){
        System.out.println(data);
        String orderId = data.get("orderId").toString();
        String paymentId = data.get("paymentId").toString();
        String signature = data.get("signature").toString();
        UUID patientId = UUID.fromString(data.get("appointmentId").toString());

        try {
            RazorpayClient client = new RazorpayClient("rzp_test_awxSLkLfejkRl3","Ko5jficcwVxKMoNPSg45cFMu");

            String secret = "Ko5jficcwVxKMoNPSg45cFMu";
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            boolean status =  Utils.verifyPaymentSignature(options, secret);

            if(status){
                paymentService.updatePaymentStatus(patientId);
            }else {
                return ResponseEntity.badRequest().body("Payment Verification Failed");
            }

            return ResponseEntity.ok().body("Payment Verified");

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error");
        }
    }

}
