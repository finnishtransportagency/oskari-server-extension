package my.app;

import fi.mml.portti.service.search.ChannelSearchResult;
import fi.mml.portti.service.search.SearchCriteria;
import fi.mml.portti.service.search.SearchResultItem;
import fi.nls.oskari.annotation.Oskari;
import fi.nls.oskari.search.channel.SearchChannel;
import fi.nls.oskari.util.IOHelper;

import java.io.IOException;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;

import java.util.Arrays;
import java.util.ArrayList;

@Oskari("WfsRoadSearchChannel")
public class WfsRoadSearchChannel extends SearchChannel {

    public ChannelSearchResult doSearch(SearchCriteria criteria) {
        ChannelSearchResult result = new ChannelSearchResult();

        String srs = criteria.getSRS();
        if (srs == null) {
            srs = "EPSG:3067";
        }

        String actualSearchString = criteria.getSearchString();
        actualSearchString = actualSearchString.substring(0, 1).toUpperCase() + actualSearchString.substring(1).toLowerCase();

        try {
            final String responseData = IOHelper.getURL("https://julkinen.vayla.fi/inspirepalvelu/digiroad/" +
                    "wfs?request=getfeature&typenames=digiroad:tiekunnalliset_yksityistiet&version=1.1.0&maxfeatures=250" +
                    "&CQL_FILTER=TIENIMI_SU%20LIKE%20%27" + actualSearchString + "%25%27");


            //Contains all the search results as blocks of string.
            String[] resultsAsStringBLocks = responseData.split("<digiroad:tiekunnalliset_yksityistiet gml:id=");


            //List of items that have been added already to the results
            //n needs to be added so it wont be shown as result.
            ArrayList<String> resultsUsedAlready = new ArrayList<>();
            resultsUsedAlready.add("n");


            //Loop all the indivitual results blocks.
            for (String resultBlock : resultsAsStringBLocks) {


                //Replace all the special xml chars.
                resultBlock = resultBlock.replaceAll("\"", "YY");
                resultBlock = resultBlock.replaceAll("<", "VV");
                resultBlock = resultBlock.replaceAll(">", "BB");
                resultBlock = resultBlock.replaceAll(":", "NN");
                resultBlock = resultBlock.replaceAll("/", "OO");

                //Copy of result block
                String resultCopyBlock = resultBlock;

                //Get the block where the name is and index where it ends.
                String nameBlock = resultBlock.substring(resultBlock.indexOf("NNTIENIMI_SUBB") + 14);
                int indexOfNameEnding = nameBlock.indexOf("VVOOdigi");

                //Collect the actual name
                String nameOfTheResult = "" + nameBlock.charAt(0);
                for (int i = 1; i < indexOfNameEnding; i++) {
                    nameOfTheResult += nameBlock.charAt(i);
                }
                if (resultsUsedAlready.contains(nameOfTheResult)) {
                    continue;
                }
                resultsUsedAlready.add(nameOfTheResult);

                //Construct actual result item.
                SearchResultItem item = new SearchResultItem();
                item.addValue("key2", resultBlock);
                item.setTitle(nameOfTheResult);
                item.setType("Tiekunnallinen yksityistie");

                //Split CoordinatesBlock to actual coordinates.
                String coordinatesBlock = resultCopyBlock.substring(resultCopyBlock.indexOf("NNposListBB") + 11);
                String[] pilko = coordinatesBlock.split("VVOOgmlNN");
                String[] pilko2 = pilko[0].split(" ");
                item.setLon(pilko2[0]);
                item.setLat(pilko2[1]);

                result.addItem(item);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error searching", ex);
        }
        return result;
    }
}