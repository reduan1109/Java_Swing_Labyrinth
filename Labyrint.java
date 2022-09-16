import java.util.*;
import java.io.*;

public class Labyrint {
    //lagrer rutenettet i denne arraylisten
    ArrayList<ArrayList<Rute>> alleRader = new ArrayList<>();
    //disse er egentlig ikke brukt til noe særlig, men hvis jeg føler at programme går litt tregt kan jeg bruke disse til å lagge arrays av den primitive typen
    int antallRader, antallKolonner;

    //konstruktøren for klassen Labyrint, det er her det meste skjer
    public Labyrint(String filNavn) throws FileNotFoundException {
        //det er en forutsetning at labyrintene ligger i en mappe kallt labyrinter
        //metoden fileToString finner du på bunnen, den gjør akkuratt det den sier at den gjør altså gjør om en fil om til en tekststreng
        
        String filData = this.fileToString(filNavn);
        //Splitter på linjesjift og lager en arraylist av arrayen
        ArrayList<String> filDataArray = new ArrayList<>(Arrays.asList(filData.split("\n")));

        //Lagrer dimensjonene i hver sin variabel, og fjerner dimensjonene fra filfataene for enklere behandling
        String[] areal = filDataArray.get(0).split(" ");
        filDataArray.remove(0);
        antallRader = Integer.parseInt(areal[0]);
        antallKolonner = Integer.parseInt(areal[1]);
        
        //metodene finner du i bunnen av dokumentet
        //lagRutenett fyller alleRader med dataene fra filen, ekskludert dimensjonene
        //fordelNaboer finner naboene til rutene og assigner dem til den ruten
        this.lagRutenett(filDataArray);
        this.fordelNaboer();
    }

    //Returnerer alleRader, altså rutenettet
    public ArrayList<ArrayList<Rute>> hentAlleRader() {return alleRader;}
    public int hentAntallKolonner() {return antallKolonner;}
    public int hentAntallRader() {return antallRader;}

    //En metode som finner en vilkårlig rute
    public Rute finnRute(int kollonne, int rad) {
        try {
            return alleRader.get(rad).get(kollonne);
        } catch (Exception e) {
            //Returnerer null ved nullPointerException - hvis ikke den finnes i rutenettet
            return null;
        } 
    }

    //finnUtveiFra metodenn kaller på finnrute med koordinater
    public ArrayList<ArrayList<Tuppel>> finnUtveiFra(int kollonne, int rad ) throws UnsupportedOperationException{
        Rute start = this.finnRute(kollonne, rad);

        //hvis den ikke finnes eller er sort, throw exception
        if (start == null) throw new UnsupportedOperationException("Rute (" +kollonne+ ", " +rad+ ") er en sort rute, eller finnes ikke.");
        if (start.toString().equals("#")) throw new UnsupportedOperationException("Rute (" +kollonne+ ", " +rad+ ") er en sort rute, eller finnes ikke.");

        //ellers kall finnUtvei på rute
        return start.finnUtvei();
    }

    //Selvforklarende toString metode med en stringBuilder
    public String toString() {
        StringBuilder output = new StringBuilder();
        
        output.append(antallRader + ", " + antallKolonner + "\n");
        for (ArrayList<Rute> rad : alleRader) {
            for (Rute rute : rad) {
                output.append(rute.toString());
            }
            output.append("\n");
        }
        
        return output.toString();
    }

    //Denne her er ganske enkel og selvforklarende
    private String fileToString(String filNavn) throws FileNotFoundException {
        StringBuilder output = new StringBuilder();
        Scanner scann = new Scanner(new File(filNavn));
            
        while (scann.hasNext()) {
            output.append(scann.nextLine() + "\n");
        }
        
        scann.close();
        return output.toString();
    }
    
    //Denne looper igjennom rutene og kaller finnNabo på ruten
    private void fordelNaboer() {
        for (int i = 0; i < alleRader.size(); i++) {

            ArrayList<Rute> rad = alleRader.get(i);
            for (int j = 0; j < rad.size(); j++) {
                Rute rute = rad.get(j);
                rute.finnNaboer();
            }
        }
    }

    //Denne lopper over fildataene og lager rutenettet
    private void lagRutenett(ArrayList<String> filDataArray) {
        //for hver linje i filDataArray (filen splittet i linjer)...
        for (int i = 0; i < filDataArray.size(); i++) {
            String linje = filDataArray.get(i).trim();
            //...og for hvert symbol...
            ArrayList<Rute> rad = new ArrayList<>();
            for (int j = 0; j < linje.length(); j++) {
                //...sjekk hvilken type det er, hvis den er sort...
                if (linje.charAt(j) == '#') {
                    //...lag en sortRute og legg til...
                    rad.add(new SortRute(i, j, this));
                } 
                //...sjekk om den er på kanten av labyrintet
                else if (j == 0 ||i == 0 || j >= linje.length()-1 || i >= filDataArray.size()-1 ) {
                    //...lag aapning...
                    rad.add(new Aapning(i, j, this));
                } else {
                    //ellers lag hvitRute.
                    rad.add(new HvitRute(i, j, this));
                }
            }
            //en linje er nå behandlet, legg til i alleRader - repeat.
            alleRader.add(rad);
        }
    }
}
