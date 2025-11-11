package com.deliverytech.delivery_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.ClienteAtivoDTO;
import com.deliverytech.delivery_api.dto.PedidoPeriodoDTO;
import com.deliverytech.delivery_api.dto.ProdutoMaisVendidoDTO;
import com.deliverytech.delivery_api.dto.VendasRestauranteDTO;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

@Service
public class RelatorioService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;

    public RelatorioService(PedidoRepository pedidoRepository, 
                          ProdutoRepository produtoRepository,
                          ClienteRepository clienteRepository,
                          RestauranteRepository restauranteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
    }

    // Vendas por restaurante
    public List<VendasRestauranteDTO> getVendasPorRestaurante() {
        return pedidoRepository.findVendasAgrupadasPorRestaurante();
    }

    // Produtos mais vendidos
    public List<ProdutoMaisVendidoDTO> getProdutosMaisVendidos() {
        return pedidoRepository.findProdutosMaisVendidos();
    }

    // Clientes mais ativos
    public List<ClienteAtivoDTO> getClientesAtivos() {
        return pedidoRepository.findClientesMaisAtivos();
    }

    // Pedidos por período
    public List<PedidoPeriodoDTO> getPedidosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return pedidoRepository.calcularPedidosPorPeriodo(dataInicio, dataFim);
    }
}
