package ru.tikonovns.capstone.spring.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    /*
     Возвращает Authentication из SecurityContext.
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /*
     Проверка: есть ли вообще аутентифицированный пользователь.
     */
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    /*
     Возвращает текущий principal как AppUserPrincipal.
     Если пользователь не авторизован, выбрасывает исключение.
     */
    public AppUserPrincipal getPrincipal() {
        Authentication authentication = getAuthentication();

        if (!isAuthenticated()) {
            throw new IllegalStateException("Пользователь не авторизован");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof AppUserPrincipal appUserPrincipal)) {
            throw new IllegalStateException("Текущий principal не является AppUserPrincipal");
        }

        return appUserPrincipal;
    }

    /*
     ID текущего пользователя.
     */
    public Long getCurrentUserId() {
        return getPrincipal().getId();
    }

    /*
     Username текущего пользователя.
     */
    public String getCurrentUsername() {
        return getPrincipal().getUsername();
    }

    /*
     Код роли текущего пользователя.
     Например: ROLE_INITIATOR / ROLE_DISPATCHER / ROLE_EXECUTOR
     */
    public String getCurrentRoleCode() {
        return getPrincipal().getRoleCode();
    }

    /*
     Проверка роли текущего пользователя.
     */
    public boolean hasRole(String roleCode) {
        return getPrincipal().getRoleCode().equals(roleCode);
    }

    /*
     Проверка принадлежности к рабочей группе.
     */
    public boolean isMemberOfWorkGroup(String workGroupCode) {
        return getPrincipal().isInWorkGroup(workGroupCode);
    }
}