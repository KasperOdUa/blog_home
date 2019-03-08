package com.example.blog_home.controller;


import com.example.blog_home.model.Post;
import com.example.blog_home.repository.CategoryRepository;
import com.example.blog_home.repository.PostRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.print.Book;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(value = "/post")
public class PostController {

    @Value("${image.upload.dir}")
    private String imageUploadDir;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/add")
    public String addPostView(ModelMap map) {
        map.addAttribute("categories", categoryRepository.findAll());
        return "addPost";
    }

    @GetMapping("/openPost")
    public String fromPost(ModelMap map, @RequestParam("id") int id) {
        Post post = postRepository.getOne(id);
        map.addAttribute("post", post);
        return "postPage";

    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute Post post, @RequestParam(name = "picture") MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File picture = new File(imageUploadDir + File.separator + fileName);
        file.transferTo(picture);
        post.setPicUrl(fileName);
        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/postById")
    public String postById(ModelMap map,@RequestParam("id") int id) {
        map.addAttribute("post", postRepository.getOne(id));
        return "post";
    }

    @GetMapping("/getImage")
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(imageUploadDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping("/postByCategory")
    public String postByCategory(ModelMap map ,@RequestParam("id") int id ) {
        List<Post> posts = postRepository.findAll();
        List<Post> postByCategoryId = new LinkedList<>();
        for (Post p : posts) {
            if (p.getCategory().getId() == id)  {
                postByCategoryId.add(p);
            }
        }
        map.addAttribute("posts", postByCategoryId);
        return "postByCategory";
    }
    @GetMapping("/delete")
    public String deleteById(@RequestParam("id") int id) {
        Optional<Post> one = postRepository.findById(id);
        if (one.isPresent()) {
            postRepository.deleteById(id);
        }
        return "redirect:/";
    }
}
