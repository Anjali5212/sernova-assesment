package com.sernova.dto;

public record PersonAddressDto(Long personId,String firstName, String lastName, Long addressId, String line1,String city,String country,String type) {
}
