package com.fung.richtextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.fung.entity.SpecialSymbol;

import java.util.List;

/**
 *  author : fzy
 *  date   : 2021/8/7 9:00
 *  desc   : 富文本控件
 */

public class RichTextView extends AppCompatTextView {

    private Rect mRect;
    private Paint mPaint;
    private int mColor;
    private float density;
    private float mStrokeWidth;
    private List<SpecialSymbol> specialSymbolList;

    public void setSpecialSymbolList(List<SpecialSymbol> specialSymbolList) {
        this.specialSymbolList = specialSymbolList;
        handleTextSpan(specialSymbolList);
        invalidate();
    }

    public List<SpecialSymbol> getSpecialSymbolList() {
        return specialSymbolList;
    }

    public RichTextView(Context context) {
        this(context, null, 0);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取屏幕密度
        density = context.getResources().getDisplayMetrics().density;
        //获取自定义属性
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStrokeWidth(3f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            //得到TextView显示有多少行
            int count = getLineCount();
            //得到TextView的布局
            final Layout layout = getLayout();
            float x_start, x_stop, x_diff;
            int firstCharInLine = 0, lastCharInLine = 0;
            if (specialSymbolList != null && specialSymbolList.size() > 0) {
                for (int j = 0; j < specialSymbolList.size(); j++) {
                    SpecialSymbol specialSymbol = specialSymbolList.get(j);
                    int startLine = layout.getLineForOffset(specialSymbol.getStart());
                    int endLine = layout.getLineForOffset(specialSymbol.getEnd());
                    if (specialSymbol.getStyle().equals("UNDERLINE")) {
                        for (int i = startLine; i <= endLine; i++) {
                            //getLineBounds得到这一行的外包矩形,
                            // 这个字符的顶部Y坐标就是rect的top 底部Y坐标就是rect的bottom
                            int baseline = getLineBounds(i, mRect);
                            if (startLine == endLine) {
                                firstCharInLine = specialSymbol.getStart();
                                lastCharInLine = specialSymbol.getEnd();
                            } else if (i == startLine) {
                                firstCharInLine = specialSymbol.getStart();
                                lastCharInLine = layout.getLineEnd(i) - 1;
                            } else if (i == endLine) {
                                firstCharInLine = layout.getLineStart(i);
                                lastCharInLine = specialSymbol.getEnd();
                            } else {
                                firstCharInLine = layout.getLineStart(i);
                                lastCharInLine = layout.getLineEnd(i) - 1;
                            }
                            //Log.e("UnderLineTextView", "第" + i + "行，first: " + firstCharInLine + ",last: " + lastCharInLine);
                            //要得到这个字符的左边X坐标 用layout.getPrimaryHorizontal
                            //得到字符的右边X坐标用layout.getSecondaryHorizontal
                            x_start = layout.getPrimaryHorizontal(firstCharInLine);
                            x_diff = layout.getPrimaryHorizontal(firstCharInLine + 1) - x_start;
                            if (x_diff < 0) {
                                x_stop = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
                            } else {
                                x_stop = layout.getPrimaryHorizontal(lastCharInLine) + x_diff;
                            }
                            //                           x_stop = layout.getPrimaryHorizontal(lastCharInLine);
                            if (i == count - 1) {
                                if (getPaddingTop() == 0) {//如果textview 没有设置padding，一行的话，划线会紧挨着textview文字底部，给他设置默认padding 上下都是8
                                    setPadding(0, 8, 0, 0);
                                }
                                if (getPaddingBottom() == 0) {
                                    setPadding(0, getPaddingTop(), 0, 8);
                                }
                                canvas.drawLine(x_start + getPaddingLeft(),
                                        layout.getLineBottom(i) + getPaddingBottom() / 2 + getPaddingTop(),
                                        x_stop + getPaddingRight(),
                                        layout.getLineBottom(i) + getPaddingBottom() / 2 + getPaddingTop(), mPaint);
                            } else {
                                canvas.drawLine(x_start + getPaddingLeft(),
                                        layout.getLineBottom(i) - getLineSpacingExtra() / 2 + getPaddingTop(),
                                        x_stop + getPaddingRight(),
                                        layout.getLineBottom(i) - getLineSpacingExtra() / 2 + getPaddingTop(), mPaint);
                            }
                        }
                    } else if (specialSymbol.getStyle().equals("DOT")) {
                        for (int i = startLine; i <= endLine; i++) {
                            //getLineBounds得到这一行的外包矩形,
                            // 这个字符的顶部Y坐标就是rect的top 底部Y坐标就是rect的bottom
                            int baseline = getLineBounds(i, mRect);
                            if (startLine == endLine) {
                                firstCharInLine = specialSymbol.getStart();
                                lastCharInLine = specialSymbol.getEnd();
                            } else if (i == startLine) {
                                firstCharInLine = specialSymbol.getStart();
                                lastCharInLine = layout.getLineEnd(i) - 1;
                            } else if (i == endLine) {
                                firstCharInLine = layout.getLineStart(i);
                                lastCharInLine = specialSymbol.getEnd();
                            } else {
                                firstCharInLine = layout.getLineStart(i);
                                lastCharInLine = layout.getLineEnd(i) - 1;
                            }
                            int start = layout.getLineStart(i);
                            int fontWidth = (int) (layout.getLineWidth(i) / (layout.getLineEnd(i) - start));//textview总宽度/总字符个数=每一个字符的宽度
                            for (int k = firstCharInLine; k <= lastCharInLine; k++) {
                                x_start = layout.getPrimaryHorizontal(k);
                                float x = x_start + fontWidth / 2;
                                float y = 0;
                                if (i == count - 1) {
                                    if (getPaddingTop() == 0) {//如果textview 没有设置padding，一行的话，划线会紧挨着textview文字底部，给他设置默认padding 上下都是8
                                        setPadding(0, 8, 0, 0);
                                    }
                                    if (getPaddingBottom() == 0) {
                                        setPadding(0, getPaddingTop(), 0, 8);
                                    }
                                    y = layout.getLineBottom(i) + getPaddingBottom() / 2 + getPaddingTop();
                                } else {
                                    y = layout.getLineBottom(i) - getLineSpacingExtra() / 2 + getPaddingTop();
                                }
                                canvas.drawCircle(x + getPaddingLeft(), y, 3f, mPaint);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDraw(canvas);
    }


    private void handleTextSpan(List<SpecialSymbol> specialSymbolList) {
        try {
            if (specialSymbolList != null && specialSymbolList.size() > 0) {
                String text = getText().toString();
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
                for (int j = 0; j < specialSymbolList.size(); j++) {
                    SpecialSymbol specialSymbol = specialSymbolList.get(j);
                    if (specialSymbol.getStyle().equals("STRONG")) {
                        StyleSpan ab = new StyleSpan(Typeface.BOLD);
                        stringBuilder.setSpan(ab, specialSymbol.getStart(), specialSymbol.getEnd() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else if (specialSymbol.getStyle().equals("BLUE")) {
                        ForegroundColorSpan ab = new ForegroundColorSpan((Color.BLUE));
                        stringBuilder.setSpan(ab, specialSymbol.getStart(), specialSymbol.getEnd() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    } else {
                        StyleSpan ab = new StyleSpan(Typeface.NORMAL);
                        stringBuilder.setSpan(ab, specialSymbol.getStart(), specialSymbol.getEnd() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
                setText(stringBuilder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

