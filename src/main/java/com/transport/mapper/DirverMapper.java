package com.transport.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.transport.dto.driver.DriverRequest;
import com.transport.dto.driver.DriverResponse;
import com.transport.entity.domain.Driver;

@Mapper(componentModel = "spring")
public interface DirverMapper {
    // Convert từ request sang entity khi tạo mới
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driverCode", ignore = true)
    @Mapping(target = "salaryReports", ignore = true)
    @Mapping(target = "trips", ignore = true)
    @Mapping(target = "user", ignore = true)
    Driver toDriverFromCreateRequest(DriverRequest request);

    // Convert từ entity sang response
    DriverResponse toDriverResponse(Driver driver);

    // Cập nhật entity từ request (dùng cho update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driverCode", ignore = true)
    @Mapping(target = "salaryReports", ignore = true)
    @Mapping(target = "trips", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateDriverFromRequest(DriverRequest request, @MappingTarget Driver driver);
}
