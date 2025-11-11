package com.deliverytech.delivery_api.dto;

//import java.math.BigDecimal;
//import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.validation.ValidCEP;

import io.swagger.v3.oas.annotations.media.Schema;

//import com.deliverytech.delivery_api.model.StatusPedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para cadastrar um pedido")
public class PedidoDTO {
    //private Long id;

    @Schema(description = "Id do cliente", example = "15", required = true)
    @NotNull(message = "Cliente é obrigatório")
    @Positive(message = "Cliente ID deve ser positivo")
    private Long clienteId; // Referencia apenas o ID do cliente

    @Schema(description = "Id do restaurante", example = "7", required = true)
    @NotNull(message = "Restaurante é obrigatório")
    @Positive(message = "Cliente ID deve ser positivo")
    private Long restauranteId;

    @Schema(description = "Endereço completo para entrega do pedido", example = "Rua das Flores, 123 - Centro",  required = true)
    @NotBlank(message = "Endereço de entrega é obrigatório")
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String enderecoEntrega;

    @Schema(description = "Lista de itens do pedido", required = true)
    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    @Valid
    private List<ItemPedidoDTO> itens;
   
    @Schema(description = "CEP do endereço de entrega", example = "01234-567", required = true)
    @NotBlank(message = "CEP é obrigatório")
    @ValidCEP
    private String cep;

    @Schema(description = "Observações adicionais para o pedido", example = "Por favor, entregar na portaria", required = false)
    @Size(max = 500, message = "Observações não podem exceder 500 caracteres")
    private String observacoes;

    @Schema(description = "Forma de pagamento escolhida", example = "CARTAO_CREDITO", required = true)
    @NotBlank(message = "Forma de pagamento é obrigatória")
    @Pattern(regexp = "^(DINHEIRO|CARTAO_CREDITO|CARTAO_DEBITO|PIX)$",
    message = "Forma de pagamento deve ser: DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO ou PIX")
    private String formaPagamento;

    /*      
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private String observacoes;
    private RestauranteDTO restaurante;
    */
}
