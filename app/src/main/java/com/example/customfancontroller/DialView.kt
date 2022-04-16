package com.example.customfancontroller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DialView: View {

    private var SELECTION_COUNT = 4 // Total number of selections.

    private var mWidth // Custom view width.
            = 0f
    private var mHeight // Custom view height.
            = 0f
    private var mTextPaint // For text in the view.
            : Paint? = null
    private var mDialPaint // For dial circle in the view.
            : Paint? = null
    private var mRadius // Radius of the circle.
            = 0f
    private var mActiveSelection // The active selection.
            = 0

    // String buffer for dial labels and float for ComputeXY result.
    private val mTempLabel = StringBuffer(8)
    private val mTempResult = FloatArray(2)


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context:Context, attrs: AttributeSet): super(context , attrs){
        init()
    }

    constructor(context:Context, attrs:AttributeSet, defStyleAttr:Int):
            super(context , attrs , defStyleAttr){
        init()
    }

    fun init(){

//        This is to improve performance, because onDraw() is called frequently

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint!!.setColor(Color.BLACK)
        mTextPaint!!.setStyle(Paint.Style.FILL_AND_STROKE)
        mTextPaint!!.setTextAlign(Paint.Align.CENTER);
        mTextPaint!!.setTextSize(40f)

        mDialPaint =  Paint(Paint.ANTI_ALIAS_FLAG)
        mDialPaint!!.setColor(Color.GRAY)
        // Initialize current selection.
        mActiveSelection = 0
        // TODO: Set up onClick listener for this view.
        setOnClickListener { // Rotate selection to the next valid choice.
            mActiveSelection = (mActiveSelection + 1) % SELECTION_COUNT
            // Set dial background color to green if selection is >= 1.
            if (mActiveSelection >= 1) {
                mDialPaint!!.color = Color.GREEN
            } else {
                mDialPaint!!.color = Color.GRAY
            }
            // Redraw the view.
            invalidate()
        }
    }


//    The onSizeChanged() method is called when the layout is
//    inflated and when the view has changed.
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Calculate the radius from the width and height.
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = (Math.min(mWidth, mHeight) / 2 * 0.8).toFloat()

    }

    private fun computeXYForPosition(pos: Int, radius: Float): FloatArray? {
        val result = mTempResult
        val startAngle = Math.PI * (9 / 8.0) // Angles are in radians.
        val angle = startAngle + pos * (Math.PI / 4)
        result[0] = (radius * Math.cos(angle)).toFloat() + mWidth / 2
        result[1] = (radius * Math.sin(angle)).toFloat() + mHeight / 2
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Draw the dial.
        canvas!!.drawCircle(mWidth / 2, mHeight / 2, mRadius, mDialPaint!!)
        // Draw the text labels.

        val labelRadius = mRadius + 20
        val label = mTempLabel
        for (i in 0 until SELECTION_COUNT) {
            val xyData = computeXYForPosition(i, labelRadius)
            val x = xyData!![0]
            val y = xyData!![1]
            label.setLength(0)
            label.append(i)
            canvas!!.drawText(label, 0, label.length, x, y, mTextPaint!!)
        }
        // Draw the indicator mark.
        // Draw the indicator mark.
        val markerRadius = mRadius - 35
        val xyData = computeXYForPosition(
            mActiveSelection,
            markerRadius
        )
        val x = xyData!![0]
        val y = xyData!![1]
        canvas!!.drawCircle(x, y, 20F, mTextPaint!!)
    }

}