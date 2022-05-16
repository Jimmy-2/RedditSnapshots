package com.example.snapshotsforreddit.ui.tabs.settings

import android.R
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.snapshotsforreddit.data.AppTheme
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch




data class ThemeText(val theme: AppTheme, val text: String) {
    override fun toString(): String = text
}

@AndroidEntryPoint
class ThemeDialogFragment : AppCompatDialogFragment() {

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var listAdapter: ArrayAdapter<ThemeText>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        listAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_list_item_single_choice
        )

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Change app theme")
            .setSingleChoiceItems(listAdapter, 0) { dialog, position ->
                listAdapter.getItem(position)?.theme?.let {
                    viewModel.onThemeSelected(it)
                }
                dialog.dismiss()
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.availableThemes.collect { themes ->
                    listAdapter.clear()
                    listAdapter.addAll(
                        themes.map { theme ->
                            ThemeText(theme, getThemeText(theme))
                        }
                    )
                    updateSelectedTheme(viewModel.theme.value)
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.theme.collect {
                    println("HELLO DIALOG REPEAT ${it}")
                    updateSelectedTheme(it)
                }
            }
        }
    }

    private fun updateSelectedTheme(selected: AppTheme?) {
        val selectedPosition = (0 until listAdapter.count).indexOfFirst { index ->
            listAdapter.getItem(index)?.theme == selected
        }
        (dialog as AlertDialog).listView.setItemChecked(selectedPosition, true)
    }

    private fun getThemeText(theme: AppTheme) = when (theme) {
        AppTheme.LIGHT -> "Light"
        AppTheme.DARK -> "Dark"
        else -> "System Default"
    }

    companion object {
        fun newInstance() = ThemeDialogFragment()
    }

}