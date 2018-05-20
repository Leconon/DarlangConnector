package com.darlang.dataconnector.converter;

/**
 *
 * @author leonardo
 */
public enum Command {
    
    USING("using", false) {
        @Override
        public String createSql(int commandIndex, String parm) throws Exception {
            // tabela1, tabela2, tabela3
            parm = parm.trim();
            
            if (parm.isEmpty()) {
                throw new Exception("Faltam parâmetros para o comando USING");
            }
            
            String lastResult = "";
            if (commandIndex > 1) {
                 lastResult = "\"!_table_" + (commandIndex - 1) + "\", ";
            }
            
            String sql = "create temporary table \"!_table_" + commandIndex + "\" "
                    + "as (select * from " + lastResult + parm + ")";
            return sql;
        }        
    },
    
    SELECT("select", false) {
        @Override
        public String createSql(int commandIndex, String parm) throws Exception {
            String sql = "create temporary table \"!_table_" + commandIndex + "\" "
                    + "as (select " + parm + " from \"!_table_" + (commandIndex - 1) + "\")";
            return sql;
        }        
    },
    
    FILTER("filter", false) {
        @Override
        public String createSql(int commandIndex, String parm) throws Exception {
            String sql = "create temporary table \"!_table_" + commandIndex + "\" "
                    + "as (select * from \"!_table_" + (commandIndex - 1) + "\" "
                    + "where " + parm + ")";
            return sql;
        }        
    },
    
    PRINT("print", true) {
        @Override
        public String createSql(int commandIndex, String parm) throws Exception {
            String sql = "select * from \"!_table_" + (commandIndex - 1) + "\"";
            return sql;
        }        
    };
    
    private final String call;
    private final boolean hasReturn;

    private Command(String call, boolean hasReturn) {
        this.call = call;
        this.hasReturn = hasReturn;
    }

    public String getCall() {
        return call;
    }

    public boolean hasReturn() {
        return hasReturn;
    }
    
    public String createSql(int commandIndex, String parm) throws Exception {
        throw new Exception("Unavailable command");
    }
    
    public static Object findCommand(String name) {
        for (Command command : values()) {
            if (command.getCall().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return name;
    }
    
}
