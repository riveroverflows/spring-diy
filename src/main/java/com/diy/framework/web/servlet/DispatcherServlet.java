package com.diy.framework.web.servlet;

import com.diy.app.LectureController;
import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.mvc.view.UrlBasedViewResolver;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> controllers = new ConcurrentHashMap<>();
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    public DispatcherServlet() {
        System.out.println("[DispatcherServlet] no args constructor is called");
    }

    @Override
    public void init() {
        System.out.println("[DispatcherServlet] init() is called");
        controllers.put("/lectures", new LectureController());
        viewResolvers.add(new JspViewResolver());
        viewResolvers.add(new UrlBasedViewResolver());
    }

    public <T extends Controller> void addController(String path, T controller) {
        controllers.put(path, controller);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) {
        System.out.println("[DispatcherServlet] service() is called.");

        final var uri = req.getRequestURI();
        final var controller = controllers.getOrDefault(uri, null);

        if (controller == null) {
            System.out.println("[DispatcherServlet] no such controller: " + uri);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            System.out.println("[DispatcherServlet] request URI: " + uri + ", controller: " + controller.getClass().getName());
            final ModelAndView mav = controller.handleRequest(req, resp);
            render(req, resp, mav);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView mav) throws Exception {
        final String viewName = mav.getViewName();
        final View view = resolveViewName(viewName);
        if (view == null) {
            throw new RuntimeException("[DispatcherServlet] view not found: " + viewName);
        }
        view.render(req, resp, mav);
    }

    private View resolveViewName(final String viewName) {
        for (final ViewResolver viewResolver : viewResolvers) {
            final View view = viewResolver.resolve(viewName);
            if (view != null) {
                return view;
            }
        }
        return null;
    }
}
