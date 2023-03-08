package com.evil.devreminder.service;

import com.evil.devreminder.domain.Picture;
import com.evil.devreminder.domain.Trivia;

public interface PictureService {
    String getPictureOfTheDayLink();
    Picture getPictureOfTheDay();
}
