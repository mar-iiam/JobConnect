package com.example.jobconnect.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Schema(description = "Generic paginated response wrapper")
public class PageResponseDto<T> {

    @Schema(description = "Items on the current page")
    private List<T> content;

    @Schema(description = "Current page number, starting at 0", example = "0")
    private int page;

    @Schema(description = "Requested page size", example = "10")
    private int size;

    @Schema(description = "Total number of items across all pages", example = "42")
    private long totalElements;

    @Schema(description = "Total number of pages", example = "5")
    private int totalPages;

    @Schema(description = "True if this is the first page", example = "true")
    private boolean first;

    @Schema(description = "True if this is the last page", example = "false")
    private boolean last;

    public PageResponseDto(
            List<T> content,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean first,
            boolean last) {

        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public static <T> PageResponseDto<T> from(Page<T> page) {
        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}