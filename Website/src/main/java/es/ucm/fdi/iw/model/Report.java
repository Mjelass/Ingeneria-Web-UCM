package es.ucm.fdi.iw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rep")
    @SequenceGenerator(name = "rep", sequenceName = "rep", initialValue = 11, allocationSize = 1)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_source", referencedColumnName = "id")
    private User userSource;
    @ManyToOne
    @JoinColumn(name = "user_target", referencedColumnName = "id")
    private User userTarget;
    @Column(columnDefinition = "TEXT")
    private String description;
}
