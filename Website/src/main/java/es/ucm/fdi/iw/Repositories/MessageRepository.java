package es.ucm.fdi.iw.Repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ucm.fdi.iw.model.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query(value = "SELECT * FROM Message AS m "+
        "WHERE m.event_id = :eventId", nativeQuery = true)
    ArrayList<Message> getMsgFromChat(@Param("eventId") long eventId);
}