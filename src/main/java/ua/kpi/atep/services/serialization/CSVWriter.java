/*
 * CSVWriter.java
 */
package ua.kpi.atep.services.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Writer of double values separated by commas according to format
 * String
 * 
 * tightly coupled to simulation web socket
 *
 * @author Home
 */
public class CSVWriter extends Writer {

    public static final String DEFAULT_SEPARATOR = "\t";

    public static final String DEFAULT_FORMAT = "%.2f";

    private static final String USE_WRITE_CHUNK_INSTEAD
            = "Use writeChunk instead";

    private static final String LENGTHS_MUST_CORRESPOND
            = "Lengths must correspond";
    private static final String HEADERS_NOT_CORRESPOND_TO_VALUES
            = "Headers not correspond to values";

    public static final String DEFAULT_LINE_SEPARATOR = System.lineSeparator();

    private final ByteArrayOutputStream bytes;

    private final PrintWriter writer;

    private String format = DEFAULT_FORMAT;
    private String separator = DEFAULT_SEPARATOR;
    private String lineSeparator = DEFAULT_LINE_SEPARATOR;
    private final int width;
    private final int size;

    /**
     *
     * @param size maximum allowed size
     * @param headers headers of the sequence
     */
    public CSVWriter(int size, String... headers) {
        this.size = size;
        this.width = headers.length;
        bytes = new ByteArrayOutputStream(size);
        writer = new PrintWriter(bytes, true);

        for (String header : headers) {
            writer.append(header).append(this.separator);
        }
        writer.append(this.lineSeparator);
    }

    public void writeChunk(double[]... valuesChunk) {
        if (valuesChunk.length != width) {
            throw new IllegalArgumentException(HEADERS_NOT_CORRESPOND_TO_VALUES);
        }

        int chunkLength = valuesChunk[0].length;

        for (double[] chunk : valuesChunk) {
            if (chunk.length != chunkLength) {
                throw new IllegalArgumentException(LENGTHS_MUST_CORRESPOND);
            }
        }

        double[] tuple = new double[width];

        for (int k = 0; k < chunkLength; k++) {
            for (int i = 0; i < tuple.length; ++i) {
                tuple[i] = valuesChunk[i][k];
            }
            writeTuple(tuple);
        }
    }

    private void writeTuple(double... valuesTuple) {
        assert (valuesTuple.length == width);

        for (double num : valuesTuple) {
            writer.printf(format + separator, num);
        }
        writer.print(lineSeparator);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        throw new UnsupportedOperationException(USE_WRITE_CHUNK_INSTEAD);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
    
    public int getSize() {
        return bytes.size();
    }

    @Override
    public String toString() {
        return bytes.toString();
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

}
