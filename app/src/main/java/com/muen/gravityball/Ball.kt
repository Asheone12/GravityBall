package com.muen.gravityball

data class Ball(
    var size:Float,      //小球的碰撞大小
    var dLength:Float,  //小球的运动速度系数
    var x:Float,        //当前小球的x坐标
    var y:Float,        //当前小球的Y坐标
    var dx:Float,       //当前小球速度的x分量
    var dy:Float        //当前小球速度的y分量
)