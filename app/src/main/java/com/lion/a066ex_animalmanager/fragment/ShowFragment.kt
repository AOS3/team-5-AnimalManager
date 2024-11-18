package com.lion.a066ex_animalmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.FragmentShowBinding
import com.lion.a066ex_animalmanager.repository.AnimalRepository
import com.lion.a066ex_animalmanager.util.FragmentName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShowFragment : Fragment() {

    lateinit var fragmentShowBinding: FragmentShowBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentShowBinding = FragmentShowBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드 호출
        settingToolbarShow()

        // TextView를 구성하는 메서드 호출
        settingTextView()

        return fragmentShowBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbarShow(){
        fragmentShowBinding.apply {
            // 타이틀
            materialToolbarShow.title = "동물 정보"
            // 네비게이션 버튼
            materialToolbarShow.setNavigationIcon(R.drawable.arrow_back_24px)
            materialToolbarShow.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
            }

            // 메뉴 버튼
            materialToolbarShow.setOnMenuItemClickListener {
                when(it.itemId){
                    // 정보 수정 화면으로 이동
                    R.id.show_toolbar_menu_modify -> {
                        // 동물 번호를 담아준다.
                        val dataBundle = Bundle()
                        dataBundle.putInt("animalIdx", arguments?.getInt("animalIdx")!!)
                        mainActivity.replaceFragment(FragmentName.MODIFY_FRAGMENT, true, dataBundle)
                    }
                    // 데이터를 삭제하고 메인화면으로 이동한다.
                    R.id.show_toolbar_menu_delete -> {
                        deleteAnimalInfo()
                    }
                }
                true
            }
        }
    }

    // TextView에 값을 설정하는 메서드
    fun settingTextView(){
        fragmentShowBinding.apply {
            // Text값이 보일 수 있으므로 TextView들을 초기화해준다.
            textViewShowType.text = ""
            textViewShowName.text = ""
            textViewShowAge.text = ""
            textViewShowGender.text = ""
            textViewShowFood.text = ""

            // 동물 번호를 추출한다.
            val animalIdx = arguments?.getInt("animalIdx")!!
            // 동물 데이터를 가져와 출력한다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    AnimalRepository.selectAnimalInfoByAnimalIdx(mainActivity, animalIdx)
                }
                val animalViewModel = work1.await()

                textViewShowType.text = "종류 : ${animalViewModel.animalType.str}"
                textViewShowName.text = "이름 : ${animalViewModel.animalName}"
                textViewShowAge.text = "나이 : ${animalViewModel.animalAge}살"
                textViewShowGender.text = "성별 : ${animalViewModel.animalGender.str}"
                textViewShowFood.text = if (animalViewModel.animalFood.isNotEmpty()) {
                    "좋아하는 간식 : ${animalViewModel.animalFood}"
                }else{
                    "좋아하는 간식이 없습니다."
                }
            }
        }
    }
    // 삭제처리 메서드
    fun deleteAnimalInfo(){
        // 다이얼로그를 띄워준다.
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(mainActivity)
        materialAlertDialogBuilder.setTitle("정보 삭제")
        materialAlertDialogBuilder.setMessage("삭제를 할 경우 복원이 불가능합니다.")
        materialAlertDialogBuilder.setNegativeButton("취소", null)
        materialAlertDialogBuilder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO) {
                    val animalIdx = arguments?.getInt("animalIdx")
                    AnimalRepository.deleteAnimalByAnimalIdx(mainActivity, animalIdx!!)
                }
                work1.join()
                mainActivity.removeFragment(FragmentName.SHOW_FRAGMENT)
                Toast.makeText(mainActivity, "삭제되었습니다", Toast.LENGTH_SHORT).show()
            }
        }
        materialAlertDialogBuilder.show()
    }

}