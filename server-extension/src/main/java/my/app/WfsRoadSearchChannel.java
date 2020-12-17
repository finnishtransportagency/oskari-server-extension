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
            // TODO: do the actual search
//            final String responseData = IOHelper.getURL("https://julkinen.vayla.fi/inspirepalvelu/digiroad/" +
//                    "wfs?request=getfeature&typenames=digiroad:tiekunnalliset_yksityistiet&version=1.1.0&maxfeatures=50" +
//                    "&CQL_FILTER=TIENIMI_SU%20LIKE%20%27" + criteria.getSearchString() + "%25%27");
            final String responseData = IOHelper.getURL("https://julkinen.vayla.fi/inspirepalvelu/digiroad/" +
                    "wfs?request=getfeature&typenames=digiroad:tiekunnalliset_yksityistiet&version=1.1.0&maxfeatures=250" +
                    "&CQL_FILTER=TIENIMI_SU%20LIKE%20%27" + actualSearchString + "%25%27");
//            final String responseData = IOHelper.getURL("https://www.google.fi/?q="
//                    + criteria.getSearchString());
            // parse responseData and populate result with SearchResultItems


            String[] asd = responseData.split("<digiroad:tiekunnalliset_yksityistiet gml:id=");

            //LOOP
            int counter = 0;
            ArrayList<String> already = new ArrayList<>();
            already.add("n");
            for (String st : asd) {


                SearchResultItem item = new SearchResultItem();
//                String value = ((st.split("<digiroad:TIENIMI_SU>"))[1].split("</digiroad:TIENIMI_SU>"))[0];
//                String[] things = st.substring(st.lastindexOF("<digiroad:TIENIMI_SU>"+1));
                st = st.replaceAll("\"", "YY");
                st = st.replaceAll("<", "VV");
                st = st.replaceAll(">", "BB");
                st = st.replaceAll(":", "NN");
                st = st.replaceAll("/", "OO");
//                st = st.replaceAll("\"", "YY");
//                st = st.replaceAll("\"", "YY");
//                st = st.replaceAll("\"", "YY");
//                String value = st.substring(st.lastIndexOf("NNTIENIMI_SUBB") + 1, st.lastIndexOf("VVOOdigiroadNNTIENIMI_SUBB") + 1);
                String ei = st;
                String value = st.substring(st.indexOf("NNTIENIMI_SUBB") + 14);
//                String[] kala = value.split("VVOOdigi");
                int asd3 = value.indexOf("VVOOdigi");
//                int joki = value.subSequence(0, asd3).length();
//                value = value.substring(0, value.indexOf("VVOOdigi"));
//                 value = value.substring(value.lastIndexOf("digiroad:TIENIMI_SU&gt;")+1, value.lastIndexOf("<digiroad:lisatiedot>")+1);
//                value = value.substring(value.lastIndexOf("digiroad:TIENIMI_SU&gt;") + 1);
//                LOG.info("Updated views:", "koiraa kalaa kissaa jäätelöö ");
                counter++;
                // item.setTitle("MySearchResult");

                String disgust = "" + value.charAt(0);
                for (int i = 1; i < asd3; i++) {
                    disgust += value.charAt(i);
                }
                if (already.contains(disgust)) {
                    continue;
                }
                already.add(disgust);
                item.addValue("key2", st);

                String jotainCoord = ei.substring(ei.indexOf("NNposListBB") + 11);
                String[] pilko = jotainCoord.split("VVOOgmlNN");
                String[] pilko2 = pilko[0].split(" ");
                int asd4 = ei.indexOf("NNposListBB");
//                String disgust2 = "" + jotainCoord.charAt(0);
//                for (int i = 1; i < asd4; i++) {
//                    disgust2 += jotainCoord.charAt(i);
//                }


//                double eka = Double.parseDouble(pilko2[0]);

//                String juusto = "mpoo" + pilko2[0];

//                item.setTitle("asd: <p>))" + juusto + "(( </p>koira" + "asd: <p>))" + Arrays.toString(pilko2) + "(( </p>koira\"");
                item.setTitle(disgust);
//                item.setDescription("Maan kuulu maissi sokkelo: ");
//                item.setType("Tiekunnallinen yksityistie  \"asd:2 <p>))\" " + jotainCoord + "\"(( </p>koira2\"");
                item.setType("Tiekunnallinen yksityistie");
//                item.setRegion("helsinki");

                item.setLon(pilko2[0]);
                item.setLat(pilko2[1]);
                result.addItem(item);
            }
            //LOOP_end

//            SearchResultItem item = new SearchResultItem();
//            item.addValue("key", responseData);
//            // item.setTitle("MySearchResult");
//
//
//            item.setTitle("Maissin sokkelo");
//            item.setDescription("Maan kuulu maissi sokkelo: ");
//            item.setType("kala");
//            item.setRegion("helsinki");
//
//            item.setLon(6654528);
//            item.setLat(345344);
//            result.addItem(item);
//
//
//            SearchResultItem item2 = new SearchResultItem();
//            item2.addValue("key", responseData);
//            // item.setTitle("MySearchResult");
//
//
//            item2.setTitle(responseData);
//            item2.setDescription("Maan kuulu maissi sokkelo2: ");
//            item2.setType("kala2");
//            item2.setRegion("helsinki2");
//
//            item2.setLon(6654528);
//            item2.setLat(345344);
//
//            result.addItem(item2);
//            System.out.println("Koira");
//            System.out.println("Response data:");
//            System.out.println(responseData);
        } catch (IOException ex) {
            throw new RuntimeException("Error searching", ex);
        }
        return result;
    }
}