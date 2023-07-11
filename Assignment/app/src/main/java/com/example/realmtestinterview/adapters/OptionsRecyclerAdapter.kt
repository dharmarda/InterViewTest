package com.example.realmtestinterview.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.realmtestinterview.BR
import com.example.realmtestinterview.FacilitiesFragment
import com.example.realmtestinterview.R
import com.example.realmtestinterview.databinding.OptionRowItemBinding
import com.example.realmtestinterview.dataclasses.Option

class OptionsRecyclerAdapter(val mfragment:Fragment) : RecyclerView.Adapter<OptionsRecyclerAdapter.MyHolder>() {

    var alldatalist=ArrayList<Option>()
    var selectedoption:Option?=null
    constructor(fment:Fragment,alllist:ArrayList<Option>):this(fment){
        this.alldatalist=alllist

    }
    inner class MyHolder(mitem:View): RecyclerView.ViewHolder(mitem) {
        lateinit var mbinding:OptionRowItemBinding
        constructor(mr:OptionRowItemBinding):this(mr.root)
        {
            this.mbinding=mr
        }
        fun bind (itemobj:Option,mcontext:Context){
            mbinding.setVariable(BR.option,itemobj)
            mbinding.executePendingBindings()
            var resourceID = mcontext.resources.getIdentifier(
                itemobj.icon!!.replace("-","").replace(" ","").lowercase(), "drawable", mcontext.packageName
            )
            Log.d("dharmesh",""+resourceID + " " +itemobj.name.replace("-","").lowercase())
            mbinding.imgitem.setImageResource(resourceID)


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var rowbinding: OptionRowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mfragment.context),
            R.layout.option_row_item, parent, false
        )
        return MyHolder(rowbinding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(alldatalist.get(position), mfragment.requireContext())

        holder.itemView.setOnClickListener {

            if(alldatalist.get(position).excludedlist==null)
                alldatalist.get(position).excludedlist=ArrayList<Option>()
            (mfragment as FacilitiesFragment).setExcludedList(alldatalist.get(position).excludedlist,alldatalist.get(position))

        }
        Log.d("dharmesh",""+(mfragment as FacilitiesFragment).myviewmodel.excludedlist)
        var matchedids=mfragment.selectedoption?.filter { ij->ij.id.equals(alldatalist.get(position).id) }
                                        ?.distinctBy { ik->ik.id }
        if(matchedids!=null && matchedids.size>0){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mfragment.requireContext(),R.color.selectedcolor))
            holder.mbinding.txtoption.setTextColor(ContextCompat.getColor(mfragment.requireContext(),R.color.graydisabled))
        }
        else {
            var foundinexcluded=(mfragment as FacilitiesFragment).excludedlist.filter { it->it.id.equals(alldatalist.get(position).id) }
            if (foundinexcluded.size>0)
            {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        mfragment.requireContext(),
                        R.color.gray
                    )
                )
                holder.mbinding.txtoption.setTextColor(
                    ContextCompat.getColor(
                        mfragment.requireContext(),
                        R.color.graydisabled
                    )
                )
                holder.itemView.isEnabled = false
            } else {
                holder.itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        mfragment.requireContext(),
                        R.color.graylight
                    )
                )
                holder.mbinding.txtoption.setTextColor(
                    ContextCompat.getColor(
                        mfragment.requireContext(),
                        R.color.black
                    )
                )
                holder.itemView.isEnabled = true
            }
        }



    }

    override fun getItemCount(): Int {
        return alldatalist.size
    }

}