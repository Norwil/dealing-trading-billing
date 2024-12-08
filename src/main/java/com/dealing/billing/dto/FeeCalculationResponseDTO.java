package com.dealing.billing.dto;


import lombok.Data;

@Data
public class FeeCalculationResponseDTO {

    private double custodyFee;
    private double tradeFee;
    private double wireFee;
    private double totalFee;
}
