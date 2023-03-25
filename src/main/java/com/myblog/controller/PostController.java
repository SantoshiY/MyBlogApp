package com.myblog.controller;

import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController   //restcontroller coz we r devloping api (backend)
@RequestMapping("/api/posts")
public class PostController {
    //here we parform dependancy injection with help of constructor (line no. 15 & 16)
    private PostService ps;
    public PostController(PostService ps) {
        this.ps = ps;
    }

    //http://localhost:8080/api/posts
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto dto = ps.createPost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/posts
    //http://localhost:8080/api/posts?pageNo=0&pageSize=5    (for pagination testing)
    //http://localhost:8080/api/posts?pageNo=0&pageSize=5&sortBy=title (for pagination and sorting)
    //http://localhost:8080/api/posts?pageNo=0&pageSize=5&sortBy=title&sortDir=asc   (for pagination and sorting as well as asc or desc)

    @GetMapping
    public PostResponse getAllPosts(@RequestParam(value="pageNo", defaultValue = "0", required = false) int pageNo,
                                    @RequestParam(value="pageSize", defaultValue = "10", required = false) int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                    @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        return ps.getAllPosts(pageNo, pageSize, sortBy, sortDir);   //we can create same method name in 2 differnt classes
    }

    //http://localhost:8080/api/posts/1
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id){
        PostDto dto = ps.getPostById(id);
        return ResponseEntity.ok(dto);  //return new ResponseEntity<>(dto, HttpStatus.ok);
    }

    //http://localhost:8080/api/posts/1
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto pd, @PathVariable("id") long id){
        PostDto dto = ps.updatePost(pd, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id){
        ps.deleteById(id);
        return new ResponseEntity<>("Post is deleted on id: "+id, HttpStatus.OK);
    }
}
