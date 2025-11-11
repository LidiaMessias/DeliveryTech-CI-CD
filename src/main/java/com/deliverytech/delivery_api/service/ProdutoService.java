package com.deliverytech.delivery_api.service;

import java.util.List;

import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
//import com.deliverytech.delivery_api.dto.RestauranteDTO;

public interface ProdutoService {

    ProdutoResponseDTO cadastrarProduto(ProdutoDTO dto, Long restauranteId);

    List<ProdutoResponseDTO> buscarProdutosPorRestaurante(Long restauranteId);

    ProdutoResponseDTO buscarProdutoPorId(Long id);

    ProdutoResponseDTO atualizarProduto(Long id, ProdutoDTO dto);

    void removerProduto(Long id);

    ProdutoResponseDTO alterarDisponibilidade(Long id, boolean disponivel);

    List<ProdutoResponseDTO> buscarProdutosPorCategoria(String categoria);

    List<ProdutoResponseDTO> buscarProdutosPorNome(String nome);

}
