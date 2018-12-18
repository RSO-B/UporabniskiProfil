package dtos;

import java.util.List;

public class Album {
    private Integer id;

    private String naslov;

    private String opis;

    private List<Slika> slikeList;

    private String uporabnik_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public List<Slika> getSlikeList() {
        return slikeList;
    }

    public void setSlikeList(List<Slika> slikeList) {
        this.slikeList = slikeList;
    }

    public String getUporabnik_id() {
        return uporabnik_id;
    }

    public void setUporabnik_id(String uporabnik_id) {
        this.uporabnik_id = uporabnik_id;
    }
}
