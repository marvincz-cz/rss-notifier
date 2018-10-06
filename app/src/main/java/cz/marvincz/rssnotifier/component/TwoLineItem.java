package cz.marvincz.rssnotifier.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cz.marvincz.rssnotifier.R;

public class TwoLineItem extends FrameLayout {

    private TextView title;
    private TextView description;
    private ImageView icon;
    private ImageView actionIcon;

    public TwoLineItem(Context context) {
        super(context);
        init(context, null);
    }

    public TwoLineItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TwoLineItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        View view = inflate(context, R.layout.item_two_line, this);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        icon = view.findViewById(R.id.icon);
        actionIcon = view.findViewById(R.id.actionIcon);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OneLineItem,
                0, 0);

        if (typedArray.hasValue(R.styleable.TwoLineItem_title)) {
            title.setText(typedArray.getText(R.styleable.TwoLineItem_title));
        }

        if (typedArray.hasValue(R.styleable.TwoLineItem_description)) {
            description.setText(typedArray.getText(R.styleable.TwoLineItem_description));
        }

        if (typedArray.hasValue(R.styleable.TwoLineItem_icon)) {
            icon.setImageDrawable(typedArray.getDrawable(R.styleable.TwoLineItem_icon));
            icon.setVisibility(VISIBLE);
        } else {
            icon.setVisibility(GONE);
        }

        if (typedArray.hasValue(R.styleable.TwoLineItem_actionIcon)) {
            actionIcon.setImageDrawable(typedArray.getDrawable(R.styleable.TwoLineItem_actionIcon));
            actionIcon.setVisibility(VISIBLE);
        } else {
            actionIcon.setVisibility(GONE);
        }
    }

    public void setTitle(CharSequence text) {
        title.setText(text);
    }

    public void setTitle(@StringRes int text) {
        title.setText(text);
    }

    public void setDescription(CharSequence text) {
     description.setText(text);
    }

    public void setDescription(@StringRes int text) {
     description.setText(text);
    }

    public void setIcon(@Nullable Drawable drawable) {
        icon.setImageDrawable(drawable);
        icon.setVisibility((drawable == null) ? GONE : VISIBLE);
    }

    public void setIcon(@DrawableRes int resource) {
        icon.setImageResource(resource);
        icon.setVisibility((resource == 0) ? GONE : VISIBLE);
    }

    public void setActionIcon(@Nullable Drawable drawable) {
        actionIcon.setImageDrawable(drawable);
        actionIcon.setVisibility((drawable == null) ? GONE : VISIBLE);
    }

    public void setActionIcon(@DrawableRes int resource) {
        actionIcon.setImageResource(resource);
        actionIcon.setVisibility((resource == 0) ? GONE : VISIBLE);
    }
}