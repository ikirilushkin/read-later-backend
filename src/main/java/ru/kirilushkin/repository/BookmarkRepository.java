package ru.kirilushkin.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kirilushkin.domain.Bookmark;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class BookmarkRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<Bookmark> bookmarkMapper = (rs, i) -> {
        Object[] arr = (Object[]) rs.getArray("tags").getArray();
        List<String> tags = new ArrayList<>();
        Arrays.asList(arr).forEach(item -> tags.add(item.toString()));
        return new Bookmark(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("url"),
                tags,
                rs.getBoolean("read")
        );
    };

    public BookmarkRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Bookmark bookmark) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", bookmark.getTitle());
        params.addValue("url", bookmark.getUrl());
        params.addValue("tags", bookmark.getTags().toArray());
        jdbcTemplate.update(
                "INSERT INTO bookmarks (title, url, tags) VALUES (:title, :url, :tags)",
                params
        );
    }

    public void deleteById(int id) {
        jdbcTemplate.update(
                "DELETE FROM bookmarks WHERE id = :id;",
                Map.of("id", id)
        );
    }

    public void updateRead(int id, boolean read) {
        jdbcTemplate.update(
                "UPDATE bookmarks SET read = :read WHERE id = :id;",
                Map.of("id", id, "read", read)
        );
    }

    public List<Bookmark> getAll(boolean read) {
        return jdbcTemplate.query(
                "SELECT id, title, url, tags, read FROM bookmarks WHERE read = :read;",
                Map.of("read", read),
                bookmarkMapper
        );
    }

    public List<Bookmark> search(Boolean unread, Boolean read, String tag, String link) {
        StringBuilder sb = new StringBuilder("SELECT id, title, url, tags, read FROM bookmarks");
        List<String> conditions = createSearchConditions(unread, read, tag, link);
        if (conditions.size() > 0) {
            sb.append(" WHERE ");
            sb.append(conditions.get(0) + " ");
            conditions = conditions.subList(1, conditions.size());
            conditions.forEach(condition -> sb.append("AND " + condition + " "));
        }
        sb.append(";");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("unread", false);
        params.addValue("read", true);
        params.addValue("tag", "%" + tag + "%");
        params.addValue("link", "%" + link + "%");
        return jdbcTemplate.query(
                sb.toString(),
                params,
                bookmarkMapper
        );
    }

    private List<String> createSearchConditions(Boolean unread, Boolean read, String tag, String link) {
        List<String> conditions = new ArrayList<>();
        getReadConditions(conditions, unread, read);
        if (tag != null) {
            conditions.add("tags LIKE :tag");
        }
        if (link != null) {
            conditions.add("title LIKE :link OR url LIKE :link");
        }
        return conditions;
    }

    private void getReadConditions(List<String> conditions, Boolean unread, Boolean read) {
        if (unread == true || read == true) {
            StringBuilder sb = new StringBuilder("(");
            if (unread) {
                sb.append("read = :unread ");
            } if (read) {
                if (unread) {
                    sb.append("OR ");
                }
                sb.append("read = :read");
            }
            sb.append(")");
            conditions.add(sb.toString());
        }
    }

    public Bookmark getByUrl(String url) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, title, url, tags, read FROM bookmarks WHERE url = :url",
                    Map.of("url", url),
                    bookmarkMapper
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }
}
