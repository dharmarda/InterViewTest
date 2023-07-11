package com.example.realmtestinterview.dataclasses

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class FacilitiesModel : RealmModel{
    @PrimaryKey
    var mid:String=""
    @Required
    var facility_id:String=""
    @Required
    var name:String =""
}