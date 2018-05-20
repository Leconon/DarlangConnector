package com.darlang.dataconnector;

import com.darlang.dataconnector.converter.Processor;
import java.sql.Connection;

/**
 *
 * @author leonardo
 */
public class DarlangExecutor {
    
    private final Connection connection;

    public DarlangExecutor(Connection connection) {
        this.connection = connection;
    }        
    
    public void executeScript(String script) throws Exception {
        Processor processor = new Processor(connection);
        processor.processQuery(script);
    }
    
}
