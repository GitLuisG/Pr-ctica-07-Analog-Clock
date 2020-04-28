package edu.upv.poo;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Practica07 
        extends JFrame
        implements ClockListener {

    public static final String TITLE = "Analog Clock";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Practica07().run();
    }
    
    public Practica07() {
        super(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        AnalogClockPanel analogClockPanel =
                new AnalogClockPanel();
        getContentPane().add(
                analogClockPanel, BorderLayout.CENTER);
        analogClockPanel.addClockListener(this);
        pack();
    }
    
    public void run() {
        setVisible(true);
    }

    @Override
    public void hourChanged(HourChangedEvent e) {
        setTitle(TITLE + " | " + e.getHourStr());
    }
    
}
