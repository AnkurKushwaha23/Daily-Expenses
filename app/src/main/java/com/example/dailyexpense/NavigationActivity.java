package com.example.dailyexpense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dailyexpense.Adapter.ViewPagerAdapter;

public class NavigationActivity extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "Login";
    ViewPager sliderViewPager;
    LinearLayout dotIndicator;
    ViewPagerAdapter viewPagerAdapter;
    Button backButton, skipButton, nextButton;
    TextView[] dots;

    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Handle page scrolling if needed
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onPageSelected(int position) {
            setDotIndicator(position);

            backButton.setVisibility(position > 0 ? View.VISIBLE : View.INVISIBLE);

            if (position == viewPagerAdapter.getCount() - 1) {
                nextButton.setText("Start");
            } else {
                nextButton.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Handle page scroll state changes if needed
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = sliderViewPager.getCurrentItem();
                if (currentItem > 0) {
                    sliderViewPager.setCurrentItem(currentItem - 1, true);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = sliderViewPager.getCurrentItem();
                int lastItemIndex = viewPagerAdapter.getCount() - 1;

                if (currentItem < lastItemIndex) {
                    sliderViewPager.setCurrentItem(currentItem + 1, true);
                } else {
                    startActivity(new Intent(NavigationActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this, MainActivity.class));
                finish();
            }
        });

        sliderViewPager = findViewById(R.id.slideViewPager);
        dotIndicator = findViewById(R.id.dotIndicator);

        viewPagerAdapter = new ViewPagerAdapter(this);
        sliderViewPager.setAdapter(viewPagerAdapter);
        setDotIndicator(0);
        sliderViewPager.addOnPageChangeListener(viewPagerListener);
    }

    private void setDotIndicator(int position) {
        dots = new TextView[viewPagerAdapter.getCount()];
        dotIndicator.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(i == position ? R.color.black : R.color.bg, getApplicationContext().getTheme()));
            dotIndicator.addView(dots[i]);
        }
    }
}
