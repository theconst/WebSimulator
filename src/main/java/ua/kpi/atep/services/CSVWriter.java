/*
 * CSVStream.java
 */
package ua.kpi.atep.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 *
 * @author Home
 */
public class CSVWriter extends Writer {
    
    private static final int SIZE = 3072;

    public static final String DEFAULT_SEPARATOR = ",";

    public static final String DEFAULT_LINE_SEPARATOR = System.lineSeparator();
    
    private final ByteArrayOutputStream bytes = new ByteArrayOutputStream(SIZE);

    private final PrintWriter writer = new PrintWriter(bytes, true);

    private final String separator;
    private final String lineSeparator;
    private final int width;
    
    public CSVWriter(String... headers) {
        this(DEFAULT_SEPARATOR, DEFAULT_LINE_SEPARATOR, headers);
    }

    public CSVWriter(String separator, String lineSeparator, String... headers) {
        this.separator = separator;
        this.lineSeparator = lineSeparator;
        this.width = headers.length;

        for (String header : headers) {
            writer.append(header).append(this.separator);
        }

        writer.append(this.lineSeparator);
    }

    public void writeChunk(double[]... valuesChunk) {
        if (valuesChunk.length != width) {
            throw new IllegalArgumentException("Headers not correspond to values");
        }
        
        int chunkLength = valuesChunk[0].length;
        
        for (double[] chunk : valuesChunk) {
            if (chunk.length != chunkLength) {
                throw new IllegalArgumentException("Lengths must correspond");
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
        assert(valuesTuple.length == width);
        
        for (double num : valuesTuple) {
            writer.print(num);
            writer.print(separator);
        }
        writer.print(lineSeparator);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        
        throw new UnsupportedOperationException("Use writeChunk instead");
//        wrapped.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
    
    @Override
    public String toString() {
        return bytes.toString();
    }
}
