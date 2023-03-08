package com.evil.devreminder.service;

import java.io.InputStream;
import java.util.List;

public interface CSVReaderService {
    List<String[]> process(InputStream is) throws Exception;
}
