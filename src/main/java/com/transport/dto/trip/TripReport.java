package com.transport.dto.trip;

import java.util.List;

public record TripReport(List<TripByVehicleReponse> trips, long totalTrips) {}
