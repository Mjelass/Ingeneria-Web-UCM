package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ucm.fdi.iw.model.Event;

@Controller
public class SearchController {

 @Autowired
 private EntityManager entityManager;
 
 @GetMapping("/search")
 public String search(Model model){
  model.addAttribute("allEvents", entityManager
                  .createQuery("select e from Event e join e.userOwner u", Event.class) 
                  .getResultList());
  return "search";
 }
}
