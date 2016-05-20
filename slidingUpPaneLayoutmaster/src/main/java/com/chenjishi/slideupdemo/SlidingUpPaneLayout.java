package com.chenjishi.slideupdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by chenjishi on 14/11/5.
 */
public class SlidingUpPaneLayout extends ViewGroup {

    private static final String TAG = "SlidingUpPaneLayout";

    /**
     * Default size of the overhang for a pane in the open state.
     * At least this much of a sliding pane will remain visible.
     * This indicates that there is more content available and provides
     * a "physical" edge to grab to pull it closed.
     */
    /**
     * 设置下滑后前面板剩余的宽度
     */
//    MainActivity mainActivity = new MainActivity();
//    private int appArea = mainActivity.getAppArea();
    private static final int DEFAULT_OVERHANG_SIZE = 35; // dp;

    /**
     * If no fade color is given by default it will fade to 80% gray.
     */
    /**
     * 设置下滑后前面板覆盖色的颜色，现设置为透明色，即不变色
     */
    private static final int DEFAULT_FADE_COLOR = 0x00000000;

    /**
     * The fade color used for the sliding panel. 0 = no fading.
     */
    private int mSliderFadeColor = DEFAULT_FADE_COLOR;

    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    /**
     * The fade color used for the panel covered by the slider. 0 = no fading.
     */
    private int mCoveredFadeColor;

    /**
     * Drawable used to draw the shadow between panes by default.
     */
    private Drawable mShadowDrawableTop;

    private float mEdgeSize;

    /**
     * The size of the overhang in pixels.
     * This is the minimum section of the sliding panel that will
     * be visible in the open state to allow for a closing drag.
     */
    private final int mOverhangSize;

    /**
     * True if a panel can slide with the current measurements
     */
    private boolean mCanSlide;

    /**
     * The child view that can slide, if any.
     */
    private View mSlideableView;

    /**
     * How far the panel is offset from its closed position.
     * range [0, 1] where 0 = closed, 1 = open.
     */
    private float mSlideOffset;

    /**
     * How far the non-sliding panel is parallaxed from its usual position when open.
     * range [0, 1]
     */
    private float mParallaxOffset;

    /**
     * How far in pixels the slideable panel may move.
     */
    private int mSlideRange;

    /**
     * A panel view is locked into internal scrolling or another condition that
     * is preventing a drag.
     */
    private boolean mIsUnableToDrag;

    /**
     * Distance in pixels to parallax the fixed pane by when fully closed
     */
    private int mParallaxBy;

    private float mInitialMotionX;
    private float mInitialMotionY;

    private PanelSlideListener mPanelSlideListener;

    private final ViewDragHelper mDragHelper;

    /**
     * Stores whether or not the pane was open the last time it was slideable.
     * If open/close operations are invoked this state is modified. Used by
     * instance state save/restore.
     */
    private boolean mPreservedOpenState;
    private boolean mFirstLayout = true;

    private final ArrayList<DisableLayerRunnable> mPostedRunnables =
            new ArrayList<DisableLayerRunnable>();

    private final Rect mTmpRect = new Rect();

    static final SlidingPanelLayoutImpl IMPL;

    static {
        final int deviceVersion = Build.VERSION.SDK_INT;
        if (deviceVersion >= 17) {
            IMPL = new SlidingPanelLayoutImplJBMR1();
        } else if (deviceVersion >= 16) {
            IMPL = new SlidingPanelLayoutImplJB();
        } else {
            IMPL = new SlidingPanelLayoutImplBase();
        }
    }

    public interface PanelSlideListener {

        public void onPanelSlide(View panel, float slideOffset);

        public void onPanelOpened(View panel);

        public void onPanelClosed(View panel);
    }

    public SlidingUpPaneLayout(Context context) {
        this(context, null);
    }

    public SlidingUpPaneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingUpPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final float density = context.getResources().getDisplayMetrics().density;
        mOverhangSize = (int) (DEFAULT_OVERHANG_SIZE * density + 0.5f);

        setWillNotDraw(false);

