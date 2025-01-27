package site.javadev.security;

import site.javadev.Model.PersonSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

public class PersonDetails implements UserDetails {

    private final PersonSecurity personSecurity;

    public PersonDetails(PersonSecurity personSecurity) {
        this.personSecurity = personSecurity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //return Collections.EMPTY_LIST;
        String role = personSecurity.getRole();
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return personSecurity.getPassword();
    }

    @Override
    public String getUsername() {
        return personSecurity.getUsername();
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
