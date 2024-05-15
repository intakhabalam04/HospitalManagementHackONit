package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Model.Email;
import com.intakhab.hospitalmanagementhackonit.Model.Insurance;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.InsuranceRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import com.intakhab.hospitalmanagementhackonit.Service.EmailService;
import com.intakhab.hospitalmanagementhackonit.Service.InsuranceService;
import com.intakhab.hospitalmanagementhackonit.Service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {

    private final SecurityService securityService;
    private final InsuranceRepo insuranceRepo;
    private final UserRepo userRepo;
    private final EmailService emailService;

    @Override
    public Insurance getInsuranceByPlan(int plan) {
        User user = securityService.currentUser();
        Insurance insurance = new Insurance();
        insurance.setName(user.getName());
        insurance.setEmail(user.getEmail());
        insurance.setGender(user.getGender());
        insurance.setPolicyEndDate(LocalDate.now().plusYears(5));
        insurance.setSumInsured(1000000);
        insurance.setPremium(5000);

        Map<Integer, String> insurancePlans = Map.of(1, "Individual Health Insurance", 2, "Family Floater Health Insurance", 3, "Senior Citizens Health Insurance", 4, "Critical Illness Health Insurance", 5, "Group Health Insurance Plan", 6, "Maternity Health Insurance");

        insurance.setInsuranceName(insurancePlans.get(plan));

        return insurance;

    }

    @Override
    public Insurance saveInsurance(Insurance insurance) {
        Insurance insurance1 = new Insurance();
        insurance1.setName(insurance.getName());
        insurance1.setEmail(insurance.getEmail());
        insurance1.setGender(insurance.getGender());
        insurance1.setPolicyEndDate(insurance.getPolicyEndDate());
        insurance1.setSumInsured(insurance.getSumInsured());
        insurance1.setPremium(insurance.getPremium());
        insurance1.setInsuranceName(insurance.getInsuranceName());
        insurance1.setDob(insurance.getDob());
        insurance1.setPincode(insurance.getPincode());

        User user = securityService.currentUser();
        List<Insurance> insuranceList = user.getInsuranceList();
        System.out.println(insuranceList);
        if (insuranceList.isEmpty()) {
            List<Insurance> insuranceList1 = new ArrayList<>();
            user.setInsuranceList(insuranceList1);
            userRepo.save(user);
        } else {
            insuranceList.add(insurance1);
            user.setInsuranceList(insuranceList);
            userRepo.save(user);
        }
        insurance1.setUser(user);

        Email email = new Email();
        email.setSubject("Insurance Policy");
        email.setReceiver(insurance1.getEmail());
        email.setMessage("Your insurance policy has been successfully applied. Your policy details are as follows: \n" +
                "Name: " + insurance1.getName() + "\n" +
                "Email: " + insurance1.getEmail() + "\n" +
                "Insurance Plan: " + insurance1.getInsuranceName() + "\n" +
                "Sum Insured: " + insurance1.getSumInsured() + "\n" +
                "Premium: " + insurance1.getPremium() + "\n" +
                "Policy End Date: " + insurance1.getPolicyEndDate() + "\n" +
                "Thank you for choosing us.");
        emailService.sendEmail(email);


        return insuranceRepo.save(insurance1);
    }

}
