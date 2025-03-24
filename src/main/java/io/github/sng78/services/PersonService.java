package io.github.sng78.services;

import io.github.sng78.models.Book;
import io.github.sng78.models.Person;
import io.github.sng78.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Person findById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Optional<Person> findByFullName(String fullName) {
        return repository.findByFullName(fullName);
    }

    @Transactional
    public void save(Person person) {
        repository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        repository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = repository.findById(id);
        if (person.isPresent()) {
            // если в методе нет логики, происходит ленивая инициализация, поэтому вызываем ее принудительно
//            Hibernate.initialize(person.get().getBooks());
            List<Book> books = person.get().getBooks();
            books.stream()
                    .filter(book -> ChronoUnit.DAYS.between(book.getWasTakenIn().toLocalDate(), LocalDate.now()) > 10)
                    .forEach(book -> book.setExpired(true));
            return books;
        }
        return Collections.emptyList();
    }
}
