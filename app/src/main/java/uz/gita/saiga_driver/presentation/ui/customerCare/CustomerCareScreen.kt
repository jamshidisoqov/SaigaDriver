package uz.gita.saiga_driver.presentation.ui.customerCare

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenCustomerCareBinding
import uz.gita.saiga_driver.utils.extensions.hideProgress
import uz.gita.saiga_driver.utils.extensions.include
import uz.gita.saiga_driver.utils.extensions.showProgress

// Created by Jamshid Isoqov on 2/7/2023
class CustomerCareScreen : Fragment(R.layout.screen_customer_care) {


    private val viewBinding: ScreenCustomerCareBinding by viewBinding()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = viewBinding.include {
        showProgress()
        customerCarePage.apply {
            settings.apply {
                domStorageEnabled = true
                loadsImagesAutomatically = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                javaScriptEnabled = true
            }
            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    hideProgress()
                }
            }
            webChromeClient = WebChromeClient()

            loadUrl("https://saiga.uz/drivers")
        }

    }

}