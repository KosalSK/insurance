
public class MerchantsCategoriesRecyclerviewAdapter extends RecyclerView.Adapter<MerchantsCategoriesRecyclerviewAdapter.MerchantsHolder> {
    private Context mContext;
    private List<MerchantCategoriesResponse.Data> mCategoriesDataModelList;
    public static int row_index = -1;
    private onItemClickListener mOnItemClickListener;

    public MerchantsCategoriesRecyclerviewAdapter(Context context, List<MerchantCategoriesResponse.Data> categoriesDataModelList, onItemClickListener onItemClickListener) {
        mContext = context;
        mCategoriesDataModelList = categoriesDataModelList;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MerchantsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_item_list, viewGroup, false);
        MerchantsHolder merchantsHolder = new MerchantsHolder(v);
        boolean getValue = mCategoriesDataModelList.get(i).setSelected(false);
        if (getValue == true) {
            merchantsHolder.mCategoriesName.setTextColor(Color.parseColor("#ffffff"));
            merchantsHolder.mCategoriesName.setBackgroundResource(R.drawable.textview_bg_red);
        } else {
            merchantsHolder.mCategoriesName.setTextColor(Color.parseColor("#FF473A"));
            merchantsHolder.mCategoriesName.setBackgroundResource(R.drawable.textview_corner_style);
            MerchantsActivity.mAllCategoriesTextView.setTextColor(Color.parseColor("#ffffff"));
            MerchantsActivity.mAllCategoriesTextView.setBackgroundResource(R.drawable.textview_bg_red);
        }
        return merchantsHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull MerchantsHolder merchantsHolder, int i) {
        merchantsHolder.itemView.setTag(mCategoriesDataModelList.get(i));
        merchantsHolder.mCategoriesName.setText(mCategoriesDataModelList.get(i).getName());


        merchantsHolder.mCategoriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = i;
                notifyDataSetChanged();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(mContext, mCategoriesDataModelList.get(i));
                }
            }
        });

        if (row_index == i) {
            merchantsHolder.mCategoriesName.setTextColor(Color.parseColor("#ffffff"));
            merchantsHolder.mCategoriesName.setBackgroundResource(R.drawable.textview_bg_red);
            MerchantsActivity.mAllCategoriesTextView.setTextColor(Color.parseColor("#FF473A"));
            MerchantsActivity.mAllCategoriesTextView.setBackgroundResource(R.drawable.textview_corner_style);


        } else {
            merchantsHolder.mCategoriesName.setTextColor(Color.parseColor("#FF473A"));
            merchantsHolder.mCategoriesName.setBackgroundResource(R.drawable.textview_corner_style);
        }


    }


    @Override
    public int getItemCount() {
        return mCategoriesDataModelList.size();
    }

    public class MerchantsHolder extends RecyclerView.ViewHolder {
        private TextView mCategoriesName;
        private LinearLayout mCategoriesLayout;

        public MerchantsHolder(@NonNull View itemView) {
            super(itemView);
            mCategoriesName = (TextView) itemView.findViewById(R.id.categoriesname_textview);
            mCategoriesLayout = (LinearLayout) itemView.findViewById(R.id.categoirestab_layout);
        }
    }

    interface onItemClickListener {
        void onClick(Context context, MerchantCategoriesResponse.Data categoriesDataModel);
    }
}
