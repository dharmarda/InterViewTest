package com.example.realmtestinterview.dataclasses

import com.google.gson.annotations.SerializedName
import io.realm.annotations.RealmClass

data class GetAllFeaturesResponse(

    var facilities: List<Facility>,
    var exclusions: List<List<Exclusion>>,
)

data class Facility(
    @SerializedName("facility_id")
    val facilityId: String,
    val name: String,
    var options: ArrayList<Option>,
    var excludeids:ArrayList<String> =ArrayList<String>()
)

data class Option(
    val name: String,
    val icon: String,
    val id: String,
    var isselected:Boolean=false,
    var excludedlist: ArrayList<Option> = ArrayList<Option>(),
    var mfacilityid:String="-1"

)

data class Exclusion(
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("options_id")
    val optionsId: String,
)
data class FacilityOptionClass(val f1id:String,val op1id:String,val f2id:String,val op2id :String)