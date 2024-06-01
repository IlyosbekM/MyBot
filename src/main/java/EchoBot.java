import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class EchoBot extends TelegramLongPollingBot {

    // Bot credentials
    private static final String BOT_TOKEN = "6973887476:AAFMbLkAoWNxaQQ0eVHWrA1VbLsoNdvbkj8";
    private static final String BOT_USERNAME = "@Ilyosbek_java_bot";
    // Chat ID to receive contact information
    private static final String MY_CHAT_ID = "1934543593";

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new EchoBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();

            if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();

                if (messageText.equals("/start")) {
                    sendContactRequest(chatId);
                } else {
                    // Echo the received message back to the user
                    SendMessage message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setText(messageText);  // Echo the received message

                    try {
                        execute(message); // Sending the message
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if (update.getMessage().hasContact()) {
                // Handle the received contact information
                var contact = update.getMessage().getContact();

                SendContact sendContact = new SendContact();
                sendContact.setChatId(MY_CHAT_ID);
                sendContact.setFirstName(contact.getFirstName());
                sendContact.setLastName(contact.getLastName());
                sendContact.setPhoneNumber(contact.getPhoneNumber());

                try {
                    execute(sendContact); // Forward the contact to your chat
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                // Acknowledge contact received
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("Thank you for sharing your contact!");

                try {
                    execute(message); // Sending the message
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendContactRequest(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Please share your contact information:");

        // Create a keyboard with a button to share contact information
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton("📞 Share Contact");
        button.setRequestContact(true);
        row.add(button);

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message); // Sending the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}






//
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//import oshi.SystemInfo;
//import oshi.hardware.CentralProcessor;
//
//public class CpuMonitorBot extends TelegramLongPollingBot {
//
//    private static final String BOT_TOKEN = "6973887476:AAFMbLkAoWNxaQQ0eVHWrA1VbLsoNdvbkj8";
//    private static final String BOT_USERNAME = "Ilyosbek_java_bot";
//    private static final String CHAT_ID = "1934543593";
//    private final SystemInfo si;
//    private final CentralProcessor processor;
//
//    private boolean notifiedAbove50 = false;
//    private boolean notifiedBelow30 = false;
//    private long[] prevTicks;
//
//    public CpuMonitorBot() {
//        si = new SystemInfo();
//        processor = si.getHardware().getProcessor();
//        prevTicks = processor.getSystemCpuLoadTicks();
//    }
//
//    @Override
//    public String getBotToken() {
//        return BOT_TOKEN;
//    }
//
//    @Override
//    public String getBotUsername() {
//        return BOT_USERNAME;
//    }
//
//    @Override
//    public void onUpdateReceived(org.telegram.telegrambots.meta.api.objects.Update update) {
//    }
//
//    public void monitorCpu() {
//        while (true) {
//            double cpuLoad = getCpuLoad();
//
//            if (cpuLoad > 50 && !notifiedAbove50) {
//                sendNotification("Warning: CPU usage is above 50%! Current usage: " + String.format("%.2f", cpuLoad) + "%");
//                notifiedAbove50 = true;
//                notifiedBelow30 = false;
//            } else if (cpuLoad < 30 && !notifiedBelow30) {
//                sendNotification("Info: CPU usage is below 30%. Current usage: " + String.format("%.2f", cpuLoad) + "%");
//                notifiedBelow30 = true;
//                notifiedAbove50 = false;
//            }
//
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private double getCpuLoad() {
//        long[] newTicks = processor.getSystemCpuLoadTicks();
//        double load = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
//        prevTicks = newTicks; // Update previous ticks for the next comparison
//        return load;
//    }
//
//    private void sendNotification(String messageText) {
//        SendMessage message = new SendMessage();
//        message.setChatId(CHAT_ID);
//        message.setText(messageText);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            CpuMonitorBot bot = new CpuMonitorBot();
//            botsApi.registerBot(bot);
//            bot.monitorCpu();
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//}
