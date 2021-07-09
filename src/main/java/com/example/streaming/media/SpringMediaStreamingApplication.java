package com.example.streaming.media;

import com.example.streaming.media.video.Video;
import com.example.streaming.media.video.VideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringMediaStreamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMediaStreamingApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(VideoRepository videoRepository){
        return args -> {
            videoRepository.save(Video.builder().filePath("/video/sample_mp4_file.mp4").build());
            videoRepository.save(Video.builder().filePath("/video/Sample_Video_File_For_Testing.mp4").build());
        };
    }
}