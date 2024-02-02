package lk.ijse.chatRoom.model;

import lk.ijse.chatRoom.dto.UserDTO;
import lk.ijse.chatRoom.util.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    public static boolean existsUser(String username) throws SQLException {
        String query = "SELECT username FROM user WHERE username = ?";
        ResultSet rs = SQLUtil.execute(query,username);
        return rs.next();
    }

    public static UserDTO getUserDetails(String username) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ?";
        ResultSet rs = SQLUtil.execute(query,username);
        if (rs.next()){
            return new UserDTO(rs.getString(1),rs.getString(2),rs.getBinaryStream(3));
        } else {
            return null;
        }
    }

    public static boolean saveUser(UserDTO userDto) throws SQLException {
        String query = "INSERT INTO user VALUES (?,?,?)";
        return SQLUtil.execute(query,userDto.getUsername(),userDto.getPassword(),userDto.getImage());
    }

    public static boolean updatePassword(String username, String password) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE username = ?";
        return SQLUtil.execute(query, password, username);
    }

}
