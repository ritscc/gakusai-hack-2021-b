package dev.abelab.gifttree.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.*;
import dev.abelab.gifttree.annotation.Authenticated;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.service.AuthService;

/**
 * Rest controller auth advice
 */
@RequiredArgsConstructor
@RestControllerAdvice(annotations = Authenticated.class)
public class RestControllerAuthAdvice {

    private final AuthService authService;

    @ModelAttribute("LoginUser")
    public User getLoginUser(@RequestHeader(name = HttpHeaders.AUTHORIZATION, required = true) final String credentials) {
        return this.authService.getLoginUser(credentials);
    }

}
