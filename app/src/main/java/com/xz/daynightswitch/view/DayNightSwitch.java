package com.xz.daynightswitch.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.xz.daynightswitch.R;

public class DayNightSwitch extends CompoundButton {
    float switch_width = 0;
    float switch_height = 0;

    float padW,padH;
    boolean isRunning = false;
    private float[] bgColor = {0,0,0};
    private float[] buttonColor = {0,0,0};

    private float[] bgColorDay = {69, 164, 222};
    private float[] bgColorNight = {14, 22, 33};
    private float[] buttonColorDay = {254, 230, 0};
    private float[] buttonColorNight = {181, 213, 226};
    private float[] buttonColorNightHole = {0,116, 135, 165};

    private float[] buttonColorCloud2 = {255 ,145, 201, 236};
    private float[] buttonColorCloud1 = {255 ,246, 250, 255};
    private float[] buttonColorStar = {255 ,255, 255, 255};
    private PointF[] pStars = {
            new PointF(),
            new PointF(),
            new PointF(),
            new PointF(),
            new PointF(),
            new PointF(),
            new PointF(),
            new PointF(),
            new PointF(),
            new PointF(),
            new PointF()};

    int[] mColors1= {
            Color.argb(255, 246, 250, 255)
            ,Color.argb(255, 82, 94, 110)
    };
    int[] mColors2= {
            Color.argb(255, 82, 94, 110)
            ,Color.argb(255, 246, 250, 255)
    };

    int[] mColorsAlpha1= {
            Color.argb(255/2, 246, 250, 255)
            ,Color.argb(255/2, 82, 94, 110)
    };
    int[] mColorsAlpha2= {
            Color.argb(255/2, 82, 94, 110)
            ,Color.argb(255/2, 246, 250, 255)
    };

    int[] shadowColors= {
            Color.argb(255, 0, 0, 0)
            ,Color.argb(0, 0, 0, 0)
    };

    private int shadowColor = Color.argb(50, 0, 0, 0);

    PointF centerF = new PointF();
    PointF buttonF = new PointF();
    PointF buttonHoleF1 = new PointF();
    PointF buttonHoleF2 = new PointF();
    PointF buttonHoleF3 = new PointF();
    PointF buttonCloudBase = new PointF();
    PointF buttonCloudBase2 = new PointF();


    float buttonR;

    PointF centerLF = new PointF();
    PointF centerRF = new PointF();

    private Paint backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint buttonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint buttonWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint buttonCloudPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint buttonCloudPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint buttonStarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint buttonHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint buttonPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint buttonPaintHoleArc1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint buttonPaintHoleArc2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint buttonPaintHoleArc3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint btnShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint strokePaintArc1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint strokePaintArc2 = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint strokePaintTop = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint strokePaintBottom = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF strokeRectF ,leftRect,rightRect ,buttonRectF,buttonHoleRectF1,buttonHoleRectF2,buttonHoleRectF3;
    Handler swHandler = new Handler();
    @Nullable private OnCheckedChangeListener onCheckedChangeListener;

    public DayNightSwitch(Context context) {
        this(context,null);
    }

