/*package com.deliverytech.delivery_api.mapper;

import java.util.stream.Collectors;

import java.util.List;
import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.dto.ClienteDTO;
import com.deliverytech.delivery_api.dto.PedidoDTO;
import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;

@Component
public class ClienteMapper {

    public ProdutoDTO toProdutoDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setCategoria(produto.getCategoria());
        dto.setDisponivel(produto.isDisponivel());
        return dto;
    }

    private RestauranteDTO toRestauranteDTO(Restaurante restaurante) {
        RestauranteDTO dto = new RestauranteDTO();
        dto.setId(restaurante.getId());
        dto.setNome(restaurante.getNome());
        dto.setCategoria(restaurante.getCategoria());
        dto.setEndereco(restaurante.getEndereco());
        dto.setTelefone(restaurante.getTelefone());
        dto.setTaxaEntrega(restaurante.getTaxaEntrega());
        dto.setAvaliacao(restaurante.getAvaliacao());
        dto.setAtivo(restaurante.isAtivo());

        // Converte a lista de produtos
        List<ProdutoDTO> produtosDTO = restaurante.getProdutos().stream()
                .map(this::toProdutoDTO) // Reutiliza o método de conversão
                .collect(Collectors.toList());
        dto.setProdutos(produtosDTO);

        return dto;
    }

    // Método para converter uma lista de Clientes em uma lista de ClienteDTO
    public List<ClienteDTO> toClienteDTOList(List<Cliente> clientes) {
        return clientes.stream()
                .map(this::toClienteDTO)
                .collect(Collectors.toList());
    }

    // Método principal de conversão de Cliente para ClienteDTO
    public ClienteDTO toClienteDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setEndereco(cliente.getEndereco()); 
        dto.setAtivo(cliente.isAtivo());
        dto.setDataCriacao(cliente.getDataCriacao());

        // Converte a lista de pedidos, se existir
        if (cliente.getPedidos() != null) {
            List<PedidoDTO> pedidosDTO = cliente.getPedidos().stream()
                    .map(this::toPedidoDTO) // Chama o método de conversão de Pedido
                    .collect(Collectors.toList());
            dto.setPedidos(pedidosDTO);
        }

        return dto;
    }

    // Método de conversão de Pedido para PedidoDTO
    public PedidoDTO toPedidoDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setStatus(pedido.getStatus());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setObservacoes(pedido.getObservacoes());
        dto.setClienteId(pedido.getCliente().getId());

        // Exemplo de conversão de relacionamento
        if (pedido.getRestaurante() != null) {
            dto.setRestaurante(toRestauranteDTO(pedido.getRestaurante()));
        }

        return dto;
    }
}
*/