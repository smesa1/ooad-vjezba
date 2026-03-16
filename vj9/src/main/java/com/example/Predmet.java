package com.example;

public class Predmet {
    private Integer id;
    private String naziv;
    private String sifra;
    private Integer espb;
    private String opis;

    // Konstruktori
    public Predmet() {}

    public Predmet(Integer id, String naziv, String sifra, Integer espb, String opis) {
        this.id = id;
        this.naziv = naziv;
        this.sifra = sifra;
        this.espb = espb;
        this.opis = opis;
    }

    // Getters i Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getSifra() { return sifra; }
    public void setSifra(String sifra) { this.sifra = sifra; }

    public Integer getEspb() { return espb; }
    public void setEspb(Integer espb) { this.espb = espb; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    @Override
    public String toString() {
        return id + " - " + sifra + ": " + naziv + " (" + espb + " ESPB)";
    }
}
