package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            List<IndiaCensusCSV> censusCSVList = csvBuilder.getCSVFileList(reader,IndiaCensusCSV.class);
            return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                                              CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }

    }

    public <E> void loadIndiaCensusData(String indiaCensusCsvFilePath, Class<E> indiaCensusCSVClass) throws CensusAnalyserException {
        if(indiaCensusCSVClass.equals(IndiaCensusCSV.class)){
           loadIndiaCensusData(indiaCensusCsvFilePath);
        }else{
            throw new CensusAnalyserException("Wrong Type of Object",CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
        }
    }

    public <E> void loadIndiaCensusData(String indiaCensusCsvFilePath, Class<E> indiaCensusCSVClass, char c) throws CensusAnalyserException {
        if(c==','){
            loadIndiaCensusData(indiaCensusCsvFilePath,indiaCensusCSVClass);
        }else{
            throw new CensusAnalyserException("Wrong Type of Delimiter",CensusAnalyserException.ExceptionType.CENSUS_DELIMITER_PROBLEM);
        }
    }
    public int loadIndiaStateCodeData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            List<CSVStates> censusCSVList = csvBuilder.getCSVFileList(reader,CSVStates.class);
            return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (Exception e){
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_HEADER_PROBLEM);
        }
    }

    public <E> void loadIndiaStateCodeData(String indiaStateCodeCsvFilePath, Class<E> censusAnalyserExceptionClass) throws CensusAnalyserException {
        if(censusAnalyserExceptionClass.equals(CSVStates.class)){
            loadIndiaStateCodeData(indiaStateCodeCsvFilePath);
        }else{
            throw new CensusAnalyserException("Wrong Type of Object",CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
        }
    }

    public void loadIndiaStateCodeData(String indiaCensusCsvFilePath, Class<IndiaCensusCSV> indiaCensusCSVClass, char c) throws CensusAnalyserException {
        if(c==','){
            loadIndiaStateCodeData(indiaCensusCsvFilePath,indiaCensusCSVClass);
        }else{
            throw new CensusAnalyserException("Wrong Type of Object",CensusAnalyserException.ExceptionType.CENSUS_DELIMITER_PROBLEM);
        }
    }

    public <E> int getCount(Iterator<E> censusCSVIterator){
        Iterable<E> csvIterable=() ->censusCSVIterator;
        return (int) StreamSupport.stream(csvIterable.spliterator(),false).count();
    }

    public String getStateWiseSortedCensusData(String indiaStateCodeCsvFilePath) {
        return null;
    }
}
