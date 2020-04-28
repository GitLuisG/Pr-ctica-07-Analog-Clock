package edu.upv.poo;

import java.util.EventObject;

public class HourChangedEvent extends EventObject {
   
    public HourChangedEvent(Object source, String hourStr) {
        super(source);
        this.hourStr = hourStr;
    }            
    
    private final String hourStr;
   
    public String getHourStr() { return hourStr; }
    
}
