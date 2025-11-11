package com.deliverytech.delivery_api.controller;

import java.util.List;
//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.ClienteDTO;
import com.deliverytech.delivery_api.dto.ClienteResponseDTO;
//import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.service.ClienteServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes do sistema.")
public class ClienteController {

    @Autowired
    private ClienteServiceImpl clienteService;

    // Cadastrar um novo cliente
    @PostMapping
    @Operation(summary = "Cadastrar cliente", description = "Cadastra um novo cliente no sistema", tags = {"Clientes"})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro")
    })
    public ResponseEntity<ClienteResponseDTO> cadastrarCliente(
        @Parameter(description = "Dados do cliente a ser cadastrado")
        @Valid @RequestBody ClienteDTO clienteDTO) {
        
        ClienteResponseDTO clienteSalvo = clienteService.cadastrarCliente(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);

    }

    // Buscar cliente por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Busca um cliente pelo seu ID", tags = {"Clientes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> buscarPorId(
            @Parameter(description = "ID do cliente a ser buscado")
            @PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarClientePorId(id);   
        return ResponseEntity.ok(cliente);
        
    }

    // Buscar cliente por email
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar cliente por email", description = "Busca um cliente pelo seu email", tags = {"Clientes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> buscarClientePorEmail(
            @Parameter(description = "Email do cliente a ser buscado")
            @PathVariable String email) {
        ClienteResponseDTO cliente = clienteService.buscarClientePorEmail(email);      
        return ResponseEntity.ok(cliente);
        
    }

    // Listar todos os clientes ativos
    @GetMapping
    @Operation(summary = "Listar clientes ativos", description = "Lista todos os clientes ativos no sistema", tags = {"Clientes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Clientes recuperados com sucesso")
    })
    public ResponseEntity<List<ClienteResponseDTO>> listarClientesAtivos() {
        List<ClienteResponseDTO> clientes = clienteService.listarClientesAtivos();
        return ResponseEntity.ok(clientes);
    }

    // Atualizar dados do cliente
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente", tags = {"Clientes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @Parameter(description = "ID do cliente a ser buscado") @PathVariable Long id,
            @Parameter(description = "Dados atualizados do cliente")
            @Valid @RequestBody ClienteDTO clienteDTO) {
        
        ClienteResponseDTO clienteAtualizado = clienteService.atualizarCliente(id, clienteDTO);
        return ResponseEntity.ok(clienteAtualizado);
        
    }

    // Ativar e Desativar cliente
    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar/Desativar cliente", description = "Ativa ou desativa um cliente existente", tags = {"Clientes"})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente ativado/desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> ativarDesativarCliente(
            @Parameter(description = "ID do cliente a ser ativado/desativado")
            @PathVariable Long id) {
        
        ClienteResponseDTO cliente = clienteService.ativarDesativarCliente(id);
        return ResponseEntity.ok(cliente);
        
    }

}

 /* 
    // Listar todos os clientes
    @GetMapping("/todos")
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        List<ClienteDTO> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    } 

    @GetMapping("/com-pedidos")
    public ResponseEntity<List<ClienteDTO>> listarAtivosComPedidos() {
        List<ClienteDTO> clientes = clienteService.listarClientesAtivosComPedidos();
        return ResponseEntity.ok(clientes);
    }

    // Buscar clientes por nome
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorNome(@RequestParam String nome) {
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }
*/
