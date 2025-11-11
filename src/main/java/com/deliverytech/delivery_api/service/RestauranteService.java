package com.deliverytech.delivery_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;

public interface RestauranteService {

    RestauranteResponseDTO cadastrarRestaurante(RestauranteDTO dto);

    RestauranteResponseDTO buscarRestaurantePorId(Long id);

    List<RestauranteResponseDTO> buscarRestaurantesPorCategoria(String categoria);

    List<RestauranteResponseDTO> buscarRestaurantesDisponiveis(); // Ativos

    Page<RestauranteResponseDTO> listarRestaurantes(String categoria, Boolean aitvo, Pageable pageable); // Lista com filtros

    RestauranteResponseDTO atualizarRestaurante(Long id, RestauranteDTO dto);

    RestauranteResponseDTO alterarStatusRestaurante(Long id);

    //RestauranteResponseDTO calcularTaxaEntrega(Long restauranteId, String cep);

}
