package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefone;

    private String endereco;

    @Builder.Default // <-- Corrige o aviso do Lombok
    @Column(nullable = false)
    private boolean ativo = true;

    @Builder.Default // <-- Corrige o aviso do Lombok
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Builder.Default // <-- Corrige o aviso do Lombok
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new ArrayList<>();
    
}