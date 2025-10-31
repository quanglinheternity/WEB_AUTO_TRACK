package com.transport.service.route;

import java.util.List;

import org.springframework.stereotype.Service;

import com.transport.dto.route.RouteRequest;
import com.transport.dto.route.RouteResponse;
import com.transport.dto.route.RouteUpdateRequest;
import com.transport.entity.domain.Route;
import com.transport.exception.AppException;
import com.transport.exception.ErrorCode;
import com.transport.mapper.RouteMapper;
import com.transport.repository.route.RouteRepository;
import com.transport.util.CodeGenerator;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@Transactional
@Slf4j
public class RouteServiceImpl implements RouteService {
    RouteRepository routeRepository;
    RouteMapper routeMapper;
    public List<RouteResponse> getAll() {
        return routeRepository.findAll().stream().map(routeMapper::toRouteResponse).toList();
    }
    public RouteResponse getById(Long id) {
        return routeRepository.findById(id).map(routeMapper::toRouteResponse)
        .orElseThrow(()-> new AppException(ErrorCode.ROUTE_NOT_FOUND));
    }
    public RouteResponse create(RouteRequest request) {
        Route route = routeRepository.findByName(request.getName());
        if (route != null) {
            throw new AppException(ErrorCode.ROUTE_NAME_ALREADY_EXISTS);
        }
        route = routeMapper.toCreateRoute(request);
        route.setCode(CodeGenerator.generateCode("TD"));
    
        return routeMapper.toRouteResponse(routeRepository.save(route));
    }
    public RouteResponse update(Long id, RouteUpdateRequest request) {
        Route route = routeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROUTE_NOT_FOUND));
        if (!route.getTrips().isEmpty()) {
            throw new AppException(ErrorCode.ROUTE_HAS_TRIPS);
        }
        if (request.getName() != null && routeRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.ROUTE_NAME_ALREADY_EXISTS);
        }
        routeMapper.updateRouteFromRequest(request, route);
        return routeMapper.toRouteResponse(routeRepository.save(route));
    }
    public void delete(Long id) {
        Route route = routeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROUTE_NOT_FOUND));
        if (!route.getTrips().isEmpty()) {
            throw new AppException(ErrorCode.ROUTE_HAS_TRIPS);
        }
        routeRepository.delete(route);
    }


}
