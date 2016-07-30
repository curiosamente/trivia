package curiosamente.com.app.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import curiosamente.com.app.R;


public class RoundedAndCrossedImageView extends ImageView {

    public RoundedAndCrossedImageView(Context context) {
        super(context);
    }

    public RoundedAndCrossedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedAndCrossedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && drawable instanceof VectorDrawable) {
            ((VectorDrawable) drawable).draw(canvas);
            b = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas();
            c.setBitmap(b);
            drawable.draw(c);
        }
        else {
            b = ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        int w = getWidth(), h = getHeight();
        Bitmap roundBitmap =  getCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);


        canvas.rotate(45,canvas.getWidth()/2,canvas.getHeight()/2);


        int top = (int) ((canvas.getHeight() / 2) - (canvas.getHeight() / 8));
        int bottom =  (int) ((canvas.getHeight() / 2) + (canvas.getHeight() / 8));
        Rect r = new Rect(0, top,roundBitmap.getWidth(), bottom);



        Paint textPaint = new Paint();
        textPaint.setTextSize(20);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();

        String text = getResources().getString(R.string.cashed);
        int numOfChars = textPaint.breakText(text,true,getWidth(),null);
        int start = (text.length()-numOfChars)/2;

        Paint rect = new Paint();
        rect.setColor(Color.RED);
        canvas.drawRect(r ,rect);

        int baseLineY = (int) ((canvas.getHeight() / 2) + ((fontMetrics.bottom - fontMetrics.top)/4));
        canvas.drawText(text,start,start+numOfChars,r.exactCenterX(), baseLineY,textPaint);

    }


    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if(bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2+0.7f, sbmp.getHeight() / 2+0.7f,
                sbmp.getWidth() / 2+0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);


        return output;
    }
}