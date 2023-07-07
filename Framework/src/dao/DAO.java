/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author hoby
 */
public abstract class DAO {
    String host;
    
    String port;
    
    String dbName;
    
    String user;
    
    String password;
    
    /*  Getter and Setter   */
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    /*  Constructor    */

    public DAO(String host, String port, String dbName,String user, String password) {
        this.setHost(host);
        this.setPort(port);
        this.setDbName(dbName);
        this.setUser(user);
        this.setPassword(password);
    }
    
    /** Abstract method */
    
    /** Create connection   */
    public abstract DBConnection createConnection() throws SQLException;
    
    /** Insert query with prepared statement */
    public abstract String insertSQL(String table, int valueLength);
    
    /** Select a column in a table */
    public abstract String selectSQLValue(String column, String table, String condition);
    
    /** Select all column (to transform into object) */
    public abstract String selectSQLObject(String table, String condition);
    
    /** create sequence */
    public abstract String createSequenceSQL(String name);
    
    /** Get sequence */
    public abstract String getSequence(int length,String prefixe, String sequence);
    
    /** Update one column */
    public abstract String updateonecol(String table, String change, String value, String reference, String condition);
    
    public abstract String updateSQL(String table, String condition, List<Affectation> affectations);
    
    /** Delete query */
    public abstract String deleteSQL(String table, String condition);
    
    /** Select sum  */
    public abstract String selectSum(String table, String col);
    
    public void insert(Connection connection, String table, List<Object> objects) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL(table, objects.size()));
        for(int i=0; i<objects.size(); i++) {
            if(objects.get(i).getClass() == Integer.class) {
                preparedStatement.setInt(i+1, (int) objects.get(i));
            } else if (objects.get(i).getClass() == Double.class) {
                preparedStatement.setDouble(i+1, (double) objects.get(i));
            } else {
                Method statementSetter = PreparedStatement.class.getDeclaredMethod("set" + objects.get(i).getClass().getSimpleName(), int.class, objects.get(i).getClass());
                statementSetter.invoke(preparedStatement, i+1, objects.get(i));
            }
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    
    public void Save(Connection connection, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SQLException {
        List<Object> attribute = new ArrayList<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            Method getter = object.getClass().getMethod("get" + DBTool.upperFirst(field.getName()));
            attribute.add(getter.invoke(object));
        }

        insert(connection, object.getClass().getSimpleName(), attribute);
    }
    
    public List<Object> selectListObject(Connection connection, Class<?> object, String condition, int limit) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSQLObject(object.getSimpleName(), condition));
        List<Object> results = new ArrayList<>();
        Field[] fields = DBTool.getFieldWithSuperclass(object.getConstructor().newInstance());
        int limitCount = 0;

        while(resultSet.next() && limitCount!=limit) {
            Object newObject = object.getConstructor().newInstance();
            for (Field field : fields) {
                Method objectSetter;
                System.out.println(object.getDeclaredMethod("set" + DBTool.upperFirst(field.getName()), field.getType()));
                objectSetter = object.getDeclaredMethod("set" + DBTool.upperFirst(field.getName()), field.getType());
                System.out.println(ResultSet.class.getDeclaredMethod("get" + DBTool.upperFirst(field.getType().getSimpleName()), String.class));
                Method resultGetType = ResultSet.class.getDeclaredMethod("get" + DBTool.upperFirst(field.getType().getSimpleName()), String.class);
                objectSetter.invoke(newObject, resultGetType.invoke(resultSet, field.getName()));
            }
            results.add(newObject);
            limitCount++;
        }

        statement.close();
        return results;
    }
    
    public List<Object> selectListObject(Connection connection, Class<?> object, String condition) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return selectListObject(connection, object, condition, -1);
    }
    
    public Object selectObject(Connection connection,  Class<?> object, String condition) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Object> objectList = selectListObject(connection, object, condition, 1);
        if(objectList.isEmpty()) return null;
        return objectList.get(0);
    }
    
    public Object getById(Connection connection,  Class<?> object, String id) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Object> objectList = selectListObject(connection, object, id, 1);
        if(objectList.isEmpty()) return null;
        return objectList.get(0);
    }
    
    public double selectSUM(Connection connection,  Object table, String column) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSum(table.getClass().getSimpleName(), column));
        double result = 0;
        if(resultSet.next()) result = resultSet.getDouble(1);
        statement.close();
        return result;
    }
    
    public String selectValue(Connection connection, String column, Object table, String condition) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSQLValue(column, table.getClass().getSimpleName(), condition));
        String result = null;
        if(resultSet.next()) result = resultSet.getString(1);
        statement.close();
        return result;
    }

    public Vector<String> selectValues(Connection connection, String column, Object table, String condition) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSQLValue(column, table.getClass().getSimpleName(), condition));
        Vector<String> result = new Vector<>();
        while(resultSet.next()) result.add(resultSet.getString(1));
        statement.close();
        return result;
    }
    
    public String getSequence(Connection connection, String prefixe, String sequence) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getSequence(7,prefixe,sequence));
        String result = null;
        if(resultSet.next()) result = resultSet.getString(1);
        statement.close();
        return result;
    }  
    
    public void change(Connection connection, Object table, String change, String value, String reference, String condition) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PreparedStatement preparedStatement = connection.prepareStatement(updateonecol(table.getClass().getSimpleName(), change, value, reference,condition));

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    
    public void update(Connection connection, String table, String condition, List<Affectation> affectations) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PreparedStatement preparedStatement = connection.prepareStatement(updateSQL(table, condition, affectations));

        for(int i=0; i<affectations.size(); i++) {
            System.out.println(affectations.get(i).getValue().getClass());
            /*if(affectations.get(i).getValue().getClass() == Integer.class) {
                preparedStatement.setInt(i+1, (int) affectations.get(i).getValue());
            }*/ if (affectations.get(i).getValue().getClass() == Double.class) {
                preparedStatement.setDouble(i+1, (double) affectations.get(i).getValue());
            } else {
                Method statementSetter = PreparedStatement.class.getDeclaredMethod("set" + affectations.get(i).getValue().getClass().getSimpleName(), int.class, affectations.get(i).getValue().getClass());
                statementSetter.invoke(preparedStatement, i+1, affectations.get(i).getValue());
            }
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
    
    public void updateObject(Connection connection, String condition, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SQLException {
        List<Affectation> affectationList = new ArrayList<>();
        for(Field field: object.getClass().getDeclaredFields()) {
            System.out.println(field.getName());
            Method getter = object.getClass().getMethod("get" + DBTool.upperFirst(field.getName()));
            affectationList.add(new Affectation(field.getName(), getter.invoke(object)));
        }

        System.out.println(affectationList.get(5).getValue());
        update(connection, object.getClass().getSimpleName(), condition, affectationList);
    }
    
    public void delete(Connection connection, String table, String condition) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(deleteSQL(table, condition));
        statement.close();
    }
}

