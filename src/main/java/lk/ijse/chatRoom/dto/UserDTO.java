package lk.ijse.chatRoom.dto;

import java.io.InputStream;

public class UserDTO {  //  DTO --> Data Transfer Object --> To transfer data set safely among the layers

    private String username;
    private String password;
    private InputStream image;

    public UserDTO() {
    }

    public UserDTO(String username, String password, InputStream image) {
        this.username = username;
        this.password = password;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", image=" + image +
                '}';
    }
}
