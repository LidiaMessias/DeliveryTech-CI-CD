package com.deliverytech.delivery_api.service;

import java.util.List;

import com.deliverytech.delivery_api.dto.ClienteDTO;
import com.deliverytech.delivery_api.dto.ClienteResponseDTO;

public interface ClienteService {

    ClienteResponseDTO cadastrarCliente(ClienteDTO dto);
    
    ClienteResponseDTO buscarClientePorId(Long id);

    ClienteResponseDTO buscarClientePorEmail(String email);

    ClienteResponseDTO atualizarCliente(Long id, ClienteDTO dto);

    ClienteResponseDTO ativarDesativarCliente(Long id);

    List<ClienteResponseDTO> listarClientesAtivos();

}
