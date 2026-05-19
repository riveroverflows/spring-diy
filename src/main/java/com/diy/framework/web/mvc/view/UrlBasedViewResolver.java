package com.diy.framework.web.mvc.view;

public class UrlBasedViewResolver implements ViewResolver {
    @Override
    public View resolve(final String viewName) {
        System.out.println("[UrlBasedViewResolver] viewName: " + viewName);
        if (viewName == null || viewName.isBlank() || !viewName.startsWith("redirect:")) {
            return null;
        }
        final String redirectUrl = viewName.substring("redirect:".length());
        return new RedirectView(redirectUrl);
    }
}
