package censusanalyser;

import censusanalyser.CSVClasses.CSVStates;
import censusanalyser.CSVClasses.CensusDAO;
import censusanalyser.CSVClasses.IndiaCensusCSV;
import censusanalyser.CSVClasses.USCensusCSV;
import censusanalyser.exception.CSVBuilderException;
import censusanalyser.exception.CensusAnalyserException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CensusLoader {
    Map<String, CensusDAO> censusCSVMap=null;
    List<CensusDAO> censusCSVList=null;

    public CensusLoader() {
        censusCSVMap=new HashMap<String, CensusDAO>();
        censusCSVList=new ArrayList<CensusDAO>();
    }

    public <E> Map<String, CensusDAO> loadCensusData(Class<E> censusCSVClass, char c, String... csvFilePath) {
        try {
            String name = censusCSVClass.getName();
            if(name.equals("censusanalyser.CSVClasses.IndiaCensusCSV")) {
                checkType(IndiaCensusCSV.class, (Class<E>) censusCSVClass);
            }else if(name.equals("censusanalyser.CSVClasses.USCensusCSV")) {
                checkType(USCensusCSV.class, (Class<E>) censusCSVClass);
            }else if(name.equals("censusanalyser.CSVClasses.CSVStates")) {
                checkType(CSVStates.class, (Class<E>) censusCSVClass);
            }else{
                throw new CensusAnalyserException("Wrong Type of Object",CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
            }
            checkSeparator(c);
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> csvStatesIterable =() ->csvFileIterator;
            Stream<E> stream = StreamSupport.stream(csvStatesIterable.spliterator(), false);
            if(name.equals("censusanalyser.CSVClasses.IndiaCensusCSV")){
                Stream<IndiaCensusCSV> streamIndia = stream.map(IndiaCensusCSV.class::cast);
                streamIndia.forEach(censusCSV -> censusCSVMap.put(censusCSV.state,new CensusDAO(censusCSV)));
            }else if(name.equals("censusanalyser.CSVClasses.USCensusCSV")){
                Stream<USCensusCSV> streamIndia = stream.map(USCensusCSV.class::cast);
                streamIndia.forEach(censusCSV -> censusCSVMap.put(censusCSV.state,new CensusDAO(censusCSV)));
            }
            if(csvFilePath.length==1){
                return censusCSVMap;
            }
            reader = Files.newBufferedReader(Paths.get(csvFilePath[1]));
            csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<CSVStates> csvFileIteratorState = csvBuilder.getCSVFileIterator(reader, CSVStates.class);
            Iterable<CSVStates> csvStatesIterableState = () -> csvFileIteratorState;
            Stream<CSVStates> streamState = StreamSupport.stream(csvStatesIterableState.spliterator(), false);
            streamState.filter(csvState -> censusCSVMap.get(csvState.state) !=null).forEach(censusCSV -> censusCSV.stateCode=censusCSVMap.get(censusCSV.state).stateId);
            return censusCSVMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }

    public void checkType(Class expected,Class indiaCensusCSVClass) throws CensusAnalyserException {
        if(!indiaCensusCSVClass.equals(expected)){
            throw new CensusAnalyserException("Wrong Type of Object",CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
        }
    }

    public void checkSeparator(char c) throws CensusAnalyserException {
        if(c!=','){
            throw new CensusAnalyserException("Wrong Type of Delimiter",CensusAnalyserException.ExceptionType.CENSUS_DELIMITER_PROBLEM);
        }
    }
}
