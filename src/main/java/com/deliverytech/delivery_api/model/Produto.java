package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import lombok.*;    
import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "produto")
@Schema(description = "Entidade que representa um produto oferecido por um restaurante.")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do produto.", example = "1")
    private Long id;

    @Schema(description = "Nome do produto.", example = "Pizza Margherita")
    private String nome;

    @Schema(description = "Descrição do produto.", example = "Deliciosa pizza com molho de tomate, mussarela e manjericão.")
    private String descricao;

    @Schema(description = "Categoria do produto.", example = "Pizza")
    private String categoria; 

    @Schema(description = "Preço do produto.", example = "29.90")
    private BigDecimal preco; 

    @Schema(description = "Indica se o produto está disponível para venda.", example = "true", defaultValue = "true")
    private boolean disponivel; 

    @Schema(description = "URL da imagem do produto.", example = "http://example.com/imagens/produto1.jpg")
    private String imagemUrl;

    @Schema(description = "Restaurante ao qual o produto pertence.", example = "1")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Schema(description = "Lista de itens de pedido associados a este produto.")
    @OneToMany(mappedBy = "produto")
    private List<ItemPedido> itensPedido;
}
