package com.evil.devreminder.service;

import java.io.InputStream;
import java.util.List;

public interface CSVReaderService {
    List<String[]> read(InputStream is) throws Exception;
}
