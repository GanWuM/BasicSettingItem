package ganwu.doing.views.setting;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.preference.Preference;
import ganwu.doing.R;
import ganwu.doing.utils.AnimationUtils;
import ganwu.doing.utils.DataUtils;
import ganwu.doing.utils.UnitUtils;

public class BasicSettingItem extends LinearLayout {
	
	View view;
	int  iconTint;
	int  textColor, backgroundColor;
	String title, content;
	Drawable                             icon;
	Preference.OnPreferenceClickListener onPreferenceClickListener;
	int                                  radius;
	
	public BasicSettingItem(Context context, @Nullable AttributeSet attributeSet) {
		super(context, attributeSet);
		
		TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.BasicSettingItem);
		
		view = LayoutInflater.from(context).inflate(R.layout.basic_setting_item, this);
		
		CardView background = view.findViewById(R.id.background);
		try {
			backgroundColor = a.getColor(R.styleable.BasicSettingItem_bsi_backgroundColor, Color.parseColor("#00000000"));
		} catch (UnsupportedOperationException e) {
			TypedArray array = context.getTheme().obtainStyledAttributes(android.R.style.TextAppearance_Large, new int[]{R.attr.basic_background});
			backgroundColor = array.getColor(0, Color.parseColor("#FFFFFF"));
		}
		background.setCardBackgroundColor(backgroundColor);
		
		try {
			radius = a.getDimensionPixelSize(R.styleable.BasicSettingItem_bsi_radius, UnitUtils.dpToPx(Math.round((float) DataUtils.getIntSetting("pref_29", 30) / 3f * 2f)));
		} catch (NullPointerException | NumberFormatException e) {
			radius = isInEditMode() ? 40 : UnitUtils.dpToPx(20);
		}
		background.setRadius(radius);
		
		ImageView icon = view.findViewById(R.id.icon);
		this.icon = a.getDrawable(R.styleable.BasicSettingItem_bsi_icon);
		icon.setImageDrawable(this.icon);
		icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		
		try {
			iconTint = a.getColor(R.styleable.BasicSettingItem_bsi_iconTint, Color.parseColor("#000000"));
		} catch (UnsupportedOperationException e) {
			TypedArray array = context.getTheme().obtainStyledAttributes(android.R.style.TextAppearance_Large, new int[]{R.attr.basic_icontint});
			iconTint = array.getColor(0, Color.parseColor("#000000"));
		}
		
		try {
			icon.setImageTintList(ColorStateList.valueOf(iconTint));
		} catch (NullPointerException ignore) {
		
		}
		
		try {
			int[] attrs = new int[]{R.attr.darkmode_textcolor};
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs);
			int color = typedArray.getColor(0, 0xffffffff);
			typedArray.recycle();
			
			textColor = a.getColor(R.styleable.BasicSettingItem_bsi_textColor, color);
		} catch (UnsupportedOperationException e) {
			TypedArray array = context.getTheme().obtainStyledAttributes(android.R.style.TextAppearance_Large, new int[]{R.attr.textColor});
			iconTint = array.getColor(0, Color.parseColor("#000000"));
		}
		
		TextView title = view.findViewById(R.id.title);
		title.setVisibility(View.VISIBLE);
		try {
			this.title = a.getText(R.styleable.BasicSettingItem_bsi_title).toString();
			title.setTextColor(textColor);
		} catch (NullPointerException e) {
			this.title = "";
			title.setVisibility(View.GONE);
		}
		title.setText(this.title);
		
		TextView content = view.findViewById(R.id.content);
		try {
			this.content = a.getText(R.styleable.BasicSettingItem_bsi_content).toString();
			content.setTextColor(textColor);
		} catch (NullPointerException e) {
			this.content = "";
			content.setVisibility(View.GONE);
		}
		content.setText(this.content);
		
		AnimationUtils.initTouchAnimation(findViewById(R.id.main));
		
		findViewById(R.id.main).setOnClickListener(view -> {
			if (! findViewById(R.id.main).isEnabled()) {
				return;
			}
			onPreferenceClickListener.onPreferenceClick(new Preference(getContext()));
		});
		
		onPreferenceClickListener = preference -> false;
		
		a.recycle();
	}
	
	public void setSummary(String summary) {
		this.content = summary;
		TextView content = view.findViewById(R.id.content);
		content.setText(summary);
		content.setVisibility(summary.equals("") ? View.GONE : View.VISIBLE);
	}
	
	public void setOnPreferenceClickListener(Preference.OnPreferenceClickListener onPreferenceClickListener) {
		this.onPreferenceClickListener = onPreferenceClickListener;
	}
	
	public void setSubview(View view1) {
		LinearLayout subview = findViewById(R.id.subview);
		subview.removeAllViews();
		subview.addView(view1);
	}
	
	public View getSubview() {
		LinearLayout subview = findViewById(R.id.subview);
		return subview.getChildAt(0);
	}
	
	public String getSummary(){
		TextView content = view.findViewById(R.id.content);
		return content.getText().toString();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		findViewById(R.id.main).setEnabled(enabled);
		
		if (enabled) {
			findViewById(R.id.main).setAlpha(1f);
		} else {
			findViewById(R.id.main).setAlpha(0.4f);
		}
	}
	
}
