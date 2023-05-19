package es.ucm.fdi.iw.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ucm.fdi.iw.model.Rating;

public interface RatingUserRepository extends JpaRepository<Rating, Long> {
    @Query(value = "SELECT COALESCE(SUM(ru.rating) / COUNT(*), 0) FROM RATING_USER AS ru " +
    "WHERE ru.USER_TARG_ID = :userId", nativeQuery = true)
    float getAverageRating(@Param("userId") long userId);
}
