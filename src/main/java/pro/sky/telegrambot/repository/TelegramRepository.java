package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entities.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TelegramRepository extends JpaRepository<NotificationTask, Long> {

    @Query(value = "SELECT * FROM notification_task WHERE datetime <= :current ORDER BY datetime", nativeQuery = true)
    List<NotificationTask> findCurrentTasks(@Param("current") LocalDateTime current);

}
