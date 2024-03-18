package com.jkb.cmi.controller;

import com.jkb.cmi.dto.UserDto;
import com.jkb.cmi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @PostMapping("/signup")
    ResponseEntity<Void> signUp(@RequestBody UserDto userDto) {
        userService.signUp(userDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.login(userDto));
    }


}
