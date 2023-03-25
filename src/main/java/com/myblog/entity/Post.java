package com.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//data is lombok annotation which help us to create getters and setters
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="posts", uniqueConstraints ={@UniqueConstraint(columnNames = {"title"})})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="title", nullable=false)
    private String title;
    @Column(name="description", nullable=false)
    private String description;
    @Column(name="content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)  //mappedBy means it ll mapped the table by given column ---- cascade means we relate the post from comment tables(if we remove post so comment is also remove) ----orphanRemoval means if the post is delete then comment can delete automatically
    private Set<Comment> comments = new HashSet<>();  //here we use set because it does not consider duplicate values --- list consider duplicate values.
}
