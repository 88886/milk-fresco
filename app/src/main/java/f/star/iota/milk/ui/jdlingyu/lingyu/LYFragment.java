package f.star.iota.milk.ui.jdlingyu.lingyu;


import android.os.Bundle;

import f.star.iota.milk.base.ScrollImageFragment;


public class LYFragment extends ScrollImageFragment<LYPresenter, LYAdapter> {

    public static LYFragment newInstance(String url) {
        LYFragment fragment = new LYFragment();
        Bundle bundle = new Bundle();
        bundle.putString("base_url", url+"/");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected LYPresenter getPresenter() {
        return new LYPresenter(this);
    }

    @Override
    protected LYAdapter getAdapter() {
        return new LYAdapter();
    }

    @Override
    protected boolean isHideFab() {
        return false;
    }
}
