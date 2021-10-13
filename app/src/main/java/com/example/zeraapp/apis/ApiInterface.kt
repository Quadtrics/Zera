package com.example.zeraapp.apis


import com.example.zeraapp.models.AuthUsersListRoot.AuthUserRoot
import com.example.zeraapp.models.ChangePassword.ChangePasswordPojo
import com.example.zeraapp.models.ForgotPassword.ForgotPasswordPojo
import com.example.zeraapp.models.GetAuthUsers.AuthTransactions
import com.example.zeraapp.models.AuthUsers.AuthUsersListRoot
import com.example.zeraapp.models.SignUpPojo.SignupPojo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface
ApiInterface {


    @POST("login")
    @FormUrlEncoded
    fun Login(
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("device_type") device_type: String?,
        @Field("device_token") device_token: String?
    ): Call<SignupPojo?>?

    @POST("signup")
    @FormUrlEncoded
    fun signUp(
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("deviceType") device_type: String?,
        @Field("deviceToken") device_token: String?
    ): Call<SignupPojo?>?


    @POST("update-user/{id}")
    @FormUrlEncoded
    fun userUpdate(
        @Path("id") id: String?,
        @Field("user_name") user_name: String?,
        @Field("email") email: String?,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("state") state: String?,
        @Field("city") city: String?,
        @Field("dob") dob: String?,
        @Field("address") address: String?,
        @Field("time-period") time_period: String?
    ): Call<SignupPojo?>?


    @POST("update-aut-user/{id}")
    @FormUrlEncoded
    fun userAuthUpdate(
        @Path("id") id: String?,
        @Field("name") name: String?,
        @Field("phoneNumber") phoneNumber: String?,
        @Field("state") state: String?,
        @Field("city") city: String?,
        @Field("dob") dob: String?,
        @Field("address") address: String?
    ): Call<AuthUserRoot?>?


    @POST("update-user/{id}")
    @FormUrlEncoded
    fun userUpdate(
        @Path("id") id: String?,
        @Field("time_period") time_period: String?,
        @Field("maturity_date") maturity_date: String?
    ): Call<SignupPojo?>?

    @Multipart
    @POST("update-user/{id}")
    fun userUpdate(
        @Path("id") id: String?,
        @Part image: MultipartBody.Part?
    ): Call<SignupPojo?>?

    @Multipart
    @POST("upload-user-contract")
    fun uploadContract(
        @PartMap value: HashMap<String, RequestBody> ,
//        @Part("user_ID") userId: String?,
        @Part contract: MultipartBody.Part?
//        @PartMap HashMap<String, RequestBody> value,

    ): Call<SignupPojo?>?


    @POST("forgot-password")
    @FormUrlEncoded
    fun forgotPassword(
        @Field("email") email: String?,
    ): Call<ForgotPasswordPojo?>?


    @POST("change-password")
    @FormUrlEncoded
    fun changePassword(
        @Field("currentPassword") currentPassword: String?,
        @Field("newPassword") newPassword: String?,
        @Field("confirmPassword") confirmPassword: String?
    ): Call<ChangePasswordPojo?>?

    @GET("get-aut-users")
    fun getAuthUser(@Query("prime_user") storeId: String): Call<AuthUsersListRoot>

    @GET("delete-aut-user")
    fun deleteAuthUser(@Query("authUserId") authUserId: String): Call<ChangePasswordPojo>

    @GET("get-a-aut-user")
    fun getAuthUserDetail(@Query("authUserId") storeId: String): Call<AuthUsersListRoot>


    @GET("user")
    fun getUser(@Query("id") id: String): Call<SignupPojo>


    @GET("get-payments")
    fun getPayments(
        @Query("user_id") user_id: String?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int?
    ): Call<AuthTransactions>

    @POST("add-payment")
    @FormUrlEncoded
    fun addPayment(
        @Field("amount") amount: Int?,
        @Field("type") type : String?,
        @Field("user_id") user_id : String?,
        @Field("date") date : String?,
    ): Call<ChangePasswordPojo?>


    @POST("add-aut-user")
    @FormUrlEncoded
    fun addAuthUser(
        @Field("prime_user") prime_userId: String?,
        @Field("name") name : String?,
        @Field("email") email : String?,
        @Field("phoneNumber") phoneNumber  : String?,
        @Field("address") address   : String?,
        @Field("state") state  : String?,
        @Field("city") city   : String?,
        @Field("dob") dob    : String?,
        @Field("deviceToken") deviceToken  : String?,
        @Field("deviceType") deviceType  : String?,
    ): Call<AuthUserRoot?>


    //Profile Api 3
//    @POST("getprofile")
//    fun getProfile(@Body map: HashMap<String, String>): Call<RestResponse<ProfileModel>>
//
//    //EditProfile Api 4
//    @Multipart
//    @POST("editprofile")
//    fun setProfile(@Part("user_id") userId: RequestBody,@Part("name") name: RequestBody, @Part profileimage: MultipartBody.Part?): Call<SingleResponse>
//
//    //Chnage Password  Api 5
//    @POST("changepassword")
//    fun setChangePassword(@Body map: HashMap<String, String>):Call<SingleResponse>
//
//    //Category  Api 6
//
//    @GET("stores")
//    fun getStoreCategory():Call<ListResponse<StoresModel>>
//
//    //Item  Api 7
//    @POST("item")
//    fun getFoodItem(@Body map: HashMap<String, String>,@Query("page")strPageNo:String):Call<RestResponse<FoodItemResponseModel>>
//
//    //Item  Api 9
//    @POST("orderhistory")
//    fun getOrderHistory(@Body map: HashMap<String, String>):Call<ListResponse<OrderHistoryModel>>
//
//    //Item  Api 10
//    @GET("rattinglist")
//    fun getRatting():Call<ListResponse<RattingModel>>
//
//    //Getcart  Api 11
//    @POST("getcart")
//    fun getCartItem(@Body map: HashMap<String, String>):Call<ListResponse<CartItemModel>>
//
//    //Ratting  Api 12
//    @POST("ratting")
//    fun setRatting(@Body map: HashMap<String, String>):Call<SingleResponse>
//
//    //ItemDetail  Api 13
//    @POST("itemdetails")
//    fun setItemDetail(@Body map: HashMap<String, String>):Call<RestResponse<ItemDetailModel>>
//
//    //cart  Api 14
//    @POST("cart")
//    fun setAddToCart(@Body map: HashMap<String, String>):Call<SingleResponse>
//
//    //QtyUpdate Api 15
//    @POST("qtyupdate")
//    fun setQtyUpdate(@Body map: HashMap<String, String>):Call<SingleResponse>
//
//    //DeleteCartItem Api 16
//    @POST("deletecartitem")
//    fun setDeleteCartItem(@Body map: HashMap<String, String>):Call<SingleResponse>
//
//    //Summary Api 17
//    @POST("summary")
//    fun setSummary(@Body map: HashMap<String, String>):Call<RestSummaryResponse>
//
//    //OrderPayment Api 18
//    @POST("order")
//    fun setOrderPayment(@Body map: HashMap<String, String>,@Query("store_id")storeId:String):Call<SingleResponse>
//
//    //forgotPassword Api 19
//    @POST("forgotPassword")
//    fun setforgotPassword(@Body map: HashMap<String, String>):Call<SingleResponse>
//
//    //OrderDetail Api 20
//    @POST("getorderdetails")
//    fun setgetOrderDetail(@Body map: HashMap<String, String>):Call<RestOrderDetailResponse>
//
//    //Search Api 21
//    @POST("searchitem")
//    fun setSearch(@Body map: HashMap<String, String>,@Query("page")strPageNo:String):Call<RestResponse<FoodItemResponseModel>>
//
//    //PromoCode Api 23
//    @POST("favoritelist")
//    fun getFavouriteList(@Body map: HashMap<String, String>,@Query("page")strPageNo:String):Call<RestResponse<FoodFavouriteResponseModel>>
//
//    //AddFavorite Api 24
//    @POST("addfavorite")
//    fun setAddFavorite(@Body map: HashMap<String, String>):Call<SingleResponse>
//
//    //Removefavorite Api 25
//    @POST("removefavorite")
//    fun setRemovefavorite(@Body map: HashMap<String, String>):Call<SingleResponse>
//
//    //PromoCode Api 26
//    @GET("promocodelist")
//    fun getPromoCodeList(@Query("store_id")storeId:String):Call<ListResponse<PromocodeModel>>
//
//    //ApplyPromocode Api 27
//    @POST("promocode")
//    fun setApplyPromocode(@Body map: HashMap<String, String>):Call<RestResponse<GetPromocodeModel>>
//
//    //ApplyPromocode Api 27
//    @POST("cartcount")
//    fun getCartCount(@Body map: HashMap<String, String>):Call<CartCountModel>
//
//    //ApplyPromocode Api 28
//    @GET("banner")
//    fun getBanner(@Query("store_id")storeId:String):Call<ListResponse<BannerModel>>
//
//    //LocationApi 29
//    @GET("restaurantslocation")
//    fun getLocation():Call<RestResponse<LocationModel>>
//
//    //check Status Api 30
//    @GET("isopenclose")
//    fun getCheckStatusRestaurant():Call<SingleResponse>
//
//
//    //Checkpincode Api 31
//    @POST("checkpincode")
//    fun setCheckPinCode(@Body map: HashMap<String,String>):Call<SingleResponse>
//
//    //Checkpincode Api 32
//    @POST("resendemailverification")
//    fun setResendEmailVerification(@Body map: HashMap<String,String>):Call<SingleResponse>
//
//    //getChatList Api 33
//    @POST("emailverify")
//    fun setEmailVerify(@Body map: HashMap<String, String>): Call<JsonObject>
//
//    @POST("driverlatlong")
//    fun setDriverlatlong(@Body map: HashMap<String, String>):Call<SingleResponse>
//
//
//    //ApplyPromocode Api 28
//    @GET("savedcard")
//    fun getSaveCard(@Query("user_id")user_id:String):Call<SaveCardPojo>
//
//    //payment method
//    @GET("payment-methods")
//    fun getPaymentMethod():Call<PaymentMethodPojo>
//
//    //Item  Api 9
//    @POST("check-credit-card")
//    fun getCheckCard(@Query("user_id")user_id:String,@Query("xCardNum")xCardNum:String):Call<CheckCardModle>
//

}