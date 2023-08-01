package org.telemeter.Rest;

import java.util.List;
import org.jboss.logging.Logger;
import org.jboss.logmanager.LogManager;

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
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    EntityManager em;

    private static final Logger logger = Logger.getLogger(PersonResource.class.getName());

    @GET
    public List<Person> getAllPersons() {
        logger.info("Getting all persons.");
        return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") Long id) {
        logger.info("Getting person by ID: " + id);
        Person person = em.find(Person.class, id);
        if (person != null) {
            return Response.ok(person).build();
        } else {
            logger.error("Person with ID " + id + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Transactional
    public Response createPerson(Person person) {
        // Add input validation here if required
        logger.info("Creating person: " + person);
        em.merge(person);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/createError")
    @Transactional
    public Response createPersonError(Person person) {
        // Input validation: Check if name and age are not null, and age is positive
        if (person.getName() == null || person.getAge() <= 0) {
            logger.error("Invalid input for creating person: " + person);
            throw new WebApplicationException("Invalid input for creating person.",
                    Response.Status.INTERNAL_SERVER_ERROR);
        }

        logger.info("Creating person: " + person);
        em.merge(person);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePerson(@PathParam("id") Long id, Person updatedPerson) {
        logger.info("Updating person with ID: " + id);
        Person person = em.find(Person.class, id);
        if (person != null) {
            // Add input validation for updatedPerson if required
            person.setName(updatedPerson.getName());
            person.setAge(updatedPerson.getAge());
            em.merge(person); // Persist the changes
            return Response.ok().build();
        } else {
            logger.error("Person with ID " + id + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePerson(@PathParam("id") Long id) {
        logger.info("Deleting person with ID: " + id);
        Person person = em.find(Person.class, id);
        if (person != null) {
            em.remove(person);
            return Response.noContent().build();
        } else {
            logger.error("Person with ID " + id + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
