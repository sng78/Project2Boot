package io.github.sng78.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Size(min = 1, max = 128, message = "Введите от 1 до 128 символов")
    @Column(name = "title")
    private String title;

    @NotBlank
    @Size(min = 2, max = 128, message = "Введите от 2 до 128 символов")
    @Column(name = "author")
    private String author;

    @NotNull
    @Min(value = 1001, message = "Год должен быть больше 1000")
    @Column(name = "year_of_publication")
    private int yearOfPublication;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Column(name = "was_taken_in")
    private LocalDateTime wasTakenIn;

    @Transient
    private boolean isExpired;

    public Book() {
    }

    public Book(int id, String title, String author, int yearOfPublication) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDateTime getWasTakenIn() {
        return wasTakenIn;
    }

    public void setWasTakenIn(LocalDateTime wasTakenIn) {
        this.wasTakenIn = wasTakenIn;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