    public DayNightSwitch(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DayNightSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dealAttrs(context,attrs);
    }
    private void dealAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayNightSwitch);
        if (typedArray != null) {
            typedArray.recycle();
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int viewHeight = h;
        int viewWidth = w;
        if(viewWidth> viewHeight*2.1f){
            switch_height = viewHeight ;
            switch_width = viewHeight*2.1f;
        }else {
            switch_width = viewWidth ;
            switch_height = viewWidth/2.1f;
        }
        padW = (viewWidth -switch_width)/2;
        padH = (viewHeight -switch_height)/2;
        float centerX = (getPaddingLeft() +viewWidth - getPaddingRight())/2;
        float centerY = (getPaddingTop() +viewHeight - getPaddingBottom())/2;

        centerF = new PointF(centerX,centerY);

        Log.i("switch","switch_height:"+switch_height
                +",switch_width:"+switch_width
                +",switch_width:"+switch_width
                +",padW:"+padW
                +",padH:"+padH
                +",centerX:"+centerX
                +",centerY:"+centerY

        );
        centerLF = new PointF(centerX - switch_width/2 + switch_height/2,centerY);
        centerRF = new PointF(centerX + switch_width/2 - switch_height/2,centerY);

        strokeRectF = new RectF(centerX-switch_width/2, centerY-switch_height/2,
                centerX+switch_width/2,
                centerY+switch_height/2);
        leftRect = new RectF(centerX - switch_width/2, centerY-switch_height/2,
                centerX - switch_width/2 +switch_height,
                centerY + switch_height/2);
        rightRect = new RectF(centerX+switch_width/2 -switch_height, centerY-switch_height/2,
                centerX+switch_width/2,
                centerY + switch_height/2);

        buttonR = switch_height*0.75f;

        buttonCloudBase = new PointF(centerX ,centerY);
        buttonCloudBase2 = new PointF(centerX ,centerY);


        pStars[0].x = getPaddingLeft()+padW+switch_width*0.15f;
        pStars[0].y = getPaddingTop()+padH+switch_height*0.28f -switch_height;

        pStars[1].x = getPaddingLeft()+padW+switch_width*0.13f;
        pStars[1].y = getPaddingTop()+padH+switch_height*0.8f-switch_height;

        pStars[2].x = getPaddingLeft()+padW+switch_width*0.17f;
        pStars[2].y = getPaddingTop()+padH+switch_height*0.7f-switch_height;

        pStars[3].x = getPaddingLeft()+padW+switch_width*0.25f;
        pStars[3].y = getPaddingTop()+padH+switch_height*0.20f-switch_height;

        pStars[4].x = getPaddingLeft()+padW+switch_width*0.26f;
        pStars[4].y = getPaddingTop()+padH+switch_height*0.45f-switch_height;

        pStars[5].x = getPaddingLeft()+padW+switch_width*0.26f;
        pStars[5].y = getPaddingTop()+padH+switch_height*0.85f-switch_height;

        pStars[6].x = getPaddingLeft()+padW+switch_width*0.38f;
        pStars[6].y = getPaddingTop()+padH+switch_height*0.5f-switch_height;

        pStars[7].x = getPaddingLeft()+padW+switch_width*0.43f;
        pStars[7].y = getPaddingTop()+padH+switch_height*0.25f-switch_height;

        pStars[8].x = getPaddingLeft()+padW+switch_width*0.45f;
        pStars[8].y = getPaddingTop()+padH+switch_height*0.75f-switch_height;

        pStars[9].x = getPaddingLeft()+padW+switch_width*0.5f;
        pStars[9].y = getPaddingTop()+padH+switch_height*0.55f-switch_height;

        pStars[10].x = getPaddingLeft()+padW+switch_width*0.53f;
        pStars[10].y = getPaddingTop()+padH+switch_height*0.3f-switch_height;

        if(isChecked()){
            bgColor = bgColorNight;
            buttonColor = buttonColorNight;
            buttonF.x = centerRF.x;
            buttonF.y = centerRF.y;
            buttonColorNightHole[0] = 255;
            buttonColorStar[0] = 255;
        }else {
            bgColor = bgColorDay;
            buttonColor = buttonColorDay;
            buttonF.x = centerLF.x;
            buttonF.y = centerLF.y;
            buttonColorNightHole[0] = 0;
            buttonColorStar[0] = 0;

        }
        setHoleF();
        setPaint();
        invalidate();
    }

    private void setHoleF(){
        buttonHoleF1.x = buttonF.x - buttonR/4;
        buttonHoleF1.y = buttonF.y;

        buttonHoleF2.x = buttonF.x + buttonR/6;
        buttonHoleF2.y = buttonF.y + buttonR/6;

        buttonHoleF3.x = buttonF.x + buttonR/12;
        buttonHoleF3.y = buttonF.y- buttonR/3.5f;

        mColorsAlpha2= new int[]{
                Color.argb((int) buttonColorNightHole[0]/2, 82, 94, 110)
                , Color.argb((int) buttonColorNightHole[0]/2, 246, 250, 255)
        };

        buttonRectF = new RectF(buttonF.x-buttonR/2 +switch_height/80, buttonF.y-buttonR/2+switch_height/80,
                buttonF.x + buttonR/2 -switch_height/80,
                buttonF.y + buttonR/2 -switch_height/80);

        buttonHoleRectF1 = new RectF(buttonHoleF1.x-buttonR/6 + switch_height/160, buttonHoleF1.y-buttonR/6+ switch_height/160,
                buttonHoleF1.x + buttonR/6- switch_height/160,
                buttonHoleF1.y + buttonR/6- switch_height/160);
        buttonHoleRectF2 = new RectF(buttonHoleF2.x-buttonR/8+ switch_height/160, buttonHoleF2.y-buttonR/8+ switch_height/160,
                buttonHoleF2.x + buttonR/8- switch_height/160,
                buttonHoleF2.y + buttonR/8- switch_height/160);
        buttonHoleRectF3 = new RectF(buttonHoleF3.x-buttonR/12+ switch_height/160, buttonHoleF3.y-buttonR/12+ switch_height/160,
                buttonHoleF3.x + buttonR/12- switch_height/160,
                buttonHoleF3.y + buttonR/12- switch_height/160);
    }
    private void setPaint() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);  // 关闭硬件加速,阴影才会绘制

        backPaint.setARGB(255, (int) bgColor[0], (int) bgColor[1], (int) bgColor[2]);
        buttonPaint.setARGB(255, (int) buttonColor[0], (int) buttonColor[1], (int) buttonColor[2]);
        buttonHolePaint.setARGB( (int) buttonColorNightHole[0], (int) buttonColorNightHole[1], (int) buttonColorNightHole[2], (int) buttonColorNightHole[3]);
        buttonWhitePaint.setARGB(25, 255,255,255);

        buttonStarPaint.setARGB( (int) buttonColorStar[0], (int) buttonColorStar[1], (int) buttonColorStar[2], (int) buttonColorStar[3]);
        buttonStarPaint.setStyle(Paint.Style.FILL);
        buttonCloudPaint1.setARGB( (int) buttonColorCloud1[0], (int) buttonColorCloud1[1], (int) buttonColorCloud1[2], (int) buttonColorCloud1[3]);
        buttonCloudPaint2.setARGB( (int) buttonColorCloud2[0], (int) buttonColorCloud2[1], (int) buttonColorCloud2[2], (int) buttonColorCloud2[3]);


        shadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        int[] mColors= {
                Color.argb(0, 246, 250, 255)
                ,Color.argb(150, 22, 22, 22)
        };
        RadialGradient gradient = new RadialGradient(centerF.x, centerF.y , switch_width, mColors, null, Shader.TileMode.CLAMP);
        shadowPaint.setShader(gradient);
        shadowPaint.setStyle(Paint.Style.FILL);

        btnShadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        RadialGradient shadowgradient = new RadialGradient(buttonF.x +buttonR/15, buttonF.y+buttonR/15 , buttonR, shadowColors, null, Shader.TileMode.CLAMP);
        btnShadowPaint.setShader(shadowgradient);
        btnShadowPaint.setStyle(Paint.Style.FILL);
        btnShadowPaint.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.NORMAL));

        // todo 从AttributeSet获取设置的值
        strokePaintArc1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        LinearGradient gradient1 = new LinearGradient((leftRect.right-leftRect.left)/2,leftRect.bottom,(leftRect.right-leftRect.left)/2,leftRect.top,mColors1,null,Shader.TileMode.CLAMP);
        strokePaintArc1.setShader(gradient1);
        strokePaintArc1.setStyle(Paint.Style.STROKE);
        strokePaintArc1.setStrokeWidth(switch_height/15);

        strokePaintArc2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        LinearGradient gradient2 = new LinearGradient((rightRect.right-rightRect.left)/2,rightRect.top,(rightRect.right-rightRect.left)/2,rightRect.bottom,mColors2,null,Shader.TileMode.CLAMP);
        strokePaintArc2.setShader(gradient2);
        strokePaintArc2.setStyle(Paint.Style.STROKE);
        strokePaintArc2.setStrokeWidth(switch_height/15);

        buttonPaintArc.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        buttonPaintArc.setStyle(Paint.Style.STROKE);
        buttonPaintArc.setStrokeWidth(switch_height/40);
        LinearGradient gradientButtonArc = new LinearGradient(buttonF.x,buttonF.y - buttonR/2,buttonF.x,buttonF.y + buttonR/2,mColorsAlpha1,null,Shader.TileMode.CLAMP);
        buttonPaintArc.setShader(gradientButtonArc);

        buttonPaintHoleArc1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        buttonPaintHoleArc1.setStyle(Paint.Style.STROKE);
        buttonPaintHoleArc1.setStrokeWidth(switch_height/80);
        LinearGradient gradientButtonHoleArc = new LinearGradient(buttonHoleF1.x,buttonHoleF1.y - buttonR/12,buttonHoleF1.x,buttonHoleF1.y+ buttonR/12,mColorsAlpha2,null,Shader.TileMode.CLAMP);
        buttonPaintHoleArc1.setShader(gradientButtonHoleArc);

        buttonPaintHoleArc2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        buttonPaintHoleArc2.setStyle(Paint.Style.STROKE);
        buttonPaintHoleArc2.setStrokeWidth(switch_height/80);
        LinearGradient gradientButtonHoleArc2 = new LinearGradient(buttonHoleF2.x,buttonHoleF2.y - buttonR/16,buttonHoleF2.x,buttonHoleF2.y+ buttonR/16,mColorsAlpha2,null,Shader.TileMode.CLAMP);
        buttonPaintHoleArc2.setShader(gradientButtonHoleArc2);

        buttonPaintHoleArc3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        buttonPaintHoleArc3.setStyle(Paint.Style.STROKE);
        buttonPaintHoleArc3.setStrokeWidth(switch_height/80);
        LinearGradient gradientButtonHoleArc3 = new LinearGradient(buttonHoleF3.x,buttonHoleF3.y - buttonR/24,buttonHoleF3.x,buttonHoleF3.y+ buttonR/24,mColorsAlpha2,null,Shader.TileMode.CLAMP);
        buttonPaintHoleArc3.setShader(gradientButtonHoleArc3);

        strokePaintTop.setColor(Color.argb(255, 82, 94, 110));
        strokePaintTop.setStyle(Paint.Style.STROKE);
        strokePaintTop.setStrokeWidth(switch_height/30);

        strokePaintBottom.setColor(Color.argb(255, 246, 250, 255));
        strokePaintBottom.setStyle(Paint.Style.STROKE);
        strokePaintBottom.setStrokeWidth(switch_height/30);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("onDraw",new Gson().toJson(bgColor));
        if(isRunning){
            changePaint();
        }
        drawBack(canvas);
        drawCloud(canvas);
        drawNightButtonStar(canvas);
        drawButton(canvas);
        drawNightButtonHole(canvas);
        drawBackStoke(canvas);
    }
    private void changePaint(){
        backPaint.setARGB(255, (int) bgColor[0], (int) bgColor[1], (int) bgColor[2]);
        buttonPaint.setARGB(255, (int) buttonColor[0], (int) buttonColor[1], (int) buttonColor[2]);
        buttonHolePaint.setARGB((int) buttonColorNightHole[0], (int) buttonColorNightHole[1], (int) buttonColorNightHole[2], (int) buttonColorNightHole[3]);
        buttonStarPaint.setARGB( (int) buttonColorStar[0], (int) buttonColorStar[1], (int) buttonColorStar[2], (int) buttonColorStar[3]);
        buttonCloudPaint1.setARGB( (int) buttonColorCloud1[0], (int) buttonColorCloud1[1], (int) buttonColorCloud1[2], (int) buttonColorCloud1[3]);
        buttonCloudPaint2.setARGB( (int) buttonColorCloud2[0], (int) buttonColorCloud2[1], (int) buttonColorCloud2[2], (int) buttonColorCloud2[3]);
        LinearGradient gradientButtonArc = new LinearGradient(buttonF.x,buttonF.y - buttonR/2,buttonF.x,buttonF.y + buttonR/2,mColorsAlpha1,null,Shader.TileMode.CLAMP);
        buttonPaintArc.setShader(gradientButtonArc);
        LinearGradient gradientButtonHoleArc = new LinearGradient(buttonHoleF1.x,buttonHoleF1.y - buttonR/12,buttonHoleF1.x,buttonHoleF1.y+ buttonR/12,mColorsAlpha2,null,Shader.TileMode.CLAMP);
        buttonPaintHoleArc1.setShader(gradientButtonHoleArc);
        LinearGradient gradientButtonHoleArc2 = new LinearGradient(buttonHoleF2.x,buttonHoleF2.y - buttonR/16,buttonHoleF2.x,buttonHoleF2.y+ buttonR/16,mColorsAlpha2,null,Shader.TileMode.CLAMP);
        buttonPaintHoleArc2.setShader(gradientButtonHoleArc2);
        LinearGradient gradientButtonHoleArc3 = new LinearGradient(buttonHoleF3.x,buttonHoleF3.y - buttonR/24,buttonHoleF3.x,buttonHoleF3.y+ buttonR/24,mColorsAlpha2,null,Shader.TileMode.CLAMP);
        buttonPaintHoleArc3.setShader(gradientButtonHoleArc3);
        RadialGradient shadowgradient = new RadialGradient(buttonF.x +buttonR/15, buttonF.y+buttonR/15 , buttonR, shadowColors, null, Shader.TileMode.CLAMP);
        btnShadowPaint.setShader(shadowgradient);
    }
    private void drawBack(Canvas canvas){
        canvas.drawRoundRect(strokeRectF, switch_height, switch_height, backPaint);

        Path path = new Path();
        path.addRoundRect(strokeRectF,switch_height, switch_height,Path.Direction.CCW);
        canvas.clipPath(path);
    }
    private void drawCloud(Canvas canvas){

        canvas.drawCircle(buttonCloudBase2.x +buttonR*1.4f, buttonCloudBase2.y-buttonR*0.1f,buttonR/1.5f, buttonCloudPaint2);
        canvas.drawCircle(buttonCloudBase2.x +buttonR*0.9f, buttonCloudBase2.y+buttonR*0.1f,buttonR*0.4f, buttonCloudPaint2);
        canvas.drawCircle(buttonCloudBase2.x +buttonR*0.5f, buttonCloudBase2.y+buttonR*0.5f,buttonR*0.6f, buttonCloudPaint2);
        canvas.drawCircle(buttonCloudBase2.x-buttonR*0.2f, buttonCloudBase2.y+buttonR*0.6f,buttonR*0.4f, buttonCloudPaint2);
        canvas.drawCircle(buttonCloudBase2.x-buttonR, buttonCloudBase2.y+buttonR*1.1f,buttonR, buttonCloudPaint2);

        canvas.drawCircle(buttonCloudBase.x +buttonR*1.6f, buttonCloudBase.y+buttonR/9,buttonR/1.5f, buttonCloudPaint1);
        canvas.drawCircle(buttonCloudBase.x +buttonR*1.5f, buttonCloudBase.y+buttonR*0.9f,buttonR, buttonCloudPaint1);
        canvas.drawCircle(buttonCloudBase.x +buttonR*0.7f, buttonCloudBase.y+buttonR*0.6f,buttonR*0.4f, buttonCloudPaint1);
        canvas.drawCircle(buttonCloudBase.x +buttonR*0.5f, buttonCloudBase.y+buttonR*1.1f,buttonR*0.75f, buttonCloudPaint1);
        canvas.drawCircle(buttonCloudBase.x-buttonR*0.2f, buttonCloudBase.y+buttonR*0.8f,buttonR*0.4f, buttonCloudPaint1);
        canvas.drawCircle(buttonCloudBase.x-buttonR, buttonCloudBase.y+buttonR*1.3f,buttonR, buttonCloudPaint1);
    }
    private void drawButton(Canvas canvas){
        canvas.drawCircle(buttonF.x, buttonF.y,buttonR*1.4f, buttonWhitePaint);
        canvas.drawCircle(buttonF.x, buttonF.y,buttonR*1.1f, buttonWhitePaint);
        canvas.drawCircle(buttonF.x, buttonF.y,buttonR*0.8f, buttonWhitePaint);

        canvas.drawArc(leftRect, 90, 180, false, strokePaintArc1);
        canvas.drawArc(rightRect, 270, 180, false, strokePaintArc2);

        canvas.drawLine(centerLF.x,centerLF.y+switch_height/2-switch_height/60,centerRF.x,centerRF.y+switch_height/2-switch_height/60,strokePaintBottom);
        canvas.drawLine(centerLF.x,centerLF.y-switch_height/2+switch_height/60,centerRF.x,centerRF.y-switch_height/2+switch_height/60,strokePaintTop);

        canvas.drawCircle(buttonF.x +buttonR/15, buttonF.y+buttonR/15,buttonR/2, btnShadowPaint);
        canvas.drawCircle(buttonF.x, buttonF.y,buttonR/2, buttonPaint);
    }

    private void drawBackStoke(Canvas canvas){
        canvas.drawArc(buttonRectF, 315, 360, false, buttonPaintArc);
        canvas.drawArc(buttonHoleRectF1, 315, 360, false, buttonPaintHoleArc1);
        canvas.drawArc(buttonHoleRectF2, 315, 360, false, buttonPaintHoleArc2);
        canvas.drawArc(buttonHoleRectF3, 315, 360, false, buttonPaintHoleArc3);
    }
    private void drawNightButtonHole(Canvas canvas){
        canvas.drawCircle(buttonHoleF1.x, buttonHoleF1.y,buttonR/6, buttonHolePaint);
        canvas.drawCircle(buttonHoleF2.x, buttonHoleF2.y,buttonR/8, buttonHolePaint);
        canvas.drawCircle(buttonHoleF3.x, buttonHoleF3.y,buttonR/12, buttonHolePaint);
    }
    private void drawNightButtonStar(Canvas canvas){
        canvas.drawCircle(pStars[1].x, pStars[1].y,buttonR/60, buttonStarPaint);
        canvas.drawCircle(pStars[2].x, pStars[2].y,buttonR/65, buttonStarPaint);
        canvas.drawPath(getStarPath(pStars[3],buttonR/15),buttonStarPaint);
        canvas.drawCircle(pStars[4].x, pStars[4].y,buttonR/50, buttonStarPaint);
        canvas.drawCircle(pStars[5].x, pStars[5].y,buttonR/55, buttonStarPaint);
        canvas.drawCircle(pStars[6].x, pStars[6].y,buttonR/60, buttonStarPaint);
        canvas.drawCircle(pStars[7].x, pStars[7].y,buttonR/55, buttonStarPaint);
        canvas.drawPath(getStarPath(pStars[8],buttonR/18),buttonStarPaint);
        canvas.drawCircle(pStars[9].x, pStars[9].y,buttonR/55, buttonStarPaint);
        canvas.drawPath(getStarPath(pStars[10],buttonR/10),buttonStarPaint);
    }

    private Path getStarPath(PointF p,float r){
        Path starPath = new Path();
        starPath.reset();
        starPath.moveTo(p.x,p.y-r);
        starPath.quadTo(p.x, p.y, p.x-r, p.y);
        starPath.quadTo(p.x, p.y, p.x, p.y+r);
        starPath.quadTo(p.x, p.y, p.x+r, p.y);
        starPath.quadTo(p.x, p.y, p.x,p.y-r);

        starPath.close();
        return starPath;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!isRunning){
                    isRunning = true;
                    new Thread(checkRunnable).start();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return false;
    }
    Runnable checkRunnable =new Runnable() {
        @Override
        public void run() {
            if(isChecked()){
                setChecked(false);
            }else {
                setChecked(true);
            }

            onCheckedChangeListener.onCheckedChanged(DayNightSwitch.this,isChecked());

            int time = 0;
            float v0 = switch_height/32;
            float a = (switch_width -switch_height -v0*100) *2 /10000 ; //S=v0t+1/2at^2
            float initialX = buttonF.x;
            float distan = Math.abs(buttonF.x-centerF.x);
            while (time<100){
                if(!isChecked()){
                    //day 2 night
                    bgColor = new float[]{bgColor[0] + 0.55f, bgColor[1] + 1.42f, bgColor[2] + 1.89f};
                    buttonColor = new float[]{buttonColor[0] + 0.73f, buttonColor[1] + 0.17f, buttonColor[2] - 2.26f};
                    if(255 - 2.55f*time*1.6f >0){
                        buttonColorNightHole[0] = 255 - 2.55f*time*1.6f;
                        buttonColorStar[0] = 255 - 2.55f*time*1.6f;
                    }else {
                        buttonColorNightHole[0]=0;
                    }
                    buttonF.x = initialX - (v0*time + 0.5f*a*time*time) ;
                    buttonCloudBase.y = buttonCloudBase.y-switch_height/50;
                    buttonCloudBase2.y = buttonCloudBase2.y-switch_height/25;
                    for(PointF pStar:pStars ){
                        pStar.y = pStar.y-switch_height/100;
                    }
                    buttonColorCloud1[0] = 2.55f*time;
                    buttonColorCloud2[0] = 2.55f*time;

                }else {
                    bgColor = new float[]{bgColor[0] - 0.55f, bgColor[1] - 1.42f, bgColor[2] - 1.89f};
                    buttonColor = new float[]{buttonColor[0] - 0.73f, buttonColor[1] - 0.17f, buttonColor[2] + 2.26f};
                    buttonColorNightHole[0] = 2.55f*time;
                    buttonColorStar[0] = 2.55f*time;
                    buttonF.x = initialX + (v0*time+ 0.5f*a*time*time);


                    buttonCloudBase.y = buttonCloudBase.y+switch_height/50;
                    buttonCloudBase2.y = buttonCloudBase2.y+switch_height/25;
                    for(PointF pStar:pStars ){
                        pStar.y = pStar.y+switch_height/100;
                    }
                    if(255 - 2.55f*time*1.6f >0){
                        buttonColorCloud1[0] = 255 - 2.55f*time*1.6f;
                        buttonColorCloud2[0] = 255 - 2.55f*time*1.6f;

                    }else {
                        buttonColorCloud1[0]=0;
                        buttonColorCloud2[0]=0;
                    }
                }
                setHoleF();
                invalidate();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                time++;
            }
            isRunning = false;

        }
    };
    @Override
    public void setOnCheckedChangeListener(@Nullable OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }
}
