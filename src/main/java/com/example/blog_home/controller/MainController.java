package com.example.blog_home.controller;

import com.example.blog_home.model.Category;
import com.example.blog_home.model.Post;
import com.example.blog_home.repository.CategoryRepository;
import com.example.blog_home.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String main(ModelMap map){
        List<Post> all = postRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        map.addAttribute("posts", all);
        map.addAttribute("categories", categories);
        return "index";
    }
}
