package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedido") 
@Schema(description = "Entidade que representa um pedido realizado por um cliente.")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do pedido.", example = "1")
    private Long id;

    @Schema(description = "Data e hora em que o pedido foi realizado.", example = "2024-06-15T14:30:00")
    @Builder.Default
    private LocalDateTime dataPedido = LocalDateTime.now();

    @Schema(description = "Endereço de entrega do pedido.", example = "Rua das Acácias, 456 - Bairro Jardim - São Paulo - SP")
    private String enderecoEntrega;

    @Schema(description = "Subtotal do pedido antes da taxa de entrega.", example = "59.90")
    private BigDecimal subtotal;

    @Schema(description = "Taxa de entrega do pedido.", example = "5.00")
    private BigDecimal taxaEntrega;

    @Schema(description = "Valor total do pedido, incluindo subtotal e taxa de entrega.", example = "64.90")
    private BigDecimal valorTotal;

    @Schema(description = "Observações adicionais do cliente para o pedido.", example = "Por favor, entregar na portaria.")
    private String observacoes;

    @Schema(description = "CEP do endereço de entrega.", example = "01234-567")
    private String cep;

    @Schema(description = "Forma de pagamento escolhida para o pedido.", example = "CARTAO_CREDITO")
    private String formaPagamento;

    @Schema(description = "Status atual do pedido.", example = "PENDENTE")
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Schema(description = "Cliente que realizou o pedido.", example = "1")
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Schema(description = "Restaurante ao qual o pedido foi feito.", example = "1")
    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    @Schema(description = "Lista de itens incluídos no pedido.")
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    /*
    @Id
     
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    private String enderecoEntrega;
    private BigDecimal subTotal;
    private BigDecimal taxaEntrega;
    
     
    @ManyToOne
    
    @JoinColumn(name = "cliente_id", nullable = false) // Garante que todo pedido
    tenha um cliente
    private Cliente cliente;
    
    @ManyToOne
    
    @JoinColumn(name = "restaurante_id", nullable = false) // Garante que todo
    pedido tenha um restaurante
    private Restaurante restaurante;
    
    @Builder.Default
    private BigDecimal valorTotal = BigDecimal.ZERO;
     
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    
    @Builder.Default
    private LocalDateTime dataPedido = LocalDateTime.now();
    
    // Inicializando a lista para evitar NullPointerException
    
    @Builder.Default
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval =
    true)
    private List<ItemPedido> itens = new ArrayList<>();
    */

}
