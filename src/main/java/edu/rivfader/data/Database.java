package edu.rivfader.data;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.EOFException;

/**
 * Class which encapsulates access to the database files.
 * @author harald
 */
public class Database {
    /**
     * contains the suffix for all table variables.
     */
    private static final String TABLE_SUFFIX = ".table";
    /**
     * contains the name of the catalogue file.
     */
    private static final String CATALOGUE_FILE = "cataloge";
    /**
     * contains the base name of the database.
     */
    private final File baseFolder;
    /**
     * contains the table currently open for output, if outBuffer != null.
     */
    private String tableOpenForOutput;
    /**
     * contains a stream into the currently open output table.
     */
    private ObjectOutputStream outBuffer;
    /**
     * contains the name of the outbuffer file.
     */
    private File outBufferFile;
    public Database(final String databaseFolder) {
        baseFolder = new File(databaseFolder);
    }

    /**
     * Implements an object output stream that outputs no
     * header (which makes it possible to append to another object
     * output stream that way)
     * @author harald
     */
    private static class AppendingObjectOutputStream
            extends ObjectOutputStream {
        /**
         * constructs a new appending object output stream.
         * @param es a place to write to
         */
        public AppendingObjectOutputStream(final OutputStream os)
            throws IOException {
            super(os);
        }

        @Override
        protected void writeStreamHeader() {
            // nothing
        }
    }

    /**
     * loads the table and returns an iterator over the rows.
     * @param tableName the name of the table to load
     * @return a row iterator.
     */
    public Iterator<Row> loadTable(final String tableName) throws IOException {
        ObjectInputStream input = new ObjectInputStream(
                                    new FileInputStream(
                                        new File(baseFolder,
                                            tableName+TABLE_SUFFIX)));
        return new RowReaderIterator(input);
    }

    /**
     * creates a new table.
     * @param tableName the name of the table to create.
     * @param columnNames the names of the columns, the type is varchar.
     */
    public void createTable(final String tableName,
                            final List<String> columnNames)
        throws IOException {
        if(!baseFolder.exists()) {
            if(!baseFolder.mkdir()) {
                throw new IOException("Cannot create database folder");
            }
            Map<String, List<String>> cataloge =
                new HashMap<String, List<String>>();
            ObjectOutputStream output = new ObjectOutputStream(
                                            new FileOutputStream(
                                                new File(baseFolder,
                                                         CATALOGUE_FILE)));
            output.writeObject(cataloge);
            output.close();
        }

        Map<String, List<String>> catalog;
        ObjectInputStream input = new ObjectInputStream(
                                    new FileInputStream(
                                        new File(baseFolder,
                                                 CATALOGUE_FILE)));
        try {
            catalog = (Map<String, List<String>>) input.readObject();
        } catch(ClassNotFoundException e) {
            throw new IOException("catalogue file borked", e);
        }
        input.close();

        catalog.put(tableName, columnNames);

        ObjectOutputStream output = new ObjectOutputStream(
                                    new FileOutputStream(
                                        new File(baseFolder, CATALOGUE_FILE)));
        output.writeObject(catalog);
        output.close();

        File tableFile = new File(baseFolder, tableName+TABLE_SUFFIX);
        tableFile.createNewFile();
        output = new ObjectOutputStream(
                                        new FileOutputStream(
                                            tableFile));
        output.close();
    }

    /**
     * deletes a table if it exists.
     * @param tableName the name of the table to delete.
     */
    public void dropTable(final String tableName)
        throws IOException {
        File doomedFile = new File(baseFolder, tableName+TABLE_SUFFIX);
        if(!doomedFile.exists()) {
            return;
        }
        if(!doomedFile.delete()) {
            throw new IOException("Table " + tableName
                                   + " could not be deleted");
        }
        Map<String, List<String>> catalogue;
        ObjectInputStream input = new ObjectInputStream(
                                    new FileInputStream(
                                        new File(baseFolder, CATALOGUE_FILE)));
        try {
            catalogue = (Map<String, List<String>>) input.readObject();
        } catch(ClassNotFoundException e) {
            throw new IOException("catalogue file broken", e);
        }
        input.close();
        catalogue.remove(tableName);
        ObjectOutputStream output = new ObjectOutputStream(
                                        new FileOutputStream(
                                            new File(baseFolder,
                                                CATALOGUE_FILE)));
        output.writeObject(catalogue);
        output.close();
    }

