package coleo.com.abjo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import coleo.com.abjo.R;
import coleo.com.abjo.data_class.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageItem> {

    private ArrayList<Message> messages;
    private Context context;

    public MessageAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message_item_layout, parent, false);
        return new MessageItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageItem holder, int position) {
        Message temp = messages.get(position);
        holder.getText().setText(temp.getText());
        holder.getTitle().setText(temp.getTitle());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    class MessageItem extends RecyclerView.ViewHolder {
        private TextView text;
        private TextView title;

        public MessageItem(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.message_text_id);
            title = itemView.findViewById(R.id.message_title_id);
        }

        public TextView getText() {
            return text;
        }

        public TextView getTitle() {
            return title;
        }
    }

}
