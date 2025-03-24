package io.github.sng78.services;

import io.github.sng78.models.Book;
import io.github.sng78.models.Person;
import io.github.sng78.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (!sortByYear) {
            return repository.findAll();
        }
        return repository.findAll(Sort.by("yearOfPublication"));
    }

    public List<Book> findAll(int page, int booksPerPage, boolean sortByYear) {
        if (!sortByYear) {
            return repository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        }
        return repository
                .findAll(PageRequest.of(page, booksPerPage, Sort.by("yearOfPublication")))
                .getContent();
    }

    public Book findById(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<Book> searchByTitle(String searchString) {
        return repository.findByTitleStartingWith(searchString);
    }

    @Transactional
    public void save(Book book) {
        repository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book book = repository.findById(id).orElse(null);
        updatedBook.setId(id);
        updatedBook.setPerson(book != null ? book.getPerson() : null);
        repository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    public Optional<Person> getOwner(int id) {
        return repository.findById(id).map(Book::getPerson);
    }

    @Transactional
    public void setBusy(int id, Person person) {
        repository.findById(id).ifPresent(book -> {
            book.setPerson(person);
            book.setWasTakenIn(LocalDateTime.now());
        });
    }

    @Transactional
    public void setFree(int id) {
        repository.findById(id).ifPresent(book -> {
            book.setPerson(null);
            book.setWasTakenIn(null);
            book.setExpired(false);
        });
    }
}
