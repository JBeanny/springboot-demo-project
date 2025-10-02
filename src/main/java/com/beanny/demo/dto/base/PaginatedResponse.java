package com.beanny.demo.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonPropertyOrder(value = {"content","pagination"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> content;
    private Object pagination;
    
    public static <T> PaginatedResponse from(Page<T> page) {
        return new PaginatedResponse(page.getContent(), page.getPageable());
    }
}
