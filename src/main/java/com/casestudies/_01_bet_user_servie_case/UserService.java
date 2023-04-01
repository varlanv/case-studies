package com.casestudies._01_bet_user_servie_case;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private Map<Long, User> userCache = new HashMap<>();

    public User findById(long id) {
        if (userCache.containsKey(id)) {
            return userCache.get(id);
        }
        User user = userRepository.findById(id).orElse(null);
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

    public void delete(long id) {
        userCache.remove(id);
        userRepository.deleteById(id);
    }
}
