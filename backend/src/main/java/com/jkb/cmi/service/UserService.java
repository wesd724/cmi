package com.jkb.cmi.service;

import com.jkb.cmi.dto.UserDto;
import com.jkb.cmi.entity.CashAsset;
import com.jkb.cmi.entity.User;
import com.jkb.cmi.repository.CashAssetRepository;
import com.jkb.cmi.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CashAssetRepository cashAssetRepository;

    public void signUp(UserDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
        userRepository.save(user);

        CashAsset cashAsset = CashAsset.builder()
                .balance(100000000L).user(user)
                .build();
        cashAssetRepository.save(cashAsset);
    }

    public Boolean login(UserDto userDto) {
        try {
            userRepository.findByUsernameAndPassword(userDto.getUsername(), userDto.getPassword())
                    .orElseThrow(IllegalArgumentException::new);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }

    }
}
