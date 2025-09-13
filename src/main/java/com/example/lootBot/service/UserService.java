package com.example.lootBot.service;

import com.example.lootBot.model.User;
import com.example.lootBot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void registerUser(Long chatId) {
        repository.findById(chatId)
                .orElseGet(() -> repository.save(new User(chatId, 100)));
    }

    public User getOrCreateUser(Long chatId) {
        return repository.findById(chatId)
                .orElseGet(() -> repository.save(new User(chatId, 100)));
    }

    public void save(User user) {
        repository.save(user);
    }
}
