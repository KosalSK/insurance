package com.insurance.ecoinsoft.app.merchants;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.insurance.ecoinsoft.app.BaseActivity;
import com.insurance.ecoinsoft.app.R;
import com.insurance.ecoinsoft.app.model.response.PromotionsResponse;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PromotionsRecyclerviewAdapter extends RecyclerView.Adapter<PromotionsRecyclerviewAdapter.MerchantsViewHolder> {
    private Context mContext;
    private List<PromotionsResponse.Data> mPromotionsDataModelList;
    private onItemClickListener mOnItemClickListener;
    private String mStartDate;
    private String mEndDate;

    public PromotionsRecyclerviewAdapter(Context context, List<PromotionsResponse.Data> merchantsDataModelList, onItemClickListener onItemClickListener) {
        mContext = context;
        mPromotionsDataModelList = merchantsDataModelList;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MerchantsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.merchant_promotion_item_list, viewGroup, false);
        return new MerchantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchantsViewHolder merchantsViewHolder, int i) {
        merchantsViewHolder.itemView.setTag(mPromotionsDataModelList.get(i));
        merchantsViewHolder.mImageView.setImageResource(R.drawable.kfc);
        merchantsViewHolder.mTitleTextView.setText(mPromotionsDataModelList.get(i).getMerchant().getName());
        merchantsViewHolder.mDescriptionTextView.setText(mPromotionsDataModelList.get(i).getDescription());
        merchantsViewHolder.mDiscountTextView.setText(mPromotionsDataModelList.get(i).getName());
        mStartDate = BaseActivity.formatDate(mPromotionsDataModelList.get(i).getStartDate());
        merchantsViewHolder.mStartDateTextView.setText(BaseActivity.splitDate(mStartDate));
        mEndDate = BaseActivity.formatDate(mPromotionsDataModelList.get(i).getEndDate());
        merchantsViewHolder.mEndDateTextView.setText(BaseActivity.splitDate(mEndDate));
    }

    @Override
    public int getItemCount() {
        return mPromotionsDataModelList.size();
    }

    public class MerchantsViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private TextView mDiscountTextView;
        private TextView mStartDateTextView;
        private TextView mEndDateTextView;

        public MerchantsViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.promotion_imageview);
            mTitleTextView = (TextView) itemView.findViewById(R.id.promotion_title_textview);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.promotion_description_textview);
            mDiscountTextView = (TextView) itemView.findViewById(R.id.promotion_discount_textview);
            mStartDateTextView = (TextView) itemView.findViewById(R.id.promotion_startdate_textview);
            mEndDateTextView = (TextView) itemView.findViewById(R.id.promotion_enddate_textview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnItemClickListener) {
                        mOnItemClickListener.onClick(mContext, mPromotionsDataModelList.get(getAdapterPosition()));
                    }

                }
            });
        }
    }

    public interface onItemClickListener {
        void onClick(Context context, PromotionsResponse.Data promotionResponse);
    }

    public void setfilter(List<PromotionsResponse.Data> itemList) {
        mPromotionsDataModelList = new ArrayList<>();
        mPromotionsDataModelList.addAll(itemList);
        notifyDataSetChanged();
    }
}
