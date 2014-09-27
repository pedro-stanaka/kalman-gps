package br.uel.cross.geoutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pedro on 26/09/14.
 */
public class GeoReader {

    public static Position readPositionFromStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try  {
            String latlong = reader.readLine();
            while (latlong != null) {
                Pattern pattern = Pattern.compile(".*?(\\-?\\d+\\.?\\d*)\\s+(\\-?\\d+\\.?\\d*)");
                Matcher matcher = pattern.matcher(latlong);
                if (matcher.matches()) { // If found any group of lat long extract and returns
                    Position position = new Position(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)));
                    reader.close();
                    stream.close();
                    return position;
                }else{
                    latlong = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
