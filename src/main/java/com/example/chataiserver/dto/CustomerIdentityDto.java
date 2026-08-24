package com.example.chataiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** The minimum information needed to identify a customer in a search result. */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerIdentityDto {

    private String customerId;
    private String fullName;
}
