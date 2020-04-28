package edu.upv.poo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnalogClockPanel extends JPanel {
    
    public AnalogClockPanel() {
        setPreferredSize(
                new Dimension(PREFERRED_SIZE, PREFERRED_SIZE));
        lastReadT = getHourStr();
        timer.start();
    }
    
    public int getHour12() {
        return Calendar.getInstance().get(Calendar.HOUR);
    }
    
    public int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }
    
    public int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }
    
    public int getSecond() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }
    
    public int getClockSize() {
        int sizeBase = getWidth() < getHeight() ? 
                getWidth() : getHeight();
        return sizeBase - 2 * CLOCK_MARGIN;
    }
    
    public int getCenter() {
        return getClockSize() / 2 + CLOCK_MARGIN;
    }
    
    public String getHourStr() {
        int h = getHour();
        int m = getMinute();
        int s = getSecond();
        return String.format("%02d:%02d:%02d", h, m ,s);
    }
    
    public void addClockListener(ClockListener l) {
        if (l == null || clockListeners.indexOf(l) >= 0) return;
        clockListeners.add(l);
    }
    
    public void removeClockListener(ClockListener l) {
        if (l == null) return;
        clockListeners.remove(l);
    }   
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        drawClockFaceBase(g2);
        drawClockHands(g2);
    }
    
    private static final int CLOCK_MARGIN = 10;
    private static final int REFRESH_RATE = 10;
    private static final int PREFERRED_SIZE = 500;
    
    private Timer timer = new Timer(
            REFRESH_RATE, e -> update());
    private BufferedImage clockFaceBase;
    private String lastReadT;
    private final ArrayList<ClockListener> clockListeners = 
            new ArrayList<>();
    
    private void update() {
        String now = getHourStr();
        if (now.equals(lastReadT)) return;
        lastReadT = now;
        repaint();
        notifiyHourChanged(now);
    }
    
    private void drawLineRadiusBase(
            Graphics g, float angle, int minRad, int maxRad) {
        int x1 = getCenter() + (int)(minRad * Math.sin(angle));
        int y1 = getCenter() + (int)(minRad * Math.cos(angle));
        int x2 = getCenter() + (int)(maxRad * Math.sin(angle));
        int y2 = getCenter() + (int)(maxRad * Math.cos(angle));
        g.drawLine(x1, y1, x2, y2);
    }
    
    private void drawClockFaceBase(Graphics2D g) {
        
        if (clockFaceBase == null || 
                clockFaceBase.getWidth() != getWidth() ||
                clockFaceBase.getHeight() != getHeight()) {
            
            clockFaceBase = (BufferedImage)createImage(
                    getWidth(), getHeight());
            Graphics2D gcfb = clockFaceBase.createGraphics();
            gcfb.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            gcfb.setColor(Color.CYAN);
            gcfb.fillOval(
                    CLOCK_MARGIN, CLOCK_MARGIN, 
                    getClockSize(), getClockSize());
            
            gcfb.setColor(Color.BLACK);
            gcfb.drawOval(
                    CLOCK_MARGIN, CLOCK_MARGIN, 
                    getClockSize(), getClockSize());
            
            // Dibujar las marcars de la caratula del reloj.
            for (int s = 0; s < 60; s++) {
                
                float angleSeg = (float)Math.PI / 30.0f * s;
                int minRadSeg = s % 5 == 0 ?
                        getClockSize() / 2 - 10 :
                        getClockSize() / 2 - 5;
                int maxRadSeg = getClockSize() / 2;
                
                drawLineRadiusBase(
                        gcfb, angleSeg, minRadSeg, maxRadSeg);
                
            }  // end for
            
        }  // end if
        
        g.drawImage(clockFaceBase, null, 0, 0);
        
    }
    
    private void drawClockHands(Graphics g) {
        
        float pi = (float)Math.PI;
        float piX3 = pi * 3f;
        
        // dibujar segundero.
        float second = getSecond();
        int secondRad = 
                getClockSize() / 2 - (int)(getClockSize() * 0.05f);
        float secondAngle = piX3 - pi / 30f * second;
        drawLineRadiusBase(g, secondAngle, 0, secondRad);
        
        // dibujar minutero.
        float minuteF = getMinute() + second / 60f;
        int minuteRad = (int)(secondRad * 3f / 4f);
        float minuteAngle = piX3 - pi / 30f * minuteF;
        drawLineRadiusBase(g, minuteAngle, 0, minuteRad);
        
        // manecilla de la hora.
        float hourF = getHour12() + minuteF / 60f;
        int hourRad = secondRad / 2;
        float hourAngle = piX3 - pi / 30f * 5f * hourF;
        drawLineRadiusBase(g, hourAngle, 0, hourRad);
        
    }
    
    private void notifiyHourChanged(String hourStr) {
        for (ClockListener l : clockListeners)
            l.hourChanged(new HourChangedEvent(this, hourStr));
    }
    
}
