package edu.stanford.bmir.protege.web.server.pagination;

import edu.stanford.bmir.protege.web.shared.pagination.Page;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2017
 */
public class PageCollector<T> implements Collector<T, List<T>, Optional<Page<T>>> {

    public static final int DEFAULT_PAGE_SIZE = 30;

    private int counter = 0;

    private final int pageSize;

    private final int pageNumber;

    private PageCollector(int pageSize, int pageNumber) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return (ts, t) -> {
            int currentPage = (counter / pageSize) + 1;
            if(currentPage == pageNumber) {
                ts.add(t);
            }
            counter++;
        };
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (ts, ts2) -> {
            ts.addAll(ts2);
            return ts;
        };
    }

    @Override
    public Function<List<T>, Optional<Page<T>>> finisher() {
        return ts -> {
            int elementCount = counter;
            int fullPages = elementCount / pageSize;
            int remainder = elementCount % pageSize;
            int pageCount = fullPages + (remainder == 0 ? 0 : 1);
            if(pageNumber > pageCount) {
                return Optional.empty();
            }
            else {
                Page<T> page = new Page<>(pageNumber, pageCount, ts, elementCount);
                return Optional.of(page);
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    public static <T> PageCollector<T> toPage(int pageNumber, int pageSize) {
        return toPageNumber(pageNumber).forPageSize(pageSize);
    }

    public static <T> PageCollectorBuilder<T> toPageNumber(int pageNumber) {
        if(pageNumber < 1) {
            throw new IllegalArgumentException("Page number must be greater than zero");
        }
        return new PageCollectorBuilder<>(pageNumber);
    }


    public static final class PageCollectorBuilder<T> {

        private final int pageNumber;

        public PageCollectorBuilder(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public <S> PageCollector<S> forPageSize(int pageSize) {
            if(pageSize < 1) {
                throw new IllegalArgumentException("Page size must be greater than zero");
            }
            return new PageCollector<S>(pageSize, pageNumber);
        }

        public <S> PageCollector<S> withDefaultPageSize() {
            return new PageCollector<S>(DEFAULT_PAGE_SIZE, pageNumber);
        }

    }

}
