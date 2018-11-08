package uporabniks;

import javax.persistence.*;

@Entity(name = "uporabnik")
@NamedQueries(value =
        {
                @NamedQuery(name = "uporabnik.getAll", query = "SELECT u FROM uporabnik u"),
                @NamedQuery(name = "uporabnik.getId", query = "SELECT u FROM uporabnik u WHERE u.id = :id"),
                @NamedQuery(name = "uporabnik.geUpoIme", query = "SELECT u FROM uporabnik u WHERE u.upoIme = :upoIme")
        })
public class Uporabnik {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        private String ime;

        private String priimek;

        private String email;

        private String upoIme;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUpoIme() {
        return upoIme;
    }

    public void setUpoIme(String upoIme) {
        this.upoIme = upoIme;
    }

    public Uporabnik(String ime, String priimek, String email, String upoIme) {
        this.ime = ime;
        this.priimek = priimek;
        this.email = email;
        this.upoIme = upoIme;
    }

    public Uporabnik(Uporabnik u ) {
        this.ime = u.ime;
        this.priimek = u.priimek;
        this.email = u.email;
        this.upoIme = u.upoIme;
    }

    public Uporabnik() {
    }

    @Override
    public String toString() {
        return "Uporabnik{" +
                ", ime='" + ime + '\'' +
                ", priimek='" + priimek + '\'' +
                ", email='" + email + '\'' +
                ", upoIme='" + upoIme + '\'' +
                '}';
    }
}
