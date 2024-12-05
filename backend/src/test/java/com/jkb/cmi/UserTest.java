package com.jkb.cmi;

import com.jkb.cmi.user.entity.User;
import com.jkb.cmi.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void test1() {
        try {
            User user = userRepository.findByUsernameAndPassword("test", "123")
                    .orElseThrow(() -> new IllegalArgumentException("NOT EXIST USER"));
            System.out.println(user.getId());
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void test2() {
        User user = userRepository.getByUsername("asd");
        System.out.println(user);
    }
}
