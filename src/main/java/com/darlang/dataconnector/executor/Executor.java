package com.darlang.dataconnector.executor;

import com.darlang.dataconnector.core.Line;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leonardo
 */
public class Executor {
    
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }
    
    public ResultSet executeLines(List<Line> lines) throws SQLException {
        int lastTableNumber = 0;
        for (Line line : lines) {
            if (!line.isExecuted()) {
                try {                    
                    if (line.isProcessed()) {
                        line.setExecuted(true);
                        executeSql(line.getGeneretedSql());
                        lastTableNumber = line.getNumber();
                        line.setOk(false);
                    } else {
                        throw new Exception("Não é possível executar o comando pois há um erro na linha " + line.getNumber());
                    }
                } catch (Exception ex) {
                    System.err.println(">> Erro ao executar linha " + line.getNumber() + ". Comandos ignorados.");
                    Logger.getLogger(Line.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return getResult(lastTableNumber);
    }
    
    private void executeSql(String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.execute();
    }
    
    private ResultSet getResult(int tableNumber) throws SQLException {
        String sql = "select * from \"!_table_" + tableNumber + "\"";
        System.out.println(sql);
        PreparedStatement ps = connection.prepareStatement(sql);
        return ps.executeQuery();        
    }    
}
