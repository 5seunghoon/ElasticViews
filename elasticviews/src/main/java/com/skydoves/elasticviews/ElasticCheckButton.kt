/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 skydoves
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.skydoves.elasticviews

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

@Suppress("unused")
class ElasticCheckButton : AppCompatButton {

  var checkedAlpha = 0.7f
  var scale = 0.9f
  var duration = 500
  var isChecked = false
    set(value) {
      field = value
      updateElasticCheckButton()
    }

  private var onClickListener: OnClickListener? = null
  private var onFinishListener: ElasticFinishListener? = null

  constructor(context: Context) : super(context) {
    onCreate()
  }

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
    onCreate()
    getAttrs(attributeSet)
  }

  constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
    onCreate()
    getAttrs(attributeSet, defStyle)
  }

  private fun onCreate() {
    this.isAllCaps = false
    super.setOnClickListener {
      isChecked = !isChecked
      if (scaleX == 1f) {
        elasticAnimation(this) {
          setDuration(this@ElasticCheckButton.duration)
          setScaleX(scale)
          setScaleY(scale)
          setOnFinishListener(object : ElasticFinishListener {
            override fun onFinished() {
              invokeListeners()
            }
          })
        }.doAction()
      }
    }
  }

  private fun getAttrs(attrs: AttributeSet) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ElasticCheckButton)
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun getAttrs(attrs: AttributeSet, defStyle: Int) {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ElasticCheckButton, defStyle, 0)
    try {
      setTypeArray(typedArray)
    } finally {
      typedArray.recycle()
    }
  }

  private fun setTypeArray(typedArray: TypedArray) {
    this.scale = typedArray.getFloat(R.styleable.ElasticCheckButton_checkButton_scale, scale)
    this.duration = typedArray.getInt(R.styleable.ElasticCheckButton_checkButton_duration, duration)
    this.checkedAlpha = typedArray.getFloat(R.styleable.ElasticCheckButton_checkButton_alpha, checkedAlpha)
    this.isChecked = typedArray.getBoolean(R.styleable.ElasticCheckButton_checkButton_isChecked, isChecked)
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    updateElasticCheckButton()
  }

  private fun updateElasticCheckButton() {
    if (isChecked) {
      this.alpha = checkedAlpha
    }
  }

  override fun setOnClickListener(listener: OnClickListener?) {
    this.onClickListener = listener
  }

  fun setOnFinishListener(listener: ElasticFinishListener) {
    this.onFinishListener = listener
  }

  private fun invokeListeners() {
    alpha = when (isChecked) {
      true -> checkedAlpha
      false -> 1.0f
    }
    onClickListener?.onClick(this)
    onFinishListener?.onFinished()
  }
}
