package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para registro de um novo usuário no sistema")
public class RegisterRequest {

    @Schema(description = "Nome completo do usuário", example = "Carlos Alberto", required = true, minLength = 2, maxLength = 100)
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Email do usuário", example = "carlosalberto@email.com", required = true, format = "email")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;

    @Schema(description = "Senha do usuário", example = "senhaSegura123", required = true, minLength = 6, maxLength = 20)
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password;

    @Schema(description = "Papel/role do usuário no sistema", example = "CLIENTE", required = true)
    @NotNull(message = "Role é obrigatória")
    private Role role;

    @Schema(description = "ID do restaurante associado (apenas para papel RESTAURANTE)", example = "2")
    private Long restauranteId;
    
}
