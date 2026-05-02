package com.project.revhive.demo.controller;

import com.project.revhive.demo.model.Bookmark;
import com.project.revhive.demo.service.BookmarkService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping
    public String addBookmark(@RequestParam Long userId,
                              @RequestParam String postId) {
        return bookmarkService.addBookmark(userId, postId);
    }

    @DeleteMapping
    public String removeBookmark(@RequestParam Long userId,
                                 @RequestParam String postId) {
        return bookmarkService.removeBookmark(userId, postId);
    }

    @GetMapping
    public List<Bookmark> getBookmarks(@RequestParam Long userId) {
        return bookmarkService.getBookmarks(userId);
    }
}