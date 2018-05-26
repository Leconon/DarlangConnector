package com.darlang.dataconnector;

import com.darlang.dataconnector.core.DarlangSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 *
 * @author leonardo
 */
public class DarlangExecutor {
    
    private DarlangSession session;

    public DarlangExecutor(Connection connection) {
        this.session = new DarlangSession(connection);
    }

    /**
     * Add lines (separed by '\n') to the execution queue.
     * @param query Code to be added into execution queue.
     */
    public void addQuery(String query) throws Exception {        
        if (session == null) {
            System.err.println(">> Sessão encerrada");
            return;
        }
        for (StringTokenizer tknBreaks = new StringTokenizer(query, "\n"); tknBreaks.hasMoreTokens();) {
            session.addLine(tknBreaks.nextToken());
        }
    }

    public ResultSet execute() throws SQLException {        
        return session.executeQueue();
    }

    public void close() throws SQLException {
        session.close();
        session = null;
    }

    public void newSession(Connection connection) throws SQLException {
        session.close();
        session = new DarlangSession(connection);
    }

}
