/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author hoby
 */
public class DBConnection {
    DAO database;
    
    Connection connection;
    
    /*  Getter and Setter   */
    public DAO getDatabase() {
        return database;
    }

    public void setDatabase(DAO database) {
        this.database = database;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    /*  Constructor */

    public DBConnection(DAO database, Connection connection) {
        this.setDatabase(database);
        this.setConnection(connection);
    }
    
    public void setAutoCommit(boolean state) throws SQLException {
        getConnection().setAutoCommit(state);
    }
    
    public void commit() throws SQLException{
        getConnection().commit();
    }
    
    public void rollback() throws SQLException{
        getConnection().rollback();
    }
    
    public void close() throws SQLException{
        getConnection().close();
    }
    
}
