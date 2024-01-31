package com.vinsguru.webfluxpatterns.sec03.service;

import com.vinsguru.webfluxpatterns.sec03.client.ProductClient;
import com.vinsguru.webfluxpatterns.sec03.dto.*;
import com.vinsguru.webfluxpatterns.sec03.util.DebugUtil;
import com.vinsguru.webfluxpatterns.sec03.util.OrchestrationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderFulfillmentService fulfillmentService;

    @Autowired
    private OrderCancellationService cancellationService;

    public Mono<OrderResponse> placeOrder(Mono<OrderRequest> mono){
        return mono
                .map(OrchestrationRequestContext::new)
                .flatMap(this::getProduct) // get product price
                .doOnNext(OrchestrationUtil::buildRequestContext) //generate the payment, inventory and shipping request with a new order id
                .flatMap(fulfillmentService::placeOrder) // call payment and shipping
                .doOnNext(this::doOrderPostProcessing) // if status failed, do cancellation
                .doOnNext(DebugUtil::print) // just for debugging
                .map(this::toOrderResponse);
    }

    private Mono<OrchestrationRequestContext> getProduct(OrchestrationRequestContext ctx){
        return this.productClient.getProduct(ctx.getOrderRequest().getProductId())
                .map(Product::getPrice)
                .doOnNext(ctx::setProductPrice)
                .map(i -> ctx); // if no product found, return empty here, map wont execute here if no product response
        		// .thenReturn(ctx); // if no product found, return context here irrespective of product response. product response is empty in the context here
    }

    private void doOrderPostProcessing(OrchestrationRequestContext ctx){
        if(Status.FAILED.equals(ctx.getStatus()))
            this.cancellationService.cancelOrder(ctx);
    }

    private OrderResponse toOrderResponse(OrchestrationRequestContext ctx){
        var isSuccess = Status.SUCCESS.equals(ctx.getStatus());
        var address = isSuccess ? ctx.getShippingResponse().getAddress() : null;
        var deliveryDate = isSuccess ? ctx.getShippingResponse().getExpectedDelivery() : null;
        return OrderResponse.create(
            ctx.getOrderRequest().getUserId(),
            ctx.getOrderRequest().getProductId(),
            ctx.getOrderId(),
            ctx.getStatus(),
            address,
            deliveryDate
        );
    }

}
