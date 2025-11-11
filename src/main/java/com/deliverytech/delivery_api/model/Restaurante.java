package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "restaurante")
@Schema(description = "Entidade que representa um restaurante no sistema de delivery.")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do restaurante.", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante.", example = "Restaurante Saboroso")
    private String nome;

    @Schema(description = "Endereço do restaurante.", example = "Rua das Flores, 123 - Centro - Santos - SP")
    private String endereco;

    @Schema(description = "Categoria culinária do restaurante.", example = "Italiana")
    private String categoria;

    @Schema(description = "Telefone de contato do restaurante.", example = "13 12345678")
    private String telefone;

    @Schema(description = "Horário de funcionamento do restaurante.", example = "10:00 - 22:00")
    private String horarioFuncionamento;

    @Schema(description = "Avaliação média do restaurante.", example = "4.5", minimum = "0", maximum = "5")
    private BigDecimal avaliacao;

    @Schema(description = "Taxa de entrega do restaurante.", example = "5.00")
    @Builder.Default
    private BigDecimal taxaEntrega = BigDecimal.ZERO;

    @Schema(description = "Tempo estimado de entrega em minutos.", example = "30")
    private Integer tempoEntrega;

    @Schema(description = "Indica se o restaurante está ativo no sistema.", example = "true", defaultValue = "true")
    @Builder.Default
    private boolean ativo = true;

    @Schema(description = "Lista de produtos oferecidos pelo restaurante.")
    @Builder.Default
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Produto> produtos = new ArrayList<>();

    @Schema(description = "Lista de pedidos realizados ao restaurante.")
    @Builder.Default
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Pedido> pedidos = new ArrayList<>();
    
}