package es.ucm.fdi.iw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.Repositories.UserEventRepository;
import es.ucm.fdi.iw.Repositories.UserRepository;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Rating;
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

    @Autowired
    private UserRepository userRepository;

    // Return event/{id} page
    @GetMapping("{id}")
    public String index(@PathVariable long id, Model model, HttpSession session) {
        Event target = entityManager.find(Event.class, id);
        if (target == null) { // TODO Error Msg
            return "index";
        }
        // Get UserEvent row from user logged and event id.
        User u = (User) session.getAttribute("u");
        UserEvent ue = null;
        if (u != null) {
            UserEventId ueId = new UserEventId();
            ueId.setEvent(id);
            ueId.setUser(u.getId());
            ue = entityManager.find(UserEvent.class, ueId);
        }

        File files = localData.getFile("event", ""+id);
        ArrayList<String> photosNames = new ArrayList<>();
        if (files.listFiles() != null){
            for(var f: files.listFiles()){
                String fileName = f.getName();
                if (fileName.indexOf(".") > 0) {
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                }
                photosNames.add(fileName);
            }
        }

        model.addAttribute("isLogged", u != null);
        model.addAttribute("fav", ue == null ? false : ue.getFav());
        model.addAttribute("joined", ue == null ? false : ue.getJoined());
        int numFavs = eventRepository.getNumFavsEvent(id);

        // Check if user is already in event
        boolean isEventFinished = target.getStatus().equals(Event.Status.FINISH);
        boolean canRate = (ue != null) && isEventFinished && ue.getJoined();
        model.addAttribute("canRate", canRate);

        model.addAttribute("numFavs", numFavs);
        int ownerRatings = 10;
        model.addAttribute("ownerRatings", ownerRatings);
        model.addAttribute("photos", photosNames);
        model.addAttribute("isOwner", u != null && (target.getUserOwner().getId() == u.getId()));
        model.addAttribute("event", target);
        return "event";
    }

    // Used to modify an existent event.
    @PostMapping("{id}/change")
	public String changeEvent(@PathVariable long id, HttpServletResponse response, 
        HttpSession session, Model model, @RequestParam(required = false) Event.Status status, 
        @RequestParam(required = false) String description){
        User u = (User) session.getAttribute("u");
        Event e = entityManager.find(Event.class, id);
        if (u != null && u.getId() == e.getUserOwner().getId()) {
            // TODO check values make sense: OPEN and don't has vacancies.
            e.setStatus(status == null ? e.getStatus(): status);
            e.setDescription(description == null ? e.getDescription(): description);
            eventRepository.save(e);
        }
        return "redirect:/event/" + id;
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
        // TODO check event status
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
    


    // Method to process user joined list to rating users
    @GetMapping("{id}/saveRating")
    public String saveRating(@PathVariable long id, Model model, HttpSession session) {
        Event event = entityManager.find(Event.class, id);
        User ratingUser = (User) session.getAttribute("u");

        if (event == null || ratingUser == null || event.getStatus() != Event.Status.FINISH) {
            return "redirect:/event/" + event.getId();
        }

        // Get a list of all UserEvent rows associated with this event, with joined == true
        ArrayList<User> joinedUsers = userRepository.getJoinedUsers(event.getId());
        // Page<User> pageJoinedUser = pageImplement(joinedUsers);
        if (!joinedUsers.isEmpty()) {
            model.addAttribute("joinedUser", joinedUsers);
        }else{
            return "redirect:/event/" + event.getId();
        }
        
        return "event";
    }


    @PostMapping("{id}/saveRating")
    @Transactional
    public String submitRatings(@PathVariable long id, HttpSession session) {
        Event event = entityManager.find(Event.class, id);
        User ratingUser = (User) session.getAttribute("u");
    
        if (event == null || ratingUser == null || event.getStatus() != Event.Status.FINISH) {
            return "redirect:/event/" + id;
        }
    
        // for (Map.Entry<String, String> entry : form.entrySet()) {
        //     String userId = entry.getKey();
        //     String ratingValue = entry.getValue();
    
        //     User ratedUser = entityManager.find(User.class, Long.parseLong(userId));
        //     int rating = Integer.parseInt(ratingValue);
    
        //     Rating ratingObj = new Rating();
        //     ratingObj.setEvent(event);
        //     ratingObj.setRatingUser(ratingUser);
        //     ratingObj.setRatedUser(ratedUser);
        //     ratingObj.setRating(rating);
        //     ratingRepository.save(ratingObj);
        // }
    
        return "redirect:/event/" + id;
    }
    
    // || IMAGES METHODS
    // TODO check user
    /**
     * Get a pic from event with name n
     * @param id
     * @param n
     * @return
     * @throws IOException
     */
	@GetMapping("{id}/pic/{n}")
	public StreamingResponseBody getPic(@PathVariable long id, @PathVariable String n) throws IOException {
		// File f = localData.getFile("event", ""+id);
        // int total = f.listFiles().length;
        File f = localData.getFile("event/" + id, "" + n + ".jpg");
		InputStream in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : EventController.defaultPic());
		return os -> FileCopyUtils.copy(in, os);
	}
    /**
     * Get first pic from an event
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping("{id}/firstPic")
	public StreamingResponseBody getFirstPic(@PathVariable long id) throws IOException {
        File fileDir = localData.getFile("event", "" + id);
        if(fileDir == null) {
            return os -> FileCopyUtils.copy(new BufferedInputStream(EventController.defaultPic()), os);
        }
        File[] files = fileDir.listFiles();
		InputStream in = new BufferedInputStream(files != null && files.length != 0 && files[0].exists() ?
            new FileInputStream(files[0]) : EventController.defaultPic());
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
	public String addPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id,
			HttpServletResponse response, HttpSession session, Model model) throws IOException, NoSuchAlgorithmException {

		if (photo.isEmpty()) {
			log.info("failed to add photo: emtpy file?");
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
		return "redirect:/event/" + id;
	}


    @PostMapping("{id}/rmPic/{n}")
	public String removePic(@PathVariable long id, @PathVariable String n,
			HttpServletResponse response, HttpSession session, Model model) throws IOException, NoSuchAlgorithmException {
        File f = localData.getFile("event/" + id, "" + n + ".jpg");
        if(f == null){
            log.info("failed to remove photo: emtpy file?");
        }
        else {
            f.delete();
        }
        return "redirect:/event/" + id;
    }

    @PostMapping("{id}/coverPic/{n}")
	public String setCoverPic(@PathVariable long id, @PathVariable String n,
			HttpServletResponse response, HttpSession session, Model model) throws IOException, NoSuchAlgorithmException {
        if (n.compareTo("cover") == 0) {
            return "redirect:/event/" + id;
        }
        File f = localData.getFile("event/" + id, "" + n + ".jpg");
        if(f == null){
            log.info("failed to set event cover: emtpy file?");
        }
        else {
            // Change actual cover.jpg to <hash>.jpg
            File fCover = localData.getFile("event/" + id, "cover.jpg");
            if (fCover.exists()){
                byte[] bytes = Files.readAllBytes(fCover.toPath());
                MessageDigest m = MessageDigest.getInstance("MD5");
                m.reset();
                m.update(bytes);
                byte[] digest = m.digest();
                BigInteger bigInt = new BigInteger(1,digest);
                String hashtext = bigInt.toString(16);
                Path source = Paths.get(fCover.getAbsolutePath());
                Files.move(source, source.resolveSibling(hashtext + ".jpg"));
            }
            // Change name <hash n>.jpg to cover.jpg
            Path source = Paths.get(f.getAbsolutePath());
            Files.move(source, source.resolveSibling("cover.jpg"));
        }
        return "redirect:/event/" + id;
    }

    @GetMapping("{id}/coverPic")
	public StreamingResponseBody getCoverPic(@PathVariable long id) throws IOException {
        File file = localData.getFile("event/" + id, "cover.jpg");
		InputStream in = new BufferedInputStream(file.exists()?
            new FileInputStream(file) : EventController.defaultPic());
		return os -> FileCopyUtils.copy(in, os);
	}

    /**
	 * Returns the default event pic
	 * 
	 * @return
	 */
	private static InputStream defaultPic() {
		return new BufferedInputStream(Objects.requireNonNull(
				UserController.class.getClassLoader().getResourceAsStream(
						"static/img/default-event-pic.jpg")));
	}
}
