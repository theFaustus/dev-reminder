package com.evil.devreminder.service.impl;

import com.evil.devreminder.domain.Note;
import com.evil.devreminder.domain.NoteType;
import com.evil.devreminder.service.CSVReaderService;
import com.evil.devreminder.service.NoteService;
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
    private final NoteService noteService;

    public List<String[]> process(InputStream is) throws Exception {
        Reader reader = new InputStreamReader(is);
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list;
        list = csvReader.readAll();
        list.stream()
                .skip(1)
                .forEach(line -> noteService.save(new Note(line[0].trim(), line[1], NoteType.valueOf(line[2].trim()))));
        reader.close();
        csvReader.close();
        return list;
    }
}
