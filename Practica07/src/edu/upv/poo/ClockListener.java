package edu.upv.poo;

import java.util.EventListener;

public interface ClockListener extends EventListener {
    
    void hourChanged(HourChangedEvent e);
    
}
