package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.RestauranteDTO;
import com.deliverytech.delivery_api.dto.RestauranteResponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.ConflictException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

//import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Cadastrar novo restaurante
    @Override
    public RestauranteResponseDTO cadastrarRestaurante(RestauranteDTO restauranteDto) {
        // Validar nome único
        if (restauranteRepository.findByNome(restauranteDto.getNome()).isPresent()) {
            throw new ConflictException("Já existe um restaurante cadastrado com esse nome: " + restauranteDto.getNome());
        }

        // validarDadosRestaurante(restaurante);
        validarDadosRestaurante(restauranteDto);

        // Converter DTO para entidade
        Restaurante restaurante = modelMapper.map(restauranteDto, Restaurante.class);
        restaurante.setAtivo(true);

        Restaurante novoRestaurante = restauranteRepository.save(restaurante);
        return modelMapper.map(novoRestaurante, RestauranteResponseDTO.class);
    }

    // Buscar restaurante por ID
    @Override
    @Transactional(readOnly = true)
    public RestauranteResponseDTO buscarRestaurantePorId(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + id));

        return modelMapper.map(restaurante, RestauranteResponseDTO.class); 
    }

    // Buscar por categoria
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarRestaurantesPorCategoria(String categoria){
        List<Restaurante> restaurantes = restauranteRepository.findByCategoria(categoria);

        return restaurantes.stream()
            .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
            .collect(Collectors.toList());
    }

    // Listar todos os restaurantes ativos
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> buscarRestaurantesDisponiveis() {
        List<Restaurante> restaurantesAtivos = restauranteRepository.findByAtivoTrue();

        return restaurantesAtivos.stream()
            .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
            .collect(Collectors.toList());
    }

    // Listar restaurantes por filtro
    @Override
    @Transactional(readOnly = true)
    public Page<RestauranteResponseDTO> listarRestaurantes(String categoria, Boolean ativo, Pageable pageable) {
        // Se não há filtros, aproveita paginação no repositório
        if (categoria == null && ativo == null) {
            Page<Restaurante> page = restauranteRepository.findAll(pageable);
            return page.map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class));
        }

        // Caso haja filtro, busca todos e filtra em memória (substituir por queries paginadas para produção)
        List<Restaurante> all = restauranteRepository.findAll();

        List<RestauranteResponseDTO> filtered = all.stream()
            .filter(r -> categoria == null || (r.getCategoria() != null && r.getCategoria().equalsIgnoreCase(categoria)))
            .filter(r -> ativo == null || r.isAtivo() == ativo)
            .map(r -> modelMapper.map(r, RestauranteResponseDTO.class))
            .collect(Collectors.toList());

        int total = filtered.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<RestauranteResponseDTO> content = start > end ? List.of() : filtered.subList(start, end);

        return new PageImpl<>(content, pageable, total);
    }
   
    // Atualizar dados do restaurante
    @Override
    public RestauranteResponseDTO atualizarRestaurante(Long id, RestauranteDTO dto){
        Restaurante restauranteExistente = restauranteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID: " + id));

        // Verificar se nome não está sendo usado por outro restaurante
        if (!restauranteExistente.getNome().equals(dto.getNome()) &&
                restauranteRepository.findByNome(dto.getNome()).isPresent()) {
            throw new BusinessException("Nome já cadastrado: " +
                    dto.getNome());
        }

        // Atualizar campos
        restauranteExistente.setNome(dto.getNome());
        restauranteExistente.setCategoria(dto.getCategoria());
        restauranteExistente.setEndereco(dto.getEndereco());
        restauranteExistente.setTelefone(dto.getTelefone());
        restauranteExistente.setTaxaEntrega(dto.getTaxaEntrega());

        Restaurante restauranteAtualizado = restauranteRepository.save(restauranteExistente);
        return modelMapper.map(restauranteAtualizado, RestauranteResponseDTO.class);
    }

    @Override
    @Transactional 
    public RestauranteResponseDTO alterarStatusRestaurante(Long id) {
        
        Restaurante restaurante = restauranteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado com ID " + id));

        // Alternar o status 
        boolean novoStatus = !restaurante.isAtivo();
        restaurante.setAtivo(novoStatus);

        Restaurante restauranteAtualizado = restauranteRepository.save(restaurante);
        return modelMapper.map(restauranteAtualizado, RestauranteResponseDTO.class);
        
    }

    // VALIDAÇÕES DE NEGÓCIO
    private void validarDadosRestaurante(RestauranteDTO restaurante) {
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (restaurante.getTaxaEntrega() != null &&
            restaurante.getTaxaEntrega().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Taxa de entrega não pode ser negativa");
        }
    }

}

 /*
    // Listar restaurantes por categoria e ativos
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponseDTO> listarRestaurantes(String categoria, Boolean ativo){
        List<Restaurante> restaurantes = restauranteRepository.findByCategoriaAndAtivoTrue(categoria, ativo);

        return restaurantes.stream()
            .map(restaurante -> modelMapper.map(restaurante, RestauranteResponseDTO.class))
            .collect(Collectors.toList());
    }
*/