package es.ucm.fdi.iw.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {


    public enum Status {
      OPEN,
      CLOSE,
      FINISH
    }

    public enum Type {
      TRIP,
      CONCERT,
      CAMPING,
      MUSEUM
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate initDate;
    private LocalDate finishDate;
    private String description;
    private int price;
    private int capacity;
    private int occupied;

    
    private String notes; // split by ',' to separate notes
    @Enumerated(EnumType.STRING)
    private Type type; 

      
    @OneToOne
    private User userOwner;
  
    // //Lista de los participantes del viaje
    // @OneToMany
    // @JoinColumn(name = "EventParts")
    // private List<User> participants = new ArrayList<>();

    // @OneToMany
    // @JoinColumn(name = "rolsParts")
    // private List<Rols> rolsParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserEvent> userEvent = new ArrayList<>();
  
}
