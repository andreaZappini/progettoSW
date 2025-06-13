package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class DatiCondivisi {
    private static Elenco<Utente> elencoUtenti = new Elenco<>();
    private static Elenco<TipoVisita> elencoTipiVisita = new Elenco<>();
    private static Elenco<Luogo> elencoLuoghi = new Elenco<>();
    private static Elenco<ListaVisite> elencoVisite = new Elenco<>();
    private static Elenco<ListaDate> datePrecluse = new Elenco<>();
    private static Elenco<Visita> archivioVisite = new Elenco<>();
    private static String ambitoTerritoriale = "";
    private static int numeroMassimoIscrittiFruitore = 0;
    private static LocalDate dataUltimaEsecuzione;
    private static LocalDate[] intervalloPianoVisite;

    public static Elenco<Visita> getArchivio() {
        return archivioVisite;
    }

    public static void setArchivioVisite(Elenco<Visita> elenco) {
        DatiCondivisi.archivioVisite = elenco;
    }

    public static void aggiungiVisitaArchivio(Visita v) {
        archivioVisite.aggiungi(v);
    }

    public static void setIntervalloPianoVisite(LocalDate[] intervallo) {
        intervalloPianoVisite = intervallo;
    }

    public static LocalDate[] getIntervalloPianoVisite() {
        return intervalloPianoVisite;
    }

    
    public enum StatiRaccoltaDisponibilita {
        APERTA,
        CHIUSA
    }

    public static LocalDate getDataUltimaEsecuzione() {
        return dataUltimaEsecuzione;
    }

    public static void setDataUltimaEsecuzione(LocalDate d){
        dataUltimaEsecuzione = d;
    }

    private static StatiRaccoltaDisponibilita raccoltaDisponibilitaMese1 = StatiRaccoltaDisponibilita.APERTA;
    private static StatiRaccoltaDisponibilita raccoltaDisponibilitaMese2 = StatiRaccoltaDisponibilita.CHIUSA;
    
    public static void chiudiRaccoltaDisponibilitaMese1() {
        raccoltaDisponibilitaMese1 = StatiRaccoltaDisponibilita.CHIUSA;
    }

    public static void chiudiRaccoltaDisponibilitaMese2() {
        raccoltaDisponibilitaMese2 = StatiRaccoltaDisponibilita.CHIUSA;
    }

    public static void apriRaccoltaDisponibilitaMese1() {
        raccoltaDisponibilitaMese1 = StatiRaccoltaDisponibilita.APERTA;
    }

    public static void apriRaccoltaDisponibilitaMese2() {
        raccoltaDisponibilitaMese2 = StatiRaccoltaDisponibilita.APERTA;
    }

    public static StatiRaccoltaDisponibilita getRaccoltaDisponibilitaMese1() {
        return raccoltaDisponibilitaMese1;
    }

    public static StatiRaccoltaDisponibilita getRaccoltaDisponibilitaMese2() {
        return raccoltaDisponibilitaMese2;
    }

    public static void setElencoUtenti(Elenco<Utente> elencoUtenti) {
        DatiCondivisi.elencoUtenti = elencoUtenti;
    }

    public static void setElencoTipiVisita(Elenco<TipoVisita> elencoTipiVisita) {
        DatiCondivisi.elencoTipiVisita = elencoTipiVisita;
    }

    public static void setElencoLuoghi(Elenco<Luogo> elencoLuoghi) {
        DatiCondivisi.elencoLuoghi = elencoLuoghi;
    }

    public static void setVisite(Elenco<ListaVisite> elencoVisite) {
        DatiCondivisi.elencoVisite = elencoVisite;
    }

    public static void setDatePrecluse(Elenco<ListaDate> datePrecluse) {
        DatiCondivisi.datePrecluse = datePrecluse;
    }

    public static void setListaDateConChiave(String chiave, ArrayList<LocalDate> date) {
        datePrecluse.getElementByKey(chiave).pulisciDate();
        datePrecluse.getElementByKey(chiave).aggiungiDate(date);
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

    public static Elenco<ListaVisite> getVisite() {
        return elencoVisite;
    }

    public static Elenco<ListaDate> getDatePrecluse() {
        return datePrecluse;
    }

    public static ListaDate getDatePrecluseMese(String chiave){
        return datePrecluse.getElementByKey(chiave);
    }

    public static void aggiungiTipoVisita(TipoVisita v){
        elencoTipiVisita.aggiungi(v);
    }

    public static void aggiungiUtente(Utente u){
        elencoUtenti.aggiungi(u);
    }

    public static void aggiungiVisita(ListaVisite v){
        elencoVisite.aggiungi(v);
    }

    public static void aggiungiDataMese3(LocalDate d){
        datePrecluse.getElementByKey("3").getDate().add(d);
    }

    public static void aggiungiElencoUtente(Elenco<? extends Utente> e){
        elencoUtenti.aggiungi(e);
    }

    // public static void aggiungiDatePrecluseMese3(LocalDate d){
    //     datePrecluse.getElementByKey("3").getDate().add(d);
    // }

    public static void aggiungiLuogo(Luogo l){
        elencoLuoghi.aggiungi(l);
    }

    public static void rimuoviLuogo(Luogo l){
        elencoLuoghi.rimuovi(l);
    }
    public static void rimuoviTipoVisita(TipoVisita t){
        elencoTipiVisita.rimuovi(t);
    }

    public static void rimuoviVolontario(Volontario v){
        elencoUtenti.rimuovi(v);
    }

    public static void aggiungiVisitaMese1(Visita v){
        elencoVisite.getElementByKey("1").aggiungiVisita(v);
    }

    public static void aggiungiListaDate(ListaDate ld){
        datePrecluse.aggiungi(ld);
    }

    public static void setVisiteConChiave(String chiave, Elenco<Visita> visite){
        elencoVisite.getElementByKey(chiave).setVisite(visite);
    }

    public static Elenco<Fruitore> getFruitori(){
       return elencoUtenti.getClassiUtente(Fruitore.class);
    }
}
