package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.dto.Post;
import ru.netology.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> all() {
        return repository.all().values().stream()
                .filter(post -> !post.isRemoved())
                .collect(Collectors.toList());
    }

    public Post getById(long id) {
        var post = repository.getById(id);
        if (post == null || post.isRemoved()) {
            throw new NotFoundException("Post with id=" + id + " not found or removed");
        }
        return post;
    }

    public Post save(Post post) {
        if (post.getId() != 0) {
            var existing = repository.getById(post.getId());
            if (existing != null && existing.isRemoved()) {
                throw new NotFoundException("Cannot update removed post with id=" + post.getId());
            }
        }
        return repository.save(post);
    }

    public void removeById(long id) {
        var post = repository.getById(id);
        if (post == null) {
            throw new NotFoundException("Post with id=" + id + " not found");
        }
        post.setRemoved(true);
    }
}