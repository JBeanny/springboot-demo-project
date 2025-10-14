package com.beanny.demo.common.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CacheKeyGenerator {
    private static final String DELIMITER = ":";
    private static final String PAGE_PREFIX = "page";
    private static final String SIZE_PREFIX = "size";
    
    public static String generatePaginatedKey(String prefix,Pageable pageable) {
        StringBuilder keyBuilder = new StringBuilder();
        // key -> products:page:0:size:10:sort:price-desc-
        keyBuilder
                .append(prefix)
                .append(DELIMITER)
                .append(PAGE_PREFIX)
                .append(DELIMITER)
                .append(pageable.getPageNumber())
                .append(DELIMITER)
                .append(SIZE_PREFIX)
                .append(DELIMITER)
                .append(pageable.getPageSize());
        
        // only if sort is available
        if(pageable.getSort().isSorted()) {
            keyBuilder
                    .append(DELIMITER)
                    .append("sort")
                    .append(DELIMITER);
            
            for (Sort.Order order : pageable.getSort()) {
                keyBuilder
                        .append(order.getProperty())
                        .append("-")
                        .append(order.getDirection().name().toLowerCase())
                        .append("-");
            }
            keyBuilder.setLength(keyBuilder.length() - 1);
        }
        
        return keyBuilder.toString();
    }
}
