package com.deliverytech.delivery_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Buscar cliente por Id
    Optional<Cliente> findById(Long id);

    // Buscar cliente por email (método derivado)
    Optional<Cliente> findByEmail(String email);

    // Verificar se email já existe
    boolean existsByEmail(String email);

    // Buscar clientes ativos
    List<Cliente> findByAtivoTrue();

    // Buscar clientes por nome (contendo)
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
 
    // Buscar todos os clientes
    List<Cliente> findAll();

    // Buscar clientes por telefone
    Optional<Cliente> findByTelefone(String telefone);

    // Query customizada - ranking de clientes por número de pedidos
    @Query(value = "SELECT c.nome, COUNT(p.id) as total_pedidos " +
        "FROM cliente c " +
        "LEFT JOIN pedido p ON c.id = p.cliente_id " +
        "GROUP BY c.id, c.nome " +
        "ORDER BY total_pedidos DESC " +
        "LIMIT 10", nativeQuery = true)
    List<Object[]> rankingClientesPorPedidos();

    // Query customizada - clientes com pedidos
    @Query("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.pedidos p WHERE c.ativo = true")
    List<Cliente> findByAtivoTrueWithPedidos();

    // Query nativa - clientes por cidade
    @Query(value = "SELECT * FROM clientes WHERE endereco LIKE %:cidade% AND ativo = true",
        nativeQuery = true)
    List<Cliente> findByCidade(@Param("cidade") String cidade);

    // Contar clientes ativos
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.ativo = true")
    Long countClientesAtivos();

}
