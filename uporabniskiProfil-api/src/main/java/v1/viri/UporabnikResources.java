package v1.viri;

import beans.UporabnikBeans;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import entities.Uporabnik;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
@Path("uporabniki")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UporabnikResources {

    @Inject
    private UporabnikBeans uporabnikBeans;

    @Context
    private UriInfo uriInfo;

    @GET
    public Response getUporabnikList(){
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();

        List<Uporabnik> uporabnikList = uporabnikBeans.getUporabnikList(query);
        Long count = uporabnikBeans.getUporabnikCount(query);
        return Response.ok(uporabnikList).header("X-Total-Count", count).build();
    }


    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") Integer id) {

        Uporabnik uporabnik = uporabnikBeans.getUporabnik(id);

        if (uporabnik == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(uporabnik).build();
    }

    @POST
    public Response createCustomer(Uporabnik uporabnik) {

        if ((uporabnik.getFirstName() == null || uporabnik.getFirstName().isEmpty()) || (uporabnik.getLastName() == null
                || uporabnik.getLastName().isEmpty())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            uporabnikBeans.createUporanmik(uporabnik);
        }

        if (uporabnik.getId() != null) {
            return Response.status(Response.Status.CREATED).entity(uporabnik).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(uporabnik).build();
        }
    }
    @PUT
    @Path("{id}")
    public Response putZavarovanec(@PathParam("id") String id, Uporabnik uporabnik) {

        uporabnik = uporabnikBeans.putUporabnik(id, uporabnik);

        if (uporabnik == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (uporabnik.getId() != null)
                return Response.status(Response.Status.OK).entity(uporabnik).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteCustomer(@PathParam("id") String id) {

        boolean deleted = uporabnikBeans.deleteUporabnik(id);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
