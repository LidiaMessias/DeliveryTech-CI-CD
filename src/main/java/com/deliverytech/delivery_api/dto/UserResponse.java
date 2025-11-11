package com.deliverytech.delivery_api.dto;

import java.time.LocalDateTime;

import com.deliverytech.delivery_api.model.Usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de resposta de um usuário")
public class UserResponse {

    @Schema(description = "Identificador único do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "Carlos Alberto")
    private String nome;

    @Schema(description = "Email do usuário", example = "carlosalberto@email.com")
    private String email;

    @Schema(description = "Papel/role do usuário no sistema", example = "CLIENTE")
    private String role;

    @Schema(description = "Indica se o usuário está ativo no sistema", example = "true")
    private Boolean ativo;

    @Schema(description = "Data e hora de criação do usuário", example = "2024-01-15T10:30:00")
    private LocalDateTime dataCriacao;

    @Schema(description = "ID do restaurante associado (apenas para papel RESTAURANTE)", example = "2")
    private Long restauranteId;

    public UserResponse() {

    }   

    public UserResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.role = usuario.getRole().name();
        this.ativo = usuario.isAtivo();
        this.dataCriacao = usuario.getDataCriacao();
        this.restauranteId = usuario.getRestauranteId();
    }
}
