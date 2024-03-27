package com.muen.gravityball

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    var paint: Paint
    var tableBm: Bitmap
    var ballBm: Bitmap
    var ballGoThread:BallGoThread
    var viewDrawThread: ViewDrawThread
    var ball: Ball = Ball(20f,2.5f,100f,80f,0f,0f)
    var rect1 = Rect()
    var rect2 = Rect()

    init {
        this.holder.addCallback(this)   //设置生命周期回调接口的实现
        tableBm = BitmapFactory.decodeResource(context.resources,R.drawable.youxiang)
        ballBm = BitmapFactory.decodeResource(context.resources,R.drawable.ball)
        ball.size = ballBm.width.toFloat()
        paint = Paint()
        paint.isAntiAlias = true    //打开抗锯齿

        ballGoThread = BallGoThread(this)
        viewDrawThread = ViewDrawThread(this)

    }

    fun onMyDraw(canvas: Canvas?) {
        //贴底纹
        /*for(i in 0 until 9){
            for(j in 0 until 9){
                canvas?.drawBitmap(tableBm,tableBm.width.toFloat() * i,tableBm.height.toFloat() *j,paint)
            }
        }*/
        //canvas?.drawBitmap(tableBm,0f,0f,paint)
        canvas?.drawColor(resources.getColor(R.color.white),PorterDuff.Mode.LIGHTEN)
        //paint.color = android.graphics.Color.RED
        //canvas?.drawLine(0f,height.toFloat()/2 - 100,width.toFloat(),height.toFloat()/2 - 100,paint)
        paint.color = android.graphics.Color.GREEN
        rect1 = Rect(0,0,width,height/2 - 200)
        rect2 = Rect(0,height/2 + 200,width,height)
        canvas?.drawRect(rect1,paint)
        canvas?.drawRect(rect2,paint)
        //贴球
        canvas?.drawBitmap(ballBm, ball.x, ball.y,paint)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val canvas = holder.lockCanvas()    //获取画布
        ball.y = height.toFloat()/2
        try {
            synchronized(holder){
                onMyDraw(canvas)
            }
        }catch(e:Exception){
            e.printStackTrace()
        }finally {
            if(canvas!=null){
                holder.unlockCanvasAndPost(canvas)
            }
        }

        //启动球定时根据重力移动的线程
        ballGoThread.start()
        //启动定时重新绘制画面的线程
        viewDrawThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }
}