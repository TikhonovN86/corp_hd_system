package ru.tikonovns.capstone.spring.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.tikonovns.capstone.spring.entity.User;
import ru.tikonovns.capstone.spring.entity.UserWorkGroup;
import ru.tikonovns.capstone.spring.entity.WorkGroup;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Адаптер между entity User и Spring Security.
 * Spring Security не работает с User, он работает только с объектом типа UserDetails.
 * User -> AppUserPrincipal (преобразование в security-модель) -> Spring Security.
 * После логина Spring кладёт UserDetails (мой AppUserPrincipal) в сессию.
 */
@Getter
@AllArgsConstructor
public class AppUserPrincipal implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String roleCode;
    private final Set<String> workGroupCodes;
    /*
    "Права/роли" пользователя, которые понимает Spring Security.
    Это обертка над кодом роли, которую схавает Spring Security.
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /*
    Фабричный метод для преобразования User -> AppUserPrincipal
     */
    public static AppUserPrincipal fromUser(User user) {
        //Берем код роли пользователя.
        String roleCode = user.getRole().getCode();

        //Берем коллекцию РГ пользователя.
        Set<String> workGroupCodes = user.getUserWorkGroups()
                .stream()
                .map(UserWorkGroup::getWorkGroup)
                .filter(Objects::nonNull)
                .map(WorkGroup::getCode)
                .collect(Collectors.toSet());

        //Код роли пользователя заворачиваем в понятную Spring Security обертку GrantedAuthority.
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(roleCode)
        );

        //Собираем объект.
        return new AppUserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                roleCode,
                workGroupCodes,
                authorities
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    //Формируем имя для UI и логов.
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (lastName != null) {
            sb.append(lastName);
        }
        if (firstName != null) {
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append(firstName);
        }
        if (middleName != null && !middleName.isBlank()) {
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append(middleName);
        }
        return sb.toString();
    }

    //Проверка принадлежности пользователя к группе по коду.
    public boolean isInWorkGroup(String workGroupCode) {
        return workGroupCodes.contains(workGroupCode);
    }

    //Возвоащает список ролей пользователя.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
