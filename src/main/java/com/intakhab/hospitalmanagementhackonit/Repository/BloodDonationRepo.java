package com.intakhab.hospitalmanagementhackonit.Repository;

import com.intakhab.hospitalmanagementhackonit.Model.BloodDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BloodDonationRepo extends JpaRepository<BloodDonation, UUID> {
}
