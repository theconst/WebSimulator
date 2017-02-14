/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kpi.atep.model.dynamic.object;

import java.util.Arrays;

/**
 *
 * @author Home
 */
public class Output extends ProcessVariableVector {
 
    private double[] alarmHi;
    
    private double[] alarmLow;
    
    private double[] alarmHiCritical;
    
    private double[] alarmLowCritical;
    
    
    public Output(String[] names, double[] initial) {
        super(names, initial);
    }
    
    public Output(Output other) {
        this(other.getNames(), other.getInitial());
        this.alarmHi = other.getAlarmHi(true);
        this.alarmLow = other.getAlarmLow(true);
        this.alarmLowCritical = other.getAlarmLowCritical(true);
        this.alarmHiCritical = other.getAlarmHiCritical(true);
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
