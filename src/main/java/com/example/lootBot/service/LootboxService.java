package com.example.lootBot.service;

import com.example.lootBot.model.Lootbox;
import com.example.lootBot.model.Reward;
import com.example.lootBot.model.User;
import com.example.lootBot.repository.LootboxRepository;
import com.example.lootBot.repository.RewardRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LootboxService {

    private final UserService userService;
    private final LootboxRepository lootboxRepository;
    private final RewardRepository rewardRepository;

    public LootboxService(UserService userService,
                          LootboxRepository lootboxRepository,
                          RewardRepository rewardRepository) {
        this.userService = userService;
        this.lootboxRepository = lootboxRepository;
        this.rewardRepository = rewardRepository;
    }

    /**
     * Попытка открыть лутбокс
     */
    public Optional<Reward> openLootbox(Long chatId, Long lootboxId) {
        User user = userService.getOrCreateUser(chatId);

        Optional<Lootbox> lootboxOpt = lootboxRepository.findById(lootboxId);
        if (lootboxOpt.isEmpty()) {
            return Optional.empty();
        }

        Lootbox lootbox = lootboxOpt.get();

        // Проверяем баланс
        if (user.getBalance() < lootbox.getPrice()) {
            return Optional.empty();
        }

        // Списываем стоимость
        user.setBalance(user.getBalance() - lootbox.getPrice());
        userService.save(user);

        // Достаём награды
        List<Reward> rewards = rewardRepository.findByLootboxId(lootbox.getId());
        if (rewards.isEmpty()) {
            return Optional.empty();
        }

        // Случайный выбор награды с учётом dropChance
        Random random = new Random();
        if (random.nextDouble() > lootbox.getDropChance()) {
            return Optional.empty(); // проигрыш
        }

        Reward reward = rewards.get(random.nextInt(rewards.size()));
        return Optional.of(reward);
    }
}
