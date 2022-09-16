public class Tuppel {
    int kollone, rad;

    public Tuppel(int kollone, int rad) {
        this.kollone = kollone;
        this.rad = rad;
    }

    public int hentKollone(){return kollone;}
    public int hentRad(){return rad;}

    //public String toString(){return "(" + (kollone+1) + ", " + (rad+1) + ")";}
    public String toString(){return "(" + kollone + ", " + rad + ")";}
}