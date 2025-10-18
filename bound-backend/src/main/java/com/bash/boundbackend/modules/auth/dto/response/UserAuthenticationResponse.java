package com.bash.boundbackend.modules.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserAuthenticationResponse {
    private String jwtToken;
}
