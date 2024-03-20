package com.emi.onlineshop.services;

import com.emi.onlineshop.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    public boolean executePayment(Order order) {

        LOG.info("Trying to pay {} RON.", order.getTotal());

        if (order.getTotal() < 15) {
            LOG.info("Payment successful");
            return true;
        }

        LOG.info("Not enough money");
        return false;
    }
}
