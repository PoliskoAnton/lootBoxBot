package com.example.lootBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class LootBoxBot extends TelegramLongPollingBot {

    private final Map<Long, Integer> coins = new HashMap<>();
    private final Random random = new Random();

    @Override
    public String getBotUsername() {
        return "OneDay_Chenalbot"; // укажи имя бота
    }

    @Override
    public String getBotToken() {
        return System.getenv("7603105133:AAGcGeTNzDxMlEwKmrgLAkFcZgS3XmzIzxI"); // вставь токен от BotFather
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            coins.putIfAbsent(chatId, 100); // стартовый баланс

            switch (text) {
                case "/start" -> sendText(chatId, "Привет! У тебя " + coins.get(chatId) + " монет.");
                case "/balance" -> sendText(chatId, "Баланс: " + coins.get(chatId));
                case "/buy" -> buyChest(chatId);
                default -> sendText(chatId, "Команды: /start, /balance, /buy");
            }
        }
    }

    private void buyChest(long chatId) {
        int balance = coins.get(chatId);
        if (balance < 10) {
            sendText(chatId, "Недостаточно монет! Нужно 10.");
            return;
        }
        coins.put(chatId, balance - 10);

        // шанс 50% что выпадет картинка
        if (random.nextBoolean()) {
            try {
                String imageUrl = getRandomWaifuUrl();
                SendPhoto photo = new SendPhoto();
                photo.setChatId(chatId);
                photo.setPhoto(new org.telegram.telegrambots.meta.api.objects.InputFile(imageUrl));
                execute(photo);
            } catch (Exception e) {
                e.printStackTrace();
                sendText(chatId, "Ошибка при получении картинки.");
            }
        } else {
            sendText(chatId, "Сундук пуст... 😢 Баланс: " + coins.get(chatId));
        }
    }

    private String getRandomWaifuUrl() throws Exception {
        URL url = new URL("https://api.waifu.pics/sfw/waifu"); // пример API
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        Scanner scanner = new Scanner(conn.getInputStream());
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();

        // простое извлечение ссылки из JSON (без библиотек)
        int start = response.indexOf("\"url\":\"") + 7;
        int end = response.indexOf("\"", start);
        return response.substring(start, end).replace("\\/", "/");
    }

    private void sendText(long chatId, String text) {
        try {
            execute(new SendMessage(String.valueOf(chatId), text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new LootBoxBot());
    }
}
