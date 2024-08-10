package prod.bookapp.configuration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

public class PaginationUtils {
    public static <T> Page<T> paginate(List<T> list, Pageable pageable) {
        if (list == null || list.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        if (start > end || start < 0 || end > list.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, list.size());
        }

        List<T> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }
}
