package com.youllbecold.trustme.recommend.ranged.usecase

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.network.NetworkStatusProvider
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.weather.RangedWeatherProvider
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.recommend.ui.mappers.toWeatherCondPersistList
import com.youllbecold.trustme.recommend.ui.model.WeatherWithRecommendation
import com.youllbecold.trustme.recommend.usecases.RecommendationUseCase

/**
 * Use case for recommending clothes for a date.
 */
class RecommendForDateUseCase(
    private val app: Application,
    private val locationController: LocationController,
    private val networkStatusProvider: NetworkStatusProvider,
    private val recommendationUseCase: RecommendationUseCase,
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
        when {
            !networkStatusProvider.hasInternet() ->
                return RecommendationStatus(status = LoadingStatus.NoInternet)
            !PermissionChecker.hasLocationPermission(app) ->
                return RecommendationStatus(status = LoadingStatus.MissingPermission)
        }

        val location = locationController.simpleLocation // Readily available
            ?: LocationController.getLastLocation(app) // Ask for it otherwise

        if (location == null) {
            return RecommendationStatus(status = LoadingStatus.GenericError)
        }

        val weather = weatherUseCase.obtainRangedWeather(
            location = location,
            date = datetimeRange.date.localDate,
            timeFrom = datetimeRange.timeFrom.localTime,
            timeTo = datetimeRange.timeTo.localTime,
            useCelsiusUnits = useCelsius
        ).getOrNull()

        if (weather == null) {
            return RecommendationStatus(status = LoadingStatus.GenericError)
        }

        val recommendation = recommendationUseCase.recommend(weather)

        if (recommendation == null) {
            return RecommendationStatus(status = LoadingStatus.GenericError)
        }

        return RecommendationStatus(
            weatherWithRecommendation = WeatherWithRecommendation(
                weather = weather.toWeatherCondPersistList(),
                recommendationState = recommendation
            ),
            status = LoadingStatus.Idle,
            dateTimeState = datetimeRange
        )
    }
 }

@Stable
data class RecommendationStatus(
    val status: LoadingStatus = LoadingStatus.Idle,
    val weatherWithRecommendation: WeatherWithRecommendation? = null,
    val dateTimeState: DateTimeState? = null
)
