package coleo.com.abjo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import coleo.com.abjo.R;
import coleo.com.abjo.adapter.MessageAdapter;
import coleo.com.abjo.data_class.Message;

public class MassageActivity extends AppCompatActivity {

    private RecyclerView messageList;
    private MessageAdapter adapter;
    private ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage);

        messageList = findViewById(R.id.message_list);
        //todo get from server
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        messageList.setLayoutManager(mLayoutManager);
        messages = new ArrayList<>();
        adapter = new MessageAdapter(messages, this);
        messageList.setAdapter(adapter);

    }

    public void update() {
        adapter.notifyDataSetChanged();
    }

}
