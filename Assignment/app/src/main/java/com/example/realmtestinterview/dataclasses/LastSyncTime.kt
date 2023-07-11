package com.example.realmtestinterview.dataclasses

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class LastSyncTime : RealmModel {
    @PrimaryKey
    var id:String=""
    @Required
    var syncdate:String=""
}