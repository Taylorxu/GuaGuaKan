package www.xuzhiguang.com.guaguakan

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.MotionEvent
import android.widget.ImageView


class GuaGuaLeView : View {
    var rnd: Random? = null
    var paint: Paint? = null
    var clearPaint: Paint? = null
    val PRIZE = arrayListOf("一套房子", "一台游戏机", "一个大美女")
    /**涂抹线条的粗细*/
    private val FINGER = 50F
    /**缓冲区*/
    private var bmpBuffer: Bitmap? = null
    /**缓冲画布区 */
    private var cvsBuffer: Canvas? = null
    private var curX = 0F
    private var curY = 0F
    var wp=0
    var hp=0


    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        rnd = Random()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.textSize = 100F
        paint?.color = Color.WHITE
        clearPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        clearPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        clearPaint?.strokeJoin = Paint.Join.ROUND
        clearPaint?.strokeCap = Paint.Cap.ROUND
        clearPaint?.strokeWidth = FINGER

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        wp=w
        hp=h
        if (bmpBuffer == null) {
            bmpBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            cvsBuffer = Canvas(bmpBuffer)
            cvsBuffer!!.drawColor(Color.parseColor("#FF808080"))

            drawBackground()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bmpBuffer, 0F, 0F, paint)
    }

    //绘制背景,
    fun drawBackground() {

//        val bmpBackground = BitmapFactory.decodeResource(resources, R.mipmap.zhongjiang)//图片不合适
//        val bmpBackgroundMutable = bmpBackground.copy(Bitmap.Config.ARGB_8888,true)
        val bmpBackgroundMutable = Bitmap.createBitmap(wp, hp, Bitmap.Config.ARGB_8888)
        val cvsBackground = Canvas(bmpBackgroundMutable)
        val text = PRIZE[getPrizeIndex()]
        val rect = Rect()
        paint?.getTextBounds(text, 0, text.length, rect)
        val x = (bmpBackgroundMutable.width - rect.width()) / 2
        val y = (bmpBackgroundMutable.height - rect.height()) / 2
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, paint)
        paint?.setShadowLayer(10F, 0F, 0F, Color.GREEN)
        cvsBackground.drawText(text, x.toFloat(), y.toFloat(), paint)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.background = BitmapDrawable(
                    resources, bmpBackgroundMutable)
        } else {
            this.setBackgroundDrawable(
                    BitmapDrawable(bmpBackgroundMutable))
        }
    }

    //获取随机中奖信息
    private fun getPrizeIndex(): Int {
        return rnd?.nextInt(PRIZE.size)!!
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var x = event?.x
        var y = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                curX = x!!
                curY = y!!
            }
            MotionEvent.ACTION_MOVE -> {
                cvsBuffer?.drawLine(curX, curY, x!!, y!!, clearPaint)
                invalidate()
                curX = x!!
                curY = y!!

            }
            MotionEvent.ACTION_UP -> {
                invalidate()
            }
        }
        return true
    }
}