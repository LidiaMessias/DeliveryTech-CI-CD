package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.model.StatusPedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de resposta de um pedido")
public class PedidoResponseDTO {

    @Schema(description = "Identificador único do pedido", example = "1")
    private Long id;

    @Schema(description = "Id do cliente que realizou o pedido", example = "15")
    private Long clienteId;

    @Schema(description = "Id do restaurante ao qual o pedido foi feito", example = "7")
    private Long restauranteId;

    @Schema(description = "Endereço completo para entrega do pedido", example = "Rua das Flores, 123 - Centro - São Paulo - SP")
    private String enderecoEntrega;

    @Schema(description = "Data e hora em que o pedido foi realizado", example = "2024-06-15T14:30:00")
    private LocalDateTime dataPedido;

    @Schema(description = "Status atual do pedido", example = "PENDENTE")
    private StatusPedido status;

    @Schema(description = "Valor total do pedido, incluindo subtotal e taxa de entrega", example = "64.90")
    private BigDecimal valorTotal;

    @Schema(description = "Observações adicionais do cliente para o pedido", example = "Por favor, entregar na portaria.")
    private String observacoes;

    @Schema(description = "Lista de itens do pedido")
    private List<ItemPedidoDTO> itens;

    @Schema(description = "CEP do endereço de entrega", example = "01234-567")
    private String cep;

    @Schema(description = "Forma de pagamento escolhida", example = "CARTAO_CREDITO")
    private String formaPagamento;
    
}
