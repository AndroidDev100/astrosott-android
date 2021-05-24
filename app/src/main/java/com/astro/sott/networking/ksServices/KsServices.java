package com.astro.sott.networking.ksServices;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.ApplicationMain;
import com.astro.sott.activities.SelectAccount.SelectAccountModel.DtvMbbHbbModel;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.search.constants.SearchFilterEnum;
import com.astro.sott.activities.search.constants.SortByEnum;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.activities.subscription.manager.PaymentItemDetail;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.commonBeanModel.MediaTypeModel;
import com.astro.sott.beanModel.commonBeanModel.SearchModel;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.BillPaymentCallBack;
import com.astro.sott.callBacks.commonCallBacks.BookmarkingPositionCallBack;
import com.astro.sott.callBacks.commonCallBacks.CommonCallBack;
import com.astro.sott.callBacks.commonCallBacks.CommonResponseCallBack;
import com.astro.sott.callBacks.commonCallBacks.CommonResponseHandler;
import com.astro.sott.callBacks.commonCallBacks.DtvListCallBack;
import com.astro.sott.callBacks.commonCallBacks.EntitlementResponseCallBack;
import com.astro.sott.callBacks.commonCallBacks.FollowTvSeriesCallBack;
import com.astro.sott.callBacks.commonCallBacks.HBBAccountCallBack;
import com.astro.sott.callBacks.commonCallBacks.HouseholdpaymentResponseCallBack;
import com.astro.sott.callBacks.commonCallBacks.HungamaResponse;
import com.astro.sott.callBacks.commonCallBacks.InvokeApiCallBack;
import com.astro.sott.callBacks.commonCallBacks.MBBAccountCallBack;
import com.astro.sott.callBacks.commonCallBacks.MBBAccountListCallBack;
import com.astro.sott.callBacks.commonCallBacks.PurchaseSubscriptionCallBack;
import com.astro.sott.callBacks.commonCallBacks.RecentSearchCallBack;
import com.astro.sott.callBacks.commonCallBacks.RemovePaymentCallBack;
import com.astro.sott.callBacks.commonCallBacks.SubscriptionAssetListResponse;
import com.astro.sott.callBacks.commonCallBacks.SubscriptionResponseCallBack;
import com.astro.sott.callBacks.commonCallBacks.UpdatePaymentMethodCallBack;
import com.astro.sott.callBacks.commonCallBacks.UserPrefrencesCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.AdContextCallback;
import com.astro.sott.callBacks.kalturaCallBacks.AddWatchListCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.AllWatchlistCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.ContinueWatchingCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.DMSCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.DeleteDeviceCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.DeleteFromFollowlistCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.DeleteWatchListCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.EpisodeProgressCallback;
import com.astro.sott.callBacks.kalturaCallBacks.GenreCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.GetSeriesCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.HomechannelCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.HouseHoldAddCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.HouseHoldDevice;
import com.astro.sott.callBacks.kalturaCallBacks.KsAnonymousLoginCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.KsAppTokenCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.KsHouseHoldDevice;
import com.astro.sott.callBacks.kalturaCallBacks.KsHouseHoldDeviceAddCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.LogoutCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.NextEpisodeCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.NotificationCallback;
import com.astro.sott.callBacks.kalturaCallBacks.NotificationStatusCallback;
import com.astro.sott.callBacks.kalturaCallBacks.OttUserDetailsCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.PlayBackContextCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.PopularSearchCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.ProductPriceCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.PushTokenCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.RefreshTokenCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.SearchResultCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.SeasonCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.SignUpCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.SimilarMovieCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.SubCategoryCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.TrailorCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.TrailorToAssetCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.TrendingCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.UpdateDeviceCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.WatchlistCallBack;
import com.astro.sott.callBacks.otpCallbacks.AutoMsisdnCallback;
import com.astro.sott.callBacks.otpCallbacks.DTVAccountCallback;
import com.astro.sott.callBacks.otpCallbacks.OtpCallback;
import com.astro.sott.callBacks.otpCallbacks.OtpVerificationCallback;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.modelClasses.DTVContactInfoModel;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.modelClasses.dmsResponse.FilterLanguages;
import com.astro.sott.modelClasses.dmsResponse.FilterValues;
import com.astro.sott.modelClasses.dmsResponse.ParentalRatingLevels;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.modelClasses.dmsResponse.SubtitleLanguages;
import com.astro.sott.modelClasses.playbackContext.PlaybackContextResponse;
import com.astro.sott.networking.refreshToken.RefreshKS;
import com.astro.sott.networking.retrofit.ApiInterface;
import com.astro.sott.networking.retrofit.RequestConfig;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.FileFormatHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.astro.sott.utils.helpers.StringUtils;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.ksPreferenceKey.SubCategoriesPrefs;
import com.astro.sott.utils.userInfo.UserInfo;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.commonCallBacks.ApiCallBack;
import com.astro.sott.callBacks.commonCallBacks.CancelRenewalResponseCallBack;
import com.astro.sott.callBacks.commonCallBacks.ChannelCallBack;
import com.astro.sott.callBacks.commonCallBacks.DTVCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.AssetRuleCallback;
import com.astro.sott.callBacks.kalturaCallBacks.KsLoginCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.KsStartSessionCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.SpecificAssetCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.TrailerAssetCallBack;
import com.astro.sott.modelClasses.OtpModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.UDID;
import com.enveu.Enum.ImageSource;
import com.enveu.Enum.LandingPageType;
import com.enveu.Enum.Layouts;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kaltura.client.APIOkRequestsExecutor;
import com.kaltura.client.Client;
import com.kaltura.client.Configuration;
import com.kaltura.client.RequestQueue;
import com.kaltura.client.enums.AssetReferenceType;
import com.kaltura.client.enums.AssetType;
import com.kaltura.client.enums.EntityReferenceBy;
import com.kaltura.client.enums.InboxMessageStatus;
import com.kaltura.client.enums.PinType;
import com.kaltura.client.enums.RuleLevel;
import com.kaltura.client.enums.TransactionType;
import com.kaltura.client.enums.WatchStatus;
import com.kaltura.client.services.AppTokenService;
import com.kaltura.client.services.AssetHistoryService;
import com.kaltura.client.services.AssetService;
import com.kaltura.client.services.BookmarkService;
import com.kaltura.client.services.EntitlementService;
import com.kaltura.client.services.FollowTvSeriesService;
import com.kaltura.client.services.HouseholdDeviceService;
import com.kaltura.client.services.HouseholdLimitationsService;
import com.kaltura.client.services.HouseholdPaymentGatewayService;
import com.kaltura.client.services.HouseholdPaymentMethodService;
import com.kaltura.client.services.HouseholdService;
import com.kaltura.client.services.InboxMessageService;
import com.kaltura.client.services.LicensedUrlService;
import com.kaltura.client.services.NotificationService;
import com.kaltura.client.services.NotificationsSettingsService;
import com.kaltura.client.services.OttCategoryService;
import com.kaltura.client.services.OttUserService;
import com.kaltura.client.services.ParentalRuleService;
import com.kaltura.client.services.PersonalListService;
import com.kaltura.client.services.PinService;
import com.kaltura.client.services.ProductPriceService;
import com.kaltura.client.services.SearchHistoryService;
import com.kaltura.client.services.SubscriptionService;
import com.kaltura.client.services.TransactionService;
import com.kaltura.client.services.UserAssetRuleService;
import com.kaltura.client.types.AggregationCountFilter;
import com.kaltura.client.types.AppToken;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.AssetGroupBy;
import com.kaltura.client.types.AssetHistory;
import com.kaltura.client.types.AssetHistoryFilter;
import com.kaltura.client.types.AssetHistorySuppressFilter;
import com.kaltura.client.types.AssetMetaOrTagGroupBy;
import com.kaltura.client.types.BookmarkFilter;
import com.kaltura.client.types.Channel;
import com.kaltura.client.types.ChannelFilter;
import com.kaltura.client.types.DetachedResponseProfile;
import com.kaltura.client.types.DynamicOrderBy;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.EntitlementFilter;
import com.kaltura.client.types.FilterPager;
import com.kaltura.client.types.FollowTvSeries;
import com.kaltura.client.types.FollowTvSeriesFilter;
import com.kaltura.client.types.Household;
import com.kaltura.client.types.HouseholdDevice;
import com.kaltura.client.types.HouseholdDeviceFilter;
import com.kaltura.client.types.HouseholdPaymentMethod;
import com.kaltura.client.types.InboxMessage;
import com.kaltura.client.types.InboxMessageFilter;
import com.kaltura.client.types.KeyValue;
import com.kaltura.client.types.LicensedUrlBaseRequest;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.LoginResponse;
import com.kaltura.client.types.MediaAsset;
import com.kaltura.client.types.NotificationsSettings;
import com.kaltura.client.types.OTTCategory;
import com.kaltura.client.types.OTTUser;
import com.kaltura.client.types.PaymentGatewayConfiguration;
import com.kaltura.client.types.PersonalList;
import com.kaltura.client.types.PersonalListFilter;
import com.kaltura.client.types.Pin;
import com.kaltura.client.types.PlaybackContextOptions;
import com.kaltura.client.types.ProductPriceFilter;
import com.kaltura.client.types.Purchase;
import com.kaltura.client.types.RelatedFilter;
import com.kaltura.client.types.SearchAssetFilter;
import com.kaltura.client.types.SearchHistory;
import com.kaltura.client.types.SearchHistoryFilter;
import com.kaltura.client.types.StringValue;
import com.kaltura.client.types.Subscription;
import com.kaltura.client.types.SubscriptionEntitlement;
import com.kaltura.client.types.SubscriptionFilter;
import com.kaltura.client.types.UserAssetRuleFilter;
import com.kaltura.client.utils.request.MultiRequestBuilder;
import com.kaltura.client.utils.response.OnCompletion;
import com.kaltura.client.utils.response.base.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;

public class KsServices {

    private static final String TAG = "KsServices";
    private final Context activity;
    private ChannelCallBack channelCallBack;
    private SimilarMovieCallBack similarMovieCallBack;
    private String searchString;
    private KsAnonymousLoginCallBack anonymouscallBack;
    private Client client;
    private List<Response<ListResponse<Asset>>> responseList, movieList;
    private SpecificAssetCallBack specificAssetCallBack;
    private ProductPriceCallBack productPriceCallBack;
    private HouseHoldDevice houseHoldDevice2;
    private KsStartSessionCallBack ksStartSessionCallBack;
    private String keyHash = "";
    private int houseHold_limitation_id;
    private int deviceLimit = 5;
    private long tabID;
    private int recommendedIndex = -1;
    private SearchResultCallBack searchResultCallBack;
    private ArrayList<SearchModel> searchOutputModel;
    private int count = 0;
    private TrailorToAssetCallBack toAssetCallBack;
    private CommonCallBack ksHouseHoldIdCallBack;
    private KsHouseHoldDevice ksHouseHoldDevice;
    private KsHouseHoldDeviceAddCallBack ksHouseHoldDeviceAddCallBack;
    private DeleteWatchListCallBack deleteWatchListCallBack;
    private List<MediaTypeModel> currentMediaTypes;
    private AssetRuleCallback assetRuleCallback;
    private KsLoginCallBack ksLoginCallBack;
    private TrailorCallBack trailorCallBack;
    private NotificationStatusCallback notificationStatusCallback;
    private FollowTvSeriesCallBack followTvSeriesCallBack;
    private PopularSearchCallBack popularCallBack;
    private WatchlistCallBack watchlistCallBack;
    private AddWatchListCallBack addwatchlistCallBack;
    private SeasonCallBack seasonCallBackSeries;
    private DeleteDeviceCallBack deleteDeviceCallBack;
    private SeasonCallBack seasonCallBack;
    private AllWatchlistCallBack allWatchlist;
    private NotificationCallback notificationCallback;
    private CommonResponseHandler commonResponseHandler;
    private SubscriptionResponseCallBack subscriptionResponseCallBack;
    private EntitlementResponseCallBack entitlementResponseCallBack;
    private CancelRenewalResponseCallBack cancelRenewalResponseCallBack;
    private HouseholdpaymentResponseCallBack householdpaymentResponseCallBack;
    private List<Response<ListResponse<Asset>>> responseListt;
    private List<VIUChannel> channelList;
    private List<AssetService.ListAssetBuilder> listAssetBuilders;
    private int continueWatchingIndex = -1;
    private HomechannelCallBack homechannelCallBack;
    private BookmarkingPositionCallBack bookmarkingPositionCallBack;
    private UserPrefrencesCallBack prefrencesCallBack;
    private DtvListCallBack dtvListCallBack;
    private MBBAccountListCallBack mbbAccountListCallBack;
    private DTVCallBack dtvCallBack;
    private MBBAccountCallBack mBBAccountCallBack;
    private HBBAccountCallBack hbbAccountCallBack;
    private GenreCallBack genreCallBack;
    private UpdateDeviceCallBack updateDeviceCallBack;
    private SignUpCallBack signUpCallBack;
    private HouseHoldAddCallBack houseHoldAddCallBack;
    private BillPaymentCallBack billPaymentCallBack;
    private PurchaseSubscriptionCallBack purchaseSubscriptionCallBack;
    private InvokeApiCallBack invokeApiCallBack;
    private RemovePaymentCallBack removePaymentCallBack;
    private UpdatePaymentMethodCallBack updatePaymentMethodCallBack;
    private List<OttUserService.UpdateDynamicDataOttUserBuilder> ottUserBuilders;
    private SubscriptionAssetListResponse subscriptionAssetListResponse;

