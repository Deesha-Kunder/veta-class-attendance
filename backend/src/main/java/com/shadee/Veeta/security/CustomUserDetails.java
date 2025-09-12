package com.shadee.Veeta.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadee.Veeta.modelclass.Users;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    @EqualsAndHashCode.Include
    private String id;
    private String username;
    private String email;
    private String password;

    private Collection< ? extends GrantedAuthority> authorities;

    public static CustomUserDetails build(Users users){
        GrantedAuthority authority = new SimpleGrantedAuthority(users.getRole().name());
        return new CustomUserDetails(
                users.getUserId(),
                users.getUsername(),
                users.getEmail(),
                users.getPassword(),
                List.of(authority)
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
