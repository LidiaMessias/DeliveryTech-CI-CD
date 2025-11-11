package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoResponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.ItemPedido;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService{

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Criar novo pedido
    @Override
    @Transactional
    public PedidoResponseDTO criarPedido(PedidoDTO pedidoDto) {
        // Validar se cliente existe e está ativo
        Cliente cliente = clienteRepository.findById(pedidoDto.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));

        if (!cliente.isAtivo()) {
            throw new BusinessException("Cliente inativo, não pode fazer pedidos.");
        }

        // Validar se restaurante existe e está ativo
        Restaurante restaurante = restauranteRepository.findById(pedidoDto.getRestauranteId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));

        if (!restaurante.isAtivo()) {
            throw new BusinessException("Restaurante não está disponível");
        }

        // Validar se todos os produtos existem e estão disponíveis
        List<ItemPedido> itensPedido = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemPedidoDTO itemDTO : pedidoDto.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + itemDTO.getProdutoId()));

            if (!produto.isDisponivel()) {
                throw new BusinessException("Produto indisponível: " + produto.getNome());
            }

            if (!produto.getRestaurante().getId().equals(pedidoDto.getRestauranteId())) {
                throw new BusinessException("Produto não pertence ao restaurante selecionado.");
            }

            // Criar item do pedido
            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());

            item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));

            itensPedido.add(item);
            subtotal = subtotal.add(item.getSubtotal());

        }

        // Calcular total do pedido
        BigDecimal taxaEntrega = restaurante.getTaxaEntrega();
        BigDecimal valorTotal = subtotal.add(taxaEntrega);

        // Salvar Pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setEnderecoEntrega(pedidoDto.getEnderecoEntrega());
        pedido.setCep(pedidoDto.getCep());
        pedido.setSubtotal(subtotal);
        pedido.setTaxaEntrega(taxaEntrega);
        pedido.setValorTotal(valorTotal);
        pedido.setFormaPagamento(pedidoDto.getFormaPagamento());

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // Salvar itens do pedido
        for (ItemPedido item : itensPedido) {
            item.setPedido(pedidoSalvo);
        }
        pedidoSalvo.setItens(itensPedido);

        // 7. Atualizar estoque (se aplicável) - Simulação
        // Em um cenário real, aqui seria decrementado o estoque

        // 8. Retornar pedido criado
        return modelMapper.map(pedidoSalvo, PedidoResponseDTO.class);
    }

    public Page<PedidoResponseDTO> listarPedidos(StatusPedido status, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        // Converte LocalDate para LocalDateTime para as consultas JPA
        LocalDateTime inicio = dataInicio != null ? dataInicio.atStartOfDay() : null;
        LocalDateTime fim = dataFim != null ? dataFim.atTime(23, 59, 59) : null;

        Page<Pedido> pedidos;

        if (status != null && inicio != null && fim != null) {
        // Opção 1: Todos os 3 filtros estão presentes
        pedidos = pedidoRepository.findByStatusAndDataPedidoBetween(status, inicio, fim, pageable);
        
        } else if (status != null) {
        // Opção 2: Somente filtro por Status
        pedidos = pedidoRepository.findByStatus(status, pageable); // Assume que você tem este método
        
        } else if (inicio != null && fim != null) {
        // Opção 3: Somente filtro por Data
        pedidos = pedidoRepository.findByDataPedidoBetweenOrderByDataPedidoDesc(inicio, fim, pageable);
        
        } else {
        // Opção 4: Sem filtros (apenas Paginação)
        pedidos = pedidoRepository.findAll(pageable);
        }
        return pedidos.map(pedido -> modelMapper.map(pedido, PedidoResponseDTO.class));
    }

    // Buscar por ID
    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com ID: " + id));
        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    // Listar pedidos por cliente
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPedidosPorCliente(Long clienteId) {
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);

        return pedidos.stream()
            .map(pedido -> modelMapper.map(pedido, PedidoResponseDTO.class))
            .collect(Collectors.toList());
    }

    // Listar pedidos por restaurante
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> buscarPedidosPorRestaurante(Long restauranteId, StatusPedido status) {
        List<Pedido> pedidos;

        if (status != null) {
            pedidos = pedidoRepository.findByRestauranteIdAndStatus(restauranteId, status);
        } else {
            pedidos = pedidoRepository.findByRestauranteId(restauranteId);
        }
        return pedidos.stream()
            .map(pedido -> modelMapper.map(pedido, PedidoResponseDTO.class))
            .collect(Collectors.toList());
    }

    // Atualizar status do pedido
    @Override
    public PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));

        // Validar transições de status permitidas
        if (!isTransicaoValida(pedido.getStatus(), novoStatus)) {
            throw new BusinessException("Transição de status inválida: " + pedido.getStatus() + " -> " + novoStatus);
        }

        pedido.setStatus(novoStatus);
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);

        return modelMapper.map(pedidoAtualizado, PedidoResponseDTO.class);
    }

    // Calcular total do pedido
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPedido(List<ItemPedidoDTO> itens) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoDTO item : itens) {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

            BigDecimal subtotalItem = produto.getPreco()
                .multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotalItem);
        }

        return total;
    }

    // Cancelar pedido
    @Override
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));
        
        if (!podeSerCancelado(pedido.getStatus())) {
            throw new BusinessException("Pedido não pode ser cancelado no status: " + pedido.getStatus());
        }    

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);

    }

    private boolean isTransicaoValida(StatusPedido statusAtual, StatusPedido novoStatus) {
        // Lógica de transições válidas
        switch (statusAtual) {
            case PENDENTE:
            return novoStatus == StatusPedido.CONFIRMADO || novoStatus == StatusPedido.CANCELADO;

            case CONFIRMADO:
            return novoStatus == StatusPedido.PREPARANDO || novoStatus == StatusPedido.CANCELADO;

            case PREPARANDO:
            return novoStatus == StatusPedido.SAIU_PARA_ENTREGA;

            case SAIU_PARA_ENTREGA:
            return novoStatus == StatusPedido.ENTREGUE;

            default:
            return false;
        }
    }

    private boolean podeSerCancelado(StatusPedido status) {
        return status == StatusPedido.PENDENTE || status == StatusPedido.CONFIRMADO;
    }

}

/*
// Adicionar item ao pedido
    public Pedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade) {
        Pedido pedido = buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " +
                        pedidoId));
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " +
                        produtoId));
        if (!produto.isDisponivel()) {
            throw new IllegalArgumentException("Produto não disponível: " + produto.getNome());
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        // Verificar se produto pertence ao mesmo restaurante do pedido
        if (!produto.getRestaurante().equals(pedido.getRestaurante())) {
            throw new IllegalArgumentException("Produto não pertence ao restaurante do pedido");
        }
        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        item.calcularSubtotal();

        //pedido.adicionarItem(item);
        return pedidoRepository.save(pedido);
    }

    // Confirmar pedido
    public Pedido confirmarPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " +
                        pedidoId));
        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new IllegalArgumentException("Apenas pedidos pendentes podem ser confirmados");
        }
        if (pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve ter pelo menos um item");
        }
        pedido.setStatus(StatusPedido.CONFIRMADO);
        return pedidoRepository.save(pedido);
    }

    // Buscar por número do pedido
    @Transactional(readOnly = true)
    public Pedido buscarPorNumero(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
    }
*/
