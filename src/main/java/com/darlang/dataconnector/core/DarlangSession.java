package com.darlang.dataconnector.core;

import com.darlang.dataconnector.executor.Executor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class DarlangSession {

    private final Connection connection;
    private List<Line> lines = new ArrayList<>();

    public DarlangSession(Connection connection) {
        this.connection = connection;
    }

    public Line addLine(String input) throws Exception {
        Line line = new Line(lines.size() + 1, input);
        lines.add(line);        
        line.process();
        return line;
    }

    public ResultSet executeQueue() throws SQLException {
        Executor executor = new Executor(connection);
        return executor.executeLines(lines);
    }

    public void close() throws SQLException {
        connection.rollback();
    }    
}
