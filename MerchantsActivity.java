package com.insurance.ecoinsoft.app.merchants;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.insurance.ecoinsoft.app.BaseActivity;
import com.insurance.ecoinsoft.app.R;
import com.insurance.ecoinsoft.app.merchantpromotiondetails.MerchantspromotionDetailActivity;
import com.insurance.ecoinsoft.app.model.response.MerchantCategoriesResponse;
import com.insurance.ecoinsoft.app.model.response.PromotionsResponse;
import com.insurance.ecoinsoft.app.pref.AppSharedPreferences;
import com.insurance.ecoinsoft.app.pref.Constant;
import com.insurance.ecoinsoft.app.util.CustomizeIntent;
import com.insurance.ecoinsoft.app.util.CustomizeToast;
import com.insurance.ecoinsoft.app.util.NetworkManager;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MerchantsActivity extends BaseActivity implements MerchantsInterface.View, MerchantsCategoriesRecyclerviewAdapter.onItemClickListener, PromotionsRecyclerviewAdapter.onItemClickListener {
    private RecyclerView mMerchantsRecyclerView;
    public static PromotionsRecyclerviewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static ArrayList<PromotionsResponse.Data> mPromotionsDataModels;
    private RecyclerView mCategoriesTabRecyclerView;
    private RecyclerView.Adapter mCategoriesAdapter;
    private RecyclerView.LayoutManager mCategoriesLayoutManager;
    private ArrayList<MerchantCategoriesResponse.Data> mCategoriesDataModels;
    private SearchView mSearchView;
    private PromotionsRecyclerviewAdapter adapter;

    public static MerchantsInterface.Presenter mPresenter;
    private int categoriesID;

    public static TextView mAllCategoriesTextView;
    private String mAllCategories;
    public static boolean mSelect = false;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchants);
        setPresenter(new MerchantsPresenter(this, this));
        onSetUpBackPressGeneralToolbar(getResources().getString(R.string.our_merchant));
        MerchantsCategoriesRecyclerviewAdapter.row_index = -1;
        initCategoriesTab();
        initPromotions();
        onSearchView();
        if (NetworkManager.getInstance().isNetworkConnected(this)) {
            showProgressBar(true);
            mPresenter.loadingMerchantsCategories();
            mPresenter.loadingPromotions(0);
        }
        mAllCategoriesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadingPromotions(1);
                mAllCategoriesTextView.setTextColor(Color.parseColor("#ffffff"));
                mAllCategoriesTextView.setBackgroundResource(R.drawable.textview_bg_red);
                for (int i = 0; i < mCategoriesDataModels.size(); i++) {
                    mCategoriesDataModels.get(i).setSelected(false);
                    mCategoriesAdapter.notifyDataSetChanged();
                    MerchantsCategoriesRecyclerviewAdapter.row_index = -1;
                }
                mPromotionsDataModels.clear();
            }
        });
    }

    private void onSearchView() {
        mSearchView = (SearchView) findViewById(R.id.categoriestab_searchview);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!mSearchView.isIconified()) {
                    mSearchView.setIconified(true);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mPresenter.loadingPromotionFilter(s);
                mPromotionsDataModels.clear();
                mAdapter.notifyDataSetChanged();
//                final List<PromotionsResponse.Data> filtermodelist = filter(mPromotionsDataModels, s);
//                mAdapter.setfilter(filtermodelist);
                return true;
            }
        });
    }

    private List<PromotionsResponse.Data> filter(List<PromotionsResponse.Data> filterList, String query) {
        query = query.toLowerCase();
        final List<PromotionsResponse.Data> filteredModelList = new ArrayList<>();
        for (PromotionsResponse.Data model : filterList) {
            final String searchViewText = model.getName().toLowerCase();
            if (searchViewText.contains(query)) {
                filteredModelList.add(model);
            }
            if (searchViewText.equals("")) {
                CustomizeToast.getInstance(getApplicationContext()).showToast(getApplicationContext(), "SS");
            }
        }
        return filteredModelList;
    }


    private void initPromotions() {
        mProgressBar = (ProgressBar) findViewById(R.id.merchants_progressbar);
        mMerchantsRecyclerView = (RecyclerView) findViewById(R.id.merchants_recyclerview);

        mMerchantsRecyclerView.setHasFixedSize(true);
        mMerchantsRecyclerView.setFocusable(false);

        mLayoutManager = new LinearLayoutManager(this);
        mMerchantsRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mMerchantsRecyclerView.addItemDecoration(dividerItemDecoration);

        mPromotionsDataModels = new ArrayList<>();
        mAdapter = new PromotionsRecyclerviewAdapter(this, mPromotionsDataModels, this);
        mMerchantsRecyclerView.setAdapter(mAdapter);
    }

    private void initCategoriesTab() {
        mAllCategoriesTextView = (TextView) findViewById(R.id.allcategories_textview);
        mCategoriesTabRecyclerView = (RecyclerView) findViewById(R.id.categoirestab_recyclerview);
        mCategoriesTabRecyclerView.setHasFixedSize(true);
        mCategoriesTabRecyclerView.setFocusable(false);

        mCategoriesLayoutManager = new LinearLayoutManager(getApplicationContext());

        mCategoriesTabRecyclerView.setLayoutManager(mCategoriesLayoutManager);
        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCategoriesTabRecyclerView.setLayoutManager(HorizontalLayout);
        mCategoriesDataModels = new ArrayList<>();

        mCategoriesAdapter = (new MerchantsCategoriesRecyclerviewAdapter(getApplicationContext(), mCategoriesDataModels, this));

        mCategoriesTabRecyclerView.setAdapter(mCategoriesAdapter);

    }

    @Override
    public void onShowProgressDialog() {

    }

    @Override
    public void onHideProgressDialog() {

    }

    @Override
    public void onShowMerchantsCategoriesDataSuccess(JSONObject object) {
        if (BaseActivity.isResponseSuccess(object)) {
            Gson mGson = new Gson();
            MerchantCategoriesResponse merchantResponse = mGson.fromJson(object.toString(), MerchantCategoriesResponse.class);
            if (merchantResponse.getData().size() == 0 || merchantResponse.getData().isEmpty()) {
                CustomizeToast.getInstance(this).showToast(this, "No data");
            } else {
                mCategoriesDataModels.addAll(merchantResponse.getData());
                mCategoriesAdapter.notifyDataSetChanged();
            }
        } else {
            CustomizeToast.getInstance(this).showToast(this, "Unable to get Data");
        }

    }

    @Override
    public void onShowMerchantsCategoriesDataError(Object object) {

    }

    @Override
    public void onShowPromotionsDataSuccess(JSONObject object) {
        if (BaseActivity.isResponseSuccess(object)) {
            Gson mGson = new Gson();
            PromotionsResponse promotionsResponse = mGson.fromJson(object.toString(), PromotionsResponse.class);
            if (promotionsResponse.getData().size() == 0 || promotionsResponse.getData().isEmpty()) {
                CustomizeToast.getInstance(this).showToast(this, "No Promotion");
            } else {
                mPromotionsDataModels.addAll(promotionsResponse.getData());
                mAdapter.notifyDataSetChanged();
            }
            showProgressBar(false);
        } else {
            CustomizeToast.getInstance(this).showToast(this, "Unable to Promotion");
        }
    }

    @Override
    public void onShowPromotionsDataError(Object object) {
        showProgressBar(false);
    }

    @Override
    public void onShowPromtionFilterDataSuccess(JSONObject object) {
        if (BaseActivity.isResponseSuccess(object)) {
            Gson mGson = new Gson();
            PromotionsResponse promotionsResponse = mGson.fromJson(object.toString(), PromotionsResponse.class);
            if (promotionsResponse.getData().size() == 0 || promotionsResponse.getData().isEmpty()) {
                CustomizeToast.getInstance(this).showToast(this, "Search Not found");
            } else {
                mPromotionsDataModels.addAll(promotionsResponse.getData());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onShowPromtoionFilterDataError(Object object) {

    }

    @Override
    public void setPresenter(MerchantsInterface.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClick(Context context, PromotionsResponse.Data promotionResponse) {
        CustomizeIntent customizeIntent = new CustomizeIntent(this);
        customizeIntent.startIntent(MerchantspromotionDetailActivity.class, promotionResponse);
    }

    @Override
    public void onClick(Context context, MerchantCategoriesResponse.Data categoriesDataModel) {
        if (NetworkManager.getInstance().isNetworkConnected(this)) {
            categoriesID = categoriesDataModel.getId();
            mPresenter.loadingPromotions(categoriesID);
            mPromotionsDataModels.clear();
            mAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_profile_user:
                CustomizeToast.getInstance(this).showToast(this, "Profile");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressBar(boolean isShow) {
        if (isShow) {
            mProgressBar.setVisibility(View.VISIBLE);
            mMerchantsRecyclerView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mMerchantsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
