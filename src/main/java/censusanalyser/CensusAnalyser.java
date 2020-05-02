package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser<E>{
    public int loadIndiaCensusData(String csvFilePath,Class<E> indiaCensusCSVClass,char c) throws CensusAnalyserException {
        try {
            checkType((Class<E>) IndiaCensusCSV.class,indiaCensusCSVClass);
            checkSeparator(c);
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

    public int loadIndiaCensusData(String indiaCensusCsvFilePath) {
        return loadIndiaCensusData(indiaCensusCsvFilePath,(Class<E>) IndiaCensusCSV.class,',');
    }


    public int loadIndiaCensusData(String indiaCensusCsvFilePath, Class<E> rightType) {
        try{
            return loadIndiaCensusData(indiaCensusCsvFilePath, rightType,',');
        }catch (Exception e){
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
        }
    }

    public int loadIndiaStateCodeData(String csvFilePath,Class<E> indiaCensusCSVClass,char c) throws CensusAnalyserException {
        try {
            checkType((Class<E>) CSVStates.class,indiaCensusCSVClass);
            checkSeparator(c);
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            List<CSVStates> censusCSVList = csvBuilder.getCSVFileList(reader,CSVStates.class);
            return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }

    public int loadIndiaStateCodeData(String indiaStateCodeCsvFilePath) {
        return loadIndiaStateCodeData(indiaStateCodeCsvFilePath,(Class<E>) CSVStates.class,',');
    }

    public int loadIndiaStateCodeData(String indiaStateCodeCsvFilePath, Class<E> rightType) {
        try{
            return loadIndiaStateCodeData(indiaStateCodeCsvFilePath, rightType,',');
        } catch (Exception e){
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
        }
    }

    public void checkType(Class<E> expected,Class<E> indiaCensusCSVClass) throws CensusAnalyserException {
        if(!indiaCensusCSVClass.equals(expected)){
            throw new CensusAnalyserException("Wrong Type of Object",CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
        }
    }

    public <E> void checkSeparator(char c) throws CensusAnalyserException {
        if(c!=','){
            throw new CensusAnalyserException("Wrong Type of Delimiter",CensusAnalyserException.ExceptionType.CENSUS_DELIMITER_PROBLEM);
        }
    }

    public <E> int getCount(Iterator<E> censusCSVIterator){
        Iterable<E> csvIterable=() ->censusCSVIterator;
        return (int) StreamSupport.stream(csvIterable.spliterator(),false).count();
    }


    public String getStateWiseSortedCensusData(String csvFilePath) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            List<IndiaCensusCSV> censusCSVList = csvBuilder.getCSVFileList(reader,IndiaCensusCSV.class);
            Comparator<IndiaCensusCSV> comparing = Comparator.comparing(census -> census.state);
            this.sort(censusCSVList,comparing);
            String json = new Gson().toJson(censusCSVList);
            return json;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }

    }

    private void sort(List<IndiaCensusCSV> censusCSVList,Comparator<IndiaCensusCSV> comparing) {
        for(int i=0;i< censusCSVList.size()-1;i++){
            for(int j=0; j < censusCSVList.size()-i-1 ;j++){
                IndiaCensusCSV census1= censusCSVList.get(j);
                IndiaCensusCSV census2 = censusCSVList.get(j+1);
                if(comparing.compare(census1,census2)>0){
                    censusCSVList.set(j,census2);
                    censusCSVList.set(j+1,census1);
                }
            }
        }
    }
}
