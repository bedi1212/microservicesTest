package com.bedi.customer;

import com.bedi.clients.fraud.FraudCheckResponse;
import com.bedi.clients.fraud.FraudClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
    @AllArgsConstructor
    public class CustomerService {

        private final RestTemplate restTemplate;

        private final CustomerRepositery customerRepositery;

        private final FraudClient fraudClient;

        public void registerCustomer(CustomerRegistrationRequest request) {
            Customer customer = Customer.builder()
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .build();
            //save and flush ID
            customerRepositery.saveAndFlush(customer);

//            //microservices call
//            FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
//                    "http://FRAUD:8081/api/vi/fraudCheck/{customerId}",
//                    FraudCheckResponse.class,
//                    customer.getId()
//            );

            FraudCheckResponse fraudCheckResponse= fraudClient.isFraudster(customer.getId());

            if (fraudCheckResponse.isFraudster()){
                throw new IllegalStateException("fraudster");
            }



        }
    }
