package com.fridayafternoon.campusspotlight;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class Event implements Parcelable {
    public String count, name, date, location, time, description, tags, link, title, type, organization;


    protected Event(Parcel in) {
        count = in.readString();
        name = in.readString();
        date = in.readString();
        location = in.readString();
        time = in.readString();
        description = in.readString();
        tags = in.readString();
        link = in.readString();
        title = in.readString();
        type = in.readString();
        organization = in.readString();
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

    public Event(String name, String date, String location, String time, String description, String tags, String link) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.time = time;
        this.description = description;
        this.tags = tags;
        this.link = link;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
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
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(count);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(location);
        dest.writeString(time);
        dest.writeString(description);
        dest.writeString(tags);
        dest.writeString(link);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(organization);
    }
}
