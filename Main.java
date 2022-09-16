import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Main {
    //Disse variablene er statiske fordi det var den beste workarounden jeg fant for "must be final or effectivly finall error"
    static ArrayList<ArrayList<Rute>> labArray;
    static Labyrint lab1;
    public static void main(String[] args) {
        //Denne blokken med kode henter absolutt filbane til fil
        //TODO: burde egentlig sende filobjektet direkte, men da maa jeg endre paa ting og det er ork
        JFileChooser velger = new JFileChooser(new File(System.getProperty("user.dir")));
        if (velger.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) System.exit(1);
        String filbane = velger.getSelectedFile().getAbsolutePath();

        //finner extension paa fil, sikkert en allerede laget metode i file, men jeg skjenner ikke til den
        String extension = "";
        int i = filbane.lastIndexOf('.');
        if (i > 0) {
            extension = filbane.substring(i+1);
        }

        //TODO: burde kanskje være en dialogboks her, saann at brukeren vet hva som har skjedd
        if (!extension.equals("in")) System.exit(1);

        //Lager labyrint objekt, burde ha en dialogboks for aa si at neo gikk galt
        //forventer ikke at noe skal gaa galt fordi, da ville det ha gaatt galt over
        try {
            lab1 = new Labyrint(filbane);
        } catch (Exception e) {
            //TODO: dialogboks her
            System.exit(1);
        }
        labArray = lab1.hentAlleRader();

        /*-----FRAME-----*/
        JFrame vindu = new JFrame("Labyrint");
        vindu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /*-----FRAME-----*/

        /*-----TOP-----*/
        JPanel top = new JPanel();
        JLabel topLabel = new JLabel("Trykk paa en hvit rute");
        top.add(topLabel);
        /*-----TOP-----*/
        
        /*-----BODY-----*/
        //Panel og layout
        JPanel mid = new JPanel();
        JPanel labyrintGrid = new JPanel(new GridLayout(lab1.hentAntallRader(), lab1.hentAntallKolonner()));
        
        //TODO: sjekk om linjene i kommentaren under er riktig
        //Du brude kanskje lese linje 131-139 før du leser dette
        class RuteKnapp extends JButton {
            //Lagrer ruten paa denne posisjonen
            Rute rute = null;

            RuteKnapp (Rute rute) {
                //For Mac brukerne der ute, og fargeblinde
                super(rute.toString());
                this.rute = rute;
            }
        
            class Loes implements ActionListener {
                @Override
                public void actionPerformed (ActionEvent e) {
                    //Finner løsninger
                    ArrayList<ArrayList<Tuppel>> loesninger = lab1.finnUtveiFra(rute.hentKollonne(), rute.hentRad());
                    int antloesnigner = loesninger.size();

                    //Makes no fucking sense bro if - if - if funker ikke, men if - else if - else funker?!?
                    //Endrer på label sånn at bruker vet hvor mange ruter som ble funnet
                    if (antloesnigner == 0) {topLabel.setText("Det ble ikke funnet noen loesninger fra den ruten");}
                    else if (antloesnigner == 1) {topLabel.setText("Det ble funnet 1 loesning fra den ruten.");}
                    else {topLabel.setText("Det ble funnet "+antloesnigner+" loesninger, korteste vises.");}

                    //looper for aa finne den korteste loesnignen
                    ArrayList<Tuppel> kortestLoesning = null;
                    try {
                        kortestLoesning = loesninger.get(0);
                        for (ArrayList<Tuppel> loesning : loesninger) {
                            if (kortestLoesning.size() > loesning.size()) kortestLoesning = loesning;
                        }
                    } catch (IndexOutOfBoundsException bro) {
                        //TODO: dialog boks som sier at det ikke er noen loesninger der, kanskje farge rute rød
                        return;
                    }

                    //removeAll fordi det ser ut som om det er "umulig" (relativt vanskelig) aa faa tak i spesefike koordinater i layoutgrid (med mindre jeg lagrer knappepekerne i en array)
                    labyrintGrid.removeAll();

                    //Looper over hele labyrinten for aa bygge opp gridet igjen
                    for (int i = 0; i < labArray.size(); i++) {
                        ArrayList<Rute> rad = labArray.get(i);
                        for (int j = 0; j < rad.size(); j++) {
                            Rute r = rad.get(j);
                            
                            //lager ruteknappen
                            //legg til knapp og initier
                            RuteKnapp plz = new RuteKnapp(r);
                            plz.initGUI(); 
                            labyrintGrid.add(plz);

                            //for hver tuppel i loesningen...
                            for (Tuppel tuppel : kortestLoesning) {
                                //sjekk om knappen vi behandler er plasert paa tuppelets koordinater...
                                if (r.hentKollonne() == tuppel.hentKollone() && r.hentRad() == tuppel.hentRad()){
                                    //...hvis ja set farge grønn, ellers ingenting
                                    plz.setText("+");
                                    plz.setBackground(Color.GREEN);
                                }
                            }
                        }
                    }
                    
                    //validate og paint isteden for pack (?)
                    //Note to self; validate og paint, er omega quick i forhold til repack
                    mid.revalidate();
                    mid.repaint();
                    //vindu.pack();
                }
            }
        
            void initGUI () {
                //litt styling, funker daarlig paa Mac, men jeg er prosa student
                setMargin(new Insets(1,1,1,1));
                //enkleste måten å få litt dynamisk styling
                if (lab1.hentAntallRader()>50) setPreferredSize(new Dimension(15, 15));
                else if (lab1.hentAntallRader()>30) setPreferredSize(new Dimension(20, 20));
                else if (lab1.hentAntallRader()>20) setPreferredSize(new Dimension(30, 30));
                else {setPreferredSize(new Dimension(45, 45));}
                
                if (rute instanceof SortRute) {
                    setBorderPainted(false);
                    setBackground(Color.BLACK);
                } else {
                    setBackground(Color.WHITE);
                    addActionListener(new Loes());
                }
            }
        }

        //Bygger rutenettet første gang
        for (ArrayList<Rute> rad : labArray) {
            for (Rute rute : rad) {
                RuteKnapp rk = new RuteKnapp(rute);
                labyrintGrid.add(rk);
                rk.initGUI();
            }
        }
        //legger til rutenettet for første gang
        mid.add(labyrintGrid);
        /*-----BODY-----*/

        //Ny labyrint knapp + panel
        JPanel bottom = new JPanel();
        JButton nyLabKnapp = new JButton("Ny Labyrint");
        //Kunne laget en egen kanpp klasse men det føltest litt unødvendig, saa jeg lagde bare en actionlistener
        class NyLabyrint implements ActionListener {
            @Override
            public void actionPerformed (ActionEvent e) {
                //finner fil
                JFileChooser velger = new JFileChooser(new File(System.getProperty("user.dir")));
                //bare en return, fordi det kan være slikt at brukeren simpelt hen ombestemte seg
                if (velger.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;

                String filbane = velger.getSelectedFile().getAbsolutePath();
                //finner extension paa fil, sikkert en allerede laget metode i file, men jeg skjenner ikke til den
                String extension = "";
                int i = filbane.lastIndexOf('.');
                if (i > 0) {
                    extension = filbane.substring(i+1);
                }

                //TODO: burde kanskje være en dialogboks her, saann at brukeren vet hva som har skjedd
                if (!extension.equals("in")) System.exit(1);
        
                //Lager labyrint, burde ha en dialogboks for aa si at neo gikk galt
                Labyrint lab = null;
                try {
                    lab = new Labyrint(filbane);
                } catch (Exception plz) {
                    //TODO: Dialogboks her
                    System.exit(1);
                }
                //oppdaterer static saann at alt faar tilgang
                setLab(lab);
                setLabArray(lab.hentAlleRader());

                //fjerner items i grid
                labyrintGrid.removeAll();
                //setter nytt layout med hensyn til nye dimensjoner
                labyrintGrid.setLayout(new GridLayout(lab.hentAntallRader(), lab.hentAntallKolonner()));

                //bygger
                for (ArrayList<Rute> rad : labArray) {
                    for (Rute rute : rad) {
                        RuteKnapp rk = new RuteKnapp(rute);
                        labyrintGrid.add(rk);
                        rk.initGUI();
                    }
                }

                topLabel.setText("Trykk paa en hvit rute...");

                //quickness big business
                bottom.revalidate();
                bottom.repaint();
                //vindu.pack();
            }
        }

        //legger til knapp + actionlistner
        nyLabKnapp.addActionListener(new NyLabyrint());
        bottom.add(nyLabKnapp);

        //TODO: legg til en exitknapp

        //legger til alt
        vindu.setLayout(new BorderLayout());
        vindu.add(top, BorderLayout.NORTH);
        vindu.add(mid, BorderLayout.CENTER);
        vindu.add(bottom, BorderLayout.SOUTH);
        vindu.pack();
        vindu.setVisible(true);
    }

    public static void setLabArray(ArrayList<ArrayList<Rute>> array){
        labArray = array;
    }

    public static void setLab(Labyrint labyrint){
        lab1 = labyrint;
    }
}