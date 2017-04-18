package edu.stevens.cs522.chatserver.managers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.Set;

import edu.stevens.cs522.chatserver.async.AsyncContentResolver;
import edu.stevens.cs522.chatserver.async.IContinue;
import edu.stevens.cs522.chatserver.async.IEntityCreator;
import edu.stevens.cs522.chatserver.async.QueryBuilder;
import edu.stevens.cs522.chatserver.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.chatserver.contracts.BaseContract;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;


/**
 * Created by dduggan.
 */

public class MessageManager extends Manager<Message> {

    private static final int LOADER_ID = 1;

    private static final IEntityCreator<Message> creator = new IEntityCreator<Message>() {
        @Override
        public Message create(Cursor cursor) {
            return new Message(cursor);
        }
    };

    private AsyncContentResolver contentResolver;

    private Context myContext;

    public MessageManager(Context context) {
        super(context, creator, LOADER_ID);
        contentResolver = new AsyncContentResolver(context.getContentResolver());
        this.myContext = context;
        Log.i("MessageManager", "Created MessageManager instance");
    }

    public void getAllMessagesAsync(IQueryListener<Message> listener) {
        // TODO use QueryBuilder to complete this
        Log.i("MessageManager", "getAllMessages: calling querybuilder executequery");
        QueryBuilder.executeQuery("main", (Activity) myContext, MessageContract.CONTENT_URI, LOADER_ID, creator, listener);
    }

    public void persistAsync(final Message message) {
        IContinue<Uri> callback = new IContinue<Uri>() {
            public void kontinue(Uri uri) {
                message.id = (int) MessageContract.getId(uri);
            }
        };
        ContentValues values = new ContentValues();
        Log.i("MessageManager", "persistAsync: message text="+message.messageText);
        message.writeToProvider(values);
        Log.i("MessageManager", "persistAsync: calling contentresolver insertAsync");
        contentResolver.insertAsync(MessageContract.CONTENT_URI, values, callback);
    }

}
