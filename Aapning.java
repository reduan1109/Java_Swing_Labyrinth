import java.util.*;
public class Aapning extends HvitRute {
    public Aapning(int rad, int kollonne, Labyrint labyrint) {
        super(rad, kollonne, labyrint);
    }

    @Override
    //når Rute.gaa() er en Rute av subklasse Aapning blir denne metoden kalt på
    public void gaa(ArrayList<Tuppel> besoekt) {
        //siden det er denne gaa funksjonen det snakk om vet vi at vi er i en aapning og trenger ikke gjøre noe annet en å bvege oss opp i returnstacken igjen
        //lager et tuppel og legger til i besoekt, som blir lagt til i loesning
        besoekt.add(new Tuppel(kollonne, rad));
        loesning.add(besoekt);
    }
    
    @Override
    public String toString() {
        return ".";
    }

    @Override
    public String charTilTegn() {
        return this.toString();
    }
}