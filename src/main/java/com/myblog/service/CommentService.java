package com.myblog.service;

import com.myblog.payload.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO saveComments(long id, CommentDTO commentDTO);
    List<CommentDTO> getCommentByPostId(long pid);

    CommentDTO getCommentById(long postId, long commentId);

    CommentDTO updateCommentById(long postId, long commentId, CommentDTO commentDTO);

    void deleteCommentById(long postId, long commentId);
}
