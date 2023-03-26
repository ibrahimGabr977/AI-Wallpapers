package com.hope.igb.aiwallpapers.screen.display.util

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import kotlin.math.sqrt

//only works perfectly if imageview match parent in height and width

class LimitedZoomController : View.OnTouchListener {

    private var matrix = Matrix()
    private var savedMatrix = Matrix()
    private val none = 0
    private val drag = 1
    private val zoom = 2
    private var mode = none
    private var start = PointF()
    private var mid = PointF()
    private var oldDist = 1f
    private val maxScale = 3.0f
    private val minScale = 1.0f
    private val f = FloatArray(9)



    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        val zoomingImageView = view as ImageView


        zoomingImageView.scaleType = ImageView.ScaleType.MATRIX

        val scale: Float


        when (motionEvent.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                matrix.set(zoomingImageView.imageMatrix)
                savedMatrix.set(matrix)
                start[motionEvent.x] = motionEvent.y
                mode = drag
            }
            MotionEvent.ACTION_POINTER_UP -> mode = none
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(motionEvent)
                if (oldDist > 5f) {
                    savedMatrix.set(matrix)
                    midPoint(mid, motionEvent)
                    mode = zoom
                }
            }
            MotionEvent.ACTION_MOVE -> if (mode == drag) {

                matrix.set(savedMatrix)
                matrix.getValues(f)
                if (f[Matrix.MSCALE_Y] != minScale)
                    matrix.postTranslate(motionEvent.x - start.x, motionEvent.y - start.y)



            } else if (mode == zoom) {


                val newDist = spacing(motionEvent)
                if (newDist > 5f) {

                    matrix.set(savedMatrix)
                    scale = newDist / oldDist
                    matrix.postScale(scale,scale, mid.x, mid.y)
                    matrix.getValues(f)

                    if(f[Matrix.MSCALE_X] <= minScale) {
                        matrix.postScale((minScale)/f[Matrix.MSCALE_X], (minScale)/f[Matrix.MSCALE_Y], mid.x, mid.y)
                    } else if(f[Matrix.MSCALE_X] >= maxScale) {
                        matrix.postScale((maxScale)/f[Matrix.MSCALE_X], (maxScale)/f[Matrix.MSCALE_Y], mid.x, mid.y)
                    }

                }





            }
            else -> {}
        }
        if (f[Matrix.MSCALE_Y] != minScale)
        limitDrag(matrix, zoomingImageView,
            zoomingImageView.drawable.bounds.width(),
            zoomingImageView.measuredHeight)

        zoomingImageView.imageMatrix = matrix
        return true
    }


    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }


    private fun limitDrag(m: Matrix, view: ImageView, imageWidth: Int, imageHeight: Int) {
        val values = FloatArray(9)
        m.getValues(values)
        val orig = floatArrayOf(0f, 0f, imageWidth.toFloat(), imageHeight.toFloat())
        val trans = FloatArray(4)
        m.mapPoints(trans, orig)
        val transLeft = trans[0]
        val transTop = trans[1]
        val transRight = trans[2]
        val transBottom = trans[3]
        val transWidth = transRight - transLeft
        val transHeight = transBottom - transTop
        var xOffset = 0f
        if (transWidth > view.width) {
            if (transLeft > 0) {
                xOffset = -transLeft
            } else if (transRight < view.width) {
                xOffset = view.width - transRight
            }
        } else {
            if (transLeft < 0) {
                xOffset = -transLeft
            } else if (transRight > view.width) {
                xOffset = -(transRight - view.width)
            }
        }
        var yOffset = 0f
        if (transHeight > view.height) {
            if (transTop > 0) {
                yOffset = -transTop
            } else if (transBottom < view.height) {
                yOffset = view.height - transBottom
            }
        } else {
            if (transTop < 0) {
                yOffset = -transTop
            } else if (transBottom > view.height) {
                yOffset = -(transBottom - view.height)
            }
        }
        val transX = values[Matrix.MTRANS_X]
        val transY = values[Matrix.MTRANS_Y]
        values[Matrix.MTRANS_X] = transX + xOffset
        values[Matrix.MTRANS_Y] = transY + yOffset
        m.setValues(values)
    }




}