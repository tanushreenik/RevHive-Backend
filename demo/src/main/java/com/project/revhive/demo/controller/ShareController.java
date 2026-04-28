package com.project.revhive.demo.controller;

import com.project.revhive.demo.model.Share;
import com.project.revhive.demo.service.ShareService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shares")
public class ShareController {

    private final ShareService shareService;

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
    }

    @PostMapping
    public String sharePost(@RequestParam Long userId,
                            @RequestParam String postId) {
        return shareService.sharePost(userId, postId);
    }

    @GetMapping("/count")
    public long getShareCount(@RequestParam String postId) {
        return shareService.getShareCount(postId);
    }

    @GetMapping
    public List<Share> getShares(@RequestParam String postId) {
        return shareService.getShares(postId);
    }
}