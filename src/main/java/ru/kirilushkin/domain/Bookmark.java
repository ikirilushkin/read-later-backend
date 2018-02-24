package ru.kirilushkin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

    private int id;

    private String title;

    private String url;

    private List<String> tags = new ArrayList<>();

    private boolean read;
}
