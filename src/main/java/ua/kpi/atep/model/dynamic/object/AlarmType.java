/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kpi.atep.model.dynamic.object;

/**
 *
 * @author Home
 */
public enum AlarmType {
    HI, LOW, HI_CRITICAL, LO_CRITICAL;

    @Override
    public String toString() {
        switch (this) {
            case HI:
                return "high";
            case LOW:
                return "low";
            case HI_CRITICAL:
                return "high critical";
            case LO_CRITICAL:
                return "low critical";
            default:
                throw new RuntimeException();
        }
    }
}
