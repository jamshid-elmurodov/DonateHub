package donatehub.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PagedRes<T> {
    private int page;
    private int numberOfElements;
    private int totalPages;
    private long totalElements;
    private List<T> data;

    public PagedRes(Page<T> page) {
        this.page = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.data = page.getContent();
    }
}