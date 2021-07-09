package com.example.streaming.media.index;

import com.example.streaming.media.video.VideoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
    private final VideoRepository videoRepository;

    public IndexController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @GetMapping
    public String indexPage(Model model) {
        model.addAttribute("videoList", videoRepository.findAll());
        return "index";
    }
}