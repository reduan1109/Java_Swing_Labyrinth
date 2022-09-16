import java.util.*;

//Rute klassen min
abstract class Rute {
    //Static liste over løsning, burde være instansvariabel til den klassen gaa ble først kallt på, men dette er enklere
    protected static ArrayList<ArrayList<Tuppel>> loesning = new ArrayList<>();
    //Alle rutene har tilgang til rutenettet det tilhører
    protected Labyrint ruteNett;
    //dette flagget er flagget jeg bruker i besøkt
    protected boolean flagg;
    //naboene til ruten
    protected Rute naboNord, naboSoer, naboOest, naboVest;
    //koordinater
    protected int rad, kollonne;

    //konstruktøren er selvforklarende
    protected Rute(int rad, int kollonne, Labyrint labyrint) {
        this.rad = rad;
        this.kollonne = kollonne;
        this.ruteNett = labyrint;
    }

    //NAboen nord for denne ruten er en rad over, men på samme kollone. Naboen øst er på en kollone til høyre, men på samme rad osv osv
    public void finnNaboer() {
        //kaller på labyrints finnrute med riktif koordinater fordi det er enklest
        naboNord = ruteNett.finnRute(kollonne, (rad-1));
        naboSoer = ruteNett.finnRute(kollonne, (rad+1));
        naboOest = ruteNett.finnRute((kollonne+1), rad);
        naboVest = ruteNett.finnRute((kollonne-1), rad);
    }
    
    //det står ikke noe om returverdi i oppgaveteksten (?) og derme har jeg bestemt at dette er nok den ekleste løsningen for seinere bruk
    public ArrayList<ArrayList<Tuppel>> finnUtvei(){
        //kaller gaa (polymorfi) og returnerer løsning
        this.gaa(new ArrayList<>());
	ArrayList<ArrayList<Tuppel>> bro = loesning;
	loesning = new ArrayList<>();
        return bro;
    }

    //abstracte metoder
    public abstract String charTilTegn();
    //protected fordi den ikke skal kalles på utenifra, det er det finnutvei er for 
    protected abstract void gaa(ArrayList<Tuppel> besoekt);

    //hent metoder, selvforklarende
    public ArrayList<ArrayList<Tuppel>> hentLoesning(){return loesning;}
    public int hentKollonne(){return kollonne;}
    public int hentRad(){return rad;}
}
