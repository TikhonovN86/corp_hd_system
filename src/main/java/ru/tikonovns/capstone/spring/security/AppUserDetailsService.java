package ru.tikonovns.capstone.spring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ru.tikonovns.capstone.spring.entity.User;
import ru.tikonovns.capstone.spring.repository.UserRepository;

/**
 * Промежуточный класс между Spring Security и БД.
 * Метод loadUserByUsername() реализован здесь, а не в UserService для разделения бизнес-логики с security.
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /*
    Метод, через который Spring Security получает пользователя в формате UserDetails (мой AppUserPrincipal).
    Берется username из формы логина ->
    находится пользователь в бд ->
    пользователь оборачивается в UserDetails -> Spring Security получает пользователя в нужном формате.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRoleAndGroups(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь с username '" + username + "' не найден"
                ));

        return AppUserPrincipal.fromUser(user);
    }
}