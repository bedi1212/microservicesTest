package com.bedi.customer;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
    @AllArgsConstructor
    public class CustomerService {

        private final RestTemplate restTemplate;

        private final CustomerRepositery customerRepositery;

        public void registerCustomer(CustomerRegistrationRequest request) {
            Customer customer = Customer.builder()
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .build();
            //save and flush ID
            customerRepositery.saveAndFlush(customer);

            //microservices call
            FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                    "http:localhost:8081/api/vi/fraudCheck/{customerId}",
                    FraudCheckResponse.class,
                    customer.getId()
            );

            if (fraudCheckResponse.isFraudCustomer()){
                throw new IllegalStateException("fraudster");
            }

        }
    }
