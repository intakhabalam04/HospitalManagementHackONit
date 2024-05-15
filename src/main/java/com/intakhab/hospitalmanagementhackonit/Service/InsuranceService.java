package com.intakhab.hospitalmanagementhackonit.Service;

import com.intakhab.hospitalmanagementhackonit.Model.Insurance;

public interface InsuranceService {
    Insurance getInsuranceByPlan(int plan);

    Insurance saveInsurance(Insurance insurance);
}
