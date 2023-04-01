package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * A message that users can send each other.
 *
 */
@Entity
// @NamedQueries({
// 	@NamedQuery(name="Message.countUnread",
// 	query="SELECT COUNT(m) FROM Message m "
// 			+ "WHERE m.receiver.id = :userId AND m.dateRead = null")
// })
@Data
public class Message implements Transferable<Message.Transfer> {
	
	private static Logger log = LogManager.getLogger(Message.class);	
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "msg")
    @SequenceGenerator(name = "msg", sequenceName = "msg" , initialValue = 1)
	private long id;
	@ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
	private User sender;
	// @ManyToOne
    // @JoinColumn(name = "event_id", referencedColumnName = "id")
	// private User receiver;
	@ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
	private Event event;

	private String text;
	
	private LocalDateTime dateSent;
	// private LocalDateTime dateRead;
	
	/**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 */
    @Getter
    @AllArgsConstructor
	public static class Transfer {
		// private String from;
		// private String to;
		// private String sent;
		// private String received;
		// private String text;
		private String senderId;
		private String sender;
		private String event;
		private String text;
		private String dateSent;
		long id;
		public Transfer(Message m) {
			this.senderId = String.valueOf(m.getSender().getId());
			this.sender = m.getSender().getUsername();
			this.event = String.valueOf(m.getEvent().getId());
			// this.to = m.getReceiver().getUsername();
			this.dateSent = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateSent());
			// this.received = m.getDateRead() == null ?
			// 		null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateRead());
			this.text = m.getText();
			this.id = m.getId();
		}
	}

	@Override
	public Transfer toTransfer() {
		// return new Transfer(sender.getUsername(), receiver.getUsername(), 
		// 	DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateSent),
		// 	dateRead == null ? null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateRead),
		// 	text, id
        // );
		return new Transfer(this);
    }
}
