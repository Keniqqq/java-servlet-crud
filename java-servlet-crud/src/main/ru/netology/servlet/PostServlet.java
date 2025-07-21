package ru.netology.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.netology.model.Post;
import ru.netology.service.PostService;
import ru.netology.util.HttpUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PostServlet extends HttpServlet {
    private final PostService service = new PostService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var pathInfo = req.getPathInfo(); // /api/posts или /api/posts/1

        if (pathInfo == null || "/".equals(pathInfo)) {
            handleGetAll(resp);
        } else {
            final var idStr = pathInfo.substring(1); // убираем '/'
            try {
                final var id = Long.parseLong(idStr);
                handleGetById(id, resp);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var post = mapper.readValue(req.getReader(), Post.class);
        final var saved = service.save(post);
        writeJsonResponse(resp, saved, HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final var pathInfo = req.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        final var idStr = pathInfo.substring(1);
        try {
            final var id = Long.parseLong(idStr);
            service.removeById(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleGetAll(HttpServletResponse resp) throws IOException {
        final List<Post> posts = service.all();
        writeJsonResponse(resp, posts, HttpServletResponse.SC_OK);
    }

    private void handleGetById(long id, HttpServletResponse resp) throws IOException {
        final Post post = service.getById(id);
        if (post != null) {
            writeJsonResponse(resp, post, HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void writeJsonResponse(HttpServletResponse resp, Object data, int status) throws IOException {
        resp.setContentType(HttpUtils.CONTENT_TYPE_JSON);
        resp.setStatus(status);
        final PrintWriter writer = resp.getWriter();
        mapper.writeValue(writer, data);
    }
}