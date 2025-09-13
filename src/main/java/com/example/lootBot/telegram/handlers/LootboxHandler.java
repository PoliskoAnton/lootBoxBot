package com.example.lootBot.telegram.handlers;

import com.example.lootBot.model.Reward;
import com.example.lootBot.service.LootboxService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class LootboxHandler implements UpdateHandler {

    private final LootboxService lootboxService;

    public LootboxHandler(LootboxService lootboxService) {
        this.lootboxService = lootboxService;
    }

    @Override
    public void handle(Update update, TelegramLongPollingBot bot) {
        if (update.hasMessage() && update.getMessage().getText().equals("/lootbox")) {
            Long chatId = update.getMessage().getChatId();

            Optional<Reward> rewardOpt = lootboxService.openLootbox(chatId, 1L); // например лутбокс с id=1

            try {
                if (rewardOpt.isEmpty()) {
                    bot.execute(new SendMessage(chatId.toString(), "Ничего не выпало 😢 или мало монет."));
                } else {
                    Reward reward = rewardOpt.get();

                    if ("IMAGE".equals(reward.getType())) {
                        SendPhoto photo = new SendPhoto(chatId.toString(), new InputFile(reward.getContent()));
                        bot.execute(photo);
                    } else {
                        bot.execute(new SendMessage(chatId.toString(), "Ты получил: " + reward.getContent()));
                    }
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
