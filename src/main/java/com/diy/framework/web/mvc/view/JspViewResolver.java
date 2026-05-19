package com.diy.framework.web.mvc.view;

public class JspViewResolver implements ViewResolver {
    @Override
    public View resolve(final String viewName) {
        System.out.println("[JspViewResolver] viewName: " + viewName);
        if (viewName == null || viewName.isBlank() || viewName.startsWith("redirect:")) {
            return null;
        }
        return new JspView("/" + viewName + ".jsp");
    }
}
