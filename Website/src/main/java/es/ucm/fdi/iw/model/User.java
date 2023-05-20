
package es.ucm.fdi.iw.model;

import java.time.LocalDate;
import java.time.Period;
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
@NamedQueries({
        @NamedQuery(name="User.byUsername",
                query="SELECT u FROM User u "
                        + "WHERE u.username = :username AND u.enabled = TRUE"),
        @NamedQuery(name="User.hasUsername",
                query="SELECT COUNT(u) "
                        + "FROM User u "
                        + "WHERE u.username = :username"),
        @NamedQuery(name="User.allUsers",
                query="SELECT u FROM User u "
                        + "WHERE u.status != 'null'"),
        @NamedQuery(name="User.blackList",
                query="SELECT u FROM User u "
                        + "WHERE u.status = 'BLACK_LISTED'"),
        @NamedQuery(name="User.disabledUsers",
                query="SELECT u FROM User u "
                        + "WHERE u.enabled = FALSE")
        
})
@Table(name="IWUser")
public class User implements Transferable<User.Transfer> {

    public enum Role {
        USER,			// normal users 
        ADMIN,          // admin users
    }
    public enum Level {
        NONE,
        BRONZE,
        SILVER,
        GOLD
    }
    public enum Status {
        ACTIVE,
        //SUSPENDED,
        BLACK_LISTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    private String location;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    private Float rating;
    private String  languages;// split by ',' to separate languages
 
    @Enumerated(EnumType.STRING)
    private Level level;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(unique = true)
    private String email;

    private int numReports;

     /*
    //Eventos en los que ha participado antes(estan en estado finish)
    @OneToMany
    @JoinColumn(name = "old_events")
    private List<Event> OldEvents = new ArrayList<>();

    //Eventos que el user les tiene en favoritos
    @OneToMany
    @JoinColumn(name = "fav_events")
    private List<Event> favEvents = new ArrayList<>();
    */


    @OneToMany(mappedBy = "event")
    private List<UserEvent> userEvent = new ArrayList<>();

    private boolean enabled;
    private String roles; // split by ',' to separate roles User or Admin

	@OneToMany
	@JoinColumn(name = "sender_id")
	private List<Message> sent = new ArrayList<>();
	@OneToMany
	@JoinColumn(name = "receiver_id")	
	private List<Message> received = new ArrayList<>();		

    /**
     * Checks whether this user has a given role.
     * @param role to check
     * @return true iff this user has that role.
     */
    public boolean hasRole(Role role) {
        String roleName = role.name();
        return Arrays.asList(roles.split(",")).contains(roleName);
    }

    public int getAge(){
        LocalDate now = LocalDate.now();
        return Period.between(birthdate, now).getYears();
    }
 

    @Getter
    @AllArgsConstructor
    public static class Transfer {
		private long id;
        private String username;
		private int totalReceived;
		private int totalSent;
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(id,	username, received.size(), sent.size());
	}
	
	@Override
	public String toString() {
		// return toTransfer().toString();
        return "user-" + id + "-" + username;
	}

    public boolean getEnabled(){
        return this.enabled;
    }
}

