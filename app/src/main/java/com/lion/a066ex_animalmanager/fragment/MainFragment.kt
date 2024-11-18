package com.lion.a066ex_animalmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.a066ex_animalmanager.MainActivity
import com.lion.a066ex_animalmanager.R
import com.lion.a066ex_animalmanager.databinding.FragmentMainBinding
import com.lion.a066ex_animalmanager.databinding.RowMainBinding
import com.lion.a066ex_animalmanager.repository.AnimalRepository
import com.lion.a066ex_animalmanager.util.FragmentName
import com.lion.a066ex_animalmanager.viewmodel.AnimalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity

    var animalList = mutableListOf<AnimalViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // Toobar를 구성하는 메서드 호출
        settingToolbarMain()

        // FAB를 구성하는 메서드 호출
        settingFABMain()

        // RecyclerView를 구성하는 메서드 호출
        settingRecyclerView()

        // RecyclerView 갱신 메서드를 호출한다.
        refreshRecyclerViewMain()

        return fragmentMainBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbarMain(){
        fragmentMainBinding.apply {
            // 타이틀
            materialToolbarMain.title = "동물 목록"
        }
    }

    // FAB를 구성하는 메서드
    fun settingFABMain(){
        fragmentMainBinding.apply {
            fabMainAdd.setOnClickListener {
                // 입력화면으로 이동한다.
                mainActivity.replaceFragment(FragmentName.INPUT_FRAGMENT, true, null)
            }
        }
    }

    // RecyclerView 를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentMainBinding.apply {
            // 어댑터
            recyclerViewMain.adapter = RecyclerViewMainAdapter()
            // layoutManager
            recyclerViewMain.layoutManager = LinearLayoutManager(mainActivity)
            // 구분선
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewMain.addItemDecoration(deco)
        }
    }

    // 동물 정보를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshRecyclerViewMain(){
        // 동물 정보를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO) {
                // 동물 정보를 가져온다
                AnimalRepository.selectAnimalInfoAll(mainActivity)
            }
            animalList = work1.await()
            // RecyclerView를 갱신한다.
            fragmentMainBinding.recyclerViewMain.adapter?.notifyDataSetChanged()
        }
    }


    // RecyclerView Adapter
    inner class RecyclerViewMainAdapter : RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolderMain>(){
        inner class ViewHolderMain(val rowMainBinding: RowMainBinding) : RecyclerView.ViewHolder(rowMainBinding.root), View.OnClickListener{
            override fun onClick(v: View?) {
                // 사용자가 누른 동물의 번호를 담아준다.
                val dataBundle = Bundle()
                dataBundle.putInt("animalIdx", animalList[adapterPosition].animalInx)
                // 출력화면으로 이동한다.
                mainActivity.replaceFragment(FragmentName.SHOW_FRAGMENT, true, dataBundle)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMain {
            val rowMainBinding = RowMainBinding.inflate(layoutInflater, parent, false)
            val viewHolderMain = ViewHolderMain(rowMainBinding)

            // 리스너를 설정한다.
            rowMainBinding.root.setOnClickListener(viewHolderMain)

            return viewHolderMain
        }

        override fun getItemCount(): Int {
            return animalList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
            holder.rowMainBinding.textViewRowMain.text = animalList[position].animalName
        }
    }
}