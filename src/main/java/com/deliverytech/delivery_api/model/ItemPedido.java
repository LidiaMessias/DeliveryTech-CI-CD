package com.deliverytech.delivery_api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pedido associado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    // Produto associado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Positive(message = "A quantidade deve ser positiva")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull(message = "O preço unitário é obrigatório")
    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @Builder.Default // <-- A CORREÇÃO ESTÁ AQUI
    @Column(nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    private String observacoes;

    // Calcula o subtotal do item
    public void calcularSubtotal() {
        if (precoUnitario != null && quantidade != null) {
            subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        } else {
            subtotal = BigDecimal.ZERO;
        }
    }
}
