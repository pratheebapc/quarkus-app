package org.telemeter.Rest;

import java.util.List;

import org.telemeter.Entity.Person;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    EntityManager em;

    @GET
    public List<Person> getAllPersons() {
        return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public Person getPersonById(@PathParam("id") Long id) {
        return em.find(Person.class, id);
    }

    @POST
    @Transactional
    public void createPerson(Person person) {
    em.merge(person);
    }


    @PUT
    @Path("/{id}")
    @Transactional
    public void updatePerson(@PathParam("id") Long id, Person updatedPerson) {
        Person person = em.find(Person.class, id);
        if (person != null) {
            person.setName(updatedPerson.getName());
            person.setAge(updatedPerson.getAge());
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deletePerson(@PathParam("id") Long id) {
        Person person = em.find(Person.class, id);
        if (person != null) {
            em.remove(person);
        }
    }
}