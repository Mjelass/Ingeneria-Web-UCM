package es.ucm.fdi.iw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.ui.Model;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;

import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.Repositories.MessageRepository;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.User;

@Controller
@RequestMapping("chat")
public class MessageController {
    
    private static final Logger log = LogManager.getLogger(MessageController.class);
    @Autowired
	private EntityManager entityManager;

    @Autowired
	private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EventRepository eventRepository;

	@GetMapping("")
    public String index(Model model, HttpSession session) {
        User u = (User) session.getAttribute("u");
        ArrayList<Event> events = eventRepository.getUserJoinedEvents(u.getId());
        HashMap<Long, ArrayList<Message>> chats = new HashMap<>();
        for (Event e: events) {
            chats.put(e.getId(), messageRepository.getMsgFromChat(e.getId()));
        }
        model.addAttribute("userId", u.getId());
        model.addAttribute("events", events);
        model.addAttribute("chats", chats);
        return "chat";
    }

    // Update userEvent table
    @PostMapping("{id}/sendMsg")
    @ResponseBody
    @Transactional
    public String updateUserEvent(@PathVariable long id, Model model, HttpSession session,
            @RequestBody Message msg) throws JsonProcessingException {
        User u = (User) session.getAttribute("u");
        Event e = (Event) entityManager.find(Event.class, id);
        if (u == null || e == null) {
            throw new IllegalArgumentException();
        }
        msg.setSender(u);
        msg.setEvent(e);
        msg.setDateSent(LocalDateTime.now());
        messageRepository.save(msg);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msg.toTransfer());
        log.info("Sending a message to {} with contents '{}'", id, json);
        messagingTemplate.convertAndSend("/sendMsg/chat/" + e.getId(), json);
        return "ok";
    }
}
