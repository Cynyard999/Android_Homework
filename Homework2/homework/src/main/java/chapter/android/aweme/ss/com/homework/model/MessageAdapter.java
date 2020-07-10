package chapter.android.aweme.ss.com.homework.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chapter.android.aweme.ss.com.homework.R;
import chapter.android.aweme.ss.com.homework.widget.CircleImageView;

/**
 * 适配器
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MsgViewHolder> {
    private static final String TAG = "MessageAdapter";

    private final List<Message> messages;

    private final ListItemClickListener mOnCLickListener;

    private static int viewHolderCount;

    public MessageAdapter(List<Message> data, ListItemClickListener listener) {
        messages = data;
        mOnCLickListener = listener;
        viewHolderCount = 0;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.im_list_item, viewGroup, false);
        viewHolderCount++;
        return new MsgViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int index) {
        holder.bind(index);
    }

    /**
     * 通过icon种类获取对应图片资源id
     *
     * @param icon 种类
     * @return 资源id
     */
    private int getIconByType(String icon) {
        int resId = 0;
        switch (icon) {
            case "TYPE_ROBOT":
                resId = R.drawable.session_robot;
                break;
            case "TYPE_GAME":
                resId = R.drawable.icon_micro_game_comment;
                break;
            case "TYPE_SYSTEM":
                resId = R.drawable.session_system_notice;
                break;
            case "TYPE_STRANGER":
                resId = R.drawable.session_stranger;
                break;
            case "TYPE_USER":
                resId = R.drawable.icon_girl;
                break;
            default:
                break;
        }
        return resId;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    /**
     * 自定义ViewHolder
     */
    public class MsgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView name;
        private final TextView msg;
        private final TextView time;
        private final CircleImageView circleImageView;
        private final ImageView notice;

        MsgViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_title);
            msg = itemView.findViewById(R.id.tv_description);
            time = itemView.findViewById(R.id.tv_time);
            circleImageView = itemView.findViewById(R.id.iv_avatar);
            notice = itemView.findViewById(R.id.robot_notice);
            itemView.setOnClickListener(this);
        }

        public void bind(int index) {
            name.setText(messages.get(index).getTitle());
            time.setText(messages.get(index).getTime());
            msg.setText(messages.get(index).getDescription());
            if (messages.get(index).isOfficial()) {
                notice.setVisibility(View.VISIBLE);
            }
            switch (messages.get(index).getIcon()) {
                case "TYPE_USER":
                    circleImageView.setImageResource(R.drawable.icon_girl);
                    break;
                case "TYPE_GAME":
                    circleImageView.setImageResource(R.drawable.icon_micro_game_comment);
                    break;
                case "TYPE_SYSTEM":
                    circleImageView.setImageResource(R.drawable.session_system_notice);
                    break;
                case "TYPE_ROBOT":
                    circleImageView.setImageResource(R.drawable.session_robot);
                    break;
                case "TYPE_STRANGER":
                    circleImageView.setImageResource(R.drawable.session_stranger);
                    break;
            }
        }
            @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String msg = messages.get(position).getDescription();
            String name = messages.get(position).getTitle();
            if (mOnCLickListener != null)
                mOnCLickListener.onListItemClick(name,msg);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(String name,String message);
    }
}
