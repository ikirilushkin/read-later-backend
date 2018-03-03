package ru.kirilushkin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kirilushkin.validator.UniqueBookmarkUrl;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

    private int id;

    @NotNull(message = "validation.error.bookmark.title.size")
    @Size(min = 1, max = 100, message = "validation.error.bookmark.title.size")
    private String title;

    @NotNull(message = "validation.error.bookmark.url.size")
    @Size(min = 1, message = "validation.error.bookmark.url.size")
    //@UniqueBookmarkUrl
    private String url;

    private List<String> tags = new ArrayList<>();

    private boolean read;
}
