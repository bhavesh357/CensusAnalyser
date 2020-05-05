package censusanalyser;

import censusanalyser.CSVClasses.CSVStates;
import censusanalyser.CSVClasses.CSVStatesDAO;
import censusanalyser.CSVClasses.IndiaCensusCSV;
import censusanalyser.CSVClasses.IndiaCensusCSVDAO;
import censusanalyser.exception.CSVBuilderException;
import censusanalyser.exception.CensusAnalyserException;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CensusAnalyser<E>{
    Map<String, IndiaCensusCSVDAO> censusCSVMap=null;
    List<IndiaCensusCSVDAO> censusCSVList=null;
    Map<String, CSVStatesDAO> censusStateMap=null;
    List<CSVStatesDAO> censusList=null;

    public CensusAnalyser() {
        censusList=new ArrayList<CSVStatesDAO>();
        censusStateMap = new HashMap<String, CSVStatesDAO>();

        censusCSVMap=new HashMap<String, IndiaCensusCSVDAO>();
        censusCSVList=new ArrayList<IndiaCensusCSVDAO>();
    }

    public int[] loadData(String filePathForCensus, Class<E> type1, char c1, String filePathForState, Class<E> type2, char c2){
        int[] sizes = new int[2];
        sizes[0] = loadIndiaCensusData(filePathForCensus,type1,c1);
        sizes[1] = loadIndiaStateCodeData(filePathForState,type2,c2);
        return sizes;
    }
    public int loadIndiaCensusData(String csvFilePath,Class<E> indiaCensusCSVClass,char c) throws CensusAnalyserException {
        try {
            checkType((Class<E>) IndiaCensusCSV.class,indiaCensusCSVClass);
            checkSeparator(c);
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvStatesIterable =() ->csvFileIterator;
            Stream<IndiaCensusCSV> stream = StreamSupport.stream(csvStatesIterable.spliterator(), false);
            stream.forEach(censusCSV -> censusCSVMap.put(censusCSV.state,new IndiaCensusCSVDAO(censusCSV)));
            censusCSVList.addAll(censusCSVMap.values());
            return censusCSVMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                                              CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }

    }

    public int loadIndiaStateCodeData(String csvFilePath,Class<E> indiaCensusCSVClass,char c) throws CensusAnalyserException {
        try {
            checkType((Class<E>) CSVStates.class,indiaCensusCSVClass);
            checkSeparator(c);
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            /*
            censusStateMap = csvBuilder.getCSVFileMapState(reader,CSVStates.class);
            censusList.addAll(censusStateMap.values());
             */
            Iterator<CSVStates> csvFileIterator = csvBuilder.getCSVFileIterator(reader, CSVStates.class);
            Iterable<CSVStates> csvStatesIterable =() ->csvFileIterator;
            Stream<CSVStates> stream = StreamSupport.stream(csvStatesIterable.spliterator(), false);
            stream.forEach(censusCSV -> censusStateMap.put(censusCSV.state,new CSVStatesDAO(censusCSV)));
            censusList.addAll(censusStateMap.values());
            return censusList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }


    public void checkType(Class<E> expected,Class<E> indiaCensusCSVClass) throws CensusAnalyserException {
        if(!indiaCensusCSVClass.equals(expected)){
            throw new CensusAnalyserException("Wrong Type of Object",CensusAnalyserException.ExceptionType.CENSUS_TYPE_PROBLEM);
        }
    }

    public void checkSeparator(char c) throws CensusAnalyserException {
        if(c!=','){
            throw new CensusAnalyserException("Wrong Type of Delimiter",CensusAnalyserException.ExceptionType.CENSUS_DELIMITER_PROBLEM);
        }
    }

    public String getStateWiseSortedCensusData() {
        checkIfNull(censusCSVList);
        Comparator<IndiaCensusCSVDAO> comparing = Comparator.comparing(census -> census.state);
        this.sort(comparing,censusCSVList,true);
        return getJson(censusCSVList);
    }
    public String getPopulationWiseSortedCensusData() {
        checkIfNull(censusCSVList);
        Comparator<IndiaCensusCSVDAO> comparing = Comparator.comparing(census -> census.population);
        this.sort(comparing,censusCSVList,false);
        return getJson(censusCSVList);
    }

    public String getPopulationDensityWiseSortedCensusData() {
        checkIfNull(censusCSVList);
        Comparator<IndiaCensusCSVDAO> comparing = Comparator.comparing(census -> census.density);
        this.sort(comparing,censusCSVList,false);
        return getJson(censusCSVList);
    }

    public String getAreaWiseSortedCensusData() {
        checkIfNull(censusCSVList);
        Comparator<IndiaCensusCSVDAO> comparing = Comparator.comparing(census -> census.area);
        this.sort(comparing,censusCSVList,false);
        return getJson(censusCSVList);
    }

    public String getStateCodeWiseSortedCensusData() {
        checkIfNull(censusList);
        Comparator<CSVStatesDAO> comparing = Comparator.comparing(census -> census.stateCode);
        this.sort(comparing,censusList,true);
        return getJson(censusList);
    }

    private void checkIfNull(List list){
        if(list == null || list.size()==0){
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
    }

    private String getJson(List list){
        String json = new Gson().toJson(list);
        return json;
    }

    private void sort(Comparator comparing,List list,boolean isAscending) {
        for(int i=0;i< list.size()-1;i++){
            for(int j=0; j < list.size()-i-1 ;j++){
                Object census1= list.get(j);
                Object  census2 = list.get(j+1);
                if(isAscending==true){
                    if(comparing.compare(census1,census2)>0){
                        list.set(j,census2);
                        list.set(j+1,census1);
                    }
                }else {
                    if (comparing.compare(census1, census2) < 0) {
                        list.set(j, census2);
                        list.set(j + 1, census1);
                    }
                }
            }
        }
    }

    public int[] loadData(String filePath1, Class<E> type1, String filePath2, Class<E> type2) {
        return loadData(filePath1,type1,',',filePath2,type2,',');
    }

    public int[] loadData(String filePath1, String filePath2) {
        return loadData(filePath1,(Class<E>) IndiaCensusCSV.class,',',filePath2,(Class<E>) CSVStates.class,',');
    }
}
