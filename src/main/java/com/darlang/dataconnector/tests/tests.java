package com.darlang.dataconnector.tests;

import com.darlang.dataconnector.DarlangExecutor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leonardo
 */
public class tests {

    public static void main(String[] args) {

        /*String query = " using funcionario, cargo \n filter cargo_func = id_cargo and id_cargo = 1 \n select nome_func \n print;";
        System.out.println("EXECUTAR: \n" + query);
        System.out.println("");*/
 /*PreparedStatement ps = DBConnection.getPreparedStatement("create TEMPORARY table if not exists teste as (select * from cliente);select * from teste;");
        ResultSet res = null;

        try {
            ps.execute("create TEMPORARY table if not exists teste as (select * from funcionario)");
            res = ps.executeQuery("select * from teste");
            while (res.next()) {
                System.out.println(res.getObject(2));
            }
            ps.getConnection().rollback();
        } catch (SQLException ex) {
            Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        try {
            DarlangExecutor darlangExecutor = new DarlangExecutor(DBConnection.getConexao());
            //darlangExecutor.executeScript(query);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print(">> ");

                double ini, fim;
                ini = System.nanoTime();

                String in = scanner.nextLine();
                darlangExecutor.addQuery(in);
                ResultSet rs = darlangExecutor.execute();

                fim = System.nanoTime();
                System.out.println("> " + (fim - ini));

                System.out.println("");
                print(rs);
                
                System.out.println("-------------------------");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            DBConnection.getConexao().rollback();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void print(ResultSet rs) {
        try {
            ResultSet resultSet = rs;
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) {
                        System.out.print(",  ");
                    }
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
