package com.findme.api.config;

import com.findme.api.model.Role;
import com.findme.api.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserInfoUserDetails implements UserDetails {
	
	private final String name;
	private final String password;
	private final List<GrantedAuthority> authorities = new ArrayList<>();
	
	public UserInfoUserDetails(User userInfo) {
		name=userInfo.getUsername();
		password=userInfo.getPassword();
		for (Role role : userInfo.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.toString()));
		}
		System.out.println("UserDetails: " + name + " " + password + " " + authorities);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getUsername() {
		return name;
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
