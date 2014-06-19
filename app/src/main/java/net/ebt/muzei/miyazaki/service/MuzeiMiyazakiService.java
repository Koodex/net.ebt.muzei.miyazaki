package net.ebt.muzei.miyazaki.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by eboudrant on 6/17/14.
 */
public class MuzeiMiyazakiService extends RemoteMuzeiArtSource {

    private static final String TAG = "MuzeiMiyazakiService";
    private static final String SOURCE_NAME = "MuzeiMiyazakiArtSource";
    private static final String CURRENT_PREF_NAME = "MuzeiMiyazakiArtSource.current";
    private static final String BASE_URL = "https://i.imgur.com/";
    private static final int ROTATE_TIME_MILLIS = 24 * 60 * 60 * 1000; // rotate every 3 hours

    public MuzeiMiyazakiService() {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {

        String current = FILES[getNextArtworkIndex()];
        publishArtwork(new Artwork.Builder()
                .imageUri(Uri.parse(BASE_URL + current))
                .token(current)
                .build());

        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);

    }

    /**
     * Current sequence is stored in a preferences
     * If there is no value then regenerate a sequence (shuffle)
     * @return indexToShow
     */
    private int getNextArtworkIndex() {
        int indexToShow = 0;

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(CURRENT_PREF_NAME, Context.MODE_PRIVATE);
        String sequence = prefs.getString(CURRENT_PREF_NAME, null);

        if(sequence == null) {

            // Reshuffle the array
            ArrayList<Integer> shuffled = new ArrayList<Integer>(FILES.length);
            for(int i=0; i<FILES.length; i++)
                shuffled.add(i);
            Collections.shuffle(shuffled);

            // Build show the first and build the next sequence
            StringBuilder builder = new StringBuilder();
            for(int i=0; i<FILES.length; i++) {
                if(i == 0) {
                    indexToShow = shuffled.get(i);
                } else {
                    builder.append(shuffled.get(i));
                    builder.append(" ");
                }
            }
            sequence = builder.toString().trim();

        } else {

            if(sequence.contains(" ")) {
                // Remove and show the first from the sequence
                indexToShow = Integer.parseInt(sequence.substring(0, sequence.indexOf(" ")));
                sequence = sequence.substring(sequence.indexOf(" ") + 1);
            } else {
                // Show the latest and reset the sequence
                indexToShow = Integer.parseInt(sequence);
                sequence = null;
            }

        }

        if(sequence != null) {
            prefs.edit().putString(CURRENT_PREF_NAME, sequence).commit();
        } else {
            prefs.edit().remove(CURRENT_PREF_NAME).commit();
        }

        return indexToShow;
    }

    private static final String[] FILES = {
            "bYKUtAV.jpg",
            "5WVktYO.jpg",
            "ZLFpbxm.jpg",
            "dyXbuVK.jpg",
            "zEYl5Cp.jpg",
            "0byg3HK.jpg",
            "O8pQHDY.jpg",
            "yZxeGfq.png",
            "OVlKiK7.png",
            "KzUPWxF.jpg",
            "wCqGzTU.png",
            "Eh79054.jpg",
            "klGyEd6.png",
            "omNQMoZ.jpg",
            "RmPrDRL.jpg",
            "Vg3Ruyl.jpg",
            "CliUfqX.jpg",
            "N6tlFJc.jpg",
            "g5Q1NCa.jpg",
            "1ZmrLjK.jpg",
            "tLH51iW.jpg",
            "QQLEwzF.jpg",
            "w9fNuEX.jpg",
            "mUOUz2Y.jpg",
            "pYHoTtp.jpg",
            "rC7mw31.jpg",
            "i1Lv4CY.jpg",
            "hZZRnoR.jpg",
            "2hvl6qP.jpg",
            "9VopmwA.jpg",
            "CMTYkgj.jpg",
            "v9mnBmP.jpg",
            "Mly98OD.jpg",
            "NK4ZyuA.jpg",
            "BB1YAfb.jpg",
            "CpSFYwY.jpg",
            "DC4L7zI.jpg",
            "nxuSi9d.jpg",
            "MEej3vQ.jpg",
            "OKI3GDJ.jpg",
            "4OTXXx5.jpg",
            "09EwMmq.jpg",
            "xXpKKpU.jpg",
            "bNsIXto.jpg",
            "aodIouB.jpg",
            "3wWtNXJ.jpg",
            "530ygYW.jpg",
            "TieinE6.jpg",
            "vWSioof.jpg",
            "OLwHF9J.jpg",
            "NTN0jVS.jpg",
            "yIFFaRt.jpg",
            "sfb9PbR.jpg",
            "d3ca61p.jpg",
            "XU3xQNh.jpg",
            "yMSLTPj.jpg",
            "5Jz8lse.jpg",
            "ApzUMsW.jpg",
            "GXg3FUs.jpg",
            "cwWI4AG.jpg",
            "O1R2quB.jpg",
            "fnjieyM.jpg",
            "f9mYEvO.jpg",
            "FzrnX0c.jpg",
            "fF4PYMn.jpg",
            "LsUvp5g.jpg",
            "vQE6pwk.jpg",
            "oL3ETVL.jpg",
            "JiACR7N.jpg",
            "8GloK0A.jpg",
            "Zq7dJpw.jpg",
            "XcjkS7n.jpg",
            "qEalfZp.jpg"
    };

}