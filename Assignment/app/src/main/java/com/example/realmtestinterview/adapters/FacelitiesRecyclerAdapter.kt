package com.example.realmtestinterview.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realmtestinterview.BR
import com.example.realmtestinterview.R
import com.example.realmtestinterview.databinding.FacilityRowItemBinding
import com.example.realmtestinterview.dataclasses.Facility
import com.example.realmtestinterview.dataclasses.Option
import kotlin.coroutines.coroutineContext

class FacelitiesRecyclerAdapter(val mfragment:Fragment) : RecyclerView.Adapter<FacelitiesRecyclerAdapter.MyHolder>() {

    var alldatalist=ArrayList<Facility>()
    var excludedids=ArrayList<String>()
    constructor(fment:Fragment,alllist:ArrayList<Facility>):this(fment){
        this.alldatalist=alllist
    }
    inner class MyHolder(mitem:View): RecyclerView.ViewHolder(mitem) {
        lateinit var mbinding:FacilityRowItemBinding
        constructor(mr:FacilityRowItemBinding):this(mr.root)
        {
            this.mbinding=mr
        }
        fun bind (itemobj:Facility,mcontext:Context){
            mbinding.setVariable(BR.item,itemobj)
            mbinding.executePendingBindings()
            var resourceID = mcontext.resources.getIdentifier(
                itemobj.name!!.replace("-","").lowercase(), "drawable", mcontext.packageName
            )
            Log.d("dharmesh",""+resourceID + " " +itemobj.name.replace("-","").lowercase())
            if(resourceID==0)
                mbinding.imgitem.setImageResource(com.google.android.material.R.drawable.ic_clock_black_24dp)
            else
                mbinding.imgitem.setImageResource(resourceID)
            mbinding.recmyoptions.adapter=OptionsRecyclerAdapter(mfragment,itemobj.options as ArrayList<Option> /* = java.util.ArrayList<com.example.realmtestinterview.dataclasses.Option> */)
            mbinding.recmyoptions.layoutManager=LinearLayoutManager(mcontext,LinearLayoutManager.HORIZONTAL,false)



        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var rowbinding: FacilityRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mfragment.context),
            R.layout.facility_row_item, parent, false
        )
        return MyHolder(rowbinding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(alldatalist.get(position), mfragment.requireContext())
        holder.mbinding.root.setOnClickListener {
            excludedids=alldatalist.get(position).excludeids
            notifyDataSetChanged()

        }
    }

    override fun getItemCount(): Int {
        return alldatalist.size
    }

}