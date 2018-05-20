package com.darlang.dataconnector.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {        
    private static final String NOME_BANCO = "BancoTeste";    
    private static final String BANCO = "jdbc:postgresql://172.17.0.2:5432/"+NOME_BANCO;    
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "sql";  
    private static Connection CONN = null;
    
    public static Connection getConexao() throws ClassNotFoundException, SQLException{
        if (CONN == null)
        {            
            Class.forName(DRIVER);
            CONN = DriverManager.getConnection(BANCO, USUARIO, SENHA);            
            CONN.setAutoCommit(false);
        }
        return CONN;
    }
    
    public static PreparedStatement getPreparedStatement(String sql)
    {
        if (CONN == null)
        {
            try
            {
                CONN = getConexao();
            } 
            catch (ClassNotFoundException | SQLException ex)
            {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try 
        {
            return CONN.prepareStatement(sql);
        } catch (SQLException e)
        {
            System.out.println("Erro de sql: "+e.getMessage());
        }
        return null;
    }    
}
