package com.csipon.crm.scheduler.cacher.impl;

import com.csipon.crm.domain.model.User;
import com.csipon.crm.scheduler.cacher.Cache;
import com.csipon.crm.domain.model.Order;
import com.csipon.crm.scheduler.searcher.OrderSearcher;
import com.csipon.crm.service.entity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Pasha on 12.05.2017.
 */
@Component
public class OrderCache extends Cache<Order> {
    /**
     * Map for caching csr orders, which need activate after hour or less
     * Long - csr id
     * {@code Map<Long,Order>} map with csr orders
     */
    private Map<Long, Map<Long, Order>> activateCache;
    private Map<Long, Map<Long, Order>> pauseCache;
    private Map<Long, Map<Long, Order>> resumeCache;
    private Map<Long, Map<Long, Order>> disableCache;
    private static final int INITIAL_CACHE_CAPACITY = 1000;

    private final OrderSearcher searcher;
    private final UserService userService;


    @Autowired
    public OrderCache(OrderSearcher searcher, UserService userService) {
        this.activateCache = new ConcurrentHashMap<>(INITIAL_CACHE_CAPACITY);
        this.pauseCache = new ConcurrentHashMap<>(INITIAL_CACHE_CAPACITY);
        this.resumeCache = new ConcurrentHashMap<>(INITIAL_CACHE_CAPACITY);
        this.disableCache = new ConcurrentHashMap<>(INITIAL_CACHE_CAPACITY);
        this.searcher = searcher;
        this.userService = userService;
    }


    public void fillCache() {
        List<User> csr = userService.getOnlineCsrs();
        cacheFiller(activateCache, searcher.searchForActivate(csr));
        cacheFiller(pauseCache, searcher.searchForPause(csr));
        cacheFiller(resumeCache, searcher.searchForResume(csr));
        cacheFiller(disableCache, searcher.searchForDisable(csr));
    }

    public Map<Long, Order> getActivateElement(Long key) {
        checkActivateId(key);
        return activateCache.get(key);
    }

    public Map<Long, Order> getPauseElement(Long key) {
        checkPauseId(key);
        return pauseCache.get(key);
    }

    public Map<Long, Order> getResumeElement(Long key) {
        checkResumeId(key);
        return resumeCache.get(key);
    }

    public Map<Long, Order> getDisableElement(Long key) {
        checkDisableId(key);
        return disableCache.get(key);
    }

    private void cacheFiller(Map<Long, Map<Long, Order>> cache, List<Order> orderList) {
        for (Order order : orderList) {
            Map<Long, Order> orders;
            if (cache.get(order.getCsr().getId()) == null
                    || cache.get(order.getCsr().getId()).isEmpty()) {
                orders = new LinkedHashMap<>();
            } else {
                orders = cache.get(order.getCsr().getId());
            }
            orders.put(order.getId(), order);
            cache.put(order.getCsr().getId(), orders);
        }
    }


    public Integer getCountElements(Long csrId) {
        checkActivateId(csrId);
        checkPauseId(csrId);
        checkResumeId(csrId);
        checkDisableId(csrId);
        return activateCache.get(csrId).keySet().size() + pauseCache.get(csrId).keySet().size() +
                resumeCache.get(csrId).keySet().size() + disableCache.get(csrId).keySet().size();
    }


    private void checkResumeId(Long csrId) {
        resumeCache.computeIfAbsent(csrId, k -> convertListOrder(searcher.searchCsrResumeOrder(csrId)));
    }

    private void checkDisableId(Long csrId) {
        disableCache.computeIfAbsent(csrId, k -> convertListOrder(searcher.searchCsrDisableOrder(csrId)));
    }

    private void checkActivateId(Long csrId) {
        activateCache.computeIfAbsent(csrId, k -> convertListOrder(searcher.searchCsrProcessingOrder(csrId)));
    }

    private void checkPauseId(Long csrId) {
        pauseCache.computeIfAbsent(csrId, k -> convertListOrder(searcher.searchCsrPauseOrder(csrId)));
    }

    public void removeFromActivateCache(Long csrId, Long orderId) {
        removerFromCache(csrId, orderId, activateCache);
    }

    public void removeFromPauseCache(Long csrId, Long orderId) {
        removerFromCache(csrId, orderId, pauseCache);
    }

    public void removeFromDisableCache(Long csrId, Long orderId) {
        removerFromCache(csrId, orderId, disableCache);
    }

    public void removeFromResumeCache(Long csrId, Long orderId) {
        removerFromCache(csrId, orderId, resumeCache);
    }

    private void removerFromCache(Long csrId, Long orderId, Map<Long, Map<Long, Order>> cache) {
        if (cache.get(csrId) != null) {
            cache.get(csrId).remove(orderId);
        }
    }


    /**
     * Take a list of online csr-s and take set of all cached csr-s
     * after remove from set all csr that is online, after clean
     * all that remained
     */
    public void cleanCache() {
        List<User> csrOnline = userService.getOnlineCsrs();
        cacheCleaner(activateCache, csrOnline);
        cacheCleaner(pauseCache, csrOnline);
        cacheCleaner(disableCache, csrOnline);
        cacheCleaner(resumeCache, csrOnline);
    }

    private void cacheCleaner(Map<Long, Map<Long, Order>> cache, List<User> csrOnline) {
        Set<Long> csrCache = new HashSet<>(cache.keySet());
        stayOfflineCsr(csrCache, csrOnline);
        cleanOfflineCsr(csrCache, cache);
    }

    /**
     * @param cacheCsr  set that stay after removing
     * @param onlineCsr array online csrs that be removed from cache csr
     */
    private void stayOfflineCsr(Set<Long> cacheCsr, List<User> onlineCsr) {
        for (User user : onlineCsr) {
            cacheCsr.remove(user.getId());
        }
    }

    private void cleanOfflineCsr(Set<Long> cacheCsr, Map<Long, Map<Long, Order>> cache) {
        for (Long id : cacheCsr) {
            cache.remove(id);
        }
    }


    private Map<Long, Order> convertListOrder(List<Order> orders) {
        Map<Long, Order> orderMap = new LinkedHashMap<>();
        for (Order order : orders) {
            orderMap.put(order.getId(), order);
        }
        return orderMap;
    }
}
