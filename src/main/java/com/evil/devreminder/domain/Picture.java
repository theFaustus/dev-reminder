package com.evil.devreminder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Picture {
    private String copyRight;
    private String explanation;
    private String url;
    private String title;
}
