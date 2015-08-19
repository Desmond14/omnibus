package pl.omnibus.persistence.dao;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

public class DaoTestBase {
    private static final String JDBC_DRIVER = org.postgresql.Driver.class.getName();
    private static final String JDBC_URL = "jdbc:postgresql:omnibus";
    private static final String USER = "omnibus";
    private static final String PASSWORD = "omnibus123";
    private static final String DATASET_FILE = "/dataset.xml";

    protected void prepareDatabase() throws Exception {
        IDataSet dataSet = readDataSet();
        cleanTables(dataSet);
        populateTables(dataSet);
    }

    private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(
                getClass().getResourceAsStream(DATASET_FILE)
        );
    }

    private void cleanTables(IDataSet dataSet) throws Exception {
        IDatabaseTester databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.DELETE_ALL);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }
    private void populateTables(IDataSet dataSet) throws Exception {
        IDatabaseTester databaseTester = new JdbcDatabaseTester(JDBC_DRIVER, JDBC_URL, USER, PASSWORD);
        databaseTester.setSetUpOperation(DatabaseOperation.INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }
}
