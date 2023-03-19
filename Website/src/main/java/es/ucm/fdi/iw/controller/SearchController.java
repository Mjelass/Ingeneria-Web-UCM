package es.ucm.fdi.iw.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.model.Event;

@Controller
public class SearchController {

 @Autowired
 private EntityManager entityManager;

 @Autowired
 private EventRepository eventRepository; 


public  List<Integer> splitdate(String date) {
   
    List<Integer> aux = new ArrayList<>();
    
                // Split the input string using space as the delimiter
                String[] words = date.split("-");
                for(int i=0;i<words.length;i++){
                        aux.add(Integer.parseInt(words[i]));
                }
        
    return aux;
}
 
 @GetMapping("/search")
 public String search(Model model,
         @RequestParam(name="page",defaultValue="0")int page,
         @RequestParam(name="size",defaultValue="3")int size,
         @RequestParam(name="search",defaultValue = "")String search,
         @RequestParam(name = "init", defaultValue = "") String init,
         @RequestParam(name = "fin", defaultValue = "") String fin
      
 ) {
        Page<Event> PageEvents;
        
       LocalDate initDate=LocalDate.of(1999,1,1);
       LocalDate finDate=LocalDate.of(3000,1,1);
       if(!init.isEmpty()){
        initDate=LocalDate.of(splitdate(init).get(0),splitdate(init).get(1),splitdate(init).get(2));
        finDate=LocalDate.of(splitdate(fin).get(0),splitdate(fin).get(1),splitdate(fin).get(2));
       }

       if(search.isEmpty()){
                PageEvents = eventRepository.findAll(PageRequest.of(page, size));
       }else {
        
        List<Event> ev = entityManager.createNamedQuery("Event.byTitle")
        .setParameter("x", "%"+search+"%")
        .setParameter("y", initDate)
        .setParameter("z", finDate).getResultList();
        
        int total = ev.size();
        int fromIndex = (page) * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<Event> pageList = ev.subList(fromIndex, toIndex);
        PageRequest pageRequest = PageRequest.of(page, size);
        PageEvents = new PageImpl<>(pageList, pageRequest, total);
        }
   model.addAttribute("allEvents",PageEvents.getContent());
   model.addAttribute("numpages", new int[PageEvents.getTotalPages()]);
   model.addAttribute("currentPage",page);
   model.addAttribute("search",search);
   model.addAttribute("init",init);
   model.addAttribute("fin",fin);
  return "search";
 }

}

