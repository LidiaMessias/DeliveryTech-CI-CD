package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.ApiResponseWrapper;
import com.deliverytech.delivery_api.dto.ProdutoDTO;
import com.deliverytech.delivery_api.dto.ProdutoResponseDTO;
import com.deliverytech.delivery_api.service.ProdutoServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
@Tag(name = "Produtos", description = "Operações relacionadas aos produtos")
public class ProdutoController {

    @Autowired
    private ProdutoServiceImpl produtoService;

    // Cadastrar um novo produto
    @PostMapping
    @PreAuthorize("hasRole('RESTAURANTE') or hasRole('ADMIN')")
    @Operation(
        summary = "Cadastrar produto", 
        description = "Cria um novo produto no sistema",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        tags = {"Produtos"}
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<ProdutoResponseDTO>> cadastrarProduto(
            @Valid @RequestBody 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do produto a ser criado")
            ProdutoDTO produtoDto,
            @RequestParam Long restauranteId) {
        
        ProdutoResponseDTO produtoSalvo = produtoService.cadastrarProduto(produtoDto, restauranteId);
        ApiResponseWrapper<ProdutoResponseDTO> response = new ApiResponseWrapper<>(true, produtoSalvo, "Produto criado com sucesso");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    
    // Listar produtos por restaurante
    @GetMapping("/{restauranteId}/produtos")
    @Operation(summary = "Produtos do restaurante", description = "Lista todos os produtos de um restaurante", tags = {"Produtos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<List<ProdutoResponseDTO>>> buscarProdutosPorRestaurante(
            @Parameter(description = "ID do restaurante")
            @PathVariable Long restauranteId) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorRestaurante(restauranteId);
        ApiResponseWrapper<List<ProdutoResponseDTO>> response = new ApiResponseWrapper<>(true, produtos, "Produtos encontrados");
        return ResponseEntity.ok(response);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Recupera um produto específico pelo ID", tags = {"Produtos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<ProdutoResponseDTO>> buscarProdutoPorId(
            @Parameter(description = "ID do produto")
            @PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarProdutoPorId(id);
        ApiResponseWrapper<ProdutoResponseDTO> response = new ApiResponseWrapper<>(true, produto, "Produto encontrado");
        return ResponseEntity.ok(response);    
    }
    
    // Atualizar produto
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @produtoService.isOwner(#id)")
    @Operation(
        summary = "Atualizar produto", 
        description = "Atualiza os dados de um produto existente",
        tags = {"Produtos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ApiResponseWrapper<ProdutoResponseDTO>> atualizarProduto(
            @Parameter(description = "ID do produto")
            @PathVariable Long id,
            @Valid @RequestBody ProdutoDTO produtoDto) {
        
        ProdutoResponseDTO produtoAtualizado = produtoService.atualizarProduto(id, produtoDto);
        ApiResponseWrapper<ProdutoResponseDTO> response = new ApiResponseWrapper<>(true, produtoAtualizado, "Produto atualizado com sucesso");
        return ResponseEntity.ok(response);  
    }

    // Remover Produto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @produtoService.isOwner(#id)")
    @Operation(summary = "Remover produto", description = "Remove um produto do sistema", tags = {"Produtos"})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "409", description = "Produto possui pedidos associados")
    })
    public ResponseEntity<Void> removerProduto(
            @Parameter(description = "ID do produto")
            @PathVariable Long id) {
        produtoService.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

    // Alterar disponibilidade
    @PatchMapping("/{id}/disponibilidade")
    @Operation(summary = "Alterar disponibilidade", description = "Alterna a disponibilidade do produto", tags = {"Produtos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Disponibilidade alterada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<ProdutoResponseDTO>> alterarDisponibilidade(
            @Parameter(description = "ID do produto")
            @PathVariable Long id,
            @RequestParam boolean disponivel) {
        
        ProdutoResponseDTO produto = produtoService.alterarDisponibilidade(id, disponivel);
        ApiResponseWrapper<ProdutoResponseDTO> response = new ApiResponseWrapper<>(true, produto, "Disponibilidade alterada com sucesso");
        return ResponseEntity.ok(response);
    }
    
    // Buscar por categoria
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar por categoria", description = "Lista produtos de uma categoria específica", tags = {"Produtos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    })
    public ResponseEntity<ApiResponseWrapper<List<ProdutoResponseDTO>>> buscarProdutosPorCategoria(
            @Parameter(description = "Categoria do produto")
            @PathVariable String categoria) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorCategoria(categoria);
        ApiResponseWrapper<List<ProdutoResponseDTO>> response = new ApiResponseWrapper<>(true, produtos, "Produtos encontrados");
        return ResponseEntity.ok(response);
    }

    // Buscar por Nome
    @GetMapping("/buscar")
    @Operation(summary = "Buscar por nome", description = "Busca produtos pelo nome", tags = {"Produtos"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public ResponseEntity<ApiResponseWrapper<List<ProdutoResponseDTO>>>buscarPorNome(
            @Parameter(description = "Nome do produto")
            @RequestParam String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarProdutosPorNome(nome);
        ApiResponseWrapper<List<ProdutoResponseDTO>> response =
        new ApiResponseWrapper<>(true, produtos, "Busca realizada com sucesso");
        return ResponseEntity.ok(response);
    }
}

    /* 
    // Buscar por faixa de preço
    @GetMapping("/faixa-preco")
    public ResponseEntity<List<Produto>> buscarPorFaixaPreco(@RequestParam("precoMin") String precoMinStr,
            @RequestParam("precoMax") String precoMaxStr) {
        try {
            // Converter os parâmetros de String para BigDecimal
            BigDecimal precoMin = new BigDecimal(precoMinStr);
            BigDecimal precoMax = new BigDecimal(precoMaxStr);

            List<Produto> produtos = produtoService.buscarPorFaixaPreco(precoMin, precoMax);
            return ResponseEntity.ok(produtos);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    */
