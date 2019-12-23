package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connect {

    private static final String URL = "localhost:3306";
    private static final String DB = "mediacenter";
    private static final String USERNAME = "root"; //TODO: alterar
    private static final String PASSWORD = "root"; //TODO: alterar

    public static Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        //cliente deve fechar conexão!
        return DriverManager.getConnection("jdbc:mysql://"+URL+"/"+DB+"?user="+USERNAME+"&password="+PASSWORD);
    }

    /**
     * Fecha a ligação à base de dados, se aberta.
     * @param c
     */
    public static void close(Connection c) {
        try {
            if(c!=null && !c.isClosed()) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

