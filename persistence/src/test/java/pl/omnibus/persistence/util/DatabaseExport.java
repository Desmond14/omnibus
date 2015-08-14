package pl.omnibus.persistence.util;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

public final class DatabaseExport {
    private static final String CONNECTION_URL = "jdbc:postgresql:omnibus";
    private static final String CONNECTION_USER = "omnibus";
    private static final String CONNECTION_PASSWORD = "omnibus123";

    private DatabaseExport(){}

    public static void main(String[] args) throws Exception{
        Connection jdbcConnection = DriverManager.getConnection(CONNECTION_URL, CONNECTION_USER, CONNECTION_PASSWORD);
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
        exportFullDataSet(connection);
    }

    public static void exportFullDataSet(IDatabaseConnection connection) throws Exception{
        IDataSet fullDataSet = connection.createDataSet();
        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("dataset.xml"));
    }
}