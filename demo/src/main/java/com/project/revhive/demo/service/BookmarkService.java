package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Bookmark;
import com.project.revhive.demo.repository.BookmarkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public String addBookmark(Long userId, String postId) {

        if (bookmarkRepository.existsByUserIdAndPostId(userId, postId)) {
            return "Already bookmarked";
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setUserId(userId);
        bookmark.setPostId(postId);

        bookmarkRepository.save(bookmark);
        return "Bookmarked successfully";
    }

    public String removeBookmark(Long userId, String postId) {

        return bookmarkRepository.findAll()
                .stream()
                .filter(b -> b.getUserId().equals(userId)
                        && b.getPostId().equals(postId))
                .findFirst()
                .map(b -> {
                    bookmarkRepository.delete(b);
                    return "Bookmark removed";
                })
                .orElse("Bookmark not found");
    }

    public List<Bookmark> getBookmarks(Long userId) {
        return bookmarkRepository.findByUserId(userId);
    }
}