package com.project.revhive.demo.controller;

import com.project.revhive.demo.model.Bookmark;
import com.project.revhive.demo.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@Tag(name="Bookmark Controller ", description = "All the operation related to post bookmark are performed here")
@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private static final Logger logger= LoggerFactory.getILoggerFactory(BookmarkController.class);
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @Operation(summary = "Add Bookmark")
    @PostMapping
    public ResponseEntity<String> addBookmark(@RequestParam Long userId,
                                              @RequestParam String postId) {
        logger.info("Request recieved to add bookmark for the user");
        String response= bookmarkService.addBookmark(userId, postId);
        logger.info("Post book marked successfully");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove Bookmark")
    @DeleteMapping
    public ResponseEntity<String> removeBookmark(@RequestParam Long userId,
                                                 @RequestParam String postId) {
        logger.info("Request recieved to remove bookmark for the user");
        String response= bookmarkService.removeBookmark(userId, postId);
        logger.info("Bookmarked removed successfully");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all the post bookmarked by the user")
    @GetMapping
    @GetMapping
    public List<Bookmark> getBookmarks(@RequestParam Long userId) {

        logger.info("Fetching bookmarks for userId");

        List<Bookmark> bookmarks = bookmarkService.getBookmarks(userId);

        logger.info("Found bookmarks for userId");

        return bookmarks;
    }
}
