package com.jkb.cmi.user.service;

import com.jkb.cmi.user.dto.UserRequest;
import com.jkb.cmi.asset.entity.CashAsset;
import com.jkb.cmi.user.entity.User;
import com.jkb.cmi.event.UserSignUpEvent;
import com.jkb.cmi.asset.repository.CashAssetRepository;
import com.jkb.cmi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CashAssetRepository cashAssetRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Boolean signUp(UserRequest userRequest) {
        if(userRepository.existsByUsername(userRequest.getUsername()))
            return false;

        User user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();
        userRepository.save(user);

        CashAsset cashAsset = CashAsset.builder()
                .balance(200000000L).user(user)
                .build();
        cashAssetRepository.save(cashAsset);

        eventPublisher.publishEvent(new UserSignUpEvent(user.getId()));
        return true;
    }

    public Boolean login(UserRequest userRequest) {
        try {
            userRepository.findByUsernameAndPassword(userRequest.getUsername(), userRequest.getPassword())
                    .orElseThrow(IllegalArgumentException::new);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }
}
