package es.ucm.fdi.iw.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ucm.fdi.iw.model.UserEvent;


public interface UserEventRepository extends JpaRepository<UserEvent,Long> {
    @Modifying
    @Query(value = "INSERT INTO UserEvent (user, event, fav, joined, rol) " +
    "VALUES(:userId, :eventId, :fav, :joined, :rol)",
        nativeQuery = true)
    void deactivateUsersNotLoggedInSince(@Param("userId") long userId, 
    @Param("eventId") long eventId, @Param("fav") Boolean fav,
    @Param("rol") String rol);
}
