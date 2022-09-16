import java.util.*;

public class SortRute extends Rute {
    
    public SortRute(int rad, int kollonne, Labyrint labyrint) {
        super(rad, kollonne, labyrint);
        flagg = true;
    }

    //Dette burde aldri skje ettersom denne subklassen starter med flagg lik true
    @Override
    public void gaa(ArrayList<Tuppel> besoekt){
        System.out.println("The cake is a lie, og dette er en feilmelding.");
    }

    public String toString() {
        return "#";
    }

    @Override
    public String charTilTegn() {
        return this.toString();
    }
}