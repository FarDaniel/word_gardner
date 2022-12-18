package aut.dipterv.word_gardner.local_data.dao

import androidx.room.*
import aut.dipterv.word_gardner.local_data.dataenums.ColorOption.CardColor
import aut.dipterv.word_gardner.local_data.models.*
import aut.dipterv.word_gardner.local_data.relations.folder_pack.PackFolderCrossRef
import aut.dipterv.word_gardner.local_data.relations.pack_wordcard.WordCardsOfPack

@Dao
interface WordPackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPack(pack: Pack): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordCard(card: WordCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: Folder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackFolderCrossRef(packFolderCrossRef: PackFolderCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatistics(statistics: Statistics)

    @Transaction
    @Query("SELECT * FROM Folder WHERE folderId = :folderId")
    suspend fun getFolder(folderId: Long): List<Folder>

    @Transaction
    @Query("SELECT * FROM Pack WHERE packId = :packId")
    suspend fun getPack(packId: Long): List<Pack>

    @Query("SELECT * FROM statistics WHERE testIdentifier = :testIdentifier AND foreignWord = :foreignWord")
    suspend fun getStatistics(testIdentifier: Int, foreignWord: String): List<Statistics>

    @Query("SELECT * FROM statistics")
    suspend fun getStatistics(): List<Statistics>

    @Query(
        "UPDATE statistics SET succeeded = :succeeded," +
                " succeededTimeAverage = :succeededTimeAverage," +
                " faulted = :faulted," +
                " faultedTimeAverage = :faultedTimeAverage " +
                "WHERE id = :id"
    )
    suspend fun updateStatistics(
        id: Long,
        succeeded: Int,
        succeededTimeAverage: Float,
        faulted: Int,
        faultedTimeAverage: Float
    )

    @Query("SELECT * FROM Pack ORDER BY category")
    suspend fun getPacks(): List<Pack>

    @Query("SELECT * FROM Folder ORDER BY category")
    suspend fun getFolders(): List<Folder>

    @Query("SELECT DISTINCT category FROM Pack")
    suspend fun getPackCategories(): List<String>

    @Transaction
    @Query("SELECT * FROM Pack WHERE packId = :packId")
    suspend fun getWordCardsOfPack(packId: Long): List<WordCardsOfPack>

    @Query("SELECT DISTINCT category FROM Folder")
    suspend fun getFolderCategories(): List<String>

    @Query("SELECT packId FROM PackFolderCrossRef WHERE folderId = :folderId")
    suspend fun getPackIdsFromFolder(folderId: Long): List<Long>

    @Query("DELETE FROM Pack WHERE packId = :packId")
    suspend fun deletePack(packId: Long)

    @Query("DELETE FROM Folder WHERE folderId = :folderId")
    suspend fun deleteFolder(folderId: Long)

    @Query("DELETE FROM PackFolderCrossRef WHERE folderId = :folderId")
    suspend fun deletePackFolderCrossRefsOfFolder(folderId: Long)

    @Query("DELETE FROM WordCard WHERE inPackId = :inPackId")
    suspend fun deleteWordCards(inPackId: Long)

    @Query("UPDATE WordCard SET backGround = :backGround WHERE foreignWord = :foreignWord AND nativeWord = :nativeWord")
    suspend fun updateBackGroundOfWordCard(
        backGround: CardColor,
        foreignWord: String,
        nativeWord: String
    )

    @Query("SELECT * FROM Pack WHERE onlineId = :onlineId")
    suspend fun getPackByOnlineId(onlineId: Long): List<Pack>

    @Query("SELECT * FROM Folder WHERE onlineId = :onlineId")
    suspend fun getFolderByOnlineId(onlineId: Long): List<Folder>

    @Query("SELECT * FROM User LIMIT 1")
    suspend fun getUser(): User?

    @Query("DELETE FROM statistics")
    suspend fun deleteStatistics()

}
