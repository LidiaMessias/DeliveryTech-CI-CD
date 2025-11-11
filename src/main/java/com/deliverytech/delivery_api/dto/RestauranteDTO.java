package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;
//import java.util.List;

import com.deliverytech.delivery_api.validation.ValidCategoria;
import com.deliverytech.delivery_api.validation.ValidTelefone;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para cadastro de restaurante")
public class RestauranteDTO {
    //private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizza Express", required = true, minLength = 2, maxLength = 100)
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Categoria/tipo de culinária do restaurante", example = "Italiana", required = true)
    @NotBlank(message = "A categoria é obrigatória")
    @ValidCategoria
    private String categoria;

    @Schema(description = "Endereço completo do restaurante", example = "Rua das Flores, 123 - Centro - São Paulo - SP", required = true, maxLength = 200)
    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String endereco;

    @Schema(description = "Telefone para contato", example = "13912349999", required = true)
    @NotBlank(message = "Telefone é obrigatório")
    @ValidTelefone
    //@Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;

    @Schema(description = "Taxa de entrega em reais", example = "5.50", minimum = "0", maximum = "50.0", required = true)
    @NotNull(message = "A taxa de entrega é obrigatória.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Taxa de entrega deve ser positiva")
    @DecimalMax(value = "50.0", message = "Taxa de entrega não pode exceder R$ 50,00")
    private BigDecimal taxaEntrega;
    
    @Schema(description = "Tempo estimado de entrega em minutos", example = "45", minimum = "10", maximum = "120", required = true)
    @NotNull(message = "Tempo de entrega é obrigatório")
    @Min(value = 10, message = "Tempo mínimo de entrega é 10 minutos")
    @Max(value = 120, message = "Tempo máximo de entrega é 120 minutos")
    private Integer tempoEntrega;

    /* 
    @Schema(description = "Horário de funcionamento", example = "08:00-22:00")
    @NotBlank(message = "Horário de funcionamento é obrigatório")
    private String horarioFuncionamento;

    private boolean ativo;
    private BigDecimal avaliacao;
    private List<ProdutoDTO> produtos;
    */
}