    public KsServices(Context context) {
        this.activity = context;
        SharedPrefHelper session = SharedPrefHelper.getInstance(activity);
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public void deleteFromWatchlistList(final String idfromAssetWatchlist, DeleteWatchListCallBack callBack) {

        deleteWatchListCallBack = callBack;
        Runnable runnable = () -> {
            clientSetupKs();
            PersonalListService.DeletePersonalListBuilder deletePersonalListBuilder = (PersonalListService.DeletePersonalListBuilder) PersonalListService.delete(Long.parseLong(idfromAssetWatchlist)).setCompletion(result -> {
                if (result.isSuccess()) {

                    deleteWatchListCallBack.deleteWatchlistDetail(true, "", "");
//
                }
//                else {
//                    if (result.error != null) {
//                        deleteWatchListCallBack.deleteWatchlistDetail(false, result.error.getCode(), result.error.getMessage());
//                    } else {
//                        deleteWatchListCallBack.deleteWatchlistDetail(false, "", activity.getResources().getString(R.string.something_went_wrong));
//                    }
//
//                }

                else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        // PrintLogging.printLog("","errorCodess-->>"+errorCode);
                        Log.e("errorCodessMyPlayList", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        deleteFromWatchlistList(idfromAssetWatchlist, callBack);
                                    } else {
                                        deleteWatchListCallBack.deleteWatchlistDetail(false, "", activity.getResources().getString(R.string.something_went_wrong));
                                    }
                                }
                            });

                        } else {
                            deleteWatchListCallBack.deleteWatchlistDetail(false, result.error.getCode(), result.error.getMessage());
                        }
                    } else {
                        deleteWatchListCallBack.deleteWatchlistDetail(false, "", activity.getResources().getString(R.string.something_went_wrong));
                    }
                    //channelCallBack.response(false, commonResponse);
                }

            });
            getRequestQueue().queue(deletePersonalListBuilder.build(client));
        };
        new Thread(runnable).start();

    }

    public void getWatchlist(String ksql, AllWatchlistCallBack callBack) {
        allWatchlist = callBack;
        responseListt = new ArrayList<>();
        final SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        searchAssetFilter.setKSql(KSQL.watchList(ksql));
        searchAssetFilter.orderBy("NAME_ASC");
        final FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(50);
        Runnable runnable = () -> {
            clientSetupKs();
            AssetService.ListAssetBuilder listAssetBuilder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {


                if (result.isSuccess()) {
                    if (result.results.getTotalCount() > 0) {
                        responseListt.add(result);
                        allWatchlist.getAllWatchlistDetail(true, "", "", responseListt);
                    } else {
                        allWatchlist.getAllWatchlistDetail(true, "", "", responseListt);
                    }
                } else {
                    if (result.error != null) {
                        allWatchlist.getAllWatchlistDetail(false, result.error.getCode(), result.error.getMessage(), responseListt);
                    } else {
                        allWatchlist.getAllWatchlistDetail(false, "", activity.getResources().getString(R.string.something_went_wrong), responseListt);
                    }

                }
            });


            getRequestQueue().queue(listAssetBuilder.build(client));
        };
        new Thread(runnable).start();
    }

    public void getAdsContext(String assetId, String fileId, AdContextCallback callBack) {
        clientSetupKs();
        PlaybackContextOptions playbackContextOptions = new PlaybackContextOptions();
        playbackContextOptions.assetFileIds(fileId);
        playbackContextOptions.mediaProtocol("https");
        playbackContextOptions.context("PLAYBACK");
        playbackContextOptions.urlType("PLAYMANIFEST");
        playbackContextOptions.streamerType("");

        AssetService.GetAdsContextAssetBuilder listAssetBuilder = AssetService.getAdsContext(assetId, AssetType.MEDIA, playbackContextOptions).setCompletion(result -> {
            if (result.isSuccess() && result.results != null && result.results.getSources() != null && result.results.getSources().size() > 0 && result.results.getSources().get(0) != null && result.results.getSources().get(0).getAdsPolicy() != null && result.results.getSources().get(0).getAdsPolicy() != null) {
                callBack.getAdsPolicy(result.results.getSources().get(0).getAdsPolicy().getValue());
            } else {
                callBack.getAdsPolicy("");
            }
        });
        getRequestQueue().queue(listAssetBuilder.build(client));

    }

    private List<VIUChannel> dtChannelList;

    public void callDeepSearchAssetListing(long l, List<VIUChannel> list, String ksql, String filterValue, int counter, int pageSize, HomechannelCallBack callBack) {
        clientSetupKs();
        homechannelCallBack = callBack;
        this.dtChannelList = list;
        listAssetBuilders = new ArrayList<>();
        responseList = new ArrayList<Response<ListResponse<Asset>>>();

        for (int i = 0; i < dtChannelList.size(); i++) {
            long idd = dtChannelList.get(i).getId();
            //  Log.w("idsssoftiles", idd + " " + counter);
            int iid = (int) idd;
            ChannelFilter channelFilter = new ChannelFilter();
            channelFilter.setIdEqual(iid);
            String name = "";

            String KsqlValue = forDeepSearch(ksql, filterValue);
            PrintLogging.printLog("", "genreValueIs" + KsqlValue);


            channelFilter.setKSql(KsqlValue);
            if (AppConstants.SORT_VALUE.equalsIgnoreCase("")) {

            } else {
                channelFilter.setOrderBy(AppConstants.SORT_VALUE);
            }


            PrintLogging.printLog("", "sortvalueIS" + AppConstants.SORT_VALUE);
            // PersonalListSearchFilter


            FilterPager filterPager = new FilterPager();
            filterPager.setPageIndex(counter);
            if (pageSize <= 0)
                filterPager.setPageSize(25);
            else
                filterPager.setPageSize(pageSize);

            AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(result -> {
                //   Log.w("homeListing", result.isSuccess() + "");
                if (result.isSuccess()) {
                    responseList.add(result);
                    count++;
                    //   Log.w("countValues", count + "");
                    if (count == listAssetBuilders.size()) {
                        int totalCount = result.results.getTotalCount();
                        if (totalCount != 0) {
                            Log.w("countValues in", count + "");
                            if (result.results != null && result.results.getObjects() != null) {
                                if (result.results.getObjects().size() > 0) {
                                    homechannelCallBack.response(true, responseList, dtChannelList);
                                } else {
                                    homechannelCallBack.response(false, responseList, dtChannelList);
                                }
                            } else {
                                homechannelCallBack.response(false, responseList, dtChannelList);
                            }

                        } else {
                            homechannelCallBack.response(false, responseList, dtChannelList);
                        }

                    }

                } else {
                   /* ErrorHandling.checkErrorType(result.error, (code, status) -> {
                        if (code.equalsIgnoreCase(AppConstants.KS_EXPIRE) && status) {
                            callDeepSearchAssetListing(l, list, ksql, filterValue, counter, pageSize, callBack);
                        } else {
                            homechannelCallBack.response(false, responseList, dtChannelList);
                        }
                    });*/
                }


            });
            listAssetBuilders.add(builder);
        }
        // count=listAssetBuilders.size();

       /* MultiRequestBuilder multiRequestBuilder=new MultiRequestBuilder();

        for (int j=0;j<listAssetBuilders.size();j++){
            multiRequestBuilder = multiRequestBuilder.add(listAssetBuilders.get(j));
        }*/
        getRequestQueue().queue(listAssetBuilders.get(0).build(client));

    }

    public static String forDeepSearch(String ksql, String name) {
        String one = "(and name ~'";
        String two = name;
        String third = "' (or " + ksql + "))";
        String KSQL = one + two + third;
        return KSQL;

    }


    public void callAssetListing(long l, List<VIUChannel> list, int counter, HomechannelCallBack callBack) {
        clientSetupKs();
        homechannelCallBack = callBack;
        this.channelList = list;
        listAssetBuilders = new ArrayList<>();
        responseList = new ArrayList<>();

        for (int i = 0; i < channelList.size(); i++) {
            long idd = channelList.get(i).getId();
            Log.w("idsssoftiles", idd + " " + counter);
            int iid = (int) idd;
            ChannelFilter channelFilter = new ChannelFilter();
            channelFilter.setIdEqual(iid);

            // PersonalListSearchFilter

            // Sear

            FilterPager filterPager = new FilterPager();
            filterPager.setPageIndex(counter);
            filterPager.setPageSize(20);

            AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(result -> {
                Log.w("homeListing", result.isSuccess() + "");
                if (result.isSuccess()) {
                    responseList.add(result);
                    count++;
                    Log.w("countValues", count + "");
                    if (count == listAssetBuilders.size()) {
                        int totalCount = result.results.getTotalCount();
                        if (totalCount != 0) {
                            Log.w("countValues in", count + "");
                            homechannelCallBack.response(true, responseList, channelList);
                        } else {
                            homechannelCallBack.response(false, responseList, channelList);
                        }

                    }

                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callAssetListing(l, list, counter, callBack);
                                    } else {
                                        homechannelCallBack.response(false, responseList, channelList);
                                    }
                                }
                            });

                        } else {
                            homechannelCallBack.response(false, responseList, channelList);
                        }
                    } else {
                        homechannelCallBack.response(false, responseList, channelList);
                    }
                    //channelCallBack.response(false, commonResponse);
                }


            });
            listAssetBuilders.add(builder);
        }
        // count=listAssetBuilders.size();

       /* MultiRequestBuilder multiRequestBuilder=new MultiRequestBuilder();

        for (int j=0;j<listAssetBuilders.size();j++){
            multiRequestBuilder = multiRequestBuilder.add(listAssetBuilders.get(j));
        }*/
        getRequestQueue().queue(listAssetBuilders.get(0).build(client));

    }

    public void addToFollowlist(String id, AddWatchListCallBack callBack) {
        addwatchlistCallBack = callBack;

        final FollowTvSeries followTvSeries = new FollowTvSeries();
        if (!id.equals("")) {
            followTvSeries.setAssetId(Integer.parseInt(id));
        }


        Runnable runnable = () -> {
            clientSetupKs();

            FollowTvSeriesService.AddFollowTvSeriesBuilder addPersonalListBuilder = FollowTvSeriesService.add(followTvSeries).setCompletion(result -> {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        addwatchlistCallBack.getWatchlistDetail(String.valueOf(result.results.getAssetId()), "", "");
                    } else {
                        addwatchlistCallBack.getWatchlistDetail("", "", activity.getResources().getString(R.string.something_went_wrong));
                    }
                } else {
                    if (result.error != null) {
                        addwatchlistCallBack.getWatchlistDetail("", result.error.getCode(), result.error.getMessage());
                    } else {
                        addwatchlistCallBack.getWatchlistDetail("", "", activity.getResources().getString(R.string.something_went_wrong));
                    }

                }

            });
            getRequestQueue().queue(addPersonalListBuilder.build(client));
        };
        new Thread(runnable).start();
    }

    public void deleteSeriesAsset(final String assetID, DeleteWatchListCallBack callBack) {

        deleteWatchListCallBack = callBack;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                clientSetupKs();


                FollowTvSeriesService.DeleteFollowTvSeriesBuilder deleteBuilder = FollowTvSeriesService.delete(Integer.parseInt(assetID)).setCompletion(new OnCompletion<Response<Boolean>>() {
                    @Override
                    public void onComplete(Response<Boolean> result) {
                        PrintLogging.printLog(this.getClass(), "", "deleteSeries" + result.isSuccess());
                        if (result.isSuccess()) {

                            deleteWatchListCallBack.deleteWatchlistDetail(true, "", "");
//
                        }

//                        else {
//                            if (result.error != null) {
//                                deleteWatchListCallBack.deleteWatchlistDetail(false, result.error.getCode(), result.error.getMessage());
//                            } else {
//                                deleteWatchListCallBack.deleteWatchlistDetail(false, "", activity.getResources().getString(R.string.something_went_wrong));
//                            }
//
//                        }

                        else {
                            if (result.error != null) {
                                String errorCode = result.error.getCode();
                                Log.e("KsExpireDeleteSeries", errorCode);
                                if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                                    new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                        @Override
                                        public void response(CommonResponse response) {
                                            if (response.getStatus()) {
                                                deleteSeriesAsset(assetID, callBack);
                                            } else {
                                                deleteWatchListCallBack.deleteWatchlistDetail(false, "", activity.getResources().getString(R.string.something_went_wrong));
                                            }
                                        }
                                    });

                                } else {
                                    deleteWatchListCallBack.deleteWatchlistDetail(false, result.error.getCode(), result.error.getMessage());
                                }
                            } else {
                                deleteWatchListCallBack.deleteWatchlistDetail(false, "", activity.getResources().getString(R.string.something_went_wrong));
                            }
                            //channelCallBack.response(false, commonResponse);
                        }
                    }
                });
                getRequestQueue().queue(deleteBuilder.build(client));
            }

        };
        new Thread(runnable).start();

    }

    public void callNumberOfEpisodes(String seriesId, int mediaType, CommonResponseHandler callBack) {
        clientSetupKs();
        commonResponseHandler = callBack;
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        String kSQL = KSQL.forNumberOfEpisodes(seriesId);
        searchAssetFilter.setKSql(kSQL);
        searchAssetFilter.setTypeIn(String.valueOf(mediaType));

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                PrintLogging.printLog(this.getClass(), "", "episodesDataStatus-->>" + result.isSuccess() + "");
                if (result.isSuccess()) {
                    commonResponseHandler.response(true, "", "", result);
                } else {
                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callNumberOfEpisodes(seriesId, mediaType, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        commonResponseHandler.response(false, "", "", result);
                                    }
                                }
                            });
                        else {
                            commonResponseHandler.response(false, "", "", result);
                        }
                    } else {
                        commonResponseHandler.response(false, "", "", result);
                    }
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void checkSeriesList(FollowTvSeriesCallBack callBack) {
        followTvSeriesCallBack = callBack;

        final FollowTvSeriesFilter followTvSeriesFilter = new FollowTvSeriesFilter();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                clientSetupKs();
                FollowTvSeriesService.ListFollowTvSeriesBuilder listBuilder = FollowTvSeriesService.list(followTvSeriesFilter).setCompletion(new OnCompletion<Response<ListResponse<FollowTvSeries>>>() {
                    @Override
                    public void onComplete(Response<ListResponse<FollowTvSeries>> result) {
                        PrintLogging.printLog(this.getClass(), "", "followseries" + result.isSuccess());
                        if (result.isSuccess()) {
                            if (result.results.getTotalCount() > 0) {
                                followTvSeriesCallBack.getSeriesFollowList(true, "", "", result);
                            } else {
                                followTvSeriesCallBack.getSeriesFollowList(true, "", "", result);
                            }
                        } else {
                            if (result.error != null) {
                                String errorCode = result.error.getCode();
                                Log.e("KsExpireSeries", errorCode);
                                if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                                    new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                        @Override
                                        public void response(CommonResponse response) {
                                            if (response.getStatus()) {
                                                checkSeriesList(callBack);
                                            } else {
                                                followTvSeriesCallBack.getSeriesFollowList(false, "", activity.getResources().getString(R.string.something_went_wrong), result);
                                            }
                                        }
                                    });

                                } else {
                                    followTvSeriesCallBack.getSeriesFollowList(false, result.error.getCode(), result.error.getMessage(), result);
                                }
                            } else {
                                followTvSeriesCallBack.getSeriesFollowList(false, "", activity.getResources().getString(R.string.something_went_wrong), result);
                            }
                            //channelCallBack.response(false, commonResponse);
                        }
//                        else {
//                            if (result.error != null) {
//                                followTvSeriesCallBack.getSeriesFollowList(false, result.error.getCode(), result.error.getMessage(), result);
//                            } else {
//                                followTvSeriesCallBack.getSeriesFollowList(false, "", activity.getResources().getString(R.string.something_went_wrong), result);
//                            }
//
//                        }
                    }
                });

                getRequestQueue().queue(listBuilder.build(client));
            }

        };
        new Thread(runnable).start();
    }


    public void callSeriesData(int mediaType, String seriesID, GetSeriesCallBack callBack) {


        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        FilterPager filterPager = new FilterPager();

        filterPager.setPageIndex(1);
        filterPager.setPageSize(1);

        if (mediaType == MediaTypeConstant.getEpisode(activity)) {
            String ksql = KSQL.getSeriesKSQL(MediaTypeConstant.getSeries(activity), seriesID);
            searchAssetFilter.setKSql(ksql);
        }


        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            PrintLogging.printLog("", "response" + result.isSuccess());
            if (result.isSuccess()) {
                if (result.results != null && result.results.getObjects() != null && result.results.getTotalCount() > 0) {
                    callBack.onSuccess(result.results.getObjects());
                } else {
                    callBack.onFailure();
                }
            } else {
                if (result.error != null) {

                    String errorCode = result.error.getCode();
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    callSeriesData(mediaType, seriesID, callBack);
                                } else {
                                    callBack.onFailure();
                                }
                            }
                        });
                    else {
                        callBack.onFailure();
                    }
                } else {
                    callBack.onFailure();
                }
            }

        });
        getRequestQueue().queue(builder.build(client));
    }


    public void getAssetFromTrailer(String refId, GetSeriesCallBack callBack) {


        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        FilterPager filterPager = new FilterPager();

        filterPager.setPageIndex(1);
        filterPager.setPageSize(1);

        String ksql = KSQL.getAssetFromTrailerKSQL(MediaTypeConstant.getMovie(activity), MediaTypeConstant.getSeries(activity), refId);
        searchAssetFilter.setKSql(ksql);


        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            PrintLogging.printLog("", "response" + result.isSuccess());
            if (result.isSuccess()) {
                if (result.results != null && result.results.getObjects() != null && result.results.getTotalCount() > 0) {
                    callBack.onSuccess(result.results.getObjects());
                } else {
                    callBack.onFailure();
                }
            } else {
                if (result.error != null) {

                    String errorCode = result.error.getCode();
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getAssetFromTrailer(refId, callBack);
                                } else {
                                    callBack.onFailure();
                                }
                            }
                        });
                    else {
                        callBack.onFailure();
                    }
                } else {
                    callBack.onFailure();
                }
            }

        });
        getRequestQueue().queue(builder.build(client));
    }


    public void getProgramFromLinear(String channelId, GetSeriesCallBack callBack) {


        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        FilterPager filterPager = new FilterPager();

        filterPager.setPageIndex(1);
        filterPager.setPageSize(1);
        String one = "(and epg_channel_id='";
        String two = "' start_date<'0' end_date>'0')";
        String ksql = one + channelId + two;
        searchAssetFilter.setKSql(ksql);
        searchAssetFilter.setTypeIn(MediaTypeConstant.getProgram(activity) + "");


        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            PrintLogging.printLog("", "response" + result.isSuccess());
            if (result.isSuccess()) {
                if (result.results != null && result.results.getObjects() != null && result.results.getTotalCount() > 0) {
                    callBack.onSuccess(result.results.getObjects());
                } else {
                    callBack.onFailure();
                }
            } else {
                if (result.error != null) {

                    String errorCode = result.error.getCode();
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getProgramFromLinear(channelId, callBack);
                                } else {
                                    callBack.onFailure();
                                }
                            }
                        });
                    else {
                        callBack.onFailure();
                    }
                } else {
                    callBack.onFailure();
                }
            }

        });
        getRequestQueue().queue(builder.build(client));
    }

    public void callSpotlightSesionEpisode(String seriesId, int assetType, List<Integer> results, HomechannelCallBack callBack) {
        homechannelCallBack = callBack;
        listAssetBuilders = new ArrayList<>();
        responseList = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            long idd = results.get(i);
            Log.w("idsssoftiles", "idsprints" + idd + "-->>");
            int iid = (int) idd;
            String one = "(and SeriesId='";
            String two = "' Season number='";
            String three = "')";
            String kSQL = one + seriesId + two + iid + three;
            Log.w("idsssoftiles", "idsprints" + idd + "-->>" + kSQL);
            SearchAssetFilter searchAssetFilter = new SearchAssetFilter();

            searchAssetFilter.setKSql(kSQL);

            if (assetType == MediaTypeConstant.getDrama(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getWebEpisode(activity) + "");
            } else if (assetType == MediaTypeConstant.getWebEpisode(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getWebEpisode(activity) + "");
            }

            //  searchAssetFilter.typeIn(MediaTypeConstant.getSpotlightEpisode() + "");
            DynamicOrderBy dynamicOrderBy = new DynamicOrderBy();
            dynamicOrderBy.setName("Episode number");
            dynamicOrderBy.orderBy("META_ASC");
            searchAssetFilter.setDynamicOrderBy(dynamicOrderBy);

            AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter).setCompletion(result -> {
                Log.w("", "episodesDataStatus-->>" + result.isSuccess() + "");
                responseList.add(result);
                count++;

                Log.w("countValues", count + "");
                if (count == listAssetBuilders.size()) {
                    Log.w("countValues in", count + "");
                    KsServices.this.homechannelCallBack.response(true, responseList, channelList);
                }
            });
            listAssetBuilders.add(builder);
        }
        MultiRequestBuilder multiRequestBuilder = new MultiRequestBuilder();
        for (int j = 0; j < listAssetBuilders.size(); j++) {
            multiRequestBuilder = multiRequestBuilder.add(listAssetBuilders.get(j));
        }
        getRequestQueue().queue(multiRequestBuilder.build(client));


    }

    public void callSpotlightSeasons(int i, String seriesId, int assetType, SeasonCallBack seasonCallBack) {
        seasonCallBackSeries = seasonCallBack;
        clientSetupKs();
        List<AssetGroupBy> assetGroup = new ArrayList<>();
        AssetMetaOrTagGroupBy assetGroupBySeries = new AssetMetaOrTagGroupBy();

        assetGroupBySeries.setValue("Season number");
        assetGroup.add(assetGroupBySeries);

        DetachedResponseProfile responseProfile = new DetachedResponseProfile();
        DetachedResponseProfile relatedProfile = new DetachedResponseProfile();

        AggregationCountFilter aggregationCountFilter = new AggregationCountFilter();
        relatedProfile.setFilter(aggregationCountFilter);
        relatedProfile.name("Episodes_In_Season");
        List<DetachedResponseProfile> list = new ArrayList<>();
        list.add(relatedProfile);
        responseProfile.setRelatedProfiles(list);


        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        searchAssetFilter.setGroupBy(assetGroup);

        String one = "SeriesId=";
        String two = "'";
        String kSQL = one + "'" + seriesId + two;
        PrintLogging.printLog(this.getClass(), "", "respponsssss" + kSQL);

        searchAssetFilter.orderBy("NAME_ASC");
        searchAssetFilter.setKSql(kSQL);

        if (assetType == MediaTypeConstant.getDrama(activity)) {
            searchAssetFilter.typeIn(MediaTypeConstant.getWebEpisode(activity) + "");
        } else if (assetType == MediaTypeConstant.getWebEpisode(activity)) {
            searchAssetFilter.typeIn(MediaTypeConstant.getWebEpisode(activity) + "");
        }
        PrintLogging.printLog(this.getClass(), "", "assetType" + assetType);


        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                PrintLogging.printLog(this.getClass(), "", "seasosss" + result.isSuccess());
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            seasonCallBackSeries.result(true, result);
                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    }
                } else {
                    seasonCallBackSeries.result(false, result);
                }
            }
        });
        builder.setResponseProfile(responseProfile);
        getRequestQueue().queue(builder.build(client));

    }


    public void callSeasonEpisodes(int counter, String seriesId, int assetType, List<Integer> results, int seasonCounter, String sortType, SimilarMovieCallBack callBack) {
        clientSetupKs();
        similarMovieCallBack = callBack;
        final CommonResponse commonResponse = new CommonResponse();
        try {
            long idd = results.get(seasonCounter);
            // Log.w("idsssoftiles", "idsprints" + idd + "-->>");
            int iid = (int) idd;
            String one = "(and SeriesID='";
            String two = "' SeasonNumber='";
            String three = "')";
            String kSQL = one + seriesId + two + iid + three;
            // Log.w("idsssoftiles", "idsprints" + idd + "-->>" + kSQL);
            SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
            searchAssetFilter.setKSql(kSQL);
            Log.e("ASSET TYPE", String.valueOf(assetType));
            if (assetType == MediaTypeConstant.getSeries(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getEpisode(activity) + "");
            } else if (assetType == MediaTypeConstant.getWebEpisode(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getEpisode(activity) + "");
            }

            // searchAssetFilter.typeIn("603");
            DynamicOrderBy dynamicOrderBy = new DynamicOrderBy();
            dynamicOrderBy.setName(sortType);
            dynamicOrderBy.orderBy("META_ASC");
            searchAssetFilter.setDynamicOrderBy(dynamicOrderBy);

            FilterPager filterPager = new FilterPager();
            filterPager.setPageIndex(counter);
            filterPager.setPageSize(20);


            AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            if (result.results.getObjects().size() > 0) {
                                commonResponse.setStatus(true);
                                commonResponse.setAssetList(result);
                                similarMovieCallBack.response(true, commonResponse);
                            } else {
                                similarMovieCallBack.response(false, commonResponse);
                            }
                        } else {
                            similarMovieCallBack.response(false, commonResponse);
                        }
                    } else {
                        similarMovieCallBack.response(false, commonResponse);
                    }
                } else {


                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callSeasonEpisodes(counter, seriesId, assetType, results, seasonCounter, sortType, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        similarMovieCallBack.response(false, commonResponse);
                                    }
                                }
                            });
                        else {
                            similarMovieCallBack.response(false, commonResponse);
                        }
                    } else {
                        similarMovieCallBack.response(false, commonResponse);
                    }


                }
            });
            getRequestQueue().queue(builder.build(client));
        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "Exception", "" + e);

        }

    }

    public void callSeasonEpisodesForBingeWatch(int counter, String seriesId, int assetType, List<Integer> results, int seasonCounter, String sortType, SimilarMovieCallBack callBack) {
        clientSetupKs();
        similarMovieCallBack = callBack;
        final CommonResponse commonResponse = new CommonResponse();
        try {
            long idd = results.get(seasonCounter);
            // Log.w("idsssoftiles", "idsprints" + idd + "-->>");
            int iid = (int) idd;
            String one = "(and SeriesID='";
            String two = "' SeasonNumber='";
            String three = "')";
            String kSQL = one + seriesId + two + iid + three;
            // Log.w("idsssoftiles", "idsprints" + idd + "-->>" + kSQL);
            SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
            searchAssetFilter.setKSql(kSQL);
            Log.d("frfrfrfrfrf", kSQL);
            Log.e("ASSET TYPE", String.valueOf(assetType));
            if (assetType == MediaTypeConstant.getSeries(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getEpisode(activity) + "");
            } else if (assetType == MediaTypeConstant.getWebEpisode(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getEpisode(activity) + "");
            }

            // searchAssetFilter.typeIn("603");
            DynamicOrderBy dynamicOrderBy = new DynamicOrderBy();
            dynamicOrderBy.setName(sortType);
            dynamicOrderBy.orderBy("META_ASC");
            searchAssetFilter.setDynamicOrderBy(dynamicOrderBy);

            FilterPager filterPager = new FilterPager();
            filterPager.setPageIndex(counter);
            filterPager.setPageSize(20);


            AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            if (result.results.getObjects().size() > 0) {
                                commonResponse.setStatus(true);
                                commonResponse.setAssetList(result);
                                similarMovieCallBack.response(true, commonResponse);
                            } else {
                                similarMovieCallBack.response(false, commonResponse);
                            }
                        } else {
                            similarMovieCallBack.response(false, commonResponse);
                        }
                    } else {
                        similarMovieCallBack.response(false, commonResponse);
                    }
                } else {


                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callSeasonEpisodes(counter, seriesId, assetType, results, seasonCounter, sortType, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        similarMovieCallBack.response(false, commonResponse);
                                    }
                                }
                            });
                        else {
                            similarMovieCallBack.response(false, commonResponse);
                        }
                    } else {
                        similarMovieCallBack.response(false, commonResponse);
                    }


                }
            });
            getRequestQueue().queue(builder.build(client));
        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "Exception", "" + e);

        }

    }

    public void callEpisodes(int counter, String seriesId, int assetType, String sortType, SimilarMovieCallBack callBack) {
        clientSetupKs();
        similarMovieCallBack = callBack;
        final CommonResponse commonResponse = new CommonResponse();
        try {
            // long idd = results.get(seasonCounter);
            // Log.w("idsssoftiles", "idsprints" + idd + "-->>");
            int iid = (int) 0;
            String one = "(and SeriesID='";
            String two = "' SeasonNumber='";
            String three = "')";
            String kSQL = one + seriesId + three;
            // Log.w("idsssoftiles", "idsprints" + idd + "-->>" + kSQL);
            SearchAssetFilter searchAssetFilter = new SearchAssetFilter();

            searchAssetFilter.setKSql(kSQL);
            Log.e("ASSET TYPE", String.valueOf(assetType));
            if (assetType == MediaTypeConstant.getSeries(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getEpisode(activity) + "");
            } else if (assetType == MediaTypeConstant.getWebEpisode(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getEpisode(activity) + "");
            }

            // searchAssetFilter.typeIn("603");
            DynamicOrderBy dynamicOrderBy = new DynamicOrderBy();
            dynamicOrderBy.setName(sortType);
            dynamicOrderBy.orderBy("META_ASC");
            searchAssetFilter.setDynamicOrderBy(dynamicOrderBy);

            FilterPager filterPager = new FilterPager();
            filterPager.setPageIndex(counter);
            filterPager.setPageSize(20);


            AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            if (result.results.getObjects().size() > 0) {
                                commonResponse.setStatus(true);
                                commonResponse.setAssetList(result);
                                similarMovieCallBack.response(true, commonResponse);
                            } else {
                                similarMovieCallBack.response(false, commonResponse);
                            }
                        } else {
                            similarMovieCallBack.response(false, commonResponse);
                        }
                    } else {
                        similarMovieCallBack.response(false, commonResponse);
                    }
                } else {


                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callEpisodes(counter, seriesId, assetType, sortType, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        similarMovieCallBack.response(false, commonResponse);
                                    }
                                }
                            });
                        else {
                            similarMovieCallBack.response(false, commonResponse);
                        }
                    } else {
                        similarMovieCallBack.response(false, commonResponse);
                    }


                }
            });
            getRequestQueue().queue(builder.build(client));
        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "Exception", "" + e);

        }

    }

    //TODO KS Handling on callSesionEpisode Listing Activity
    public void callSesionEpisode(int counter, String seriesId, int assetType, List<Integer> results, HomechannelCallBack callBack) {
        clientSetupKs();
        homechannelCallBack = callBack;
        listAssetBuilders = new ArrayList<>();
        responseList = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            long idd = results.get(i);
            int iid = (int) idd;
            String one = "(and SeriesId='";
            String two = "' Season number='";
            String three = "')";
            String kSQL = one + seriesId + two + iid + three;
            SearchAssetFilter searchAssetFilter = new SearchAssetFilter();

            searchAssetFilter.setKSql(kSQL);
            if (assetType == MediaTypeConstant.getDrama(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getWebEpisode(activity) + "");
            } else if (assetType == MediaTypeConstant.getWebEpisode(activity)) {
                searchAssetFilter.typeIn(MediaTypeConstant.getWebEpisode(activity) + "");
            }
            // searchAssetFilter.typeIn("603");
            DynamicOrderBy dynamicOrderBy = new DynamicOrderBy();
            dynamicOrderBy.setName("Episode number");
            dynamicOrderBy.orderBy("META_ASC");
            searchAssetFilter.setDynamicOrderBy(dynamicOrderBy);

            FilterPager filterPager = new FilterPager();
            filterPager.setPageIndex(counter);
            filterPager.setPageSize(20);


            AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
                //  Log.w("homeListing", result.isSuccess() + "");
                responseList.add(result);
                count++;

                //  Log.w("countValues", count + "");
                if (count == listAssetBuilders.size()) {
                    //  Log.w("countValues in", count + "");
                    KsServices.this.homechannelCallBack.response(true, responseList, null);
                }

            });
            listAssetBuilders.add(builder);
        }

        MultiRequestBuilder multiRequestBuilder = new MultiRequestBuilder();
        for (int j = 0; j < listAssetBuilders.size(); j++) {
            multiRequestBuilder = multiRequestBuilder.add(listAssetBuilders.get(j));
        }
        getRequestQueue().queue(multiRequestBuilder.build(client));
    }

    public void getClipData(String ref_id, SeasonCallBack callBack) {
        seasonCallBackSeries = callBack;
        clientSetupKs();


        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        String one = "(and asset_type='";
        String two = "' TrailerParentRefId ~ '";
        String three = "')";
        String kSQL = one + MediaTypeConstant.getClip() + two + ref_id + three;
        PrintLogging.printLog(this.getClass(), "", "trailorKSQL" + kSQL);
        searchAssetFilter.setKSql(kSQL);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(1);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                PrintLogging.printLog(this.getClass(), "", "clipDataa" + result.isSuccess());
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            seasonCallBackSeries.result(true, result);
                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    }
                } else {
                    seasonCallBackSeries.result(false, result);
                }
            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void getSeriesFromClip(String ref_id, TrailorToAssetCallBack callBack) {
        toAssetCallBack = callBack;
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();

        // "kSql" : "(or Ref Id ~ 'DHORIZONSPECTRE')"
        String one = "(or Ref Id = '";
        String two = "')";
        String kSQL = one + ref_id + two;
        PrintLogging.printLog(this.getClass(), "", "seriesKSQL" + kSQL);
        searchAssetFilter.setKSql(kSQL);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(1);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                PrintLogging.printLog(this.getClass(), "", "seriesValues" + result.isSuccess());
                if (result.isSuccess()) {
                    if (result.results.getTotalCount() > 0) {
                        PrintLogging.printLog(this.getClass(), "", "trailorValues" + result.results.getTotalCount());
                        toAssetCallBack.getAsset(result.results.getObjects().get(0));
                        // trailorCallBack.getTrailorURL(true,AssetContent.getTrailorUrl(result.results.getObjects().get(0)));
                    } else {
                        toAssetCallBack.getAsset(null);
                    }
                } else {
                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getSeriesFromClip(ref_id, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        toAssetCallBack.getAsset(null);
                                    }
                                }
                            });
                        else {
                            toAssetCallBack.getAsset(null);
                        }
                    } else {
                        toAssetCallBack.getAsset(null);
                    }
                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void callSeasons(int i, String seriesID, int assetType, SeasonCallBack callBack) {
        seasonCallBack = callBack;
        clientSetupKs();

        List<AssetGroupBy> assetGroup = new ArrayList<>();
        AssetMetaOrTagGroupBy assetGroupBy = new AssetMetaOrTagGroupBy();

        assetGroupBy.setValue("SeasonNumber");
        assetGroup.add(assetGroupBy);

        DetachedResponseProfile responseProfile = new DetachedResponseProfile();
        DetachedResponseProfile relatedProfile = new DetachedResponseProfile();

        AggregationCountFilter aggregationCountFilter = new AggregationCountFilter();
        relatedProfile.setFilter(aggregationCountFilter);
        relatedProfile.name("Episodes_In_Season");
        List<DetachedResponseProfile> list = new ArrayList<>();
        list.add(relatedProfile);
        responseProfile.setRelatedProfiles(list);


        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        searchAssetFilter.setGroupBy(assetGroup);

        String one = "SeriesId='";
        String two = "'";
        String kSQL = one + seriesID + two;

        searchAssetFilter.setKSql(kSQL);

        if (assetType == MediaTypeConstant.getSeries(activity)) {
            searchAssetFilter.typeIn(MediaTypeConstant.getEpisode(activity) + "");
        } else if (assetType == MediaTypeConstant.getEpisode(activity)) {
            searchAssetFilter.typeIn(MediaTypeConstant.getEpisode(activity) + "");
        }

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        Log.e("Result", String.valueOf(result.isSuccess()));
                        if (result.results.getTotalCount() > 0) {
                            seasonCallBack.result(true, result);
                        } else {
                            seasonCallBack.result(false, result);
                        }
                    }//
                } else {
                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callSeasons(i, seriesID, assetType, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        seasonCallBack.result(false, result);
                                    }
                                }
                            });
                        else {
                            seasonCallBack.result(false, result);
                        }
                    } else {
                        seasonCallBack.result(false, result);
                    }

                }
            }
        });
        builder.setResponseProfile(responseProfile);
        getRequestQueue().queue(builder.build(client));
    }

    public void getNotificationStatus(String id, String status, NotificationStatusCallback callback) {
        notificationStatusCallback = callback;
        clientSetupKs();
        InboxMessageService.UpdateStatusInboxMessageBuilder messageBuilder = InboxMessageService.updateStatus(id, InboxMessageStatus.get(status)).setCompletion(result -> {
            if (result.isSuccess()) {

                notificationStatusCallback.getnotificationstatus(result);

            } else {


                if (result.error != null) {

                    String errorCode = result.error.getCode();
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getNotificationStatus(id, status, callback);
                                    //getSubCategories(context, subCategoryCallBack);
                                } else {
                                    notificationStatusCallback.getnotificationstatus(result);
                                }
                            }
                        });
                    else {
                        notificationStatusCallback.getnotificationstatus(result);
                    }
                } else {
                    notificationStatusCallback.getnotificationstatus(result);
                }
            }
        });

        getRequestQueue().queue(messageBuilder.build(client));
    }

    public void getNotification(NotificationCallback callback) {
        notificationCallback = callback;
        clientSetupKs();

        InboxMessageFilter filter = new InboxMessageFilter();
        FilterPager pagerFilter = new FilterPager();
        pagerFilter.setPageIndex(1);
        pagerFilter.setPageSize(100);

        InboxMessageService.ListInboxMessageBuilder getInboxMessageBuilder = InboxMessageService.list(filter, pagerFilter).setCompletion(new OnCompletion<Response<ListResponse<InboxMessage>>>() {
            @Override
            public void onComplete(Response<ListResponse<InboxMessage>> result) {
                PrintLogging.printLog(this.getClass(), "", "notificationList" + result.isSuccess());
                if (result.isSuccess()) {
                    if (result.results.getTotalCount() > 0) {
                        PrintLogging.printLog(this.getClass(), "", "videosview" + result.results.getTotalCount());
                        notificationCallback.getnotification(true, result);
                    } else {
                        notificationCallback.getnotification(false, result);
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(response -> {
                                if (response.getStatus()) {
                                    getNotification(callback);
                                } else {
                                    notificationCallback.getnotification(false, result);
                                }
                            });

                        } else {
                            notificationCallback.getnotification(false, result);
                        }
                    } else {
                        notificationCallback.getnotification(false, result);
                    }
                }
            }
        });

        getRequestQueue().queue(getInboxMessageBuilder.build(client));
    }

    public void deleteHouseHoldDevice(String udid, final DeleteDeviceCallBack callBack) {
        loginClient(KsPreferenceKey.getInstance(activity).getStartSessionKs());
        deleteDeviceCallBack = callBack;
        HouseholdDeviceService.DeleteHouseholdDeviceBuilder builder = HouseholdDeviceService.delete(udid).setCompletion(new OnCompletion<Response<Boolean>>() {
            @Override
            public void onComplete(Response<Boolean> result) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        deleteDeviceCallBack.deleteSatus(true, "");
                    } else {
                        deleteDeviceCallBack.deleteSatus(false, "");
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(response -> {
                                if (response.getStatus()) {
                                    deleteHouseHoldDevice(udid, callBack);
                                } else {
                                    deleteDeviceCallBack.deleteSatus(false, "");
                                }
                            });

                        } else {
                            deleteDeviceCallBack.deleteSatus(false, result.error.getMessage());
                        }
                    } else {
                        deleteDeviceCallBack.deleteSatus(false, "");
                    }
                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void getSimilarChannel(int counter, Integer type, String genre, SeasonCallBack seasonCallBack) {
        seasonCallBackSeries = seasonCallBack;
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();

        String similarChannel = KSQL.forSimilarChannel(type, genre);

        searchAssetFilter.setKSql(similarChannel);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(25);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            if (result.results.getObjects().size() > 0) {
                                seasonCallBackSeries.result(true, result);
                            } else {
                                seasonCallBackSeries.result(false, result);
                            }

                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    }
                } else {

                    if (result.error != null) {
                        String errorCode = result.error.getCode();

                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getSimilarChannel(counter, type, genre, seasonCallBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        seasonCallBackSeries.result(false, result);
                                    }
                                }
                            });
                        else {
                            seasonCallBackSeries.result(false, result);
                        }
                    } else {
                        seasonCallBackSeries.result(false, result);
                    }


                }
            } else {
                seasonCallBackSeries.result(false, null);
            }
        });
        getRequestQueue().queue(builder.build(client));

    }

    public void callLiveNowRail(int counter, SeasonCallBack seasonCallBack) {
        seasonCallBackSeries = seasonCallBack;
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        String kSQL = "(and start_date<'0' end_date>'0' )";
        searchAssetFilter.setTypeIn("0");
        searchAssetFilter.setKSql(kSQL);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            if (result.results.getObjects().size() > 0) {
                                seasonCallBackSeries.result(true, result);
                            } else {
                                seasonCallBackSeries.result(false, result);
                            }

                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callLiveNowRail(counter, seasonCallBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        seasonCallBackSeries.result(false, result);
                                    }
                                }
                            });
                        else {
                            seasonCallBackSeries.result(false, result);
                        }
                    } else {
                        seasonCallBackSeries.result(false, result);
                    }
                }
            } else {
                seasonCallBackSeries.result(false, null);
            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void callLiveEPGDRail(String externalId, String startDate, String endDate, List<VIUChannel> list) {
        String EPGListKSQL;
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();

        EPGListKSQL = KSQL.forEPGRail(externalId, startDate, endDate);
        searchAssetFilter.setKSql(EPGListKSQL);
        searchAssetFilter.typeIn("0");
        searchAssetFilter.setOrderBy("START_DATE_ASC");
        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            responseList.add(result);
                            homechannelCallBack.response(true, this.responseList, list);
                        } else {
                            homechannelCallBack.response(false, null, null);
                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("KsExpireDeviceManage", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callLiveEPGDRail(externalId, startDate, endDate, list);
                                    } else {
                                        homechannelCallBack.response(false, null, null);

                                    }
                                }
                            });

                        } else {
                            homechannelCallBack.response(false, null, null);

                        }
                    } else {
                        homechannelCallBack.response(false, null, null);

                    }
                }
            } else {
                homechannelCallBack.response(false, null, null);

            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void callLiveEPGDRailListing(String externalId, String startDate, String endDate, int counter, TrendingCallBack trendingCallBack) {
        String EPGListKSQL;
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();

        EPGListKSQL = KSQL.forEPGRail(externalId, startDate, endDate);
        searchAssetFilter.setKSql(EPGListKSQL);
        searchAssetFilter.typeIn("0");
        searchAssetFilter.setOrderBy("START_DATE_ASC");
        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            trendingCallBack.getList(true, result.results.getObjects(), result.results.getTotalCount());

                        } else {
                            trendingCallBack.getList(false, null, 0);

                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("KsExpireDeviceManage", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callLiveEPGDRailListing(externalId, startDate, endDate, counter, trendingCallBack);
                                    } else {
                                        trendingCallBack.getList(false, null, 0);

                                    }
                                }
                            });

                        } else {
                            trendingCallBack.getList(false, null, 0);

                        }
                    } else {
                        trendingCallBack.getList(false, null, 0);

                    }
                }
            } else {
                trendingCallBack.getList(false, null, 0);

            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void callLiveEPGDayWise(String externalId, String startDate, String endDate, int type, int counter, SeasonCallBack callBack) {
        String EPGListKSQL;
        seasonCallBackSeries = callBack;
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        if (type == 1) {
            String particularProgKSQL = KSQL.forParticularProgram(externalId, "0", "0");
            searchAssetFilter.setKSql(particularProgKSQL);
        } else {
            EPGListKSQL = KSQL.forEPGListing(externalId, startDate, endDate);
            searchAssetFilter.setKSql(EPGListKSQL);
        }
        searchAssetFilter.typeIn("0");
        searchAssetFilter.setOrderBy("START_DATE_ASC");
        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(50);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            seasonCallBackSeries.result(true, result);
                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("KsExpireDeviceManage", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callLiveEPGDayWise(externalId, startDate, endDate, type, counter, callBack);
                                    } else {
                                        seasonCallBackSeries.result(false, result);
                                    }
                                }
                            });

                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    } else {
                        seasonCallBackSeries.result(false, result);
                    }
                }
            } else {
                seasonCallBackSeries.result(false, null);
            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void callCatchupData(String externalId, String startDate, int type, SeasonCallBack callBack) {
        String EPGListKSQL;
        seasonCallBackSeries = callBack;
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        if (type == 1) {
            String particularProgKSQL = KSQL.forCatchUpPreviousProgram(externalId, startDate);
            searchAssetFilter.setKSql(particularProgKSQL);
            Log.d("ksqlValueIs", particularProgKSQL);
        } else {
            String particularProgKSQL = KSQL.forCatchUpNextProgram(externalId, startDate);
            searchAssetFilter.setKSql(particularProgKSQL);
            Log.d("ksqlValueIs", particularProgKSQL);
        }


        searchAssetFilter.typeIn("0");
        if (type == 1) {

            searchAssetFilter.setOrderBy("START_DATE_DESC");
        } else {
            searchAssetFilter.setOrderBy("START_DATE_ASC");
        }
        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(5);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            seasonCallBackSeries.result(true, result);
                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("KsExpireDeviceManage", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callCatchupData(externalId, startDate, type, callBack);
                                    } else {
                                        seasonCallBackSeries.result(false, result);
                                    }
                                }
                            });

                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    } else {
                        seasonCallBackSeries.result(false, result);
                    }
                }
            } else {
                seasonCallBackSeries.result(false, null);
            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void liveCatchupData(String externalId, SeasonCallBack callBack) {
        String EPGListKSQL;
        seasonCallBackSeries = callBack;
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        String particularProgKSQL = KSQL.forParticularProgram(externalId, "0", "0");
        searchAssetFilter.setKSql(particularProgKSQL);


        searchAssetFilter.typeIn("0");
        searchAssetFilter.setOrderBy("START_DATE_ASC");
        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(1);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            seasonCallBackSeries.result(true, result);
                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("KsExpireDeviceManage", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        liveCatchupData(externalId, callBack);
                                    } else {
                                        seasonCallBackSeries.result(false, result);
                                    }
                                }
                            });

                        } else {
                            seasonCallBackSeries.result(false, result);
                        }
                    } else {
                        seasonCallBackSeries.result(false, result);
                    }
                }
            } else {
                seasonCallBackSeries.result(false, null);
            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void getTrailorURL(String ref_id, int assetType, TrailorCallBack callBack) {
        clientSetupKs();
        trailorCallBack = callBack;
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        String one = "(and asset_type='";
        String two = "' ParentRefId = '";
        String three = "')";
        String kSQL = one + MediaTypeConstant.getTrailer(activity) + two + ref_id + three;
        PrintLogging.printLog(this.getClass(), "", "trailorKSQL" + kSQL);
        searchAssetFilter.setKSql(kSQL);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(1);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                PrintLogging.printLog(this.getClass(), "", "trailorValues" + result.isSuccess());
                if (result.isSuccess()) {
                    if (result.results.getTotalCount() > 0) {
                        PrintLogging.printLog(this.getClass(), "", "trailorValues" + result.results.getTotalCount());
                        trailorCallBack.getTrailorURL(true, AssetContent.getTrailorUrl(result.results.getObjects().get(0)));
                    } else {
                        trailorCallBack.getTrailorURL(false, "");
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getTrailorURL(ref_id, assetType, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        trailorCallBack.getTrailorURL(false, "");
                                    }
                                }
                            });
                        else {
                            trailorCallBack.getTrailorURL(false, "");
                        }
                    } else {
                        trailorCallBack.getTrailorURL(false, "");
                    }


                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void getBoxSetShows(String ref_id, int assetType, TrailerAssetCallBack callBack) {
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        String one = "(and asset_type='";
        String two = "' ParentRefId ~ '";
        String three = "')";
        String kSQL = one + assetType + two + ref_id + three;
        searchAssetFilter.setKSql(kSQL);
        DynamicOrderBy dynamicOrderBy = new DynamicOrderBy();
        dynamicOrderBy.setName(AppLevelConstants.KEY_TITLE_SORT);
        dynamicOrderBy.orderBy("META_ASC");
        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                if (result.isSuccess()) {
                    if (result.results != null && result.results.getObjects() != null) {
                        if (result.results.getTotalCount() > 0) {
                            callBack.getTrailorAsset(true, result.results.getObjects());
                        } else {
                            callBack.getTrailorAsset(false, null);
                        }
                    } else {
                        callBack.getTrailorAsset(false, null);
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getTrailorAsset(ref_id, assetType, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        callBack.getTrailorAsset(false, null);
                                    }
                                }
                            });
                        else {
                            callBack.getTrailorAsset(false, null);
                        }
                    } else {
                        callBack.getTrailorAsset(false, null);
                    }


                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void getTrailorAsset(String ref_id, int assetType, TrailerAssetCallBack callBack) {
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        String one = "(and (or asset_type='";
        String four = "' asset_type='";
        String two = "') ParentRefId ~ '";
        String three = "')";
        String kSQL = one + MediaTypeConstant.getTrailer(activity) + four + MediaTypeConstant.getHighlight(activity) + two + ref_id + three;
        searchAssetFilter.setKSql(kSQL);
        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                if (result.isSuccess()) {
                    if (result.results != null && result.results.getObjects() != null) {
                        if (result.results.getTotalCount() > 0) {
                            callBack.getTrailorAsset(true, result.results.getObjects());
                        } else {
                            callBack.getTrailorAsset(false, null);
                        }
                    } else {
                        callBack.getTrailorAsset(false, null);
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getTrailorAsset(ref_id, assetType, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        callBack.getTrailorAsset(false, null);
                                    }
                                }
                            });
                        else {
                            callBack.getTrailorAsset(false, null);
                        }
                    } else {
                        callBack.getTrailorAsset(false, null);
                    }


                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void getHighLightAsset(String ref_id, int assetType, TrailerAssetCallBack callBack) {
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        String one = "(and asset_type='";
        String two = "' externalId ~ '";
        String three = "')";
        String kSQL = one + MediaTypeConstant.getHighlight(activity) + two + ref_id + three;
        searchAssetFilter.setKSql(kSQL);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                if (result.isSuccess()) {
                    if (result.results != null && result.results.getObjects() != null) {
                        if (result.results.getTotalCount() > 0) {
                            callBack.getTrailorAsset(true, result.results.getObjects());
                        } else {
                            callBack.getTrailorAsset(false, null);
                        }
                    } else {
                        callBack.getTrailorAsset(false, null);
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getHighLightAsset(ref_id, assetType, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        callBack.getTrailorAsset(false, null);
                                    }
                                }
                            });
                        else {
                            callBack.getTrailorAsset(false, null);
                        }
                    } else {
                        callBack.getTrailorAsset(false, null);
                    }


                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void deleteFromWatchlist(final String idfromAssetWatchlist, DeleteWatchListCallBack callBack) {

        deleteWatchListCallBack = callBack;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                clientSetupKs();
                PersonalListService.DeletePersonalListBuilder deletePersonalListBuilder = (PersonalListService.DeletePersonalListBuilder) PersonalListService.delete(Long.parseLong(idfromAssetWatchlist)).setCompletion(new OnCompletion<Response<Void>>() {
                    @Override
                    public void onComplete(Response<Void> result) {
                        PrintLogging.printLog(this.getClass(), "", "deleteDevice" + result.isSuccess());
                        if (result.isSuccess()) {
                            deleteWatchListCallBack.deleteWatchlistDetail(true, "", "");
                        } else {
                            if (result.error != null) {
                                String errorCode = result.error.getCode();
                                // PrintLogging.printLog("","errorCodess-->>"+errorCode);
                                if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                                    new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                        @Override
                                        public void response(CommonResponse response) {
                                            if (response.getStatus()) {
                                                deleteFromWatchlist(idfromAssetWatchlist, callBack);
                                            } else {
                                                deleteWatchListCallBack.deleteWatchlistDetail(false, "", activity.getResources().getString(R.string.something_went_wrong));
                                            }
                                        }
                                    });

                                } else {
                                    deleteWatchListCallBack.deleteWatchlistDetail(false, result.error.getCode(), result.error.getMessage());
                                }
                            } else {
                                deleteWatchListCallBack.deleteWatchlistDetail(false, "", activity.getResources().getString(R.string.something_went_wrong));
                            }
                            //channelCallBack.response(false, commonResponse);
                        }
                    }
                });
                getRequestQueue().queue(deletePersonalListBuilder.build(client));
            }

        };
        new Thread(runnable).start();

    }

    public void addToWatchlist(int playlistidtype, String id, String titleName, AddWatchListCallBack callBack) {
        addwatchlistCallBack = callBack;
        final PersonalList personalList = new PersonalList();
        personalList.setName(titleName);
        personalList.setKsql(id);
        personalList.setPartnerListType(Integer.valueOf(AppConstants.WATCHLIST_PARTNER_TYPE));

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
        clientSetupKs();
        PersonalListService.AddPersonalListBuilder addPersonalListBuilder = PersonalListService.add(personalList).setCompletion(new OnCompletion<Response<PersonalList>>() {
            @Override
            public void onComplete(Response<PersonalList> result) {
                PrintLogging.printLog(this.getClass(), "", "returnedValues" + result.isSuccess());
                if (result.isSuccess()) {

                    PrintLogging.printLog(this.getClass(), "", "returnedValues" + result.results.getId());
                    addwatchlistCallBack.getWatchlistDetail(result.results.getId().toString(), "", "");
//
                } else {
                    //PrintLogging.printLog(this.getClass(),"", "returnedValues" + result.results.getId());
                    if (result.error != null) {
                        addwatchlistCallBack.getWatchlistDetail("", result.error.getCode(), result.error.getMessage());
                    } else {
                        addwatchlistCallBack.getWatchlistDetail("", "", activity.getResources().getString(R.string.something_went_wrong));
                    }
                }


            }
        });
        getRequestQueue().queue(addPersonalListBuilder.build(client));
//            }

//        };
//        new Thread(runnable).start();
    }

    public void addHouseHoldDevice(SharedPrefHelper session, final KsHouseHoldDeviceAddCallBack callBack) {
        loginClient(KsPreferenceKey.getInstance(activity).getStartSessionKs());
        PrintLogging.printLog(this.getClass(), "", "kalturaLoginUserName" + KsPreferenceKey.getInstance(activity).getStartSessionKs());
        this.ksHouseHoldDeviceAddCallBack = callBack;
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
//                 String udid = AppCommonMethods.callpreference().getUdid();
                String udid = UDID.getDeviceId(activity, activity.getContentResolver());
                PrintLogging.printLog(this.getClass(), "", "werrewrewrwer" + udid);
                // String udid = KsPreferenceKey.getInstances(activity).getFakeUDID();
                String _houseHoldId = KsPreferenceKey.getInstance(activity).getHouseHoldId();
                HouseholdDevice householdDevice = new HouseholdDevice();
                householdDevice.setUdid(udid);
                householdDevice.setName(AppCommonMethods.getDeviceName(activity));
//                householdDevice.setHouseholdId(Integer.parseInt(_houseHoldId));
                householdDevice.setBrandId(2);

                PrintLogging.printLog(this.getClass(), "", "valuessPtt" + _houseHoldId + "-->" + udid);

                HouseholdDeviceService.AddHouseholdDeviceBuilder builder = HouseholdDeviceService.add(householdDevice).setCompletion(result -> {
                    Log.w("houseHoldsdd", result.isSuccess() + "-->>");
                    if (result.isSuccess()) {


                        Log.w("houseHolder", result.isSuccess() + "");
                        ksHouseHoldDeviceAddCallBack.success(true, result);
                    } else {
                        if (result.error != null) {
                            ksHouseHoldDeviceAddCallBack.failure(false, result.error.getCode(), result.error.getMessage(), result);
                        } else {
                            ksHouseHoldDeviceAddCallBack.failure(false, "", activity.getResources().getString(R.string.something_went_wrong), result);
                        }
                    }

                });
                getRequestQueue().queue(builder.build(client));
            }

        };
        new Thread(runnable).start();

    }

    public void callHouseHoldList(final SharedPrefHelper session, final KsHouseHoldDevice ksHouseHoldDevice) {
        loginClient(KsPreferenceKey.getInstance(activity).getStartSessionKs());
//        Long l = Long.valueOf(1);
        int _houseHoldId = 0;
        if (!TextUtils.isEmpty(KsPreferenceKey.getInstance(activity).getHouseHoldId()))
            _houseHoldId = Integer.parseInt(KsPreferenceKey.getInstance(activity).getHouseHoldId());
        HouseholdDeviceFilter householdDeviceFilter = new HouseholdDeviceFilter();
        householdDeviceFilter.setHouseholdIdEqual(_houseHoldId);
        householdDeviceFilter.setDeviceFamilyIdIn("1");
        HouseholdDeviceService.ListHouseholdDeviceBuilder builder = HouseholdDeviceService.list()
                .setCompletion(result -> {
                    if (result != null) {
                        if (result.isSuccess()) {
                            ksHouseHoldDevice.success(true, result);
                        } else {
                            ksHouseHoldDevice.failure(false, result);
                        }
                    } else {
                        ksHouseHoldDevice.failure(false, null);
                    }
                });
        getRequestQueue().queue(builder.build(client));
    }

    public void callLicencedURL(final SharedPrefHelper session) {
        Runnable runnable = () -> {
            String ksValue = session.getString("session_ks", "");
            client.setKs(ksValue);
            LicensedUrlBaseRequest licensedUrlBaseRequest = new LicensedUrlBaseRequest();
            licensedUrlBaseRequest.setAssetId("314815");
            LicensedUrlService.GetLicensedUrlBuilder builder = LicensedUrlService.get(licensedUrlBaseRequest).setCompletion(result -> Log.w("valueeeefromUrl", result.error.getCode() + ""));

            getRequestQueue().queue(builder.build(client));
        };
        new Thread(runnable).start();
    }

    public void kalturaAddToken(final String session_ks) {
        try {
            int expiryDate = getTokenExpiryDate();
            loginClient(session_ks);
            AppToken token = new AppToken();
            token.expiry(String.valueOf(expiryDate)); // session expiry ks valid for 7 days

            token.sessionDuration("604800");
            token.hashType("SHA256");
            AppTokenService.AddAppTokenBuilder builder = AppTokenService.add(token)
                    .setCompletion(result -> {
                        if (result.isSuccess()) {
                            KsPreferenceKey.getInstance(activity).setTokenId(result.results.getId() + "");
                            KsPreferenceKey.getInstance(activity).setToken(result.results.getToken() + "");
                        } else {
                        }
                    });
            getRequestQueue().queue(builder.build(client));

        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "", "Exception" + e.getMessage());
        }
    }

    public void addToken(final String session_ks, final KsAppTokenCallBack ksAppTokenCallBack) {
        try {
            int expiryDate = getTokenExpiryDate();
            loginClient(session_ks);
            AppToken token = new AppToken();
            token.expiry(String.valueOf(expiryDate)); // session expiry ks valid for 7 days

            token.sessionDuration("604800");
            token.hashType("SHA256");
            AppTokenService.AddAppTokenBuilder builder = AppTokenService.add(token)
                    .setCompletion(result -> {
                        if (result.isSuccess()) {
                            ksAppTokenCallBack.success(true, result);
                        } else {
                            ksAppTokenCallBack.failure(false, result);
                        }
                    });
            getRequestQueue().queue(builder.build(client));

        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "", "Exception" + e.getMessage());
        }
    }

    public void callGetHouseHold(CommonCallBack callBack) {
        this.ksHouseHoldIdCallBack = callBack;
        String ksValue = KsPreferenceKey.getInstance(activity).getStartSessionKs();
        loginClient(ksValue);

        CommonResponse commonResponse = new CommonResponse();

        HouseholdService.GetHouseholdBuilder builder = HouseholdService.get().setCompletion(result -> {
            Gson gson = new Gson();
            if (result.isEmpty()) {
                commonResponse.setStatus(false);
                if (result.error != null) {
                    commonResponse.setErrorCode(result.error.getCode());
                    commonResponse.setMessage(result.error.getMessage());
                }
                ksHouseHoldIdCallBack.response(false, commonResponse);
            } else if (result.isSuccess()) {
                houseHold_limitation_id = result.results.getHouseholdLimitationsId();
                long id = result.results.getId();
                KsPreferenceKey.getInstance(activity).setHouseHoldId(id + "");
                getDeviceLimit(houseHold_limitation_id, commonResponse);
            } else {
                Log.e("RESPONSE===>", gson.toJson(result));
                commonResponse.setStatus(false);
                if (result.error != null) {
                    commonResponse.setErrorCode(result.error.getCode());
                    commonResponse.setMessage(result.error.getMessage());
                }
                ksHouseHoldIdCallBack.response(false, commonResponse);
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void notificationPushToken(String fcmToken, final PushTokenCallBack pushTokenCallBack) {

        clientSetupKs();

        NotificationService.SetDevicePushTokenNotificationBuilder builder = NotificationService.setDevicePushToken(fcmToken).setCompletion(result -> {

            try {

                if (result.isSuccess()) {

                    pushTokenCallBack.result(true, result);
                } else {

                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        notificationPushToken(fcmToken, pushTokenCallBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        pushTokenCallBack.result(false, result);
                                    }
                                }
                            });
                        else {
                            pushTokenCallBack.result(false, result);
                        }
                    } else {
                        pushTokenCallBack.result(false, result);
                    }
                }

            } catch (Exception e) {
                pushTokenCallBack.result(false, result);
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    private void getDeviceLimit(int houseHold_limitation_id, CommonResponse commonResponse) {
        HouseholdLimitationsService.GetHouseholdLimitationsBuilder builder = HouseholdLimitationsService.get(houseHold_limitation_id).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results != null) {
                    int deviceFamilyLimitationSize = result.results.getDeviceFamiliesLimitations().size();
                    if (deviceFamilyLimitationSize > 0) {
                        for (int i = 0; i < result.results.getDeviceFamiliesLimitations().size(); i++) {
                            if (result.results.getDeviceFamiliesLimitations().get(i).getName().equalsIgnoreCase(AppLevelConstants.SMART_PHONE)) {
                                deviceLimit = result.results.getDeviceFamiliesLimitations().get(i).getDeviceLimit();
                                KsPreferenceKey.getInstance(activity).setHouseHoldDeviceLimit(deviceLimit + "");
                                break;
                            }

                        }


                    }
                }

                if (deviceLimit > 0) {

                    commonResponse.setStatus(true);

                    ksHouseHoldIdCallBack.response(true, commonResponse);
                } else {

                    commonResponse.setStatus(false);

                    if (result.error != null) {
                        commonResponse.setMessage(result.error.getMessage());
                        ksHouseHoldIdCallBack.response(false, commonResponse);
                    }

                }


            } else {

                commonResponse.setStatus(false);

                if (result.error != null) {
                    commonResponse.setMessage(result.error.getMessage());
                    ksHouseHoldIdCallBack.response(false, commonResponse);
                }
            }
        });
        getRequestQueue().queue(builder.build(client));


    }

    public void login(String userName, String password, KsLoginCallBack callBack) {
        this.ksLoginCallBack = callBack;

        StringBuilderHolder.getInstance().clear();
        StringBuilderHolder.getInstance().append(AppLevelConstants.MOBILE);
        StringBuilderHolder.getInstance().append("_");
        StringBuilderHolder.getInstance().append(userName);
        StringBuilderHolder.getInstance().append("_");

        String android_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        StringBuilderHolder.getInstance().append(android_id);

        String udid = StringBuilderHolder.getInstance().getText().toString();


        Map<String, StringValue> params = new Hashtable<>();
        StringValue stringValue = new StringValue();
        stringValue.setValue("");
        params.put("", stringValue);

        OttUserService.LoginOttUserBuilder builder = OttUserService.login(AppLevelConstants.PARTNER_ID, userName, password, params, udid)
                .setCompletion(new OnCompletion<Response<LoginResponse>>() {
                    @Override
                    public void onComplete(final Response<LoginResponse> result) {
                        if (result.isEmpty()) {
                            ksLoginCallBack.failure(false, activity.getResources().getString(R.string.something_went_wrong), result);
                        } else if (result.isSuccess()) {
                            ksLoginCallBack.success(true, "", result);
                        } else {
                            if (result.error != null) {
                                ksLoginCallBack.failure(false, result.error.getMessage(), result);
                            } else {
                                ksLoginCallBack.failure(false, activity.getResources().getString(R.string.something_went_wrong), result);
                            }

                        }
                    }
                });
        getRequestQueue().queue(builder.build(client));
    }

    public void compareWatchlist(int counter, WatchlistCallBack callBack) {
        watchlistCallBack = callBack;

        final PersonalListFilter personalListFilter = new PersonalListFilter();
        personalListFilter.partnerListTypeIn(AppConstants.WATCHLIST_PARTNER_TYPE);
        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(40);

        Runnable runnable = () -> {
            clientSetupKs();
            PersonalListService.ListPersonalListBuilder listBuilder = PersonalListService.list(personalListFilter, filterPager).setCompletion(result -> {
                PrintLogging.printLog("", "watchlisValues" + result.isSuccess());
                if (result.isSuccess()) {
                    if (result.results.getTotalCount() > 0) {
                        PrintLogging.printLog("", "watchlisValues" + result.results.getTotalCount());
                        watchlistCallBack.getWatchlistDetail(true, "", result);
                    } else {
                        PrintLogging.printLog("", "watchlisValues" + result.results.getTotalCount());
                        watchlistCallBack.getWatchlistDetail(false, "", result);
                    }
                } else {
                    watchlistCallBack.getWatchlistDetail(false, "", null);

                    /*ErrorHandling.checkErrorType(result.error, (code, status) -> {
                        if (code.equalsIgnoreCase(AppConstants.KS_EXPIRE) && status) {
                            compareWatchlist(counter, watchlistCallBack);
                        } else {
                            watchlistCallBack.getWatchlistDetail(false, result.error.getCode(), result);
                        }
                    });*/
                }
            });

            getRequestQueue().queue(listBuilder.build(client));
        };
        new Thread(runnable).start();
    }


    public void compareWatchlist(WatchlistCallBack callBack) {
        watchlistCallBack = callBack;

        final PersonalListFilter personalListFilter = new PersonalListFilter();
        personalListFilter.partnerListTypeIn(AppConstants.WATCHLIST_PARTNER_TYPE);


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                clientSetupKs();
                PersonalListService.ListPersonalListBuilder listBuilder = PersonalListService.list(personalListFilter).setCompletion(new OnCompletion<Response<ListResponse<PersonalList>>>() {
                    @Override
                    public void onComplete(Response<ListResponse<PersonalList>> result) {
                        PrintLogging.printLog(this.getClass(), "", "watchlisValues" + result.isSuccess());
                        if (result.isSuccess()) {
                            if (result.results.getTotalCount() > 0) {
                                PrintLogging.printLog(this.getClass(), "", "watchlisValues" + result.results.getTotalCount());
                                watchlistCallBack.getWatchlistDetail(true, "", result);
                            } else {
                                PrintLogging.printLog(this.getClass(), "", "watchlisValues" + result.results.getTotalCount());
                                watchlistCallBack.getWatchlistDetail(false, "", result);
                            }
                        }
//                        else {
//                            if (result.error != null) {
//                                watchlistCallBack.getWatchlistDetail(false, result.error.getCode(), result);
//                            } else {
//                                watchlistCallBack.getWatchlistDetail(false, "", result);
//                            }
//
//                        }
                        else {
                            if (result.error != null) {
                                String errorCode = result.error.getCode();
                                // PrintLogging.printLog("","errorCodess-->>"+errorCode);
                                Log.e("errorCodessMywatchList", errorCode);
                                if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                                    new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                        @Override
                                        public void response(CommonResponse response) {
                                            if (response.getStatus()) {
                                                compareWatchlist(callBack);
                                            } else {
                                                watchlistCallBack.getWatchlistDetail(false, "", result);
                                            }
                                        }
                                    });

                                } else {
                                    watchlistCallBack.getWatchlistDetail(false, result.error.getCode(), result);
                                }
                            } else {
                                watchlistCallBack.getWatchlistDetail(false, "", result);
                            }
                            //channelCallBack.response(false, commonResponse);
                        }


                    }
                });

                getRequestQueue().queue(listBuilder.build(client));
            }

        };
        new Thread(runnable).start();
    }

    public void compareWatchlist(String partner, int counter, WatchlistCallBack callBack) {
        watchlistCallBack = callBack;

        final PersonalListFilter personalListFilter = new PersonalListFilter();
        personalListFilter.partnerListTypeIn(partner);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(20);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                clientSetupKs();
                PersonalListService.ListPersonalListBuilder listBuilder = PersonalListService.list(personalListFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<PersonalList>>>() {
                    @Override
                    public void onComplete(Response<ListResponse<PersonalList>> result) {
                        PrintLogging.printLog(this.getClass(), "", "watchlisValues" + result.isSuccess());
                        if (result.isSuccess()) {
                            if (result.results.getTotalCount() > 0) {
                                PrintLogging.printLog(this.getClass(), "", "watchlisValues" + result.results.getTotalCount());
                                watchlistCallBack.getWatchlistDetail(true, "", result);
                            } else {
                                PrintLogging.printLog(this.getClass(), "", "watchlisValues" + result.results.getTotalCount());
                                watchlistCallBack.getWatchlistDetail(false, "", result);
                            }
                        }
//                        else {
//                            if (result.error != null) {
//                                watchlistCallBack.getWatchlistDetail(false, result.error.getCode(), result);
//                            } else {
//                                watchlistCallBack.getWatchlistDetail(false, "", result);
//                            }
//
//                        }

                        else {
                            if (result.error != null) {
                                String errorCode = result.error.getCode();
                                // PrintLogging.printLog("","errorCodess-->>"+errorCode);
                                Log.e("errorCodessMyPlayList", errorCode);
                                if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                                    new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                        @Override
                                        public void response(CommonResponse response) {
                                            if (response.getStatus()) {
                                                compareWatchlist(partner, counter, callBack);
                                            } else {
                                                watchlistCallBack.getWatchlistDetail(false, "", result);
                                            }
                                        }
                                    });

                                } else {
                                    watchlistCallBack.getWatchlistDetail(false, "", result);
                                }
                            } else {
                                watchlistCallBack.getWatchlistDetail(false, "", result);
                            }
                            //channelCallBack.response(false, commonResponse);
                        }


                    }
                });

                getRequestQueue().queue(listBuilder.build(client));
            }

        };
        new Thread(runnable).start();
    }

    public void getAssetFromTrailor(String ref_id, TrailorToAssetCallBack callBack) {
        toAssetCallBack = callBack;
        clientSetupKs();
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();

        // "kSql" : "(or Ref Id ~ 'DHORIZONSPECTRE')"
        String one = "(or Ref Id = '";
        String two = "')";
        String kSQL = one + ref_id + two;
        PrintLogging.printLog(this.getClass(), "", "trailorKSQL" + kSQL);
        searchAssetFilter.setKSql(kSQL);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(1);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                PrintLogging.printLog(this.getClass(), "", "trailorValues" + result.isSuccess());
                if (result.isSuccess()) {
                    if (result.results.getTotalCount() > 0) {
                        PrintLogging.printLog(this.getClass(), "", "trailorValues" + result.results.getTotalCount());
                        toAssetCallBack.getAsset(result.results.getObjects().get(0));
                        // trailorCallBack.getTrailorURL(true,AssetContent.getTrailorUrl(result.results.getObjects().get(0)));
                    } else {
                        toAssetCallBack.getAsset(null);
                    }
                } else {
                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getAssetFromTrailor(ref_id, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        toAssetCallBack.getAsset(null);
                                    }
                                }
                            });
                        else {
                            toAssetCallBack.getAsset(null);
                        }
                    } else {
                        toAssetCallBack.getAsset(null);
                    }
                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void movieAssetListing(String genreSkl, final int assetType, final int id, int counter, HomechannelCallBack callBack) {
        clientSetupKs();
        homechannelCallBack = callBack;
        listAssetBuilders = new ArrayList<>();
        movieList = new ArrayList<>();

        RelatedFilter relatedFilter = new RelatedFilter();
        String one = "(and asset_type='";
        String two = "' (or " + genreSkl + "))";
        String third = String.valueOf(assetType);
        relatedFilter.setKSql(one + third + two);
        relatedFilter.setIdEqual(id);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {

            if (result.isSuccess()) {
                movieList.add(result);
                similarAssetListing(assetType, id, movieList);
            } else {
                similarAssetListing(assetType, id, movieList);
            }
        });
        listAssetBuilders.add(builder);

        // count=listAssetBuilders.size();

       /* MultiRequestBuilder multiRequestBuilder=new MultiRequestBuilder();
        for (int j=0;j<listAssetBuilders.size();j++){
            multiRequestBuilder = multiRequestBuilder.add(listAssetBuilders.get(j));
        }*/
        getRequestQueue().queue(listAssetBuilders.get(0).build(client));

    }

    private void similarAssetListing(int assetType, int id, final List<Response<ListResponse<Asset>>> movieList) {
        clientSetupKs();
        listAssetBuilders = new ArrayList<>();
        RelatedFilter relatedFilter = new RelatedFilter();
        relatedFilter.setKSql("(and asset_type='" + String.valueOf(assetType) + "')");
        relatedFilter.setIdEqual(id);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(50);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {
            Log.w("similarListing", result + "");
            if (result.isSuccess()) {
                movieList.add(result);
                homechannelCallBack.response(true, movieList, channelList);
            } else {
                homechannelCallBack.response(false, movieList, channelList);

            }
        });
        listAssetBuilders.add(builder);
        getRequestQueue().queue(listAssetBuilders.get(0).build(client));

    }

    public void callStartSession(SharedPrefHelper session, KsStartSessionCallBack callBack) {
        try {
            this.ksStartSessionCallBack = callBack;
            String token = KsPreferenceKey.getInstance(activity).getToken();
            String ksValue = KsPreferenceKey.getInstance(activity).getAnonymousks();
            final String idValue = KsPreferenceKey.getInstance(activity).getTokenId();
            getFbKeyHash(activity, ksValue + token);
            clientSetupKs();
            String udid = AppCommonMethods.callpreference(activity).getUdid();
            int expiryDate = getTokenExpiryDate();
            AppTokenService.StartSessionAppTokenBuilder builder = AppTokenService
                    .startSession(idValue, keyHash, "", expiryDate, udid).setCompletion(result -> {
                        if (result.isSuccess()) {
                            ksStartSessionCallBack.success(true, result);
                        } else {
                            // ksStartSessionCallBack.failure(false, result);
                            callLogoutApi();
                            UserInfo.getInstance(activity).setActive(false);
                            KsPreferenceKey.getInstance(activity).setUser(null);
                            KsPreferenceKey.getInstance(activity).setStartSessionKs("");
                            KsPreferenceKey.getInstance(activity).setMsisdn("");
                            Intent intent = new Intent(activity, HomeActivity.class);
                            TaskStackBuilder.create(activity).addNextIntentWithParentStack(intent).startActivities();

                        }
                    });
            getRequestQueue().queue(builder.build(client));
        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "", "Exception" + e.getMessage());
        }

    }

    private void getFbKeyHash(Context mainActivity, String s) {
        MessageDigest digest;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes());

            keyHash = bytesToHexString(digest.digest());

            Log.i("Eamorr", "result is " + keyHash);
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void checkHouseholdDevice(HouseHoldDevice callBack) {
        clientSetupKs();
        PrintLogging.printLog(this.getClass(), "", "ErrorCodeIs" + KsPreferenceKey.getInstance(activity).getStartSessionKs());

        final CommonResponse commonResponse = new CommonResponse();

        houseHoldDevice2 = callBack;
        HouseholdDeviceService.GetHouseholdDeviceBuilder builder = HouseholdDeviceService.get().setCompletion(new OnCompletion<Response<HouseholdDevice>>() {
            @Override
            public void onComplete(Response<HouseholdDevice> result) {
                if (result.isSuccess()) {
                    commonResponse.setStatus(true);
                    houseHoldDevice2.response(commonResponse);
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setMessage(result.error.getMessage());
                    commonResponse.setErrorCode(result.error.getCode());

                    houseHoldDevice2.response(commonResponse);

                    PrintLogging.printLog(this.getClass(), "", "ErrorCodeIs" + result.error.getCode());
                    PrintLogging.printLog(this.getClass(), "", "ErrorMessageIs" + result.error.getMessage());
                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void assetRuleApi(Long assetId, AssetRuleCallback callback) {
        clientSetupKs();
        assetRuleCallback = callback;

        UserAssetRuleFilter userAssetRuleFilter = new UserAssetRuleFilter();
        userAssetRuleFilter.setAssetIdEqual(assetId);
        userAssetRuleFilter.setAssetTypeEqual(Integer.valueOf(AppLevelConstants.WATCHLIST_PARTNER_TYPE));

        UserAssetRuleService.ListUserAssetRuleBuilder ruleBuilder = UserAssetRuleService.list(userAssetRuleFilter).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            assetRuleCallback.getAssetrule(true, result, 0, "", "");
                        } else {
                            assetRuleCallback.getAssetrule(false, result, 0, "", activity.getResources().getString(R.string.something_went_wrong));
                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        // PrintLogging.printLog("","errorCodess-->>"+errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        assetRuleApi(assetId, callback);
                                    } else {
                                        assetRuleCallback.getAssetrule(false, result, 0, "", activity.getResources().getString(R.string.something_went_wrong));
                                    }
                                }
                            });

                        } else {
                            assetRuleCallback.getAssetrule(false, result, 0, result.error.getCode(), result.error.getMessage());
                        }
                    } else {
                        assetRuleCallback.getAssetrule(false, result, 0, "", activity.getResources().getString(R.string.something_went_wrong));
                    }
                    //channelCallBack.response(false, commonResponse);
                }
            } else {
                assetRuleCallback.getAssetrule(false, null, 0, "", activity.getResources().getString(R.string.something_went_wrong));
            }
        });
        getRequestQueue().queue(ruleBuilder.build(client));
    }

    public void getAssetPurchaseStatus(final String fileId, ProductPriceCallBack callBack) {
        clientSetupKs();

        productPriceCallBack = callBack;

        ProductPriceFilter productPriceFilter = new ProductPriceFilter();
        productPriceFilter.setFileIdIn(fileId);

        ProductPriceService.ListProductPriceBuilder builder = ProductPriceService.list(productPriceFilter).setCompletion(result -> {

            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null && result.results.getObjects().get(0).getPurchaseStatus() != null) {
                            productPriceCallBack.getProductprice(true, result, result.results.getObjects().get(0).getPurchaseStatus().toString(), "", "");
                        } else {
                            productPriceCallBack.getProductprice(false, result, "", "", activity.getResources().getString(R.string.something_went_wrong));
                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getAssetPurchaseStatus(fileId, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        productPriceCallBack.getProductprice(false, result, "", "", activity.getResources().getString(R.string.something_went_wrong));
                                    }
                                }
                            });
                        } else {
                            productPriceCallBack.getProductprice(false, result, "", result.error.getCode(), result.error.getMessage());
                        }
                    } else {
                        productPriceCallBack.getProductprice(false, result, "", "", activity.getResources().getString(R.string.something_went_wrong));
                    }


                }
            } else {
                productPriceCallBack.getProductprice(false, null, "", "", activity.getResources().getString(R.string.something_went_wrong));
            }

        });

        getRequestQueue().queue(builder.build(client));


    }

    public void getLiveEventPurchaseStatus(final String fileId, ProductPriceCallBack callBack) {
        clientSetupKs();

        productPriceCallBack = callBack;

        ProductPriceFilter productPriceFilter = new ProductPriceFilter();
        productPriceFilter.setSubscriptionIdIn(fileId);

        ProductPriceService.ListProductPriceBuilder builder = ProductPriceService.list(productPriceFilter).setCompletion(result -> {

            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null && result.results.getObjects().get(0).getPurchaseStatus() != null) {
                            productPriceCallBack.getProductprice(true, result, result.results.getObjects().get(0).getPurchaseStatus().toString(), "", "");
                        } else {
                            productPriceCallBack.getProductprice(false, result, "", "", activity.getResources().getString(R.string.something_went_wrong));
                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getAssetPurchaseStatus(fileId, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        productPriceCallBack.getProductprice(false, result, "", "", activity.getResources().getString(R.string.something_went_wrong));
                                    }
                                }
                            });
                        } else {
                            productPriceCallBack.getProductprice(false, result, "", result.error.getCode(), result.error.getMessage());
                        }
                    } else {
                        productPriceCallBack.getProductprice(false, result, "", "", activity.getResources().getString(R.string.something_went_wrong));
                    }


                }
            } else {
                productPriceCallBack.getProductprice(false, null, "", "", activity.getResources().getString(R.string.something_went_wrong));
            }

        });

        getRequestQueue().queue(builder.build(client));


    }


    public void getAssetListPurchaseStatus(final String fileId, ProductPriceCallBack callBack) {
        clientSetupKs();

        productPriceCallBack = callBack;

        ProductPriceFilter productPriceFilter = new ProductPriceFilter();
        productPriceFilter.setFileIdIn(fileId);

        ProductPriceService.ListProductPriceBuilder builder = ProductPriceService.list(productPriceFilter).setCompletion(result -> {

            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            productPriceCallBack.getProductprice(true, result, "", "", "");
                        } else {
                            productPriceCallBack.getProductprice(false, result, "", "", activity.getResources().getString(R.string.something_went_wrong));
                        }
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getAssetPurchaseStatus(fileId, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        productPriceCallBack.getProductprice(false, result, "", "", activity.getResources().getString(R.string.something_went_wrong));
                                    }
                                }
                            });
                        } else {
                            productPriceCallBack.getProductprice(false, result, "", result.error.getCode(), result.error.getMessage());
                        }
                    } else {
                        productPriceCallBack.getProductprice(false, result, "", "", activity.getResources().getString(R.string.something_went_wrong));
                    }


                }
            } else {
                productPriceCallBack.getProductprice(false, null, "", "", activity.getResources().getString(R.string.something_went_wrong));
            }

        });

        getRequestQueue().queue(builder.build(client));


    }

    public void showAllKeyword(Context context
            , final String mediaType, final String keyToSearch, int counter, SearchResultCallBack CallBack) {
        searchResultCallBack = CallBack;
        searchOutputModel = new ArrayList<>();
        listAssetBuilders = new ArrayList<>();

        final FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(15);

        // PrintLogging.printLog(this.getClass(),"", "counter value  :" + counter);
        final SearchAssetFilter assetFilter = new SearchAssetFilter();
        String modifyString = AppCommonMethods.getSearchFieldsKsql(keyToSearch, "", 1, context);
        /*String tag3 = "name ~ '";
        String tag2 = "'";

        final String modifyString =
                tag3 +
                        keyToSearch +
                        tag2;*/
        assetFilter.setKSql(KsPreferenceKey.getInstance(context).getSearchKSQL());
        if (mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getMovie(context)))) {
            assetFilter.setTypeIn(String.valueOf(MediaTypeConstant.getMovie(context)) + "," + String.valueOf(MediaTypeConstant.getCollection(context)));
        } else if (mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getSeries(context))) || mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getEpisode(context)))) {
            assetFilter.setTypeIn(String.valueOf(MediaTypeConstant.getSeries(context)) + "," + String.valueOf(MediaTypeConstant.getEpisode(context)));
        } else if (mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getLinear(context))) || mediaType.equalsIgnoreCase(String.valueOf(MediaTypeConstant.getProgram(context)))) {
            assetFilter.setTypeIn(String.valueOf(MediaTypeConstant.getLinear(context)) + "," + String.valueOf(MediaTypeConstant.getProgram(context)));
        } else {
            assetFilter.setTypeIn(String.valueOf(MediaTypeConstant.getCollection(context)));
        }

        if (!KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase("")) {
            if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.AZ.name())) {
                assetFilter.orderBy(SortByEnum.NAME_ASC.name());
            } else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.POPULAR.name())) {
                assetFilter.orderBy(SortByEnum.VIEWS_DESC.name());
            } else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.NEWEST.name())) {
                assetFilter.orderBy(SortByEnum.CREATE_DATE_DESC.name());
            } else {
                assetFilter.orderBy(SortByEnum.RELEVANCY_DESC.name());
            }

        } else {
            assetFilter.orderBy(SortByEnum.RELEVANCY_DESC.name());
        }

        Runnable runnable = () -> {

            clientSetupKs();

            AssetService.ListAssetBuilder assetService = AssetService.list(assetFilter, filterPager).setCompletion(result -> {

                if (result.isSuccess()) {
                    if (result.results.getTotalCount() > 0) {
                        if (result.results.getObjects() != null && result.results.getObjects().size() > 0) {
                            SearchModel temp = new SearchModel();
                            temp.setTotalCount(result.results.getTotalCount());
                            temp.setType(result.results.getObjects().get(0).getType());
                            // temp.setAllItemsInSection(result.results.getObjects());

                            List<Asset> assetss = AppCommonMethods.applyFreePaidFilter(result.results, context);
                            temp.setAllItemsInSection(assetss);
                            //  temp.setTotalCount(assetss.size());

                            temp.setSearchString(keyToSearch);
                            searchOutputModel.add(temp);
                            searchResultCallBack.response(true, searchOutputModel, "resultFound");
                        } else {
                            searchResultCallBack.response(true, searchOutputModel, "noResultFound");

                        }
                    } else
                        searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        // PrintLogging.printLog("","errorCodess-->>"+errorCode);
                        Log.e("ksExpireMoreResultAll", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        showAllKeyword(context
                                                , mediaType, keyToSearch, counter, CallBack);
                                    } else {
                                        searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                                    }
                                }
                            });

                        } else {
                            searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                        }
                    } else {
                        searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                    }
                    //channelCallBack.response(false, commonResponse);
                }

            });

            getRequestQueue().queue(assetService.build(client));
        };
        new Thread(runnable).start();
    }

    public void callSpecificAsset(String assetId, SpecificAssetCallBack callBack) {
        specificAssetCallBack = callBack;
        clientSetupKs();
        AssetService.GetAssetBuilder builder = AssetService.get(assetId, AssetReferenceType.MEDIA).setCompletion(result -> {
            PrintLogging.printLog("", "SpecificAsset" + result.isSuccess());
            if (result.isSuccess()) {
                if (result.results != null) {
                    specificAssetCallBack.getAsset(true, result.results);
                } else {
                    specificAssetCallBack.getAsset(false, null);
                }
            }

//            else {
//                specificAssetCallBack.getAsset(false, result.results);
//            }

            else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    // PrintLogging.printLog("","errorCodess-->>"+errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    callSpecificAsset(assetId, callBack);
                                } else {
                                    specificAssetCallBack.getAsset(false, null);
                                }
                            }
                        });

                    } else {
                        specificAssetCallBack.getAsset(false, null);
                    }
                } else {
                    specificAssetCallBack.getAsset(false, null);
                }
                //channelCallBack.response(false, commonResponse);
            }

        });
        getRequestQueue().queue(builder.build(client));
    }


    public void getLinearAssetId(String assetId, List<VIUChannel> list, int counter) {
        clientSetupKs();
        AssetService.GetAssetBuilder builder = AssetService.get(assetId, AssetReferenceType.MEDIA).setCompletion(result -> {
            PrintLogging.printLog("", "SpecificAsset" + result.isSuccess());
            if (result.isSuccess()) {
                if (result.results != null) {
                    try {
                        Asset asset = result.results;
                        MediaAsset mediaAsset = (MediaAsset) asset;
                        String externalId = mediaAsset.getExternalIds();
                        int index = Integer.parseInt(list.get(counter).getCustomDays());
                        String endDate = getNextDateTimeStamp(index + 1);
                        callLiveEPGDRail(externalId, "0", endDate, list);
                    } catch (Exception e) {
                        homechannelCallBack.response(false, null, null);
                    }

                } else {
                    homechannelCallBack.response(false, null, null);
                }
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    // PrintLogging.printLog("","errorCodess-->>"+errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getLinearAssetId(assetId, list, counter);
                                } else {
                                    homechannelCallBack.response(false, null, null);
                                }
                            }
                        });

                    } else {
                        homechannelCallBack.response(false, null, null);
                    }
                } else {
                    homechannelCallBack.response(false, null, null);
                }
            }

        });
        getRequestQueue().queue(builder.build(client));
    }

    public void getLinearAssetIdListing(String assetId, String customDays, int counter, TrendingCallBack trendingCallBack) {
        clientSetupKs();
        AssetService.GetAssetBuilder builder = AssetService.get(assetId, AssetReferenceType.MEDIA).setCompletion(result -> {
            PrintLogging.printLog("", "SpecificAsset" + result.isSuccess());
            if (result.isSuccess()) {
                if (result.results != null) {
                    try {
                        Asset asset = result.results;
                        MediaAsset mediaAsset = (MediaAsset) asset;
                        String externalId = mediaAsset.getExternalIds();
                        int index = Integer.parseInt(customDays);
                        String endDate = getNextDateTimeStamp(index + 1);
                        callLiveEPGDRailListing(externalId, "0", endDate, counter, trendingCallBack);
                    } catch (Exception e) {
                        trendingCallBack.getList(false, null, 0);

                    }

                } else {
                    trendingCallBack.getList(false, null, 0);

                }
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    // PrintLogging.printLog("","errorCodess-->>"+errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getLinearAssetIdListing(assetId, customDays, counter, trendingCallBack);
                                } else {
                                    trendingCallBack.getList(false, null, 0);

                                }
                            }
                        });

                    } else {
                        trendingCallBack.getList(false, null, 0);

                    }
                } else {
                    trendingCallBack.getList(false, null, 0);

                }
            }

        });
        getRequestQueue().queue(builder.build(client));
    }

    public static String getNextDateTimeStamp(int dateIndex) {
        String formattedDate;
        int nextDay = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, dateIndex);

        Date tomorrow = calendar.getTime();


        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        df.setTimeZone(TimeZone.getDefault());
        formattedDate = df.format(tomorrow);

        calendar.clear();
        return getTimeStamp(formattedDate);
    }

    private static String getTimeStamp(String todayDate) {
        /*Calendar currnetDateTime = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss a");
        String  currentTime = df.format(currnetDateTime.getTime());
        Log.w("currentTime-->>",currentTime);*/
        long timestamp = 0;
        String startTime = " 00:00:00 AM";
        String dateStr;

        dateStr = todayDate + startTime;

        DateFormat readFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aa", Locale.US);
        DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = null;
        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = "";
        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        if (date != null) {
            long output = date.getTime() / 1000L;
            String str = Long.toString(output);
            timestamp = Long.parseLong(str);
            PrintLogging.printLog("AppCommonMethods", "", "printDatedate" + formattedDate + "-->>" + timestamp);
            System.out.println(formattedDate);
        }


        return String.valueOf(timestamp);
    }

    public void searchKeyword(Context context, final String keyToSearch, final List<MediaTypeModel> model, int counter, SearchResultCallBack CallBack, String searchKeyword, String selectedGenre) {
        searchResultCallBack = CallBack;
        searchOutputModel = new ArrayList<>();
        currentMediaTypes = model;
        listAssetBuilders = new ArrayList<>();
        clientSetupKs();
        Runnable runnable = () -> setQuickSearchBuilder(context, keyToSearch, model, counter, CallBack, searchKeyword, selectedGenre);
        new Thread(runnable).start();
    }

    public void searchKeywordAuto(int autoCompleteCounter, Context context, final String keyToSearch, final List<MediaTypeModel> model, SearchResultCallBack CallBack, String searchKeyword, String selectedGenre) {
        searchResultCallBack = CallBack;
        searchOutputModel = new ArrayList<>();
        currentMediaTypes = model;
        searchString = keyToSearch;
        count = 0;
        listAssetBuilders = new ArrayList<>();
        clientSetupKs();
        PrintLogging.printLog(this.getClass(), "", "model.size()" + model.size());
        Runnable runnable = () -> setSearchBuilderAuto(context, keyToSearch, model, autoCompleteCounter, CallBack, searchKeyword, selectedGenre);
        new Thread(runnable).start();


    }

    private void setSearchBuilderAuto(Context context, final String keyToSearch, final List<MediaTypeModel> model, int count, SearchResultCallBack CallBack, String searchKeyword, String selectredGenre) {
        final FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        Log.e(String.valueOf(count) + "====>", currentMediaTypes.get(count).getId());

        final SearchAssetFilter assetFilter = new SearchAssetFilter();
        assetFilter.setKSql(keyToSearch);
        assetFilter.setTypeIn(currentMediaTypes.get(count).getId());

        AssetService.ListAssetBuilder assetService = AssetService.list(assetFilter, filterPager).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results.getTotalCount() > 0) {
                    if (result.results.getObjects() != null) {
                        if (result.results.getObjects().size() > 0) {
                            SearchModel temp = new SearchModel();
                            temp.setTotalCount(result.results.getTotalCount());
                            temp.setHeaderTitle(getNameFromType(result.results.getObjects().get(0).getType()));
                            temp.setType(result.results.getObjects().get(0).getType());
                            temp.setAllItemsInSection(result.results.getObjects());
                            temp.setSearchString(searchString);
                            if (!iscategoryAdded(result.results.getObjects().get(0).getType())) {
                                searchOutputModel.add(temp);
                            }
                            Log.e("SEARCH SIZE", String.valueOf(searchOutputModel.size()));
                            if (searchOutputModel.size() > 0) {
                                searchResultCallBack.response(true, searchOutputModel, "resultFound");
                            } else {
                                searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                            }
                        } else {
                            searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                        }

                    } else {
                        searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                    }

                } else {
                    searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                }
            } else {
                if ((result.error.getCode().equals(AppLevelConstants.KS_EXPIRE))) {
                    searchKeyword(context, keyToSearch, model, count, CallBack, searchKeyword, selectredGenre);
                }
            }
        });
        getRequestQueue().queue(assetService.build(client));
    }


    public void getCridDetail(Context context, final String cridId, SpecificAssetCallBack specificAssetCallBack) {
        clientSetupKs();
        final FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);
        final SearchAssetFilter assetFilter = new SearchAssetFilter();
        String KSql = "EventCRID = '" + cridId + "'";
        assetFilter.setKSql(KSql);
        assetFilter.setTypeIn(MediaTypeConstant.getLinear(context) + "");

        AssetService.ListAssetBuilder assetService = AssetService.list(assetFilter, filterPager).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results.getTotalCount() > 0) {
                    if (result.results.getObjects() != null) {
                        if (result.results.getObjects().size() > 0 && result.results.getObjects().get(0) != null) {
                            specificAssetCallBack.getAsset(false, result.results.getObjects().get(0));

                        } else {
                            specificAssetCallBack.getAsset(false, null);

                        }

                    } else {
                        specificAssetCallBack.getAsset(false, null);

                    }

                } else {
                    specificAssetCallBack.getAsset(false, null);

                }
            } else {
                specificAssetCallBack.getAsset(false, null);
            }
        });
        getRequestQueue().queue(assetService.build(client));
    }

    private void setQuickSearchBuilder(Context context, final String keyToSearch, final List<MediaTypeModel> model, int count, SearchResultCallBack CallBack, String searchKeyword, String selectedGenre) {
        final FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(15);

        Log.e(String.valueOf(count) + "====>", currentMediaTypes.get(count).getId());

        final SearchAssetFilter assetFilter = new SearchAssetFilter();
        assetFilter.name(searchKeyword);
        assetFilter.setKSql(keyToSearch);
        assetFilter.setTypeIn(currentMediaTypes.get(count).getId());
        if (!KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase("")) {
            if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.AZ.name())) {
                assetFilter.orderBy(SortByEnum.NAME_ASC.name());
            } else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.POPULAR.name())) {
                assetFilter.orderBy(SortByEnum.VIEWS_DESC.name());
            } else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.NEWEST.name())) {
                assetFilter.orderBy(SortByEnum.CREATE_DATE_DESC.name());
            } else {
                assetFilter.orderBy(SortByEnum.RELEVANCY_DESC.name());
            }

        } else {
            assetFilter.orderBy(SortByEnum.RELEVANCY_DESC.name());
        }

      /*  if (count==0){

            String str[]=currentMediaTypes.get(count).getId().split(",");
            if (str[0].equalsIgnoreCase(String.valueOf(MediaTypeConstant.getMovie(context)))){
                assetFilter.setTypeIn(String.valueOf(MediaTypeConstant.getMovie(context)));
            }else {
                assetFilter.setTypeIn(String.valueOf(MediaTypeConstant.getCollection(context)));
            }

        }else {
            assetFilter.setTypeIn(currentMediaTypes.get(count).getId());
        }*/


        AssetService.ListAssetBuilder assetService = AssetService.list(assetFilter, filterPager).setLanguage("may").setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results.getTotalCount() > 0) {
                    if (result.results.getObjects() != null) {
                        if (result.results.getObjects().size() > 0) {
                            SearchModel temp = new SearchModel();

                            temp.setHeaderTitle(getNameFromType(result.results.getObjects().get(0).getType()));
                            temp.setType(result.results.getObjects().get(0).getType());
                            List<Asset> assetss = AppCommonMethods.applyFreePaidFilter(result.results, context);
                            temp.setAllItemsInSection(assetss);
                            temp.setTotalCount(assetss.size());
                           /* if (result.results.getObjects().get(0).getType()==MediaTypeConstant.getMovie(context)
                            || result.results.getObjects().get(0).getType()==MediaTypeConstant.getCollection(context)){
                                List<Asset> assets=AppCommonMethods.removePagesFromCollection(result.results,context);
                                temp.setAllItemsInSection(assets);
                                temp.setTotalCount(assets.size());
                            }else {
                                //List<Asset> assets=AppCommonMethods.removePagesFromCollection(result.results);
                                temp.setTotalCount(result.results.getTotalCount());
                                temp.setAllItemsInSection(result.results.getObjects());
                            }*/

                            temp.setSearchString(searchString);
                            if (!iscategoryAdded(result.results.getObjects().get(0).getType())) {
                                searchOutputModel.add(temp);
                            }
                            Log.e("SEARCH SIZE", String.valueOf(searchOutputModel.size()));
                            if (searchOutputModel.size() > 0) {
                                searchResultCallBack.response(true, searchOutputModel, "resultFound");
                            } else {
                                searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                            }
                        } else {
                            searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                        }

                    } else {
                        searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                    }

                } else {
                    searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                }
            } else {
                if ((result.error.getCode().equals(AppLevelConstants.KS_EXPIRE))) {
                    searchKeyword(context, keyToSearch, model, count, CallBack, searchKeyword, selectedGenre);
                }
            }
        });
        getRequestQueue().queue(assetService.build(client));
    }


    private void setSearchBuilder(Context context, final String keyToSearch, final List<MediaTypeModel> model, int count, SearchResultCallBack CallBack) {
        final FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        if (context.getResources().getBoolean(R.bool.isTablet)) {
            filterPager.setPageSize(5);
        } else {
            filterPager.setPageSize(4);
        }

        Log.e(String.valueOf(count) + "====>", currentMediaTypes.get(count).getId());

        final SearchAssetFilter assetFilter = new SearchAssetFilter();
        assetFilter.setKSql(keyToSearch);
        assetFilter.setTypeIn(currentMediaTypes.get(count).getId());

        AssetService.ListAssetBuilder assetService = AssetService.list(assetFilter, filterPager).setLanguage("may").setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results.getTotalCount() > 0) {
                    if (result.results.getObjects() != null) {
                        if (result.results.getObjects().size() > 0) {
                            SearchModel temp = new SearchModel();
                            temp.setTotalCount(result.results.getTotalCount());
                            temp.setHeaderTitle(getNameFromType(result.results.getObjects().get(0).getType()));
                            temp.setType(result.results.getObjects().get(0).getType());
                            temp.setAllItemsInSection(result.results.getObjects());
                            temp.setSearchString(searchString);
                            if (!iscategoryAdded(result.results.getObjects().get(0).getType())) {
                                searchOutputModel.add(temp);
                            }
                            Log.e("SEARCH SIZE", String.valueOf(searchOutputModel.size()));
                            if (searchOutputModel.size() > 0) {
                                searchResultCallBack.response(true, searchOutputModel, "resultFound");
                            } else {
                                searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                            }
                        } else {
                            searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                        }

                    } else {
                        searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                    }

                } else {
                    searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                }
            } else {
                if ((result.error.getCode().equals(AppLevelConstants.KS_EXPIRE))) {
                    searchKeyword(context, keyToSearch, model, count, CallBack, keyToSearch, "");
                }
            }
        });
        getRequestQueue().queue(assetService.build(client));
    }

    private String getNameFromType(int type) {
        String returnValue = "";
        String stringType = String.valueOf(type);
        for (int i = 0; i < currentMediaTypes.size(); i++) {
            if (stringType.equalsIgnoreCase(currentMediaTypes.get(i).getId())) {
                returnValue = currentMediaTypes.get(i).getName();
            }
        }
        return returnValue;
    }

    private boolean iscategoryAdded(int category) {
        boolean isAdded = false;
        for (int i = 0; i < searchOutputModel.size(); i++) {
            if (category == searchOutputModel.get(i).getType()) {
                isAdded = true;
            }
        }
        return isAdded;

    }

    public void popularSearch(Context context, PopularSearchCallBack CallBack) {
        popularCallBack = CallBack;

        final ChannelFilter channelFilter = new ChannelFilter();
        channelFilter.setIdEqual(333171);

        Runnable runnable = () -> {
            clientSetupKs();
            AssetService.ListAssetBuilder assetService = AssetService.list(channelFilter).setCompletion(result -> {
                if (result.isSuccess()) {
                    popularCallBack.response(true, result);
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        popularSearch(context, CallBack);
                                    } else {
                                        popularCallBack.response(false, result);
                                    }
                                }
                            });

                        } else {
                            popularCallBack.response(false, result);
                        }
                    } else {
                        popularCallBack.response(false, result);
                    }
                    //channelCallBack.response(false, commonResponse);
                }

            });

            getRequestQueue().queue(assetService.build(client));
        };
        new Thread(runnable).start();
    }

    public void hitApiDMSWithInner(final DMSCallBack dmsCallBack) {

        ApiInterface endpoint = RequestConfig.getClient(BuildConfig.KALTURA_BASE_URL).create(ApiInterface.class);
        JsonObject requestParam = new JsonObject();
        requestParam.addProperty("apiVersion", BuildConfig.KALTURA_API_VERSION);
        requestParam.addProperty("applicationName", BuildConfig.KALTURA_APPLICATION_NAME);
        requestParam.addProperty("clientVersion", BuildConfig.KALTURA_CLIENT_VERSION);
        requestParam.addProperty("platform", BuildConfig.KALTURA_PLATFORM);
        requestParam.addProperty("udid", UDID.getDeviceId(activity, activity.getContentResolver()));
        requestParam.addProperty("tag", BuildConfig.KALTURA_TAG);
        requestParam.addProperty("partnerId", AppLevelConstants.PARTNER_ID);

        Call<ResponseDmsModel> call = endpoint.getDMS(requestParam);
        call.enqueue(new Callback<ResponseDmsModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDmsModel> call, @NonNull retrofit2.Response<ResponseDmsModel> response) {
                ResponseDmsModel cl = response.body();
                SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(activity);
                Gson gson = new Gson();
                String json = gson.toJson(cl);
                sharedPrefHelper.setString("DMS_Response", json);
                sharedPrefHelper.setString("DMS_Date", "" + System.currentTimeMillis());
                callanonymousLoginWithKs(dmsCallBack);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseDmsModel> call, @NonNull Throwable t) {
                dmsCallBack.configuration(false);
            }
        });
    }


    public void getPlaybackContext(String assetId, String fileId, PlayBackContextCallBack playBackContextCallBack) {

        ApiInterface endpoint = RequestConfig.getClient(BuildConfig.KALTURA_BASE_URL).create(ApiInterface.class);
        JsonObject requestParam = new JsonObject();
        JsonObject contextDataParams = new JsonObject();

        requestParam.addProperty("apiVersion", BuildConfig.KALTURA_API_VERSION);
        requestParam.addProperty("assetId", assetId);
        requestParam.addProperty("assetType", "media");
        String ks = "";
        if (UserInfo.getInstance(activity).isActive()) {
            ks = KsPreferenceKey.getInstance(activity).getStartSessionKs();
        } else {
            ks = KsPreferenceKey.getInstance(activity).getAnonymousks();
        }
        requestParam.addProperty("ks", ks);
        contextDataParams.addProperty("objectType", "KalturaPlaybackContextOptions");
        contextDataParams.addProperty("mediaProtocol", "https");
        contextDataParams.addProperty("assetFileIds", fileId);
        contextDataParams.addProperty("context", "PLAYBACK");
        contextDataParams.addProperty("streamerType", "mpegdash");
        contextDataParams.addProperty("urlType", "PLAYMANIFEST");
        requestParam.add("contextDataParams", contextDataParams);


        Call<PlaybackContextResponse> call = endpoint.getPlaybackContext(requestParam);
        call.enqueue(new Callback<PlaybackContextResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlaybackContextResponse> call, @NonNull retrofit2.Response<PlaybackContextResponse> response) {
                if (response != null && response.isSuccessful() && response.body() != null && response.body().getResult() != null && response.body().getResult().getSources() != null && response.body().getResult().getSources().get(0) != null && response.body().getResult().getSources().get(0).getUrl() != null) {
                    playBackContextCallBack.getUrl(response.body().getResult().getSources().get(0).getUrl());
                } else {
                    playBackContextCallBack.getUrl("");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaybackContextResponse> call, @NonNull Throwable t) {
                playBackContextCallBack.getUrl("");
            }
        });
    }

    private void callanonymousLoginWithKs(final DMSCallBack callBack) {
        clientSetup();
        final Handler handler = new Handler();
        Runnable runnable = () -> {
            String udid = AppCommonMethods.callpreference(activity).getUdid();
            Map<String, StringValue> params = new Hashtable<>();
            OttUserService.AnonymousLoginOttUserBuilder builder = OttUserService.anonymousLogin(AppLevelConstants.PARTNER_ID, udid)
                    .setCompletion(result -> handler.post(() -> {
                        if (result.isSuccess()) {
                            KsPreferenceKey.getInstance(activity).setAnonymousks(result.results.getKs());
                            callBack.configuration(true);
                        } else {
                            callBack.configuration(false);
                        }
                    }));

            getRequestQueue().queue(builder.build(client));
        };
        new Thread(runnable).start();
    }

    public void hitApiDMS(final DMSCallBack callBack) {
        ApiInterface endpoint = RequestConfig.getClient(BuildConfig.KALTURA_BASE_URL).create(ApiInterface.class);
        JsonObject requestParam = new JsonObject();
        requestParam.addProperty("apiVersion", BuildConfig.KALTURA_API_VERSION);
        requestParam.addProperty("applicationName", BuildConfig.KALTURA_APPLICATION_NAME);
        requestParam.addProperty("clientVersion", BuildConfig.KALTURA_CLIENT_VERSION);
        requestParam.addProperty("platform", BuildConfig.KALTURA_PLATFORM);
        requestParam.addProperty("udid", UDID.getDeviceId(activity, activity.getContentResolver()));
        requestParam.addProperty("tag", BuildConfig.KALTURA_TAG);
        requestParam.addProperty("partnerId", BuildConfig.KALTURA_PARTNER_ID);
        Log.e("REQUEST", requestParam.toString());
        Log.e("oncreate: ", "in8");
        Call<ResponseDmsModel> call = endpoint.getDMS(requestParam);
        call.enqueue(new Callback<ResponseDmsModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDmsModel> call, @NonNull retrofit2.Response<ResponseDmsModel> response) {

                Log.e("oncreate: ", "in9");
                PrintLogging.printLog(this.getClass(), "Dms Log response", "" + response.toString());
                // Log.e("Mytag", "" + response.body().getParams().getMediaTypes().getMovie());
                PrintLogging.printLog(this.getClass(), "", "DMS" + "--" + response.isSuccessful());
                try {


                    ResponseDmsModel responseDmsModel = response.body();
                    if (responseDmsModel != null && response.body() != null) {
                        if (response.body().getParams().getGateways() != null) {
                            StringBuilder stringBuilder = new StringBuilder(response.body().getParams().getGateways().getJsonGW());
                            stringBuilder.append(activity.getString(R.string.suffix_api_v3));
                            Log.e("Phonex Base Url", stringBuilder.toString());
                            KsPreferenceKey.getInstance(activity).setKalturaPhoenixUrl(stringBuilder.toString());
                        }
                    }
                    SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance(activity);
                    Gson gson = new Gson();


                    if (responseDmsModel == null) {
                        return;
                    }

                    ArrayList<FilterLanguages> fliterLanguageList = new ArrayList<>();
                    for (Map.Entry<String, JsonElement> entry : responseDmsModel.getParams().getFilterLanguages().entrySet()) {
                        FilterLanguages levels = new FilterLanguages();
                        levels.setKey(entry.getKey());
                        levels.setValue(entry.getValue().getAsString());
                        levels.setSelected(false);
                        fliterLanguageList.add(levels);
                    }

                    responseDmsModel.setFilterLanguageList(fliterLanguageList);

                    ArrayList<AudioLanguages> audioLanguageList = new ArrayList<>();
                    for (Map.Entry<String, JsonElement> entry : responseDmsModel.getParams().getAudioLanguages().entrySet()) {
                        AudioLanguages levels = new AudioLanguages();
                        levels.setKey(entry.getKey());
                        levels.setValue(entry.getValue().getAsString());
                        audioLanguageList.add(levels);
                    }

                    responseDmsModel.setAudioLanguageList(audioLanguageList);
                    Log.w("SubtitleLanguage", responseDmsModel.getAudioLanguageList().get(0).getKey());

                    ArrayList<SubtitleLanguages> subtitleLanguageList = new ArrayList<>();
                    for (Map.Entry<String, JsonElement> entry : responseDmsModel.getParams().getSubtitleLanguages().entrySet()) {
                        SubtitleLanguages levels = new SubtitleLanguages();
                        levels.setKey(entry.getKey());
                        levels.setValue(entry.getValue().getAsJsonArray());
                        subtitleLanguageList.add(levels);
                    }

                    responseDmsModel.setSubtitleLanguageList(subtitleLanguageList);
                    Log.w("SubtitleLanguage", responseDmsModel.getSubtitleLanguageList().get(0).getKey());


                    ArrayList<FilterValues> filterValuesList = new ArrayList<>();
                    for (Map.Entry<String, JsonElement> entry : responseDmsModel.getParams().getSubtitleLanguages().entrySet()) {
                        FilterValues levels = new FilterValues();
                        levels.setKey(entry.getKey());
                        // levels.setValue(entry.getValue().getAsJsonArray());
                        filterValuesList.add(levels);
                    }

                    responseDmsModel.setFilterValuesList(filterValuesList);
                    Log.w("searchValues->>", new Gson().toJson(filterValuesList));

                    ArrayList<ParentalRatingLevels> parentalRatingLevels = new ArrayList<>();

                 /*   for (Map.Entry<String, JsonElement> entry : responseDmsModel.getParams().getParentalRatingLevels().entrySet()) {
                        ParentalRatingLevels levels = new ParentalRatingLevels();
                        levels.setKey(entry.getKey());
                        levels.setValue(entry.getValue().getAsInt());
                        parentalRatingLevels.add(levels);

                    }
                    responseDmsModel.setParentalRatingLevels(parentalRatingLevels);
                    Log.d("ParentalLevel", new Gson().toJson(parentalRatingLevels));
*/

               /* ArrayList<ParentalLevels> parentalLevels = new ArrayList<>();
                ArrayList<ParentalDescription> descriptions = new ArrayList<>();
                ArrayList<ParentalMapping> parentalMappingArray = new ArrayList<>();



                    responseDmsModel.getParams().setDefaultParentalLevel(responseDmsModel.getParams().getParentalRatings().get(AppLevelConstants.DEFAULT_PARENTAL_LEVEL).getAsString());
                    for (Map.Entry<String, JsonElement> entry : responseDmsModel.getParams().getParentalRatings().getAsJsonObject(AppLevelConstants.PARENTAL_LEVEL).entrySet()) {
                        ParentalLevels levels = new ParentalLevels();
                        levels.setKey(entry.getKey());
                        levels.setValue(entry.getValue().getAsInt());
                        parentalLevels.add(levels);
                        Log.d("ParentalLevel",new Gson().toJson(parentalLevels));

                    }

                    for(Map.Entry<String, JsonElement> entry1 : responseDmsModel.getParams().getParentalRatings().getAsJsonObject(AppLevelConstants.PARENTAL_DESCRIPTION).entrySet()){
                        ParentalDescription parentalDescription = new ParentalDescription();
                        parentalDescription.setKey(entry1.getKey());
                        parentalDescription.setDescription(entry1.getValue().getAsString());
                        descriptions.add(parentalDescription);
                        Log.d("ParentalDescription",new Gson().toJson(descriptions));
                    }
                    for(Map.Entry<String, JsonElement> entry2 : responseDmsModel.getParams().getParentalRatings().getAsJsonObject(AppLevelConstants.PARENTAL_MAPPING).entrySet()){
                        ParentalMapping parentalMapping = new ParentalMapping();
                        parentalMapping.setKey(entry2.getKey());
                        parentalMapping.setMappingList(entry2.getValue());
                        parentalMappingArray.add(parentalMapping);F


                    }


                    responseDmsModel.setParentalLevels(parentalLevels);
                    responseDmsModel.setParentalDescriptions(descriptions);
                    responseDmsModel.setMappingArrayList(parentalMappingArray);*/

                    //responseDmsModel.setParentalLevels(parentalLevels);
                    String json = gson.toJson(responseDmsModel);
                    sharedPrefHelper.setString(AppLevelConstants.DMS_RESPONSE, json);
                    sharedPrefHelper.setString("DMS_Date", "" + System.currentTimeMillis());
                    KsPreferenceKey.getInstance(activity).setLowBitrateMaxLimit(responseDmsModel.getParams().getLowBitRateMaxLimit());
                    KsPreferenceKey.getInstance(activity).setMediumBitrateMaxLimit(responseDmsModel.getParams().getMediumBitRateMaxLimit());
                    KsPreferenceKey.getInstance(activity).setHighBitrateMaxLimit(responseDmsModel.getParams().getHighBitRatemaxLimit());

                   /* KsPreferenceKey.getInstance(activity).setDefaultEntitlement(responseDmsModel.getParams().getDefaultEntitlement());
                    KsPreferenceKey.getInstance(activity).setATBpaymentGatewayId(responseDmsModel.getParams().getATBpaymentGatewayId());
                    KsPreferenceKey.getInstance(activity).setSubscriptionOffer(responseDmsModel.getParams().getSubscriptionOffer());
                    KsPreferenceKey.getInstance(activity).setRoot(responseDmsModel.getParams().getCategories().getRoot());*/
                    Log.d("ParentalLevel", FileFormatHelper.getDash_widevine(activity));
                    callBack.configuration(true);
                } catch (Exception e) {
                    Log.e("oncreate: ", "in10" + "crash");
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseDmsModel> call, @NonNull Throwable t) {
                Log.e("oncreate: ", "in10");
                PrintLogging.printLog(this.getClass(), "", "responseDMS" + t.toString());
            }
        });
    }

    public void callanonymousLogin(SharedPrefHelper session, KsAnonymousLoginCallBack ksAnonymousLoginCallBack) {
        clientSetup();
        this.anonymouscallBack = ksAnonymousLoginCallBack;
        String udid = UDID.getDeviceId(activity, activity.getContentResolver());
        Map<String, StringValue> params = new Hashtable<>();
        OttUserService.AnonymousLoginOttUserBuilder builder = OttUserService.anonymousLogin(AppLevelConstants.PARTNER_ID, udid)
                .setCompletion(result -> {
                    if (result.isSuccess()) {
                        KsPreferenceKey.getInstance(activity).setAnonymousks(result.results.getKs());
                        anonymouscallBack.success(true, result);
                    } else {
                        anonymouscallBack.failure(false, result);
                    }

                });

        getRequestQueue().queue(builder.build(client));
    }

    public void clientSetup() {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(activity);
        Configuration config = new Configuration();
        config.setConnectTimeout(30000);
        if (responseDmsModel != null) {
            config.setEndpoint(responseDmsModel.getParams().getApiProxyUrlKaltura());
        } else {
            config.setEndpoint(AppConstants.END_POINT);
        }
        client = new Client(config);

    }

    private RequestQueue getRequestQueue() {
        RequestQueue requestQueue = APIOkRequestsExecutor.getExecutor();
        requestQueue.enableLogs(true);
        return requestQueue;
    }

    public void callHomeChannels(Context context, final int type, final int tab, final HomechannelCallBack homechannelCallBack) {
        clientSetupKs();
        if (type == 0) {
            subcategoryData(context, tab, homechannelCallBack);
        } else {
            ApplicationMain appState = ((ApplicationMain) activity.getApplicationContext());
            OttCategoryService.GetOttCategoryBuilder builder = OttCategoryService.get((int) tabID).setCompletion(result -> {
                if (result.isSuccess()) {
                    List<Channel> channelList = result.results.getChannels();
                    if (channelList.size() > 0) {
                        long id = channelList.get(0).getId();
                    }
                } else {
                    homechannelCallBack.response(true, responseList, null);
                }
            });

            getRequestQueue().queue(builder.build(client));

        }
    }


    private void clientSetupKs() {

           /* ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(activity);
            Configuration config = new Configuration();
            config.setParam("timestamp", System.currentTimeMillis());
            config.setConnectTimeout(30000);
            if (responseDmsModel != null) {
                if (BuildConfig.FLAVOR.equalsIgnoreCase("qa")) {
                  //  config.setEndpoint(responseDmsModel.getParams().getGateways().getJsonGW().concat("/latest"));
                    config.setEndpoint("https://rest-sgs1.ott.kaltura.com");
                } else {
                    config.setEndpoint(responseDmsModel.getParams().getGateways().getJsonGW());
                }
            } else {
                config.setEndpoint(AppConstants.END_POINT);
            }
            client = new Client(config);
            if (KsPreferenceKey.getInstance(activity).getUserActive()) {
                client.setKs(KsPreferenceKey.getInstance(activity).getStartSessionKs());
            } else {
                client.setKs(KsPreferenceKey.getInstance(activity).getAnonymousks());
            }*/

        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(activity);
        Configuration config = new Configuration();
        config.setConnectTimeout(30000);
        if (responseDmsModel != null && responseDmsModel.getParams() != null && responseDmsModel.getParams().getApiProxyUrlKaltura() != null) {
            config.setEndpoint(responseDmsModel.getParams().getApiProxyUrlKaltura());
        } else {
            config.setEndpoint(BuildConfig.KALTURA_BASE_URL);
        }
        client = new Client(config);
        if (new KsPreferenceKey(activity).getAppLangName().equalsIgnoreCase("ms")) {
            client.setLanguage("may");
        } else {
            client.setLanguage("en");
        }
        if (UserInfo.getInstance(activity).isActive()) {
            client.setKs(KsPreferenceKey.getInstance(activity).getStartSessionKs());
        } else {
            client.setKs(KsPreferenceKey.getInstance(activity).getAnonymousks());
        }


    }

    private void loginClient(String ks) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(activity);
        Configuration config = new Configuration();
        config.setConnectTimeout(30000);
        if (responseDmsModel != null) {
            config.setEndpoint(responseDmsModel.getParams().getApiProxyUrlKaltura());

        } else {
            config.setEndpoint(AppConstants.END_POINT);
        }
        client = new Client(config);
        client.setKs(ks);

    }

    private void subcategoryData(Context context, int tab, HomechannelCallBack homechanelCallBack) {
        if (tab == AppLevelConstants.TAB_HOME) {
            tabID = SubCategoriesPrefs.getInstance(context).getHomeTabId();
            PrintLogging.printLog(this.getClass(), "", tab + "-->>" + tabID);
            callHomeChannels(context, 1, tab, homechanelCallBack);
        } else if (tab == AppLevelConstants.TAB_LIVE) {
            tabID = SubCategoriesPrefs.getInstance(context).getLiveTabId();
            PrintLogging.printLog(this.getClass(), "", tab + "-->>" + tabID);
            callHomeChannels(context, 1, tab, homechanelCallBack);
        } else if (tab == AppLevelConstants.TAB_VIDEO) {
            tabID = SubCategoriesPrefs.getInstance(context).getVideoTabId();
            PrintLogging.printLog(this.getClass(), "", tab + "-->>" + tabID);
            callHomeChannels(context, 1, tab, homechanelCallBack);
        } else if (tab == AppLevelConstants.TAB_MOVIE_DESCRIPTION) {
            tabID = SubCategoriesPrefs.getInstance(context).getMovieDetailsId();
            PrintLogging.printLog(this.getClass(), "", tab + "-->>" + tabID);
            callHomeChannels(context, 1, tab, homechanelCallBack);
        } else if (tab == AppLevelConstants.TAB_SHORTFILM_DESCRIPTION) {
            tabID = SubCategoriesPrefs.getInstance(context).getShortVideoDetailsId();
            PrintLogging.printLog(this.getClass(), "", tab + "-->>" + tabID);
            callHomeChannels(context, 1, tab, homechanelCallBack);
        } else if (tab == AppLevelConstants.TAB_DRAMA_DETAILS) {
            tabID = SubCategoriesPrefs.getInstance(context).getDramaDetailsId();
            PrintLogging.printLog(this.getClass(), "", tab + "-->>" + tabID);
            callHomeChannels(context, 1, tab, homechanelCallBack);
        } else if (tab == AppLevelConstants.TAB_DRAMA_EPISODES_DETAILS) {
            tabID = SubCategoriesPrefs.getInstance(context).getDramaEpisodeDetailsId();
            PrintLogging.printLog(this.getClass(), "", tab + "-->>" + tabID);
            callHomeChannels(context, 1, tab, homechanelCallBack);
        } else if (tab == AppLevelConstants.TAB_LIVETV_DETAIL) {
            tabID = SubCategoriesPrefs.getInstance(context).getLiveTvDetailsId();
            PrintLogging.printLog(this.getClass(), "", tab + "-->>" + tabID);
            callHomeChannels(context, 1, tab, homechanelCallBack);
        } else if (tab == AppLevelConstants.TAB_FORWARDED_EPG_DETAIL) {
            tabID = SubCategoriesPrefs.getInstance(context).getForwardEpgId();
            PrintLogging.printLog(this.getClass(), "", tab + "-->>" + tabID);
            callHomeChannels(context, 1, tab, homechanelCallBack);
        }
    }

    public void callContinueWatchingForListing(final ContinueWatchingCallBack homechannelCallBack) {
        clientSetupKs();
        AssetHistoryFilter assetHistoryFilter = new AssetHistoryFilter();
        assetHistoryFilter.statusEqual("all");
        assetHistoryFilter.daysLessThanOrEqual("30");

        FilterPager pagerFilter = new FilterPager();
        pagerFilter.setPageIndex(1);
        pagerFilter.setPageSize(100);

        AssetHistoryService.ListAssetHistoryBuilder builder = new AssetHistoryService.ListAssetHistoryBuilder(assetHistoryFilter, pagerFilter).setCompletion(new OnCompletion<Response<ListResponse<AssetHistory>>>() {
            @Override
            public void onComplete(Response<ListResponse<AssetHistory>> result) {
                if (result.isSuccess()) {
                    AppCommonMethods.setContinueWatchingPreferences(result.results.getObjects(), activity);
                    KsPreferenceKey.getInstance(activity).setContinueWatchingIndex(continueWatchingIndex);
                    getAssetListForListing(result.results.getObjects(), homechannelCallBack);
                } else {
                    //  homechannelCallBack.response(false, null);

                    if (result.error != null) {

                        String errorCode = result.error.getCode();

                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callContinueWatchingForListing(homechannelCallBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        homechannelCallBack.response(false, null);
                                    }
                                }
                            });
                        else {
                            homechannelCallBack.response(false, null);
                        }
                    } else {
                        homechannelCallBack.response(false, null);
                    }
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void getAssetListForListing(List<AssetHistory> objects, final ContinueWatchingCallBack homechannelCallBack) {
        String assetId = AssetContent.getAssetIds(activity, objects);

        clientSetupKs();
        SearchAssetFilter relatedFilter = new SearchAssetFilter();

        String ksql = KSQL.forContinueWatchingAssets(assetId);
        relatedFilter.setKSql(ksql);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(100);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {
            try {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            if (result.results.getObjects().size() > 0) {
                                homechannelCallBack.response(true, result);
                            } else {
                                responseList.add(null);
                                homechannelCallBack.response(false, null);
                            }
                        } else {
                            responseList.add(null);
                            homechannelCallBack.response(false, null);
                        }
                    } else {
                        responseList.add(null);
                        homechannelCallBack.response(false, null);
                    }
                } else {

                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getAssetListForListing(objects, homechannelCallBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        responseList.add(null);
                                        homechannelCallBack.response(false, null);

                                    }
                                }
                            });
                        else {
                            responseList.add(null);
                            homechannelCallBack.response(false, null);

                        }
                    } else {
                        responseList.add(null);
                        homechannelCallBack.response(false, null);

                    }


                }
            } catch (Exception e) {
                PrintLogging.printLog(this.getClass(), "Exception", "" + e);

            }

           /* if (result.isSuccess()) {
                AppCommonMethods.checkContinueWatchingData(responseList, homechannelCallBack, continueWatchingIndex, result, channelList);

                PrintLogging.printLog("", "assetIds" + result.results.getObjects().size());

            } else {
                responseList.add(continueWatchingIndex, null);
                homechannelCallBack.response(true, responseList, channelList);
            }*/
        });
        getRequestQueue().queue(builder.build(client));
    }

    private void callContinueWatchingAPI(final List<Response<ListResponse<Asset>>> responseList, final List<VIUChannel> channelList, final HomechannelCallBack homechannelCallBack) {

        clientSetupKs();
        AssetHistoryFilter assetHistoryFilter = new AssetHistoryFilter();
        assetHistoryFilter.statusEqual(WatchStatus.PROGRESS.name());
        assetHistoryFilter.daysLessThanOrEqual(AppCommonMethods.getAssetHistory(activity));

        FilterPager pagerFilter = new FilterPager();
        pagerFilter.setPageIndex(1);
        pagerFilter.setPageSize(100);

        DetachedResponseProfile responseProfile = new DetachedResponseProfile();
        DetachedResponseProfile relatedProfiles = new DetachedResponseProfile();

        AssetHistorySuppressFilter assetHistorySuppressFilter = new AssetHistorySuppressFilter();
        relatedProfiles.setFilter(assetHistorySuppressFilter);
        relatedProfiles.setName("suppress");
        List<DetachedResponseProfile> list = new ArrayList<>();
        list.add(relatedProfiles);
        responseProfile.setRelatedProfiles(list);

        AssetHistoryService.ListAssetHistoryBuilder builder = new AssetHistoryService.ListAssetHistoryBuilder(assetHistoryFilter, pagerFilter).setCompletion(new OnCompletion<Response<ListResponse<AssetHistory>>>() {
            @Override
            public void onComplete(Response<ListResponse<AssetHistory>> result) {
                if (result.isSuccess()) {
                    AppCommonMethods.setContinueWatchingPreferences(result.results.getObjects(), activity);
                    KsPreferenceKey.getInstance(activity).setContinueWatchingIndex(continueWatchingIndex);
                    getAssetList(result.results.getObjects(), responseList, channelList, homechannelCallBack);
                } else {

                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        Log.e("errorCodesscont", errorCode + "");
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callContinueWatchingAPI(responseList, channelList, homechannelCallBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        responseList.add(null);
                                        homechannelCallBack.response(false, responseList, channelList);

                                    }
                                }
                            });
                        else {
                            responseList.add(null);
                            homechannelCallBack.response(false, responseList, channelList);

                        }
                    } else {
                        responseList.add(null);
                        homechannelCallBack.response(false, responseList, channelList);

                    }


                }
            }
        });
        builder.setResponseProfile(responseProfile);
        getRequestQueue().queue(builder.build(client));
    }

    private void getAssetList(List<AssetHistory> objects, final List<Response<ListResponse<Asset>>> responseList, final List<VIUChannel> channelList, final HomechannelCallBack homechannelCallBack) {
        String assetId = AssetContent.getAssetIds(activity, objects);

        clientSetupKs();
        SearchAssetFilter relatedFilter = new SearchAssetFilter();

        String ksql = KSQL.forContinueWatchingAssets(assetId);
        relatedFilter.setKSql(ksql);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(100);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results != null) {
                    if (result.results.getObjects() != null) {
                        if (result.results.getObjects().size() > 0) {
                            responseList.add(result);
                            homechannelCallBack.response(true, responseList, channelList);
                        } else {
                            responseList.add(null);
                            homechannelCallBack.response(false, null, null);
                        }
                    } else {
                        responseList.add(null);
                        homechannelCallBack.response(false, null, null);
                    }
                } else {
                    responseList.add(null);
                    homechannelCallBack.response(false, null, null);
                }
            } else {


                if (result.error != null) {

                    String errorCode = result.error.getCode();
                    Log.e("errorCodessAsset", errorCode + "");
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getAssetList(objects, responseList, channelList, homechannelCallBack);
                                    //getSubCategories(context, subCategoryCallBack);
                                } else {
                                    responseList.add(null);
                                    homechannelCallBack.response(false, null, null);

                                }
                            }
                        });
                    else {
                        responseList.add(null);
                        homechannelCallBack.response(false, null, null);

                    }
                } else {
                    responseList.add(null);
                    homechannelCallBack.response(false, null, null);

                }


            }
           /* if (result.isSuccess()) {
                AppCommonMethods.checkContinueWatchingData(responseList, homechannelCallBack, continueWatchingIndex, result, channelList);

                PrintLogging.printLog("", "assetIds" + result.results.getObjects().size());

            } else {
                responseList.add(continueWatchingIndex, null);
                homechannelCallBack.response(true, responseList, channelList);
            }*/
        });
        getRequestQueue().queue(builder.build(client));
    }

    private void checkRecomendateAndContinue(List<VIUChannel> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (channelList.get(i).getDescription().equals(AppLevelConstants.KEY_CONTINUE_WATCHING)) {
                PrintLogging.printLog(this.getClass(), "", "indexChecking co-->>" + i);
                continueWatchingIndex = i;
                // channelList2.add(i,channelList.get(i));
            } else if (channelList.get(i).getDescription().equals(AppLevelConstants.KEY_RECOMMENDED)) {
                PrintLogging.printLog(this.getClass(), "", "indexChecking re-->>" + i);
                recommendedIndex = i;
                // increaseChannelList(i,channelList);

            }
        }

        PrintLogging.printLog(this.getClass(), "", "indexesValues" + continueWatchingIndex + "-->>" + recommendedIndex);

    }

    public void callChannelData(final long id, List<VIUChannel> list, HomechannelCallBack callBack) {
        homechannelCallBack = callBack;

        this.channelList = list;
        checkRecomendateAndContinue(list);
        listAssetBuilders = new ArrayList<>();
        responseList = new ArrayList<>();
        int size = channelList.size();

        for (int i = 0; i < size; i++) {
            boolean status = UserInfo.getInstance(activity).isActive();
            if (status) {
                PrintLogging.printLog(this.getClass(), "", "channellLisSize==" + size);
                if (channelList.get(i).getDescription().equals(AppLevelConstants.KEY_CONTINUE_WATCHING)) {
                    PrintLogging.printLog(this.getClass(), "", "idsPrint" + "-->> continue" + channelList.get(i).getName() + "-->>" + channelList.get(i).getDescription());
                    continueWatchingIndex = i;
                    if (size == 1) {
                        callContinueWatchingAPI(responseList, channelList, homechannelCallBack);
                    }
                } else {
                    long idd = channelList.get(i).getId();
                    PrintLogging.printLog(this.getClass(), "", "idsPrint" + +idd + "-->>" + channelList.get(i).getName() + "-->>" + channelList.get(i).getDescription());
                    int iid = (int) idd;
                    ChannelFilter channelFilter = new ChannelFilter();
                    channelFilter.setIdEqual(iid);

                    FilterPager filterPager = new FilterPager();
                    filterPager.setPageIndex(1);
                    filterPager.setPageSize(20);

                    AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(result -> {
                        Log.w("homeListing", result.isSuccess() + "");
                        if (result.isSuccess()) {
                            responseList.add(result);
                        } else {
                            responseList.add(null);
                        }
                        count++;

                        if (count == listAssetBuilders.size()) {
                            if (result.isSuccess()) {
                                if (result.results != null) {
                                    if (continueWatchingIndex != -1) {
                                        callContinueWatchingAPI(responseList, channelList, homechannelCallBack);
                                    } else {
                                        KsPreferenceKey.getInstance(activity).setContinueWatchingIndex(continueWatchingIndex);
                                        homechannelCallBack.response(true, responseList, channelList);
                                    }

                                    //callSesionEpisode(result.results.getObjects());
                                }//
                            } else {
                                homechannelCallBack.response(false, responseList, channelList);
                            }
                        }
                    });
                    listAssetBuilders.add(builder);
                }
            } else {

                continueWatchingIndex = -1;
                KsPreferenceKey.getInstance(activity).setContinueWatchingIndex(continueWatchingIndex);
                PrintLogging.printLog(this.getClass(), "", "elsewatching" + KsPreferenceKey.getInstance(activity).getContinueWatchingIndex() + "");

                long idd = channelList.get(i).getId();
                PrintLogging.printLog(this.getClass(), "", "idsPrint" + +idd + "-->>" + channelList.get(i).getName() + "-->>" + channelList.get(i).getDescription());
                int iid = (int) idd;
                ChannelFilter channelFilter = new ChannelFilter();
                channelFilter.setIdEqual(iid);

                FilterPager filterPager = new FilterPager();
                filterPager.setPageIndex(1);
                filterPager.setPageSize(20);

                AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(result -> {
                    Log.w("homeListing", result.isSuccess() + "");
                    if (result.isSuccess()) {
                        responseList.add(result);
                    } else {
                        responseList.add(null);
                    }
                    count++;

                    if (count == listAssetBuilders.size()) {
                        if (result.isSuccess()) {
                            if (result.results != null) {
                                if (continueWatchingIndex != -1) {
                                    callContinueWatchingAPI(responseList, channelList, homechannelCallBack);
                                } else {
                                    homechannelCallBack.response(true, responseList, channelList);
                                }

                                //callSesionEpisode(result.results.getObjects());
                            }//
                        } else {
                            homechannelCallBack.response(false, responseList, channelList);
                        }
                    }
                });
                listAssetBuilders.add(builder);
            }


        }

        MultiRequestBuilder multiRequestBuilder = new MultiRequestBuilder();
        for (int j = 0; j < listAssetBuilders.size(); j++) {
            multiRequestBuilder = multiRequestBuilder.add(listAssetBuilders.get(j));
        }
        getRequestQueue().queue(multiRequestBuilder.build(client));


    }

    public void DeeplinkingLivecallSpecificAsset(String assetId, SpecificAssetCallBack callBack) {
        specificAssetCallBack = callBack;
        clientSetupKs();
        AssetService.GetAssetBuilder builder = AssetService.get(assetId, AssetReferenceType.EPG_INTERNAL).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results != null) {
                    specificAssetCallBack.getAsset(true, result.results);
                } else {
                    specificAssetCallBack.getAsset(false, null);
                }
            } else {


                if (result.error != null) {

                    String errorCode = result.error.getCode();
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    DeeplinkingLivecallSpecificAsset(assetId, callBack);
                                    //getSubCategories(context, subCategoryCallBack);
                                } else {
                                    specificAssetCallBack.getAsset(false, result.results);
                                }
                            }
                        });
                    else {
                        specificAssetCallBack.getAsset(false, result.results);
                    }
                } else {
                    specificAssetCallBack.getAsset(false, result.results);
                }


            }

        });
        getRequestQueue().queue(builder.build(client));
    }

    public void checkUserPreferences(Context context, UserPrefrencesCallBack userPrefrencesCallBack) {
        prefrencesCallBack = userPrefrencesCallBack;
//        boolean isActive = new KsPreferenceKeys(activity).getUserActive();
        clientSetupKs();

        OttUserService.GetOttUserBuilder builder = OttUserService.get().setCompletion(result -> {
            if (result.isSuccess()) {
                AssetContent.getUserTypeForDialogAndNonDialogUser(result.results.getDynamicData(), context);
                if (AssetContent.getUserPreference(result.results.getDynamicData()) == null)
                    prefrencesCallBack.response("");
                else
                    prefrencesCallBack.response(AssetContent.getUserPreference(result.results.getDynamicData()));

            }

//            else {
//                prefrencesCallBack.response("");
//            }

            else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreCheckUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    checkUserPreferences(context, userPrefrencesCallBack);
                                } else {
                                    prefrencesCallBack.failure();
                                }
                            }
                        });

                    } else {
                        prefrencesCallBack.failure();
                    }
                } else {
                    prefrencesCallBack.failure();
                }
                //channelCallBack.response(false, commonResponse);
            }

        });

        getRequestQueue().queue(builder.build(client));
    }

    public void getGenre(GenreCallBack callBack) {
        clientSetupKs();
        genreCallBack = callBack;
        SearchAssetFilter searchAssetFilter = new SearchAssetFilter();
        searchAssetFilter.setTypeIn(MediaTypeConstant.getGenre(activity) + "");
        searchAssetFilter.orderBy("NAME_ASC");

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(100);

        AssetService.ListAssetBuilder builder = AssetService.list(searchAssetFilter, filterPager).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    if (result.results.getTotalCount() > 0) {
                        genreCallBack.getAllgenres(true, result);
                    } else {
                        genreCallBack.getAllgenres(false, result);
                    }
                }

