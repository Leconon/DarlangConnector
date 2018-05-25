package com.darlang.dataconnector;

import com.darlang.dataconnector.core.DarlangSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 *
 * @author leonardo
 */
public class DarlangExecutor {

    /*private final Connection connection;

    public DarlangExecutor(Connection connection) {
        this.connection = connection;
    }

    public void executeScript(String script) throws Exception {
        Processor processor = new Processor(connection);
        processor.processQuery(script);
    }*/

    private DarlangSession session;

    public DarlangExecutor(Connection connection) {
        this.session = new DarlangSession(connection);
    }

    /*public DarlangExecutor(Connection connection) {
        this.connection = connection;
    } */

    /*public void executeScript(String script) {
        Processor processor = new Processor(connection);
        for (StringTokenizer tknBreaks = new StringTokenizer(script, "\n"); tknBreaks.hasMoreTokens();) {
            List<Object> processedQuery = processor.processQuery(script);
            DarlangSession.Line line = session.addLine(script);
            processor.executePool(processedQuery);
            line.setOk(true);
        }
    }*/

    /**
     * Add lines (separed by '\n') to the execution queue.
     * @param query Code to be added into execution queue.
     */
    public void addQuery(String query) {
        System.out.println("2");
        if (session == null) {
            System.err.println(">> Sessão encerrada");
            return;
        }
        for (StringTokenizer tknBreaks = new StringTokenizer(query, "\n"); tknBreaks.hasMoreTokens();) {
            session.addLine(tknBreaks.nextToken());
        }
    }

    public void execute() {
        System.out.println("3");
        session.executeQueue();
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
