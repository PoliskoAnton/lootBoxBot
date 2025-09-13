package com.example.lootBot.repository;

import com.example.lootBot.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    List<Reward> findByLootboxId(Long lootboxId);
}

