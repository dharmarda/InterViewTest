package com.example.realmtestinterview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realmtestinterview.adapters.FacelitiesRecyclerAdapter
import com.example.realmtestinterview.databinding.FragmentFirstBinding
import com.example.realmtestinterview.dataclasses.Exclusion
import com.example.realmtestinterview.dataclasses.Facility
import com.example.realmtestinterview.dataclasses.Option
import com.example.realmtestinterview.viemodel.AllAPisViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FacilitiesFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    val myviewmodel :AllAPisViewModel by viewModels()
    var selectedoption=ArrayList<Option>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var excludedlist=ArrayList<Option>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myviewmodel.excludedlist.postValue(ArrayList<Option>())
        binding.myrecyclerview.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        myviewmodel.facilitiesList.observeForever {
            var excludedlist=myviewmodel.facilitiesList.value?.exclusions
            var allexcludedobjects=ArrayList<Exclusion>()
            var allmovies=myviewmodel.facilitiesList.value?.facilities as ArrayList<Facility>
            excludedlist?.forEach {
                var i=0;
                var firstobject=it[0]
                var secondobject=it[1]
                if(i==0){
                    allmovies.forEach { ii->
                        Log.d("dharmesh",firstobject.facilityId)
                        if(ii.facilityId.equals(firstobject.facilityId)){
                            ii.options.forEach { pj->
                                if(pj.id.equals(firstobject.optionsId)){
                                    if(pj.excludedlist==null)
                                        pj.excludedlist= ArrayList()
                                    var exoption=Option("","",secondobject.optionsId,false,
                                        arrayListOf(),firstobject.facilityId)
                                    pj.excludedlist.add(exoption)
                                }
                            }
                        }
                    }
                }
            }
            allmovies.forEach { it->
                it.options.forEach { ij->
                    ij.mfacilityid=it.facilityId
                }
            }


            myviewmodel.excludedlist.observeForever {
                binding.myrecyclerview.adapter?.notifyDataSetChanged()
            }
            binding.myrecyclerview.adapter=FacelitiesRecyclerAdapter(this,allmovies)
        }
        myviewmodel.getAllFacilities()

    }
    public fun setExcludedList(exlist:ArrayList<Option>,currentselected:Option){
        excludedlist.removeIf {exid-> exid.mfacilityid.equals(currentselected.mfacilityid) }
        exlist.forEach { mexcludedd->
            selectedoption.removeIf { sel->sel.id.equals(mexcludedd.id) }
        }
        selectedoption?.removeIf { it->it.mfacilityid.equals(currentselected.mfacilityid) }

        selectedoption.add(currentselected)

        excludedlist.addAll(exlist)
        var mlist=excludedlist
        myviewmodel.excludedlist.postValue(mlist)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}