package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class GestoreVisite {

    private static final GestoreVisite instance = new GestoreVisite();

    private GestoreVisite() {
        DatiCondivisi.aggiungiVisita(new ListaVisite("0"));
        DatiCondivisi.aggiungiVisita(new ListaVisite("1"));
    }
    public static GestoreVisite getInstance() {
        return instance;
    }

    public void creaPianoViste(LocalDate inizio, LocalDate fine) {
        System.out.println(inizio);
        System.out.println(fine);
        for(LocalDate giorno = inizio; giorno.isBefore(fine); giorno = giorno.plusDays(1)){
            System.out.println("Giorno: " + giorno.toString());
            
            if(DatiCondivisi.getDatePrecluse().contiene(giorno.toString())) 
                continue;

            ciclotipoVisita: //label per uscire dal ciclo dei tipi di visita
            for(TipoVisita tipo : DatiCondivisi.getElencoTipiVisita().getElenco().values()){
                System.out.println("Tipo: " + tipo.toString());
                
                for(Volontario volo : tipo.getElencoVolontari().getElenco().values()){

                    System.out.println("Volontario: " + volo.toString());

                    boolean cond1 = volo.getElencoDisponibilita().contiene(giorno.toString());
                    boolean cond2 = tipo.getGiorniDisponibili().contains(Giorni.traduci(giorno));

                    System.out.println("Condizione 1: " + cond1);
                    System.out.println("Condizione 2: " + cond2);

                    if(cond1 && cond2){
                        Visita visita = new Visita(giorno, tipo);
                        System.out.println("Visita: " + visita.toString());
                        DatiCondivisi.aggiungiVisitaMese1(visita);
                        break ciclotipoVisita; // Esce dal ciclo dei tipi di visita se trova una visita valida
                    }
                }
            }
        }
        System.out.println("✅ Visite totali nella chiave '1': " +
    DatiCondivisi.getVisite().getElementByKey("1").getVisite().getElenco().size());
    }

    
    public LocalDate[] intervallo(){
        if(DatiCondivisi.getRaccoltaDisponibilitaMese1() == DatiCondivisi.StatiRaccoltaDisponibilita.APERTA &&
            DatiCondivisi.getRaccoltaDisponibilitaMese2() == DatiCondivisi.StatiRaccoltaDisponibilita.CHIUSA)
            return GestioneTempo.getInstance().intervalloDate(0);

        else if(DatiCondivisi.getRaccoltaDisponibilitaMese1() == DatiCondivisi.StatiRaccoltaDisponibilita.CHIUSA &&
            DatiCondivisi.getRaccoltaDisponibilitaMese2() == DatiCondivisi.StatiRaccoltaDisponibilita.APERTA)
            return GestioneTempo.getInstance().intervalloDate(1);

        else{
            return null;
        }
    }

    public void aggiornaVisiteMese(int mesi){
        for(int step = 0; step < mesi; step++){
    
            Elenco<Visita> visiteOriginali = DatiCondivisi.getVisite().getElementByKey("1").getVisite();
            Elenco<Visita> copia = new Elenco<>();
    
            for (Visita v : visiteOriginali.getElenco().values()) {
                v.cambiaStato(StatiVisita.VISITA_PROPOSTA);
                copia.aggiungi(v);
            }
    
            DatiCondivisi.getVisite().getElementByKey("0").getVisite().pulisciElenco();
            DatiCondivisi.setVisiteConChiave("0", copia);
    
            DatiCondivisi.getVisite().getElementByKey("1").getVisite().pulisciElenco();
        }
    }

    // public void aggiornaStato(){
    //     Elenco<Visita> visiteOriginali = new Elenco<>();
    //     ArrayList<Giorni> giorni = new ArrayList<>();
    //     giorni.add(Giorni.LUNEDI);
    //     TipoVisita tipo = new TipoVisita("t", "t", "t", "t", giorni, 8, 9, "t", 8, 9);
    //     HashMap<Fruitore, Integer> iscrizioni = new HashMap<>();
    //     iscrizioni.put(new Fruitore("fruitore1", "password1"), 8);
    //     visiteOriginali.aggiungi(new Visita( LocalDate.of(2025, 06, 02), tipo, StatiVisita.VISITA_CONFERMATA, iscrizioni, 8));
    //     visiteOriginali.aggiungi(new Visita( LocalDate.of(2025, 06, 03), tipo, StatiVisita.VISITA_CONFERMATA, iscrizioni, 8));
    //     for(Visita v : visiteOriginali.getElenco().values()) {
    //         System.out.println("Visita: " + v.toString());
    //         switch(v.getStato()) {

    //             case VISITA_CANCELLATA:
             
    //                 DatiCondivisi.getVisite().getElementByKey("0").getVisite().rimuovi(v);
    //                 break;

    //             case VISITA_CONFERMATA:
             
    //                 if(v.getDataVisita().isAfter(LocalDate.of(2025, 06, 01))){
    //                     v.cambiaStato(StatiVisita.VISITA_EFFETTUATA);
    //                     System.out.println("aggiungo a archivio");
    //                     System.out.println(DatiCondivisi.getArchivio().numeroElementi());
    //                     DatiCondivisi.aggiungiVisitaArchivio(v);
    //                 }
    //                 break;

    //             case VISITA_COMPLETA:
                             
    //                 if(GestioneTempo.getInstance().getDataCorrente().equals(v.getDataVisita().minusDays(3)))
    //                     v.cambiaStato(StatiVisita.VISITA_CONFERMATA);
    //                 break;

    //             case VISITA_EFFETTUATA:
             
    //                 break;
    //             case VISITA_PROPOSTA:
                             
    //                 if(GestioneTempo.getInstance().getDataCorrente().equals(v.getDataVisita().minusDays(3))){

    //                     if(v.getIscritti() < v.getMinPartecipanti())
    //                         v.cambiaStato(StatiVisita.VISITA_CANCELLATA);
                        
    //                     else
    //                         v.cambiaStato(StatiVisita.VISITA_CONFERMATA);
    //                 }
    //                 if(v.getMaxPartecipanti() == v.getIscritti())
    //                     v.cambiaStato(StatiVisita.VISITA_COMPLETA);
                    
    //             case VISITA_PROPONIBILE:
                             
    //                 // Se la visita è ancora proponibile, non facciamo nulla
    //                 break;
    //             default:
    //                 throw new IllegalStateException("Stato visita non gestito: " + v.getStato());
                
    //         }
    //     }
    // }

    public void aggiornaStato(){

        for(Visita v : DatiCondivisi.getVisite().getElementByKey("0").getVisite().getElenco().values()) {
            switch(v.getStato()) {

                case VISITA_CANCELLATA:
                    DatiCondivisi.getVisite().getElementByKey("0").getVisite().rimuovi(v);
                    break;

                case VISITA_CONFERMATA:
                    if(v.getDataVisita().isBefore(GestioneTempo.getInstance().getDataCorrente())){
                        v.cambiaStato(StatiVisita.VISITA_EFFETTUATA);
                        DatiCondivisi.aggiungiVisitaArchivio(v);
                    }
                    break;

                case VISITA_COMPLETA:
                    if(GestioneTempo.getInstance().getDataCorrente().equals(v.getDataVisita().minusDays(3)))
                        v.cambiaStato(StatiVisita.VISITA_CONFERMATA);
                    break;

                case VISITA_EFFETTUATA:

                    break;
                case VISITA_PROPOSTA:
                
                    if(GestioneTempo.getInstance().getDataCorrente().equals(v.getDataVisita().minusDays(3))){

                        if(v.getIscritti() < v.getMinPartecipanti())
                            v.cambiaStato(StatiVisita.VISITA_CANCELLATA);
                        
                        else
                            v.cambiaStato(StatiVisita.VISITA_CONFERMATA);
                    }
                    if(v.getMaxPartecipanti() == v.getIscritti())
                        v.cambiaStato(StatiVisita.VISITA_COMPLETA);
                    
                case VISITA_PROPONIBILE:
                
                    // Se la visita è ancora proponibile, non facciamo nulla
                    break;
                default:
                    throw new IllegalStateException("Stato visita non gestito: " + v.getStato());
                
            }
        }
    }

    public String visiteDisponibili(){
        StringBuffer sb = new StringBuffer();
        for (Visita v : DatiCondivisi.getVisite().getElementByKey("0").getVisite().getElenco().values()) {
            StatiVisita stato = v.getStato();
            if(stato == StatiVisita.VISITA_PROPOSTA||
                stato == StatiVisita.VISITA_CONFERMATA){
                sb.append("CODICE" + v.toString() + "\n");
                sb.append("\t-" + v.getTipo().getTitolo() + "\n");
                sb.append("\t-" + v.getTipo().getDescrizione() + "\n");
                sb.append("\t-" + v.getTipo().getPuntoIncontro() + "\n");
                sb.append("\t-" + v.getDataVisita() + "\n");
                sb.append("\t-" + v.getTipo().getOraInizio() + "\n");
                sb.append("\t-" + "biglietto necessario: " + v.getTipo().getBigliettoNecessario() + "\n");
            }else if(stato == StatiVisita.VISITA_CANCELLATA){
                sb.append("visita cancellata: " + v.toString() + "\n");
                sb.append("\t-" + v.getTipo().getTitolo() + "\n");
                sb.append("\t-" + "data prevista della visita: " + v.getDataVisita() + "\n");
            }
        }
        return sb.toString();
    }

    public String visitePrenotabili(){
        StringBuffer sb = new StringBuffer();
        for (Visita v : DatiCondivisi.getVisite().getElementByKey("0").getVisite().getElenco().values()) {
            StatiVisita stato = v.getStato();
            if(stato == StatiVisita.VISITA_PROPOSTA){
                sb.append(v.toString() + "\n");
            }
        }
        return sb.toString();
    }

    public void rimuoviViistePassateFruitore(){
        Elenco<Fruitore> fruitori = DatiCondivisi.getElencoUtenti().getClassiUtente(Fruitore.class);
        for(Fruitore f : fruitori.getElenco().values()) {

            for (Visita v : f.getPrenotazioniVisite().getElenco().values()) {
                if(v.getDataVisita().isBefore(GestioneTempo.getInstance().getDataCorrente().plusDays(1))){
                    f.rimuoviPrenotazione(v);
                }
            }
        }
    }
}