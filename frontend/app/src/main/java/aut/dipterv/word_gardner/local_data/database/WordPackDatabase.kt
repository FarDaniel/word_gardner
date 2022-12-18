package aut.dipterv.word_gardner.local_data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import aut.dipterv.word_gardner.local_data.dao.WordPackDao
import aut.dipterv.word_gardner.local_data.models.*
import aut.dipterv.word_gardner.local_data.relations.folder_pack.PackFolderCrossRef

@Database(
    entities = [
        Folder::class,
        Pack::class,
        Statistics::class,
        User::class,
        WordCard::class,
        PackFolderCrossRef::class
    ],
    version = 1
)
abstract class WordPackDatabase : RoomDatabase() {

    abstract fun wordPackDao(): WordPackDao

    companion object {
        @Volatile
        private var INSTANCE: WordPackDatabase? = null

        fun getInstance(context: Context): WordPackDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    WordPackDatabase::class.java,
                    "WordPack_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}