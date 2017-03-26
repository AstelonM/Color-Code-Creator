package com.mihai.colorcodecreator;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import android.widget.Toolbar;
import java.util.ArrayList;

public class SlidingActivity extends FragmentActivity {

    private ViewPager pager;
    private PagerAdapter adapter;
    private Toolbar toolbar;
    public static final String COD_CULORI = "culori";
    public static final String COD_CULORI_NOI = "culoriNoi";
    private ArrayList<CuloareSalvata> culoriBaza;
    private ArrayList<CuloareSalvata> culoriSalvate;
    private ArrayList<CuloareSalvata> culoriSalvateNoi;

    private class SlidingPagerAdapter extends FragmentStatePagerAdapter
    {
        public SlidingPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = new SlidingFragment();
            Bundle b = new Bundle();
            if(position < culoriBaza.size())
            {
                b.putInt(SlidingFragment.BUNDLE_COD, culoriBaza.get(position).getCod());
                b.putString(SlidingFragment.BUNDLE_NUME, culoriBaza.get(position).getNume());
            }
            else
            {
                position -= culoriBaza.size() - 1;
                if(position <culoriSalvate.size())
                {
                    b.putInt(SlidingFragment.BUNDLE_COD, culoriSalvate.get(position).getCod());
                    b.putString(SlidingFragment.BUNDLE_NUME, culoriSalvate.get(position).getNume());
                }
                else
                {
                    position -= culoriSalvate.size() - 1;
                    if(position < culoriSalvateNoi.size())
                    {
                        b.putInt(SlidingFragment.BUNDLE_COD, culoriSalvateNoi.get(position).getCod());
                        b.putString(SlidingFragment.BUNDLE_NUME, culoriSalvateNoi.get(position).getNume());
                    }
                }
            }
            f.setArguments(b);
            return f;
        }

        @Override
        public int getCount() {
            int nr = 0;
            if(culoriBaza != null)
                nr += culoriBaza.size();
            if(culoriSalvate != null)
                nr += culoriSalvate.size();
            if(culoriSalvateNoi != null)
                nr += culoriSalvateNoi.size();
            return nr;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if(position < culoriBaza.size())
                return culoriBaza.get(position).getNume();
            position -= culoriBaza.size() - 1;
            if(position <culoriSalvate.size())
                return culoriSalvate.get(position).getNume();
            position -= culoriSalvate.size() - 1;
            if(position < culoriSalvateNoi.size())
                return culoriSalvateNoi.get(position).getNume();
            return "Eroare";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);
        pager = (ViewPager) findViewById(R.id.pager);
        toolbar= (Toolbar) findViewById(R.id.slidingActionBar);
        Bundle sursa = getIntent().getExtras();
        culoriBaza = new ArrayList<>(11);
        culoriBaza.add(new CuloareSalvata(Color.BLACK, "Negru"));
        culoriBaza.add(new CuloareSalvata(Color.DKGRAY, "Gri Inchis"));
        culoriBaza.add(new CuloareSalvata(Color.GRAY, "Gri"));
        culoriBaza.add(new CuloareSalvata(Color.LTGRAY, "Gri Deschis"));
        culoriBaza.add(new CuloareSalvata(Color.WHITE, "Alb"));
        culoriBaza.add(new CuloareSalvata(Color.RED, "Rosu"));
        culoriBaza.add(new CuloareSalvata(Color.YELLOW, "Galben"));
        culoriBaza.add(new CuloareSalvata(Color.GREEN, "Verde"));
        culoriBaza.add(new CuloareSalvata(Color.CYAN, "Turcoaz"));
        culoriBaza.add(new CuloareSalvata(Color.BLUE, "Albastru"));
        culoriBaza.add(new CuloareSalvata(Color.MAGENTA, "Purpuriu Inchis"));
        culoriSalvate = (ArrayList<CuloareSalvata>) sursa.getSerializable(COD_CULORI);
        culoriSalvateNoi = (ArrayList<CuloareSalvata>) sursa.getSerializable(COD_CULORI_NOI);
        adapter = new SlidingPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        setActionBar(toolbar);
        ActionBar ab = getActionBar();
        try
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        catch(NullPointerException e)
        {
            CharSequence exceptie = "Eroare la incarcarea toolbarului";
            Toast.makeText(getApplicationContext(), exceptie, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

}
