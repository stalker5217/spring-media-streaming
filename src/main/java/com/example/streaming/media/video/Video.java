package com.example.streaming.media.video;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
public class Video {
    @Id @GeneratedValue
    private Long id;

    private String filePath;
}
