package com.astro.sott.fragments.detailRailFragment.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.astro.sott.baseModel.RecommendationRailFragment;
import com.astro.sott.fragments.ShowFragment.ui.ShowFragment;
import com.astro.sott.fragments.episodeFrament.EpisodesFragment;
import com.astro.sott.fragments.trailerFragment.TrailerFragment;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.fragments.moreLikeThisFragment.MoreLikeThisFragment;
import com.astro.sott.utils.helpers.LiveTvViewPager;
import com.astro.sott.utils.helpers.MediaTypeConstant;

/*
 * For Series fragmentType
 *0-No Episodes ,Trailer,Highlights
 * 1-Only Episode
 * 2- Trailer||Highlight
 *3-Episode && Trailer ||Highlight
 * 4-Episode && Related
 * 5-Related && trailer
 * 6-Episode && Trailer ||Highlight&& Related
 * 7-Only Related
 * For Movie fragmentType
 * 0--No Trailer and Highlights
 * 1--Only Trailer
 * 2- Both Trailer & Highlights
 * 3- Only HighLights
 *
 * */
public class DetailPagerAdapter extends FragmentStatePagerAdapter {
    private final Context mContext;
    private int mCurrentPosition = -1;
    private RailCommonData railData;
    private int adapterSize;
    private int fragmentType;

    public DetailPagerAdapter(FragmentManager fm, Context mContext, RailCommonData railCommonData, int size, int fragmentType) {
        super(fm);
        this.mContext = mContext;
        this.railData = railCommonData;
        adapterSize = size;
        this.fragmentType = fragmentType;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != mCurrentPosition && container instanceof LiveTvViewPager) {
            Fragment fragment = (Fragment) object;
            LiveTvViewPager pager = (LiveTvViewPager) container;

            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (railData.getObject().getType() == MediaTypeConstant.getSeries(mContext)) {
            if (adapterSize != 3) {
                if (fragmentType == 1) {
                    return setEpisodeFragment();
                } else if (fragmentType == 2) {
                    return setTrailerFragment();
                } else if (fragmentType == 7) {
                    return setRecommendationFragment();
                } else if (fragmentType == 3) {
                    if (position == 0) {
                        return setEpisodeFragment();
                    } else {
                        return setTrailerFragment();
                    }
                } else if (fragmentType == 4) {
                    if (position == 0) {
                        return setEpisodeFragment();
                    } else {
                        return setRecommendationFragment();
                    }
                } else {
                    if (position == 0) {
                        return setTrailerFragment();
                    } else {
                        return setRecommendationFragment();
                    }
                }
            } else {
                if (position == 0) {
                    return setEpisodeFragment();
                } else if (position == 1) {
                    return setTrailerFragment();
                } else {
                    return setRecommendationFragment();
                }
            }

        } else if (railData.getObject().getType() == MediaTypeConstant.getCollection(mContext)) {
            if (adapterSize == 3) {
                if (position == 0) {
                    return setShowFragment();
                } else if (position == 1) {
                    return setTrailerFragment();
                } else {
                    return setRecommendationFragment();
                }
            } else if (adapterSize == 2) {
                if (TabsData.getInstance().getMovieShows() == null && TabsData.getInstance().getSeriesShows() == null) {
                    if (position == 0) {
                        return setTrailerFragment();
                    } else {
                        return setRecommendationFragment();
                    }
                } else if (TabsData.getInstance().getYouMayAlsoLikeData() == null) {
                    if (position == 0) {
                        return setShowFragment();
                    } else {
                        return setTrailerFragment();
                    }
                } else {
                    if (position == 0) {
                        return setShowFragment();
                    } else {
                        return setRecommendationFragment();
                    }
                }
            } else {
                if (TabsData.getInstance().getMovieShows() == null && TabsData.getInstance().getSeriesShows() == null) {
                    if (TabsData.getInstance().getYouMayAlsoLikeData() == null) {
                        return setTrailerFragment();
                    } else {
                        return setRecommendationFragment();

                    }
                } else {
                    return setShowFragment();
                }
            }
        } else {
            if (fragmentType != 0) {
                if (position == 0) {
                    return setTrailerFragment();
                } else {
                    return setRecommendationFragment();
                }
            } else {
                return setRecommendationFragment();
            }
        }

    }


    public Fragment setRecommendationFragment() {
        RecommendationRailFragment recommendationRailFragment = new RecommendationRailFragment();
        Bundle args = new Bundle();
        args.putInt("BUNDLE_TAB_ID", 0);
        args.putInt("MEDIA_TYPE", railData.getObject().getType());
        args.putInt("LAYOUT_TYPE", AppLevelConstants.Rail5);
        args.putParcelable("ASSET_OBJ", railData.getObject());
        recommendationRailFragment.setArguments(args);
        return recommendationRailFragment;
    }

