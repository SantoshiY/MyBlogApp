package com.myblog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String body;
    @ManyToOne(fetch=FetchType.LAZY)   // fetchType.LAZY means when the data is needed then only fetch else not there is one more option fetchType.EGARE it means always data fetched.
    @JoinColumn(name="post_id", nullable = false)
    private Post post;
}
