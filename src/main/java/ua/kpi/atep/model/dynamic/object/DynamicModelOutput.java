/*
 * DynamicModelOutput.java
 */
package ua.kpi.atep.model.dynamic.object;

import java.util.Arrays;

/**
 *
 * @author Home
 */
public class DynamicModelOutput extends ProcessVariable {
 
    private double[] alarmHi;
    
    private double[] alarmLow;
    
    private double[] alarmHiCritical;
    
    private double[] alarmLowCritical;
    
    
    public DynamicModelOutput(String[] names, double[] initial) {
        super(names, initial);
    }
    
    public double[] getAlarm(AlarmType alarm) {
        return getAlarm(alarm, false);
    }

    public double[] getAlarm(AlarmType alarm, boolean deep) {
        switch(alarm) {
            case HI: 
                return getAlarmHi(deep);
            case LOW:
                return getAlarmLow(deep);
            case HI_CRITICAL:
                return getAlarmHiCritical(deep);
            case LO_CRITICAL:
                return getAlarmLowCritical(deep);
            default:
                throw new RuntimeException();
        }
    }
    
    public double[] getAlarmHi() {
        return getAlarmHi(false);
    }

    public double[] getAlarmHi(boolean deep) {
        return getCopyOf(alarmHi, deep);
    }

    public void setAlarmHi(double[] alarmHi) {
        checkEquals(size(), alarmHi.length);
        this.alarmHi = Arrays.copyOf(alarmHi, alarmHi.length);
    }

    public double[] getAlarmLow(boolean deep) {
        return getCopyOf(alarmLow, deep);
    }
    
    public double[] getAlarmLow() {
        return getAlarmLow(false);
    }

    public void setAlarmLow(double[] alarmLow) {
        checkEquals(size(), alarmLow.length);
        this.alarmLow = Arrays.copyOf(alarmLow, alarmLow.length);
    }
    
    public double[] getAlarmHiCritical() {
        return getAlarmHiCritical(false);
    }

    public double[] getAlarmHiCritical(boolean deep) {
        return getCopyOf(alarmHiCritical, deep);
    }

    public void setAlarmHiCritical(double[] alarmHiCritical) {
        checkEquals(size(), alarmHiCritical.length);
        this.alarmHiCritical = Arrays.copyOf(alarmHiCritical, 
                alarmHiCritical.length);
    }

    public double[] getAlarmLowCritical(boolean deep) {
        return getCopyOf(alarmLowCritical, deep);
    }
    
    public double[] getAlarmLowCritical() {
        return getAlarmLowCritical(false);
    }

    public void setAlarmLowCritical(double[] alarmLowCritical) {
        checkEquals(size(), alarmLowCritical.length);
        this.alarmLowCritical = Arrays.copyOf(alarmLowCritical,
                alarmLowCritical.length);
    }
       
}
