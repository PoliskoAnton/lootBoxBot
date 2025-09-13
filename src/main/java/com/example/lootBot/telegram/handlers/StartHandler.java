package com.example.lootBot.telegram.handlers;

import com.example.lootBot.service.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class StartHandler implements UpdateHandler {

    private final UserService userService;

    public StartHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(Update update, TelegramLongPollingBot bot) {
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            Long chatId = update.getMessage().getChatId();
            userService.registerUser(chatId);

            SendMessage msg = new SendMessage(chatId.toString(),
                    "Добро пожаловать! У тебя 100 монет.");
            try {
                bot.execute(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


}
