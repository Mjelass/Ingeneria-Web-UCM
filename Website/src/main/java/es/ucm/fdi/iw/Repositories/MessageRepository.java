package es.ucm.fdi.iw.Repositories;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ucm.fdi.iw.model.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query(value = "SELECT * FROM Message AS m "+
        "WHERE m.event_id = :eventId ORDER BY m.date_sent DESC", nativeQuery = true)
    ArrayList<Message> getMsgFromChat(@Param("eventId") long eventId);

    @Query(value = "SELECT * FROM Message "+
        "WHERE event_id = :eventId ORDER BY date_sent DESC", nativeQuery = true)
    Page<Message> getMsgFromChat(@Param("eventId") long eventId, final Pageable pageable);

    @Query(value = "SELECT * FROM Message "+
        "WHERE event_id = :eventId AND date_sent < :date ORDER BY date_sent DESC", nativeQuery = true)
    Page<Message> getMsgFromChatBefore(@Param("eventId") long eventId, LocalDateTime date, final Pageable pageable);
}