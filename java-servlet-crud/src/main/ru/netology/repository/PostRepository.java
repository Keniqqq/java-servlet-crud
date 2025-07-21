package ru.netology.repository;

import ru.netology.model.Post;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong lastId = new AtomicLong(0);

    @Repository
    public class PostRepository {
        // ...
    }

    @PostConstruct
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);// можно добавить тестовые данные
    }

    public Map<Long, Post> all() {
        return Map.copyOf(posts); // immutable copy
    }

    public Post getById(long id) {
        return posts.get(id);
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            // Создание нового поста
            long newId = lastId.incrementAndGet();
            post.setId(newId);
            posts.put(newId, post);
            return post;
        } else {
            // Обновление существующего
            if (posts.containsKey(post.getId())) {
                posts.put(post.getId(), post);
                return post;
            } else {
                throw new IllegalArgumentException("Post with id " + post.getId() + " not found");
            }
        }
    }

    public void removeById(long id) {
        if (!posts.containsKey(id)) {
            throw new IllegalArgumentException("Post with id " + id + " not found");
        }
        posts.remove(id);
    }
}