package com.willshex.blogwt.server.dev.actions;

import java.util.List;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.willshex.blogwt.server.dev.DevAction;
import com.willshex.blogwt.server.service.post.PostServiceProvider;
import com.willshex.blogwt.shared.api.Pager;
import com.willshex.blogwt.shared.api.SortDirectionType;
import com.willshex.blogwt.shared.api.datatype.PostSortType;
import com.willshex.blogwt.shared.helper.PagerHelper;
import com.willshex.blogwt.server.dev.DevServlet.AllPaged;

public class DeletePostsAction implements DevAction {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        processPaged(
                (Integer s, Integer c, PostSortType o,
                        SortDirectionType d) -> PostServiceProvider.provide()
                                .getPosts(Boolean.TRUE, Boolean.FALSE, s, c, o,
                                        d),
                PostServiceProvider.provide()::deletePost);
    }

    public static <T, E extends Enum<E>> Pager processPaged(Pager p,
            AllPaged<T, E> supplier, Consumer<T> processor) {
        List<T> list = supplier.get(p.start, p.count, null, null);

        for (T item : list) {
            processor.accept(item);
        }

        return list.size() == p.count.intValue() ? PagerHelper.moveForward(p)
                : null;
    }

    private static <T, E extends Enum<E>> void processPaged(
            AllPaged<T, E> supplier, Consumer<T> processor) {
        Pager p = PagerHelper.createDefaultPager().count(Integer.valueOf(1200));
        do {
            p = processPaged(p, supplier, processor);
        } while (p != null);
    }

}
