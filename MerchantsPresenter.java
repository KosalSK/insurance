package com.insurance.ecoinsoft.app.merchants;

import android.content.Context;

import com.google.gson.JsonObject;
import com.insurance.ecoinsoft.app.model.request.MerchantCategoriesRequest;
import com.insurance.ecoinsoft.app.model.request.PromotionFilterRequest;
import com.insurance.ecoinsoft.app.model.request.PromotionsRequest;

import org.json.JSONObject;

public class MerchantsPresenter implements MerchantsInterface.Model, MerchantsInterface.Presenter {
    private Context mContext;
    private MerchantsInterface.View mView;

    public MerchantsPresenter(Context context, MerchantsInterface.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void loadingMerchantsCategories() {
        MerchantCategoriesRequest merchantRequest = new MerchantCategoriesRequest(this);
        merchantRequest.merchantRequest();

    }


    @Override
    public void onShowMerchantsCategoiesDataSuccess(JSONObject object) {
        mView.onShowMerchantsCategoriesDataSuccess(object);

    }

    @Override
    public void onShowMerchantsCategoriesDataError(Object object) {
        mView.onShowMerchantsCategoriesDataError(object);
    }

    @Override
    public void loadingPromotions(int num) {
        PromotionsRequest merchantRequest = new PromotionsRequest(this);
        merchantRequest.promotionsRequest(num);
    }

    @Override
    public void loadingPromotionFilter(String str) {
        PromotionFilterRequest promotionFilterRequest = new PromotionFilterRequest(this);
        promotionFilterRequest.promotionFilterRequest(str);
    }

    @Override
    public void onShowPromotionsDataSuccess(JSONObject object) {
        mView.onShowPromotionsDataSuccess(object);
    }

    @Override
    public void onShowPromotionsDataError(Object object) {
        mView.onShowPromotionsDataError(object);

    }

    @Override
    public void onShowPromtionFilterDataSuccess(JSONObject object) {
        mView.onShowPromtionFilterDataSuccess(object);
    }

    @Override
    public void onShowPromtoionFilterDataError(Object object) {
        mView.onShowPromtoionFilterDataError(object);
    }
}
