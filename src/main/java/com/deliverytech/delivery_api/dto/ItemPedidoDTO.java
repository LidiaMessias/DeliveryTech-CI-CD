package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Dados para inserir um item no pedido")
public class ItemPedidoDTO {

    @Schema(description = "ID do produto", example = "21", required = true)
    @NotNull(message = "Produto é obrigatório")
    @Positive(message = "Produto ID deve ser positivo")
    private Long produtoId;

    @Schema(description = "Quantidade do produto", example = "2", required = true, minimum = "1", maximum = "10")
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    @Max(value = 50, message = "Quantidade máxima é 50")
    private Integer quantidade;

}

    /*
    @Size(max = 200, message = "Observações não podem exceder 200 caracteres")
    private String observacoes;
    */
