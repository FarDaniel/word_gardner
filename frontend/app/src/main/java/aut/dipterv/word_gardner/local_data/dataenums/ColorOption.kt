package aut.dipterv.word_gardner.local_data.dataenums

import android.graphics.Color

object ColorOption {
    enum class CardColor(val colorCode: Int, val id: Int) {
        GRAY(Color.argb(255, 45, 45, 45), 0),
        RED(Color.argb(255, 150, 0, 0), 1),
        ORANGE(Color.argb(255, 125, 82, 42), 2),
        GREEN(Color.argb(255, 50, 125, 50), 3),
        PURPLE(Color.argb(255, 96, 5, 124), 4),
        BLUE(Color.argb(255, 50, 50, 100), 5),
        YELLOW(Color.argb(255, 150, 150, 0), 6);
    }

    fun getById(id: Int): CardColor {
        return when (id) {
            0 -> CardColor.GRAY
            1 -> CardColor.RED
            2 -> CardColor.ORANGE
            3 -> CardColor.GREEN
            4 -> CardColor.PURPLE
            5 -> CardColor.BLUE
            6 -> CardColor.YELLOW
            else -> CardColor.GRAY
        }
    }

    fun getByName(color: String): CardColor {
        return when (color) {
            "gray" -> CardColor.GRAY
            "red" -> CardColor.RED
            "orange" -> CardColor.ORANGE
            "green" -> CardColor.GREEN
            "purple" -> CardColor.PURPLE
            "blue" -> CardColor.BLUE
            "yellow" -> CardColor.YELLOW
            else -> CardColor.GRAY
        }
    }
}
