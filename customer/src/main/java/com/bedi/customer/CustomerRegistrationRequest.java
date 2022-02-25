package com.bedi.customer;

        public record CustomerRegistrationRequest(
                String firstName,
                String lastName,
                String email
        ) {
        }
