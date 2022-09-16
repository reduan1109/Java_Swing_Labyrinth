import java.util.*;

public class HvitRute extends Rute {
    //konstruktøren for klassen, kaller på super konstr.
    public HvitRute(int rad, int kollonne, Labyrint labyrint) {
        super(rad, kollonne, labyrint);
    }

    //overrider super.gaa fordi polymorfi
    @Override
    public void gaa(ArrayList<Tuppel> besoekt){
        //setter flagget lik true for å signalisere at vi har vært her
        flagg = true;

        //lager en fresh tom vei som vi kan bruke for å "mappe" labyrinten med 
        ArrayList<Tuppel> nyVei = new ArrayList<>(besoekt);
        //legger til denne ruten som et tuppel for å holde styr på hvor vi har vært
        nyVei.add(new Tuppel(kollonne, rad));

        //kaller gå på alle de andre naboene, hvis de ikke er null, og vi ikke har vært der tidligere
        if (naboNord != null) {if (!naboNord.flagg) naboNord.gaa(nyVei);}
        if (naboSoer != null) {if (!naboSoer.flagg) naboSoer.gaa(nyVei);}
        if (naboOest != null) {if (!naboOest.flagg) naboOest.gaa(nyVei);}
        if (naboVest != null) {if (!naboVest.flagg) naboVest.gaa(nyVei);}
        
        //setter flagget tilbake for å lagge krøll på seinere kall
        flagg = false;
    }

    public String toString() {
        return ".";
    }

    @Override
    public String charTilTegn() {
        return this.toString();
    }

}