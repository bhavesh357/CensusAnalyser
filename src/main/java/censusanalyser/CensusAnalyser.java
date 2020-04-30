package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            Iterator<IndiaCensusCSV> censusCSVIterator = getCSVFileIterator(reader,IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvIterable=() ->censusCSVIterator;
            int namOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(),false).count();
            return namOfEateries;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                                              CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch(Exception e){
            throw  new CensusAnalyserException(e.getMessage(),
                                              CensusAnalyserException.ExceptionType.CENSUS_HEADER_PROBLEM);
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
            Iterator<CSVStates> censusCSVIterator = getCSVFileIterator(reader,CSVStates.class);
            Iterable<CSVStates> csvIterable=() ->censusCSVIterator;
            int namOfStates = (int) StreamSupport.stream(csvIterable.spliterator(),false).count();
            return namOfStates;
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

    private <E> Iterator<E> getCSVFileIterator(Reader reader,Class<E> csvClass){
        CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        CsvToBean<E> csvToBean = csvToBeanBuilder.build();
        return (Iterator<E>) csvToBean.iterator();
    }
}
