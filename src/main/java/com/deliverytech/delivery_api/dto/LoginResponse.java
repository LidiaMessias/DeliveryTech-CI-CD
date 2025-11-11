package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de resposta de login")
public class LoginResponse {

    @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tipo = "Bearer";

    @Schema(description = "Tempo de expiração do token em segundos", example = "3600")
    private Long expiracao;

    @Schema(description = "Dados do usuário autenticado")
    private UserResponse usuario;
    
}
