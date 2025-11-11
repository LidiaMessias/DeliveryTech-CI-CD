package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

public record ClienteAtivoDTO(
    String clienteNome,
    Long totalPedidos,
    BigDecimal valorTotalGasto
) {}
