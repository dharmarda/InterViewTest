package com.example.realmtestinterview.viemodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.realmtestinterview.apis.ApiRepository
import com.example.realmtestinterview.dataclasses.*
import io.realm.Realm
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AllAPisViewModel constructor() : ViewModel() {
    private val mainRepository=ApiRepository()
    val errorMessage = MutableLiveData<String>()
    val facilitiesList = MutableLiveData<GetAllFeaturesResponse>()
    var job: Job? = null
    private var realm: Realm = Realm.getDefaultInstance()
    val loading = MutableLiveData<Boolean>()
    val excludedlist=MutableLiveData<ArrayList<Option>>()

    fun getAllFacilities() {

        val mdate=Calendar.getInstance().time
        val dtstring=SimpleDateFormat("dd-MMM-yyyy").format(mdate)
        if(CheckDataIsSynced(dtstring))
        {
            loading.value = false
            var m=GetAllData()
            return
        }

        job = CoroutineScope(Dispatchers.IO).launch {

            val response = mainRepository.getAllMovies()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.facilities?.forEach { it->
                        AddFacility(it.facilityId,it.name)
                        it.options.forEach { ij->
                            AddFacilityOptions(it.facilityId,ij.name,ij.id,ij.icon,it.name)
                        }
                    }
                    response.body()?.exclusions?.forEach { mlist->
                        var f1id=mlist[0].facilityId
                        var op1id=mlist[0].optionsId

                        var f2id=mlist[1].facilityId
                        var op2id=mlist[1].optionsId
                        AddExclussion(f1id,op1id,f2id,op2id)
                    }
                    AddSyncTime(dtstring)
                    loading.value = false
                    var m=GetAllData()
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }
    fun AddSyncTime( syncdate: String) {
        realm.executeTransaction { r: Realm ->
            try{
                val facility = r.createObject(LastSyncTime::class.java, UUID.randomUUID().toString() )
                facility.syncdate =syncdate
                realm.insertOrUpdate(facility)
            }catch (ex:Exception){

            }
        }
    }
    fun CheckDataIsSynced(mdate:String):Boolean{
        val alldates = realm.where(LastSyncTime::class.java)
            .equalTo("syncdate",mdate )
            .findFirst()
        return alldates != null
    }
    fun AddFacility(facilityid: String, name: String) {
        realm.executeTransaction { r: Realm ->
            try{
                val facility = r.createObject(FacilitiesModel::class.java, facilityid)
                facility.name = name
                facility.facility_id = facilityid
                realm.insertOrUpdate(facility)
            }catch (ex:Exception){
                UpdateFacility(facilityid,name)
            }
        }
    }
    fun UpdateFacility(facilityid: String, name: String) {
        try{
        realm.executeTransaction { r: Realm ->

                val facility = realm.where(FacilitiesModel::class.java)
                    .equalTo("mid", facilityid)
                    .findFirst()
                facility?.name = name
                facility?.facility_id = facilityid
                realm.insertOrUpdate(facility)

        }
        }catch (ess:Exception){
            ess.printStackTrace()
        }
    }
    fun AddFacilityOptions(facilityid: String, name: String,optionid:String,icon:String,facilityname:String) {
        realm.executeTransaction { r: Realm ->

            try {
                 val foption= r.createObject(FacilityOptionId::class.java, optionid)
                foption.name = name
                foption.facilityid = facilityid
                foption.optionid=optionid
                foption.icon=icon
                foption.facilityname=facilityname
                realm.insertOrUpdate(foption)
            }catch (myex:Exception){
                UpdateFacilityOptions(facilityid,name,optionid,icon,facilityname)

            }
        }
    }
    fun UpdateFacilityOptions(facilityid: String, name: String,optionid:String,icon:String,facilityname:String) {
        try {
        realm.executeTransaction { r: Realm ->


                val foption=realm.where(FacilityOptionId::class.java)
                    .equalTo("opid", optionid)
                    .findFirst()
                foption?.name = name
                foption?.facilityid = facilityid
                foption?.optionid=optionid
                foption?.icon=icon
                foption?.facilityname=facilityname
                realm.insertOrUpdate(foption)
            }
        }catch (myex:Exception){
            try{

            }catch (updateexception:Exception){

            }
        }
    }
    fun AddExclussion(f1id: String, op1id: String,f2id:String,op2id:String) {
        realm.executeTransaction { r: Realm ->
            val foption = r.createObject(ExclussionList::class.java, UUID.randomUUID().toString())
            foption.f1id = f1id
            foption.op1id = op1id
            foption.f2id=f2id
            foption.op2id=op2id

            realm.insertOrUpdate(foption)
        }
    }
    public fun GetAllData(): MutableLiveData<GetAllFeaturesResponse> {
        val list = GetAllFeaturesResponse(ArrayList<Facility>(),ArrayList<List<Exclusion>>())

        val allfacilities = realm.where(FacilityOptionId::class.java).findAll()
        var distinctfids=allfacilities.distinctBy { it->it.facilityid }
        var myfacilities=ArrayList<Facility>()
        distinctfids.forEach { it->
            var fobject=Facility(it.facilityid,it.facilityname,ArrayList<Option>(), ArrayList())
            var facioptions=allfacilities.filter { im->im.facilityid.equals(it.facilityid) }
            facioptions.forEach { ij->
                var option=Option(ij.name,ij.icon,ij.optionid,false, ArrayList(),ij.facilityid)
                fobject.options.add(option)
            }
            myfacilities.add(fobject)
        }
        val allexclussions = realm.where(ExclussionList::class.java).findAll()
        var myalldistinctids=allexclussions.stream().map { it->FacilityOptionClass(it.f1id,it.op1id,it.f2id,it.op2id) }.distinct()
        var exclussions=ArrayList<List<Exclusion>>()
        myalldistinctids.forEach {it ->
            var mlist=ArrayList<Exclusion>()
            val ex1=Exclusion(it.f1id,it.op1id)
            val ex2=Exclusion(it.f2id,it.op2id)
            mlist.add(ex1)
            mlist.add(ex2)
            exclussions.add(mlist)
        }

        list.facilities=myfacilities as ArrayList<Facility>
        list.exclusions=exclussions
        facilitiesList.postValue(list)
        return facilitiesList
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}