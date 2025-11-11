package com.deliverytech.delivery_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para cadastro de cliente")
public class ClienteDTO {
    //private Long id;

    @Schema(description = "Nome do cliente", example = "Maria Conceição", required = true)
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Email do cliente", example = "maria@email.com", required = true)
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "O Email deve ter formato válido")
    private String email;

    @Schema(description = "Telefone para contato", example = "13912349999", required = true)
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;

    @Schema(description = "Endereço completo do cliente", example = "Rua Amazonas, 789 - Centro - Rio de Janeiro - RJ", required = true, maxLength = 200)
    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String endereco;

    /* 
    private boolean ativo;
    private LocalDateTime dataCriacao;
    private List<PedidoDTO> pedidos;
    */
}
