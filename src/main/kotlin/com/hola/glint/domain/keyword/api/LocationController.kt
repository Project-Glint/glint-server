package com.hola.glint.domain.keyword.api

import com.hola.glint.common.dto.ResponseData
import com.hola.glint.domain.keyword.api.dto.LocationResponseDto
import com.hola.glint.domain.keyword.application.LocationService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/location")
class LocationController(
    private val locationService: LocationService,
) {
    /*
    @GetMapping
    @Operation(summary = "List all locations", description = "모든 위치 조회")
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocation();
        return ResponseEntity.ok(locations);
    }*/

    @GetMapping("")
    @Operation(summary = "Get a location by state and city", description = "[시,도]와 [시,군,구]를 통한 위치 조회")
    fun getLocationByStateAndCity(
        @RequestParam state: String,
        @RequestParam city: String,
    ): ResponseEntity<ResponseData<LocationResponseDto>> {
        return ResponseData.success(locationService.findByName(state, city))
    }

    @GetMapping("/{locationId}/location")
    @Operation(summary = "Get a location by its ID", description = "Location Id를 통한 위치 조회")
    fun getLocationById(@PathVariable locationId: Long): ResponseEntity<ResponseData<LocationResponseDto>> {
        return ResponseData.success(locationService.findById(locationId))
    }

    @Operation(summary = "List all states", description = "모든 [시,도] 조회")
    @GetMapping("/states")
    fun allStates(): ResponseEntity<List<String>> {
        val states: List<String> = locationService.allLocationSate()
        return ResponseEntity.ok(states)
    }

    @GetMapping("/cities")
    @Operation(summary = "List all cities by state", description = "[시,도]를 통한 모든 [시,군,구] 조회")
    fun getCitiesByState(@RequestParam state: String): ResponseEntity<ResponseData<List<LocationResponseDto>>> {
        return ResponseData.success(locationService.getAllCityByState(state))
    }
    // 생성, 수정, 조회 주석 처리
}