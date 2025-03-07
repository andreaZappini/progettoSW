;public class GestioneTempo implements Runnable{

    private long start;
    
    public void run(){}
    public void start(long start){
        this.start = start;
    }

    //return del tipo data[0] = mese, data[1] = giorno
    public int[] giorno(){
        long tempTrascorso = System.currentTimeMillis() - this.start;
        long millisToSec = tempTrascorso / 1000;
        long SecToDay = millisToSec / 10; //un giorno dura 10 secondi
        int mese = ((int)SecToDay) / 30 + 1;
        int giorno = ((int)SecToDay) % 30 + 1;
        int[] data = {mese, giorno};
        return data;
    }
}