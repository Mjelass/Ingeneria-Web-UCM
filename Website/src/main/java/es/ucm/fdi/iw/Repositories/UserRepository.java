package es.ucm.fdi.iw.Repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import es.ucm.fdi.iw.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // List of user joined

    @Query(value = "SELECT * FROM User_Event ue " +
    "INNER JOIN IWUser u ON ue.user_id = u.id " +
    "WHERE ue.event_id = :eventId AND ue.joined = TRUE AND ue.user_id <> :userId", nativeQuery = true)  
    ArrayList<User> getJoinedUsers(@Param("eventId") long eventId, @Param("userId") long userId);

}
