package es.ucm.fdi.iw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;

import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.Repositories.MessageRepository;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.UserEvent;
import es.ucm.fdi.iw.model.UserEventId;

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

    private static final int MSG_PAGE = 20;

	@GetMapping("")
    public String index(Model model, HttpSession session) {
        User u = (User) session.getAttribute("u");
        ArrayList<Event> events = eventRepository.getEventsJoined(u.getId());
        HashMap<Long, ArrayList<Message>> chats = new HashMap<>();
        for (Event e: events) {
            ArrayList<Message> msgs = new ArrayList<>(messageRepository.getMsgFromChat(e.getId(), 
                PageRequest.of(0, MSG_PAGE)).getContent());
            Collections.reverse(msgs);
            chats.put(e.getId(), msgs);
            // chats.put(e.getId(), messageRepository.getMsgFromChat(e.getId()));
        }
        model.addAttribute("userId", u.getId());
        model.addAttribute("events", events);
        model.addAttribute("chats", chats);
        model.addAttribute("maxMsgPage", MSG_PAGE);
        return "chat";
    }

    @PostMapping("{id}/loadMsgs/{date}")
    public ResponseEntity<?> loadMsgs(@PathVariable long id, @PathVariable String date, Model model, HttpSession session) {
        LocalDateTime beforeDate = LocalDateTime.parse(date);
        User u = (User) session.getAttribute("u");
        UserEventId ueId = new UserEventId();
        ueId.setEvent(id);
        ueId.setUser(u.getId());
        UserEvent ue = entityManager.find(UserEvent.class, ueId);
        if (ue == null){
            return ResponseEntity.ok("Invalid Request");
        }
        return ResponseEntity.ok(convertToResponse(messageRepository.getMsgFromChatBefore(id, beforeDate, PageRequest.of(0, MSG_PAGE))));
    }

    // Create a new message in chat {id}
    @PostMapping("{id}/sendMsg")
    @ResponseBody
    @Transactional
    public String sendMsg(@PathVariable long id, Model model, HttpSession session,
            @RequestBody Message msg) throws JsonProcessingException {
        User u = (User) session.getAttribute("u");
        Event e = (Event) entityManager.find(Event.class, id);
        // Search if user logged is in event with id=id.
        UserEventId ueId = new UserEventId();
        ueId.setEvent(id);
        ueId.setUser(u.getId());
        UserEvent ue = entityManager.find(UserEvent.class, ueId);
        
        if (u == null || e == null || ue == null) {
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


    private Map<String, Object> convertToResponse(final Page<Message> pageMsgs) {
        Map<String, Object> response = new HashMap<>();
        List<Message.Transfer> msgs = new ArrayList<>();
        for (var m : pageMsgs.getContent()) {
            msgs.add(m.toTransfer());
        }
        response.put("msgs", msgs);
        response.put("page-items", pageMsgs.getNumberOfElements());
        response.put("current-page", pageMsgs.getNumber());
        response.put("total-items", pageMsgs.getTotalElements());
        response.put("total-pages", pageMsgs.getTotalPages());
        return response;
    }
}
