package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entities.NotificationTask;
import pro.sky.telegrambot.repository.TelegramRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    private TelegramRepository repository;

    public TelegramBotUpdatesListener(TelegramRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        for(NotificationTask task : repository.findCurrentTasks(currentTime)) {
            String message = task.getMessage();
            Long id = task.getChat_id();

            telegramBot.execute(new SendMessage(id, "Напоминание(" + task.getDatetime() + "): " + message));
            repository.delete(task);
        }
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() == null) {
                return;
            }

            String text = update.message().text();
            long id = update.message().chat().id();

            if (text.equals("/start")) {
                telegramBot.execute(new SendMessage(id, "Здарофф"));
                return;
            }

            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                String date = matcher.group(1);
                String item = matcher.group(3);

                LocalDateTime localDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                NotificationTask task = repository.save(new NotificationTask()
                        .setChatId(id)
                        .setDatetime(localDate)
                        .setMessage(item));

                if (task != null) {
                    telegramBot.execute(new SendMessage(id, "Ваша задача записана под номером " + task.getId()));
                }
            } else {
                telegramBot.execute(new SendMessage(id, "Моя твоя не понимай"));
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
