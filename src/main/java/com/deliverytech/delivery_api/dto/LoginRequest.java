package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para requisição de login")
public class LoginRequest {

    @Schema(description = "Email do usuário", example = "robertofalsus@email.com", required = true, format = "email")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;

    @Schema(description = "Senha do usuário", example = "senhaForte123", required = true, minLength = 6)
    @NotBlank(message = "Senha é obrigatória")
    private String password;

}
