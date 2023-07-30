package com.example.elegantnumberbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import com.google.android.material.button.MaterialButton;

/**
 * Created by Ashik Vetrivelu on 10/08/16.
 */
public class ElegantNumberButton extends RelativeLayout {
    private final Context context;
    private AttributeSet attrs;
    private int styleAttr;
    private OnClickListener mListener;
    private int initialNumber;
    private int lastNumber;
    private int currentNumber;
    private int finalNumber;
    private EditText etCounter;
    private OnValueChangeListener mOnValueChangeListener;

    public MaterialButton addBtn, subtractBtn;

    public ElegantNumberButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {
        inflate(context, R.layout.layout, this);
        // use updated method to get the resources

        Resources res = getResources();
        @SuppressLint("UseCompatLoadingForDrawables") Drawable defaultDrawable = res.getDrawable(R.drawable.background, context.getTheme());
        int defaultColor = res.getColor(R.color.white, context.getTheme());
        int defaultTextColor = res.getColor(R.color.cello, context.getTheme());

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ElegantNumberButton,
                styleAttr, 0);

        initialNumber = a.getInt(R.styleable.ElegantNumberButton_initialNumber, 0);
        finalNumber = a.getInt(R.styleable.ElegantNumberButton_finalNumber, Integer.MAX_VALUE);
        float textSize = a.getDimension(R.styleable.ElegantNumberButton_textSize, 13);
        int color = a.getColor(R.styleable.ElegantNumberButton_backGroundColor, defaultColor);
        int textColor = a.getColor(R.styleable.ElegantNumberButton_textColor, defaultTextColor);
        Drawable drawable = a.getDrawable(R.styleable.ElegantNumberButton_backgroundDrawable);


        subtractBtn = findViewById(R.id.btnSubtract);
        addBtn = findViewById(R.id.btnAdd);
        etCounter = findViewById(R.id.etNumberCounter);
        LinearLayout mLayout = findViewById(R.id.layout);

        subtractBtn.setTextColor(textColor);
        addBtn.setTextColor(textColor);
        etCounter.setTextColor(textColor);
        subtractBtn.setTextSize(textSize);
        addBtn.setTextSize(textSize);
        etCounter.setTextSize(textSize);





        if (drawable == null) {
            drawable = defaultDrawable;
        }
        assert drawable != null;
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
        mLayout.setBackground(drawable);

        subtractBtn.setCornerRadius(1000);
        addBtn.setCornerRadius(1000);

        etCounter.setText(String.valueOf(initialNumber));

        currentNumber = initialNumber;
        lastNumber = initialNumber;



        subtractBtn.setOnClickListener(mView -> {
            int num = Integer.parseInt(etCounter.getText().toString());
            setNumber(String.valueOf(num - 1), true, true);
            System.out.println("subtractBtn.setOnClickListener");
        });
        addBtn.setOnClickListener(mView -> {
            int num = Integer.parseInt(etCounter.getText().toString());
            setNumber(String.valueOf(num + 1), true, true);
            System.out.println("addBtn.setOnClickListener");
        });
        a.recycle();


        etCounter.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus) {
                try {
                    Integer onChangedNumber = Integer.parseInt(etCounter.getText().toString());
                    if(onChangedNumber == lastNumber) {
                        return;
                    }
                    setNumber(String.valueOf(onChangedNumber), true, false);
                } catch (NumberFormatException e) {
                    etCounter.setText(String.valueOf(lastNumber));
                }
            }
        });

        etCounter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Integer onChangedNumber = Integer.parseInt(etCounter.getText().toString());
                    if(onChangedNumber == lastNumber) {
                        return;
                    }
                    // store the position of the cursor
                    int cursorPosition = etCounter.getSelectionStart();
                    setNumber(String.valueOf(onChangedNumber), true, false);
                    // set the cursor to the same position
                    etCounter.setSelection(cursorPosition);
                } catch (NumberFormatException e) {
                    // do nothing
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void callListener(View view, boolean isClicking) {
        if (mListener != null) {
            mListener.onClick(view);
        }

        // debug mOnValueChangeListener is null
        System.out.println("mOnValueChangeListener is null: " + (mOnValueChangeListener == null));

        if (mOnValueChangeListener != null) {
            // debug lastNumber  and currentNumber
            System.out.println("lastNumber: " + lastNumber);
            System.out.println("currentNumber: " + currentNumber);
            if (lastNumber != currentNumber) {
                mOnValueChangeListener.onValueChange(this, lastNumber, currentNumber);
                if(isClicking)
                    mOnValueChangeListener.onValueChangeByClicking(this, lastNumber, currentNumber);
                else
                    mOnValueChangeListener.onValueChangeByTyping(this, lastNumber, currentNumber);
            }
        }
    }

    public String getNumber() {
        return String.valueOf(currentNumber);
    }

    public void setNumber(String number) {
        lastNumber = currentNumber;
        this.currentNumber = Integer.parseInt(number);
        if (this.currentNumber > finalNumber) {
            this.currentNumber = finalNumber;
        }
        if (this.currentNumber < initialNumber) {
            this.currentNumber = initialNumber;
        }
        etCounter.setText(String.valueOf(currentNumber));
    }

    public void setNumber(String number, boolean notifyListener, boolean isClicking) {
        if (notifyListener) {
            callListener(this, isClicking);
        }
        setNumber(number);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    @FunctionalInterface
    public interface OnClickListener {
        void onClick(View view);
    }

    public interface OnValueChangeListener {
        void onValueChangeByClicking(ElegantNumberButton view, int oldValue, int newValue);

        void onValueChange(ElegantNumberButton elegantNumberButton, int lastNumber, int currentNumber);

        void onValueChangeByTyping(ElegantNumberButton elegantNumberButton, int lastNumber, int currentNumber);
    }

    public void setRange(Integer startingNumber, Integer endingNumber) {
        this.initialNumber = startingNumber;
        this.finalNumber = endingNumber;
    }

    public void updateColors(int backgroundColor, int textColor) {
        this.etCounter.setBackgroundColor(backgroundColor);
        this.addBtn.setBackgroundColor(backgroundColor);
        this.subtractBtn.setBackgroundColor(backgroundColor);

        this.etCounter.setTextColor(textColor);
        this.addBtn.setTextColor(textColor);
        this.subtractBtn.setTextColor(textColor);
    }

    public void updateTextSize(int unit, float newSize) {
        this.etCounter.setTextSize(unit, newSize);
        this.addBtn.setTextSize(unit, newSize);
        this.subtractBtn.setTextSize(unit, newSize);
    }
}
