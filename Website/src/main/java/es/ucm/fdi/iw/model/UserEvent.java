
package es.ucm.fdi.iw.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * An authorized user of the system.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    // @Id
    // private Long userId;
    // @Id
    // private Long eventId;
    // private 

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private Long id;

    // @Id
    // @ManyToOne
    // @MapsId("userId")
    // private User user;

    // @Id
    // @ManyToOne
    // @MapsId("eventId")
    // private Event event;
    
    private Boolean fav;

    private Boolean joined;

    private String rol; // separated by ,
    
}

