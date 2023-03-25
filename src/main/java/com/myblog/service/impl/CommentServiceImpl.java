package com.myblog.service.impl;

import com.myblog.entity.Comment;
import com.myblog.entity.Post;
import com.myblog.exception.BlogApiException;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.CommentDTO;
import com.myblog.repository.CommentRepo;
import com.myblog.repository.PostRepository;
import com.myblog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepo cr;
    private PostRepository pr;
    private ModelMapper mm;
    public CommentServiceImpl(CommentRepo cr, PostRepository pr, ModelMapper mm){
        this.cr=cr;
        this.pr=pr;
        this.mm=mm;
    }
    @Override
    public CommentDTO saveComments(long id, CommentDTO commentDTO) {
        Comment c = mapToEntity(commentDTO);
        Post post = pr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id));
        c.setPost(post);
        Comment comment = cr.save(c);
        return mapToDTO(comment);

    }

    @Override
    public List<CommentDTO> getCommentByPostId(long pid) {
        List<Comment> byPostId = cr.findByPostId(pid);


        // convert list of comment entities to list of comment dto's
        return byPostId.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(long postId, long commentId) {
        Post post = pr.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        Comment comment = cr.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));

        if (comment.getPost().getId() != (post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
            return mapToDTO(comment);

    }

    @Override
    public CommentDTO updateCommentById(long postId, long commentId, CommentDTO commentDTO) {
        Post post = pr.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        Comment comment = cr.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));
        if(comment.getPost().getId() != post.getId()){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to Post");
        }
        comment.setId(commentId);
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        comment.setBody(commentDTO.getBody());
        Comment updateComent = cr.save(comment);
        return mapToDTO(updateComent);
    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = pr.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        Comment comment = cr.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));
        if(comment.getPost().getId()!=post.getId()){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to Post");

        }
        cr.deleteById(commentId);
    }

    private CommentDTO mapToDTO(Comment comment){
        CommentDTO commentDto = mm.map(comment, CommentDTO.class);

        //CommentDTO commentDto = new CommentDTO();
        //commentDto.setId(comment.getId());
        //commentDto.setName(comment.getName());
        //commentDto.setEmail(comment.getEmail());
        //commentDto.setBody(comment.getBody());
        return  commentDto;
    }

    private Comment mapToEntity(CommentDTO commentDto){
        Comment comment = mm.map(commentDto, Comment.class);
        //Comment comment = new Comment();
        //comment.setId(commentDto.getId());
        //comment.setName(commentDto.getName());
        //comment.setEmail(commentDto.getEmail());
        //comment.setBody(commentDto.getBody());
        return  comment;
    }
}