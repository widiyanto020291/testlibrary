package net.sourceforge.opencamera.minigallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.sourceforge.opencamera.R;

import java.io.File;
import java.util.Date;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<FileType> mList;
    private Context context;

    private static final int VIEW_IMAGE = 9;
    private static final int VIEW_VIDEO = 10;
    private static final int VIEW_MORE = 11;

    private OnItemClickListener onItemClickListener;

    public GalleryAdapter(Context mContext, List<FileType> mList) {
        this.mList = mList;
        this.context = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        FileType fileType = mList.get(position);
        if (fileType.getType() == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            return VIEW_IMAGE;
        }else if (fileType.getType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            return VIEW_VIDEO;
        }else{
            return VIEW_MORE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_IMAGE) {
            return new ImageViewHolder(inflater.inflate(R.layout.item_gallery, parent, false));
        }else if(viewType == VIEW_VIDEO) {
            return new VideoViewHolder(inflater.inflate(R.layout.item_gallery, parent, false));
        }else {
            return new MoreViewHolder(inflater.inflate(R.layout.item_gallery, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final FileType fileType = mList.get(position);
        if (holder instanceof ImageViewHolder) {
            Glide.with(context)
                    .load(fileType.getUri())
                    .thumbnail(0.1f)
                    .apply(new RequestOptions().centerCrop()
                            .placeholder(R.color.icons_background)
                           )
                    .into(((ImageViewHolder) holder).mImg);

            ((ImageViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onImageClick(fileType, holder.getAdapterPosition());
                }
            });
        }else if (holder instanceof VideoViewHolder) {
            Glide.with(context)
                    .load(fileType.getUri())
                    .thumbnail(0.1f)
                    .apply(new RequestOptions().centerCrop()
                            .placeholder(R.color.icons_background)
                    )
                    .into(((VideoViewHolder) holder).mImg);
        }else if(holder instanceof  MoreViewHolder){
            ((MoreViewHolder) holder).mImg.setImageResource(R.drawable.ic_gallery);
            ((MoreViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onMoreImageClick();
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onImageClick(FileType fileType, int pos);
        void onMoreImageClick();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        public ImageViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
        }
    }
    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        public VideoViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
        }
    }

    public static class MoreViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        public MoreViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
        }
    }

}
