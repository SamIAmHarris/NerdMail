package com.bignerdranch.android.nerdmail.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.bignerdranch.android.nerdmail.R;
import com.bignerdranch.android.nerdmail.model.DataManager;
import com.bignerdranch.android.nerdmailservice.Email;

/**
 * Created by SamMyxer on 4/7/16.
 */
public class EmailListItemView extends View  implements View.OnTouchListener {
    private static final String TAG = "EmailListItemView";

    //Are in DP units
    private static final int PADDING_SIZE = 16;
    private static final int BODY_PADDING_SIZE = 4;
    private static final int LARGE_TEXT_SIZE = 16;
    private static final int SMALL_TEXT_SIZE = 14;
    private static final int STAR_SIZE = 32;
    private static final int DIVIDER_SIZE = 1;

    //variables for when we convert to pixels
    float screenDensity;
    float paddingSize;
    float starPixelSize;
    float dividerSize;
    float largeTextSize;
    float smallTextSize;

    float bodyPaddingSize;

    private Paint backgroundPaint;
    private Paint dividerPaint;
    private Paint starPaint;
    private TextPaint senderAddressTextPaint;
    private TextPaint subjectTextPaint;
    private TextPaint bodyTextPaint;

    private Bitmap importantStar;
    private Bitmap unimportantStar;

    private Email email;

    float starTop;
    float starLeft;

    public EmailListItemView(Context context) {
        //Call this so we call our other constructor that handles all the setup
        //Call to super would call normal view code when we are handling it
        //ourselves
        this(context, null);
    }

