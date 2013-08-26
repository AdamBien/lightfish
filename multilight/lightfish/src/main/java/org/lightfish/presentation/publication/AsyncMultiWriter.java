package org.lightfish.presentation.publication;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author rveldpau
 */
public class AsyncMultiWriter extends Writer {

    private Collection<Writer> writers = new ArrayList<>();

    public void addWriter(Writer writer) {
        this.writers.add(writer);
    }

    public void removeWriter(Writer writer) {
        this.writers.remove(writer);
    }

    public void clearWriters() {
        this.writers.clear();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        IOException lastException = null;
        for (Writer writer : writers) {
            try {
                writer.write(cbuf, off, len);
            } catch (IOException ioe) {
                lastException = ioe;
            }
        }
        if (lastException != null) {
            throw lastException;
        }
    }

    @Override
    public void flush() throws IOException {
        IOException lastException = null;
        for (Writer writer : writers) {
            try {
                writer.flush();
            } catch (IOException ioe) {
                lastException = ioe;
            }
        }
        if (lastException != null) {
            throw lastException;
        }
    }

    @Override
    public void close() throws IOException {
        IOException lastException = null;
        for (Writer writer : writers) {
            try {
                writer.close();
            } catch (IOException ioe) {
                lastException = ioe;
            }
        }
        if (lastException != null) {
            throw lastException;
        }
    }
}
