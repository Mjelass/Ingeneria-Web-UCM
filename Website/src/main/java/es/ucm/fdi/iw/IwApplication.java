package es.ucm.fdi.iw;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import es.ucm.fdi.iw.Repositories.EventRepository;
import es.ucm.fdi.iw.Repositories.UserRepository;
import es.ucm.fdi.iw.model.Event;
import es.ucm.fdi.iw.model.User;

@SpringBootApplication
public class IwApplication implements CommandLineRunner {

 @Autowired
	private EventRepository eventRepository;
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(IwApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Aqui es para probar a insertar en la base de datos
		// User user1 = new User(1,"a","{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W","ff","ll","Madrid",LocalDate.now(),"me gusta viajar",8L,"muchos",null,null,null,null, true,"ADMIN,USER",null,null);

		// User user2 = new User(2,"b","{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W","ff","ll","Madrid",LocalDate.now(),"me gusta viajar",8L,"muchos",null,null,null, null, true,"USER",null,null);

		// User user3 = new User(4,"userTest3","pass","ff","ll","Madrid",LocalDate.now(),"me gusta viajar",8L,"muchos",null,null,null, null, true,"USER",null,null);

		// userRepository.save(user1);
		// userRepository.save(user2);
		// userRepository.save(user3);

		// //Crear un objeto Event
		// Event xx = new Event(1L,"New York",LocalDate.of(2023,04,28),LocalDate.of(2023,05,28), "thats amazing trip", 150, 5, 3,"ALLOW_CHILDREN,ALLOW_PETS","CULTURAL,PARTY",user1,null,null);
		// //poner el evento en la base de datos
		// eventRepository.save(xx);
		// Event pp = eventRepository.findById(1L).get();
		// System.out.println("********"+pp.getTitle()+"***************");
		// System.out.println("********"+pp.getDescription()+"***************");
	
    
				
	}

}