    /**
     * stores a row in a table.
     * @param tableName the name of the table to store to.
     * @param row the row to store.
     */
    public void storeRow(final String tableName, final Row row)
        throws IOException{
        if(outBuffer == null) {
            throw new IllegalStateException("Buffer not open");
        }
        if(!tableName.equals(tableOpenForOutput)) {
            throw new IllegalStateException("Writing to wrong table?");
        }

        outBuffer.writeObject(row);
    }

    /**
     * opens a table for writing.
     * @param tableName the table to open
     */
    public void openTableForWriting(final String tableName)
        throws IOException {
        if(outBuffer != null) {
            throw new IllegalStateException("Buffer already open");
        }
        tableOpenForOutput = tableName;
        outBufferFile = File.createTempFile("table", "out");
        outBuffer = new ObjectOutputStream(
                        new FileOutputStream(outBufferFile));
    }

    /**
     * closes the table.
     * @param tableName the table to close
     */
    public void closeTable(final String tableName)
        throws IOException {
        if (outBuffer != null) {
            outBuffer.close();
            ObjectInputStream input = new ObjectInputStream(
                            new FileInputStream(outBufferFile));
            ObjectOutputStream output = new ObjectOutputStream(
                            new FileOutputStream(
                                new File(baseFolder, tableName+TABLE_SUFFIX)));
            while(true) {
                try {
                    output.writeObject(input.readObject());
                } catch(ClassNotFoundException e) {
                    throw new IOException("temp file was borked", e);
                } catch(EOFException e) {
                    break;
                }
            }
            input.close();
            output.close();
        }
        outBuffer = null;
    }

    /**
     * appends a row to a table.
     * @param tableName the table to append to
     * @param row the row to append
     */
    public void appendRow(final String tableName, final Row row)
        throws IOException{
        ObjectOutputStream output = new AppendingObjectOutputStream(
                                        new FileOutputStream(
                                            new File(baseFolder,
                                                     tableName+TABLE_SUFFIX),
                                            true));
        output.writeObject(row);
        output.close();
    }

    /**
     * returns the list of column names of a table.
     * @param tableName the name of the table
     */
    public List<String> getColumnNames(final String tableName)
        throws IOException {
        ObjectInputStream input = new ObjectInputStream(
                        new FileInputStream(
                            new File(baseFolder, CATALOGUE_FILE)));
        Map<String, List<String>> catalog;
        try {
            catalog = (Map<String, List<String>>) input.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("catalogue file borked", e);
        }
        input.close();
        return catalog.get(tableName);
    }

    /**
     * Iterates over all rows contained in an ObjectInputStream.
     */
    private static class RowReaderIterator implements Iterator<Row> {
        /**
         * contains the input stream to iterate over.
         */
        private ObjectInputStream input;

        private Row nextRow;
        private boolean nextRowValid;

        /**
         * constructs a new RowReaderIterator.
         * @param pInput the input stream to read from
         */
        public RowReaderIterator(final ObjectInputStream pInput) {
            input = pInput;
        }

        @Override
        public boolean hasNext() {
            if(nextRowValid) {
                return true;
            }

            try {
                nextRow = (Row) input.readObject();
            } catch(ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch(EOFException e) {
                return false;
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
            nextRowValid = true;
            return true;
        }

        @Override
        public Row next() {
            if(hasNext()) {
                nextRowValid = false;
                return nextRow;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