    public Fragment setEpisodeFragment() {
        EpisodesFragment episodesFragment = new EpisodesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ASSET_OBJ", railData.getObject());
        bundle.putInt("LAYOUT_TYPE", AppConstants.Rail5);
        bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        episodesFragment.setArguments(bundle);
        return episodesFragment;
    }

    public Fragment setTrailerFragment() {
        TrailerFragment trailerFragment = new TrailerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        trailerFragment.setArguments(bundle);
        return trailerFragment;
    }

    public Fragment setShowFragment() {
        ShowFragment showFragment = new ShowFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        showFragment.setArguments(bundle);
        return showFragment;
    }

    @Override
    public int getCount() {
        return adapterSize;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (railData.getObject().getType() == MediaTypeConstant.getSeries(mContext)) {
            if (fragmentType == 1) {
                return mContext.getString(R.string.episode_title);
            } else if (fragmentType == 2) {
                return mContext.getString(R.string.trailer_more);
            } else if (fragmentType == 3) {
                if (position == 0) {
                    return mContext.getString(R.string.episode_title);

                } else {
                    return mContext.getString(R.string.trailer_more);
                }

            } else if (fragmentType == 4) {
                if (position == 0) {
                    return mContext.getString(R.string.episode_title);

                } else {
                    return mContext.getString(R.string.related);
                }
            } else if (fragmentType == 5) {
                if (position == 0) {
                    return mContext.getString(R.string.trailer_more);
                } else {
                    return mContext.getString(R.string.related);
                }
            } else if (fragmentType == 6) {
                if (position == 0) {
                    return mContext.getString(R.string.episode_title);
                } else if (position == 1) {
                    return mContext.getString(R.string.trailer_more);
                } else {
                    return mContext.getString(R.string.related);

                }
            } else {
                return mContext.getString(R.string.related);
            }

        } else if (railData.getObject().getType() == MediaTypeConstant.getCollection(mContext)) {
            if (adapterSize == 3) {
                if (position == 0) {
                    return mContext.getString(R.string.shows);
                } else if (position == 1) {
                    if (fragmentType == 1) {
                        return mContext.getString(R.string.trailer_title);
                    } else if (fragmentType == 2) {
                        return mContext.getString(R.string.trailer_more);

                    } else {
                        return mContext.getString(R.string.highlights);
                    }
                } else {
                    return mContext.getString(R.string.related);
                }
            } else if (adapterSize == 2) {
                if (TabsData.getInstance().getMovieShows() == null && TabsData.getInstance().getSeriesShows() == null) {
                    if (position == 0) {
                        if (fragmentType == 1) {
                            return mContext.getString(R.string.trailer_title);
                        } else if (fragmentType == 2) {
                            return mContext.getString(R.string.trailer_more);

                        } else {
                            return mContext.getString(R.string.highlights);
                        }
                    } else {
                        return mContext.getString(R.string.related);
                    }
                } else if (TabsData.getInstance().getYouMayAlsoLikeData() == null) {
                    if (position == 0) {
                        return mContext.getString(R.string.shows);
                    } else {
                        if (fragmentType == 1) {
                            return mContext.getString(R.string.trailer_title);
                        } else if (fragmentType == 2) {
                            return mContext.getString(R.string.trailer_more);

                        } else {
                            return mContext.getString(R.string.highlights);
                        }
                    }
                } else {
                    if (position == 0) {
                        return mContext.getString(R.string.shows);
                    } else {
                        return mContext.getString(R.string.related);
                    }
                }
            } else {
                if (TabsData.getInstance().getMovieShows() == null && TabsData.getInstance().getSeriesShows() == null) {
                    if (TabsData.getInstance().getYouMayAlsoLikeData() == null) {
                        if (fragmentType == 1) {
                            return mContext.getString(R.string.trailer_title);
                        } else if (fragmentType == 2) {
                            return mContext.getString(R.string.trailer_more);

                        } else {
                            return mContext.getString(R.string.highlights);
                        }
                    } else {
                        return mContext.getString(R.string.related);

                    }
                } else {
                    return mContext.getString(R.string.shows);
                }
            }

        } else {
            if (adapterSize > 1) {
                switch (position) {
                    case 0:
                        if (fragmentType == 1) {
                            return mContext.getString(R.string.trailer_title);
                        } else if (fragmentType == 2) {
                            return mContext.getString(R.string.trailer_more);

                        } else if (fragmentType == 3) {
                            return mContext.getString(R.string.highlights);
                        }
                    case 1:
                        return mContext.getString(R.string.related);

                    default:
                        return null;
                }
            } else {
                return mContext.getString(R.string.related);//mContext.getString(R.string.more_like_this);
            }
        }
    }
}
