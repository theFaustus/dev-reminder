package com.evil.devreminder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Crypto {
    private int rank;
    private String name;
    private String symbol;
    private double price;
    private double availableSupply;
    private double totalSupply;
    private double maxSupply;
}
