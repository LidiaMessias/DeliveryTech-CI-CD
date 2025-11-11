package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de resposta de um restaurante")
public class RestauranteResponseDTO {

    @Schema(description = "Identificador único do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizza Express")
    private String nome;

    @Schema(description = "Categoria/tipo de culinária do restaurante", example = "Italiana")
    private String categoria;

    @Schema(description = "Endereço completo do restaurante", example = "Rua das Flores, 123 - Centro - São Paulo - SP")
    private String endereco;

    @Schema(description = "Telefone para contato", example = "13912349999")
    private String telefone;

    @Schema(description = "Taxa de entrega em reais", example = "5.50")
    private BigDecimal taxaEntrega;

    @Schema(description = "Indica se o restaurante está ativo no sistema", example = "true")
    private boolean ativo;

    @Schema(description = "Avaliação média do restaurante (0 a 5 estrelas)", example = "4.5", minimum = "0", maximum = "5")
    private BigDecimal avaliacao;
    
}
