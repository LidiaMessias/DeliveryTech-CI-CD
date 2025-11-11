package com.deliverytech.delivery_api.repository;

//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.dto.ClienteAtivoDTO;
import com.deliverytech.delivery_api.dto.PedidoPeriodoDTO;
import com.deliverytech.delivery_api.dto.ProdutoMaisVendidoDTO;
import com.deliverytech.delivery_api.dto.VendasRestauranteDTO;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.StatusPedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

        // Buscar pedidos por cliente
        List<Pedido> findByClienteId(Long clienteId);

        // Buscar pedidos por restaurante
        List<Pedido> findByRestauranteId(Long restauranteId);

        // Buscar pedidos por restaurante e status definido
        List<Pedido> findByRestauranteIdAndStatus(Long restauranteId, StatusPedido status);

        // Buscar pedidos por cliente e ordenar por data
        List<Pedido> findByClienteOrderByDataPedidoDesc(Cliente cliente);

        // Buscar pedidos por cliente ID e ordenar por data
        List<Pedido> findByClienteIdOrderByDataPedidoDesc(Long clienteId);

        // Buscar por status
        List<Pedido> findByStatusOrderByDataPedidoDesc(StatusPedido status);

        // Buscar por número do pedido
        Optional<Pedido> findById(Long id);

        // Buscar pedidos por status usando paginação
        Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);

        // Buscar pedidos por período
        Page<Pedido> findByDataPedidoBetweenOrderByDataPedidoDesc(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

        // Buscar pedidos por status e período
        Page<Pedido> findByStatusAndDataPedidoBetween(StatusPedido status, LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);

        // Queries para os Relatórios
        // Relatório - Total de vendas por restaurante
        @Query("SELECT new com.deliverytech.delivery_api.dto.VendasRestauranteDTO(r.nome, COUNT(p), SUM(p.valorTotal)) " +
           "FROM Pedido p JOIN p.restaurante r " +
           "GROUP BY r.id, r.nome " +
           "ORDER BY SUM(p.valorTotal) DESC")
        List<VendasRestauranteDTO> findVendasAgrupadasPorRestaurante();

        // Relatório dos produtos mais vendidos
        @Query("SELECT new com.deliverytech.delivery_api.dto.ProdutoMaisVendidoDTO(pr.produto.nome, SUM(pr.quantidade)) " +
           "FROM Pedido p JOIN p.itens pr " +
           "GROUP BY pr.produto.id, pr.produto.nome " +
           "ORDER BY SUM(pr.quantidade) DESC")
        List<ProdutoMaisVendidoDTO> findProdutosMaisVendidos();

        // Relatório de clientes mais ativos
        @Query("SELECT new com.deliverytech.delivery_api.dto.ClienteAtivoDTO(c.nome, COUNT(p), SUM(p.valorTotal)) " +
           "FROM Pedido p JOIN p.cliente c " +
           "GROUP BY c.id, c.nome " +
           "ORDER BY COUNT(p) DESC")
        List<ClienteAtivoDTO> findClientesMaisAtivos();

        // Relatóro de pedidos por período
        @Query("SELECT new com.deliverytech.delivery_api.dto.PedidoPeriodoDTO(CAST(p.dataPedido AS java.time.LocalDate), COUNT(p), SUM(p.valorTotal)) " +
           "FROM Pedido p " +
           "WHERE p.dataPedido BETWEEN :dataInicio AND :dataFim " +
           "GROUP BY CAST(p.dataPedido AS java.time.LocalDate) " +
           "ORDER BY CAST(p.dataPedido AS java.time.LocalDate)")
        List<PedidoPeriodoDTO> calcularPedidosPorPeriodo(
                @Param("dataInicio") LocalDateTime dataInicio, 
                @Param("dataFim") LocalDateTime dataFim);

}


        /*
        // Buscar pedidos com valor total acima de um determinado valor
        @Query("SELECT p FROM Pedido p WHERE p.valorTotal > :valor ORDER BY p.valorTotal DESC")
        List<Pedido> buscarPedidosComValorAcimaDe(@Param("valor") BigDecimal valor);

        // Relatório de pedidos por período
        @Query("SELECT p FROM Pedido p " +
                        "WHERE p.dataPedido BETWEEN :inicio AND :fim " +
                        "AND p.status = :status " +
                        "ORDER BY p.dataPedido DESC")
        List<Pedido> relatorioPedidosPorPeriodoEStatus(
                        @Param("inicio") LocalDateTime inicio,
                        @Param("fim") LocalDateTime fim,
                        @Param("status") StatusPedido status
        );

        // Total de vendas por período
        @Query
        ("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim AND p.status = :status ORDER BY p.dataPedido DESC"
        )
        BigDecimal calcularVendasPorPeriodo(      
                @Param("inicio") LocalDateTime inicio, 
                @Param("fim") LocalDateTime fim,
                @Param("status") StatusPedido status
        );

        
        Buscar pedidos do dia
        
        @Query("SELECT p FROM Pedido p WHERE DATE(p.dataPedido) = CURRENT_DATE ORDER BY p.dataPedido DESC"
        )
        List<Pedido> findPedidosDoDia();
         
        // Buscar pedidos por restaurante
         
        @Query
        ("SELECT p FROM Pedido p WHERE p.restaurante.id = :restauranteId ORDER BY p.dataPedido DESC"
        )
        List<Pedido> findByRestauranteId(
         
        @Param("restauranteId") Long restauranteId);
         
        // Relatório - pedidos por status
        
        @Query("SELECT p.status, COUNT(p) FROM Pedido p GROUP BY p.status")
        List<Object[]> countPedidosByStatus();
        
        // Pedidos pendentes (para dashboard)
         
        @Query
        ("SELECT p FROM Pedido p WHERE p.status IN ('PENDENTE', 'CONFIRMADO','PREPARANDO')"
        +"ORDER BY p.dataPedido ASC")
        List<Pedido> findPedidosPendentes();
        */


         

