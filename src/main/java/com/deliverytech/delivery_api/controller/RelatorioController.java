package com.deliverytech.delivery_api.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.ApiResponseWrapper;
import com.deliverytech.delivery_api.dto.ClienteAtivoDTO;
import com.deliverytech.delivery_api.dto.PedidoPeriodoDTO;
import com.deliverytech.delivery_api.dto.ProdutoMaisVendidoDTO;
import com.deliverytech.delivery_api.dto.VendasRestauranteDTO;
import com.deliverytech.delivery_api.service.RelatorioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
@Tag(name = "Relatórios", description = "Operações relacionadas à relatórios do sistema.")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    // Relatório de vendas por restaurante
    @GetMapping("/vendas-por-restaurante")
    @Operation(summary = "Total de vendas do restaurante", description = "Soma todos os pedidos de um restaurante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<List<VendasRestauranteDTO>>> getVendasPorRestaurante() {

        List<VendasRestauranteDTO> vendas = relatorioService.getVendasPorRestaurante();
        ApiResponseWrapper<List<VendasRestauranteDTO>> response = new ApiResponseWrapper<>(true, vendas, "Relatório calculado com sucesso");
        
        return ResponseEntity.ok(response);
    }

    // Relatório dos produtos mais vendidos
    @GetMapping("/produtos-mais-vendidos")
    @Operation(summary = "Ranking dos produtos mais vendidos", description = "Relatório com os produtos mais vendidos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Relatório recuperado com sucesso")
    })
    public ResponseEntity<ApiResponseWrapper<List<ProdutoMaisVendidoDTO>>> getProdutosMaisVendidos() {

        List<ProdutoMaisVendidoDTO> maisVendidos = relatorioService.getProdutosMaisVendidos();
        ApiResponseWrapper<List<ProdutoMaisVendidoDTO>> response = new ApiResponseWrapper<>(true, maisVendidos, "Relatório recuperado com sucesso");
        
        return ResponseEntity.ok(response);
    }

    // Relatório dos clientes mais ativos
    @GetMapping("/clientes-ativos")
    @Operation(summary = "Relatório dos clientes mais ativos", description = "Lista os clientes que mais efetuam compras")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Relatório recuperado com sucesso")
    })
    public ResponseEntity<ApiResponseWrapper<List<ClienteAtivoDTO>>> getClientesAtivos() {
        List<ClienteAtivoDTO> maisAtivos = relatorioService.getClientesAtivos();
        ApiResponseWrapper<List<ClienteAtivoDTO>> response = new ApiResponseWrapper<>(true, maisAtivos, "Relatório recuperado com sucesso");
        
        return ResponseEntity.ok(response);
    }

    // Relatório de pedidos por período
    @GetMapping("/pedidos-por-periodo")
    @Operation(summary = "Relatório de pedidos por período", description = "Lista os pedidos dentro de um determinado período")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedidos recuperados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum pedido efetuado nesse período")
    })
    public ResponseEntity<ApiResponseWrapper<List<PedidoPeriodoDTO>>> getPedidosPorPeriodo(
            @Parameter(description = "Data inicial")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        // Conversão de LocalDate para LocalDateTime no Controller:
        LocalDateTime inicio = dataInicio.atStartOfDay();        // 00:00:00
        LocalDateTime fim = dataFim.atTime(23, 59, 59);

        List<PedidoPeriodoDTO> relatorio = relatorioService.getPedidosPorPeriodo(inicio, fim);
        ApiResponseWrapper<List<PedidoPeriodoDTO>> response = new ApiResponseWrapper<>(true, relatorio, "Pedidos recuperados com sucesso");
    
        return ResponseEntity.ok(response);
    }
}
