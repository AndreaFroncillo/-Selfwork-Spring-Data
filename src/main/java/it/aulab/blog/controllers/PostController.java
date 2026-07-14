package it.aulab.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.aulab.blog.dtos.PostDto;
import it.aulab.blog.services.AuthorService;
import it.aulab.blog.services.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Post");
        model.addAttribute("posts", postService.readAll());

        return "posts/index";
    }

    @GetMapping("/{id}")
    public String show(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("title", "Dettaglio post");
        model.addAttribute("post", postService.read(id));

        return "posts/show";
    }

    @GetMapping("/create")
    public String createView(Model model) {
        model.addAttribute("title", "Nuovo post");
        model.addAttribute("post", new PostDto());
        model.addAttribute("authors", authorService.readAll());

        return "posts/create";
    }

    @PostMapping
    public String create(
            @ModelAttribute("post") PostDto postDto
    ) {
        postService.create(postDto);

        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String editView(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("title", "Modifica post");
        model.addAttribute("post", postService.read(id));
        model.addAttribute("authors", authorService.readAll());

        return "posts/edit";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute("post") PostDto postDto
    ) {
        postService.update(id, postDto);

        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.delete(id);

        return "redirect:/posts";
    }
}