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
    private PaginationMetadata pagination;
    
    public static <T> PaginatedResponse from(Page<T> page) {
        PaginationMetadata metadata = new PaginationMetadata(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                null
        );
        
        return new PaginatedResponse(page.getContent(), metadata);
    }
    
    public static <T> PaginatedResponse from(Page<T> page, String baseUrl) {
        Link links = generateLinks(baseUrl,page.getNumber(),page.getSize(),page.getTotalPages());
        
        PaginationMetadata metadata = new PaginationMetadata(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious(),
                links
        );
        
        return new PaginatedResponse(page.getContent(), metadata);
    }
    
    private static Link generateLinks(String baseUrl, int page,int size,int totalPages) {
        Link links = new Link();
        
        // self link
        links.setSelf(buildUrl(baseUrl,page,size));
        
        // first link
        links.setFirst(buildUrl(baseUrl,0,size));
        
        // last link
        links.setLast(buildUrl(baseUrl,totalPages-1,size));
        
        // previous link
        if(page > 0) {
            links.setPrevious(buildUrl(baseUrl,page - 1,size));
        }
        
        // next link
        if(page < totalPages - 1) {
            links.setNext(buildUrl(baseUrl,page + 1,size));
        }
        
        return links;
    }
    
    private static String buildUrl(String baseUrl, int page, int size) {
         StringBuilder url = new StringBuilder(baseUrl);
         url.append("?page=").append(page);
         url.append("&size=").append(size);
         
         return url.toString();
    }
}
