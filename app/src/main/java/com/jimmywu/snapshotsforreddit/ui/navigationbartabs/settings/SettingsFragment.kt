package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jimmywu.snapshotsforreddit.R
import com.jimmywu.snapshotsforreddit.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel: SettingsViewModel by viewModels()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentSettingsBinding.bind(view)


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigationActions.collect {
                if (it is SettingsNavigationAction.NavigateToThemeSelector) {
                    ThemeDialogFragment.newInstance()
                        .show(parentFragmentManager, null)
                }
            }
        }

        binding.apply {
            buttonChangeTheme.setOnClickListener{
                viewModel.onChangeColorClicked()
            }
        }


    }


}

