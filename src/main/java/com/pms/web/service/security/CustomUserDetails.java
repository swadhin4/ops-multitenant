package com.pms.web.service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

  private static final long serialVersionUID = -7720854002321418429L;

  private String salt;

  public CustomUserDetails(final String username, final String password, final boolean enabled,
      final boolean accountNonExpired, final boolean credentialsNonExpired, final boolean accountNonLocked,
      final Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(final String salt) {
    this.salt = salt;
  }

}
