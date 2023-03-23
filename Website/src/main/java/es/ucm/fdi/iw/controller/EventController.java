package es.ucm.fdi.iw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.Repositories.UserEventRepository;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.UserEvent;
import es.ucm.fdi.iw.model.UserEventId;
import es.ucm.fdi.iw.model.Event.Status;

@Controller()
@RequestMapping("event")
public class EventController {
    private static final Logger log = LogManager.getLogger(EventController.class);

    private static final String List = null;

	@Autowired
	private EntityManager entityManager;

	@Autowired
    private LocalData localData;

    @Autowired
	private EventRepository eventRepository;

    @Autowired
	private UserEventRepository userEventRepository;

    // Return event/{id} page
	@GetMapping("{id}")
    public String index(@PathVariable long id, Model model, HttpSession session) {
        Event target = entityManager.find(Event.class, id);
        // Get UserEvent row from user logged and event id.
        User u = (User)session.getAttribute("u");
        UserEvent ue = null;
        if (u != null) {
            UserEventId ueId = new UserEventId();
            ueId.setEvent(id);
            ueId.setUser(u.getId());
		    ue = entityManager.find(UserEvent.class, ueId);
        }

        model.addAttribute("isLogged", u != null);
        model.addAttribute("fav", ue == null? false: ue.getFav());
        model.addAttribute("joined", ue == null? false: ue.getJoined());
        int numFavs = eventRepository.getNumFavsEvent(id);
        model.addAttribute("numFavs", numFavs);
        int ownerRatings = 10;
        model.addAttribute("ownerRatings", ownerRatings);
        model.addAttribute("event", target);
		return "event";
    }

    // Update userEvent table
    @PostMapping("{id}/userEvent")
    @ResponseBody
    public String updateUserEvent(@PathVariable long id, Model model, HttpSession session,
        @RequestBody UserEvent body) {
        Boolean fav;
        Boolean joined;
        String rol;
        int additionJoin = 0;
        // Search UserEvent row with event=id and user=u.getId()
        User u = (User)session.getAttribute("u");
        Event e = entityManager.find(Event.class, id);
        UserEventId ueId = new UserEventId();
        ueId.setEvent(id);
        ueId.setUser(u.getId());
        UserEvent ue = entityManager.find(UserEvent.class, ueId);
        // There is no row, so we set the default value in attributes that
        // were no sent.
        if(ue == null){
            fav = body.getFav() == null ? false: body.getFav();
            joined = body.getJoined() == null ? false: body.getJoined();
            rol = body.getRol() == null ? "": body.getRol();
            // Update event occcupied atribute.
            additionJoin = joined? 1: 0;
        }
        // There is a row, so we update it with sent values.
        else {
            fav = body.getFav() == null ? ue.getFav(): body.getFav();
            joined = body.getJoined() == null ? ue.getJoined(): body.getJoined();
            rol = body.getRol() == null ? ue.getRol(): body.getRol();
            // Update event occcupied atribute.
            if (ue.getJoined() != joined) { // Value has changed
                additionJoin = joined? 1: -1;
            }
        }
        // If an event is not open you can't join.
        if (additionJoin == 1 && e.getStatus() != Status.OPEN){
            throw new IllegalArgumentException();
        }
        // Update event occupied atribute.
        e.setOccupied(e.getOccupied() + additionJoin);
        // Change event's status to closed or open.
        e.setStatus(e.getOccupied() == e.getCapacity()? Status.CLOSED: Status.OPEN);
        // Update UserEvent and Event tables.
        ue = new UserEvent(u, e, fav, joined, rol);
        userEventRepository.save(ue);
        eventRepository.save(e);
        return "ok";
    }


}
