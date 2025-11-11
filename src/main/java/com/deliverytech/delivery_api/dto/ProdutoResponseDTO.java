package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de resposta de um produto")
public class ProdutoResponseDTO {

    @Schema(description = "Identificador único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    private String nome;

    @Schema(description = "Descrição do produto", example = "Deliciosa pizza com molho de tomate, mussarela e manjericão")
    private String descricao;

    @Schema(description = "Preço do produto em reais", example = "29.90")
    private BigDecimal preco;

    @Schema(description = "Categoria do produto", example = "Pizza")
    private String categoria;  

    @Schema(description = "ID do restaurante ao qual o produto pertence", example = "1")
    private Long restauranteId;

    @Schema(description = "Indica se o produto está disponível para venda", example = "true")
    private boolean disponivel;
    
}
