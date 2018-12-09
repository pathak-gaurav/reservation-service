package com.gaurav.reservationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Stream;

@SpringBootApplication
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
}

@Component
class SampleDataCLR implements CommandLineRunner {

    private final ReservationRepository reservationRepository;

    @Autowired
    public SampleDataCLR(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Stream.of("Zack", "Gaurav", "Cody", "Zoella", "Elizabeth", "Charles", "William", "George")
                .forEach(x -> reservationRepository.save(new Reservation(x)));

        reservationRepository.findAll().forEach(System.out::println);
        reservationRepository.findByReservationName("Elizabeth").forEach(System.out::println);
    }
}

@Component
class CustomHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        return Health.status("This is Gaurav Pathak Service's").build();
    }
}


/*@RestController
class ReservationRestController {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationRestController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations")
    public Collection<Reservation> findAll(Model model) {
        return reservationRepository.findAll();
    }
}*/

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @RestResource(path = "by-name")
    Collection<Reservation> findByReservationName(@Param("rn") String rn);
}

@Entity
class Reservation {

    @Id
    @GeneratedValue
    private Long id;
    private String reservationName;

    public Reservation() {
    }

    public Reservation(String reservationName) {
        this.reservationName = reservationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationName='" + reservationName + '\'' +
                '}';
    }
}
