package com.deliverytech.delivery_api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status do pedido")
public enum StatusPedido {
    @Schema(description = "Pedido criado, aguardando confirmação")
    PENDENTE,    
    
    @Schema(description = "Pedido confirmado pelo restaurante")
    CONFIRMADO,  
    
    @Schema(description = "Pedido em preparo na cozinha")
    PREPARANDO, 

    @Schema(description = "Pedido saiu para entrega")
    SAIU_PARA_ENTREGA,

    @Schema(description = "Pedido entregue ao cliente")
    ENTREGUE,        

    @Schema(description = "Pedido cancelado")
    CANCELADO        
}

