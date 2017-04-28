package com.quintype.musicstreaming.utils;

import com.quintype.musicstreaming.models.Audio;
import com.quintype.musicstreaming.models.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshaykoul on 27/04/17.
 */

public class Utilities {

    public static ArrayList<Audio> getAudioFromTracks(List<Track> trackList, String
            clientId) {
        ArrayList<Audio> adioList = new ArrayList<>();
        for (Track track : trackList) {
            Audio audio = new Audio(track.getId(), track.getDuration(), track.getDescription(),
                    track.getTitle(), track.getGenre(), track.getUser().getUsername(),
                    getHirezArtwork(track.getArtworkUrl()), track.getStreamUrl() + "?client_id="
                    + clientId);
            adioList.add(audio);
        }
        return adioList;
    }


    private static String getHirezArtwork(String artworkUrl) {
        if (artworkUrl != null) {
            return artworkUrl.replace("large.jpg", "t500x500.jpg");
        } else {
            return artworkUrl;
        }
    }
}
