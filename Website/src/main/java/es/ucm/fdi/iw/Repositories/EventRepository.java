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
                        "WHERE User_Event.user_id = :userId AND User_Event.Joined = TRUE", nativeQuery = true)
        ArrayList<Event> getEventsJoined(@Param("userId") long userId);

        // To Filter by Status
        @Query(value = "SELECT Event.* FROM Event " +
                        "INNER JOIN User_Event ON Event.id = User_Event.event_id " +
                        "WHERE User_Event.user_id = :userId AND Event.status = :status AND User_Event.Joined = TRUE", nativeQuery = true)
        ArrayList<Event> getEventsJoinedByStatus(@Param("userId") long userId, @Param("status") String status);

        @Query(value = "SELECT * FROM Event " +
                        "WHERE Event.user_owner_id = :userId", nativeQuery = true)
        ArrayList<Event> getUserEvents(@Param("userId") long userId);

        // To Filter by Status
        @Query(value = "SELECT * FROM Event " +
                        "WHERE Event.user_owner_id = :userId AND Event.status = :status", nativeQuery = true)
        ArrayList<Event> getUserEventsByStatus(@Param("userId") long userId, @Param("status") String status);

}
