package com.darlang.dataconnector.core.converter;

import com.darlang.dataconnector.core.DarlangSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author leonardo
 */
public class Processor {        
    
    public static List<Object> processQuery(String query) {
        query = replaceSimbols(query);        
        List<Object> queryObject = new ArrayList<>();
        for (StringTokenizer tknQuery = new StringTokenizer(query, " ", true); tknQuery.hasMoreTokens();) {
            String token = tknQuery.nextToken();
            queryObject.add(Command.findCommand(token));
        }
        return queryObject;
    }

    private static String replaceSimbols(String query) {
        query = query.replace(";", " ; ");
        return query;
    }

    public static void executePool(List<Object> queryObject, int lineIndex, Connection connection) throws Exception {
        Command actualCommand = null;
        String parms = "";        
        int i = 1;
        for (Object obj : queryObject) {
            if (obj instanceof String) {
                parms += obj;
            } else if (obj instanceof Command) {
                if (actualCommand != null) {
                    String sql = actualCommand.createSql(lineIndex, i++, parms);                    
                    executeSql(sql, actualCommand.hasReturn(), connection);
                    parms = "";
                }
                actualCommand = (Command) obj;
            }
        }
        if (actualCommand != null) {
            String sql = actualCommand.createSql(lineIndex, i++, parms);
            executeSql(sql, actualCommand.hasReturn(),connection);            
        }        
    }
    
    private static void executeSql(String sql, boolean hasReturn, Connection connection) throws SQLException {
        //System.out.println(">> " + sql);
        PreparedStatement ps = connection.prepareStatement(sql);
        System.out.println(">> " + sql);
        if (hasReturn) {            
            ResultSet rs = ps.executeQuery();
            //List<Map> table = new ArrayList<>();
            Map<String, List<String>> columns = new HashMap<>();
            
            for (int i = 1; i <= ps.getMetaData().getColumnCount(); i++) {                
                String columnName = ps.getMetaData().getColumnName(i);
                columns.put(columnName, new ArrayList<>());
            }
            
            while (rs.next()) {
                for (String name : columns.keySet()) {
                    columns.get(name).add(rs.getString(name));
                }
            }
            
            for (String name : columns.keySet()) {
                //columns.get(name).add(rs.getString(name));
                System.out.println("> " + columns.get(name));
            }
        } else {
            ps.execute();
        }        
    }
    
    private void processLine(int lineIndex) {
        
    }

}
