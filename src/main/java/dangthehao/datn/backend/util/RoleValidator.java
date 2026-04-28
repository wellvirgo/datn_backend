package dangthehao.datn.backend.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class RoleValidator {
  RoleHierarchy roleHierarchy;

  public boolean canAssignRole(Authentication authentication, String targetRole) {
    String expectedRole = "ROLE_" + targetRole;

    Collection<? extends GrantedAuthority> reachableRoles =
        roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());

    return reachableRoles.stream().anyMatch(role -> expectedRole.equals(role.getAuthority()));
  }
}
