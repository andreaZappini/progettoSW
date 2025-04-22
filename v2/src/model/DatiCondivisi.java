package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class DatiCondivisi {
    private static Elenco<Utente> elencoUtenti = new Elenco<>();
    private static Elenco<TipoVisita> elencoTipiVisita = new Elenco<>();
    private static Elenco<Luogo> elencoLuoghi = new Elenco<>();
    private static Elenco<Visita> elencoVisite = new Elenco<>();
    private static Elenco<LocalDate> datePrecluse = new Elenco<>();
    private static String ambitoTerritoriale = "";
    private static int numeroMassimoIscrittiFruitore = 0;

    public static void setElencoUtenti(Elenco<Utente> elencoUtenti) {
        DatiCondivisi.elencoUtenti = elencoUtenti;
    }

    public static void setElencoTipiVisita(Elenco<TipoVisita> elencoTipiVisita) {
        DatiCondivisi.elencoTipiVisita = elencoTipiVisita;
    }

    public static void setElencoLuoghi(Elenco<Luogo> elencoLuoghi) {
        DatiCondivisi.elencoLuoghi = elencoLuoghi;
    }

    public static void setVisite(Elenco<Visita> elencoVisite) {
        DatiCondivisi.elencoVisite = elencoVisite;
    }

    public static void setDatePrecluse(Elenco<LocalDate> datePrecluse) {
        DatiCondivisi.datePrecluse = datePrecluse;
    }

    public static void setNumeroMassimoIscrittiFruitore(int n){
        DatiCondivisi.numeroMassimoIscrittiFruitore = n;
    }

    public static void setAmbitoTerritoriale(String ambitoTerritoriale) {
        DatiCondivisi.ambitoTerritoriale = ambitoTerritoriale;
    }

    // public static void setDatePrecluseMese(String mese, ArrayList<LocalDate> date) {
    //     datePrecluse.getElementByKey(mese).getDate().clear();
    //     datePrecluse.getElementByKey(mese).aggiungiDate(date);
    // }

    public static String getAmbitoTerritoriale() {
        return ambitoTerritoriale;
    }

    public static int getNumeroMassimoIscrittiFruitore() {
        return numeroMassimoIscrittiFruitore;
    }

    public static Elenco<Utente> getElencoUtenti() {
        return elencoUtenti;
    }

    public static Elenco<TipoVisita> getElencoTipiVisita() {
        return elencoTipiVisita;
    }

    public static Elenco<Luogo> getElencoLuoghi() {
        return elencoLuoghi;
    }

    public static Elenco<Visita> getVisite() {
        return elencoVisite;
    }

    public static Elenco<LocalDate> getDatePrecluse() {
        return datePrecluse;
    }

    public static void aggiungiTipoVisita(TipoVisita v){
        elencoTipiVisita.aggiungi(v);
    }

    public static void aggiungiUtente(Utente u){
        elencoUtenti.aggiungi(u);
    }

    public static void aggiungiVisita(Visita v){
        elencoVisite.aggiungi(v);
    }

    public static void aggiungiData(LocalDate d){
        datePrecluse.aggiungi(d);
    }

    public static void aggiungiElencoUtente(Elenco<Utente> e){
        elencoUtenti.aggiungi(e);
    }

    // public static void aggiungiDatePrecluseMese3(LocalDate d){
    //     datePrecluse.getElementByKey("3").getDate().add(d);
    // }

    public static void aggiungiLuogo(Luogo l){
        elencoLuoghi.aggiungi(l);
    }
}
