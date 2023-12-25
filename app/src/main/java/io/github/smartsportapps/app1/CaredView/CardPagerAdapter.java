package io.github.smartsportapps.app1.CaredView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.smartsportapps.app1.ActiviteAccueil;
import io.github.smartsportapps.app1.ActiviteStatistiques;
import io.github.smartsportapps.app1.Alarm.AlarmActivity;
import io.github.smartsportapps.app1.R;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private final int ZERO = 0;
    private final int PREMIER_JOUR_D_ENTRAINEMENT = 1;
    private final int DERNIER_JOUR_D_ENTRAINEMENT = 115;
    private final int DELAIS_ATTENTE = 2000;
    private final String str_nomDesPreferences = "preferences";
    private final String str_TotalDeTractions = "totalDeTractions";
    private final String str_NombreDeTractions = "nombreDeTractions";
    private final String str_PremierChargement = "premierChargement";
    private final String str_JourEnCours = "jourEnCours";
    private final String str_ad = "compteur_ad";
    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    private Context context;
    private SharedPreferences preferencesPartagees;
    private Handler handler;

    public CardPagerAdapter(Context context) {
        this.context = context;
    }

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        Button btm = (Button) view.findViewById(R.id.btn_card);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (position) {
                    case 0: {
                        Intent intent = new Intent(v.getContext(), ActiviteStatistiques.class);
                        v.getContext().startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent alarme = new Intent(v.getContext(), AlarmActivity.class);
                        v.getContext().startActivity(alarme);
                        break;
                    }
                    case 2: {
                        ((ActiviteAccueil) v.getContext()).resetData(view);
                        break;
                    }
                }
            }
        };

        btm.setOnClickListener(onClickListener);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItem item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        Button cButton = (Button) view.findViewById(R.id.btn_card);

        titleTextView.setText(item.getTitle());
        cButton.setBackgroundResource(item.getImage());

        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
