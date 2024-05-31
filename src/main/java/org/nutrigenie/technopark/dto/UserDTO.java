package org.nutrigenie.technopark.dto;

import lombok.Getter;
import lombok.Setter;

public class UserDTO {

    @Getter
    @Setter
    public static class Login{
        private String email;
        private String password;
    }

    @Getter @Setter
    public static class LogOut{
        private long id;
    }

}
