package io.github.smartsportapps.app1.CaredView;

public class CardItem {

    private int mImageResource;
    private int mTitleResource;

    public CardItem(int title, int image) {
        mTitleResource = title;
        mImageResource = image;
    }

    public int getImage() {
        return mImageResource;
    }

    public int getTitle() {
        return mTitleResource;
    }
}
