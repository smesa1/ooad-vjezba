package com.example;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
         System.out.println("Evo ja ovo napisala");
          initBaza();
         boolean running = true;

         System.out.println("Dobar dan?");

        while (running) {
            printMenu();
            int choice = getIntInput("Izaberite opciju: ");

            switch (choice) {
                case 1 -> testZadatak1();
                case 2 -> testZadatak2();
                case 3 -> prikaziSvePodatke();
                case 4 -> testValidacije();
                case 0 -> {
                    running = false;
                    System.out.println("Hvala i doviđenja!");
                }
                default -> System.out.println("Nevažeći izbor!");
            }
        }

        scanner.close();
    }
    private static boolean validirajIme(String ime) {
        return ime != null && ime.length() >= 2;
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Unesite validan broj!");
            return getIntInput(prompt); // rekurzija za ponovni unos
        }
    }

    private static boolean validirajMaticniBrojSaDatumom(String maticniBroj, LocalDate datumRodjenja) {
        if (maticniBroj == null || datumRodjenja == null) {
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String datumString = datumRodjenja.format(formatter);

        return maticniBroj.startsWith(datumString);
    }


    private static void initBaza() {
        OsobaModel.kreirajTabeluOsoba();
        PredmetModel.kreirajTabeluPredmet();

        OsobaModel.dodajPocetnePodatke();
        PredmetModel.dodajPocetnePodatke();
    }

    private static void printMenu() {
        System.out.println("\n=== GLAVNI MENI ===");
        System.out.println("1. Test Zadatak 1 (Osobe - brisanje i ažuriranje)");
        System.out.println("2. Test Zadatak 2 (Predmeti - CRUD operacije)");
        System.out.println("3. Prikaži sve podatke");
        System.out.println("4. Test validacije (ime i matični broj)");
        System.out.println("0. Izlaz");
    }

    private static void testZadatak1() {
        System.out.println("\n=== TEST ZADATKA 1 ===");

        // Prikaži sve osobe
        System.out.println("\nTrenutne osobe u bazi:");
        List<Osoba> osobe = OsobaModel.dajSveOsobe();
        osobe.forEach(System.out::println);

        System.out.println("\n--- Test brisanja osobe ---");
        int idZaBrisanje = getIntInput("Unesite ID osobe za brisanje (0 za preskok): ");
        if (idZaBrisanje > 0) {
            String rezultat = OsobaModel.obrisiOsobuPold(idZaBrisanje);
            System.out.println("Rezultat brisanja: " + rezultat);
        }

        System.out.println("\n--- Test ažuriranja osobe ---");
        int idZaAzuriranje = getIntInput("Unesite ID osobe za ažuriranje: ");

        if (idZaAzuriranje > 0) {
            System.out.println("\nUnesite nove podatke (pritisnite Enter za preskok):");

            System.out.print("Novo ime: ");
            String novoIme = scanner.nextLine();
            if (novoIme.isEmpty()) novoIme = null;

            System.out.print("Novo prezime: ");
            String novoPrezime = scanner.nextLine();
            if (novoPrezime.isEmpty()) novoPrezime = null;

            System.out.print("Nova adresa: ");
            String novaAdresa = scanner.nextLine();
            if (novaAdresa.isEmpty()) novaAdresa = null;

            System.out.print("Novi datum rođenja (YYYY-MM-DD): ");
            String datumStr = scanner.nextLine();
            LocalDate noviDatum = null;
            if (!datumStr.isEmpty()) {
                try {
                    noviDatum = LocalDate.parse(datumStr);
                } catch (Exception e) {
                    System.out.println("Nevažeći format datuma!");
                }
            }

            System.out.print("Novi matični broj: ");
            String noviMaticni = scanner.nextLine();
            if (noviMaticni.isEmpty()) noviMaticni = null;

            System.out.print("Nova uloga (STUDENT/NASTAVNO_OSOBLJE): ");
            String ulogaStr = scanner.nextLine();
            Uloga novaUloga = null;
            if (!ulogaStr.isEmpty()) {
                try {
                    novaUloga = Uloga.valueOf(ulogaStr.toUpperCase());
                } catch (Exception e) {
                    System.out.println("Nevažeća uloga!");
                }
            }

            String rezultat = OsobaModel.azurirajOsobu(
                    idZaAzuriranje, novoIme, novoPrezime, novaAdresa,
                    noviDatum, noviMaticni, novaUloga
            );

            System.out.println("Rezultat ažuriranja: " + rezultat);
        }

        // Prikaži ažurirane osobe
        System.out.println("\nAžurirane osobe:");
        OsobaModel.dajSveOsobe().forEach(System.out::println);
    }

    // TEST ZADATKA 2
    private static void testZadatak2() {
        System.out.println("\n=== TEST ZADATKA 2 ===");

        // Prikaži sve predmete
        System.out.println("\nTrenutni predmeti u bazi:");
        List<Predmet> predmeti = PredmetModel.dajSvePredmete();
        predmeti.forEach(System.out::println);

         int idZaPretragu = getIntInput("Unesite ID predmeta za pretragu: ");

        if (idZaPretragu > 0) {
            Predmet predmet = PredmetModel.dajPredmetPold(idZaPretragu);

            if (predmet != null) {
                System.out.println("Pronađen predmet:");
                System.out.println("ID: " + predmet.getId());
                System.out.println("Naziv: " + predmet.getNaziv());
                System.out.println("Šifra: " + predmet.getSifra());
                System.out.println("ESPB: " + predmet.getEspb());
                System.out.println("Opis: " + predmet.getOpis());
            } else {
                System.out.println("Predmet sa ID " + idZaPretragu + " nije pronađen.");
            }
        }

         int idPredmetZaAzuriranje = getIntInput("Unesite ID predmeta za ažuriranje: ");

        if (idPredmetZaAzuriranje > 0) {
            System.out.println("\nUnesite nove podatke (pritisnite Enter za preskok):");

            System.out.print("Novi naziv: ");
            String noviNaziv = scanner.nextLine();
            if (noviNaziv.isEmpty()) noviNaziv = null;

            System.out.print("Nova šifra: ");
            String novaSifra = scanner.nextLine();
            if (novaSifra.isEmpty()) novaSifra = null;

            System.out.print("Novi ESPB: ");
            String espbStr = scanner.nextLine();
            Integer noviEspb = null;
            if (!espbStr.isEmpty()) {
                try {
                    noviEspb = Integer.parseInt(espbStr);
                } catch (Exception e) {
                    System.out.println("Nevažeći ESPB!");
                }
            }

            System.out.print("Novi opis: ");
            String noviOpis = scanner.nextLine();
            if (noviOpis.isEmpty()) noviOpis = null;

            String rezultat = PredmetModel.azurirajPredmet(
                    idPredmetZaAzuriranje, noviNaziv, novaSifra, noviEspb, noviOpis
            );

            System.out.println("Rezultat ažuriranja: " + rezultat);
        }

         int idPredmetZaBrisanje = getIntInput("Unesite ID predmeta za brisanje (0 za preskok): ");

        if (idPredmetZaBrisanje > 0) {
            String rezultat = PredmetModel.obrisiPredmetPold(idPredmetZaBrisanje);
            System.out.println("Rezultat brisanja: " + rezultat);
        }

         System.out.println("\nAžurirani predmeti:");
        PredmetModel.dajSvePredmete().forEach(System.out::println);
    }

    private static void prikaziSvePodatke() {
        System.out.println("\n=== SVI PODACI U BAZI ===\n");

        System.out.println("--- OSOBE ---");
        List<Osoba> osobe = OsobaModel.dajSveOsobe();
        if (osobe.isEmpty()) {
            System.out.println("Nema osoba u bazi.");
        } else {
            osobe.forEach(osoba -> {
                System.out.println("ID: " + osoba.getId());
                System.out.println("  Ime: " + osoba.getIme() + " " + osoba.getPrezime());
                System.out.println("  Adresa: " + osoba.getAdresa());
                System.out.println("  Datum rođenja: " + osoba.getDatumRodjenja());
                System.out.println("  Matični broj: " + osoba.getMaticniBroj());
                System.out.println("  Uloga: " + osoba.getUloga());
                System.out.println();
            });
        }

        System.out.println("--- PREDMETI ---");
        List<Predmet> predmeti = PredmetModel.dajSvePredmete();
        if (predmeti.isEmpty()) {
            System.out.println("Nema predmeta u bazi.");
        } else {
            predmeti.forEach(predmet -> {
                System.out.println("ID: " + predmet.getId());
                System.out.println("  Šifra: " + predmet.getSifra());
                System.out.println("  Naziv: " + predmet.getNaziv());
                System.out.println("  ESPB: " + predmet.getEspb());
                System.out.println("  Opis: " + predmet.getOpis());
                System.out.println();
            });
        }
    }

    private static void testValidacije() {

        System.out.println("\nTest 1: Validno ime (min 2 karaktera)");
        System.out.println("'John': " + (validirajIme("John") ? "VALIDNO" : "NEVALIDNO"));
        System.out.println("'J': " + (validirajIme("J") ? "VALIDNO" : "NEVALIDNO"));

        System.out.println("\nTest 2: Matični broj i datum rođenja");
        LocalDate datum1 = LocalDate.of(1995, 1, 15);
        String maticni1 = "15011995123456";
        System.out.println(maticni1 + " sa " + datum1 + ": " +
                (validirajMaticniBrojSaDatumom(maticni1, datum1) ? "POKLAPA SE" : "NE POKLAPA SE"));

        LocalDate datum2 = LocalDate.of(1980, 5, 20);
        String maticni2 = "20051980444444";
        System.out.println(maticni2 + " sa " + datum2 + ": " +
                (validirajMaticniBrojSaDatumom(maticni2, datum2) ? "POKLAPA SE" : "NE POKLAPA SE"));

         String maticni3 = "01011995123456";
        System.out.println(maticni3 + " sa " + datum1 + ": " +
                (validirajMaticniBrojSaDatumom(maticni3, datum1) ? "POKLAPA SE" : "NE POKLAPA SE"));
    }
}