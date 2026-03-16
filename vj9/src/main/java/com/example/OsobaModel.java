package com.example;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OsobaModel {
    private static final String DB_URL = "jdbc:sqlite:starabaza.db";

    // Pomoćna metoda za konekciju
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Kreiranje tabele
    public static void kreirajTabeluOsoba() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Osoba (
                id INTEGER PRIMARY KEY,
                ime TEXT NOT NULL,
                prezime TEXT NOT NULL,
                adresa TEXT,
                datumRodjenja TEXT,
                maticniBroj TEXT,
                uloga TEXT
            );
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela Osoba je kreirana!");
        } catch (SQLException e) {
            System.out.println("Greška pri kreiranju tabele Osoba: " + e.getMessage());
        }
    }

    // ZADATAK 1: Brisanje osobe po ID-u
    public static String obrisiOsobuPold(Integer id) {
        String sql = "DELETE FROM Osoba WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int brojObrisanih = pstmt.executeUpdate();

            if (brojObrisanih > 0) {
                return "Osoba je uspjesno obrisana";
            } else {
                return "Ne postoji osoba sa datim id-em";
            }
        } catch (SQLException e) {
            return "SQL greška: " + e.getMessage();
        }
    }

    // ZADATAK 1: Provjera validnosti imena i matičnog broja
    private static boolean validirajIme(String ime) {
        // Ime mora imati najmanje 2 karaktera
        return ime != null && ime.length() >= 2;
    }

    private static boolean validirajMaticniBrojSaDatumom(String maticniBroj, LocalDate datumRodjenja) {
        if (maticniBroj == null || datumRodjenja == null) {
            return false;
        }

        // Provjera da li matični broj počinje sa datumom rođenja (ddmmyyyy)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String datumString = datumRodjenja.format(formatter);

        return maticniBroj.startsWith(datumString);
    }

    // ZADATAK 1: Ažuriranje osobe sa provjerama
    public static String azurirajOsobu(Integer id, String novoIme, String novoPrezime,
                                       String novaAdresa, LocalDate noviDatumRodjenja,
                                       String noviMaticniBroj, Uloga novaUloga) {

        StringBuilder upit = new StringBuilder("UPDATE Osoba SET ");
        boolean imaPromjene = false;
        List<Object> parametri = new ArrayList<>();

        // Provjera i dodavanje imena
        if (novoIme != null && !novoIme.isEmpty()) {
            if (!validirajIme(novoIme)) {
                System.out.println("Ime nije validno (min 2 karaktera) - polje se ne mijenja");
                // Tretiramo kao da korisnik ne želi mijenjati ovo polje
            } else {
                upit.append("ime = ?, ");
                parametri.add(novoIme);
                imaPromjene = true;
            }
        }

        // Provjera i dodavanje prezimena
        if (novoPrezime != null && !novoPrezime.isEmpty()) {
            upit.append("prezime = ?, ");
            parametri.add(novoPrezime);
            imaPromjene = true;
        }

        // Dodavanje adrese
        if (novaAdresa != null && !novaAdresa.isEmpty()) {
            upit.append("adresa = ?, ");
            parametri.add(novaAdresa);
            imaPromjene = true;
        }

        // Provjera matičnog broja sa datumom rođenja
        if (noviDatumRodjenja != null && noviMaticniBroj != null && !noviMaticniBroj.isEmpty()) {
            if (!validirajMaticniBrojSaDatumom(noviMaticniBroj, noviDatumRodjenja)) {
                System.out.println("Matični broj se ne poklapa sa datumom rođenja - polje se ne mijenja");
                // Ne dodajemo ova polja
            } else {
                upit.append("datumRodjenja = ?, ");
                parametri.add(noviDatumRodjenja.toString());

                upit.append("maticniBroj = ?, ");
                parametri.add(noviMaticniBroj);
                imaPromjene = true;
            }
        } else {
            // Ako se šalje samo jedno od ova dva polja
            if (noviDatumRodjenja != null) {
                upit.append("datumRodjenja = ?, ");
                parametri.add(noviDatumRodjenja.toString());
                imaPromjene = true;
            }

            if (noviMaticniBroj != null && !noviMaticniBroj.isEmpty()) {
                upit.append("maticniBroj = ?, ");
                parametri.add(noviMaticniBroj);
                imaPromjene = true;
            }
        }

        // Dodavanje uloge
        if (novaUloga != null) {
            upit.append("uloga = ?, ");
            parametri.add(novaUloga.name());
            imaPromjene = true;
        }

        // Ako nema promjena
        if (!imaPromjene) {
            return "Sva polja su ista kao i prije!";
        }

        // Uklanjanje zadnjeg zareza i razmaka
        upit.delete(upit.length() - 2, upit.length());
        upit.append(" WHERE id = ?");
        parametri.add(id);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(upit.toString())) {

            // Postavljanje parametara
            for (int i = 0; i < parametri.size(); i++) {
                pstmt.setObject(i + 1, parametri.get(i));
            }

            int promijenjeniRedovi = pstmt.executeUpdate();

            if (promijenjeniRedovi > 0) {
                return "Osoba je uspjesno azurirana";
            } else {
                return "Ne postoji osoba sa datim id-em";
            }
        } catch (SQLException e) {
            return "SQL greška: " + e.getMessage();
        }
    }

    // Dodavanje početnih podataka
    public static void dodajPocetnePodatke() {
        String sql = """
            INSERT INTO Osoba (id, ime, prezime, adresa, datumRodjenja, maticniBroj, uloga)
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Prva osoba - validni podaci
            pstmt.setInt(1, 1);
            pstmt.setString(2, "John");
            pstmt.setString(3, "Doe");
            pstmt.setString(4, "Some Address");
            pstmt.setString(5, "1995-01-15");
            pstmt.setString(6, "15011995123456"); // Počinje sa 15011995 (15.01.1995)
            pstmt.setString(7, "STUDENT");
            pstmt.executeUpdate();

            // Druga osoba
            pstmt.setInt(1, 2);
            pstmt.setString(2, "Alice");
            pstmt.setString(3, "Alister");
            pstmt.setString(4, "Another Address");
            pstmt.setString(5, "1980-05-20");
            pstmt.setString(6, "20051980444444"); // Počinje sa 20051980 (20.05.1980)
            pstmt.setString(7, "NASTAVNO_OSOBLJE");
            pstmt.executeUpdate();

            System.out.println("Početni podaci za Osobe su dodani!");
        } catch (SQLException e) {
            System.out.println("Greška pri dodavanju podataka: " + e.getMessage());
        }
    }

    // Dohvat svih osoba
    public static List<Osoba> dajSveOsobe() {
        List<Osoba> osobe = new ArrayList<>();
        String sql = "SELECT * FROM Osoba";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Osoba osoba = new Osoba(
                        rs.getInt("id"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("adresa"),
                        LocalDate.parse(rs.getString("datumRodjenja")),
                        rs.getString("maticniBroj"),
                        Uloga.valueOf(rs.getString("uloga"))
                );
                osobe.add(osoba);
            }
        } catch (SQLException e) {
            System.out.println("Greška pri čitanju osoba: " + e.getMessage());
        }

        return osobe;
    }

    // Dohvat osobe po ID-u
    public static Osoba dajOsobuPold(Integer id) {
        String sql = "SELECT * FROM Osoba WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Osoba(
                        rs.getInt("id"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("adresa"),
                        LocalDate.parse(rs.getString("datumRodjenja")),
                        rs.getString("maticniBroj"),
                        Uloga.valueOf(rs.getString("uloga"))
                );
            }
        } catch (SQLException e) {
            System.out.println("Greška pri čitanju osobe: " + e.getMessage());
        }

        return null;
    }
}