package uz.gita.saiga_driver.presentation.dialogs.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import uz.gita.saiga_driver.MainActivity
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.DialogMapBinding
import uz.gita.saiga_driver.presentation.presenter.MapViewModelImpl
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.MapHelper
import uz.gita.saiga_driver.utils.NUKUS
import uz.gita.saiga_driver.utils.extensions.*


// Created by Jamshid Isoqov on 1/31/2023
@AndroidEntryPoint
class MapDialog(
    private val mapHint: String,
    private val currentLatLng: LatLng? = null
) :
    BottomSheetDialogFragment(R.layout.dialog_map) {

    private val viewBinding: DialogMapBinding by viewBinding()

    private var job: Job? = null

    private lateinit var center: LatLng

    private val viewModel: MapViewModel by viewModels<MapViewModelImpl>()

    private lateinit var googleMap: GoogleMap

    lateinit var behavior: BottomSheetBehavior<*>

    private var mapListener: ((String, LatLng) -> Unit)? = null

    fun setMapListener(block: (String, LatLng) -> Unit) {
        mapListener = block
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        behavior = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        MainActivity.activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    @SuppressLint("ClickableViewAccessibility")
    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        inputToAddress.hint = mapHint

        swipeView.setOnTouchListener { _, event ->
            val isDrag = when (event.action) {
                MotionEvent.ACTION_DOWN -> true
                MotionEvent.ACTION_UP -> false
                else -> true
            }
            behavior.isDraggable = isDrag
            false
        }
        mapInit()
        btnSave.clicks()
            .debounce(DEBOUNCE_VIEW_CLICK)
            .onEach {
                mapListener?.invoke(inputToAddress.text.toString(), center)
                dismiss()
            }.launchIn(lifecycleScope)

        imageMyLocation.setOnClickListener {
            hasPermission(Manifest.permission.ACCESS_FINE_LOCATION, onPermissionGranted = {
                viewModel.requestCurrentLocation()
            }, onPermissionDenied = {
                Snackbar.make(
                    viewBinding.swipeView,
                    "Permission denied",
                    Snackbar.LENGTH_SHORT
                ).show()
            })
        }

        viewModel.address.onEach { text ->
            mapListener?.invoke(text, center)
            inputToAddress.setText(text)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.loadingSharedFlow.onEach {
            if (it) {
                imageMyLocation.inVisible()
                progressSpinKit.visible()
            } else {
                progressSpinKit.inVisible()
                imageMyLocation.visible()
            }
        }.launchIn(lifecycleScope)

        viewModel.messageSharedFlow.onEach {
            showMessageDialog(it)
        }.launchIn(lifecycleScope)

        viewModel.errorSharedFlow.onEach {
            showErrorDialog(message = it)
        }.launchIn(lifecycleScope)

        viewModel.currentLocationFlow.observe(viewLifecycleOwner) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 16f))
        }

    }

    private fun mapInit() = viewBinding.include {
        val map = childFragmentManager.findFragmentById(R.id.mapFragment) as MapHelper
        map.getMapAsync(map)
        map.onMapReady {
            googleMap = it
            googleMap.uiSettings.apply {
                isCompassEnabled = false
                isRotateGesturesEnabled = false
                isMyLocationButtonEnabled = false
            }
            if (!this@MapDialog::center.isInitialized)
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLatLng ?: NUKUS,
                        16f
                    )
                )

            googleMap.setOnCameraIdleListener {
                job?.cancel()
                job = viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000L)
                    viewModel.getAddressByLocation(googleMap.cameraPosition.target)
                }
                center = googleMap.cameraPosition.target
            }
        }
    }


}