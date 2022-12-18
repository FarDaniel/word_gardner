package aut.dipterv.word_gardner_backend.repository

import aut.dipterv.word_gardner_backend.model.PackFolderCrossRef
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PackFolderCrossRefRepository : CrudRepository<PackFolderCrossRef, Long> {

    @Query("SELECT * FROM pack_folder_cross_ref WHERE folder_id = ?1 ", nativeQuery = true)
    fun getByFolder(id: Long): List<PackFolderCrossRef>

}