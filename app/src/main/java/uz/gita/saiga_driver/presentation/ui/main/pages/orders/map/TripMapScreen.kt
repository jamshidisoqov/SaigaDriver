package uz.gita.saiga_driver.presentation.ui.main.pages.orders.map

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.map.DestinationData
import uz.gita.saiga_driver.databinding.ScreenMapTripBinding
import uz.gita.saiga_driver.presentation.presenter.TripMapViewModelImpl
import uz.gita.saiga_driver.utils.MapHelper
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.currentLocation
import uz.gita.saiga_driver.utils.currentLocationBearing
import uz.gita.saiga_driver.utils.extensions.bitmapFromVector
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.log

// Created by Jamshid Isoqov on 2/5/2023
@AndroidEntryPoint
class TripMapScreen : Fragment(R.layout.screen_map_trip) {

    private val args: TripMapScreenArgs by navArgs()

    private val viewBinding: ScreenMapTripBinding by viewBinding()

    private val viewModel: TripMapViewModel by viewModels<TripMapViewModelImpl>()

    private lateinit var googleMap: GoogleMap

    private var line: Polyline? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        val direction = args.order.direction
        val addressFrom = direction.addressFrom
        log(addressFrom.toString())
        val start = LatLng(addressFrom.lat ?: 42.460168, addressFrom.lon ?: 59.607280)
        val end = currentLocation.value ?: NUKUS
        mapInit(
            DestinationData(
                fromWhere = "Start address",
                toWhere = addressFrom.title!!,
                start = start,
                end = end,
            )
        )


        viewBinding.iconBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.routesFlow.onEach {
            val listPoints: List<LatLng> = it.route?.get(0)!!.points
            val options = PolylineOptions().width(5f).color(Color.BLUE).geodesic(true)
            val iterator = listPoints.iterator()
            while (iterator.hasNext()) {
                val data = iterator.next()
                options.add(data)
            }
            line?.remove()
            line = googleMap.addPolyline(options)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it.route[0].latLgnBounds.center))
            val builder = LatLngBounds.Builder()
            builder.include(start)
            builder.include(end)
            val bounds = builder.build()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 60))

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getRoutesByLocation(
            DestinationData("Start address", "To address", start, end)
        )


    }

    private fun mapInit(destination: DestinationData) {
        val mapScreen =
            childFragmentManager.findFragmentById(R.id.trip_detail_map_container) as MapHelper
        mapScreen.getMapAsync(mapScreen)

        val start = destination.start
        val end = destination.end

        mapScreen.onMapReady {
            googleMap = it
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            googleMap.uiSettings.apply {
                isCompassEnabled = false
            }
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            googleMap.clear()
            googleMap.addMarker(
                MarkerOptions().position(start)
                    .title(args.order.direction.addressFrom.title)
                    .icon(bitmapFromVector(R.drawable.ic_target_red))
            )
            googleMap.addMarker(
                MarkerOptions().position(end)
                    .title(args.order.direction.addressTo!!.title)
                    .icon(bitmapFromVector(R.drawable.ic_target_blue))
            )

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 10F))

            val options = MarkerOptions().position(end)
                .title("Current location")
                .flat(true)
                .anchor(1f, 1f)
                .position(currentLocation.value ?: NUKUS)
                .icon(bitmapFromVector(R.drawable.ic_navigation))

            val marker = googleMap.addMarker(options)


            currentLocationBearing.observe(viewLifecycleOwner) { pair ->
                marker?.apply {
                    rotation = pair.second
                    position = pair.first
                }
            }
        }
    }
}