package com.darlang.dataconnector.core;

import com.darlang.dataconnector.core.converter.Processor;
import com.darlang.dataconnector.core.converter.SqlCommandConstructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public Line addLine(String input) {
        Line line = new Line(lines.size(), input);
        lines.add(line);
        return line;
    }

    public void executeQueue() {
        System.out.println("4");
        for (Line line : lines) {
            if (!line.isExecuted()) {
                try {
                    System.out.println("5");
                    line.execute();
                } catch (Exception ex) {
                    System.err.println(">> Erro ao executar linha " + line.getNumber() + ". Comandos ignorados.");
                    Logger.getLogger(DarlangSession.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void close() throws SQLException {
        connection.rollback();
    }

    public class Line {
        private final int number;
        private final String input;
        private boolean executed = false;
        private boolean ok = false;

        public Line(int number, String input) {
            this.number = number;
            this.input = input;
        }

        public int getNumber() {
            return number;
        }

        public String getInput() {
            return input;
        }

        public boolean isOk() {
            return ok;
        }

        public void setOk(boolean ok) {
            this.ok = ok;
        }

        public boolean isExecuted() {
            return executed;
        }

        public void setExecuted(boolean executed) {
            this.executed = executed;
        }

        public void execute() throws Exception {
            System.out.println("6");
            List<Object> processedQuery = Processor.processQuery(input);
            SqlCommandConstructor constructor = new SqlCommandConstructor(number);
            System.out.println(constructor.lineToSql(processedQuery));
            executed = true;
            //Processor.executePool(processedQuery, connection);
            ok = true;
        }

    }

}
