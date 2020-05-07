package censusanalyser;

import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.model.CSVStates;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.IndiaCensusCSV;
import censusanalyser.model.USCensusCSV;

import java.util.Map;

public class CensusAdapterFactory {
    public static Map<String, CensusDAO> getCensusData(CensusAdapter.TYPE type,Class censusCSVClass, char c, String... csvFilePath) {
        if(type.equals(CensusAdapter.TYPE.INDIA)) {
            CensusAdapter.checkClass(censusCSVClass,IndiaCensusCSV.class);
            return new IndiaCensusAdapter().loadCensusData(type, c, csvFilePath);
        }else if(type.equals(CensusAdapter.TYPE.US)){
            CensusAdapter.checkClass(censusCSVClass, USCensusCSV.class);
            return new USCensusAdapter().loadCensusData(type,c,csvFilePath);
        }else if(type.equals(CensusAdapter.TYPE.INDIA_STATE)){
            CensusAdapter.checkClass(censusCSVClass,CSVStates.class);
            return new IndiaStateCensusAdapter().loadCensusData(type,c,csvFilePath);
        }else{
            throw new CensusAnalyserException("Wrong Type of Object",CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
        }
    }
}
