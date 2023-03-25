package com.myblog.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PostDto {
    private long id;
    @NotEmpty
    @Size(min=2, message = "title must be 2 characters")
    private String title;
    @NotEmpty
    @Size(min=10, message = "Description must be 10 characters")
    private String description;
    @NotEmpty
    @Size(min=10, message = "Content must be 10 characters")
    private String content;
}
