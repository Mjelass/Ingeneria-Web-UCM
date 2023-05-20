package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.Repositories.ReportRepository;
import es.ucm.fdi.iw.Repositories.UserRepository;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Report;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;
import es.ucm.fdi.iw.model.User.Status;
import es.ucm.fdi.iw.model.User.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User management.
 *
 * Access to this end-point is authenticated.
 */
@Controller()
@RequestMapping("user")
public class UserController {

	private static final Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private LocalData localData;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Exception to use when denying access to unauthorized users.
	 * 
	 * In general, admins are always authorized, but users cannot modify
	 * each other's profiles.
	 */
	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "No eres administrador, y éste no es tu perfil") // 403
	public static class NoEsTuPerfilException extends RuntimeException {
	}

	/**
	 * Encodes a password, so that it can be saved for future checking. Notice
	 * that encoding the same password multiple times will yield different
	 * encodings, since encodings contain a randomly-generated salt.
	 * 
	 * @param rawPassword to encode
	 * @return the encoded password (typically a 60-character string)
	 *         for example, a possible encoding of "test" is
	 *         {bcrypt}$2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
	 */
	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	/**
	 * Generates random tokens. From https://stackoverflow.com/a/44227131/15472
	 * 
	 * @param byteLength
	 * @return
	 */
	public static String generateRandomBase64Token(int byteLength) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] token = new byte[byteLength];
		secureRandom.nextBytes(token);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(token); // base64 encoding
	}

	/**
	 * Landing page for a user profile
	 */
	@GetMapping("{id}")
	public String index(@PathVariable long id, Model model, HttpSession session,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "4") int size,
			@RequestParam(name = "search", defaultValue = "") String search,
			@RequestParam(name = "init", defaultValue = "") String init,
			@RequestParam(name = "fin", defaultValue = "") String fin) {
		User target = entityManager.find(User.class, id);
		User u = (User) session.getAttribute("u");

		if (u != null && u.getEnabled() == false) {
			return "redirect:/error";
		}

		ArrayList<Event> evs = eventRepository.getUserEvents(target.getId());

		ArrayList<Event> evJoined = eventRepository.getEventsJoined(target.getId());

		ArrayList<Event> evJoinedFinish = eventRepository.getEventsJoinedByStatus(target.getId(), Event.Status.FINISH.toString());

		ArrayList<Report> userReports = reportRepository.findReportsByUserId(target.getId());
	
		int numReports = 0;
		if (!userReports.isEmpty()) {
			numReports = userReports.size();
		}

		Page<Event> pageEvents = pageImplement(evs);
		Page<Event> pageEventsOpen = pageImplement(evJoined);
		Page<Event> pageEventsFinished = pageImplement(evJoinedFinish);

		model.addAttribute("allMyEvents", pageEvents.getContent());
		model.addAttribute("allEventsJoined", pageEventsOpen.getContent());
		model.addAttribute("allEventsJoinedClosed", pageEventsFinished.getContent());

		// Page Event Finish
		model.addAttribute("numpages", new int[pageEventsOpen.getTotalPages()]);
		model.addAttribute("numResults", pageEventsOpen.getTotalPages() * size);
		model.addAttribute("numFirstRes", (page * size) + 1);
		model.addAttribute("size", size);
		model.addAttribute("currentPage", page);
		model.addAttribute("numPages", pageEventsOpen.getTotalPages());
		model.addAttribute("search", search);
		model.addAttribute("init", init);
		model.addAttribute("fin", fin);

		model.addAttribute("user", target);
		model.addAttribute("idRequest", target.getId());
		model.addAttribute("idUser", u != null ? u.getId() : -1);
		model.addAttribute("isOwner", u != null && (target.getId() == u.getId()));
		model.addAttribute("numReports", numReports);
		return "user";
	}

	public Page<Event> pageImplement(ArrayList<Event> ev) {
		int total = ev.size();
		int page = 0, size = 5;
		int fromIndex = (page) * size;
		int toIndex = Math.min(fromIndex + size, total);
		Page<Event> pageEvents;

		List<Event> pageList = ev.subList(fromIndex, toIndex);
		PageRequest pageRequest = PageRequest.of(page, size);
		pageEvents = new PageImpl<>(pageList, pageRequest, total);
		return pageEvents;
	}

	/**
	 * Alter or create a user
	 */
	@PostMapping("/{id}")
	@Transactional
	public String postUser(
			HttpServletResponse response,
			@PathVariable long id,
			@ModelAttribute User edited,
			@RequestParam(required = false) String pass2,
			Model model, HttpSession session) throws IOException {

		User requester = (User) session.getAttribute("u");
		User target = null;
		if (id == -1 && requester.hasRole(Role.ADMIN)) {
			// create new user with random password
			target = new User();
			target.setPassword(encodePassword(generateRandomBase64Token(12)));
			target.setEnabled(true);
			entityManager.persist(target);
			entityManager.flush(); // forces DB to add user & assign valid id
			id = target.getId(); // retrieve assigned id from DB
		}

		// retrieve requested user
		target = entityManager.find(User.class, id);
		model.addAttribute("user", target);

		if (requester.getId() != target.getId() &&
				!requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}

		if (edited.getPassword() != null) {
			if (!edited.getPassword().equals(pass2)) {
				// FIXME: complain
			} else {
				// save encoded version of password
				target.setPassword(encodePassword(edited.getPassword()));
			}
		}
		target.setUsername(edited.getUsername());
		target.setFirstName(edited.getFirstName());
		target.setLastName(edited.getLastName());

		// update user session so that changes are persisted in the session, too
		if (requester.getId() == target.getId()) {
			session.setAttribute("u", target);
		}

		return "user";
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

	/**
	 * Downloads a profile pic for a user id
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@GetMapping("{id}/pic")
	public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
		File f = localData.getFile("user", "" + id + ".jpg");
		InputStream in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : UserController.defaultPic());
		return os -> FileCopyUtils.copy(in, os);
	}

	/**
	 * Uploads a profile pic for a user id
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@PostMapping("{id}/pic")
	public String setPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id,
			HttpServletResponse response, HttpSession session, Model model) throws IOException {

		User target = entityManager.find(User.class, id);
		model.addAttribute("user", target);

		// check permissions
		User requester = (User) session.getAttribute("u");
		if (requester.getId() != target.getId() &&
				!requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}

		log.info("Updating photo for user {}", id);
		File f = localData.getFile("user", "" + id + ".jpg");
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
		} else {
			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
				log.info("Uploaded photo for {} into {}!", id, f.getAbsolutePath());
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warn("Error uploading " + id + " ", e);
			}
		}
		return "redirect:/user/" + id;
	}

	/**
	 * Returns JSON with all received messages
	 * TODO Remove or change
	 */
	@GetMapping(path = "received", produces = "application/json")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody // para indicar que no devuelve vista, sino un objeto (jsonizado)
	public List<Message.Transfer> retrieveMessages(HttpSession session) {
		long userId = ((User) session.getAttribute("u")).getId();
		User u = entityManager.find(User.class, userId);
		log.info("Generating message list for user {} ({} messages)",
				u.getUsername(), u.getReceived().size());
		return u.getReceived().stream().map(Transferable::toTransfer).collect(Collectors.toList());
	}

	/**
	 * Returns JSON with count of unread messages
	 * TODO remove or change
	 */
	@GetMapping(path = "unread", produces = "application/json")
	@ResponseBody
	public String checkUnread(HttpSession session) {
		long userId = ((User) session.getAttribute("u")).getId();
		long unread = entityManager.createNamedQuery("Message.countUnread", Long.class)
				.setParameter("userId", userId)
				.getSingleResult();
		session.setAttribute("unread", unread);
		return "{\"unread\": " + unread + "}";
	}

	/**
	 * Posts a message to a user.
	 * 
	 * @param id of target user (source user is from ID)
	 * @param o  JSON-ized message, similar to {"message": "text goes here"}
	 * @throws JsonProcessingException
	 */
	// TODO Remove or change
	@PostMapping("/{id}/msg")
	@ResponseBody
	@Transactional
	public String postMsg(@PathVariable long id,
			@RequestBody JsonNode o, Model model, HttpSession session)
			throws JsonProcessingException {

		String text = o.get("message").asText();
		User u = entityManager.find(User.class, id);
		User sender = entityManager.find(
				User.class, ((User) session.getAttribute("u")).getId());
		model.addAttribute("user", u);

		// construye mensaje, lo guarda en BD
		Message m = new Message();
		// m.setReceiver(u);
		m.setSender(sender);
		m.setDateSent(LocalDateTime.now());
		m.setText(text);
		entityManager.persist(m);
		entityManager.flush(); // to get Id before commit

		ObjectMapper mapper = new ObjectMapper();
		/*
		 * // construye json: método manual
		 * ObjectNode rootNode = mapper.createObjectNode();
		 * rootNode.put("from", sender.getUsername());
		 * rootNode.put("to", u.getUsername());
		 * rootNode.put("text", text);
		 * rootNode.put("id", m.getId());
		 * String json = mapper.writeValueAsString(rootNode);
		 */
		// persiste objeto a json usando Jackson
		String json = mapper.writeValueAsString(m.toTransfer());

		log.info("Sending a message to {} with contents '{}'", id, json);

		messagingTemplate.convertAndSend("/user/" + u.getUsername() + "/queue/updates", json);
		return "{\"result\": \"message sent.\"}";
	}

	@PostMapping("{id}/report")
	@ResponseBody
	@Transactional
	public String updateUserEvent(@PathVariable long id, Model model, HttpSession session,
			@RequestBody Report report) {
		User userSource = (User) session.getAttribute("u");
		User userTarget = entityManager.find(User.class, id);
		if (userSource == null || userTarget == null) {
			throw new IllegalArgumentException();
		}
		report.setUserSource(userSource);
		report.setUserTarget(userTarget);
		reportRepository.save(report);

		userTarget.setNumReports(userTarget.getNumReports() + 1);

		return "ok";
	}

	// ADD User to the dataBase when you fill the form
	// Falta redirigirlo a la pagina errorSignUp si hay error
	@Transactional
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");	

		

		User InsertUser = new User();
		InsertUser.setFirstName(user.getFirstName());
		InsertUser.setLastName(user.getLastName());
		InsertUser.setPassword(encodePassword(user.getPassword()));
		InsertUser.setUsername(user.getUsername());
		InsertUser.setLocation(user.getLocation());
		InsertUser.setDescription(user.getDescription());
		InsertUser.setLanguages(user.getLanguages());
		InsertUser.setEmail(user.getEmail());
		InsertUser.setBirthdate(user.getBirthdate());
		InsertUser.setRating(0F);
		InsertUser.setStatus(Status.ACTIVE);
		InsertUser.setRoles(Role.USER.name());
		InsertUser.setEnabled(true);
		InsertUser.setUserEvent(null);
		InsertUser.setSent(null);
		InsertUser.setLevel(Level.BRONZE);
		InsertUser.setReceived(null);
		InsertUser.setNumReports(0);
		entityManager.persist(InsertUser);
		entityManager.flush();
		model.addAttribute("User", InsertUser);

		if(u.hasRole(Role.ADMIN)){
			return "redirect:/admin/";	
		}
		
		return "redirect:/";

	}

	// add or erase user from blacklist
	@Transactional
	@GetMapping("{id}/blacklistUser")
	public String blacklistUser(@PathVariable long id, Model model, HttpSession session){
		User user = entityManager.find(User.class, id);
		User u = (User) session.getAttribute("u");	

		if (user != null && u != null && u.hasRole(Role.ADMIN)) {
			if (user.getStatus().equals(Status.BLACK_LISTED))
				user.setStatus(Status.ACTIVE);
			else
				user.setStatus(Status.BLACK_LISTED);

			userRepository.save(user);
			return "redirect:/admin/blackListUser";
		}
		return "redirect:/";
	}

	@Transactional
	@PostMapping("{id}/deleteUser")
	public String deleteUser(@PathVariable long id, Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");	
		User user = entityManager.find(User.class, id);

		if (user != null && u != null && u.hasRole(Role.ADMIN)) {
			if(user.getEnabled())
				user.setEnabled(false);
			else 
				user.setEnabled(true);

			userRepository.save(user);
			return "redirect:/admin/allUsers";
		}
		return "redirect:/";
	}

	@PostMapping("{id}/deleteReport")
	@Transactional
	public String deleteReport(@PathVariable long id, Model model, HttpSession session) {
		User u = (User) session.getAttribute("u");
		Report report = entityManager.find(Report.class, id);
		if (u.hasRole(Role.ADMIN) &&report != null) {
			// User user = entityManager.find(User.class, report.userTarget.getId());
			// if(user!=null){
			// 	user.setNumReports(user.getNumReports() - 1);
			// }
			//report.
			// TODO Eliminar report
		}
		
		return "redirect:/admin/allReports";
	}
}
