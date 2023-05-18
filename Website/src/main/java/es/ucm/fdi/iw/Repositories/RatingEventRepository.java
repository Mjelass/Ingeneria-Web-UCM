package es.ucm.fdi.iw.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ucm.fdi.iw.model.RatingEvent;

public interface RatingEventRepository extends JpaRepository<RatingEvent, Long> {
    @Query(value = "SELECT count(*) FROM RATING_EVENT AS re " +
    "WHERE re.event_id = :eventId AND re.user_src_id  = :userId", nativeQuery = true)
    int getNumRatingEvent(@Param("eventId") long eventId, @Param("userId") long userId);
}
