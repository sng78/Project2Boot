package io.github.sng78.util;

import io.github.sng78.models.Person;
import io.github.sng78.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PersonService service;

    @Autowired
    public PersonValidator(PersonService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        Optional<Person> serviceFindByFullName = service.findByFullName(person.getFullName());

        if (serviceFindByFullName.isPresent() && serviceFindByFullName.get().getId() != person.getId()) {
            errors.rejectValue("fullName", "", "Человек с таким ФИО существует");
        }
    }
}
