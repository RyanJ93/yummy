package dev.enricosola.yummy.support;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import dev.enricosola.yummy.entity.User;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class AuthenticatedUserDetails implements UserDetails {
    private List<GrantedAuthority> authorityList;
    private final String username;
    private final String password;

    private void generateAuthorities(List<String> roleList){
        this.authorityList = new ArrayList<>();
        for ( String role : roleList ){
            this.authorityList.add(new SimpleGrantedAuthority(role));
        }
    }

    public AuthenticatedUserDetails(String username, String password, List<String> roleList){
        this.password = new BCryptPasswordEncoder().encode(password);
        this.username = username;
        this.generateAuthorities(roleList);
    }

    public AuthenticatedUserDetails(User user, List<String> roleList){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.generateAuthorities(roleList);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.authorityList;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
