package com.diy.framework.web.mvc.view;

public class HtmlViewResolver implements ViewResolver {
    @Override
    public View resolve(final String viewName) {
        System.out.println("[HtmlViewResolver] viewName: " + viewName);
        if (viewName == null || viewName.isBlank() || viewName.startsWith("redirect:")) {
            return null;
        }
        return new HtmlView("/" + viewName + ".html");
    }
}
