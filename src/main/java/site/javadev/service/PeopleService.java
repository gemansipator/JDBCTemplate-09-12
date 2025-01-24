package site.javadev.service;

import site.javadev.model.PersonSecurity;
import site.javadev.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void savePerson(PersonSecurity personSecurity) {

        personSecurity.setPassword(passwordEncoder.encode(personSecurity.getPassword()));
        personSecurity.setRole("ROLE_USER");

        peopleRepository.save(personSecurity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void doAdminSomething() {
        System.out.println("Admin is doing something");
    }

}
