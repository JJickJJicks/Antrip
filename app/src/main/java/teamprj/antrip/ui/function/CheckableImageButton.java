package teamprj.antrip.ui.function;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.chip.ChipGroup;

import teamprj.antrip.R;

public class CheckableImageButton extends AppCompatImageButton implements Checkable {
    private boolean mChecked;
    private boolean mBroadcasting;
    private int mPersonality;
    private OnCheckedChangeListener onCheckedChangeListener;

    private static final int[] CHECKED_STATE_SET = { R.attr.isChecked };
    private static final int PERSONALITY_RADIO_BUTTON = 0;
    private static final int PERSONALITY_CHECK_BOX = 1;

    public CheckableImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    @Override
    public boolean performClick() {
        if(mPersonality == PERSONALITY_RADIO_BUTTON)
            setChecked(true);
        else if(mPersonality == PERSONALITY_CHECK_BOX)
            toggle();
        return super.performClick();
    }

    public CheckableImageButton(Context context) {
        super(context);
    }

    @Override
    public void setChecked(boolean checked) {
        if(mChecked != checked){
            mChecked = checked;
            refreshDrawableState();

            if(mBroadcasting)
                return;
        }

        mBroadcasting = true;
        if(onCheckedChangeListener != null)
            onCheckedChangeListener.onCheckedChanged(this, mChecked);

    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);

    }

    private void initView(){

    }

    private void getAttrs(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CheckableImageButton);
        mPersonality = a.getInt(R.styleable.CheckableImageButton_personality, PERSONALITY_RADIO_BUTTON);
        boolean checked = a.getBoolean(R.styleable.CheckableImageButton_isChecked, false);
        setChecked(checked);

        a.recycle();
    }

    public static interface  OnCheckedChangeListener{
        void onCheckedChanged(CheckableImageButton button, boolean isChecked);
    }
}
