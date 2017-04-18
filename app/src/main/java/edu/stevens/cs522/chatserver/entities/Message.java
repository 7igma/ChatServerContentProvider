package edu.stevens.cs522.chatserver.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import edu.stevens.cs522.chatserver.contracts.MessageContract;

/**
 * Created by dduggan.
 */

public class Message implements Parcelable {

    public long id;

    public String messageText;

    public Date timestamp;

    public String sender;

    public long senderId;

    // TODO add operations for parcels (Parcelable), cursors and contentvalues
    public Message()
    {
    }

    protected Message(Parcel in) {
        id = in.readLong();
        messageText = in.readString();
        long tmpTimestamp = in.readLong();
        timestamp = tmpTimestamp != -1 ? new Date(tmpTimestamp) : null;
        sender = in.readString();
        senderId = in.readLong();
    }

    public Message(Cursor cursor)
    {
        this.messageText = MessageContract.getMessageText(cursor);
        this.timestamp = MessageContract.getTimestamp(cursor);
        this.sender = MessageContract.getSender(cursor);
        this.senderId = MessageContract.getSenderId(cursor);
    }

    public void writeToProvider(ContentValues out)
    {
        MessageContract.putMessageText(out, messageText);
        MessageContract.putTimestamp(out, timestamp);
        MessageContract.putSender(out, sender);
        MessageContract.putSenderId(out, senderId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(messageText);
        dest.writeLong(timestamp != null ? timestamp.getTime() : -1L);
        dest.writeString(sender);
        dest.writeLong(senderId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
