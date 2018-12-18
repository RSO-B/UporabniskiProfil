package beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import configuration.AppProperties;
import dtos.Album;
import dtos.Slika;
import entities.Uporabnik;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class UporabnikBeans {

    private Logger log = Logger.getLogger(UporabnikBeans.class.getName());

    @PersistenceContext(unitName = "uporabnik-jpa")
    private EntityManager em;

    private Client httpClient;

    @Inject
    private AppProperties appProperties;

    @Inject
    @DiscoverService("Albumi")
    private Optional<String> baseUrl;

    @PostConstruct
    public void start() {
        httpClient = ClientBuilder.newClient();
    }

    @PreDestroy
    public void reset() {

            log.log(Level.INFO, "Uničenje AlbumBeans zrno");
    }

    public List<Uporabnik> getUporabnikList(QueryParameters query) {

        List<Uporabnik> uporabnikList  = JPAUtils.queryEntities(em, Uporabnik.class, query);

        for (Uporabnik upo : uporabnikList) {
            upo.setAlbumList(getAlbumList(upo.getId()));
        }

        return uporabnikList;
    }
    public Long getUporabnikCount(QueryParameters query) {

        return JPAUtils.queryEntitiesCount(em, Uporabnik.class, query);
    }

    public Uporabnik getUporabnik(Integer id) {

        Uporabnik uporabnik = em.find(Uporabnik.class, id);

        if (uporabnik == null) {
            throw new NotFoundException();
        }

        List<Album> albumList = getAlbumList(uporabnik.getId());
        uporabnik.setAlbumList(albumList);

        return uporabnik;
    }

    @CircuitBreaker(requestVolumeThreshold = 3)
    @Timeout(value = 2,unit = ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getAlbumFallBack")
    public List<Album> getAlbumList(Integer upo_id) {

        if (appProperties.isExternalServicesEnabled() && !appProperties.getUrlAlbum().isEmpty() /*&& baseUrl.isPresent()*/) {
            try {
                return httpClient
                        .target(appProperties.getUrlAlbum() + "/v1/album?filter=uporabnik_id:EQ:" + upo_id)
                        .request().get(new GenericType<List<Album>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.severe(e.getMessage());
                throw new InternalServerErrorException(e);
            }
        }

        return null;

    }

    public List<Slika> getAlbumFallBack(Integer upo_id){
        return Collections.emptyList();
    }

    @Transactional
    public void createUporanmik(Uporabnik a) {
        if (a != null) {
            em.persist(a);
        }
    }

    public Uporabnik putUporabnik(String id, Uporabnik uporabnik) {

        Uporabnik c = em.find(Uporabnik.class, id);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            uporabnik.setId(c.getId());
            uporabnik = em.merge(uporabnik);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return uporabnik;
    }

    public boolean deleteUporabnik(String id) {

        Uporabnik uporabnik = em.find(Uporabnik.class, id);

        if (uporabnik != null) {
            try {
                beginTx();
                em.remove(uporabnik);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }


    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }

    public void loadOrder(Integer n) {

    }
}
