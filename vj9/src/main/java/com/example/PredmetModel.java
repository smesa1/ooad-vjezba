package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PredmetModel {
    private static final String DB_URL = "jdbc:sqlite:baza.db";

     private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

     public static void kreirajTabeluPredmet() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Predmet (
                id INTEGER PRIMARY KEY,
                naziv TEXT NOT NULL,
                sifra TEXT NOT NULL UNIQUE,
                espb INTEGER NOT NULL,
                opis TEXT
            );
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela Predmet je kreirana!");
        } catch (SQLException e) {
            System.out.println("Greška pri kreiranju tabele Predmet: " + e.getMessage());
        }
    }

    // Dodavanje početnih podataka
    public static void dodajPocetnePodatke() {
        String sql = """
            INSERT INTO Predmet (id, naziv, sifra, espb, opis)
            VALUES (?, ?, ?, ?, ?);
            """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Prvi predmet
            pstmt.setInt(1, 1);
            pstmt.setString(2, "Razvoj programskih rješenja");
            pstmt.setString(3, "RPR");
            pstmt.setInt(4, 6);
            pstmt.setString(5, "Osnove razvoja softvera");
            pstmt.executeUpdate();

            // Drugi predmet
            pstmt.setInt(1, 2);
            pstmt.setString(2, "Baze podataka");
            pstmt.setString(3, "BP");
            pstmt.setInt(4, 5);
            pstmt.setString(5, "Relacione baze podataka");
            pstmt.executeUpdate();

            // Treći predmet
            pstmt.setInt(1, 3);
            pstmt.setString(2, "Objektno orijentisano programiranje");
            pstmt.setString(3, "OOP");
            pstmt.setInt(4, 7);
            pstmt.setString(5, "Napredno OOP u Javi");
            pstmt.executeUpdate();

            System.out.println("Početni podaci za Predmete su dodani!");
        } catch (SQLException e) {
            System.out.println("Greška pri dodavanju predmeta: " + e.getMessage());
        }
    }

    // Dohvat svih predmeta
    public static List<Predmet> dajSvePredmete() {
        List<Predmet> predmeti = new ArrayList<>();
        String sql = "SELECT * FROM Predmet";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Predmet predmet = new Predmet(
                        rs.getInt("id"),
                        rs.getString("naziv"),
                        rs.getString("sifra"),
                        rs.getInt("espb"),
                        rs.getString("opis")
                );
                predmeti.add(predmet);
            }
        } catch (SQLException e) {
            System.out.println("Greška pri čitanju predmeta: " + e.getMessage());
        }

        return predmeti;
    }

    // Dohvat predmeta po ID-u
    public static Predmet dajPredmetPold(Integer id) {
        String sql = "SELECT * FROM Predmet WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Predmet(
                        rs.getInt("id"),
                        rs.getString("naziv"),
                        rs.getString("sifra"),
                        rs.getInt("espb"),
                        rs.getString("opis")
                );
            }
        } catch (SQLException e) {
            System.out.println("Greška pri čitanju predmeta: " + e.getMessage());
        }

        return null;
    }

    // Ažuriranje predmeta
    public static String azurirajPredmet(Integer id, String noviNaziv, String novaSifra,
                                         Integer noviEspb, String noviOpis) {
        StringBuilder upit = new StringBuilder("UPDATE Predmet SET ");
        boolean imaPromjene = false;
        List<Object> parametri = new ArrayList<>();

        if (noviNaziv != null && !noviNaziv.isEmpty()) {
            upit.append("naziv = ?, ");
            parametri.add(noviNaziv);
            imaPromjene = true;
        }

        if (novaSifra != null && !novaSifra.isEmpty()) {
            upit.append("sifra = ?, ");
            parametri.add(novaSifra);
            imaPromjene = true;
        }

        if (noviEspb != null) {
            upit.append("espb = ?, ");
            parametri.add(noviEspb);
            imaPromjene = true;
        }

        if (noviOpis != null) {
            upit.append("opis = ?, ");
            parametri.add(noviOpis);
            imaPromjene = true;
        }

        if (!imaPromjene) {
            return "Sva polja su ista kao i prije!";
        }

        upit.delete(upit.length() - 2, upit.length());
        upit.append(" WHERE id = ?");
        parametri.add(id);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(upit.toString())) {

            for (int i = 0; i < parametri.size(); i++) {
                pstmt.setObject(i + 1, parametri.get(i));
            }

            int promijenjeniRedovi = pstmt.executeUpdate();

            if (promijenjeniRedovi > 0) {
                return "Predmet je uspjesno azuriran";
            } else {
                return "Ne postoji predmet sa datim id-em";
            }
        } catch (SQLException e) {
            return "SQL greška: " + e.getMessage();
        }
    }

    // Brisanje predmeta po ID-u
    public static String obrisiPredmetPold(Integer id) {
        String sql = "DELETE FROM Predmet WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int brojObrisanih = pstmt.executeUpdate();

            if (brojObrisanih > 0) {
                return "Predmet je uspjesno obrisan";
            } else {
                return "Ne postoji predmet sa datim id-em";
            }
        } catch (SQLException e) {
            return "SQL greška: " + e.getMessage();
        }
    }
}