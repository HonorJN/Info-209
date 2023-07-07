/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author hoby
 */
public class Connect {
    static DAO PG_DB = new PostgreSQL("localhost", "5432", "societe", "postgres", "1413");
    
    public static DAO getPgDb() {
        return PG_DB;
    }
}
