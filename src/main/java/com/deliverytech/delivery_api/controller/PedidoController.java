package com.deliverytech.delivery_api.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.ApiResponseWrapper;
import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PagedResponseWrapper;
import com.deliverytech.delivery_api.dto.PedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoResponseDTO;
import com.deliverytech.delivery_api.model.StatusPedido;
import com.deliverytech.delivery_api.service.PedidoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "Operações relacionadas aos pedidos")
public class PedidoController {

    @Autowired
    private PedidoServiceImpl pedidoService;

    // Criar novo pedido
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(
        summary = "Criar pedido", 
        description = "Cria um novo pedido no sistema",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        tags = {"Pedidos"}
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente ou restaurante não encontrado"),
        @ApiResponse(responseCode = "409", description = "Produto indisponível")
    })
    public ResponseEntity<ApiResponseWrapper<PedidoResponseDTO>> criarPedido(
            @Valid @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do pedido a ser criado")
            PedidoDTO dto) {   
        PedidoResponseDTO pedido = pedidoService.criarPedido(dto);
        ApiResponseWrapper<PedidoResponseDTO> response = new ApiResponseWrapper<>(true, pedido, "Pedido criado com sucesso");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);       
    }

    // Buscar pedido por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Recupera um pedido específico com todos os detalhes", tags = {"Pedidos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<PedidoResponseDTO>> buscarPorId(
            @Parameter(description = "ID do pedido")
            @PathVariable Long id) { 
        PedidoResponseDTO pedido = pedidoService.buscarPedidoPorId(id);
        ApiResponseWrapper<PedidoResponseDTO> response = new ApiResponseWrapper<>(true, pedido, "Pedido encontrado");
        return ResponseEntity.ok(response);       
    }

    // Listar pedidos com filtros
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Listar pedidos", 
        description = "Lista pedidos com filtros opcionais e paginação",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        tags = {"Pedidos"}
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    public ResponseEntity<PagedResponseWrapper<PedidoResponseDTO>> listarPedidos(
            @Parameter(description = "Status do pedido") @RequestParam(required = false) StatusPedido status,
            @Parameter(description = "Data inicial")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {

        Page<PedidoResponseDTO> pedidos = pedidoService.listarPedidos(status, dataInicio, dataFim, pageable);
        PagedResponseWrapper<PedidoResponseDTO> response = new PagedResponseWrapper<>(pedidos);

        return ResponseEntity.ok(response);
    }

    // Listar pedidos por cliente
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Histórico do cliente", description = "Lista todos os pedidos de um cliente", tags = {"Pedidos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<List<PedidoResponseDTO>>> buscarPorCliente(
            @Parameter(description = "ID do cliente")
            @PathVariable Long clienteId) {
        List<PedidoResponseDTO> pedidos = pedidoService.buscarPedidosPorCliente(clienteId);
        ApiResponseWrapper<List<PedidoResponseDTO>> response = new ApiResponseWrapper<>(true, pedidos, "Histórico recuperado com sucesso");
        return ResponseEntity.ok(response);
    }

    // Listar pedidos por restaurante
    @GetMapping("/restaurante/{restauranteId}")
    @PreAuthorize("hasRole('RESTAURANTE')")
    @Operation(
        summary = "Pedidos do restaurante", 
        description = "Lista todos os pedidos de um restaurante",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        tags = {"Pedidos"}
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<List<PedidoResponseDTO>>> buscarPorRestaurante(
            @Parameter(description = "ID do restaurante")
            @PathVariable Long restauranteId,
            @Parameter(description = "Status do pedido")
            @RequestParam(required = false) StatusPedido status) {
    List<PedidoResponseDTO> pedidos = pedidoService.buscarPedidosPorRestaurante(restauranteId, status);
    ApiResponseWrapper<List<PedidoResponseDTO>> response = new ApiResponseWrapper<>(true, pedidos, "Pedidos recuperados com sucesso");
    return ResponseEntity.ok(response);
    }

    // Atualizar status do pedido 
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido", tags = {"Pedidos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Transição de status inválida")
    })
    public ResponseEntity<ApiResponseWrapper<PedidoResponseDTO>> atualizarStatusPedido(
            @Parameter(description = "ID do pedido")
            @PathVariable Long id,
            @Valid @RequestBody StatusPedido novoStatus) {

        PedidoResponseDTO pedido = pedidoService.atualizarStatusPedido(id, novoStatus);
        ApiResponseWrapper<PedidoResponseDTO> response = new ApiResponseWrapper<>(true, pedido, "Status atualizado com sucesso");
        return ResponseEntity.ok(response);
    }

    // Cancelar pedido
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido se possível", tags = {"Pedidos"})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Pedido não pode ser cancelado")
    })
    public ResponseEntity<Void> cancelarPedido(
            @Parameter(description = "ID do pedido")
            @PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calcular")
    @Operation(summary = "Calcular total do pedido", description = "Calcula o total de um pedido sem salvá-lo", tags = {"Pedidos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total calculado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<BigDecimal>> calcularTotal(
            @Valid @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Itens para cálculo")
            List<ItemPedidoDTO> itens) {
        BigDecimal total = pedidoService.calcularTotalPedido(itens);
        ApiResponseWrapper<BigDecimal> response = new ApiResponseWrapper<>(true, total, "Total calculado com sucesso");
        return ResponseEntity.ok(response);
    }
    
}

/*
// Adicionar item ao pedido
    @PostMapping("/{pedidoId}/itens")
    public ResponseEntity<?> adicionarItem(@PathVariable Long pedidoId,
            @RequestParam Long produtoId,
            @RequestParam Integer quantidade) {
        try {
            Pedido pedido = pedidoService.adicionarItem(pedidoId, produtoId, quantidade);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }

    // Confirmar pedido
    @PutMapping("/{pedidoId}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Long pedidoId) {
        try {
            Pedido pedido = pedidoService.confirmarPedido(pedidoId);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }
*/
