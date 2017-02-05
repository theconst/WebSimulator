/*
 *
 */
package ua.kpi.atep.services.serialization;


import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.springframework.stereotype.Service;

import ua.kpi.atep.model.entity.Student;


import static ua.kpi.atep.services.serialization.JSONStudentListSerializer.StudentSerilizableProps.GROUP;
import static ua.kpi.atep.services.serialization.JSONStudentListSerializer.StudentSerilizableProps.LOGIN;
import static ua.kpi.atep.services.serialization.JSONStudentListSerializer.StudentSerilizableProps.NAME;
import static ua.kpi.atep.services.serialization.JSONStudentListSerializer.StudentSerilizableProps.VARIANT;

/**
 *
 * @author Home
 */
@Service
public class JSONStudentListSerializer
        implements Serializer<List<Student>, String, String> {

    protected enum StudentSerilizableProps {
        LOGIN, NAME, GROUP, VARIANT;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    @Override
    public String serialize(List<Student> list) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        list.forEach(x -> builder.add(buildStudent(x)));
        
        return builder.build().toString();
    }

    private JsonObject buildStudent(Student s) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add(LOGIN.toString(), s.getLogin());
        builder.add(NAME.toString(), s.getName());
        builder.add(GROUP.toString(), s.getGroup());
        builder.add(VARIANT.toString(), s.getAssignment().getId());

        return builder.build();
    }
}
