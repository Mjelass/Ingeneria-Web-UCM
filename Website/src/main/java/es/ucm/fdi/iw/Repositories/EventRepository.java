package es.ucm.fdi.iw.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.ucm.fdi.iw.model.Event;


public interface EventRepository extends JpaRepository<Event,Long> {
 
}
