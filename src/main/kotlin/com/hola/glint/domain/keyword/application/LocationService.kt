package com.hola.glint.domain.keyword.application

import com.hola.glint.common.exception.ErrorCode
import com.hola.glint.domain.keyword.api.dto.LocationResponseDto
import com.hola.glint.domain.keyword.entity.Location
import com.hola.glint.domain.keyword.repository.LocationRepository
import com.hola.glint.system.error.BadRequestException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class LocationService(
    private val locationRepository: LocationRepository,
) {
    fun findById(locationId: Long): LocationResponseDto {
        val location = locationRepository.findById(locationId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND, "Location not found with id: $locationId") }
        return LocationResponseDto.from(location)
    }

    fun findByName(state: String, city: String): LocationResponseDto {
        val location = locationRepository.findByStateAndCity(state, city)
        return LocationResponseDto.from(location)
    }

    fun getEntityByName(state: String, city: String): Location {
        return locationRepository.findByStateAndCity(state, city)
    }

    fun allLocation() = locationRepository.findAll()

    fun allLocationSate(): List<String> {
        val states = locationRepository.findAll().map { it.state }
        if (states.isEmpty()) {
            throw BadRequestException(ErrorCode.ENTITY_NOT_FOUND,"State not found")
        }
        return states
    }

    fun getAllCityByState(state: String): List<LocationResponseDto> { // 특정 state에 해당하는 모든 city 조회
        val locationList: List<Location> = locationRepository.findAllCityByState(state)
        if (locationList.isEmpty()) {
            throw BadRequestException(ErrorCode.ENTITY_NOT_FOUND,"Cities not found with state: $state")
        }
        return locationList.map { LocationResponseDto.from(it) }
    }

    fun getStateByCity(city: String): String { // 특정 city에 해당하는 state 조회
        return locationRepository.findStateByCity(city)?.state
            ?: throw BadRequestException(ErrorCode.ENTITY_NOT_FOUND,"State not found with city: $city")

    }

    fun getLocationNameById(locationId: Long): String { // location id를 통한 전체 위치명 문자열로 반환
        return locationRepository.findById(locationId)
            .map { location -> location.state + " " + location.city }
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND,"Location not found with locationId: $locationId") }
    }

    fun getLocationByStateAndCityName(state: String, city: String): Location { // 특정 state와 city에 해당하는 Location 반환
        return locationRepository.findByStateAndCity(state, city)
    }

    fun getLocationIdByStateAndCityName(state: String, city: String): Long { // 특정 state와 city에 해당하는 location id 반환
        return locationRepository.findByStateAndCity(state, city)?.id
            ?: throw BadRequestException(ErrorCode.ENTITY_NOT_FOUND,"Location id not found with state: $state and city: $city")
    }

    @Transactional
    fun createNewLocation(state: String, city: String): Location {
        return locationRepository.findByStateAndCity(state, city)
            ?: locationRepository.save(Location.createNewLocation(state, city))

    }

    @Transactional
    fun updateLocationById(locationId: Long, state: String, city: String): Location {
        val location: Location = locationRepository.findById(locationId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND,"Location not found with location id: $locationId") }
        location.updateLocation(state, city)
        return locationRepository.save(location)
    }

    @Transactional
    fun deleteLocation(locationId: Long) {
        val location: Location = locationRepository.findById(locationId)
            .orElseThrow { BadRequestException(ErrorCode.ENTITY_NOT_FOUND,"Location not found with location id: $locationId") }
        locationRepository.delete(location)
    }

    fun getLocationsByIds(locationIds: List<Long>): List<Location> {
        return locationRepository.findAllById(locationIds)
    }
}
