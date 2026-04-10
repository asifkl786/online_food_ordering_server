package com.ofos.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class MenuItemResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer preparationTime;
    private String imageUrl;
    private Boolean isAvailable;
    private Boolean isVegetarian;
    private Integer calories;
    private Long categoryId;
    private String categoryName;
    private List<String> availableAddons;
}