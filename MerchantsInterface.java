package com.insurance.ecoinsoft.app.merchants;

import com.google.gson.JsonObject;
import com.insurance.ecoinsoft.app.BaseModel;
import com.insurance.ecoinsoft.app.BasePresenter;
import com.insurance.ecoinsoft.app.BaseView;

import org.json.JSONObject;

public class MerchantsInterface {
    interface View extends BaseView<Presenter> {
        void onShowProgressDialog();

        void onHideProgressDialog();

        void onShowMerchantsCategoriesDataSuccess(JSONObject object);

        void onShowMerchantsCategoriesDataError(Object object);

        void onShowPromotionsDataSuccess(JSONObject object);

        void onShowPromotionsDataError(Object object);

        void onShowPromtionFilterDataSuccess(JSONObject object);

        void onShowPromtoionFilterDataError(Object object);


    }

    interface Presenter extends BasePresenter {
        void loadingMerchantsCategories();

        void loadingPromotions(int num);

        void loadingPromotionFilter(String str);

    }

    public interface Model extends BaseModel {
        void onShowMerchantsCategoiesDataSuccess(JSONObject object);

        void onShowMerchantsCategoriesDataError(Object object);

        void onShowPromotionsDataSuccess(JSONObject object);

        void onShowPromotionsDataError(Object object);

        void onShowPromtionFilterDataSuccess(JSONObject object);

        void onShowPromtoionFilterDataError(Object object);
    }
}
