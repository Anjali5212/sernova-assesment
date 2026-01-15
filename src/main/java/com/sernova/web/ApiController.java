package com.sernova.web;

import com.sernova.domain.Address;
import com.sernova.domain.Person;
import com.sernova.domain.PersonRepository;
import com.sernova.dto.PersonAddressDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PersonRepository personRepository;

    public ApiController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from Sernova test API");
    }

    @GetMapping("/persons")
    public ResponseEntity<List<Person>> persons() {
        return ResponseEntity.ok(personRepository.findAll());
    }

    @GetMapping("/persons-with-addresses")
    public ResponseEntity<Page<PersonAddressDto>> personsWithAddresses(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(personRepository.findPersonsWithAddresses(PageRequest.of(page, size)));
    }

    @PostMapping("/seed/people")
    public ResponseEntity<String> seedPeople() {
        // Insert 10,000 persons with 2 addresses each. Simple batching to reduce memory usage.
        int total = 10_000;
        int batchSize = 500;
        List<Person> batch = new ArrayList<>(batchSize);
        for (int i = 1; i <= total; i++) {
            Person p = new Person();
            p.setFirstName("First" + i);
            p.setLastName("Last" + i);

            Address home = new Address();
            home.setLine1("Home Street " + i);
            home.setCity("City" + (i % 100));
            home.setCountry("Country");
            home.setType("HOME");
            p.addAddress(home);

            Address work = new Address();
            work.setLine1("Work Ave " + i);
            work.setCity("City" + (i % 100));
            work.setCountry("Country");
            work.setType("WORK");
            p.addAddress(work);

            batch.add(p);

            if (batch.size() == batchSize) {
                personRepository.saveAll(batch);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) {
            personRepository.saveAll(batch);
        }
        return ResponseEntity.ok("Seeded " + total + " persons with 2 addresses each");
    }
}
