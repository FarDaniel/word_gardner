package aut.dipterv.word_gardner.local_data.dataenums

import android.graphics.Color

enum class Difficulty(val colorCode: Int) {
    EASY(Color.argb(255, 42, 85, 42)),
    MEDIUM(Color.argb(255, 202, 120, 32)),
    HARD(Color.argb(255, 138, 54, 54)),
    UNDEFINED(Color.argb(255, 120, 120, 120))
}