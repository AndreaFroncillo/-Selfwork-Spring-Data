package it.aulab.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.aulab.blog.dtos.CommentDto;
import it.aulab.blog.services.CommentService;
import it.aulab.blog.services.PostService;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Commenti");
        model.addAttribute("comments", commentService.readAll());

        return "comments/index";
    }

    @GetMapping("/{id}")
    public String show(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("title", "Dettaglio commento");
        model.addAttribute("comment", commentService.read(id));

        return "comments/show";
    }

    @GetMapping("/create")
    public String createView(Model model) {
        model.addAttribute("title", "Nuovo commento");
        model.addAttribute("comment", new CommentDto());
        model.addAttribute("posts", postService.readAll());

        return "comments/create";
    }

    @PostMapping
    public String create(
            @ModelAttribute("comment") CommentDto commentDto
    ) {
        commentService.create(commentDto);

        return "redirect:/comments";
    }

    @GetMapping("/{id}/edit")
    public String editView(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("title", "Modifica commento");
        model.addAttribute("comment", commentService.read(id));
        model.addAttribute("posts", postService.readAll());

        return "comments/edit";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute("comment") CommentDto commentDto
    ) {
        commentService.update(id, commentDto);

        return "redirect:/comments/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        commentService.delete(id);

        return "redirect:/comments";
    }
}