package org.nutrigenie.technopark.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name = "email")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Column(name = "user_name")
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String userName;

    @Column(name = "password")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
//    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,100}",
//            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @Column(name = "current_refresh_token")
    private String currentRefreshToken;

    @Column(name = "role")
    private String role;

    @Column(name = "authority_level")
    private int authorityLevel;

}
