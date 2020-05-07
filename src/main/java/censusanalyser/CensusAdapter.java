package censusanalyser;
import censusanalyser.csvBuilder.CSVBuilderFactory;
import censusanalyser.csvBuilder.ICSVBuilder;
import censusanalyser.exception.CSVBuilderException;
import censusanalyser.exception.CensusAnalyserException;
import censusanalyser.model.CSVStates;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.IndiaCensusCSV;
import censusanalyser.model.USCensusCSV;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class CensusAdapter {


    public enum TYPE {
        INDIA,US,INDIA_STATE;
    }

    public <E> Map<String, CensusDAO> loadCensusData(Class censusCSVClass,TYPE type, char c, String csvFilePath) {
        try {
            checkSeparator(c);
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> csvStatesIterable =() ->csvFileIterator;
            Stream<E> stream = StreamSupport.stream(csvStatesIterable.spliterator(), false);
            Map<String, CensusDAO> censusCSVMap = new HashMap<String, CensusDAO>();
            if (type.equals(TYPE.INDIA)) {
                Stream<IndiaCensusCSV> streamIndia = stream.map(IndiaCensusCSV.class::cast);
                streamIndia.forEach(censusCSV -> censusCSVMap.put(censusCSV.state,new CensusDAO(censusCSV)));
            }else if(type.equals(TYPE.US)){
                Stream<USCensusCSV> streamIndia = stream.map(USCensusCSV.class::cast);
                streamIndia.forEach(censusCSV -> censusCSVMap.put(censusCSV.state,new CensusDAO(censusCSV)));
            }else {
                Stream<CSVStates> streamIndia = stream.map(CSVStates.class::cast);
                streamIndia.forEach(censusCSV -> censusCSVMap.put(censusCSV.state,new CensusDAO(censusCSV)));
            }
            return censusCSVMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }


    public void checkSeparator(char c) throws CensusAnalyserException {
        if(c!=','){
            throw new CensusAnalyserException("Wrong Type of Delimiter",CensusAnalyserException.ExceptionType.CENSUS_DELIMITER_PROBLEM);
        }
    }
    public static void checkClass(Class censusCSVClass, Class usCensusCSVClass) {
        if(!censusCSVClass.equals(usCensusCSVClass)){
            throw new CensusAnalyserException("Wrong Type of Delimiter",CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
        }
    }

}