//                else {
//                    genreCallBack.getAllgenres(false, result);
//                }

                else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("ksExipreGenre", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getGenre(callBack);
                                    } else {
                                        genreCallBack.getAllgenres(false, result);
                                    }
                                }
                            });

                        } else {
                            genreCallBack.getAllgenres(false, result);
                        }
                    } else {
                        genreCallBack.getAllgenres(false, result);
                    }
                    //channelCallBack.response(false, commonResponse);
                }
            }
        });

        getRequestQueue().queue(builder.build(client));


    }

    public void storeUserPrefrences(String value, UserPrefrencesCallBack userPrefrencesCallBack) {
        prefrencesCallBack = userPrefrencesCallBack;
        StringValue value1 = new StringValue();
        value1.setValue(value);

        PrintLogging.printLog("", "", "storedValueIs" + value1.getValue());

//        boolean isActive = new KsPreferenceKeys(activity).getUserActive();
        clientSetupKs();
        OttUserService.UpdateDynamicDataOttUserBuilder builder = OttUserService.updateDynamicData(AppLevelConstants.KEY_CONTENT_PREFRENCES, value1).setCompletion(result -> {
            PrintLogging.printLog("", "", "userPrefrenceSave" + result.isSuccess());
            if (result.isSuccess()) {
                prefrencesCallBack.response("true");
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreStoreUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    storeUserPrefrences(value, userPrefrencesCallBack);
                                } else {
                                    prefrencesCallBack.failure();
                                }
                            }
                        });

                    } else {
                        prefrencesCallBack.failure();
                    }
                } else {
                    prefrencesCallBack.failure();
                }
                //channelCallBack.response(false, commonResponse);
            }


        });
        getRequestQueue().queue(builder.build(client));
    }

    public void updateParentalControl(String parentalStatus, UserPrefrencesCallBack userPrefrencesCallBack) {
        prefrencesCallBack = userPrefrencesCallBack;
        StringValue value1 = new StringValue();
        value1.setValue(parentalStatus);
        clientSetupKs();
        OttUserService.UpdateDynamicDataOttUserBuilder builder = OttUserService.updateDynamicData(AppLevelConstants.KEY_CONTENT_PREFRENCES_F_PARENTAL, value1).setCompletion(result -> {
            if (result.isSuccess()) {
                prefrencesCallBack.response("true");
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(response -> {
                            if (response.getStatus()) {
                                updateParentalControl(parentalStatus, userPrefrencesCallBack);
                            } else {
                                prefrencesCallBack.failure();
                            }
                        });

                    } else {
                        prefrencesCallBack.failure();
                    }
                } else {
                    prefrencesCallBack.failure();
                }
            }


        });
        getRequestQueue().queue(builder.build(client));
    }

    public void callBookMarking(Asset asset, BookmarkingPositionCallBack callBack) {
        bookmarkingPositionCallBack = callBack;
        clientSetupKs();

        BookmarkFilter bookmarkFilter = new BookmarkFilter();
        bookmarkFilter.assetIdIn(asset.getId() + "");
        bookmarkFilter.assetTypeEqual("MEDIA");


        BookmarkService.ListBookmarkBuilder builder = BookmarkService.list(bookmarkFilter).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results.getTotalCount() > 0) {
                    if (result.results.getObjects().get(0).getPosition() == 0 || result.results.getObjects().get(0).getFinishedWatching()) {
                        bookmarkingPositionCallBack.position(0);
                    } else {
                        bookmarkingPositionCallBack.position(result.results.getObjects().get(0).getPosition());
                    }
                } else {
                    bookmarkingPositionCallBack.position(0);
                }

            } else {


                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    callBookMarking(asset, callBack);
                                    //getSubCategories(context, subCategoryCallBack);
                                } else {
                                    bookmarkingPositionCallBack.position(0);
                                }
                            }
                        });
                    else {
                        bookmarkingPositionCallBack.position(0);
                    }
                } else {
                    bookmarkingPositionCallBack.position(0);
                }


            }
        });
        getRequestQueue().queue(builder.build(client));
    }


    public void getEpisodeProgress(String assetList, EpisodeProgressCallback callBack) {
        clientSetupKs();

        BookmarkFilter bookmarkFilter = new BookmarkFilter();
        bookmarkFilter.assetIdIn(assetList);
        bookmarkFilter.assetTypeEqual("MEDIA");
        BookmarkService.ListBookmarkBuilder builder = BookmarkService.list(bookmarkFilter).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results.getTotalCount() > 0) {
                    if (result.results.getObjects() != null) {
                        callBack.getEpisodeProgressList(result.results.getObjects());
                    } else {
                        callBack.getEpisodeProgressList(null);
                    }
                } else {
                    callBack.getEpisodeProgressList(null);
                }

            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getEpisodeProgress(assetList, callBack);
                                    //getSubCategories(context, subCategoryCallBack);
                                } else {
                                    callBack.getEpisodeProgressList(null);
                                }
                            }
                        });
                    else {
                        callBack.getEpisodeProgressList(null);
                    }
                } else {
                    callBack.getEpisodeProgressList(null);
                }


            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void updateHouseHoldDevice(String udid, HouseholdDevice householdDevice, final UpdateDeviceCallBack callBack) {
        clientSetupKs();
        updateDeviceCallBack = callBack;

        HouseholdDeviceService.UpdateHouseholdDeviceBuilder builder = HouseholdDeviceService.update(udid, householdDevice).setCompletion(new OnCompletion<Response<HouseholdDevice>>() {
            @Override
            public void onComplete(Response<HouseholdDevice> result) {

                PrintLogging.printLog(this.getClass(), "", "update device" + result.isSuccess());

                if (result.isSuccess()) {
                    if (result.results != null) {
                        updateDeviceCallBack.updateStatus(true, "");
                    } else {
                        updateDeviceCallBack.updateStatus(true, "");
                    }
                } else {


                    if (result.error != null) {

                        String errorCode = result.error.getCode();

                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        updateHouseHoldDevice(udid, householdDevice, callBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        updateDeviceCallBack.updateStatus(false, result.error.getMessage());
                                    }
                                }
                            });
                        else {
                            updateDeviceCallBack.updateStatus(false, result.error.getMessage());
                        }
                    } else {
                        updateDeviceCallBack.updateStatus(false, result.error.getMessage());
                    }


                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void signUp(String userName, String password, SignUpCallBack callBack) {
        clientSetupKs();
        this.signUpCallBack = callBack;

        OTTUser ottUser = new OTTUser();
        ottUser.setUsername(userName);


        StringValue stringValue = new StringValue();
        stringValue.setValue("Mobile");
        stringValue.setDescription("");

        StringValue parentalStringValue = new StringValue();
        parentalStringValue.setValue("Active");
        parentalStringValue.setDescription("");


        Map<String, StringValue> paramsStringValue = new HashMap<>();
        paramsStringValue.put("userClass", stringValue);
        paramsStringValue.put(AppLevelConstants.KEY_CONTENT_PREFRENCES_F_PARENTAL, parentalStringValue);
        ottUser.setDynamicData(paramsStringValue);

        OttUserService.RegisterOttUserBuilder builder = OttUserService.register(AppLevelConstants.PARTNER_ID, ottUser, password).setCompletion(result -> {

            if (result.isSuccess()) {
                signUpCallBack.signupStatus(true, "Successfully Registered", null);
            } else {
                if (result.error != null) {
                    signUpCallBack.signupStatus(false, result.error.getMessage(), result.error);
                } else {
                    signUpCallBack.signupStatus(false, activity.getResources().getString(R.string.something_went_wrong), null);
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void addHouseHold(HouseHoldAddCallBack callBack) {
        loginClient(KsPreferenceKey.getInstance(activity).getStartSessionKs());
        this.houseHoldAddCallBack = callBack;
        Runnable runnable = () -> {
            Household household = new Household();
            if (KsPreferenceKey.getInstance(activity).getUser().getUsername() != null) {
                household.setName(KsPreferenceKey.getInstance(activity).getUser().getUsername());
                household.setDescription(KsPreferenceKey.getInstance(activity).getUser().getUsername() + " " + "HouseHoldDescription");
            }
            HouseholdService.AddHouseholdBuilder builder = HouseholdService.add(household).setCompletion(result -> {
                if (result.isSuccess()) {
                    productPrice(houseHoldAddCallBack);
                } else {
                    if (result.error != null) {
                        houseHoldAddCallBack.houseHoldAddStatus(false, result.error.getMessage());
                    } else {
                        houseHoldAddCallBack.houseHoldAddStatus(false, activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

            });

            getRequestQueue().queue(builder.build(client));
        };
        new Thread(runnable).start();

    }

    private void productPrice(HouseHoldAddCallBack houseHoldAddCallBack) {
        loginClient(KsPreferenceKey.getInstance(activity).getStartSessionKs());
        ProductPriceFilter productPriceFilter = new ProductPriceFilter();
        productPriceFilter.setIsLowest(true);
        productPriceFilter.setSubscriptionIdIn(KsPreferenceKey.getInstance(activity).getDefaultEntitlement());

        ProductPriceService.ListProductPriceBuilder builder = ProductPriceService.list(productPriceFilter).setCompletion(result -> {


            if (result.isSuccess()) {

                if (result.results.getObjects() != null && result.results.getObjects().size() > 0) {
                    if (result.results.getObjects().get(0).getPurchaseStatus().toString().equalsIgnoreCase(AppLevelConstants.FOR_PURCHASE)) {

                        paymentGateway(houseHoldAddCallBack, result.results.getObjects().get(0).getPrice().getAmount(), result.results.getObjects().get(0).getPrice().getCurrency());

                    } else if (result.results.getObjects().get(0).getPurchaseStatus().toString().equalsIgnoreCase(AppLevelConstants.SUBSCRIPTION_PURCHASE)) {

                        houseHoldAddCallBack.houseHoldAddStatus(true, "");

                    }
                } else {

                    houseHoldAddCallBack.houseHoldAddStatus(false, activity.getResources().getString(R.string.something_went_wrong));
                }
            } else {
                if (result.error != null) {
                    houseHoldAddCallBack.houseHoldAddStatus(false, result.error.getMessage());
                } else {
                    houseHoldAddCallBack.houseHoldAddStatus(false, activity.getResources().getString(R.string.something_went_wrong));
                }
            }
        });


        getRequestQueue().queue(builder.build(client));
    }

    public void callLogoutApi(LogoutCallBack callBack) {
        clientSetupKs();
        OttUserService.LogoutOttUserBuilder builder = OttUserService.logout().setCompletion(result -> {
            if (result.isSuccess()) {
                callBack.logoutStatus(true, "");

            } else {
                callBack.logoutStatus(false, "");

            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void callLogoutApi() {
        clientSetupKs();
        OttUserService.LogoutOttUserBuilder builder = OttUserService.logout().setCompletion(result -> {
            if (result.isSuccess()) {

            } else {

            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    private void paymentGateway(HouseHoldAddCallBack houseHoldAddCallBack, Double amount, String currency) {
        loginClient(KsPreferenceKey.getInstance(activity).getStartSessionKs());
        HouseholdPaymentGatewayService.SetChargeIDHouseholdPaymentGatewayBuilder builder = HouseholdPaymentGatewayService.setChargeID(AppLevelConstants.PAYMENT_GATEWAY_EXTERNAL_ID, KsPreferenceKey.getInstance(activity).getUser().getUsername()).setCompletion(result -> {
            if (result.isSuccess()) {
                purchaseSubscription(houseHoldAddCallBack, amount, currency);
            } else {
                if (result.error != null) {
                    houseHoldAddCallBack.houseHoldAddStatus(false, result.error.getMessage());
                } else {
                    houseHoldAddCallBack.houseHoldAddStatus(false, activity.getResources().getString(R.string.something_went_wrong));
                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    private void purchaseSubscription(HouseHoldAddCallBack houseHoldAddCallBack, Double amount, String currency) {
        loginClient(KsPreferenceKey.getInstance(activity).getStartSessionKs());
        Purchase purchase = new Purchase();
        purchase.setProductId(Integer.parseInt(KsPreferenceKey.getInstance(activity).getDefaultEntitlement()));
        purchase.setPrice(amount);
        purchase.setCurrency(currency);
        purchase.setProductType(TransactionType.SUBSCRIPTION);

        TransactionService.PurchaseTransactionBuilder builder = TransactionService.purchase(purchase).setCompletion(result -> {

            if (result.isSuccess()) {

                houseHoldAddCallBack.houseHoldAddStatus(true, "");
            } else {
                if (result.error != null) {
                    houseHoldAddCallBack.houseHoldAddStatus(false, result.error.getMessage());
                } else {
                    houseHoldAddCallBack.houseHoldAddStatus(false, activity.getResources().getString(R.string.something_went_wrong));
                }
            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void getmPin(String msisdn, Context context, OtpCallback otpCallback) {
        ApiInterface endpoint = RequestConfig.getOTPClient(BuildConfig.SMS_BASE_URL).create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("number", msisdn);
        Call<OtpModel> call = endpoint.sendOTP(jsonObject);
        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(@NonNull Call<OtpModel> otpModel, @NonNull retrofit2.Response<OtpModel> response) {
                Log.e("Response Code", String.valueOf(response.code()));
                if (response.code() == 201 || response.code() == 200) {
                    try {
                        DecodedJWT decodedJWT = AppCommonMethods.jwtVerification(response.body().getToken());
                        String txnId = decodedJWT.getClaim("txnId").asString();

                        OtpModel otpModel1 = response.body();
                        if (txnId != null) {
                            otpModel1.setTxnId(txnId);
                            otpCallback.smsReceived(otpModel1);
                        } else {
                            otpCallback.smsError(new Throwable("Something went Wrong... Please Try again."), 1);
                        }
                    } catch (Exception e) {
                        PrintLogging.printLog(this.getClass(), "", "Exception" + e.getMessage());
                    }

                } else {
                    otpCallback.smsFailure(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<OtpModel> call, @NonNull Throwable t) {
                // otpCallback.smsError(t);
                otpCallback.smsError(new Throwable("Something went Wrong... Please Try again."), 2);
            }
        });
    }

    public void verifyPin(Context context, String userName, String otp, String txnId, Boolean isUserVerified, OtpVerificationCallback otpVerificationCallback) {


        StringBuilderHolder.getInstance().clear();
        StringBuilderHolder.getInstance().append(AppLevelConstants.MOBILE);
        StringBuilderHolder.getInstance().append("_");
        StringBuilderHolder.getInstance().append(userName);
        StringBuilderHolder.getInstance().append("_");

        String android_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        StringBuilderHolder.getInstance().append(android_id);

        String udid = StringBuilderHolder.getInstance().getText().toString();


        String url = BuildConfig.VERIFY_BASE_URL;
        final String transactionId = txnId;
        ApiInterface endpoint = RequestConfig.getVerifyOTPClient(url).create(ApiInterface.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("number", userName);
        jsonObject.addProperty("otp", otp);
        jsonObject.addProperty("txnId", txnId);
        jsonObject.addProperty("udid", udid);
        jsonObject.addProperty("validateOTPOnly", isUserVerified);
        Call<OtpModel> call = endpoint.verifyPin(jsonObject);
        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(@NonNull Call<OtpModel> otpModel, @NonNull retrofit2.Response<OtpModel> response) {
                if (response.code() == 200) {
                    DecodedJWT decodedJWT = AppCommonMethods.jwtVerification(response.body().getToken());
                    String txnId = decodedJWT.getClaim("txnId").asString();
                    if (!isUserVerified) {
                        boolean isUserRegistered = decodedJWT.getClaim("isRegistered").asBoolean();
                        Gson gson = new Gson();
                        JsonElement jsonElement = gson.toJsonTree(decodedJWT.getClaim("result").asMap());
                        LoginResponse loginSession = gson.fromJson(jsonElement, LoginResponse.class);
                        KsPreferenceKey.getInstance(context).setUser(loginSession.getUser());
                        KsPreferenceKey.getInstance(context).setStartSessionKs(loginSession.getLoginSession().getKs());
                        KsPreferenceKey.getInstance(context).setUserRegistered(isUserRegistered);
                    }
                    if (txnId != null && txnId.equals(transactionId)) {
                        otpVerificationCallback.otpVerified(response.body());
                    } else {
                        otpVerificationCallback.onError(new Throwable("Something went Wrong... Please Try again."));
                    }
                } else {
                    otpVerificationCallback.otpUnauthorized(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<OtpModel> call, @NonNull Throwable t) {
                otpVerificationCallback.onError(t);
            }
        });
    }


    public void callChannelList(Context context, final int type, final int channelType, ChannelCallBack callBack) {
        channelCallBack = callBack;
        final CommonResponse commonResponse = new CommonResponse();
        clientSetupKs();
        Runnable runnable = () -> {
            long tabID = AppCommonMethods.getChannelID(context, channelType);
            OttCategoryService.GetOttCategoryBuilder builder = OttCategoryService.get((int) tabID).setCompletion(result -> {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getChannels() != null) {
                            if (result.results.getChannels().size() > 0) {
                                List<Channel> channelList = result.results.getChannels();
                                if (channelList.size() > 0) {
                                    commonResponse.setChannelList(channelList);
                                    channelCallBack.response(true, commonResponse);
                                } else {
                                    channelCallBack.response(false, commonResponse);
                                }
                            } else {
                                channelCallBack.response(false, commonResponse);
                            }
                        } else {
                            channelCallBack.response(false, commonResponse);
                        }

                    } else {
                        channelCallBack.response(false, commonResponse);
                    }

                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callChannelList(context, type, channelType, callBack);
                                    } else {
                                        channelCallBack.response(false, commonResponse);
                                    }
                                }
                            });

                        } else {
                            channelCallBack.response(false, commonResponse);
                        }
                    } else {
                        channelCallBack.response(false, commonResponse);
                    }
                }
            });
            getRequestQueue().queue(builder.build(client));
        };
        new Thread(runnable).start();
    }


    public void callChannelRail(int i2, long channelID, List<VIUChannel> dtChannelList, int counter, HomechannelCallBack callBack) {
        homechannelCallBack = callBack;

        if (dtChannelList.size() != counter) {

            listAssetBuilders = new ArrayList<>();
            responseList = new ArrayList<>();
            int size = dtChannelList.size();

            boolean injected = checkContinueWatching(dtChannelList);
            if (!injected) {
                KsPreferenceKey.getInstance(activity).setContinueWatchingIndex(-1);
            }

            boolean status = UserInfo.getInstance(activity).isActive();
            if (status) {
                if (size == 1) {
                    if (dtChannelList.get(counter).getDescription().equals(AppLevelConstants.KEY_CONTINUE_WATCHING)) {
                        continueWatchingIndex = counter;

                        callContinueWatchingAPI(responseList, dtChannelList, homechannelCallBack);

                    } else {
                        getRailData(channelID, dtChannelList, counter, homechannelCallBack);
                    }
                } else {
                    if (dtChannelList.get(counter).getDescription().equals(AppLevelConstants.KEY_CONTINUE_WATCHING)) {
                        continueWatchingIndex = counter;

                        callContinueWatchingAPI(responseList, dtChannelList, homechannelCallBack);
                    } else {
                        getRailData(channelID, dtChannelList, counter, homechannelCallBack);
                    }
                }

            } else {
                continueWatchingIndex = -1;
                KsPreferenceKey.getInstance(activity).setContinueWatchingIndex(continueWatchingIndex);
                getRailData(channelID, dtChannelList, counter, homechannelCallBack);
            }

        }

    }

    private boolean checkContinueWatching(List<VIUChannel> list) {
        boolean injected = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDescription().equalsIgnoreCase(AppLevelConstants.KEY_CONTINUE_WATCHING)) {
                injected = true;
            }

        }
        return injected;
    }

    private void getRailData(long channelID, List<VIUChannel> list, int counter, HomechannelCallBack homechannelCallBack) {
        boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            PrintLogging.printLog("getRailData", "isDFP" + list.get(counter).getDescription());
            if (list.get(counter).getDescription().contains(Layouts.HRO.name())) {
                try {
                    if (list.get(counter).getImageSource().equalsIgnoreCase(ImageSource.AST.name())) {
                        if (list.get(counter).isProgram()) {
                            getAssetDetailEpg(list.get(counter), list);
                        } else {
                            getAssetDetailMedia(list.get(counter), list);
                        }
                    } else if (list.get(counter).getImageSource().equalsIgnoreCase(ImageSource.MNL.name())) {
                        if (list.get(counter).getLandingPageType().equalsIgnoreCase(LandingPageType.AST.name())) {
                            if (list.get(counter).isProgram()) {
                                getAssetDetailEpg(list.get(counter), list);
                            } else {
                                getAssetDetailMedia(list.get(counter), list);
                            }
                        } else
                            homechannelCallBack.response(true, responseList, list);

                    } else {
                        homechannelCallBack.response(true, responseList, list);
                    }
                } catch (Exception e) {
                    PrintLogging.printLog("getRailData", "isDFP" + +channelID + "-->>" + list.get(counter).getDescription());
                }
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_FB_FANIOS)) {
                homechannelCallBack.response(false, null, null);
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_FB_FANWEB)) {
                homechannelCallBack.response(false, null, null);
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_FB_FAN)) {
                homechannelCallBack.response(false, responseList, list);
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_FB_FANTABLET)) {
                homechannelCallBack.response(true, responseList, list);
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_MY_WATCHLIST)) {
                if (UserInfo.getInstance(activity).isActive()) {
                    getWatchlistRails(list.get(counter), list);
                } else {
                    homechannelCallBack.response(false, null, null);
                }
            } else if (list.get(counter).getDescription().contains(AppLevelConstants.PPV_RAIL)) {
                if (UserInfo.getInstance(activity).isActive()) {
                    getPurchaseList(list);
                } else {
                    homechannelCallBack.response(false, null, null);
                }
            } else if (list.get(counter).getDescription().contains(AppLevelConstants.LIVECHANNEL_RAIL)) {
                if (!list.get(counter).getCustomLinearAssetId().equalsIgnoreCase("")) {
                    getLinearAssetId(list.get(counter).getCustomLinearAssetId(), list, counter);
                } else {
                    homechannelCallBack.response(false, null, null);
                }
            } else if (list.get(counter).getDescription().contains(AppLevelConstants.TRENDING)) {
                getTrending(responseList, list, counter);
            } else if (!StringUtils.isNullOrEmptyOrZero(list.get(counter).getDescription()) && list.get(counter).getDescription().toUpperCase().contains(AppConstants.KEY_DFP_ADS)) {
                try {
                    if (UserInfo.getInstance(activity).isVip()) {
                        homechannelCallBack.response(false, null, null);
                    } else {
                        homechannelCallBack.response(true, responseList, list);

                    }
                } catch (Exception e) {
                    PrintLogging.printLog("getRailData", "isDFP" + +channelID + "-->>" + list.get(counter).getDescription());

                }

            } else {
                //anonymousksSetup(new KsPreferenceKeys(activity).getAnonymousks());
                clientSetupKs();
                PrintLogging.printLog("", "idsPrint" + +channelID + "-->>" + list.get(counter).getName() + "-->>" + list.get(counter).getDescription());
                int iid = (int) channelID;
                ChannelFilter channelFilter = new ChannelFilter();
                channelFilter.setIdEqual(iid);

                VIUChannel channel = list.get(counter);

                FilterPager filterPager = new FilterPager();
                filterPager.setPageIndex(1);
                filterPager.setPageSize(channel.getContentSize());
                // filterPager.setPageSize(20);
                AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
                    @Override
                    public void onComplete(Response<ListResponse<Asset>> result) {
                        if (result.isSuccess()) {
                            if (result.results != null) {
                                if (result.results.getObjects() != null) {
                                    if (result.results.getObjects().size() > 0) {
                                        responseList.add(result);
                                        homechannelCallBack.response(true, responseList, list);
                                    } else {
                                        homechannelCallBack.response(false, null, null);
                                    }
                                } else {
                                    homechannelCallBack.response(false, null, null);
                                }
                            } else {
                                homechannelCallBack.response(false, null, null);
                            }
                        } else {

                            if (result.error != null) {
                                String errorCode = result.error.getCode();
                                PrintLogging.printLog("", "errorCodess-->>" + errorCode);
                                if (errorCode.equalsIgnoreCase(AppConstants.KS_EXPIRE)) {
                                    new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                        @Override
                                        public void response(CommonResponse response) {
                                            if (response.getStatus()) {
                                                getRailData(channelID, list, counter, homechannelCallBack);
                                            } else {
                                                homechannelCallBack.response(false, null, null);
                                            }
                                        }
                                    });

                                } else {
                                    homechannelCallBack.response(false, null, null);
                                }
                            } else {
                                homechannelCallBack.response(false, null, null);
                            }

                        }
                    }
                });
                getRequestQueue().queue(builder.build(client));
            }
        } else {
            PrintLogging.printLog("getRailData", "isDFP" + list.get(counter).getDescription());

            if (list.get(counter).getDescription().contains(Layouts.HRO.name())) {
                try {

                    if (list.get(counter).getImageSource().equalsIgnoreCase(ImageSource.AST.name())) {
                        if (list.get(counter).isProgram()) {
                            getAssetDetailEpg(list.get(counter), list);
                        } else {
                            getAssetDetailMedia(list.get(counter), list);
                        }
                    } else if (list.get(counter).getImageSource().equalsIgnoreCase(ImageSource.MNL.name())) {
                        if (list.get(counter).getLandingPageType().equalsIgnoreCase(LandingPageType.AST.name())) {
                            if (list.get(counter).isProgram()) {
                                getAssetDetailEpg(list.get(counter), list);
                            } else {
                                getAssetDetailMedia(list.get(counter), list);
                            }
                        } else
                            homechannelCallBack.response(true, responseList, list);

                    } else {
                        homechannelCallBack.response(true, responseList, list);
                    }

                } catch (Exception e) {
                    PrintLogging.printLog("getRailData", "isDFP" + +channelID + "-->>" + list.get(counter).getDescription());
                }
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_FB_FANIOS)) {
                homechannelCallBack.response(false, null, null);
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_FB_FANWEB)) {
                homechannelCallBack.response(false, null, null);
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_FB_FAN)) {
                homechannelCallBack.response(true, responseList, list);
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_FB_FANTABLET)) {
                homechannelCallBack.response(false, responseList, list);
            } else if (!StringUtils.isNullOrEmptyOrZero(list.get(counter).getDescription()) && list.get(counter).getDescription().toUpperCase().contains(AppConstants.KEY_DFP_ADS)) {
                try {
                    if (UserInfo.getInstance(activity).isVip()) {
                        homechannelCallBack.response(false, null, null);
                    } else {
                        homechannelCallBack.response(true, responseList, list);

                    }
                } catch (Exception e) {
                    PrintLogging.printLog("getRailData + KEY_PHONE", "isDFP" + +channelID + "-->>" + list.get(counter).getDescription());

                }
            } else if (list.get(counter).getDescription().contains(AppConstants.CAROUSEL_CUSTOM)) {
                homechannelCallBack.response(false, responseList, list);
            } else if (list.get(counter).getDescription().contains(AppConstants.KEY_MY_WATCHLIST)) {
                if (UserInfo.getInstance(activity).isActive()) {
                    getWatchlistRails(list.get(counter), list);
                } else {
                    homechannelCallBack.response(false, null, null);
                }
            } else if (list.get(counter).getDescription().contains(AppLevelConstants.PPV_RAIL)) {
                if (UserInfo.getInstance(activity).isActive()) {
                    getPurchaseList(list);
                } else {
                    homechannelCallBack.response(false, null, null);
                }
            } else if (list.get(counter).getDescription().contains(AppLevelConstants.LIVECHANNEL_RAIL)) {
                if (!list.get(counter).getCustomLinearAssetId().equalsIgnoreCase("")) {
                    getLinearAssetId(list.get(counter).getCustomLinearAssetId(), list, counter);
                } else {
                    homechannelCallBack.response(false, null, null);
                }
            } else if (list.get(counter).getDescription().contains(AppLevelConstants.TRENDING)) {
                getTrending(responseList, list, counter);
            } else {
                clientSetupKs();
                PrintLogging.printLog("", "idsPrint" + +channelID + "-->>" + list.get(counter).getName() + "-->>" + list.get(counter).getDescription());
                int iid = (int) channelID;
                ChannelFilter channelFilter = new ChannelFilter();
                channelFilter.setIdEqual(iid);
                VIUChannel channel = list.get(counter);
                FilterPager filterPager = new FilterPager();
                filterPager.setPageIndex(1);
                //filterPager.setPageSize(20);
                filterPager.setPageSize(channel.getContentSize());
                AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
                    @Override
                    public void onComplete(Response<ListResponse<Asset>> result) {
                        if (result.isSuccess()) {
                            if (result.results != null) {
                                if (result.results.getObjects() != null) {
                                    if (result.results.getObjects().size() > 0) {
                                        responseList.add(result);
                                        homechannelCallBack.response(true, responseList, list);
                                    } else {
                                        homechannelCallBack.response(false, null, null);
                                    }
                                } else {
                                    homechannelCallBack.response(false, null, null);
                                }
                            } else {
                                homechannelCallBack.response(false, null, null);
                            }
                        } else {
                            if (result.error != null) {
                                String errorCode = result.error.getCode();
                                PrintLogging.printLog("", "errorCodess-->>" + errorCode);
                                if (errorCode.equalsIgnoreCase(AppConstants.KS_EXPIRE)) {
                                    new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                        @Override
                                        public void response(CommonResponse response) {
                                            if (response.getStatus()) {
                                                getRailData(channelID, list, counter, homechannelCallBack);
                                            } else {
                                                homechannelCallBack.response(false, null, null);
                                            }
                                        }
                                    });

                                } else {
                                    homechannelCallBack.response(false, null, null);
                                }
                            } else {
                                homechannelCallBack.response(false, null, null);
                            }
                        }
                    }
                });
                getRequestQueue().queue(builder.build(client));
            }
        }


    }

    private void getPurchaseList(List<VIUChannel> list) {

        clientSetupKs();
        EntitlementFilter entitlementFilter = new EntitlementFilter();
        entitlementFilter.productTypeEqual("subscription");
        entitlementFilter.entityReferenceEqual("household");
        entitlementFilter.isExpiredEqual(false + "");
        entitlementFilter.orderBy("PURCHASE_DATE_ASC");


        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        EntitlementService.ListEntitlementBuilder builder = EntitlementService.list(entitlementFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Entitlement>>>() {
            @Override
            public void onComplete(Response<ListResponse<Entitlement>> result) {
                if (result.isSuccess()) {
                    if (result.results != null & result.results.getObjects() != null) {
                        getSubscriptionInfo(result.results.getObjects(), list);
                    } else {
                        homechannelCallBack.response(false, null, null);
                    }
                } else {


                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getPurchaseList(list);
                                    } else {
                                        homechannelCallBack.response(false, null, null);
                                    }
                                }
                            });

                        } else {
                            homechannelCallBack.response(false, null, null);
                        }
                    } else {
                        homechannelCallBack.response(false, null, null);
                    }


                }
            }
        });
        getRequestQueue().queue(builder.build(client));

    }

    public void getPurchaseListListing(int counter, TrendingCallBack trendingCallBack) {

        clientSetupKs();
        EntitlementFilter entitlementFilter = new EntitlementFilter();
        entitlementFilter.productTypeEqual("subscription");
        entitlementFilter.entityReferenceEqual("household");
        entitlementFilter.isExpiredEqual(false + "");
        entitlementFilter.orderBy("PURCHASE_DATE_ASC");


        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        EntitlementService.ListEntitlementBuilder builder = EntitlementService.list(entitlementFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Entitlement>>>() {
            @Override
            public void onComplete(Response<ListResponse<Entitlement>> result) {
                if (result.isSuccess()) {
                    if (result.results != null & result.results.getObjects() != null) {
                        getSubscriptionInfoListing(result.results.getObjects(), trendingCallBack);
                    } else {
                        trendingCallBack.getList(false, null, 0);

                    }
                } else {


                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getPurchaseListListing(counter, trendingCallBack);
                                    } else {
                                        trendingCallBack.getList(false, null, 0);

                                    }
                                }
                            });

                        } else {
                            trendingCallBack.getList(false, null, 0);

                        }
                    } else {
                        trendingCallBack.getList(false, null, 0);

                    }


                }
            }
        });
        getRequestQueue().queue(builder.build(client));

    }

    private void getSubscriptionInfo(List<Entitlement> objects, List<VIUChannel> list) {
        String id = AppCommonMethods.getsubScriptionIds(objects);
        clientSetupKs();

        SubscriptionFilter subscriptionFilter = new SubscriptionFilter();
        subscriptionFilter.subscriptionIdIn(id);

        SubscriptionService.ListSubscriptionBuilder builder = SubscriptionService.list(subscriptionFilter).setCompletion(new OnCompletion<Response<ListResponse<Subscription>>>() {
            @Override
            public void onComplete(Response<ListResponse<Subscription>> result) {

                if (result.isSuccess()) {
                    if (result.results != null && result.results.getObjects() != null && result.results.getObjects().size() > 0 && result.results.getObjects().get(0) != null && result.results.getObjects().get(0).getChannels() != null && result.results.getObjects().get(0).getChannels().get(0) != null && result.results.getObjects().get(0).getChannels().get(0).getId() != null) {
                        getChannelData(result.results.getObjects().get(0).getChannels().get(0).getId() + "", list);
                    } else {
                        homechannelCallBack.response(false, null, null);
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getSubscriptionInfo(objects, list);
                                    } else {
                                        homechannelCallBack.response(false, null, null);
                                    }
                                }
                            });

                        } else {
                            homechannelCallBack.response(false, null, null);
                        }
                    } else {
                        homechannelCallBack.response(false, null, null);
                    }
                }

            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    private void getSubscriptionInfoListing(List<Entitlement> objects, TrendingCallBack trendingCallBack) {
        String id = AppCommonMethods.getsubScriptionIds(objects);
        clientSetupKs();

        SubscriptionFilter subscriptionFilter = new SubscriptionFilter();
        subscriptionFilter.subscriptionIdIn(id);

        SubscriptionService.ListSubscriptionBuilder builder = SubscriptionService.list(subscriptionFilter).setCompletion(new OnCompletion<Response<ListResponse<Subscription>>>() {
            @Override
            public void onComplete(Response<ListResponse<Subscription>> result) {

                if (result.isSuccess()) {
                    if (result.results != null && result.results.getObjects() != null && result.results.getObjects().size() > 0 && result.results.getObjects().get(0) != null && result.results.getObjects().get(0).getChannels() != null && result.results.getObjects().get(0).getChannels().get(0) != null && result.results.getObjects().get(0).getChannels().get(0).getId() != null) {
                        getChannelDataListing(result.results.getObjects().get(0).getChannels().get(0).getId() + "", trendingCallBack);
                    } else {
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getSubscriptionInfoListing(objects, trendingCallBack);
                                    } else {
                                        trendingCallBack.getList(false, null, 0);

                                    }
                                }
                            });

                        } else {
                            trendingCallBack.getList(false, null, 0);

                        }
                    } else {
                        trendingCallBack.getList(false, null, 0);

                    }
                }

            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    private void getChannelData(String id, List<VIUChannel> list) {

        clientSetupKs();
        ChannelFilter channelFilter = new ChannelFilter();
        channelFilter.setIdEqual(Integer.valueOf(id));

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                if (result.isSuccess()) {
                    responseList.add(result);
                    homechannelCallBack.response(true, responseList, list);
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getChannelData(id, list);
                                    } else {
                                        homechannelCallBack.response(false, null, null);
                                    }
                                }
                            });

                        } else {
                            homechannelCallBack.response(false, null, null);
                        }
                    } else {
                        homechannelCallBack.response(false, null, null);
                    }
                }
            }
        });
        getRequestQueue().queue(builder.build(client));

    }

    private void getChannelDataListing(String id, TrendingCallBack trendingCallBack) {

        clientSetupKs();
        ChannelFilter channelFilter = new ChannelFilter();
        channelFilter.setIdEqual(Integer.valueOf(id));

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                if (result.isSuccess() && result.results != null && result.results.getObjects() != null) {
                    trendingCallBack.getList(true, result.results.getObjects(), result.results.getTotalCount());
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getChannelDataListing(id, trendingCallBack);
                                    } else {
                                        trendingCallBack.getList(false, null, 0);
                                    }
                                }
                            });

                        } else {
                            trendingCallBack.getList(false, null, 0);

                        }
                    } else {
                        trendingCallBack.getList(false, null, 0);

                    }
                }
            }
        });
        getRequestQueue().queue(builder.build(client));

    }

    public void getWatchlistRails(VIUChannel channel, List<VIUChannel> list) {

        boolean isLogin = UserInfo.getInstance(activity).isActive();
        if (isLogin) {
            clientSetupKs();
            KsServices ksServices = new KsServices(activity);
            ksServices.compareWatchlist(1, new WatchlistCallBack() {
                @Override
                public void getWatchlistDetail(Boolean status, String errorCode, Response<ListResponse<PersonalList>> result) {
                    if (status) {
                        ksServices.getWatchlist(getAssetId(result.results.getObjects()), new AllWatchlistCallBack() {
                            @Override
                            public void getAllWatchlistDetail(Boolean status, String errorCode, String message, List<Response<ListResponse<Asset>>> listResponseResponse) {
                                if (status) {
                                    PrintLogging.printLog("", "getAllWatchlistDetail" + listResponseResponse);
                                    if (listResponseResponse != null && listResponseResponse.size() > 0) {
                                        responseList.add(listResponseResponse.get(0));
                                        homechannelCallBack.response(true, responseList, list);
                                    } else {
                                        homechannelCallBack.response(false, null, null);
                                    }

                                } else {
                                    homechannelCallBack.response(false, null, null);
                                }
                            }
                        });
                    } else {

                        homechannelCallBack.response(false, null, null);
                    }
                }
            });
        } else {
            homechannelCallBack.response(false, null, null);
        }


    }

    private String getAssetId(List<PersonalList> personalLists) {
        int size = personalLists.size();
        String value = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String ksql = personalLists.get(i).getKsql();
            builder.append(ksql).append(",");
        }
        value = builder.toString();
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }


    public void getAssetDetailEpg(VIUChannel channel, List<VIUChannel> list) {

        clientSetupKs();
        String assetId = channel.getManualImageAssetId();
        final MutableLiveData<RailCommonData> mutableLiveData = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(activity);
        AssetService.GetAssetBuilder builder = AssetService.get(assetId, AssetReferenceType.EPG_INTERNAL).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results != null) {
                    channel.setAsset(result.results);
                    homechannelCallBack.response(true, responseList, list);

                } else {

                }
            } else {
                /*ErrorHandling.checkErrorType(result.error, (code, status) -> {
                    if (code.equalsIgnoreCase(AppConstants.KS_EXPIRE) && status) {
                        homechannelCallBack.response(false, null, null);
                    } else {
                        homechannelCallBack.response(false, null, null);

                    }
                });*/
            }

        });
        getRequestQueue().queue(builder.build(client));
    }

    public void getAssetDetailMedia(VIUChannel channel, List<VIUChannel> list) {
        String assetId = "";
        if (StringUtils.isNullOrEmptyOrZero(channel.getLandingPageAssetId()))
            assetId = channel.getManualImageAssetId();
        else
            assetId = channel.getLandingPageAssetId();


        clientSetupKs();

        AssetService.GetAssetBuilder builder = AssetService.get(assetId, AssetReferenceType.MEDIA).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results != null) {
                    channel.setAsset(result.results);
                    homechannelCallBack.response(true, responseList, list);

                } else {
                }
            } else {
               /* ErrorHandling.checkErrorType(result.error, (code, status) -> {
                    if (code.equalsIgnoreCase(AppConstants.KS_EXPIRE) && status) {
                        homechannelCallBack.response(false, null, null);
                    } else {
                        homechannelCallBack.response(false, null, null);

                    }
                });*/
            }

        });
        getRequestQueue().queue(builder.build(client));
    }


    public void simillarMovieListing(String genreSkl, int assetType, int id, int counter, SimilarMovieCallBack callBack) {
        similarMovieCallBack = callBack;
        CommonResponse commonResponse = new CommonResponse();
        clientSetupKs();
        RelatedFilter relatedFilter = new RelatedFilter();
        String one = "(and asset_type='";
        String two = "' (or " + genreSkl + "))";

        String third = String.valueOf(assetType);
        relatedFilter.setKSql(one + third + two);

        relatedFilter.setIdEqual(id);
        relatedFilter.orderBy("NAME_ASC");
        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results != null) {
                    if (result.results.getObjects() != null) {
                        if (result.results.getObjects().size() > 0) {
                            commonResponse.setStatus(true);
                            commonResponse.setAssetList(result);
                            similarMovieCallBack.response(true, commonResponse);
                        } else {
                            similarMovieCallBack.response(false, commonResponse);
                        }
                    } else {
                        similarMovieCallBack.response(false, commonResponse);
                    }
                } else {
                    similarMovieCallBack.response(false, commonResponse);
                }
            } else {
                //similarMovieCallBack.response(false, commonResponse);

                if (result.error != null) {

                    String errorCode = result.error.getCode();
                    Log.e("errorCodessSimiler", errorCode + "");
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    simillarMovieListing(genreSkl, assetType, id, counter, callBack);
                                    //getSubCategories(context, subCategoryCallBack);
                                } else {
                                    similarMovieCallBack.response(false, commonResponse);
                                }
                            }
                        });
                    else {
                        similarMovieCallBack.response(false, commonResponse);
                    }
                } else {
                    similarMovieCallBack.response(false, commonResponse);
                }

            }
        });

        getRequestQueue().queue(builder.build(client));

    }


    public void similarMovieListing(String genreSkl, final int assetType, final int id, int counter, int listingType, HomechannelCallBack callBack) {
        clientSetupKs();
        homechannelCallBack = callBack;
        movieList = new ArrayList<>();
        RelatedFilter relatedFilter = new RelatedFilter();
        relatedFilter.setIdEqual(id);
        if (listingType == 1) {
            String ksql = KSQL.forsimillarMovie(assetType);
            relatedFilter.setKSql(ksql);
        } else {
            relatedFilter.orderBy("NAME_ASC");
            String ksql = KSQL.foryouMayLikeMovie(genreSkl, assetType);
            relatedFilter.setKSql(ksql);
        }

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {
            // Log.w("similarListing", result.isSuccess() + "");
            if (result.isSuccess() == true) {
                movieList.add(result);
                homechannelCallBack.response(true, movieList, null);
            } else {
                homechannelCallBack.response(false, movieList, null);
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void youMayAlsoLikeListing(String genreSkl, final int assetType, final int id, int counter, SimilarMovieCallBack callBack) {
        similarMovieCallBack = callBack;
        clientSetupKs();
        final CommonResponse commonResponse = new CommonResponse();
        RelatedFilter relatedFilter = new RelatedFilter();
        String one = "(and asset_type='";
        String two = "')";
        String third = String.valueOf(assetType);
        relatedFilter.setKSql(one + third + two);
        relatedFilter.setIdEqual(id);
        if (UserInfo.getInstance(activity).isActive())
            relatedFilter.excludeWatched("true");

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(18);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results != null) {
                    if (result.results.getObjects() != null) {
                        if (result.results.getObjects().size() > 0) {
                            commonResponse.setStatus(true);
                            commonResponse.setAssetList(result);
                            similarMovieCallBack.response(true, commonResponse);
                        } else {
                            similarMovieCallBack.response(false, commonResponse);
                        }
                    } else {
                        similarMovieCallBack.response(false, commonResponse);
                    }
                } else {
                    similarMovieCallBack.response(false, commonResponse);
                }
            } else {
                if (result.error != null) {

                    String errorCode = result.error.getCode();
                    Log.e("errorCodess cat", errorCode + "");
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    youMayAlsoLikeListing(genreSkl, assetType, id, counter, callBack);
                                    //getSubCategories(context, subCategoryCallBack);
                                } else {
                                    similarMovieCallBack.response(false, commonResponse);
                                }
                            }
                        });
                    else {
                        similarMovieCallBack.response(false, commonResponse);
                    }
                } else {
                    similarMovieCallBack.response(false, commonResponse);
                }


            }
        });

        getRequestQueue().queue(builder.build(client));

    }


    public void getNotificationSetting(CommonCallBack commonCallBack) {
        clientSetupKs();

        CommonResponse commonResponse = new CommonResponse();
        NotificationsSettingsService.GetNotificationsSettingsBuilder builder = NotificationsSettingsService.get().setCompletion(result -> {


            if (result.isSuccess()) {

                commonResponse.setMessage("");
                commonResponse.setNotification(result.results.getPushNotificationEnabled());
                commonCallBack.response(true, commonResponse);
            }
//            else {
//
//                if (result.error != null) {
//                    commonResponse.setMessage(result.error.getMessage());
//                } else {
//                    commonResponse.setMessage(activity.getResources().getString(R.string.something_went_wrong));
//                }
//
//                commonCallBack.response(false, commonResponse);
//            }

            else {
                if (result.error != null) {

                    String errorCode = result.error.getCode();
                    Log.e("errorCodess", errorCode + "");
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getNotificationSetting(commonCallBack);
                                } else {
                                    commonResponse.setMessage(activity.getResources().getString(R.string.something_went_wrong));
                                }
                            }
                        });
                    else {
                        commonCallBack.response(false, commonResponse);
                    }
                } else {
                    commonCallBack.response(false, commonResponse);
                }
                //channelCallBack.response(false, commonResponse);
            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void getNotificationSettingUpdate(boolean ischecked, ApiCallBack apiCallBack) {
        clientSetupKs();

        NotificationsSettings notificationsSettings = new NotificationsSettings();
        notificationsSettings.setPushNotificationEnabled(ischecked);
        NotificationsSettingsService.UpdateNotificationsSettingsBuilder builder = NotificationsSettingsService.update(notificationsSettings).setCompletion(result -> {

            if (result.isSuccess()) {
                apiCallBack.response(true, "");
            } else {
                if (result.error != null) {
                    apiCallBack.response(false, result.error.getMessage());
                } else {
                    apiCallBack.response(false, activity.getResources().getString(R.string.something_went_wrong));
                }

            }
        });

        getRequestQueue().queue(builder.build(client));

    }

    public void getMsisdn(AutoMsisdnCallback autoMsisdnCallback) {
        String url = BuildConfig.AUTO_MSISDN_MOCK;
        ApiInterface endpoint = RequestConfig.getClient(url).create(ApiInterface.class);
        Call<OtpModel> call = endpoint.getMsisdn();
        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(@NonNull Call<OtpModel> call, @NonNull retrofit2.Response<OtpModel> response) {
                if (response.code() == 200) {
                    autoMsisdnCallback.msisdnReceived(response.body());
                } else {
                    autoMsisdnCallback.msisdnFailure(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<OtpModel> call, @NonNull Throwable t) {
                autoMsisdnCallback.onError(t);
            }
        });
    }

    public void getSubCategories(Context context, SubCategoryCallBack subCategoryCallBack) {
        try {
            String rootCategory = KsPreferenceKey.getInstance(context).getRoot();
            clientSetupKs();
            OttCategoryService.GetOttCategoryBuilder builder = OttCategoryService.get(Integer.parseInt(rootCategory)).setCompletion(result -> {
                if (result.isSuccess()) {
                    List<OTTCategory> childCategories = result.results.getChildCategories();
                    if (childCategories.size() > 0) {
                        for (OTTCategory childCategory :
                                childCategories) {
                            switch (childCategory.getName()) {
                                case AppConstants.TAB_HOME_KEY:
                                case AppConstants.TAB_HOME_KEY_IGNORE:
                                    SubCategoriesPrefs.getInstance(context).setHomeTabId(childCategory.getId().intValue());
                                    Log.e("HOME TAB ID", String.valueOf(SubCategoriesPrefs.getInstance(context).getHomeTabId()));
                                    break;
                                case AppConstants.TAB_LIVE_TV_KEY:
                                    SubCategoriesPrefs.getInstance(context).setLiveTabId(childCategory.getId().intValue());
                                    break;
                                case AppConstants.TAB_VIDEO_KEY:
                                    SubCategoriesPrefs.getInstance(context).setVideoTabId(childCategory.getId().intValue());
                                    break;
                                case AppConstants.TAB_MOVIE_DETAILS_KEY:
                                    SubCategoriesPrefs.getInstance(context).setMovieDetailsId(childCategory.getId().intValue());
                                    break;
                                case AppConstants.TAB_SHORT_FILM_DETAILS_KEY:
                                    SubCategoriesPrefs.getInstance(context).setShortVideoDetailsId(childCategory.getId().intValue());
                                    break;
                                case AppConstants.TAB_DRAMA_DETAILS_KEY:
                                    SubCategoriesPrefs.getInstance(context).setDramaDetailsId(childCategory.getId().intValue());
                                    break;
                                case AppConstants.TAB_DRAMA_EPISODE_KEY:
                                    SubCategoriesPrefs.getInstance(context).setDramaEpisodeDetailsId(childCategory.getId().intValue());
                                    break;
                                case AppConstants.TAB_LIVE_TV_DETAILS_KEY:
                                    SubCategoriesPrefs.getInstance(context).setLiveTvDetailsId(childCategory.getId().intValue());
                                    break;
                                case AppConstants.TAB_POPULAR_SEARCH_KEY:
                                    SubCategoriesPrefs.getInstance(context).setPopularSearchId(childCategory.getId().intValue());
                                    break;
                                case AppConstants.TAB_FORWARDED_EPG_DETAIL_KEY:
                                    SubCategoriesPrefs.getInstance(context).setForwardEpgId(childCategory.getId().intValue());
                                    break;
                            }
                        }
                        subCategoryCallBack.subCategorySuccess();
                    } else {
                        subCategoryCallBack.subCategoryFailure();
                    }
                } else {
                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        Log.e("errorCodess cat", errorCode + "");
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        subCategoryCallBack.subCategoryFailure();
                                    }
                                }
                            });
                        else {
                            subCategoryCallBack.subCategoryFailure();
                        }
                    } else {
                        subCategoryCallBack.subCategoryFailure();
                    }
                    //channelCallBack.response(false, commonResponse);
                }
            });

            getRequestQueue().queue(builder.build(client));
        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "", "Exception" + e.getMessage());
        }
    }

    public void refreshStartSessionKs(SharedPrefHelper session, KsStartSessionCallBack callBack) {
        try {
            this.ksStartSessionCallBack = callBack;
            String token = KsPreferenceKey.getInstance(activity).getToken();
            String ksValue = KsPreferenceKey.getInstance(activity).getAnonymousks();
            final String idValue = KsPreferenceKey.getInstance(activity).getTokenId();
            getFbKeyHash(activity, ksValue + token);

            startSessionClient(ksValue);
            String udid = AppCommonMethods.callpreference(activity).getUdid();

            int expiryDate = getTokenExpiryDate();


            AppTokenService.StartSessionAppTokenBuilder builder = AppTokenService
                    .startSession(idValue, keyHash, "", expiryDate, udid).setCompletion(result -> {
                        // Log.w("responseeestart", result.isSuccess() + "");

                        if (result.isSuccess()) {
                            ksStartSessionCallBack.success(true, result);
                        } else {
                            // ksStartSessionCallBack.failure(false, result);

                            callLogoutApi();
                            UserInfo.getInstance(activity).setActive(false);
                            KsPreferenceKey.getInstance(activity).setUser(null);
                            KsPreferenceKey.getInstance(activity).setStartSessionKs("");
                            KsPreferenceKey.getInstance(activity).setMsisdn("");
                            Intent intent = new Intent(activity, HomeActivity.class);
                            TaskStackBuilder.create(activity).addNextIntentWithParentStack(intent).startActivities();

                        }
                    });
            getRequestQueue().queue(builder.build(client));
        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "", "Exception" + e.getMessage());
        }

    }

    private int getTokenExpiryDate() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        currentDate.add(Calendar.YEAR, 2);
        return (int) (currentDate.getTimeInMillis() / 1000);
    }


    private void startSessionClient(String ks) {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(activity);
        Configuration config = new Configuration();
        config.setConnectTimeout(30000);
        if (responseDmsModel != null) {
            config.setEndpoint(responseDmsModel.getParams().getApiProxyUrlKaltura());
        } else {
            config.setEndpoint(AppConstants.END_POINT);
        }
        client = new Client(config);
        // client.setKs(ks);
        client.setKs(KsPreferenceKey.getInstance(activity).getAnonymousks());

    }

    public void getOttUserDetails(OttUserDetailsCallBack ottUserDetailsCallBack) {
        clientSetupKs();
        OttUserService.GetOttUserBuilder builder = OttUserService.get().setCompletion(result -> {
            if (result.isSuccess()) {
                String userParentalDetails = AssetContent.getUserPreferenceForParental(result.results.getDynamicData());
                if (userParentalDetails == null)
                    ottUserDetailsCallBack.onUserParentalDetailsNotFound();
                else
                    ottUserDetailsCallBack.onSuccess(userParentalDetails);
            } else {
                if (result.error != null) {
                    if (result.error.getCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        getOttUserDetails(ottUserDetailsCallBack);
                    } else {
                        ottUserDetailsCallBack.onFailure(result.error);
                    }
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void validatePin(String pinText, CommonResponseCallBack commonResponseCallBack) {
        clientSetupKs();
        PinService.ValidatePinBuilder builder = PinService.validate(pinText, PinType.PARENTAL).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    commonResponseCallBack.onSuccess();
                } else {
                    if (result.error != null) {
                        if (result.error.getCode().equals(AppLevelConstants.KS_EXPIRE)) {
                            validatePin(pinText, commonResponseCallBack);
                        } else {
                            commonResponseCallBack.onFailure(result.error);
                        }
                    }
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void setPin(String pinText, CommonResponseCallBack commonResponseCallBack) {
        clientSetupKs();

        Pin pin = new Pin();
        pin.setPin(pinText);
        pin.setOrigin(RuleLevel.USER);
        pin.setType(PinType.PARENTAL);

        PinService.UpdatePinBuilder builder = PinService.update(EntityReferenceBy.USER, PinType.PARENTAL, pin).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results != null) {
                    commonResponseCallBack.onSuccess();
                }
            } else {
                if (result.error != null) {
                    if (result.error.getCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        setPin(pinText, commonResponseCallBack);
                    } else {
                        commonResponseCallBack.onFailure(result.error);
                    }
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void enableParental(CommonResponseCallBack commonResponseCallBack) {
        clientSetupKs();
        ParentalRuleService.EnableParentalRuleBuilder builder = ParentalRuleService.enable(MediaTypeConstant.getDefaultParentalRule(activity), EntityReferenceBy.USER).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    commonResponseCallBack.onSuccess();
                } else {
                    if (result.error != null) {
                        if (result.error.getCode().equals(AppLevelConstants.KS_EXPIRE)) {
                            enableParental(commonResponseCallBack);
                        } else {
                            commonResponseCallBack.onFailure(result.error);
                        }
                    }
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void disableParental(CommonResponseCallBack commonResponseCallBack) {
        clientSetupKs();
        ParentalRuleService.DisableParentalRuleBuilder builder = ParentalRuleService.disable(MediaTypeConstant.getDefaultParentalRule(activity), EntityReferenceBy.USER).setCompletion(result -> {
            if (result != null) {
                if (result.isSuccess()) {
                    commonResponseCallBack.onSuccess();
                } else {
                    if (result.error != null) {
                        if (result.error.getCode().equals(AppLevelConstants.KS_EXPIRE)) {
                            enableParental(commonResponseCallBack);
                        } else {
                            commonResponseCallBack.onFailure(result.error);
                        }
                    }
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void saveDTVAccount(String dtvAccount, DTVCallBack dtvCallBack1) {
        dtvCallBack = dtvCallBack1;
        StringValue value1 = new StringValue();
        value1.setValue(dtvAccount);
        value1.setDescription(AppLevelConstants.DTV_ACCOUNT_DESCRIPTION);

        Log.d("dtvValueIs", dtvAccount);
        Log.d("dtvValueIs", AppLevelConstants.DTV_ACCOUNT_DESCRIPTION);

        PrintLogging.printLog("", "", "storedValueIs" + value1.getValue());

//        boolean isActive = new KsPreferenceKeys(activity).getUserActive();
        clientSetupKs();

        OttUserService.UpdateDynamicDataOttUserBuilder builder = OttUserService.updateDynamicData(AppLevelConstants.DTV_ACCOUNT, value1).setCompletion(result -> {
            PrintLogging.printLog("", "", "userPrefrenceSave" + result.isSuccess());
            if (result.isSuccess()) {
                dtvCallBack.response("true");
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreStoreUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    saveDTVAccount(dtvAccount, dtvCallBack1);
                                } else {
                                    dtvCallBack.failure();
                                }
                            }
                        });

                    } else {
                        dtvCallBack.failure();
                    }
                } else {
                    dtvCallBack.failure();
                }
                //channelCallBack.response(false, commonResponse);
            }


        });
        getRequestQueue().queue(builder.build(client));
    }


    public void saveDTVAccountData(String dtvAccount, DTVCallBack dtvCallBack1) {
        dtvCallBack = dtvCallBack1;
        StringValue value1 = new StringValue();
        value1.setValue(dtvAccount);
        value1.setDescription(AppLevelConstants.DTV_ACCOUNT_DESCRIPTION);

        Log.d("dtvValueIs", dtvAccount);
        Log.d("dtvValueIs", AppLevelConstants.DTV_ACCOUNT_DESCRIPTION);

        PrintLogging.printLog("", "", "storedValueIs" + value1.getValue());

//        boolean isActive = new KsPreferenceKeys(activity).getUserActive();
        SetupKs();

        OttUserService.UpdateDynamicDataOttUserBuilder builder = OttUserService.updateDynamicData(AppLevelConstants.DTV_ACCOUNT, value1).setCompletion(result -> {
            PrintLogging.printLog("", "", "userPrefrenceSave" + result.isSuccess());
            if (result.isSuccess()) {
                dtvCallBack.response("true");
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreStoreUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    saveDTVAccountData(dtvAccount, dtvCallBack1);
                                } else {
                                    dtvCallBack.failure();
                                }
                            }
                        });

                    } else {
                        dtvCallBack.failure();
                    }
                } else {
                    dtvCallBack.failure();
                }
                //channelCallBack.response(false, commonResponse);
            }


        });
        getRequestQueue().queue(builder.build(client));
    }

    private void SetupKs() {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(activity);
        Configuration config = new Configuration();
        config.setParam("timestamp", System.currentTimeMillis());
        config.setConnectTimeout(30000);
        if (responseDmsModel != null) {
            config.setEndpoint(responseDmsModel.getParams().getApiProxyUrlKaltura());
        } else {
            config.setEndpoint(AppConstants.END_POINT);
        }
        client = new Client(config);

        client.setKs(KsPreferenceKey.getInstance(activity).getStartSessionKs());

    }


    public void callProgramAsset(String assetId, SpecificAssetCallBack callBack) {
        specificAssetCallBack = callBack;
        //anonymousksSetup(new KsPreferenceKeys(activity).getAnonymousks());
        clientSetupKs();
        AssetService.GetAssetBuilder builder = AssetService.get(assetId, AssetReferenceType.EPG_INTERNAL).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results != null) {
                    specificAssetCallBack.getAsset(true, result.results);
                } else {
                    specificAssetCallBack.getAsset(false, result.results);
                }
            } else {
//                ErrorHandling.checkErrorType(result.error, (code, status) -> {
//                    if (code.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE) && status) {
//                        callSpecificAsset(assetId, specificAssetCallBack);
//                    } else {
//                        specificAssetCallBack.getAsset(false, result.results);
//                    }
//                });

                if (result.error != null) {
                    if (result.error.getCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        callSpecificAsset(assetId, specificAssetCallBack);
                    } else {
                        specificAssetCallBack.getAsset(false, result.results);
                    }
                }
            }

        });
        getRequestQueue().queue(builder.build(client));
    }

    // get DTVContactInfo
    public void getDtvAccountDetails(String dtvAccountNumber, DTVAccountCallback dtvAccountCallback) {
        // ApiInterface endpoint = RequestConfig.getClient(BuildConfig.VERIFY_ACCOUNT_BASE_URL).create(ApiInterface.class);
        ApiInterface endpoint = RequestConfig.getDTVClient(AppConstants.CERT_GET_DTV_INFO).create(ApiInterface.class);
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("dtvAccNo", Integer.parseInt(dtvAccountNumber));
            Call<DTVContactInfoModel> call = endpoint.getDTVContactInfo(jsonObject);
            call.enqueue(new Callback<DTVContactInfoModel>() {
                @Override
                public void onResponse(Call<DTVContactInfoModel> call, retrofit2.Response<DTVContactInfoModel> response) {
                    Log.e("Response Code", String.valueOf(response.code()));
                    if (response.code() == 200) {
                        dtvAccountCallback.dtvSuccess(response.body());
                    } else {
                        dtvAccountCallback.dtvFailure(response);
                    }
                }

                @Override
                public void onFailure(Call<DTVContactInfoModel> call, Throwable t) {
                    dtvAccountCallback.dtvError(t);
                }
            });
        } catch (NumberFormatException e) {
            PrintLogging.printLog(this.getClass(), "", "Exception" + e.getMessage());
        }

    }

    //get Hungama URL
    public void getHungamaUrl(String providerExternalContentId, Context context, HungamaResponse hungamaResponse) {
        // ApiInterface endpoint = RequestConfig.getClient(BuildConfig.VERIFY_ACCOUNT_BASE_URL).create(ApiInterface.class);
        ApiInterface endpoint = RequestConfig.getClient("https://api.salthungama.com/sngapi/").create(ApiInterface.class);
        try {

            Call<JsonElement> call = endpoint.getHungama(providerExternalContentId, KsPreferenceKey.getInstance(context).getUser().getId(), "DIALOGVIU", "b487d5aeae88b09ec25fb0eb18cf6615", UDID.getDeviceId(context, context.getContentResolver()));
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(response.body().toString());
                            hungamaResponse.onSuccess(json.get("stream_url").toString());

                        } catch (JSONException e) {
                            hungamaResponse.onFailureFailure();
                        }

                    } else {
                        hungamaResponse.onFailureFailure();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    hungamaResponse.onError(t);
                }
            });

        } catch (NumberFormatException e) {
            PrintLogging.printLog(this.getClass(), "", "Exception" + e.getMessage());
        }
    }

    public void getDtvAccountList(Context context, DtvListCallBack callBack) {
        dtvListCallBack = callBack;
//        boolean isActive = new KsPreferenceKeys(activity).getUserActive();
        clientSetupKs();

        OttUserService.GetOttUserBuilder builder = OttUserService.get().setCompletion(result -> {
            if (result.isSuccess()) {
                AssetContent.getUserTypeForDialogAndNonDialogUser(result.results.getDynamicData(), context);
                if (AssetContent.getDtvAccountDetail(result.results.getDynamicData()) == null) {
                    dtvListCallBack.response("");
                } else {
                    dtvListCallBack.response(AssetContent.getDtvAccountDetail(result.results.getDynamicData()));

                }
            }

//            else {
//                prefrencesCallBack.response("");
//            }

            else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreCheckUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getDtvAccountList(context, callBack);
                                } else {
                                    dtvListCallBack.failure();
                                }
                            }
                        });

                    } else {
                        dtvListCallBack.failure();
                    }
                } else {
                    dtvListCallBack.failure();
                }
                //channelCallBack.response(false, commonResponse);
            }

        });

        getRequestQueue().queue(builder.build(client));
    }

    public void callSubscriptionPackageListApi(String id, SubscriptionResponseCallBack callBack) {
        clientSetupKs();

        try {

            subscriptionResponseCallBack = callBack;

            SubscriptionFilter subscriptionFilter = new SubscriptionFilter();
            subscriptionFilter.setMediaFileIdEqual(Integer.parseInt(id));

            SubscriptionService.ListSubscriptionBuilder builder = SubscriptionService.list(subscriptionFilter).setCompletion(new OnCompletion<Response<ListResponse<Subscription>>>() {
                @Override
                public void onComplete(Response<ListResponse<Subscription>> result) {

                    if (result.isSuccess()) {
                        if (result.results != null) {
                            subscriptionResponseCallBack.response(true, "", "", result.results.getObjects());
                        } else {
                            subscriptionResponseCallBack.response(false, "", "", result.results.getObjects());
                        }
                    } else {
                        if (result.error != null) {
                            String errorCode = result.error.getCode();
                            Log.e("errorCodessName", errorCode);
                            if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                                new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                    @Override
                                    public void response(CommonResponse response) {
                                        if (response.getStatus()) {
                                            callSubscriptionPackageListApi(id, callBack);
                                        } else {
                                            subscriptionResponseCallBack.response(false, "", "", null);
                                        }
                                    }
                                });

                            } else {
                                subscriptionResponseCallBack.response(false, "", "", null);
                            }
                        } else {
                            subscriptionResponseCallBack.response(false, "", "", null);
                        }
                    }

                }
            });

            getRequestQueue().queue(builder.build(client));

        } catch (Exception e) {
        }
    }


    public void callViewChannelApi(String id, int counter, CommonCallBack callBack) {
        clientSetupKs();
        CommonResponse commonResponse = new CommonResponse();
        ksHouseHoldIdCallBack = callBack;
        ChannelFilter channelFilter = new ChannelFilter();
        channelFilter.idEqual(id);

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(25);


        AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result != null) {
                    commonResponse.setStatus(true);
                    commonResponse.setAssetList(result);
                    ksHouseHoldIdCallBack.response(true, commonResponse);
                } else {
                    ksHouseHoldIdCallBack.response(false, commonResponse);
                }
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("errorCodessName", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    callViewChannelApi(id, counter, callBack);
                                } else {
                                    ksHouseHoldIdCallBack.response(false, null);
                                }
                            }
                        });

                    } else {
                        ksHouseHoldIdCallBack.response(false, null);
                    }
                } else {
                    ksHouseHoldIdCallBack.response(false, null);
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }


    public void callEntitlementListApi(EntitlementResponseCallBack callBack) {
        clientSetupKs();
        entitlementResponseCallBack = callBack;

        EntitlementFilter entitlementFilter = new EntitlementFilter();
        entitlementFilter.productTypeEqual("subscription");
        entitlementFilter.entityReferenceEqual("user");


        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(10);

        EntitlementService.ListEntitlementBuilder builder = EntitlementService.list(entitlementFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Entitlement>>>() {
            @Override
            public void onComplete(Response<ListResponse<Entitlement>> result) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        entitlementResponseCallBack.response(true, "", "", result.results.getObjects());
                    } else {
                        entitlementResponseCallBack.response(false, "", "", result.results.getObjects());
                    }
                } else {


                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callEntitlementListApi(callBack);
                                    } else {
                                        entitlementResponseCallBack.response(false, "", "", null);
                                    }
                                }
                            });

                        } else {
                            entitlementResponseCallBack.response(false, "", "", null);
                        }
                    } else {
                        entitlementResponseCallBack.response(false, "", "", null);
                    }


                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void callMySubcriptionListApi(String id, SubscriptionResponseCallBack callBack) {
        clientSetupKs();
        subscriptionResponseCallBack = callBack;

        SubscriptionFilter subscriptionFilter = new SubscriptionFilter();
        subscriptionFilter.subscriptionIdIn(id);

        SubscriptionService.ListSubscriptionBuilder builder = SubscriptionService.list(subscriptionFilter).setCompletion(new OnCompletion<Response<ListResponse<Subscription>>>() {
            @Override
            public void onComplete(Response<ListResponse<Subscription>> result) {

                if (result.isSuccess()) {
                    if (result.results != null) {
                        subscriptionResponseCallBack.response(true, "", "", result.results.getObjects());
                    } else {
                        subscriptionResponseCallBack.response(false, "", "", result.results.getObjects());
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callSubscriptionPackageListApi(id, callBack);
                                    } else {
                                        subscriptionResponseCallBack.response(false, "", "", null);
                                    }
                                }
                            });

                        } else {
                            subscriptionResponseCallBack.response(false, "", "", null);
                        }
                    } else {
                        subscriptionResponseCallBack.response(false, "", "", null);
                    }
                }

            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void callHouseholdpaymentmethod(HouseholdpaymentResponseCallBack callBack) {
        clientSetupKs();
        householdpaymentResponseCallBack = callBack;

        HouseholdPaymentMethodService.ListHouseholdPaymentMethodBuilder builder = HouseholdPaymentMethodService.list().setCompletion(new OnCompletion<Response<ListResponse<HouseholdPaymentMethod>>>() {
            @Override
            public void onComplete(Response<ListResponse<HouseholdPaymentMethod>> result) {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        householdpaymentResponseCallBack.response(true, "", "", result.results.getObjects());
                    } else {
                        householdpaymentResponseCallBack.response(false, "", "", result.results.getObjects());
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callHouseholdpaymentmethod(callBack);
                                    } else {
                                        householdpaymentResponseCallBack.response(false, "", "", result.results.getObjects());
                                    }
                                }
                            });

                        } else {
                            householdpaymentResponseCallBack.response(false, "", "", result.results.getObjects());
                        }
                    } else {
                        householdpaymentResponseCallBack.response(false, "", "", result.results.getObjects());
                    }
                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }


    public void callCancelSubscriptionApi(String id, CancelRenewalResponseCallBack callBack) {
        clientSetupKs();
        cancelRenewalResponseCallBack = callBack;

        EntitlementService.CancelRenewalEntitlementBuilder builder = (EntitlementService.CancelRenewalEntitlementBuilder) EntitlementService.cancelRenewal(id).setCompletion(new OnCompletion<Response<Void>>() {
            @Override
            public void onComplete(Response<Void> result) {
                if (result.isSuccess()) {
                    cancelRenewalResponseCallBack.response(true, "", "");
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callCancelSubscriptionApi(id, callBack);
                                    } else {
                                        cancelRenewalResponseCallBack.response(false, result.error.getCode(), result.error.getMessage());
                                    }
                                }
                            });

                        } else {
                            cancelRenewalResponseCallBack.response(false, result.error.getCode(), result.error.getMessage());
                        }
                    } else {
                        cancelRenewalResponseCallBack.response(false, "", activity.getResources().getString(R.string.something_went_wrong));
                    }

                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }


    public void checkUserType(Context context) {
        clientSetupKs();

        OttUserService.GetOttUserBuilder builder = OttUserService.get().setCompletion(result -> {

            if (result.isSuccess()) {
                AssetContent.getUserTypeForDialogAndNonDialogUser(result.results.getDynamicData(), context);
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreCheckUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    checkUserType(context);
                                }
                            }
                        });

                    }
                }
            }

        });

        getRequestQueue().queue(builder.build(client));
    }


    public void saveMBBAccount(String dtvAccount, MBBAccountCallBack callBack) {
        mBBAccountCallBack = callBack;
        StringValue value1 = new StringValue();
        value1.setValue(dtvAccount);
        value1.setDescription(AppLevelConstants.MBB_ACCOUNT_DESCRIPTION);

        clientSetupKs();

        OttUserService.UpdateDynamicDataOttUserBuilder builder = OttUserService.updateDynamicData(AppLevelConstants.MBB_ACCOUNT, value1).setCompletion(result -> {
            PrintLogging.printLog("", "", "userPrefrenceSave" + result.isSuccess());
            if (result.isSuccess()) {
                mBBAccountCallBack.response("true");
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreStoreUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    saveMBBAccount(dtvAccount, callBack);
                                } else {
                                    mBBAccountCallBack.failure();
                                }
                            }
                        });

                    } else {
                        mBBAccountCallBack.failure();
                    }
                } else {
                    mBBAccountCallBack.failure();
                }
                //channelCallBack.response(false, commonResponse);
            }


        });
        getRequestQueue().queue(builder.build(client));
    }

    public void saveHBBAccount(String dtvAccount, HBBAccountCallBack callBack) {
        hbbAccountCallBack = callBack;
        StringValue value1 = new StringValue();
        value1.setValue(dtvAccount);
        value1.setDescription(AppLevelConstants.HBB_ACCOUNT_DESCRIPTION);

        clientSetupKs();

        OttUserService.UpdateDynamicDataOttUserBuilder builder = OttUserService.updateDynamicData(AppLevelConstants.HBB_ACCOUNT, value1).setCompletion(result -> {
            PrintLogging.printLog("", "", "userPrefrenceSave" + result.isSuccess());
            if (result.isSuccess()) {
                hbbAccountCallBack.response("true");
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreStoreUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    saveHBBAccount(dtvAccount, callBack);
                                } else {
                                    hbbAccountCallBack.failure();
                                }
                            }
                        });

                    } else {
                        hbbAccountCallBack.failure();
                    }
                } else {
                    hbbAccountCallBack.failure();
                }
                //channelCallBack.response(false, commonResponse);
            }


        });
        getRequestQueue().queue(builder.build(client));
    }

