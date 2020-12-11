package com.astro.sott.adapter.playlist;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.astro.sott.utils.helpers.CommonPlaylistDialog;
import com.astro.sott.R;
import com.astro.sott.callBacks.commonCallBacks.PlaylistCallback;
import com.astro.sott.databinding.MultiplePlaylistItemBinding;
import com.astro.sott.utils.helpers.PrintLogging;
import com.kaltura.client.types.PersonalList;

import java.util.List;

public class MultiplePlaylistAdapter extends RecyclerView.Adapter<MultiplePlaylistAdapter.SingleItemHolder> {

    Activity context;
    PlaylistCallback playlistCallback;
    private List<PersonalList> arrayList;
    private int mSelectedPosition = 0;

    public MultiplePlaylistAdapter(List<PersonalList> arrayList, Activity context, CommonPlaylistDialog commonPlaylistDialog) {
        this.arrayList = arrayList;
        this.context = context;
        this.playlistCallback = commonPlaylistDialog;
    }

    @NonNull
    @Override
    public MultiplePlaylistAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        MultiplePlaylistItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.multiple_playlist_item, parent, false);


        return new SingleItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MultiplePlaylistAdapter.SingleItemHolder singleItemHolder, int position) {
        singleItemHolder.multiplePlaylistItemBinding.playlistText.setText(arrayList.get(position).getName());

        singleItemHolder.multiplePlaylistItemBinding.rootLayout.setOnClickListener(view -> {
            PrintLogging.printLog(MultiplePlaylistAdapter.class, "", position + "plsfnfsdnfds");
            mSelectedPosition = position;
            notifyDataSetChanged();

        });
        if (position == mSelectedPosition) {
            singleItemHolder.multiplePlaylistItemBinding.radioSelector.setChecked(true);
            playlistCallback.onClick(arrayList.get(position).getName(), arrayList.get(position).getPartnerListType());

            //  singleItemHolder.multiplePlaylistItemBinding.radioSelector.setHighlightColor(Color.parseColor("#1E88E5"));
        } else {
            singleItemHolder.multiplePlaylistItemBinding.radioSelector.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        MultiplePlaylistItemBinding multiplePlaylistItemBinding;

        public SingleItemHolder(@NonNull MultiplePlaylistItemBinding itemView) {
            super(itemView.getRoot());
            this.multiplePlaylistItemBinding = itemView;
        }
    }
}
