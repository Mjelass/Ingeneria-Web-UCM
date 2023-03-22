package es.ucm.fdi.iw.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ucm.fdi.iw.model.Event;


public interface EventRepository extends JpaRepository<Event,Long> {
    @Query(value = "SELECT count(*) FROM User_Event AS ue "+
        "WHERE ue.event_id = :eventId AND ue.fav = true", nativeQuery = true)
    int getNumFavsEvent(@Param("eventId") long eventId);
}
