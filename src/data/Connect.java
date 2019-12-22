package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe que gere ligações à base de dados
 * @author ruicouto
 */
public class Connect {

    private static final String URL = "localhost:3306";
    private static final String DB = "mediacenter";
    private static final String USERNAME = "root"; //TODO: alterar
    private static final String PASSWORD = "root"; //TODO: alterar

    public static void createSchema(){

        // SQL command to create a database in MySQL.
        /*String sql = "CREATE SCHEMA IF NOT EXISTS `mediacenter` DEFAULT CHARACTER SET utf8 ;\n" +
                    "USE `mediacenter` ;";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            statement = connection.createStatement();
            String db = "CREATE DATABASE mediacenter";
            statement.executeUpdate(db);
            
            statement = connection.createStatement();
            String schema = "CREATE SCHEMA IF NOT EXISTS `mediacenter` DEFAULT CHARACTER SET utf8 ;\n" +
                              "USE `mediacenter` ;";
            statement.executeUpdate(schema);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                } // nothing we can do
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                } // nothing we can do
            }
        }
    }
    
    /**
     * Estabelece ligação à base de dados
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

