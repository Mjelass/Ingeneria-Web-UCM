package es.ucm.fdi.iw.controller;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.Repositories.UserRepository;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Report;
import es.ucm.fdi.iw.model.User;

/**
 *  Site administration.
 *
 *  Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
@RequestMapping("admin")
public class AdminController {

	private static final Logger log = LogManager.getLogger(AdminController.class);

    @Autowired
    private EventRepository eventRepository; 
    @Autowired
    private EntityManager entityManager;

	@GetMapping("/")
    public String index(Model model) {
    
        return "redirect:/admin/allUsers";
    }

    @GetMapping("/allUsers")
    public String allUsers(Model model,
        @RequestParam(name="page",defaultValue="0")int page,
        @RequestParam(name="size",defaultValue="3")int size) {
        List<User> allUser = entityManager.createNamedQuery("User.allUsers").getResultList();
        int total = allUser.size();
            int fromIndex = (page) * size;
            int toIndex = Math.min(fromIndex + size, total);

            List<User> pageList = allUser.subList(fromIndex, toIndex);
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<User>PageUsers = new PageImpl<>(pageList, pageRequest, total);
        model.addAttribute("allUser", PageUsers.getContent());
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("numPages", PageUsers.getTotalPages());
        model.addAttribute("numpages", new int[PageUsers.getTotalPages()]);
        model.addAttribute("nbr", "allUsers");
        return "admin";
    }
     @GetMapping("/allEvents")
    public String allEvents(Model model,
        @RequestParam(name="page",defaultValue="0")int page,
        @RequestParam(name="size",defaultValue="3")int size) {
        Page<Event> allEvents = eventRepository.findAll(PageRequest.of(page, size));
         model.addAttribute("allEvents", allEvents.getContent());
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("numpages", new int[allEvents.getTotalPages()]);
        model.addAttribute("numPages", allEvents.getTotalPages());
        model.addAttribute("nbr", "allEvents");
        
        return "admin";
    }
     @GetMapping("/blackListUser")
    public String blackListUser(Model model, 
        @RequestParam(name="page",defaultValue="0")int page,
        @RequestParam(name="size",defaultValue="3")int size) {
        List<User> blackListUser = entityManager.createNamedQuery("User.blackList").getResultList();

        int total = blackListUser.size();
            int fromIndex = (page) * size;
            int toIndex = Math.min(fromIndex + size, total);

            List<User> pageList = blackListUser.subList(fromIndex, toIndex);
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<User>PageBlackListUsers = new PageImpl<>(pageList, pageRequest, total);
        model.addAttribute("blackListUser", PageBlackListUsers.getContent());
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("numPages", PageBlackListUsers.getTotalPages());
        model.addAttribute("numpages", new int[PageBlackListUsers.getTotalPages()]);
        model.addAttribute("nbr", "blackListUser");
        return "admin";
    }
     @GetMapping("/allReports")
    public String allReports(Model model, 
        @RequestParam(name="page",defaultValue="0")int page,
        @RequestParam(name="size",defaultValue="3")int size) {
        List<Report> allReports = entityManager.createNamedQuery("Reports.allReports").getResultList();

        int total = allReports.size();
            int fromIndex = (page) * size;
            int toIndex = Math.min(fromIndex + size, total);

            List<Report> pageList = allReports.subList(fromIndex, toIndex);
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Report>PageAllReports = new PageImpl<>(pageList, pageRequest, total);
        model.addAttribute("allReports", PageAllReports.getContent());
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("numPages", PageAllReports.getTotalPages());
        model.addAttribute("numpages", new int[PageAllReports.getTotalPages()]);
        model.addAttribute("nbr", "allReports");
        return "admin";
    }

    @GetMapping("/addUser")
    public String addUser(Model model){
        model.addAttribute("User", new User());
        return "addUser";
    }

    // @PostMapping("/addUser")
    // public String addUser(@ModelAttribute User user, Model model){
    //     User InsertUser = new User();
	// 	InsertUser.setFirstName(user.getFirstName());
	// 	InsertUser.setLastName(user.getLastName());
	// 	InsertUser.setPassword(encodePassword(user.getPassword()));
	// 	InsertUser.setUsername(user.getUsername());
	// 	InsertUser.setLocation(user.getLocation());
	// 	InsertUser.setDescription(user.getDescription());
	// 	InsertUser.setLanguages(user.getLanguages());
	// 	InsertUser.setEmail(user.getEmail());
	// 	InsertUser.setBirthdate(user.getBirthdate());
	// 	InsertUser.setRating(0F);
	// 	InsertUser.setStatus(Status.ACTIVE);
	// 	InsertUser.setRoles(Role.USER.name());
	// 	InsertUser.setEnabled(true);
	// 	InsertUser.setUserEvent(null);
	// 	InsertUser.setSent(null);
	// 	InsertUser.setLevel(Level.BRONZE);
	// 	InsertUser.setReceived(null);
	// 	entityManager.persist(InsertUser);
	// 	entityManager.flush();
	// 	model.addAttribute("User", InsertUser);
	// 	return "admin";
    // }
    
}
