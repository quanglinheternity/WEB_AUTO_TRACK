package com.transport.service.trip;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.transport.dto.page.PageResponse;
import com.transport.dto.trip.ApproveTripRequest;
import com.transport.dto.trip.TripCreateRequest;
import com.transport.dto.trip.TripResponse;
import com.transport.dto.trip.TripSearchRequest;
import com.transport.dto.trip.TripUpdateRequest;
import com.transport.dto.trip.UpdateTripStatusRequest;

public interface TripService {
    PageResponse<TripResponse> getAll(TripSearchRequest request, Pageable pageable);

    TripResponse createTrip(TripCreateRequest request);

    TripResponse updateTrip(Long id, TripUpdateRequest request);

    TripResponse updateTripStatus(Long id, UpdateTripStatusRequest request);

    TripResponse approveTrip(Long id, ApproveTripRequest request);

    void delete(Long id);

    List<TripResponse> listAll();
}
