package com.wmx.android.wrstar.mvp.presenters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wmx.android.wrstar.app.WRStarApplication;
import com.wmx.android.wrstar.biz.AccountBiz;
import com.wmx.android.wrstar.biz.PersonalBiz;
import com.wmx.android.wrstar.biz.response.GetUserInfoResponse;
import com.wmx.android.wrstar.biz.response.ModifyPersonalInfoResponse;
import com.wmx.android.wrstar.constants.ServerCodes;
import com.wmx.android.wrstar.mvp.views.ICommonView;
import com.wmx.android.wrstar.mvp.views.IPersonalInfoView;
import com.wmx.android.wrstar.utils.LogUtil;

/**
 * Created by Administrator on 2016/5/16.
 */
public class PersonalInfoPresenter extends BasePresenter {

    PersonalBiz personalBiz;

    AccountBiz accountBiz;


    IPersonalInfoView personalInfoView;
    Context context;





    public PersonalInfoPresenter(ICommonView commonView, Context context, IPersonalInfoView iPersonalInfoView) {
        super(commonView);
        this.personalInfoView = iPersonalInfoView;
        this.context = context;
        personalBiz = PersonalBiz.getInstance(context);

        accountBiz= AccountBiz.getInstance(context);
    }


    public void getUserInfo() {
        String token = getToken();


        accountBiz.getUserInfo(token, new Response.Listener<GetUserInfoResponse>() {
            @Override
            public void onResponse(GetUserInfoResponse response) {


                if (response.getResult().equals(ServerCodes.SUCCESS)) {
                    personalInfoView.refreshUserInfo(response.body.user);

                } else {
                    mCommonView.showToast(response.getResultDesc());
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
            }
        }, "getUserInfo");

    }



    public void setNickName(final String mobnum) {


        final EditText inputNickName = new EditText(context);
        inputNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("输入您的新昵称(最多10字符)").setView(inputNickName).setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                final String newNickName = inputNickName.getText().toString().trim();
                if (newNickName.length() == 0) {
                    mCommonView.showToast("请输入昵称");
                    return;
                }
                mCommonView.showDialog("修改中...");

                personalBiz.setUserInfo(mobnum, newNickName, PersonalBiz.NICKNIME, new Response.Listener<ModifyPersonalInfoResponse>() {
                    @Override
                    public void onResponse(ModifyPersonalInfoResponse response) {
                        if (ServerCodes.SUCCESS.equals(response.getResult())) {
                            personalInfoView.modifyNickNameSuccess(newNickName);
                        } else {
                            personalInfoView.modifyNickNameFailed(response.getResult() + response.getResultDesc());
                        }
                        mCommonView.hideDialog();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mCommonView.netError();
                        LogUtil.i("error:" + error.toString());

                    }
                }, "setNickName");
            }
        });
        builder.show();

    }

    public void setLocation(String mobnum, final String province_city) {

        personalBiz.setUserInfo(mobnum, province_city, PersonalBiz.LOCATION, new Response.Listener<ModifyPersonalInfoResponse>() {
            @Override
            public void onResponse(ModifyPersonalInfoResponse response) {
                if (ServerCodes.SUCCESS.equals(response.getResult())) {
                    WRStarApplication.getUser().province = response.body.user.province;
                    WRStarApplication.getUser().city = response.body.user.city;
                    personalInfoView.modifyLocationSuccess(response.body.user.province + response.body.user.city);
                } else {
                    personalInfoView.modifySexFailed();
                }
                //   mCommonView.hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
                LogUtil.i("error:" + error.toString());
            }
        }, "setSex");
    }


    public void setSex(String mobnum, final String sex) {
        mCommonView.showDialog("修改中...");
        personalBiz.setUserInfo(mobnum, sex, PersonalBiz.SEX, new Response.Listener<ModifyPersonalInfoResponse>() {
            @Override
            public void onResponse(ModifyPersonalInfoResponse response) {
                if (ServerCodes.SUCCESS.equals(response.getResult())) {

                    personalInfoView.modifySexSuccess(sex.equals("0") ? "男" : "女");
                } else {
                    personalInfoView.modifySexFailed();
                }
                mCommonView.hideDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCommonView.netError();
                LogUtil.i("error:" + error.toString());
            }
        }, "setSex");
    }

    public void setSignature(final String mobnum) {


        final EditText inputNickName = new EditText(context);
        inputNickName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("输入您的新签名(最多30字符)").setView(inputNickName).setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                final String newSignature = inputNickName.getText().toString().trim();
                if (newSignature.length() == 0) {
                    mCommonView.showToast("请输入签名");
                    return;
                }
                mCommonView.showDialog("修改中...");

                personalBiz.setUserInfo(mobnum, newSignature, PersonalBiz.SIGNATURE, new Response.Listener<ModifyPersonalInfoResponse>() {
                    @Override
                    public void onResponse(ModifyPersonalInfoResponse response) {
                        if (ServerCodes.SUCCESS.equals(response.getResult())) {

                            personalInfoView.modifySignatureSuccess(newSignature);
                        } else {
                            personalInfoView.modifySignatureFailed();
                        }
                        mCommonView.hideDialog();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mCommonView.netError();
                        LogUtil.i("error:" + error.toString());
                    }
                }, "setSex");
            }
        });
        builder.show();


    }




}
