package com.wmx.android.wrstar.views.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.wmx.android.wrstar.R;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.entities.City;
import com.wmx.android.wrstar.entities.Province;
import com.wmx.android.wrstar.entities.User;
import com.wmx.android.wrstar.mvp.adapter.CityAdapter;
import com.wmx.android.wrstar.mvp.presenters.PersonalInfoPresenter;
import com.wmx.android.wrstar.mvp.views.IPersonalInfoView;
import com.wmx.android.wrstar.store.WeatherDB;
import com.wmx.android.wrstar.store.WeatherDBManager;
import com.wmx.android.wrstar.utils.PreferenceUtils;
import com.wmx.android.wrstar.views.base.AbsBaseActivity;
import com.wmx.android.wrstar.views.widgets.ActionBarPrimary;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class ChoiceCityActivity extends AbsBaseActivity implements IPersonalInfoView {


    private int currentLevel;
    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;

    private Province selectedProvince;
    private City selectedCity;

    private ArrayList<String> dataList = new ArrayList<>();

    private List<Province> provincesList;
    private List<City> cityList;

    private CityAdapter adapter;

    private WeatherDBManager mDBManager;
    private WeatherDB mWeatherDB;

    @Bind(R.id.action_bar)
    ActionBarPrimary actionBar;
    @Bind(R.id.rv_city)
    RecyclerView rvCity;


    PersonalInfoPresenter presenter;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_choice_city;
    }

    @Override
    protected void initExtraData() {

    }

    @Override
    protected void initVariables() {
        mDBManager = new WeatherDBManager(this);
        mDBManager.openDatabase();
        mWeatherDB = new WeatherDB(this);
        presenter = new PersonalInfoPresenter(this,this,this);
    }

    @Override
    protected void initViews() {

        actionBar.setActionBarListener(new ActionBarPrimary.ActionBarPrimaryListener() {
            @Override
            public void onLeftBtnClick() {
                if (currentLevel == LEVEL_PROVINCE) {
                    finish();
                } else {
                    queryProvinces();
                    rvCity.smoothScrollToPosition(0);
                }
            }

            @Override
            public void onRightTextClick() {

            }

            @Override
            public void onRightBtnClick() {

            }
        });

        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.setHasFixedSize(true);
        adapter = new CityAdapter(this, dataList);
        rvCity.setAdapter(adapter);

        adapter.setOnItemClickListener(new CityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, final int pos) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provincesList.get(pos);
                    rvCity.scrollTo(0, 0);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    showDialog("修改中...");
                    selectedCity = cityList.get(pos);
                    presenter.setLocation(WRStarApplication.getUser().mobnum,selectedCity.ProID+"-"+selectedCity.cityID);
                    String cityName = selectedCity.CityName;
//                    showToast("选择了"+cityName);
                    if(!TextUtils.isEmpty(cityName)){
                        PreferenceUtils.setLocal(ChoiceCityActivity.this,cityName);
                    }

                }
            }
        });
    }

    /**
     * 查询全国所有的省，从数据库查询
     */
    private void queryProvinces() {

        actionBar.setTitle("选择省份");


        provincesList = mWeatherDB.loadProvinces(mDBManager.getDatabase());
        dataList.clear();
        for (Province province : provincesList) {
            dataList.add(province.ProName);
        }
        adapter.notifyDataSetChanged();
        currentLevel = LEVEL_PROVINCE;


    }

    private void queryCities() {
        actionBar.setTitle("选择城市");
        dataList.clear();
        cityList = mWeatherDB.loadCities(mDBManager.getDatabase(), selectedProvince.ProSort);
        for (City city : cityList) {
            dataList.add(city.CityName);
        }
        currentLevel = LEVEL_CITY;
        adapter.notifyDataSetChanged();
        //定位到第一个item
        rvCity.smoothScrollToPosition(0);

    }

    @Override
    protected void loadData() {
        queryProvinces();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentLevel == LEVEL_PROVINCE) {
                finish();
            } else {
                queryProvinces();
                rvCity.smoothScrollToPosition(0);
            }
        }
        return false;
    }

    @Override
    protected String getPageTag() {
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBManager.closeDatabase();
    }

    @Override
    public void modifyNickNameSuccess(String newNickName) {

    }

    @Override
    public void modifyNickNameFailed(String resultDec) {

    }

    @Override
    public void modifyAvatorSuccess() {

    }

    @Override
    public void modifyAvatorFailed() {

    }

    @Override
    public void modifySexSuccess(String sex) {

    }

    @Override
    public void modifySexFailed() {

    }

    @Override
    public void bindPhoneSuccess() {

    }

    @Override
    public void bindPhoneFailed() {

    }

    @Override
    public void modifySignatureSuccess(String signature) {

    }

    @Override
    public void modifySignatureFailed() {

    }

    @Override
    public void modifyLocationSuccess(String location) {
        hideDialog();
        finish();
    }

    @Override
    public void refreshUserInfo(User user) {

    }

}
