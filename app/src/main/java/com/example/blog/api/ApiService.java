package com.example.blog.api;

import com.example.blog.model.BaseResponse;
import com.example.blog.model.Blog;
import com.example.blog.model.BlogDetail;
import com.example.blog.model.Comment;
import com.example.blog.model.CreateBlog;
import com.example.blog.model.Like;
import com.example.blog.model.Login;
import com.example.blog.model.Member;
import com.example.blog.model.Message;
import com.example.blog.model.MessageDetail;
import com.example.blog.model.MessageRO;
import com.example.blog.model.Notification;
import com.example.blog.model.UpdateStatusNotify;
import com.example.blog.model.User;
import com.example.blog.model.UserRO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @FormUrlEncoded
    @POST("api/auth/login")
    Call<Login> loginUser(@Field("username") String username,
                          @Field("password") String password);

    @GET("api/auth/info")
    Call<User> getInfoUser(@Header("authorization") String token);

    @FormUrlEncoded
    @POST("api/members/{id}")
    Call<User> editInfoUser(@Path("id") int id,
                            @Field("_name") String name,
                            @Field("_password") String password,
                            @Field("_phone_number") String phoneNumber,
                            @Field("_dob") String dob,
                            @Field("_gender") int gender,
                            @Header("authorization") String token);
    @Multipart
    @POST("api/members/{id}")
    Call<User> editAvatarUser(@Path("id") int id,
                            @Part MultipartBody.Part avatar,
                            @Header("authorization") String token);

    @FormUrlEncoded
    @POST("api/members")
    Call<User> registerUser(@Field("_name") String name,
                            @Field("_email") String email,
                            @Field("_password") String password,
                            @Field("_gender") int gender);

//    @FormUrlEncoded
//    @POST("api/blogs")
//    Call<CreateBlog> createBlog(@Field("_title") String title,
//                                @Field("_content") String content,
//                                @Header("authorization") String token);

    @GET("api/members")
    Call<Member> getListMember(@Header("authorization") String token);

    @GET("api/blogs")
    Call<Blog> getListBlogs(@Header("authorization") String token);

    @GET("api/blogs/{id}")
    Call<BlogDetail> getBlogDetail(@Path("id") int id, @Header("authorization") String token);

    @GET("api/members/{id}")
    Call<User> getMemberById(@Path("id") int id, @Header("authorization") String token);

    @POST("api/blogs/{id}/like-unlike")
    Call<Like> setLikeOrUnlike(@Path("id") int id, @Header("authorization") String token);

    @FormUrlEncoded
    @POST("api/comments/{id}")
    Call<Comment> createComment(@Path("id") int id,
                                @Field("content") String content,
                                @Header("authorization") String token);

    @GET("/api/chat/messages/{id}")
    Call<Message> getMessagesByReceiver(@Path("id") int id, @Header("authorization") String token);

    @GET("/api/chat/messages/members")
    Call<Message> getListMemeberChat(@Header("authorization") String token);

    @Multipart
    @POST("api/blogs")
    Call<CreateBlog> createBlog(@Part ("_title") RequestBody title,
                                @Part("_content") RequestBody content,
                                @Part MultipartBody.Part image,
                                @Header("authorization") String token);

    @Multipart
    @POST("api/blogs/{id}")
    Call<CreateBlog> editBlog(  @Path("id") int id,
                                @Part ("_title") RequestBody title,
                                @Part("_content") RequestBody content,
                                @Part("_status") int status,
                                @Part MultipartBody.Part image,
                                @Header("authorization") String token);

    @FormUrlEncoded
    @POST("api/chat/{id}")
    Call<MessageDetail> sendMessageText(@Path("id") int id,
                                        @Field("content") String content,
                                        @Header("authorization") String token);

    @Multipart
    @POST("api/chat/images/{id}")
    Call<MessageDetail> sendMessageImage(@Path("id") int id,
                                         @Part MultipartBody.Part image,
                                        @Header("authorization") String token);

    @FormUrlEncoded
    @POST("api/auth/forgot-password")
    Call<User> forgotPassword(@Field("email") String email);

    @GET("/api/notifications")
    Call<Notification> getNotifications(@Header("authorization") String token);

    @GET("/api/notifications/has-seen/{id}")
    Call<UpdateStatusNotify> setStatusNotification(@Path("id") int id, @Header("authorization") String token);
}
