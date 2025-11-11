package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.deliverytech.delivery_api.dto.PagedResponseWrapper;
import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
//import com.deliverytech.delivery_api.model.Restaurante;
//import com.deliverytech.delivery_api.service.RestauranteService;
import com.deliverytech.delivery_api.service.RestauranteServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/restaurantes")
@Validated
@CrossOrigin(origins = "*")
@Tag(name = "Restaurantes", description = "Operações relacionadas ao gerenciamento de restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteServiceImpl restauranteService;

    // Cadastrar um novo restaurante
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Cadastrar restaurante", 
        description = "Cria um novo restaurante no sistema. Requer permissão de administrador.", 
        security = @SecurityRequirement(name = "Bearer Authentication"), 
        tags = {"Restaurantes"} 
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "409", description = "Restaurante já existe")
    })
    public ResponseEntity<ApiResponseWrapper<RestauranteResponseDTO>> cadastrarRestaurante(
        @Valid @RequestBody
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do restaurante a ser criado")
        RestauranteDTO restauranteDto) {
        
        RestauranteResponseDTO restauranteSalvo = restauranteService.cadastrarRestaurante(restauranteDto);
        ApiResponseWrapper<RestauranteResponseDTO> response = 
            new ApiResponseWrapper<>(true, restauranteSalvo, "Restaurante criado com sucesso");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        
    }

    // Listar restaurantes com filtros opcionais
    @GetMapping
    @Operation(summary = "Listar restaurantes", description = "Lista restaurantes com filtros opcionais e paginação", tags = {"Restaurantes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", 
            content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = RestauranteResponseDTO.class)
))
    })
    public ResponseEntity<PagedResponseWrapper<RestauranteResponseDTO>> listarRestaurantes(
        @Parameter(description = "Categoria do restaurante") @RequestParam(required = false) String categoria,
        @Parameter(description = "Status ativo do restaurante") @RequestParam(required = false) Boolean ativo,
        @Parameter(description = "Parâmetros de paginação") Pageable pageable) {

        Page<RestauranteResponseDTO> restaurantes = restauranteService.listarRestaurantes(categoria, ativo, pageable);
    
        PagedResponseWrapper<RestauranteResponseDTO> response = new PagedResponseWrapper<>(restaurantes);
        return ResponseEntity.ok(response);
    }

    // Buscar restaurante por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Recupera um restaurante específico pelo ID", tags = {"Restaurantes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<RestauranteResponseDTO>> buscarRestaurantePorId(
            @Parameter(description = "Id do restaurante")
            @PathVariable @Positive(message = "O ID deve ser positivo") Long id) {
        RestauranteResponseDTO restaurante = restauranteService.buscarRestaurantePorId(id);
        ApiResponseWrapper<RestauranteResponseDTO> response =
            new ApiResponseWrapper<>(true, restaurante, "Restaurante encontrado");
        return ResponseEntity.ok(response);
    }

    // Atualizar dados do restaurante
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('RESTAURANTE') and @restauranteService.isOwner(#id))")
    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente", tags = {"Restaurantes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ApiResponseWrapper<RestauranteResponseDTO>> atualizarRestaurante(
            @Parameter(description = "ID do restaurante")
            @PathVariable @Positive(message = "O ID deve ser positivo") Long id,   
            @Valid @RequestBody RestauranteDTO restauranteDto) {
        
        RestauranteResponseDTO restauranteAtualizado = restauranteService.atualizarRestaurante(id, restauranteDto);
        ApiResponseWrapper<RestauranteResponseDTO> response =
            new ApiResponseWrapper<>(true, restauranteAtualizado, "Restaurante atualizado com sucesso");
        
        return ResponseEntity.ok(response);        
    }

    // Alterar status do restaurante
    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar/Desativar restaurante", description = "Alterna o status a􀆟vo/ina􀆟vo do restaurante", tags = {"Restaurantes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<ApiResponseWrapper<RestauranteResponseDTO>> alterarStatus(
            @Parameter(description = "ID do restaurante")
            @PathVariable @Positive(message = "O ID deve ser positivo") Long id) {

        RestauranteResponseDTO restaurante = restauranteService.alterarStatusRestaurante(id);
        ApiResponseWrapper<RestauranteResponseDTO> response =
            new ApiResponseWrapper<>(true, restaurante, "Status alterado com sucesso");

        return ResponseEntity.ok(response);
    }

    // Buscar por categoria
    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar por categoria", description = "Lista restaurantes de uma categoria específica", tags = {"Restaurantes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurantes encontrados")
    })
    public ResponseEntity<ApiResponseWrapper<List<RestauranteResponseDTO>>> buscarRestaurantesPorCategoria(
        @Parameter(description = "Categoria do restaurante")
        @PathVariable String categoria) {

        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarRestaurantesPorCategoria(categoria);
        ApiResponseWrapper<List<RestauranteResponseDTO>> response =
            new ApiResponseWrapper<>(true, restaurantes, "Restaurantes encontrados");

        return ResponseEntity.ok(response);
    }

}

/*

    // Listar todos os restaurantes ativos
    @GetMapping
    @Operation(summary = "Listar restaurantes ativos", description = "Lista todos os restaurantes ativos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    })
    public ResponseEntity<ApiResponseWrapper<List<RestauranteResponseDTO>>> buscarRestaurantesDisponiveis() {
        List<RestauranteResponseDTO> restaurantes = restauranteService.buscarRestaurantesDisponiveis();
        ApiResponseWrapper<List<RestauranteResponseDTO>> response =
            new ApiResponseWrapper<>(true, restaurantes, "Restaurantes encontrados");
        
            return ResponseEntity.ok(response);
    }
            
    @GetMapping("/{id}/taxa-entrega/{cep}")
    @Operation(summary = "Calcular taxa de entrega",
    description = "Calcula a taxa de entrega para um CEP específico")
    @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Taxa calculada com sucesso"),
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEn􀆟ty<ApiResponseWrapper<BigDecimal>> calcularTaxaEntrega(
        @Parameter(descrip􀆟on = "ID do restaurante")
        @PathVariable Long id,
        @Parameter(descrip􀆟on = "CEP de des􀆟no")
        @PathVariable String cep) {
        BigDecimal taxa = restauranteService.calcularTaxaEntrega(id, cep);
        ApiResponseWrapper<BigDecimal> response =
        new ApiResponseWrapper<>(true, taxa, "Taxa calculada com sucesso");
        return ResponseEn􀆟ty.ok(response);
    }

    @GetMapping("/proximos/{cep}")
    @Opera􀆟on(summary = "Restaurantes próximos",
    descrip􀆟on = "Lista restaurantes próximos a um CEP")
    @ApiResponses({
    @ApiResponse(responseCode = "200", descrip􀆟on = "Restaurantes próximos
    encontrados")
    })
    public ResponseEn􀆟ty<ApiResponseWrapper<List<RestauranteResponseDTO>>> buscarProximos(
        @Parameter(descrip􀆟on = "CEP de referência")
        @PathVariable String cep,
        @Parameter(descrip􀆟on = "Raio em km")
        @RequestParam(defaultValue = "10") Integer raio) {
        List<RestauranteResponseDTO> restaurantes =
        restauranteService.buscarRestaurantesProximos(cep, raio);
        ApiResponseWrapper<List<RestauranteResponseDTO>> response =
        new ApiResponseWrapper<>(true, restaurantes, "Restaurantes próximos encontrados");
        return ResponseEn􀆟ty.ok(response);
    }

 */
