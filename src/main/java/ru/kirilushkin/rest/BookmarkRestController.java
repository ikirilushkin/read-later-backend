package ru.kirilushkin.rest;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import ru.kirilushkin.domain.Bookmark;
import ru.kirilushkin.service.BookmarkService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/bookmarks")
public class BookmarkRestController {

    private final BookmarkService bookmarkService;

    public BookmarkRestController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping
    @ApiOperation("Get bookmark list")
    public List<Bookmark> getUnread(@RequestParam boolean read) {
        return bookmarkService.getAll(read);
    }

    @PostMapping
    @ApiOperation("Add bookmark")
    public void add(@RequestBody Bookmark bookmark) {
        bookmarkService.add(bookmark);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete bookmark")
    public void delete(@PathVariable int id) {
        bookmarkService.deleteById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update bookmark read status")
    public void updateRead(@PathVariable int id, @RequestBody Bookmark bookmark) {
        bookmarkService.updateRead(id, bookmark.isRead());
    }

    @GetMapping("/search")
    @ApiOperation("Search bookmarks")
    public List<Bookmark> search(@RequestParam(required = false) Boolean unread,
                                 @RequestParam(required = false) Boolean read,
                                 @RequestParam(required = false) String tag,
                                 @RequestParam(required = false) String link) {
        return bookmarkService.search(unread, read, tag, link);
    }
}
