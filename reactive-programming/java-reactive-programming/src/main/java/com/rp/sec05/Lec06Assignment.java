package com.rp.sec05;

import com.rp.courseutil.Util;
import com.rp.sec05.assignment.InventoryService;
import com.rp.sec05.assignment.OrderService;
import com.rp.sec05.assignment.RevenueService;

public class Lec06Assignment {

    public static void main(String[] args) {

    	/*
    	 * OrderService - generating the purchase orders for various category products
    	 * InventoryService - Monitors the inventory for Kids and Automotive product categories
    	 * RevenueService - Monitors the revenue generated for Kids and Automotive product categories
    	 * */
        OrderService orderService = new OrderService();
        RevenueService revenueService = new RevenueService();
        InventoryService inventoryService = new InventoryService();

        // revenue and inv - observe the order stream
        orderService.orderStream().subscribe(revenueService.subscribeOrderStream());
        orderService.orderStream().subscribe(inventoryService.subscribeOrderStream());

        inventoryService.inventoryStream()
                .subscribe(Util.subscriber("inventory"));

        revenueService.revenueStream()
                .subscribe(Util.subscriber("revenue"));

        Util.sleepSeconds(60);

    }


}
