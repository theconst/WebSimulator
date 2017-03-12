/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.kpi.atep.services.serialization;

import org.springframework.stereotype.Service;
import ua.kpi.atep.model.simulation.SimulationOutput;

/**
 *
 * Stub for simulation output
 * 
 * @author Home
 */
@Service
public class SimulationOutputSerializer 
        implements Serializer<SimulationOutput, String, String> {  
    
    @Override
    public String serialize(SimulationOutput output) {
        return output.toString();
    }
}
