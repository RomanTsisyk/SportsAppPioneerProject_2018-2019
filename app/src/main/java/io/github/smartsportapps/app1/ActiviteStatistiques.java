package io.github.smartsportapps.app1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import io.github.smartsportapps.app1.Statistiques.StatByDayFragment;
import io.github.smartsportapps.app1.Statistiques.StatByLevelFragment;
import io.github.smartsportapps.app1.viewpagerindicator.CirclePageIndicator;
import io.github.smartsportapps.app1.viewpagerindicator.IconPagerAdapter;

public class ActiviteStatistiques extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.circlePageIndicator);
        circlePageIndicator.setViewPager(pager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent accueil = new Intent(ActiviteStatistiques.this, ActiviteAccueil.class);
        accueil.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(accueil);
        finish();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return StatByDayFragment.newInstance("StatByDayFragment, Instance 1");
                case 1:
                    return StatByLevelFragment.newInstance("StatByLevelFragment, Instance 1");
                default:
                    return StatByDayFragment.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getIconResId(int index) {
            return 0;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
