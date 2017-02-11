/*
 * JsonDecoder.java 26.09.2016
 */
package ua.kpi.atep.controller.socket;

import java.io.StringReader;
import javax.json.Json;

import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Decodes the input data from the user
 *
 * @author Konstantin Kovalchuk
 */
public class JsonDecoder implements Decoder.Text<JsonObject> {

    @Override
    public JsonObject decode(String message) throws DecodeException {
        return Json.createReader(new StringReader(message)).readObject();
    }

    @Override
    public boolean willDecode(String message) {
        // exeptional behaviour for non-json messages
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
        //        do nothing
    }

    @Override
    public void destroy() {
        //        do nothing
    }
    
}
