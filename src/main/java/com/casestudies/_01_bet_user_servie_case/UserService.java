package com.casestudies._01_bet_user_servie_case;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private Map<Long, User> userCache = new HashMap<>();

    public User findById(long id) {
        User user = Optional.ofNullable(userCache.get(id))
                .orElseGet(() -> userRepository.findById(id).orElse(null));
        if (user != null) {
            userCache.put(id, user);
        }
        return user;
    }

    @Transactional
    public User save(User user) {
        User savedUser = userRepository.save(user);
        userCache.put(savedUser.getId(), savedUser);
        return savedUser;
    }

    @Transactional
    public void update(long id, User updatedUser) {
        User existingUser = findById(id);
        BigDecimal updatedBalance = existingUser.getBalance().add(updatedUser.getBalance());
        updatedUser.setBalance(updatedBalance);
        userRepository.save(updatedUser);
        userCache.put(id, updatedUser);
    }
}
