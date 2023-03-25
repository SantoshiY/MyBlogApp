package com.myblog.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentDTO {
    private long id;
    @NotEmpty(message = "it's mandatory!!")
    @Size(min=2, message = "Name must be 2 characters")
    private String name;
    @Email
    private String email;
    @NotEmpty
    @Size(min=5, message = "it must be 5 characters.")
    private String body;
    //private PostDto postDto;
}
