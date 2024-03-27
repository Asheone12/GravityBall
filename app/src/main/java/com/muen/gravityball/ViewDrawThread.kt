package com.muen.gravityball

import android.graphics.Canvas
import java.lang.Exception

class ViewDrawThread(val gameView: GameView) : Thread() {
    val flag = true
    var pauseFlag = false
    val sleepSpan = 12L
    val surfaceHolder = gameView.holder

    override fun run() {
        super.run()
        var canvas:Canvas?
        while (flag){
            canvas = null
            if(!pauseFlag){
                try {
                    //锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    canvas = surfaceHolder.lockCanvas(null)
                    synchronized(surfaceHolder){
                        gameView.onMyDraw(canvas)
                    }
                }finally {
                    if(canvas != null){
                        //释放锁
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }
                }
            }

            try {
                sleep(sleepSpan) //睡眠指定时间
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }
}