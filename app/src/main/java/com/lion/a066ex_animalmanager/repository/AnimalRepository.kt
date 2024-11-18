package com.lion.a066ex_animalmanager.repository

import android.content.Context
import com.lion.a066ex_animalmanager.dao.AnimalDatabase
import com.lion.a066ex_animalmanager.util.AnimalFood
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import com.lion.a066ex_animalmanager.viewmodel.AnimalViewModel
import com.lion.a066ex_animalmanager.vo.AnimalVO

class AnimalRepository {
    companion object{
        // 동물 정보를 저장하는 메서드
        fun insertAnimalInfo(context: Context, animalViewModel: AnimalViewModel){
            // 데이터 베이스 객체를 가져온다.
            val animalDatabase = AnimalDatabase.getInstance(context)
            // ViewModel에 있는 데이터를 VO에 담아준다.
            val animalType = animalViewModel.animalType.number
            val animalName = animalViewModel.animalName
            val animalAge = animalViewModel.animalAge
            val animalGender = animalViewModel.animalGender.number
            val animalFood = animalViewModel.animalFood

            val animalVO = AnimalVO(animalType = animalType, animalName = animalName, animalAge = animalAge,
                animalGender = animalGender, animalFood = animalFood
                )
            animalDatabase?.animalDAO()?.insertAnimalData(animalVO)

        }

        // 동물 정보 전체를 가져오는 메서드
        fun selectAnimalInfoAll(context: Context) : MutableList<AnimalViewModel>{
            // 데이터베이스 객체
            val animalDatabase = AnimalDatabase.getInstance(context)
            // 동물 데이터 전체를 가져온다.
            val animalVoList = animalDatabase?.animalDAO()?.selectAnimalDataAll()
            // 동물 데이터를 담을 리스트
            val animalViewModelList = mutableListOf<AnimalViewModel>()
            // 동물의 수 만큼 반복한다.
            animalVoList?.forEach{
                // 동물 데이터를 추출한다.
                val animalType = when(it.animalType){
                    AnimalType.ANIMAL_TYPE_DOG.number -> AnimalType.ANIMAL_TYPE_DOG
                    AnimalType.ANIMAL_TYPE_CAT.number -> AnimalType.ANIMAL_TYPE_CAT
                    else -> AnimalType.ANIMAL_TYPE_PARROT
                }
                val animalName = it.animalName
                val animalAge = it.animalAge
                val animalGender = when(it.animalGender){
                    AnimalGender.GENDER_MALE.number -> AnimalGender.GENDER_MALE
                    else -> AnimalGender.GENDER_FEMALE
                }
                val animalFood = it.animalFood
                val animalIdx = it.animalInx
                // 객체에 담는다.
                val animalViewModel = AnimalViewModel(animalIdx, animalType, animalName, animalAge, animalGender, animalFood)
                // 리스트에 담는다.
                animalViewModelList.add(animalViewModel)
            }
            return animalViewModelList
        }

        // 선택한 동물 한마리의 정보를 가져오는 메서드
        fun selectAnimalInfoByAnimalIdx(context: Context, animalIdx:Int) : AnimalViewModel{
            val animalDatabase = AnimalDatabase.getInstance(context)
            // 동물 한마리의 정보를 가져온다.
            val animalVO = animalDatabase?.animalDAO()?.selectAnimalDataByAnimalIxd(animalIdx)
            // 동물 객체에 담는다.
            val animalType = when(animalVO?.animalType){
                AnimalType.ANIMAL_TYPE_DOG.number -> AnimalType.ANIMAL_TYPE_DOG
                AnimalType.ANIMAL_TYPE_CAT.number -> AnimalType.ANIMAL_TYPE_CAT
                else -> AnimalType.ANIMAL_TYPE_PARROT
            }
            val animalName = animalVO?.animalName
            val animalAge = animalVO?.animalAge
            val animalGender = when(animalVO?.animalGender){
                AnimalGender.GENDER_MALE.number -> AnimalGender.GENDER_MALE
                else -> AnimalGender.GENDER_FEMALE
            }
            val animalFood = animalVO?.animalFood
            // 객체에 담는다.
            val animalViewModel = AnimalViewModel(animalIdx, animalType, animalName!!, animalAge!!, animalGender, animalFood!!)

            return animalViewModel
        }

        // 동물 정보 삭제
        fun deleteAnimalByAnimalIdx(context: Context, animalIdx: Int){
            val animalDatabase = AnimalDatabase.getInstance(context)
            // 삭제할 동물 번호를 담고 있는 객체를 생성한다.
            val animalVO = AnimalVO(animalInx = animalIdx)
            // 삭제한다.
            animalDatabase?.animalDAO()?.deleteAnimalData(animalVO)
        }

        // 동물 정보 수정
        fun updateAnimalInfo(context: Context, animalViewModel: AnimalViewModel){
            val animalDatabase = AnimalDatabase.getInstance(context)
            // VO에 객체를 담아준다.
            val animalIdx = animalViewModel.animalInx
            val animalType = animalViewModel.animalType.number
            val animalName = animalViewModel.animalName
            val animalAge = animalViewModel.animalAge
            val animalGender = animalViewModel.animalGender.number
            val animalFood = animalViewModel.animalFood

            val animalVO = AnimalVO(animalIdx, animalType, animalName, animalAge, animalGender, animalFood)
            // 수정한다.
            animalDatabase?.animalDAO()?.updateAnimalData(animalVO)
        }
    }
}