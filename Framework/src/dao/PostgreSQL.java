/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author hoby
 */
public class PostgreSQL extends DAO{
    
    public PostgreSQL(String host, String port, String dbName, String user, String password) {
        super(host, port, dbName, user, password);
    }
    
    /** Create a connection and return a DBConnection that contain the database and an instance of connection */
    @Override
    public DBConnection createConnection() throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception ignored) {}
        String url = "jdbc:postgresql://"+getHost()+":"+getPort()+"/"+getDbName();
        DBConnection dbConnection = new DBConnection(this,DriverManager.getConnection(url,getUser(),getPassword()));
        dbConnection.setAutoCommit(false);
        return dbConnection;
    }
    
    @Override
    public String createSequenceSQL(String name) {
        return "CREATE SEQUENCE IF NOT EXISTS "+name+" START WITH 1 INCREMENT BY 1";
    }
    
    /** Insert query with preparedStatement */
    @Override
    public String insertSQL(String table, int valueLength) {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(table).append(" VALUES(");
        for(int i=0; i<valueLength; i++) sql.append('?').append(",");
        return sql.deleteCharAt(sql.lastIndexOf(",")).append(")").toString();
    } 
    
    /** Select respectively one column in a table */
    @Override
    public String selectSQLValue(String column, String table, String condition) {
        return "SELECT " + column + " AS result FROM " + table + " WHERE " + condition;
    }
    
    /** Update a table  */
    @Override
    public String updateonecol(String table, String change, String value, String reference, String condition) {
        //System.out.println("UPDATE "+table+" SET "+column+" = "+change+" WHERE "+column1+" = '"+condition+"'");
        return "UPDATE "+table+" SET "+change+" = "+value+" WHERE "+reference+" = '"+condition+"'";
    }
    
    @Override
    public String updateSQL(String table, String condition, List<Affectation> affectations) {
        StringBuilder sql = new StringBuilder("UPDATE ").append(table).append(" SET ");
        for(Affectation affectation:affectations)
            sql.append(affectation.getColumn()).append(" ").append("=").append(" ").append("?").append(",");
        return sql.deleteCharAt(sql.lastIndexOf(",")).append(" WHERE ").append(condition).toString();
    }
    
    /** Delete from table  */
    @Override
    public String deleteSQL(String table, String condition) {
        return "DELETE FROM " + table + " WHERE " + condition;
    }

    /** Select all column (to transform into object) */
    @Override
    public String selectSQLObject(String table, String condition) {
        return "SELECT * FROM " + table + " WHERE " + condition;
    }
    
    @Override
    public String selectSum(String table, String col) {
        System.out.println("SELECT SUM("+col+") FROM "+ table );
        return "SELECT sum("+col+") FROM"+ table ;
    }
    
    /** Select all column (to transform into object) */
    @Override
    public String getSequence(int length,String prefixe, String sequence) {
        return "SELECT GETSEQUENCE("+length+",'"+prefixe+"',NEXTVAL('"+sequence+"'))";
    }
}
