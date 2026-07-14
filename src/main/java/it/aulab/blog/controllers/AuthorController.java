package it.aulab.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import it.aulab.blog.dtos.AuthorDto;
import it.aulab.blog.services.AuthorService;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Autori");
        model.addAttribute("authors", authorService.readAll());

        return "authors/index";
    }

    @GetMapping("/{id}")
    public String show(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("title", "Dettaglio autore");
        model.addAttribute("author", authorService.read(id));

        return "authors/show";
    }

    @GetMapping("/create")
    public String createView(Model model) {
        model.addAttribute("title", "Nuovo autore");
        model.addAttribute("author", new AuthorDto());

        return "authors/create";
    }

    @PostMapping
    public String create(
            @ModelAttribute("author") AuthorDto authorDto
    ) {
        authorService.create(authorDto);

        return "redirect:/authors";
    }

    @GetMapping("/{id}/edit")
    public String editView(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("title", "Modifica autore");
        model.addAttribute("author", authorService.read(id));

        return "authors/edit";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute("author") AuthorDto authorDto
    ) {
        authorService.update(id, authorDto);

        return "redirect:/authors/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        authorService.delete(id);

        return "redirect:/authors";
    }
}