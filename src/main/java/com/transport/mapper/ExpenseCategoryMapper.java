package com.transport.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.transport.dto.expenseCategory.ExpenseCategoryRequest;
import com.transport.dto.expenseCategory.ExpenseCategoryResponse;
import com.transport.entity.domain.ExpenseCategory;
import com.transport.enums.ExpenseGroup;

@Mapper(componentModel = "spring")
public interface ExpenseCategoryMapper {

    @Mapping(target = "categoryGroup", source = "categoryGroup", qualifiedByName = "mapGroupToDescription")
    ExpenseCategoryResponse toResponse(ExpenseCategory entity);

    @Named("mapGroupToDescription")
    default String mapGroupToDescription(ExpenseGroup group) {
        return group != null ? group.getDescription() : null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "expenses", ignore = true)
    ExpenseCategory toCreateExpenseCategory(ExpenseCategoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "expenses", ignore = true)
    void updateExpenseCategoryFromRequest(ExpenseCategoryRequest request, @MappingTarget ExpenseCategory entity);
}
