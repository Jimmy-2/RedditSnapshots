package com.jimmywu.snapshotsforreddit.util

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.jimmywu.snapshotsforreddit.data.datastore.AppTheme
import kotlinx.coroutines.channels.SendChannel
import java.time.*


fun getShortenedValue(value: Int?): String {
    return when {
        value == null -> {
            ""
        }
        value < 1000 -> {
            value.toString()
        }
        value < 1000000 -> {
            String.format("%.1f", (value.toDouble() / 1000)) + "K"
        }
        else -> {
            String.format("%.1f", (value.toDouble() / 1000000)) + "M"
        }
    }
}


fun calculateAgeDifferenceLocalDateTime(epoch: Long, type: Int): String {
    val createdOn =
        epoch.let { Instant.ofEpochMilli(it * 1000).atZone(ZoneId.systemDefault()).toLocalDate() }
    val ageMY = Period.between(createdOn, LocalDate.now())
    return when {
        //if both years and months == 0, we measure account age by days, hours, and minutes instead
        ageMY.years == 0 && ageMY.months == 0 -> {
            val createdOn = epoch.let {
                Instant.ofEpochMilli(it * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime()
            }
            //Days, hours, minutes, and seconds
            val ageDHMS = Duration.between(createdOn, LocalDateTime.now())
            return when {
                ageDHMS.toHours() < 24 -> {
                    if (ageDHMS.toHours() < 1) {
                        if (ageDHMS.toMinutes() < 1) {
                            return "${ageDHMS.seconds}s"
                        } else {
                            return "${ageDHMS.toMinutes()}m"
                        }
                    } else {
                        return "${ageDHMS.toHours()}h"
                    }
                }
                else -> "${ageDHMS.toHours() / 24}d"
            }
        }
        ageMY.years == 0 -> "${ageMY.months}mo"
        ageMY.months == 0 -> "${ageMY.years}y "
        //type 1 = account age, type 0 = comments
        else -> if (type == 1) {
            "${ageMY.years}y ${ageMY.months}mo"
        } else {
            //comments older than or equal to 1 year are formatted as decimals
            "${
                String.format(
                    "%.1f",
                    ((ageMY.years * 12.toDouble()) + ageMY.months.toDouble()) / 12
                )
            }y"
        }
    }
}

fun calculateAge(createdOn: LocalDateTime) {
    val daysAge = Duration.between(createdOn, LocalDateTime.now()).toDays()
    val age = daysAge/365
}



fun <E> SendChannel<E>.tryOffer(element: E): Boolean = try {
    trySend(element).isSuccess
} catch (t: Throwable) {
    false
}

//From iosched github open source
@SuppressLint("WrongConstant")
fun AppCompatActivity.updateForTheme(theme: String) = when (theme) {
    AppTheme.DARK.name -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
    AppTheme.LIGHT.name  -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
    else -> delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
}

