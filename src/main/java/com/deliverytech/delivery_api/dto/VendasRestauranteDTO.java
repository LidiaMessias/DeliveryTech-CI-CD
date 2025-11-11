package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

public record VendasRestauranteDTO(
    String restauranteNome,
    Long totalPedidos,
    BigDecimal valorTotalVendas
) {}
