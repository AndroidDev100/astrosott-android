package com.astro.sott.baseModel;

import android.content.Context;
import android.util.Log;

import com.astro.sott.R;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.carousel.model.Slide;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.enveu.Enum.ImageSource;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.AssetHistory;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_CST_CUSTOM;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_CUSTOM;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_LANDSCAPE;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_LDS_LANDSCAPE;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_POSTER;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_POTRAIT;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_PR_POTRAIT;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_SQR_SQUARE;
import static com.astro.sott.utils.constants.AppConstants.CAROUSEL_SQUARE;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_CIRCLE;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_LANDSCAPE;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_POSTER;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_POTRAIT;
import static com.astro.sott.utils.constants.AppConstants.HORIZONTAL_SQUARE;


public class CategoryRails {
    int continueWatchingCount = -1;
    List<AssetCommonBean> assetCommonList;
    private ArrayList<Slide> slides;
    int counterValue = 0;

    public void setRails(Context context,List<Response<ListResponse<Asset>>> list, List<VIUChannel> channelList, int loopCount, ArrayList<Slide> slide, List<AssetCommonBean> assetList, int counter) {
        counterValue = counter;
        PrintLogging.printLog("", "printIndex--" + loopCount + "");
        slides = slide;
        assetCommonList = assetList;


        if (channelList != null) {

            setCommonAdapterRailData(context,counter, channelList, channelList.get(counter), list);
         /*   int i = 0;
            String desc = channelList.get(counter).getDescription();
            PrintLogging.printLog("", "channelList--" + desc);
            AssetCommonBean assetCommonBean1 = new AssetCommonBean();
            switch (desc) {
                case "HRO":
                    assetCommonBean1.setStatus(true);
                    setRailData(channelList.get(counter), list, 1, assetCommonBean1, i, AppConstants.TYPE8);
                    setHeroData(channelList.get(counter), assetCommonBean1);
                    break;
                case AppConstants.KEY_FB_FAN:
                    assetCommonBean1.setStatus(true);
                    setFBAdsList(channelList.get(counter), channelList, i, list, assetCommonBean1, desc);
                    break;

                case AppConstants.KEY_FB_FANTABLET:
                    assetCommonBean1.setStatus(true);
                    setFBAdsListTab(channelList.get(counter), channelList, i, list, assetCommonBean1, desc);
                    break;
                case AppConstants.KEY_DFP_ADS:
                    assetCommonBean1.setStatus(true);
                    setDfpAdsData(channelList.get(counter), i, list, assetCommonBean1, desc);
                    break;
                case AppConstants.KEY_CONTINUE_WATCHING:
                    assetCommonBean1.setStatus(true);
                    int index = new KsPreferenceKeys(ApplicationMain.getAppContext()).getContinueWatchingIndex();
                    PrintLogging.printLog("", "continueWatchingIndex" + index);
                    if (index == counter) {
                        setContinueWatchingList(channelList.get(counter), channelList, i, list, assetCommonBean1);
                    }
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    break;

                case AppConstants.KEY_MY_WATCHLIST:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 1, assetCommonBean1, i, AppConstants.TYPE4);
                    assetCommonBean1.setRailType(AppConstants.Rail4);

                    break;

                case AppConstants.KEY_RECOMMENDED:
                    assetCommonBean1.setStatus(true);
                    int index1 = new KsPreferenceKeys(ApplicationMain.getAppContext()).getContinueWatchingIndex();
                    PrintLogging.printLog("", "continueWatchingIndex" + index1);
                    if (index1 == counter) {
                        setContinueWatchingList(channelList.get(counter), channelList, i, list, assetCommonBean1);
                    }
                    break;


                case "UGCIFP":
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setRailType(AppConstants.Rail8);
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 1, assetCommonBean1, i, AppConstants.TYPE8);
                    break;

                case "SERIESBANNER":
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setRailType(AppConstants.Rail10);
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 1, assetCommonBean1, i, AppConstants.TYPE10);
                    break;


                case CAROUSEL_LANDSCAPE:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setWidgetType(CAROUSEL_LDS_LANDSCAPE);
                    assetCommonBean1.setRailType(AppConstants.Rail1);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 0, assetCommonBean1, i, AppConstants.TYPE1);
                    break;
                case CAROUSEL_POTRAIT:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setWidgetType(CAROUSEL_PR_POTRAIT);
                    assetCommonBean1.setRailType(AppConstants.Rail1);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 0, assetCommonBean1, i, AppConstants.TYPE1);
                    break;
                case CAROUSEL_SQUARE:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setWidgetType(CAROUSEL_SQR_SQUARE);
                    assetCommonBean1.setRailType(AppConstants.Rail1);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 0, assetCommonBean1, i, AppConstants.TYPE1);
                    break;
                case CAROUSEL_CUSTOM:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setWidgetType(CAROUSEL_CST_CUSTOM);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 0, assetCommonBean1, i, AppConstants.TYPE1);
                    assetCommonBean1.setRailType(AppConstants.Rail1);
                    break;


                case HORIZONTAL_LANDSCAPE:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 1, assetCommonBean1, i, AppConstants.TYPE5);
                    assetCommonBean1.setRailType(AppConstants.Rail5);
                    break;
                case HORIZONTAL_POTRAIT:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 1, assetCommonBean1, i, AppConstants.TYPE3);
                    assetCommonBean1.setRailType(AppConstants.Rail3);
                    break;
                // TODO create poster
                case HORIZONTAL_POSTER:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 1, assetCommonBean1, i, AppConstants.TYPE4);
                    assetCommonBean1.setRailType(AppConstants.Rail4);
                case HORIZONTAL_SQUARE:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 1, assetCommonBean1, i, AppConstants.TYPE4);
                    assetCommonBean1.setRailType(AppConstants.Rail4);
                    break;
                case HORIZONTAL_CIRCLE:
                    assetCommonBean1.setStatus(true);
                    assetCommonBean1.setRailDetail(channelList.get(counter));
                    assetCommonBean1.setTitle(channelList.get(counter).getName());
                    assetCommonBean1.setID(channelList.get(counter).getId());
                    setRailData(channelList.get(counter), list, 1, assetCommonBean1, i, AppConstants.TYPE2);
                    assetCommonBean1.setRailType(AppConstants.Rail2);
                    break;

            }*/


         /*   if (desc.contains(HRO.name())) {
                AssetCommonBean assetCommonBean = new AssetCommonBean();
                assetCommonBean.setStatus(true);
                setHeroData(channelList.get(counter), assetCommonBean);
            } else if (desc.contains(AppConstants.KEY_FB_FAN)) {
                AssetCommonBean assetCommonBean = new AssetCommonBean();
                assetCommonBean.setStatus(true);
                setFBAdsList(channelList.get(counter), channelList, i, list, assetCommonBean, desc);
            } else if (desc.contains(AppConstants.KEY_FB_FANTABLET)) {
                AssetCommonBean assetCommonBean = new AssetCommonBean();
                assetCommonBean.setStatus(true);
                setFBAdsListTab(channelList.get(counter), channelList, i, list, assetCommonBean, desc);
            } else if (desc.toUpperCase().contains(AppConstants.KEY_DFP_ADS)) {
                AssetCommonBean assetCommonBean = new AssetCommonBean();
                assetCommonBean.setStatus(true);
                setDfpAdsData(channelList.get(counter), i, list, assetCommonBean, desc);
            } else {
                if (list.get(i) != null) {
                    if (list.get(i).results == null) {
                        PrintLogging.printLog("", "printIndex--" + i + "");
                    }
                    int totalCount = list.get(i).results.getTotalCount();

                    if (totalCount == 0) {
                        // Log.w(" " + i, loopCount + "");
                        PrintLogging.printLog("", "channel" + channelList.get(counter).getName());
                    } else {
                        // Log.w(" " + i, loopCount + "");
                        AssetCommonBean assetCommonBean = new AssetCommonBean();
                        assetCommonBean.setStatus(true);
                        if (loopCount == 1) {
                            assetCommonBean.setRailType(AppConstants.Rail1);
                            assetCommonBean.setTitle(channelList.get(counter).getName());
                            assetCommonBean.setID(channelList.get(counter).getId());
                            setRailData(channelList.get(counter), list, 0, assetCommonBean, i, AppConstants.TYPE1);
                            loopCount++;
                        } else {
                            String description = channelList.get(counter).getDescription();
                            if (description.equalsIgnoreCase(AppConstants.KEY_CONTINUE_WATCHING)) {
                                int index = new KsPreferenceKeys(ApplicationMain.getAppContext()).getContinueWatchingIndex();
                                PrintLogging.printLog("", "continueWatchingIndex" + index);
                                if (index == counter) {
                                    setContinueWatchingList(channelList.get(counter), channelList, i, list, assetCommonBean);
                                }
                            } else if (description.equalsIgnoreCase(AppConstants.KEY_RECOMMENDED)) {
                                setRecommendedList(channelList.get(counter), channelList, i, list, assetCommonBean);
                            } else if (description.contains(AppConstants.KEY_FB_FAN)) {
                                setFBAdsList(channelList.get(counter), channelList, i, list, assetCommonBean, description);
                            } else if (description.equalsIgnoreCase(AppConstants.TYPE8)) {
                                assetCommonBean.setRailType(AppConstants.Rail8);
                                assetCommonBean.setTitle(channelList.get(counter).getName());
                                assetCommonBean.setID(channelList.get(counter).getId());

                                setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE8);

                            } else if (description.equalsIgnoreCase(AppConstants.TYPE10)) {
                                assetCommonBean.setRailType(AppConstants.Rail10);
                                assetCommonBean.setTitle(channelList.get(counter).getName());
                                assetCommonBean.setID(channelList.get(counter).getId());

                                setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE10);

                            } else {
                                assetCommonBean.setMoreType(AppConstants.CATEGORY);
                                PrintLogging.printLog("", "descriptionValuess::" + channelList.get(counter).getName() + "--" + channelList.get(counter).getDescription());
                                if (channelList.get(counter).getDescription().equalsIgnoreCase(AppConstants.TYPE2)) {
                                    assetCommonBean.setRailType(AppConstants.Rail2);
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());

                                    setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE2);
                                } else if (channelList.get(counter).getDescription().equalsIgnoreCase(AppConstants.TYPE3)) {
                                    assetCommonBean.setRailType(AppConstants.Rail3);
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());

                                    setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE3);
                                } else if (channelList.get(counter).getDescription().equalsIgnoreCase(AppConstants.TYPE4)) {
                                    assetCommonBean.setRailType(AppConstants.Rail4);
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());

                                    setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE4);
                                } else if (channelList.get(counter).getDescription().equalsIgnoreCase(AppConstants.TYPE5)) {
                                    assetCommonBean.setRailType(AppConstants.Rail5);
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());

                                    setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE5);

                                } *//*else if (channelList.get(counter).getDescription().equalsIgnoreCase(PlaylistType.KTM.name())) {
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());
                                    setRailType(channelList.get(counter), assetCommonBean);

                                    if (channelList.get(counter).getLayout().equalsIgnoreCase(CAR.name()))
                                        setRailData(channelList.get(counter), list, 0, assetCommonBean, i, AppConstants.TYPE1);

                                }*//*
                            }
                        }
                    }
                }
            }*/
        }
    }


