package model;

public class EccezioneOggetto extends IllegalArgumentException {

    private Object oggetto;

    public EccezioneOggetto(Object oggetto) {
        this.oggetto = oggetto;
    }

    public Object getOggetto() {
        return oggetto;
    }
}