    public EmailListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);

        screenDensity = context.getResources().getDisplayMetrics().density;

        starPixelSize = Math.round(STAR_SIZE * screenDensity);
        dividerSize = Math.round(DIVIDER_SIZE * screenDensity);
        paddingSize = Math.round(PADDING_SIZE * screenDensity);

        //scale text size based on screen density and accessibility settings
        Configuration configuration = context.getResources().getConfiguration();
        float textScale = configuration.fontScale * screenDensity;
        largeTextSize = Math.round(LARGE_TEXT_SIZE * textScale);
        smallTextSize = Math.round(SMALL_TEXT_SIZE * textScale);

        bodyPaddingSize = Math.round(BODY_PADDING_SIZE * screenDensity);

        setupPaints();
        setupStarBitmaps();
    }

    private void setupStarBitmaps() {
        int bitmapSize = (int) starPixelSize;

        Bitmap importantBitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_important);
        importantStar = Bitmap
                .createScaledBitmap(importantBitmap, bitmapSize, bitmapSize, false);

        Bitmap unimportantBitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_unimportant);
        unimportantStar = Bitmap
                .createScaledBitmap(unimportantBitmap, bitmapSize, bitmapSize, false);
    }

    private void setupPaints() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.white));

        dividerPaint = new Paint();
        dividerPaint.setColor(getResources().getColor(R.color.divider_color));

        starPaint = new Paint();
        int starTint = getResources().getColor(R.color.star_tint);
        ColorFilter colorFilter = new LightingColorFilter(starTint, 1);
        starPaint.setColorFilter(colorFilter);

        senderAddressTextPaint = new TextPaint();
        senderAddressTextPaint.setTextSize(largeTextSize);
        senderAddressTextPaint.setTextAlign(Paint.Align.LEFT);
        senderAddressTextPaint.setColor(getResources().getColor(R.color.black));
        senderAddressTextPaint.setAntiAlias(true);

        subjectTextPaint = new TextPaint();
        subjectTextPaint.setTextSize(smallTextSize);
        subjectTextPaint.setTextAlign(Paint.Align.LEFT);
        subjectTextPaint.setColor(getResources().getColor(R.color.black));
        subjectTextPaint.setAntiAlias(true);

        bodyTextPaint = new TextPaint();
        bodyTextPaint.setTextSize(smallTextSize);
        bodyTextPaint.setTextAlign(Paint.Align.LEFT);
        bodyTextPaint.setColor(getResources().getColor(R.color.black));
        bodyTextPaint.setAntiAlias(true);
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else {
            width = calculateWidth();
        }

        if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            height = heightSize;
        } else {
            height = calculateHeight();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = canvas.getHeight();
        int width = canvas.getWidth();

        //draw paint to clear canvas
        canvas.drawPaint(backgroundPaint);

        //Draw the divider across the bottom of the canvas
        float dividerY = height - (dividerSize/2);
        canvas.drawLine(0, dividerY, width, dividerY, dividerPaint);

        //Draw the sender address
        Paint.FontMetrics fm = senderAddressTextPaint.getFontMetrics();
        float senderX = paddingSize;
        float senderTop = (float) Math.ceil(Math.abs(fm.top));
        float senderBottom = (float) Math.ceil(Math.abs(fm.bottom));
        float senderBaseline = paddingSize + senderTop;
        float senderY = senderBaseline + senderBottom;

        canvas.drawText(email.getSenderAddress(), senderX, senderBaseline, senderAddressTextPaint);

        //Draw the subject
        Paint.FontMetrics subjectFm = subjectTextPaint.getFontMetrics();
        float subjectX = paddingSize;
        float subjectTop = (float) Math.ceil(Math.abs(subjectFm.top));
        float subjectBottom = (float) Math.ceil(Math.abs(subjectFm.bottom));
        float subjectBaseline = senderY + subjectTop;
        float subjectY = subjectBaseline + subjectBottom;

        canvas.drawText(email.getSubject(), subjectX, subjectBaseline,
                subjectTextPaint);

        //Draw the body
        Paint.FontMetrics bodyFm = bodyTextPaint.getFontMetrics();
        float bodyX = paddingSize;
        float bodyTop = (float) Math.ceil(Math.abs(bodyFm.top));
        float bodyBottom = (float) Math.ceil(Math.abs(bodyFm.bottom));
        //First line of the body test
        float bodyFirstBaseline = subjectY + bodyTop;
        float bodyFirstY = bodyFirstBaseline + bodyBottom;
        //Second line of the body text
        float extraBodySpacing = 4 * screenDensity;
        float bodySecondBaseline = bodyFirstY + bodyTop + extraBodySpacing;

        float bodyWidth = getWidth() - (2 * paddingSize)
                - starPixelSize - paddingSize;

        String[] bodyLines = new String[2];
        if(bodyTextPaint.measureText(email.getBody()) < bodyWidth) {
            bodyLines[0] = email.getBody();
        } else {
            int currentLine = 0;
            String[] bodyWords = email.getBody().split(" ");
            int numberOfWords = bodyWords.length;
            int currentWord = 0;
            String bodyLine = "";
            String checkingLine = bodyWords[currentWord];
            while((currentWord < numberOfWords - 1) && currentLine < 2) {
                if(bodyTextPaint.measureText(checkingLine) < bodyWidth) {
                    bodyLine = checkingLine;
                    currentWord++;
                    checkingLine = bodyLine + " " + bodyWords[currentWord];
                } else {
                    bodyLines[currentLine] = bodyLine;
                    currentLine++;
                    bodyLine = "";
                    checkingLine = bodyWords[currentWord];
                }
            }
            if(currentLine < 2) {
                //add second line since we ran out of words for the second line
                bodyLines[currentLine] = bodyLine;
            }
            if(currentWord < numberOfWords -1) {
                //ellipsize second sentence
                String secondSentence = bodyLines[1];
                secondSentence = secondSentence
                        .substring(0, secondSentence.length() - 4);
                secondSentence += "...";
                bodyLines[1] = secondSentence;
            }
            if(bodyLines[0] != null) {
                canvas.drawText(bodyLines[0], bodyX, bodyFirstBaseline, bodyTextPaint);
            }
            if(bodyLines[1] != null) {
                canvas.drawText(bodyLines[1], bodyX, bodySecondBaseline, bodyTextPaint);
            }
        }

        //Draw the star
        starLeft = getWidth() - paddingSize - starPixelSize;
        float starHeight = getHeight() - senderY - dividerSize;
        starTop = (starHeight/2) + senderY - (starPixelSize/2);

        if(email.isImportant()) {
            canvas.drawBitmap(importantStar, starLeft, starTop, starPaint);
        } else {
            canvas.drawBitmap(unimportantStar, starLeft, starTop, starPaint);
        }
    }

    private int calculateHeight() {
        int layoutPadding = getPaddingTop() + getPaddingBottom();

        Paint.FontMetrics senderFm = senderAddressTextPaint.getFontMetrics();
        float senderHeight = getFontHeight(senderFm);
        Paint.FontMetrics subjectFm = subjectTextPaint.getFontMetrics();
        float subjectHeight = getFontHeight(subjectFm);
        Paint.FontMetrics bodyFm = bodyTextPaint.getFontMetrics();
        float bodyHeight = getFontHeight(bodyFm);

        float totalHeight = layoutPadding + bodyPaddingSize + senderHeight
                + subjectHeight + (bodyHeight *2) + bodyPaddingSize +
                paddingSize + dividerSize;

        return (int) totalHeight;
    }

    private float getFontHeight(Paint.FontMetrics metrics) {
        return (float) (Math.ceil(Math.abs(metrics.top)) +
                Math.ceil(Math.abs(metrics.bottom)));
    }

    private int calculateWidth() {
        Point size = new Point();
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(size);
        //use window width if unspecified
        return size.x;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(isStarClick(event)) {
                    email.setImportant(!email.isImportant());
                    DataManager dataManager = DataManager.get(getContext());
                    dataManager.updateEmail(email);
                    invalidate();
                    return true;
                }
                Log.d(TAG, "Star was not clicked");
                return true;
        }
        return false;
    }

    private boolean isStarClick(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float starRight = starLeft + starPixelSize;
        float starBottom = starTop + starPixelSize;
        boolean isXInStarRange = (x >= starLeft) && (x <= starRight);
        boolean isYInStarRange = (y >= starTop) && (y <= starBottom);
        return isXInStarRange && isYInStarRange;
    }
}
