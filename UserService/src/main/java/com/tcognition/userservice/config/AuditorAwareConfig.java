package com.tcognition.userservice.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Retrieve the current username from the security context or any other source
    	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

          if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
              return Optional.of("SYSTEM");
          }

          return Optional.of(authentication.getName());
    }
}
