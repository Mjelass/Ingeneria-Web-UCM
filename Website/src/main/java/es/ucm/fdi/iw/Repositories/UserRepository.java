package es.ucm.fdi.iw.Repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
