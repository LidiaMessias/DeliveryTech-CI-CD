package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PedidoPeriodoDTO(
    LocalDate data,
    Long totalPedidos,
    BigDecimal valorTotal
) {}
