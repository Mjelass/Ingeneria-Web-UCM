package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.model.Event;

@Controller
public class SearchController {

 @Autowired
 private EntityManager entityManager;

 @Autowired
 private EventRepository eventRepository; 
 
 @GetMapping("/search")
 public String search(Model model,
         @RequestParam(name="page",defaultValue="0")int page,
         @RequestParam(name="size",defaultValue="3")int size
 ){
  Page<Event> PageEvents = eventRepository.findAll(PageRequest.of(page, size));
   model.addAttribute("allEvents",PageEvents.getContent());
   model.addAttribute("numpages", new int[PageEvents.getTotalPages()]);
   model.addAttribute("currentPage",page);
  return "search";
 }
}
