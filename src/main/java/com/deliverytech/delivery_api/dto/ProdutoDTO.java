package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery_api.validation.ValidCategoria;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
//import com.deliverytech.delivery_api.model.Restaurante;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para cadastro de produto")
public class ProdutoDTO {
    //private Long id;

    @Schema(description = "Nome do produto", example = "Pizza 4 Queijos", required = true, minLength = 3, maxLength = 50)
    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres")
    private String nome;

    @Schema(description = "Descrição do produto", example = "Muçarela, Provolone, Parmesão e Gorgonzola com azeitonas", required = true, minLength = 3, maxLength = 100)
    @NotBlank(message = "A descrição do produto é obrigatória.")
    @Size(min = 10, max = 500, message = "A descrição deve ter entre 10 e 500 caracteres")
    private String descricao;

    @Schema(description = "Preço do produto em reais", example = "58.90", minimum = "0,01")
    @NotNull(message = "O preço do produto é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @DecimalMax(value = "500.00", message = "Preço não pode exceder R$ 500,00")
    private BigDecimal preco;

    @Schema(description = "Categoria/tipo de culinária do produto", example = "Pizza", required = true)
    @NotBlank(message = "A categoria é obrigatória.")
    @ValidCategoria
    //@Size(min = 3, max = 20, message = "O nome deve ter entre 3 e 20 caracteres")
    private String categoria;
   
    @Schema(description = "ID do restaurante", example = "2", required = true)
    @NotNull(message = "Restaurante é obrigatório")
    @Positive(message = "Restaurante ID deve ser positivo")
    private Long restauranteId;

    @Schema(description = "Disponibilidade do produto", example = "true")
    @AssertTrue(message = "Produto deve estar disponível por padrão")
    private boolean disponivel;

}

    /* 
    @Pattern(regexp = "^(https?://).*\\.(jpg|jpeg|png|gif)$",
        message = "URL da imagem deve ser válida e ter formato JPG, JPEG, PNG ou GIF")
    private String imagemUrl;
    */

