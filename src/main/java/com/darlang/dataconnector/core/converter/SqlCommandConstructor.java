package com.darlang.dataconnector.core.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author leonardo
 */
public class SqlCommandConstructor {

    private final int lineIndex;
    private int commandIndex = 1;

    public SqlCommandConstructor(int lineIndex) {        
        this.lineIndex = lineIndex;
    }

    public String lineToSql(String line) throws Exception {
        List<Object> processedLine = processQuery(line);
        List<String> sqlList = createSqlList(processedLine);
        return createSqlCommand(sqlList);
    }
    
    private List<Object> processQuery(String query) {        
        query = replaceSimbols(query);
        List<Object> queryObject = new ArrayList<>();
        for (StringTokenizer tknQuery = new StringTokenizer(query, " ", true); tknQuery.hasMoreTokens();) {
            String token = tknQuery.nextToken();
            queryObject.add(Command.findCommand(token));
        }
        return queryObject;
    }

    private String replaceSimbols(String query) {
        query = query.replace(";", " ; ");
        return query;
    }

    private List<String> createSqlList(List<Object> line) throws Exception {
        Command actualCommand = null;
        String parms = "";
        String refName = (lineIndex == 1 ? null : createReferenceName(lineIndex - 1, false));
        List<String> sqlQueries = new ArrayList<>();
        for (Object obj : line) {
            if (obj instanceof String) {
                parms += obj;
            } else if (obj instanceof Command) {
                if (actualCommand != null) {
                    sqlQueries.add(actualCommand.createSql(refName, parms));
                    refName = createReferenceName(commandIndex++, true);
                    parms = "";
                }
                actualCommand = (Command) obj;
            }
        }
        if (actualCommand != null) {
            sqlQueries.add(actualCommand.createSql(refName, parms));
        }
        return sqlQueries;
    }

    private String createReferenceName(int index, boolean isSubtable) {
        if (isSubtable) {
            return "\"!_with_" + index + "\"";
        } else {
            return "\"!_table_" + index + "\"";
        }
    }

    private String createSqlCommand(List<String> queries) {
        String sql = " create temporary table " + createReferenceName(lineIndex, false)
                + " as with ";
        int i = 0;
        for (String query : queries) {
            sql += createReferenceName(++i, true) + " as ("
                    + query + ")" + (i == queries.size() ? "" : ", ");
        }
        sql += "select * from " + createReferenceName(i, true);
        return sql;
    }

}
