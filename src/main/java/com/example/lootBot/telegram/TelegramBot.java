package com.example.lootBot.telegram;

import com.example.lootBot.config.BotConfig;

import com.example.lootBot.telegram.handlers.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final List<UpdateHandler> handlers;

    public TelegramBot(BotConfig config, List<UpdateHandler> handlers) {
        this.config = config;
        this.handlers = handlers;
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        handlers.forEach(h -> h.handle(update, this));
    }
}
