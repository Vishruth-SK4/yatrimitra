package com.example.yatrimitra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yatrimitra.model.VehicleState
import com.example.yatrimitra.network.OsrmService
import com.example.yatrimitra.utils.decodePolyline
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TrackingViewModel : ViewModel() {

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _vehicles =
        MutableStateFlow<List<VehicleState>>(emptyList())
    val vehicles: StateFlow<List<VehicleState>> = _vehicles

    private val _multiRoutes =
        MutableStateFlow<List<List<GeoPoint>>>(emptyList())
    val multiRoutes: StateFlow<List<List<GeoPoint>>> = _multiRoutes

    private val _eta = MutableStateFlow(0f)
    val eta: StateFlow<Float> = _eta

    private var isSimulating = false

    private val routeDistances =
        mutableListOf<Float>()

    // 🚗 Different speeds
    private val vehicleSpeeds = listOf(
        0.0015f, // Auto 1 → slow
        0.0025f, // Auto 2 → medium
        0.0035f  // Auto 3 → fast
    )

    private var isReversing = false

    init {
        fetchRoutes()
    }

    // 🌍 Fetch 3 Different Routes
    private fun fetchRoutes() {

        viewModelScope.launch {

            try {

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://router.project-osrm.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(OsrmService::class.java)

                // ✅ SAME START
                val start = "77.5946,12.9716"

                // ✅ SAME END
                val end = "77.6245,12.9252"

                // ✅ 3 DIFFERENT VIA POINTS
                val viaPoints = listOf(

                    // Route 1 → shortest city route
                    "77.6020,12.9650",

                    // Route 2 → eastern bypass
                    "77.6350,12.9550",

                    // Route 3 → southern long route
                    "77.5800,12.9200"
                )

                val decodedRoutes =
                    mutableListOf<List<GeoPoint>>()

                viaPoints.forEach { via ->

                    // 🚘 Route format:
                    // start → via → end

                    val coordinates =
                        "$start;$via;$end"

                    val response =
                        service.getRoute(coordinates)

                    val routeData =
                        response.routes.firstOrNull()

                    val encoded =
                        routeData?.geometry

// ✅ Store actual distance
                    val distanceKm =
                        ((routeData?.distance ?: 0.0) / 1000.0).toFloat()

                    routeDistances.add(distanceKm)

                    encoded?.let {

                        val route =
                            decodePolyline(it)

                        decodedRoutes.add(route)
                    }
                }

                _multiRoutes.value = decodedRoutes

                // 🚗 Create Vehicles
                _vehicles.value =
                    decodedRoutes.mapIndexed { index, route ->

                        VehicleState(
                            id = index + 1,
                            name = "Auto ${index + 1}",
                            position = route.first(),
                            progress = 0f,
                            eta = 0,
                            routeDistanceKm = routeDistances.getOrElse(index) { 0f },

                            // NEW DETAILS
                            driverName = when(index) {
                                0 -> "Ramesh"
                                1 -> "Suresh"
                                2 -> "Mahesh"
                                else -> "Driver ${index + 1}"
                            },

                            numberPlate = when(index) {
                                0 -> "KA 05 AB 1234"
                                1 -> "KA 03 CD 5678"
                                2 -> "KA 01 EF 9012"
                                else -> "KA 00 XX 0000"
                            },

                            contactNumber = when(index) {
                                0 -> "9876543210"
                                1 -> "9123456780"
                                2 -> "9988776655"
                                else -> "9000000000"
                            }
                        )
                    }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun isReversing(): Boolean {
        return isReversing
    }

    // ▶️ Start / Stop
    fun toggleTracking() {

        // ▶ If already running → stop manually
        if (_isRunning.value) {

            _isRunning.value = false
            return
        }

        // ▶ Start tracking
        _isRunning.value = true

        // ✅ If all autos reached destination,
        // start reverse trip
        val allReached =
            _vehicles.value.all {
                it.progress >= 0.99f
            }

        if (allReached) {
            isReversing = true
        }

        if (!isSimulating) {

            isSimulating = true
            startSimulation()
        }
    }

    // 🚘 Vehicle Simulation
    private fun startSimulation() {

        viewModelScope.launch {

            while (_isRunning.value) {

                val routes = _multiRoutes.value

                if (routes.isEmpty()) {

                    delay(100)
                    continue
                }

                val updatedVehicles =
                    _vehicles.value.mapIndexed { index, vehicle ->

                        val route =
                            routes.getOrNull(index)
                                ?: return@mapIndexed vehicle

                        // ✅ Different speed
                        val speed =
                            vehicleSpeeds.getOrElse(index) {
                                0.002f
                            }

                        val rawProgress = if (!isReversing) {
                            vehicle.progress + speed
                        } else {
                            vehicle.progress - speed
                        }

                        val nextProgress = when {
                            !isReversing && rawProgress >= 0.999f -> 1f
                            isReversing && rawProgress <= 0.001f -> 0f
                            else -> rawProgress.coerceIn(0f, 1f)
                        }

                        val exactIndex =
                            nextProgress * (route.size - 1)

                        val startIndex =
                            exactIndex.toInt()

                        val endIndex =
                            (startIndex + 1)
                                .coerceAtMost(route.size - 1)

                        val fraction =
                            exactIndex - startIndex

                        val finalPosition = when {

                            nextProgress >= 0.99f -> route.last()

                            nextProgress <= 0.01f -> route.first()

                            else -> {
                                val exactIndex = nextProgress * (route.size - 1)

                                val startIndex = exactIndex.toInt()
                                val endIndex = (startIndex + 1).coerceAtMost(route.size - 1)

                                val fraction = exactIndex - startIndex

                                val startPoint = route[startIndex]
                                val endPoint = route[endIndex]

                                val lat = startPoint.latitude +
                                        (endPoint.latitude - startPoint.latitude) * fraction

                                val lon = startPoint.longitude +
                                        (endPoint.longitude - startPoint.longitude) * fraction

                                GeoPoint(lat, lon) // ✅ CORRECT
                            }
                        }

                        VehicleState(
                            id = vehicle.id,
                            name = vehicle.name,
                            position = finalPosition,
                            progress = nextProgress,

                            eta = vehicle.eta,

                            routeDistanceKm = vehicle.routeDistanceKm,

                            // KEEP DRIVER DETAILS
                            driverName = vehicle.driverName,
                            numberPlate = vehicle.numberPlate,
                            contactNumber = vehicle.contactNumber
                        )
                    }



                // ⏳ ETA using first auto
                val vehiclesWithEta =
                    updatedVehicles.mapIndexed { index, vehicle ->

                        val totalDistanceKm =
                            routeDistances.getOrElse(index) { 5f }

                        val effectiveProgress = if (!isReversing) {

                            vehicle.progress

                        } else {

                            1f - vehicle.progress
                        }

                        val remainingDistanceKm =
                            totalDistanceKm * (1f - effectiveProgress)

                        // ✅ Different speed for each auto
                        val speedKmph = when(index) {
                            0 -> 12f
                            1 -> 18f
                            else -> 25f
                        }

                        // ETA in seconds
                        val etaSeconds = maxOf(
                            1,
                            ((remainingDistanceKm / speedKmph) * 3600).toInt()
                        )


                        vehicle.copy(
                            eta = etaSeconds
                        )
                    }

                _vehicles.value = vehiclesWithEta

                /// ✅ Stop after forward trip
                val allReached =
                    vehiclesWithEta.all {
                        it.progress >= 0.99f
                    }

                if (allReached && !isReversing) {

                    _isRunning.value = false
                }
                // ✅ Stop after reverse trip
                val allReturned =
                    vehiclesWithEta.all {
                        it.progress <= 0.01f
                    }

                if (allReturned && isReversing) {

                    _isRunning.value = false
                    isReversing = false
                }


                delay(50)
            }

            isSimulating = false
        }
    }
}