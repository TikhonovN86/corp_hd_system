package ru.tikonovns.capstone.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tikonovns.capstone.spring.security.AppUserPrincipal;
import ru.tikonovns.capstone.spring.security.CurrentUserService;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final CurrentUserService currentUserService;

    @GetMapping("/me")
    public Map<String, Object> me() {
        AppUserPrincipal principal = currentUserService.getPrincipal();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", principal.getId());
        response.put("username", principal.getUsername());
        response.put("fullName", principal.getFullName());
        response.put("email", principal.getEmail());
        response.put("roleCode", principal.getRoleCode());
        response.put("workGroupCodes", principal.getWorkGroupCodes());
        response.put("authorities", principal.getAuthorities());

        return response;
    }

    @GetMapping("/initiator/hello")
    public Map<String, Object> initiatorHello() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Доступ разрешен для инициатора");
        response.put("currentUserId", currentUserService.getCurrentUserId());
        response.put("currentUsername", currentUserService.getCurrentUsername());
        response.put("currentRole", currentUserService.getCurrentRoleCode());
        return response;
    }

    @GetMapping("/dispatcher/hello")
    public Map<String, Object> dispatcherHello() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Доступ разрешен для диспетчера");
        response.put("currentUserId", currentUserService.getCurrentUserId());
        response.put("currentUsername", currentUserService.getCurrentUsername());
        response.put("currentRole", currentUserService.getCurrentRoleCode());
        response.put(
                "isDispatcherGroupMember",
                currentUserService.isMemberOfWorkGroup("help.desk.dispatcher")
        );
        return response;
    }

    @GetMapping("/executor/hello")
    public Map<String, Object> executorHello() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Доступ разрешен для исполнителя");
        response.put("currentUserId", currentUserService.getCurrentUserId());
        response.put("currentUsername", currentUserService.getCurrentUsername());
        response.put("currentRole", currentUserService.getCurrentRoleCode());
        return response;
    }

    @GetMapping("/authenticated/hello")
    public Map<String, Object> authenticatedHello() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Доступ разрешен любому авторизованному пользователю");
        response.put("currentUserId", currentUserService.getCurrentUserId());
        response.put("currentUsername", currentUserService.getCurrentUsername());
        response.put("currentRole", currentUserService.getCurrentRoleCode());
        return response;
    }
}