package com.example.realmtestinterview.dataclasses

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class FacilityOptionId :RealmModel{
    @PrimaryKey
    var opid=""
    @Required
    var optionid:String=""
    @Required
    var facilityid:String=""
    @Required
    var name:String=""
    @Required
    var icon=""
    @Required
    var facilityname=""
}