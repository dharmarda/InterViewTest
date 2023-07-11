package com.example.realmtestinterview.dataclasses

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class ExclussionList :RealmModel{
    @PrimaryKey
    var exid=""
    @Required
    var f1id:String=""
    @Required
    var op1id:String=""
    @Required
    var f2id:String=""
    @Required
    var op2id=""

}