package ru.netology.service;

import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.List;
import java.util.Map;

public class PostService {
    private final PostRepository repository = new PostRepository();

    @Service
    public class PostService {
        private final PostRepository repository;

        public PostService(PostRepository repository) {
            this.repository = repository;
        }
        // ...
    }

    public List<Post> all() {
        return List.copyOf(repository.all().values());
    }

    public Post getById(long id) {
        return repository.getById(id);
    }

    public Post save(Post post) {
        return repository.save(post);
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}