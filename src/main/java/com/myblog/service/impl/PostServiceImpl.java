package com.myblog.service.impl;

import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.repository.PostRepository;
import com.myblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class PostServiceImpl implements PostService {
    private PostRepository pr;
    private ModelMapper mm;
    public PostServiceImpl(PostRepository pr, ModelMapper mm) {
        this.pr = pr;
        this.mm = mm;
    }
    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);   //dto to entity conversion
        Post newPost = pr.save(post);  //save into database (it is return in entity)
        PostDto newPostDto = mapToDto(newPost);  //converted entity to dto
        return newPostDto;  //we are returning back in dto from
    }
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending(); //here we use ternary oprators it will work as a if else
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);//there is inbuild method sort with the filter by which is help me to stored the string data
        Page<Post> p =pr.findAll(pageable);
        List<Post> c = p.getContent();
        List<PostDto> dto = c.stream().map(pt -> mapToDto(pt)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(dto);
        postResponse.setPageNo(p.getNumber());
        postResponse.setPageSize(p.getSize());
        postResponse.setTotalElements(p.getTotalElements());
        postResponse.setTotalPage(p.getTotalPages());
        postResponse.setLastPage(p.isLast());
        return postResponse;
    }
    @Override
    public PostDto getPostById(long id) {
        Post p = pr.findById(id).orElseThrow(()->new ResourceNotFoundException("Post", "id",id));
        return mapToDto(p);
    }
    @Override
    public PostDto updatePost(PostDto pd, long id) {
        Post p =pr.findById(id).orElseThrow(()->new ResourceNotFoundException("Post", "id", id)); // get post by id from the database

        //dto to entity
        p.setTitle(pd.getTitle());
        p.setDescription(pd.getDescription());
        p.setContent(pd.getContent());

        Post updatePost = pr.save(p); //data in form of entity save in db
        return mapToDto(updatePost); //return entity data in from of dto
        }

    @Override
    public void deleteById(long id) {
        Post p = pr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id)); // get post by id from the database

        pr.delete(p);
    }

    Post mapToEntity(PostDto postDto){
        Post p = mm.map(postDto, Post.class);
        //Post p = new Post();
        //p.setTitle(postDto.getTitle());
        //p.setDescription(postDto.getDescription());
        //p.setContent(postDto.getContent());
        return p;
    }
    PostDto mapToDto(Post post){
        PostDto pd = mm.map(post, PostDto.class);
        //PostDto pd = new PostDto();
        //pd.setId(post.getId());
        //pd.setTitle(post.getTitle());
        //pd.setDescription(post.getDescription());
        //pd.setContent(post.getContent());
        return pd;
    }
}
