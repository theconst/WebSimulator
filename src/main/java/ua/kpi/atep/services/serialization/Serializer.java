/*
 * Abstract serializer
 */
package ua.kpi.atep.services.serialization;

/**
 *
 * @author Home
 * @param <T>
 * @param <IN>
 * @param <OUT>
 */
public interface Serializer<T, IN, OUT> {

    default T deserialize(IN input) throws SerializationException {
        throw new UnsupportedOperationException();
    }

    default OUT serialize(T object) throws SerializationException {
        throw new UnsupportedOperationException();
    }

}
