package uz.gita.saiga_driver.presentation.ui.direction.detail

import android.graphics.Color
import android.location.LocationManager
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.data.remote.response.map.DestinationData
import uz.gita.saiga_driver.databinding.ScreenDirectionsDetailBinding
import uz.gita.saiga_driver.presentation.presenter.DirectionViewModelImpl
import uz.gita.saiga_driver.utils.MapHelper
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.extensions.bitmapFromVector

// Created by Jamshid Isoqov on 12/13/2022
@AndroidEntryPoint
class DirectionDetailScreen : Fragment(R.layout.screen_directions_detail) {

    private lateinit var googleMap: GoogleMap
    private var line: Polyline? = null

    private val viewModel: DirectionViewModel by viewModels<DirectionViewModelImpl>()

    private val viewBinding: ScreenDirectionsDetailBinding by viewBinding()

    private val args: DirectionDetailScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val direction = args.order.direction
        val addressFrom = direction.addressFrom
        val addressTo = direction.addressTo
        val start =  LatLng(addressFrom.lat!!, addressFrom.lon!!)
        val end =  LatLng(addressTo?.lat?: NUKUS.latitude, addressTo?.lon?: NUKUS.longitude)
        mapInit(
            DestinationData(
                fromWhere = addressFrom.title!!,
                toWhere = addressTo!!.title!!,
                start = start,
                end = end,
            )
        )


        viewBinding.iconBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.routes.onEach {
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

        viewModel.findRoutes(args.order)
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

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 16F))
        }
    }

}