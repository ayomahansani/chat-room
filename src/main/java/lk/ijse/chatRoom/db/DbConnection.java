package lk.ijse.chatRoom.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection { //  Using Singleton Design Pattern --> Create only one object and use it whole the program
    private static DbConnection dbConnection;
    private Connection connection;

    private DbConnection() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatRoom","root","Ijse@123");
    }

    public static DbConnection getInstance() throws SQLException {
        return (null == dbConnection) ? dbConnection = new DbConnection() : dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
