package com.byted.chapter5;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    // https://wanandroid.com/wxarticle/chapters/json
    @GET("wxarticle/chapters/json")
    Call<ArticleResponse> getArticles();
    //Call<ArticleResponse> getArticles(@Query("user") String name); 加参数


    // todo 添加api
    // https://www.wanandroid.com/user/register
    //方法：POST
    //	username,password,repassword
    //UserResponse为返回值
    @FormUrlEncoded
    @POST("user/register")
    Call<UserResponse> register(@Field("username") String userName,@Field("password") String password,@Field("repassword") String rePassword);

}
