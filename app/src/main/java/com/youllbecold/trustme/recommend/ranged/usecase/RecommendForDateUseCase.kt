package com.youllbecold.trustme.recommend.ranged.usecase

import android.annotation.SuppressLint
import android.app.Application
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.weather.RangedWeatherProvider
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.model.status.Error
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.Success
import com.youllbecold.trustme.common.ui.model.status.isSuccess
import com.youllbecold.trustme.recommend.ui.mappers.toWeatherCondPersistList
import com.youllbecold.trustme.recommend.ui.model.WeatherWithRecommendation
import com.youllbecold.trustme.recommend.domain.RecommendationProvider

/**
 * Use case for recommending clothes for a date.
 */
class RecommendForDateUseCase(
    private val app: Application,
    private val locationController: LocationController,
    private val recommendationProvider: RecommendationProvider,
    private val weatherUseCase: RangedWeatherProvider,
) {
    /**
     * Recommendation for specified date time range.
     */
    @SuppressLint("MissingPermission") // It is checked
    suspend fun recommendForDate(
        datetimeRange: DateTimeState,
        useCelsius: Boolean
    ): RecommendationStatus {
        if (!PermissionChecker.hasLocationPermission(app)) {
            return RecommendationStatus(status = Error.MissingPermission)
        }

        val location = locationController.quickGetLastLocation()
            ?: return RecommendationStatus(status = Error.LocationMissing)

        val weather = weatherUseCase.obtainRangedWeather(
            location = location,
            date = datetimeRange.date.localDate,
            timeFrom = datetimeRange.timeFrom.localTime,
            timeTo = datetimeRange.timeTo.localTime,
            useCelsiusUnits = useCelsius
        )

        if (!weather.status.isSuccess()) {
            return RecommendationStatus(status = weather.status)
        }

        val recommendation = recommendationProvider.recommend(weather.weather)

        if (!recommendation.status.isSuccess()) {
            return RecommendationStatus(status = Error.ApiError)
        }

        return RecommendationStatus(
            weatherWithRecommendation = WeatherWithRecommendation(
                weather = weather.weather.toWeatherCondPersistList(),
                recommendationState = recommendation.recommendation
            ),
            status = Success,
            dateTimeState = datetimeRange
        )
    }
 }

data class RecommendationStatus(
    val status: Status = Idle,
    val weatherWithRecommendation: WeatherWithRecommendation? = null,
    val dateTimeState: DateTimeState? = null
)
