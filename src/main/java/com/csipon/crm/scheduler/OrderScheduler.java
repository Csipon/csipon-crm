package com.csipon.crm.scheduler;

import com.csipon.crm.scheduler.cacher.impl.OrderCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * Created by Pasha on 11.05.2017.
 */
@Service
public class OrderScheduler {
    private final OrderCache orderCache;

    @Autowired
    public OrderScheduler(OrderCache orderCache) {
        this.orderCache = orderCache;
    }

    @Scheduled(cron = "${scheduler.cache.fill}")
    public void orderForActivate() {
        orderCache.fillCache();
    }


    @Scheduled(cron = "${scheduler.cache.clean}")
    public void cleanCache() {
        orderCache.cleanCache();
    }
}
