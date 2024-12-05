package com.jkb.cmi.user.controller;

import com.jkb.cmi.user.dto.UserRequest;
import com.jkb.cmi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @PostMapping("/sign-up")
    ResponseEntity<Boolean> signUp(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.signUp(userRequest));
    }

    @PostMapping("/login")
    ResponseEntity<Boolean> login(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.login(userRequest));
    }
}
