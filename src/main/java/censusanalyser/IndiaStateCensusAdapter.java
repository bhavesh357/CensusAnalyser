package censusanalyser;

import censusanalyser.model.CSVStates;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.IndiaCensusCSV;

import java.util.Map;

public class IndiaStateCensusAdapter extends CensusAdapter{
    public <E> Map<String, CensusDAO> loadCensusData(TYPE type, char c, String... csvFilePath) {
        Map<String, CensusDAO> censusCSVMap = super.loadCensusData(CSVStates.class,type, c, csvFilePath[0]);
        return censusCSVMap;
    }
}
