package edu.wustl.cab2b.server.util;

import static edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants.IO_0001;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;

// TODO: Auto-generated Javadoc
/**
 * Bulk data loader for any database.
 * @author Chandrakant Talele
 */
public class DataFileLoader implements DataFileLoaderInterface {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = edu.wustl.common.util.logger.Logger.getLogger(DataFileLoader.class);

    /**
     * Loads data from given file into given table.
     * <b> NOTE : </b> This method will not create table in database. It assumes that table is already present
     * @param con Connection to use to fire SQL
     * @param fileName Full path of data file.
     * @param columns Data columns in table. They should be in format (column1,column2,...) braces included
     * @param tableName Name of the table in which data to load.
     * @param dataTypes Data type of each column.
     * @param fieldSeperator String used to separate values of columns in one line of given file.
     * Order of this array must be same as columns names given in "columns" parameter above.
     */
    public void loadDataFromFile(Connection con, String fileName, String columns, String tableName,
                                 Class<?>[] dataTypes, String fieldSeperator) {
        LOGGER.debug("Entering method loadDataFromFile()");

        BufferedReader reader = readFile(fileName);
        int columnCount = columns.split(",").length;
        StringBuffer sql = getSqlString(columns, tableName, columnCount);

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(sql.toString());
            String oneRecord = reader.readLine();
            while (oneRecord != null) {
                String[] values = oneRecord.split(fieldSeperator);
                process(dataTypes, values, columnCount, preparedStatement);
                preparedStatement.executeUpdate();
                preparedStatement.clearParameters();
                oneRecord = reader.readLine();
            }
            reader.close();
            if (!con.getAutoCommit()) {
                con.commit();
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException("Error while loading path information into database", e,
                    ErrorCodeConstants.DB_0006);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException("Error while loading path information into database", e,
                    ErrorCodeConstants.DB_0006);
        } finally {
            close(preparedStatement);
        }
        LOGGER.debug("Leaving method loadDataFromFile()");
    }

    /**
     * Gets the sql string.
     *
     * @param columns the columns
     * @param tableName the table name
     * @param columnCount the column count
     * @return the sql string
     */
    private StringBuffer getSqlString(String columns, String tableName,
            int columnCount)
    {
        StringBuffer sql = new StringBuffer(400);
        sql.append("insert into ").append(tableName).append(' ').append(columns).append(" values(");
        for (int i = 0; i < columnCount; i++) {
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(')');
        return sql;
    }

    /**
     * Process.
     *
     * @param dataTypes the data types
     * @param values the values
     * @param columnCount the column count
     * @param preparedStatement the prepared statement
     * @throws SQLException the sQL exception
     */
    private void process(Class<?>[] dataTypes, String[] values,
            int columnCount, PreparedStatement preparedStatement)
            throws SQLException
    {
        for (int j = 0; j < columnCount; j++)
        {
            if (dataTypes[j].equals(String.class))
            {
                preparedStatement.setString(j + 1, values[j]);
            } else if (dataTypes[j].equals(Long.class))
            {
                preparedStatement.setLong(j + 1, Long.parseLong(values[j]));
            } else if (dataTypes[j].equals(Integer.class))
            {
                preparedStatement.setInt(j + 1, Integer.parseInt(values[j]));
            }
        }
    }

    /**
     * Read file.
     *
     * @param fileName the file name
     * @return the buffered reader
     */
    private BufferedReader readFile(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found :" + fileName, e, IO_0001);
        }
        return reader;
    }

    /**
     * Close.
     *
     * @param preparedStatement the prepared statement
     */
    private void close(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                LOGGER.error(e);
            }
        }
    }
}