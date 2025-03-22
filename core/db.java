package core;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db {
    //singleton design pattern
    private static  db instance = null;
    private Connection connection = null;
    private final String DB_URl = "jdbc:mysql://localhost:3306/recipe_guide";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "";

    private db(){
        try {
            this.connection = DriverManager.getConnection(DB_URl,DB_USERNAME,DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        return connection;
    }

    public static Connection getInstance(){
        try {
            if(instance == null ||  instance.getConnection() == null ||instance.getConnection().isClosed()){
                instance = new db();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance.getConnection();

    }
}
