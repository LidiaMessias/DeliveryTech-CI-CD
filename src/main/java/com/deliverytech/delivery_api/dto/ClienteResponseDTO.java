package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de resposta de um cliente")
public class ClienteResponseDTO {

    @Schema(description = "Identificador único do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome do cliente", example = "João Silva")
    private String nome;

    @Schema(description = "Email do cliente", example = "joaosilva@email.com.br")
    private String email;

    @Schema(description = "Telefone para contato", example = "13987654321")
    private String telefone;

    @Schema(description = "Endereço completo do cliente", example = "Avenida Brasil, 456 - Centro - Belo Horizonte - MG")
    private String endereco;

    @Schema(description = "Indica se o cliente está ativo no sistema", example = "true")
    private boolean ativo;
    
}
