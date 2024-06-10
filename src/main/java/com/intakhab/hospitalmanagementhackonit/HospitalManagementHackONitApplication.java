package com.intakhab.hospitalmanagementhackonit;

import com.intakhab.hospitalmanagementhackonit.Enum.UserRole;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@PropertySource("classpath:views.properties")
@RequiredArgsConstructor
public class HospitalManagementHackONitApplication {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementHackONitApplication.class, args);
    }

    @Bean
    public CommandLineRunner setupDefaultUser() {
        return args -> {
            List<User> adminList = userRepository.findByRole(UserRole.ADMIN);
            User admin = !adminList.isEmpty() ? adminList.get(0) : null;
            if (admin == null) {
                User newAdmin = new User();
                newAdmin.setRole(UserRole.ADMIN);
                newAdmin.setUsername("admin");
                newAdmin.setPassword(passwordEncoder.encode("admin"));
                newAdmin.setName("Admin");
                newAdmin.setEmail("admin@gmail.com");
                newAdmin.setMobile("1234567890");
                newAdmin.setRole(UserRole.ADMIN);

                userRepository.save(newAdmin);
            }
        };
    }
}
