package com.example;
import java.time.LocalDate;

public class Osoba {
    private Integer id;
    private String ime;
    private String prezime;
    private String adresa;
    private LocalDate datumRodjenja;
    private String maticniBroj;
    private Uloga uloga;

    // Konstruktori
    public Osoba() {}

    public Osoba(Integer id, String ime, String prezime, String adresa,
                 LocalDate datumRodjenja, String maticniBroj, Uloga uloga) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.adresa = adresa;
        this.datumRodjenja = datumRodjenja;
        this.maticniBroj = maticniBroj;
        this.uloga = uloga;
    }

    // Getters i Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }

    public LocalDate getDatumRodjenja() { return datumRodjenja; }
    public void setDatumRodjenja(LocalDate datumRodjenja) { this.datumRodjenja = datumRodjenja; }

    public String getMaticniBroj() { return maticniBroj; }
    public void setMaticniBroj(String maticniBroj) { this.maticniBroj = maticniBroj; }

    public Uloga getUloga() { return uloga; }
    public void setUloga(Uloga uloga) { this.uloga = uloga; }

    @Override
    public String toString() {
        return id + " - " + ime + " " + prezime + " (" + uloga + ")";
    }
}