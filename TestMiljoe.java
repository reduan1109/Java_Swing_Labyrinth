import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TestMiljoe {
    public static void main(String[] args) {
        Labyrint test = null;

        try {
            test = new Labyrint("5.in");
        } catch (Exception e) {}

        print("=Printer labyrint");
        print(test);
        test.finnUtveiFra(1,1);
        print("=Loesning funnet: "+test.finnRute(1, 1).hentLoesning());
    }
    static void print(Object o) {
        System.out.println(o);
    }
}