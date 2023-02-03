package uz.gita.saiga_driver.presentation.ui.verify

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.fraggjkee.smsconfirmationview.SmsConfirmationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.saiga_driver.R
import uz.gita.saiga_driver.databinding.ScreenVerifyCodeBinding
import uz.gita.saiga_driver.presentation.presenter.VerifyViewModelImpl
import uz.gita.saiga_driver.utils.DEBOUNCE_VIEW_CLICK
import uz.gita.saiga_driver.utils.extensions.combine
import uz.gita.saiga_driver.utils.extensions.getTimeFormat
import uz.gita.saiga_driver.utils.extensions.include

// Created by Jamshid Isoqov on 12/12/2022
@AndroidEntryPoint
class VerifyCodeScreen : Fragment(R.layout.screen_verify_code) {
    private val viewModel: VerifyViewModel by viewModels<VerifyViewModelImpl>()
    private val viewBinding: ScreenVerifyCodeBinding by viewBinding()
    private var isFinishedTime: Boolean = false
    private var isCompletedSms: Boolean = false

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) =
        viewBinding.include {

            smsChecker.onChangeListener = SmsConfirmationView.OnChangeListener { _, isComplete ->
                isCompletedSms = isComplete
                check()
            }
            tvCodeSend.clicks().debounce(DEBOUNCE_VIEW_CLICK).onEach {
                if (isFinishedTime) {
                    isFinishedTime = false
                    viewModel.resendCode()
                    runTimer()
                    check()
                }
            }.launchIn(lifecycleScope)
            runTimer()
            btnCheck.clicks().debounce(DEBOUNCE_VIEW_CLICK).onEach {
                viewModel.verifyCode(smsChecker.enteredCode)
            }.launchIn(lifecycleScope)
        }

    @SuppressLint("SetTextI18n")
    private fun runTimer() = viewBinding.include {
        var time = 180
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            while (time > 0) {
                tvCodeSend.text = "Code expired".combine(time.getTimeFormat())
                delay(1000)
                time--
            }
            isFinishedTime = true
            check()
        }
    }

    private fun check() = viewBinding.include {
        btnCheck.isEnabled = isCompletedSms && !isFinishedTime
    }
}