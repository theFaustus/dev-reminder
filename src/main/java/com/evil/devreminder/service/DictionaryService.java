package com.evil.devreminder.service;

import com.evil.devreminder.domain.Word;

public interface DictionaryService {
    public Word getRomanianWordOfTheDay();
    public Word getRomanianDefinitionFor(String term);
    public Word getEnglishWordOfTheDay();
}