//    OttUserService.UpdateDynamicDataOttUserBuilder builder = OttUserService.updateDynamicData(AppLevelConstants.HBB_ACCOUNT, value1).setCompletion(result -> {

    public void updateDtvMbbHbbAccount(Context context, List<DtvMbbHbbModel> dtvMbbHbbModels, DTVCallBack dtvCallBack1) {
        SetupKs();
        dtvCallBack = dtvCallBack1;
        int arraySize = dtvMbbHbbModels.size();
        Collections.sort(dtvMbbHbbModels, new Comparator<DtvMbbHbbModel>() {
            @Override
            public int compare(DtvMbbHbbModel dtvMbbHbbModel, DtvMbbHbbModel t1) {
                return dtvMbbHbbModel.getKey().compareTo(t1.getKey());
            }
        });


        ottUserBuilders = new ArrayList<>();
        for (int i = 0; i < arraySize; i++) {
            StringValue stringValue = new StringValue();

            if (dtvMbbHbbModels.get(i).getKey().equalsIgnoreCase(AppLevelConstants.DTV)) {
                stringValue.setValue(dtvMbbHbbModels.get(i).getValue());
                stringValue.setDescription("DTV account");
            } else if (dtvMbbHbbModels.get(i).getKey().equalsIgnoreCase(AppLevelConstants.MBB)) {
                stringValue.setValue(dtvMbbHbbModels.get(i).getValue());
                stringValue.setDescription("MBB account");
            }


            OttUserService.UpdateDynamicDataOttUserBuilder builder = OttUserService.updateDynamicData(dtvMbbHbbModels.get(i).getKey(), stringValue).setCompletion(result -> {
                PrintLogging.printLog("", "", "userPrefrenceSave" + result.isSuccess());
                if (result.isSuccess()) {
                    if (result.results != null) {
                        dtvCallBack.response("true");
                    }
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        //  saveDTVAccount(dtvAccount, dtvCallBack1);
                                        updateDtvMbbHbbAccount(context, dtvMbbHbbModels, dtvCallBack1);
                                    } else {
                                        dtvCallBack.failure();
                                    }
                                }
                            });

                        } else {
                            dtvCallBack.failure();
                        }
                    } else {
                        dtvCallBack.failure();
                    }
                    //channelCallBack.response(false, commonResponse);
                }


            });
            ottUserBuilders.add(builder);
        }
        MultiRequestBuilder multiRequestBuilder = new MultiRequestBuilder();
        for (int j = 0; j < ottUserBuilders.size(); j++) {
            multiRequestBuilder = multiRequestBuilder.add(ottUserBuilders.get(j));
        }
        getRequestQueue().queue(multiRequestBuilder.build(client));

    }

    public void getMBBAccountList(MBBAccountListCallBack callBack) {
        mbbAccountListCallBack = callBack;
        clientSetupKs();

        OttUserService.GetOttUserBuilder builder = OttUserService.get().setCompletion(result -> {
            if (result.isSuccess()) {
                if (AssetContent.getMBBAccountDetail(result.results.getDynamicData()) == null)
                    mbbAccountListCallBack.response("");
                else
                    mbbAccountListCallBack.response(AssetContent.getMBBAccountDetail(result.results.getDynamicData()));
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreCheckUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getMBBAccountList(callBack);
                                } else {
                                    mbbAccountListCallBack.failure();
                                }
                            }
                        });

                    } else {
                        mbbAccountListCallBack.failure();
                    }
                } else {
                    mbbAccountListCallBack.failure();
                }
            }

        });

        getRequestQueue().queue(builder.build(client));
    }


    public void getBillPaymentsAccount(BillPaymentCallBack callBack) {
        billPaymentCallBack = callBack;

        clientSetupKs();

        OttUserService.GetOttUserBuilder builder = OttUserService.get().setCompletion(result -> {
            if (result.isSuccess()) {
                if (AssetContent.getMBBAccountDetail(result.results.getDynamicData()) == null)
                    billPaymentCallBack.response(null);
                else
                    billPaymentCallBack.response(AssetContent.getMBBDTVAccountDetail(result.results.getDynamicData()));
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreCheckUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    getBillPaymentsAccount(callBack);
                                } else {
                                    billPaymentCallBack.failure();
                                }
                            }
                        });

                    } else {
                        billPaymentCallBack.failure();
                    }
                } else {
                    billPaymentCallBack.failure();
                }
            }

        });

        getRequestQueue().queue(builder.build(client));

    }

    public void callPurchaseApi(String paymentMethodId, Context context, String productId, String currency, String price, PurchaseSubscriptionCallBack callBack) {
        this.purchaseSubscriptionCallBack = callBack;
        String adapterId = "";
        clientSetupKs();


        List<PaymentItemDetail> paymentItemDetails = AllChannelManager.getInstance().getPaymentItemDetails();
        for (int i = 0; i < paymentItemDetails.size(); i++) {
            if (paymentItemDetails.get(i).getPackageId().equalsIgnoreCase(productId)) {
                adapterId = paymentItemDetails.get(i).getAdapterId();
                break;
            }
        }


        Purchase purchase = new Purchase();
        purchase.setProductId(Integer.parseInt(productId));
        purchase.setProductType(TransactionType.SUBSCRIPTION);
        purchase.setCurrency(currency);
        purchase.setPrice(Double.parseDouble(price));
        purchase.setPaymentGatewayId(Integer.parseInt(KsPreferenceKey.getInstance(context).getATBpaymentGatewayId()));
        purchase.setPaymentMethodId(Integer.parseInt(paymentMethodId));
        purchase.setCoupon("");
        purchase.setAdapterData(adapterId);

        TransactionService.PurchaseTransactionBuilder builder = TransactionService.purchase(purchase).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results.getFailReasonCode() == 0) {
                    purchaseSubscriptionCallBack.response(true, "", "", result.results.getPaymentGatewayReferenceId());
                } else if (result.results.getFailReasonCode() == 20) {
                    purchaseSubscriptionCallBack.response(false, "", activity.getResources().getString(R.string.insufficient_balance), "");
                } else {
                    purchaseSubscriptionCallBack.response(false, "", activity.getResources().getString(R.string.something_went_wrong), "");
                }
            } else {
                if (result.error != null) {
                    String errorCode = result.error.getCode();
                    Log.e("ksExipreCheckUser", errorCode);
                    if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                            @Override
                            public void response(CommonResponse response) {
                                if (response.getStatus()) {
                                    callPurchaseApi(paymentMethodId, context, productId, currency, price, callBack);
                                } else {
                                    purchaseSubscriptionCallBack.response(false, "", result.error.getMessage(), "");
                                }
                            }
                        });

                    } else {
                        purchaseSubscriptionCallBack.response(false, result.error.getCode(), result.error.getMessage(), "");
                    }
                } else {
                    purchaseSubscriptionCallBack.response(false, "", activity.getResources().getString(R.string.something_went_wrong), "");
                }
            }
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void callInvokeApi(String accountType, String accountNumber, Context context, InvokeApiCallBack callBack) {
        this.invokeApiCallBack = callBack;
        clientSetupKsInvoke();
        List<KeyValue> keyValue = new ArrayList<>();
        KeyValue value = new KeyValue();
        value.setKey("AccountDomain");
        if (accountType.equalsIgnoreCase(AppLevelConstants.DIALOG_TV)) {
            value.setValue("DTV");
        } else {
            value.setValue("GSM");
        }
        keyValue.add(value);
        KeyValue value1 = new KeyValue();
        value1.setKey("paymentMethod");
        value1.setValue(accountNumber);
        keyValue.add(value1);


        HouseholdPaymentGatewayService.InvokeHouseholdPaymentGatewayBuilder builder = HouseholdPaymentGatewayService.invoke(Integer.parseInt(KsPreferenceKey.getInstance(context).getATBpaymentGatewayId()), "setPaymentMethod", keyValue).setCompletion(new OnCompletion<Response<PaymentGatewayConfiguration>>() {
            @Override
            public void onComplete(Response<PaymentGatewayConfiguration> result) {
                if (result.isSuccess()) {
                    invokeApiCallBack.result(true, "", "");
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("ksExipreCheckUser", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callInvokeApi(accountType, accountNumber, context, callBack);
                                    } else {
                                        invokeApiCallBack.result(false, result.error.getCode(), result.error.getMessage());
                                    }
                                }
                            });

                        } else {
                            invokeApiCallBack.result(false, result.error.getCode(), result.error.getMessage());
                        }
                    } else {
                        invokeApiCallBack.result(false, "", activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

            }
        });
        getRequestQueue().queue(builder.build(client));

    }

    private void clientSetupKsInvoke() {
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(activity);
        Configuration config = new Configuration();
        config.setParam("timestamp", System.currentTimeMillis());
        config.setConnectTimeout(30000);
        if (responseDmsModel != null) {
            if (BuildConfig.FLAVOR.equalsIgnoreCase("qa")) {
                config.setEndpoint(responseDmsModel.getParams().getGateways().getJsonGW().concat("/restful_v5_3_2"));
            } else {
                config.setEndpoint(responseDmsModel.getParams().getGateways().getJsonGW());
            }
        } else {
            config.setEndpoint(AppConstants.END_POINT);
        }
        client = new Client(config);
        if (new KsPreferenceKey(activity).getAppLangName().equalsIgnoreCase("ms")) {
            client.setLanguage("may");
        } else {
            client.setLanguage("en");
        }
        if (UserInfo.getInstance(activity).isActive()) {
            client.setKs(KsPreferenceKey.getInstance(activity).getStartSessionKs());
        } else {
            client.setKs(KsPreferenceKey.getInstance(activity).getAnonymousks());
        }
    }

    public void callRemoveApi(int id, Context context, RemovePaymentCallBack callBack) {
        this.removePaymentCallBack = callBack;
        clientSetupKs();
        HouseholdPaymentMethodService.RemoveHouseholdPaymentMethodBuilder builder = HouseholdPaymentMethodService.remove(Integer.parseInt(KsPreferenceKey.getInstance(context).getATBpaymentGatewayId()), id).setCompletion(new OnCompletion<Response<Boolean>>() {
            @Override
            public void onComplete(Response<Boolean> result) {
                if (result.isSuccess()) {
                    removePaymentCallBack.response(true, "", "");
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("ksExipreCheckUser", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        callRemoveApi(id, context, callBack);
                                    } else {
                                        removePaymentCallBack.response(false, result.error.getMessage(), result.error.getCode());
                                    }
                                }
                            });

                        } else {
                            removePaymentCallBack.response(false, result.error.getMessage(), result.error.getCode());
                        }
                    } else {
                        removePaymentCallBack.response(false, result.error.getMessage(), result.error.getCode());
                    }
                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void updatePaymentMethod(int id, int newPaymentMethodId, Context context, UpdatePaymentMethodCallBack callBack) {
        this.updatePaymentMethodCallBack = callBack;
        clientSetupKs();
        SubscriptionEntitlement subscriptionEntitlement = new SubscriptionEntitlement();
        subscriptionEntitlement.setPaymentGatewayId(Integer.parseInt(KsPreferenceKey.getInstance(context).getATBpaymentGatewayId()));
        subscriptionEntitlement.setPaymentMethodId(newPaymentMethodId);

        EntitlementService.UpdateEntitlementBuilder builder = EntitlementService.update(id, subscriptionEntitlement).setCompletion(new OnCompletion<Response<Entitlement>>() {
            @Override
            public void onComplete(Response<Entitlement> result) {
                if (result.isSuccess()) {
                    updatePaymentMethodCallBack.response(true, "", "");
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("ksExipreCheckUser", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        updatePaymentMethod(id, newPaymentMethodId, context, callBack);
                                    } else {
                                        updatePaymentMethodCallBack.response(false, result.error.getMessage(), result.error.getCode());
                                    }
                                }
                            });

                        } else {
                            updatePaymentMethodCallBack.response(false, result.error.getMessage(), result.error.getCode());
                        }
                    } else {
                        updatePaymentMethodCallBack.response(false, result.error.getMessage(), result.error.getCode());
                    }
                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void getAssetListForSubscription(String subscriptionOffer, SubscriptionAssetListResponse callBack) {
        this.subscriptionAssetListResponse = callBack;
        clientSetupKs();

        ChannelFilter channelFilter = new ChannelFilter();
        channelFilter.setIdEqual(Integer.valueOf(subscriptionOffer));

        // PersonalListSearchFilter

        // Sear

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(channelFilter, filterPager).setCompletion(new OnCompletion<Response<ListResponse<Asset>>>() {
            @Override
            public void onComplete(Response<ListResponse<Asset>> result) {
                if (result.isSuccess()) {
                    subscriptionAssetListResponse.response(true, "", result.results.getObjects());
                } else {
                    if (result.error != null) {
                        String errorCode = result.error.getCode();
                        Log.e("errorCodessName", errorCode);
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getAssetListForSubscription(subscriptionOffer, callBack);
                                    } else {
                                        subscriptionAssetListResponse.response(false, "", null);
                                    }
                                }
                            });

                        } else {
                            subscriptionAssetListResponse.response(false, "", null);
                        }
                    } else {
                        subscriptionAssetListResponse.response(false, "", null);
                    }
                }
            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    private DeleteFromFollowlistCallBack deleteFromFollowlistCallBack;

    public void removeCWAPI(Long assetID, DeleteFromFollowlistCallBack callBack) {
        deleteFromFollowlistCallBack = callBack;
        clientSetupKs();
        AssetHistoryFilter assetHistoryFilter = new AssetHistoryFilter();
        assetHistoryFilter.daysLessThanOrEqual("30");
        assetHistoryFilter.assetIdIn(String.valueOf(assetID));

        AssetHistoryService.CleanAssetHistoryBuilder builder = (AssetHistoryService.CleanAssetHistoryBuilder) AssetHistoryService.clean(assetHistoryFilter).setCompletion(result -> {
            if (result.isSuccess()) {
                PrintLogging.printLog("", "deleteResponse" + result.isSuccess());
                deleteFromFollowlistCallBack.deleteFollowlistDetail(true);
            } else {

            }
            // PrintLogging.printLog("","deleteResponse"+result.isSuccess());
        });

        getRequestQueue().queue(builder.build(client));
    }

    public void getEpisodeToPlay(Long assetID, NextEpisodeCallBack callBack) {
        clientSetupKs();

        AssetHistoryService.GetNextEpisodeAssetHistoryBuilder builder = AssetHistoryService.getNextEpisode(assetID).setCompletion(result -> {
            if (result.isSuccess() && result.results != null) {
                callBack.getNextEpisode(true, result.results);
            } else {
                callBack.getNextEpisode(false, null);
            }
        });
        getRequestQueue().queue(builder.build(client));
    }


    public void getTrending(List<Response<ListResponse<Asset>>> responseList, List<VIUChannel> list, int counter) {
        clientSetupKs();
        SearchAssetFilter relatedFilter = new SearchAssetFilter();
        String kSql = "";
        if (list.get(counter).getCustomMediaType() != null) {
            if (list.get(counter).getCustomMediaType().equalsIgnoreCase("EPISODES")) {
                relatedFilter.setTypeIn(MediaTypeConstant.getEpisode(activity) + "");
            } else if (list.get(counter).getCustomMediaType().equalsIgnoreCase("MOVIES")) {
                relatedFilter.setTypeIn(MediaTypeConstant.getMovie(activity) + "");
            } else if (!list.get(counter).getCustomMediaType().equalsIgnoreCase("")) {
                relatedFilter.setTypeIn(AppCommonMethods.getTypeIn(activity, list.get(counter).getCustomMediaType()));
            }
        }
        if (list.get(counter).getCustomGenre() != null) {
            if (list.get(counter).getCustomGenreRule() != null && !list.get(counter).getCustomGenreRule().equalsIgnoreCase("")) {
                kSql = AppCommonMethods.splitGenre(list.get(counter).getCustomGenre(), list.get(counter).getCustomGenreRule());

            } else {
                kSql = AppCommonMethods.splitGenre(list.get(counter).getCustomGenre(), "");
            }
        }
        if (!kSql.equalsIgnoreCase("")) {
            relatedFilter.setKSql(kSql);
        }
        relatedFilter.setOrderBy("VIEWS_DESC");

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {
            try {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            if (result.results.getObjects().size() > 0) {
                                responseList.add(result);
                                homechannelCallBack.response(true, null, list);
                            } else {
                                homechannelCallBack.response(false, null, null);
                            }
                        } else {
                            homechannelCallBack.response(false, null, null);
                        }
                    } else {
                        homechannelCallBack.response(false, null, null);
                    }
                } else {

                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getTrending(responseList, list, counter);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        homechannelCallBack.response(false, null, null);

                                    }
                                }
                            });
                        else {
                            homechannelCallBack.response(false, null, null);

                        }
                    } else {
                        homechannelCallBack.response(false, null, null);

                    }


                }
            } catch (Exception e) {
                PrintLogging.printLog(this.getClass(), "Exception", "" + e);

            }
        });
        getRequestQueue().queue(builder.build(client));
    }


    public void getTrendingListing(String customMediaType, String customGenre, String customGenreRule, int counter, TrendingCallBack trendingCallBack) {
        clientSetupKs();
        SearchAssetFilter relatedFilter = new SearchAssetFilter();
        String kSql = "";
        if (customMediaType != null && !customMediaType.equalsIgnoreCase("")) {
            if (customMediaType.equalsIgnoreCase("EPISODES")) {
                relatedFilter.setTypeIn(MediaTypeConstant.getEpisode(activity) + "");
            } else if (customMediaType.equalsIgnoreCase("MOVIES")) {
                relatedFilter.setTypeIn(MediaTypeConstant.getMovie(activity) + "");
            }
        }
        if (customGenre != null && !customGenre.equalsIgnoreCase("")) {
            if (customGenreRule != null && !customGenreRule.equalsIgnoreCase("")) {
                kSql = AppCommonMethods.splitGenre(customGenre, customGenreRule);
            } else {
                kSql = AppCommonMethods.splitGenre(customGenre, "");

            }
        }
        if (!kSql.equalsIgnoreCase("")) {
            relatedFilter.setKSql(kSql);
        }
        relatedFilter.setOrderBy("VIEWS_DESC");

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(counter);
        filterPager.setPageSize(20);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {
            try {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        List<Asset> s = result.results.getObjects();

                        if (result.results.getObjects() != null && result.results.getObjects().size() > 0) {
                            trendingCallBack.getList(true, result.results.getObjects(), result.results.getTotalCount());
                        } else {
                            trendingCallBack.getList(false, null, 0);

                        }
                    } else {
                        trendingCallBack.getList(false, null, 0);

                    }

                } else {

                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getTrendingListing(customMediaType, customGenre, customGenreRule, counter, trendingCallBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        trendingCallBack.getList(false, null, 0);

                                    }
                                }
                            });
                        else {
                            trendingCallBack.getList(false, null, 0);


                        }
                    } else {
                        trendingCallBack.getList(false, null, 0);


                    }


                }
            } catch (Exception e) {
                PrintLogging.printLog(this.getClass(), "Exception", "" + e);

            }
        });

        getRequestQueue().queue(builder.build(client));

    }


    public void getAssetListBasedOnMediaType(Context context, int mediatype, final HomechannelCallBack callBack) {
        responseList = new ArrayList<Response<ListResponse<Asset>>>();
        homechannelCallBack = callBack;
        clientSetupKs();
        SearchAssetFilter relatedFilter = new SearchAssetFilter();
        relatedFilter.setTypeIn(String.valueOf(mediatype));

        FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(100);

        AssetService.ListAssetBuilder builder = AssetService.list(relatedFilter, filterPager).setCompletion(result -> {
            try {
                if (result.isSuccess()) {
                    if (result.results != null) {
                        if (result.results.getObjects() != null) {
                            if (result.results.getObjects().size() > 0) {
                                responseList.add(result);
                                homechannelCallBack.response(true, responseList, null);
                            } else {
                                responseList.add(null);
                                homechannelCallBack.response(false, responseList, null);
                            }
                        } else {
                            responseList.add(null);
                            homechannelCallBack.response(false, responseList, null);
                        }
                    } else {
                        responseList.add(null);
                        homechannelCallBack.response(false, responseList, null);
                    }
                } else {

                    if (result.error != null) {

                        String errorCode = result.error.getCode();
                        if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE))
                            new RefreshKS(activity).refreshKS(new RefreshTokenCallBack() {
                                @Override
                                public void response(CommonResponse response) {
                                    if (response.getStatus()) {
                                        getAssetListBasedOnMediaType(context, mediatype, homechannelCallBack);
                                        //getSubCategories(context, subCategoryCallBack);
                                    } else {
                                        responseList.add(null);
                                        homechannelCallBack.response(false, responseList, null);

                                    }
                                }
                            });
                        else {
                            responseList.add(null);
                            homechannelCallBack.response(false, responseList, null);

                        }
                    } else {
                        responseList.add(null);
                        homechannelCallBack.response(false, responseList, null);

                    }


                }
            } catch (Exception e) {
                PrintLogging.printLog(this.getClass(), "Exception", "" + e);

            }
        });
        getRequestQueue().queue(builder.build(client));
    }

    public void searchMovieKeyword(Context context, final String keyToSearch, final List<MediaTypeModel> model, int counter, SearchResultCallBack CallBack) {
        searchResultCallBack = CallBack;
        searchOutputModel = new ArrayList<>();
        currentMediaTypes = model;
        listAssetBuilders = new ArrayList<>();
        clientSetupKs();
        Runnable runnable = () -> setMovieQuickSearchBuilder(context, keyToSearch, model, counter, CallBack);
        new Thread(runnable).start();
    }

    private void setMovieQuickSearchBuilder(Context context, final String keyToSearch, final List<MediaTypeModel> model, int count, SearchResultCallBack CallBack) {
        final FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(15);

        Log.e(String.valueOf(count) + "====>", currentMediaTypes.get(count).getId());

        final SearchAssetFilter assetFilter = new SearchAssetFilter();
        assetFilter.setKSql(keyToSearch);
        assetFilter.setTypeIn(String.valueOf(MediaTypeConstant.getMovie(context)));

        AssetService.ListAssetBuilder assetService = AssetService.list(assetFilter, filterPager).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results.getTotalCount() > 0) {
                    if (result.results.getObjects() != null) {
                        if (result.results.getObjects().size() > 0) {
                            SearchModel temp = new SearchModel();
                            temp.setTotalCount(result.results.getTotalCount());
                            temp.setHeaderTitle(getNameFromType(result.results.getObjects().get(0).getType()));
                            temp.setType(result.results.getObjects().get(0).getType());
                            temp.setAllItemsInSection(result.results.getObjects());
                            temp.setSearchString(searchString);
                            if (!iscategoryAdded(result.results.getObjects().get(0).getType())) {
                                searchOutputModel.add(temp);
                            }
                            Log.e("SEARCH SIZE", String.valueOf(searchOutputModel.size()));
                            if (searchOutputModel.size() > 0) {
                                searchResultCallBack.response(true, searchOutputModel, "resultFound");
                            } else {
                                searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                            }
                        } else {
                            searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                        }

                    } else {
                        searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                    }

                } else {
                    searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                }
            } else {
                if ((result.error.getCode().equals(AppLevelConstants.KS_EXPIRE))) {
                    searchMovieKeyword(context, keyToSearch, model, count, CallBack);
                }
            }
        });
        getRequestQueue().queue(assetService.build(client));
    }

    public void searchVodCollectionKeyword(Context context, final String keyToSearch, final List<MediaTypeModel> model, int counter, SearchResultCallBack CallBack, String searchKeyword, String selectedGenre) {
        searchResultCallBack = CallBack;
        searchOutputModel = new ArrayList<>();
        currentMediaTypes = model;
        listAssetBuilders = new ArrayList<>();
        clientSetupKs();
        Runnable runnable = () -> setVodCollectionQuickSearchBuilder(context, keyToSearch, model, counter, CallBack, searchKeyword, selectedGenre);
        new Thread(runnable).start();
    }

    private void setVodCollectionQuickSearchBuilder(Context context, final String keyToSearch, final List<MediaTypeModel> model, int count, SearchResultCallBack CallBack, String searchKeyword, String selectedGenre) {
        final FilterPager filterPager = new FilterPager();
        filterPager.setPageIndex(1);
        filterPager.setPageSize(15);

        Log.e(String.valueOf(count) + "====>", currentMediaTypes.get(count).getId());

        final SearchAssetFilter assetFilter = new SearchAssetFilter();
        assetFilter.setKSql(keyToSearch);
        assetFilter.name(searchKeyword);
        assetFilter.setTypeIn(String.valueOf(MediaTypeConstant.getCollection(context)));
        //assetFilter.orderBy(SortByEnum.RELEVANCY_DESC.name());

        if (!KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase("")) {
            if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.AZ.name())) {
                assetFilter.orderBy(SortByEnum.NAME_ASC.name());
            } else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.POPULAR.name())) {
                assetFilter.orderBy(SortByEnum.VIEWS_DESC.name());
            } else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.NEWEST.name())) {
                assetFilter.orderBy(SortByEnum.CREATE_DATE_DESC.name());
            } else {
                assetFilter.orderBy(SortByEnum.RELEVANCY_DESC.name());
            }

        } else {
            assetFilter.orderBy(SortByEnum.RELEVANCY_DESC.name());
        }

        AssetService.ListAssetBuilder assetService = AssetService.list(assetFilter, filterPager).setCompletion(result -> {
            if (result.isSuccess()) {
                if (result.results.getTotalCount() > 0) {
                    if (result.results.getObjects() != null) {
                        if (result.results.getObjects().size() > 0) {
                            SearchModel temp = new SearchModel();
                            temp.setTotalCount(result.results.getTotalCount());
                            temp.setHeaderTitle(getNameFromType(result.results.getObjects().get(0).getType()));
                            temp.setType(result.results.getObjects().get(0).getType());
                            // List<Asset> assetss=AppCommonMethods.applyFreePaidFilter(result.results,context);
                            //temp.setAllItemsInSection(assetss);
                            //temp.setTotalCount(assetss.size());

                            if (result.results.getObjects().get(0).getType() == MediaTypeConstant.getMovie(context)
                                    || result.results.getObjects().get(0).getType() == MediaTypeConstant.getCollection(context)) {
                                List<Asset> assets = AppCommonMethods.addPagesFromCollection(result.results, context);
                                temp.setAllItemsInSection(assets);
                                temp.setTotalCount(assets.size());
                            } else {
                                //List<Asset> assets=AppCommonMethods.removePagesFromCollection(result.results);
                                temp.setTotalCount(result.results.getTotalCount());
                                temp.setAllItemsInSection(result.results.getObjects());
                            }

                            // temp.setAllItemsInSection(result.results.getObjects());
                            temp.setSearchString(searchString);
                            if (!iscategoryAdded(result.results.getObjects().get(0).getType())) {
                                searchOutputModel.add(temp);
                            }
                            Log.e("SEARCH SIZE", String.valueOf(searchOutputModel.size()));
                            if (searchOutputModel.size() > 0) {
                                searchResultCallBack.response(true, searchOutputModel, "resultFound");
                            } else {
                                searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                            }
                        } else {
                            searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                        }

                    } else {
                        searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                    }

                } else {
                    searchResultCallBack.response(true, searchOutputModel, "noResultFound");
                }
            } else {
                if ((result.error.getCode().equals(AppLevelConstants.KS_EXPIRE))) {
                    searchVodCollectionKeyword(context, keyToSearch, model, count, CallBack, searchKeyword, selectedGenre);
                }
            }
        });
        getRequestQueue().queue(assetService.build(client));
    }

    RecentSearchCallBack recentSearchCallBack;

    public void recentSearchKaltura(Context context, RecentSearchCallBack CallBack) {
        recentSearchCallBack = CallBack;
        final SearchHistoryFilter filterPager = new SearchHistoryFilter();
        filterPager.setOrderBy("NONE");

        FilterPager filterPager1 = new FilterPager();
        filterPager1.setPageIndex(1);
        filterPager1.setPageSize(500);
        List<SearchedKeywords> searchedKeywordsList = new ArrayList<>();
        LinkedHashSet<String> unique = new LinkedHashSet<String>();


        clientSetupKs();
        SearchHistoryService.ListSearchHistoryBuilder searchHistoryBuilder = SearchHistoryService.list(filterPager, filterPager1).setCompletion(new OnCompletion<Response<ListResponse<SearchHistory>>>() {
            @Override
            public void onComplete(Response<ListResponse<SearchHistory>> result) {
                if (result != null && result.results != null && result.results.getObjects() != null && result.results.getObjects().size() > 0) {
                    for (int i = 0; i < result.results.getObjects().size(); i++) {
                        unique.add(result.results.getObjects().get(i).getName());
                    }

                    for (String x : unique) {
                        SearchedKeywords searchedKeywords = new SearchedKeywords();
                        searchedKeywords.setKeyWords(x);
                        searchedKeywordsList.add(searchedKeywords);
                    }
                    recentSearchCallBack.recentSearches(searchedKeywordsList);
                } else {
                    recentSearchCallBack.recentSearches(new ArrayList<>());
                }
            }
        });

        getRequestQueue().queue(searchHistoryBuilder.build(client));
    }


}
