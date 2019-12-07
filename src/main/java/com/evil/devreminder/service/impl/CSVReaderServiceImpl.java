package com.evil.devreminder.service.impl;

import com.evil.devreminder.service.CSVReaderService;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CSVReaderServiceImpl implements CSVReaderService {
    public List<String[]> read(InputStream is) throws Exception {
        Reader reader =  new InputStreamReader(is);
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list;
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }
}
