package ru.kirilushkin.repository;

import ru.kirilushkin.domain.Bookmark;

import java.util.List;

public interface BookmarkRepository {
    void add(Bookmark bookmark);

    void deleteById(int id);

    void updateReadStatus(int id, boolean read);

    List<Bookmark> getAll(boolean read);

    List<Bookmark> search(Boolean unread, Boolean read, String tag, String link);

    Bookmark getByUrl(String url);
}
