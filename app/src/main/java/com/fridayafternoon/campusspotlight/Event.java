package com.fridayafternoon.campusspotlight;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("ParcelCreator")
public class Event implements Parcelable {
    public String count, date, location, time, description, tags, link, title, type, organization, user, key;


    protected Event(Parcel in) {
        count = in.readString();
        date = in.readString();
        location = in.readString();
        time = in.readString();
        description = in.readString();
        tags = in.readString();
        link = in.readString();
        title = in.readString();
        type = in.readString();
        organization = in.readString();
        user = in.readString();
        key = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Event() {
    }

    public Event(String date, String location, String time, String description, String tags, String link, String user) {
        this.date = date;
        this.location = location;
        this.time = time;
        this.description = description;
        this.tags = tags;
        this.link = link;
        this.user = user;
        this.key = key;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDate(){
         int dateInt =Integer.parseInt(date);
         Date date = new Date(dateInt*1000L);
         DateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");
         format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
         String formatted = format.format(date);
         return formatted;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Event{" +
                "count='" + count + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", organization='" + organization + '\'' +
                ", user='" + user + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public String getUser() {
        return user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(count);
        dest.writeString(date);
        dest.writeString(location);
        dest.writeString(time);
        dest.writeString(description);
        dest.writeString(tags);
        dest.writeString(link);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(organization);
        dest.writeString(user);
        dest.writeString(key);
    }
}
