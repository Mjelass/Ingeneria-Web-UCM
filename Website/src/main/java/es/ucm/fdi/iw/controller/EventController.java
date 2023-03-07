package es.ucm.fdi.iw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Event;

@Controller()
@RequestMapping("event")
public class EventController {
    private static final Logger log = LogManager.getLogger(EventController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
    private LocalData localData;


        /**
     * Landing page for a user profile
     */
	@GetMapping("{id}")
    public String index(@PathVariable long id, Model model, HttpSession session) {
        Event target = entityManager.find(Event.class, id);
		// Event e = (Event)session.getAttribute("u");
        int numFavs = 10; //TODO Query
        model.addAttribute("numFavs", numFavs);
        int ownerRatings = 10;
        model.addAttribute("ownerRatings", ownerRatings);
        model.addAttribute("event", target);
		return "event";
    }


}
