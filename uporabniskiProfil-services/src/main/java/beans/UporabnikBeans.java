package beans;

import entities.Uporabnik;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class UporabnikBeans {

    private final static Logger LOGGER = Logger.getLogger(UporabnikBeans.class.getName());

    @Inject
    private EntityManager em;

    @PostConstruct
    public void start() {
        LOGGER.log(Level.INFO, "Ustvarjanje UporabnikZrno zrno");
    }

    @PreDestroy
    public void reset() {
        LOGGER.log(Level.INFO, "Uničenje UporabnikZrno zrno");
    }

    public List<Uporabnik> getUporabnikList() {

        TypedQuery<Uporabnik> query = em.createNamedQuery("uporabnik.getAll", Uporabnik.class);

        return query.getResultList();

    }

    public Uporabnik getUporabnik(Integer id) {

        Uporabnik uporabnik = em.find(Uporabnik.class, id);

        if (uporabnik == null) {
            throw new NotFoundException();
        }

        return uporabnik;
    }

    public Uporabnik createUporabnik(Uporabnik uporabnik) {

        try {
            beginTx();
            em.persist(uporabnik);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return uporabnik;
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
