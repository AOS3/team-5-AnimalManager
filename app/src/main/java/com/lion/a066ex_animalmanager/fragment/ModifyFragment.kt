package com.lion.a066ex_animalmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.FragmentModifyBinding
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


class ModifyFragment : Fragment() {
    lateinit var fragmentModifyBinding: FragmentModifyBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentModifyBinding = FragmentModifyBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드 호출
        settingToolbarModify()
        // 입력 요소들 초기 설정
        settingInput()

        return fragmentModifyBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbarModify(){
        fragmentModifyBinding.apply {
            // 타이틀
            materialToolbarModify.title = "동물 정보 수정"
            // 네비게이션 버튼
            materialToolbarModify.setNavigationIcon(R.drawable.arrow_back_24px)
            materialToolbarModify.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
            }

            // 메뉴버튼
            materialToolbarModify.setOnMenuItemClickListener {
                when(it.itemId){
                    // 수정한 데이터 저장 후 출력화면으로 이동한다.
                    R.id.modify_toolbar_menu_done -> {
                        modifyDone()
                    }
                }
                true
            }

        }
    }

    // 입력 요소들 초기 설정
    fun settingInput(){
        fragmentModifyBinding.apply {
            // 동물 번호를 가져온다.
            val animalIdx = arguments?.getInt("animalIdx")!!
            // 동물 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    AnimalRepository.selectAnimalInfoByAnimalIdx(mainActivity, animalIdx)
                }
                val animalViewModel = work1.await()

                when(animalViewModel.animalType){
                    AnimalType.ANIMAL_TYPE_DOG -> {
                        toggleGroupModifyType.check(R.id.buttonModifyTypeDog)
                    }
                    AnimalType.ANIMAL_TYPE_CAT -> {
                        toggleGroupModifyType.check(R.id.buttonModifyTypeCat)
                    }
                    AnimalType.ANIMAL_TYPE_PARROT -> {
                        toggleGroupModifyType.check(R.id.buttonModifyTypeParrot)
                    }
                }
                textFieldModifyName.editText?.setText(animalViewModel.animalName)
                textFieldModifyAge.editText?.setText(animalViewModel.animalAge.toString())
                when(animalViewModel.animalGender){
                    AnimalGender.GENDER_MALE ->{
                        radioGroupModifyGender.check(R.id.radioButtonModifyMale)
                    }
                    AnimalGender.GENDER_FEMALE -> {
                        radioGroupModifyGender.check(R.id.radioButtonModifyMale)
                    }
                }
                val animalFoodList = animalViewModel.animalFood.split(", ")
                animalFoodList.forEach{
                    when(it){
                        AnimalFood.FOOD_APPLE.str -> chipModifyApple.isChecked = true
                        AnimalFood.FOOD_BANANA.str -> chipModifyBanana.isChecked = true
                        AnimalFood.FOOD_JUJUBE.str -> chipModifyJujube.isChecked = true
                    }
                }
            }
        }
    }
    // 수정 처리 메서드
    fun modifyDone(){
        fragmentModifyBinding.apply {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
            materialAlertDialogBuilder.setTitle("수정")
            materialAlertDialogBuilder.setMessage("이전 데이터로 복원할 수 없습니다")
            materialAlertDialogBuilder.setNeutralButton("취소", null)
            materialAlertDialogBuilder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->
                // 수정할 데이터
                val animalIdx = arguments?.getInt("animalIdx")!!
                val animalType = when(toggleGroupModifyType.checkedButtonId){
                    R.id.buttonModifyTypeDog -> AnimalType.ANIMAL_TYPE_DOG
                    R.id.buttonModifyTypeCat -> AnimalType.ANIMAL_TYPE_CAT
                    else -> AnimalType.ANIMAL_TYPE_PARROT
                }
                val animalName = textFieldModifyName.editText?.text.toString()
                val animalAgeText = textFieldModifyAge.editText?.text.toString()

                var valid = true

                // 동물 이름 입력 확인
                if (animalName.isEmpty()) {
                    textFieldModifyName.error = "이름을 입력해주세요"
                    valid = false
                }

                // 동물 나이 입력 확인
                val animalAge = if (animalAgeText.isNotEmpty()) {
                    animalAgeText.toIntOrNull()
                } else {
                    null
                }

                if (animalAge == null) {
                    textFieldModifyAge.error = "나이를 입력해주세요"
                    valid = false
                }

                val animalGender = when(radioGroupModifyGender.checkedRadioButtonId){
                    R.id.radioButtonModifyMale -> AnimalGender.GENDER_MALE
                    else -> AnimalGender.GENDER_FEMALE
                }
                val animalFood = mutableListOf<String>()
                chipGroupModifyFood.checkedChipIds.forEach {
                    when(it){
                        R.id.chipModifyApple -> animalFood.add(AnimalFood.FOOD_APPLE.str)
                        R.id.chipModifyBanana -> animalFood.add(AnimalFood.FOOD_BANANA.str)
                        R.id.chipModifyJujube -> animalFood.add(AnimalFood.FOOD_JUJUBE.str)
                    }
                }
                val animalFoodString = animalFood.joinToString(", ")

                if (valid){
                    val animalViewModel = AnimalViewModel(animalIdx, animalType, animalName, animalAge!!, animalGender, animalFoodString)


                    CoroutineScope(Dispatchers.Main).launch {
                        val work1 = async(Dispatchers.IO){
                            AnimalRepository.updateAnimalInfo(mainActivity, animalViewModel)
                        }
                        work1.join()
                        Toast.makeText(mainActivity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                        mainActivity.removeFragment(FragmentName.MODIFY_FRAGMENT)
                    }
                }
            }
            materialAlertDialogBuilder.show()

            // 이름 입력 필드의 포커스를 제거
            textFieldModifyName.editText?.clearFocus()
            // 나이 입력 필드의 포커스를 제거
            textFieldModifyAge.editText?.clearFocus()

            // 텍스트 필드에 포커스를 주면 오류 메시지 제거
            textFieldModifyName.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    textFieldModifyName.error = null
                }
            }

            textFieldModifyAge.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    textFieldModifyAge.error = null
                }
            }
        }
    }
}