package model;

import java.time.*;
import java.util.ArrayList;

public class GestioneTempo {

    private static ArrayList<ArrayList<LocalDate>> datePrecluseMese = new ArrayList<>();
    private YearMonth mesePartenza;

    private static final LocalDate DATA_INIZIALE = LocalDate.of(2025, 4, 18);
    private static final Instant TEMPO_REALE_INIZIALE = Instant.parse("2025-04-18T00:00:00Z");
    private static final long SECONDI_PER_GIORNO_SIMULATO = 4;

    // Singleton
    private static final GestioneTempo instance = new GestioneTempo();

    private GestioneTempo() {
        this.mesePartenza = YearMonth.from(getDataCorrente());
        for(int i = 0; i < 4; i++) {
            datePrecluseMese.add(new ArrayList<>());
        }
        aggiornaDatePrecluseMese();
    }

    public void setDatePrecluse(ArrayList<LocalDate> nuoveDate) {
        ArrayList<LocalDate> esistenti = datePrecluseMese.get(3);
        for (LocalDate data : nuoveDate) {
            if (!esistenti.contains(data)) {
                datePrecluseMese.get(3).add(data);
            }
        }
    }
    
    public static GestioneTempo getInstance() {
        return instance;
    }

    public LocalDate getDataCorrente() {
        long secondiTrascorsi = Duration.between(TEMPO_REALE_INIZIALE, Instant.now()).getSeconds();
        long giorniSimulati = secondiTrascorsi / SECONDI_PER_GIORNO_SIMULATO;
        return DATA_INIZIALE.plusDays(giorniSimulati);
    }

    private void aggiornaDatePrecluseMese() {
        for(int i = 1; i < 4; i++){
            datePrecluseMese.set(i - 1, datePrecluseMese.get(i));
        }
        datePrecluseMese.get(3);
    }

    public void checkCambioMese(){
        YearMonth meseCorrente = YearMonth.from(getDataCorrente());
        if(!meseCorrente.equals(mesePartenza)){
            mesePartenza = meseCorrente;
            aggiornaDatePrecluseMese();
            //System.out.println("Il mese è cambiato: " + mesePartenza);
        }
    }

    public LocalDate[] intervalloDate(){

        LocalDate[] res = new LocalDate[2];

        if(getDataCorrente().getDayOfMonth() < 15){
            res[0] = LocalDate.of(getDataCorrente().getYear(), getDataCorrente().getMonthValue(), 16);
        }else{
            if(getDataCorrente().getMonthValue() == 12){
                res[0] = LocalDate.of(getDataCorrente().getYear() + 1, 1, 16);
            }else{
                res[0] = LocalDate.of(getDataCorrente().getYear(), getDataCorrente().getMonthValue() + 1, 16);
            }
        }

        if (getDataCorrente().getDayOfMonth()< 15){
            if(getDataCorrente().getMonthValue() == 12){
                res[1] = LocalDate.of(getDataCorrente().getYear() + 1, 1, 15);
            }else{
                res[1] = LocalDate.of(getDataCorrente().getYear(), getDataCorrente().getMonthValue() + 1, 15);
            }
        }else{
            if(getDataCorrente().getMonthValue() == 12){
                res[1] = LocalDate.of(getDataCorrente().getYear() + 1, 2, 15);
            }else if(getDataCorrente().getMonthValue() == 11){
                res[1] = LocalDate.of(getDataCorrente().getYear() + 1, 1, 15);
            }else{
                res[1] = LocalDate.of(getDataCorrente().getYear(), getDataCorrente().getMonthValue() + 2, 15);
            }
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