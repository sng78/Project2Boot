package io.github.sng78.controllers;

import io.github.sng78.models.Book;
import io.github.sng78.models.Person;
import io.github.sng78.services.BookService;
import io.github.sng78.services.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final PersonService personService;

    private final BookService bookService;

    public BookController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }

    @GetMapping
    public String index(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                        Model model) {

        if (page == null || booksPerPage == null) {
            model.addAttribute("books", bookService.findAll(sortByYear));
        } else {
            model.addAttribute("books", bookService.findAll(page, booksPerPage, sortByYear));
        }

        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, @ModelAttribute("person") Person person, Model model) {
        model.addAttribute("book", bookService.findById(id));

        Optional<Person> owner = bookService.getOwner(id);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        } else {
            model.addAttribute("people", personService.findAll());
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/busy")
    public String setBusy(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.setBusy(id, person);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/free")
    public String setFree(@PathVariable("id") int id) {
        bookService.setFree(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam(value = "search_string", required = false) String searchString, Model model) {
        List<Book> books = bookService.searchByTitle(searchString);
        model.addAttribute("books", books);
        return "books/search";
    }
}
