package com.example.streaming.media.rangeRequests;

import com.example.streaming.media.video.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/range-requests")
public class RangeRequestsController {
    private final VideoRepository videoRepository;

    public RangeRequestsController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @GetMapping("/{id}")
    public String rangeRequestPage(@PathVariable String id, Model model) {
        model.addAttribute("id", id);
        return "range-requests";
    }

    @GetMapping("/video/{id}")
    public ResponseEntity<ResourceRegion> getPartialVideo(
            @RequestHeader HttpHeaders headers,
            @PathVariable String id) throws IOException
    {
        log.info("Video ID : {} START", id);

        try{
            var video = videoRepository.getById(Long.parseLong(id));
            var urlResource = new UrlResource("classpath", video.getFilePath());

            ResourceRegion resourceRegion;
            HttpStatus httpStatus;

            Optional<HttpRange> httpRangeOptional = headers.getRange().stream().findFirst();
            // Range 헤더가 존재하면 206으로 부분 응답
            if(httpRangeOptional.isPresent()) {
                var httpRange = httpRangeOptional.get();

                long start = httpRange.getRangeStart(urlResource.contentLength());
                long end = httpRange.getRangeEnd(urlResource.contentLength());

                log.info("Video ID : {} RANGE REQUEST {}-{}/{}", id, start, end, urlResource.contentLength());

                resourceRegion = new ResourceRegion(urlResource, start, end - start + 1);
                httpStatus = HttpStatus.PARTIAL_CONTENT;
            }
            // Range 헤더가 존재하지 않으면 200으로 전체 응답
            else {
                log.info("Video ID : {} FULL REQUEST", id);

                resourceRegion = new ResourceRegion(urlResource, 0, urlResource.contentLength());
                httpStatus = HttpStatus.OK;
            }

            log.info("Video ID : {} END", id);

            return ResponseEntity
                    .status(httpStatus)
                    .contentType(MediaTypeFactory.getMediaType(urlResource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resourceRegion);
        }
        catch(EntityNotFoundException | FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}