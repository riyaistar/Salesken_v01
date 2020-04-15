package ai.salesken.v1.utils;


import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.core.view.MotionEventCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KeypadlessKeypad extends androidx.appcompat.widget.AppCompatEditText {

    private static final Method mShowSoftInputOnFocus = getSetShowSoftInputOnFocusMethod(
            EditText.class, "setShowSoftInputOnFocus", boolean.class);

    public static Method getSetShowSoftInputOnFocusMethod(Class<?> cls, String methodName, Class<?>... parametersType) {
        Class<?> sCls = cls.getSuperclass();
        while (sCls != Object.class) {
            try {
                return sCls.getDeclaredMethod(methodName, parametersType);
            } catch (NoSuchMethodException e) {
                // Just super it again
            }
            sCls = sCls.getSuperclass();
        }
        return null;
    }

    private Context mContext;


    /**
     * Listener for Copy, Cut and Paste event
     * Currently callback only for Paste event is implemented
     */
    private OnEditTextActionListener mOnEditTextActionListener;

    public KeypadlessKeypad(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public KeypadlessKeypad(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public KeypadlessKeypad(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        init();
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
    }


    public final void appendText(CharSequence text) {
        append(text, 0, text.length());
    }

    /***
     * Initialize all the necessary components of TextView.
     */
    private void init() {

        setSingleLine(true);

        synchronized (this) {
            setInputType(getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            setFocusableInTouchMode(true);
        }

        reflexSetShowSoftInputOnFocus(false); // Workaround.

        // Ensure that cursor is at the end of the input box when initialized. Without this, the
        // cursor may be at index 0 when there is text added via layout XML.
        setSelection(getText().length());
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        hideKeyboard();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final boolean ret = super.onTouchEvent(event);
        // Must be done after super.onTouchEvent()
        hideKeyboard();
        return ret;
    }

    private void hideKeyboard() {
        final InputMethodManager imm = ((InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE));
        if (imm != null && imm.isActive(this)) {
            imm.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
        }
    }

    private void reflexSetShowSoftInputOnFocus(boolean show) {
        if (mShowSoftInputOnFocus != null) {
            invokeMethod(mShowSoftInputOnFocus, this, show);
        } else {
            // Use fallback method. Not tested.
            hideKeyboard();
        }
    }

    public static Object invokeMethod(Method method, Object receiver, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }

        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int textViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight();

        this.setMeasuredDimension(textViewWidth, height);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before,
                                 int after) {
        super.onTextChanged(text, start, before, after);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean consumed = super.onTextContextMenuItem(id);

        switch (id) {
            case android.R.id.paste:
                if (mOnEditTextActionListener != null) {
                    mOnEditTextActionListener.onPaste();
                }
                break;
        }

        return consumed;
    }


    /**
     * Setter method for {@link #mOnEditTextActionListener}
     *
     * @param onEditTextActionListener
     *         Instance of the {@link OnEditTextActionListener}
     */
    public void setOnEditTextActionListener(OnEditTextActionListener onEditTextActionListener) {
        this.mOnEditTextActionListener = onEditTextActionListener;
    }


    private Rect mRect = new Rect();

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);

        int[] location = new int[2];
        getLocationOnScreen(location);
        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + getWidth();
        mRect.bottom = location[1] + getHeight();

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (action == MotionEvent.ACTION_DOWN && !mRect.contains(x, y)) {
            InputMethodManager input = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            // Since we're replacing the text every time we add or remove a
            // character, only read the difference. (issue 5337550)
            final int added = event.getAddedCount();
            final int removed = event.getRemovedCount();
            final int length = event.getBeforeText().length();
            if (added > removed) {
                event.setRemovedCount(0);
                event.setAddedCount(1);
                event.setFromIndex(length);
            } else if (removed > added) {
                event.setRemovedCount(1);
                event.setAddedCount(0);
                event.setFromIndex(length - 1);
            } else {
                return;
            }
        } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            // The parent EditText class lets tts read "edit box" when this View has a focus, which
            // confuses users on app launch (issue 5275935).
            return;
        }
        super.sendAccessibilityEventUnchecked(event);
    }

    /**
     * Interface to get callback from the Edittext copy, cut and paste event
     * For time being only the Paste Event callback is generated
     */
    public interface OnEditTextActionListener {

        /**
         * If Edittext get paste event then this method will be called
         */
        void onPaste();
    }

}