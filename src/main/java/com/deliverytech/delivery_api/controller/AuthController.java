package com.deliverytech.delivery_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.UserDetails;

import com.deliverytech.delivery_api.dto.LoginRequest;
import com.deliverytech.delivery_api.dto.LoginResponse;
import com.deliverytech.delivery_api.dto.RegisterRequest;
import com.deliverytech.delivery_api.dto.UserResponse;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.security.SecurityUtils;
import com.deliverytech.delivery_api.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticação", description = "Operações de autenticação, autorização e registro de usuários")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @PostMapping("/login")
    @Operation(summary = "Login do usuário", description = "Autentica o usuário e retorna um token JWT", tags = {"Autenticação"})
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponse.class),
                examples = @ExampleObject(
                    name = "Login bem-sucedido",
                    value = """
                    {
                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "tipo": "Bearer",
                        "expiracao": 86400000,
                        "usuario": {
                            "id": 1,
                            "nome": "João Silva",
                            "email": "joao@email.com",
                            "role": "CLIENTE"
                        }
                    }
                """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<?> login(
        @Parameter(description = "Dados de login do usuário")
        @Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Autenticar usuário
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            ));

            // Carregar detalhes do usuário
            UserDetails userDetails = authService.loadUserByUsername(loginRequest.getEmail());

            // Gerar token JWT
            String token = jwtUtil.generateToken(userDetails);

            // Criar resposta
            Usuario usuario = (Usuario) userDetails;
            UserResponse userResponse = new UserResponse(usuario);
            LoginResponse loginResponse = new LoginResponse(token, "Bearer", jwtExpiration, userResponse);
            
            return ResponseEntity.ok(loginResponse);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno do servidor");
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Registro de novo usuário", description = "Registra um novo usuário no sistema", tags = {"Autenticação"})
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Usuário registrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponse.class),
                examples = @ExampleObject(
                    name = "Registro bem-sucedido",
                    value = """
                    {
                        "id": 2,
                        "nome": "Maria Souza",
                        "email": "mariasouza@email.com",
                        "role": "CLIENTE"
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Email já está em uso"),
        @ApiResponse(responseCode = "500", description = "Erro ao criar usuário")
    })
    public ResponseEntity<?> register(
        @Parameter(description = "Dados para registro de novo usuário")
        @Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Verificar se email já existe
            if (authService.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body("Email já está em uso");
            }

            // Criar novo usuário
            Usuario novoUsuario = authService.criarUsuario(registerRequest);

            // Retornar dados do usuário (sem token - usuário deve fazer login)
            UserResponse userResponse = new UserResponse(novoUsuario);
            return ResponseEntity.status(201).body(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Informações do usuário autenticado", description = "Recupera os detalhes do usuário atualmente autenticado", tags = {"Autenticação"})
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Detalhes do usuário recuperados com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponse.class),
                examples = @ExampleObject(
                    name = "Usuário autenticado",
                    value = """
                    {
                        "id": 1,
                        "nome": "João Silva",
                        "email": "
                        "role": "CLIENTE"
                    }
                    """
                )   
            )
        ),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    public ResponseEntity<?> getCurrentUser() {
        try {
            Usuario usuarioLogado = SecurityUtils.getCurrentUser();
            UserResponse userResponse = new UserResponse(usuarioLogado);
            return ResponseEntity.ok(userResponse);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
    }

}
