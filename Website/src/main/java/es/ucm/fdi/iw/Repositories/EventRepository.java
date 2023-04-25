package es.ucm.fdi.iw.Repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ucm.fdi.iw.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
        @Query(value = "SELECT count(*) FROM User_Event AS ue " +
                        "WHERE ue.event_id = :eventId AND ue.fav = TRUE", nativeQuery = true)
        int getNumFavsEvent(@Param("eventId") long eventId);

        @Query(value = "SELECT Event.* FROM Event " +
                        "INNER JOIN User_Event ON Event.id = User_Event.event_id " +
                        "WHERE User_Event.user_id = :userId", nativeQuery = true)
        ArrayList<Event> getUserJoinedEvents(@Param("userId") long userId);

        @Query(value = "SELECT Event.* FROM Event " +
                        "INNER JOIN User_Event ON Event.id = User_Event.event_id " +
                        "WHERE User_Event.user_id = :userId AND Event.status = :status AND User_Event.joined = TRUE", nativeQuery = true)
        ArrayList<Event> getUserJoinedEventsStatus(@Param("userId") long userId, @Param("status") String status);
}
