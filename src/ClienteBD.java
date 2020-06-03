package ep1_redes;

import java.io.File;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Criacao da conexao BD para trazer a lista de clientes
 *
 * @author Johnny Taira
 *
 */
public class ClienteBD {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/ep_redes";
    private static final String PATH_CONFIG = "config_db.txt";
	
    private static boolean CONFIGURED = false;
    static String USER = "";
    static String PASS = "";

    /**
     * just a dummy test
     *
     * @param args
     */
    public static void main(String[] args) {
        doConfig();
        ClienteBD clienteBD = new ClienteBD();
        clienteBD.getConnection();
    }

    public Statement getStatement() {

        try {
            Connection conn = this.getConnection();
            return conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Connection getConnection() {
        doConfig();
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cls) {
            cls.printStackTrace();

        }
        return null;

    }

    public ResultSet getResultSet(String sql) {
        Statement stmt = this.getStatement();
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return rs;
    }

    public void executeUpdate(String sql) {
        Statement stmt = this.getStatement();
        try {
            stmt.executeUpdate(sql);
            this.getConnection().close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void closeConnection() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
    private static void doConfig() {
        if (!CONFIGURED) {
            setDBConfig();
            CONFIGURED = true;
        }
    }

    private static void setDBConfig() {
        Scanner sc = null;
        String path = PATH_CONFIG;
        try {
            sc = new Scanner(new File(path));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (sc != null) {
            try {
                USER = sc.nextLine();
            } catch (Exception e) {
            }

            try {
                PASS = sc.nextLine();
            } catch (Exception e) {
            }
        }
    }
}
