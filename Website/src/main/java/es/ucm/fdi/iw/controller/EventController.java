package es.ucm.fdi.iw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.Repositories.UserEventRepository;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.UserEvent;
import es.ucm.fdi.iw.model.UserEventId;
import es.ucm.fdi.iw.model.Event.Status;
import es.ucm.fdi.iw.model.Event.Type;

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
        User u = (User) session.getAttribute("u");
        UserEvent ue = null;
        if (u != null) {
            UserEventId ueId = new UserEventId();
            ueId.setEvent(id);
            ueId.setUser(u.getId());
            ue = entityManager.find(UserEvent.class, ueId);
        }

        model.addAttribute("isLogged", u != null);
        model.addAttribute("fav", ue == null ? false : ue.getFav());
        model.addAttribute("joined", ue == null ? false : ue.getJoined());
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
    @Transactional
    public String updateUserEvent(@PathVariable long id, Model model, HttpSession session,
            @RequestBody UserEvent body) {
        Boolean fav;
        Boolean joined;
        String rol;
        int additionJoin = 0;
        // Search UserEvent row with event=id and user=u.getId()
        User u = (User) session.getAttribute("u");
        Event e = entityManager.find(Event.class, id);
        UserEventId ueId = new UserEventId();
        ueId.setEvent(id);
        ueId.setUser(u.getId());
        UserEvent ue = entityManager.find(UserEvent.class, ueId);
        // There is no row, so we set the default value in attributes that
        // were no sent.
        if (ue == null) {
            fav = body.getFav() == null ? false : body.getFav();
            joined = body.getJoined() == null ? false : body.getJoined();
            rol = body.getRol() == null ? "" : body.getRol();
            // Update event occcupied atribute.
            additionJoin = joined ? 1 : 0;
        }
        // There is a row, so we update it with sent values.
        else {
            fav = body.getFav() == null ? ue.getFav() : body.getFav();
            joined = body.getJoined() == null ? ue.getJoined() : body.getJoined();
            rol = body.getRol() == null ? ue.getRol() : body.getRol();
            // Update event occcupied atribute.
            if (ue.getJoined() != joined) { // Value has changed
                additionJoin = joined ? 1 : -1;
            }
        }
        // If an event is not open you can't join.
        if (additionJoin == 1 && e.getStatus() != Status.OPEN) {
            throw new IllegalArgumentException();
        }
        // Update event occupied atribute.
        e.setOccupied(e.getOccupied() + additionJoin);
        // Change event's status to closed or open.
        e.setStatus(e.getOccupied() == e.getCapacity() ? Status.CLOSED : Status.OPEN);
        // Update UserEvent and Event tables.
        ue = new UserEvent(u, e, fav, joined, rol);
        userEventRepository.save(ue);
        eventRepository.save(e);
        return "ok";
    }

    @Transactional
    @PostMapping("/saveEvent")
    public String saveUser(@ModelAttribute Event event, Model model, HttpSession session) {

        Event insertEvent = new Event();
        User u = (User) session.getAttribute("u");
        insertEvent.setTitle(event.getTitle());
        insertEvent.setInitDate(event.getInitDate());
        insertEvent.setFinishDate(event.getFinishDate());
        insertEvent.setDestination(event.getDestination());
        insertEvent.setReunionPoint(event.getReunionPoint());
        insertEvent.setDescription(event.getDescription());
        insertEvent.setPrice(event.getPrice());
        insertEvent.setCapacity(event.getCapacity());
        insertEvent.setOccupied(1);
        insertEvent.setTransport(event.getTransport());
        insertEvent.setNotes(event.getNotes());

        insertEvent.setStatus(Status.OPEN);
        insertEvent.setUserOwner(u);
        insertEvent.setType(event.getType());

        entityManager.persist(insertEvent);
        entityManager.flush();

        /*
         * model.addAttribute("event", insertEvent);
         * return "event";
         */
        return "redirect:/event/" + insertEvent.getId();
    }

    /**
     * Get a pic from event with name n
     * @param id
     * @param n
     * @return
     * @throws IOException
     */
	@GetMapping("{id}/pics/{n}")
	public StreamingResponseBody getPic(@PathVariable long id, @PathVariable long n) throws IOException {
		// File f = localData.getFile("event", ""+id);
        // int total = f.listFiles().length;
        File f = localData.getFile("event/" + id, "" + n + ".jpg");
		InputStream in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : EventController.defaultPic());
		return os -> FileCopyUtils.copy(in, os);
	}
    /**
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping("{id}/pics")
	public StreamingResponseBody getPics(@PathVariable long id) throws IOException {
		// File f = localData.getFile("event", ""+id);
        // int total = f.listFiles().length;
        File dir = localData.getFile("event/" + id, "");
        InputStream in = new BufferedInputStream(EventController.defaultPic());
        // for(var f: dir.listFiles()){
        //     in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : EventController.defaultPic());
        // }
		return os -> FileCopyUtils.copy(in, os);
	}

	/**
	 * Uploads a new pic
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("{id}/addPic")
	@ResponseBody
	public String addPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id,
			HttpServletResponse response, HttpSession session, Model model) throws IOException, NoSuchAlgorithmException {

        // int n = new Random().nextInt(9999)+10000;
		// User target = entityManager.find(User.class, id);
		// model.addAttribute("user", target);

		// log.info("Updating photo {} for event {}", n, id);
		if (photo.isEmpty()) {
			log.info("failed to addz photo: emtpy file?");
		} else {
            byte[] bytes = photo.getBytes();
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(bytes);
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            File f = localData.getFile("event/" + id, "" + hashtext + ".jpg");
			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
				
				stream.write(bytes);
				log.info("Uploaded photo for {} into {}!", id, f.getAbsolutePath());
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warn("Error uploading " + id + " ", e);
			}
		}
		return "{\"status\":\"photo uploaded correctly\"}";
	}


    @PostMapping("{id}/remPic/{n}")
	@ResponseBody
	public String removePic(@PathVariable long id, @PathVariable long n,
			HttpServletResponse response, HttpSession session, Model model) throws IOException, NoSuchAlgorithmException {
        File f = localData.getFile("event/" + id, "" + n + ".jpg");
        if(f == null){
            log.info("failed to remove photo: emtpy file?");
        }
        else {
            f.delete();
        }
        return "{\"status\":\"photo uploaded correctly\"}";
    }


    /**
	 * Returns the default profile pic
	 * 
	 * @return
	 */
	private static InputStream defaultPic() {
		return new BufferedInputStream(Objects.requireNonNull(
				UserController.class.getClassLoader().getResourceAsStream(
						"static/img/default-pic.jpg")));
	}
}
