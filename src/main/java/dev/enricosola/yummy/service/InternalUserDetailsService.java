package dev.enricosola.yummy.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import dev.enricosola.yummy.support.AuthenticatedUserDetails;
import org.springframework.stereotype.Service;
import dev.enricosola.yummy.DTO.AppConfig;
import dev.enricosola.yummy.entity.User;
import java.util.List;

@Service
@Transactional
public class InternalUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Autowired
    private AppConfig appConfig;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String adminUsername = this.appConfig.getAdminUsername(), adminPassword = this.appConfig.getAdminPassword();
        if ( adminUsername != null && adminUsername.equals(username) ){
            return new AuthenticatedUserDetails(adminUsername, adminPassword, List.of("ROLE_ADMIN"));
        }
        User user = this.userService.getByUsername(username);
        if ( user == null ){
            throw new UsernameNotFoundException("No user matching the given username found.");
        }
        return new AuthenticatedUserDetails(user, List.of("ROLE_USER"));
    }
}