    public void setCommonAdapterRailData(Context context,int counter, List<VIUChannel> channelList, VIUChannel dtChannel, List<Response<ListResponse<Asset>>> list) {
        int i = 0;
        String desc = dtChannel.getDescription();
        PrintLogging.printLog("", "channelList--" + desc);
        AssetCommonBean assetCommonBean1 = new AssetCommonBean();
        assetCommonBean1.setRailDetail(dtChannel);
        switch (desc) {
            case "HRO":
                assetCommonBean1.setStatus(true);
                if (dtChannel.getImageSource().equalsIgnoreCase(ImageSource.AST.name())) {
                    if (dtChannel.isProgram()) {
                        setRailData(context,dtChannel, list, 1, assetCommonBean1, i, AppConstants.TYPE8);
                    } else {
                        assetCommonBean1.setRailDetail(dtChannel);
                    }
                }

                setHeroData(dtChannel, assetCommonBean1);
                break;
            case AppConstants.KEY_FB_FAN:
                assetCommonBean1.setStatus(true);
                setFBAdsList(dtChannel, channelList, i, list, assetCommonBean1, desc);
                break;

            case AppConstants.KEY_FB_FANTABLET:
                assetCommonBean1.setStatus(true);
                setFBAdsListTab(dtChannel, channelList, i, list, assetCommonBean1, desc);
                break;
            case AppConstants.KEY_DFP_ADS:
                assetCommonBean1.setStatus(true);
                setDfpAdsData(dtChannel, i, list, assetCommonBean1, desc);
                break;
            case AppConstants.KEY_CONTINUE_WATCHING:
                assetCommonBean1.setStatus(true);
                int index =  KsPreferenceKey.getInstance(context).getContinueWatchingIndex();
                PrintLogging.printLog("", "continueWatchingIndex" + index);
                if (index == counter) {
                    try {
                        setContinueWatchingList(context,dtChannel, channelList, i, list, assetCommonBean1);
                    }catch (Exception ignored){

                    }

                }
                assetCommonBean1.setRailDetail(dtChannel);
                break;

            case AppConstants.KEY_MY_WATCHLIST:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 1, assetCommonBean1, i, AppConstants.TYPE5);
                assetCommonBean1.setRailType(AppConstants.Rail5);

                break;

            case AppConstants.KEY_RECOMMENDED:
                assetCommonBean1.setStatus(true);
                int index1 = KsPreferenceKey.getInstance(context).getContinueWatchingIndex();
                PrintLogging.printLog("", "continueWatchingIndex" + index1);
                if (index1 == counter) {
                    setContinueWatchingList(context,dtChannel, channelList, i, list, assetCommonBean1);
                }
                break;


            case "UGCIFP":
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setRailType(AppConstants.Rail8);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 1, assetCommonBean1, i, AppConstants.TYPE8);
                break;

