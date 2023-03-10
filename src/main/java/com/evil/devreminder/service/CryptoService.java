package com.evil.devreminder.service;

import com.evil.devreminder.domain.Crypto;
import com.evil.devreminder.domain.CryptoFearGreedIndex;

import java.util.List;

public interface CryptoService {
    CryptoFearGreedIndex getCryptoFearGreedIndex();

    List<Crypto> getTop5Cryptos();
}
