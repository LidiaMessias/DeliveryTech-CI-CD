package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoResponseDTO;
import com.deliverytech.delivery_api.model.StatusPedido;

public interface PedidoService {

    PedidoResponseDTO criarPedido(PedidoDTO dto);

    Page<PedidoResponseDTO> listarPedidos(StatusPedido status, LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    PedidoResponseDTO buscarPedidoPorId(Long id);

    List<PedidoResponseDTO> buscarPedidosPorCliente(Long clienteId);

    List<PedidoResponseDTO> buscarPedidosPorRestaurante(Long restauranteId, StatusPedido status);

    PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedido status);

    BigDecimal calcularTotalPedido(List<ItemPedidoDTO> itens);

    void cancelarPedido(Long id);
    
}