        ViewCompat.setAccessibilityDelegate(this, new AccessibilityDelegate());
        ViewCompat.setImportantForAccessibility(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);

        mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
        mDragHelper.setMinVelocity(MIN_FLING_VELOCITY * density);
    }

    public void setParallaxDistance(int parallaxBy) {
        mParallaxBy = parallaxBy;
        requestLayout();
    }

    public int getParallaxDistance() {
        return mParallaxBy;
    }

    public void setSliderFadeColor(int color) {
        mSliderFadeColor = color;
    }

    public int getSliderFadeColor() {
        return mSliderFadeColor;
    }

    public void setCoveredFadeColor(int color) {
        mCoveredFadeColor = color;
    }

    public int getCoveredFadeColor() {
        return mCoveredFadeColor;
    }

    public void setPanelSlideListener(PanelSlideListener listener) {
        mPanelSlideListener = listener;
    }

    void dispatchOnPanelOpened(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelOpened(panel);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    void dispatchOnPanelSlide(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelSlide(panel, mSlideOffset);
        }
    }

    void dispatchOnPanelClosed(View panel) {
        if (mPanelSlideListener != null) {
            mPanelSlideListener.onPanelClosed(panel);
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    void updateObscuredViewsVisibility(View panel) {
        final int startBound = getPaddingTop();
        final int endBound = getHeight() - getPaddingBottom();
        final int leftBound = getPaddingLeft();
        final int rightBound = getWidth() - getPaddingRight();
        final int left;
        final int right;
        final int top;
        final int bottom;
        if (panel != null && viewIsOpaque(panel)) {
            left = panel.getLeft();
            right = panel.getRight();
            top = panel.getTop();
            bottom = panel.getBottom();
        } else {
            left = right = top = bottom = 0;
        }

        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final View child = getChildAt(i);

            if (child == panel) {
                break;
            }

            final int clampedChildTop = Math.max(startBound, child.getTop());
            final int clampedChildLeft = Math.max(leftBound, child.getLeft());
            final int clampedChildBottom = Math.min(endBound, child.getBottom());
            final int clampedChildRight = Math.min(rightBound, child.getRight());
            final int vis;
            if (clampedChildLeft >= left && clampedChildTop >= top &&
                    clampedChildRight <= right && clampedChildBottom <= bottom) {
                vis = INVISIBLE;
            } else {
                vis = VISIBLE;
            }
            child.setVisibility(vis);
        }
    }

    public void setEdgeSize(int offset) {
        mEdgeSize = offset;
    }

    void setAllChildrenVisible() {
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == INVISIBLE) {
                child.setVisibility(VISIBLE);
            }
        }
    }

    private void onPanelDragged(int newTop) {
        if (mSlideableView == null) {
            mSlideOffset = 0;
            return;
        }

        final LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();

        final int newStart = newTop;

        final int paddingStart = getPaddingTop();
        final int lpMargin = lp.topMargin;
        final int startBound = paddingStart + lpMargin;

        mSlideOffset = (float) (newStart - startBound) / mSlideRange;

        if (mParallaxBy != 0) {
            parallaxOtherViews(mSlideOffset);
        }

        if (lp.dimWhenOffset) {
            dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
        }

        dispatchOnPanelSlide(mSlideableView);
    }

    private void parallaxOtherViews(float slideOffset) {
        final LayoutParams slideLp = (LayoutParams) mSlideableView.getLayoutParams();
        final boolean dimViews = slideLp.dimWhenOffset &&
                slideLp.topMargin <= 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View v = getChildAt(i);
            if (v == mSlideableView) continue;

            final int oldOffset = (int) ((1 - mParallaxOffset) * mParallaxBy);
            mParallaxOffset = slideOffset;
            final int newOffset = (int) ((1 - slideOffset) * mParallaxBy);
            final int dy = oldOffset - newOffset;

            v.offsetTopAndBottom(dy);

            if (dimViews) {
                dimChildView(v, 1 - mParallaxOffset, mCoveredFadeColor);
            }
        }
    }

    private void dimChildView(View v, float mag, int fadeColor) {
        final LayoutParams lp = (LayoutParams) v.getLayoutParams();

        if (mag > 0 && fadeColor != 0) {
            final int baseAlpha = (fadeColor & 0xff000000) >>> 24;
            int imag = (int) (baseAlpha * mag);
            int color = imag << 24 | (fadeColor & 0xffffff);
            if (lp.dimPaint == null) {
                lp.dimPaint = new Paint();
            }
            lp.dimPaint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_OVER));
            if (ViewCompat.getLayerType(v) != ViewCompat.LAYER_TYPE_HARDWARE) {
                ViewCompat.setLayerType(v, ViewCompat.LAYER_TYPE_HARDWARE, lp.dimPaint);
            }
            invalidateChildRegion(v);
        } else if (ViewCompat.getLayerType(v) != ViewCompat.LAYER_TYPE_NONE) {
            if (lp.dimPaint != null) {
                lp.dimPaint.setColorFilter(null);
            }
            final DisableLayerRunnable dlr = new DisableLayerRunnable(v);
            mPostedRunnables.add(dlr);
            ViewCompat.postOnAnimation(this, dlr);
        }
    }

    private static boolean viewIsOpaque(View v) {
        if (ViewCompat.isOpaque(v)) return true;

        // View#isOpaque didn't take all valid opaque scrollbar modes into account
        // before API 18 (JB-MR2). On newer devices rely solely on isOpaque above and return false
        // here. On older devices, check the view's background drawable directly as a fallback.
        if (Build.VERSION.SDK_INT >= 18) return false;

        final Drawable bg = v.getBackground();
        if (bg != null) {
            return bg.getOpacity() == PixelFormat.OPAQUE;
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayout = true;

        for (int i = 0, count = mPostedRunnables.size(); i < count; i++) {
            final DisableLayerRunnable dlr = mPostedRunnables.get(i);
            dlr.run();
        }
        mPostedRunnables.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode != MeasureSpec.EXACTLY) {
            if (isInEditMode()) {
                if (heightMode == MeasureSpec.AT_MOST) {
                    heightMode = MeasureSpec.EXACTLY;
                } else if (heightMode == MeasureSpec.UNSPECIFIED) {
                    heightMode = MeasureSpec.EXACTLY;
                    heightSize = 300;
                }
            } else {
                throw new IllegalStateException("Height must have an exact value or MATCH_PARENT");
            }
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            if (isInEditMode()) {
                widthMode = MeasureSpec.AT_MOST;
                widthSize = 300;
            } else {
                throw new IllegalStateException("Width must not be UNSPECIFIED");
            }
        }

        int layoutWidth = 0;
        int maxLayoutWidth = -1;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                layoutWidth = maxLayoutWidth = widthSize - getPaddingLeft() - getPaddingRight();
                break;
            case MeasureSpec.AT_MOST:
                maxLayoutWidth = widthSize - getPaddingLeft() - getPaddingRight();
                break;
        }

        float weightSum = 0;
        boolean canSlide = false;
        final int heightAvailable = heightSize - getPaddingTop() - getPaddingBottom();
        int heightRemaining = heightAvailable;
        final int childCount = getChildCount();

        if (childCount > 2) {
            Log.e(TAG, "onMeasure: More than two child views are not supported.");
        }

        // We'll find the current one below.
        mSlideableView = null;

        // First pass. Measure based on child LayoutParams width/height.
        // Weight will incur a second pass.
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() == GONE) {
                lp.dimWhenOffset = false;
                continue;
            }

            if (lp.weight > 0) {
                weightSum += lp.weight;

                // If we have no height, weight is the only contributor to the final size.
                // Measure this view on the weight pass only.
                if (lp.height == 0) continue;
            }

            int childWidthSpec;
            if (lp.width == LayoutParams.WRAP_CONTENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(maxLayoutWidth, MeasureSpec.AT_MOST);
            } else if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(maxLayoutWidth, MeasureSpec.EXACTLY);
            } else {
                childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
            }

            int childHeightSpec;
            final int verticalMargin = lp.topMargin + lp.bottomMargin;
            if (lp.height == LayoutParams.WRAP_CONTENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(heightAvailable - verticalMargin, MeasureSpec.AT_MOST);
            } else if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(heightAvailable - verticalMargin, MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
            }

            child.measure(childWidthSpec, childHeightSpec);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            if (widthMode == MeasureSpec.AT_MOST && childWidth > layoutWidth) {
                layoutWidth = Math.min(childWidth, maxLayoutWidth);
            }

            heightRemaining -= childHeight;
            canSlide |= lp.slideable = heightRemaining < 0;
            if (lp.slideable) {
                mSlideableView = child;
            }
        }

        // Resolve weight and make sure non-sliding panels are smaller than the full screen.
        if (canSlide || weightSum > 0) {
            final int fixedPanelHeightLimit = heightAvailable - mOverhangSize;

            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);

                if (child.getVisibility() == GONE) {
                    continue;
                }

                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                if (child.getVisibility() == GONE) {
                    continue;
                }

                final boolean skippedFirstPass = lp.height == 0 && lp.weight > 0;
                final int measuredHeight = skippedFirstPass ? 0 : child.getMeasuredHeight();
                if (canSlide && child != mSlideableView) {
                    if (lp.height < 0 && (measuredHeight > fixedPanelHeightLimit || lp.weight > 0)) {
                        // Fixed panels in a sliding configuration should
                        // be clamped to the fixed panel limit.
                        final int childWidthSpec;
                        if (skippedFirstPass) {
                            // Do initial width measurement if we skipped measuring this view
                            // the first time around.
                            if (lp.width == LayoutParams.WRAP_CONTENT) {
                                childWidthSpec = MeasureSpec.makeMeasureSpec(maxLayoutWidth, MeasureSpec.AT_MOST);
                            } else if (lp.width == LayoutParams.MATCH_PARENT) {
                                childWidthSpec = MeasureSpec.makeMeasureSpec(maxLayoutWidth, MeasureSpec.EXACTLY);
                            } else {
                                childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
                            }
                        } else {
                            childWidthSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(),
                                    MeasureSpec.EXACTLY);
                        }

                        final int childHeightSpec = MeasureSpec.makeMeasureSpec(fixedPanelHeightLimit, MeasureSpec.EXACTLY);
                        child.measure(childWidthSpec, childHeightSpec);
                    }
                } else if (lp.weight > 0) {
                    int childWidthSpec;
                    if (lp.height == 0) {
                        // This was skipped the first time; figure out a real width spec.
                        if (lp.width == LayoutParams.WRAP_CONTENT) {
                            childWidthSpec = MeasureSpec.makeMeasureSpec(maxLayoutWidth, MeasureSpec.AT_MOST);
                        } else if (lp.width == LayoutParams.MATCH_PARENT) {
                            childWidthSpec = MeasureSpec.makeMeasureSpec(maxLayoutWidth, MeasureSpec.EXACTLY);
                        } else {
                            childWidthSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), MeasureSpec.EXACTLY);
                        }
                    } else {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), MeasureSpec.EXACTLY);
                    }

                    if (canSlide) {
                        final int verticalMargin = lp.topMargin + lp.bottomMargin;
                        final int newHeight = heightAvailable - verticalMargin;
                        final int childHeightSpec = MeasureSpec.makeMeasureSpec(newHeight,
                                MeasureSpec.EXACTLY);
                        if (measuredHeight != newHeight) {
                            child.measure(childWidthSpec, childHeightSpec);
                        }
                    } else {
                        // Distribute the extra height proportionally similar to LinearLayout
                        final int heightToDistribute = Math.max(0, heightRemaining);
                        final int addedHeight = (int) (lp.weight * heightToDistribute / weightSum);
                        final int childHeightSpec = MeasureSpec.makeMeasureSpec(
                                measuredHeight + addedHeight, MeasureSpec.EXACTLY);
                        child.measure(childWidthSpec, childHeightSpec);
                    }
                }
            }
        }

        final int measuredHeight = heightSize;
        final int measuredWidth = layoutWidth + getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(measuredWidth, measuredHeight);
        mCanSlide = canSlide;

        if (mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE && !canSlide) {
            // Cancel scrolling in progress, it's no longer relevant.
            mDragHelper.abort();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);

        final int height = b - t;
        final int paddingStart = getPaddingTop();
        final int paddingEnd = getPaddingBottom();
        final int paddingLeft = getPaddingLeft();

        final int childCount = getChildCount();
        int yStart = paddingStart;
        int nextYStart = yStart;

        if (mFirstLayout) {
            mSlideOffset = mCanSlide && mPreservedOpenState ? 1.f : 0.f;
        }

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            final int childHeight = child.getMeasuredHeight();
            int offset = 0;

            if (lp.slideable) {
                final int margin = lp.topMargin + lp.bottomMargin;
                final int range = Math.min(nextYStart,
                        height - paddingEnd - mOverhangSize) - yStart - margin;
                mSlideRange = range;
                final int lpMargin = lp.topMargin;
                lp.dimWhenOffset = yStart + lpMargin + range + childHeight / 2 >
                        height - paddingEnd;
                final int pos = (int) (range * mSlideOffset);
                yStart += pos + lpMargin;
                mSlideOffset = (float) pos / mSlideRange;
            } else if (mCanSlide && mParallaxBy != 0) {
                offset = (int) ((1 - mSlideOffset) * mParallaxBy);
                yStart = nextYStart;
            } else {
                yStart = nextYStart;
            }

            final int childTop = yStart - offset;
            final int childBottom = childTop + childHeight;

            final int childLeft = paddingLeft;
            final int childRight = childLeft + child.getMeasuredWidth();
            child.layout(paddingLeft, childTop, childRight, childBottom);

            nextYStart += child.getHeight();
        }

        if (mFirstLayout) {
            if (mCanSlide) {
                if (mParallaxBy != 0) {
                    parallaxOtherViews(mSlideOffset);
                }
                if (((LayoutParams) mSlideableView.getLayoutParams()).dimWhenOffset) {
                    dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
                }
            } else {
                // Reset the dim level of all children; it's irrelevant when nothing moves.
                for (int i = 0; i < childCount; i++) {
                    dimChildView(getChildAt(i), 0, mSliderFadeColor);
                }
            }
            updateObscuredViewsVisibility(mSlideableView);
        }

        mFirstLayout = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Recalculate sliding panes and their details
        if (w != oldw) {
            mFirstLayout = true;
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (!isInTouchMode() && !mCanSlide) {
            mPreservedOpenState = child == mSlideableView;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        // Preserve the open state based on the last view that was touched.
        if (!mCanSlide && action == MotionEvent.ACTION_DOWN && getChildCount() > 1) {
            // After the first things will be slideable.
            final View secondChild = getChildAt(1);
            if (secondChild != null) {
                mPreservedOpenState = !mDragHelper.isViewUnder(secondChild,
                        (int) ev.getX(), (int) ev.getY());
            }
        }

        if (!mCanSlide || (mIsUnableToDrag && action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
        }

        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mIsUnableToDrag = false;
                final float x = ev.getX();
                final float y = ev.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;

                if (mDragHelper.isViewUnder(mSlideableView, (int) x, (int) y) &&
                        isDimmed(mSlideableView)) {
                    interceptTap = true;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = mDragHelper.getTouchSlop();
                if (ady > slop && adx > ady) {
                    mDragHelper.cancel();
                    mIsUnableToDrag = true;
                    return false;
                }
            }
        }

        final boolean interceptForDrag = mDragHelper.shouldInterceptTouchEvent(ev);

        return interceptForDrag || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mEdgeSize > 0) {
            if (!isOpen() && event.getAction() == MotionEvent.ACTION_DOWN && event.getY() > mOverhangSize) {
                return false;
            }
        }

        if (!mCanSlide) return super.onTouchEvent(event);

        mDragHelper.processTouchEvent(event);

        final int action = event.getAction();
        boolean wantTouchEvents = true;

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = event.getX();
                final float y = event.getY();
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (isDimmed(mSlideableView)) {
                    final float x = event.getX();
                    final float y = event.getY();
                    final float dx = x - mInitialMotionX;
                    final float dy = y - mInitialMotionY;
                    final int slop = mDragHelper.getTouchSlop();
                    if (dx * dx + dy * dy < slop * slop &&
                            mDragHelper.isViewUnder(mSlideableView, (int) x, (int) y)) {
                        // Taps close a dimmed open pane.
                        closePane(mSlideableView, 0);
                        break;
                    }

                }
                break;
            }
        }

        return wantTouchEvents;
    }

    private boolean closePane(View pane, int initialVelocity) {
        if (mFirstLayout || smoothSlideTo(0.f, initialVelocity)) {
            mPreservedOpenState = false;
            return true;
        }

        return false;
    }

    private boolean openPane(View pane, int initialVelocity) {
        if (mFirstLayout || smoothSlideTo(1.f, initialVelocity)) {
            mPreservedOpenState = true;
            return true;
        }

        return false;
    }

    public boolean openPane() {
        return openPane(mSlideableView, 0);
    }

    public boolean closePane() {
        return closePane(mSlideableView, 0);
    }

    public boolean isOpen() {
        return !mCanSlide || mSlideOffset == 1;
    }

    public boolean isSlideable() {
        return mCanSlide;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        boolean result;
        final int save = canvas.save(Canvas.CLIP_SAVE_FLAG);

        if (mCanSlide && !lp.slideable && mSlideableView != null) {
            // Clip against the slider; no sense drawing what will immediately be covered.
            canvas.getClipBounds(mTmpRect);
            mTmpRect.bottom = Math.min(mTmpRect.bottom, mSlideableView.getTop());
            canvas.clipRect(mTmpRect);
        }

        if (Build.VERSION.SDK_INT >= 11) {
            result = super.drawChild(canvas, child, drawingTime);
        } else {
            if (lp.dimWhenOffset && mSlideOffset > 0) {
                if (!child.isDrawingCacheEnabled()) {
                    child.setDrawingCacheEnabled(true);
                }
                final Bitmap cache = child.getDrawingCache();
                if (cache != null) {
                    canvas.drawBitmap(cache, child.getLeft(), child.getTop(), lp.dimPaint);
                    result = false;
                } else {
                    result = super.drawChild(canvas, child, drawingTime);
                }
            } else {
                if (child.isDrawingCacheEnabled()) {
                    child.setDrawingCacheEnabled(false);
                }
                result = super.drawChild(canvas, child, drawingTime);
            }
        }

        canvas.restoreToCount(save);

        return result;
    }

    private void invalidateChildRegion(View v) {
        IMPL.invalidateChildRegion(this, v);
    }

    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!mCanSlide) return false;

        final LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();

        int startBound = getPaddingTop() + lp.topMargin;
        int y = (int) (startBound + slideOffset * mSlideRange);

        if (mDragHelper.smoothSlideViewTo(mSlideableView, mSlideableView.getLeft(), y)) {
            setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }

        return false;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            if (!mCanSlide) {
                mDragHelper.abort();
                return;
            }

            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setShadowDrawableTop(Drawable d) {
        mShadowDrawableTop = d;
    }

    public void setShadowResourceTop(int resId) {
        setShadowDrawableTop(getResources().getDrawable(resId));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Drawable shadowDrawable = mShadowDrawableTop;

        final View shadowView = getChildCount() > 1 ? getChildAt(1) : null;
        if (shadowView == null || shadowDrawable == null) return;

        final int left = shadowView.getLeft();
        final int right = shadowView.getRight();

        final int shadowHeight = shadowDrawable.getIntrinsicHeight();
        final int top;
        final int bottom;

        bottom = shadowView.getTop();
        top = bottom - shadowHeight;

        shadowDrawable.setBounds(left, top, right, bottom);
        shadowDrawable.draw(canvas);
    }

    protected boolean canScroll(View v, boolean checkV, int dy, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();

            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // TODO: Add versioned support here for transformed views.
                // This will not work for transformed views in Honeycomb+
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() &&
                        y + scrollY >= child.getTop() && y + scrollY < child.getBottom() &&
                        canScroll(child, true, dy, x + scrollX - child.getLeft(),
                                y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return checkV && ViewCompat.canScrollVertically(v, -dy);
    }

    boolean isDimmed(View child) {
        if (child == null) return false;

        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return mCanSlide && lp.dimWhenOffset && mSlideOffset > 0;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams ? new LayoutParams((MarginLayoutParams) p)
                : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        ss.isOpen = isSlideable() ? isOpen() : mPreservedOpenState;

        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(state);

        if (ss.isOpen) {
            openPane();
        } else {
            closePane();
        }
        mPreservedOpenState = ss.isOpen;
    }

    static class SavedState extends BaseSavedState {
        boolean isOpen;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel source) {
            super(source);
            isOpen = source.readInt() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isOpen ? 1 : 0);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    private class DisableLayerRunnable implements Runnable {
        final View mChildView;

        DisableLayerRunnable(View childView) {
            mChildView = childView;
        }

        @Override
        public void run() {
            if (mChildView.getParent() == SlidingUpPaneLayout.this) {
                ViewCompat.setLayerType(mChildView, ViewCompat.LAYER_TYPE_NONE, null);
                invalidateChildRegion(mChildView);
            }
            mPostedRunnables.remove(this);
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        private static final int[] ATTRS = new int[]{
                android.R.attr.layout_weight
        };

        /**
         * The weighted proportion of how much of the leftover space
         * this child should consume after measurement.
         */
        public float weight = 0;

        /**
         * True if this pane is the slideable pane in the layout.
         */
        boolean slideable;

        /**
         * True if this view should be drawn dimmed
         * when it's been offset from its default position.
         */
        boolean dimWhenOffset;

        Paint dimPaint;

        public LayoutParams() {
            super(MATCH_PARENT, MATCH_PARENT);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.weight = source.weight;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            final TypedArray a = c.obtainStyledAttributes(attrs, ATTRS);
            this.weight = a.getFloat(0, 0);
            a.recycle();
        }
    }

    interface SlidingPanelLayoutImpl {
        void invalidateChildRegion(SlidingUpPaneLayout parent, View child);
    }

    static class SlidingPanelLayoutImplBase implements SlidingPanelLayoutImpl {
        @Override
        public void invalidateChildRegion(SlidingUpPaneLayout parent, View child) {
            ViewCompat.postInvalidateOnAnimation(parent, child.getLeft(), child.getTop(),
                    child.getRight(), child.getBottom());
        }
    }

    static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase {
        /*
         * Private API hacks! Nasty! Bad!
         *
         * In Jellybean, some optimizations in the hardware UI renderer
         * prevent a changed Paint on a View using a hardware layer from having
         * the intended effect. This twiddles some internal bits on the view to force
         * it to recreate the display list.
         */
        private Method mGetDisplayList;
        private Field mRecreateDisplayList;

        SlidingPanelLayoutImplJB() {
            try {
                mGetDisplayList = View.class.getDeclaredMethod("getDisplayList", (Class[]) null);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Couldn't fetch getDisplayList method; dimming won't work right.", e);
            }
            try {
                mRecreateDisplayList = View.class.getDeclaredField("mRecreateDisplayList");
                mRecreateDisplayList.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e);
            }
        }

        @Override
        public void invalidateChildRegion(SlidingUpPaneLayout parent, View child) {
            if (mGetDisplayList != null && mRecreateDisplayList != null) {
                try {
                    mRecreateDisplayList.setBoolean(child, true);
                    mGetDisplayList.invoke(child, (Object[]) null);
                } catch (Exception e) {
                    Log.e(TAG, "Error refreshing display list state", e);
                }
            } else {
                // Slow path. REALLY slow path. Let's hope we don't get here.
                child.invalidate();
                return;
            }
            super.invalidateChildRegion(parent, child);
        }
    }

    static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase {
        @Override
        public void invalidateChildRegion(SlidingUpPaneLayout parent, View child) {
            ViewCompat.setLayerPaint(child, ((LayoutParams) child.getLayoutParams()).dimPaint);
        }
    }

    class AccessibilityDelegate extends AccessibilityDelegateCompat {
        private final Rect mTmpRect = new Rect();

        @Override
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            final AccessibilityNodeInfoCompat superNode = AccessibilityNodeInfoCompat.obtain(info);
            super.onInitializeAccessibilityNodeInfo(host, superNode);
            copyNodeInfoNoChildren(info, superNode);
            superNode.recycle();

            info.setClassName(SlidingUpPaneLayout.class.getName());
            info.setSource(host);

            final ViewParent parent = ViewCompat.getParentForAccessibility(host);
            if (parent instanceof View) {
                info.setParent((View) parent);
            }

            // This is a best-approximation of addChildrenForAccessibility()
            // that accounts for filtering.
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                if (!filter(child) && (child.getVisibility() == View.VISIBLE)) {
                    // Force importance to "yes" since we can't read the value.
                    ViewCompat.setImportantForAccessibility(
                            child, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
                    info.addChild(child);
                }
            }
        }

        @Override
        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);

            event.setClassName(SlidingUpPaneLayout.class.getName());
        }

        @Override
        public boolean onRequestSendAccessibilityEvent(ViewGroup host, View child,
                                                       AccessibilityEvent event) {
            if (!filter(child)) {
                return super.onRequestSendAccessibilityEvent(host, child, event);
            }
            return false;
        }

        public boolean filter(View child) {
            return isDimmed(child);
        }

        /**
         * This should really be in AccessibilityNodeInfoCompat, but there unfortunately
         * seem to be a few elements that are not easily cloneable using the underlying API.
         * Leave it private here as it's not general-purpose useful.
         */
        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat dest,
                                            AccessibilityNodeInfoCompat src) {
            final Rect rect = mTmpRect;

            src.getBoundsInParent(rect);
            dest.setBoundsInParent(rect);

            src.getBoundsInScreen(rect);
            dest.setBoundsInScreen(rect);

            dest.setVisibleToUser(src.isVisibleToUser());
            dest.setPackageName(src.getPackageName());
            dest.setClassName(src.getClassName());
            dest.setContentDescription(src.getContentDescription());

            dest.setEnabled(src.isEnabled());
            dest.setClickable(src.isClickable());
            dest.setFocusable(src.isFocusable());
            dest.setFocused(src.isFocused());
            dest.setAccessibilityFocused(src.isAccessibilityFocused());
            dest.setSelected(src.isSelected());
            dest.setLongClickable(src.isLongClickable());

            dest.addAction(src.getActions());

            dest.setMovementGranularities(src.getMovementGranularities());
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (mIsUnableToDrag) return false;

            return ((LayoutParams) child.getLayoutParams()).slideable;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                if (mSlideOffset == 0) {
                    updateObscuredViewsVisibility(mSlideableView);
                    dispatchOnPanelClosed(mSlideableView);
                    mPreservedOpenState = false;
                } else {
                    dispatchOnPanelOpened(mSlideableView);
                    mPreservedOpenState = true;
                }
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            setAllChildrenVisible();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            onPanelDragged(top);
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final LayoutParams lp = (LayoutParams) releasedChild.getLayoutParams();

            int top = getPaddingTop() + lp.topMargin;
            if (yvel > 0 || (yvel == 0 && mSlideOffset > 0.5f)) {
                top += mSlideRange;
            }

            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mSlideRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return child.getLeft();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();

            int startBound = getPaddingTop() + lp.topMargin;
            int endBound = startBound + mSlideRange;
            final int newTop = Math.min(Math.max(top, startBound), endBound);

            return newTop;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mDragHelper.captureChildView(mSlideableView, pointerId);
        }
    }
}
