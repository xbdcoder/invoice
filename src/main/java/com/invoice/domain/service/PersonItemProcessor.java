package com.invoice.domain.service;

import com.invoice.model.Person;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) throws Exception {
        String firstName = person.getFirstName().toUpperCase();
        String lastName = person.getLastName().toUpperCase();

        return new Person(firstName, lastName);
    }
}