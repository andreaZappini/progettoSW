package model;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class GestioneTempo implements Runnable{

    private static long inizioDeiTempi = 1741260669150L;
    
    public void run(){}

    //return del tipo data[0] = giorno, data[1] = mese, data[2] = anno
    public static int[] giorno(){
        long tempTrascorso = System.currentTimeMillis() - inizioDeiTempi;
        long millisToSec = tempTrascorso / 1000;
        long secToDay = millisToSec / 10; //un giorno dura 10 secondi
        int anno = (int) secToDay / 365 + 2020;
        int mese = (int)((secToDay / 30) % 12) + 1;
        int giorno = (int)(secToDay % 30) + 1;
        int[] data = {giorno, mese, anno};
        return data;
    }

    public static String nomeMese(int n){

        Month mese = Month.of(n);
        return mese.getDisplayName(TextStyle.FULL, Locale.ITALIAN).toUpperCase();
    }
}