package censusanalyser;

import censusanalyser.csvBuilder.CSVBuilderFactory;
import censusanalyser.csvBuilder.ICSVBuilder;
import censusanalyser.model.CSVStates;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.IndiaCensusCSV;
import censusanalyser.model.USCensusCSV;
import censusanalyser.exception.CSVBuilderException;
import censusanalyser.exception.CensusAnalyserException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IndiaCensusAdapter extends CensusAdapter {
    public <E> Map<String, CensusDAO> loadCensusData(TYPE type, char c, String... csvFilePath) {
        Map<String, CensusDAO> censusCSVMap = super.loadCensusData(IndiaCensusCSV.class,type, c, csvFilePath[0]);
        this.loadStateCensusData(censusCSVMap,csvFilePath[1]);
        return censusCSVMap;
    }

    private void loadStateCensusData(Map<String, CensusDAO> censusCSVMap, String csvFilePath) {
        try{
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<CSVStates> csvFileIteratorState = csvBuilder.getCSVFileIterator(reader, CSVStates.class);
            Iterable<CSVStates> csvStatesIterableState = () -> csvFileIteratorState;
            Stream<CSVStates> streamState = StreamSupport.stream(csvStatesIterableState.spliterator(), false);
            streamState.filter(csvState -> censusCSVMap.get(csvState.state) !=null).forEach(censusCSV -> censusCSV.stateCode=censusCSVMap.get(censusCSV.state).stateId);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }
}
