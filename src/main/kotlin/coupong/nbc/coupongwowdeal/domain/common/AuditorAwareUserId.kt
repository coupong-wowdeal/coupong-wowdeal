package coupong.nbc.coupongwowdeal.domain.common

import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Optional

class AuditorAwareUserId : AuditorAware<Long> {
    override fun getCurrentAuditor(): Optional<Long> {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated) {
            return Optional.empty()
        }
        val principal = authentication.principal as UserPrincipal
        return Optional.of(principal.id)
    }
}