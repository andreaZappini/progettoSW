package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ListaDate {
    private String chiave;
    private ArrayList<LocalDate> date;

    public ListaDate(String chiave, ArrayList<LocalDate> date) {
        this.chiave = chiave;
        this.date = date;
    }

    public ArrayList<LocalDate> getDate() {
        return date;
    }

    @Override
    public String toString() {
        return chiave;
    }

    public void aggiungiDate(ArrayList<LocalDate> date) {
        this.date.addAll(date);
    }
}

