package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import com.intakhab.hospitalmanagementhackonit.Service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User findByMobile(String phoneNumber) {
        return userRepo.findByMobile(phoneNumber);
    }

    @Override
    public User findByEmail(String emailId) {
        return userRepo.findByEmail(emailId);
    }

    @Override
    public User findByUserName(String username) {
        return userRepo.findByUsername(username);
    }

}
