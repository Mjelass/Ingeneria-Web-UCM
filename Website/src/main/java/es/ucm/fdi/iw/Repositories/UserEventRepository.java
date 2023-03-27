package es.ucm.fdi.iw.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.ucm.fdi.iw.model.UserEvent;


public interface UserEventRepository extends JpaRepository<UserEvent,Long> {

}
