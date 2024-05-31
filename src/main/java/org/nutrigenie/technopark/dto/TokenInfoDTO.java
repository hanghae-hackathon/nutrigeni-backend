package org.nutrigenie.technopark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nutrigenie.technopark.model.User;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenInfoDTO {

    @NotNull
    private String grantType;

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;

    @NotNull
    private User user;

}
