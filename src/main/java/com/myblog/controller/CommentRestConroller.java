package com.myblog.controller;

import com.myblog.payload.CommentDTO;
import com.myblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestConroller {
    private CommentService cs;
    public CommentRestConroller(CommentService cs){
        this.cs=cs;
    }

    //http://localhost:8080/api/post/pid/comments
    @PostMapping("/post/{pid}/comments")
    public ResponseEntity<Object> saveComment(@PathVariable("pid") long id,@Valid @RequestBody CommentDTO commentDTO, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CommentDTO dto = cs.saveComments(id, commentDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/post/postId/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(value = "postId") Long postId){
        return cs.getCommentByPostId(postId);
    }

    //http://localhost:8080/api/post/postId/comments/commentId
    @GetMapping("/post/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable("postId") long postId,
                                                     @PathVariable("commentId") long commentId){
        CommentDTO dto = cs.getCommentById(postId, commentId);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    //http://localhost:8080/api/post/2/comments/1
    @PutMapping("/post/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentById(@PathVariable("postId") long postId,
                                                        @PathVariable("commentId") long commentId,
                                                        @RequestBody CommentDTO commentDTO){
        CommentDTO dto = cs.updateCommentById(postId, commentId, commentDTO);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    //http://localhost:8080/api/post/3/comment/1
    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("postId") long postId,
                                                    @PathVariable("commentId") long commentId){
        cs.deleteCommentById(postId,commentId);
        return new ResponseEntity<>("Comment Deleted Successfully!!",HttpStatus.OK);
    }
}
