package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Enum.UserAction;
import com.intakhab.hospitalmanagementhackonit.Enum.UserRole;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String emailPhoneUsername) throws UsernameNotFoundException {
        System.out.println(emailPhoneUsername);
        User user = userRepo.findByEmailOrMobileOrUsername(emailPhoneUsername, emailPhoneUsername, emailPhoneUsername);
        if (user == null) {
            throw new UsernameNotFoundException("Username or password not found");
        }
        if (user.getAction().equals(UserAction.PENDING)) {
            throw new DisabledException("User is not approved");
        }

        if (user.getAction().equals(UserAction.REJECTED)) {
            throw new DisabledException("User is rejected");
        }


        Collection<? extends GrantedAuthority> authorities = authorities(user);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public Collection<? extends GrantedAuthority> authorities(User user) {
        String rolesString = user.getRole().toString();
        String[] rolesArray = rolesString.split(",");

        return Arrays.stream(rolesArray).map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim())).collect(Collectors.toList());
    }
}
