package com.muen.gravityball

import android.graphics.Rect
import android.util.Log
import java.lang.Exception

class BallGoThread(val gameView:GameView):Thread() {
    private var flag = true
    var resetX = false
    var resetY = false

    override fun run() {
        super.run()
        while(flag){
            resetX = false
            resetY = false
            //计算球的新位置
            val dx = gameView.ball.dx
            val dy = gameView.ball.dy

            gameView.ball.x = gameView.ball.x + dx
            gameView.ball.y = gameView.ball.y + dy

            //判断x方向是否碰壁，若碰壁则减速
            if(gameView.ball.x <0 || gameView.ball.x>gameView.width-gameView.ball.size){
                gameView.ball.x = gameView.ball.x - dx
                resetX = true
            }

            //判断Y方向是否碰壁，若碰壁则减速
            if(gameView.ball.y < 0 || gameView.ball.y > gameView.height - gameView.ball.size){
                gameView.ball.y = gameView.ball.y - dy
                resetY = true
            }

            collide(dx, dy,gameView.ball,gameView.rect1)
            collide(dx, dy,gameView.ball,gameView.rect2)

            try {
                sleep(15)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun collide(dx: Float, dy: Float,ball: Ball,rect: Rect) {
        if (((ball.x + ball.size) > rect.left && ball.x < rect.right
                    && ball.y < rect.bottom && (ball.y + ball.size) > rect.top)
        ) {
            if(ball.x - dx < rect.left && ball.x - dx > rect.right){
                if(!resetX){
                    ball.x = ball.x - dx
                    resetX = true
                }
            }

            if(ball.y - dy < rect.top || ball.y - dy > rect.bottom ){
                if(!resetY){
                    ball.y = ball.y - dy
                    resetY = true
                }
            }

        }

    }
}