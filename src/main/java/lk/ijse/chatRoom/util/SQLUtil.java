package lk.ijse.chatRoom.util;

import lk.ijse.chatRoom.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUtil { // Created for less boilerplate codes

    public static <T>T execute(String sql, Object... objects) throws SQLException {    // using generics and var-args

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        for(int i = 0; i < objects.length; i++){
            pstm.setObject((i+1), objects[i]);
        }

        if(sql.startsWith("SELECT") || sql.startsWith("select")){
            return (T) pstm.executeQuery(); //return ResultSet ----- cast to T type
        }else{
            return (T) (Boolean) (pstm.executeUpdate() > 0);    //return boolean ----- first cast to non-primitive data type ( boolean -> Boolean ) Because generics are only related to non-primitive data types and second cast to T type
        }

    }
}

//Here using method wise generics
//  <T>   safe type --> anytype can come inside to this execute() method
//   T    return type   --> anytype can return from this execute() method
