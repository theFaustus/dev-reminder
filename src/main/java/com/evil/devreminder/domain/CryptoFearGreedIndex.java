package com.evil.devreminder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CryptoFearGreedIndex {
    private long index;
    private LocalDateTime timestamp;
    private CryptoFearGreedType cryptoFearGreedType;
}
