package csc4330.mike.dreamlink.activities;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

/**
 * Created by Matthew Wolff on 9/23/2015.
 */

@ParseClassName("Hashtag")
public class Hashtag extends ParseObject {

    /**
     * Required zero-argument constructor
     */
    public Hashtag() {}

    public void setTag(String tag) {
        put("tag",tag);
    }

    public String getTag() {
        return getString("tag");
    }

    public ParseRelation<Dream> getDreams() {
        return getRelation("dreams");
    }

    public Hashtag(String tag) {
        super();
        setTag(tag);
    }
}