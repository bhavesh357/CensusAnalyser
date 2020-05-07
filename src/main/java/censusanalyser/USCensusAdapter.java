package censusanalyser;

import censusanalyser.model.CensusDAO;
import censusanalyser.model.USCensusCSV;

import java.util.Map;

public class USCensusAdapter extends CensusAdapter{
    public <E> Map<String, CensusDAO> loadCensusData(TYPE type, char c, String... csvFilePath) {
        Map<String, CensusDAO> censusCSVMap = super.loadCensusData(USCensusCSV.class,type, c, csvFilePath[0]);
        return censusCSVMap;
    }
}
