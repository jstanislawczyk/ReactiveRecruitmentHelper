package com.reactiverecruitmenthelper.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Page<T> {

    public static final String DEFAULT_PAGE_SIZE = "10";

    List<T> pageContent;
    int pageNumber;
    int pageSize;
    long totalContentSize;
    
    @JsonProperty
    public boolean lastPage() {
        return (pageNumber + 1) * pageSize >= totalContentSize;
    }

    @JsonProperty
    public long totalPagesNumber() {
        return pageSize > 0 ? (totalContentSize - 1) / pageSize + 1 : 0;
    }

    @JsonProperty
    public boolean firstPage() {
        return pageNumber == 0;
    }
}
