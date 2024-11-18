package com.lion.a066ex_animalmanager.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.FragmentInputBinding
import com.lion.a066ex_animalmanager.repository.AnimalRepository
import com.lion.a066ex_animalmanager.util.AnimalFood
import com.lion.a066ex_animalmanager.util.AnimalGender
import com.lion.a066ex_animalmanager.util.AnimalType
import com.lion.a066ex_animalmanager.util.FragmentName
import com.lion.a066ex_animalmanager.viewmodel.AnimalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InputFragment : Fragment() {
    lateinit var fragmentInputBinding: FragmentInputBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentInputBinding = FragmentInputBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드 호출
        settingToolbarInput()

        return fragmentInputBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbarInput(){
        fragmentInputBinding.apply {
            // 타이틀
            materialToolbarInput.title = "동물 정보 입력"
            // 네비게이션 버튼
            materialToolbarInput.setNavigationIcon(R.drawable.arrow_back_24px)
            materialToolbarInput.setNavigationOnClickListener {
                // 메인 화면으로 돌아간다.
                mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
            }
            // 메뉴 버튼
            materialToolbarInput.setOnMenuItemClickListener {
                // 데이터 저장후 메인화면으로 이동한다.
                when(it.itemId){
                    R.id.input_toolbar_menu_done -> {
                        // 사용자가 입력한 데이터를 가져온다.

                        // 동물 타입
                        val animalType = when(toggleGroupInputType.checkedButtonId){
                            // 강아지
                            R.id.buttonInputTypeDog -> AnimalType.ANIMAL_TYPE_DOG
                            // 고양이
                            R.id.buttonInputTypeCat -> AnimalType.ANIMAL_TYPE_CAT
                            // 앵무새
                            else -> AnimalType.ANIMAL_TYPE_PARROT
                        }
                        // 동물 이름
                        val animalName = textFieldInputName.editText?.text.toString()
                        // 동물 나이
                        val animalAgeText = textFieldInputAge.editText?.text.toString()

                        var valid = true

                        // 동물 이름 입력 확인
                        if (animalName.isEmpty()) {
                            textFieldInputName.error = "이름을 입력해주세요"
                            valid = false
                        }

                        // 동물 나이 입력 확인
                        val animalAge = if (animalAgeText.isNotEmpty()) {
                            animalAgeText.toIntOrNull()
                        } else {
                            null
                        }

                        if (animalAge == null) {
                            textFieldInputAge.error = "나이를 입력해주세요"
                            valid = false
                        }
                        // 동물 성별
                        val animalGender = when(radioGroupInputGender.checkedRadioButtonId){
                            R.id.radioButtonInputMale -> AnimalGender.GENDER_MALE
                            else -> AnimalGender.GENDER_FEMALE
                        }
                        // 좋아하는 간식
                        val animalFood = mutableListOf<String>()
                        chipGroupInputFood.checkedChipIds.forEach {
                            when(it){
                                R.id.chipInputApple -> animalFood.add(AnimalFood.FOOD_APPLE.str)
                                R.id.chipInputBanana -> animalFood.add(AnimalFood.FOOD_BANANA.str)
                                R.id.chipInputJujube -> animalFood.add(AnimalFood.FOOD_JUJUBE.str)
                            }
                        }
                        val animalFoodString = animalFood.joinToString(", ")
                        if (valid){
                            // 객체에 담는다.
                            val animalViewModel = AnimalViewModel(0, animalType, animalName, animalAge!!, animalGender, animalFoodString)

                            // 데이터를 저장하는 메서드를 코루틴으로 운영 한다.
                            CoroutineScope(Dispatchers.Main).launch {
                                // 저장 작업이 끝날 때까지 대기 한다.
                                val work1 = async(Dispatchers.IO){
                                    // 저장한다.
                                    AnimalRepository.insertAnimalInfo(mainActivity, animalViewModel)
                                }
                                work1.join()
                                mainActivity.removeFragment(FragmentName.INPUT_FRAGMENT)
                            }
                        }
                        // 이름 입력 필드의 포커스를 제거
                        textFieldInputName.editText?.clearFocus()
                        // 나이 입력 필드의 포커스를 제거
                        textFieldInputAge.editText?.clearFocus()

                    }
                }
                true
            }
            // 텍스트 필드에 포커스를 주면 오류 메시지 제거
            textFieldInputName.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    textFieldInputName.error = null
                }
            }

            textFieldInputAge.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    textFieldInputAge.error = null
                }
            }
        }
    }
}