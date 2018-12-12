
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
        
}
