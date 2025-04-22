package model;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class GestioneTempo {

    private YearMonth mesePartenza;

    private static final LocalDate DATA_INIZIALE = LocalDate.of(2025, 4, 18);
    private static final Instant TEMPO_REALE_INIZIALE = Instant.parse("2025-04-18T00:00:00Z");
    private static final long SECONDI_PER_GIORNO_SIMULATO = 2;

    // Singleton
    private static final GestioneTempo instance = new GestioneTempo();

    private GestioneTempo() {
        this.mesePartenza = YearMonth.from(getDataCorrente());
        // for(int i = 0; i < 4; i++) {
        //     DatiCondivisi.aggiungiListaDate(new ListaDate(String.valueOf(i), new ArrayList<LocalDate>()));
        // }
        checkCambioMese();
    }

    // public ArrayList<LocalDate> getDatePrecluseMese(int n) {
    //     return DatiCondivisi.getDatePrecluse().getElementByKey(String.valueOf(n)).getDate();
    // }

    // public void setDatePrecluse(ArrayList<LocalDate> nuoveDate) {
    //     // Calcola la chiave corrispondente al mese i+3
    //     LocalDate oggi = getDataCorrente();
    //     YearMonth meseInCorso = YearMonth.from(oggi);
    //     YearMonth meseFuturo = oggi.getDayOfMonth() < 15 ? meseInCorso.plusMonths(3) : meseInCorso.plusMonths(4);
    
    //     // Trova quale chiave (0-3) è associata al meseFuturo
    //     int indice = -1;
    //     for (int i = 0; i < 4; i++) {
    //         ArrayList<LocalDate> date = DatiCondivisi.getDatePrecluse().getElementByKey(String.valueOf(i)).getDate();
    //         if (!date.isEmpty()) {
    //             YearMonth mese = YearMonth.from(date.get(0));
    //             if (mese.equals(meseFuturo)) {
    //                 indice = i;
    //                 break;
    //             }
    //       }
    //    }
    
    //     // Se non è stato trovato, usa la chiave 3 (quella vuota più lontana)
    //     if (indice == -1) {
    //         indice = 3;
    //     }
    
    //     for (LocalDate data : nuoveDate) {
    //         if (!DatiCondivisi.getDatePrecluse().getElementByKey(String.valueOf(indice)).getDate().contains(data)) {
    //             DatiCondivisi.getDatePrecluse().getElementByKey(String.valueOf(indice)).getDate().add(data);
    //         }
    //     }
    // }
    
    
    public static GestioneTempo getInstance() {
        return instance;
    }

    public LocalDate getDataCorrente() {
        long secondiTrascorsi = Duration.between(TEMPO_REALE_INIZIALE, Instant.now()).getSeconds();
        long giorniSimulati = secondiTrascorsi / SECONDI_PER_GIORNO_SIMULATO;
        return DATA_INIZIALE.plusDays(giorniSimulati);
    }

    // private void aggiornaDatePrecluseMese(int mesiTrascorsi) {
    //     for (int step = 0; step < mesiTrascorsi; step++) {
    //         for (int i = 1; i < 4; i++) {
    //             ArrayList<LocalDate> dateCorrenti = new ArrayList<>(
    //                 DatiCondivisi.getDatePrecluse().getElementByKey(String.valueOf(i)).getDate()
    //             );
    //             DatiCondivisi.getDatePrecluse().getElementByKey(String.valueOf(i - 1)).getDate().clear();
    //             DatiCondivisi.setDatePrecluseMese(String.valueOf(i - 1), dateCorrenti);
    //         }
    //         // Pulisce la lista più lontana
    //         DatiCondivisi.getDatePrecluse().getElementByKey("3").getDate().clear();
    //     }
    // }
    
    

    public void checkCambioMese() {
        LocalDate dataCorrente = getDataCorrente();
        YearMonth meseCorrente = YearMonth.from(dataCorrente);

        if (dataCorrente.getDayOfMonth() >= 15) {
            long mesiTrascorsi = ChronoUnit.MONTHS.between(mesePartenza, meseCorrente);

            if (mesiTrascorsi > 0) {
                mesePartenza = mesePartenza.plusMonths(mesiTrascorsi);
                //aggiornaDatePrecluseMese((int) mesiTrascorsi);
            }
        }
    }

    
    public LocalDate[] intervalloDate(int n) {
        LocalDate[] res = new LocalDate[2];
        LocalDate oggi = getDataCorrente();
    
        if (oggi.getDayOfMonth() < 15) {
            res[0] = oggi.plusMonths(n).withDayOfMonth(16);
            res[1] = oggi.plusMonths(n + 1).withDayOfMonth(15);
        } else {
            res[0] = oggi.plusMonths(n + 1).withDayOfMonth(16);
            res[1] = oggi.plusMonths(n + 2).withDayOfMonth(15);
        }
    
        return res;
    }
    

    public static LocalDate contieneGiorno(LocalDate inizio, LocalDate fine, int giorno) {
        LocalDate current = inizio;
        while (!current.isAfter(fine)) {
            if (current.getDayOfMonth() == giorno) {
                return current;
            }
            current = current.plusDays(1);
        }
        return null;
    }
}