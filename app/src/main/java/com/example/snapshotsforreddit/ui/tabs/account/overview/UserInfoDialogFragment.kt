package com.example.snapshotsforreddit.ui.tabs.account.overview

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class UserInfoDialogFragment(private val infoItem: RedditChildrenData, private val type: Int): AppCompatDialogFragment() {
    private val viewModel: AccountOverviewViewModel by viewModels()

    //TODO REFACTOR AND PUT LOGIC IN VIEWMODEL

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val commentKarmaCount = NumberFormat.getNumberInstance().format(infoItem.comment_karma)
        val postKarmaCount = NumberFormat.getNumberInstance().format(infoItem.link_karma)
        val awarderKarmaCount = NumberFormat.getNumberInstance().format(infoItem.awarder_karma)
        val awardeeKarmaCount = NumberFormat.getNumberInstance().format(infoItem.awardee_karma)
        val totalKarmaCount = NumberFormat.getNumberInstance().format(infoItem.total_karma)
        val epoch = infoItem.user_created_utc

        val createdOn: LocalDateTime? = epoch?.let { Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }

        val ageMY = Period.between(createdOn!!.toLocalDate(), LocalDateTime.now().toLocalDate())



        val title = when(type) {
            0 -> "$commentKarmaCount Comment Karma"
            1 ->  "$postKarmaCount Post Karma"
            2 -> "Account is ${ageMY.years} years, ${ageMY.months} months, ${ageMY.days} days old"

            else -> {""}
        }


        val message = when(type) {
            0 -> "$postKarmaCount Post Karma\n$awarderKarmaCount Awarder Karma\n$awardeeKarmaCount Awardee Karma\n\n" +
                    "$totalKarmaCount Total Karma"

            1 ->  "$commentKarmaCount Comment Karma\n$awarderKarmaCount Awarder Karma\n$awardeeKarmaCount Awardee Karma\n\n" +
                    "$totalKarmaCount Total Karma"

//            2 -> "Created on ${createdOn?.month}, ${createdOn?.dayOfMonth}th, ${createdOn?.year} at ${createdOn?.}:" +
//                    "${createdOn?.minute}"
            2 -> "Created on ${createdOn.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT))}"
            else -> {""}
        }



        return if(type == 2) {
            val titleView = layoutInflater.inflate(com.example.snapshotsforreddit.R.layout.textview_user_info, null) as TextView?
            titleView?.text = title
            titleView?.gravity = Gravity.CENTER
            titleView?.setTextAppearance(com.example.snapshotsforreddit.R.style.BodyTextAppearance_MaterialComponents_Title)

            MaterialAlertDialogBuilder(requireContext()).setCustomTitle(titleView).setMessage(message).create()

        }else {
            MaterialAlertDialogBuilder(requireContext()).setTitle(title).setMessage(message).create()
        }

    }

    companion object {
        fun newInstance(infoItem: RedditChildrenData, type: Int) = UserInfoDialogFragment(infoItem, type)
    }
}