package pro.sky.telegrambot.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {

    @Id
    @GeneratedValue
    private Integer id = 0;
    private Long chat_id;
    private String message;
    private LocalDateTime datetime;

    public NotificationTask() {}

    public Integer getId() {
        return id;
    }

    public NotificationTask setId(Integer id) {
        this.id = id;
        return this;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public NotificationTask setChatId(Long chat_id) {
        this.chat_id = chat_id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public NotificationTask setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public NotificationTask setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(chat_id, that.chat_id) && Objects.equals(message, that.message) && Objects.equals(datetime, that.datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat_id, message, datetime);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chat_id=" + chat_id +
                ", message='" + message + '\'' +
                ", datetime=" + datetime +
                '}';
    }
}
