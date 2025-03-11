;public class GestioneTempo implements Runnable{

    private long inizioDeiTempi = 1741260669150L;
    
    public void run(){}

    //return del tipo data[0] = mese, data[1] = giorno
    public int[] giorno(){
        long tempTrascorso = System.currentTimeMillis() - inizioDeiTempi;
        long millisToSec = tempTrascorso / 1000;
        long secToDay = millisToSec / 10; //un giorno dura 10 secondi
        int mese = (int)((secToDay / 30) % 12) + 1;
        int giorno = (int)(secToDay % 30) + 1;
        int[] data = {mese, giorno};
        return data;
    }
}