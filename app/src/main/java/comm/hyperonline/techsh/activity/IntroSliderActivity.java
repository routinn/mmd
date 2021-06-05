package comm.hyperonline.techsh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import comm.hyperonline.techsh.R;
import comm.hyperonline.techsh.adapter.IntroViewPagerAdapter;
import comm.hyperonline.techsh.utils.BaseActivity;
import comm.hyperonline.techsh.utils.Config;
import comm.hyperonline.techsh.utils.Constant;
import comm.hyperonline.techsh.utils.RequestParamUtils;
import comm.hyperonline.techsh.customview.textview.TextViewRegular;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroSliderActivity extends BaseActivity {

    @BindView(R.id.vpIntro)
    ViewPager vpIntro;

    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;

    @BindView(R.id.tvNext)
    TextViewRegular tvNext;

    private IntroViewPagerAdapter introViewPagerAdapter;
    private int currentPosition = 0;
    private ImageView[] dots;
    private boolean isSkip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        setBannerView();
    }


    //ToDo : set view pager
    private void setBannerView() {
        introViewPagerAdapter = new IntroViewPagerAdapter(this);
        vpIntro.setAdapter(introViewPagerAdapter);
        addBottomDots(0, introViewPagerAdapter.getCount());
        vpIntro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, vpIntro.getAdapter().getCount());
                currentPosition = position;

                if (currentPosition == 2) {
                    tvNext.setText(getResources().getString(R.string.done));
                    tvNext.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
                } else {
                    tvNext.setTextColor(getResources().getColor(R.color.gray));
                    tvNext.setText(getResources().getString(R.string.next));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(Config.IS_RTL) {

            vpIntro.setRotationY(0);
        }else {
            vpIntro.setRotationY(180);
        }

    }

    @OnClick(R.id.tvNext)
    public void tvNextClick() {

        if (tvNext.getText().toString().contains(getResources().getString(R.string.done))) {

            sendIntent();

        } else {
            vpIntro.setCurrentItem(currentPosition + 1);
            if (currentPosition == 2) {
                tvNext.setText(getResources().getString(R.string.done));
                tvNext.setTextColor(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
            } else {
                tvNext.setTextColor(getResources().getColor(R.color.gray));
                tvNext.setText(getResources().getString(R.string.next));
            }
        }

    }

    @OnClick(R.id.tvSkip)
    public void tvSkipClick() {
        sendIntent();
    }

    private void sendIntent(){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(RequestParamUtils.IS_INTRO_SHOWED, true);
        editor.commit();
        isSkip = true;
        if (sharedpreferences.getBoolean(RequestParamUtils.LOGIN_SHOW_IN_APP_START, false)) {
            Intent intent = new Intent(this, LogInActivity.class);
            intent.putExtra("is_splash", true);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //ToDo : add dot  dynemically in bottom of view pager
    private void addBottomDots(int currentPage, int lenght) {
        layoutDots.removeAllViews();
        dots = new ImageView[lenght];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.ic_intro_unfill));
            dots[i].setPadding(0, 0, 10, 0);

            layoutDots.addView(dots[i]);
            dots[i].setColorFilter(Color.parseColor(getPreferences().getString(Constant.APP_COLOR, Constant.PRIMARY_COLOR)));
        }
        if (dots.length > 0 && dots.length >= currentPage) {
            dots[currentPage].setImageDrawable(getResources().getDrawable(R.drawable.ic_intro_fill));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (isSkip)
            finish();
        if (!getPreferences().getString(RequestParamUtils.ID, "").equals("")) {
            startActivity(new Intent(IntroSliderActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(IntroSliderActivity.this, IntroSliderActivity.class));
        }
    }
}
