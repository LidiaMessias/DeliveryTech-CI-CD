package com.deliverytech.delivery_api.service;

import org.springframework.stereotype.Service;

import brave.Span;
import brave.Tracer;

@Service
public class TracingService {

    private final Tracer tracer;

    public TracingService(Tracer tracer) {
        this.tracer = tracer;
    }

    public void processarPedidoComTracing(String pedidoId) {
        Span span = tracer.nextSpan()
                .name("processar-pedido")
                .tag("pedido.id", pedidoId)
                .start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            validarPedidoComSpan(pedidoId);
            calcularFreteComSpan(pedidoId);
            processarPagamentoComSpan(pedidoId);
        } finally {
            span.finish();
        }
    }

    private void validarPedidoComSpan(String pedidoId) {
        Span span = tracer.nextSpan()
                .name("validar-pedido")
                .tag("pedido.id", pedidoId)
                .start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            // Simular validação
            Thread.sleep(50);
            span.tag("validacao.resultado", "sucesso");
        } catch (Exception e) {
            span.tag("error", e.getMessage());
        } finally {
            span.finish();
        }
    }

    private void calcularFreteComSpan(String pedidoId) {
        Span span = tracer.nextSpan()
                .name("calcular-frete")
                .tag("pedido.id", pedidoId)
                .start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            // Simular cálculo
            Thread.sleep(30);
            span.tag("frete.valor", "15.50");
        } catch (Exception e) {
            span.tag("error", e.getMessage());
        } finally {
            span.finish();
        }
    }

    private void processarPagamentoComSpan(String pedidoId) {
        Span span = tracer.nextSpan()
                .name("processar-pagamento")
                .tag("pedido.id", pedidoId)
                .start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            // Simular pagamento
            Thread.sleep(100);
            span.tag("pagamento.status", "aprovado");
        } catch (Exception e) {
            span.tag("error", e.getMessage());
        } finally {
            span.finish();
        }
    }

}
