package com.lion.a066ex_animalmanager.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lion.a066ex_animalmanager.vo.AnimalVO

@Dao
interface AnimalDAO {
    // 동물 정보 저장
    @Insert
    fun insertAnimalData(animalVO: AnimalVO)

    // 동물 정보를 가져오는 메서드
    @Query("""
        select * from AnimalTable
        order by animalInx desc""")
    fun selectAnimalDataAll(): List<AnimalVO>

    // 동물 한마리의 정보를 가져오는 메서드
    @Query("""
        select * from AnimalTable
        where animalInx = :animalIdx
    """)
    fun selectAnimalDataByAnimalIxd(animalIdx:Int) : AnimalVO

    // 동물 한마리의 정보를 삭제하는 메서드
    @Delete
    fun deleteAnimalData(animalVO: AnimalVO)

    // 동물 한마리의 정보를 수정하는 메서드
    @Update
    fun updateAnimalData(animalVO: AnimalVO)
}