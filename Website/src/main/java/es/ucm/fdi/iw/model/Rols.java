package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rols {

 public enum RoleTrip {
  RESPONSIBLE_FOOD,
  RESPONSIBLE_ACCOMODATION,
  RESPONSIBLE_TRANSPORT
 }

 @Id
 @GeneratedValue
 private Long Id;
 @ManyToOne
 private User userParticipant;
 private RoleTrip userRol;
}
