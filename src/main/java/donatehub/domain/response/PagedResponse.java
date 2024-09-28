package donatehub.domain.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PagedResponse<T> {
    private int page;
    private int numberOfElements;
    private int totalPages;
    private long totalElements;
    private List<T> data;

    public PagedResponse(Page<T> page) {
        this.page = page.getNumber();
        this.numberOfElements = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.data = page.getContent();
    }
}