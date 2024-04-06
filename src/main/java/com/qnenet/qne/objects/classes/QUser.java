package com.qnenet.qne.objects.classes;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class QUser implements UserDetails {
    private String userName;
    private char[] pwd;
    private ArrayList<GrantedAuthority> grantedAuthories;
    private boolean accountIsCurrent;
    private boolean accountIsNotLocked;
    private boolean credentialIsCurrent;
    private boolean accountIsEnabled;

    public QUser() {
    }

    public void addRoles(String... roles) {
        grantedAuthories = new ArrayList<>();
        for (String role : roles) {
            QGrantedAuthority auth = new QGrantedAuthority(role);
            grantedAuthories.add(auth);
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthories;
    }

    @Override
    public String getPassword() {
        return new String(pwd);
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountIsCurrent;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountIsNotLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialIsCurrent;
    }

    @Override
    public boolean isEnabled() {
        return accountIsEnabled;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPwd(char[] pwd) {
        this.pwd = pwd;
    }

    public void setGrantedAuthories(ArrayList<GrantedAuthority> grantedAuthories) {
        this.grantedAuthories = grantedAuthories;
    }

    public void setAccountIsCurrent(boolean accountIsCurrent) {
        this.accountIsCurrent = accountIsCurrent;
    }

    public void setAccountIsNotLocked(boolean accountIsNotLocked) {
        this.accountIsNotLocked = accountIsNotLocked;
    }

    public void setCredentialIsCurrent(boolean credentialIsCurrent) {
        this.credentialIsCurrent = credentialIsCurrent;
    }

    public void setAccountIsEnabled(boolean accountIsEnabled) {
        this.accountIsEnabled = accountIsEnabled;
    }
}