            case "SERIESBANNER":
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setRailType(AppConstants.Rail10);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 1, assetCommonBean1, i, AppConstants.TYPE10);
                break;


            case CAROUSEL_LANDSCAPE:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setWidgetType(CAROUSEL_LDS_LANDSCAPE);
                assetCommonBean1.setRailType(AppConstants.Rail1);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 0, assetCommonBean1, i, AppConstants.TYPE1);
                break;
            case CAROUSEL_POSTER:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setWidgetType(CAROUSEL_PR_POTRAIT);
                assetCommonBean1.setRailType(AppConstants.Rail1);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 0, assetCommonBean1, i, AppConstants.TYPE1);
                break;
            case CAROUSEL_POTRAIT:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setWidgetType(CAROUSEL_PR_POTRAIT);
                assetCommonBean1.setRailType(AppConstants.Rail1);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 0, assetCommonBean1, i, AppConstants.TYPE1);
                potraitSlideLoop(assetCommonBean1);

                break;
            case CAROUSEL_SQUARE:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setWidgetType(CAROUSEL_SQR_SQUARE);
                assetCommonBean1.setRailType(AppConstants.Rail1);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 0, assetCommonBean1, i, AppConstants.TYPE1);
                break;
            case CAROUSEL_CUSTOM:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setWidgetType(CAROUSEL_CST_CUSTOM);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 0, assetCommonBean1, i, AppConstants.TYPE1);
                assetCommonBean1.setRailType(AppConstants.Rail1);
                break;


            case HORIZONTAL_LANDSCAPE:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 1, assetCommonBean1, i, AppConstants.TYPE5);
                assetCommonBean1.setRailType(AppConstants.Rail5);
                break;

            case HORIZONTAL_POTRAIT:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 1, assetCommonBean1, i, AppConstants.TYPE3);
                assetCommonBean1.setRailType(AppConstants.Rail3);
                break;
            // TODO create poster
            case HORIZONTAL_POSTER:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 1, assetCommonBean1, i, AppConstants.POSTER);
                assetCommonBean1.setRailType(AppConstants.Rail12);
                break;
            case HORIZONTAL_SQUARE:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 1, assetCommonBean1, i, AppConstants.TYPE4);
                assetCommonBean1.setRailType(AppConstants.Rail4);
                break;
            case HORIZONTAL_CIRCLE:
                assetCommonBean1.setStatus(true);
                assetCommonBean1.setRailDetail(dtChannel);
                assetCommonBean1.setTitle(dtChannel.getName());
                assetCommonBean1.setID(dtChannel.getId());
                setRailData(context,dtChannel, list, 1, assetCommonBean1, i, AppConstants.TYPE2);
                assetCommonBean1.setRailType(AppConstants.Rail2);
                break;

        }

    }


    private void potraitSlideLoop(AssetCommonBean assetCommonBean) {
     /*   ArrayList<Slide> mSlide = assetCommonBean.getSlides();
        assetCommonBean.getSlides().addAll(mSlide);
        assetCommonBean.getSlides().addAll(mSlide);*/

    }

    private void setFBAdsList(VIUChannel category, List<VIUChannel> channelList, int i, List<Response<ListResponse<Asset>>> list,
                              AssetCommonBean assetCommonBean, String description) {

        //  String FAN_ID = AppCommonMethods.getFAN_ID(description);
        // if (!FAN_ID.equalsIgnoreCase("")) {
        //   PrintLogging.printLog("", "FAN_PLACEMENT_ID " + FAN_ID);
        assetCommonBean.setRailType(AppConstants.Rail9);
        assetCommonBean.setTitle(channelList.get(counterValue).getName());
        assetCommonBean.setID(channelList.get(counterValue).getId());
        assetCommonBean.setFanPlacementId(category.getAdID());
        assetCommonBean.setRailDetail(category);
        assetCommonList.add(assetCommonBean);
        // setRailData(list, 1, assetCommonBean, i, AppConstants.TYPE9);
        // }

    }

    private void setFBAdsListTab(VIUChannel category, List<VIUChannel> channelList, int i, List<Response<ListResponse<Asset>>> list,
                                 AssetCommonBean assetCommonBean, String description) {

        // String FAN_ID = AppCommonMethods.getFAN_IDTAB(description);
        //  if (!FAN_ID.equalsIgnoreCase("")) {
        //    PrintLogging.printLog("", "FAN_TAB_PLACEMENT_ID " + FAN_ID);
        assetCommonBean.setRailType(AppConstants.Rail9);
        assetCommonBean.setTitle(channelList.get(counterValue).getName());
        assetCommonBean.setID(channelList.get(counterValue).getId());
        assetCommonBean.setFanPlacementId(category.getAdID());
        assetCommonBean.setRailDetail(category);
        assetCommonList.add(assetCommonBean);
        // setRailData(list, 1, assetCommonBean, i, AppConstants.TYPE9);
        // }

    }

    private void setHeroData(VIUChannel channelList, AssetCommonBean assetCommonBean) {
        assetCommonBean.setRailType(AppConstants.HERO_CIR_CIRCLE);
        assetCommonBean.setRailDetail(channelList);
        assetCommonList.add(assetCommonBean);
    }


    private void setDfpAdsData(VIUChannel channelList, int i, List<Response<ListResponse<Asset>>> list,
                               AssetCommonBean assetCommonBean, String description) {
        assetCommonBean.setRailType(AppConstants.Rail11);
        assetCommonBean.setTitle(AppConstants.KEY_DFP_ADS);
        assetCommonBean.setFanPlacementId(channelList.getLayout() + "#" + channelList.getAdID());
        assetCommonBean.setRailDetail(channelList);
        assetCommonList.add(assetCommonBean);
    }


    int continueWatchingRailCount = 0;

    private void setRailData(Context context,VIUChannel channelList, List<Response<ListResponse<Asset>>> list, int type,
                             AssetCommonBean assetCommonBean, int position, String tileType) {
        try {
            continueWatchingRailCount = 0;
            PrintLogging.printLog("", "typeValuess--" + type);
            if (type == 0) {
                slides = new ArrayList<>();
                if (list.get(position).results.getObjects().size() > 0) {

                    for (int j = 0; j < list.get(position).results.getObjects().size(); j++) {
                        carouselDataInject(context,channelList, list, position, j);
                    }

                } else {
                    for (int j = 0; j < list.get(position).results.getObjects().size(); j++) {
                        carouselDataInject(context,channelList, list, position, j);
                    }
                }
                PrintLogging.printLog("", "typeValuess-->>" + slides.size());
                assetCommonBean.setSlides(slides);
                //setRailType(channelList, assetCommonBean);
                assetCommonList.add(assetCommonBean);
            } else {
                int totalCount = list.get(position).results.getTotalCount();
                if (totalCount == 0) {
                    // PrintLogging.printLog("", "indexessss" + totalCount);
                } else {
                    List<RailCommonData> railList = new ArrayList<RailCommonData>();
                    if (tileType == AppConstants.TYPE6) {
                        PrintLogging.printLog("", "indexessss" + AppCommonMethods.getContinueWatchingIDsPreferences(context).get(0));
                        sortContinueWatchingRail(context,list, 1, assetCommonBean, position, tileType, railList,channelList);

                    } else {
                        for (int j = 0; j < list.get(position).results.getObjects().size(); j++) {

                            RailCommonData railCommonData = new RailCommonData();
//                            railCommonData.setCatchUpBuffer(list.get(position).results.getObjects().get(j).getEnableCatchUp());
                            railCommonData.setType(list.get(position).results.getObjects().get(j).getType());
                            railCommonData.setName(list.get(position).results.getObjects().get(j).getName());
                            railCommonData.setId(list.get(position).results.getObjects().get(j).getId());
                            railCommonData.setObject(list.get(position).results.getObjects().get(j));
                            if (tileType.equals(AppConstants.TYPE6)) {
                                PrintLogging.printLog("", "valueeInLoop" + AssetContent.getVideoProgress(context,j, list.get(position).results.getObjects().get(j).getId().intValue()));
                                railCommonData.setPosition(AssetContent.getVideoProgress(context,j, list.get(position).results.getObjects().get(j).getId().intValue()));
                                railCommonData.setProgress(AssetContent.getVideoPosition(context,j, list.get(position).results.getObjects().get(j).getId().intValue()));
                            }
                            if (tileType.equals(AppConstants.TYPE7)) {
                                railCommonData.setType(AppConstants.Rail7);
                            }
                            int ugcCreator = list.get(position).results.getObjects().get(j).getType();
                            List<AssetCommonImages> imagesList = new ArrayList<AssetCommonImages>();
                            if (ugcCreator == MediaTypeConstant.getUGCCreator(context)) {
                                PrintLogging.printLog("", "creatorNameInrail" + list.get(position).results.getObjects().get(j).getName());

                                railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(position).results.getObjects().get(j).getName().trim()));
                            } else {
                                for (int k = 0; k < list.get(position).results.getObjects().get(j).getImages().size(); k++) {
                                    AssetCommonImages assetCommonImages = new AssetCommonImages();
                                    AppCommonMethods.getCategoryImageList(context,tileType, position, j, k, list, assetCommonImages, imagesList,channelList);
                                }
                            }


                            List<AssetCommonUrls> urlList = new ArrayList<AssetCommonUrls>();
                            if (list.get(position).results.getObjects().get(j).getMediaFiles() != null) {
                                for (int k = 0; k < list.get(position).results.getObjects().get(j).getMediaFiles().size(); k++) {
                                    AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                                    assetCommonUrls.setUrl(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                                    assetCommonUrls.setUrlType(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getType());
                                    assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, position, j, k));
                                    urlList.add(assetCommonUrls);
                                }
                            }

                            railCommonData.setImages(imagesList);
                            railCommonData.setUrls(urlList);
                            if (tileType.equals(AppConstants.TYPE6)) {
                                if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getUGCVideo(context) ||list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context) ||
                                        list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getLinear(context)) {
                                } else {
                                    continueWatchingRailCount++;
                                    continueWatchingCount = 1;
                                    railList.add(railCommonData);
                                    assetCommonBean.setTotalCount(continueWatchingRailCount);
                                }
                            } else {
                                railList.add(railCommonData);
                                assetCommonBean.setTotalCount(list.get(position).results.getTotalCount());
                            }

                        }

                    }

                    PrintLogging.printLog("", "continueWatchingCount==" + continueWatchingCount);
                    assetCommonBean.setRailAssetList(railList);
                    if (tileType.equals(AppConstants.TYPE6)) {
                        if (continueWatchingCount == 1) {
                            assetCommonList.add(assetCommonBean);
                            PrintLogging.printLog("", "continueWatchingRailCount-->>" + continueWatchingRailCount);
                        } else {
                            assetCommonBean.setStatus(false);
                            assetCommonList.add(assetCommonBean);

                        }
                    } else {
                        assetCommonList.add(assetCommonBean);
                    }

                }
            }
        } catch (Exception e) {
            PrintLogging.printLog("", "categoryCatch-->>" + e.toString());
        }

    }

    public void sortContinueWatchingRail(Context context,List<Response<ListResponse<Asset>>> list, int type,
                                         AssetCommonBean assetCommonBean, int position, String tileType, List<RailCommonData> railList,VIUChannel channelList) {

        List<Long> longList = AppCommonMethods.getContinueWatchingIDsPreferences(context);
        List<AssetHistory> continueWatchingList = AppCommonMethods.getContinueWatchingPreferences(context);
        if (longList != null) {
            if (longList.size() > 0) {
                for (int y = 0; y < longList.size(); y++) {
                    long con_id = longList.get(y);
                    for (int j = 0; j < list.get(position).results.getObjects().size(); j++) {
                        if (con_id == list.get(position).results.getObjects().get(j).getId()) {
                            PrintLogging.printLog("", "indexessss-->>" + list.get(position).results.getObjects().get(j).getId());
                            RailCommonData railCommonData = new RailCommonData();
//                            railCommonData.setCatchUpBuffer(list.get(position).results.getObjects().get(j).getEnableCatchUp());
                            railCommonData.setType(list.get(position).results.getObjects().get(j).getType());
                            railCommonData.setName(list.get(position).results.getObjects().get(j).getName());
                            railCommonData.setId(list.get(position).results.getObjects().get(j).getId());
                            railCommonData.setObject(list.get(position).results.getObjects().get(j));
                            if (tileType.equals(AppConstants.TYPE6)) {
                                PrintLogging.printLog("", "valueeInLoop" + AssetContent.getVideoProgress(context,j, list.get(position).results.getObjects().get(j).getId().intValue()));
                                railCommonData.setPosition(AssetContent.getVideoProgress(context,j, list.get(position).results.getObjects().get(j).getId().intValue()));
                                railCommonData.setProgress(AssetContent.getVideoPosition(context,j, list.get(position).results.getObjects().get(j).getId().intValue()));
                            }
                            if (tileType.equals(AppConstants.TYPE7)) {
                                railCommonData.setType(AppConstants.Rail7);
                            }
                            int ugcCreator = list.get(position).results.getObjects().get(j).getType();
                            List<AssetCommonImages> imagesList = new ArrayList<AssetCommonImages>();
                            if (ugcCreator == MediaTypeConstant.getUGCCreator(context)) {
                                railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(position).results.getObjects().get(j).getName()));
                            } else {
                                for (int k = 0; k < list.get(position).results.getObjects().get(j).getImages().size(); k++) {
                                    AssetCommonImages assetCommonImages = new AssetCommonImages();
                                    AppCommonMethods.getCategoryImageList(context,tileType, position, j, k, list, assetCommonImages, imagesList,channelList);
                                }
                            }


                            List<AssetCommonUrls> urlList = new ArrayList<AssetCommonUrls>();
                            if (list.get(position).results.getObjects().get(j).getMediaFiles() != null) {
                                for (int k = 0; k < list.get(position).results.getObjects().get(j).getMediaFiles().size(); k++) {
                                    AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                                    assetCommonUrls.setUrl(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                                    assetCommonUrls.setUrlType(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getType());
                                    assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, position, j, k));
                                    urlList.add(assetCommonUrls);
                                }
                            }

                            railCommonData.setImages(imagesList);
                            railCommonData.setUrls(urlList);
                            if (tileType.equals(AppConstants.TYPE6)) {

                                if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getUGCVideo(context) ||list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context) ||
                                        list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getLinear(context)) {
                                } else {
                                    boolean included=AppCommonMethods.shouldItemIncluded(continueWatchingList,list.get(position).results.getObjects().get(j).getId()+"");
                                    if (included){
                                        continueWatchingRailCount++;
                                        continueWatchingCount = 1;
                                        railList.add(railCommonData);
                                        assetCommonBean.setTotalCount(continueWatchingRailCount);
                                    }
                                }
                            } else {
                                assetCommonBean.setTotalCount(list.get(position).results.getTotalCount());
                                railList.add(railCommonData);

                            }
                        }


                    }

                }

            }
        }


    }

    private void carouselDataInject(Context context,VIUChannel channelList, List<Response<ListResponse<Asset>>> list, int position, int j) {
        try {
            Slide slide = new Slide();
            RailCommonData commonData = new RailCommonData();
            int imageSize = list.get(position).results.getObjects().get(j).getImages().size();
            int railType = list.get(position).results.getObjects().get(j).getType();
            for (int i = 0; i < imageSize; i++) {
                /*if (railType == MediaTypeConstant.getLinear(context)) {
                    if (channelList!=null && channelList.getKalturaOTTImageType()!=null && !channelList.getKalturaOTTImageType().equalsIgnoreCase("")){
                        if (list.get(position).results.getObjects().get(j).getImages().get(i).getRatio().equals(channelList.getKalturaOTTImageType())) {
                            String image_url = AppConstants.WEBP_URL+list.get(position).results.getObjects().get(j).getImages().get(i).getUrl();
                            String final_url = image_url + AppConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.carousel_image_width) + AppConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.carousel_image_height) + AppConstants.QUALITY;
                            slide.setImageFromUrl(final_url);
                        }
                    }else {
                        if (list.get(position).results.getObjects().get(j).getImages().get(i).getRatio().equals("16x9")) {
                            String image_url = AppConstants.WEBP_URL+list.get(position).results.getObjects().get(j).getImages().get(i).getUrl();
                            String final_url = image_url + AppConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.carousel_image_width) + AppConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.carousel_image_height) + AppConstants.QUALITY;
                            slide.setImageFromUrl(final_url);
                        }
                    }

                } else {


                }*/

                slide.setImageFromUrl(setImageUrl(context,list.get(position).results.getObjects().get(j), channelList.getWidgetType(),channelList));

            }
            String name = list.get(position).results.getObjects().get(j).getName();

            // PrintLogging.printLog("","carouselCondition"+image_url+ AppConstants.WIDTH+ApplicationMain.getAppContext().getResources().getDimension(R.dimen.carousel_image_width)+ AppConstants.HEIGHT+ApplicationMain.getAppContext().getResources().getDimension(R.dimen.carousel_image_height)+ AppConstants.QUALITY);
            slide.setType(1);
            slide.setTitle(name);
            commonData.setObject(list.get(position).results.getObjects().get(j));
            slide.setObjects(list.get(position).results.getObjects().get(j));
            slide.setRailCommonData(commonData);
            slides.add(slide);

        } catch (Exception e) {
            PrintLogging.printLog("", "sliderCatch" + e.toString());
        }

    }

    public String setImageUrl(Context context,Asset asset, int type,VIUChannel channelList) {
        Log.w("carousalData-->>", type + "");
        String landscapeUrl = "";
        String potraitUrl = "";
        String squareUrl = "";
        if (asset.getImages().size() > 0) {
            if (channelList != null && channelList.getKalturaOTTImageType() != null && !channelList.getKalturaOTTImageType().equalsIgnoreCase("")) {
                //12=Poster, 11= Landscape
                if (type == 12 || type == 13) {
                    for (int i = 0; i < asset.getImages().size(); i++) {
                        if (asset.getImages().get(i).getRatio().equals("16x9")) {
                            String image_url = AppConstants.WEBP_URL + asset.getImages().get(i).getUrl();
                            potraitUrl = image_url + AppConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.carousel_image_width) + AppConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.carousel_image_height) + AppConstants.QUALITY;
                        }
                    }
                } else {
                    for (int i = 0; i < asset.getImages().size(); i++) {
                        if (asset.getImages().get(i).getRatio().equals(channelList.getKalturaOTTImageType())) {
                            String image_url = AppConstants.WEBP_URL + asset.getImages().get(i).getUrl();
                            landscapeUrl = image_url + AppConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.carousel_image_width) + AppConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.carousel_image_height) + AppConstants.QUALITY;
                        }
                    }
                }
            } else {
                if (type == 12 || type == 13) {
                    for (int i = 0; i < asset.getImages().size(); i++) {
                        if (asset.getImages().get(i).getRatio().equals("2x3")) {
                            String image_url = AppConstants.WEBP_URL + asset.getImages().get(i).getUrl();
                            potraitUrl = image_url + AppConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.carousel_image_width) + AppConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.carousel_image_height) + AppConstants.QUALITY;
                        }
                    }
                } else {
                    for (int i = 0; i < asset.getImages().size(); i++) {
                        if (asset.getImages().get(i).getRatio().equals("16x9")) {
                            String image_url = AppConstants.WEBP_URL + asset.getImages().get(i).getUrl();
                            landscapeUrl = image_url + AppConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.carousel_image_width) + AppConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.carousel_image_height) + AppConstants.QUALITY;
                        }
                    }
                }
            }
        }
            switch (type) {
                case CAROUSEL_PR_POTRAIT:
                    return potraitUrl;
                case CAROUSEL_SQR_SQUARE:
                    return squareUrl;
                case CAROUSEL_CST_CUSTOM:
                    return landscapeUrl;
                default:
                    return landscapeUrl;
            }

    }

    private void setContinueWatchingList(Context context,VIUChannel category, List<VIUChannel> channelList, int i, List<Response<ListResponse<Asset>>> list, AssetCommonBean assetCommonBean) {

        List<Long> listIds = AppCommonMethods.getContinueWatchingIDsPreferences(context);
        if (listIds != null) {
            if (listIds.size() > 0) {
                assetCommonBean.setRailType(AppConstants.Rail6);
                assetCommonBean.setTitle(channelList.get(counterValue).getName());
                assetCommonBean.setMoreType(AppConstants.CONTINUE_WATCHING);
                assetCommonBean.setID(channelList.get(counterValue).getId());

                setRailData(context,category, list, 1, assetCommonBean, i, AppConstants.TYPE6);

            }
        }

    }

    private void setRecommendedList(Context context,VIUChannel category, List<VIUChannel> channelList, int i, List<Response<ListResponse<Asset>>> list, AssetCommonBean assetCommonBean) {
        assetCommonBean.setRailType(AppConstants.Rail7);
        assetCommonBean.setTitle(channelList.get(counterValue).getName());
        assetCommonBean.setID(channelList.get(counterValue).getId());

        setRailData(context,category, list, 1, assetCommonBean, i, AppConstants.TYPE7);
    }


    public void setDescriptionRails(Context context,List<Response<ListResponse<Asset>>> list, List<VIUChannel> channelList, List<AssetCommonBean> assetList, int counter) {
        counterValue = counter;
        assetCommonList = assetList;
        try {
            if (channelList != null) {
                int i = 0;

                setCommonAdapterRailData(context,counter, channelList, channelList.get(counter), list);
             /*  String desc = channelList.get(counter).getDescription();
                PrintLogging.printLog("", "channelList--" + channelList.get(counter).getDescription() + "");
                if (desc.contains(HRO.name())) {
                    AssetCommonBean assetCommonBean = new AssetCommonBean();
                    assetCommonBean.setStatus(true);
                    setHeroData(channelList.get(counter), assetCommonBean);
                } else if (desc.contains(AppConstants.KEY_FB_FAN)) {
                    AssetCommonBean assetCommonBean = new AssetCommonBean();
                    assetCommonBean.setStatus(true);
                    setFBAdsList(channelList.get(counter), channelList, i, list, assetCommonBean, desc);
                } else if (desc.contains(AppConstants.KEY_FB_FANTABLET)) {
                    AssetCommonBean assetCommonBean = new AssetCommonBean();
                    assetCommonBean.setStatus(true);
                    setFBAdsListTab(channelList.get(counter), channelList, i, list, assetCommonBean, desc);
                } else if (desc.toUpperCase().contains(AppConstants.KEY_DFP_ADS)) {
                    AssetCommonBean assetCommonBean = new AssetCommonBean();
                    assetCommonBean.setStatus(true);
                    setDfpAdsData(channelList.get(counter), i, list, assetCommonBean, desc);
                } else {
                    if (list.get(i) != null) {
                        if (list.get(i).results == null) {
                            PrintLogging.printLog("", "printIndex--" + i + "");
                        }

                        int totalCount = list.get(i).results.getTotalCount();
                        if (totalCount == 0) {

                        } else {
                            AssetCommonBean assetCommonBean = new AssetCommonBean();
                            assetCommonBean.setStatus(true);

                            String description = channelList.get(counter).getDescription();
                            if (description.equalsIgnoreCase(AppConstants.KEY_CONTINUE_WATCHING)) {
                                int index = new KsPreferenceKeys(ApplicationMain.getAppContext()).getContinueWatchingIndex();
                                PrintLogging.printLog("", "continueWatchingIndex" + index + " " + counter);
                                if (index == counter) {
                                    setContinueWatchingList(channelList.get(counter), channelList, i, list, assetCommonBean);

                                }
                            } else if (description.equalsIgnoreCase(AppConstants.KEY_RECOMMENDED)) {
                                setRecommendedList(channelList.get(counter), channelList, i, list, assetCommonBean);
                            } else if (description.contains(AppConstants.KEY_FB_FAN)) {
                                setFBAdsList(channelList.get(counter), channelList, i, list, assetCommonBean, description);
                            } else if (description.equalsIgnoreCase(AppConstants.TYPE8)) {
                                assetCommonBean.setRailType(AppConstants.Rail8);
                                assetCommonBean.setTitle(channelList.get(counter).getName());
                                assetCommonBean.setID(channelList.get(counter).getId());

                                setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE8);

                            } else if (description.equalsIgnoreCase(AppConstants.TYPE10)) {
                                assetCommonBean.setRailType(AppConstants.Rail10);
                                assetCommonBean.setTitle(channelList.get(counter).getName());
                                assetCommonBean.setID(channelList.get(counter).getId());

                                setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE10);

                            } else {
                                assetCommonBean.setMoreType(AppConstants.CATEGORY);
                                PrintLogging.printLog("", "railDescription-->" + channelList.get(counter).getDescription());
                                if (channelList.get(counter).getDescription().equalsIgnoreCase(AppConstants.TYPE2)) {
                                    assetCommonBean.setRailType(AppConstants.Rail2);
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());

                                    setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE2);
                                } else if (channelList.get(counter).getDescription().equalsIgnoreCase(AppConstants.TYPE3)) {
                                    assetCommonBean.setRailType(AppConstants.Rail3);
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());

                                    setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE3);
                                } else if (channelList.get(counter).getDescription().equalsIgnoreCase(AppConstants.TYPE4)) {
                                    assetCommonBean.setRailType(AppConstants.Rail4);
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());

                                    setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE4);
                                    PrintLogging.printLog("", "commingInSquare" + channelList.get(i).getDescription());
                                } else if (channelList.get(counter).getDescription().equalsIgnoreCase(AppConstants.TYPE5)) {
                                    assetCommonBean.setRailType(AppConstants.Rail5);
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());

                                    setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE5);

                                } else {
                                    assetCommonBean.setRailType(AppConstants.Rail5);
                                    assetCommonBean.setTitle(channelList.get(counter).getName());
                                    assetCommonBean.setID(channelList.get(counter).getId());

                                    setRailData(channelList.get(counter), list, 1, assetCommonBean, i, AppConstants.TYPE5);
                                }

                            }


                        }
                    }

                }
*/

            }

        } catch (Exception e) {
            // PrintLogging.printLog("","crashInCategoryRails-->"+e.getMessage());
        }

    }
}